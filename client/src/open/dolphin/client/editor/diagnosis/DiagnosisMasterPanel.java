/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DiagnosisMasterPanel.java
 *
 * Created on 2009/08/12, 15:03:22
 */
package open.dolphin.client.editor.diagnosis;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import javax.swing.text.JTextComponent;
import open.dolphin.client.AutoKanjiListener;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.infomodel.DiseaseEntry;
import open.dolphin.infomodel.MasterItem;
import open.dolphin.log.LogWriter;
import open.dolphin.order.AbstractMasterPanel;
import open.dolphin.order.ClaimConst;
import open.dolphin.table.ObjectTableModel;

/**
 *　傷病名マスター　MEMO:画面
 * @author
 */
public class DiagnosisMasterPanel extends AbstractMasterPanel {

    private static final long serialVersionUID = -8731279062992813732L;
    private static final String[] diseaseColumns = {"コード", "名  称", "カ ナ", "ICD10", "有効期限"}; //GlobalVariables.getStringArray("masterSearch.disease.columnNames");
    private static final String codeSystem = "ICD10_2001-10-03MEDIS"; //GlobalVariables.getString("mml.codeSystem.diseaseMaster");
    private static final String[] sortButtonNames = {"コード", "名称", "ICD10"}; //GlobalVariables.getStringArray("masterSearch.disease.sortButtonNames");
    private static final String[] sortColumnNames = {"byomeicd", "byomei", "byomeikana", "icd10", "haisiymd"}; //GlobalVariables.getStringArray("masterSearch.disease.sortColumnNames");
    private boolean sortable = false;

