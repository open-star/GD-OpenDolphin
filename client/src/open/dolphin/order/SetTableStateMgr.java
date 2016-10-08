package open.dolphin.order;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import open.dolphin.table.ObjectReflectTableModel;

/**
 * SetTableStateMgr
 *
 * @author Minagawa,Kazushi
 *
 */
public class SetTableStateMgr {

    private EmptyState emptyState;
    private HasItemState hasItemState;
    private AbstractSetTableState curState;
    private ItemTablePanel target;
    private JTable setTable;
    private boolean validModel;

    /**
     * 
     * @param target
     * @param setTable
     * @param deleteBtn
     * @param clearBtn
     * @param stampNameField
     */
    public SetTableStateMgr(ItemTablePanel target, JTable setTable, JButton deleteBtn, JButton clearBtn, JTextField stampNameField) {
        this.target = target;
        emptyState = new EmptyState(setTable, deleteBtn, clearBtn, stampNameField);
        hasItemState = new HasItemState(setTable, deleteBtn, clearBtn, stampNameField);
        this.setTable = setTable;
    }

    /**
     *
     */
    public void checkState() {
        ObjectReflectTableModel<Object> tableModel = (ObjectReflectTableModel<Object>) setTable.getModel();
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
