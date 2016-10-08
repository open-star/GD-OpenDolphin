/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.table;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.dao.SqlMasterDao.Availability;
import open.dolphin.infomodel.MedicineEntry;

/**
 *
 * @author
 */
public class OddEvenRowRendererWithExpire extends OddEvenRowRenderer {

    private Color updatedColor;
    private Color noLongerAvailableColor;
    private Color noCodeColor;

    /**
     *
     */
    public OddEvenRowRendererWithExpire() {
        super();
        setUpdatedColor(Color.yellow);
        setNoLongerAvailableColor(Color.red);
        setNoCodeColor(Color.gray);
    }

    /**
     *
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Date now = new Date();
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setForeground(table.getForeground());
            if (row % 2 == 0) {
                setBackground(getEvenColor());
            } else {
                setBackground(getOddColor());
            }
        }
        //-------------------------------------------------------
        if (value != null) {
            if (value instanceof java.lang.String) {
                this.setText((String) value);
                if (column == 0) {
                    final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
                    List<MedicineEntry> collection = new ArrayList<MedicineEntry>();
                    Availability available = dao.isCodeNoLongerAvailableFromOrca((String) value, now);
                    switch (available) {
                        case AVAILABLE:
                            break;
                        case NO_CODE:
                            this.setBackground(getNoCodeColor());
                            break;
                        case NO_LONGER_AVAILABLE:
                            this.setBackground(getNoLongerAvailableColor());
                            if (dao.getAlternateEntry((String) value, collection)) {
                                this.setBackground(getUpdatedColor());
                            }
                    }
                }
            } else {
                this.setText(value.toString());
            }
        } else {
            this.setText("");
        }
        return this;
    }

    /**
     * @param updatedColor
     */
    public void setUpdatedColor(Color updatedColor) {
        this.updatedColor = updatedColor;
    }

    /**
     * @return Returns the oddColor.
     */
    public Color getUpdatedColor() {
        return updatedColor;
    }

    /**
     * @param noLongerAvailableColor
     */
    public void setNoLongerAvailableColor(Color noLongerAvailableColor) {
        this.noLongerAvailableColor = noLongerAvailableColor;
    }

    /**
     * @return Returns the oddColor.
     */
    public Color getNoLongerAvailableColor() {
        return noLongerAvailableColor;
    }

    /**
     * @param noCodeColor 
     */
    public void setNoCodeColor(Color noCodeColor) {
        this.noCodeColor = noCodeColor;
    }

    /**
     * @return Returns the oddColor.
     */
    public Color getNoCodeColor() {
        return noCodeColor;
    }
}
