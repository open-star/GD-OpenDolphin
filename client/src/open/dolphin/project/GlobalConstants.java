package open.dolphin.project;

import open.dolphin.container.NameValuePair;
import java.awt.Color;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.Map;
import javax.swing.ImageIcon;
import open.dolphin.infomodel.DepartmentModel;
import open.dolphin.infomodel.LicenseModel;

import org.apache.velocity.VelocityContext;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class GlobalConstants {

    private static GlobalConstantsImplement stub;

    /**
     *
     * @param context
     */
    public static void createGlobalConstants(ApplicationContext context) {
        stub = new GlobalConstantsImplement(context);
        setApplicationContext(context);
    }

    /**
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return stub.getApplicationContext();
    }

    /**
     *
     * @param ctx
     */
    private static void setApplicationContext(ApplicationContext ctx) {
        stub.setApplicationContext(ctx);
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static ResourceMap getResourceMap(Class clazz) {
        return stub.getResourceMap(clazz);
    }

    /**
     *
     * @return
     */
    public static LocalStorage getLocalStorage() {
        return stub.getLocalStorage();
    }

    /**
     *
     * @return
     */
    public static VelocityContext getVelocityContext() {
        return stub.getVelocityContext();
    }

    /**
     *
     * @return
     */
    public static boolean isMac() {
        return stub.isMac();
    }

    /**
     *
     * @return
     */
    public static boolean isWin() {
        return stub.isWin();
    }

    /**
     *
     * @return
     */
    public static boolean isLinux() {
        return stub.isLinux();
    }

    /**
     *
     * @param loc
     * @return
     */
    public static String getLocation(String loc) {
        return stub.getLocation(loc);
    }

    /**
     *
     * @return default_plugins
     */
    public static String getPluginsDirectory() {
        return stub.getPluginsDirectory();
    }

    /**
     *
     * @return installed_plugins
     */
    public static String getPluginsTempDirectory() {
        return stub.getPluginsTempDirectory();
    }

    /**
     *
     * @return schemas
     */
    public static String getSchemasDirectory() {
        return stub.getSchemasDirectory();
    }

    /**
     *
     * @return
     */
    public static String getSchemasTempDirectory() {
        return stub.getSchemasTempDirectory();
    }

    /**
     *
     * @return
     */
    public static String getLogDirectory() {
        return stub.getLogDirectory();
    }

    /**
     *
     * @return
     */
    public static String getPDFDirectory() {
        return stub.getPDFDirectory();
    }

    /**
     *
     * @return
     */
    public static String getSettingDirectory() {
        return stub.getSettingDirectory();
    }

    /**
     *
     * @param name
     * @return
     */
    public static InputStream getTemplateAsStream(String name) {
        return stub.getTemplateAsStream(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static InputStream getResourceAsStream(String name) {
        return stub.getResourceAsStream(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getString(String name) {
        return stub.getString(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static int[] getIntArray(String name) {
        return stub.getIntArray(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static Color getColor(String name) {
        return stub.getColor(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static Color[] getColorArray(String name) {
        return stub.getColorArray(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static ImageIcon getImageIcon(String name) {
        return stub.getImageIcon(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static String getFrameTitle(String name) {
        return stub.getFrameTitle(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static Dimension getDimension(String name) {
        return stub.getDimension(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public static Class[] getClassArray(String name) {
        return stub.getClassArray(name);
    }

    /**
     *
     * @return
     */
    public static Map<String, Color> getEventColorTable() {
        return stub.getEventColorTable();
    }

    /**
     *
     * @param key
     * @return
     */
    public static NameValuePair[] getNameValuePair(String key) {
        return stub.getNameValuePair(key);
    }

    /**
     *
     * @return
     */
    public static LicenseModel[] getLicenseModel() {
        return stub.getLicenseModel();
    }

    /**
     *
     * @return
     */
    public static DepartmentModel[] getDepartmentModel() {
        return stub.getDepartmentModel();
    }
    //  private static DiagnosisOutcomeModel[] getDiagnosisOutcomeModel() {
    //      return stub.getDiagnosisOutcomeModel();
    //  }
    // private static DiagnosisCategoryModel[] getDiagnosisCategoryModel() {
    //     return stub.getDiagnosisCategoryModel();
    //  }
}
