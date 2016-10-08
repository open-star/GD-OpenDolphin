/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import java.awt.Label;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import open.dolphin.project.GlobalSettings;

/**
 *
 * @author tomohiro
 */
public class UserDirectoryInitializer {

    private static final String PREFERENCES_NAME = "/open/dolphin/project";
    private static final String INITIALIZED = "initialized";
    private static final String SSL_STATE = "dbSSLState";

    /**
     *
     * @param context
     */
    public static void run(org.jdesktop.application.ApplicationContext context) {

        File userPath = context.getLocalStorage().getDirectory();
        if (!userPath.exists()) {
            userPath.mkdirs();
        }
        if (!userPath.canWrite()) {
            exitWithMessage("ユーザディレクトリが書き込み可能ではありません:" + System.getProperty("line.separator") + userPath.getPath());
        }

        File securityPath = new File(userPath, "security");
        if (!securityPath.exists()) {
            securityPath.mkdir();
        }

        File loginConfigPath = new File(securityPath, "dolphin.login.config");
        if (!loginConfigPath.exists()) {
            createLoginConfigFile(loginConfigPath);
        }

        //     File schemaPath = new File(userPath, "schema");
        //    if (!schemaPath.exists()) {
        //        schemaPath.mkdir();
        //    }

        File templatePath = new File(userPath, "templates");
        if (!templatePath.exists()) {
            templatePath.mkdir();
        }

        Preferences pref = Preferences.userRoot().node(PREFERENCES_NAME);
        if (!GlobalSettings.isTrial()) {
            if (!pref.getBoolean(INITIALIZED, false)) {
                switch (JOptionPane.showConfirmDialog(null, "SSL通信を行いますか？")) {
                    case JOptionPane.YES_OPTION:
                        File trustStorePath = new File(securityPath, "dolphin.trustStore");
                        if (!trustStorePath.exists()) {
                            if (fetchTrustStore(trustStorePath)) {
                                pref.putBoolean(SSL_STATE, true);
                                pref.putBoolean(INITIALIZED, true);
                            }
                        }
                        break;
                    case JOptionPane.NO_OPTION:
                        pref.putBoolean(INITIALIZED, true);
                        break;
                    default:
                }
            }
        }
    }

    /**
     *
     * @param loginConfigPath
     */
    private static void createLoginConfigFile(File loginConfigPath) {
        try {
            java.io.BufferedWriter bufWriter = new java.io.BufferedWriter(new java.io.FileWriter(loginConfigPath));
            bufWriter.write(loginConfigFileContent());
            bufWriter.close();
        } catch (java.io.IOException ex) {
            exitWithMessage("ログイン設定ファイルの書き込みに失敗しました:" + System.getProperty("line.separator") + ex.toString());
        }
        showMessage("ログイン設定ファイルを書き込みました");
    }

    /**
     *
     * @return
     */
    private static String loginConfigFileContent() {
        return "openDolphin {" + System.getProperty("line.separator") + "  open.dolphin.security.DolphinLoginModule required;" + System.getProperty("line.separator") + "};" + System.getProperty("line.separator");
    }

    /**
     *
     * @param pane
     * @return
     */
    private static boolean isNoResultFromDialog(JOptionPane pane) {
        return pane.getValue() == null || (Integer) pane.getValue() == JOptionPane.CANCEL_OPTION;
    }

