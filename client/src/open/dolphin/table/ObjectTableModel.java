/*
 * ObjectTableModel.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
 * Copyright (C) 2003-2005 Digital Globe, Inc. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package open.dolphin.table;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * テーブルモデル MEMO:モデル
 *
 * @author  kazushi Minagawa, Digital Globe, Inc.
 */
public class ObjectTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 38474601689966826L;
    private String[] columnNames;
    private String[] methodNames;
    private int startNumRows;
    private List objectList;

    /**
     *
     */
    public ObjectTableModel() {
    }

    /** Creates a new instance of ObjectTableModel
     * @param columnNames
     * @param startNumRows
     */
    public ObjectTableModel(String[] columnNames, int startNumRows) {
        this();
        this.columnNames = columnNames;
        this.startNumRows = startNumRows;
    }

    /** Creates a new instance of ObjectTableModel
     * @param columnNames
     * @param startNumRows
     * @param methodNames
     */
    public ObjectTableModel(String[] columnNames, int startNumRows, String[] methodNames) {
        this(columnNames, startNumRows);
        this.methodNames = methodNames;
    }

    //   public void setColumnNames(String[] columnNames) {
    //      this.columnNames = columnNames;
    //  }
    //  public String[] getColumnNames() {
    //     return columnNames;
    // }
    //   public void setMethodNames(String[] methodNames) {
    //      this.methodNames = methodNames;
    //  }
    //  public String[] getMethodNames() {
    //      return methodNames;
    //  }
    //   public void setStartNumRows(int startNumRows) {
    //       this.startNumRows = startNumRows;
    //   }
    /**
     *
     * @return
     */
    protected int getStartNumRows() {
        return startNumRows;
    }

    /**
     *
     * @return
     */
    @Override
    public int getRowCount() {
        int size = getObjectCount();
        return size < getStartNumRows() ? getStartNumRows() : size;
    }

    /**
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * MEMO:Invakeを使用
     * @param row
     * @param col
     * @return
     */
    @Override
    public Object getValueAt(int row, int col) {

        Object target = getObject(row);
        if (target != null && methodNames != null) {
            try {
                Method method = target.getClass().getMethod(methodNames[col], (Class[]) null);
                return method.invoke(target, (Object[]) null);
            } catch (Exception e) {
                return null;
            }
        }
        return target;
    }

    /**
     *
     * @param col
     * @return
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     *
     * @return
     */
    public int getObjectCount() {
        return (objectList == null) ? 0 : objectList.size();
    }

    /**
     *
     * @param row
     * @return
     */
    public Object getObject(int row) {
        return isValidRow(row) ? objectList.get(row) : null;
    }

    /**
     *
     * @param row
     * @return
     */
    public boolean isValidRow(int row) {
        return ((objectList != null) && (row > -1) && (row < objectList.size())) ? true : false;
    }

    /**
     *
     * @param o
     */
    @SuppressWarnings("unchecked")
    public void addRow(Object o) {
        if (objectList == null) {
            objectList = new ArrayList();
        }
        int index = objectList.size();
        objectList.add(o);
        fireTableRowsInserted(index, index);
    }

    /**
     *
     * @param index
     * @param o
     */
    @SuppressWarnings("unchecked")
    public void insertRow(int index, Object o) {
        if (objectList == null) {
            objectList = new ArrayList();
        }

        if ((index == 0 && objectList.isEmpty()) || isValidRow(index)) {
            objectList.add(index, o);
            fireTableRowsInserted(index, index);
        }
    }

    /**
     *
     * @param from
     * @param to
     */
    @SuppressWarnings("unchecked")
    public void moveRow(int from, int to) {
        if (!isValidRow(from) || !isValidRow(to)) {
            return;
        }
        if (from == to) {
            return;
        }
        Object o = objectList.remove(from);
        objectList.add(to, o);
        fireTableRowsUpdated(0, getObjectCount());
    }

    /**
     *
     * @param index
     */
    public void removeRow(int index) {
        if (isValidRow(index)) {
            objectList.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    /**
     * 
     * @param index
     */
    public void deleteRow(int index) {
        removeRow(index);
    }

    /**
     *
     */
    public void clear() {
        if (objectList == null) {
            return;
        }
        int size = objectList.size();
        size = size > 0 ? size - 1 : 0;
        objectList.clear();
        fireTableRowsDeleted(0, size);
    }

    /**
     *
     * @param list
     */
    public void setObjectList(List list) {
        if (objectList != null) {
            objectList.clear();
        }
        objectList = list;
        this.fireTableDataChanged();

    }

    /**
     *
     * @return
     */
    public List getObjectList() {
        return objectList;
    }

    /**
     *
     * @return
     */
    public int getDataSize() {
        return getObjectCount();
    }
}
