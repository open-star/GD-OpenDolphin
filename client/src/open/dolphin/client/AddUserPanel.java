/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AddUserPanel.java
 *
 * Created on 2011/07/19, 16:40:23
 */
package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import open.dolphin.delegater.remote.RemoteUserDelegater;
import open.dolphin.infomodel.DepartmentModel;
import open.dolphin.infomodel.LicenseModel;
import open.dolphin.infomodel.RoleModel;
import open.dolphin.infomodel.UserModel;
import open.dolphin.project.GlobalVariables;

import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.security.EncryptUtil;
import open.dolphin.utils.StringTool;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.TaskService;

/**
 *
 * @author gd02
 */
public class AddUserPanel extends javax.swing.JPanel {

//        private static final String TITLE = "ユーザ管理";
//    private static final String FACILITY_INFO = "施設情報";
    //   private static final String ADD_USER = "ユーザ登録";
    //  private static final String LIST_USER = "ユーザリスト";
    //  private static final String FACILITY_SUCCESS_MSG = "施設情報を更新しました。";
    private static final String ADD_USER_SUCCESS_MSG = "ユーザを登録しました。";
    //  private static final String DELETE_USER_SUCCESS_MSG = "ユーザを削除しました。";
    //  private static final String DELETE_OK_USER_ = "選択したユーザを削除します";
    //  private static final String FACILITY_ERROR_MSG = "施設情報を更新できませんでした。";
    private static final String ADD_USER_ERROR_MSG = "ユーザを登録できませんでした。";
    //   private static final String DELETE_USER_ERROR_MSG = "ユーザを削除できませんでした。";
//    private static final String DELETE_NG_USER_ = "選択したユーザを削除できませんでした";//MEMO: unused?
    //  private static int DEFAULT_WIDTH = 600;
    //  private static int DEFAULT_HEIGHT = 370;
    // private String cn; // 氏名(sn & ' ' & givenName)
    private LicenseModel[] licenses; // 職種(MML0026)
    private DepartmentModel[] depts; // 診療科(MML0028)
    private boolean ok;
    // UserId と Password の長さ
    private int[] userIdLength = {4, 15}; // min,max
    private int[] passwordLength = {6, 10}; // min,max
    //  private String idPassPattern;//MEMO: unused?
    private String usersRole; // user に与える role 名
    private ApplicationContext appCtx;
    private Application app;
    private TaskService taskService;
    private TaskMonitor taskMonitor;

