package open.dolphin.client;

import open.dolphin.client.Dolphin.MenuMediator;
import open.dolphin.helper.IMainCommandAccepter;
/**
 *
 * @author kazm
 */
public interface IMainComponent extends IMainTool,IMainCommandAccepter  {

    /**
     *
     * @return
     */
    public String getIcon();
    
    /**
     * 
     * @param icon
     */
    public void setIcon(String icon);
    
//    public JPanel getPanel();
    
 //   public void setPanel(JPanel panel);

    /**
     *
     * @param midiator
     */
    public void setMenu(MenuMediator midiator);
    
}
