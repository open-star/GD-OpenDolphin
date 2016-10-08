package open.dolphin.client;

import open.dolphin.client.editor.stamp.StampTreeNode;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import open.dolphin.log.LogWriter;

/**
 * Tranferable class of the StampTreeNode.
 *
 * @author  Kazushi Minagawa
 */
public class LocalStampTreeNodeTransferable implements Transferable {

    /** Data Flavor of this class */
    public static DataFlavor localStampTreeNodeFlavor;

    static {
        try {
            localStampTreeNodeFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=open.dolphin.client.editor.stamp.StampTreeNode");    //リフレクション
        } catch (Exception e) {
             LogWriter.error("LocalStampTreeNodeTransferable", "");
        }
    }

    ;
    /**
     * 
     */
    public static final DataFlavor[] flavors = {LocalStampTreeNodeTransferable.localStampTreeNodeFlavor};
    private StampTreeNode node;

    /**
     * Creates new StampTreeTransferable
     * @param node
     */
    public LocalStampTreeNodeTransferable(StampTreeNode node) {
        this.node = node;
    }

    /**
     *
     * @return
     */
    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    /**
     *
     * @param flavor
     * @return
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(localStampTreeNodeFlavor) ? true : false;
    }

    /**
     *
     * @param flavor
     * @return
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    @Override
    public synchronized Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {

        if (flavor.equals(localStampTreeNodeFlavor)) {
            return node;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
