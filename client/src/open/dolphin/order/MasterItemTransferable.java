package open.dolphin.order;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import open.dolphin.infomodel.MasterItem;

/**
 * マスタアイテム Transferable クラス。
 * @author kazm
 */
public class MasterItemTransferable implements Transferable {

    /**
     *
     */
    public static DataFlavor masterItemFlavor = new DataFlavor(open.dolphin.infomodel.MasterItem.class, "MasterItem");
    /**
     *
     */
    public static final DataFlavor[] flavors = {masterItemFlavor};
    private MasterItem masterItem;

    /**
     *
     * @param masterItem
     */
    public MasterItemTransferable(MasterItem masterItem) {
        this.masterItem = masterItem;
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
        return flavor.equals(masterItemFlavor) ? true : false;
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

        if (flavor.equals(masterItemFlavor)) {
            return masterItem;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
