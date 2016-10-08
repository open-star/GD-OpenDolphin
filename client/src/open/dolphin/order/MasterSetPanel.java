/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MasterSetPanel.java
 *
 * Created on 2009/08/18, 14:26:17
 */
package open.dolphin.order;

import open.dolphin.client.editor.treatment.TreatmentMasterPanel;
import open.dolphin.client.editor.toolmaterial.ToolMaterialMasterPanel;
import open.dolphin.client.editor.injectionmedicine.InjectionMedicineMasterPanel;
import open.dolphin.client.editor.admin.AdminMasterPanel;
import open.dolphin.client.editor.medicalsupplies.MedicalSuppliesMasterPanel;
import open.dolphin.client.editor.diagnosis.DiagnosisMasterPanel;

import open.dolphin.client.BlockGlass;

import java.awt.*;
import java.beans.*;
import java.util.EnumSet;
import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * マスタ項目選択パネル　MEMO:画面
 * @author
 */
public class MasterSetPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 4282518548618120301L;
    private static final int DIGNOSIS_INDEX = 0;
    /** 傷病名マスタのインデックス */
    private static final int MEDICAL_TRAET_INDEX = 1;
    /** 診療行為マスタのインデックス */
    private static final int MEDICAL_SUPPLY_INDEX = 2;
    /** 医薬品マスタのインデックス */
    private static final int ADMIN_INDEX = 3;
    /** 用法マスのインデックス */
    private static final int INJECTION_INDEX = 4;
    /** 注射薬マスタのインデックス */
    private static final int TOOL_MATERIAL_INDEX = 5;
    /** 特定器材マスタのインデックス */
