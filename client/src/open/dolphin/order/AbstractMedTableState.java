package open.dolphin.order;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

import open.dolphin.table.ObjectReflectTableModel;

/**
 * AbsractSetTableState
 * 
 * @author Kazushi Minagawa
 *
 */
public abstract class AbstractMedTableState {

    /**
     *
     */
    protected JTable medTable;
    /**
     *
     */
    protected JButton deleteBtn;
    /**
     *
     */
    protected JButton clearBtn;
    /**
     *
     */
    protected JTextField stampNameField;
    /**
     *
     */
    protected JLabel stateLabel;

    /**
     *
     * @param medTable
     * @param deleteBtn
     * @param clearBtn
     * @param stampNameField
     * @param stateLabel
     */
    public AbstractMedTableState(JTable medTable, JButton deleteBtn, JButton clearBtn,
            JTextField stampNameField, JLabel stateLabel) {
        this.medTable = medTable;
        this.deleteBtn = deleteBtn;
        this.clearBtn = clearBtn;
        this.stampNameField = stampNameField;
        this.stateLabel = stateLabel;
    }

    /**
     *
     * @return
     */
    public ObjectReflectTableModel<Object> getTableModel() {
        return (ObjectReflectTableModel<Object>) medTable.getModel();
    }

    /**
     *
     */
    public abstract void enter();

    /**
     * 
     * @return
     */
    public abstract boolean isValidModel();
}