    /** Creates new form AddUserPanel */
    public AddUserPanel() {
        initComponents();

        appCtx = GlobalConstants.getApplicationContext();
        app = appCtx.getApplication();
        taskService = appCtx.getTaskService();
        taskMonitor = appCtx.getTaskMonitor();
        //      userIdLength = GlobalConstants.getIntArray("addUser.userId.length");
        //     passwordLength = GlobalConstants.getIntArray("addUser.password.length");
        usersRole = "user"; //GlobalVariables.getString("addUser.user.roleName");
        //     idPassPattern = "[A-Za-z0-9_+\\-.#$&@]*"; //GlobalVariables.getString("addUser.pattern.idPass");

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

        //    uid = new JTextField();
        //    uid.setColumns(10);
        uid.getDocument().addDocumentListener(dl);
        //    uid.setDocument(new RegexConstrainedDocument(idPassPattern));
        uid.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                userPassword1.requestFocus();
            }
        });
        uid.addFocusListener(AutoRomanListener.getInstance());

        //    userPassword1 = new JPasswordField();
        //    userPassword1.setColumns(10);
        userPassword1.getDocument().addDocumentListener(dl);
        //       userPassword1.setDocument(new RegexConstrainedDocument(idPassPattern));
        userPassword1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                userPassword2.requestFocus();
            }
        });
        userPassword1.addFocusListener(AutoRomanListener.getInstance());

        //    userPassword2 = new JPasswordField();
        //   userPassword2.setColumns(10);
        userPassword2.getDocument().addDocumentListener(dl);
        //      userPassword2.setDocument(new RegexConstrainedDocument(idPassPattern));
        userPassword2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sn.requestFocus();
            }
        });
        userPassword2.addFocusListener(AutoRomanListener.getInstance());

        //   sn = new JTextField();
        //  sn.setColumns(10);
        sn.getDocument().addDocumentListener(dl);
        sn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                givenName.requestFocus();
            }
        });
        sn.addFocusListener(AutoKanjiListener.getInstance());//logicsea

        //    givenName = new JTextField();
        // givenName.setColumns(10);
        givenName.getDocument().addDocumentListener(dl);
        givenName.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                emailField.requestFocus();
            }
        });
        givenName.addFocusListener(AutoKanjiListener.getInstance());

        //     emailField = new JTextField();
        //    emailField.setColumns(15);
        emailField.getDocument().addDocumentListener(dl);
        emailField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                uid.requestFocus();
            }
        });
        emailField.addFocusListener(AutoRomanListener.getInstance());

        licenses = GlobalConstants.getLicenseModel();
        //     licenseCombo = new JComboBox(licenses);
        licenseCombo.setModel(new DefaultComboBoxModel(licenses));

        depts = GlobalConstants.getDepartmentModel();
        //  deptCombo = new JComboBox(depts);
        deptCombo.setModel(new DefaultComboBoxModel(depts));
        
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addUserEntry();
            }
        };

        //   okButton = new JButton("追加");
        okButton.addActionListener(al);
        okButton.setEnabled(false);
        //   cancelButton = new JButton("閉じる");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        // レイアウト
        //  JPanel content = new JPanel(new GridBagLayout());
        //    JPanel content = this;
    //    int x = 0;
    //    int y = 0;
     //   JLabel label = new JLabel("ユーザID:", SwingConstants.RIGHT);
        //   constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //     constrain(content, uid, x + 1, y, 30, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

     //   x = 0;
     //   y += 1;
    //    label = new JLabel("パスワード:", SwingConstants.RIGHT);
        //   constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //     constrain(content, userPassword1, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
       // label = new JLabel("確認:", SwingConstants.RIGHT);
        //    constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //    constrain(content, userPassword2, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

    //    x = 0;
     //   y += 1;
    //    label = new JLabel("姓:", SwingConstants.RIGHT);
        ///  constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //   constrain(content, sn, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
      //  label = new JLabel("名:", SwingConstants.RIGHT);
        //  constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //   constrain(content, givenName, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

    //   x = 0;
     //   y += 1;
     //   label = new JLabel("医療資格:", SwingConstants.RIGHT);
        //   constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //      constrain(content, licenseCombo, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
    //    label = new JLabel("診療科:", SwingConstants.RIGHT);
        //   constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //    constrain(content, deptCombo, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

    //   x = 0;
     //   y += 1;
      //  label = new JLabel("電子メール:", SwingConstants.RIGHT);
        //   constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
        //   constrain(content, emailField, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

     //   x = 0;
      //  y += 1;
     //   label = new JLabel(" ", SwingConstants.RIGHT);
        //   constrain(content, label, x, y, 4, 1, GridBagConstraints.BOTH, GridBagConstraints.EAST);

     //   x = 0;
     //   y += 1;
     //   label = new JLabel("ユーザID - 半角英数記で" + userIdLength[0] + "文字以上" + userIdLength[1] + "文字以内");
        //   constrain(content, label, x, y, 4, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
    //    x = 0;
    //    y += 1;
     //   label = new JLabel("パスワード - 半角英数記で" + passwordLength[0] + "文字以上" + passwordLength[1] + "文字以内");
        //   constrain(content, label, x, y, 4, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);

        //     JPanel btnPanel = null;
     //   if (GlobalConstants.isMac()) {
            //    btnPanel = new JPanel();
            //    btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
            //    btnPanel.add(Box.createHorizontalGlue());
            //   btnPanel.add(cancelButton);
            //    btnPanel.add(Box.createHorizontalStrut(5));
            //   btnPanel.add(okButton);
            //        btnPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{cancelButton, okButton});
      //  } else {
            //   btnPanel = new JPanel();
            //    btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
            //   btnPanel.add(Box.createHorizontalGlue());
            //   btnPanel.add(okButton);
            ///   btnPanel.add(Box.createHorizontalStrut(5));
            //  btnPanel.add(cancelButton);
            //       btnPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{okButton, cancelButton});
