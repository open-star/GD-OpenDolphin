package open.dolphin.client.editor.stamp;

import open.dolphin.project.GlobalConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.text.Position;
import open.dolphin.client.AbstractComponentHolder;
import open.dolphin.client.ChartMediator;
import open.dolphin.client.ChartWindow;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IChart;
import open.dolphin.client.IComponentHolder;
import open.dolphin.client.KartePane;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.BundleMed;

import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;

import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalVariables;
import open.dolphin.utils.DebugDump;
import open.dolphin.utils.StringTool;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;

/**
 * KartePane に Component　として挿入されるスタンプを保持スルクラス。　MEMO:リスナー
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class StampHolder extends AbstractComponentHolder implements IComponentHolder {

    private static final Color FOREGROUND = new Color(20, 20, 140);
    private static final Color BACKGROUND = Color.white;
    private static final Color SELECTED_BORDER = new Color(255, 0, 153);
    private ModuleModel stamp;
    private StampRenderingHints hints;
    private KartePane kartePane;
    private Position start;
    private Position end;
    private boolean selected;
    private Color foreGround = FOREGROUND;
    private Color background = BACKGROUND;
    private Color selectedBorder = SELECTED_BORDER;

    /**
     *
     * @param kartePane
     * @param stamp
     */
    public StampHolder(KartePane kartePane, ModuleModel stamp) {
        super();

        this.kartePane = kartePane;

        setHints(new StampRenderingHints());
        setForeground(foreGround);
        setBackground(background);
        setBorder(BorderFactory.createLineBorder(kartePane.getTextPane().getBackground()));
        setStamp(stamp);
    }

    /**
     * Focusされた場合のメニュー制御とボーダーを表示する。
     * @param map 
     */
    @Override
    public void enter(ActionMap map) {

        map.get(GUIConst.ACTION_COPY).setEnabled(false);
        map.get(GUIConst.ACTION_CUT).setEnabled(isEditable());
        map.get(GUIConst.ACTION_PASTE).setEnabled(false);
        map.get(GUIConst.ACTION_LETTER_PASTE_FROM_KARTE).setEnabled(canPasteToLetter());
        map.get(GUIConst.ACTION_QUICK_EDIT).setEnabled(isFormulaStamp());

        setSelected(true);
    }

    /**
     *
     * @param targets
     * @param textFields
     * @param model
     * @return
     */
    private JPanel createQuickEditPane(ClaimItem[] targets, List<JTextField> textFields, ClaimBundle model) {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        panel.setLayout(layout);
        ParallelGroup nameLabelGroup = layout.createParallelGroup();
        ParallelGroup textFieldGroup = layout.createParallelGroup();
        ParallelGroup unitLabelGroup = layout.createParallelGroup();
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        for (ClaimItem target : targets) {
            if (StringTool.isEmptyString(target.getNumber())) {
                continue;
            }
            ParallelGroup itemGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            JLabel nameLabel = new JLabel(target.getName());
            nameLabelGroup.addComponent(nameLabel);
            itemGroup.addComponent(nameLabel);
            JTextField itemField = new JTextField();
            itemField.setColumns(5);
            itemField.setText(target.getNumber());
            itemField.setHorizontalAlignment(JTextField.RIGHT);
            addListenerToCountField(itemField);
            textFields.add(itemField);
            textFieldGroup.addComponent(itemField);
            itemGroup.addComponent(itemField);
            JLabel unitLabel = new JLabel(target.getUnit());
            unitLabelGroup.addComponent(unitLabel);
            itemGroup.addComponent(unitLabel);
            vGroup.addGroup(itemGroup);
        }
        vGroup.addGroup(layout.createParallelGroup().addGap(15).addGap(15).addGap(15));
        ParallelGroup numberGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        // FIXME: We need more smart code.
        JLabel nameLabel = null;
        try {
            nameLabel = new JLabel(((BundleMed) model).getDisplayString());
        } catch (ClassCastException ex) {
            nameLabel = new JLabel("回数");
        }
        nameLabelGroup.addComponent(nameLabel);
        numberGroup.addComponent(nameLabel);
        JTextField countField = new JTextField();
        countField.setColumns(5);
        countField.setText(model.getBundleNumber());
        countField.setHorizontalAlignment(JTextField.RIGHT);
        addListenerToCountField(countField);
        textFields.add(countField);
        textFieldGroup.addComponent(countField);
        numberGroup.addComponent(countField);
        // FIXME: we need more smart code.
        JLabel unitLabel = null;
        try {
            unitLabel = new JLabel(((BundleMed) model).getUnit());
        } catch (ClassCastException ex) {
            unitLabel = new JLabel("回");
        }
        unitLabelGroup.addComponent(unitLabel);
        numberGroup.addComponent(unitLabel);
        vGroup.addGroup(numberGroup);
        layout.setVerticalGroup(vGroup);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(nameLabelGroup);
        hGroup.addGroup(textFieldGroup);
        hGroup.addGroup(unitLabelGroup);
        layout.setHorizontalGroup(hGroup);
        return panel;
    }

    /**
     *
     * @return
     */
    private boolean isEditable() {
        return kartePane.getTextPane().isEditable();
    }

    /**
     *
     * @return
     */
    private boolean canPasteToLetter() {
        IChart chart = kartePane.getMediator().getChart();
        return chart instanceof ChartWindow && ((ChartWindow) chart).existLetterPane() && !isEditable();
    }

    /**
     *
     * @return
     */
    private boolean isFormulaStamp() {
        String entityName = getStamp().getModuleInfo().getEntity();
        if (entityName.equals("medOrder")) {
            return true;
        } // 処方
        if (entityName.equals("treatmentOrder")) {
            return true;
        } // 処置
        if (entityName.equals("injectionOrder")) {
            return true;
        } // 注射
        if (entityName.equals("radiologyOrder")) {
            return true;
        } // 画像診断
        if (entityName.equals("physiologyOrder")) {
            return true;
        } // 生体検査
        if (entityName.equals("surgeryOrder")) {
            return true;
        } // 手術

        BundleDolphin stampModel = (BundleDolphin) getStamp().getModel();
        if (stampModel.getClassCode().equals("960")) {
            return true;
        }

        return false;
    }

    /**
     * Focusがはずれた場合のメニュー制御とボーダーの非表示を行う。
     * @param map
     */
    @Override
    public void exit(ActionMap map) {
        setSelected(false);
    }

    /**
     * Popupメニューを表示する。
     */
    @Override
    public void showPopup(MouseEvent e) {
        if (isSelected()) {
            if (e.isPopupTrigger()) {
                JPopupMenu popup = new JPopupMenu();
                ChartMediator mediator = kartePane.getMediator();

                if (isEditable() && isFormulaStamp()) {
                    popup.add(mediator.getAction(GUIConst.ACTION_QUICK_EDIT));
                }
                if (isEditable()) {
                    popup.add(mediator.getAction(GUIConst.ACTION_COPY));
                    popup.add(mediator.getAction(GUIConst.ACTION_CUT));
                    popup.add(mediator.getAction(GUIConst.ACTION_PASTE));
                }
                if (!isEditable()) {
                    popup.add(mediator.getAction(GUIConst.ACTION_LETTER_PASTE_FROM_KARTE));
                }
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }

    /**
     * このスタンプホルダのKartePaneを返す。
     * @return
     */
    @Override
    public KartePane getKartePane() {
        return kartePane;
    }

    /**
     * スタンプホルダのコンテントタイプを返す。
     */
    @Override
    public int getContentType() {
        return IComponentHolder.TT_STAMP;
    }

    /**
     * このホルダのモデルを返す。
     * @return
     */
    public ModuleModel getStamp() {
        return stamp;
    }

    /**
     * このホルダのモデルを設定する。
     * @param stamp
     */
    public void setStamp(ModuleModel stamp) {
        this.stamp = stamp;
        renderStamp();
    }

    /**
     *
     * @return
     */
    public StampRenderingHints getHints() {
        return hints;
    }

    /**
     *
     * @param hints
     */
    public void setHints(StampRenderingHints hints) {
        this.hints = hints;
    }

    /**
     * 選択されているかどうかを返す。
     * @return 選択されている時 true
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * 選択属性を設定する。
     * @param selected 選択の時 true
     */
    @Override
    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            if (this.selected) {
                this.setBorder(BorderFactory.createLineBorder(selectedBorder));
            } else {
                this.setBorder(BorderFactory.createLineBorder(kartePane.getTextPane().getBackground()));
            }
        }
    }

    /**
     * KartePane でこのスタンプがダブルクリックされた時コールされる。
     * StampEditor を開いてこのスタンプを編集する。
     */
    @Override
    public void edit() {

        if (kartePane.getTextPane().isEditable()) {
            String category = stamp.getModuleInfo().getEntity();
            StampEditorDialog stampEditor = new StampEditorDialog(category, stamp);
            stampEditor.addPropertyChangeListener(StampEditorDialog.VALUE_PROP, this);
            stampEditor.start();
        } else {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
    }

    /**
     * エディタで編集した値を受け取り内容を表示する。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        ModuleModel newStamp = (ModuleModel) e.getNewValue();
        if (newStamp != null) {
            importStamp(newStamp);
        }
    }

    /**
     * スタンプの内容を置き換える。
     * @param newStamp
     */
    public void importStamp(ModuleModel newStamp) {
        setStamp(newStamp);
        kartePane.setDirty(true);
        kartePane.getTextPane().validate();
        kartePane.getTextPane().repaint();
    }

    /**
     * TextPane内での開始と終了ポジションを保存する。
     */
    @Override
    public void setEntry(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 開始ポジションを返す。
     */
    @Override
    public int getStartPos() {
        return start.getOffset();
    }

    /**
     * 終了ポジションを返す。
     */
    @Override
    public int getEndPos() {
        return end.getOffset();
    }

    private boolean noResultFromQuickEditDialog(JOptionPane pane) {
        return pane.getValue() == null || (Integer) pane.getValue() == JOptionPane.CANCEL_OPTION;
    }

    /**
     * Velocity を利用してスタンプの内容を表示する。
     */
    private void renderStamp() {
        try {
            IInfoModel model = getStamp().getModel();

            VelocityContext context = GlobalConstants.getVelocityContext();
            context.put("model", model);
            context.put("hints", getHints());
            context.put("stampName", getStamp().getModuleInfo().getStampName());
            context.put("bundleNumber", ((ClaimBundle) model).getBundleNumber());
            context.put("stampStatus", getStamp().getModuleInfo().getStampStatus());

            String templateFile = getStamp().getModel().getClass().getName() + ".vm";
            if (getStamp().getModuleInfo().getEntity() != null) {
                if (getStamp().getModuleInfo().getEntity().equals(IInfoModel.ENTITY_LABO_TEST)) {
                    if (GlobalVariables.getPreferences().getBoolean("laboFold", true)) {
                        templateFile = "labo.vm";
                    }
                }
            }

            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);


            InputStream instream = GlobalConstants.getTemplateAsStream(templateFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "SHIFT_JIS"));

            Velocity.evaluate(context, bw, "stmpHolder", reader);

            bw.flush();
            bw.close();
            reader.close();

            String stampText = sw.toString();
            if (GlobalSettings.isStampDump()) {
                DebugDump.dumpToFile("stamp.log", stampText);
            }
            this.setText(StringTool.zenkakuNumToHankaku(stampText));

            // MEMO: カルテペインのサイズを越えないようにする
            this.setMaximumSize(this.getPreferredSize());

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * 
     * @param field
     */
    private void addListenerToCountField(JTextField field) {
        field.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                JTextField countField = (JTextField) e.getSource();
                String oldString = countField.getText();

                if (oldString.contains(".")) {
                    countField.setText(getNewAsDecimal(oldString, -e.getWheelRotation()));
                } else {
                    countField.setText(getNewAsInteger(oldString, -e.getWheelRotation()));
                }
            }

            private String getNewAsDecimal(String oldString, int rotation) {
                double oldValue = 0.0;
                try {
                    oldValue = Double.parseDouble(oldString);
                } catch (NumberFormatException ex) {
                    LogWriter.error(getClass(), ex);
                }
                Double newValue = oldValue + rotation * 0.1;
                if (newValue < 0.1) {
                    newValue = 0.1;
                }
                DecimalFormat formatter = new DecimalFormat("0");
                formatter.setMaximumFractionDigits(1);
                formatter.setMinimumFractionDigits(1);
                String result = formatter.format(newValue).toString();
                return result;
            }

            private String getNewAsInteger(String oldString, int rotation) {
                int oldValue = 0;
                try {
                    oldValue = Integer.parseInt(oldString);
                } catch (NumberFormatException ex) {
                    LogWriter.error(getClass(), ex);
                }
                Integer newValue = oldValue + rotation;
                if (newValue < 1) {
                    newValue = 1;
                }
                String result = newValue.toString();
                return result;
            }
        });
    }

    /**
     *
     * @param parentFrame
     * @return
     */
    public final List<JTextField> showQuickEditDialog(JFrame parentFrame) {

        IInfoModel infoModel = getStamp().getModel();

        if (!(infoModel instanceof ClaimBundle)) {
            return null;
        }

        ClaimBundle model = (ClaimBundle) infoModel;

        ClaimItem[] targets = model.getClaimItem();
        List<JTextField> textFields = new ArrayList<JTextField>(targets.length);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout((new BoxLayout(mainPanel, BoxLayout.Y_AXIS)));
        mainPanel.add(createQuickEditPane(targets, textFields, model));

        JOptionPane pane = new JOptionPane(mainPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

        JDialog dialog = pane.createDialog(parentFrame, "クイック編集");
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        if (noResultFromQuickEditDialog(pane)) {
            return null;
        }

        return textFields;
    }
}
