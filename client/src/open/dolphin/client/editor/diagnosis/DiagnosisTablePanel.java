/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DiagnosisTablePanel.java
 *
 * Created on 2009/08/14, 15:15:16
 */
package open.dolphin.client.editor.diagnosis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import open.dolphin.client.AutoKanjiListener;
import open.dolphin.client.AutoRomanListener;
import open.dolphin.client.IStampModelEditor;
import open.dolphin.project.GlobalSettings;
import open.dolphin.infomodel.RegisteredDiagnosisModel;

import open.dolphin.utils.CombinedStringParser;
import open.dolphin.infomodel.MasterItem;
import open.dolphin.log.LogWriter;
import open.dolphin.order.DiagnosisStateMgr;

import open.dolphin.order.RegisteredDiagnosisTransferHandler;
import open.dolphin.project.GlobalVariables;
import open.dolphin.table.ObjectTableModel;
import open.dolphin.table.OddEvenRowRenderer;

/**
 *　傷病名エディタ　MEMO:画面　リスナー
 * @author
 * IItemTablePanel
 */
public class DiagnosisTablePanel extends JPanel implements PropertyChangeListener {

    // 傷病名の修飾語コード
    private static final String MODIFIER_CODE = "ZZZ";
    // 傷病名手入力時につけるコード
    private static final String HAND_CODE = "0000999";
    // Diagnosis table のパラメータ
    private static final int CODE_COL = 0;
    private static final int NAME_COL = 1;
    private static final int ALIAS_COL = 2;
    private static final int[] DIAGNOSIS_TABLE_COLUMN_WIDTHS = {150, 200, 200};
    private static final String TOOLTIP_TABLE = "コードのカラムで Drag & Drop で順番を入れ替えることができます";
    private static final String SELECTED_ITEM_PROP = "selectedItemProp";
    private static final int COMBINED_FIELD_LENGTH = 20;    // 複合病名表示フィールドの長さ
    private ObjectTableModel tableModel;    // Table model
    private IStampModelEditor context;    // Stamp Editor
    private DiagnosisStateMgr curState;    // 状態マシン

    /** Creates new form DiagnosisTablePanel
     * @param context
     */
    public DiagnosisTablePanel(IStampModelEditor context) {
        super(new BorderLayout());
        initComponents();
        setContext(context);

        // テーブルのカラム名を取得する
        String[] diganosisColumns = new String[]{"コード", "疾患名/修飾語", "エイリアス"};

        // テーブルモデルを生成する
        tableModel = new ObjectTableModel(diganosisColumns, 0) {

            // 病名カラムも修飾語の編集が可能
            @Override
            public boolean isCellEditable(int row, int col) {
                boolean ret = false;
                RegisteredDiagnosisModel model = (RegisteredDiagnosisModel) getObject(row);
                if (col == NAME_COL) {
                    if (model == null) {
                        ret = true;
                    } else if (!model.getDiagnosisCode().startsWith(MODIFIER_CODE)) {
                        ret = true;
                    }
                } else if (col == ALIAS_COL) {
                    if (model != null && (!model.getDiagnosisCode().startsWith(MODIFIER_CODE))) {
                        ret = true;
                    }
                }
                return ret;
            }
    /**
     *
     */
            @Override
            public Object getValueAt(int row, int col) {
                RegisteredDiagnosisModel model = (RegisteredDiagnosisModel) getObject(row);
                if (model == null) {
                    return null;
                }
                String ret = null;
                switch (col) {
                    case CODE_COL:
                        ret = model.getDiagnosisCode();
                        break;
                    case NAME_COL:
                        ret = model.getDiagnosisName();
                        break;
                    case ALIAS_COL:
                        ret = model.getDiagnosisAlias();
                        break;
                default: LogWriter.fatal(getClass(), "case default");
                }
                return ret;
            }
    /**
     *
     */
            @Override
            public void setValueAt(Object o, int row, int col) {
                if (o == null) {
                    return;
                }
                int index = ((String) o).indexOf(',');
                if (index > 0) {
                    return;
                }
                RegisteredDiagnosisModel model = (RegisteredDiagnosisModel) getObject(row);
                String value = (String) o;
                switch (col) {
                    case NAME_COL:
                        // 病名が手入力された場合は、コードに 0000999 を設定する
                        if (!value.equals("")) {
                            if (model != null) {
                                if (!model.getDiagnosis().equals(value)) {
                                    model.setDiagnosis(value);
                                    model.setDiagnosisCode(HAND_CODE);
                                    fireTableCellUpdated(row, col);
                                }
                            } else {
                                model = new RegisteredDiagnosisModel();
                                model.setDiagnosis(value);
                                model.setDiagnosisCode(HAND_CODE);
                                addRow(model);
                                curState.processEvent(DiagnosisStateMgr.Event.ADDED);
                            }
                        }
                        break;
                    case ALIAS_COL:
                        // エイリアスの入力があった場合
                        if (model != null) {
                            String test = model.getDiagnosis();
                            int idx = test.indexOf(',');
                            if (idx > 0) {
                                test = test.substring(0, idx);
                                test = test.trim();
                            }
                            if (value.equals("")) {
                                model.setDiagnosis(test);
                            } else {
                                StringBuilder sb = new StringBuilder();
                                sb.append(test);
                                sb.append(",");
                                sb.append(value);
                                model.setDiagnosis(sb.toString());
                            }
                        }
                        break;
                default: LogWriter.fatal(getClass(), "case default");
                }
            }
        };

        table.setModel(tableModel);
        table.setTransferHandler(new RegisteredDiagnosisTransferHandler(DiagnosisTablePanel.this)); // TransferHandler
        table.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int ctrlMask = InputEvent.CTRL_DOWN_MASK;
                int action = ((e.getModifiersEx() & ctrlMask) == ctrlMask) ? TransferHandler.COPY : TransferHandler.MOVE;
                JComponent c = (JComponent) e.getSource();
                TransferHandler handler = c.getTransferHandler();
                handler.exportAsDrag(c, e, action);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);

