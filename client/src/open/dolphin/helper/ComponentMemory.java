/*
 * Created on 2005/06/17
 *
 */
package open.dolphin.helper;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentListener;
import java.util.prefs.Preferences;
import open.dolphin.project.GlobalConstants;

/**
 * ComponentMemory
 *
 * @author Kazushi Minagawa
 *
 */
public class ComponentMemory implements ComponentListener {

    private Component target;
    private Point defaultLocation;
    private Dimension defaultSise;
    private Preferences prefs;
    private String name;
    private boolean report = true;

    /**
     *
     * @param target
     * @param loc
     * @param size
     * @param object
     */
    public ComponentMemory(Component target, Point loc, Dimension size, Object object) {
        this.target = target;
        this.defaultLocation = loc;
        this.defaultSise = size;
        if (object != null) {
            this.prefs = Preferences.userNodeForPackage(object.getClass());
            this.name = object.getClass().getName();
        }
        target.setLocation(this.defaultLocation);
        target.setSize(this.defaultSise);
        target.addComponentListener(this);
    }

    /**
     *
     * @param e
     */
    @Override
    public void componentMoved(java.awt.event.ComponentEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void componentResized(java.awt.event.ComponentEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void componentShown(java.awt.event.ComponentEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void componentHidden(java.awt.event.ComponentEvent e) {
    }

    /**
     *
     */
    public void setToPreferenceBounds() {
        if (prefs != null) {
            int x = prefs.getInt(name + "_x", defaultLocation.x);
            int y = prefs.getInt(name + "_y", defaultLocation.y);
            int width = prefs.getInt(name + "_width", defaultSise.width);
            int height = prefs.getInt(name + "_height", defaultSise.height);
            target.setBounds(x, y, width, height);
        }
    }

    /**
     *
     */
    public void putCenter() {
        if (GlobalConstants.isMac()) {
            putCenter(3);
        } else {
            putCenter(2);
        }
    }

    /**
     *
     * @param n
     */
    public void putCenter(int n) {
        n = n != 0 ? n : 2;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = target.getSize();
        int x = (screenSize.width - size.width) / 2;
        int y = (screenSize.height - size.height) / n;
        target.setBounds(x, y, size.width, size.height);
    }
}
