/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DiagnosisEditorDialog.java
 *
 * Created on 2009/08/05, 15:26:08
 */
package open.dolphin.client.editor.diagnosis;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import open.dolphin.project.GlobalConstants;
import open.dolphin.client.TaskTimerMonitor;
import open.dolphin.dao.SqlDaoFactory;

import open.dolphin.utils.CombinedStringParser;

import open.dolphin.infomodel.DiseaseEntry;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.order.ClaimConst;
import open.dolphin.table.ObjectTableModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

import open.dolphin.client.AutoKanjiListener;

import open.dolphin.dao.SqlMasterDao;
import open.dolphin.order.RegisteredDiagnosisTransferHandler;

/**
 *　傷病名エディタ　MEMO:画面
 * @author
 */
public class DiagnosisEditorDialog extends javax.swing.JDialog {

    private static final String[] diseaseColumns = {"コード", "名  称", "カ ナ", "ICD10", "有効期限"};
    private final int START_NUM_ROWS = 20;
    private RegisteredDiagnosisModel model;
    private CombinedStringParser diagnosisName;
    private CombinedStringParser diagnosisCode;
    /** 検索するマスタ名 */
    private String master;
    /** ソート節 */
    private String sortBy;
    /** order by 節 */
    private String order;
    /** 検索結果テーブルの table model */
    private ObjectTableModel sourceTableModel;
    private ObjectTableModel resultTableModel;
    private String currentSearchType = "";
    final SqlMasterDao dao;
    //  final SqlDiseaseMasterDao dao;
    private boolean isDirty;

