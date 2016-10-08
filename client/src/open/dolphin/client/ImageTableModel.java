package open.dolphin.client;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * ImageTableModel　MEMO:モデル
 *
 * @author Minagawa, Kazushi
 */
public class ImageTableModel extends AbstractTableModel {

    private String[] columnNames;
    private int columnCount;
    private List imageList;

    /**
     *
     * @param columnNames
     * @param columnCount
     */
    public ImageTableModel(String[] columnNames, int columnCount) {
        this.columnNames = columnNames;
        this.columnCount = columnCount;
    }

    /**
     *
     * @param col　カラム位置
     * @return　カラムの名前
     */
    @Override
    public String getColumnName(int col) {
        return (columnNames != null && col < columnNames.length) ? columnNames[col] : null;
    }

    /**
     *
     * @return　カラム数
     */
    @Override
    public int getColumnCount() {
        return columnCount;
    }

    /**
     *
     * @return　ローの数
     */
    @Override
    public int getRowCount() {

        if (imageList == null) {
            return 0;
        }

        int size = imageList.size();
        int rowCount = size / columnCount;

        return ((size % columnCount) != 0) ? rowCount + 1 : rowCount;
    }

    /**
     *
     * @param row　ロー位置
     * @param col　カラム位置
     * @return　ローとカラムで示される位置にあるオブジェクト
     */
    @Override
    public Object getValueAt(int row, int col) {
        int index = row * columnCount + col;
        if (!isValidIndex(index)) {
            return null;
        }

        ImageEntry entry = (ImageEntry) imageList.get(index);
        return (Object) entry;
    }

    /**
     *
     * @param list　イメージリスト
     */
    public void setImageList(List list) {
        if (imageList != null) {
            imageList.clear();
            imageList = null;
        }
        imageList = list;
        this.fireTableDataChanged();
    }

    /**
     *
     * @return　イメージ一覧
     */
    public List getImageList() {
        return imageList;
    }

    /**
     *
     * @param index　インデックス
     * @return　イメージリストのインデックスが範囲内
     */
    private boolean isValidIndex(int index) {
        return (imageList == null || index < 0 || index >= imageList.size()) ? false : true;
    }

    /**
     * 
     */
    public void clear() {
        if (imageList != null) {
            imageList.clear();
            this.fireTableDataChanged();
        }
    }
}
