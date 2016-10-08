/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import open.dolphin.project.GlobalSettings;
import open.dolphin.infomodel.IInfoModel;

/**
 *
 * @author
 */
public class DocumentHistoryRenderer extends JLabel implements TableCellRenderer {

    private static final Color DEFAULT_ODD_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.ODD);
    private static final Color DEFAULT_EVENN_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.EVEN);
    private static final Color SEND_HISTORY_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.SEND_HISTORY);
    private static final Color TEMPORARY_HISTORY_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.TEMPORARY_HISTORY);
    private Color oddColor;
    private Color evenColor;
    private Color sendHistoryColor;
    private Color temporaryColor;

    /**
     * 
     */
    public DocumentHistoryRenderer() {
        this(DEFAULT_ODD_COLOR, DEFAULT_EVENN_COLOR, SEND_HISTORY_COLOR, TEMPORARY_HISTORY_COLOR);
    }

    /**
     *
     * @param oddColor
     * @param evenColor
     * @param sendColor
     * @param temporaryColor
     */
    public DocumentHistoryRenderer(Color oddColor, Color evenColor, Color sendColor, Color temporaryColor) {
        setOpaque(true);
        setOddColor(oddColor);
        setEvenColor(evenColor);
        setSendHistoryColor(sendColor);
        setTemporaryColor(temporaryColor);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            if (value instanceof java.lang.String) {
                this.setText((String) value);
                if (column == 3) {
                    if (((String) value).equals(IInfoModel.STATUS_FINAL_READABLE)) {
                        setBackground(getSendHistoryColor());
                    } else {
                        if (((String) value).equals(IInfoModel.STATUS_MODIFIED_READABLE)) {
                            setBackground(getEvenColor());
                        } else {
                            setBackground(getTemporaryColor());
                        }
                    }
                } else {
                    setForeground(table.getForeground());
                    if (row % 2 == 0) {
                        setBackground(getEvenColor());
                    } else {
                        setBackground(getOddColor());
                    }
                }
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                }
            } else {
                this.setText(value.toString());
            }
        }
        return this;
    }

    /**
     * @param oddColor The oddColor to set.
     */
    public void setOddColor(Color oddColor) {
        this.oddColor = oddColor;
    }

    /**
     * @return Returns the oddColor.
     */
    public Color getOddColor() {
        return oddColor;
    }

    /**
     * @param evenColor The evenColor to set.
     */
    public void setEvenColor(Color evenColor) {
        this.evenColor = evenColor;
    }

    /**
     * @return Returns the evenColor.
     */
    public Color getEvenColor() {
        return evenColor;
    }

    /**
     * @param sendHistoryColor
     */
    public void setSendHistoryColor(Color sendHistoryColor) {
        this.sendHistoryColor = sendHistoryColor;
    }

    /**
     * @return Returns the oddColor.
     */
    public Color getSendHistoryColor() {
        return sendHistoryColor;
    }

    /**
     * @param temporaryColor
     */
    public void setTemporaryColor(Color temporaryColor) {
        this.temporaryColor = temporaryColor;
    }

    /**
     * @return Returns the evenColor.
     */
    public Color getTemporaryColor() {
        return temporaryColor;
    }
}
