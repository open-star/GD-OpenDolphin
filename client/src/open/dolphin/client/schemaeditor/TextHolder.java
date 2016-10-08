package open.dolphin.client.schemaeditor;

import java.awt.AlphaComposite;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author kazm
 */
public class TextHolder extends AreaHolder {
    
    /**
     * 
     * @param area
     * @param stroke
     * @param paint
     * @param ac
     * @param fill
     */
    public TextHolder(Area area, Stroke stroke, Paint paint, AlphaComposite ac, boolean fill) {
        super(area, stroke, paint, ac, fill);
    }
    
    /**
     *
     * @param p
     * @return
     */
    @Override
    public boolean contains(Point p) {
        Rectangle2D r = getArea().getBounds2D();
        return r.contains(p);
    }
}
