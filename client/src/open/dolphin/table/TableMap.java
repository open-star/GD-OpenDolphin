/** 
 * In a chain of data manipulators some behaviour is common. TableMap
 * provides most of this behavour and can be subclassed by filters
 * that only need to override a handful of specific methods. TableMap 
 * implements TableModel by routing all requests to its model, and
 * TableModelListener by routing all events to its listeners. Inserting 
 * a TableMap which has not been subclassed into a chain of table filters 
 * should have no effect.
 *
 * @version 1.4 12/17/97
 * @author Philip Milne */
package open.dolphin.table;

import javax.swing.table.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

/**
 * TableMap　MEMO:モデル
 *
 */
public class TableMap extends AbstractTableModel
        implements TableModelListener {

    private static final long serialVersionUID = -7544954445280970228L;
    /**
     *
     */
    protected TableModel model;

    /**
     * 
     * @return
     */
    public TableModel getModel() {
        return model;
    }

    /**
     *
     * @param model
     */
    public void setModel(TableModel model) {
        this.model = model;
        model.addTableModelListener(this);
    }

    // By default, implement TableModel by forwarding all messages 
    // to the model. 
    /**
     *
     * @param aRow
     * @param aColumn
     * @return
     */
    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn);
    }

    /**
     * 
     * @param aValue
     * @param aRow
     * @param aColumn
     */
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn);
    }

    /**
     *
     * @return
     */
    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }

    /**
     *
     * @return
     */
    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount();
    }

    /**
     *
     * @return
     */
    public int getDataCount() {
        if (model == null) {
            return 0;

        } else if (model instanceof open.dolphin.table.ObjectTableModel) {
            return ((open.dolphin.table.ObjectTableModel) model).getObjectCount();

        }

        return model.getColumnCount();
    }

    /**
     *
     * @param aColumn
     * @return
     */
    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn);
    }

    /**
     *
     * @param aColumn
     * @return
     */
    @SuppressWarnings("unchecked")
    public Class getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn);
    }

    /**
     *
     * @param row
     * @param column
     * @return
     */
    public boolean isCellEditable(int row, int column) {
        return model.isCellEditable(row, column);
    }

    /**
     *  Implementation of the TableModelListener interface, 
     * By default forward all events to all the listeners. 
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
}
