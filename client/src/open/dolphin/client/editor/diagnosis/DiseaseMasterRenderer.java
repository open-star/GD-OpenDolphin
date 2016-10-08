/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.editor.diagnosis;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import open.dolphin.project.GlobalConstants;
import open.dolphin.client.MasterRenderer;
import open.dolphin.infomodel.DiseaseEntry;
import open.dolphin.log.LogWriter;

/**
 *
 * @author
 */
/**
 * 病名マスタ Table のレンダラー
 */
public final class DiseaseMasterRenderer extends MasterRenderer {

    private static final Color[] masterColors = GlobalConstants.getColorArray("masterSearch.masterColors");
    private static final long serialVersionUID = -5209120802971568080L;
    private final int CODE_COLUMN = 0;
    private final int NAME_COLUMN = 1;
    private final int KANA_COLUMN = 2;
    private final int ICD10_COLUMN = 3;
    private final int DISUSES_COLUMN = 4;

    /**
     *
     */
    public DiseaseMasterRenderer() {
        setBeforStartColor(masterColors[0]);
        setInUseColor(masterColors[1]);
        setAfterEndColor(masterColors[2]);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean isFocused,
            int row, int col) {
        Component c = super.getTableCellRendererComponent(
                table,
                value,
                isSelected,
                isFocused,
                row, col);

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {

            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }


        JLabel label = (JLabel) c;

        if (value != null && value instanceof DiseaseEntry) {

            DiseaseEntry entry = (DiseaseEntry) value;

            String disUseDate = entry.getDisUseDate();

            setColor(label, disUseDate);

            switch (col) {

                case CODE_COLUMN:
                    label.setText(entry.getCode());
                    break;

                case NAME_COLUMN:
                    label.setText(entry.getName());
                    break;

                case KANA_COLUMN:
                    label.setText(entry.getKana());
                    break;

                case ICD10_COLUMN:
                    label.setText(entry.getIcdTen());
                    break;

                case DISUSES_COLUMN:
                    if (disUseDate != null) {
                        if (disUseDate.startsWith("9")) {
                            label.setText("");
                        } else {
                            label.setText(disUseDate);
                        }
                    }
                    break;
                default: LogWriter.fatal(getClass(), "case default");
            }

        } else {
            label.setBackground(Color.white);
            label.setText(value == null ? "" : value.toString());
        }
        return c;
    }
}

