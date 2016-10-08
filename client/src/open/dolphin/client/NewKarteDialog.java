package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.EventHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;

import open.dolphin.client.karte.template.Template;
import open.dolphin.client.karte.template.TemplateManager;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.project.GlobalVariables;
import open.dolphin.project.GlobalConstants;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.log.LogWriter;

/**
 * 新規カルテ作成のダイアログ。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class NewKarteDialog {

    private final String OPEN_ANOTHER = "別ウィンドウで編集";
    private final String ADD_TO_TAB = "タブパネルへ追加";
    private final String EMPTY_NEW = "空白の新規カルテ";
    private final String APPLY_RP = "前回処方を適用";
    private final String ALL_COPY = "全てコピー";
    private final String FROM_TEMPLATE = "使用";
    private final String DEPARTMENT = "診療科:";
    private final String SELECT_INS = "保険選択";
    private final String LAST_CREATE_MODE = "newKarteDialog.lastCreateMode";
    private final String FRAME_MEMORY = "newKarteDialog.openFrame";
    private NewKarteParams params;
    // GUI components
    private JButton okButton;
    private JButton cancelButton;
    private JRadioButton emptyNew;
    private JRadioButton applyRp;       // 前回処方を適用
    private JRadioButton allCopy;	// 全てコピー
    private JCheckBox fromTemplate;
    private JComboBox templateList;
    private JList insuranceList;
    private JLabel departmentLabel;
    private JLabel confirmDateLabel;    // 診療日
    private JButton changeConfirmDateBtn; // 診療日変更ボタン
    private JRadioButton addToTab;	// タブパネルへ追加
    //private JRadioButton addToTab2;	// タブパネルへ追加
    private JRadioButton openAnother;	// 別 Window へ表示
    private JCheckBox hospitalCheckBox;//入院か否か
    private Preferences prefs;
    private Frame parentFrame;
    private String title;
    private JPanel content;
    private JDialog dialog;
    private NewKarteParams value;

    /** 
     * Creates new OpenKarteDialog 
     * @param parentFrame 
     * @param title
     */
    public NewKarteDialog(Frame parentFrame, String title) {
        prefs = Preferences.userNodeForPackage(this.getClass());
        this.parentFrame = parentFrame;
        this.title = title;
        content = createComponent();
    }

    /**
     *
     * @param newParams
     */
    public void setValue(NewKarteParams newParams) {

        this.params = newParams;
        setDepartment(params.getDepartment());
        setInsurance(params.getInsurances());

        hospitalCheckBox.setSelected(params.isHospital());

        int lastCreateMode = prefs.getInt(LAST_CREATE_MODE, 0);
        boolean frameMemory = prefs.getBoolean(FRAME_MEMORY, true);

        ButtonGroup bg;

        switch (params.getOption()) {

            case BROWSER_NEW:
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setSelected(true);
                openAnother.setSelected(frameMemory);
                addToTab.setSelected(!frameMemory);
                bg = new ButtonGroup();
                bg.add(openAnother);
                bg.add(addToTab);
                break;

            case BROWSER_COPY_NEW:
                selectCreateMode(lastCreateMode);
                bg = new ButtonGroup();
                bg.add(emptyNew);
                bg.add(applyRp);
                bg.add(allCopy);

                openAnother.setSelected(frameMemory);
                addToTab.setSelected(!frameMemory);
                bg = new ButtonGroup();
                bg.add(openAnother);
                bg.add(addToTab);
                break;

            case BROWSER_MODIFY:
                insuranceList.setEnabled(false);
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setEnabled(false);
                openAnother.setSelected(frameMemory);
                addToTab.setSelected(!frameMemory);
                bg = new ButtonGroup();
                bg.add(openAnother);
                bg.add(addToTab);
                // OK Button
                okButton.setEnabled(true);
                break;

            case EDITOR_NEW:
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setSelected(true);
                openAnother.setSelected(true);
                openAnother.setEnabled(false);
                addToTab.setEnabled(false);
                break;

            case EDITOR_COPY_NEW:
                selectCreateMode(lastCreateMode);
                bg = new ButtonGroup();
                bg.add(applyRp);
                bg.add(allCopy);
                bg.add(emptyNew);
                openAnother.setSelected(true);
                openAnother.setEnabled(false);
                addToTab.setEnabled(false);
                break;

            case EDITOR_MODIFY:
                insuranceList.setEnabled(false);
                applyRp.setEnabled(false);
                allCopy.setEnabled(false);
                emptyNew.setEnabled(false);
                openAnother.setSelected(true);
                openAnother.setEnabled(false);
                addToTab.setEnabled(false);
                break;
            default: LogWriter.fatal(getClass(), "case default");
        }
    }
    /**
     *
     */
    public void start() {

        Object[] options = new Object[]{okButton, cancelButton};
        JOptionPane jop = new JOptionPane(content, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, okButton);
        dialog = jop.createDialog(parentFrame, title);
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                insuranceList.requestFocusInWindow();
            }
        });
        dialog.setVisible(true);
    }
    /**
     *
     * @return
     */
    public NewKarteParams getValue() {
        return value;
    }
    /**
     *
     */
    private void createButtons() {

        okButton = new JButton((String) UIManager.get("OptionPane.okButtonText"));
        okButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doOk"));
        okButton.setEnabled(false);

        cancelButton = new JButton((String) UIManager.get("OptionPane.cancelButtonText"));
        cancelButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doCancel"));
    }
    /**
     *
     * @return
     */
    private JPanel createDatePanel() {

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));

        datePanel.add(new JLabel("診療日："));
        final java.util.Date date = new java.util.Date();
        final java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        confirmDateLabel = new JLabel(format.format(date));
        datePanel.add(confirmDateLabel);
        JButton b = new JButton("今の時刻");
        b.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                confirmDateLabel.setText(format.format(date));
            }
        });
        datePanel.add(b);
        changeConfirmDateBtn = new JButton("変更");
        changeConfirmDateBtn.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                popupCalendar(e);
            }
        });
        datePanel.add(changeConfirmDateBtn);

        JPanel titledPanel = new JPanel();
        datePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));
        titledPanel.setLayout(new BorderLayout());
        titledPanel.add(datePanel, BorderLayout.CENTER);
        titledPanel.setBorder(BorderFactory.createTitledBorder("診療日編集"));

        return titledPanel;
    }
    /**
     *
     * @return
     */
    private JPanel createDepartmentLabel() {
        // 診療科情報ラベル
        departmentLabel = new JLabel();
        JPanel dp = new JPanel(new FlowLayout(FlowLayout.CENTER, 11, 0));
        dp.add(new JLabel(DEPARTMENT));
        dp.add(departmentLabel);
        return dp;
    }
    /**
     *
     * @return
     */
    private JPanel createHospitalPanel() {
        JPanel divisionPanel = new JPanel();
        divisionPanel.setLayout(new BoxLayout(divisionPanel, BoxLayout.X_AXIS));
        hospitalCheckBox = new JCheckBox("入院");
        divisionPanel.add(hospitalCheckBox);

        JPanel titledPanel = new JPanel();
        divisionPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));
        titledPanel.setLayout(new BorderLayout());
        titledPanel.add(divisionPanel, BorderLayout.CENTER);
        titledPanel.setBorder(BorderFactory.createTitledBorder("診療区分"));

        return titledPanel;
    }
    /**
     *
     * @return
     */
    private JPanel createInsuranceList() {
        // 保険選択リスト
        insuranceList = new JList();
        insuranceList.setFixedCellWidth(200);
        insuranceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        insuranceList.addListSelectionListener((ListSelectionListener) EventHandler.create(ListSelectionListener.class, this, "insuranceSelectionChanged", "valueIsAdjusting"));
        JPanel ip = new JPanel(new BorderLayout(9, 0));
        ip.setBorder(BorderFactory.createTitledBorder(SELECT_INS));
        ip.add(insuranceList, BorderLayout.CENTER);
        ip.add(new JLabel(GlobalConstants.getImageIcon("addbk_32.gif")), BorderLayout.WEST);
        return ip;
    }
    /**
     *
     * @return
     */
    private JPanel createModeSelectPanel() {

        // 前回処方適用 / 全コピー / 空白
        emptyNew = new JRadioButton(EMPTY_NEW);
        applyRp = new JRadioButton(APPLY_RP);
        allCopy = new JRadioButton(ALL_COPY);

        ActionListener memory = (ActionListener) EventHandler.create(ActionListener.class, this, "memoryMode");

        emptyNew.addActionListener(memory);
        applyRp.addActionListener(memory);
        allCopy.addActionListener(memory);

        JPanel modePanel = new JPanel();
        modePanel.setLayout(new BoxLayout(modePanel, BoxLayout.X_AXIS));
        modePanel.add(applyRp);
        modePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        modePanel.add(allCopy);
        modePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        modePanel.add(emptyNew);
        modePanel.add(Box.createHorizontalGlue());

        JPanel titledPanel = new JPanel();
        modePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));
        titledPanel.setLayout(new BoxLayout(titledPanel, BoxLayout.Y_AXIS));
        titledPanel.add(modePanel);
        titledPanel.setBorder(BorderFactory.createTitledBorder("カルテ作成方法"));

        return titledPanel;
    }
    /**
     *
     * @return
     */
    private List<Template> getTemplateList() {

        File localStorage = GlobalConstants.getApplicationContext().getLocalStorage().getDirectory();
        File directory = new File(localStorage, "templates");
        TemplateManager manager = new TemplateManager(directory);
        return manager.getList();
    }
    /**
     *  MEMO: unused?
     * @return
     */
    private JPanel createTemplatePanel() {

        fromTemplate = new JCheckBox(FROM_TEMPLATE);

        templateList = new JComboBox();

        for (Template template : getTemplateList()) {
            templateList.addItem(template);
        }

        templateList.setEnabled(false);

        fromTemplate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (((JCheckBox)e.getSource()).isSelected()) {
                    templateList.setEnabled(true);

                    emptyNew.setEnabled(false);
                    applyRp.setEnabled(false);
                    allCopy.setEnabled(false);
                }
                else {
                    templateList.setEnabled(false);

                    emptyNew.setEnabled(true);
                    applyRp.setEnabled(true);
                    allCopy.setEnabled(true);
                }
            }
        });

        JPanel templatePanel = new JPanel();
        templatePanel.setLayout(new BoxLayout(templatePanel, BoxLayout.X_AXIS));
        templatePanel.add(fromTemplate);
        templatePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        templatePanel.add(templateList);

        JPanel titledPanel = new JPanel();
        titledPanel.setLayout(new BoxLayout(titledPanel, BoxLayout.Y_AXIS));
        titledPanel.add(templatePanel);
        titledPanel.setBorder(BorderFactory.createTitledBorder("テンプレート"));

        return titledPanel;
    }
    /**
     *
     * @return
     */
    private JPanel createPlaceSelectPanel() {
        // タブパネルへ追加/別ウィンドウ
        openAnother = new JRadioButton(OPEN_ANOTHER);
        addToTab = new JRadioButton(ADD_TO_TAB);
        openAnother.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "memoryFrame"));
        addToTab.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "memoryFrame"));
        JPanel openPanel = new JPanel();
        openPanel.setLayout(new BoxLayout(openPanel, BoxLayout.X_AXIS));
        openPanel.add(openAnother);
        openPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        openPanel.add(addToTab);
        openPanel.add(Box.createHorizontalGlue());

        JPanel titledPanel = new JPanel();
        openPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));
        titledPanel.setLayout(new BorderLayout());
        titledPanel.add(openPanel, BorderLayout.CENTER);
        titledPanel.setBorder(BorderFactory.createTitledBorder("カルテ編集ウインドウ"));

        return titledPanel;
    }
    /**
     *
     * @param dept
     */
    private void setDepartment(String dept) {
        if (dept != null) {
            String[] depts = dept.split("\\s*,\\s*");
            departmentLabel.setText(depts[0]);
        }
    }
    /**
     *
     * @param o
     */
    private void setInsurance(Object[] o) {
        insuranceList.setListData(o);

        // 保険が一つしかない場合はそれを選択する
        if (o != null && o.length > 0) {
            int index = params.getInitialSelectedInsurance();
            if (index >= 0 && index < o.length) {
                insuranceList.getSelectionModel().setSelectionInterval(index, index);
            }
        }
    }
    /**
     *
     * @return
     */
    private IChart.NewKarteMode getCreateMode() {
        if (fromTemplate.isSelected()) {
            return IChart.NewKarteMode.FROM_TEMPLATE;
        }
        if (emptyNew.isSelected()) {
            return IChart.NewKarteMode.EMPTY_NEW;
        } else if (applyRp.isSelected()) {
            return IChart.NewKarteMode.APPLY_RP;
        } else if (allCopy.isSelected()) {
            return IChart.NewKarteMode.ALL_COPY;
        }
        return IChart.NewKarteMode.EMPTY_NEW;
    }
    /**
     *
     * @param mode
     */
    private void selectCreateMode(int mode) {
        emptyNew.setSelected(false);
        applyRp.setSelected(false);
        allCopy.setSelected(false);
        if (mode == 0) {
            emptyNew.setSelected(true);
        } else if (mode == 1) {
            applyRp.setSelected(true);
        } else if (mode == 2) {
            allCopy.setSelected(true);
        }
    }
    /**
     *
     * @return
     */
    protected JPanel createComponent() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createDepartmentLabel());
        panel.add(Box.createVerticalStrut(11));

        panel.add(createInsuranceList());
        panel.add(Box.createVerticalStrut(11));

        panel.add(createModeSelectPanel());
        panel.add(Box.createVerticalStrut(11));

	// MEMO: karte template is for 1.9.x later
        // panel.add(createTemplatePanel());
        // panel.add(Box.createVerticalStrut(11));
        fromTemplate = new JCheckBox(FROM_TEMPLATE);
	fromTemplate.setSelected(false);

        panel.add(createPlaceSelectPanel());
        panel.add(Box.createVerticalStrut(11));

        panel.add(createHospitalPanel());
        panel.add(Box.createVerticalStrut(11));

        panel.add(createDatePanel());

        createButtons();

        return panel;
    }

    /**
     * 保険選択リストにフォーカスする。
     * @param e 
     */
    public void controlFocus(WindowEvent e) {
        insuranceList.requestFocusInWindow();
    }

    /**
     * 保険選択の変更を処理する。
     * @param adjusting
     */
    public void insuranceSelectionChanged(boolean adjusting) {
        if (adjusting == false) {
            Object o = insuranceList.getSelectedValue();
            boolean ok = o != null ? true : false;
            okButton.setEnabled(ok);
        }
    }

    /**
     * カルテの作成方法をプレファレンスに記録する。
     */
    public void memoryMode() {

        if (emptyNew.isSelected()) {
            prefs.putInt(LAST_CREATE_MODE, 0);
        } else if (applyRp.isSelected()) {
            prefs.putInt(LAST_CREATE_MODE, 1);
        } else if (allCopy.isSelected()) {
            prefs.putInt(LAST_CREATE_MODE, 2);
        }
    }

    /**
     * カルテフレーム(ウインドウ)の作成方法をプレファレンスに記録する。
     */
    public void memoryFrame() {
        boolean openFrame = openAnother.isSelected();
        prefs.putBoolean(FRAME_MEMORY, openFrame);
        Preferences gpref = GlobalVariables.getPreferences();
        gpref.putBoolean(GlobalVariables.KARTE_PLACE_MODE, openFrame);
    }

    /**
     * パラーメータを取得しダイアログの値に設定する。
     */
    public void doOk() {

        params.setDepartment(departmentLabel.getText());
        params.setPVTHealthInsurance((PVTHealthInsuranceModel)insuranceList.getSelectedValue());
        params.setCreateMode(getCreateMode());
        params.setOpenFrame(openAnother.isSelected());
        params.setHospital(hospitalCheckBox.isSelected());

        if (fromTemplate.isSelected()) {
            params.setSelectedTemplate((Template)templateList.getSelectedItem());
        }

        // TODO フォーマット変換を別処理に。
        // ModelUtils.getDateTimeAsString の ISO_8601_DATE_FORMAT にあわせてからセット。
        params.setConfirmDate(confirmDateLabel.getText().replaceFirst(" ", "T"));

        value = params;
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * キャンセルする。ダイアログを閉じる。
         */
    public void doCancel() {
        value = null;
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * カレンダーを表示
         * @param e
         */
    public void popupCalendar(MouseEvent e) {
        final JPopupMenu popup = new JPopupMenu();
        CalendarCardPanel cc = new CalendarCardPanel(GlobalConstants.getEventColorTable());
        cc.addPropertyChangeListener(CalendarCardPanel.PICKED_DATE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(CalendarCardPanel.PICKED_DATE)) {
                    SimpleDate sd = (SimpleDate) e.getNewValue();
                    confirmDateLabel.setText(SimpleDate.simpleDateToMmldate(sd) + " 00:00:00");
                    popup.setVisible(false);
                }
            }
        });
        cc.setCalendarRange(new int[]{-12, 0});
        popup.insert(cc, 0);
        popup.show(e.getComponent(), e.getX(), e.getY());
    }
}
