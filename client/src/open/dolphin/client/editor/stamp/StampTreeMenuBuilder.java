package open.dolphin.client.editor.stamp;

import open.dolphin.project.GlobalConstants;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.client.LocalStampTreeNodeTransferable;
import open.dolphin.client.TransferAction;

import open.dolphin.infomodel.ModuleInfoBean;

/**
 * StampTreeMenuBuilder
 *
 * @author Kazushi Minagawa
 */
public class StampTreeMenuBuilder {

    private static final Icon icon = GlobalConstants.getImageIcon("foldr_16.gif");
    private Map<Object, JMenu> parents;

    /**
     * 
     */
    public StampTreeMenuBuilder() {
    }

    /**
     *
     * @param stampTree
     * @param cmp
     * @param handler
     * @return
     */
    public JMenu build(StampTree stampTree, JComponent cmp, TransferHandler handler) {
        if (parents == null) {
            parents = new HashMap<Object, JMenu>(10, 0.75f);
        } else {
            parents.clear();
        }
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) stampTree.getModel().getRoot();
        JMenu top = new JMenu(stampTree.getTreeName());
        parents.put(rootNode, top);
        Enumeration e = rootNode.preorderEnumeration();
        if (e != null) {
            e.nextElement(); // consume root
            while (e.hasMoreElements()) {
                parseChildren((StampTreeNode) e.nextElement(), cmp, handler);
            }
        }
        return top;
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
            JMenu parent = parents.get(node.getParent());
            parent.add(subMenu);
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
            JMenuItem item = new JMenuItem(info.getStampName());
            if (comp != null && handler != null) {
                item.addActionListener(new TransferAction(comp, handler, new LocalStampTreeNodeTransferable(node)));
            } else {
                item.setEnabled(false);
            }
            JMenu parent = parents.get(node.getParent());
            parent.add(item);
        }
    }
}
