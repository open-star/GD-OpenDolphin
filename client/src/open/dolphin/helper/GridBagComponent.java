package open.dolphin.helper;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.util.List;

/**
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class GridBagComponent {

    private Component component;
    private int row;
    private int col;
    private int rowSpan = 1;
    private int colSpan = 1;
    private int anchor;
    private int fill = GridBagConstraints.NONE;
    private double rowWeight = 0.0;
    private double colWeight = 0.0;

    /** Creates a new instance of GridBagComponent */
    public GridBagComponent() {
    }

    /**
     *
     * @param comp
     * @param row
     * @param col
     * @param rowSpan
     * @param colSpan
     * @param anchor
     * @param fill
     * @param list
     */
    public static void setConstrain(Component comp, int row, int col, int rowSpan, int colSpan, int anchor, int fill, List<GridBagComponent> list) {
        GridBagComponent gbc = new GridBagComponent();
        gbc.setComponent(comp);
        gbc.setRow(row);
        gbc.setCol(col);
        gbc.setRowSpan(rowSpan);
        gbc.setColSpan(colSpan);
        gbc.setAnchor(anchor);
        gbc.setFill(fill);
        list.add(gbc);
    }

    /**
     *
     * @param comp
     * @param row
     * @param col
     * @param rowSpan
     * @param colSpan
     * @param anchor
     * @param fill
     * @param roww
     * @param colw
     * @param list
     */
    public static void setConstrain(Component comp, int row, int col, int rowSpan, int colSpan, int anchor, int fill, double roww, double colw, List<GridBagComponent> list) {
        GridBagComponent gbc = new GridBagComponent();
        gbc.setComponent(comp);
        gbc.setRow(row);
        gbc.setCol(col);
        gbc.setRowSpan(rowSpan);
        gbc.setColSpan(colSpan);
        gbc.setAnchor(anchor);
        gbc.setFill(fill);
        gbc.setRowWeight(roww);
        gbc.setColWeight(colw);
        list.add(gbc);
    }

    /**
     *
     * @return
     */
    public Component getComponent() {
        return component;
    }

    /**
     *
     * @param component
     */
    public void setComponent(Component component) {
        this.component = component;
    }

    /**
     *
     * @param row
     * @param col
     */
    public void setRowCol(int row, int col) {
        setRow(row);
        setCol(col);
    }

    /**
     *
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     *
     * @return
     */
    public int getCol() {
        return col;
    }

    /**
     *
     * @param col
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     *
     * @return
     */
    public int getRowSpan() {
        return rowSpan;
    }

    /**
     *
     * @param rowSpan
     */
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    /**
     *
     * @return
     */
    public int getColSpan() {
        return colSpan;
    }

    /**
     *
     * @param colSpan
     */
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    /**
     *
     * @return
     */
    public int getAnchor() {
        return anchor;
    }

    /**
     *
     * @param anchor
     */
    public void setAnchor(int anchor) {
        this.anchor = anchor;
    }

    /**
     *
     * @return
     */
    public int getFill() {
        return fill;
    }

    /**
     *
     * @param fill
     */
    public void setFill(int fill) {
        this.fill = fill;
    }

    /**
     *
     * @return
     */
    public double getRowWeight() {
        return rowWeight;
    }

    /**
     *
     * @param rowWeight
     */
    public void setRowWeight(double rowWeight) {
        this.rowWeight = rowWeight;
    }

    /**
     *
     * @return
     */
    public double getColWeight() {
        return colWeight;
    }

    /**
     *
     * @param colWeight
     */
    public void setColWeight(double colWeight) {
        this.colWeight = colWeight;
    }
}
