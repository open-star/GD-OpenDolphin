package open.dolphin.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.text.Position;

/**
 * IComponentHolder　　MEMO:リスナー
 *
 * @author  Kauzshi Minagawa
 */
public interface IComponentHolder extends PropertyChangeListener, IKarteComposite {

    /**
     *
     */
    public static final int TT_STAMP = 0;
    /**
     *
     */
    public static final int TT_IMAGE = 1;

    /**
     *
     * @return
     */
    public int getContentType();

    /**
     * 
     * @return
     */
    public KartePane getKartePane();

    /**
     *
     * @return
     */
    public boolean isSelected();

    /**
     *
     * @param b
     */
    public void setSelected(boolean b);

    /**
     *
     */
    public void edit();

    @Override
    public void propertyChange(PropertyChangeEvent e);

    /**
     *
     * @param start
     * @param end
     */
    public void setEntry(Position start, Position end);

    /**
     *
     * @return
     */
    public int getStartPos();

    /**
     *
     * @return
     */
    public int getEndPos();
}
