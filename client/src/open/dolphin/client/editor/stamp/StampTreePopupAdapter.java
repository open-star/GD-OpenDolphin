package open.dolphin.client.editor.stamp;

import java.awt.Toolkit;

import open.dolphin.infomodel.ModuleInfoBean;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

/**
 * StampTreePopupAdapter
 *
 * @author  Kazushi Minagawa
 */
public class StampTreePopupAdapter extends MouseAdapter {

    /**
     * 
     */
    public StampTreePopupAdapter() {
    }
    /**
     *
     * @param evt
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        showPopupMenu(evt);
    }
    /**
     *
     * @param evt
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
        showPopupMenu(evt);
    }
    /**
     * 
     * @param evt
     */
    private void showPopupMenu(MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            // イベントソースの StampTree を取得する
            final StampTree tree = (StampTree) evt.getSource();
            int x = evt.getX();
            int y = evt.getY();
            // クリック位置へのパスを得る
            TreePath destPath = tree.getPathForLocation(x, y);
            if (destPath == null) {
                return;
            }
            // クリック位置の Node を得る
            final StampTreeNode node = (StampTreeNode) destPath.getLastPathComponent();
            if (node.isLeaf()) {
                // Leaf なので StampInfo 　を得る
                ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                // Editable
                if (!info.isEditable()) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }
            // Popupする
            JPopupMenu popup = new JPopupMenu();
            popup.add(new JMenuItem(
                    new AbstractAction("新規フォルダ") {

                        public void actionPerformed(ActionEvent e) {
                            tree.createNewFolder();
                            tree.saveStampTree();
                        }
                    }));
                    
            popup.add(new JMenuItem(
                    new AbstractAction("名称変更") {

                        public void actionPerformed(ActionEvent e) {
                            tree.renameNode();
                            tree.saveStampTree();
                        }
                    }));

            popup.addSeparator();
            popup.add(new JMenuItem(
                    new AbstractAction("削 除") {

                        public void actionPerformed(ActionEvent e) {
                            tree.deleteNode();
                            tree.saveStampTree();
                        }
                    }));

            popup.addSeparator();
            popup.add(new JMenuItem(
                    new AbstractAction("ひとつ上へ") {

                        public void actionPerformed(ActionEvent e) {
                            tree.moveUp();
                            tree.saveStampTree();
                        }
                    }));

            popup.add(new JMenuItem(
                    new AbstractAction("ひとつ下へ") {

                        public void actionPerformed(ActionEvent e) {
                            tree.moveDown();
                            tree.saveStampTree();
                        }
                    }));

            popup.show(evt.getComponent(), x, y);
        }
    }
}
