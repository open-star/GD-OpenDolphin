package open.dolphin.order;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

import open.dolphin.table.ObjectReflectTableModel;

/**
 * SetTableStateMgr
 * 
 * @author Minagawa,Kazushi
 *
 */
public class MedTableStateMgr {

    private MedEmptyState emptyState;
    private MedHasItemState hasItemState;
    private AbstractMedTableState curState;
    private PharmaceuticalsTablePanel target;
    private JTable medTable;
    private boolean validModel;

    /**
     *
     * @param target
     * @param medTable
     * @param deleteBtn
     * @param clearBtn
     * @param stampNameField
     * @param stateLabel
     */
    public MedTableStateMgr(PharmaceuticalsTablePanel target, JTable medTable,
            JButton deleteBtn, JButton clearBtn, 
            JTextField stampNameField, JLabel stateLabel) {
        this.target = target;
        emptyState = new MedEmptyState(medTable, deleteBtn, clearBtn, stampNameField, stateLabel);
        hasItemState = new MedHasItemState(medTable, deleteBtn, clearBtn, stampNameField, stateLabel);
        this.medTable = medTable;
    }

    /**
     * 
     */
    public void checkState() {
        
        ObjectReflectTableModel<Object> tableModel = (ObjectReflectTableModel<Object>) medTable.getModel();
        int cnt = tableModel.getObjectCount();
        if (cnt > 0) {
            curState = hasItemState;
            curState.enter();
        } else if (cnt == 0) {
            curState = emptyState;
            curState.enter();
        }
        boolean newValid = curState.isValidModel();
        
        if (newValid != validModel) {
            validModel = newValid;
            target.setValidModel(validModel);
        }
    }
}
