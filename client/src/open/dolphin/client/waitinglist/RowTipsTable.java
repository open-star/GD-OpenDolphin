package open.dolphin.client.waitinglist;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.table.ObjectReflectTableModel;

/**
 *
 * @author kazm
 */
public class RowTipsTable extends JTable {

    /**
     * 
     * @param e
     * @return
     */
    @Override
    public String getToolTipText(MouseEvent e) {

        ObjectReflectTableModel<Object> model = (ObjectReflectTableModel<Object>) getModel();
        int row = convertRowIndexToModel(rowAtPoint(e.getPoint()));
        //      int row = rowAtPoint(e.getPoint());
        PatientVisitModel pvt = (PatientVisitModel) model.getObject(row);
        return pvt != null ? pvt.getPatient().getKanaName() : null;
    }
}
