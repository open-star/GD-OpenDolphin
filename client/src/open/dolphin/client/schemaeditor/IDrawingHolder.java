/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.schemaeditor;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author kazm
 */
public interface IDrawingHolder {

    /**
     *
     * @param p
     * @return
     */
    public boolean contains(Point p);

    /**
     * 
     * @param g2
     */
    public void draw(Graphics2D g2);

    /**
     *
     * @param x
     * @param y
     */
    public void translate(double x, double y);
}
