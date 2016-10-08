package open.dolphin.helper;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import open.dolphin.project.GlobalConstants;

/**
 *
 * @author
 */
public class StripeRenderer extends DefaultListCellRenderer {

    private static final Color DEFAULT_ODD_COLOR = GlobalConstants.getColor("color.odd");
    private static final Color DEFAULT_EVENN_COLOR = GlobalConstants.getColor("color.even");
    private Color oddColor;
    private Color evenColor;

    /**
     *
     */
    public StripeRenderer() {
        this(DEFAULT_ODD_COLOR, DEFAULT_EVENN_COLOR);
    }

    /**
     *　
     * @param oddColor　縞の奇数の部分の色
     * @param evenColor　縞の偶数の部分の色
     */
    public StripeRenderer(Color oddColor, Color evenColor) {
        setOpaque(true);
        setOddColor(oddColor);
        setEvenColor(evenColor);
    }

    /**
     *
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return　テーブルのセルとして使用されるコンポーネント
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value,
                index, isSelected, cellHasFocus);

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());

        } else {

            setForeground(list.getForeground());

            if (index % 2 == 0) {
                setBackground(getEvenColor());
            } else {
                setBackground(getOddColor());
            }
        }
        return label;
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
}
