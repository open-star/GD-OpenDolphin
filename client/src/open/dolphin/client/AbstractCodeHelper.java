package open.dolphin.client;

import javax.swing.text.BadLocationException;
import open.dolphin.project.GlobalConstants;
import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.client.editor.stamp.StampTreeNode;
import open.dolphin.client.editor.stamp.StampBoxFrame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.log.LogWriter;

/**
 * KartePaneの抽象コードヘルパークラス。（入力補完）
 *
 * @author Kazyshi Minagawa
 */
public abstract class AbstractCodeHelper {

    /** キーワードの境界となる文字 */
    private static final String[] WORD_SEPARATOR = {" ", " ", "、", "。", "\n", "\t"};
    /**
     *
     */
    protected static final Icon icon = GlobalConstants.getImageIcon("foldr_16.gif");
    private JTextPane textPane;  // KartePane の JTextPane
    private int MODIFIER;    // 修飾キー
    private int start;  // キーワードの開始位置
    private int end;   // キーワードの終了位置
    /**
     *
     */
    protected ChartMediator mediator;    // ChartMediator
    /**
     * 
     */
    protected JPopupMenu popup;   // 補完リストメニュー
    /**
     *
     */
    protected Pattern pattern;   // キーワードパターン

    /** 
     * Creates a new instance of CodeHelper 
     * @param kartePane
     * @param mediator
     */
    protected AbstractCodeHelper(KartePane kartePane, ChartMediator mediator) {

        this.mediator = mediator;
        this.textPane = kartePane.getTextPane();

        Preferences prefs = Preferences.userNodeForPackage(AbstractCodeHelper.class);
        String modifier = prefs.get("modifier", "ctrl");

        if (modifier.equals("ctrl")) {
            MODIFIER = KeyEvent.CTRL_DOWN_MASK;
        } else if (modifier.equals("meta")) {
            MODIFIER = KeyEvent.META_DOWN_MASK;
        }

        this.textPane.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    if ((e.getModifiersEx() == MODIFIER) && e.getKeyCode() == KeyEvent.VK_SPACE) {
                        buildAndShowPopup();
                    }
                } catch (BadLocationException ex) {
                        LogWriter.error(getClass(), ex);
                }
            }
        });
    }

    /**
     *
     * @param text
     */
    protected abstract void buildPopup(String text);

    /**
     *
     * @param entity
     */
    protected void buildEntityPopup(String entity) {

        // 引数の entityに対応する StampTree を取得する
        StampBoxFrame stampBox = mediator.getStampBox();
        StampTree tree = stampBox.getStampTree(entity);
        if (tree != null) {
            popup = new JPopupMenu();
            Map<Object, Object> ht = new HashMap<Object, Object>(5, 0.75f);
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
            ht.put(rootNode, popup);
            Enumeration e = rootNode.preorderEnumeration();
            if (e != null) {
                e.nextElement(); // consume root
                while (e.hasMoreElements()) {
                    StampTreeNode node = (StampTreeNode) e.nextElement();
                    if (!node.isLeaf()) {
                        JMenu subMenu = new JMenu(node.getUserObject().toString());
                        if (node.getParent() == rootNode) {
                            JPopupMenu parent = (JPopupMenu) ht.get(node.getParent());
                            parent.add(subMenu);
                            //   ht.put(node, subMenu);
                        } else {
                            JMenu parent = (JMenu) ht.get(node.getParent());
                            parent.add(subMenu);
                            //     ht.put(node, subMenu);
                        }
                        ht.put(node, subMenu);

                        // 配下の子を全て列挙しJmenuItemにまとめる
                        JMenuItem item = new JMenuItem(node.getUserObject().toString());
                        item.setIcon(icon);
                        subMenu.add(item);
                        addActionListner(item, node);
                    } else if (node.isLeaf()) {
                        ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                        String stampName = info.getStampName();
                        JMenuItem item = new JMenuItem(stampName);
                        addActionListner(item, node);
                        if (node.getParent() == rootNode) {
                            JPopupMenu parent = (JPopupMenu) ht.get(node.getParent());
                            parent.add(item);
                        } else {
                            JMenu parent = (JMenu) ht.get(node.getParent());
                            parent.add(item);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param item
     * @param node
     */
    protected void addActionListner(JMenuItem item, final StampTreeNode node) {

        ActionListener ral = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                importStamp(textPane, textPane.getTransferHandler(), new LocalStampTreeNodeTransferable(node));
            }
        };

        item.addActionListener(ral);
    }

    /**
     *
     * @throws BadLocationException
     */
    private void showPopup() throws BadLocationException {

        if (popup == null || popup.getComponentCount() < 1) {
            return;
        }

        int pos = textPane.getCaretPosition();
        Rectangle r = textPane.modelToView(pos);
        popup.show(textPane, r.x, r.y);
    }

    /**
     *
     * @param comp
     * @param handler
     * @param tr
     */
    private void importStamp(JComponent comp, TransferHandler handler, LocalStampTreeNodeTransferable tr) {
        textPane.setSelectionStart(start);
        textPane.setSelectionEnd(end);
        textPane.replaceSelection("");
        handler.importData(comp, tr);
        closePopup();
    }

    /**
     *
     */
    private void closePopup() {
        if (popup != null) {
            popup.removeAll();
            popup = null;
        }
    }

    /**
     * 単語の境界からキャレットの位置までのテキストを取得し、
     * 長さがゼロ以上でれば補完メニューをポップアップする。
     */
    private void buildAndShowPopup() throws BadLocationException {

        end = textPane.getCaretPosition();
        start = end;
        boolean found = false;
        while (start > 0) {
            start--;
            String text = textPane.getText(start, 1);
            for (String test : WORD_SEPARATOR) {
                if (test.equals(text)) {
                    found = true;
                    break;
                }
            }
            if (found) {
                start++;
                break;
            }
        }
        String str = textPane.getText(start, end - start);
        if (str.length() > 0) {
            buildPopup(str);
            showPopup();
        }

    }
}
