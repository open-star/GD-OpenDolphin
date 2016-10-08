package open.dolphin.plugin;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.tree.TreeNode;


/**
 */
public class DoStampTreeNodeTransferable implements Transferable {

    /** Data Flavor of this class */
    public static DataFlavor doStampTreeNodeFlavor;

    static {
        try {
            doStampTreeNodeFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=javax.swing.tree.TreeNode");   //リフレクション
        } catch (Exception e) {

        }
    }

    /**
     *
     */
    public static final DataFlavor[] flavors = {DoStampTreeNodeTransferable.doStampTreeNodeFlavor};
    private TreeNode node;

    /** Creates new StampTreeTransferable
     * @param node
     */
    public DoStampTreeNodeTransferable(TreeNode node) {
        this.node = node;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(doStampTreeNodeFlavor) ? true : false;
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor)  throws UnsupportedFlavorException, IOException {

        if (flavor.equals(doStampTreeNodeFlavor)) {
            return node;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
