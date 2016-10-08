/*
 * Created on 2005/09/03
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package open.dolphin.table;

import java.lang.reflect.Method;

import javax.swing.table.AbstractTableModel;
import open.dolphin.log.LogWriter;

/**
 * プロパティテーブルモデル　MEMO:モデル
 * 
 * @author Minagawa,Kazushi
 */
public class PropertyTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 2526602991294064807L;
    private String[] columnNames;
    private String[] attrNames;
    private String[] methodNames;
    private Object target;

    /**
     * 
     * @param columnNames
     * @param attrNames
     * @param methodNames
     */
    public PropertyTableModel(String[] columnNames, String[] attrNames, String[] methodNames) {
        super();
        this.columnNames = columnNames;
        this.attrNames = attrNames;
        this.methodNames = methodNames;
    }

    /**
     *
     * @param attrNames
     * @param methodNames
     */
    public PropertyTableModel(String[] attrNames, String[] methodNames) {
        this(new String[]{"項目", "値"}, attrNames, methodNames);
    }

    /**
     *
     * @param col
     * @return
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     *
     * @return　length
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     *
     * @return
     */
    public int getRowCount() {
        return attrNames.length;
    }

    /**
     * MEMO:Invakeを使用
     * @param row
     * @param col
     * @return
     */
    public Object getValueAt(int row, int col) {

        if (col == 0) {
            return attrNames[row];
        }

        if (target != null && methodNames != null) {

            try {
                Method targetMethod = target.getClass().getMethod(methodNames[row], (Class[]) null);
                return targetMethod.invoke(target, (Object[]) null);

            } catch (Exception e) {
                LogWriter.error(getClass(), e);
                return null;
            }
        }
        return null;
    }

    /**
     *
     * @param o
     */
    public void setObject(Object o) {
        this.target = o;
        this.fireTableDataChanged();
    }
}