    /**
     *
     * @param parent
     * @param modal
     * @param model
     */
    public DiagnosisEditorDialog(java.awt.Frame parent, boolean modal, RegisteredDiagnosisModel model) {
        super(parent, modal);
        initComponents();

        dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
        sortBy = "byomeikana";

        isDirty = false;
        this.model = model;

        diagnosisName = new CombinedStringParser(this.model.getDiagnosis());
        diagnosisCode = new CombinedStringParser(this.model.getDiagnosisCode());
        master = ClaimConst.MasterSet.DIAGNOSIS.getName();

        InitialSourceTable();
        InitialDiseaseTable();



        DocumentListener myListener = new TextChenge();
        diagnosisField.getDocument().addDocumentListener(myListener);
        diagnosisField.setText(diagnosisName.toPlainString());

        diagnosisField.addFocusListener(AutoKanjiListener.getInstance());
        sourceSearchField.addFocusListener(AutoKanjiListener.getInstance());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        diseasePopupMenu = new javax.swing.JPopupMenu();
        upMenuItem = new javax.swing.JMenuItem();
        downMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        diseaseScroller = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        diagnosisField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        partsButton = new javax.swing.JButton();
        typeButton = new javax.swing.JButton();
        diseaseButton = new javax.swing.JButton();
        suffixButton = new javax.swing.JButton();
        sourceSearchField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        sourceScroller = new javax.swing.JScrollPane();
        sourceTable = new javax.swing.JTable();

        diseasePopupMenu.setName("diseasePopupMenu"); // NOI18N

        upMenuItem.setText("上へ");
        upMenuItem.setName("upMenuItem"); // NOI18N
        upMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upMenuItemActionPerformed(evt);
            }
        });
        diseasePopupMenu.add(upMenuItem);

        downMenuItem.setText("下へ");
        downMenuItem.setName("downMenuItem"); // NOI18N
        downMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downMenuItemActionPerformed(evt);
            }
        });
        diseasePopupMenu.add(downMenuItem);

        deleteMenuItem.setText("削除");
        deleteMenuItem.setName("deleteMenuItem"); // NOI18N
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        diseasePopupMenu.add(deleteMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("傷病名編集ダイアログ");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        okButton.setText("OK");
        okButton.setName("okButton"); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        diseaseScroller.setName("diseaseScroller"); // NOI18N

        resultTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "コード", "名  称", "カ ナ", "ICD10", "有効期限"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        resultTable.setComponentPopupMenu(diseasePopupMenu);
        resultTable.setName("resultTable"); // NOI18N
        resultTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        resultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resultTableMouseClicked(evt);
            }
        });
        resultTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                resultTableMouseDragged(evt);
            }
        });
        diseaseScroller.setViewportView(resultTable);
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        resultTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        resultTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        resultTable.getColumnModel().getColumn(4).setPreferredWidth(50);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        diagnosisField.setName("diagnosisField"); // NOI18N
        diagnosisField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diagnosisFieldActionPerformed(evt);
            }
        });
        diagnosisField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                diagnosisFieldPropertyChange(evt);
            }
        });

        jLabel2.setText("傷病名：");
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(diagnosisField, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(diagnosisField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        partsButton.setText("部位");
        partsButton.setName("partsButton"); // NOI18N
        partsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partsButtonActionPerformed(evt);
            }
        });

        typeButton.setText("特性");
        typeButton.setName("typeButton"); // NOI18N
        typeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeButtonActionPerformed(evt);
            }
        });

        diseaseButton.setText("病名");
        diseaseButton.setName("diseaseButton"); // NOI18N
        diseaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diseaseButtonActionPerformed(evt);
            }
        });

        suffixButton.setText("接尾");
        suffixButton.setName("suffixButton"); // NOI18N
        suffixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suffixButtonActionPerformed(evt);
            }
        });

        sourceSearchField.setName("sourceSearchField"); // NOI18N
        sourceSearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceSearchFieldActionPerformed(evt);
            }
        });

        jLabel1.setText("検索語：");
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sourceSearchField, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(partsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diseaseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suffixButton)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suffixButton)
                    .addComponent(diseaseButton)
                    .addComponent(typeButton)
                    .addComponent(partsButton)
                    .addComponent(sourceSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sourceScroller.setName("sourceScroller"); // NOI18N

        sourceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "コード", "名  称", "カ ナ", "ICD10", "有効期限"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        sourceTable.setName("sourceTable"); // NOI18N
        sourceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sourceTableMouseClicked(evt);
            }
        });
        sourceTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sourceTableKeyTyped(evt);
            }
        });
        sourceScroller.setViewportView(sourceTable);
        sourceTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        sourceTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        sourceTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        sourceTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        sourceTable.getColumnModel().getColumn(4).setPreferredWidth(50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(diseaseScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(259, 259, 259)
                        .addComponent(okButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sourceScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diseaseScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void partsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partsButtonActionPerformed

        currentSearchType = ClaimConst.SubTypeSet.PREFIX.getName();
        String key = sourceSearchField.getText().trim();
        search(key, true, currentSearchType, sourceTableModel);
    }//GEN-LAST:event_partsButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void typeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeButtonActionPerformed

        currentSearchType = ClaimConst.SubTypeSet.TYPE.getName();
        String key = sourceSearchField.getText().trim();
        search(key, true, currentSearchType, sourceTableModel);
    }//GEN-LAST:event_typeButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void diseaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diseaseButtonActionPerformed
        currentSearchType = "";
        String key = sourceSearchField.getText().trim();
        if (!key.equals("")) {
            search(key, true, currentSearchType, sourceTableModel);
        }
    }//GEN-LAST:event_diseaseButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void suffixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suffixButtonActionPerformed
        currentSearchType = ClaimConst.SubTypeSet.POSTFIX.getName();
        String key = sourceSearchField.getText().trim();
        search(key, true, currentSearchType, sourceTableModel);
    }//GEN-LAST:event_suffixButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setResult();
        model.setDiagnosis(diagnosisName.toCombinedString());
        model.setDiagnosisCode(diagnosisCode.toCombinedString());
        isDirty = true;
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void diagnosisFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diagnosisFieldActionPerformed
        String key = diagnosisField.getText().trim();
        search(key, true, ClaimConst.SubTypeSet.NAMES.getName(), resultTableModel);
    }//GEN-LAST:event_diagnosisFieldActionPerformed
    /**
     *
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing
    /**
     *
     * @param evt
     */
    private void resultTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resultTableMouseClicked
        int selected = resultTable.getSelectedRow();
        DiseaseEntry diseaseEntry = (DiseaseEntry) resultTableModel.getValueAt(selected, 0);
        if (diseaseEntry != null) {
            if (diseaseEntry.getCode() != null) {
                search("", true, dao.getSerchClass(diseaseEntry.getCode()), sourceTableModel);
            }
        }
    }//GEN-LAST:event_resultTableMouseClicked
    /**
     *
     * @param evt
     */
    private void resultTableMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resultTableMouseDragged
    }//GEN-LAST:event_resultTableMouseDragged
    /**
     *
     * @param evt
     */
    private void sourceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sourceTableMouseClicked
        if (evt.getClickCount() == 2) {
            int selected = sourceTable.getSelectedRow();
            DiseaseEntry sourceEntry = (DiseaseEntry) sourceTableModel.getValueAt(selected, 0);
            if (sourceEntry != null) {
                DiseaseEntry diseaseEntry;
                int chenged = resultTable.getSelectedRow();
                diseaseEntry = (DiseaseEntry) resultTableModel.getValueAt(chenged, 0);
                if (diseaseEntry == null) {
                    diseaseEntry = new DiseaseEntry();
                    resultTableModel.addRow(diseaseEntry);
                }
                diseaseEntry.copyFrom(sourceEntry);
                redrawDiseaseTable();
            }
        }
    }//GEN-LAST:event_sourceTableMouseClicked
    /**
     *
     * @param evt
     */
    private void upMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upMenuItemActionPerformed
        int selected = resultTable.getSelectedRow();
        DiseaseEntry fromEntry = (DiseaseEntry) resultTableModel.getValueAt(selected, 0);
        if (fromEntry != null) {
            DiseaseEntry toEntry = (DiseaseEntry) resultTableModel.getValueAt(selected - 1, 0);
            if (toEntry != null) {
                fromEntry.swapEntry(toEntry);
                redrawDiseaseTable();
                isDirty = true;
            }
        }
    }//GEN-LAST:event_upMenuItemActionPerformed
    /**
     *
     * @param evt
     */
    private void downMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downMenuItemActionPerformed
        int selected = resultTable.getSelectedRow();
        DiseaseEntry fromEntry = (DiseaseEntry) resultTableModel.getValueAt(selected, 0);
        if (fromEntry != null) {
            DiseaseEntry toEntry = (DiseaseEntry) resultTableModel.getValueAt(selected + 1, 0);
            if (toEntry != null) {
                fromEntry.swapEntry(toEntry);
                redrawDiseaseTable();
                isDirty = true;
            }
        }
    }//GEN-LAST:event_downMenuItemActionPerformed
    /**
     *
     * @param evt
     */
    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        int selected = resultTable.getSelectedRow();
        DiseaseEntry deleteEntry = (DiseaseEntry) resultTableModel.getValueAt(selected, 0);
        if (deleteEntry != null) {
            resultTableModel.deleteRow(selected);
            redrawDiseaseTable();
            isDirty = true;
        }
    }//GEN-LAST:event_deleteMenuItemActionPerformed
    /**
     *
     * @param evt
     */
    private void sourceTableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sourceTableKeyTyped
    }//GEN-LAST:event_sourceTableKeyTyped
    /**
     *
     * @param evt
     */
    private void sourceSearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceSearchFieldActionPerformed
        String key = sourceSearchField.getText().trim();
        search(key, true, currentSearchType, sourceTableModel);
    }//GEN-LAST:event_sourceSearchFieldActionPerformed
    /**
     *
     * @param evt
     */
    private void diagnosisFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_diagnosisFieldPropertyChange
        String key = diagnosisField.getText().trim();
        search(key, true, ClaimConst.SubTypeSet.NAMES.getName(), resultTableModel);
    }//GEN-LAST:event_diagnosisFieldPropertyChange
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JTextField diagnosisField;
    private javax.swing.JButton diseaseButton;
    private javax.swing.JPopupMenu diseasePopupMenu;
    private javax.swing.JScrollPane diseaseScroller;
    private javax.swing.JMenuItem downMenuItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton okButton;
    private javax.swing.JButton partsButton;
    private javax.swing.JTable resultTable;
    private javax.swing.JScrollPane sourceScroller;
    private javax.swing.JTextField sourceSearchField;
    private javax.swing.JTable sourceTable;
    private javax.swing.JButton suffixButton;
    private javax.swing.JButton typeButton;
    private javax.swing.JMenuItem upMenuItem;
    // End of variables declaration//GEN-END:variables

    /**
     *
     */
    private void InitialSourceTable() {

        sourceTableModel = new ObjectTableModel(diseaseColumns, START_NUM_ROWS) {

            @Override
            public Class getColumnClass(int col) {
                return DiseaseEntry.class;
            }
        };

        JTableHeader header = sourceTable.getTableHeader();
        header.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });

        sourceTable.setModel(sourceTableModel);

        // レンダラを登録する
        DiseaseMasterRenderer diseaseRenderer = new DiseaseMasterRenderer();
        sourceTable.setDefaultRenderer(DiseaseEntry.class, diseaseRenderer);
    }

    /**
     *
     */
    private void InitialDiseaseTable() {

        resultTableModel = new ObjectTableModel(diseaseColumns, START_NUM_ROWS) {

            @Override
            public Class getColumnClass(int col) {
                return DiseaseEntry.class;
            }
        };
        resultTableModel.setObjectList(getInitialValues());

        // 行クリック処理を登録する
        ListSelectionModel selection = resultTable.getSelectionModel();
        selection.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
            }
        });

        resultTable.setTransferHandler(new RegisteredDiagnosisTransferHandler(null)); // TransferHandler);

        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setRowSelectionAllowed(true);
        ListSelectionModel m = resultTable.getSelectionModel();//MEMO: unused?

        resultTable.setModel(resultTableModel);

        // レンダラを登録する
        DiseaseMasterRenderer diseaseRenderer = new DiseaseMasterRenderer();
        resultTable.setDefaultRenderer(DiseaseEntry.class, diseaseRenderer);
    }

    /**
     *
     * @return
     */
    private List getInitialValues() {
        List<DiseaseEntry> result = new ArrayList<DiseaseEntry>();

        for (String code : diagnosisCode) {
            List bycode = dao.getDiseaseCodeFromOrca(code);
            if (bycode.size() == 1) {
                DiseaseEntry entry = (DiseaseEntry) bycode.get(0);
                result.add(entry);
            } else {
                System.err.print("code not found");
            }
        }
        return result;
    }

    /**
     *
     */
    private void redrawDiseaseTable() {
        setResult();
        diseaseScroller.setViewportView(resultTable);
        diagnosisField.setText(diagnosisName.toPlainString());
    }

    /**
     *
     * @param text
     * @param startsWith
     * @param subtype
     * @param resultModel
     */
    protected void search(final String text, final boolean startsWith, final String subtype, final ObjectTableModel resultModel) {

        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();

        Task task = new Task<Object, Void>(app) {

            @Override
            protected Object doInBackground() throws Exception {
                return dao.getByName(master, text, startsWith, subtype, sortBy, order);
            }

            @Override
            protected void succeeded(Object result) {

                if (dao.isNoError()) {
                    resultModel.setObjectList((List) result);
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

        TaskMonitor taskMonitor = appCtx.getTaskMonitor();
        String message = "マスタ検索";
        String note = text + "を検索しています...";
        Component c = SwingUtilities.getWindowAncestor(this);
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, note, 200, 60 * 1000);
        taskMonitor.addPropertyChangeListener(w);

        appCtx.getTaskService().execute(task);
    }

    /**
     *
     */
    private void setResult() {
        diagnosisName.clear();
        diagnosisCode.clear();
        int rowCount = resultTableModel.getRowCount();
        for (int index = 0; index < rowCount; index++) {
            DiseaseEntry o = (DiseaseEntry) resultTableModel.getObject(index);
            if (o != null) {
                diagnosisName.add(o.getName());
                diagnosisCode.add(o.getCode());
            } else {
                break;
            }
        }
    }

    /**
     * @return the isDirty
     */
    public boolean isDirty() {
        return isDirty;
    }

    /**
     *
     */
    private class TextChenge implements DocumentListener {

        /**
         *
         * @param e
         */
        @Override
        public void insertUpdate(DocumentEvent e) {
            okButton.setEnabled(!diagnosisField.getText().trim().isEmpty());
        }

        /**
         * 
         * @param e
         */
        @Override
        public void removeUpdate(DocumentEvent e) {
            okButton.setEnabled(!diagnosisField.getText().trim().isEmpty());
        }

        /**
         *
         * @param e
         */
        @Override
        public void changedUpdate(DocumentEvent e) {
            okButton.setEnabled(!diagnosisField.getText().trim().isEmpty());
        }
    }
}
