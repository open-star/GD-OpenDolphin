package open.dolphin.client.editor.stamp;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Tranferable class of the StampTree.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class StampTreeTransferable implements Transferable, ClipboardOwner {

    /** Data Flavor of this class */
    public static DataFlavor stampTreeNodeFlavor = new DataFlavor(open.dolphin.client.editor.stamp.StampTreeNode.class, "Stamp Tree Node");
    /**
     * 
     */
    public static final DataFlavor[] flavors = {StampTreeTransferable.stampTreeNodeFlavor};
    private StampTreeNode node;

    /** Creates new StampTreeTransferable
     * @param node
     */
    public StampTreeTransferable(StampTreeNode node) {
        this.node = node;
    }

    /**
     *
     * @return
     */
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    /**
     *
     * @param flavor
     * @return
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(stampTreeNodeFlavor) ? true : false;
    }

    /**
     *
     * @param flavor
     * @return
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(stampTreeNodeFlavor)) {
            return node;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "StampTreeTransferable";
    }

    /**
     *
     * @param clipboard
     * @param contents
     */
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
