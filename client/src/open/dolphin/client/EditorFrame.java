/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditorFrame.java
 *
 * Created on 2010/03/05, 14:40:59
 */
package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import open.dolphin.client.karte.DocumentHistoryPanel;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.Serializable;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import open.dolphin.project.GlobalSettings;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.helper.WindowSupport;
import org.jdesktop.application.ResourceMap;

import java.util.concurrent.Callable;
import open.dolphin.project.GlobalVariables;
import open.dolphin.helper.IChartCommandAccepter;
import open.dolphin.helper.IMainCommandAccepter;
import open.dolphin.helper.PlugInMenuSupport;
import open.dolphin.log.LogWriter;
import open.dolphin.utils.Persistent;

/**
 *　カルテエディタ画面　MEMO:画面
 * @author
 */
public class EditorFrame extends javax.swing.JFrame implements IChart, IChartCommandAccepter {

    // このクラスの２つのモード（状態）でメニューの制御に使用する
    /**
     *
     */
    public enum EditorMode {

        /**
         *
         */
        BROWSER,
        /**
         *
         */
        EDITOR
    };
    private String name;
    private IMainWindow context;
    //   private static List<IChart> allEditorFrames = new ArrayList<IChart>(3);    // 全インスタンスを保持するリスト
    private IChart parentChart;    // このフレームの実のコンテキストチャート
    private IKarteViewer view;    // このフレームに表示する KarteView オブジェクト
    private KarteEditor editor;    // このフレームに表示する KarteEditor オブジェクト
    private JPanel myToolPanel;    // ToolBar パネル
    private JScrollPane scroller;    // スクローラコンポーネント
    private StatusPanel statusPanel;    // Status パネル
    private EditorMode mode;    // このフレームの動作モード
    private WindowSupport windowSupport;    // WindowSupport オブジェクト
    private ChartMediator mediator;    // Mediator オブジェクト
    private BlockGlass blockGlass;    // Block GlassPane
    private ResourceMap resMap;
    private JPanel content;
    private String FKeyword;//MEMO: unused?
    private boolean isActivated;

    /**
     *
     * @param chartCtx
     * @param editor
     */
    public EditorFrame(IChart chartCtx, KarteEditor editor) {
        initComponents();
        this.parentChart = chartCtx;
        this.context = chartCtx.getContext();
        this.editor = editor;
        this.editor.setParent(this);
        context.addEditorFrame(this);
    }

    /**
     *
     * @return
     */
    private Serializable initialBounds() {
        return new Rectangle(5, 20, 724, 740);
    }

    /**
     *
     * @param keyword　キーワード
     */
    @Override
    public void setKeyword(String keyword) {
        FKeyword = keyword;
    }

    /**
     *　名前で示されるドキュメント
     * @param name　名前
     * @return　ドキュメント
     */
    @Override
    public IChartDocument getChartDocument(String name) {
        return parentChart.getChartDocument(name);
    }

