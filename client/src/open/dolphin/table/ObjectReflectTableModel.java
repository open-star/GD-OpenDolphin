package open.dolphin.table;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import open.dolphin.log.LogWriter;

/**
 * リフレクション　廃止予定　　MEMO:モデル
 *
 * @param <T>
 * @author Minagawa,Kazushi
 */
public class ObjectReflectTableModel<T> extends AbstractTableModel {

    private static final long serialVersionUID = -8280948755982277457L;
    private String[] columnNames;    // カラム名配列
    private String[] methodNames;    // 属性値を取得するためのメソッド名
    private Class[] columnClasses;    // カラムクラス配列
    private int startNumRows;    // 開始時行数
    private int columnCount;    // カラム数
    private List<T> objectList;    // データオブジェクトリスト

    /**
     * ObjectReflectTableModelを生成する。
     * @param columnNames カラム名配列
     * @param startNumRows	開始時行数
     * @param methodNames メソッド名配列
     * @param columnClasses カラムクラス配列
     */
    public ObjectReflectTableModel(String[] columnNames, int startNumRows, String[] methodNames, Class[] columnClasses) {
        this.columnNames = columnNames;
        this.startNumRows = startNumRows;
        this.methodNames = methodNames;
        this.columnClasses = columnClasses;
        if (this.columnNames != null) {
            columnCount = columnNames.length;
        }
        objectList = new ArrayList<T>();
    }

    /**
     * カラム名なしでTableModelを生成する。
     * @param columnCount カラム数
     * @param startNumRows 開始時行数
     * @param methodNames メソッド名配列
     * @param columnClasses カラムクラス配列
     */
    public ObjectReflectTableModel(int columnCount, int startNumRows, String[] methodNames, Class[] columnClasses) {
        this.columnCount = columnCount;
        this.startNumRows = startNumRows;
        this.methodNames = methodNames;
        this.columnClasses = columnClasses;
        objectList = new ArrayList<T>();
    }

    /**
     * カラム名を返す。
     * @param index カラムインデックス
     */
    @Override
    public String getColumnName(int index) {
        return (columnNames != null && index < columnNames.length) ? columnNames[index] : null;
    }

    /**
     * カラム数を返す。
     * @return カラム数
     */
    @Override
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * 行数を返す。
     * @return 行数
     */
    @Override
    public int getRowCount() {
        return (objectList != null && objectList.size() > startNumRows) ? objectList.size() : startNumRows;
    }

    /**
     * カラムのクラス型を返す。
     * @param index
     */
    @Override
    public Class getColumnClass(int index) {
        return (columnClasses != null && index < columnClasses.length) ? columnClasses[index] : String.class;
    }

    /**
     * オブジェクトの値を返す。
     * MEMO:Invakeを使用
     * @param row 行インデックス
     * @param col　カラムインデックス
     * @return
     */
    @Override
    public Object getValueAt(int row, int col) {

        Object object = getObject(row);
        if (object != null && methodNames != null && col < methodNames.length) {
            try {
                Method targetMethod = object.getClass().getMethod(methodNames[col], (Class[]) null);
                return targetMethod.invoke(object, (Object[]) null);
            } catch (Exception e) {
                LogWriter.error(getClass(), e);
            }
        }
        return null;
    }

    /**
     * データリストを設定する。
     * @param objectList データリスト
     */
    public void setObjectList(List<T> objectList) {
        if (this.objectList != null) {
            this.objectList.clear();
            this.objectList = null;
        }
        this.objectList = objectList; // 参照しているのみ
        this.fireTableDataChanged();
    }

    /**
     *
     * @param index
     * @return
     */
    public String getMethodName(int index) {
        return methodNames[index];
    }

    /**
     * コンストラクト後にカラム名を変更する。
     * @param columnName 
     * @param col
     */
    public void setColumnName(String columnName, int col) {
        if (col >= 0 && col < columnNames.length) {
            columnNames[col] = columnName;
            this.fireTableStructureChanged();
        }
    }

