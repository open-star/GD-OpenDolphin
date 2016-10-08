/*
 * Open Dolphin Main Class.
 */
package open.dolphin.client;

import open.dolphin.client.editor.stamp.StampBoxFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;

import open.dolphin.client.editor.template.TemplateDialog;
import open.dolphin.client.patientsearch.PatientSearchView;
import open.dolphin.client.settings.ProjectSettingDialog;
import open.dolphin.client.waitinglist.WaitingListView;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.helper.ComponentMemory;
import open.dolphin.helper.MainMenuSupport;
import open.dolphin.helper.WindowSupport;

import open.dolphin.log.LogWriter;
import open.dolphin.project.*;
import open.dolphin.server.IPVTServer;
import open.dolphin.pvtclientserver.PVTClientServer;
import open.dolphin.sendclaim.SendClaimImpl;
import open.dolphin.sendmml.SendMmlImpl;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

import org.jdesktop.application.ResourceMap;

import open.dolphin.security.CertificateManager;
import open.dolphin.utils.UserDirectoryInitializer;
import open.dolphin.helper.IMainCommandAccepter;
import java.awt.Desktop;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import open.dolphin.client.labotestimporter.LaboTestImporter;
//import open.dolphin.dolphinpeer.DolphinPeerClient;
//import open.dolphin.dolphinpeer.DolphinPeerServer;
import open.dolphin.helper.PlugInMenuSupport;
import open.dolphin.helper.SchemaSupport;
import open.dolphin.helper.SynchronizedTask;
import open.dolphin.infomodel.UserModel;
import open.dolphin.plugin.PluginWrapper;
import open.dolphin.plugin.allergypanel.AllergyPanelPlugin;
import open.dolphin.plugin.labotestdocumentpanel.LaboTestDocumentPanelPlugin;
import open.dolphin.plugin.memopanel.MemoPanelPlugin;
import open.dolphin.plugin.patientinfodocumentpanel.PatientInfoDocumentPanelPlugin;
import open.dolphin.plugin.patientvisitinspector.PatientVisitInspectorPlugin;
import open.dolphin.plugin.physicalpanel.PhysicalPanelPlugin;
import open.socket.data.Command;
import open.socket.data.RequestObject;

