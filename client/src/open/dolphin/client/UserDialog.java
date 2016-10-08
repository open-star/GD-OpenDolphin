package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import open.dolphin.delegater.remote.RemoteUserDelegater;
import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.UserModel;
import open.dolphin.helper.ComponentMemory;
import open.dolphin.project.GlobalVariables;
import open.dolphin.table.*;

import java.util.List;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import open.dolphin.project.GlobalSettings;
import open.dolphin.log.LogWriter;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.TaskService;

/**
 *　ユーザ追加　MEMO:画面
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class UserDialog extends JDialog { // AbstractMainTool {

    private static final String TITLE = "ユーザ管理";
    private static final String FACILITY_INFO = "施設情報";
    private static final String ADD_USER = "ユーザ登録";
    private static final String LIST_USER = "ユーザリスト";
    private static final String FACILITY_SUCCESS_MSG = "施設情報を更新しました。";
 //   private static final String ADD_USER_SUCCESS_MSG = "ユーザを登録しました。";
    private static final String DELETE_USER_SUCCESS_MSG = "ユーザを削除しました。";
    private static final String DELETE_OK_USER_ = "選択したユーザを削除します";
    private static final String FACILITY_ERROR_MSG = "施設情報を更新できませんでした。";
  //  private static final String ADD_USER_ERROR_MSG = "ユーザを登録できませんでした。";
    private static final String DELETE_USER_ERROR_MSG = "ユーザを削除できませんでした。";
   // private static final String DELETE_NG_USER_ = "選択したユーザを削除できませんでした";//MEMO: unused?
    private static int DEFAULT_WIDTH = 600;
    private static int DEFAULT_HEIGHT = 370;
    private JDialog dialog;
    private ApplicationContext appCtx;
    private Application app;
    private TaskService taskService;
    private TaskMonitor taskMonitor;
    //   private IMainWindow context;

    /** Creates a new instance of AddUserService */
    public UserDialog() {
        setName(TITLE);
        appCtx = GlobalConstants.getApplicationContext();
        app = appCtx.getApplication();
        taskService = appCtx.getTaskService();
        taskMonitor = appCtx.getTaskMonitor();
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
                new Dimension(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)), this);
        cm.putCenter();

        // Component を生成する
        AddUserPanel ap = new AddUserPanel();
        FacilityInfoPanel fp = new FacilityInfoPanel();
        UserListPanel mp = new UserListPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(FACILITY_INFO, fp);
        tabbedPane.addTab(ADD_USER, ap);
        tabbedPane.addTab(LIST_USER, mp);
        fp.get();

        // Frame に加える
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
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
     * 施設（医療機関）情報を変更するクラス　MEMO:画面
     */
    protected class FacilityInfoPanel extends JPanel {

        // 施設情報フィールド
        private JTextField facilityName;
        private JTextField zipField1;
        private JTextField zipField2;
        private JTextField addressField;
        private JTextField areaField;
        private JTextField cityField;
        private JTextField numberField;
        private JTextField urlField;
        // 更新等のボタン
        private JButton updateBtn;
        private JButton clearBtn;
        private JButton closeBtn;
        private boolean hasInitialized;

        /**
         *
         */
        public FacilityInfoPanel() {

            // GUI生成           
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
            facilityName = new JTextField();
            facilityName.setColumns(30);
            facilityName.getDocument().addDocumentListener(dl);

            zipField1 = new JTextField();
            zipField1.setColumns(3);
            zipField1.getDocument().addDocumentListener(dl);

            zipField2 = new JTextField();
            zipField2.setColumns(3);
            zipField2.getDocument().addDocumentListener(dl);

            addressField = new JTextField();
            addressField.setColumns(30);
            addressField.getDocument().addDocumentListener(dl);

            areaField = new JTextField();
            areaField.setColumns(3);
            areaField.getDocument().addDocumentListener(dl);

            cityField = new JTextField();
            cityField.setColumns(3);
            cityField.getDocument().addDocumentListener(dl);

            numberField = new JTextField();
            numberField.setColumns(3);
            numberField.getDocument().addDocumentListener(dl);

            urlField = new JTextField();
            urlField.setColumns(30);
            urlField.getDocument().addDocumentListener(dl);


            facilityName.addFocusListener(AutoKanjiListener.getInstance());
            zipField1.addFocusListener(AutoRomanListener.getInstance());
            zipField2.addFocusListener(AutoRomanListener.getInstance());
            addressField.addFocusListener(AutoKanjiListener.getInstance());
            areaField.addFocusListener(AutoRomanListener.getInstance());
            cityField.addFocusListener(AutoRomanListener.getInstance());
            numberField.addFocusListener(AutoRomanListener.getInstance());
            urlField.addFocusListener(AutoRomanListener.getInstance());


            updateBtn = new JButton("更新");
            updateBtn.setEnabled(false);
            updateBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    update();
                }
            });

            clearBtn = new JButton("戻す");
            clearBtn.setEnabled(false);
            clearBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    get();
                }
            });

            closeBtn = new JButton("閉じる");
            closeBtn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    stop();
                }
            });

            // レイアウト
            JPanel content = new JPanel(new GridBagLayout());

            int x = 0;
            int y = 0;
            JLabel label = new JLabel("医療機関名:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, facilityName, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("郵便番号:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);

            JPanel p = new JPanel();
            p.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p.add(zipField1);
            p.add(new JLabel(" - "));
            p.add(zipField2);
            constrain(content, p, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("住  所:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, addressField, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("電話番号:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);

            JPanel telephonePanel = new JPanel();
            telephonePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            telephonePanel.add(areaField);
            telephonePanel.add(new JLabel(" - "));
            telephonePanel.add(cityField);
            telephonePanel.add(new JLabel(" - "));
            telephonePanel.add(numberField);
            constrain(content, telephonePanel, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("URL:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, urlField, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel(" ", SwingConstants.RIGHT);
            constrain(content, label, x, y, 2, 1, GridBagConstraints.BOTH, GridBagConstraints.EAST);

            JPanel btnPanel = null;
            if (GlobalConstants.isMac()) {
                btnPanel = new JPanel();
                btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                btnPanel.add(Box.createHorizontalGlue());
                btnPanel.add(clearBtn);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(closeBtn);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(updateBtn);
            } else {
                btnPanel = new JPanel();
                btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                btnPanel.add(Box.createHorizontalGlue());
                btnPanel.add(updateBtn);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(clearBtn);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(closeBtn);
            }

            this.setLayout(new BorderLayout(0, 11));
            this.add(content, BorderLayout.CENTER);
            this.add(btnPanel, BorderLayout.SOUTH);
            this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
        }

        /**
         *
         */
        public void get() {
            UserModel user = GlobalVariables.getUserModel();
            FacilityModel facility = user.getFacility();

            if (facility.getFacilityName() != null) {
                facilityName.setText(facility.getFacilityName());
            }

            if (facility.getZipCode() != null) {
                String val = facility.getZipCode();
                try {
                    StringTokenizer st = new StringTokenizer(val, "-");
                    if (st.hasMoreTokens()) {
                        zipField1.setText(st.nextToken());
                        zipField2.setText(st.nextToken());
                    }
                } catch (Exception e) {
                    LogWriter.error(this.getClass(), "", e);
                }
            }

            if (facility.getAddress() != null) {
                addressField.setText(facility.getAddress());
            }

            if (facility.getTelephone() != null) {
                String val = facility.getTelephone();
                try {
                    StringTokenizer st = new StringTokenizer(val, "-");
                    if (st.hasMoreTokens()) {
                        areaField.setText(st.nextToken());
                        cityField.setText(st.nextToken());
                        numberField.setText(st.nextToken());
                    }
                } catch (Exception e) {
                    LogWriter.error(this.getClass(), "", e);
                }
            }

            if (facility.getUrl() != null) {
                urlField.setText(facility.getUrl());
            }

            hasInitialized = true;
        }

        /**
         *
         */
        private void checkButton() {
            if (hasInitialized) {
                boolean nameEmpty = facilityName.getText().trim().equals("") ? true : false;
                boolean zip1Empty = zipField1.getText().trim().equals("") ? true : false;
                boolean zip2Empty = zipField2.getText().trim().equals("") ? true : false;
                boolean addressEmpty = addressField.getText().trim().equals("") ? true : false;
                boolean areaEmpty = areaField.getText().trim().equals("") ? true : false;
                boolean cityEmpty = cityField.getText().trim().equals("") ? true : false;
                boolean numberEmpty = numberField.getText().trim().equals("") ? true : false;
                if (nameEmpty && zip1Empty && zip2Empty && addressEmpty && areaEmpty && cityEmpty && numberEmpty) {
                    if (clearBtn.isEnabled()) {
                        clearBtn.setEnabled(false);
                    }
                } else {
                    if (!clearBtn.isEnabled()) {
                        clearBtn.setEnabled(true);
                    }
                }
                // 施設名フィールドが空の場合
                if (nameEmpty) {
                    if (updateBtn.isEnabled()) {
                        updateBtn.setEnabled(false);
                    }
                    return;
                }
                // 施設名フィールドは空ではない
                if (!updateBtn.isEnabled()) {
                    updateBtn.setEnabled(true);
                }
            }
        }

        /**
         *
         */
        private void update() {

            final UserModel user = GlobalVariables.getUserModel();
            // ディタッチオブジェクトが必要である
            FacilityModel facility = user.getFacility();

            // 医療機関コードは変更できない

            // 施設名
            String val = facilityName.getText().trim();
            if (!val.equals("")) {
                facility.setFacilityName(val);
            }

            // 郵便番号
            val = zipField1.getText().trim();
            String val2 = zipField2.getText().trim();
            if ((!val.equals("")) && (!val2.equals(""))) {
                facility.setZipCode(val + "-" + val2);
            }

            // 住所
            val = addressField.getText().trim();
            if (!val.equals("")) {
                facility.setAddress(val);
            }

            // 電話番号
            val = areaField.getText().trim();
            val2 = cityField.getText().trim();
            String val3 = numberField.getText().trim();
            if ((!val.equals("")) && (!val2.equals("")) && (!val3.equals(""))) {
                facility.setTelephone(val + "-" + val2 + "-" + val3);
            }

            // URL
            val = urlField.getText().trim();
            if (!val.equals("")) {
                facility.setUrl(val);
            }

            // 登録日
            // 変更しない

            // タスクを実行する
            final RemoteUserDelegater udl = new RemoteUserDelegater();

            int cnt;
            try {
                cnt = udl.updateFacility(user);
                Boolean result = cnt > 0 ? true : false;
                if (result.booleanValue()) {
                    JOptionPane.showMessageDialog(this, FACILITY_SUCCESS_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, FACILITY_ERROR_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                LogWriter.error(this.getClass(), "", ex);
            }
        }
    }

    /**
     * ユーザリストを取得するクラス　MEMO:画面
     */
    protected class UserListPanel extends JPanel {

        private ObjectTableModel tableModel;
        private JTable table;
        private JButton getButton;
        private JButton deleteButton;
        private JButton cancelButton;

        /**
         *
         */
        public UserListPanel() {

            String[] columns = new String[]{"ユーザID", "姓", "名", "医療資格", "診療科"};

            // ユーザテーブル
            tableModel = new ObjectTableModel(columns, 7) {

                // 編集不可
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }

                // オブジェクトをテーブルに表示する
                @Override
                public Object getValueAt(int row, int col) {

                    UserModel entry = (UserModel) getObject(row);
                    if (entry == null) {
                        return null;
                    }

                    String ret = null;
                    switch (col) {
                        case 0:
                            ret = entry.idAsLocal();
                            break;
                        case 1:
                            ret = entry.getSirName();
                            break;
                        case 2:
                            ret = entry.getGivenName();
                            break;
                        case 3:
                            ret = entry.getLicenseModel().getLicenseDesc();
                            break;
                        case 4:
                            ret = entry.getDepartmentModel().getDepartmentDesc();
                            break;
                        default: LogWriter.fatal(getClass(), "case default");
                    }
                    return ret;
                }
            };

            table = new JTable(tableModel);
            // Selection を設定する
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setRowSelectionAllowed(true);
            table.setToolTipText(DELETE_OK_USER_);

            ListSelectionModel m = table.getSelectionModel();
            m.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting() == false) {
                        // 削除ボタンをコントロールする
                        // 医療資格が other 以外は削除できない
                        int index = table.getSelectedRow();
                        UserModel entry = (UserModel) tableModel.getObject(index);
                        if (entry == null) {
                            return;
                        } else {
                            controleDelete(entry);
                        }
                    }
                }
            });

            // Layout
            JScrollPane scroller = new JScrollPane(table,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
            getButton = new JButton("ユーザリスト");
            getButton.setEnabled(true);
            getButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getUsers();
                }
            });

            deleteButton = new JButton("削除");
            deleteButton.setEnabled(false);
            deleteButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteUser();
                }
            });
            deleteButton.setToolTipText(DELETE_OK_USER_);

            cancelButton = new JButton("閉じる");
            cancelButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    stop();
                }
            });

            JPanel btnPanel = null;
            if (GlobalConstants.isMac()) {
                btnPanel = new JPanel();
                btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                btnPanel.add(Box.createHorizontalGlue());
                btnPanel.add(deleteButton);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(cancelButton);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(getButton);

                //        btnPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{deleteButton, cancelButton, getButton});
            } else {
                btnPanel = new JPanel();
                btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                btnPanel.add(Box.createHorizontalGlue());
                btnPanel.add(getButton);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(deleteButton);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(cancelButton);

                //      btnPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{getButton, deleteButton, cancelButton});
            }
            this.setLayout(new BorderLayout(0, 17));
            this.add(scroller, BorderLayout.CENTER);
            this.add(btnPanel, BorderLayout.SOUTH);
            this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
        }

        /**
         * 医療資格が other 以外は削除できない。
         * @param user
         */
        private void controleDelete(UserModel user) {
            boolean isMe = user.getId() == GlobalVariables.getUserModel().getId() ? true : false;
            deleteButton.setEnabled(!isMe);
        }

        /**
         * 施設内の全ユーザを取得する。
         */
        private void getUsers() {

            final RemoteUserDelegater udl = new RemoteUserDelegater();

            int maxEstimation = 60000; //GlobalVariables.getInt("task.default.maxEstimation");
            int delay = 200; //GlobalVariables.getInt("task.default.delay");
            String note = "検索しています..."; //GlobalVariables.getString("task.default.searchMessage");
            Task task = new Task<List, Void>(app) {

                @Override
                protected List doInBackground() throws Exception {//Async
                    List<UserModel> result = udl.getAllUser();
                    return result;
                }

                @Override
                protected void succeeded(List results) {
                    if (udl.isError()) {
                        JOptionPane.showMessageDialog(dialog, "施設内全ユーザ情報の取得に失敗しました.", GlobalConstants.getFrameTitle(getName()), JOptionPane.WARNING_MESSAGE);
                    } else {
                        tableModel.setObjectList(results);
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
            TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, dialog, message, note, delay, maxEstimation);
            taskMonitor.addPropertyChangeListener(w);
            taskService.execute(task);
        }

        /**
         * 選択したユーザを削除する。
         *
         */
        private void deleteUser() {

            int row = table.getSelectedRow();
            UserModel entry = (UserModel) tableModel.getObject(row);
            if (entry == null) {
                return;
            }

            //
            // 作成したドキュメントも削除するかどうかを選ぶ
            //
            boolean deleteDoc = true;//MEMO: unused?
            if (entry.getLicenseModel().getLicense().equals("doctor")) {
                deleteDoc = false;
            }

            final RemoteUserDelegater udl = new RemoteUserDelegater();

            int maxEstimation = 60000; //GlobalVariables.getInt("task.default.maxEstimation");
            int delay = 200; //GlobalVariables.getInt("task.default.delay");
            String note = "削除しています..."; //GlobalVariables.getString("task.default.deleteMessage");        
            final String deleteId = entry.getUserId();

            Task task = new Task<List, Void>(app) {

                @Override
                protected List doInBackground() throws Exception {//Async
                    List result = null;
                    if (udl.removeUser(deleteId) > 0) {
                        result = udl.getAllUser();
                    }
                    return result;
                }

                @Override
                protected void succeeded(List results) {
                    if (udl.isError()) {
                        //                LogWriter.warn(UserDialog.class, udl.getErrorMessage());
                        JOptionPane.showMessageDialog(dialog, DELETE_USER_ERROR_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.WARNING_MESSAGE);
                    } else {
                        tableModel.setObjectList(results);
                        JOptionPane.showMessageDialog(dialog, DELETE_USER_SUCCESS_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.INFORMATION_MESSAGE);
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
            TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, dialog, message, note, delay, maxEstimation);
            taskMonitor.addPropertyChangeListener(w);
            taskService.execute(task);
        }
    }

    /**
     * 施設内ユーザ登録クラス　MEMO:画面
     *//*
    protected class AddUserPanelOrg extends JPanel {

        private JTextField uid; // 利用者ID
        private JPasswordField userPassword1; // パスワード
        private JPasswordField userPassword2; // パスワード
        private JTextField sn; // 姓
        private JTextField givenName; // 名
        // private String cn; // 氏名(sn & ' ' & givenName)
        private LicenseModel[] licenses; // 職種(MML0026)
        private JComboBox licenseCombo;
        private DepartmentModel[] depts; // 診療科(MML0028)
        private JComboBox deptCombo;
        // private String authority; // LASに対する権限(admin:管理者,user:一般利用者)
        private JTextField emailField; // メールアドレス
        // JTextField description;
        private JButton okButton;
        private JButton cancelButton;
        private boolean ok;
        // UserId と Password の長さ
        private int[] userIdLength = {4, 15}; // min,max
        private int[] passwordLength = {6, 10}; // min,max
        private String idPassPattern;//MEMO: unused?
        private String usersRole; // user に与える role 名

   
        public AddUserPanelOrg() {

            //      userIdLength = GlobalConstants.getIntArray("addUser.userId.length");
            //     passwordLength = GlobalConstants.getIntArray("addUser.password.length");
            usersRole = "user"; //GlobalVariables.getString("addUser.user.roleName");
            idPassPattern = "[A-Za-z0-9_+\\-.#$&@]*"; //GlobalVariables.getString("addUser.pattern.idPass");

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

            uid = new JTextField();
            uid.setColumns(10);
            uid.getDocument().addDocumentListener(dl);
            //    uid.setDocument(new RegexConstrainedDocument(idPassPattern));
            uid.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    userPassword1.requestFocus();
                }
            });
            uid.addFocusListener(AutoRomanListener.getInstance());

            userPassword1 = new JPasswordField();
            userPassword1.setColumns(10);
            userPassword1.getDocument().addDocumentListener(dl);
            //       userPassword1.setDocument(new RegexConstrainedDocument(idPassPattern));
            userPassword1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    userPassword2.requestFocus();
                }
            });
            userPassword1.addFocusListener(AutoRomanListener.getInstance());

            userPassword2 = new JPasswordField();
            userPassword2.setColumns(10);
            userPassword2.getDocument().addDocumentListener(dl);
            //      userPassword2.setDocument(new RegexConstrainedDocument(idPassPattern));
            userPassword2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    sn.requestFocus();
                }
            });
            userPassword2.addFocusListener(AutoRomanListener.getInstance());

            sn = new JTextField();
            sn.setColumns(10);
            sn.getDocument().addDocumentListener(dl);
            sn.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    givenName.requestFocus();
                }
            });
            sn.addFocusListener(AutoKanjiListener.getInstance());//logicsea

            givenName = new JTextField();
            givenName.setColumns(10);
            givenName.getDocument().addDocumentListener(dl);
            givenName.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    emailField.requestFocus();
                }
            });
            givenName.addFocusListener(AutoKanjiListener.getInstance());

            emailField = new JTextField();
            emailField.setColumns(15);
            emailField.getDocument().addDocumentListener(dl);
            emailField.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    uid.requestFocus();
                }
            });
            emailField.addFocusListener(AutoRomanListener.getInstance());

            licenses = GlobalConstants.getLicenseModel();
            licenseCombo = new JComboBox(licenses);

            depts = GlobalConstants.getDepartmentModel();
            deptCombo = new JComboBox(depts);

            ActionListener al = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    addUserEntry();
                }
            };

            okButton = new JButton("追加");
            okButton.addActionListener(al);
            okButton.setEnabled(false);
            cancelButton = new JButton("閉じる");
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
            JLabel label = new JLabel("ユーザID:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST); 
            constrain(content, uid, x + 1, y, 30, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("パスワード:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, userPassword1, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
            label = new JLabel("確認:", SwingConstants.RIGHT);
            constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, userPassword2, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("姓:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, sn, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
            label = new JLabel("名:", SwingConstants.RIGHT);
            constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, givenName, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("医療資格:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, licenseCombo, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);
            label = new JLabel("診療科:", SwingConstants.RIGHT);
            constrain(content, label, x + 2, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, deptCombo, x + 3, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel("電子メール:", SwingConstants.RIGHT);
            constrain(content, label, x, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.EAST);
            constrain(content, emailField, x + 1, y, 1, 1, GridBagConstraints.NONE, GridBagConstraints.WEST);

            x = 0;
            y += 1;
            label = new JLabel(" ", SwingConstants.RIGHT);
            constrain(content, label, x, y, 4, 1, GridBagConstraints.BOTH, GridBagConstraints.EAST);

            x = 0;
            y += 1;
            label = new JLabel("ユーザID - 半角英数記で" + userIdLength[0] + "文字以上" + userIdLength[1] + "文字以内");
            constrain(content, label, x, y, 4, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
            x = 0;
            y += 1;
            label = new JLabel("パスワード - 半角英数記で" + passwordLength[0] + "文字以上" + passwordLength[1] + "文字以内");
            constrain(content, label, x, y, 4, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);

            JPanel btnPanel = null;
            if (GlobalConstants.isMac()) {
                btnPanel = new JPanel();
                btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                btnPanel.add(Box.createHorizontalGlue());
                btnPanel.add(cancelButton);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(okButton);
                //        btnPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{cancelButton, okButton});
            } else {
                btnPanel = new JPanel();
                btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
                btnPanel.add(Box.createHorizontalGlue());
                btnPanel.add(okButton);
                btnPanel.add(Box.createHorizontalStrut(5));
                btnPanel.add(cancelButton);
                //       btnPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{okButton, cancelButton});
            }

            this.setLayout(new BorderLayout(0, 17));
            this.add(content, BorderLayout.CENTER);
            this.add(btnPanel, BorderLayout.SOUTH);

            this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));
        }

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
                        JOptionPane.showMessageDialog(dialog, ADD_USER_SUCCESS_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(dialog, ADD_USER_ERROR_MSG, GlobalConstants.getFrameTitle(getName()), JOptionPane.WARNING_MESSAGE);
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
            TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, dialog, message, addMsg, delay, maxEstimation);
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

  /*
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
    }
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
}
