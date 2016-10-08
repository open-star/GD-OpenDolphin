package open.dolphin.client;

import open.dolphin.table.OddEvenRowRenderer;
import open.dolphin.table.ObjectReflectTableModel;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import open.dolphin.project.GlobalSettings;

/**
 * Oblect のリストをテーブルに表示するサポートクラス。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class ObjectListTable extends JTable {

    // 束縛プロパティ名
    /**
     *
     */
    public static final String SELECTED_OBJECT = "selectedObjects";
    // 束縛プロパティ名
    /**
     *
     */
    public static final String DOUBLE_CLICKED_OBJECT = "doubleClickedObject";
    // 束縛プロパティ名
    /**
     *
     */
    public static final String OBJECT_VALUE = "objectValue";
    // 束縛サポート
    private PropertyChangeSupport boundSupport;
    // テーブルで選択されたオブジェクトの配列
    private Object[] selectedObjects;
    // テーブル上でダブルクリックされたオブジェクト
    private Object doubleClickedObject;
    // Table Model
    private ObjectReflectTableModel<Object> tableModel;
    // このクラスの JTable

    /**
     * Creates new ObjectListTable
     * @param columnNames
     * @param startNumRows
     * @param methodNames 
     * @param classes
     */
    public ObjectListTable(String[] columnNames, int startNumRows, String[] methodNames, Class[] classes) {
        this(columnNames, startNumRows, methodNames, classes, true);
    }

    /**
     * Creates new ObjectListTable
     * @param columnNames 
     * @param startNumRows
     * @param methodNames
     * @param oddEvenColor
     * @param classes
     */
    public ObjectListTable(String[] columnNames, int startNumRows, String[] methodNames, Class[] classes, boolean oddEvenColor) {
        tableModel = new ObjectReflectTableModel<Object>(columnNames, startNumRows, methodNames, classes);

        setModel(tableModel);

        connect();
        if (oddEvenColor) {
            setDefaultRenderer(Object.class, new OddEvenRowRenderer());
        }
    }

    /**
     * 
     */
    public void clear() {
        tableModel.clear();
    }

    /**
     * 
     * @param prop
     * @param l
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     * 
     * @param prop
     * @param l
     */
    @Override
    public void removePropertyChangeListener(String prop,
            PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     *
     * @return
     */
    public Object[] getSelectedObject() {
        return selectedObjects;
    }

    /**
     * 
     * @return
     */
    public Object getDoubleClickedObject() {
        return doubleClickedObject;
    }

    /**
     * 
     * @param list
     */
    public void setObjectList(List<Object> list) {
        tableModel.setObjectList(list);
    }

    /**
     * 
     * @param columnWidth
     */
    public void setColumnWidth(int[] columnWidth) {
        for (int i = 0; i < columnWidth.length; i++) {
            getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
        }
    }

    /**
     * 
     * @return
     */
    public JScrollPane getScroller() {
        JScrollPane result = new JScrollPane(this);
        result.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        return result;
    }

    /**
     * 
     * @param showHeader
     * @return
     */
    public JPanel getPanel(boolean showHeader) {
        JPanel ret = new JPanel(new BorderLayout());
        ret.add(this, BorderLayout.CENTER);
        if (showHeader) {
            ret.add(getTableHeader(), BorderLayout.NORTH);
        }
        return ret;
    }

    /**
     * 
     * @return
     */
    public JPanel getPanel() {
        return getPanel(true);
    }

    /**
     * 
     */
    protected void connect() {

        // 選択されたオブジェクトを通知する
        // 利用する側で削除ボタンなどの制御をしている可能性があるためヌルの場合も通知する
        // 利用する側は Object[] obj = (Object[])e.getNewValue(), if (obj != null &&
        // obj.length > 0) で判断する
        ListSelectionModel sleModel = getSelectionModel();
        sleModel.addListSelectionListener((ListSelectionListener) EventHandler.create(ListSelectionListener.class, this, "listSelectionChanged", ""));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Object value = (Object) tableModel.getObject(getSelectedRow());
                    if (value != null) {
                        notifyDoubleClickedObject(value);
                    }
                }
            }
        });
    }

    /**
     * 
     * @param e
     */
    public void listSelectionChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            int[] selectedRows = getSelectedRows();
            if (selectedRows.length > 0) {
                List<Object> list = new ArrayList<Object>(1);
                for (int i = 0; i < selectedRows.length; i++) {
                    Object obj = tableModel.getObject(selectedRows[i]);
                    if (obj != null) {
                        list.add(obj);
                    }
                }
                notifySelectedObjects(list.toArray());
            }
        }
    }

    /**
     * 
     * @param selected
     */
    protected void notifySelectedObjects(Object[] selected) {
        if (boundSupport != null) {
            Object[] old = selectedObjects;
            selectedObjects = selected;
            boundSupport.firePropertyChange(SELECTED_OBJECT, old, selectedObjects);
        }
    }

    /**
     * 
     * @param clicked
     */
    protected void notifyDoubleClickedObject(Object clicked) {
        if (boundSupport != null) {
            Object old = doubleClickedObject;
            old = null;	// 強制通知
            doubleClickedObject = clicked;
            boundSupport.firePropertyChange(DOUBLE_CLICKED_OBJECT, old, doubleClickedObject);
        }
    }

    /**
     * 
     * @param obj
     */
    protected void notifyObjectValue(Object obj) {
        if (boundSupport != null) {
            Object old = null;
            boundSupport.firePropertyChange(OBJECT_VALUE, old, obj);
        }
    }

    /**
     * テーブルへのデータ追加と削除の簡易サポート
     * @param add
     */
    public void addRow(Object add) {
        tableModel.addRow(add);
    }

    /**
     *
     * @param c
     */
    public void addRows(Collection c) {
        tableModel.addRows(c);
    }

    /**
     *
     */
    public void deleteSelectedRow() {
        int selected = this.getSelectedRow();
        if (selected > -1) {
            tableModel.deleteRow(selected);
        }
    }

    /**
     *
     */
    public void deleteSelectedRows() {
        Object[] objects = this.getSelectedObject();
        if (objects != null && objects.length > 0) {
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < objects.length; i++) {
                list.add(objects[i]);
            }
            tableModel.deleteRows(list);
        }
    }
}
