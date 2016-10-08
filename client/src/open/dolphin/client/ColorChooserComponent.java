package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JComponent;

/**
 * ColorChooserLabel MEMO:Component
 *
 * @author Minagawa,Kazushi
 */
public class ColorChooserComponent extends JComponent implements MouseListener, MouseMotionListener {

    /**
     * 
     */
    public static final String SELECTED_COLOR = "selectedColor";
    private Color[] colors;
    private Color[] colorStart;
    private Dimension size;
    private Color strokeColor = Color.DARK_GRAY;
    private int strokeWidth = 2;
    private PropertyChangeSupport boundSupport = new PropertyChangeSupport(this);
    private Color selected;
    private int index = -1;

    /**
     * Creates a new progress panel with default values
     */
    protected ColorChooserComponent() {
        colorStart = GlobalConstants.getColorArray("color.set.default.start");
        colors = GlobalConstants.getColorArray("color.set.default.end");
        size = GlobalConstants.getDimension("colorCooserComp.default.size");
        strokeWidth = 1;
        this.setPreferredSize(new Dimension(2 * size.width * colors.length + size.width, 2 * size.height));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Creates a new progress panel with default values
     */
    private ColorChooserComponent(Dimension size, Color[] colors) {
        this.size = size;
        this.colors = colors;
        this.setPreferredSize(new Dimension(2 * size.width * colors.length + size.width, 2 * size.height));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     *
     * @param prop
     * @param l
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     *
     * @param prop
     * @param l
     */
    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     * MEMO: unused?
     * @return
     */
    private Color getSelectedColor() {
        return selected;
    }

    /**
     *
     * @param selected
     */
    private void setSelectedColor(Color selected) {
        this.selected = selected;
        boundSupport.firePropertyChange(SELECTED_COLOR, null, this.selected);
    }

    /**
     *
     * @param e
     */
    public void mouseDragged(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
        int x = e.getX() / size.width;
        int mod = x % 2;
        if (mod != 0) {
            index = x / 2;
        }
        repaint();
    }

    /**
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     *
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
        int x = e.getX() / size.width;
        int mod = x % 2;
        if (mod != 0) {
            index = x / 2;
        }
        repaint();
    }

    /**
     *
     * @param e
     */
    public void mouseExited(MouseEvent e) {
        index = -1;
        repaint();
    }

    /**
     *
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size.width;
        int mod = x % 2;
        if (mod != 0) {
            index = x / 2;
        }
        if (index >= 0 && index < colors.length) {
            setSelectedColor(colors[index]);
        }
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        double dx = size.getWidth() * 2;
        double offsetX = size.getWidth();
        double offsetY = (this.getPreferredSize().getHeight() - size.getHeight()) / 2;

        BasicStroke stroke = new BasicStroke(strokeWidth);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        for (int i = 0; i < colors.length; i++) {
            double x = offsetX + i * dx;
            double y = offsetY;
            Ellipse2D.Double body = new Ellipse2D.Double(x, y, size.getWidth(), size.getHeight());
            GradientPaint lightToDark = new GradientPaint((int) x, (int) y, colorStart[i], (int) x + size.width, (int) y + size.height, colors[i]);
            g2.setPaint(lightToDark);
            g2.fill(body);
            if (i == index) {
                g2.setColor(strokeColor);
                g2.setStroke(stroke);
                g2.draw(body);
                //g2.setColor(colors[i]);
                //g2.fill(body);
                //GradientPaint lightToDark = new GradientPaint((int)x, (int)y, Color.LIGHT_GRAY, (int)x, (int)y + size.height, colors[i]);
                //g2.setPaint(lightToDark);
                //g2.fill(body);
                //g2.setColor(Color.DARK_GRAY);
                //g2.setStroke(new BasicStroke(2));
                //g2.draw(body);
            } //else {
            //g2.setColor(colors[i]);
            //g2.fill(body);
            //}
        }
    }
}
