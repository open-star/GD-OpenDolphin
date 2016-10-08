package open.dolphin.client;

import javax.swing.ActionMap;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import open.dolphin.helper.ChartMenuSupport;
import open.dolphin.helper.MainMenuSupport;
import open.dolphin.helper.PlugInMenuSupport;

/**
 *
 * @author kazm
 */
public abstract interface IMenuAdapter {
    
    /**
     * 
     * @param chartContext
     * @param main
     * @param chart
     * @param plugin
     */
    public abstract void setMenuSupports(IChart chartContext, MainMenuSupport main, ChartMenuSupport chart, PlugInMenuSupport plugin);
    
    /**
     *
     * @return
     */
    public abstract JMenuBar getMenuBarProduct();
    
    /**
     *
     * @return
     */
    public abstract JPanel getToolPanelProduct();
    
    /**
     *
     * @return
     */
    public abstract ActionMap getActionMap();
    
    /**
     *
     * @param menuBar
     * @param enabled
     */
    public abstract void build(JMenuBar menuBar, boolean enabled);
}
