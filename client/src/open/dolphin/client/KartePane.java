package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import open.dolphin.client.editor.stamp.StampHolderTransferHandler;
import open.dolphin.client.editor.stamp.StampListTransferable;
import open.dolphin.client.editor.stamp.StampHolder;
import open.dolphin.client.editor.stamp.StampEditorDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledEditorKit;

import open.dolphin.dao.SqlOrcaSetDao;
import open.dolphin.delegater.remote.RemoteStampDelegater;
import open.dolphin.infomodel.ExtRefModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.infomodel.StampModel;

import open.dolphin.client.ChartMediator.CompState;
import open.dolphin.helper.DBTask;
import open.dolphin.helper.ImageHelper;

import open.dolphin.client.definition.FontSettings;
import open.dolphin.client.schemaeditor.SchemaEditorImpl;
import open.dolphin.container.Pair;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.ClaimBundle.Enabled;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.InteractEntry;
import open.dolphin.infomodel.MedicineEntry;
import open.dolphin.infomodel.SsKijyoEntry;
import open.dolphin.log.LogWriter;

/**
 * カルテ描画領域　MEMO:リスナー
 *
 * @author Kazushi Minagawa, Digital Globe, inc.
 */
public class KartePane implements DocumentListener, MouseListener, CaretListener, PropertyChangeListener, IKarteComposite {

    private static final int TITLE_LENGTH = 15;                       // 文書に付けるタイトルを自動で取得する時の長さ
    private static final Color UNEDITABLE_COLOR = new Color(227, 250, 207); // 編集不可時の背景色
    private JTextPane textPane;                                       // マルチフォントに対応したテキスト編集用コンポーネント
    private String myRole;                                            // SOA または P のロール
    private IChartDocument parent;                                    // この KartePaneのオーナ
    private StampHolderTransferHandler stampHolderTransferHandler;    // StampHolderのTransferHandler
    private SchemaHolderTransferHandler schemaHolderTransferHandler;  // SchemaHolderのTransferHandler
    private int stampId;
    private boolean dirty;                                            // Dirty Flag
    private boolean hasSelection;                                     // Selection Flag
    private CompState curState;
    private int initialLength;                                        // 初期化された時のDocumentの長さ
    private ChartMediator mediator;                                   // ChartMediator(MenuSupport)
    private String docId;                                             // このオブジェクトで生成する文書DocumentModelの文書ID
    private Color uneditableColor = UNEDITABLE_COLOR;                 // 保存後及びブラウズ時の編集不可を表すカラー
    private IComponentHolder[] drragedStamp;                          // このペインからDragg及びDroppされたスタンプの情報
    private int draggedCount;
    private int droppedCount;

    /** 
     * Creates new KartePane2 
     * @param parent 
     */
    public KartePane(IChartDocument parent) {
        this.parent = parent;
    }

    /**
     *
     * @param margin
     */
    public void setMargin(Insets margin) {
        textPane.setMargin(margin);
    }

    /**
     *
     * @param size
     */
    public void setPreferredSize(Dimension size) {
        textPane.setPreferredSize(size);
    }

    /**
     * 
     * @param size
     */
    public void setSize(Dimension size) {
        textPane.setMinimumSize(size);
        textPane.setMaximumSize(size);
    }

    /**
     * このPaneのオーナを返す。
     * @return KarteEditorオーナ
     */
    public IChartDocument getParent() {
        return parent;
    }

    /**
     * 編集不可を表すカラーを設定する。
     * @param uneditableColor 編集不可を表すカラー
     */
    public void setUneditableColor(Color uneditableColor) {
        this.uneditableColor = uneditableColor;
    }

    /**
     * 編集不可を表すカラーを返す。
     * @return 編集不可を表すカラー
     */
    public Color getUneditableColor() {
        return uneditableColor;
    }

    /**
     * このPaneで生成するDocumentModelの文書IDを設定する。
     * @param docId 文書ID
     */
    protected void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * このPaneで生成するDocumentModelの文書IDを返す。
     * @return 文書ID
     */
    protected String getDocId() {
        return docId;
    }

