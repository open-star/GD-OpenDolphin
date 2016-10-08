/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.component;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author
 */
public class GhostGlassPane extends JPanel {

    private BufferedImage draggingGhost = null;
    private DnDTabbedPane tabbedPane;

    /**
     *
     * @param tabbedPane
     */
    public GhostGlassPane(DnDTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setOpaque(false);
    }

    /**
     *
     * @param draggingGhost
     */
    public void setImage(BufferedImage draggingGhost) {
        this.draggingGhost = draggingGhost;
    }

    /**
     *
     * @param tab
     */
    public void setTargetTabbedPane(DnDTabbedPane tab) {
        tabbedPane = tab;
    }

    /**
     *
     * @param g
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        DnDTabbedPane.DropLocation dl = tabbedPane.getDropLocation();
        if (draggingGhost != null && dl != null) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            if (dl.isDropable()) {
                tabbedPane.paintDropLine(g2);
            }
            Point p = SwingUtilities.convertPoint(tabbedPane, dl.getDropPoint(), this);
            double xx = p.getX() - (draggingGhost.getWidth(this) / 2d);
            double yy = p.getY() - (draggingGhost.getHeight(this) / 2d);
            g2.drawImage(draggingGhost, (int) xx, (int) yy, this);
        }
    }
}