    /**
     * コンストラクト後にメソッドを変更する。
     * @param methodName 
     * @param col
     */
    public void setMethodName(String methodName, int col) {
        if (col >= 0 && col < methodNames.length) {
            methodNames[col] = methodName;
            if (objectList != null) {
                this.fireTableDataChanged();
            }
        }
    }

    /**
     * データリストを返す。
     * @return データリスト
     */
    public List<T> getObjectList() {
        return objectList;
    }

    /**
     * データリストをクリアする。
     */
    public void clear() {
        if (objectList != null) {
            objectList.clear();
            this.fireTableDataChanged();
        }
    }

    /**
     * 指定された行のオブジェクトを返す。
     * @param index 行インデックス
     * @return オブジェクト
     */
    public T getObject(int index) {
        return (objectList != null && index >= 0 && index < objectList.size()) ? objectList.get(index) : null;
    }

    /**
     * オブジェクト数(=データ数)を返す
     * @return オブジェクト数
     */
    public int getObjectCount() {
        return objectList != null ? objectList.size() : 0;
    }

    /**
     *
     * @param add
     */
    public void addRow(T add) {
        if (add != null) {
            if (objectList == null) {
                objectList = new ArrayList<T>();
            }
            int index = objectList.size();
            objectList.add(add);
            this.fireTableRowsInserted(index, index);
        }
    }

    /**
     *
     * @param index
     * @param add
     */
    public void addRow(int index, T add) {
        if (add != null && index > -1 && objectList != null) {
            if ((objectList.isEmpty() && index == 0) || (index < objectList.size())) {
                objectList.add(index, add);
                this.fireTableRowsInserted(index, index);
            }
        }
    }

    /**
     *
     * @param index
     * @param o
     */
    public void insertRow(int index, T o) {
        addRow(index, o);
    }

    /**
     *
     * @param from
     * @param to
     */
    public void moveRow(int from, int to) {
        if (!isValidRow(from) || !isValidRow(to)) {
            return;
        }
        if (from == to) {
            return;
        }
        T o = objectList.remove(from);
        objectList.add(to, o);
        fireTableRowsUpdated(0, getObjectCount());
    }

    /**
     *
     * @param c
     */
    public void addRows(Collection c) {
        if (c != null) {
            if (objectList == null) {
                objectList = new ArrayList<T>();
            }
            int first = objectList.size();
            for (Iterator iter = c.iterator(); iter.hasNext();) {
                objectList.add((T) iter.next());
            }
            int last = objectList.size() - 1;
            this.fireTableRowsInserted(first, last);
        }
    }

    /**
     *
     * @param index
     */
    public void deleteRow(int index) {
        if (index > -1 && index < objectList.size()) {
            objectList.remove(index);
            this.fireTableRowsDeleted(index, index);
        }
    }

    /**
     *
     * @param delete
     */
    public void deleteRow(T delete) {
        if (objectList != null) {
            if (objectList.remove(delete)) {
                this.fireTableDataChanged();
            }
        }
    }

    /**
     *
     * @param c
     */
    public void deleteRows(Collection c) {
        if (objectList != null) {
            if (c != null) {
                objectList.removeAll(c);
                this.fireTableDataChanged();
            }
        }
    }

    /**
     *
     * @param o
     * @return
     */
    public int getIndex(Object o) {
        int index = 0;
        boolean found = false;
        if (objectList != null && o != null) {
            for (Object obj : objectList) {
                if (obj == o) {
                    found = true;
                    break;
                } else {
                    index++;
                }
            }
        }
        return found ? index : -1;
    }

    /**
     *
     * @param row
     * @return
     */
    public boolean isValidRow(int row) {
        return ((objectList != null) && (row > -1) && (row < objectList.size())) ? true : false;
    }
}
