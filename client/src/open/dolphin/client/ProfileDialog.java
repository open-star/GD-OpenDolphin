package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;

import open.dolphin.infomodel.RoleModel;
import open.dolphin.delegater.remote.RemoteUserDelegater;
import open.dolphin.infomodel.DepartmentModel;
import open.dolphin.infomodel.LicenseModel;
import open.dolphin.infomodel.UserModel;
import open.dolphin.helper.ComponentMemory;
import open.dolphin.project.*;

import java.awt.im.InputSubset;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.log.LogWriter;
import open.dolphin.security.EncryptUtil;
import open.dolphin.utils.StringTool;

/**
 *　プロフィールダイアログ
 * @author
 */
public class ProfileDialog extends JDialog { // AbstractMainTool {

    private static final String TITLE = "プロフィール変更";
    private static int DEFAULT_WIDTH = 568;
    private static int DEFAULT_HEIGHT = 300;
    private static final String PROGRESS_NOTE = "ユーザ情報を変更しています...";//MEMO: unused?
    private static final String UPDATE_BTN_TEXT = "変更";
    private static final String CLOSE_BTN_TEXT = "閉じる";
    private static final String USER_ID_TEXT = "ユーザID:";
    private static final String PASSWORD_TEXT = "パスワード:";
    private static final String CONFIRM_TEXT = "確認:";
    private static final String SIR_NAME_TEXT = "姓:";
    private static final String GIVEN_NAME_TEXT = "名:";
    private static final String EMAIL_TEXT = "電子メール:";
    private static final String LISENCE_TEXT = "医療資格:";
    private static final String DEPT_TEXT = "診療科:";
    private static final String PASSWORD_ASSIST_1 = "パスワード(半角英数で";
    private static final String PASSWORD_ASSIST_2 = "文字以上";
    private static final String PASSWORD_ASSIST_3 = "文字以内) 変更しない場合は空白にしておきます。";
    private static final String SUCCESS_MESSAGE = "ユーザ情報を変更しました。";
    private static final String ERROR_MESSAGE = "ユーザ情報の変更に失敗しました。";
    private static final String DUMMY_PASSWORD = "";
    private JDialog dialog;
    /**
     *
     */
    protected JButton okButton;

    /**
     *
     */
    public ProfileDialog() {
        setName(TITLE);
    }

    /**
     * 
     */
    public void start() {
        // Super Class で Frame を初期化する
        String title = GlobalConstants.getFrameTitle(getName());
        dialog = this;
        setTitle(title);
        setModal(true);

        //<TODO>アイコンを適切な物に変更
        ImageIcon icon = GlobalConstants.getImageIcon("web_32.gif");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
        ComponentMemory cm = new ComponentMemory(this, new Point(0, 0),
                new Dimension(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)),
                this);
        cm.putCenter();

