package open.dolphin.client;

/**
 *
 * @author kazm
 */

public interface IMainService  {
    
    /**
     *
     * @return　名前
     */
    public String getName();
    
    /**
     *
     * @param name
     */
    public void setName(String name);
    
    /**
     *
     * @return ウインドウのインスタンス
     */
    public IMainWindow getContext();
    
    /**
     *
     * @param context
     */
    public void setContext(IMainWindow context);
    
    /**
     *
     */
    public void start();
    
    /**
     * 
     */
    public void stop();
    
}
