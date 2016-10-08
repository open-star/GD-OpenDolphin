package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.infomodel.IInfoModel;

/**
 * SaveDialog 「ドキュメント保存」ダイアログ
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SaveDialog {

    private static final String[] PRINT_COUNT = {
        "0", "1", "2", "3", "4", "5"
    };
    private static final String[] TITLE_LIST = {"経過記録", "処方", "処置", "検査", "画像", "指導"};
    private static final String TITLE = "ドキュメント保存";
    private static final String SAVE = "保存";
    private static final String TMP_SAVE = "仮保存";
    private static final String TEMPLATE_SAVE = "テンプレート保存";
    private JCheckBox patientCheck;
    private JCheckBox clinicCheck;
    // 保存ボタン
    private JButton okButton;
    // キャンセルボタン
    private JButton cancelButton;
    // 仮保存ボタン
    private JButton tmpButton;
    // テンプレート保存ボタン
    private JButton templateButton;
    private JTextField titleField;
    private JComboBox titleCombo;
    private JComboBox printCombo;
    private JLabel departmentLabel;
    private JCheckBox sendClaim;  // CLAIM 送信
    private SaveParams value;   // 戻り値のSaveParams/
    private JDialog dialog;    // ダイアログ
    private boolean ok;

    /**
     * Creates new OpenKarteDialog　「ドキュメント保存」ダイアログを生成します。
     * @param parent 親ウィンドウ
     * @param params 「ドキュメント保存」のパラメーターのインスタンス
     */
    public SaveDialog(Window parent, SaveParams params) {
        JPanel contentPanel = createComponent();
        // Object[] options = new Object[]{okButton, tmpButton, templateButton, cancelButton};
        Object[] options = new Object[]{okButton, tmpButton, cancelButton};//[保存]、[仮保存]、[取消]
        JOptionPane jop = new JOptionPane(contentPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, okButton);
        dialog = jop.createDialog(parent, GlobalConstants.getFrameTitle(TITLE));

        initializeWith(params);
    }

    /**
     * 「ドキュメント保存」ダイアログを表示します。
     * @return
     * OKのとき：「ドキュメント保存」のパラメーターのインスタンス<br>
     * OKでないとき：null
     */
    public SaveParams open() {
        ok = false;
        dialog.setVisible(true);
        if (ok == true) {
            return getParams();
        } else {
            return null;
        }
    }

    /**
     * [保存]、[仮保存]、[取消]ボタンを生成する
     */
    private void createButtons() {
        createOkButton();
        createCancelButton();
        createTmpButton();
        // createTemplateButton();
    }

    /**
     *  MEMO: unused?
     */
    private void createTemplateButton() {
        // テンプレート保存ボタン
        templateButton = new JButton(TEMPLATE_SAVE);
        templateButton.setToolTipText("テンプレートとして保存します");
        templateButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doTemplate"));
        templateButton.setEnabled(false);
    }

    /**
     * [仮保存]ボタン
     */
    private void createTmpButton() {
        // 仮保存ボタン
        tmpButton = new JButton(TMP_SAVE);
        tmpButton.setToolTipText("診療行為は送信しません");
        tmpButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doTemp"));
        tmpButton.setEnabled(false);
    }

    /**
     * [取消]ボタン
     */
    private void createCancelButton() {
        // キャンセルボタン
        String buttonText = (String) UIManager.get("OptionPane.cancelButtonText");
        cancelButton = new JButton(buttonText);
        cancelButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doCancel"));
    }

    /**
     * [保存]ボタン
     */
    private void createOkButton() {
        // OKボタン
        okButton = new JButton(SAVE);
        okButton.setToolTipText("診療行為の送信はチェックボックスに従います。");
        okButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "doOk"));
        okButton.setEnabled(false);
    }

    /**
     * 診療科、印刷部数を表示するラベルとパネルを生成する
     * @return 診療科、印刷部数を表示するパネル
     */
    private JPanel createPrintPanel() {
        // 診療科、印刷部数を表示するラベルとパネルを生成する
        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        departmentLabel = new JLabel();
        printPanel.add(new JLabel("診療科:"));
        printPanel.add(departmentLabel);
        printPanel.add(Box.createRigidArea(new Dimension(11, 0)));
        // Print
        printCombo = new JComboBox(PRINT_COUNT);
        printCombo.setSelectedIndex(1);
        printPanel.add(new JLabel("印刷部数:"));
        printPanel.add(printCombo);

        return printPanel;
    }

    /**
     *　診療行為を送信する (仮保存の場合は送信しない)パネル
     * @return [診療行為を送信する (仮保存の場合は送信しない)]チェックボックスを表示するパネル
     */
    private JPanel createSendClaimPanel() {
        // CLAIM 送信ありなし
        JPanel sendClaimPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sendClaim = new JCheckBox("診療行為を送信する (仮保存の場合は送信しない)");
        sendClaimPanel.add(sendClaim);

        return sendClaimPanel;
    }

    /**
     * 文書タイトルを選択するコンボボックスを表示するパネルを返す。
     * @return [文書タイトル]コンボボックスを表示するパネル
     */
    private JPanel createTitlePanel() {
        // 文書タイトルを選択するコンボボックス
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleCombo = new JComboBox(TITLE_LIST);
        titleCombo.setPreferredSize(new Dimension(220, titleCombo.getPreferredSize().height));
        titleCombo.setMaximumSize(titleCombo.getPreferredSize());
        titleCombo.setEditable(true);
        titlePanel.add(new JLabel("タイトル:"));
        titlePanel.add(titleCombo);

        // ComboBox のエディタコンポーネントへリスナを設定する
        titleField = (JTextField) titleCombo.getEditor().getEditorComponent();

        titleField.addFocusListener(AutoKanjiListener.getInstance());

        DocumentListener dl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkTitle();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkTitle();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkTitle();
            }
        };

        titleField.getDocument().addDocumentListener(dl);

        return titlePanel;
    }

    /**
     * 「ドキュメント保存」のパラメーターのインスタンスを返します。
     * @return 「ドキュメント保存」のパラメーターのインスタンス
     */
    private SaveParams getParams() {
        return value;
    }

    /**
     * コンポーネントにSaveParamsの値を設定する。
     * @param params ドキュメント保存のパラメータークラス
     */
    private void initializeWith(SaveParams params) {

        value = params;

        showTitle();
        showDepartment();
        showPrintCount();
        showSendClaim();    //[送信]チェックボックス
        showAccessRight();

        checkTitle();
    }

    /**
     *　「ドキュメント保存」のパラメーターを参照して、アクセス権を設定する
     */
    private void showAccessRight() {
        // アクセス権を設定する
        if (value.getSendMML()) {
            // 患者への参照と診療歴のある施設の参照許可を設定する
            boolean permit = value.isAllowPatientRef();
            patientCheck.setSelected(permit);
            permit = value.isAllowClinicRef();
            clinicCheck.setSelected(permit);
        } else {
            // MML 送信をしないときdiasbleにする
            patientCheck.setEnabled(false);
            clinicCheck.setEnabled(false);
        }
    }

    /**
     * 「ドキュメント保存」のパラメーターを参照して
     * [送信]チェックボックスを有効、または無効にします。
     */
    private void showSendClaim() {
        // CLAIM 送信をチェックする
        if (value.isDisableSendClaim()) {
            sendClaim.setEnabled(false); // シングルカルテで CLAIM 送信自体を行わない場合
        } else {
            sendClaim.setSelected(value.isSendClaim());
        }
    }

    /**
     *　「ドキュメント保存」のパラメーターを参照して、[印刷部数]コンボボックス
     */
    private void showPrintCount() {
        // 印刷部数選択
        int count = value.getPrintCount();
        if (count != -1) {
            printCombo.setSelectedItem(String.valueOf(count));
        } else {
            printCombo.setEnabled(false);
        }
    }

    /**
     * 「ドキュメント保存」のパラメーターを参照して
     * 受付情報からの診療科を表示する
     */
    private void showDepartment() {
        // 診療科を表示する
        // 受付情報からの診療科を設定する
        String department = value.getDepartment();
        if (department != null) {
            String[] depts = department.split("\\s*,\\s*");
            if (depts[0] != null) {
                departmentLabel.setText(depts[0]);
            } else {
                departmentLabel.setText(department);
            }
        }
    }

    /**
     * 「ドキュメント保存」のパラメーターを参照して、タイトルを表示する。
     */
    private void showTitle() {
        // Titleを表示する
        String title = value.getTitle();
        if (title != null && (!title.equals("") && (!title.equals("経過記録")))) {
            titleCombo.insertItemAt(title, 0);
        }
        titleCombo.setSelectedIndex(0);
    }

    /**
     * GUIコンポーネントを初期化する。
     * @return パネル
     */
    private JPanel createComponent() {

        JPanel content = new JPanel();
        content.setLayout(new GridLayout(0, 1));//このコンテナのレイアウトマネージャーを設定します。

        content.add(createTitlePanel()); // [文書タイトル]コンボボックス
        content.add(createPrintPanel()); // 診療科、印刷部数


        // MEMO: 実際には使用されていない
        // AccessRightを設定するボタンとパネルを生成する
        patientCheck = new JCheckBox("患者に参照を許可する");
        clinicCheck = new JCheckBox("診療歴のある病院に参照を許可する");

        content.add(createSendClaimPanel()); // 診療行為を送信する (仮保存の場合は送信しない)

        createButtons(); //[保存]、[仮保存]、[取消]ボタンを生成する

        return content;
    }

    /**
     * タイトルフィールドの有効性をチェックする。
     */
    public void checkTitle() {
        boolean enabled = titleField.getText().trim().isEmpty() ? false : true;
        okButton.setEnabled(enabled);
        tmpButton.setEnabled(enabled);
        //templateButton.setEnabled(enabled);
    }

    /**
     *
     * @param defaultTiele
     * @param send
     */
    private void setParams(String defaultTiele, boolean send) {

        // 文書タイトルを取得する
        String val = (String) titleCombo.getSelectedItem();
        if (!val.isEmpty()) {
            value.setTitle(val);
        } else {
            value.setTitle(defaultTiele);
        }

        // Department
        value.setDepartment(departmentLabel.getText());

        // 印刷部数を取得する
        // テンプレート保存時でも印刷するかもしれない
        int count = Integer.parseInt((String) printCombo.getSelectedItem());
        value.setPrintCount(count);

        // CLAIM 送信
        value.setSendClaim(send);
        // 患者への参照許可を取得する
        value.setAllowPatientRef(false);
        // 診療歴のある施設への参照許可を設定する
        value.setAllowClinicRef(false);

    }

    // TODO: doOk, doTmp, doTemplate の同じ処理をまとめる
    /**
     * GUIコンポーネントから値を取得し、saveparamsに設定する。
     */
    public void doOk() {

        if (sendClaim.isSelected()) {
            value.setStatus(IInfoModel.STATUS_FINAL);
            setParams("経過記録", true);
        } else {
            value.setStatus(IInfoModel.STATUS_MODIFIED);
            setParams("経過記録", false);
        }

        ok = true;
        close();
    }

    /**
     * 仮保存の場合のパラメータを設定する。
     */
    public void doTemp() {

        value.setStatus(IInfoModel.STATUS_TMP);

        setParams("仮保存", false);
        ok = true;
        close();
    }

    /**
     * テンプレート保存の時のパラメータを設定する
     */
    public void doTemplate() {

        value.setStatus(IInfoModel.STATUS_TEMPLATE);

        setParams("新規テンプレート", false);
        ok = true;
        close();
    }

    /**
     * キャンセルしたことを設定する。
     */
    public void doCancel() {
        value = null;
        ok = false;
        close();
    }

    /**
     *
     */
    private void close() {
        dialog.setVisible(false);
        dialog.dispose();
    }
}
