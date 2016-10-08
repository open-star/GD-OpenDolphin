/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import java.awt.Component;
import javax.swing.JFileChooser;

/**
 *
 * @author
 */
public class FileOpenDialog extends JFileChooser {

    /**
     *
     */
    public static final int LOAD = 0;
    /**
     * 
     */
    public static final int SAVE = 1;
    private Component parent;
    private int mode;
    private String extention;

    /**
     *
     * @param parent
     * @param title
     * @param mode
     * @param extention
     */
    public FileOpenDialog(Component parent, String title, int mode, String extention) {
        this.mode = mode;
        this.parent = parent;
        this.extention = extention;
        setFileSelectionMode(FILES_ONLY);
        setMultiSelectionEnabled(false);
        setDialogTitle(title);
        setAcceptAllFileFilterUsed(false);
    }

    /**
     *
     * @return
     */
    public int open() {
        int result;
        switch (mode) {
            case LOAD:
                result = showOpenDialog(parent);
                break;
            case SAVE:
                result = showSaveDialog(parent);
                break;
            default:
                return ERROR_OPTION;
        }
        return result;
    }

    /**
     *
     * @return
     */
    public String getPath() {
        String result = getSelectedFile().getAbsolutePath();
        //   if (getSelectedFile().isFile()) {
        switch (mode) {
            case LOAD:
                break;
            case SAVE:
                if (!result.endsWith('.' + extention)) {
                    result += '.' + extention;
                }
                break;
            default:
                return "";
        }
        //   }
        return result;
    }
}
