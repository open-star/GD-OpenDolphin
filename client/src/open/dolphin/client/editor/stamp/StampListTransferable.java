package open.dolphin.client.editor.stamp;

import java.awt.datatransfer.*;
import java.io.*;

/**
 * Transferable class of the PTrain.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class StampListTransferable implements Transferable, ClipboardOwner {

    /** Data Flavor of this class */
    public static DataFlavor stampListFlavor = new DataFlavor(open.dolphin.client.editor.stamp.StampList.class, "Stamp List");
    /**
     *
     */
    public static final DataFlavor[] flavors = {StampListTransferable.stampListFlavor};
    private StampList list;

    /** Create new StampListTransferable
     * @param list 
     */
    public StampListTransferable(StampList list) {
        this.list = list;
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
        return flavor.equals(stampListFlavor) ? true : false;
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

        if (flavor.equals(stampListFlavor)) {
            return list;
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
        return "Stamp List Transferable";
    }

    /**
     *
     * @param clipboard
     * @param contents
     */
    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