/**
 * アプリケーションのメインウインドウクラス。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class Dolphin extends Application implements IMainWindow, IMainCommandAccepter {

    /**
     * @return the stampBox
     */
    public static StampBoxFrame getStampBox() {
        return stampBox;
    }
    private WindowSupport windowSupport;    // Window と Menu サポート
    private MenuMediator menuMediator;    // MenuMediator
    private StateManager stateManager;    // 状態制御
    final private HashMap<String, IMainService> providers = new HashMap<String, IMainService>();
    private JTabbedPane tabbedPane;    // pluginを格納する tabbedPane
    private PageFormat pageFormat;  // プリンターセットアップはMainWindowのみで行い、設定された PageFormat各プラグインが使用する
    private Properties saveEnv;    // 環境設定用の Properties
    private BlockGlass blockGlass;    // BlockGlass
    private SchemaBox imageBox;
    private TemplateDialog templateEditor;
    private IPVTServer pvtServer;    // 受付受信サーバ
    //  private DolphinPeerServer peerServer;
    //   private DolphinPeerClient peerClient;
    private SendClaimImpl sendClaim;    // CLAIM リスナ
    private SendMmlImpl sendMml;    // MML リスナ
    private ResourceMap resource;    // ResourceMap
    private JPanel content;    //詳細パネル
    private static StampBoxFrame stampBox;    // StampBox
    private static JFrame mainWindow;    //フレーム
    private static List<IChart> allEditorFrames;
    private static List<IChart> allCharts;  //  IChart インスタンスを管理するstatic 変数
    private PlugInMenuSupport plugins; //名称とプラグインのペアを格納する連想配列

    /**
     *
     * Creates new IMainWindow
     */
    public Dolphin() {
    }

    /**
     * 多重起動の回避
     */
    public void duplicateGuard() {
        try {
            //起動チェック
            //      final FileOutputStream fos = new FileOutputStream(new File(getContext().getLocalStorage().getDirectory(), "lock"));
            final File lockFile = new File(getContext().getLocalStorage().getDirectory(), "lock");
            final FileOutputStream fos = new FileOutputStream(lockFile);
            final FileChannel fc = fos.getChannel();
            final FileLock lock = fc.tryLock();

            if (lock == null) {
                JOptionPane.showMessageDialog(null, "既に OpenDolphin は起動しています.", "エラー", JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                //ロック開放処理を登録
                Runtime.getRuntime().addShutdownHook(new Thread() {

                    @Override
                    public void run() {
                        try {
                            try {
                                if (lock != null && lock.isValid()) {
                                    lock.release();
                                }
                            } finally {
                                fc.close();
                                fos.close();
                                lockFile.delete();
                            }
                        } catch (IOException ex) {
                            LogWriter.fatal(getClass(), "ロックの解除に失敗しました.");
                        }
                    }
                });
            }
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * 各種プラグインとその名称を連想配列に格納します。<br>
     * "installed_plugins"というディレクトリが無い場合、プラグインはロードされません。
     * @throws IllegalAccessException 
     * @throws InstantiationException インスタンスを生成できない場合
     * @throws MalformedURLException
     * @throws ClassNotFoundException クラスが見つからなかった場合
     */
    private void initPlugins() throws IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException {
        plugins = new PlugInMenuSupport(); //名称とプラグインのペアを格納する連想配列

        //標準のプラグインのロード
        plugins.loadPlugin(new AllergyPanelPlugin());
        plugins.loadPlugin(new LaboTestDocumentPanelPlugin());//container
        plugins.loadPlugin(new MemoPanelPlugin());
        plugins.loadPlugin(new PatientInfoDocumentPanelPlugin());//container
        plugins.loadPlugin(new PatientVisitInspectorPlugin());
        plugins.loadPlugin(new PhysicalPanelPlugin());

        //追加のプラグインのロード
            if (plugins.installPlugins(GlobalConstants.getPluginsTempDirectory(), GlobalConstants.getPluginsDirectory())) {
            plugins.loadPlugins(GlobalConstants.getPluginsDirectory(), "open.dolphin.plugin.");
        }
    }

    /**
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException インスタンスを生成できない場合
     * @throws MalformedURLException
     * @throws ClassNotFoundException クラスが見つからなかった場合
     */
    private void initSchemas() throws IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException {
        SchemaSupport schemas = new SchemaSupport();
        schemas.installSchemas(GlobalConstants.getSchemasTempDirectory(), GlobalConstants.getSchemasDirectory());
    }

    /**
     * メイン
     * @param args
     */
    @Override
    protected void initialize(String[] args) {
        duplicateGuard(); //多重起動の回避

        ApplicationContext context = getContext();
        UserDirectoryInitializer.run(context);

        StringBuilder logFile = new StringBuilder();
        logFile.append(context.getLocalStorage().getDirectory()).append(File.separator).append("log").append(File.separator).append("dolphin.log");
        LogWriter.config(logFile.toString(), "INFO");

        resource = context.getResourceManager().getResourceMap(Dolphin.class);
        GlobalConstants.createGlobalConstants(context);
        GlobalVariables.createGlobalVariables();

        CertificateManager.createCertificateFile(context);
        allEditorFrames = new ArrayList<IChart>(3);

        allCharts = new ArrayList<IChart>(3);  //  IChart インスタンスを管理するstatic 変数


        //    final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
        //     dao.setTransactionISolationLevel("SERIALIZABLE");
    }

    /**
     * 開始
     */
    @Override
    protected void startup() {
        try {
            initPlugins(); //各種プラグインとその名前を連想配列に関連付けます。
            initSchemas();
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        // ExitListner を登録する
        this.addExitListener(new ExitAdapter());
        // ログインダイアログを表示し認証を行う
        PropertyChangeListener pl = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                LoginDialog.LoginStatus result = (LoginDialog.LoginStatus) e.getNewValue();
                switch (result) {
                    case AUTHENTICATED: //認証された
                        startServices();
                        initCustomComponents();
                        break;
                    case NOT_AUTHENTICATED:
                        exit();
                        break;
                    case CANCELD:
                        exit();
                        break;
                    default:
                        LogWriter.fatal(getClass(), "case default");
                }
            }
        };
        //    LoginDialog login = new LoginDialog(null, getContext(), true);
        LoginDialog login = new LoginDialog(null, true, plugins);
        login.addPropertyChangeListener("LOGIN_PROP", pl);
        login.start();
    }

    /**
     * 
     */
    @Override
    protected void shutdown() {

        if (providers != null) {
            try {
                HashMap cloneMap = null;
                synchronized (providers) {
                    cloneMap = (HashMap) providers.clone();
                }
                Iterator iter = cloneMap.values().iterator();
                while (iter != null && iter.hasNext()) {
                    IMainService pl = (IMainService) iter.next();
                    pl.stop();
                }
            } catch (Exception e) {
                LogWriter.error(getClass(), e);
            }
        }
        if (windowSupport != null) {
            mainWindow = windowSupport.getFrame();
            mainWindow.setVisible(false);
            mainWindow.dispose();
        }
        // save dolphin dir

        if (!GlobalSettings.isTrial()) {
            String dir = "";
            Preferences pref = Preferences.userRoot().node("open/dolphin/project");
            File file = new File("");
            dir = file.getAbsolutePath();
            pref.put("whereisDolphin", dir);
        }


    }

    /**
     *
     * @return
     */
    public boolean close() {
        exit();
        return true;
    }

    /**
     * 
     * @param command
     * @return
     */
    @Override
    public boolean dispatchMainCommand(IMainCommandAccepter.MainCommand command) {
        switch (command) {
            case printerSetup:
                return printerSetup();
            case setKarteEnviroment:
                return setKarteEnviroment();
            case showStampBox:
                return showStampBox();
            case showSchemaBox:
                return showSchemaBox();
            case showTemplateEditor:
                return showTemplateEditor();
            case changePassword:
                return changePassword();
            case addUser:
                return addUser();
            case close:
                return close();
            case browseDolphinSupport:
                return browseDolphinSupport();
            case browseDolphinProject:
                return browseDolphinProject();
            case browseMedXml:
                return browseMedXml();
            case showAbout:
                return showAbout();
            default:
        }
        return false;
    }

    /**
     * 
     * @param pvt
     * @return
     */
    private boolean openPatient(PatientVisitModel pvt) {
        boolean isReadOnry = false;
        for (open.dolphin.plugin.IPlugin plugin : plugins.values()) {
            PluginWrapper pluginWrapper = new PluginWrapper(plugin);
            RequestObject request = new RequestObject();
            UserModel loginUser = GlobalVariables.getUserModel();

            request.setUserId(loginUser.getId());
            request.setUserName(loginUser.getSirName());
            request.setPatientId(pvt.getPatient().getId());
            request.setPatientName(pvt.getPatient().getFullName());
            request.setCommand(Command.KARTE_OPEN);

            Object result = pluginWrapper.message(request);
            if (result != null) {
                if (pluginWrapper.IsDispatched()) {
                    pluginWrapper.update(result);
                    //          isReadOnry = (result.getData().equals("locked"));
                    break;
                }
            }
        }
        return isReadOnry;
    }

    /**
     * 
     */
    class ExitAdapter implements ExitListener {

        /**
         *
         * @param e
         * @return
         */
        @Override
        public boolean canExit(EventObject e) {
            return canProcessExit();
        }

        /**
         *
         * @param event
         */
        @Override
        public void willExit(EventObject event) {
            processExit();
        }
    }

    /**
     * 起動時のバックグラウンドで実行されるべきタスクを行う。
     */
    private void startServices() {
        // 環境設定ダイアログで変更される場合があるので保存する
        saveEnv = new Properties();

        //    startPeerServer();
        // PVT Sever を起動する
        if (GlobalVariables.getUseAsPVTServer()) {
            startPvtServer();
        } else {
            saveEnv.put(GUIConst.KEY_PVT_SERVER, GUIConst.SERVICE_NOT_RUNNING);
        }
        // CLAIM送信を生成する
        if (GlobalVariables.getSendClaim()) {
            startSendClaim();
            //        startPeerClient();

        } else {
            saveEnv.put(GUIConst.KEY_SEND_CLAIM, GUIConst.SERVICE_NOT_RUNNING);
        }
        if (GlobalVariables.getClaimAddress() != null) {
            saveEnv.put(GUIConst.ADDRESS_CLAIM, GlobalVariables.getClaimAddress());
        }
        // MML送信を生成する
        if (GlobalVariables.getSendMML()) {
            startSendMml();
        } else {
            saveEnv.put(GUIConst.KEY_SEND_MML, GUIConst.SERVICE_NOT_RUNNING);
        }
        if (GlobalVariables.getCSGWPath() != null) {
            saveEnv.put(GUIConst.CSGW_PATH, GlobalVariables.getCSGWPath());
        }
    }

    /**
     * 
     * @return
     */
    private String getWindowTitle() {
        return GlobalConstants.getFrameTitle(resource.getString("title")) + " | " + GlobalVariables.getUserId() + " | " + GlobalVariables.getUserModel().getLicenseModel().getLicenseDesc();
    }

    /**
     * コンポーネントの初期化
     */
    private void initCustomComponents() {

        // 設定に必要な定数をコンテキストから取得する
        // String windowTitle =  ;
        Rectangle setBounds = new Rectangle(0, 0, 1000, 690);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int defaultX = (screenSize.width - setBounds.width) / 2;
        int defaultY = (screenSize.height - setBounds.height) / 2;
        int defaultWidth = 666;
        int defaultHeight = 678;
        // WindowSupport を生成する この時点で Frame,WindowMenu を持つMenuBar が生成されている

        JFrame frame = new MainWindow();
        windowSupport = WindowSupport.create(frame, getWindowTitle());
        mainWindow = windowSupport.getFrame();		// IMainWindow の JFrame

        //<TODO>アイコンを適切な物に変更
        ImageIcon icon = new ImageIcon(getClass().getResource("/open/dolphin/resources/images/web_32.gif"));
        mainWindow.setIconImage(icon.getImage());

        JMenuBar myMenuBar = windowSupport.getMenuBar();	// IMainWindow の JMenuBar
        // Windowにこのクラス固有の設定をする
        Point loc = new Point(defaultX, defaultY);
        Dimension size = new Dimension(defaultWidth, defaultHeight);
        mainWindow.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });

        ComponentMemory cm = new ComponentMemory(mainWindow, loc, size, this);
        cm.setToPreferenceBounds();

        // BlockGlass を設定する
        blockGlass = new BlockGlass();
        mainWindow.setGlassPane(blockGlass);

        // mainWindowのメニューを生成しメニューバーに追加する
        menuMediator = new MenuMediator(this);
        //      IMenuAdapter appMenu = MenuFactory.create();
        IMenuAdapter appMenu = new WindowsMenuAdapter();
        appMenu.setMenuSupports(null, menuMediator, null, plugins);
        appMenu.build(myMenuBar, false);
        menuMediator.registerActions(appMenu.getActionMap());

        // mainWindowのコンテントGUIを生成しFrameに追加する
        tabbedPane = new JTabbedPane();
        content = new JPanel(new BorderLayout());
        content.setOpaque(true);
        content.add(tabbedPane, BorderLayout.CENTER);
        content.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        mainWindow.setContentPane(content);

        List<IMainComponent> mainComponentList = new ArrayList<IMainComponent>(3);
        mainComponentList.add(0, new WaitingListView());    //「受付リスト」パネル
        mainComponentList.add(1, new PatientSearchView());  //「患者検索」パネル
        mainComponentList.add(2, new LaboTestImporter());   //「ラボレシーバ」パネル

        int index = 0;
        for (IMainComponent plugin : mainComponentList) {
            plugin.setMenu(menuMediator);
            plugin.setContext(this);
            plugin.start();
            tabbedPane.addTab(plugin.getName(), (Component) plugin);
            providers.put(String.valueOf(index), plugin);
            index += 1;
        }
        mainComponentList.clear();

        // タブの切り替えで plugin.enter() をコールする
        tabbedPane.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int index = tabbedPane.getSelectedIndex();
                IMainComponent plugin = (IMainComponent) providers.get(String.valueOf(index));
                if (plugin.getContext() == null) {
                    plugin.setContext(Dolphin.this);
                    plugin.start();
                    tabbedPane.setComponentAt(index, (Component) plugin);
                } else {
                    plugin.enter();
                }
                menuMediator.setLast(plugin);
            }
        });

        enabledAction(GUIConst.ACTION_SHOW_TEMPLATE_EDITOR, true);

        // StaeMagrを使用してメインウインドウの状態を制御する
        stateManager = new StateManager();
        stateManager.processLogin(true);

        stampBox = new StampBoxFrame();
        getStampBox().setContext(Dolphin.this);

        try {
            if (getStampBox().getStartingTask().call()) {
                getStampBox().start();
                getStampBox().getFrame().setVisible(true);
                providers.put("stampBox", getStampBox());
                windowSupport.getFrame().setVisible(true);
                enabledAction(GUIConst.ACTION_SHOW_TEMPLATE_EDITOR, true);
            }
        } catch (Exception ex) {
            LogWriter.fatal(getClass(), "スタンプの読み込みに失敗しました." + LogWriter.stackTrace(ex));
            JOptionPane.showMessageDialog(getFrame(), "スタンプの読み込みに失敗しました.", "エラー", JOptionPane.OK_OPTION);
            System.exit(1);
        }
    }

    /**
     * 
     * @return
     */
    @Override
    public BlockGlass getGlassPane() {
        return blockGlass;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public IMainService getPlugin(String id) {
        return providers.get(id);
    }

    /**
     * 
     * @return 名称とプラグインのペアを格納する連想配列
     */
    @Override
    public PlugInMenuSupport getPlugin() {
        return plugins;
    }

    /**
     * カルテをオープンする。
     * @param pvt 患者来院情報
     * @param keyword
     */
    @Override
    public void openKarte(PatientVisitModel pvt, String keyword) {
        boolean isReadOnry = false;
        isReadOnry = openPatient(pvt);

        IChart chart = new ChartWindow(this, pvt, isReadOnry);
        chart.setKeyword(keyword);
        chart.start();
    }

    /**
     * 
     * @return
     */
    @Override
    public MainMenuSupport getMenuSupport() {
        return menuMediator;
    }

    /**
     * IMainWindow のアクションを返す。
     * @param name Action名
     * @return Action
     */
    @Override
    public Action getAction(String name) {
        return menuMediator.getAction(name);
    }

    /**
     * 
     * @return
     */
    @Override
    public JMenuBar getMenuBar() {
        return windowSupport.getMenuBar();
    }

    /**
     *
     * @param actions
     */
    @Override
    public void registerActions(ActionMap actions) {
        menuMediator.registerActions(actions);
    }

    /**
     *
     * @param name
     * @param b
     */
    @Override
    public void enabledAction(String name, boolean b) {
        menuMediator.enabledAction(name, b);
    }

    /**
     * 
     * @return
     */
    public JFrame getFrame() {
        return windowSupport.getFrame();
    }

    /**
     *
     * @return
     */
    @Override
    public PageFormat getPageFormat() {
        if (pageFormat == null) {
            PrinterJob printJob = PrinterJob.getPrinterJob();
            if (printJob != null) {
                pageFormat = printJob.defaultPage();
            }
        }
        return pageFormat;
    }

    /**
     * ブロックする。
     */
    @Override
    public void block() {
        blockGlass.block();
    }

    /**
     * ブロックを解除する。
     */
    @Override
    public void unblock() {
        blockGlass.unblock();
    }

    /**
     * IPVTServer を開始する。
     */
    private void startPvtServer() {
        pvtServer = new PVTClientServer();
        pvtServer.setContext(this);
        pvtServer.start();
        providers.put("pvtServer", pvtServer);
        saveEnv.put(GUIConst.KEY_PVT_SERVER, GUIConst.SERVICE_RUNNING);
    }

    /**
     * IPVTServer を開始する。
     */
    //  private void startPeerServer() {
    //     peerServer = new DolphinPeerServer();
    //      peerServer.setContext(this);
    //      peerServer.start();
    //      providers.put("PeerServer", peerServer);
    //      saveEnv.put(GUIConst.KEY_PEER_SERVER, GUIConst.SERVICE_RUNNING);
    // }
    /**
     * MML送信を開始する。
     */
    //   private void startPeerClient() {
    //       peerClient = new DolphinPeerClient();
    //      peerClient.setContext(this);
    //      peerClient.start();
    //      providers.put("PeerClient", peerClient);
    //     saveEnv.put(GUIConst.KEY_PEER_CLIENT, GUIConst.SERVICE_RUNNING);
    //  }
    /**
     * CLAIM 送信を開始する。
     */
    private void startSendClaim() {
        sendClaim = new SendClaimImpl();
        sendClaim.setContext(this);
        sendClaim.start();
        providers.put("sendClaim", sendClaim);
        saveEnv.put(GUIConst.KEY_SEND_CLAIM, GUIConst.SERVICE_RUNNING);
    }

    /**
     * MML送信を開始する。
     */
    private void startSendMml() {
        sendMml = new SendMmlImpl();
        sendMml.setContext(this);
        sendMml.start();
        providers.put("sendMml", sendMml);
        saveEnv.put(GUIConst.KEY_SEND_MML, GUIConst.SERVICE_RUNNING);
    }

    /**
     * プリンターをセットアップする。
     * @return
     */
    public boolean printerSetup() {
        //command
        Runnable printer_initialize = new Runnable() {

            @Override
            public void run() {
                PrinterJob printJob = PrinterJob.getPrinterJob();
                if (pageFormat != null) {
                    pageFormat = printJob.pageDialog(pageFormat);
                } else {
                    pageFormat = printJob.defaultPage();
                    pageFormat = printJob.pageDialog(pageFormat);
                }
            }
        };
        Thread t = new Thread(printer_initialize);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
        return true;
    }

    /**
     * カルテの環境設定を行う。
     */
    private boolean setKarteEnviroment() {
        ProjectSettingDialog sd = new ProjectSettingDialog(plugins);
        sd.addPropertyChangeListener("SETTING_PROP", new PreferenceListener());
        sd.setLoginState(stateManager.isLogin());
        sd.setProject("karteSetting");
        sd.start();
        return true;
    }

    /**
     * 環境設定を行う。
     */
    public void doPreference() {
        ProjectSettingDialog sd = new ProjectSettingDialog(plugins);
        sd.addPropertyChangeListener("SETTING_PROP", new PreferenceListener());
        sd.setLoginState(stateManager.isLogin());
        sd.setProject(null);
        sd.start();
    }

    /**
     * 環境設定のリスナクラス。環境設定が終了するとここへ通知される。　MEMO:リスナー
     */
    class PreferenceListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            if (e.getPropertyName().equals("SETTING_PROP")) {
                boolean valid = ((Boolean) e.getNewValue()).booleanValue();
                if (valid) {
                    // 設定の変化を調べ、サービスの制御を行う
                    List<String> messages = new ArrayList<String>(2);
                    // PvtServer
                    boolean oldRunning = saveEnv.getProperty(GUIConst.KEY_PVT_SERVER).equals(GUIConst.SERVICE_RUNNING) ? true : false;
                    boolean newRun = GlobalVariables.getUseAsPVTServer();
                    boolean start = ((!oldRunning) && newRun) ? true : false;
                    boolean stop = ((oldRunning) && (!newRun)) ? true : false;

                    //    startPeerServer();
                    if (start) {
                        startPvtServer();
                        messages.add("受付受信を開始しました。");
                    } else if (stop && pvtServer != null) {
                        pvtServer.stop();
                        pvtServer = null;
                        saveEnv.put(GUIConst.KEY_PVT_SERVER, GUIConst.SERVICE_NOT_RUNNING);
                        messages.add("受付受信を停止しました。");
                    }

                    // SendClaim
                    oldRunning = saveEnv.getProperty(GUIConst.KEY_SEND_CLAIM).equals(GUIConst.SERVICE_RUNNING) ? true : false;

                    newRun = GlobalVariables.getSendClaim();
                    start = ((!oldRunning) && newRun) ? true : false;
                    stop = ((oldRunning) && (!newRun)) ? true : false;

                    boolean restart = false;
                    String oldAddress = saveEnv.getProperty(GUIConst.ADDRESS_CLAIM);
                    String newAddress = GlobalVariables.getClaimAddress();
                    if (oldAddress != null && newAddress != null && (!oldAddress.equals(newAddress)) && newRun) {
                        restart = true;
                    }

                    if (start) {
                        startSendClaim();
                        saveEnv.put(GUIConst.ADDRESS_CLAIM, newAddress);
                        messages.add("CLAIM送信を開始しました。(送信アドレス=" + newAddress + ")");
                        //      startPeerClient();

                    } else if (stop && sendClaim != null) {
                        sendClaim.stop();
                        sendClaim = null;
                        saveEnv.put(GUIConst.KEY_SEND_CLAIM, GUIConst.SERVICE_NOT_RUNNING);
                        saveEnv.put(GUIConst.ADDRESS_CLAIM, newAddress);
                        messages.add("CLAIM送信を停止しました。");

                    } else if (restart) {
                        sendClaim.stop();
                        sendClaim = null;
                        startSendClaim();
                        saveEnv.put(GUIConst.ADDRESS_CLAIM, newAddress);
                        messages.add("CLAIM送信をリスタートしました。(送信アドレス=" + newAddress + ")");
                        //               startPeerClient();
                    }

                    // SendMML
                    oldRunning = saveEnv.getProperty(GUIConst.KEY_SEND_MML).equals(GUIConst.SERVICE_RUNNING) ? true : false;
                    newRun = GlobalVariables.getSendMML();
                    start = ((!oldRunning) && newRun) ? true : false;
                    stop = ((oldRunning) && (!newRun)) ? true : false;

                    restart = false;
                    oldAddress = saveEnv.getProperty(GUIConst.CSGW_PATH);
                    newAddress = GlobalVariables.getCSGWPath();
                    if (oldAddress != null && newAddress != null && (!oldAddress.equals(newAddress)) && newRun) {
                        restart = true;
                    }

                    if (start) {
                        startSendMml();
                        saveEnv.put(GUIConst.CSGW_PATH, newAddress);
                        messages.add("MML送信を開始しました。(送信アドレス=" + newAddress + ")");

                    } else if (stop && sendMml != null) {
                        sendMml.stop();
                        sendMml = null;
                        saveEnv.put(GUIConst.KEY_SEND_MML, GUIConst.SERVICE_NOT_RUNNING);
                        saveEnv.put(GUIConst.CSGW_PATH, newAddress);
                        messages.add("MML送信を停止しました。");

                    } else if (restart) {
                        sendMml.stop();
                        sendMml = null;
                        startSendMml();
                        saveEnv.put(GUIConst.CSGW_PATH, newAddress);
                        messages.add("MML送信をリスタートしました。(送信アドレス=" + newAddress + ")");
                    }

                    if (messages.size() > 0) {
                        String[] msgArray = messages.toArray(new String[messages.size()]);
                        Object msg = msgArray;
                        Component cmp = null;
                        String title = "環境設定"; //GlobalVariables.getString("settingDialog.title");
                        JOptionPane.showMessageDialog(cmp, msg, GlobalConstants.getFrameTitle(title), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * 
     * @param patientId
     * @return
     */
    @Override
    public boolean isKarteOpened(long patientId) {
        boolean opened = false;
        for (IChart chart : allCharts) {
            if (chart.getPatient().getId() == patientId) {
                opened = true;
                break;
            }
        }
        return opened;
    }

    /**
     * 
     * @param chart
     */
    @Override
    public void addChart(IChart chart) {
        allCharts.add(chart);
    }

    /**
     * 
     * @param chart
     * @return
     */
    @Override
    public boolean removeChart(IChart chart) {
        return allCharts.remove(chart);
    }

    /**
     * 
     * @param chart
     */
    @Override
    public void addEditorFrame(IChart chart) {
        allEditorFrames.add(chart);
    }

    /**
     * 
     * @param chart
     */
    @Override
    public void removeEditorFrame(IChart chart) {
        allEditorFrames.remove(chart);
    }

    /**
     * 
     * @return
     */
    private boolean isDirty() {

        if (allCharts != null && allCharts.size() > 0) {
            for (IChart chart : allCharts) {
                if (chart.isDirty()) {
                    return true;
                }
            }
        }

        if (allEditorFrames != null && allEditorFrames.size() > 0) {
            for (IChart chart : allEditorFrames) {
                if (chart.isDirty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 
     * @return
     */
    private List<Callable<Boolean>> getStoppingTask() {

        // StoppingTask を集める
        List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>(1);

        try {
            HashMap<String, IMainService> cloneMap = null;
            synchronized (providers) {
                cloneMap = (HashMap) providers.clone();
            }
            Iterator iter = cloneMap.values().iterator();
            while (iter != null && iter.hasNext()) {
                IMainService pl = (IMainService) iter.next();
                if (pl instanceof IMainTool) {
                    Callable<Boolean> task = ((IMainTool) pl).getStoppingTask();
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return tasks;
    }
    /*
     * INFORMATION Dolphinの終了
     *
     */

    /**
     *
     * @return
     */
    public boolean canProcessExit() {
        //終了確認
        if (providers.isEmpty()) {
            return true;
        }
        if (JOptionPane.showConfirmDialog(null, "OpenDolphin を終了して良いですか？", "確認", JOptionPane.YES_NO_OPTION) == 0) {
            if (isDirty()) {
                alertDirty();
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     */
    public void processExit() {

        KarteClose();

        final List<Callable<Boolean>> tasks = getStoppingTask();
        try {
            boolean success = true;
            for (Callable<Boolean> c : tasks) {
                Boolean result = c.call();
                if (!result.booleanValue()) {
                    success = false;
                    break;
                }
            }
            if (!success) {
                doStoppingAlert();
            }
            OnExit();
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * 
     */
    private void OnExit() {
        UserModel loginUser = GlobalVariables.getUserModel();

        if (loginUser == null) {
            return;
        }

        for (open.dolphin.plugin.IPlugin plugin : plugins.values()) {
            PluginWrapper pluginWrapper = new PluginWrapper(plugin);
            RequestObject request = new RequestObject();

            request.setUserId(loginUser.getId());
            request.setUserName(loginUser.getSirName());
            request.setCommand(Command.LOGOUT);

            Object result = pluginWrapper.message(request);//MEMO: unused?
        }
    }

    /**
     * 
     */
    public void KarteClose() {
        for (IChart chart : allCharts) {
            chart.close();
        }
    }

    /**
     * 未保存のドキュメントがある場合の警告を表示する。
     */
    private void alertDirty() {
        String msg0 = resource.getString("exitDolphin.msg0"); //"未保存のドキュメントがあります。";
        String msg1 = resource.getString("exitDolphin.msg1"); //"保存または破棄した後に再度実行してください。";
        String taskTitle = resource.getString("exitDolphin.taskTitle");
        JOptionPane.showMessageDialog((Component) null, new Object[]{msg0, msg1}, GlobalConstants.getFrameTitle(taskTitle), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 終了処理中にエラーが生じた場合の警告をダイアログを表示する。
     * @param errorTask エラーが生じたタスク
     * @return ユーザの選択値
     */
    private void doStoppingAlert() {
        String msg1 = "アプリケーションの環境保存中にエラーが生じました。";
        Object message = new Object[]{msg1};

        // 終了する
        String exitOption = resource.getString("exitDolphin.exitOption");

        // 環境保存
        String taskTitle = resource.getString("exitDolphin.taskTitle");
        String title = GlobalConstants.getFrameTitle(taskTitle);
        //   String[] options = new String[]{cancelOption, exitOption};
        String[] options = new String[]{exitOption};
        int option = JOptionPane.showOptionDialog(null, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);//MEMO: unused?
    }

    /**
     * ユーザのパスワードを変更する。
     */
    private boolean changePassword() {
        ProfileDialog cp = new ProfileDialog();
        cp.start();
        return true;
    }

    /**
     * ユーザ登録を行う。管理者メニュー。
     */
    private boolean addUser() {
        UserDialog au = new UserDialog();
        au.start();
        return true;
    }

    /**
     * ドルフィンサポートをオープンする。
     */
    private boolean browseDolphinSupport() {
        browseURL(resource.getString("menu.dolphinSupportUrl"));
        return true;
    }

    /**
     * ドルフィンプロジェクトをオープンする。
     */
    private boolean browseDolphinProject() {
        browseURL(resource.getString("menu.dolphinUrl"));
        return true;
    }

    /**
     * MedXMLをオープンする。
     */
    private boolean browseMedXml() {
        browseURL(resource.getString("menu.medXmlUrl"));
        return true;
    }

    /**
     * URLをオープンする。
     * @param url URL
     */
    private void browseURL(final String url) {

        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();

        final Desktop desktop = Desktop.getDesktop();
        SynchronizedTask task = new SynchronizedTask<Object, Void>(app) {

            @Override
            protected Object doInBackground() throws Exception {
                try {
                    try {
                        desktop.browse(new URI(url));
                    } catch (URISyntaxException ex) {
                        LogWriter.error(getClass(), ex);
                    }
                } catch (IOException ex) {
                    LogWriter.error(getClass(), ex);
                }
                return true;
            }

            @Override
            protected void succeeded(Object result) {
            }

            @Override
            protected void cancelled() {
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
            }
        };

        //  appCtx.getTaskService().execute(task);

        task.execute();
    }

    /**
     * About を表示する。
     */
    private boolean showAbout() {
        AbstractProjectFactory f = GlobalVariables.getProjectFactory();
        f.createAboutDialog();
        return true;
    }

    /**
     * シェーマボックスを表示する。
     */
    private boolean showSchemaBox() {
        if (imageBox == null) {
            imageBox = new SchemaBox();
            imageBox.setContext(this);
            imageBox.start();
        }
        imageBox.getFrame().setVisible(true);
        imageBox.getFrame().toFront();
        return true;
    }

    /**
     * 表示する。
     */
    private boolean showTemplateEditor() {
        if (templateEditor == null) {
            templateEditor = new TemplateDialog(null, false);
        }
        templateEditor.setVisible(true);
        templateEditor.toFront();
        return true;
    }

    /**
     * スタンプボックスを表示する。
     */
    private boolean showStampBox() {
        if (!stampBox.getFrame().isVisible()) {
            getStampBox().getFrame().setVisible(true);
        }
        getStampBox().getFrame().toFront();
        return true;
    }

    /**
     * MenuMediator
     */
    public final class MenuMediator extends MainMenuSupport implements open.dolphin.helper.IMainCommandAccepter {

        /**
         *
         * @param owner
         */
        public MenuMediator(IMainCommandAccepter owner) {
            super(owner);
        }

        /**
         * global property の制御
         * @param e
         */
        public void menuSelected(MenuEvent e) {
        }

        /**
         *
         * @param actions
         */
        @Override
        public void registerActions(ActionMap actions) {
            super.registerActions(actions);
            // メインウインドウなので閉じるだけは無効にする
            //getAction(GUIConst.ACTION_WINDOW_CLOSING).setEnabled(false);
        }

        /**
         *
         * @param command
         * @return
         */
        @Override
        public boolean dispatchMainCommand(IMainCommandAccepter.MainCommand command) {
            switch (command) {
                case printerSetup:
                    return printerSetup();
                case setKarteEnviroment:
                    return setKarteEnviroment();
                case showStampBox:
                    return showStampBox();
                case showSchemaBox:
                    return showSchemaBox();
                case showTemplateEditor:
                    return showTemplateEditor();
                case changePassword:
                    return changePassword();
                case addUser:
                    return addUser();
                case close:
                    return close();
                case browseDolphinSupport:
                    return browseDolphinSupport();
                case browseDolphinProject:
                    return browseDolphinProject();
                case browseMedXml:
                    return browseMedXml();
                case showAbout:
                    return showAbout();
                default:
            }
            return false;
        }
    }

    /**
     * MainWindowState
     */
    abstract class MainWindowState {

        public MainWindowState() {
        }

        public abstract void enter();

        public abstract boolean isLogin();
    }

    /**
     * LoginState
     */
    class LoginState extends MainWindowState {

        /**
         *
         */
        public LoginState() {
        }

        /**
         *
         * @return
         */
        @Override
        public boolean isLogin() {
            return true;
        }

        /**
         *
         */
        @Override
        public void enter() {

            // Menuを制御する
            menuMediator.disableAllMenus();
            String[] enables = new String[]{
                GUIConst.ACTION_PRINTER_SETUP,
                GUIConst.ACTION_PROCESS_EXIT,
                GUIConst.ACTION_SET_KARTE_ENVIROMENT,
                GUIConst.ACTION_SHOW_STAMPBOX,
                GUIConst.ACTION_SHOW_SCHEMABOX,
                GUIConst.ACTION_CHANGE_PASSWORD,
                GUIConst.ACTION_CONFIRM_RUN,
                GUIConst.ACTION_SOFTWARE_UPDATE,
                GUIConst.ACTION_BROWS_DOLPHIN,
                GUIConst.ACTION_BROWS_DOLPHIN_PROJECT,
                GUIConst.ACTION_BROWS_MEDXML,
                GUIConst.ACTION_SHOW_ABOUT
            };
            menuMediator.enableMenus(enables);
            menuMediator.enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        }
    }

    /**
     * LogoffState
     */
    class LogoffState extends MainWindowState {

        /**
         *
         */
        public LogoffState() {
        }

        /**
         *
         * @return
         */
        @Override
        public boolean isLogin() {
            return false;
        }

        /**
         *
         */
        @Override
        public void enter() {
            menuMediator.disableAllMenus();
        }
    }

    /**
     * StateManager
     */
    class StateManager {

        private MainWindowState loginState = new LoginState();
        private MainWindowState logoffState = new LogoffState();
        private MainWindowState currentState = logoffState;

        /**
         *
         */
        public StateManager() {
        }

        /**
         *
         * @return
         */
        public boolean isLogin() {
            return currentState.isLogin();
        }

        /**
         *
         * @param b
         */
        public void processLogin(boolean b) {
            currentState = b ? loginState : logoffState;
            currentState.enter();
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(Dolphin.class, args);
    }
}
