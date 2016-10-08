/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * KarteDocumentViewer.java
 *
 * Created on 2010/03/08, 15:22:02
 */
package open.dolphin.client;

import java.util.TooManyListenersException;
import open.dolphin.project.GlobalConstants;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import open.dolphin.client.karte.DocumentHistoryPanel;
import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.project.GlobalVariables;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.TaskService;
import open.dolphin.helper.IChartCommandAccepter;
import open.dolphin.helper.IMainCommandAccepter;
import open.dolphin.log.LogWriter;

/**
 *　カルテビューア画面　MEMO:画面
 * @author
 */
public class KarteDocumentViewer extends JPanel implements IDocumentViewer, IChartCommandAccepter, IMainCommandAccepter {

    /**
     *
     */
    public static final String BUSY_PROP = "busyProp";    // Busy プロパティ名
    private static final String TITLE_UPDATE = "更新";    // 更新を表す文字 
    private static final String TITLE = "参 照";
    // このアプリケーションは文書履歴を複数選択することができる
    // このリストはそれに対応した KarteViewer1(2号カルテ)を保持している
    // このリストの内容（KarteViewer1)が一枚のパネルに並べて表示される
    private List<IKarteViewer> karteList;
    // 上記パネル内でマウスで選択されているカルテ(karteViewer)
    // 前回処方を適用した新規カルテはこの選択されたカルテが元になる
    private IKarteViewer selectedKarte; // 選択されている karteViewer
    private boolean busy; // busy プリパティ
    private boolean ascending;  // 文書履を昇順で表示する場合に true
    // private boolean showModified; // 文書の修正履歴を表示する場合に true 
    private StateMgr stateMgr;   // このクラスの状態マネージャ 
    // 選択を解除されたカルテのリスト
    private List<IKarteViewer> removed;
    private JPanel scrollerPanel;
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
    /**
     *
     */
    protected Application app;
    /**
     *
     */
    protected ApplicationContext appCtx;
    /**
     *
     */
    protected TaskMonitor taskMonitor;
    /**
     *
     */
    protected TaskService taskService;
    private KarteEditor editor;

    /** Creates new form KarteDocumentViewer
     * @param parent
     */
    public KarteDocumentViewer(IChart parent) {
        super();
        this.parent = parent;
        this.title = TITLE;
        initComponents();
        appCtx = GlobalConstants.getApplicationContext();
        app = appCtx.getApplication();
        taskMonitor = appCtx.getTaskMonitor();
        taskService = appCtx.getTaskService();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.KarteDocumentViewer;
    }

    /**
     * busy かどうかを返す。
     * @return busy の時 true
     */
    public boolean isBusy() {
        return busy;
    }

    /**
     *
     */
    @Override
    public void start() {
        karteList = new ArrayList<IKarteViewer>(1);
        stateMgr = new StateMgr();
        connect();
        enter();
    }

    /**
     *
     */
    @Override
    public void stop() {
        if (karteList != null) {
            for (IKarteViewer karte : karteList) {
                karte.stop();
            }
            karteList.clear();
        }
    }

    /**
     *
     */
    @Override
    public void enter() {

        parent.getStatusPanel().setMessage("");
        parent.getChartMediator().setAccepter(this);
        disableMenus();

        parent.enabledAction(GUIConst.ACTION_NEW_KARTE, true);
        parent.enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
        parent.enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());

