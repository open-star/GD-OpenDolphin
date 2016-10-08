package open.dolphin.order;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.table.ObjectTableModel;
import open.dolphin.client.editor.diagnosis.DiagnosisTablePanel;
import open.dolphin.log.LogWriter;

/**
 * RegisteredDiagnosisTransferHandler
 *
 * @author Minagawa,Kazushi
 *
 */
public class RegisteredDiagnosisTransferHandler extends TransferHandler {

    private DataFlavor registeredDiagnosisFlavor = RegisteredDiagnosisTransferable.registeredDiagnosisFlavor;
    private JTable sourceTable;
    private boolean shouldRemove;
    private int fromIndex;
    private int toIndex;
    private DiagnosisTablePanel editor;

    /**
     *
     * @param editor
     */
    public RegisteredDiagnosisTransferHandler(DiagnosisTablePanel editor) {
        this.editor = editor;
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        sourceTable = (JTable) c;
        ObjectTableModel tableModel = (ObjectTableModel) sourceTable.getModel();
        fromIndex = sourceTable.getSelectedRow();
        RegisteredDiagnosisModel dragItem = (RegisteredDiagnosisModel) tableModel.getObject(fromIndex);
        return dragItem != null ? new RegisteredDiagnosisTransferable(dragItem) : null;
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
     * @param t
     * @return
     */
    @Override
    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {
                //     RegisteredDiagnosisModel dropItem = (RegisteredDiagnosisModel) t.getTransferData(registeredDiagnosisFlavor);
                JTable dropTable = (JTable) c;
                ObjectTableModel tableModel = (ObjectTableModel) dropTable.getModel();
                toIndex = dropTable.getSelectedRow();
                shouldRemove = dropTable == sourceTable ? true : false;
                if (shouldRemove) {
                    tableModel.moveRow(fromIndex, toIndex);
                    if (editor != null) {
                        editor.redraw();
                    }
                }
                sourceTable.getSelectionModel().setSelectionInterval(toIndex, toIndex);
                return true;

            } catch (Exception ioe) {
                LogWriter.error(getClass(), ioe);
            }
        }

        return false;
    }

    /**
     *
     * @param c
     * @param data
     * @param action
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
   //     if (action == MOVE && shouldRemove) {
   //     }
        shouldRemove = false;
        fromIndex = -1;
        toIndex = -1;
    }

    /**
     *
     * @param c
     * @param flavors
     * @return
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        JTable dropTable = (JTable) c;
        ObjectTableModel tableModel = (ObjectTableModel) dropTable.getModel();
        if (tableModel.getObject(dropTable.getSelectedRow()) != null) {
            for (int i = 0; i < flavors.length; i++) {
                if (registeredDiagnosisFlavor.equals(flavors[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