//    private JTabbedPane tabbedPane;    /** マスタ検索パネルを格納するタブペイン */
    private DiagnosisMasterPanel diagnosis;
    /** 傷病名マスタ */
    private TreatmentMasterPanel treatment;
    /** 診療行為マスタ */
    private MedicalSuppliesMasterPanel medicalSupplies;
    /** 医薬品マスタ */
    private AdminMasterPanel administration;
    /** 用法マスタ */
    private InjectionMedicineMasterPanel injection;
    /** 注射薬マスタ */
    private ToolMaterialMasterPanel toolMaterial;
    /** 特定器材マスタ */
    private EnumSet<ClaimConst.MasterSet> masterSet;
    /** 使用するマスタの Set */
    private BusyListener busyListener;
    private ItemCountListener itemCountListener;

    /**
     * Creates new MasterTabPanel
     */
    public MasterSetPanel() {

        super(new BorderLayout(0, 11));
        EnumSet<ClaimConst.MasterSet> enumSet = EnumSet.of(
                ClaimConst.MasterSet.DIAGNOSIS,
                ClaimConst.MasterSet.TREATMENT,
                ClaimConst.MasterSet.MEDICAL_SUPPLY,
                ClaimConst.MasterSet.ADMINISTRATION,
                ClaimConst.MasterSet.INJECTION_MEDICINE,
                ClaimConst.MasterSet.TOOL_MATERIAL);
        setMasterSet(enumSet);

        initInstances();
        initComponents();
        intialize();
    }

    /**
     * Creates new MasterTabPanel
     * @param enumSet
     */
    public MasterSetPanel(EnumSet<ClaimConst.MasterSet> enumSet) {
        super(new BorderLayout());
        setMasterSet(enumSet);
        initInstances();
        initComponents();
        intialize();
    }

    /**
     * リソースを解放する。
     */
    public void dispose() {
        if (tabbedPane != null) {
            int cnt = tabbedPane.getTabCount();
            for (int i = 0; i < cnt; i++) {
                AbstractMasterPanel mp = (AbstractMasterPanel) tabbedPane.getComponentAt(i);
                if (mp != null) {
                    mp.dispose();
                }
            }
        }
    }

    /**
     * マスターセットを返す。
     * @return マスターセット
     */
    public EnumSet<ClaimConst.MasterSet> getMasterSet() {
        return masterSet;
    }

    /**
     * マスターセットを設定する。
     * @param masterSet マスターセット
     */
    public void setMasterSet(EnumSet<ClaimConst.MasterSet> masterSet) {
        this.masterSet = masterSet;
        if (tabbedPane != null) {
            enabled();
        }
    }

    /**
     * Glass pane を設定する。
     * @param glass イベントブロックする Glass Pane
     */
    public void setGlass(BlockGlass glass) {
    }

    /**
     *
     */
    private void initInstances() {

        // 傷病名マスタを生成する
        if (masterSet.contains(ClaimConst.MasterSet.DIAGNOSIS)) {
            diagnosis = new DiagnosisMasterPanel(ClaimConst.MasterSet.DIAGNOSIS.getName());
        }

        // 診療行為マスタを生成する
        if (masterSet.contains(ClaimConst.MasterSet.TREATMENT)) {
            treatment = new TreatmentMasterPanel(ClaimConst.MasterSet.TREATMENT.getName());
        }

        // 医薬品マスタを生成する
        if (masterSet.contains(ClaimConst.MasterSet.MEDICAL_SUPPLY)) {
            medicalSupplies = new MedicalSuppliesMasterPanel(ClaimConst.MasterSet.MEDICAL_SUPPLY.getName());
        }

        // 用法マスタを生成する
        if (masterSet.contains(ClaimConst.MasterSet.ADMINISTRATION)) {
            administration = new AdminMasterPanel(ClaimConst.MasterSet.ADMINISTRATION.getName());
        }

        // 注射薬マスタを生成する
        if (masterSet.contains(ClaimConst.MasterSet.INJECTION_MEDICINE)) {
            injection = new InjectionMedicineMasterPanel(ClaimConst.MasterSet.INJECTION_MEDICINE.getName());
        }

        // 特定器材マスタを生成する
        if (masterSet.contains(ClaimConst.MasterSet.TOOL_MATERIAL)) {
            toolMaterial = new ToolMaterialMasterPanel(ClaimConst.MasterSet.TOOL_MATERIAL.getName());
        }

        // BUSY リスナを生成する
        busyListener = new BusyListener();

        // 件数リスナを生成する
        itemCountListener = new ItemCountListener();
    }

    /**
     * 初期化する。
     * 使用するマスタ検索パネルを生成しタブへ格納する。
     */
    private void intialize() {

        enabled();

        // 件数 Label を生成する
        countLabel.setText(paddCount(0));
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 診療行為番号を表示するラベルを生成する
        classCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Fontを設定する
        Font font = new Font("Dialog", Font.PLAIN, 10);
        countLabel.setFont(font);
        classCodeLabel.setFont(font);

        statusP.setLayout(new FlowLayout(FlowLayout.RIGHT));
        statusP.add(Box.createHorizontalStrut(6));

        this.add(tabbedPane, BorderLayout.CENTER);
        this.add(statusP, BorderLayout.SOUTH);
    }

    /**
     *
     */
    public void clear() {
        if (diagnosis != null) {
            diagnosis.clear();
        }

        if (treatment != null) {
            treatment.clear();
        }

        if (medicalSupplies != null) {
            medicalSupplies.clear();
        }

        if (administration != null) {
            administration.clear();
        }

        if (injection != null) {
            injection.clear();
        }

        if (toolMaterial != null) {
            toolMaterial.clear();
        }
    }

    /**
     * オーダクラス(スタンプボックスのタブに関連づけされている番号)を設定する。
     * このコードをもつ診療行為をマスタから検索する。
     * @param serchClass
     */
    public void setSearchClass(String serchClass) {
        treatment.setSearchClass(serchClass);
        if (serchClass != null) {
            classCodeLabel.setText("診療行為:" + serchClass);
        } else {
            classCodeLabel.setText("診療行為:100-999");
        }
    }

    /**
     * 撮影部位検索の enable/disable を制御する。
     * @param enabled enableにする時 true
     */
    public void setRadLocationEnabled(boolean enabled) {
        treatment.setRadLocationEnabled(enabled);
    }

    /**
     * マスタ検索パネルに項目が選択された時のリスナを登録する。
     * マスタ検索パネルの結果テーブルでの項目選択は束縛プロパティになっている。
     * @param l 選択項目プロパティへのリスナ
     */
    private void addListeners(PropertyChangeListener l) {

        if (masterSet.contains(ClaimConst.MasterSet.DIAGNOSIS)) {
            diagnosis.addPropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            diagnosis.addPropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            diagnosis.addPropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.TREATMENT)) {
            treatment.addPropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            treatment.addPropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            treatment.addPropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.MEDICAL_SUPPLY)) {
            medicalSupplies.addPropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            medicalSupplies.addPropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            medicalSupplies.addPropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        // 用法
        if (masterSet.contains(ClaimConst.MasterSet.ADMINISTRATION)) {
            administration.addPropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            administration.addPropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            administration.addPropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.INJECTION_MEDICINE)) {
            injection.addPropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            injection.addPropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            injection.addPropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.TOOL_MATERIAL)) {
            toolMaterial.addPropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            toolMaterial.addPropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            toolMaterial.addPropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }
    }

    /**
     * プログラムの終了時に項目選択への束縛リスナを削除する。
     * @param l マスタ項目選択への束縛リスナ
     */
    private void removeListeners(PropertyChangeListener l) {

        if (masterSet.contains(ClaimConst.MasterSet.DIAGNOSIS)) {
            diagnosis.removePropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            diagnosis.removePropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            diagnosis.removePropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.TREATMENT)) {
            treatment.removePropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            treatment.removePropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            treatment.removePropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.MEDICAL_SUPPLY)) {
            medicalSupplies.removePropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            medicalSupplies.removePropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            medicalSupplies.removePropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        // 用法
        if (masterSet.contains(ClaimConst.MasterSet.ADMINISTRATION)) {
            administration.removePropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            administration.removePropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            administration.removePropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.INJECTION_MEDICINE)) {
            injection.removePropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            injection.removePropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            injection.removePropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }

        if (masterSet.contains(ClaimConst.MasterSet.TOOL_MATERIAL)) {
            toolMaterial.removePropertyChangeListener(AbstractMasterPanel.SELECTED_ITEM_PROP, l);
            toolMaterial.removePropertyChangeListener(AbstractMasterPanel.BUSY_PROP, busyListener);
            toolMaterial.removePropertyChangeListener(AbstractMasterPanel.ITEM_COUNT_PROP, itemCountListener);
        }
    }

    /**
     * 使用されるマスタのタブを enabled にする。
     * 使用されないマスタのタブは disabled にする。
     */
    private void enabled() {

        tabbedPane.setEnabledAt(DIGNOSIS_INDEX, false);
        tabbedPane.setEnabledAt(MEDICAL_TRAET_INDEX, false);
        tabbedPane.setEnabledAt(MEDICAL_SUPPLY_INDEX, false);
        tabbedPane.setEnabledAt(ADMIN_INDEX, false);
        tabbedPane.setEnabledAt(INJECTION_INDEX, false);
        tabbedPane.setEnabledAt(TOOL_MATERIAL_INDEX, false);

        if (masterSet.contains(ClaimConst.MasterSet.DIAGNOSIS)) {
            tabbedPane.setEnabledAt(DIGNOSIS_INDEX, true);
        }

        if (masterSet.contains(ClaimConst.MasterSet.TREATMENT)) {
            tabbedPane.setEnabledAt(MEDICAL_TRAET_INDEX, true);
        }

        if (masterSet.contains(ClaimConst.MasterSet.MEDICAL_SUPPLY)) {
            tabbedPane.setEnabledAt(MEDICAL_SUPPLY_INDEX, true);
        }

        if (masterSet.contains(ClaimConst.MasterSet.ADMINISTRATION)) {
            tabbedPane.setEnabledAt(ADMIN_INDEX, true);
        }

        if (masterSet.contains(ClaimConst.MasterSet.INJECTION_MEDICINE)) {
            tabbedPane.setEnabledAt(INJECTION_INDEX, true);

//INET
            tabbedPane.setEnabledAt(MEDICAL_SUPPLY_INDEX, true);


        }

        if (masterSet.contains(ClaimConst.MasterSet.TOOL_MATERIAL)) {
            tabbedPane.setEnabledAt(TOOL_MATERIAL_INDEX, true);
        }
    }

    /**
     * 傷病名編集を開始する。
     * @param listener 傷病名エディタ
     */
    public void startDiagnosis(PropertyChangeListener listener) {
        classCodeLabel.setText("MEDIS ICD10");
        EnumSet<ClaimConst.MasterSet> enumSet = EnumSet.of(ClaimConst.MasterSet.DIAGNOSIS);
        setMasterSet(enumSet);
        addListeners(listener);
        tabbedPane.setSelectedIndex(DIGNOSIS_INDEX);
        diagnosis.enter();
    }

    /**
     * 傷病名編集を終了する。
     * @param listener 傷病名エディタ
     */
    public void stopDiagnosis(PropertyChangeListener listener) {
        removeListeners(listener);
    }

    /**
     * 処方エディタを開始する。
     * @param editor 処方エディタ
     */
    public void startMedicine(PropertyChangeListener editor) {
        // 器材・医薬品・注射薬
        classCodeLabel.setText("診療行為:210-230");
        EnumSet<ClaimConst.MasterSet> enumSet = EnumSet.of(
                ClaimConst.MasterSet.MEDICAL_SUPPLY,
                ClaimConst.MasterSet.ADMINISTRATION,
                ClaimConst.MasterSet.INJECTION_MEDICINE,
                ClaimConst.MasterSet.TOOL_MATERIAL);
        setMasterSet(enumSet);
        addListeners(editor);
        tabbedPane.setSelectedIndex(MEDICAL_SUPPLY_INDEX);
    }

    /**
     * 処方エディタを終了する。
     * @param editor 処方エディタ
     */
    public void stopMedicine(PropertyChangeListener editor) {
        removeListeners(editor);
    }

    /**
     * 注射エディタを開始する。
     * @param editor 注射エディタ
     */
    public void startInjection(PropertyChangeListener editor) {
        //診療行為・注射薬・器材
        EnumSet<ClaimConst.MasterSet> enumSet = EnumSet.of(
                ClaimConst.MasterSet.TREATMENT,
                //INET
                ClaimConst.MasterSet.MEDICAL_SUPPLY,
                ClaimConst.MasterSet.INJECTION_MEDICINE,
                ClaimConst.MasterSet.TOOL_MATERIAL);
        setMasterSet(enumSet);
        addListeners(editor);
        tabbedPane.setSelectedIndex(MEDICAL_TRAET_INDEX);
    }

    /**
     * 注射エディタを終了する。
     * @param editor
     */
    public void stopInjection(PropertyChangeListener editor) {
        removeListeners(editor);
    }

    /**
     * 診断料/指導在宅エディタを開始する。
     * @param editor 診断料/指導在宅
     */
    public void startCharge(PropertyChangeListener editor) {
        // 診療行為設定
        EnumSet<ClaimConst.MasterSet> enumSet = EnumSet.of(ClaimConst.MasterSet.TREATMENT);
        setMasterSet(enumSet);
        addListeners(editor);
        tabbedPane.setSelectedIndex(MEDICAL_TRAET_INDEX);
    }

    /**
     * 診断料/指導在宅エディタを終了する。
     * @param editor 診断料/指導在宅
     */
    public void stopCharge(PropertyChangeListener editor) {
        removeListeners(editor);
    }

    /**
     * 処置/画像診断/検体検査/手術/生体検査/その他エディタを開始する。
     * @param editor エディタ
     */
    public void startTest(PropertyChangeListener editor) {
        // 診療行為・器材・医薬品・注射薬
        EnumSet<ClaimConst.MasterSet> enumSet = EnumSet.of(
                ClaimConst.MasterSet.TREATMENT,
                ClaimConst.MasterSet.MEDICAL_SUPPLY,
                ClaimConst.MasterSet.INJECTION_MEDICINE,
                ClaimConst.MasterSet.TOOL_MATERIAL);
        setMasterSet(enumSet);
        addListeners(editor);
        tabbedPane.setSelectedIndex(MEDICAL_TRAET_INDEX);
    }

    /**
     * 処置/画像診断/検体検査/手術/生体検査/その他エディタを終了する。
     * @param editor エディタ
     */
    public void stopTest(PropertyChangeListener editor) {
        removeListeners(editor);
    }

    /**
     * 汎用エディタを開始する。
     * @param editor
     */
    public void startGeneral(PropertyChangeListener editor) {
        // 汎用検索
        treatment.setSearchClass(null);
        // 診療行為・器材・医薬品・注射薬
        EnumSet<ClaimConst.MasterSet> enumSet = EnumSet.of(
                ClaimConst.MasterSet.TREATMENT,
                ClaimConst.MasterSet.MEDICAL_SUPPLY,
                ClaimConst.MasterSet.INJECTION_MEDICINE,
                ClaimConst.MasterSet.TOOL_MATERIAL);
        setMasterSet(enumSet);
        addListeners(editor);
        tabbedPane.setSelectedIndex(MEDICAL_TRAET_INDEX);
    }

    /**
     * 汎用スタンプ作成を終了する。
     * @param editor
     */
    public void stopGeneral(PropertyChangeListener editor) {
        removeListeners(editor);
    }

    /**
     * 親フレームの GlassPane を返す。
     * @return 親フレームの BlockGlass
     */
    protected BlockGlass getBlockGlass() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) {
            Component c = null;
            if (w instanceof JDialog) {
                JDialog frame = (JDialog) w;
                c = frame.getGlassPane();
            }
            if (w instanceof JFrame) {
                JFrame frame = (JFrame) w;
                c = frame.getGlassPane();

            }
            return c != null && c instanceof BlockGlass ? (BlockGlass) c : null;
        }
        return null;
    }

    /**
     * Block する。
     */
    protected void block() {
        BlockGlass glass = getBlockGlass();
        if (glass != null) {
            glass.block();
        }
    }

    /**
     * Unblock する。
     */
    protected void unblock() {
        BlockGlass glass = getBlockGlass();
        if (glass != null) {
            glass.unblock();
        }
    }

    /**
     *　MEMO:リスナー
     */
    protected class BusyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            if (e.getPropertyName().equals(AbstractMasterPanel.BUSY_PROP)) {

                boolean busy = ((Boolean) e.getNewValue()).booleanValue();
                if (busy) {
                    //glass.start();
                    countLabel.setText("件数:  ? ");
                    block();
                } else {
                    //glass.stop();
                    unblock();
                }
            }
        }
    }

    /**
     *　MEMO:リスナー
     */
    protected class ItemCountListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            if (e.getPropertyName().equals(AbstractMasterPanel.ITEM_COUNT_PROP)) {
                int count = ((Integer) e.getNewValue()).intValue();
                countLabel.setText(paddCount(count));
            }
        }
    }

    /**
     *
     * @param num
     * @return
     */
    private String paddCount(int num) {
        StringBuilder sb = new StringBuilder();
        sb.append("件数:");
        String numStr = String.valueOf(num);
        int len = numStr.length();
        int cnt = 4 - len;
        for (int i = 0; i < cnt; i++) {
            sb.append(" ");
        }
        sb.append(numStr);
        return sb.toString();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        tabbedPane.addTab(ClaimConst.MasterSet.DIAGNOSIS.getDispName(), diagnosis);
        tabbedPane.addTab(ClaimConst.MasterSet.TREATMENT.getDispName(), treatment);
        tabbedPane.addTab(ClaimConst.MasterSet.MEDICAL_SUPPLY.getDispName(), medicalSupplies);
        tabbedPane.addTab(ClaimConst.MasterSet.ADMINISTRATION.getDispName(), administration);
        tabbedPane.addTab(ClaimConst.MasterSet.INJECTION_MEDICINE.getDispName(), injection);
        tabbedPane.addTab(ClaimConst.MasterSet.TOOL_MATERIAL.getDispName(), toolMaterial);
        statusP = new javax.swing.JPanel();
        countLabel = new javax.swing.JLabel();
        classCodeLabel = new javax.swing.JLabel();

        tabbedPane.setName("tabbedPane"); // NOI18N
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabbedPaneStateChanged(evt);
            }
        });

        statusP.setName("statusP"); // NOI18N

        countLabel.setName("countLabel"); // NOI18N

        classCodeLabel.setName("classCodeLabel"); // NOI18N

        javax.swing.GroupLayout statusPLayout = new javax.swing.GroupLayout(statusP);
        statusP.setLayout(statusPLayout);
        statusPLayout.setHorizontalGroup(
            statusPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPLayout.createSequentialGroup()
                .addGap(215, 215, 215)
                .addComponent(countLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(classCodeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        statusPLayout.setVerticalGroup(
            statusPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(countLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(classCodeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        //
        // 選択されたインデックスに対応するマスタへ enter() を通知する
        //
        int index = tabbedPane.getSelectedIndex();
        AbstractMasterPanel masterPanel = (AbstractMasterPanel) tabbedPane.getComponentAt(index);
        masterPanel.enter();
    }//GEN-LAST:event_tabbedPaneStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel classCodeLabel;
    private javax.swing.JLabel countLabel;
    private javax.swing.JPanel statusP;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
