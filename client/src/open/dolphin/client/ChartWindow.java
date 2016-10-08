/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ChartWindow.java
 *
 * Created on 2010/03/02, 14:49:55
 */
package open.dolphin.client;

import java.lang.reflect.InvocationTargetException;
import open.dolphin.client.diagnosisdocumentpanel.DiagnosisDocumentPanel;
import java.util.TooManyListenersException;
import open.dolphin.client.karte.DocumentHistoryPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import open.dolphin.client.karte.template.error.CantReadTemplateException;
import open.dolphin.helper.StripeRenderer;
import open.dolphin.helper.WindowSupport;
import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.infomodel.VersionModel;
import open.dolphin.utils.GUIDGenerator;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import java.util.concurrent.Callable;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import open.dolphin.client.caremapdocument.CareMapDocumentPanel;
import open.dolphin.client.imagebrowserdocument.ImageBrowserDocumentPanel;
import open.dolphin.client.karte.PatientInspector;
import open.dolphin.client.karte.template.Template;
import open.dolphin.component.DnDTabbedPane;
import open.dolphin.component.TabTransferHandler;
import open.dolphin.container.Pair;
import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.infomodel.GenericAdapter;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.sendclaim.SendClaimImpl;
import open.dolphin.sendmml.SendMmlImpl;
import open.dolphin.utils.CombinedStringParser;
import open.dolphin.helper.IChartCommandAccepter;
import open.dolphin.helper.IMainCommandAccepter;
import open.dolphin.helper.PlugInMenuSupport;
import open.dolphin.log.LogWriter;
import open.dolphin.plugin.PluginWrapper;
import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalVariables;
import open.socket.data.Command;
import open.socket.data.RequestObject;

/**
 *　カルテ画面　MEMO:画面
 * @author
 */
public class ChartWindow extends javax.swing.JFrame implements IChart, IInfoModel, IChartCommandAccepter {

    private static final long DELAY = 10L;
    /**
     *
     */
    public static final String CHART_STATE = "chartStateProp";   //カルテ状態の束縛プロパティ名
    //  public static final int CLOSE_NONE = 0;// 診察未終了で閉じている状態
    /**
     *
     */
    //   public static final int CLOSE_SAVE = 1;// 診察が終了し閉じている状態
    /**
     *
     */
    // public static final int OPEN_NONE = 2; // 診察未終了でオープンしている状態
    /**
     *
     */
    // public static final int OPEN_SAVE = 3; // 診察が終了しオープンしている状態
    public static final int CANCEL_PVT = -1; // 受付キャンセル
    private List<DnDTabbedPane> tabbedPanels;    // インスペクタを格納するタブペイン View
    private static PropertyChangeSupport boundSupport = new PropertyChangeSupport(new Object());// IChart 状態の通知を行うための static 束縛サポート
    private PatientInspector inspector;    // 患者インスペクタ
    private WindowSupport windowSupport;    // Window Menu をサポートする委譲クラス
    private JPanel toolPanel;
    private PatientVisitModel pvt;    // 患者来院情報
    private boolean readOnly;    // Read Only の時 true
    private IChart.state chartState;    // IChart のステート
    private ChartMediator mediator;    // Chart内のドキュメントに共通の MEDIATOR
    private StateManager status;    // State Mgr
    private SendMmlImpl mmlListener;    // MML送信 listener
    private SendClaimImpl claimListener;    // CLAIM 送信 listener
    private KarteBean karte;    // このチャートの KarteBean
    private BlockGlass blockGlass;    // GlassPane
    private ResourceMap resMap;    // Resource Map
    private ScheduledExecutorService scheduler;    // タイマー
    private ScheduledFuture<?> beeperHandle;
    private long statred;
    private long delay = DELAY;
    private String name;
    private IMainWindow context;
    private String FKeyword;
    private KarteEditor editor;
    private EditorFrame editorFrame;
    private ChartDocumentMap chartDocuments;
    private boolean isActivated;

    /**
     *
     * @return　アクティブか
     */
    @Override
    public boolean isActivated() {
        return isActivated;
    }

    /**
     *
     * @return　名前
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name　名前
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return　コンテキスト
     */
    @Override
    public IMainWindow getContext() {
        return context;
    }

    /**
     *
     * @param context　コンテキスト
     */
    @Override
    public void setContext(IMainWindow context) {
        this.context = context;
    }

    /**
     *
     */
    @Override
    public void enter() {
    }

    /**
     *
     * @return
     */
    @Override
    public Callable<Boolean> getStartingTask() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public Callable<Boolean> getStoppingTask() {
        return null;
    }

    /**
     *
     * @param name　名前
     * @return　ドキュメント
     */
    @Override
    public IChartDocument getChartDocument(String name) {
        return chartDocuments.get(name);
    }

    /**
     *
     * @param name　名前
     * @param document　ドキュメント
     */
    @Override
    public void setChartDocument(String name, IChartDocument document) {
        chartDocuments.put(name, document);
    }

