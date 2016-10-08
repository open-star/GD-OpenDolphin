package open.dolphin.client;

import java.awt.Component;
import javax.swing.ActionMap;

/**
 *
 * @author kazm
 */
public interface IKarteComposite {
    
    /**
     * 
     * @param map
     */
    public void enter(ActionMap map);
    
    /**
     *
     * @param map
     */
    public void exit(ActionMap map);
    
    /**
     *
     * @return
     */
    public Component getComponent();
    
}
