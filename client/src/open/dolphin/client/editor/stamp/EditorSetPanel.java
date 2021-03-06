/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditorSetPanel.java
 *
 * Created on 2010/03/08, 16:41:25
 */
package open.dolphin.client.editor.stamp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import open.dolphin.project.GlobalConstants;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.delegater.remote.RemoteStampDelegater;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.order.LBaseChargeStampEditor;
import open.dolphin.order.LDiagnosisEditor;
import open.dolphin.order.LGeneralStampEditor;
import open.dolphin.order.LInjectionStampEditor;
import open.dolphin.order.LInstractionChargeStampEditor;
import open.dolphin.order.LOtherStampEditor;
import open.dolphin.order.LPharmaceuticalsStampEditor;
import open.dolphin.order.LPhysiologyStampEditor;
import open.dolphin.order.LRadiologyStampEditor;
import open.dolphin.order.LStayOnHomeChargeStampEditor;
import open.dolphin.order.LSurgeryStampEditor;
import open.dolphin.order.LTestStampEditor;
import open.dolphin.order.LTreatmentStampEditor;
import open.dolphin.order.MasterSetPanel;

/**
 *　スタンプエディタ画面　MEMO:画面　リスナー
 * @author
 */
public class EditorSetPanel extends javax.swing.JPanel implements IStampEditorDialog, PropertyChangeListener, TreeSelectionListener {

    private LBaseChargeStampEditor baseChargeStampEditor;
    private LDiagnosisEditor diagnosisStampEditor;
    private LGeneralStampEditor generalStampEditor;
    private LInjectionStampEditor injectionStampEditor;
    private LInstractionChargeStampEditor instractionStampEditor;
    private LStayOnHomeChargeStampEditor stayOnHomeChargeStampEditor;
    private LPharmaceuticalsStampEditor medStampEditor;
    private LOtherStampEditor otherStampEditor;
    private LPhysiologyStampEditor physiologyStampEditor;
    private LRadiologyStampEditor radiologyStampEditor;
    private LSurgeryStampEditor surgeryStampEditor;
    private LTestStampEditor testStampEditor;
    private LTreatmentStampEditor treatmentStampEditor;
    private MasterSetPanel masterSet;
    private StampModelEditor currentEditor;
    private CardLayout cardLayout;
    private Map<String, StampModelEditor> editors;
    private PropertyChangeSupport boundSupport = new PropertyChangeSupport(this);
    private Object editorValue;
    private StampTreeNode selectedNode;
  //  private ApplicationContext applicationContext;
  //  private Application application;

