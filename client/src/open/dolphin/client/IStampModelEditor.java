package open.dolphin.client;

import java.beans.PropertyChangeListener;


/**
 * Stamp Model Editor が実装するインターフェイス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 **/
public interface IStampModelEditor {
    
    /**
     *
     * @return
     */
    public IStampEditorDialog getContext();
    
    /**
     *
     * @param context
     */
    public void setContext(IStampEditorDialog context);
    
    /**
     *
     */
    public void start();
    
    /**
     *
     * @return
     */
    public String getTitle();
    
    /**
     *
     * @return
     */
    public Object getValue();
    
    /**
     *
     * @param o
     */
    public void setValue(Object o);
    
    /**
     * 
     * @param prop
     * @param l
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener l);
    
    /**
     *
     * @param prop
     * @param l
     */
    public void removePropertyChangeListener(String prop, PropertyChangeListener l);
    
    /**
     *
     * @return
     */
    public boolean isValidModel();
    
    /**
     *
     * @param b
     */
    public void setValidModel(boolean b);
    
    /**
     *
     */
    public void dispose();
}