        stateMgr.enter();
    }

    /**
     * 選択されているKarteViwerを返す。
     * @return 選択されているKarteViwer
     */
    public IKarteViewer getSelectedKarte() {
        return selectedKarte;
    }

    /**
     * マウスクリック(選択)されたKarteViwerをselectedKarteに設定する。
     * 他のカルテが選択されている場合はそれを解除する。
     * StateMgrを Haskarte State にする。
     * @param view 選択されたKarteViwer
     */
    public void setSelectedKarte(IKarteViewer view) {

        IKarteViewer old = selectedKarte;
        selectedKarte = view;

        // 他のカルテが選択されている場合はそれを解除する
        if (selectedKarte != old) {
            if (selectedKarte != null) {
                for (IKarteViewer karte : karteList) {
                    karte.setSelected(false);
                }
                selectedKarte.setSelected(true);
                stateMgr.processCleanEvent();

            } else {
                // null
                stateMgr.processEmptyEvent();
            }
        }
    }

    /**
     * 新規カルテ作成の元になるカルテを返す。
     * @return 作成の元になるカルテ
     */
    public IKarteViewer getBaseKarte() {
        IKarteViewer ret = getSelectedKarte();
        if (ret == null) {
            if (karteList != null && karteList.size() > 0) {
                ret = ascending ? karteList.get(karteList.size() - 1) : karteList.get(0);
            }
        }
        return ret;
    }

    /**
     * 文書履歴の抽出期間が変更された場合、
     * karteList をclear、選択されているkarteViewerを解除、sateMgrをNoKarte状態に設定する。
     */
    @Override
    public void historyPeriodChanged() {
        if (karteList != null) {
            karteList.clear();
        }
        setSelectedKarte(null);
        parent.showDocument(KarteDocumentViewer.TITLE);
    }

    /**
     * GUIコンポーネントにリスナを設定する。
     *
     */
    private void connect() {

        // 文書履歴に昇順／降順、修正履歴表示の設定をする
        // この値の初期値はデフォル値であり、個々のドキュメント（画面）単位にメニューで変更できる。（適用されるのは個々のドキュメントのみ）
        // デフォルト値の設定は環境設定で行う。
        DocumentHistoryPanel panel = parent.getDocumentHistory();
        ascending = panel.isAscending();

        parent.getDocumentHistory().setShowModified(GlobalVariables.getShowModifiedKarte(), false);
        parent.getDocumentHistory().setShowUnsend(GlobalVariables.getShowUnsendKarte(), false);
        parent.getDocumentHistory().setShowSend(GlobalVariables.getShowSendKarte(), false);
        parent.getDocumentHistory().setShowNewest(GlobalVariables.getShowNewestKarte()); // カルテ(DocumentModel)を表示する

    }

    /**
     * KarteViewerを生成し表示する。
     *
     * @param selectedHistories 選択された文書情報 DocInfo 配列
     * @param scroller
     */
    @Override
    public void showDocuments(DocInfoModel[] selectedHistories, final JScrollPane scroller) {

        if (selectedHistories == null || selectedHistories.length == 0) {
            return;
        }

        // 現在のリストと比較し、新たに追加されたもの、削除されたものに分ける
        List<DocInfoModel> added = new ArrayList<DocInfoModel>(1); // 追加されたもの
        if (removed == null) {
            removed = new ArrayList<IKarteViewer>(1); // 選択が解除されているもの
        } else {
            removed.clear();
        }

        // 追加されたものと選択を解除されたものに分ける

        // 1. 選択リストにあって 現在の karteList にないものは追加する
        for (DocInfoModel selectedDocInfo : selectedHistories) {
            boolean found = false;
            for (IKarteViewer viewer : karteList) {
                if (viewer.getModel().getDocInfo().equals(selectedDocInfo)) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                added.add(selectedDocInfo);
            }
        }

        // 2. karteList にあって選択リストにないものはkarteListから削除する
        for (IKarteViewer viewer : karteList) {
            boolean found = false;
            for (DocInfoModel selectedDocInfo : selectedHistories) {
                if (viewer.getModel().getDocInfo().equals(selectedDocInfo)) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                removed.add(viewer);
            }
        }

        // 解除されたものがあればそれをリストから取り除く
        if (removed != null && removed.size() > 0) {
            for (IKarteViewer karte : removed) {
                karteList.remove(karte);
            }
        }

        // 追加されたものがない場合
        if (added == null || added.isEmpty()) {

            boolean vsc = GlobalVariables.getScrollKarteV();

            if (scrollerPanel != null) {
                scrollerPanel.removeAll();
            }

            scrollerPanel = new JPanel();
            if (vsc) {
                scrollerPanel.setLayout(new BoxLayout(scrollerPanel, BoxLayout.Y_AXIS));
            } else {
                scrollerPanel.setLayout(new BoxLayout(scrollerPanel, BoxLayout.X_AXIS));
            }

            for (IKarteViewer view : karteList) {
                scrollerPanel.add((JPanel) view);
            }

            scroller.setViewportView(scrollerPanel);

            if (vsc) {
                showKarteListV();
            } else {
                showKarteListH();
            }

            return;
        }

        // 取得する文書ID(PK)を生成し
        List<Long> docId = new ArrayList<Long>(added.size());
        for (DocInfoModel bean : added) {
            docId.add(new Long(bean.getDocPk()));
        }

        RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
        KarteTask task = new KarteTask(parent, docId, added, ddl, scroller);
        task.execute();
    }

    private IKarteViewer createKarteViewer(DocInfoModel docInfo) {
        if (docInfo != null && docInfo.getDocType().equals(IInfoModel.DOCTYPE_S_KARTE)) {
            return new KarteViewerSingle(parent);
        }
        return new KarteViewerDouble(parent);
    }

    /**
     * データベースで検索した KarteModelを Viewer で表示する。
     *
     * @param models KarteModel
     * @param docInfos DocInfo
     */
    private void addKarteViewer(List<DocumentModel> models, List<DocInfoModel> docInfos, final JScrollPane scroller) {
        if (models != null) {
            int index = 0;
            for (DocumentModel karteModel : models) {

                karteModel.setDocInfo(docInfos.get(index++)); // ?

                // シングル及び２号用紙の判定を行い、KarteViewer1 を生成する
                final IKarteViewer karteViewer = createKarteViewer(karteModel.getDocInfo());
                //   karteViewer.setContext(getParentContext());
                karteViewer.setModel(karteModel);
                karteViewer.setAvoidEnter(true);

                // このコールでモデルのレンダリングが開始される
                karteViewer.start();

                // 2号カルテの場合ダブルクリックされたカルテを別画面で表示する
                // MouseListener を生成して KarteViewer1 の Pane にアタッチする
                if (karteModel.getDocInfo().getDocType().equals(IInfoModel.DOCTYPE_KARTE)) {
                    final MouseListener ml = new MouseAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int cnt = e.getClickCount();
                            if (cnt == 2) {
                                // 選択した Karte を EditoFrame で開く
                                setSelectedKarte(karteViewer);
                                openKarte();
                            } else if (cnt == 1) {
                                setSelectedKarte(karteViewer);
                            }
                        }
                    };
                    karteViewer.addMouseListener(ml);
                }
                karteList.add(karteViewer);
            }

            // 時間軸でソート、viewへ通知、選択処理をする
            if (ascending) {
                Collections.sort(karteList);
            } else {
                Collections.sort(karteList, Collections.reverseOrder());
            }

            // 選択する
            if (karteList.size() > 0) {
                if (ascending) {
                    setSelectedKarte(karteList.get(karteList.size() - 1));
                } else {
                    setSelectedKarte(karteList.get(0));
                }
            }
        }

        boolean vsc = GlobalVariables.getScrollKarteV();

        if (scrollerPanel != null) {
            scrollerPanel.removeAll();
        }

        scrollerPanel = new JPanel();
        //scrollerPanel.setVisible(false);

        if (vsc) {
            scrollerPanel.setLayout(new BoxLayout(scrollerPanel, BoxLayout.Y_AXIS));
        } else {
            scrollerPanel.setLayout(new BoxLayout(scrollerPanel, BoxLayout.X_AXIS));
        }

        for (IKarteViewer view : karteList) {
            scrollerPanel.add((JPanel) view);
        }

        scroller.setViewportView(scrollerPanel);

        if (vsc) {
            showKarteListV();
        } else {
            showKarteListH();
        }
    }

    private void showKarteListV() {

        Runnable awt = new Runnable() {

            @Override
            public void run() {

                if (karteList.size() > 1) {

                    int totalHeight = 0;

                    for (IKarteViewer view : karteList) {
                        int w = ((JPanel) view).getPreferredSize().width;
                        int h = view.getActualHeight() + 30;
                        totalHeight += h;
                        ((JPanel) view).setPreferredSize(new Dimension(w, h));
                    }

                    int spWidth = scrollerPanel.getPreferredSize().width;
                    scrollerPanel.setPreferredSize(new Dimension(spWidth, totalHeight));
                }

                scrollerPanel.scrollRectToVisible(new Rectangle(0, 0, scrollerPanel.getWidth(), 100));

                // 選択から外れたものは表示しない
                if (removed != null) {
                    for (IKarteViewer karte : removed) {
                        karte.stop();
                    }
                    removed.clear();
                }
            }
        };

        EventQueue.invokeLater(awt);
    }

    private void showKarteListH() {

        Runnable awt = new Runnable() {

            @Override
            public void run() {

                if (karteList.size() > 1) {

                    int maxHeight = 0;

                    for (IKarteViewer view : karteList) {
                        int w = ((JPanel) view).getPreferredSize().width;
                        int h = view.getActualHeight() + 20;
                        maxHeight = maxHeight >= h ? maxHeight : h;
                        ((JPanel) view).setPreferredSize(new Dimension(w, h));
                    }

                    int spWidth = scrollerPanel.getPreferredSize().width;
                    scrollerPanel.setPreferredSize(new Dimension(spWidth, maxHeight));
                }

                scrollerPanel.scrollRectToVisible(new Rectangle(0, 0, scrollerPanel.getWidth(), 100));
                parent.showDocument(KarteDocumentViewer.TITLE);

                if (removed != null) {
                    for (IKarteViewer karte : removed) {
                        karte.stop();
                    }
                    removed.clear();
                }
            }
        };
        EventQueue.invokeLater(awt);
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchMainCommand(IMainCommandAccepter.MainCommand command) {
        switch (command) {
            case openKarte:
                return openKarte();
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
            switch (command) {
                case modifyKarte:
                    return modifyKarte();
                case delete:
                    return delete();
                case print:
                    return print();
                case showModified:
                    return showModified();
                case hideModified:
                    return hideModified();
                case showUnsend:
                    return showUnsend();
                case hideUnsend:
                    return hideUnsend();
                case showSend:
                    return showSend();
                case hideSend:
                    return hideSend();
                case showNewest:
                    return showNewest();
                case hideNewest:
                    return hideNewest();
                case ascending:
                    return ascending();
                case descending:
                    return descending();
                default:
            }
        } catch (TooManyListenersException ex) {
             LogWriter.error(getClass(), ex);
        }
        return false;
    }
    /*
    private boolean pasteMedicationToLetter(String message) {
    LetterView view = ((ChartWindow) parent).getLetterPane();
    if (view != null) {
    // TODO Bundle 側で OrderName 付きのものを用意
    view.getMedication().setText(view.getMedication().getText() + System.getProperty("line.separator") + message);
    }
    return true;
    }

    private boolean pasteLetter() {
    
    //     if (parent instanceof ChartWindow && ((ChartWindow) parent).existLetterPane()) {
    //          JComponent stampHolder = this.editor.getSOAPane().;
    //          if (stampHolder instanceof StampHolder) {
    //              ModuleModel stamp = ((StampHolder) stampHolder).getStamp();
    //             String stampName = stamp.getModuleInfo().getStampName();
    //             BundleDolphin bundle = (BundleDolphin) stamp.getModel();

    //             pasteMedicationToLetter(bundle.toString().replaceFirst(System.getProperty("line.separator"), "(" + stampName + ")" + System.getProperty("line.separator")));
    //         }
    //     }

    return true;
    }
     */

    /**
     * カルテを修正する。
     */
    private boolean modifyKarte() throws TooManyListenersException {

        if (getBaseKarte() == null) {
            return true;
        }

        parent.enabledAction(GUIConst.ACTION_NEW_KARTE, false);

        String docType = getBaseKarte().getModel().getDocInfo().getDocType();
        ChartWindow chart = (ChartWindow) parent;
        String dept = parent.getPatientVisit().getDepartment();
        String deptCode = parent.getPatientVisit().getDepartmentCode();
        Preferences prefs = GlobalVariables.getPreferences();
        NewKarteParams params = new NewKarteParams(IChart.NewKarteOption.BROWSER_MODIFY);
        params.setDocType(docType);
        params.setDepartment(dept);
        params.setDepartmentCode(deptCode);
        // このフラグはカルテを別ウインドウで編集するかどうか
        params.setOpenFrame(prefs.getBoolean(GlobalVariables.KARTE_PLACE_MODE, true));
        DocumentModel editModel = chart.getKarteModelToEdit(getBaseKarte().getModel());
        int mode = docType.equals(IInfoModel.DOCTYPE_KARTE) ? KarteEditor.DOUBLE_MODE : KarteEditor.SINGLE_MODE;

        editor = chart.createEditor(params.isOpenFrame(), editModel, parent, true, mode); //tab update
        editor.setModify(true);

        // Single Karte の場合 EF させない
        if (mode == KarteEditor.SINGLE_MODE) {
            params.setOpenFrame(false);
        }
        if (params.isOpenFrame()) {
            EditorFrame editorFrame = new EditorFrame(parent, editor);
            editorFrame.start();
        } else {
            if (!chart.existEditorPane(KarteEditor.DOUBLE_MODE)) {
                //    editor.setContext(chart);
                editor.initialize();
                editor.start();
                chart.addChartDocument(editor, TITLE_UPDATE);
            }
        }

        return true;
    }

    private boolean print() {
        IKarteViewer view = getSelectedKarte();
        if (view != null) {
            view.dispatchChartCommand(ChartCommand.print);
            //       view.print();
        }
        return true;
    }

    /**
     * 昇順表示にする。
     */
    private boolean ascending() {
        ascending = true;
        parent.getDocumentHistory().setAscending(ascending);
        return true;
    }

    /**
     * 降順表示にする。
     */
    private boolean descending() {
        ascending = false;
        parent.getDocumentHistory().setAscending(ascending);
        return true;
    }

    /**
     * 修正履歴の表示モードにする。
     */
    private boolean showModified() {
        parent.getDocumentHistory().setShowModified(true);
        return true;
    }

    private boolean hideModified() {
        parent.getDocumentHistory().setShowModified(false);
        return true;
    }

    private boolean showUnsend() {
        parent.getDocumentHistory().setShowUnsend(true);
        return true;
    }

    private boolean hideUnsend() {
        parent.getDocumentHistory().setShowUnsend(false);
        return true;
    }

    private boolean showSend() {
        parent.getDocumentHistory().setShowSend(true);
        return true;
    }

    private boolean hideSend() {
        parent.getDocumentHistory().setShowSend(false);
        return true;
    }

    private boolean showNewest() {
        parent.getDocumentHistory().setShowNewest(true);
        return true;
    }

    private boolean hideNewest() {
        parent.getDocumentHistory().setShowNewest(false);
        return true;
    }

    /**
     * karteList 内でダブルクリックされたカルテ（文書）を EditorFrame で開く。
     */
    private boolean openKarte() {

        if (getSelectedKarte() != null) {
            EditorFrame editorFrame = new EditorFrame(parent, null);

            // 表示している文書タイプに応じて Viewer を作成する
            DocumentModel model = getSelectedKarte().getModel();
            String docType = model.getDocInfo().getDocType();

            if (docType.equals(IInfoModel.DOCTYPE_S_KARTE)) {
                KarteViewerSingle view = new KarteViewerSingle(parent);
                view.setModel(model);
                editorFrame.setKarteViewer(view);
                editorFrame.start();
            } else if (docType.equals(IInfoModel.DOCTYPE_KARTE)) {
                KarteViewerDouble view = new KarteViewerDouble(parent);
                view.setModel(model);
                editorFrame.setKarteViewer(view);
                editorFrame.start();
            }
        }
        return true;
    }

    /**
     * 表示選択されているカルテを論理削除する。
     * 患者を間違えた場合等に履歴に表示されないようにするため。
     */
    private boolean delete() {

        // 対象のカルテを得る
        IKarteViewer delete = getBaseKarte();
        if (delete == null) {
            return true;
        }

        // Dialog を表示し理由を求める
        String message = "このドキュメントを削除しますか ?   ";
        final JCheckBox box1 = new JCheckBox("作成ミス");
        final JCheckBox box2 = new JCheckBox("診察キャンセル");
        final JCheckBox box3 = new JCheckBox("その他");
        box1.setSelected(true);

        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (box1.isSelected() || box2.isSelected()) {
                    return;
                } else if (!box3.isSelected()) {
                    box3.setSelected(true);
                }
            }
        };

        box1.addActionListener(al);
        box2.addActionListener(al);
        box3.addActionListener(al);

        Object[] msg = new Object[5];
        msg[0] = message;
        msg[1] = box1;
        msg[2] = box2;
        msg[3] = box3;
        msg[4] = new JLabel(" ");
        String deleteText = "削除する";
        String cancelText = (String) UIManager.get("OptionPane.cancelButtonText");

        int option = JOptionPane.showOptionDialog(
                this,
                msg,
                GlobalConstants.getFrameTitle("ドキュメント削除"),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new String[]{deleteText, cancelText},
                cancelText);

        // キャンセルの場合はリターンする
        if (option != 0) {
            return true;
        }

        //
        // 削除する status = 'D'
        //
        long deletePk = delete.getModel().getId();
        RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
        DeleteTask task = new DeleteTask(parent, deletePk, ddl);
        task.execute();
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean itLayoutSaved() {
        return true;
    }
    //元々はAbstractChartDocumentから継承

    /**
     *
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
        ChartMediator mediator = parent.getChartMediator();
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
     * 文書をデータベースから取得するタスククラス。
     */
    class KarteTask extends DBTask<List<DocumentModel>> {

        private RemoteDocumentDelegater ddl;
        private List<Long> docId;
        private List<DocInfoModel> docInfos;
        private JScrollPane scroller;

        public KarteTask(IChart ctx, List<Long> docId, List<DocInfoModel> docInfos, RemoteDocumentDelegater ddl, JScrollPane scroller) {
            super(ctx);
            this.docId = docId;
            this.ddl = ddl;
            this.docInfos = docInfos;
            this.scroller = scroller;
        }

        @Override
        protected List<DocumentModel> doInBackground() {
            List<DocumentModel> result = ddl.getDocuments(docId);
            if (!ddl.isError()) {
                return result;
            } else {
                return null;
            }
        }

        @Override
        protected void succeeded(List<DocumentModel> list) {
            if (list != null) {
                addKarteViewer(list, docInfos, scroller);
            }
        }
    }

    /**
     * カルテの削除タスククラス。
     */
    class DeleteTask extends DBTask<Boolean> {

        private RemoteDocumentDelegater ddl;
        private long docPk;

        public DeleteTask(IChart ctx, long docPk, RemoteDocumentDelegater ddl) {
            super(ctx);
            this.docPk = docPk;
            this.ddl = ddl;
        }

        @Override
        protected Boolean doInBackground() throws Exception {
            ddl.deleteDocument(docPk);
            return !ddl.isError();
        }

        @Override
        protected void succeeded(Boolean result) {
            if (result.booleanValue()) {
                IChart chart = (KarteDocumentViewer.this).parent;
                chart.getDocumentHistory().getDocumentHistory();
            } else {
                warning("ドキュメント削除", ddl.getErrorMessage()); //warning(GlobalVariables.getString("ドキュメント削除"), ddl.getErrorMessage());
            }
        }
    }

    /**
     * 抽象状態クラス。
     */
    protected abstract class BrowserState {

        /**
         *
         */
        public BrowserState() {
        }

        /**
         *
         */
        public abstract void enter();
    }

    /**
     * 表示するカルテがない状態を表す。
     */
    protected final class EmptyState extends BrowserState {

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
            //    boolean canEdit = isReadOnly() ? false : true;
            //    getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, canEdit);     // 新規カルテ
            parent.enabledAction(GUIConst.ACTION_NEW_KARTE, true);     // 新規カルテ
            //    getParentContext().enabledAction(GUIConst.ACTION_NEW_DOCUMENT, canEdit);  // 新規文書
            parent.enabledAction(GUIConst.ACTION_MODIFY_KARTE, false);    // 修正
            parent.enabledAction(GUIConst.ACTION_DELETE, false);          // 削除
            parent.enabledAction(GUIConst.ACTION_PRINT, false);           // 印刷
            //    getParentContext().enabledAction(GUIConst.ACTION_DIRECTION, false);
            //    parent.enabledAction(GUIConst.ACTION_ASCENDING, false);       // 昇順
            //    parent.enabledAction(GUIConst.ACTION_DESCENDING, false);      // 降順
            //    parent.enabledAction(GUIConst.ACTION_SHOW_MODIFIED, false);   // 修正履歴表示
            //    parent.enabledAction(GUIConst.ACTION_SHOW_UNSEND, false);
            //    parent.enabledAction(GUIConst.ACTION_SHOW_SEND, false);

            parent.enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        }
    }

    /**
     * カルテが表示されている状態を表す。
     */
    protected final class ClaenState extends BrowserState {

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
            boolean tmpKarte = false;
            IKarteViewer base = getBaseKarte();
            if (base != null) {
                String state = base.getModel().getDocInfo().getStatus();
                if (state.equals(IInfoModel.STATUS_TMP)) {
                    tmpKarte = true;
                }
            }
            boolean newOk = canEdit && (!tmpKarte) ? true : false;//MEMO: unused?
            //     getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, newOk);        // 新規カルテ
            parent.enabledAction(GUIConst.ACTION_NEW_KARTE, true);        // 新規カルテ

            parent.enabledAction(GUIConst.ACTION_NEW_DOCUMENT, canEdit);   // 新規文書
            parent.enabledAction(GUIConst.ACTION_MODIFY_KARTE, canEdit);   // 修正
            parent.enabledAction(GUIConst.ACTION_DELETE, canEdit);         // 削除
            parent.enabledAction(GUIConst.ACTION_PRINT, true);             // 印刷
            //   getParentContext().enabledAction(GUIConst.ACTION_DIRECTION, true);
            parent.enabledAction(GUIConst.ACTION_ASCENDING, true);         // 昇順
            parent.enabledAction(GUIConst.ACTION_DESCENDING, true);        // 降順
            parent.enabledAction(GUIConst.ACTION_SHOW_MODIFIED, true);     // 修正履歴表示
            parent.enabledAction(GUIConst.ACTION_SHOW_UNSEND, true);
            parent.enabledAction(GUIConst.ACTION_SHOW_SEND, true);
            parent.enabledAction(GUIConst.ACTION_SHOW_NEWEST, true);
        }
    }

    /**
     * StateContext クラス。
     */
    protected final class StateMgr {

        private BrowserState emptyState = new EmptyState();
        private BrowserState cleanState = new ClaenState();
        private BrowserState currentState;

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
