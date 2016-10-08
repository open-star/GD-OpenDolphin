package open.dolphin.client;

import java.awt.datatransfer.*;
import java.io.*;

/**
 * Transferable class of the Icon list.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class SchemaListTransferable implements Transferable, ClipboardOwner {

    /** Data Flavor of this class */
    public static DataFlavor schemaListFlavor = new DataFlavor(open.dolphin.client.SchemaList.class, "Schema List");
    /**
     *
     */
    public static final DataFlavor[] flavors = {SchemaListTransferable.schemaListFlavor};
    private SchemaList list;

    /** Creates new SchemaListTransferable
     * @param list 
     */
    public SchemaListTransferable(SchemaList list) {
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
        return flavor.equals(schemaListFlavor) ? true : false;
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

        if (flavor.equals(schemaListFlavor)) {
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
        return "Icon List Transferable";
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