    /**
     *
     * @param name　名前
     * @param document　ドキュメント
     */
    @Override
    public void setChartDocument(String name, IChartDocument document) {
        parentChart.setChartDocument(name, document);
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchChartCommand(IChartCommandAccepter.ChartCommand command) {
        switch (command) {
            case save:
                return save();
            case close:
                return close();
            case print:
                return print();
            default:
        }
        return false;
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
    private static PageFormat pageFormat = null;

    static {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if (printJob != null && pageFormat == null) {
            // set default format
            pageFormat = printJob.defaultPage();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        if (evt.getOppositeWindow() != null) {
            isActivated = false;
        }
    }//GEN-LAST:event_formWindowDeactivated

    /**
     *
     * @param evt
     */
    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        if (evt.getOppositeWindow() != null) {
            isActivated = true;
        }
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     *
     * @return
     */
    public IChart getChart() {
        return parentChart;
    }

    /**
     * 表示する KarteViewer1 オブジェクトを設定する。
     * @param view 表示する KarteView
     */
    public void setKarteViewer(IKarteViewer view) {
        this.view = view;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * 患者モデルを返す。
     * @return 患者モデル
     */
    @Override
    public PatientModel getPatient() {
        return parentChart.getPatient();
    }

    /**
     * 対象としている KarteBean オブジェクトを返す。
     * @return KarteBean オブジェクト
     */
    @Override
    public KarteBean getKarte() {
        return parentChart.getKarte();
    }

    /**
     * 対象となる KarteBean オブジェクトを設定する。
     * @param karte KarteBean オブジェクト
     */
    @Override
    public void setKarte(KarteBean karte) {
        parentChart.setKarte(karte);
    }

    /**
     * 来院情報を返す。
     * @return 来院情報
     */
    @Override
    public PatientVisitModel getPatientVisit() {
        return parentChart.getPatientVisit();
    }

    /**
     * 来院情報を設定する。
     * @param model 来院情報モデル
     */
    @Override
    public void setPatientVisit(PatientVisitModel model) {
        parentChart.setPatientVisit(model);
    }

    /**
     * IChart state を返す。
     * @return IChart の state 属性
     */
    @Override
    public IChart.state getChartState() {
        return parentChart.getChartState();
    }

    /**
     * IChart state を設定する。
     * @param state IChart の state
     */
    @Override
    public void setChartState(IChart.state state) {
        parentChart.setChartState(state);
    }

    /**
     * ReadOnly かどうかを返す。
     * @return readOnly の時 true
     */
    @Override
    public boolean isReadOnly() {
        return parentChart.isReadOnly();
    }

    /**
     * ReadOnly 属性を設定する。
     */
    @Override
    public void setReadOnly(boolean b) {
        parentChart.setReadOnly(b);
    }

    /**
     * このオブジェクトの JFrame を返す。
     * @return JFrame オブジェクト
     */
    @Override
    public JFrame getFrame() {
        return this;
    }

    /**
     * StatusPanel を返す。
     * @return StatusPanel
     */
    @Override
    public StatusPanel getStatusPanel() {
        return this.statusPanel;
    }

    /**
     * StatusPanel を設定する。
     * @param statusPanel StatusPanel オブジェクト
     */
    @Override
    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    /**
     * ChartMediator を返す。
     * @return ChartMediator
     */
    @Override
    public ChartMediator getChartMediator() {
        return mediator;
    }

    /**
     * Menu アクションを制御する。
     * @param name
     */
    @Override
    public void enabledAction(String name, boolean enabled) {
        mediator.enabledAction(name, enabled);
    }

    /**
     * DocumentHistory を返す。
     * @return DocumentHistory
     */
    @Override
    public DocumentHistoryPanel getDocumentHistory() {
        return parentChart.getDocumentHistory();
    }

    /**
     * 引数のタブ番号にあるドキュメントを表示する。
     * @param tabName
     */
    @Override
    public void showDocument(String tabName) {
        parentChart.showDocument(tabName);
    }

    /**
     * dirty かどうかを返す。
     * @return dirty の時 true
     */
    @Override
    public boolean isDirty() {
        return (mode == EditorMode.EDITOR) ? editor.isDirty() : false;
    }

    /**
     * 
     * @return
     */
    @Override
    public PVTHealthInsuranceModel[] getHealthInsurances() {
        return parentChart.getHealthInsurances();
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {
        initialize();
    }

    /**
     *
     * @return
     */
    private String getWindowTitle() {
        String karteStr = resMap.getString("karteStr");
        StringBuilder sb = new StringBuilder();
        sb.append(getPatient().getFullName());
        sb.append("(");
        String kana = getPatient().getKanaName();
        kana = kana.replace("　", " ");
        sb.append(kana);
        sb.append(") : ");
        sb.append(getPatient().getPatientId());
        sb.append(karteStr);
        sb.append(" | ").append(GlobalVariables.getUserId());
        sb.append(" | ").append(GlobalVariables.getUserModel().getLicenseModel().getLicenseDesc());
        return sb.toString();
    }

    /**
     * 初期化する。
     */
    private void initialize() {

        // ResourceMap を保存する
        resMap = GlobalConstants.getResourceMap(EditorFrame.class);
        windowSupport = WindowSupport.create(this, getWindowTitle());
        JMenuBar myMenuBar = windowSupport.getMenuBar();
        setName("editorFrame");

        content = new JPanel(new BorderLayout());
        mediator = new ChartMediator(this);        // Mediator が変更になる

        IMenuAdapter appMenu = new WindowsMenuAdapter();

        appMenu.setMenuSupports(parentChart, parentChart.getContext().getMenuSupport(), mediator, parentChart.getContext().getPlugin());
        appMenu.build(myMenuBar, true);

        mediator.registerActions(appMenu.getActionMap());

        myToolPanel = appMenu.getToolPanelProduct();
        content.add(myToolPanel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar(); // このクラス固有のToolBarを生成する
        myToolPanel.add(toolBar);

        // テキストツールを生成する
        Action action = mediator.getActions().get(GUIConst.ACTION_INSERT_TEXT);
        JButton textBtn = new JButton();
        textBtn.setName("textBtn");
        textBtn.setAction(action);
        textBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                mediator.addTextMenu(popup);
                if (!e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        toolBar.add(textBtn);

        // シェーマツールを生成する
        action = mediator.getActions().get(GUIConst.ACTION_INSERT_SCHEMA);
        JButton schemaBtn = new JButton();
        schemaBtn.setName("schemaBtn");
        schemaBtn.setAction(action);
        schemaBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                getContext().dispatchMainCommand(IMainCommandAccepter.MainCommand.showSchemaBox);
            }
        });
        toolBar.add(schemaBtn);

        // スタンプツールを生成する
        action = mediator.getActions().get(GUIConst.ACTION_INSERT_STAMP);
        JButton stampBtn = new JButton();
        stampBtn.setName("stampBtn");
        stampBtn.setAction(action);
        stampBtn.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                JPopupMenu popup = new JPopupMenu();
                mediator.addStampMenu(popup);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
        toolBar.add(stampBtn);
        /*
        // 保険選択ツールを生成する
        action = mediator.getActions().get(GUIConst.ACTION_SELECT_INSURANCE);
        JButton insBtn = new JButton();
        insBtn.setName("insBtn");
        insBtn.setAction(action);
        insBtn.addMouseListener(new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
        JPopupMenu popup = new JPopupMenu();
        PVTHealthInsuranceModel[] insurances = getHealthInsurances();
        for (final PVTHealthInsuranceModel hm : insurances) {

        ActionListener ra = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        mediator.applyInsurance(hm);
        }
        };

        JMenuItem menuItem = new JMenuItem(hm.toString());
        menuItem.addActionListener(ra);
        popup.add(menuItem);
        }

        popup.show(e.getComponent(), e.getX(), e.getY());
        }
        });
        toolBar.add(insBtn);
         */
        statusPanel = new StatusPanel();

        if (view != null) {
            mode = EditorMode.BROWSER;
            view.start();
            scroller = new JScrollPane((JPanel) view);
            scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
            enabledAction(GUIConst.ACTION_NEW_DOCUMENT, false);
            scroller.getVerticalScrollBar().setUnitIncrement(GlobalSettings.karteScrollUnitIncrement());
        } else if (editor != null) {
            mode = EditorMode.EDITOR;
            editor.initialize();
            editor.start();
            enabledAction(GUIConst.ACTION_NEW_KARTE, false);
            enabledAction(GUIConst.ACTION_NEW_DOCUMENT, false);
            enabledAction(GUIConst.ACTION_SAVE, false);
        }

        content.add(editor, BorderLayout.CENTER);
        getContentPane().setLayout(new BorderLayout(0, 7));
        getContentPane().add(content, BorderLayout.CENTER);
        getContentPane().add((JPanel) statusPanel, BorderLayout.SOUTH);
        resMap.injectComponents(this);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                processWindowOpened();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                processWindowClosing();
            }
        });

        blockGlass = new BlockGlass();
        setGlassPane(blockGlass);

        this.setBounds((Rectangle) Persistent.loadLayout(this.getClass().getName(), "layout", initialBounds()));

        setVisible(true);

        Runnable awt = new Runnable() {

            @Override
            public void run() {
                if (view != null) {
                    ((JPanel) view).scrollRectToVisible(new Rectangle(0, 0, ((JPanel) view).getWidth(), 50));
                } else if (editor != null) {
                    ((JPanel) editor).scrollRectToVisible(new Rectangle(0, 0, ((JPanel) editor).getWidth(), 50));
                }
            }
        };
        EventQueue.invokeLater(awt);
    }

    /**
     * プログラムを終了する。
     */
    @Override
    public void stop() {
        mediator.dispose();
        context.removeEditorFrame(this);
        Persistent.saveLayout(this.getClass().getName(), "layout", this.getBounds());
        setVisible(false);
        dispose();
    }

    /**
     * KarteEditorの保存を呼び出す。
     * @return true
     */
    public boolean save() {
        editor.save();
        return true;
    }

    /**
     * ウインドウの close box が押された時の処理を実行する。
     */
    private void processWindowClosing() {
        close();
    }

    /**
     * ウインドウオープン時の処理を行う。
     */
    private void processWindowOpened() {
        //現在のウィンドウと重ならない所にスタンプ箱を表示すると良い<todo>
        Dolphin.getStampBox().getFrame().setVisible(true);
    }

    /**
     *
     * @return
     */
    private PageFormat getPageFormat() {
        return parentChart.getContext().getPageFormat();
    }

    /**
     * 印刷する。
     */
    private boolean print() {
        switch (mode) {
            case BROWSER:
                if (view != null) {
                    view.printPanel2(getPageFormat());
                }
                break;
            case EDITOR:
                if (editor != null) {
                    editor.printPanel2(getPageFormat());
                }
                break;
            default:
                LogWriter.fatal(getClass(), "case default");
        }
        return true;
    }

    /**
     * クローズする。
     * @return
     */
    @Override
    public boolean close() {

        if (mode == EditorMode.EDITOR) {
            if (editor.isDirty()) {
                String save = resMap.getString("unsavedtask.saveText"); //"保存";
                String discard = resMap.getString("unsavedtask.discardText"); //"破棄";
                String question = resMap.getString("unsavedtask.question"); // 未保存のドキュメントがあります。保存しますか ?
                String title = resMap.getString("unsavedtask.title"); // 未保存処理
                String cancelText = (String) UIManager.get("OptionPane.cancelButtonText");
                int option = JOptionPane.showOptionDialog(
                        this,
                        question,
                        GlobalConstants.getFrameTitle(title),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{save, discard, cancelText},
                        save);
                switch (option) {

                    case 0:
                        editor.dispatchChartCommand(ChartCommand.save);
                        break;
                    case 1:
                        enabledAction(GUIConst.ACTION_MODIFY_KARTE, true);
                        enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
                        enabledAction(GUIConst.ACTION_DIRECTION, false);
                        stop();
                        break;
                    case 2:
                        break;
                    default:
                        LogWriter.fatal(getClass(), "case default");
                }
            } else {
                enabledAction(GUIConst.ACTION_MODIFY_KARTE, true);
                enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
                enabledAction(GUIConst.ACTION_DIRECTION, false);
                stop();
            }
        } else {
            enabledAction(GUIConst.ACTION_MODIFY_KARTE, true);
            enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
            enabledAction(GUIConst.ACTION_DIRECTION, false);
            stop();
        }
        parentChart.enabledAction(GUIConst.ACTION_NEW_KARTE, true);
        parentChart.enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
        return true;
    }

    /**
     *
     * @param label
     * @param document
     */
    @Override
    public void closeChartDocument(String label, IChartDocument document) {
    }

    /**
     *
     * @return
     */
    @Override
    public PlugInMenuSupport getPlugins() {
        return context.getPlugin();
    }
}
