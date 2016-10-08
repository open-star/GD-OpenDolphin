package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.print.PageFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.TouTouReply;
import open.dolphin.project.GlobalVariables;
import open.dolphin.helper.IChartCommandAccepter;

/**
 * 文書履歴で選択された紹介状を表示するクラス　MEMO:画面
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class LetterReplyViewer extends PrintablePanel implements IDocumentViewer, IChartDocument, IChartCommandAccepter {//, IMainCommandAccepter

    private StateMgr stateMgr;
    private LetterReplyView view;
    private TouTouReply model;
    //元々はAbstractChartDocumentから継承
    private static final String[] CHART_MENUS = {
        GUIConst.ACTION_OPEN_KARTE, GUIConst.ACTION_SAVE, GUIConst.ACTION_DIRECTION, GUIConst.ACTION_DELETE, GUIConst.ACTION_PRINT, GUIConst.ACTION_MODIFY_KARTE,
        GUIConst.ACTION_ASCENDING, GUIConst.ACTION_DESCENDING, GUIConst.ACTION_SHOW_MODIFIED, GUIConst.ACTION_SHOW_UNSEND, GUIConst.ACTION_SHOW_SEND, GUIConst.ACTION_SHOW_NEWEST,
        GUIConst.ACTION_INSERT_TEXT, GUIConst.ACTION_INSERT_SCHEMA, GUIConst.ACTION_INSERT_STAMP, GUIConst.ACTION_SELECT_INSURANCE,
        GUIConst.ACTION_CUT, GUIConst.ACTION_COPY, GUIConst.ACTION_PASTE, GUIConst.ACTION_UNDO, GUIConst.ACTION_REDO
    };
    private IChart parent;
    private String title;
    private boolean dirty;
    //  private Application app;
    //  private ApplicationContext appCtx;
    //   private TaskMonitor taskMonitor;
    //   private TaskService taskService;

    /**
     * 
     * @param parent
     */
    public LetterReplyViewer(IChart parent) {
        this.parent = parent;
        //   appCtx = GlobalConstants.getApplicationContext();
        //   app = appCtx.getApplication();
        //   taskMonitor = appCtx.getTaskMonitor();
        //    taskService = appCtx.getTaskService();

    }

    /**
     * 
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.LetterReplyViewer;
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchChartCommand(open.dolphin.helper.IChartCommandAccepter.ChartCommand command) {
        switch (command) {
            case save:
                return save();
            case print:
                return print();
            default:
        }
        return false;
    }

    /**
     * 
     */
    @Override
    public void start() {
        stateMgr = new StateMgr();
        this.enter();
    }

    /**
     * 
     */
    @Override
    public void stop() {
    }

    /**
     *
     */
    @Override
    public void enter() {

        parent.getStatusPanel().setMessage("");
        getParentContext().getChartMediator().setAccepter(this);
        disableMenus();

        getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, true);
        getParentContext().enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
        getParentContext().enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        stateMgr.enter();
    }

    /**
     * 
     * @return
     */
    private boolean print() {

        if (this.model == null) {
            return true;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("PDFファイルを作成しますか?");

        int option = JOptionPane.showOptionDialog(
                getParentContext().getFrame(),
                sb.toString(),
                GlobalConstants.getFrameTitle("紹介患者経過報告印刷"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"PDF作成", "フォーム印刷", "取消し"},
                "PDF作成");

        if (option == 0) {
            makePDF();
        } else if (option == 1) {
            PageFormat pageFormat = getParentContext().getContext().getPageFormat();
            String name = getParentContext().getPatient().getFullName();
            PrintablePanel panel = (PrintablePanel) this.view;
            panel.printPanel(pageFormat, 1, false, name, 0);
        }
        return true;
    }

    /**
     * 
     */
    public void makePDF() {

        if (this.model == null) {
            return;
        }

        Runnable r = new Runnable() {

            @Override
            public void run() {

                PDFReplyMaker pdf = new PDFReplyMaker();
                String pdfDir = GlobalVariables.getPreferences().get("pdfStore", System.getProperty("user.dir"));
                pdf.setDocumentDir(pdfDir);
                pdf.setModel(model);
                final boolean result = pdf.create();
                final String fileName = pdf.getFileName();
                final String dir = pdf.getDocumentDir();

                Runnable awt = new Runnable() {

                    @Override
                    public void run() {
                        if (result) {
                            StringBuilder sb = new StringBuilder();
                            //String fileName = pdf.getFileName();
                            //String dir = pdf.getDocumentDir();
                            sb.append(fileName);
                            sb.append("を");
                            sb.append(System.getProperty("line.separator"));
                            sb.append(dir);
                            sb.append("に保存しました。");
                            sb.append(System.getProperty("line.separator"));
                            sb.append("PDF ビュワーを起動し印刷してください。");
                            JOptionPane.showMessageDialog(
                                    getParentContext().getFrame(),
                                    sb.toString(),
                                    GlobalConstants.getFrameTitle("紹介状作成"),
                                    JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            JOptionPane.showMessageDialog(
                                    getParentContext().getFrame(),
                                    "紹介状PDFファイルを生成することができません。",
                                    GlobalConstants.getFrameTitle("紹介状作成"),
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                };
                EventQueue.invokeLater(awt);
            }
        };
        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }

    /**
     * 
     */
    @Override
    public void historyPeriodChanged() {
        stateMgr.processEmptyEvent();
    }

    /**
     * 
     * @param docs
     * @param scroller
     */
    @Override
    public void showDocuments(DocInfoModel[] docs, JScrollPane scroller) {

        if (docs == null || docs.length == 0) {
            stateMgr.processEmptyEvent();
            return;
        }

        DocInfoModel docInfo = docs[0];
        long pk = docInfo.getDocPk();

        if (pk == 0L) {
            return;
        }

        LetterGetTask task = new LetterGetTask(getParentContext(), pk, scroller);

        task.execute();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean itLayoutSaved() {
        return true;
    }

    /**
     * 元々はAbstractChartDocumentから継承
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    @Override
    public IChart getParentContext() {
        return parent;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean prepare() {
        return true;
    }

    /**
     *
     * @return
     */
    private boolean save() {
        //command
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    /**
     *
     * @param dirty
     */
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     *
     * @return
     */
    public boolean isReadOnly() {
        return parent.isReadOnly();
    }

    /**
     *
     */
    public void disableMenus() {
        // このウインドウに関連する全てのメニューをdisableにする
        ChartMediator mediator = getParentContext().getChartMediator();
        mediator.disableMenus(CHART_MENUS);
    }

    /**
     * 共通の警告表示を行う。
     * @param title
     * @param message
     */
    protected void warning(String title, String message) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        JOptionPane.showMessageDialog(parent, message, GlobalConstants.getFrameTitle(title), JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @return
     */
    @Override
    public List<JTabbedPane> getTabbedPanels() {
        return null;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean update(Object o) {
        return true;
    }

    /**
     *
     */
    class LetterGetTask extends DBTask<LetterModel> {

        private long letterPk;
        private JScrollPane scroller;

        /**
         *
         * @param app
         * @param letterPk
         * @param scroller
         */
        public LetterGetTask(IChart app, long letterPk, JScrollPane scroller) {
            super(app);
            this.letterPk = letterPk;
            this.scroller = scroller;
        }

        /**
         *
         * @return
         * @throws Exception
         */
        @Override
        protected LetterModel doInBackground() throws Exception {

            RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
            LetterModel letter = ddl.getLetterReply(letterPk);

            if (!ddl.isError()) {
                return letter;
            } else {
                return null;
            }
        }

        /**
         *
         * @param letter
         */
        @Override
        protected void succeeded(LetterModel letter) {

            if (letter != null) {
                model = (TouTouReply) letter;
                view = new LetterReplyView();
                view.getConfirmed().setText(ModelUtils.getDateAsString(model.getConfirmed()));
                view.getClientHospital().setText(model.getClientHospital());
                view.getClientDept().setText(model.getClientDept());
                view.getClientDoctor().setText(model.getClientDoctor());
                view.getVisited().setText(model.getVisited());

                view.getPatientName().setText(model.getPatientName());
                view.getPatientBirthday().setText(model.getPatientBirthday());

                view.getInformedContent().setText(model.getInformedContent());

                view.getClientHospital().setEditable(false);
                view.getClientDept().setEditable(false);
                view.getClientDoctor().setEditable(false);
                view.getVisited().setEditable(false);
                view.getInformedContent().setEditable(false);
                scroller.setViewportView(view);
                stateMgr.processCleanEvent();
            }
        }
    }

    /**
     * 抽象状態クラス。
     */
    protected abstract class LetterState {

        /**
         *
         */
        public LetterState() {
        }

        /**
         *
         */
        public abstract void enter();
    }

    /**
     * 表示するカルテがない状態を表す。
     */
    protected final class EmptyState extends LetterState {

        /**
         *
         */
        public EmptyState() {
        }

        /**
         *
         */
        @Override
        public void enter() {
            boolean canEdit = isReadOnly() ? false : true;//MEMO: unused?

            //      getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, canEdit);     // 新規カルテ
            //      getParentContext().enabledAction(GUIConst.ACTION_NEW_DOCUMENT, canEdit);  // 新規文書
            getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, true);     // 新規カルテ
            getParentContext().enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);  // 新規文書

            getParentContext().enabledAction(GUIConst.ACTION_MODIFY_KARTE, false);    // 修正
            getParentContext().enabledAction(GUIConst.ACTION_DELETE, false);          // 削除
            getParentContext().enabledAction(GUIConst.ACTION_PRINT, false);           // 印刷
            getParentContext().enabledAction(GUIConst.ACTION_DIRECTION, false);
            getParentContext().enabledAction(GUIConst.ACTION_ASCENDING, false);       // 昇順
            getParentContext().enabledAction(GUIConst.ACTION_DESCENDING, false);      // 降順
            getParentContext().enabledAction(GUIConst.ACTION_SHOW_MODIFIED, false);   // 修正履歴表示
            getParentContext().enabledAction(GUIConst.ACTION_SHOW_UNSEND, false);
            getParentContext().enabledAction(GUIConst.ACTION_SHOW_SEND, false);
            getParentContext().enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        }
    }

    /**
     * カルテが表示されている状態を表す。
     */
    protected final class ClaenState extends LetterState {

        /**
         *
         */
        public ClaenState() {
        }

        /**
         *
         */
        @Override
        public void enter() {

            //
            // 新規カルテが可能なケース 仮保存でないことを追加
            //
            boolean canEdit = isReadOnly() ? false : true;
            getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, canEdit);      // 新規カルテ
            getParentContext().enabledAction(GUIConst.ACTION_NEW_DOCUMENT, canEdit);   // 新規文書
            getParentContext().enabledAction(GUIConst.ACTION_MODIFY_KARTE, false);     // 修正
            getParentContext().enabledAction(GUIConst.ACTION_DELETE, false);           // 削除
            getParentContext().enabledAction(GUIConst.ACTION_PRINT, true);             // 印刷
            getParentContext().enabledAction(GUIConst.ACTION_DIRECTION, true);
            getParentContext().enabledAction(GUIConst.ACTION_ASCENDING, false);        // 昇順
            getParentContext().enabledAction(GUIConst.ACTION_DESCENDING, false);       // 降順
            getParentContext().enabledAction(GUIConst.ACTION_SHOW_MODIFIED, false);    // 修正履歴表示
            getParentContext().enabledAction(GUIConst.ACTION_SHOW_UNSEND, false);
            getParentContext().enabledAction(GUIConst.ACTION_SHOW_SEND, false);
            getParentContext().enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        }
    }

    /**
     * StateContext クラス。
     */
    protected final class StateMgr {

        private LetterState emptyState = new EmptyState();
        private LetterState cleanState = new ClaenState();
        private LetterState currentState;

        /**
         *
         */
        public StateMgr() {
            currentState = emptyState;
        }

        /**
         *
         */
        public void processEmptyEvent() {
            currentState = emptyState;
            this.enter();
        }

        /**
         *
         */
        public void processCleanEvent() {
            currentState = cleanState;
            this.enter();
        }

        /**
         *
         */
        public void enter() {
            currentState.enter();
        }
    }
}
