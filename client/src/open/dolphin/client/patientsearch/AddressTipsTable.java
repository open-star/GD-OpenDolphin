package open.dolphin.client.patientsearch;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.table.ObjectReflectTableModel;

/**
 *
 * @author kazm
 */
public class AddressTipsTable extends JTable {

    /**
     *
     * @param e
     * @return
     */
    @Override
    public String getToolTipText(MouseEvent e) {

        ObjectReflectTableModel<Object> model = (ObjectReflectTableModel<Object>) getModel();
        int row = convertRowIndexToModel(rowAtPoint(e.getPoint()));
        //     int row = rowAtPoint(e.getPoint());
        PatientModel pvt = (PatientModel) model.getObject(row);
        return pvt != null ? pvt.contactAddress() : null;
    }
}
