package open.dolphin.client;

import open.dolphin.client.diagnosisdocumentpanel.DiagnosisDocumentPanel;
import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.client.editor.stamp.StampTreeMenuBuilder;
import open.dolphin.client.editor.stamp.StampTreePopupBuilder;
import open.dolphin.client.editor.stamp.StampHolder;
import open.dolphin.client.editor.stamp.StampBoxFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.undo.UndoManager;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.project.GlobalVariables;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.event.MenuListener;
import javax.swing.text.StyledEditorKit;

import open.dolphin.client.definition.FontSettings;
import open.dolphin.project.GlobalSettings;
import open.dolphin.helper.ChartMenuSupport;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.utils.StringTool;
import open.dolphin.helper.IChartCommandAccepter;

/**
 * メニューと画面のやり取り
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class ChartMediator extends ChartMenuSupport implements UndoableEditListener, MenuListener, IChartCommandAccepter {

    /**
     *
     */
    protected static enum CompState {

        /**
         * 
         */
        NONE,
        /**
         *
         */
        SOA,
        /**
         * 
         */
        SOA_TEXT,
        /**
         * 
         */
        SCHEMA,
        /**
         * 
         */
        P,
        /**
         *
         */
        P_TEXT,
        /**
         *
         */
        STAMP
    };
    private int curSize = 3;
    private IChart chart;
    private IKarteComposite curKarteComposit;
    private JFrame curWindow;
    private UndoManager undoManager;
    private Action undoAction;
    private Action redoAction;
    private FocusPropertyChangeListener fpcl;

    /**
     *　MEMO:リスナー
     */
    class FocusPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            String prop = e.getPropertyName();
            if ("focusOwner".equals(prop)) {
                Component comp = (Component) e.getNewValue();
                if (comp instanceof JTextPane) {
                    Object obj = ((JTextPane) comp).getClientProperty("kartePane");
                    if (obj != null && obj instanceof KartePane) {
                        setCurKarteComposit((IKarteComposite) obj);
                    }
                } else if (comp instanceof IKarteComposite) {
                    setCurKarteComposit((IKarteComposite) comp);
                }
            } else if ("activeWindow".equals(prop)) {
                Object frame = e.getNewValue();
                if (frame instanceof JFrame) {
                    setCurWindow((JFrame) e.getNewValue());
                }
            }
        }
    }

    /**
     *
     * @param owner
     */
    public ChartMediator(IChartCommandAccepter owner) {
        super(owner);
        setAccepter(this);

        chart = (IChart) owner;
        fpcl = new FocusPropertyChangeListener();
        undoManager = new UndoManager();

        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(fpcl);
    }

    /**
     * メニューリスナの実装。
     * 挿入及びテキストメニューが選択された時の処理を行う。
     */
    @Override
    public void menuSelected(MenuEvent e) {
        // 挿入とテキストメニューにリスナが登録されている
        JMenu selectedMenu = (JMenu) e.getSource();
        String cmd = selectedMenu.getActionCommand();

        // 挿入メニューの時
        // StampBox のツリーをメニューにする
        if (cmd.equals(GUIConst.MENU_INSERT)) {
            selectedMenu.removeAll();
            List<StampTree> trees = getStampBox().getAllTrees();    // StampBox の全ツリーを取得する
            for (StampTree tree : trees) {                          // ツリーをイテレートする
                String entity = tree.getEntity();                   // ツリーのエンティティを取得する
                if (entity.equals(IInfoModel.ENTITY_DIAGNOSIS)) {
                    selectedMenu.add(createDiagnosisMenu(tree));    // 傷病名の時、傷病名メニューを構築し追加する
                    selectedMenu.addSeparator();
                } else if (entity.equals(IInfoModel.ENTITY_TEXT)) {
                    selectedMenu.add(createTextMenu(tree));         // テキストの時、テキストメニューを構築し追加する
                    selectedMenu.addSeparator();
                } else {
                    selectedMenu.add(createStampMenu(tree));        // 通常のPオーダの時
                }
            }
        } else if (cmd.equals(GUIConst.MENU_TEXT)) {
            adjustStyleMenu(); // テキストメニューの場合、スタイルを制御する
        }
    }

    /**
     *
     */
    @Override
    public void cut() {
        if (curKarteComposit != null) {
            JComponent focusOwner = getCurrentComponent();
            if (focusOwner != null) {
                Action action = focusOwner.getActionMap().get(TransferHandler.getCutAction().getValue(Action.NAME));
                if (action != null) {
                    action.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
                    setCurKarteComposit(null);
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public void copy() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action action = focusOwner.getActionMap().get(TransferHandler.getCopyAction().getValue(Action.NAME));
            if (action != null) {
                action.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
        }
    }

    /**
     *
     */
    @Override
    public void paste() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action action = focusOwner.getActionMap().get(TransferHandler.getPasteAction().getValue(Action.NAME));
            if (action != null) {
                action.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void menuDeselected(MenuEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void menuCanceled(MenuEvent e) {
    }

    /**
     *
     * @param map
     */
    @Override
    public void registerActions(ActionMap map) {

        super.registerActions(map);

        undoAction = map.get(GUIConst.ACTION_UNDO);
        redoAction = map.get(GUIConst.ACTION_REDO);

        // 昇順降順を Preference から取得し設定しておく
        boolean isAsc = GlobalVariables.getAscendingKarte();

        Action action = null;
        if (isAsc) {
            action = map.get(GUIConst.ACTION_ASCENDING);
        } else {
            action = map.get(GUIConst.ACTION_DESCENDING);
        }
        JRadioButtonMenuItem rdi = (JRadioButtonMenuItem) action.getValue("menuItem");
        rdi.setSelected(true);
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchChartCommand(IChartCommandAccepter.ChartCommand command) {
        switch (command) {
            case letterPasteFromKarte:
                return pasteLetter();
            case quickEdit:
                return quickEdit();
            case delete:
                return delete();
            case resetStyle:
                return resetStyle();
            case undo:
                return undo();
            case redo:
                return redo();
            case fontLarger:
                return fontLarger();
            case fontSmaller:
                return fontSmaller();
            case fontStandard:
                return fontStandard();
            case fontBold:
                return styleAction("font-bold");
            case fontItalic:
                return styleAction("font-italic");
            case fontUnderline:
                return styleAction("font-underline");
            case leftJustify:
                return styleAction("left-justify");
            case centerJustify:
                return styleAction("center-justify");
            case rightJustify:
                return styleAction("right-justify");
            case fontRed:
                return colorAction(GlobalSettings.getColors(GlobalSettings.Parts.RED));
            case fontOrange:
                return colorAction(GlobalSettings.getColors(GlobalSettings.Parts.ORANGE));
            case fontYellow:
                return colorAction(GlobalSettings.getColors(GlobalSettings.Parts.YELLOW));
            case fontGreen:
                return colorAction(GlobalSettings.getColors(GlobalSettings.Parts.GREEN));
            case fontBlue:
                return colorAction(GlobalSettings.getColors(GlobalSettings.Parts.BLUE));
            case fontPurple:
                return colorAction(GlobalSettings.getColors(GlobalSettings.Parts.PURPLE));
            case fontGray:
                return colorAction(GlobalSettings.getColors(GlobalSettings.Parts.GRAY));
            case fontBlack:
                return colorAction(Color.BLACK);
            default:
        }
        return false;
    }

    /**
     *
     * @param e
     */
    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        undoManager.addEdit(e.getEdit());
        updateUndoAction();
        updateRedoAction();
    }

    /**
     *
     * @param frame
     */
    protected void setCurWindow(JFrame frame) {
        this.curWindow = frame;
    }

    /**
     *
     * @return
     */
    protected JFrame getCurWindow() {
        return this.curWindow;
    }

    /**
     *
     * @param newComposit
     */
    protected void setCurKarteComposit(IKarteComposite newComposit) {
        IKarteComposite old = this.curKarteComposit;
        this.curKarteComposit = newComposit;

        if (old != curKarteComposit) {

            if (old != null) {
                old.exit(getActions());
            }

            enabledAction(GUIConst.ACTION_CUT, false);
            enabledAction(GUIConst.ACTION_COPY, false);
            enabledAction(GUIConst.ACTION_PASTE, false);
            enabledAction(GUIConst.ACTION_UNDO, false);
            enabledAction(GUIConst.ACTION_REDO, false);
            enabledAction(GUIConst.ACTION_INSERT_TEXT, false);
            enabledAction(GUIConst.ACTION_INSERT_SCHEMA, false);
            enabledAction(GUIConst.ACTION_INSERT_STAMP, false);

            if (curKarteComposit != null) {
                curKarteComposit.enter(getActions());
            }
        }
    }

    /**
     *
     */
    public void dispose() {
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.removePropertyChangeListener(fpcl);
    }

    /**
     *
     * @return
     */
    private JComponent getCurrentComponent() {
        if (curKarteComposit != null) {
            return (JComponent) curKarteComposit.getComponent();
        }
        return null;
    }

    /**
     * フォーマット関連メニューを調整する。
     * @param kartePane
     */
    private void adjustStyleMenu() {
        boolean enabled = false;
        KartePane kartePane = null;
        if (getLast() instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) getLast();
            kartePane = editor.getSOAPane();
            enabled = kartePane.getTextPane().isEditable();
        }

        // サブメニューを制御する
        getAction("size").setEnabled(enabled);
        getAction("style").setEnabled(enabled);
        getAction("justify").setEnabled(enabled);
        getAction("color").setEnabled(enabled);

        getAction("fontRed").setEnabled(enabled);
        getAction("fontOrange").setEnabled(enabled);
        getAction("fontYellow").setEnabled(enabled);
        getAction("fontGreen").setEnabled(enabled);
        getAction("fontBlue").setEnabled(enabled);
        getAction("fontPurple").setEnabled(enabled);
        getAction("fontGray").setEnabled(enabled);

        getAction("fontLarger").setEnabled(enabled);
        getAction("fontSmaller").setEnabled(enabled);
        getAction("fontStandard").setEnabled(enabled);

        getAction("fontBold").setEnabled(enabled);
        getAction("fontItalic").setEnabled(enabled);
        getAction("fontUnderline").setEnabled(enabled);

        getAction("leftJustify").setEnabled(enabled);
        getAction("centerJustify").setEnabled(enabled);
        getAction("rightJustify").setEnabled(enabled);
    }

    /**
     * スタンプTreeから傷病名メニューを構築する。
     * @param insertMenu テキストメニュー
     */
    private JMenu createDiagnosisMenu(StampTree stampTree) {
        // chainの先頭がDiagnosisDocumentの時のみ使用可能とする

        JMenu myMenu = null;
        boolean enabled = false;

        DiagnosisDocumentPanel diagnosis = null;

        Object obj = getLast();
        if (obj instanceof DiagnosisDocumentPanel) {
            diagnosis = (DiagnosisDocumentPanel) obj;
            enabled = true;
        }

        if (!enabled) {
            // chainの先頭がDiagnosisでない場合はメニューをdisableにする
            myMenu = new JMenu(stampTree.getTreeName());
            myMenu.setEnabled(false);

        } else {
            // 傷病名Tree、テーブル、ハンドラからメニューを構築する
            JComponent comp = diagnosis.getDiagnosisTable();
            TransferHandler handler = comp.getTransferHandler();
            StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
            myMenu = builder.build(stampTree, comp, handler);
        }

        return myMenu;
    }

    /**
     * スタンプTreeからテキストメニューを構築する。
     * @param insertMenu テキストメニュー
     */
    private JMenu createTextMenu(StampTree stampTree) {
        JMenu myMenu = null;
        boolean enabled = false;
        KartePane kartePane = null;

        if (getLast() instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) getLast();
            kartePane = editor.getSOAPane();
            if (kartePane != null) {
                enabled = kartePane.getTextPane().isEditable();
            }
        }

        if (!enabled) {
            myMenu = new JMenu(stampTree.getTreeName());
            myMenu.setEnabled(false);
        } else {
            JComponent comp = getCurrentComponent();
            if (comp == null) {
                comp = kartePane.getTextPane();
            }
            TransferHandler handler = comp.getTransferHandler();
            StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
            myMenu = builder.build(stampTree, comp, handler);
        }

        return myMenu;
    }

    /**
     * スタンプメニューを構築する。
     * @param insertMenu スタンプメニュー
     */
    private JMenu createStampMenu(StampTree stampTree) {
        // chain の先頭が KarteEditor でかつ Pane が編集可の場合のみメニューが使える
        JMenu myMenu = null;
        boolean enabled = false;
        KartePane kartePane = null;

        if (getLast() instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) getLast();
            kartePane = editor.getPPane();
            if (kartePane != null) {
                enabled = kartePane.getTextPane().isEditable();
            }
        }

        if (!enabled) {
            myMenu = new JMenu(stampTree.getTreeName());
            myMenu.setEnabled(false);

        } else {
            // StampTree、JTextPane、Handler からメニューを構築する
            JComponent comp = kartePane.getTextPane();
            TransferHandler handler = comp.getTransferHandler();
            StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
            myMenu = builder.build(stampTree, comp, handler);
        }

        return myMenu;
    }

    /**
     * 引数のポップアップメニューへテキストメニューを追加する。
     * @param popup テキストメニューを追加するポップアップメニュー
     */
    public void addTextMenu(JPopupMenu popup) {
        boolean enabled = false;
        KartePane kartePane = null;

        if (getLast() instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) getLast();
            kartePane = editor.getSOAPane();
            if (kartePane != null) {
                enabled = (kartePane.getTextPane().isEditable()) ? true : false;
            }
        }

        StampTree stampTree = getStampBox().getStampTree(IInfoModel.ENTITY_TEXT);

        // ASP スタンプボックスで entity に対応する Tree がない場合がある
        if (stampTree != null) {

            if (!enabled) {
                JMenu myMenu = new JMenu(stampTree.getTreeName());
                myMenu.setEnabled(false);
                popup.add(myMenu);
                return;

            } else {
                JComponent comp = getCurrentComponent();
                if (comp == null) {
                    comp = kartePane.getTextPane();
                }
                TransferHandler handler = comp.getTransferHandler();
                StampTreePopupBuilder builder = new StampTreePopupBuilder();
                builder.build(stampTree, popup, comp, handler);
            }
        }
    }

    /**
     * PPane のコンテキストメニューまたはツールバーの stampIcon へスタンプメニューを追加する。
     * @param menu Ppane のコンテキストメニュー
     * @param kartePane PPnae
     */
    protected void addStampMenu(JPopupMenu menu, final KartePane kartePane) {
        // 引数のPaneがPかつ編集可の時のみ追加する
        // コンテキストメニューなのでこれはOK
        if (kartePane != null && kartePane.getMyRole().equals(IInfoModel.ROLE_P) && kartePane.getTextPane().isEditable()) {
            StampBoxFrame stampBox = getStampBox();
            List<StampTree> trees = stampBox.getAllTrees();
            StampTreeMenuBuilder builder = new StampTreeMenuBuilder();
            JComponent cmp = kartePane.getTextPane();
            TransferHandler handler = cmp.getTransferHandler();

            // StampBox内の全Treeをイテレートする
            for (StampTree stampTree : trees) {

                // 傷病名とテキストは別に作成するのでスキップする
                String entity = stampTree.getEntity();
                if (entity.equals(IInfoModel.ENTITY_DIAGNOSIS) || entity.equals(IInfoModel.ENTITY_TEXT)) {
                    continue;
                }

                JMenu subMenu = builder.build(stampTree, cmp, handler);
                menu.add(subMenu);
            }
        }
    }

    /**
     * 引数のポップアップメニューへスタンプメニューを追加する。
     * このメソッドはツールバーの stamp icon の actionPerformed からコールされる。
     * @param popup
     */
    public void addStampMenu(JPopupMenu popup) {
        boolean enabled = false;
        KartePane kartePane = null;

        if (getLast() instanceof KarteEditor) {
            KarteEditor editor = (KarteEditor) getLast();
            kartePane = editor.getPPane();
            if (kartePane != null) {
                enabled = kartePane.getTextPane().isEditable();
            }
        }

        if (enabled) {
            addStampMenu(popup, kartePane);
        }
    }

    /**
     *
     * @param entity
     * @return
     */
    protected StampTree getStampTree(String entity) {
        return getStampBox().getStampTree(entity);
    }

    /**
     *
     * @return
     */
    protected StampBoxFrame getStampBox() {
        return (StampBoxFrame) chart.getContext().getPlugin("stampBox");
    }

    /**
     *
     * @param entity
     * @return
     */
    protected boolean hasTree(String entity) {
        StampBoxFrame stBox = (StampBoxFrame) chart.getContext().getPlugin("stampBox");
        StampTree tree = stBox.getStampTree(entity);
        return (tree != null);
    }
    /*
    public void applyInsurance(PVTHealthInsuranceModel pvtHIModel) {
    Object target = getLast();

    if (target != null) {
    try {
    Method meth = target.getClass().getMethod("applyInsurance", new Class[]{pvtHIModel.getClass()});
    meth.invoke(target, new Object[]{pvtHIModel});
    } catch (Exception e) {
    LogWriter.error(getClass(), e);
    }
    }
    }
     */

    /**
     *
     * @return
     */
    public IChart getChart() {
        return chart;
    }

    /**
     *
     * @param message
     * @return
     */
    private boolean pasteMedicationToLetter(String message) {
        LetterView view = ((ChartWindow) chart).getLetterPane();
        if (view != null) {
            // TODO Bundle 側で OrderName 付きのものを用意
            view.getMedication().setText(view.getMedication().getText() + System.getProperty("line.separator") + message);
        }
        return true;
    }

    /**
     *
     * @return
     */
    private boolean pasteLetter() {
        if (chart instanceof ChartWindow && ((ChartWindow) chart).existLetterPane()) {
            JComponent stampHolder = getCurrentComponent();
            if (stampHolder instanceof StampHolder) {
                ModuleModel stamp = ((StampHolder) stampHolder).getStamp();
                String stampName = stamp.getModuleInfo().getStampName();
                BundleDolphin bundle = (BundleDolphin) stamp.getModel();

                pasteMedicationToLetter(bundle.toString().replaceFirst(System.getProperty("line.separator"), "(" + stampName + ")" + System.getProperty("line.separator")));
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    private boolean quickEdit() {
        if (chart instanceof IChart) {
            JComponent currentComponent = getCurrentComponent();
            if (currentComponent instanceof StampHolder) {
                StampHolder stampHolder = (StampHolder) currentComponent;
                ModuleModel moduleModel = stampHolder.getStamp();
                IInfoModel model = moduleModel.getModel();

                if (!(model instanceof ClaimBundle)) {
                    return false;
                }

                ClaimBundle bundle = (ClaimBundle) model;
                final List<JTextField> results = stampHolder.showQuickEditDialog(getCurWindow());

                if (results == null) {
                    return false;
                }

                JTextField bundleNumber = results.remove(results.size() - 1);

                List<ClaimItem> targets = new ArrayList<ClaimItem>();
                ClaimItem[] candidates = bundle.getClaimItem();

                for (int i = 0; i < candidates.length; i++) {
                    if (StringTool.isEmptyString(candidates[i].getNumber())) {
                        continue;
                    }
                    targets.add(candidates[i]);
                }

                for (int i = 0; i < targets.size(); i++) {
                    String value = results.get(i).getText();
                    try {
                        Double.parseDouble(value);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(getCurWindow(), "数字を入力してください");
                        return false;
                    }

                    targets.get(i).setNumber(value);
                }

                int number = 0;

                try {
                    number = Integer.parseInt(bundleNumber.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(getCurWindow(), "数字を入力してください");
                    return false;
                }

                bundle.setBundleNumber(number);
                stampHolder.importStamp(moduleModel);
            }
            return true;
        }
        return false;
    }

    /**
     * MEMO:何もしない
     * @return
     */
    public boolean delete() {
        return true;
    }

    /**
     *
     * @return
     */
    private boolean resetStyle() {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null && focusOwner instanceof JTextPane) {
            JTextPane pane = (JTextPane) focusOwner;
            pane.setCharacterAttributes(SimpleAttributeSet.EMPTY, true);
        }
        return true;
    }

    /**
     *
     * @return
     */
    private boolean undo() {
        undoManager.undo();
        updateUndoAction();
        updateRedoAction();
        return true;
    }

    /**
     *
     * @return
     */
    private boolean redo() {
        undoManager.redo();
        updateRedoAction();
        updateUndoAction();
        return true;
    }

    /**
     *
     */
    private void updateUndoAction() {
        if (undoManager.canUndo()) {
            undoAction.setEnabled(true);
        } else {
            undoAction.setEnabled(false);
        }
    }

    /**
     *
     */
    private void updateRedoAction() {
        if (undoManager.canRedo()) {
            redoAction.setEnabled(true);
        } else {
            redoAction.setEnabled(false);
        }
    }

    /**
     * TODO: 選択範囲のフォントサイズを考慮する
     * @return
     */
    private boolean fontLarger() {
        JTextPane focusOwner = (JTextPane) getCurrentComponent();
        int selectionStart = focusOwner.getSelectionStart();
        int selectionEnd = focusOwner.getSelectionEnd();
        if (focusOwner != null && selectionStart != selectionEnd) {
            if (curSize < 6) {
                curSize++;
            }
            int size = FontSettings.getKarteFontSize(curSize);
            Action a = focusOwner.getActionMap().get("font-size-" + size);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
            if (curSize == 6) {
                enabledAction("fontLarger", false);
            }
        }
        return true;
    }

    /**
     * TODO: 選択範囲のフォントサイズを考慮する
     * @return
     */
    private boolean fontSmaller() {
        JTextPane focusOwner = (JTextPane) getCurrentComponent();
        int selectionStart = focusOwner.getSelectionStart();
        int selectionEnd = focusOwner.getSelectionEnd();
        if (focusOwner != null && selectionStart != selectionEnd) {
            if (curSize > 0) {
                curSize--;
            }
            int size = FontSettings.getKarteFontSize(curSize);
            Action a = focusOwner.getActionMap().get("font-size-" + size);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
            if (curSize == 0) {
                enabledAction("fontSmaller", false);
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    private boolean fontStandard() {
        JTextPane focusOwner = (JTextPane) getCurrentComponent();
        if (focusOwner != null) {
            curSize = 3;
            int size = FontSettings.getKarteDefaultFontSize();
            Action a = focusOwner.getActionMap().get("font-size-" + size);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
            enabledAction("fontSmaller", true);
            enabledAction("fontLarger", true);
        }
        return true;
    }

    /**
     *
     * @param style
     * @return
     */
    private boolean styleAction(String style) {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = focusOwner.getActionMap().get(style);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, null));
            }
        }
        return true;
    }

    /**
     *
     * @param color
     * @return
     */
    private boolean colorAction(Color color) {
        JComponent focusOwner = getCurrentComponent();
        if (focusOwner != null) {
            Action a = new StyledEditorKit.ForegroundAction("color", color);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner, ActionEvent.ACTION_PERFORMED, "foreground"));
            }
        }
        return true;
    }
}
