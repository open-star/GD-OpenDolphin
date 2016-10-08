/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreeNode;

/**
 *
 * @author
 */
public class DoTransferHandler extends TransferHandler {

    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        TreeNode dragNode = (TreeNode) tree.getLastSelectedPathComponent();
        if (dragNode != null) {
            return new DoStampTreeNodeTransferable(dragNode);
        }
        return null;
    }
}
