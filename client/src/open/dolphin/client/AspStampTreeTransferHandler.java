package open.dolphin.client;

import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.client.editor.stamp.StampTreeNode;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * AspStampTreeTransferHandler
 *
 * @author Minagawa,Kazushi
 *
 */
public class AspStampTreeTransferHandler extends TransferHandler {

    /**
     * 
     * @param c
     * @return
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        StampTree sourceTree = (StampTree) c;
        StampTreeNode dragNode = (StampTreeNode) sourceTree.getLastSelectedPathComponent();
        return new LocalStampTreeNodeTransferable(dragNode);
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /**
     *
     * @param c
     * @param tr
     * @return
     */
    @Override
    public boolean importData(JComponent c, Transferable tr) {
        return false;
    }

    /**
     *
     * @param c
     * @param data
     * @param action
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
    }

    /**
     *
     * @param c
     * @param flavors
     * @return
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return false;
    }
}
