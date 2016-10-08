package open.dolphin.client.editor.stamp;

import open.dolphin.project.GlobalConstants;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.client.LocalStampTreeNodeTransferable;
import open.dolphin.client.TransferAction;

import open.dolphin.utils.CombinedStringParser;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * StampTreeMenuBuilder
 *
 * @author Kazushi Minagawa
 */
public class StampTreePopupBuilder {

    private static final Icon icon = GlobalConstants.getImageIcon("foldr_16.gif");
    private Map<Object, JMenu> parents;
    private JPopupMenu popup;
    private DefaultMutableTreeNode rootNode;

    /**
     * 
     */
    public StampTreePopupBuilder() {
    }

    /**
     *
     * @param stampTree
     * @param popup
     * @param cmp
     * @param handler
     */
    public void build(StampTree stampTree, JPopupMenu popup, JComponent cmp, TransferHandler handler) {

        if (parents == null) {
            parents = new HashMap<Object, JMenu>(10, 0.75f);
        } else {
            parents.clear();
        }

        this.popup = popup;

        rootNode = (DefaultMutableTreeNode) stampTree.getModel().getRoot();
        Enumeration e = rootNode.preorderEnumeration();
        e.nextElement(); // consume root

        while (e.hasMoreElements()) {
            parseChildren((StampTreeNode) e.nextElement(), cmp, handler);
        }
    }
    /**
     *
     * @param node
     * @param comp
     * @param handler
     */
    private void parseChildren(StampTreeNode node, JComponent comp, TransferHandler handler) {

        if (!node.isLeaf()) {
            JMenu subMenu = new JMenu(node.getUserObject().toString());
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            if (parentNode == rootNode) {
                popup.add(subMenu);
            } else {
                JMenu parent = parents.get(node.getParent());
                parent.add(subMenu);
            }
            parents.put(node, subMenu);

            // 配下の子を全て列挙しJmenuItemにまとめる
            JMenuItem item = new JMenuItem(node.getUserObject().toString());
            item.setIcon(icon);
            subMenu.add(item);

            if (comp != null && handler != null) {
                item.addActionListener(new TransferAction(comp, handler, new LocalStampTreeNodeTransferable(node)));
            } else {
                item.setEnabled(false);
            }

        } else {
            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
            CombinedStringParser conbinedStampName = new CombinedStringParser(info.getStampName());
            JMenuItem item = new JMenuItem(conbinedStampName.toPlainString());
            if (comp != null && handler != null) {
                item.addActionListener(new TransferAction(comp, handler, new LocalStampTreeNodeTransferable(node)));
            } else {
                item.setEnabled(false);
            }
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            if (parentNode == rootNode) {
                popup.add(item);
            } else {
                JMenu parent = parents.get(node.getParent());
                parent.add(item);
            }
        }
    }
}














