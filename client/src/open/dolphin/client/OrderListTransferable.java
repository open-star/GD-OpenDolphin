package open.dolphin.client;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Transferable class of the PTrain.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class OrderListTransferable implements Transferable, ClipboardOwner {

    /** Data Flavor of this class */
    public static DataFlavor orderListFlavor = new DataFlavor(open.dolphin.client.OrderList.class, "Order List");
    /**
     * 
     */
    public static final DataFlavor[] flavors = {OrderListTransferable.orderListFlavor};
    private OrderList list;

    /**
     * Creates new OrderListTransferable
     * @param list
     */
    public OrderListTransferable(OrderList list) {
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
        return flavor.equals(orderListFlavor) ? true : false;
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

        if (flavor.equals(orderListFlavor)) {
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
        return "Order List Transferable";
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
