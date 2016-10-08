package open.dolphin.client.editor.stamp;

import java.awt.Color;

/**
 * StampRenderingHints
 * 
 * @author Minagawa, Kazushi
 *
 */
public class StampRenderingHints {

    private int fontSize = 12;
    private Color foreground;
    private Color background = Color.WHITE;
    private Color labelColor;
    private int border = 0;
    private int cellSpacing = 0;
    private int cellPadding = 3;

    /**
     *
     * @return
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     *
     * @param fontSize
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     *
     * @return
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     *
     * @param foreground
     */
    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    /**
     *
     * @return
     */
    public Color getBackground() {
        return background;
    }

    /**
     *
     * @param background
     */
    public void setBackground(Color background) {
        this.background = background;
    }

    /**
     *
     * @return
     */
    public Color getLabelColor() {
        return labelColor;
    }

    /**
     *
     * @param labelColor
     */
    public void setLabelColor(Color labelColor) {
        this.labelColor = labelColor;
    }

    /**
     *
     * @return
     */
    public int getBorder() {
        return border;
    }

    /**
     *
     * @param border
     */
    public void setBorder(int border) {
        this.border = border;
    }

    /**
     *
     * @return
     */
    public int getCellPadding() {
        return cellPadding;
    }

    /**
     *
     * @param cellPadding
     */
    public void setCellPadding(int cellPadding) {
        this.cellPadding = cellPadding;
    }

    /**
     *
     * @return
     */
    public int getCellSpacing() {
        return cellSpacing;
    }

    /**
     *
     * @param cellSpacing
     */
    public void setCellSpacing(int cellSpacing) {
        this.cellSpacing = cellSpacing;
    }

    /**
     *
     * @return
     */
    public String getForegroundAs16String() {
        if (getForeground() == null) {
            return "#000C9C";
        } else {
            int r = getForeground().getRed();
            int g = getForeground().getGreen();
            int b = getForeground().getBlue();
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(Integer.toHexString(r));
            sb.append(Integer.toHexString(g));
            sb.append(Integer.toHexString(b));
            return sb.toString();
        }
    }

    /**
     *
     * @return
     */
    public String getBackgroundAs16String() {
        if (getBackground() == null) {
            return "#FFFFFF";
        } else {
            int r = getBackground().getRed();
            int g = getBackground().getGreen();
            int b = getBackground().getBlue();
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(Integer.toHexString(r));
            sb.append(Integer.toHexString(g));
            sb.append(Integer.toHexString(b));
            return sb.toString();
        }
    }

    /**
     *
     * @return
     */
    public String getLabelColorAs16String() {
        if (getLabelColor() == null) {
            return "#FFCED9";
        } else {
            int r = getLabelColor().getRed();
            int g = getLabelColor().getGreen();
            int b = getLabelColor().getBlue();
            StringBuilder sb = new StringBuilder();
            sb.append("#");
            sb.append(Integer.toHexString(r));
            sb.append(Integer.toHexString(g));
            sb.append(Integer.toHexString(b));
            return sb.toString();
        }
    }
}
