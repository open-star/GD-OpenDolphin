package open.dolphin.client;

import java.awt.datatransfer.*;
import javax.swing.*;
import open.dolphin.infomodel.SchemaModel;

/**
 * SchemaHolderTransferHandler
 * 
 * @author Kazushi Minagawa
 *
 */
public class SchemaHolderTransferHandler extends TransferHandler {

    private static final long serialVersionUID = -1293765478832142035L;

    /**
     *
     */
    public SchemaHolderTransferHandler() {
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        SchemaHolder source = (SchemaHolder) c;
        KartePane context = source.getKartePane();
        context.setDrragedStamp(new IComponentHolder[]{source});
        context.setDraggedCount(1);
        SchemaModel schema = source.getSchema();
        SchemaList list = new SchemaList();
        list.schemaList = new SchemaModel[]{schema};
        Transferable tr = new SchemaListTransferable(list);
        return tr;
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
     * @param data
     * @param action
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        SchemaHolder test = (SchemaHolder) c;
        KartePane context = test.getKartePane();
        if (action == MOVE
                && context.getDrragedStamp() != null
                && context.getDraggedCount() == context.getDroppedCount()) {
            context.removeSchema(test); // TODO 
        }
        context.setDrragedStamp(null);
        context.setDraggedCount(0);
        context.setDroppedCount(0);
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

    /**
     * スタンプをクリップボードへ転送する。
     * @param comp
     * @param clip
     * @param action
     */
    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        SchemaHolder sh = (SchemaHolder) comp;
        Transferable tr = createTransferable(comp);
        clip.setContents(tr, null);
        if (action == MOVE) {
            KartePane kartePane = sh.getKartePane();
            if (kartePane.getTextPane().isEditable()) {
                kartePane.removeSchema(sh);
            }
        }
    }
}
