package open.dolphin.client;

import javax.swing.JButton;


/**
 *
 * @author
 */
public interface IStampEditorDialog {
    
    /**
     *
     */
    public static final String EDITOR_VALUE_PROP = "editorValueProp";
    
    /**
     * 
     * @return
     */
    public JButton getOkButton();
    
    /**
     *
     */
    public void close();
    
}
