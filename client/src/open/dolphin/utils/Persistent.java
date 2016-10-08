/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import java.io.IOException;
import java.io.Serializable;
import open.dolphin.log.LogWriter;

import open.dolphin.project.GlobalConstants;
import org.jdesktop.application.ApplicationContext;

/**
 *
 * @author
 */
public class Persistent {

    /**
     * 
     * @param klass
     * @param type
     * @param defaultValue
     * @return
     */
    public static Serializable loadLayout(String klass, String type, Serializable defaultValue) {
        Serializable result = null;
        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        try {
            result = (Serializable) appCtx.getLocalStorage().load(klass + "." + type + ".xml");
        } catch (Exception ex) {
            LogWriter.error("", "");
        }
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     *
     * @param klass
     * @param type
     * @param layout
     */
    public static void saveLayout(String klass, String type, Serializable layout) {
        try {
            GlobalConstants.getLocalStorage().save(layout, klass + "." + type + ".xml");
        } catch (IOException ex) {
            LogWriter.error("Persistent", "saveLayout");
        }
    }

    /**
     *
     * @param klass
     * @param type
     */
    public static void deleteLayout(String klass, String type) {
        try {
            GlobalConstants.getLocalStorage().deleteFile(klass + "." + type + ".xml");
        } catch (IOException ex) {
            LogWriter.error("Persistent", "deleteLayout");
        }
    }
}
