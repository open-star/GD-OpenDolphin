package open.dolphin.table;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Table 選択のアダプタクラス。
 * 
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class TableSelectionAdapter implements ListSelectionListener, MouseListener {

    private JTable table;
    private ObjectReflectTableModel<Object> tableModel;
    private ITableSelectionListener listener;

    /**
     *
     * @param table
     * @param listener
     */
    public TableSelectionAdapter(JTable table, ITableSelectionListener listener) {

        this.table = table;
        this.tableModel = (ObjectReflectTableModel<Object>) this.table.getModel();
        this.listener = listener;

        ListSelectionModel slm = this.table.getSelectionModel();
        slm.addListSelectionListener(this);

        this.table.addMouseListener(this);
    }

    /**
     *
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            int[] selectedRows = this.table.getSelectedRows();
            if (selectedRows.length > 0) {
                List<Object> list = new ArrayList<Object>(1);
                for (int i = 0; i < selectedRows.length; i++) {
                    Object obj = this.tableModel.getObject(selectedRows[i]);
                    if (obj != null) {
                        list.add(obj);
                    }
                }
                listener.rowSelectionChanged(list.toArray());
            }
        }
    }

    /**
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Object value = (Object) tableModel.getObject(table.getSelectedRow());
            if (value != null) {
                this.listener.rowDoubleClicked(value);
            }
        }
    }

    /**
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    public void mouseExited(MouseEvent e) {
    }
}
