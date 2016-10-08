/*
 * ColorFillIcon.java
 *
 * Created on 2001/12/08, 13:23
 */
package open.dolphin.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * Core Java Foundation Class by Kim topley.
 */
public class ColorFillIcon implements Icon {

    private int width;
    private int height;
    private Color fillColor;
    private Color shadow;
    private int borderSize;
    private int fillHeight;
    private int fillWidth;
    private static final int BORDER_SIZE = 2;
    private static final int DEFAULT_SIZE = 32;

    /**
     * Creates new ColorFillIcon
     * @param fill
     * @param width
     * @param height 
     * @param borderSize
     */
    public ColorFillIcon(Color fill, int width, int height, int borderSize) {
        super();
        this.fillColor = fill;
        this.width = width;
        this.height = height;
        this.borderSize = borderSize;
        this.shadow = Color.black;
        this.fillWidth = width - 2 * borderSize;
        this.fillHeight = height - 2 * borderSize;
    }

    private ColorFillIcon(Color fill, int size) {
        this(fill, size, size, BORDER_SIZE);
    }

    private ColorFillIcon(Color fill) {
        this(fill, DEFAULT_SIZE, DEFAULT_SIZE, BORDER_SIZE);
    }

    /**
     * MEMO: unused?
     * @param c
     */
    private void setShadow(Color c) {
        shadow = c;
    }

    /**
     * MEMO: unused?
     * @param c
     */
    private void setFillColor(Color c) {
        fillColor = c;
    }

    /**
     *
     * @return
     */
    @Override
    public int getIconWidth() {
        return width;
    }

    /**
     *
     * @return
     */
    @Override
    public int getIconHeight() {
        return height;
    }

    /**
     *
     * @param comp
     * @param g
     * @param x
     * @param y
     */
    @Override
    public void paintIcon(Component comp, Graphics g, int x, int y) {
        Color c = g.getColor();
        if (borderSize > 0) {
            g.setColor(shadow);
            for (int i = 0; i < borderSize; i++) {
                g.drawRect(x + i, y + i,
                        width - 2 * i - 1, height - 2 * i - 1);
            }
        }

        g.setColor(fillColor);
        g.fillRect(x + borderSize, y + borderSize, fillWidth, fillHeight);
        g.setColor(c);
    }
}