    /** Creates new form DiagnosisMasterPanel
     * @param master
     */
    public DiagnosisMasterPanel(String master) {
        super(master);
    }
    /**
     *
     */
    @Override
    protected void initialize() {
        initComponents();

        sortButtons = new JRadioButton[sortButtonNames.length];
        for (int i = 0; i < sortButtonNames.length; i++) {
            JRadioButton radio = new JRadioButton(sortButtonNames[i]);
            sortButtons[i] = radio;
            bg.add(radio);
            radio.addActionListener(new SortActionListener(this, sortColumnNames[i], i));
        }

        int index = prefs.getInt("masterSearch.disease.sort", 0);
        sortButtons[index].setSelected(true);
        setSortBy(sortColumnNames[index]);

        tableModel = new ObjectTableModel(diseaseColumns, START_NUM_ROWS) {

            @Override
            public Class getColumnClass(int col) {
                return DiseaseEntry.class;
            }
        };

        // Table を生成する
        table.setModel(tableModel);

        JTableHeader header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (sortable) {
                    doSort(e);
                }
            }
        });

        // 行クリック処理を登録する
        ListSelectionModel m = table.getSelectionModel();//MEMO: unused?

        // 行選択を可能にする
        table.setRowSelectionAllowed(true);

        // カラム幅を設定する
        TableColumn column = null;
        int[] width = new int[]{50, 200, 200, 40, 50};
        int len = width.length;
        for (int i = 0; i < len; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width[i]);
        }

        // レンダラを登録する
        DiseaseMasterRenderer dr = new DiseaseMasterRenderer();

        table.setDefaultRenderer(DiseaseEntry.class, dr);

        // Layout

        searchField.addFocusListener(AutoKanjiListener.getInstance());
        findField.addFocusListener(AutoKanjiListener.getInstance());

        // Keyword
        keyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 2));
        keyPanel.setBorder(BorderFactory.createTitledBorder(keywordBorderTitle));
        searchButtonPanel.setLayout(new BoxLayout(searchButtonPanel, BoxLayout.X_AXIS));
        combinedSearchPanel.setLayout(new BoxLayout(combinedSearchPanel, BoxLayout.X_AXIS));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        this.setLayout(new BorderLayout(0, 11));
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
    }
    /**
     *
     * @param keyField
     * @param searchClass
     * @param scrollPane
     */
    private void doSearch(JTextComponent keyField, String searchClass, JScrollPane scrollPane) {
        try {
            this.searchClass = searchClass;
            search(keyField.getText().trim(), true);
            setFocus(keyField);
            scrollToTop(scrollPane);
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }
    /**
     *
     * @param e
     */
    private void doSort(MouseEvent e) {
        int viewColumn = table.getTableHeader().columnAtPoint(e.getPoint());
        int modelColumn = table.convertColumnIndexToModel(viewColumn);
        setSortBy(sortColumnNames[modelColumn]);
        String key = findField.getText().trim();
        search(key, true);
    }
    /**
     *
     * @param component
     */
    private void setFocus(final JComponent component) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    /**
     *
     * @param scrollPane
     */
    private void scrollToTop(JScrollPane scrollPane) {
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setValue(bar.getMinimum());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.ButtonGroup();
        topPanel = new javax.swing.JPanel();
        keyPanel = new javax.swing.JPanel();
        combinedSearchPanel = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        searchButtonPanel = new javax.swing.JPanel();
        prefixButton = new javax.swing.JButton();
        typeButton = new javax.swing.JButton();
        diseaseButton = new javax.swing.JButton();
        postfixButton = new javax.swing.JButton();
        findField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        scroller = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        topPanel.setName("topPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DiagnosisMasterPanel.class);
        keyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("masterSearch.text.keywordBorderTitle"))); // NOI18N
        keyPanel.setName("keyPanel"); // NOI18N

        combinedSearchPanel.setName("combinedSearchPanel"); // NOI18N

        searchField.setColumns(10);
        searchField.setName("searchField"); // NOI18N
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        searchField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                searchFieldPropertyChange(evt);
            }
        });

        jLabel2.setText("傷病名入力:");
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout combinedSearchPanelLayout = new javax.swing.GroupLayout(combinedSearchPanel);
        combinedSearchPanel.setLayout(combinedSearchPanelLayout);
        combinedSearchPanelLayout.setHorizontalGroup(
            combinedSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(combinedSearchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        combinedSearchPanelLayout.setVerticalGroup(
            combinedSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(combinedSearchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(combinedSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        searchButtonPanel.setName("searchButtonPanel"); // NOI18N

        prefixButton.setText("部位");
        prefixButton.setName("prefixButton"); // NOI18N
        prefixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefixButtonActionPerformed(evt);
            }
        });

        typeButton.setText("性質");
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

        postfixButton.setText("接尾");
        postfixButton.setName("postfixButton"); // NOI18N
        postfixButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postfixButtonActionPerformed(evt);
            }
        });

        findField.setColumns(10);
        findField.setName("findField"); // NOI18N
        findField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findFieldActionPerformed(evt);
            }
        });
        findField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                findFieldPropertyChange(evt);
            }
        });

        jLabel3.setText("部分検索:");
        jLabel3.setName("jLabel3"); // NOI18N

        javax.swing.GroupLayout searchButtonPanelLayout = new javax.swing.GroupLayout(searchButtonPanel);
        searchButtonPanel.setLayout(searchButtonPanelLayout);
        searchButtonPanelLayout.setHorizontalGroup(
            searchButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(findField, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(prefixButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(typeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(diseaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(postfixButton)
                .addContainerGap())
        );
        searchButtonPanelLayout.setVerticalGroup(
            searchButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchButtonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchButtonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(findField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prefixButton)
                    .addComponent(typeButton)
                    .addComponent(diseaseButton)
                    .addComponent(postfixButton)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout keyPanelLayout = new javax.swing.GroupLayout(keyPanel);
        keyPanel.setLayout(keyPanelLayout);
        keyPanelLayout.setHorizontalGroup(
            keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keyPanelLayout.createSequentialGroup()
                .addComponent(combinedSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        keyPanelLayout.setVerticalGroup(
            keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(keyPanelLayout.createSequentialGroup()
                .addGroup(keyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(combinedSearchPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keyPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(keyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                .addContainerGap())
        );

        scroller.setName("scroller"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "コード", "名称", "カナ", "ICD10", "有効期限"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        scroller.setViewportView(table);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     *
     * @param evt
     */
    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        sortable = false;
        doSearch(searchField, ClaimConst.SubTypeSet.NAMES.getName(), scroller);
    }//GEN-LAST:event_searchFieldActionPerformed
    /**
     *
     * @param evt
     */
    private void prefixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefixButtonActionPerformed
        sortable = true;
        doSearch(findField, ClaimConst.SubTypeSet.PREFIX.getName(), scroller);
    }//GEN-LAST:event_prefixButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void typeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeButtonActionPerformed
        sortable = true;
        doSearch(findField, ClaimConst.SubTypeSet.TYPE.getName(), scroller);
    }//GEN-LAST:event_typeButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void diseaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diseaseButtonActionPerformed
        sortable = true;
        String key = findField.getText().trim();
        if (!key.equals("")) {
            searchClass = "";
            search(key, true);
            setFocus(findField);
            scrollToTop(scroller);
        }
    }//GEN-LAST:event_diseaseButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void postfixButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postfixButtonActionPerformed
        sortable = true;
        doSearch(findField, ClaimConst.SubTypeSet.POSTFIX.getName(), scroller);
    }//GEN-LAST:event_postfixButtonActionPerformed
    /**
     *
     * @param evt
     */
    private void searchFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_searchFieldPropertyChange
        if (!searchField.getText().equals("")) {
            sortable = false;
            doSearch(searchField, ClaimConst.SubTypeSet.NAMES.getName(), scroller);
        }
    }//GEN-LAST:event_searchFieldPropertyChange
    /**
     *
     * @param evt
     */
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked

        if (evt.getClickCount() == 2) {
            int[] rows = table.getSelectedRows();
            for (int row : rows) {
                DiseaseEntry entry = (DiseaseEntry) tableModel.getObject(row);
                //        if (entry != null) {
                if (entry != null && (entry.useState(new GregorianCalendar()) != 2)) {//isInUse()は日付がNULL、Emptyで真を返してしまうので
                    findField.setText("");
                    MasterItem mItem = new MasterItem(0, entry);
                    mItem.setClaimDiseaseCode(mItem.getCode());
                    mItem.setMasterTableId(codeSystem);
                    final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
                    switch (dao.IsAutoCulcFromOrca(mItem.getCode())) {
                        case NORMAL:
                            setSelectedItem(mItem);
                            break;
                        case KENSA://検査項目か
                            setSelectedItem(mItem);
                            break;
                        case AUTO://自動算定項目か
                            if (JOptionPane.showConfirmDialog(null, "自動算定項目です。よろしいですか？", "確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                setSelectedItem(mItem);
                            }
                            break;
                        case EXPIRE://期限切れ

                        case NOCODE://コードなし

                        default:
                            setSelectedItem(mItem);
                    }
                }
                setFocus(findField);
            }
        }
    }//GEN-LAST:event_tableMouseClicked
    /**
     *
     * @param evt
     */
    private void findFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findFieldActionPerformed
        doSearch(findField, "", scroller);
    }//GEN-LAST:event_findFieldActionPerformed
    /**
     * 
     * @param evt
     */
    private void findFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_findFieldPropertyChange
    }//GEN-LAST:event_findFieldPropertyChange
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bg;
    private javax.swing.JPanel combinedSearchPanel;
    private javax.swing.JButton diseaseButton;
    private javax.swing.JTextField findField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel keyPanel;
    private javax.swing.JButton postfixButton;
    private javax.swing.JButton prefixButton;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JPanel searchButtonPanel;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable table;
    private javax.swing.JPanel topPanel;
    private javax.swing.JButton typeButton;
    // End of variables declaration//GEN-END:variables
    // protected void partSearch(final String text, final String subtype) {
    //     searchClass = subtype;
    //     search(text, true);
    // }
}