    /** Creates new form EditorSetPanel */
        /**
         * 
         */
    public EditorSetPanel() {
        initComponents();
   //     applicationContext = GlobalConstants.getApplicationContext();
  //      application = applicationContext.getApplication();
        cardLayout = new CardLayout();
        editorSet.setLayout(cardLayout);
        initCustomComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stampSaveButton = new javax.swing.JButton();
        stampLoadButton = new javax.swing.JButton();
        editorSet = new javax.swing.JPanel();
        center = new javax.swing.JPanel();
        btnPanel = new javax.swing.JPanel();

        stampSaveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/right.png"))); // NOI18N
        stampSaveButton.setEnabled(false);
        stampSaveButton.setName("stampSaveButton"); // NOI18N
        stampSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stampSaveButtonActionPerformed(evt);
            }
        });

        stampLoadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/left.png"))); // NOI18N
        stampLoadButton.setEnabled(false);
        stampLoadButton.setName("stampLoadButton"); // NOI18N
        stampLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stampLoadButtonActionPerformed(evt);
            }
        });

        editorSet.setName("editorSet"); // NOI18N

        javax.swing.GroupLayout editorSetLayout = new javax.swing.GroupLayout(editorSet);
        editorSet.setLayout(editorSetLayout);
        editorSetLayout.setHorizontalGroup(
            editorSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        editorSetLayout.setVerticalGroup(
            editorSetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 11, 0));
        setPreferredSize(new java.awt.Dimension(700, 690));
        setLayout(new java.awt.BorderLayout());

        center.setName("center"); // NOI18N
        center.setLayout(new java.awt.BorderLayout());
        add(center, java.awt.BorderLayout.CENTER);

        btnPanel.setName("btnPanel"); // NOI18N
        btnPanel.setLayout(new javax.swing.BoxLayout(btnPanel, javax.swing.BoxLayout.Y_AXIS));
        add(btnPanel, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

        /**
         *  スタンプからスタンプエディタへ。
         * 右矢印ボタン。
         * 
         * @param evt
         */
    private void stampSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stampSaveButtonActionPerformed
        Cursor currentCursor = getCursor();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            saveStamp();
        } finally {
            setCursor(currentCursor);
        }
    }//GEN-LAST:event_stampSaveButtonActionPerformed

        /**
         * スタンプエディタからスタンプへ。
         * 左矢印ボタン。
         * @param evt
         */
    private void stampLoadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stampLoadButtonActionPerformed
        Cursor currentCursor = getCursor();
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            loadStamp();
        } finally {
            setCursor(currentCursor);
        }
    }//GEN-LAST:event_stampLoadButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnPanel;
    private javax.swing.JPanel center;
    private javax.swing.JPanel editorSet;
    private javax.swing.JButton stampLoadButton;
    private javax.swing.JButton stampSaveButton;
    // End of variables declaration//GEN-END:variables
    /**
     *
     */
    private void saveStamp() {
        setEditorValue(currentEditor.getValue());
        clear();
        stampSaveButton.setEnabled(false);
    }
    /**
     *
     */
    private void loadStamp() {
        // StampInfoからスタンプをロードしエディタにセットする
        final StampTreeNode node = getSelectedNode();
        if (node == null || !(node.getUserObject() instanceof ModuleInfoBean)) {
            return;
        }
        final ModuleInfoBean stampInfo = (ModuleInfoBean) node.getUserObject();
        final RemoteStampDelegater sdl = new RemoteStampDelegater();

        StampModel result = sdl.getStamp(stampInfo.getStampId());
        if ((!sdl.isError()) && result != null) {
            if (result != null) {
                IInfoModel model = result.toInfoModel();
                if (model != null) {
                    ModuleModel stamp = new ModuleModel();
                    stamp.setModel(model);
                    stamp.setModuleInfo(stampInfo);
                    if (currentEditor != null) {
                        currentEditor.setValue(stamp);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(editorSet), "スタンプの取得に失敗しました.", GlobalConstants.getFrameTitle("Stamp取得"), JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * 編集したスタンプオブジェクトを返す。
     * @return 編集したスタンプオブジェクト
     */
    public Object getEditorValue() {
        return editorValue;
    }

    /**
     * 編集値をセットする。この属性は束縛プロパティであり、リスナへ通知される。
     * @param value 編集されたスタンプ
     */
    public void setEditorValue(Object value) {
        editorValue = value;
        boundSupport.firePropertyChange(IStampEditorDialog.EDITOR_VALUE_PROP, null, editorValue);
        currentEditor.setValue(null);
    }

    /**
     *
     * @return
     */
    public StampTreeNode getSelectedNode() {
        return selectedNode;
    }

    /**
     * スタンプボックスで選択されているノード（スタンプ）をセットする。
     * @param node スタンプボックスで選択されているスタンプノード
     */
    public void setSelectedNode(StampTreeNode node) {
        selectedNode = node;
    }
    /**
     *
     */
    @Override
    public void close() {
        if (currentEditor != null) {
            currentEditor.dispose();
        }
        masterSet.dispose();
    }

    /**
     * プロパティチェンジリスナを登録する。
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.addPropertyChangeListener(prop, listener);
    }

    /**
     * プロパティチェンジリスナを削除する。
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    public void remopvePropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.removePropertyChangeListener(prop, listener);
    }

    /**
     * スタンプボックスのタブが切り替えられた時、対応するエディタを show する。
     * @param entity 
     */
    public void show(String entity) {
        // 現在エディタがあれば後始末する
        if (currentEditor != null) {
            currentEditor.dispose();
            currentEditor.removePropertyChangeListener(StampEditorDialog.VALIDA_DATA_PROP, this);
            stampSaveButton.setEnabled(false);
            stampLoadButton.setEnabled(false);
        }
        // 要求されたエディタを開始する
        currentEditor = editors.get(entity);
        // このクラスは VALID_DATA_PROP のリスナになっている
        currentEditor.addPropertyChangeListener(StampEditorDialog.VALIDA_DATA_PROP, this);
        currentEditor.start();

        if (entity.equals("diagnosis")) {
            stampLoadButton.setEnabled(false);
            currentEditor.setValue(null);
        } else {
            ModuleModel stamp = new ModuleModel();
            ModuleInfoBean stampInfo = new ModuleInfoBean();
            stampInfo.initialize(null, "エディタから発行", null, entity, "p");
            stamp.setModuleInfo(stampInfo);
            currentEditor.setValue(stamp);
        }

        if (currentEditor instanceof LRadiologyStampEditor) {
            masterSet.setRadLocationEnabled(true);
        } else {
            masterSet.setRadLocationEnabled(false);
        }
        cardLayout.show(editorSet, entity);
    }

    /**
     * 編集中のスタンプの有効/無効の属性通知を受け、スタンプボックスへ登録する
     * 右向きボタンを制御する。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(StampEditorDialog.VALIDA_DATA_PROP)) {
            Boolean i = (Boolean) e.getNewValue();
            stampSaveButton.setEnabled(i.booleanValue());
        }
    }

    /**
     * スタンプツリーで選択されたスタンプに応じて取り込みボタンを制御する。
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {

        StampTree tree = (StampTree) e.getSource();
        StampTreeNode node = (StampTreeNode) tree.getLastSelectedPathComponent();
        boolean enabled = false;
        StampTreeNode selected = null;

        // ノードが葉で傷病名でない時のみ enabled にする
        // またその時以外は選択ノード属性をnullにする
        if (node != null && node.isLeaf()) {
            ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
            if (info.isSerialized() && (!info.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS))) {
                enabled = true;
                selected = node;
            }
        }
        stampLoadButton.setEnabled(enabled);
        setSelectedNode(selected);
    }
    /**
     *
     * @return
     */
    @Override
    public JButton getOkButton() {
        return null;
    }
    /**
     * 
     */
    public void clear() {
        if (baseChargeStampEditor != null) {
            baseChargeStampEditor.clear();
        }

        if (diagnosisStampEditor != null) {
            diagnosisStampEditor.clear();
        }

        if (generalStampEditor != null) {
            generalStampEditor.clear();
        }

        if (injectionStampEditor != null) {
            injectionStampEditor.clear();
        }

        if (instractionStampEditor != null) {
            instractionStampEditor.clear();
        }

        if (stayOnHomeChargeStampEditor != null) {
            stayOnHomeChargeStampEditor.clear();
        }

        if (medStampEditor != null) {
            medStampEditor.clear();
        }

        if (otherStampEditor != null) {
            otherStampEditor.clear();
        }

        if (physiologyStampEditor != null) {
            physiologyStampEditor.clear();
        }

        if (radiologyStampEditor != null) {
            radiologyStampEditor.clear();
        }

        if (surgeryStampEditor != null) {
            surgeryStampEditor.clear();
        }

        if (testStampEditor != null) {
            testStampEditor.clear();
        }

        if (treatmentStampEditor != null) {
            treatmentStampEditor.clear();
        }

        masterSet.clear();
    }

    /**
     * GUI コンポーネントを生成する。
     */
    private void initCustomComponents() {

        // マスターセットパネルを生成する
        masterSet = new MasterSetPanel();

        // エディタ(セットテーブル)を生成する
        baseChargeStampEditor = new LBaseChargeStampEditor(this, masterSet);
        diagnosisStampEditor = new LDiagnosisEditor(this, masterSet);
        generalStampEditor = new LGeneralStampEditor(this, masterSet);
        injectionStampEditor = new LInjectionStampEditor(this, masterSet);//注射
        instractionStampEditor = new LInstractionChargeStampEditor(this, masterSet);
        stayOnHomeChargeStampEditor = new LStayOnHomeChargeStampEditor(this, masterSet);//在宅
        medStampEditor = new LPharmaceuticalsStampEditor(this, masterSet);
        otherStampEditor = new LOtherStampEditor(this, masterSet);
        physiologyStampEditor = new LPhysiologyStampEditor(this, masterSet);
        radiologyStampEditor = new LRadiologyStampEditor(this, masterSet);
        surgeryStampEditor = new LSurgeryStampEditor(this, masterSet);
        testStampEditor = new LTestStampEditor(this, masterSet);
        treatmentStampEditor = new LTreatmentStampEditor(this, masterSet);

        // カードパネルにエディタを追加する
        editorSet.add(baseChargeStampEditor, "baseChargeOrder");
        editorSet.add(diagnosisStampEditor, "diagnosis");
        editorSet.add(generalStampEditor, "generalOrder");
        editorSet.add(injectionStampEditor, "injectionOrder");
        editorSet.add(instractionStampEditor, "instractionChargeOrder");
        editorSet.add(stayOnHomeChargeStampEditor, "stayOnHomeChargeOrder");
        editorSet.add(medStampEditor, "medOrder");
        editorSet.add(otherStampEditor, "otherOrder");
        editorSet.add(physiologyStampEditor, "physiologyOrder");
        editorSet.add(radiologyStampEditor, "radiologyOrder");
        editorSet.add(surgeryStampEditor, "surgeryOrder");
        editorSet.add(testStampEditor, "testOrder");
        editorSet.add(treatmentStampEditor, "treatmentOrder");

        // カードパネルの PreferedSize を設定する
        editorSet.setPreferredSize(new Dimension(GUIConst.DEFAULT_EDITOR_WIDTH, GUIConst.DEFAULT_EDITOR_HEIGHT));

        // 配置する
        center.add(editorSet, BorderLayout.NORTH);
        center.add(masterSet, BorderLayout.CENTER);

        btnPanel.add(Box.createVerticalStrut(100));
        btnPanel.add(stampSaveButton);
        btnPanel.add(stampLoadButton);
        btnPanel.add(Box.createVerticalGlue());

        // Hashテーブルに登録し show(entity) で使用する
        editors = new HashMap<String, StampModelEditor>();
        editors.put("baseChargeOrder", baseChargeStampEditor);
        editors.put("diagnosis", diagnosisStampEditor);
        editors.put("generalOrder", generalStampEditor);
        editors.put("injectionOrder", injectionStampEditor);
        editors.put("instractionChargeOrder", instractionStampEditor);
        editors.put("stayOnHomeChargeOrder", stayOnHomeChargeStampEditor);
        editors.put("medOrder", medStampEditor);
        editors.put("otherOrder", otherStampEditor);
        editors.put("physiologyOrder", physiologyStampEditor);
        editors.put("radiologyOrder", radiologyStampEditor);
        editors.put("surgeryOrder", surgeryStampEditor);
        editors.put("testOrder", testStampEditor);
        editors.put("treatmentOrder", treatmentStampEditor);
    }
}
