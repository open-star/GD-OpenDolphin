package open.dolphin.project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import open.dolphin.infomodel.DepartmentModel;
import open.dolphin.infomodel.LicenseModel;
import open.dolphin.container.NameValuePair;

import open.dolphin.log.LogWriter;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.ResourceMap;

/**
 * Dolphin Client のコンテキストクラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class GlobalConstantsImplement {

    private final String RESOURCE_LOCATION = "/open/dolphin/resources/";
    private final String TEMPLATE_LOCATION = "/open/dolphin/resources/templates/";
    private final String IMAGE_LOCATION = "/open/dolphin/resources/images/";
    private final String SCHEMA_LOCATION = "/open/dolphin/resources/schema/";
    private final String RESOURCE = "open.dolphin.resources.Dolphin_ja";
    private ResourceBundle resBundle;
    private Map<String, Color> eventColorTable;
    private ApplicationContext applicationContext; //リソースやアクション等のアプリケーション固有情報
    private boolean SSL = true;

    /**
     * GlobalVariablesImplement オブジェクトを生成する。
     * @param context リソースやアクション等のアプリケーション固有情報
     */
    public GlobalConstantsImplement(ApplicationContext context) {

        try {
            // ResourceBundle を得る
            resBundle = ResourceBundle.getBundle(RESOURCE);

            // Log4J のコンフィグレーションを行う
            StringBuilder logConfig = new StringBuilder();
            logConfig.append(getLocation("setting"));
            logConfig.append(File.separator);
            logConfig.append(getString("log.config.file"));

            // Velocity を初期化する
            StringBuilder velocityLog = new StringBuilder();
            velocityLog.append(context.getLocalStorage().getDirectory());
            velocityLog.append(File.separator);
            velocityLog.append(getString("log.dir"));
            velocityLog.append(File.separator);
            velocityLog.append(getString("application.velocity.log.file"));
            Velocity.setProperty("runtime.log", velocityLog.toString());
            Velocity.init();
            //   bootLogger.info("Velocity を初期化しました");

            // デフォルトの UI フォントを変更する
            setUIFonts();

            if (isMac()) {
                System.setProperty("apple.laf.useScreenMenuBar", String.valueOf(GlobalSettings.useScreenMenuBarOnMac()));
            }

            // マックメニューバーへ対応する
            //    String osName = System.getProperty("os.name").toLowerCase();
            //    if (osName.startsWith("mac")) {
            //        System.setProperty("apple.laf.useScreenMenuBar", String.valueOf(GlobalSettings.useScreenMenuBarOnMac()));
            //    }

            // login configuration file
            StringBuilder loginConfigPath = new StringBuilder();
            loginConfigPath.append(context.getLocalStorage().getDirectory());
            loginConfigPath.append(File.separator);
            loginConfigPath.append(getString("security.dir"));
            loginConfigPath.append(File.separator);
            loginConfigPath.append(getString("application.security.login.config"));
            String loginConfig = loginConfigPath.toString();
            System.setProperty("java.security.auth.login.config", loginConfig);
            //       bootLogger.info("ログイン構成ファイルを設定しました: " + loginConfig);

            if (SSL) {
                // System Properties を設定する
                Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

                // SSL trust store
                StringBuilder trustStorePath = new StringBuilder();
                trustStorePath.append(context.getLocalStorage().getDirectory());
                trustStorePath.append(File.separator);
                trustStorePath.append(getString("security.dir"));
                trustStorePath.append(File.separator);
                trustStorePath.append(getString("application.security.ssl.trustStore"));
                String trustStore = trustStorePath.toString();
                System.setProperty("javax.net.ssl.trustStore", trustStore);
            }

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     * @return
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     *
     * @param applicationContext
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public ResourceMap getResourceMap(Class clazz) {
        return applicationContext.getResourceMap(clazz);
    }

    /**
     *
     * @return
     */
    public LocalStorage getLocalStorage() {
        return applicationContext.getLocalStorage();
    }

    /**
     *
     * @return
     */
    public VelocityContext getVelocityContext() {
        return new VelocityContext();
    }

    /**
     *
     * @return
     */
    public boolean isMac() {
        return System.getProperty("os.name").toLowerCase().startsWith("mac") ? true : false;
    }

    /**
     *
     * @return
     */
    public boolean isWin() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows") ? true : false;
    }

    /**
     *
     * @return
     */
    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().startsWith("linux") ? true : false;
    }

    /**
     *
     * @return default_plugins
     */
    public String getPluginsDirectory() {
        return getLocalStorage().getDirectory().getAbsolutePath() + File.separator + "default_plugins";
    }

    /**
     *
     * @return installed_plugins
     */
    public String getPluginsTempDirectory() {
        return System.getProperty("user.dir") + File.separator + "installed_plugins";
    }

    /**
     *
     * @return schemas
     */
    public String getSchemasDirectory() {
        return getLocalStorage().getDirectory().getAbsolutePath() + File.separator + "schemas";
    }

    /**
     *
     * @return
     */
    public String getSchemasTempDirectory() {
        return System.getProperty("user.dir") + File.separator + "schema";
    }

    /**
     *
     * @return
     */
    public String getLogDirectory() {
        return getLocalStorage().getDirectory().getAbsolutePath() + File.separator + "log";
    }

    /**
     *
     * @return
     */
    public String getSettingDirectory() {
        return getLocalStorage().getDirectory().getAbsolutePath();
    }

    /**
     *
     * @param dir
     * @return
     */
    public String getLocation(String dir) {

        String ret = "";
        StringBuilder sb = new StringBuilder();

        //     sb.append(System.getProperty(getString("base.dir")));
        sb.append(System.getProperty("user.dir"));

        if (dir.equals("base")) {
            ret = sb.toString();
        } else if (dir.equals("lib")) {
            sb.append(File.separator);
            if (isMac()) {
                sb.append(getString("lib.mac.dir"));
            } else {
                sb.append(getString("lib.dir"));
            }
            ret = sb.toString();
        } else if (dir.equals("dolphin.jar")) {
            if (isMac()) {
                sb.append(File.separator);
                sb.append(getString("dolphin.jar.mac.dir"));
            }
            ret = sb.toString();
        } else if (dir.equals("security")) {
            sb.append(File.separator);
            sb.append(getString("security.dir"));
            ret = sb.toString();
        } else if (dir.equals("log")) {
            sb.append(File.separator);
            sb.append(getString("log.dir"));
            ret = sb.toString();
        } else if (dir.equals("setting")) {
            sb.append(File.separator);
            sb.append(getString("setting.dir"));
            ret = sb.toString();
        } else if (dir.equals("schema")) {
            File localStorage = getApplicationContext().getLocalStorage().getDirectory();
            File directory = new File(localStorage, getString("schema.dir"));
            ret = directory.toString();
        } else if (dir.equals("plugins")) {
            sb.append(File.separator);
            //     sb.append(getString("plugins.dir"));
            sb.append("installed_plugins");
            ret = sb.toString();
        } else if (dir.equals("pdf")) {
            sb.append(File.separator);
            sb.append(getString("pdf.dir"));
            ret = sb.toString();
        }
        return ret;
    }

    //   public String getBaseDirectory() {
    //      return getLocation("base");
    //  }
    //   public String getSettingDirectory() {
    //       return getLocation("setting");
    //   }
    //  public String getSecurityDirectory() {
    //      return getLocation("security");
    //  }
    /**
     *
     * @return
     */
    public String getPDFDirectory() {
        return getLocation("pdf");
    }

    //  public String getVersion() {
    //      return getString("version");
    //  }
    //  public String getUpdateURL() {
    //      if (isMac()) {
    //          return getString("updater.url.mac");
    //      } else if (isWin()) {
    //          return getString("updater.url.win");
    //      } else if (isLinux()) {
    //          return getString("updater.url.linux");
    //      } else {
    //          return getString("updater.url.linux");
    //      }
    //  }
    /**
     *
     * @param title
     * @return
     */
    public String getFrameTitle(String title) {
        try {
            String resTitle = getString(title);
            if (resTitle != null) {
                title = resTitle;
            }
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
        StringBuilder buf = new StringBuilder();
        buf.append(title);
        buf.append("-");
        buf.append(getString("application.title"));
        return buf.toString();
    }

    //  public URL getResource(String name) {
    //     if (!name.startsWith("/")) {
    //          name = RESOURCE_LOCATION + name;
    //      }
    //    return this.getClass().getResource(name);
    //  }
    //  public URL getMenuBarResource() {
    //     return getResource("MainMenuBar.xml");
    //   return isMac() ? getResource("MacMainMenuBar.xml") : getResource("WindowsMainMenuBar.xml");
    //  }
    /**
     *
     * @param name
     * @return
     */
    public URL getImageResource(String name) {
        if (!name.startsWith("/")) {
            name = IMAGE_LOCATION + name;
        }
        return this.getClass().getResource(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public InputStream getResourceAsStream(String name) {
        if (!name.startsWith("/")) {
            name = RESOURCE_LOCATION + name;
        }
        return this.getClass().getResourceAsStream(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public InputStream getTemplateAsStream(String name) {
        if (!name.startsWith("/")) {
            name = TEMPLATE_LOCATION + name;
        }
        return this.getClass().getResourceAsStream(name);
    }

    /**
     *
     * @param name
     * @return
     */
    public ImageIcon getImageIcon(String name) {
        return new ImageIcon(getImageResource(name));
    }
    /*
    private ImageIcon getSchemaIcon(String name) {
    if (!name.startsWith("/")) {
    name = SCHEMA_LOCATION + name;
    }
    return new ImageIcon(this.getClass().getResource(name));
    }
     */

    /**
     *
     * @return
     */
    public LicenseModel[] getLicenseModel() {
        String[] desc = getStringArray("licenseDesc");
        String[] code = getStringArray("license");
        String codeSys = getString("licenseCodeSys");
        LicenseModel[] ret = new LicenseModel[desc.length];
        LicenseModel model = null;
        for (int i = 0; i < desc.length; i++) {
            model = new LicenseModel();
            model.setLicense(code[i]);
            model.setLicenseDesc(desc[i]);
            model.setLicenseCodeSys(codeSys);
            ret[i] = model;
        }
        return ret;
    }

    /**
     *
     * @return
     */
    public DepartmentModel[] getDepartmentModel() {
        String[] desc = getStringArray("departmentDesc");
        String[] code = getStringArray("department");
        String codeSys = getString("departmentCodeSys");
        DepartmentModel[] ret = new DepartmentModel[desc.length];
        DepartmentModel model = null;
        for (int i = 0; i < desc.length; i++) {
            model = new DepartmentModel();
            model.setDepartment(code[i]);
            model.setDepartmentDesc(desc[i]);
            model.setDepartmentCodeSys(codeSys);
            ret[i] = model;
        }
        return ret;
    }
    /*
    public DiagnosisOutcomeModel[] getDiagnosisOutcomeModel() {
    String[] desc = {"回復", "全治", "続発症(の発生)", "終了", "中止", "継続", "死亡", "悪化", "不変", "転医", "転医(急性病院へ)", "転医(慢性病院へ)", "自宅へ退院", "不明"}; //getStringArray("diagnosis.outcomeDesc");
    String[] code = {"recovering", "fullyRecovered", "sequelae", "end", "pause", "continued", "died", "worsening", "unchanged", "transfer", "transferAcute", "transferChronic", "home", "unknown"}; //getStringArray("diagnosis.outcome");
    String codeSys = "MML0016";
    DiagnosisOutcomeModel[] ret = new DiagnosisOutcomeModel[desc.length];
    DiagnosisOutcomeModel model = null;
    for (int i = 0; i < desc.length; i++) {
    model = new DiagnosisOutcomeModel();
    model.setOutcome(code[i]);
    model.setOutcomeDesc(desc[i]);
    model.setOutcomeCodeSys(codeSys);
    ret[i] = model;
    }
    return ret;
    }

    public DiagnosisCategoryModel[] getDiagnosisCategoryModel() {
    String[] desc = {"回復", "全治", "続発症(の発生)", "終了", "中止", "継続", "死亡", "悪化", "不変", "転医", "転医(急性病院へ)", "転医(慢性病院へ)", "自宅へ退院", "不明"}; //getStringArray("diagnosis.outcomeDesc");
    String[] code = {"recovering", "fullyRecovered", "sequelae", "end", "pause", "continued", "died", "worsening", "unchanged", "transfer", "transferAcute", "transferChronic", "home", "unknown"}; //getStringArray("diagnosis.outcome");
    String[] codeSys = {"MML0016"}; //getStringArray("diagnosis.outcomeCodeSys");
    DiagnosisCategoryModel[] ret = new DiagnosisCategoryModel[desc.length];
    DiagnosisCategoryModel model = null;
    for (int i = 0; i < desc.length; i++) {
    model = new DiagnosisCategoryModel();
    model.setDiagnosisCategory(code[i]);
    model.setDiagnosisCategoryDesc(desc[i]);
    model.setDiagnosisCategoryCodeSys(codeSys[i]);
    ret[i] = model;
    }
    return ret;
    }
     */

    /**
     *
     * @param key
     * @return
     */
    public NameValuePair[] getNameValuePair(String key) {
        NameValuePair[] ret = null;
        String[] code = getStringArray(key + ".value");
        String[] name = getStringArray(key + ".name");
        int len = code.length;
        ret = new NameValuePair[len];

        for (int i = 0; i < len; i++) {
            ret[i] = new NameValuePair(name[i], code[i]);
        }
        return ret;
    }

    /**
     *
     * @return
     */
    public Map<String, Color> getEventColorTable() {
        if (eventColorTable == null) {
            setupEventColorTable();
        }
        return eventColorTable;
    }

    private void setupEventColorTable() {
        // イベントカラーを定義する
        eventColorTable = new HashMap<String, Color>(10, 0.75f);
        eventColorTable.put("TODAY", getColor("color.TODAY_BACK"));
        eventColorTable.put("BIRTHDAY", getColor("color.BIRTHDAY_BACK"));
        eventColorTable.put("PVT", getColor("color.PVT"));
        eventColorTable.put("DOC_HISTORY", getColor("color.PVT"));
    }

    /**
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return resBundle.getString(key);
    }

    private String[] getStringArray(String key) {
        String line = getString(key);
        return line.split(",");
    }

//   public int getInt(String key) {
    //      return Integer.parseInt(getString(key));
    // }
    /**
     *
     * @param key
     * @return
     */
    public int[] getIntArray(String key) {
        String[] obj = getStringArray(key);
        int[] ret = new int[obj.length];
        for (int i = 0; i < obj.length; i++) {
            ret[i] = Integer.parseInt(obj[i]);
        }
        return ret;
    }

    //   public long getLong(String key) {
    //      return Long.parseLong(getString(key));
    //   }
/*
    public long[] getLongArray(String key) {
    String[] obj = getStringArray(key);
    long[] ret = new long[obj.length];
    for (int i = 0; i < obj.length; i++) {
    ret[i] = Long.parseLong(obj[i]);
    }
    return ret;
    }
     * */
    /*
    private float getFloat(String key) {
    return Float.parseFloat(getString(key));
    }

    private float[] getFloatArray(String key) {
    String[] obj = getStringArray(key);
    float[] ret = new float[obj.length];
    for (int i = 0; i < obj.length; i++) {
    ret[i] = Float.parseFloat(obj[i]);
    }
    return ret;
    }

    private double getDouble(String key) {
    return Double.parseDouble(getString(key));
    }

    private double[] getDoubleArray(String key) {
    String[] obj = getStringArray(key);
    double[] ret = new double[obj.length];
    for (int i = 0; i < obj.length; i++) {
    ret[i] = Double.parseDouble(obj[i]);
    }
    return ret;
    }
     */
    //   public boolean getBoolean(String key) {
    //      return Boolean.valueOf(getString(key)).booleanValue();
    //   }
/*
    public boolean[] getBooleanArray(String key) {
    String[] obj = getStringArray(key);
    boolean[] ret = new boolean[obj.length];
    for (int i = 0; i < ret.length; i++) {
    ret[i] = Boolean.valueOf(obj[i]).booleanValue();
    }
    return ret;
    }

    private Point lgetPoint(String name) {
    int[] data = getIntArray(name);
    return new Point(data[0], data[1]);
    }
     */
    /**
     *
     * @param name
     * @return
     */
    public Dimension getDimension(String name) {
        int[] data = getIntArray(name);
        return new Dimension(data[0], data[1]);
    }
    /*
    private Insets getInsets(String name) {
    int[] data = getIntArray(name);
    return new Insets(data[0], data[1], data[2], data[3]);
    }
     */

    /**
     *
     * @param key
     * @return
     */
    public Color getColor(String key) {
        int[] data = getIntArray(key);
        return new Color(data[0], data[1], data[2]);
    }

    /**
     *
     * @param key
     * @return
     */
    public Color[] getColorArray(String key) {
        int[] data = getIntArray(key);
        int cnt = data.length / 3;
        Color[] ret = new Color[cnt];
        for (int i = 0; i < cnt; i++) {
            int bias = i * 3;
            ret[i] = new Color(data[bias], data[bias + 1], data[bias + 2]);
        }
        return ret;
    }

    /**
     *
     * @param name
     * @return
     */
    public Class[] getClassArray(String name) {
        String[] clsStr = getStringArray(name);
        Class[] ret = new Class[clsStr.length];
        try {
            for (int i = 0; i < clsStr.length; i++) {
                ret[i] = Class.forName(clsStr[i]);
            }
            return ret;

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
        return null;
    }

    /**
     * Windows のデフォルトフォントを設定する。
     */
    private void setUIFonts() {

        if (isWin() || isLinux()) {
            int size = 12;
            if (isLinux()) {
                size = 13;
            }
            Font font = new Font("SansSerif", Font.PLAIN, size);
            UIManager.put("Label.font", font);
            UIManager.put("Button.font", font);
            UIManager.put("ToggleButton.font", font);
            UIManager.put("Menu.font", font);
            UIManager.put("MenuItem.font", font);
            UIManager.put("CheckBox.font", font);
            UIManager.put("CheckBoxMenuItem.font", font);
            UIManager.put("RadioButton.font", font);
            UIManager.put("RadioButtonMenuItem.font", font);
            UIManager.put("ToolBar.font", font);
            UIManager.put("ComboBox.font", font);
            UIManager.put("TabbedPane.font", font);
            UIManager.put("TitledBorder.font", font);
            UIManager.put("List.font", font);
        }
    }
}
