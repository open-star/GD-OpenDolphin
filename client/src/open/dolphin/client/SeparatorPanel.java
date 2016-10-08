/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SeparatorPanel.java
 *
 * Created on 2010/03/08, 15:47:04
 */
package open.dolphin.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *　検査画面用　MEMO:画面
 * @author 
 */
public class SeparatorPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 335333326611592227L;
    private static final Color DEFAULT_LEFT_COLOR = Color.WHITE;
    private static final Color DEFAULT_RIGHT_COLOR = Color.LIGHT_GRAY;
    private Color leftColor = DEFAULT_LEFT_COLOR;
    private Color rightColor = DEFAULT_RIGHT_COLOR;

    /** Creates new form SeparatorPanel */
    public SeparatorPanel() {
        initComponents();
        setOpaque(false);
        this.setPreferredSize(new Dimension(3, 12));
    }

    /**
     *
     * @param leftColor
     * @param rightColor
     */
    public SeparatorPanel(Color leftColor, Color rightColor) {
        this();
        this.leftColor = leftColor;
        this.rightColor = rightColor;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(leftColor);
        g.drawLine(0, 0, 0, getHeight());
        g.setColor(rightColor);
        g.drawLine(1, 0, 1, getHeight());
    }
}