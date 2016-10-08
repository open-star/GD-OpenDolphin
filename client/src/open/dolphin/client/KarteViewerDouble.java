package open.dolphin.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import javax.swing.text.BadLocationException;
import open.dolphin.helper.KarteHeader;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.project.GlobalVariables;
import open.dolphin.helper.IChartCommandAccepter;
import open.dolphin.log.LogWriter;

/**
 * 2号カルテクラス　MEMO:画面
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class KarteViewerDouble extends KartePanelDouble implements Comparable, IChartDocument, IKarteViewer {

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
    // 選択されている時のボーダ色
    private static final Color SELECTED_COLOR = new Color(255, 0, 153);
    // 選択された状態のボーダ
    private static final Border SELECTED_BORDER = BorderFactory.createLineBorder(SELECTED_COLOR);
    // 選択されていない時のボーダ色
    private static final Color NOT_SELECTED_COLOR = new Color(227, 250, 207);
    // 選択されていない状態のボーダ
    private static final Border NOT_SELECTED_BORDER = BorderFactory.createLineBorder(NOT_SELECTED_COLOR);
    // タイムスタンプの foreground カラー
    private static final Color TIMESTAMP_FORE = Color.BLUE;
    // タイムスタンプのフォントサイズ
    private static final int TIMESTAMP_FONT_SIZE = 14;
    // タイムスタンプフォント
    private static final Font TIMESTAMP_FONT = new Font("Dialog", Font.PLAIN, TIMESTAMP_FONT_SIZE);
    // タイムスタンプパネル FlowLayout のマージン
    private static final int TIMESTAMP_SPACING = 7;
    // 仮保存中のドキュメントを表す文字
    private String UNDER_TMP_SAVE = " - 仮保存中";//MEMO: unused?
    //   protected static final String UNDER_TMP_SAVE = " - 未送信";
    //
    // インスタンス変数
    //
    // この view のモデル
    private DocumentModel model;
    // タイムスタンプラベル
    private JLabel timeStampLabel;
    // SOA Pane
    private KartePane soaPane;
    //  protected PrintablePanel kartePanel;
    // タイムスタンプの foreground カラー
    private Color timeStampFore = TIMESTAMP_FORE;//MEMO: unused?
    // タイムスタンプのフォント
    private Font timeStampFont = TIMESTAMP_FONT;//MEMO: unused?
    private int timeStampSpacing = TIMESTAMP_SPACING;//MEMO: unused?
    private boolean avoidEnter;
    // 選択されているかどうかのフラグ
    private boolean selected;
    private int mode;//MEMO: unused?
    private KartePane pPane;    // P Pane

    /**
     * Creates new KarteViewer1
     * @param parent 
     */
    public KarteViewerDouble(IChart parent) {
        super(false);
        this.parent = parent;
        UNDER_TMP_SAVE = " - 未送信";
    }

    /**
     * 
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.KarteViewerDouble;
    }

    /**
     * 
     * @return
     */
    @Override
    public int getActualHeight() {
        try {
            JTextPane pane = soaPane.getTextPane();
            int pos = pane.getDocument().getLength();
            Rectangle r = pane.modelToView(pos);
            int hsoa = r.y;

            pane = pPane.getTextPane();
            pos = pane.getDocument().getLength();
            r = pane.modelToView(pos);
            int hp = r.y;
            return Math.max(hsoa, hp);

        } catch (BadLocationException ex) {
            LogWriter.error(getClass(), ex);
        }
        return 0;
    }

    /**
     *
     */
    public void adjustSize() {
        int h = getActualHeight();
        int soaWidth = soaPane.getTextPane().getPreferredSize().width;
        int pWidth = pPane.getTextPane().getPreferredSize().width;
        soaPane.getTextPane().setPreferredSize(new Dimension(soaWidth, h));
        pPane.getTextPane().setPreferredSize(new Dimension(pWidth, h));
    }

    /**
     * P Pane を返す。
     * @return pPane
     */
    public KartePane getPPane() {
        return pPane;
    }

    /**
     * ２号カルテで初期化する。
     */
    public void initialize() {

        timeStampLabel = getTimeStampLabel();
        // SOA Pane を生成する
        soaPane = new KartePane(this);
        soaPane.setTextPane(getSoaTextPane());
        soaPane.setRole(IInfoModel.ROLE_SOA);
        if (model != null) {
            // Schema 画像にファイル名を付けるのために必要
            String docId = model.getDocInfo().getDocId();
            soaPane.setDocId(docId);
        }
        // P Pane を生成する
        pPane = new KartePane(this);
        pPane.setTextPane(getPTextPane());
        pPane.setRole(IInfoModel.ROLE_P);
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {
        initialize();
        // Model を表示する
        if (getModel() != null) {
            // 確定日を分かりやすい表現に変える
            //     String timeStamp = ModelUtils.getDateAsFormatString(model.getDocInfo().getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT);
            //     timeStamp += " ";
            ////     timeStamp += model.getCreator().getCommonName();
            //     timeStamp += " ";
            //     timeStamp += model.getCreator().getDepartmentModel().getDepartmentDesc();
            //      timeStamp += " ";
            //      timeStamp += model.getCreator().getLicenseModel().getLicenseDesc();

            //    if (model.getDocInfo().getStatus().equals(IInfoModel.STATUS_TMP)) {
            //         timeStamp += UNDER_TMP_SAVE;
            //     }
            KarteHeader header = new KarteHeader(model, "参照");
            String timeStamp = header.toString();
            /*
            StringBuilder buf = new StringBuilder();
            buf.append("参照");
            buf.append(": ");
            buf.append(ModelUtils.getDateAsFormatString(new Date(), IInfoModel.KARTE_DATE_FORMAT));
            buf.append(" [");
            buf.append(ModelUtils.getDateAsFormatString(model.getDocInfo().getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT));
            buf.append(" ]");

            buf.append(" ");
            buf.append(model.getCreator().getCommonName());
            buf.append(" ");
            buf.append(model.getCreator().getDepartmentModel().getDepartmentDesc());
            buf.append(" ");
            buf.append(model.getCreator().getLicenseModel().getLicenseDesc());
            if (model.getDocInfo().getStatus().equals(IInfoModel.STATUS_TMP)) {
            buf.append(" - 仮保存中");
            }
            String timeStamp = buf.toString();
             */

            timeStampLabel.setText(timeStamp);

            KarteRenderer renderer = new KarteRenderer(soaPane, pPane);
            renderer.render(model);
        }

        // モデル表示後にリスナ等を設定する
        ChartMediator mediator = getParentContext().getChartMediator();
        soaPane.init(false, mediator);
        pPane.init(false, mediator);
        enter();
    }

    /**
     *
     */
    @Override
    public void stop() {
        soaPane.clear();
        pPane.clear();
    }

    /**
     * 
     * @param ml
     */
    @Override
    public void addMouseListener(MouseListener ml) {
        soaPane.getTextPane().addMouseListener(ml);
        pPane.getTextPane().addMouseListener(ml);
    }

    /**
     * 
     * @return
     */
    @Override
    public String getDocType() {
        if (model != null) {
            String docType = model.getDocInfo().getDocType();
            return docType;
        }
        return null;
    }

    /**
     * 
     * @param b
     */
    @Override
    public void setAvoidEnter(boolean b) {
        avoidEnter = b;
    }

    /**
     * 
     * @param format
     */
    @Override
    public void printPanel2(final PageFormat format) {
        String name = getParentContext().getPatient().getFullName();
        //  GlobalVariablesImplement grobalVariables = GlobalVariables.getGlobalVariables();
        //    PrintService service = tableModel.findPrinter(assignment, clusterIndex);
        //    List<ModuleModel> modules = tableModel.getModules(assignment);

        // PrintKarte print = new PrintKarte(service.createPrintJob(), assignment, modules, grobalVariables, description, 60, 60);


        printPanel(format, 1, false, name, getActualHeight() + 30);
    }

    /**
     * 
     * @param format
     * @param copies
     * @param useDialog
     */
    @Override
    public void printPanel2(final PageFormat format, final int copies, final boolean useDialog) {
        String name = getParentContext().getPatient().getFullName();
        printPanel(format, copies, useDialog, name, getActualHeight() + 30);
    }

    /**
     * 
     * @return
     */
    private boolean save() {
        return false;
    }

    /**
     * 
     * @return
     */
    private boolean print() {
        //command
        PageFormat pageFormat = getParentContext().getContext().getPageFormat();
        this.printPanel2(pageFormat);
        return true;
    }

    /**
     * 
     * @return
     */
    private boolean direction() {
        return false;
    }

    /**
     * SOA Pane を返す。
     * @return soaPane
     */
    public KartePane getSOAPane() {
        return soaPane;
    }

    /**
     * コンテナからコールされる enter() メソッドで
     * メニューを制御する。
     */
    @Override
    public void enter() {
        if (!avoidEnter) {
            parent.getStatusPanel().setMessage("");
            getParentContext().getChartMediator().setAccepter(this);
            disableMenus();
            getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, true);
            getParentContext().enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
            getParentContext().enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());

            boolean canEdit = getParentContext().isReadOnly() ? false : true;       // ReadOnly 属性
            boolean tmp = model.getDocInfo().getStatus().equals(IInfoModel.STATUS_TMP) ? true : false;     // 仮保存かどうか
            boolean newOk = canEdit && (!tmp) ? true : false;    // 新規カルテ作成が可能な条件 MEMO: unused?

            ChartMediator mediator = getParentContext().getChartMediator();
            mediator.enabledAction(GUIConst.ACTION_NEW_KARTE, true);
            mediator.enabledAction(GUIConst.ACTION_PRINT, true);             // 印刷
            mediator.enabledAction(GUIConst.ACTION_MODIFY_KARTE, canEdit);   // 修正
        }
    }

    /**
     * 表示するモデルを設定する。
     * @param model 表示するDocumentModel
     */
    @Override
    public void setModel(DocumentModel model) {
        this.model = model;
    }

    /**
     * 表示するモデルを返す。
     * @return 表示するDocumentModel
     */
    @Override
    public DocumentModel getModel() {
        return model;
    }

    /**
     * 選択状態を設定する。
     * 選択状態によりViewのボーダの色を変える。
     * @param selected 選択された時 true
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            setBorder(SELECTED_BORDER);
        } else {
            setBorder(NOT_SELECTED_BORDER);
        }
    }

    /**
     * 選択されているかどうかを返す。
     * @return 選択されている時 true
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * 
     * @return
     */
    @Override
    public int hashCode() {
        return getModel().getDocInfo().getDocId().hashCode() + 72;
    }

    /**
     * 
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (other != null && other.getClass() == this.getClass()) {
            DocInfoModel otheInfo = ((IKarteViewer) other).getModel().getDocInfo();
            return getModel().getDocInfo().equals(otheInfo);
        }
        return false;
    }

    /**
     * 
     * @param other
     * @return
     */
    @Override
    public int compareTo(Object other) {
        if (other != null && other.getClass() == this.getClass()) {
            DocInfoModel otheInfo = ((IKarteViewer) other).getModel().getDocInfo();
            return getModel().getDocInfo().compareTo(otheInfo);
        }
        return -1;
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
            case direction:
                return direction();
            case print:
                return print();
            default:
        }
        return false;
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
     */
    private void disableMenus() {
        // このウインドウに関連する全てのメニューをdisableにする
        ChartMediator mediator = getParentContext().getChartMediator();
        mediator.disableMenus(CHART_MENUS);
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
}