    /** Creates new form ChartWindow
     * @param context
     * @param pvt
     * @param isReadOnry
     */
    protected ChartWindow(IMainWindow context, PatientVisitModel pvt, boolean isReadOnry) {
        initComponents();
        this.context = context;
        this.pvt = pvt;
        this.readOnly = isReadOnry;

        chartDocuments = new ChartDocumentMap();

        tabbedPane.setTransferHandler(new TabTransferHandler());
        profilerTabedPane.setTransferHandler(new TabTransferHandler());

        tabbedPanels = new ArrayList<DnDTabbedPane>();
        tabbedPanels.add(tabbedPane);
        tabbedPanels.add(profilerTabedPane);

        //      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     *
     */
    public void release() {
        try {
            inspector.release();
            saveLayout("tabLocation", getTabLocations());
            //     saveLayout("layout", this.getBounds());
            saveLayout("dividerLocation", getDividerLocation());
        } catch (IOException ex) {
            LogWriter.error(getClass(), ex);
        }
    }

    /*
    　プラグイン全て メッセージサーバににKARTE_CLOSEメッセージを送る。
     */
    private void closePatient() {
        if (!readOnly) {
            for (open.dolphin.plugin.IPlugin plugin : context.getPlugin().values()) {
                PluginWrapper pluginWrapper = new PluginWrapper(plugin);
                RequestObject request = new RequestObject();

                request.setPlace("");
                request.setPatientId(pvt.getPatient().getId());
                request.setCommand(Command.KARTE_CLOSE);

                Object result = pluginWrapper.message(request);
                if (result != null) {
                    if (pluginWrapper.IsDispatched()) {
                        pluginWrapper.update(result);
                        break;
                    }
                }
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splitPane1 = new javax.swing.JSplitPane();
        rightPanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();
        karteAndInspectorPanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        tabbedPane = new open.dolphin.component.DnDTabbedPane();
        profilerTabedPane = new open.dolphin.component.DnDTabbedPane();
        statusPanel = new open.dolphin.client.StatusPanel();

        splitPane1.setDividerLocation(100);
        splitPane1.setDividerSize(30);
        splitPane1.setContinuousLayout(true);
        splitPane1.setName("splitPane1"); // NOI18N
        splitPane1.setOneTouchExpandable(true);

        rightPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        rightPanel.setName("rightPanel"); // NOI18N
        rightPanel.setLayout(new java.awt.BorderLayout());
        splitPane1.setRightComponent(rightPanel);

        leftPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        leftPanel.setName("leftPanel"); // NOI18N
        leftPanel.setLayout(new java.awt.BorderLayout());
        splitPane1.setLeftComponent(leftPanel);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("chartFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.BorderLayout(0, 7));

        karteAndInspectorPanel.setName("karteAndInspectorPanel"); // NOI18N
        karteAndInspectorPanel.setLayout(new java.awt.BorderLayout());

        splitPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        splitPane.setDividerLocation(100);
        splitPane.setDividerSize(10);
        splitPane.setContinuousLayout(true);
        splitPane.setName("splitPane"); // NOI18N
        splitPane.setOneTouchExpandable(true);

        tabbedPane.setName("tabbedPane"); // NOI18N
        tabbedPane.setOpaque(true);
        splitPane.setRightComponent(tabbedPane);

        profilerTabedPane.setName("profilerTabedPane"); // NOI18N
        profilerTabedPane.setOpaque(true);
        splitPane.setLeftComponent(profilerTabedPane);

        karteAndInspectorPanel.add(splitPane, java.awt.BorderLayout.CENTER);

        mainPanel.add(karteAndInspectorPanel, java.awt.BorderLayout.CENTER);

        statusPanel.setName("statusPanel"); // NOI18N
        mainPanel.add(statusPanel, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if (evt.getOppositeWindow() != null) {
            isActivated = true;
        }
    }//GEN-LAST:event_formWindowActivated

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        if (evt.getOppositeWindow() != null) {
            isActivated = false;
        }
    }//GEN-LAST:event_formWindowDeactivated
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel karteAndInspectorPanel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel mainPanel;
    private open.dolphin.component.DnDTabbedPane profilerTabedPane;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JSplitPane splitPane1;
    private open.dolphin.client.StatusPanel statusPanel;
    private open.dolphin.component.DnDTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

    /**
     * タブ位置の初期化<br>
     * プロパティーのキーおよび値に名称と位置を設定します。
     * @return 結果（不変のプロパティーセット）
     */
    private Serializable initialTabLocations() {
        Properties result = new Properties();
        result.setProperty(DocumentBridgeImpl.TITLE, "0.0");
        result.setProperty(DiagnosisDocumentPanel.TITLE, "0.1");
        result.setProperty(CareMapDocumentPanel.TITLE, "0.2");

        String[] location = new String[2];
        location[0] = "0.3";
        location[1] = "0.4";

        // プラグインの名前と位置をプロパティに設定する
        int index = 0;
        for (open.dolphin.plugin.IPlugin plugin : context.getPlugin().values()) {
            PluginWrapper pluginWrapper = new PluginWrapper(plugin);
            if (pluginWrapper.getType() == open.dolphin.plugin.IPlugin.Type.container) {
                result.setProperty(pluginWrapper.getName(), location[index]);
                index++;
            }
        }

        // result.setProperty(LaboTestDocumentPanel.TITLE, "0.3");
        // result.setProperty(PatientInfoDocumentPanel.TITLE, "0.4");
        result.setProperty(ImageBrowserDocumentPanel.TITLE, "0.5");
        result.setProperty(PatientInspector.TITLE, "1.0");

        return result;
    }

    /**
     *
     * @return
     */
    private Serializable initialBounds() {
        return new Rectangle(5, 20, 990, 740);
    }

    /**
     *
     * @return
     */
    private Serializable initialDividerLocation() {
        Properties result = new Properties();
        result.setProperty("DividerLocation", "240");
        return result;
    }

    /**
     *
     * @param type
     * @param defaultValue
     * @return
     * @throws IOException
     */
    private Serializable loadLayout(String type, Serializable defaultValue) throws IOException {
        Serializable result = null;
        ApplicationContext appCtx = GlobalConstants.getApplicationContext();

        result = (Serializable) appCtx.getLocalStorage().load(this.getClass().getName() + "." + type + ".xml");

        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     *
     * @param type
     * @param layout
     * @throws IOException
     */
    private void saveLayout(String type, Serializable layout) throws IOException {
        GlobalConstants.getLocalStorage().save(layout, this.getClass().getName() + "." + type + ".xml");
    }

    /**
     *
     * @return　
     */
    private Properties getTabLocations() {
        Properties result = new Properties();
        int panel_index = 0;
        for (DnDTabbedPane pane : tabbedPanels) {
            //    for (int index = 0; index < pane.getComponents().length; index++) {
            int components = 0;
            for (int index = 0; index < pane.getComponentCount(); index++) {
                try {
                    if (((IChartDocument) pane.getComponentAt(index)).itLayoutSaved()) {
                        CombinedStringParser parser = new CombinedStringParser();
                        parser.add(Integer.toString(panel_index));
                        parser.add(Integer.toString(components));
                        result.setProperty(pane.getTitleAt(index), parser.toCombinedString());
                        components++;
                    }
                } catch (Exception e) {
                   LogWriter.error(getClass(), e);
                }
            }
            panel_index++;
        }
        return result;
    }

    /**
     *
     * @return
     */
    private Properties getDividerLocation() {
        Properties result = new Properties();
        result.setProperty("DividerLocation", Integer.toString(splitPane.getDividerLocation()));
        return result;
    }

    /**
     * ドキュメントタブを生成する。
     */
    private void applyTabLocations(Properties layout) {

        List<Map<Integer, String>> sorters = new ArrayList<Map<Integer, String>>();
        sorters.add(new HashMap<Integer, String>());
        sorters.add(new HashMap<Integer, String>());

        for (Enumeration e = layout.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            CombinedStringParser parser = new CombinedStringParser(layout.getProperty(key));
            int value = Integer.parseInt(parser.get(0));
            sorters.get(value).put(Integer.parseInt(parser.get(1)), key);
        }

        int sorter_index = 0;
        for (Map<Integer, String> sorter : sorters) {
            for (int index = 0; index < sorter.size(); index++) {
                String key = sorter.get(index);
                tabbedPanels.get(sorter_index).addTab(key, (Component) chartDocuments.get(key));
            }
            sorter_index++;
        }
    }

    /**
     *
     * @param layout
     */
    private void applyDividerLocation(Properties layout) {
        splitPane.setDividerLocation(Integer.parseInt(layout.getProperty("DividerLocation")));

    }

    /**
     *
     * @param keyword
     */
    @Override
    public void setKeyword(String keyword) {
        FKeyword = keyword;
    }

    /**
     * このチャートのカルテを返す。
     * @return カルテ
     */
    @Override
    public KarteBean getKarte() {
        return karte;
    }

    /**
     * このチャートのカルテを設定する。
     * @param karte このチャートのカルテ
     */
    @Override
    public void setKarte(KarteBean karte) {
        this.karte = karte;
    }

    /**
     * IChart の JFrame を返す。
     * @return チャートウインドウno JFrame
     */
    @Override
    public JFrame getFrame() {
        return windowSupport.getFrame();
    }

    /**
     * Chart内ドキュメントが共通に使用する Status パネルを返す。
     * @return IStatusPanel
     */
    @Override
    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    /**
     * Chart内ドキュメントが共通に使用する Status パネルを設定する。
     * @param statusPanel IStatusPanel
     */
    @Override
    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
     * 来院情報を設定する。
     * @param pvt 来院情報
     */
    @Override
    public void setPatientVisit(PatientVisitModel pvt) {
        this.pvt = pvt;
    }

    /**
     * 来院情報を返す。
     * @return 来院情報
     */
    @Override
    public PatientVisitModel getPatientVisit() {
        return pvt;
    }

    /**
     * ReadOnly かどうかを返す。
     * @return ReadOnlyの時 true
     */
    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * ReadOnly 属性を設定する。
     * @param readOnly ReadOnly user の時 true
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * このチャートが対象としている患者モデルを返す。
     * @return チャートが対象としている患者モデル
     */
    @Override
    public PatientModel getPatient() {
        return getKarte().getPatient();
    }

    /**
     * このチャートが対象としている患者モデルを設定する。
     * @param patientModel チャートが対象とする患者モデル
     */
    public void setPatientModel(PatientModel patientModel) {
        this.getKarte().setPatient(patientModel);
    }

    /**
     * チャートのステート属性を返す。
     * @return チャートのステート属性
     */
    @Override
    public IChart.state getChartState() {
        return chartState;
    }

    /**
     * チャートのステートを設定する。
     * @param chartState チャートステート
     */
    @Override
    public void setChartState(IChart.state chartState) {
        this.chartState = chartState;
        // インスタンスを管理する static オブジェクト
        // を使用し束縛リスナへ通知する
        ChartWindow.fireChanged(this);
    }

    /**
     * チャート内で共通に使用する Mediator を返す。
     * @return ChartMediator
     */
    @Override
    public ChartMediator getChartMediator() {
        return mediator;
    }

    /**
     * チャート内で共通に使用する Mediator を設定する。
     * @param mediator ChartMediator
     */
    public void setChartMediator(ChartMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Menu アクションを制御する。
     */
    @Override
    public void enabledAction(String name, boolean enabled) {
        mediator.enabledAction(name, enabled);
    }

    /**
     * 文書ヒストリオブジェクトを返す。
     * @return 文書ヒストリオブジェクト DocumentHistory
     */
    @Override
    public DocumentHistoryPanel getDocumentHistory() {
        return (DocumentHistoryPanel) chartDocuments.get(DocumentHistoryPanel.TITLE);
    }

    /**
     * 引数で指定されたタブ番号のドキュメントを表示する。
     * @param tabName
     */
    @Override
    public void showDocument(String tabName) {

        for (int chart_index = 0; chart_index < tabbedPane.getTabCount(); chart_index++) {
            if (tabbedPane.getTitleAt(chart_index).equals(tabName)) {
                tabbedPane.setSelectedIndex(chart_index);
            }
            if (tabbedPane.getTabComponentAt(chart_index) != null) {
                List<JTabbedPane> panels = ((IChartDocument) tabbedPane.getTabComponentAt(chart_index)).getTabbedPanels();
                if (panels != null) {
                    for (JTabbedPane pane : panels) {
                        for (int patient_index = 0; patient_index < pane.getTabCount(); patient_index++) {
                            if (pane.getTitleAt(chart_index).equals(tabName)) {
                                pane.setSelectedIndex(chart_index);
                                tabbedPane.setSelectedIndex(chart_index);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param target
     */
    public void showDocument(Component target) {

        for (Component component : tabbedPane.getComponents()) {
            if (component.equals(target)) {
                tabbedPane.setSelectedComponent(target);
                return;
            }
        }

        for (Component component : profilerTabedPane.getComponents()) {
            if (component.equals(target)) {
                profilerTabedPane.setSelectedComponent(target);
                return;
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean existLetterPane() {
        return chartDocuments.isExist(IChartDocument.TYPE.LetterImpl);
    }

    /**
     *
     * @return
     */
    public LetterView getLetterPane() {
        return ((LetterImpl) chartDocuments.getAsType(IChartDocument.TYPE.LetterImpl)).getView();
    }

    /**
     *
     * @param mode
     * @return
     */
    public boolean existEditorPane(final int mode) {
        boolean result = false;
        try {
            return chartDocuments.enumerate(new GenericAdapter<IChartDocument, Object>() {

                @Override
                public boolean onResult(IChartDocument document, Object result) {
                    if (document instanceof KarteEditor && ((KarteEditor) document).getMode() == mode) {
                        return true;
                    }
                    return false;
                }

                @Override
                public void onError(Exception ex) {
                }
            });
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return result;
    }

    /**
     *
     * @return
     */
    public boolean existEditorPane() {
        return chartDocuments.isExist(IChartDocument.TYPE.KarteEditor);
    }

    /**
     *
     * @return
     */
    public KarteEditor getEditorPane() {
        return ((KarteEditor) chartDocuments.getAsType(IChartDocument.TYPE.KarteEditor));
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isDirty() {
        return chartDocuments.isDirty();
    }

    /**
     *
     */
    @Override
    public void start() {
        // ResourceMap を保存する
        resMap = GlobalConstants.getResourceMap(ChartWindow.class);

        ApplicationContext applicationContext = GlobalConstants.getApplicationContext();
        Application app = applicationContext.getApplication();//MEMO: unused?

        int past = GlobalVariables.getKarteExtractionPeriod();

        GregorianCalendar today = new GregorianCalendar();
        today.add(GregorianCalendar.MONTH, past);
        today.clear(Calendar.HOUR_OF_DAY);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);

        RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
        KarteBean karteBean = ddl.getKarte(getPatientVisit().getPatient().getId(), today.getTime());

        try {
            karteBean.setPatient(getPatientVisit().getPatient());
            setKarte(karteBean);
            initCustomComponents();

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    getDocumentHistory().getDocumentHistory();
                }
            });
        } catch (IOException ex) {
            LogWriter.error(getClass(), ex);
        }
    }

    /**
     * Frame のタイトルを患者氏名(カナ):患者ID に設定する
     *
     * @return
     */
    private String getWindowTitle() {
        String Inspector = resMap.getString("inspector");
        StringBuilder sb = new StringBuilder();
        sb.append(getPatient().getFullName());
        sb.append("(");
        String kana = getPatient().getKanaName();
        kana = kana.replace("　", " ");
        sb.append(kana).append(")").append(" : ").append(getPatient().getPatientId());
        sb.append(Inspector).append(" | ").append(GlobalVariables.getUserId()).append(" | ");
        sb.append(GlobalVariables.getUserModel().getLicenseModel().getLicenseDesc());
        return sb.toString();
    }

    /**
     *
     * @throws IOException
     */
    private void RestoreLayouts() throws IOException {
        applyTabLocations((Properties) loadLayout("tabLocation", initialTabLocations()));
        this.setBounds((Rectangle) loadLayout("layout", initialBounds()));
        EventQueue.invokeLater(new Runnable() {

            /**
             *
             */
            @Override
            public void run() {
                try {
                    applyDividerLocation((Properties) loadLayout("dividerLocation", initialDividerLocation()));
                } catch (IOException ex) {
                    LogWriter.error(getClass(), ex);
                }
            }
        });
    }

    /**
     *
     * @param decorationed
     * @return
     */
    private JToolBar decorateToolBar(JToolBar decorationed) {
        // テキストツールを生成する
        Action action = mediator.getActions().get(GUIConst.ACTION_INSERT_TEXT);
        JButton textBtn = new JButton();

        textBtn.setName("textBtn");
        textBtn.setAction(action);
        textBtn.addMouseListener(new MouseAdapter() {

            /**
             *
             */
            @Override
            public void mousePressed(MouseEvent e) {
                Object source = e.getSource();
                if (source instanceof JButton) {
                    if (((JButton) source).isEnabled()) {
                        JPopupMenu popup = new JPopupMenu();
                        mediator.addTextMenu(popup);
                        if (!e.isPopupTrigger()) {
                            popup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            }
        });
        decorationed.add(textBtn);

        // シェーマツールを生成する
        action = mediator.getActions().get(GUIConst.ACTION_INSERT_SCHEMA);
        JButton schemaBtn = new JButton();

        schemaBtn.setName("schemaBtn");
        schemaBtn.setAction(action);
        schemaBtn.addMouseListener(new MouseAdapter() {

            /**
             *
             */
            @Override
            public void mousePressed(MouseEvent e) {
                Object source = e.getSource();
                if (source instanceof JButton) {
                    if (((JButton) source).isEnabled()) {
                        getContext().dispatchMainCommand(IMainCommandAccepter.MainCommand.showSchemaBox);
                    }
                }
            }
        });
        decorationed.add(schemaBtn);

        // スタンプツールを生成する
        action = mediator.getActions().get(GUIConst.ACTION_INSERT_STAMP);
        JButton stampBtn = new JButton();

        stampBtn.setName("stampBtn");
        stampBtn.setAction(action);
        stampBtn.addMouseListener(new MouseAdapter() {

            /**
             *
             */
            @Override
            public void mousePressed(MouseEvent e) {
                Object source = e.getSource();
                if (source instanceof JButton) {
                    if (((JButton) source).isEnabled()) {
                        JPopupMenu popup = new JPopupMenu();
                        mediator.addStampMenu(popup);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
        decorationed.add(stampBtn);

        return decorationed;
    }

    /**
     * 患者のカルテを検索取得し、GUI を構築する。
     * このメソッドはバックグランドスレッドで実行される。
     */
    private void initCustomComponents() throws IOException {

        setTitle(getWindowTitle());

        windowSupport = WindowSupport.create(this, getWindowTitle());
        JMenuBar menuBar = windowSupport.getMenuBar();  // チャート用のメニューバーを得る

        // Status パネルに表示する情報を生成する
        // カルテ登録日 Status パネルの右側に配置する
        String rdFormat = resMap.getString("rdFormat");         // yyyy-MM-dd
        String rdPrifix = resMap.getString("rdDatePrefix");     // カルテ登録日:
        String patienIdPrefix = resMap.getString("patientIdPrefix"); // 患者ID:
        Date date = getKarte().getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat(rdFormat);
        String created = sdf.format(date);
        statusPanel.setRightInfo(rdPrifix + created);           // カルテ登録日:yyyy/mm/dd

        // 患者ID Status パネルの左に配置する
        statusPanel.setLeftInfo(patienIdPrefix + getKarte().getPatient().getPatientId()); // 患者ID:xxxxxx
        mediator = new ChartMediator(this);  // ChartMediator を生成する
        IMenuAdapter appMenu = new WindowsMenuAdapter();

        appMenu.setMenuSupports(this, getContext().getMenuSupport(), mediator, getContext().getPlugin());
        appMenu.build(menuBar, false);
        mediator.registerActions(appMenu.getActionMap());

        loadDocuments();// Document プラグインのタブを生成する
        RestoreLayouts();

        tabbedPane.setSelectedIndex(0);
        tabbedPane.addChangeListener((ChangeListener) EventHandler.create(ChangeListener.class, this, "tabChanged", ""));

        toolPanel = appMenu.getToolPanelProduct();
        toolPanel.add(inspector.getBasicInfoInspector(), 0);
        toolPanel.add(decorateToolBar(new JToolBar()));

        karteAndInspectorPanel.add(toolPanel, BorderLayout.NORTH);
        mainPanel.add((JPanel) statusPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        resMap.injectComponents(mainPanel);

        status = new StateManager();

        // BlockGlass を設定する
        blockGlass = new BlockGlass();
        setGlassPane(blockGlass);

        // このチャートの Window にリスナを設定する
        addWindowListener(new WindowAdapter() {

            /**
             *
             */
            @Override
            public void windowClosing(WindowEvent e) {
                // CloseBox の処理を行う
                processWindowClosing();
            }

            /**
             *
             */
            @Override
            public void windowOpened(WindowEvent e) {
                // Window がオープンされた時の処理を行う
                ChartWindow.this.windowOpened(ChartWindow.this);
            }

            /**
             *
             */
            @Override
            public void windowClosed(WindowEvent e) {
                // Window がクローズされた時の処理を行う
                ChartWindow.this.windowClosed(ChartWindow.this);
            }

            @Override
            public void windowActivated(WindowEvent e) {
                // 文書履歴へフォーカスする
                getDocumentHistory().requestFocus();
            }
        });

        setLocationByPlatform(false);

        // MML 送信 Queue
        if (GlobalVariables.getSendMML()) {
            mmlListener = (SendMmlImpl) getContext().getPlugin("sendMml");
        }

        // CLAIM 送信 Queue
        if (GlobalVariables.getSendClaim()) {
            claimListener = (SendClaimImpl) getContext().getPlugin("sendClaim");
        }

        getFrame().setVisible(true);

        // timer 開始
        statred = System.currentTimeMillis();
        scheduler = Executors.newScheduledThreadPool(1);
        final Runnable beeper = new Runnable() {
            /*
             *
             */

            @Override
            public void run() {
                long time = System.currentTimeMillis() - statred;
                time /= 1000L;
                statusPanel.setTimeInfo(time);
            }
        };
        beeperHandle = scheduler.scheduleAtFixedRate(beeper, delay, delay, TimeUnit.SECONDS);
    }

    /**
     * MML送信リスナを返す。
     * @return MML送信リスナ
     */
    public SendMmlImpl getMMLListener() {
        return mmlListener;
    }

    /**
     * CLAIM送信リスナを返す。
     * @return CLAIM送信リスナ
     */
    public SendClaimImpl getCLAIMListener() {
        return claimListener;
    }

    /**
     * メニューを制御する。
     */
    public void controlMenu() {
        status.controlMenu();
    }

    /**
     *　各種パネルを追加する。
     * イメージ、病名
     */
    private void loadDocuments() {
        DocumentHistoryPanel documentHistoryPanel = (DocumentHistoryPanel) chartDocuments.addPanel(new DocumentHistoryPanel(this, FKeyword));
        chartDocuments.addPanel(new ImageBrowserDocumentPanel(this));
        chartDocuments.addPanel(new CareMapDocumentPanel(this));
        chartDocuments.addPanel(new DiagnosisDocumentPanel(this));
        chartDocuments.addPanel(new DocumentBridgeImpl(this));

        // プラグインを追加する
        for (final Map.Entry entry : context.getPlugin().entrySet()) {
            PluginWrapper pluginWrapper = new PluginWrapper((open.dolphin.plugin.IPlugin) entry.getValue());
            if ((pluginWrapper.getType() == open.dolphin.plugin.IPlugin.Type.panel) || (pluginWrapper.getType() == open.dolphin.plugin.IPlugin.Type.container)) {
                IChartDocument panel = pluginWrapper.panel(this);
                if (panel != null) {
                    chartDocuments.addPanel(panel);
                }
            }
        }

        inspector = (PatientInspector) chartDocuments.addPanel(new PatientInspector(this, FKeyword));
        inspector.setBorder(BorderFactory.createEmptyBorder(7, 7, 5, 2));
        inspector.setDocumentHistory(documentHistoryPanel);
    }

    /**
     * ドキュメントタブにプラグインを遅延生成し追加する。
     * @param e
     */
    public void tabChanged(ChangeEvent e) {
        // 選択されたタブ番号に対応するプラグインをテーブルから検索する
        int index = tabbedPane.getSelectedIndex();
        Component component = (Component) tabbedPane.getComponentAt(index);
        IChartDocument documentPanel = null;

        // KarteEditorの場合、IChartDocumentそのものでなく、それを含むJScrollPaneがコンポーネントとして
        // 返るため、そのJScrollPane中のKarteEditorをその場所から取り出している。
        // 本来はKarteEditor自体がJScrollPaneを持つべきであり、そのように変更する必要がある。
        // 変更後はこのコードは不要となる。
        // if (component instanceof JScrollPane) {
        //   LogWriter.info(getClass(), "Tab Change4 " + ((IChartDocument) component).getTitle());
        //   documentPanel = ((IChartDocument) ((JViewport) ((JScrollPane) component).getComponent(0)).getComponent(0));
        // } else {
        //     LogWriter.info(getClass(), "Tab Change3 " + ((IChartDocument) component).getTitle());
        documentPanel = (IChartDocument) component;
        // }
        documentPanel.enter();
    }

    /**
     * 新規カルテを作成する。
     */
    private boolean newKarte() throws TooManyListenersException, CantReadTemplateException {

        String dept = getPatientVisit().getDeptNoTokenize();
        String deptCode = getPatientVisit().getDepartmentCode();
        String insuranceUid = getPatientVisit().getInsuranceUid();
        // 新規ドキュメントのタイプ=2号カルテと可能なオプションを設定する
        String docType = IInfoModel.DOCTYPE_KARTE;
        IChart.NewKarteOption option = null;
        IKarteViewer base = null;

        for (IChartDocument document : chartDocuments.values()) {
            if (document instanceof DocumentBridgeImpl) {
                // Chart画面のタブパネル
                DocumentBridgeImpl bridge = (DocumentBridgeImpl) document;
                base = bridge.getBaseKarte();
            } else if (document instanceof KarteDocumentViewer) {
                KarteDocumentViewer viwer = (KarteDocumentViewer) document;
                base = viwer.getBaseKarte();
            }
        }

        if (base != null) {
            if (base.getDocType().equals(IInfoModel.DOCTYPE_KARTE)) {
                option = IChart.NewKarteOption.BROWSER_COPY_NEW;
            } else {
                option = IChart.NewKarteOption.BROWSER_NEW;      // ベースがあても２号カルテでない場合
            }
        } else {
            option = IChart.NewKarteOption.BROWSER_NEW;   // ベースのカルテがない場合
        }
        // 新規カルテ作成時に確認ダイアログを表示するかどうか
        NewKarteParams params = null;
        Preferences prefs = GlobalVariables.getPreferences();
        if (prefs.getBoolean(GlobalVariables.KARTE_SHOW_CONFIRM_AT_NEW, true)) {
            // 新規カルテダイアログへパラメータを渡し、コピー新規のオプションを制御する
            params = getNewKarteParams(docType, option, null, dept, deptCode, insuranceUid);
        } else {
            params = new NewKarteParams(option); // 保険、作成モード、配置方法を手動で設定する
            params.setDocType(docType);
            params.setDepartment(dept);
            params.setDepartmentCode(deptCode);
            PVTHealthInsuranceModel[] ins = getHealthInsurances(); // 保険
            params.setPVTHealthInsurance(ins[0]);
            if (insuranceUid != null) {
                for (int i = 0; i < ins.length; i++) {
                    if (ins[i].getGUID() != null) {
                        if (insuranceUid.equals(ins[i].getGUID())) {
                            params.setPVTHealthInsurance(ins[i]);
                            break;
                        }
                    }
                }
            }
            // 作成モード
            switch (option) {
                case BROWSER_NEW:
                    params.setCreateMode(IChart.NewKarteMode.EMPTY_NEW);
                    break;
                case BROWSER_COPY_NEW:
                    int cMode = prefs.getInt(GlobalVariables.KARTE_CREATE_MODE, 0);
                    if (cMode == 0) {
                        params.setCreateMode(IChart.NewKarteMode.EMPTY_NEW);
                    } else if (cMode == 1) {
                        params.setCreateMode(IChart.NewKarteMode.APPLY_RP);
                    } else if (cMode == 2) {
                        params.setCreateMode(IChart.NewKarteMode.ALL_COPY);
                    }
                    break;
                default: LogWriter.fatal(getClass(), "case default");
            }
            params.setOpenFrame(prefs.getBoolean(GlobalVariables.KARTE_PLACE_MODE, true));   // 配置方法
        }

        if (params == null) { // キャンセルした場合はリターンする
            return true;
        }

        DocumentModel editModel = null;
        // Baseになるカルテがあるかどうかでモデルの生成が異なる
        if (params.getCreateMode() == IChart.NewKarteMode.EMPTY_NEW) {
            editModel = getKarteModelToEdit(params);
        } else if (params.getCreateMode() == IChart.NewKarteMode.FROM_TEMPLATE) {
            editModel = getKarteModelToEditFromTemplate(params);
        } else {
            editModel = getKarteModelToEdit(base.getModel(), params);
        }
        if (params.getConfirmDate() != null) {
            editModel.setConfirmDate(params.getConfirmDate());
        }
        editor = createEditor(params.isOpenFrame(), editModel, this, true, KarteEditor.DOUBLE_MODE);
        editor.setIsHospital(params.isHospital());
        enabledAction(GUIConst.ACTION_NEW_DOCUMENT, false);

        enabledAction(GUIConst.ACTION_NEW_KARTE, false);

        if (params.isOpenFrame()) {
            editorFrame = new EditorFrame(this, editor);
            editorFrame.start();
            enabledAction(GUIConst.ACTION_DIRECTION, false);
        } else {
            if (!existEditorPane(KarteEditor.DOUBLE_MODE)) {
                editor.initialize();
                editor.start();
                this.addChartDocument(editor, params);
                enabledAction(GUIConst.ACTION_DIRECTION, true);
            }
        }

        return true;
    }

    /**
     * EmptyNew 新規カルテのモデルを生成する。
     * @param params 作成パラメータセット
     * @return 新規カルテのモデル
     */
    public DocumentModel getKarteModelToEdit(NewKarteParams params) {
        DocumentModel model = new DocumentModel();        // カルテモデルを生成する
        DocInfoModel docInfo = model.getDocInfo();        // DocInfoを設定する
        docInfo.setDocId(GUIDGenerator.generate(docInfo));        // docId 文書ID
        docInfo.setPurpose(PURPOSE_RECORD);        // 生成目的
        if (params != null) {
            docInfo.setDocType(params.getDocType());        // DocumentType
        }
        docInfo.setDepartmentDesc(getPatientVisit().getDeptNoTokenize());
        docInfo.setDepartment(getPatientVisit().getDepartmentCode());

        // 健康保険を設定する
        if (params != null) {
            PVTHealthInsuranceModel insurance = params.getPVTHealthInsurance();
            docInfo.setHealthInsurance(insurance.getInsuranceClassCode());
            docInfo.setHealthInsuranceDesc(insurance.toString());
            docInfo.setHealthInsuranceGUID(insurance.getGUID());
        }

        // Versionを設定する
        VersionModel version = new VersionModel();
        version.initialize();
        docInfo.setVersionNumber(version.getVersionNumber());

        // Document の Status を設定する
        // 新規カルテの場合は none
        docInfo.setStatus(STATUS_NONE);
        return model;
    }

    /**
     * コピーして新規カルテを生成する場合のカルテモデルを生成する。
     * @param oldModel コピー元のカルテモデル
     * @param params 生成パラメータセット
     * @return 新規カルテのモデル
     */
    public DocumentModel getKarteModelToEdit(DocumentModel oldModel, NewKarteParams params) {

        // 新規モデルを作成し、表示されているモデルの内容をコピーする
        DocumentModel newModel = new DocumentModel();
        boolean applyRp = params.getCreateMode() == IChart.NewKarteMode.APPLY_RP ? true : false;
        copyModel(oldModel, newModel, applyRp);
        DocInfoModel docInfo = newModel.getDocInfo();        // 新規カルテの DocInfo を設定する
        docInfo.setDocId(GUIDGenerator.generate(docInfo));        // 文書ID
        docInfo.setPurpose(PURPOSE_RECORD);        // 生成目的
        docInfo.setDocType(params.getDocType());        // DocumentType

        docInfo.setDepartmentDesc(getPatientVisit().getDeptNoTokenize());
        docInfo.setDepartment(getPatientVisit().getDepartmentCode());

        // 健康保険を設定する
        PVTHealthInsuranceModel insurance = params.getPVTHealthInsurance();
        docInfo.setHealthInsurance(insurance.getInsuranceClassCode());
        docInfo.setHealthInsuranceDesc(insurance.toString());
        docInfo.setHealthInsuranceGUID(insurance.getGUID());

        // Versionを設定する
        VersionModel version = new VersionModel();
        version.initialize();
        docInfo.setVersionNumber(version.getVersionNumber());

        // Document の Status を設定する
        // 新規カルテの場合は none
        docInfo.setStatus(STATUS_NONE);
        return newModel;
    }

    /**
     *
     * @param params
     * @return
     * @throws CantReadTemplateException
     */
    private DocumentModel getKarteModelToEditFromTemplate(NewKarteParams params) throws CantReadTemplateException {
        Template template = params.getSelectedTemplate();
        if (template != null) {
            return getKarteModelToEdit(template.getBody(), params);
        }
        return getKarteModelToEdit(params);
    }

    /**
     * 修正の場合のカルテモデルを生成する。
     * @param oldModel 修正対象のカルテモデル
     * @return 新しい版のカルテモデル
     */
    public DocumentModel getKarteModelToEdit(DocumentModel oldModel) {

        // 修正対象の DocInfo を取得する
        DocInfoModel oldDocInfo = oldModel.getDocInfo();

        // 新しい版のモデルにモジュールと画像をコピーする
        DocumentModel newModel = new DocumentModel();
        copyModel(oldModel, newModel, false);
        DocInfoModel newInfo = newModel.getDocInfo();        // 新しい版の DocInfo を設定する
        newInfo.setDocId(GUIDGenerator.generate(newInfo));        // 文書ID
        newInfo.setFirstConfirmDate(oldDocInfo.getFirstConfirmDate());       // 新しい版の firstConfirmDate = 元になる版の firstConfirmDate
        newInfo.setDocType(oldDocInfo.getDocType());        // docType = old one
        newInfo.setPurpose(oldDocInfo.getPurpose());        // purpose = old one
        newInfo.setTitle(oldDocInfo.getTitle());        // タイトルも引き継ぐ

        // 診療科を設定する
        // 元になる版の情報を利用する
        newInfo.setDepartmentDesc(oldDocInfo.getDepartmentDesc());
        newInfo.setDepartment(oldDocInfo.getDepartment());

        // 健康保険を設定する
        // 元になる版の情報を利用する
        newInfo.setHealthInsuranceDesc(oldDocInfo.getHealthInsuranceDesc());
        newInfo.setHealthInsurance(oldDocInfo.getHealthInsurance());
        newInfo.setHealthInsuranceGUID(oldDocInfo.getHealthInsuranceGUID());

        // 親文書IDを設定する
        newInfo.setParentId(oldDocInfo.getDocId());
        newInfo.setParentIdRelation(PARENT_OLD_EDITION);

        // old PK を設定する
        newInfo.setParentPk(oldModel.getId());

        // Versionを設定する
        // new = old + 1.0
        VersionModel newVersion = new VersionModel();
        newVersion.setVersionNumber(oldDocInfo.getVersionNumber());
        newVersion.incrementNumber(); // version number ++
        newInfo.setVersionNumber(newVersion.getVersionNumber());

        // Document Status を設定する
        // 元になる版の status (Final | Temporal | Modified)
        newInfo.setStatus(oldDocInfo.getStatus());

        return newModel;
    }

    /**
     * カルテエディタを生成する。
     * @param isOpenFrame
     * @param mode
     * @param parent
     * @param editModel
     * @param editable 
     * @return カルテエディタ
     * @throws TooManyListenersException 
     */
    public KarteEditor createEditor(boolean isOpenFrame, DocumentModel editModel, IChart parent, boolean editable, int mode) throws TooManyListenersException {
        KarteEditor result = null;
        result = new KarteEditor(parent);
        result.setOpenFrame(isOpenFrame);
        result.addMMLListner(mmlListener);
        result.addCLAIMListner(claimListener);
        result.setModel(editModel);
        result.setEditable(editable);
        result.setMode(mode);
        return result;
    }

    // モデルをコピーする
    // ToDO 参照ではいけない
    // DocInfo の設定はない
    private void copyModel(DocumentModel oldModel, DocumentModel newModel, boolean applyRp) {
        // 前回処方を適用する場合
        if (applyRp) {
            Set<ModuleModel> modules = oldModel.getModules();
            if (modules != null) {
                Collection<ModuleModel> apply = new ArrayList<ModuleModel>(5);
                for (ModuleModel bean : modules) {
                    IInfoModel model = bean.getModel();
                    if (model instanceof ClaimBundle) {
                        if (((ClaimBundle) model).getClassCode().startsWith("2")) { // 処方かどうかを判定する
                            apply.add(bean);
                        }
                    }
                }
                if (!apply.isEmpty()) {
                    newModel.setModules(new LinkedHashSet<ModuleModel>(apply));
                }
            }
        } else {
            // 全てコピー
            newModel.setModules(oldModel.getModules());
            newModel.setSchemas(oldModel.getSchemas());
        }
    }

    /**
     * カルテ作成時にダアイログをオープンし、保険を選択させる。
     *
     * @param docType
     * @param option 
     * @param insuranceUid
     * @param f
     * @return NewKarteParams
     */
    private NewKarteParams getNewKarteParams(String docType, IChart.NewKarteOption option, JFrame f, String dept, String deptCode, String insuranceUid) {
        NewKarteParams params = new NewKarteParams(option);
        params.setDocType(docType);
        params.setDepartment(dept);
        params.setDepartmentCode(deptCode);
        params.setHospital(GlobalVariables.getIsHospital());

        // 患者の健康保険コレクション
        Collection<PVTHealthInsuranceModel> insurances = pvt.getPatient().getPvtHealthInsurances();

        // コレクションが null の場合は自費保険を追加する
        if (insurances == null || insurances.isEmpty()) {
            insurances = new ArrayList<PVTHealthInsuranceModel>(1);
            PVTHealthInsuranceModel model = new PVTHealthInsuranceModel();
            model.setInsuranceClass(INSURANCE_SELF);
            model.setInsuranceClassCode(INSURANCE_SELF_CODE);
            model.setInsuranceClassCodeSys(INSURANCE_SYS);
            insurances.add(model);
        }

        // 保険コレクションを配列に変換し、パラメータにセットする
        // ユーザがこの中の保険を選択する
        PVTHealthInsuranceModel[] insModels = (PVTHealthInsuranceModel[]) insurances.toArray(new PVTHealthInsuranceModel[insurances.size()]);
        params.setInsurances(insModels);
        int index = 0;
        if (insuranceUid != null) {
            for (int i = 0; i < insModels.length; i++) {
                if (insModels[i].getGUID() != null) {
                    if (insModels[i].getGUID().equals(insuranceUid)) {
                        index = i;
                        break;
                    }
                }
            }
        }
        params.setInitialSelectedInsurance(index);
        String text = option == IChart.NewKarteOption.BROWSER_MODIFY ? resMap.getString("modifyKarteTitle") : resMap.getString("newKarteTitle");
        text = GlobalConstants.getFrameTitle(text);
        JFrame frame = f != null ? f : getFrame();        // モーダルダイアログを表示する
        NewKarteDialog od = new NewKarteDialog(frame, text);
        od.setValue(params);
        od.start();

        // 戻り値をリターンする
        params = od.getValue();
        return params;
    }

    /**
     * 患者の健康保険を返す。
     * @return 患者の健康保険配列
     */
    @Override
    public PVTHealthInsuranceModel[] getHealthInsurances() {
        // 患者の健康保険
        Collection<PVTHealthInsuranceModel> insurances = pvt.getPatient().getPvtHealthInsurances();
        if (insurances == null || insurances.isEmpty()) {
            insurances = new ArrayList<PVTHealthInsuranceModel>(1);
            PVTHealthInsuranceModel model = new PVTHealthInsuranceModel();
            model.setInsuranceClass(INSURANCE_SELF);
            model.setInsuranceClassCode(INSURANCE_SELF_CODE);
            model.setInsuranceClassCodeSys(INSURANCE_SYS);
            insurances.add(model);
        }
        return (PVTHealthInsuranceModel[]) insurances.toArray(new PVTHealthInsuranceModel[insurances.size()]);
    }

    /**
     * タブにドキュメントを追加する。
     * @param doc 追加するドキュメント
     * @param params 追加するドキュメントの情報を保持する NewKarteParams
     */
    public void addChartDocument(IChartDocument doc, NewKarteParams params) {
        String title = null;
        if (params.getPVTHealthInsurance() != null) {
            title = getTabTitle(params.getDepartment(), params.getPVTHealthInsurance().getInsuranceClass());
        } else {
            title = getTabTitle(params.getDepartment(), null);
        }
        addChartDocument(doc, title);
    }

    /**
     * タブにドキュメントを追加する。
     * @param doc
     * @param title タブタイトル
     */
    public void addChartDocument(IChartDocument doc, String title) {
        tabbedPane.addTab(title, (JPanel) doc);
        int index = tabbedPane.getTabCount() - 1;
        tabbedPane.setSelectedIndex(index);
        chartDocuments.put(doc.getTitle(), doc);
    }

    /**
     * 新規カルテ用のタブタイトルを作成する
     * @param dept
     * @param insurance 保険名
     * @return タブタイトル
     */
    public String getTabTitle(String dept, String insurance) {
        String[] depts = dept.split("\\s*,\\s*");
        StringBuilder buf = new StringBuilder();
        buf.append(resMap.getString("newKarteTabTitle"));
        if (insurance != null) {
            buf.append("(");
            buf.append(depts[0]);
            buf.append("・");
            buf.append(insurance);
            buf.append(")");
        }
        return buf.toString();
    }

    /**
     * 新規文書作成で選択されたプラグインを起動する。
     *
     * @param pluginClass 起動するプラグインのクラス名
     */
    private void invokePlugin(Class klass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        if (klass.equals(LetterImpl.class) && existLetterPane()) {
            javax.swing.JOptionPane.showMessageDialog(null, "既に紹介状は開かれています。", "紹介状", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Constructor<IChartDocument> constructor = klass.getConstructor(new Class[]{IChart.class});
        IChartDocument doc = (IChartDocument) constructor.newInstance(this);

        if (doc instanceof KarteEditor) {
            String dept = getPatientVisit().getDeptNoTokenize();
            String deptCode = getPatientVisit().getDepartmentCode();
            String insuranceUid = getPatientVisit().getInsuranceUid();
            IChart.NewKarteOption option = IChart.NewKarteOption.BROWSER_NEW;
            String docType = IInfoModel.DOCTYPE_S_KARTE;
            NewKarteParams params = new NewKarteParams(option);
            params.setDocType(docType);
            params.setDepartment(dept);
            params.setDepartmentCode(deptCode);

            // 保険
            PVTHealthInsuranceModel[] ins = getHealthInsurances();
            params.setPVTHealthInsurance(ins[0]);
            if (insuranceUid != null) {
                for (int i = 0; i < ins.length; i++) {
                    if (ins[i].getGUID() != null) {
                        if (insuranceUid.equals(ins[i].getGUID())) {
                            params.setPVTHealthInsurance(ins[i]);
                            break;
                        }
                    }
                }
            }
            SimpleDate today = new SimpleDate(new GregorianCalendar());
            params.setConfirmDate(SimpleDate.simpleDateToMmldate(today) + "T00:00:00");
            DocumentModel editModel = getKarteModelToEdit(params);
            editor = (KarteEditor) doc;
            editor.setModel(editModel);
            editor.setEditable(true);
            editor.setMode(KarteEditor.SINGLE_MODE);
            editor.initialize();
            editor.start();
            this.addChartDocument(editor, params);
        } else {
            doc.start();
            addChartDocument(doc, doc.getTitle());
        }

    }

    /**
     * カルテ以外の文書を作成する。
     */
    private boolean newDocument() {

        // 拡張ポイント新規文書のプラグインをリストアップし、
        // リストで選択させる
        List<Pair<String, Class>> documents = new ArrayList<Pair<String, Class>>(3);
        Pair<String, Class> pair = null;

        pair = new Pair<String, Class>("紹介状", LetterImpl.class);
        documents.add(pair);

        pair = new Pair<String, Class>("紹介患者経過報告書", LetterReplyImpl.class);
        documents.add(pair);

        pair = new Pair<String, Class>("プレイン文書(台紙)", KarteEditor.class);
        documents.add(pair);

        JLabel newDocsLabel = new JLabel();
        newDocsLabel.setName("newDocsLabel");
        resMap.injectComponent(newDocsLabel);

        final JList docList = new JList(documents.toArray());
        docList.setCellRenderer(new StripeRenderer());

        JPanel panel = new JPanel(new BorderLayout(7, 0));

        panel.add(newDocsLabel, BorderLayout.WEST);
        panel.add(docList, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));

        JPanel content = new JPanel(new BorderLayout());
        content.add(panel, BorderLayout.CENTER);
        content.setBorder(BorderFactory.createTitledBorder("作成する文書"));

        final JButton okButton = new JButton("了解");
        final JButton cancelButton = new JButton("取消し");
        Object[] options = new Object[]{okButton, cancelButton};

        JOptionPane jop = new JOptionPane(content, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, okButton);

        final JDialog dialog = jop.createDialog(getFrame(), GlobalConstants.getFrameTitle("新規文書作成"));
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                docList.requestFocusInWindow();
            }
        });

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dialog.setVisible(false);
                    dialog.dispose();
                    Pair<String, Class> pair = (Pair<String, Class>) docList.getSelectedValue();
                    Class clsName = pair.getValue();
                    invokePlugin(clsName);
                } catch (NoSuchMethodException ex) {
                    LogWriter.error(getClass(), ex);
                } catch (InstantiationException ex) {
                    LogWriter.error(getClass(), ex);
                } catch (IllegalAccessException ex) {
                    LogWriter.error(getClass(), ex);
                } catch (IllegalArgumentException ex) {
                    LogWriter.error(getClass(), ex);
                } catch (InvocationTargetException ex) {
                    LogWriter.error(getClass(), ex);
                }
            }
        });
        okButton.setEnabled(false);

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        docList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = docList.getSelectedIndex();
                    if (index >= 0) {
                        okButton.setEnabled(true);
                    }
                }
            }
        });

        dialog.setVisible(true);
        return true;
    }

    /**
     *
     * @param dirtyList
     * @return
     */
    private boolean prepareAll(List<Pair<IChartDocument, JCheckBox>> dirtyList) {
        for (Pair<IChartDocument, JCheckBox> pair : dirtyList) {
            if (pair.value.isSelected()) {
                if (!pair.key.prepare()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @param dirtyList
     */
    private void innerSaveAll(List<Pair<IChartDocument, JCheckBox>> dirtyList) {
        for (Pair<IChartDocument, JCheckBox> pair : dirtyList) {
            if (pair.value.isSelected()) {
                pair.key.dispatchChartCommand(ChartCommand.save);
            }
        }
    }

    /**
     * 全てのドキュメントを保存する。
     * @param dirtyList 未保存ドキュメントのリスト
     */
    private boolean saveAll(List<Pair<IChartDocument, JCheckBox>> dirtyList) {

        if (prepareAll(dirtyList)) {
            innerSaveAll(dirtyList);
            return true;
        }
        return false;
    }

    /**
     * CloseBox がクリックされた時の処理を行う。
     */
    public void processWindowClosing() {
        close();
    }

    /**
     * チャートウインドウを閉じる。
     */
    @Override
    public boolean close() {
        closePatient();

        List<Pair<IChartDocument, JCheckBox>> dirtyList = new ArrayList<Pair<IChartDocument, JCheckBox>>();
        for (IChartDocument component : chartDocuments.values()) {
            if (component.isDirty()) {
                dirtyList.add(new Pair(component, new JCheckBox(component.getTitle())));
            }
        }

        if (dirtyList.size() > 0) {
            String saveAll = resMap.getString("unsavedtask.saveText");     // 保存;
            String discard = resMap.getString("unsavedtask.discardText");  // 破棄;
            String question = resMap.getString("unsavedtask.question");    // 未保存のドキュメントがあります。保存しますか ?
            String title = resMap.getString("unsavedtask.title");          // 未保存処理
            String cancelText = (String) UIManager.get("OptionPane.cancelButtonText");
            Object[] message = new Object[dirtyList.size() + 1];
            message[0] = (Object) question;
            int index = 1;
            for (Pair<IChartDocument, JCheckBox> pair : dirtyList) {
                JCheckBox checkBox = pair.value;
                checkBox.setSelected(true);
                message[index] = checkBox;
                index++;
            }

            int state = JOptionPane.showOptionDialog(getFrame(), message, GlobalConstants.getFrameTitle(title), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{saveAll, discard, cancelText}, saveAll);
            switch (state) {
                case 0:      // save
                    if (saveAll(dirtyList)) {
                        stop();
                    }
                    break;
                case 1:   // discard
                    stop();
                    break;
                case 2: // cancel
                    break;
                default: LogWriter.fatal(getClass(), "case default");
            }
        } else {
            stop();
        }
        return true;
    }

    /**
     *
     */
    @Override
    public void stop() {
        beeperHandle.cancel(true);
        for (IChartDocument component : chartDocuments.values()) {
            if (component != null) {
                component.stop();
            }
        }

        mediator.dispose();
        release();
        inspector.dispose();

        getFrame().setVisible(false);
        getFrame().setJMenuBar(null);
        getFrame().dispose();
    }

    /**
     *
     * @param command
     * @return
     */
    public boolean dispatchMainCommand(open.dolphin.helper.IMainCommandAccepter.MainCommand command) {
        switch (command) {
            case close:
                return close();
            default:
        }
        return false;
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchChartCommand(IChartCommandAccepter.ChartCommand command) {
        try {
            try {
                switch (command) {
                    case newKarte:
                        return newKarte();
                    case newDocument:
                        return newDocument();
                    case close:
                        return close();
                    case direction:
                        return direction();
                    default:
                }
            } catch (CantReadTemplateException ex) {
                LogWriter.error(getClass(), ex);
            }
        } catch (TooManyListenersException ex) {
            LogWriter.error(getClass(), ex);
        }
        return false;
    }

    /**
     *
     * @return
     */
    private boolean direction() {
        if (editor != null) {
            DirectionDialog direction = new DirectionDialog(null, true, editor.getActualModel());
            direction.setVisible(true);
        }
        return true;
    }

    /**
     * チャートステートの束縛リスナを登録する。
     * @param prop 束縛プロパティ名
     * @param l 束縛リスナ
     */
    public static void _addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     * チャートステートの束縛リスナを削除する。
     * @param prop 束縛プロパティ名
     * @param l 束縛リスナ
     */
    public static void _removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     * チャートウインドウのオープンを通知する。
     * @param opened オープンした ChartPlugin
     */
    public void windowOpened(ChartWindow opened) {
        // インスタンスを保持するリストへ追加する
        context.addChart(opened);

        // PVT (IChart) の状態を設定する
        PatientVisitModel model = opened.getPatientVisit();
        state oldState = model.getState();
        state newState = state.CLOSE_NONE;
        switch (oldState) {
            case CLOSE_NONE:
                newState = state.OPEN_NONE;
                break;
            case CLOSE_SAVE:
                newState = state.OPEN_SAVE;
                break;
            case OPEN_NONE:
                break;
            case OPEN_SAVE:
                break;
            case CANCEL_PVT:
            default:
            //            throw new RuntimeException("Invalid Chart State");
        }

        // 通知する
        model.setState(newState);
        boundSupport.firePropertyChange(ChartWindow.CHART_STATE, null, model);
    }

    /**
     * チャートウインドウのクローズを通知する。
     * @param closed クローズした ChartPlugin
     */
    public void windowClosed(ChartWindow closed) {
        // インスタンスリストから取り除く
        if (context.removeChart(closed)) {
            // チャートの状態を PVT に設定する
            PatientVisitModel model = closed.getPatientVisit();
            state oldState = model.getState();
            state newState = state.CLOSE_NONE;
            switch (oldState) {
                case OPEN_NONE:
                    newState = state.CLOSE_NONE;
                    break;
                case OPEN_SAVE:
                    newState = state.CLOSE_SAVE;
                    break;
                case CANCEL_PVT:
                default:

            }
            // 通知する
            model.setState(newState);
            boundSupport.firePropertyChange(ChartWindow.CHART_STATE, null, model);
            closed = null;
        }
    }

    /**
     *
     * @param label
     * @param document
     */
    @Override
    public void closeChartDocument(String label, IChartDocument document) {
        tabbedPane.remove((Component) document);
        profilerTabedPane.remove((Component) document);
        chartDocuments.remove(label);
    }

    /**
     * チャート状態の変化を通知する。
     * @param changed
     */
    public static void fireChanged(ChartWindow changed) {
        PatientVisitModel model = changed.getPatientVisit();
        model.setState(changed.getChartState());
    }

    /**
     *
     * @return
     */
    @Override
    public PlugInMenuSupport getPlugins() {
        return context.getPlugin();
    }

    /**
     *
     */
    protected abstract class ChartState {

        /**
         *
         */
        public ChartState() {
        }

        /**
         *
         */
        public abstract void controlMenu();
    }

    /**
     * ReadOnly ユーザの State クラス。
     */
    protected final class ReadOnlyState extends ChartState {

        /**
         *
         */
        public ReadOnlyState() {
        }

        /**
         * 新規カルテ作成及び修正メニューを disable にする。
         */
        @Override
        public void controlMenu() {
            enabledAction(GUIConst.ACTION_NEW_KARTE, false);
            enabledAction(GUIConst.ACTION_MODIFY_KARTE, false);
        }
    }

    /**
     * 保険証がない場合の State クラス。
     */
    protected final class NoInsuranceState extends ChartState {

        /**
         *
         */
        public NoInsuranceState() {
        }

        /**
         *
         */
        @Override
        public void controlMenu() {
            enabledAction(GUIConst.ACTION_NEW_KARTE, false);
        }
    }

    /**
     * 通常の State クラス。
     */
    protected final class OrdinalyState extends ChartState {

        /**
         *
         */
        public OrdinalyState() {
        }

        /**
         *
         */
        @Override
        public void controlMenu() {
            enabledAction(GUIConst.ACTION_NEW_KARTE, true);
        }
    }

    /**
     * State Manager クラス。
     */
    protected final class StateManager {

        private ChartState readOnlyState = new ReadOnlyState();
        private ChartState noInsuranceState = new NoInsuranceState();
        private ChartState ordinalyState = new OrdinalyState();
        private ChartState currentState;

        /**
         *
         */
        public StateManager() {
            if (isReadOnly()) {
                enterReadOnlyState();
            } else {
                enterOrdinalyState();
            }
        }

        /**
         *
         */
        public void enterReadOnlyState() {
            currentState = readOnlyState;
            currentState.controlMenu();
        }

        /**
         *
         */
        public void enterNoInsuranceState() {
            currentState = noInsuranceState;
            currentState.controlMenu();
        }

        /**
         *
         */
        public void enterOrdinalyState() {
            currentState = ordinalyState;
            currentState.controlMenu();
        }

        /**
         *
         */
        public void controlMenu() {
            currentState.controlMenu();
        }
    }
}
