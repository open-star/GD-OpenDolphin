/*
 package open.dolphin.client.editor.stamp;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import open.dolphin.infomodel.ModuleModel;
*/
/**
 * Tranferable class of the Stamp.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
/*
public final class StampTransferable implements Transferable, ClipboardOwner {


    public static DataFlavor stampFlavor = new DataFlavor(open.dolphin.infomodel.ModuleModel.class, "Stamp");
    public static final DataFlavor[] flavors = {StampTransferable.stampFlavor};
    private ModuleModel stamp;


    public StampTransferable(ModuleModel stamp) {
        this.stamp = stamp;
    }

    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(stampFlavor) ? true : false;
    }

    @Override
    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(stampFlavor)) {
            return stamp;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }

    @Override
    public String toString() {
        return "StampTransferable";
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }
}
*/