        ListSelectionModel m = table.getSelectionModel();
        m.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    curState.processEvent(DiagnosisStateMgr.Event.SELECTED);
                }
            }
        });

        table.setToolTipText(TOOLTIP_TABLE);
        table.setDefaultRenderer(Object.class, new OddEvenRowRenderer());//病名に期限なし

        // CellEditor を設定する
        // 疾患名
        TableColumn column = table.getColumnModel().getColumn(NAME_COL);
        JTextField nametf = new JTextField();

        nametf.addFocusListener(AutoKanjiListener.getInstance());

        DefaultCellEditor nameEditor = new DefaultCellEditor(nametf);
        int clickCountToStart = GlobalVariables.getPreferences().getInt("diagnosis.table.clickCountToStart", 2);
        nameEditor.setClickCountToStart(clickCountToStart);
        column.setCellEditor(nameEditor);

        // 病名エイリアス
        column = table.getColumnModel().getColumn(ALIAS_COL);
        JTextField aliastf = new JTextField();

        aliastf.addFocusListener(AutoRomanListener.getInstance()); // alias

        DefaultCellEditor aliasEditor = new DefaultCellEditor(aliastf);
        aliasEditor.setClickCountToStart(clickCountToStart);
        column.setCellEditor(aliasEditor);

        // 列幅設定
        int len = DIAGNOSIS_TABLE_COLUMN_WIDTHS.length;
        for (int i = 0; i < len; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(DIAGNOSIS_TABLE_COLUMN_WIDTHS[i]);
        }

        // 複合病名と Command button
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));

        // 複合病名フィールドを生成する

        btnPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        combinedDiagnosis.setColumns(COMBINED_FIELD_LENGTH);
        p.setLayout(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        btnPanel.add(Box.createRigidArea(new Dimension(5, 0)));

        // 親ダイアログのOKボタンを追加する
        if (getContext().getContext().getOkButton() != null) {
            btnPanel.add(getContext().getContext().getOkButton());
        }

        // 状態マシンを開始する
        curState = new DiagnosisStateMgr(removeButton, clearButton, stateLabel, tableModel, table, getContext());
        curState.enter();

        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        add(scroller, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroller = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnPanel = new javax.swing.JPanel();
        removeButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        p = new javax.swing.JPanel();
        combinedDiagnosis = new javax.swing.JTextField();
        stateLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        scroller.setName("scroller"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        scroller.setViewportView(table);

        btnPanel.setName("btnPanel"); // NOI18N

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/del_16.gif"))); // NOI18N
        removeButton.setToolTipText("選択した傷病名を削除します");
        removeButton.setEnabled(false);
        removeButton.setName("removeButton"); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/remov_16.gif"))); // NOI18N
        clearButton.setToolTipText("テーブルをクリアします");
        clearButton.setEnabled(false);
        clearButton.setName("clearButton"); // NOI18N
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        p.setName("p"); // NOI18N

        combinedDiagnosis.setEditable(false);
        combinedDiagnosis.setToolTipText("テーブルの行を連結して修飾語付きの傷病名にします");
        combinedDiagnosis.setName("combinedDiagnosis"); // NOI18N

        stateLabel.setName("stateLabel"); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/about_16.gif"))); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel1.setText("連結した傷病名:");
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout pLayout = new javax.swing.GroupLayout(p);
        p.setLayout(pLayout);
        pLayout.setHorizontalGroup(
            pLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combinedDiagnosis, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(238, Short.MAX_VALUE))
        );
        pLayout.setVerticalGroup(
            pLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(combinedDiagnosis, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE))
                    .addComponent(stateLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout btnPanelLayout = new javax.swing.GroupLayout(btnPanel);
        btnPanel.setLayout(btnPanelLayout);
        btnPanelLayout.setHorizontalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnPanelLayout.createSequentialGroup()
                .addComponent(p, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearButton)
                .addContainerGap())
        );
        btnPanelLayout.setVerticalGroup(
            btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnPanelLayout.createSequentialGroup()
                .addGroup(btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(btnPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(btnPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(clearButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(btnPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(p, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scroller, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     *
     * @param evt
     */
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        tableModel.clear();
        combinedDiagnosis.setText("");
        curState.processEvent(DiagnosisStateMgr.Event.CLEARED);
    }//GEN-LAST:event_clearButtonActionPerformed
    /**
     * 
     * @param evt
     */
    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        int row = table.getSelectedRow();
        // TableModel でレンジチェックしているので安全
        tableModel.removeRow(row);
        redraw();
        curState.processEvent(DiagnosisStateMgr.Event.DELETED);
    }//GEN-LAST:event_removeButtonActionPerformed

    /**
     *
     */
    public void clear() {
        tableModel.clear();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnPanel;
    private javax.swing.JButton clearButton;
    private javax.swing.JTextField combinedDiagnosis;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel p;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    /**
     * StampEditor を返す。
     * @return この編集テーブルの StampEditor
     */
    public IStampModelEditor getContext() {
        return context;
    }

    /**
     * StampEditor を設定する。
     * @param context この編集テーブルの StampEditor
     */
    public void setContext(IStampModelEditor context) {
        this.context = context;
    }

    /**
     * マスタ検索テーブルで選択されたアイテムを編集テーブルへ取り込む。
     * @param e PropertyChangeEvent
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();

        if (prop.equals(SELECTED_ITEM_PROP)) {
            // 通知された MasterItem から RegisteredDiagnosisModel
            // を生成し、編集テーブルへ加える。
            MasterItem item = (MasterItem) e.getNewValue();
            if (item != null) {
                RegisteredDiagnosisModel model = new RegisteredDiagnosisModel();
                model.setDiagnosis(item.getName());
                model.setDiagnosisCode(item.getCode());
                model.setDiagnosisCodeSystem(item.getMasterTableId());

                tableModel.addRow(model);
                redraw();
                curState.processEvent(DiagnosisStateMgr.Event.ADDED); // 状態マシンへイベントを送信する
            }
        }
    }

    /**
     * テーブルをスキャンし、傷病名コンポジットする。
     */
    public void redraw() {

        if (hasModifier()) {
            CombinedStringParser sb = new CombinedStringParser();
            int count = tableModel.getDataSize();
            for (int i = 0; i < count; i++) {
                RegisteredDiagnosisModel diag = (RegisteredDiagnosisModel) tableModel.getObject(i);
                sb.add(diag.getDiagnosis());
            }
            combinedDiagnosis.setText(sb.toPlainString());
        } else {
            combinedDiagnosis.setText("");
        }
    }

    /**
     * 修飾語をふくんでいるかどうかを返す。
     */
    private boolean hasModifier() {
        boolean hasModifier = false;
        int count = tableModel.getDataSize();
        for (int i = 0; i < count; i++) {
            RegisteredDiagnosisModel diag = (RegisteredDiagnosisModel) tableModel.getObject(i);
            if (diag.getDiagnosisCode().startsWith(MODIFIER_CODE)) {
                hasModifier = true;
                break;
            }
        }
        return hasModifier;
    }

    /**
     * 傷病名テーブルをスキャンし修飾語つきの傷病にして返す。
     * @return
     */
    public Object getValue() {
        if (hasModifier()) {
            return getValue1();
        } else {
            return getValue2();
        }
    }

    /**
     * 傷病名テーブルをスキャンし修飾語つきの傷病にして返す。
     */
    private Object getValue1() {

        RegisteredDiagnosisModel diagnosis = null;
        CombinedStringParser combinedCode = new CombinedStringParser();
        CombinedStringParser combinedName = new CombinedStringParser();

        // テーブルをスキャンする
        int count = tableModel.getDataSize();
        for (int i = 0; i < count; i++) {
            RegisteredDiagnosisModel diag = (RegisteredDiagnosisModel) tableModel.getObject(i);
            String diagCode = diag.getDiagnosisCode();
            if (!diagCode.startsWith(MODIFIER_CODE)) {
                // 修飾語でない場合は基本病名と見なし、パラメータを設定する
                diagnosis = new RegisteredDiagnosisModel();
                diagnosis.setDiagnosisCodeSystem(diag.getDiagnosisCodeSystem());
            } else {
                diagCode = diagCode.substring(MODIFIER_CODE.length());// ZZZ をトリムする ORCA 実装
            }
            combinedCode.add(diagCode);
            combinedName.add(diag.getDiagnosis());
        }

        if (diagnosis != null && combinedName.size() > 0 && combinedCode.size() > 0) {
            // 名前とコードを設定する
            diagnosis.setDiagnosis(combinedName.toCombinedString());
            diagnosis.setDiagnosisCode(combinedCode.toCombinedString());
            List<RegisteredDiagnosisModel> result = new ArrayList<RegisteredDiagnosisModel>(1);
            result.add(diagnosis);
            return result;
        } else {
            return null;
        }
    }

    /**
     * 傷病名テーブルをスキャンし修飾語つきの傷病にして返す。
     */
    private Object getValue2() {
        return tableModel.getObjectList();
    }

    /**
     *
     * @param o
     */
    public void setValue(Object[] o) {
    }
}
