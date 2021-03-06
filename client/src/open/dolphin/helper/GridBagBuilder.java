/*
 * Copyright (C) 2005 Digital Globe, Inc. All rights reserved.
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
package open.dolphin.helper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import open.dolphin.client.AutoKanjiListener;
import open.dolphin.client.AutoRomanListener;

/**
 * @author Kazushi Minagawa Digital Globe, Inc.
 *
 */
public class GridBagBuilder {

    private static final int CMP_HGAP = 7;
    private static final int CMP_VGAP = 7;
    private static final int TITLE_SPACE_TOP = 0;
    private static final int TITLE_SPACE_LEFT = 0;
    private static final int TITLE_SPACE_BOTTOM = 10;
    private static final int TITLE_SPACE_RIGHT = 0;
    private JPanel container;
    private JPanel product;
    private int cmpSpaceH = CMP_HGAP;
    private int cmpSpaceV = CMP_VGAP;
    private int titleSpaceTop = TITLE_SPACE_TOP;
    private int titleSpaceLeft = TITLE_SPACE_LEFT;
    private int titleSpaceBottom = TITLE_SPACE_BOTTOM;
    private int titleSpaceRight = TITLE_SPACE_RIGHT;

    /**
     *
     * @param panel
     */
    public GridBagBuilder(JPanel panel) {
        product = panel;
        container = product;
    }

    /**
     *
     * @param panel
     * @param title
     */
    public GridBagBuilder(JPanel panel, String title) {
        this(panel);
        this.setTitle(new JPanel(new GridBagLayout()), title);
    }

    /**
     * タイトルボーダを設定する。
     */
    private void setTitle(JPanel panel, String title) {

        if (title != null) {
            product.setBorder(BorderFactory.createTitledBorder(title));
            container = panel;
            container.setBorder(BorderFactory.createEmptyBorder(getTitleSpaceTop(),
                    getTitleSpaceLeft(),
                    getTitleSpaceBottom(),
                    getTitleSpaceRight()));

            product.add(container, BorderLayout.CENTER);
        }
    }

    /**
     * 座標(x, y) anchor の位置に長さ一のコンポーネントを追加する。
     * @param c 
     * @param x
     * @param anchor
     * @param y
     */
    public void add(Component c, int x, int y, int anchor) {
        add(c, x, y, 1, 1, anchor);
    }

    /**
     * 座標(x, y) anchor の位置にスパン(width, height)のコンポーネントを追加する。
     * @param cmp 
     * @param x
     * @param y
     * @param anchor
     * @param width
     * @param height
     */
    public void add(Component cmp, int x, int y, int width, int height, int anchor) {

        int top = (y == 0) ? 0 : getCmpSpaceV();
        int left = (x == 0) ? 0 : getCmpSpaceH();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.fill = GridBagConstraints.NONE;	// 大きくしない !!!
        c.anchor = anchor;

        // X,Y 方向とも２番目以降の部品は水平方向に 7,
        // 垂直方向に 5 ピクセルの間隔をあける
        if (top != 0 || left != 0) {
            c.insets = new Insets(top, left, 0, 0);  // top left bottom right
        }

        ((GridBagLayout) container.getLayout()).setConstraints(cmp, c);
        container.add(cmp);
    }

    /**
     * 座標(x, y)の位置にスパン１で重み(wx, wy)のコンポーネントを追加する。
     * @param cmp 
     * @param x 
     * @param wy
     * @param fill
     * @param wx
     * @param y
     */
    public void add(Component cmp, int x, int y, int fill, double wx, double wy) {
        add(cmp, x, y, 1, 1, fill, wx, wy);
    }

    /**
     * 座標(x, y)の位置にスパン(width, height)で重み(wx, wy)のコンポーネントを追加する。
     * @param cmp
     * @param x
     * @param fill
     * @param width
     * @param y
     * @param height
     * @param wx
     * @param wy
     */
    public void add(Component cmp, int x, int y, int width, int height, int fill, double wx, double wy) {

        int top = (y == 0) ? 0 : getCmpSpaceV();
        int left = (x == 0) ? 0 : getCmpSpaceH();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.fill = fill;
        c.weightx = wx;
        c.weighty = wy;

        if (top != 0 || left != 0) {
            c.insets = new Insets(top, left, 0, 0);  // top left bottom right
        }

        ((GridBagLayout) container.getLayout()).setConstraints(cmp, c);
        container.add(cmp);
    }