    /**
     * ChartMediatorを設定する。
     * @param mediator ChartMediator
     */
    protected void setMediator(ChartMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * ChartMediatorを返す。
     * @return ChartMediator
     */
    public ChartMediator getMediator() {
        return mediator;
    }

    /**
     * このPaneのロールを設定する。
     * @param myRole SOAまたはPのロール
     */
    public void setMyRole(String myRole) {
        this.myRole = myRole;
    }

    /**
     *  このPaneのロールを返す。
     * @return SOAまたはPのロール
     */
    public String getMyRole() {
        return myRole;
    }

    /**
     * JTextPane(マルチフォントに対応したテキスト編集用コンポーネント)を設定する。
     * @param textPane マルチフォントに対応したテキスト編集用コンポーネント
     */
    public void setTextPane(JTextPane textPane) {
        this.textPane = textPane;
        if (this.textPane != null) {
            KarteStyledDocument doc = new KarteStyledDocument();
            this.textPane.setDocument(doc);
            this.textPane.putClientProperty("kartePane", this);
            this.textPane.setFont(FontSettings.getKarteDefaultFont());
            doc.setParent(this);
            stampHolderTransferHandler = new StampHolderTransferHandler();
            schemaHolderTransferHandler = new SchemaHolderTransferHandler();
        } else {
            LogWriter.error(this.getClass(), "setTextPane(JTextPane textPane) textPane is null");
        }
    }

    /**
     * JTextPane(マルチフォントに対応したテキスト編集用コンポーネント)を返す。
     * @return マルチフォントに対応したテキスト編集用コンポーネント
     */
    public JTextPane getTextPane() {
        return textPane;
    }

    /**
     * JTextPaneのStyledDocumentを返す。
     * @return JTextPaneのStyledDocument
     */
    protected KarteStyledDocument getDocument() {
        return (KarteStyledDocument) getTextPane().getDocument();
    }

    /**
     * 初期長を設定する。
     * @param initialLength
     */
    public void setInitialLength(int initialLength) {
        this.initialLength = initialLength;
    }

    /**
     * 初期長を返す。
     * @return Documentの初期長
     */
    public int getInitialLength() {
        return initialLength;
    }

    /**
     * このPaneからDragされたスタンプ数を返す。
     * @return このPaneからDragされたスタンプ数
     */
    public int getDraggedCount() {
        return draggedCount;
    }

    /**
     * このPaneからDragされたスタンプ数を設定する。
     * @param draggedCount このPaneからDragされたスタンプ数
     */
    public void setDraggedCount(int draggedCount) {
        this.draggedCount = draggedCount;
    }

    /**
     * このPaneにDropされたスタンプ数を返す。
     * @return このPaneにDropされたスタンプ数
     */
    public int getDroppedCount() {
        return droppedCount;
    }

    /**
     * このPaneにDropされたスタンプ数を設定する。
     * @param droppedCount このPaneにDropされたスタンプ数
     */
    public void setDroppedCount(int droppedCount) {
        this.droppedCount = droppedCount;
    }

    /**
     * このPaneからDragされたスタンプを返す。
     * @return このPaneからDragされたスタンプ配列
     */
    public IComponentHolder[] getDrragedStamp() {
        return drragedStamp;
    }

    /**
     * このPaneからDragされたスタンプを設定（記録）する。
     * @param drragedStamp このPaneからDragされたスタンプ配列
     */
    public void setDrragedStamp(IComponentHolder[] drragedStamp) {
        this.drragedStamp = drragedStamp;
    }

    /**
     * 初期化する。
     * @param editable 編集可能かどうかのフラグ
     * @param mediator チャートメディエータ（実際にはメニューサポート）
     */
    public void init(boolean editable, ChartMediator mediator) {

        // Mediatorを保存する
        setMediator(mediator);

        // JTextPaneへアクションを登録する
        // Undo & Redo
        ActionMap map = getTextPane().getActionMap();
        KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        map.put(keystroke, mediator.getAction(GUIConst.ACTION_UNDO));
        keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        map.put(keystroke, mediator.getAction(GUIConst.ACTION_REDO));
        keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        map.put(keystroke, mediator.getAction(GUIConst.ACTION_CUT));
        keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        map.put(keystroke, mediator.getAction(GUIConst.ACTION_COPY));
        keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        map.put(keystroke, mediator.getAction(GUIConst.ACTION_PASTE));

        // Drag は editable に関係なく可能
        getTextPane().setDragEnabled(true);

        // リスナを登録する
        getTextPane().addMouseListener(this);
        getTextPane().addCaretListener(this);

        // Editable Property を設定する
        setEditableProp(editable);
    }

    /**
     * 
     * @param editable
     */
    public void setEditableProp(boolean editable) {
        getTextPane().setEditable(editable);
        if (editable) {
            getTextPane().getDocument().addDocumentListener(this);
            getTextPane().addFocusListener(AutoKanjiListener.getInstance());

            getTextPane().getDocument().addUndoableEditListener(mediator);
            if (myRole.equals(IInfoModel.ROLE_SOA)) {
                SOACodeHelper helper = new SOACodeHelper(this, getMediator());//MEMO: unused?
            } else {
                PCodeHelper helper = new PCodeHelper(this, getMediator());//MEMO: unused?
            }
            getTextPane().setBackground(Color.WHITE);
            getTextPane().setOpaque(true);
        } else {
            getTextPane().getDocument().removeDocumentListener(this);

            getTextPane().removeFocusListener(AutoKanjiListener.getInstance());

            getTextPane().getDocument().removeUndoableEditListener(mediator);
            setBackgroundUneditable();
        }
    }

    /**
     * JTextPaneへの挿入でdirtyかどうかを判定する
     * @param e
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        boolean newDirty = getDocument().getLength() > getInitialLength() ? true : false;
        setDirty(newDirty);
    }

    /**
     * 削除が起こった時dirtyかどうかを判定する
     * @param e
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        boolean newDirty = getDocument().getLength() > getInitialLength() ? true : false;
        setDirty(newDirty);
    }

    /**
     * 
     * @param e
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    /**
     * 
     * @param e
     */
    @Override
    public void caretUpdate(CaretEvent e) {
        boolean newSelection = (e.getDot() != e.getMark()) ? true : false;
        if (newSelection != hasSelection) {
            hasSelection = newSelection;

            // テキスト選択の状態へ遷移する
            if (hasSelection) {
                curState = getMyRole().equals(IInfoModel.ROLE_SOA) ? CompState.SOA_TEXT : CompState.P_TEXT;
            } else {
                curState = getMyRole().equals(IInfoModel.ROLE_SOA) ? CompState.SOA : CompState.P;
            }
            controlMenus(mediator.getActions());
        }
    }

    /**
     * リソースをclearする。
     */
    public void clear() {
        getTextPane().getDocument().removeDocumentListener(this);
        getTextPane().removeMouseListener(this);
        getTextPane().removeFocusListener(AutoKanjiListener.getInstance());
        getTextPane().removeCaretListener(this);

        try {
            KarteStyledDocument doc = getDocument();
            doc.remove(0, doc.getLength());
            doc = null;
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }

        setTextPane(null);
    }

    /**
     * メニューを制御する。
     *
     */
    private void controlMenus(ActionMap map) {
        if (getTextPane() != null) {
            // 各Stateはenableになる条件だけを管理する
            switch (curState) {

                case NONE:
                    break;

                case SOA:
                    // SOAPaneにFocusがありテキスト選択がない状態
                    if (getTextPane().isEditable()) {
                        map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());
                        map.get(GUIConst.ACTION_INSERT_TEXT).setEnabled(true);
                        map.get(GUIConst.ACTION_INSERT_SCHEMA).setEnabled(true);
                    }
                    break;

                case SOA_TEXT:

                    // SOAPaneにFocusがありテキスト選択がある状態
                    map.get(GUIConst.ACTION_CUT).setEnabled(getTextPane().isEditable());
                    map.get(GUIConst.ACTION_COPY).setEnabled(true);
                    boolean pasteOk = (getTextPane().isEditable() && canPaste()) ? true : false;
                    map.get(GUIConst.ACTION_PASTE).setEnabled(pasteOk);
                    break;

                case P:
                    // PPaneにFocusがありテキスト選択がない状態

                    if (getTextPane().isEditable()) {
                        map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());
                        map.get(GUIConst.ACTION_INSERT_TEXT).setEnabled(true);
                        map.get(GUIConst.ACTION_INSERT_STAMP).setEnabled(true);
                    }

                    break;

                case P_TEXT:
                    // PPaneにFocusがありテキスト選択がある状態
                    map.get(GUIConst.ACTION_CUT).setEnabled(getTextPane().isEditable());
                    map.get(GUIConst.ACTION_COPY).setEnabled(true);
                    pasteOk = (getTextPane().isEditable() && canPaste()) ? true : false;
                    map.get(GUIConst.ACTION_PASTE).setEnabled(pasteOk);
                    break;
                default: LogWriter.fatal(getClass(), "case default");
            }
        }
    }

    /**
     * 
     * @param map
     */
    @Override
    public void enter(ActionMap map) {
        curState = getMyRole().equals(IInfoModel.ROLE_SOA) ? CompState.SOA : CompState.P;
        controlMenus(map);
    }

    /**
     * 
     * @param map
     */
    @Override
    public void exit(ActionMap map) {
    }

    /**
     * 
     * 
     * @return マルチフォントに対応したテキスト編集用コンポーネント
     */
    @Override
    public Component getComponent() {
        return getTextPane();
    }

    /**
     * 右クリックで表示されるメニューを作る
     * @return ポップアップメニュー オブジェクト
     */
    protected JPopupMenu createMenus() {

        final JPopupMenu contextMenu = new JPopupMenu();

        // cut, copy, paste メニューを追加する
        contextMenu.add(mediator.getAction(GUIConst.ACTION_CUT));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_COPY));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_PASTE));

        contextMenu.add(new JSeparator());

        contextMenu.add(mediator.getAction(GUIConst.ACTION_FONT_LARGER));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_FONT_SMALLER));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_FONT_STANDARD));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_FONT_BOLD));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_FONT_ITALIC));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_FONT_UNDERLINE));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_LEFT_JUSTIFY));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_CENTER_JUSTIFY));
        contextMenu.add(mediator.getAction(GUIConst.ACTION_RIGHT_JUSTIFY));
        //   contextMenu.add(mediator.getAction(GUIConst.ACTION_INSERT_TOSEKI_STAMP));

        // テキストカラーメニューを追加する
        if (getTextPane().isEditable()) {
            ColorChooserComponent ccl = new ColorChooserComponent();
            ccl.addPropertyChangeListener(ColorChooserComponent.SELECTED_COLOR, new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    Color selected = (Color) e.getNewValue();
                    Action action = new StyledEditorKit.ForegroundAction("selected", selected);
                    action.actionPerformed(new ActionEvent(getTextPane(), ActionEvent.ACTION_PERFORMED, "foreground"));
                    contextMenu.setVisible(false);
                }
            });
            JLabel l = new JLabel("  カラー:");
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            p.add(l);
            p.add(ccl);
            contextMenu.add(p);
        } else {
            contextMenu.addSeparator();
        }

        // PPane の場合はStampMenuを追加する
        if (getMyRole().equals(IInfoModel.ROLE_P)) {
            //contextMenu.addSeparator();
            mediator.addStampMenu(contextMenu, this);
        } else {
            // TextMenuを追加する
            mediator.addTextMenu(contextMenu);
        }
        return contextMenu;
    }

    /**
     * ポップアップメニューを表示します。
     * @param e マウスイベント
     */
    private void mabeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu contextMenu = createMenus();
            contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        mabeShowPopup(e);
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        mabeShowPopup(e);
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * 背景を編集不可カラーに設定する。
     */
    protected void setBackgroundUneditable() {
        getTextPane().setBackground(getUneditableColor());
        getTextPane().setOpaque(true);
    }

    /**
     * ロールとパートナを設定する。
     * @param role このペインのロール
     */
    public void setRole(String role) {
        setMyRole(role);
    }

    /**
     * Dirtyかどうかを返す。
     * @return dirty の時 true
     */
    protected boolean isDirty() {
        return getTextPane().isEditable() ? dirty : false;
    }

    /**
     *
     * @param newDirty
     */
    public void setDirty(boolean newDirty) {
        if (newDirty != dirty) {
            dirty = newDirty;
            getParent().setDirty(dirty);
        }
    }

    /**
     * 保存時につけるドキュメントのタイトルをDocument Objectから抽出する。
     * @return 先頭から指定された長さを切り出した文字列
     */
    protected String getTitle() {
        try {
            KarteStyledDocument doc = getDocument();
            int len = doc.getLength();
            int freeTop = 0; // doc.getFreeTop();
            int freeLen = len - freeTop;
            freeLen = freeLen < TITLE_LENGTH ? freeLen : TITLE_LENGTH;
            return getTextPane().getText(freeTop, freeLen).trim();
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }

        return null;
    }

    /**
     * Documentの段落スタイルを設定する。
     * @param str スタイル
     */
    public void setLogicalStyle(String str) {
        getDocument().setLogicalStyle(str);
    }

    /**
     * Documentの段落論理スタイルをクリアする。
     */
    public void clearLogicalStyle() {
        getDocument().clearLogicalStyle();
    }

    /**
     * 段落を構成する。
     */
    public void makeParagraph() {
        getDocument().makeParagraph();
    }

    /**
     * Documentに文字列を挿入する。
     * @param s
     * @param a
     */
    public void insertFreeString(String s, AttributeSet a) {
        getDocument().insertFreeString(s, a);
    }

    /**
     *
     * @param stamp
     * @param model
     * @param dao
     * @param notice
     */
    private void stampWithSymptomCheck(final ModuleModel stamp, IInfoModel model, SqlMasterDao dao, final String notice) {
        boolean isCancel = false;
        List<Pair<InteractEntry, SsKijyoEntry>> symptoms = checkSymptom(dao, model);
        if (!symptoms.isEmpty()) {
            SymptomsDialog symptomDialog = new SymptomsDialog((JFrame) textPane.getRootPane().getParent(), true, dao, symptoms);
            symptomDialog.setVisible(true);
            isCancel = symptomDialog.IsCancel();
        }
        if (!isCancel) {
            if (stamp != null) {
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        stamp.getModuleInfo().setStampStatus(notice);
                        StampHolder stampHolder = new StampHolder(KartePane.this, stamp);
                        stampHolder.setTransferHandler(stampHolderTransferHandler);
                        KarteStyledDocument targetDocument = getDocument();
                        targetDocument.stamp(stampHolder);
                    }
                });
            }
        }
    }

    /**
     * このペインに Stamp を挿入する。
     * @param stamp
     */
    public void stamp(final ModuleModel stamp) {
        IInfoModel model = stamp.getModel();
        GregorianCalendar gc = new GregorianCalendar();

        if (model instanceof ClaimBundle) {
            final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
            List<MedicineEntry> updates = new ArrayList<MedicineEntry>();
            Enabled enabled = ((BundleDolphin) model).getStampEnabled(updates, gc);
            if (enabled == Enabled.UPDATE_AVAILABLE) {
                if (JOptionPane.showConfirmDialog(null, "期限切れの項目を含むスタンプを貼り付けます。" + System.getProperty("line.separator") + "よろしいですか？", "確認", JOptionPane.YES_NO_OPTION) == 0) {
                    stampWithSymptomCheck(stamp, model, dao, "（期限切れ）");
                }
            } else {
                stampWithSymptomCheck(stamp, model, dao, "");
            }
        }
    }

    /**
     * このペインに Stamp を挿入する。
     * @param stamp
     */
    public void flowStamp(ModuleModel stamp) {
        if (stamp != null) {
            StampHolder holder = new StampHolder(this, stamp);
            holder.setTransferHandler(stampHolderTransferHandler);
            KarteStyledDocument doc = getDocument();
            doc.flowStamp(holder);
        }
    }

    /**
     * このペインにシェーマを挿入する。
     * @param schema シェーマ
     */
    public void stampSchema(final SchemaModel schema) {
        if (schema != null) {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    SchemaHolder h = new SchemaHolder(KartePane.this, schema);
                    h.setTransferHandler(schemaHolderTransferHandler);
                    KarteStyledDocument doc = getDocument();
                    doc.stampSchema(h);
                }
            });
        }
    }

    /**
     * このペインにシェーマを挿入する。
     * @param schema  シェーマ
     */
    public void flowSchema(SchemaModel schema) {
        if (schema != null) {
            SchemaHolder h = new SchemaHolder(this, schema);
            h.setTransferHandler(schemaHolderTransferHandler);
            KarteStyledDocument doc = (KarteStyledDocument) getTextPane().getDocument();
            doc.flowSchema(h);
        }
    }

    /**
     * このペインに TextStamp を挿入する。
     * @param s
     */
    public void insertTextStamp(final String s) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                KarteStyledDocument doc = getDocument();
                doc.insertTextStamp(s);
            }
        });
    }

    /**
     * INFORMATION カルテへのスタンプのドロップ
     * StampInfoがDropされた時、そのデータをペインに挿入する。
     * @param stampInfo ドロップされたスタンプ情報
     */
    public void stampInfoDropped(ModuleInfoBean stampInfo) {

        // Drop された StampInfo の属性に応じて処理を振分ける

        String entity = stampInfo.getEntity();
        String role = stampInfo.getStampRole();

        // 病名の場合は２号カルテペインには展開しない
        if (entity.equals(IInfoModel.ENTITY_DIAGNOSIS)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (entity.equals(IInfoModel.ENTITY_TEXT)) {      // Text スタンプを挿入する
            applyTextStamp(stampInfo);
            return;
        }

        if (role.equals(IInfoModel.ROLE_ORCA_SET)) {        // ORCA 入力セットの場合
            applyOrcaSet(stampInfo);
            return;
        }

        if (stampInfo.isSerialized()) {        // データベースに保存されているスタンプを挿入する
            applySerializedStamp(stampInfo);
            return;
        }

        ModuleModel stamp = new ModuleModel();        // Stamp エディタを起動する
        stamp.setModuleInfo(stampInfo);

        StampEditorDialog stampEditor = new StampEditorDialog(entity, stamp);
        stampEditor.addPropertyChangeListener(StampEditorDialog.VALUE_PROP, this);
        stampEditor.start();
    }

    /**
     * StampInfoがDropされた時、そのデータをペインに挿入する。
     * @param addList スタンプ情報のリスト
     */
    public void stampInfoDropped(final List<ModuleInfoBean> addList) {

        final RemoteStampDelegater sdl = new RemoteStampDelegater();

        DBTask task = new DBTask<List<StampModel>>(parent.getParentContext()) {

            @Override
            protected List<StampModel> doInBackground() throws Exception {
                List<StampModel> list = sdl.getStamp(addList);
                return list;
            }

            @Override
            public void succeeded(List<StampModel> list) {

                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        ModuleInfoBean stampInfo = addList.get(i);
                        StampModel theModel = list.get(i);
                        IInfoModel model = theModel.toInfoModel();
                        if (model != null) {
                            ModuleModel stamp = new ModuleModel();
                            stamp.setModel(model);
                            stamp.setModuleInfo(stampInfo);
                            stamp(stamp);
                        }
                    }
                }
            }
        };

        task.execute();
    }

    /**
     * TextStampInfo が Drop された時の処理を行なう。
     * @param addList
     */
    public void textStampInfoDropped(final List<ModuleInfoBean> addList) {

        final RemoteStampDelegater sdl = new RemoteStampDelegater();

        DBTask task = new DBTask<List<StampModel>>(parent.getParentContext()) {

            @Override
            protected List<StampModel> doInBackground() throws Exception {
                List<StampModel> list = sdl.getStamp(addList);
                return list;
            }

            @Override
            public void succeeded(List<StampModel> list) {
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        StampModel theModel = list.get(i);
                        //     IInfoModel model = (IInfoModel) BeanUtils.xmlDecode(theModel.getStampBytes());
                        IInfoModel model = theModel.toInfoModel();
                        if (model != null) {
                            insertTextStamp(model.toString() + System.getProperty("line.separator"));
                        }
                    }
                }
            }
        };
        task.execute();
    }

    /**
     * TextStamp をこのペインに挿入する。
     */
    private void applyTextStamp(final ModuleInfoBean stampInfo) {

        final RemoteStampDelegater sdl = new RemoteStampDelegater();

        DBTask task = new DBTask<StampModel>(parent.getParentContext()) {

            /**
             *
             */
            @Override
            protected StampModel doInBackground() throws Exception {
                StampModel getStamp = sdl.getStamp(stampInfo.getStampId());
                return getStamp;
            }

            /**
             *
             */
            @Override
            public void succeeded(StampModel result) {
                if (result != null) {
                    try {
                        IInfoModel model = result.toInfoModel();
                        if (model != null) {
                            insertTextStamp(model.toString());
                        }

                    } catch (Exception e) {
                        LogWriter.error(getClass(), e);
                    }
                }
            }
        };
        task.execute();
    }

    /**
     * 禁忌
     * @param dao
     * @param importModel
     * @return
     */
    private List<Pair<InteractEntry, SsKijyoEntry>> checkSymptom(SqlMasterDao dao, IInfoModel importModel) {
        List<Pair<InteractEntry, SsKijyoEntry>> result = new ArrayList<Pair<InteractEntry, SsKijyoEntry>>();
        List<String> onStampCode = new ArrayList<String>();
        for (ClaimItem item : getDocument().getClaimItems()) {
            onStampCode.add(item.getCode());
        }

        ClaimItem[] importClaims = ((ClaimBundle) importModel).getClaimItem();
        for (int index = 0; index < importClaims.length; index++) {
            dao.getSsKijyoEntry(importClaims[index].getCode(), onStampCode, result);
        }
        return result;
    }

    /**
     * 永続化されているスタンプを取得してこのペインに展開する。
     * @param stampInfo
     */
    private void applySerializedStamp(final ModuleInfoBean stampInfo) {

        final RemoteStampDelegater sdl = new RemoteStampDelegater();

        DBTask task = new DBTask<StampModel>(parent.getParentContext()) {

            @Override
            protected StampModel doInBackground() throws Exception {
                StampModel getStamp = sdl.getStamp(stampInfo.getStampId());
                return getStamp;
            }

            @Override
            public void succeeded(StampModel result) {

                if (result != null) {
                    IInfoModel model = result.toInfoModel();
                    if (model instanceof ClaimBundle) {
                        ModuleModel stamp = new ModuleModel();

                        stamp.setModel(model);
                        stamp.setModuleInfo(stampInfo);
                        stamp(stamp);
                    }
                }
            }
        };
        task.execute();
    }

    /**
     * ORCA の入力セットを取得してこのペインに展開する。
     * @param stampInfo
     */
    private void applyOrcaSet(final ModuleInfoBean stampInfo) {

        final SqlOrcaSetDao sdl = new SqlOrcaSetDao();

        DBTask task = new DBTask<List<ModuleModel>>(parent.getParentContext()) {

            @Override
            protected List<ModuleModel> doInBackground() throws Exception {
                List<ModuleModel> models = sdl.getStampFromOrca(stampInfo);
                return models;
            }

            @Override
            public void succeeded(List<ModuleModel> models) {
                if (models != null) {
                    for (ModuleModel stamp : models) {
                        stamp(stamp);
                    }
                }
            }
        };

        task.execute();
    }

    //  private void showMetaDataMessage() {
    //       Window w = SwingUtilities.getWindowAncestor(getTextPane());
    //     JOptionPane.showMessageDialog(w,  "画像のメタデータが取得できず、読み込むことができません。",
    //             GlobalConstants.getFrameTitle("画像インポート"), JOptionPane.WARNING_MESSAGE);
    //}

    /**
     * 
     * @return
     */
    private boolean showMaxSizeMessage() {

        int maxImageWidth = 522; //GlobalVariables.getInt("image.max.width");
        int maxImageHeight = 522; //GlobalVariables.getInt("image.max.height");
        final Preferences pref = Preferences.userNodeForPackage(this.getClass());

        String title = GlobalConstants.getFrameTitle("画像サイズについて");
        JLabel msg1 = new JLabel("カルテに挿入する画像は、最大で " + maxImageWidth + " x " + maxImageHeight + " pixcel に制限しています。");
        JLabel msg2 = new JLabel("画像を縮小しカルテに展開しますか?");
        final JCheckBox cb = new JCheckBox("今後このメッセージを表示しない");
        cb.setFont(FontSettings.getKarteFont(0));
        cb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pref.putBoolean("showImageSizeMessage", !cb.isSelected());
            }
        });
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        p1.add(msg1);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        p2.add(msg2);
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        p3.add(cb);
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.add(p1);
        box.add(p2);
        box.add(p3);
        box.setBorder(BorderFactory.createEmptyBorder(0, 0, 11, 11));
        Window w = SwingUtilities.getWindowAncestor(getTextPane());

        int option = JOptionPane.showOptionDialog(w,
                new Object[]{box},
                GlobalConstants.getFrameTitle(title),
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                GlobalConstants.getImageIcon("about_32.gif"),
                new String[]{"縮小する", "取消す"}, "縮小する");
        return option == 0 ? true : false;
    }

    /**
     *
     */
    private void showNoReaderMessage() {
        Window w = SwingUtilities.getWindowAncestor(getTextPane());
        JOptionPane.showMessageDialog(w,
                "選択した画像を読むことができるリーダが存在しません。",
                GlobalConstants.getFrameTitle("画像インポート"),
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @param entry
     */
    public void imageEntryDropped(final ImageEntry entry) {

        int width = entry.getWidth();
        int height = entry.getHeight();
        int maxImageWidth = 522; //GlobalVariables.getInt("image.max.width");
        int maxImageHeight = 522; //GlobalVariables.getInt("image.max.height");
        Preferences pref = Preferences.userNodeForPackage(this.getClass());
        boolean ok = true;

        if (width == 0 || height == 0) {
            Icon icon = entry.getImageIcon();
            width = icon.getIconWidth();
            height = icon.getIconHeight();
            if (width > maxImageWidth || height > maxImageHeight) {
                if (pref.getBoolean("showImageSizeMessage", true)) {
                    ok = showMaxSizeMessage();
                }
            }
        } else if (width > maxImageWidth || height > maxImageHeight) {
            if (pref.getBoolean("showImageSizeMessage", true)) {
                ok = showMaxSizeMessage();
            }
        }

        if (!ok) {
            return;
        }

        DBTask task = new DBTask<ImageIcon>(parent.getParentContext()) {

            @Override
            protected ImageIcon doInBackground() throws Exception {

                URL url = new URL(entry.getUrl());
                BufferedImage importImage = ImageIO.read(url);

                int width = importImage.getWidth();
                int height = importImage.getHeight();
                int maxImageWidth = 522; //GlobalVariables.getInt("image.max.width");
                int maxImageHeight = 522; //GlobalVariables.getInt("image.max.height");

                if (width > maxImageWidth || height > maxImageHeight) {
                    importImage = ImageHelper.getFirstScaledInstance(importImage, maxImageWidth);
                }

                return new ImageIcon(importImage);
            }

            /**
             *
             */
            @Override
            public void succeeded(ImageIcon icon) {

                if (icon != null) {

                    SchemaModel schema = new SchemaModel();
                    schema.setIcon(icon);

                    // IInfoModel として ExtRef を保持している
                    ExtRefModel ref = new ExtRefModel();
                    ref.setContentType("image/jpeg");
                    ref.setTitle("Schema Image");
                    schema.setExtRef(ref);

                    stampId++;
                    String fileName = getDocId() + "-" + stampId + ".jpg";
                    schema.setFileName(fileName);
                    ref.setHref(fileName);

                    //         PluginLoader<ISchemaEditor> loader  = PluginLoader.load(ISchemaEditor.class, GlobalVariables.getPluginClassLoader());

                    List<SchemaEditorImpl> loader = new ArrayList<SchemaEditorImpl>();
                    loader.add(new SchemaEditorImpl());

                    Iterator<SchemaEditorImpl> iter = loader.iterator();
                    if (iter.hasNext()) {
                        final SchemaEditorImpl editor = iter.next();
                        editor.setSchema(schema);
                        editor.setEditable(true);
                        editor.addPropertyChangeListener(KartePane.this);
                        Runnable awt = new Runnable() {

                            @Override
                            public void run() {
                                editor.start();
                            }
                        };
                        EventQueue.invokeLater(awt);
                    }
                }
            }
        };
        task.execute();
    }

    /**
     * Schema が DnD された場合、シェーマエディタを開いて編集する。
     * @param path
     */
    public void insertImage(String path) {

        if (path == null) {
            return;
        }

        String suffix = path.toLowerCase();
        int index = suffix.lastIndexOf('.');
        if (index == 0) {
            showNoReaderMessage();
            return;
        }
        suffix = suffix.substring(index + 1);

        Iterator readers = ImageIO.getImageReadersBySuffix(suffix);

        if (!readers.hasNext()) {
            showNoReaderMessage();
            return;
        }

        ImageReader reader = (ImageReader) readers.next();

        int width = 0;
        int height = 0;
        String name = null;
        try {
            File file = new File(path);
            name = file.getName();
            reader.setInput(new FileImageInputStream(file), true);
            width = reader.getWidth(0);
            height = reader.getHeight(0);

        } catch (Exception e) {
            LogWriter.error(KartePane.class, e.getMessage());
            return;
        }
        reader = null;
        ImageEntry entry = new ImageEntry();
        entry.setPath(path);
        entry.setFileName(name);
        entry.setNumImages(1);
        entry.setWidth(width);
        entry.setHeight(height);
        imageEntryDropped(entry);
    }

    /**
     * StampEditor の編集が終了するとここへ通知される。
     * 通知されたスタンプをペインに挿入する。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();

        if (prop.equals("imageProp")) {

            SchemaModel schema = (SchemaModel) e.getNewValue();
            if (schema != null) {
                // 編集されたシェーマをこのペインに挿入する
                stampSchema(schema);
            }

        } else if (prop.equals(StampEditorDialog.VALUE_PROP)) {

            Object o = e.getNewValue();
            if (o != null) {
                // 編集された Stamp をこのペインに挿入する
                ModuleModel stamp = (ModuleModel) o;
                stamp(stamp);
            }
        }
    }

    /**
     * メニュー制御のため、ペースト可能かどうかを返す。
     * @return ペースト可能な時 true
     */
    protected boolean canPaste() {

        boolean ret = false;
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (t == null) {
            return false;
        }

        if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return true;
        }

        if (getMyRole().equals(IInfoModel.ROLE_P)) {
            if (t.isDataFlavorSupported(OrderListTransferable.orderListFlavor)) {
                ret = true;
            }
        } else {
            if (t.isDataFlavorSupported(StampListTransferable.stampListFlavor) || t.isDataFlavorSupported(SchemaListTransferable.schemaListFlavor)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * このペインからスタンプを削除する。
     * @param sh 削除するスタンプのホルダ
     */
    public void removeStamp(StampHolder sh) {
        getDocument().removeStamp(sh.getStartPos(), 2);
    }

    /**
     * このペインからスタンプを削除する。
     * @param sh 削除するスタンプのホルダリスト
     */
    public void removeStamp(StampHolder[] sh) {
        if (sh != null && sh.length > 0) {
            for (int i = 0; i < sh.length; i++) {
                removeStamp(sh[i]);
            }
        }
    }

    /**
     * このペインからシェーマを削除する。
     * @param sh 削除するシェーマのホルダ
     */
    public void removeSchema(SchemaHolder sh) {
        getDocument().removeStamp(sh.getStartPos(), 2);
    }

    /**
     * このペインからシェーマを削除する。
     * @param sh 削除するシェーマのホルダリスト
     */
    public void removeSchema(SchemaHolder[] sh) {
        if (sh != null && sh.length > 0) {
            for (int i = 0; i < sh.length; i++) {
                removeSchema(sh[i]);
            }
        }
    }
}