//        }

        //   this.setLayout(new BorderLayout(0, 17));
        //    this.add(content, BorderLayout.CENTER);
        //    this.add(btnPanel, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        uid = new javax.swing.JTextField();
        userPassword1 = new javax.swing.JPasswordField();
        userPassword2 = new javax.swing.JPasswordField();
        sn = new javax.swing.JTextField();
        givenName = new javax.swing.JTextField();
        licenseCombo = new javax.swing.JComboBox();
        deptCombo = new javax.swing.JComboBox();
        emailField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        uid.setName("uid"); // NOI18N

        userPassword1.setName("userPassword1"); // NOI18N

        userPassword2.setName("userPassword2"); // NOI18N

        sn.setName("sn"); // NOI18N

        givenName.setName("givenName"); // NOI18N

        licenseCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        licenseCombo.setName("licenseCombo"); // NOI18N

        deptCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        deptCombo.setName("deptCombo"); // NOI18N

        emailField.setName("emailField"); // NOI18N

        okButton.setText("追加");
        okButton.setName("okButton"); // NOI18N

        cancelButton.setText("閉じる");
        cancelButton.setName("cancelButton"); // NOI18N

        jLabel1.setText("ユーザID");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("パスワード");
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText("姓");
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText("確認");
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText("名");
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText("医療資格");
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText("診療科");
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText("電子メール");
        jLabel8.setName("jLabel8"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(userPassword1, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                    .addComponent(uid)
                    .addComponent(sn)
                    .addComponent(emailField)
                    .addComponent(licenseCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(givenName, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(userPassword2, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(deptCombo, 0, 186, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(uid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userPassword2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(givenName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(licenseCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(deptCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JComboBox deptCombo;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField givenName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JComboBox licenseCombo;
    private javax.swing.JButton okButton;
    private javax.swing.JTextField sn;
    private javax.swing.JTextField uid;
    private javax.swing.JPasswordField userPassword1;
    private javax.swing.JPasswordField userPassword2;
    // End of variables declaration//GEN-END:variables

    private void addUserEntry() {

        if (!StringTool.patternCheck(uid.getText().trim(), userIdLength[0], userIdLength[1], "[A-Za-z0-9_+\\-.#$&@]*")) {
            return;
        }

        if (!passwordOk()) {
            return;
        }

        //    String userId = uid.getText().trim();
        String pass = new String(userPassword1.getPassword());
        UserModel loginUser = GlobalVariables.getUserModel();
        String facilityId = loginUser.getFacility().getFacilityId();

        String hashPass = EncryptUtil.createPasswordHash(pass);
        pass = null;

        final UserModel user = new UserModel();
        user.initialize(facilityId, uid.getText(), sn.getText(), givenName.getText(), hashPass);

        // 施設情報
        // 管理者のものを使用する
        user.setFacility(GlobalVariables.getUserModel().getFacility());

        // 医療資格
        int index = licenseCombo.getSelectedIndex();
        user.setLicenseModel(licenses[index]);

        // 診療科
        index = deptCombo.getSelectedIndex();
        user.setDepartmentModel(depts[index]);

        // MemberType
        // 管理者のものを使用する
        user.setMemberType(GlobalVariables.getUserModel().getMemberType());

        // RegisteredDate
        if (GlobalVariables.getUserModel().getMemberType().equals("ASP_TESTER")) {
            user.setRegisteredDate(GlobalVariables.getUserModel().getRegisteredDate());
        } else {
            user.setRegisteredDate(new Date());
        }

        // Email
        user.setEmail(emailField.getText().trim());

        // Role = user
        RoleModel rm = new RoleModel();
        rm.setRole(usersRole);
        user.addRole(rm);
        rm.setUser(user);
        rm.setUserId(user.getUserId()); // 必要

        // タスクを実行する
        final RemoteUserDelegater udl = new RemoteUserDelegater();

        int maxEstimation = 60000; //GlobalVariables.getInt("task.default.maxEstimation");
        int delay = 200; //GlobalVariables.getInt("task.default.delay");
        String addMsg = "登録しています..."; //GlobalVariables.getString("task.default.addMessage");

        Task task = new Task<Boolean, Void>(app) {//Async

            @Override
            protected Boolean doInBackground() throws Exception {
                int cnt = udl.putUser(user);
                return cnt > 0 ? true : false;
            }

            @Override
            protected void succeeded(Boolean results) {
                if (results.booleanValue()) {
                    JOptionPane.showMessageDialog(null, ADD_USER_SUCCESS_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(null, ADD_USER_ERROR_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.WARNING_MESSAGE);
                }
            }

            @Override
            protected void cancelled() {
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
            }
        };

        String message = null;
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, this, message, addMsg, delay, maxEstimation);
        taskMonitor.addPropertyChangeListener(w);

        taskService.execute(task);
    }
    /*
    private boolean userIdOk() {

    String userId = uid.getText().trim();
    if (userId.equals("")) {
    return false;
    }

    int len = userId.length();
    return (len >= userIdLength[0] && len <= userIdLength[1]) ? true : false;
    }
     */

    /**
     *
     * @return
     */
    private boolean passwordOk() {

        String passwd1 = new String(userPassword1.getPassword());
        String passwd2 = new String(userPassword2.getPassword());

        if (passwd1.equals("") || passwd2.equals("")) {
            return false;
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

    private void clearFields() {
        uid.setText("");
        userPassword1.setText("");
        userPassword2.setText("");
        sn.setText("");
        givenName.setText("");
        emailField.setText("");
    }

    private void constrain(JPanel container, Component cmp, int x, int y, int width, int height, int fill, int anchor) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = width;
        c.gridheight = height;
        c.fill = fill;
        c.anchor = anchor;
        c.insets = new Insets(0, 0, 5, 7);


        //    ((GridBagLayout) container.getLayout()).setConstraints(cmp, c);
        container.add(cmp);

    }

    public void stop() {
        setVisible(false);
    }
}
