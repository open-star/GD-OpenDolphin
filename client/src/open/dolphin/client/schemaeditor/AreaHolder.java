package open.dolphin.client.schemaeditor;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

/**
 *
 * @author kazm
 */
public class AreaHolder implements IDrawingHolder {

    private Area area;
    private Stroke stroke;
    private Paint paint;
    private AlphaComposite ac;
    private boolean fill;

    /**
     *
     */
    public AreaHolder() {
    }

    /**
     *
     * @param area
     * @param stroke
     * @param paint
     * @param ac
     * @param fill
     */
    public AreaHolder(Area area, Stroke stroke, Paint paint, AlphaComposite ac, boolean fill) {
        this();
        setArea(area);
        setStroke(stroke);
        setPaint(paint);
        setAlphaComposite(ac);
        setFill(fill);
    }

    /**
     *
     * @return
     */
    public Shape getArea() {
        return area;
    }

    /**
     *
     * @param area
     */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
     *
     * @return
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     *
     * @param color
     */
    public void setPaint(Paint color) {
        this.paint = color;
    }

    /**
     *
     * @return
     */
    public boolean isFill() {
        return fill;
    }

    /**
     *
     * @param fill
     */
    public void setFill(boolean fill) {
        this.fill = fill;
    }

    /**
     *
     * @return
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     *
     * @param stroke
     */
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    /**
     *
     * @return
     */
    public AlphaComposite getAlphaComposite() {
        return ac;
    }

    /**
     *
     * @param ac
     */
    public void setAlphaComposite(AlphaComposite ac) {
        this.ac = ac;
    }

    /**
     *
     * @param p
     * @return
     */
    public boolean contains(Point p) {
        return area.contains(p.getX(), p.getY());
    }

    /**
     *
     * @param g2d
     */
    public void draw(Graphics2D g2d) {

        Stroke saveStroke = g2d.getStroke();
        Paint savePaint = g2d.getPaint();
        Composite saveComposite = g2d.getComposite();

        g2d.setComposite(getAlphaComposite());
        g2d.setPaint(getPaint());

        if (isFill()) {
            if (getStroke() != null) {
                g2d.setStroke(getStroke());
            }
            g2d.fill(getArea());
        } else {
            if (getStroke() != null) {
                g2d.setStroke(getStroke());
            }
            g2d.draw(getArea());
        }

        g2d.setStroke(saveStroke);
        g2d.setPaint(savePaint);
        g2d.setComposite(saveComposite);
    }

    /**
     *
     * @param x
     * @param y
     */
    public void translate(double x, double y) {
        AffineTransform trans = AffineTransform.getTranslateInstance(
                x,
                y);
        area.transform(trans);
    }
}
