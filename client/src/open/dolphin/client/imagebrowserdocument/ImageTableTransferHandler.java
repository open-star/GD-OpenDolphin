package open.dolphin.client.imagebrowserdocument;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import open.dolphin.client.ImageEntry;
import open.dolphin.client.ImageEntryTransferable;

/**
 * SchemaHolderTransferHandler
 * 
 * @author Kazushi Minagawa
 *
 */
public class ImageTableTransferHandler extends TransferHandler {

    private static final long serialVersionUID = -1293765478832142035L;

    /**
     *
     */
    public ImageTableTransferHandler() {
    }

    /**
     *
     * @param c　コンポーネント
     * @return　ドラッグ＆ドロップ出来るオブジェクト
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        JTable imageTable = (JTable) c;
        int row = imageTable.getSelectedRow();
        int col = imageTable.getSelectedColumn();
        if (row != -1 && col != -1) {
            ImageEntry entry = (ImageEntry) imageTable.getValueAt(row, col);
            if (entry != null) {
                Transferable tr = new ImageEntryTransferable(entry);
                return tr;
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     *
     * @param c
     * @return　COPY_OR_MOVE
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
    }

    /**
     *
     * @param c　コンポーネント
     * @param flavors　フレーバー
     * @return　インポート可能か
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return false;
    }

    /**
     *
     * @param comp　コンポーネント
     * @param clip　
     * @param action
     */
    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
    }
}