    /**
     *
     * @param x
     * @param y
     * @param fill
     */
    public void addGlue(int x, int y, int fill) {
        add(new JLabel(""), x, y, 1, 1, fill, 1.0, 1.0);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void addHGlue(int x, int y) {
        add(new JLabel(""), x, y, 1, 1, GridBagConstraints.HORIZONTAL, 1.0, 1.0);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void addVGlue(int x, int y) {
        add(new JLabel(""), x, y, 1, 1, GridBagConstraints.VERTICAL, 1.0, 1.0);
    }

    /**
     *
     * @param cmpSpaceH
     */
    public void setCmpSpaceH(int cmpSpaceH) {
        this.cmpSpaceH = cmpSpaceH;
    }

    /**
     *
     * @return
     */
    public int getCmpSpaceH() {
        return cmpSpaceH;
    }

    /**
     *
     * @param cmpSpaceV
     */
    public void setCmpSpaceV(int cmpSpaceV) {
        this.cmpSpaceV = cmpSpaceV;
    }

    /**
     *
     * @return
     */
    public int getCmpSpaceV() {
        return cmpSpaceV;
    }

    /**
     *
     * @param titleSpaceTop
     */
    public void setTitleSpaceTop(int titleSpaceTop) {
        this.titleSpaceTop = titleSpaceTop;
    }

    /**
     *
     * @return
     */
    public int getTitleSpaceTop() {
        return titleSpaceTop;
    }

    /**
     *
     * @param titleSpaceLeft
     */
    public void setTitleSpaceLeft(int titleSpaceLeft) {
        this.titleSpaceLeft = titleSpaceLeft;
    }

    /**
     *
     * @return
     */
    public int getTitleSpaceLeft() {
        return titleSpaceLeft;
    }

    /**
     *
     * @param titleSpaceBottom
     */
    public void setTitleSpaceBottom(int titleSpaceBottom) {
        this.titleSpaceBottom = titleSpaceBottom;
    }

    /**
     *
     * @return
     */
    public int getTitleSpaceBottom() {
        return titleSpaceBottom;
    }

    /**
     *
     * @param titleSpaceRight
     */
    public void setTitleSpaceRight(int titleSpaceRight) {
        this.titleSpaceRight = titleSpaceRight;
    }

    /**
     *
     * @return
     */
    public int getTitleSpaceRight() {
        return titleSpaceRight;
    }

    /**
     *
     * @param row
     * @param col
     * @param title
     * @param length
     * @param kanji
     */
    public void addTextItem(int row, int col, String title, int length, boolean kanji) {

        JLabel l = new JLabel(title, SwingConstants.RIGHT);

        JTextField tf = new JTextField(length);
        tf.setMargin(new Insets(1, 2, 1, 2));

        if (kanji) {
            tf.addFocusListener(AutoKanjiListener.getInstance());
        } else {
            tf.addFocusListener(AutoRomanListener.getInstance());
        }

        add(l, col, row, 1, 1, SwingConstants.EAST);
        add(tf, col, row, 1, 1, SwingConstants.WEST);
    }

    /**
     * 
     * @param components
     */
    public void layout(List<GridBagComponent> components) {

        for (GridBagComponent gbc : components) {
            int x = gbc.getCol();
            int y = gbc.getRow();
            int top = (y == 0) ? 0 : getCmpSpaceV();
            int left = (x == 0) ? 0 : getCmpSpaceH();

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = x;
            c.gridy = y;
            c.gridwidth = gbc.getColSpan();
            c.gridheight = gbc.getRowSpan();
            c.anchor = gbc.getAnchor();
            c.fill = gbc.getFill();
            c.weightx = gbc.getColWeight();
            c.weighty = gbc.getRowWeight();

            if (top != 0 || left != 0) {
                c.insets = new Insets(top, left, 0, 0);  // top left bottom right
            }

            ((GridBagLayout) container.getLayout()).setConstraints(gbc.getComponent(), c);
            container.add(gbc.getComponent());
        }
    }
}