    /**
     *
     * @return
     */
    private static String askIpAdderss() {
        JPanel panel = new JPanel();
        panel.setLayout((new BoxLayout(panel, BoxLayout.X_AXIS)));

        List<JTextField> fields = new ArrayList<JTextField>(4);
        for (int i = 0; i < 4; i++) {
            JTextField field = new JTextField(3);
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setDocument(new IPAddressDocument(field));
            fields.add(field);
        }

        for (Iterator<JTextField> iterator = fields.iterator(); iterator.hasNext();) {
            JTextField field = iterator.next();
            panel.add(field);
            if (iterator.hasNext()) {
                panel.add(new Label("."));
            }
        }

        JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog("データベースサーバの IP アドレスを入力してください");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        if (isNoResultFromDialog(pane)) {
            return null;
        }

        StringBuilder ipAddress = new StringBuilder();
        for (Iterator<JTextField> iterator = fields.iterator(); iterator.hasNext();) {
            JTextField field = iterator.next();
            String value = field.getText();
            if (StringTool.isEmptyString(value)) {
                continue;
            }
            ipAddress.append(value);
            if (iterator.hasNext()) {
                ipAddress.append('.');
            }
        }
        return ipAddress.toString();
    }

    /**
     *
     * @param trustStorePath
     * @return 
     */
    public static boolean fetchTrustStore(File trustStorePath) {
        String ipAddress = askIpAdderss();
        if (!StringTool.isEmptyString(ipAddress)) {
            try {
                java.net.URL url = new java.net.URL("http://" + ipAddress + ":1026/cert");
                java.io.FileOutputStream foWriter = new java.io.FileOutputStream(trustStorePath);
                java.io.InputStream iStream = url.openStream();
                int ch;
                try {
                    while ((ch = iStream.read()) != -1) {
                        foWriter.write(ch);
                    }
                } finally {
                    foWriter.close();
                }
            } catch (java.net.MalformedURLException ex) {
                exitWithMessage("不正な IP アドレスです" + ex.toString());
                return false;
            } catch (java.io.IOException ex) {
                exitWithMessage("キーストアの保存に失敗しました：" + System.getProperty("line.separator") + ex.toString());
                return false;
            }
            showMessage("キーストアを取得しました");
            return true;
        }
        return false;
    }

    /**
     *
     * @param message
     */
    private static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     *
     * @param message
     */
    private static void exitWithMessage(String message) {
        showMessage(message);
        // System.exit(-1);
    }
}

/**
 *
 * @author
 */
class IPAddressDocument extends PlainDocument {

    int currentValue = 0;
    JTextField field = null;

    /**
     *
     * @param field
     */
    public IPAddressDocument(JTextField field) {
        super();
        this.field = field;
    }

    /**
     *
     * @return
     */
    public int getValue() {
        return currentValue;
    }

    /**
     *
     * @param offset
     * @param str
     * @param attributes
     * @throws BadLocationException
     */
    @Override
    public void insertString(int offset, String str, AttributeSet attributes) throws BadLocationException {
        if (str == null) {
            return;
        }
        if (str.equals(".")) {
            field.transferFocus();
            return;
        }
        String newValue;
        int length = getLength();
        if (length == 0) {
            newValue = str;
        } else {
            String currentContent = getText(0, length);
            StringBuilder currentBuffer = new StringBuilder(currentContent);
            currentBuffer.insert(offset, str);
            newValue = currentBuffer.toString();
        }
        currentValue = checkInput(newValue, offset);
        super.insertString(offset, str, attributes);
    }

    /**
     *
     * @param offset
     * @param length
     * @throws BadLocationException
     */
    @Override
    public void remove(int offset, int length) throws BadLocationException {
        int currentLength = getLength();
        String currentContent = getText(0, currentLength);
        String before = currentContent.substring(0, offset);
        String after = currentContent.substring(length + offset, currentLength);
        String newValue = before + after;
        currentValue = checkInput(newValue, offset);
        super.remove(offset, length);
    }

    /**
     *
     * @param proposedValue
     * @param offset
     * @return
     * @throws BadLocationException
     */
    private int checkInput(String proposedValue, int offset) throws BadLocationException {
        if (proposedValue.length() <= 0) {
            return 0;
        }
        if (proposedValue.length() > 3) {
            throw new BadLocationException(proposedValue, offset);
        }
        try {
            int newValue = Integer.parseInt(proposedValue);
            return newValue;
        } catch (NumberFormatException e) {
            throw new BadLocationException(proposedValue, offset);
        }
    }
}