        ProfilePanel cp = new ProfilePanel();
        cp.get();
        getContentPane().add(cp, BorderLayout.CENTER);
        getRootPane().setDefaultButton(okButton);
        setVisible(true);
    }

    //  @Override
    /**
     *
     */
    public void stop() {
        setVisible(false);
        dispose();
    }

    /**
     * 紹介状　MEMO:画面
     */
    protected class ProfilePanel extends JPanel {

        private JTextField uid; // 利用者ID
        private JPasswordField userPassword1; // パスワード1
        private JPasswordField userPassword2; // パスワード2
        private JTextField sn; // 姓
        private JTextField givenName; // 名
        private JTextField emailField;
        private LicenseModel[] licenses; // 職種(MML0026)
        private JComboBox licenseCombo;
        private DepartmentModel[] depts; // 診療科(MML0028)
        private JComboBox deptCombo;
        private JButton okButton;
        private JButton cancelButton;
        private boolean ok;
        //  private int[] userIdLength;
        //   private int[] passwordLength; // min,max
        private int[] userIdLength = {4, 15}; // min,max
        private int[] passwordLength = {6, 10}; // min,max

        /**
         *
         */
        public ProfilePanel() {

            //      userIdLength = GlobalConstants.getIntArray("addUser.userId.length");
            //    passwordLength = GlobalConstants.getIntArray("addUser.password.length");

            FocusAdapter imeOn = new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent event) {
                    JTextField tf = (JTextField) event.getSource();
                    tf.getInputContext().setCharacterSubsets(
                            new Character.Subset[]{InputSubset.KANJI});
                }
            };

            FocusAdapter imeOff = new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent event) {
                    JTextField tf = (JTextField) event.getSource();
                    tf.getInputContext().setCharacterSubsets(null);
                }
            };

            // DocumentListener
            DocumentListener dl = new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent e) {
                }

                @Override
                public void insertUpdate(DocumentEvent e) {
                    checkButton();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    checkButton();
                }
            };

            // ユーザIDフィールドを生成する
            uid = createTextField(10, null, imeOff, null);
            String pattern = "[A-Za-z0-9_+\\-.#$&@]*"; //GlobalVariables.getString("addUser.pattern.idPass");
            RegexConstrainedDocument userIdDoc = new RegexConstrainedDocument(pattern);
            uid.setEditable(false);
            uid.setDocument(userIdDoc);
            uid.getDocument().addDocumentListener(dl);
            //uid.setToolTipText(pattern);

            // パスワードフィールドを設定する
            userPassword1 = createPassField(10, null, imeOff, null);
            userPassword1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    userPassword2.requestFocus();
                }
            });

            userPassword2 = createPassField(10, null, imeOff, null);
            userPassword2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    sn.requestFocus();
                }
            });
            RegexConstrainedDocument passwordDoc1 = new RegexConstrainedDocument(pattern);
            userPassword1.setDocument(passwordDoc1);
            userPassword1.setToolTipText(pattern);
            userPassword1.getDocument().addDocumentListener(dl);
            RegexConstrainedDocument passwordDoc2 = new RegexConstrainedDocument(pattern);
            userPassword2.setDocument(passwordDoc2);
            userPassword2.getDocument().addDocumentListener(dl);
            userPassword2.setToolTipText(pattern);

            // 姓
            sn = createTextField(10, null, imeOn, dl);
            sn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    givenName.requestFocus();
                }
            });

            // 名
            givenName = createTextField(10, null, imeOn, dl);
            givenName.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    userPassword1.requestFocus();
                }
            });

            // 電子メール
            emailField = createTextField(20, null, imeOff, null);
            pattern = "[A-Za-z0-9_\\-.@]*"; //GlobalVariables.getString("addUser.pattern.email");
            RegexConstrainedDocument emailDoc = new RegexConstrainedDocument(pattern);
            emailField.setDocument(emailDoc);
            emailField.getDocument().addDocumentListener(dl);

            // 医療資格
            licenses = GlobalConstants.getLicenseModel();
            licenseCombo = new JComboBox(licenses);
            boolean readOnly = GlobalVariables.isReadOnly();
            licenseCombo.setEnabled(!readOnly);

            // 診療科
            depts = GlobalConstants.getDepartmentModel();
            deptCombo = new JComboBox(depts);
            deptCombo.setEnabled(true);

            // OK Btn
            ActionListener al = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    changePassword();
                }
            };

            okButton = new JButton(UPDATE_BTN_TEXT);
            okButton.addActionListener(al);
            //okButton.setMnemonic(KeyEvent.VK_U);
            okButton.setEnabled(false);

            // Cancel Btn
            cancelButton = new JButton(CLOSE_BTN_TEXT);
            cancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    stop();
                }
            });

            // レイアウト
            JPanel content = new JPanel(new GridBagLayout());

            int x = 0;
            int y = 0;
            JLabel label = new JLabel(USER_ID_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, uid, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel(PASSWORD_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, userPassword1, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
            label = new JLabel(CONFIRM_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, userPassword2, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel(SIR_NAME_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, sn, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
            label = new JLabel(GIVEN_NAME_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, givenName, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel(EMAIL_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, emailField, x + 1, y, 2, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel(LISENCE_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, licenseCombo, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
            label = new JLabel(DEPT_TEXT, SwingConstants.RIGHT);
            constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, deptCombo, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel(" ", SwingConstants.RIGHT);
            constrain(content, label, x, y, 4, 1, GridBagConstraints.BOTH, GridBagConstraints.EAST);

            x = 0;
            y += 1;
            label = new JLabel(PASSWORD_ASSIST_1 + passwordLength[0] + PASSWORD_ASSIST_2 + passwordLength[1] + PASSWORD_ASSIST_3);
            constrain(content, label, x, y, 4, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);

            JPanel btnPanel = null;

            //   if (isMac()) {
            if (GlobalConstants.isMac()) {
                btnPanel = createCommandButtonPanel(new JPanel(), new JButton[]{cancelButton, okButton});
            } else {
                btnPanel = createCommandButtonPanel(new JPanel(), new JButton[]{okButton, cancelButton});
            }

            this.setLayout(new BorderLayout(0, 17));
            this.add(content, BorderLayout.CENTER);
            this.add(btnPanel, BorderLayout.SOUTH);

            this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
        }

        private JPanel createCommandButtonPanel(JPanel p, JButton[] btns) {
            p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
            p.add(Box.createHorizontalGlue());
            p.add(btns[0]);
            for (int i = 1; i < btns.length; i++) {
                p.add(Box.createHorizontalStrut(5));
                p.add(btns[i]);
            }
            return p;
        }

        /**
         *
         */
        public void get() {

            // UserModel を Project から設定する
            UserModel user = GlobalVariables.getUserModel();
            uid.setText(user.idAsLocal());
            sn.setText(user.getSirName());
            givenName.setText(user.getGivenName());
            userPassword1.setText(DUMMY_PASSWORD);
            userPassword2.setText(DUMMY_PASSWORD);
            emailField.setText(user.getEmail());
            String license = user.getLicenseModel().getLicense();
            for (int i = 0; i < licenses.length; i++) {
                if (license.equals(licenses[i].getLicense())) {
                    licenseCombo.setSelectedIndex(i);
                    break;
                }
            }
            String deptStr = user.getDepartmentModel().getDepartment();
            for (int i = 0; i < depts.length; i++) {
                if (deptStr.equals(depts[i].getDepartment())) {
                    deptCombo.setSelectedIndex(i);
                    break;
                }
            }
            checkButton();
        }

        /**
         *
         */
        private void changePassword() {

            // 有効なパスワードでなければリターンする
            if (!passwordOk()) {
                return;
            }

            // Project からユーザモデルを取得する
            UserModel user = GlobalVariables.getUserModel();

            // 更新が成功するまでは変更しない
            final UserModel updateModel = new UserModel();
            updateModel.setId(user.getId());
            updateModel.setFacility(user.getFacility());
            updateModel.setMemberType(user.getFacility().getMemberType());
            //updateModel.setMemberType(user.getMemberType());

            // ログインIDを設定する
            String userId = user.getFacility().getFacilityId() + ":" + uid.getText().trim();
            updateModel.setUserId(userId);

            // パスワードを設定する
            final String password = new String(userPassword1.getPassword());

            if (!password.equals(DUMMY_PASSWORD)) {

                String charset = null;//MEMO: unused?
                String hashPass = EncryptUtil.createPasswordHash(password);
                updateModel.setPassword(hashPass);

            } else {
                // パスワードは変更されていない
                updateModel.setPassword(user.getPassword());
            }

            // 姓名を設定する
            String snSt = sn.getText().trim();
            updateModel.setSirName(snSt);
            String givenNameSt = givenName.getText().trim();
            updateModel.setGivenName(givenNameSt);
            updateModel.setCommonName(snSt + " " + givenNameSt);

            // 電子メールを設定する
            updateModel.setEmail(emailField.getText().trim());

            // 医療資格を設定する
            int selected = licenseCombo.getSelectedIndex();
            updateModel.setLicenseModel(licenses[selected]);

            // 診療科を設定する
            selected = deptCombo.getSelectedIndex();
            updateModel.setDepartmentModel(depts[selected]);

            // Roleを付け加える
            Collection<RoleModel> roles = user.getRoles();
            for (RoleModel role : roles) {
                role.setUserId(user.getUserId());
                RoleModel updateRole = new RoleModel();
                updateRole.setId(role.getId());
                updateRole.setRole(role.getRole());
                updateRole.setUser(updateModel);
                updateRole.setUserId(updateModel.getUserId());
                updateModel.addRole(updateRole);
            }

            // タスクを実行する
            final RemoteUserDelegater udl = new RemoteUserDelegater();
            //     int maxEstimation = 60000; //GlobalVariables.getInt("task.default.maxEstimation");
            //    int delay = 200; //GlobalVariables.getInt("task.default.delay");

            int cnt;
            try {
                cnt = udl.updateUser(updateModel);
                Boolean result = cnt > 0 ? new Boolean(true) : new Boolean(false);//MEMO: unused?
                if (!udl.isError()) {
                    // Project を更新する
                    GlobalVariables.setUserModel(updateModel);
                    DolphinPrincipal principal = new DolphinPrincipal();
                    principal.setUserId(updateModel.idAsLocal());
                    principal.setFacilityId(updateModel.getFacility().getFacilityId());
                    GlobalVariables.setUserId(updateModel.idAsLocal());
                    GlobalVariables.setDolphinPrincipal(principal);
                    JOptionPane.showMessageDialog(dialog, SUCCESS_MESSAGE, GlobalConstants.getFrameTitle("パスワード変更"), JOptionPane.INFORMATION_MESSAGE);
                    stop();
                } else {
                    JOptionPane.showMessageDialog(dialog, ERROR_MESSAGE, GlobalConstants.getFrameTitle("パスワード変更"), JOptionPane.WARNING_MESSAGE);
                    stop();
                }
            } catch (Exception ex) {
                LogWriter.error(getClass(), ex);
            }


            /*
            Task task = new Task<Boolean, Void>(app) {

            @Override
            protected Boolean doInBackground() throws Exception {
            LogWriter.debug(getClass(), "ChangePassword doInBackground");
            int cnt = udl.updateUser(updateModel);
            return cnt > 0 ? new Boolean(true) : new Boolean(false);
            }

            @Override
            protected void succeeded(Boolean result) {
            LogWriter.debug(getClass(), "ChangePassword succeeded");
            if (udl.isNoError()) {
            // Project を更新する
            GlobalVariables.setUserModel(updateModel);
            DolphinPrincipal principal = new DolphinPrincipal();
            principal.setUserId(updateModel.idAsLocal());
            principal.setFacilityId(updateModel.getFacility().getFacilityId());
            GlobalVariables.setUserId(updateModel.idAsLocal());
            GlobalVariables.setDolphinPrincipal(principal);
            JOptionPane.showMessageDialog(getFrame(), SUCCESS_MESSAGE, GlobalConstants.getFrameTitle("パスワード変更"), JOptionPane.INFORMATION_MESSAGE);
            stop();
            } else {
            JOptionPane.showMessageDialog(getFrame(), ERROR_MESSAGE, GlobalConstants.getFrameTitle("パスワード変更"), JOptionPane.WARNING_MESSAGE);
            stop();
            }
            }

            @Override
            protected void cancelled() {
            LogWriter.debug(getClass(), "ChangePassword cancelled");

            }

            @Override
            protected void failed(java.lang.Throwable cause) {
            LogWriter.debug(getClass(), "ChangePassword failed");
            LogWriter.error(getClass(), cause.getMessage(), cause.getCause());
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
            LogWriter.debug(getClass(), "ChangePassword interrupted");
            LogWriter.warn(getClass(), e.getMessage());
            }
            };

            TaskMonitor taskMonitor = appCtx.getTaskMonitor();
            String message = null;
            Component c = getFrame();
            TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, PROGRESS_NOTE, delay, maxEstimation);
            taskMonitor.addPropertyChangeListener(w);

            appCtx.getTaskService().execute(task);
             */

        }

        /**
         *
         * @param busy
         */
        private void setBusy(boolean busy) {
            if (busy) {
                okButton.setEnabled(false);
            } else {
                okButton.setEnabled(true);
            }
        }

        /**
         *
         * @return
         */
        private boolean userIdOk() {

            String userId = uid.getText().trim();
            if (userId.equals("")) {
                return false;
            }

            if (userId.length() < userIdLength[0] || userId.length() > userIdLength[1]) {
                return false;
            }

            return true;
        }

        /**
         *
         * @return
         */
        private boolean passwordOk() {

            String passwd1 = new String(userPassword1.getPassword());
            String passwd2 = new String(userPassword2.getPassword());

            if (passwd1.equals(DUMMY_PASSWORD) && passwd2.equals(DUMMY_PASSWORD)) {
                return true;
            }

            if ((passwd1.length() < passwordLength[0]) || (passwd1.length() > passwordLength[1])) {
                return false;
            }

            if ((passwd2.length() < passwordLength[0]) || (passwd2.length() > passwordLength[1])) {
                return false;
            }

            return passwd1.equals(passwd2) ? true : false;
        }

        /**
         *
         */
        private void checkButton() {

            //      boolean uidOk = userIdOk();
            //        boolean passwordOk = passwordOk();
            //      boolean snOk = sn.getText().trim().equals("") ? false : true;
            //         boolean givenOk = givenName.getText().trim().equals("") ? false : true;
            //    boolean emailOk = email.getText().trim().equals("") ? false : true;
            //    boolean newOk = (uidOk && passwordOk && snOk && givenOk && emailOk) ? true : false;

            boolean userOk = StringTool.patternCheck(uid.getText().trim(), userIdLength[0], userIdLength[1], "[A-Za-z0-9_+\\-.#$&@]*");
            boolean passwordOk = passwordOk();
            boolean snOk = StringTool.patternCheck(sn.getText().trim(), 1, 0, "");
            boolean givenOk = StringTool.patternCheck(givenName.getText().trim(), 1, 0, "");
            boolean emailOk = StringTool.patternCheck(emailField.getText().trim(), 0, 0, ".+@.+");
            boolean newOk = (userOk && passwordOk && snOk && givenOk && emailOk);

            if (ok != newOk) {
                ok = newOk;
                okButton.setEnabled(ok);
            }
        }
    }

    /**
     *
     * @param val
     * @param margin
     * @param fa
     * @param dl
     * @return
     */
    private JTextField createTextField(int val, Insets margin, FocusAdapter fa, DocumentListener dl) {

        if (val == 0) {
            val = 30;
        }
        JTextField tf = new JTextField(val);

        if (margin == null) {
            margin = new Insets(1, 2, 1, 2);
        }
        tf.setMargin(margin);

        if (dl != null) {
            tf.getDocument().addDocumentListener(dl);
        }

        if (fa != null) {
            tf.addFocusListener(fa);
        }

        return tf;
    }

    /**
     *
     * @param val
     * @param margin
     * @param fa
     * @param dl
     * @return
     */
    private JPasswordField createPassField(int val, Insets margin, FocusAdapter fa, DocumentListener dl) {

        if (val == 0) {
            val = 30;
        }
        JPasswordField tf = new JPasswordField(val);

        if (margin == null) {
            margin = new Insets(1, 2, 1, 2);
        }
        tf.setMargin(margin);

        if (dl != null) {
            tf.getDocument().addDocumentListener(dl);
        }

        if (fa != null) {
            tf.addFocusListener(fa);
        }

        return tf;
    }

    /**
     *
     * @param container
     * @param cmp
     * @param x
     * @param y
     * @param width
     * @param height
     * @param fill
     * @param anchor
     */
    private void constrain(JPanel container, Component cmp, int x, int y, int width, int height, int fill, int anchor) {

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.fill = fill;
        c.anchor = anchor;
        c.insets = new Insets(0, 0, 5, 7);
        ((GridBagLayout) container.getLayout()).setConstraints(cmp, c);
        container.add(cmp);
    }
    //  private boolean isMac() {
    //      String os = System.getProperty("os.name").toLowerCase();
    //      return os.startsWith("mac") ? true : false;
    //  }
}
