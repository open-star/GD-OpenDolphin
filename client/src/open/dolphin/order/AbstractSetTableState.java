package open.dolphin.order;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import open.dolphin.table.ObjectReflectTableModel;

/**
 * AbsractSetTableState
 * 
 * @param <T>
 * @author Kazushi Minagawa
 *
 */
public abstract class AbstractSetTableState<T> {

    /**
     *
     */
    protected JTable setTable;
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
     * @param setTable
     * @param deleteBtn
     * @param clearBtn
     * @param stampNameField
     */
    public AbstractSetTableState(JTable setTable, JButton deleteBtn, JButton clearBtn, JTextField stampNameField) {
        this.setTable = setTable;
        this.deleteBtn = deleteBtn;
        this.clearBtn = clearBtn;
        this.stampNameField = stampNameField;
    }

    /**
     *
     * @return
     */
    public ObjectReflectTableModel<T> getTableModel() {
        return (ObjectReflectTableModel<T>) setTable.getModel();
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
