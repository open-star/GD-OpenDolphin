/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.order;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import open.dolphin.client.GUIConst;
import open.dolphin.client.AutoKanjiListener;
import open.dolphin.client.AutoRomanListener;
import open.dolphin.client.IStampModelEditor;
import open.dolphin.client.SymptomsDialog;
import open.dolphin.project.GlobalSettings;
import open.dolphin.container.Pair;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.helper.NumericDocument;
import open.dolphin.infomodel.IStampInfo;
import open.dolphin.infomodel.InteractEntry;
import open.dolphin.infomodel.MasterItem;
import open.dolphin.infomodel.MedicineEntry;
import open.dolphin.infomodel.SsKijyoEntry;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.SinryoCode;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.InfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;
import open.dolphin.table.OddEvenRowRendererWithExpire;
import open.dolphin.table.ObjectReflectTableModel;

/**
 * マスタ項目選択パネル　MEMO:画面　リスナー
 *
 * @author
 */
public class ItemTablePanel extends JPanel implements IItemTablePanel {

    /**
     *
     */
    protected static final String DEFAULT_STAMP_NAME = "新規スタンプ";
    private static final String FROM_EDITOR_STAMP_NAME = "エディタから";
    private static final String DEFAULT_NUMBER = "1";
    private static final String[] COLUMN_NAMES = {"コード", "診療内容", "数 量", "単 位"};
    private static final String[] METHOD_NAMES = {"getCode", "getName", "getNumber", "getUnit"};
    private static final int[] COLUMN_WIDTH = {50, 200, 10, 10};
    private static final int NUM_ROWS = 0;
    private static final String TOOLTIP_DND = "ドラッグ & ドロップで順番を入れ替えることができます。";
    private static final int SRYCD_COLUMN = 0;
    // 数量カラムのインデックス
    private static final int NUMBER_COLUMN = 2;
    // CLAIM 関係
    private boolean findClaimClassCode; // 診療行為区分を診療行為アイテムから取得するとき true
    private String orderName;           // ドルフィンのオーダ履歴用の名前
    private String classCode;           // 診療行為区分 400,500,600 .. etc
    private String classCodeId;         // 診療行為区分定義のテーブルID == Claim007
    private String subclassCodeId;      // == Claim003
    private String entity;              //
    // GUI コンポーネント
    private ObjectReflectTableModel<Object> tableModel;
    private IStampModelEditor parent;
    private boolean validModel;
    private SetTableStateMgr stateMgr;

    /** Creates new form ItemTablePanel
     * @param parent
     */
    public ItemTablePanel(IStampModelEditor parent) {
        super();
        initComponents();
        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        this.parent = parent;
        // セットテーブルのモデルを生成する
        tableModel = new ObjectReflectTableModel<Object>(COLUMN_NAMES, NUM_ROWS, METHOD_NAMES, null) {
            // NUMBER_COLUMN を編集可能にする

            @Override
            public boolean isCellEditable(int row, int col) {
                String cd = (String) tableModel.getValueAt(row, SRYCD_COLUMN);
                if (SinryoCode.isComment(cd)) {
                    return false;
                }
                return col == NUMBER_COLUMN;
            }
            // NUMBER_COLUMN に値を設定する

            @Override
            public void setValueAt(Object o, int row, int col) {
                if (o != null) {
                    if (o instanceof String) {
                        if (((String) o).trim().equals("")) {
                            o = new String("0");
                        }
                    } else {
                        super.setValueAt(o, row, col);
                    }
                }

                // MasterItem に数量を設定する
                MasterItem mItem = (MasterItem) getObject(row);

                if (col == NUMBER_COLUMN && mItem != null) {
                    mItem.setNumber((String) o);
                    stateMgr.checkState();
                }
            }
        };

        // セットテーブルを生成する
        setTable.setModel(tableModel);
        setTable.setTransferHandler(new MasterItemTransferHandler()); // TransferHandler

        setTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 選択モード
        setTable.setRowSelectionAllowed(true);
        ListSelectionModel m = setTable.getSelectionModel();

        m.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    notifySelectedRow();
                }
            }
        });

        setTable.setToolTipText(TOOLTIP_DND);
        setTable.setDefaultRenderer(Object.class, new OddEvenRowRendererWithExpire());

        // カラム幅を設定する
        TableColumn column = null;
        if (COLUMN_WIDTH != null) {
            int len = COLUMN_WIDTH.length;
            for (int i = 0; i < len; i++) {
                column = setTable.getColumnModel().getColumn(i);
                column.setPreferredWidth(COLUMN_WIDTH[i]);
            }
        }

        // 数量カラムにセルエディタを設定する
        JTextField volumeEditor = new JTextField();

        volumeEditor.addFocusListener(AutoRomanListener.getInstance());

        volumeEditor.setDocument(new NumericDocument());
        column = setTable.getColumnModel().getColumn(NUMBER_COLUMN);
        DefaultCellEditor de = new DefaultCellEditor(volumeEditor);
        int ccts = GlobalVariables.getPreferences().getInt("order.table.clickCountToStart", 2);
        de.setClickCountToStart(ccts);
        column.setCellEditor(de);


        commentField.addFocusListener(AutoKanjiListener.getInstance());
        stampNameField.addFocusListener(AutoKanjiListener.getInstance());


        if (parent.getContext().getOkButton() != null) {
            bp.add(parent.getContext().getOkButton());
        }

        south.add(infoP);
        south.add(Box.createHorizontalGlue());
        south.add(bp);

        this.setPreferredSize(new Dimension(GUIConst.DEFAULT_EDITOR_WIDTH, GUIConst.DEFAULT_EDITOR_HEIGHT));

        // StateMgrを生成する
        stateMgr = new SetTableStateMgr(this, setTable, removeButton, clearButton, stampNameField);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        south = new javax.swing.JPanel();
        infoP = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        stampNameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        numberCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        commentField = new javax.swing.JTextField();
        bp = new javax.swing.JPanel();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        scroller = new javax.swing.JScrollPane();
        setTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout(5, 0));

        south.setName("south"); // NOI18N
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.X_AXIS));

        infoP.setName("infoP"); // NOI18N
        infoP.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("セット名");
        jLabel1.setName("jLabel1"); // NOI18N
        infoP.add(jLabel1);

        stampNameField.setBackground(new java.awt.Color(251, 239, 128));
        stampNameField.setColumns(10);
        stampNameField.setName("stampNameField"); // NOI18N
        infoP.add(stampNameField);

        jLabel2.setText("回 数");
        jLabel2.setName("jLabel2"); // NOI18N
        infoP.add(jLabel2);

        numberCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        numberCombo.setName("numberCombo"); // NOI18N
        infoP.add(numberCombo);

        jLabel3.setText("メ モ");
        jLabel3.setName("jLabel3"); // NOI18N
        infoP.add(jLabel3);

        commentField.setColumns(15);
        commentField.setName("commentField"); // NOI18N
        infoP.add(commentField);

        south.add(infoP);

        bp.setName("bp"); // NOI18N
        bp.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        editButton.setText("編集");
        editButton.setToolTipText("コメントコードの場合編集できます");
        editButton.setEnabled(false);
        editButton.setName("editButton"); // NOI18N
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        bp.add(editButton);

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/del_16.gif"))); // NOI18N
        removeButton.setToolTipText("選択したアイテムを削除します。");
        removeButton.setEnabled(false);
        removeButton.setName("removeButton"); // NOI18N
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        bp.add(removeButton);

        clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/remov_16.gif"))); // NOI18N
        clearButton.setToolTipText("セット内容をクリアします。");
        clearButton.setEnabled(false);
        clearButton.setName("clearButton"); // NOI18N
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        bp.add(clearButton);

        south.add(bp);

        add(south, java.awt.BorderLayout.SOUTH);

        scroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.setAutoscrolls(true);
        scroller.setName("scroller"); // NOI18N

        setTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        setTable.setName("setTable"); // NOI18N
        setTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                setTableMouseDragged(evt);
            }
        });
        scroller.setViewportView(setTable);

        add(scroller, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        removeSelectedItem();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        editSelectedItem();
    }//GEN-LAST:event_editButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void setTableMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setTableMouseDragged

        //TODO エディタから発行の場合だけ、ドロップで順番を入れ替えることが出来ない。
        //TODO エディタから発行の場合だけ、ドラッグで２回呼ばれるようだ。
        int ctrlMask = InputEvent.CTRL_DOWN_MASK;
        int action;
        if ((evt.getModifiersEx() & ctrlMask) == ctrlMask) {
            action = TransferHandler.COPY;
        } else {
            action = TransferHandler.MOVE;
        }

        JComponent c = (JComponent) evt.getSource();
        TransferHandler handler = c.getTransferHandler();
        handler.exportAsDrag(c, evt, action);        // TODO add your handling code here:
    }//GEN-LAST:event_setTableMouseDragged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bp;
    private javax.swing.JButton clearButton;
    private javax.swing.JTextField commentField;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel infoP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox numberCombo;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable setTable;
    private javax.swing.JPanel south;
    private javax.swing.JTextField stampNameField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setValidModel(boolean valid) {
        validModel = valid;
        parent.setValidModel(validModel);
    }

    @Override
    public void setOrderName(String val) {
        orderName = val;
    }

    /**
     *
     * @param val
     */
    @Override
    public void setClassCode(String val) {
        classCode = val;
    }

    /**
     *
     * @param val
     */
    @Override
    public void setClassCodeId(String val) {
        classCodeId = val;
    }

    @Override
    public void setSubClassCodeId(String val) {
        subclassCodeId = val;
    }

    /**
     *
     * @param b
     */
    @Override
    public void setFindClaimClassCode(boolean b) {
        findClaimClassCode = true;
    }

    // スタンプ名を設定する
    private void setStampName(IStampInfo moduleInfo) {
        String text = stampNameField.getText().trim();
        if (!text.equals("")) {
            moduleInfo.setStampName(text);
        } else {
            moduleInfo.setStampName(DEFAULT_STAMP_NAME);
        }
    }

    // 診療行為コードを取得する
    // 最初に見つかった手技の診療行為コードをCLAIMに設定する
    // Dolphin Project の決定事項
    private String getClassCodeFromItems(MasterItem mItem) {
        String result = null;
        if (findClaimClassCode && (mItem.getClassCode() == ClaimConst.SYUGI)) {
            if (mItem.getClaimClassCode() != null) {
                if (!mItem.getClaimClassCode().trim().isEmpty()) { // 注射の場合、点数集計先コードから新たに診療行為コードを生成する // Kirishima ver. より
                    if (mItem.getClaimClassCode().equals(ClaimConst.INJECTION_311)) {
                        result = ClaimConst.INJECTION_310;
                    } else if (mItem.getClaimClassCode().equals(ClaimConst.INJECTION_321)) {
                        result = ClaimConst.INJECTION_320;
                    } else if (mItem.getClaimClassCode().equals(ClaimConst.INJECTION_331)) {
                        result = ClaimConst.INJECTION_330;
                    } else {
                        result = mItem.getClaimClassCode();// 注射以外のケース
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * @param masterRecord
     * @return
     */
    private ClaimItem createClaimItem(MasterItem masterRecord) {
        ClaimItem result = new ClaimItem(masterRecord);
        if (masterRecord.getSstKijunCdSet() != null) {
            result.setSstKijunCdSet(masterRecord.getSstKijunCdSet());
        }
        result.setClassCode(String.valueOf(masterRecord.getClassCode()));    // 診療種別区分(手技/材料・薬剤の別) mItem が保持を設定する

        //    result.setClassCode(String.valueOf(masterRecord.getClaimClassCode()));  // 診療種別区分(手技/材料・薬剤の別) mItem が保持を設定する

        result.setClassCodeSystem(subclassCodeId); // == Claom003

        String number = masterRecord.getNumber();
        if (number != null) {
            number = number.trim();
            if (!number.isEmpty()) {
                result.setNumber(number);
                result.setUnit(masterRecord.getUnit());
                result.setNumberCode(getNumberCode(masterRecord.getClassCode()));
                result.setNumberCodeSystem(ClaimConst.NUMBER_CODE_ID);
            }
        }
        return result;
    }

    /**
     *
     * @param itemList
     * @param bundle
     */
    private void setBundleItem(java.util.List itemList, BundleDolphin bundle) {
        if (itemList != null) {
            boolean found = false;  // 診療行為があるかどうかのフラグ
            String jihiClassCode = null;
            for (Iterator iter = itemList.iterator(); iter.hasNext();) {
                MasterItem masterRecord = (MasterItem) iter.next();
                bundle.addClaimItem(createClaimItem(masterRecord));
                if (!found) {
                    String _classCode = getClassCodeFromItems(masterRecord);
                    if (_classCode != null) {
                        classCode = _classCode;
                        found = true;
                    }
                }
                if (SinryoCode.isJihiHikazei(masterRecord.getCode())) {
                    jihiClassCode = ClaimConst.JIHI_HIKAZEI;
                } else if (SinryoCode.isJihiKazei(masterRecord.getCode())) {
                    jihiClassCode = ClaimConst.JIHI_KAZEI;
                }
            }

            if ((classCode == null || classCode.trim().isEmpty()) && jihiClassCode != null) {
                classCode = jihiClassCode;
            }
        }
    }

    /**
     *
     * @param bundle
     */
    private void setBandle(BundleDolphin bundle) {
        // Dolphin Appli で使用するオーダ名称を設定する
        // StampHolder で使用される（タブ名に相当）
        bundle.setOrderName(orderName);
        setMemo(bundle);// バンドルメモ
        bundle.setBundleNumber((String) numberCombo.getSelectedItem());   // バンドル数
        bundle.setClassCode(classCode);   // 診療行為コード
        bundle.setClassCodeSystem(classCodeId); // Claim007 固定の値
        bundle.setClassName(MMLTable.getClaimClassCodeName(classCode)); // 上記テーブルで定義されている診療行為の名称
    }

    /**
     * 
     * @param bundle
     */
    private void setMemo(BundleDolphin bundle) {
        String memo = commentField.getText();        // バンドルメモ
        if (!memo.equals("")) {
            bundle.setMemo(memo);
        }
    }

    /**
     * エディタで編集したスタンプの値を返す。
     * @return スタンプ(ModuleMode = ModuleInfo + InfoModel)
     */
    @Override
    public Object getValue() {
        //  public ModuleModel getValue() {
        // 常に新規のモデルとして返す
        ModuleModel result = new ModuleModel();
        IStampInfo moduleInfo = result.getModuleInfo();
        moduleInfo.setEntity(entity);
        moduleInfo.setStampRole(IInfoModel.ROLE_P);
        setStampName(moduleInfo);

        BundleDolphin bundle = new BundleDolphin();    // BundleDolphin を生成する

        java.util.List itemList = tableModel.getObjectList();        // セットテーブルのマスターアイテムを取得する
        setBundleItem(itemList, bundle);
        setBandle(bundle);
        result.setModel((InfoModel) bundle);
        return (Object) result;
    }

    /**
     * 編集するスタンプの内容を表示する。
     * @param theStamp 編集するスタンプ、戻り値は常に新規スタンプである。
     */
    @Override
    public void setValue(Object theStamp) {
        //  public void setValue(ModuleModel theStamp) {
        //     Date now = new Date();
        // 連続して編集される場合があるのでテーブル内容等をクリアする
        clear();
        if (theStamp != null) // null であればリターンする
        {
            // 引数で渡された Stamp をキャストする
            ModuleModel target = (ModuleModel) theStamp;

            // Entityを保存する
            entity = target.getModuleInfo().getEntity();

            // Stamp 名と表示形式を設定する
            String stampName = target.getModuleInfo().getStampName();
            boolean serialized = target.getModuleInfo().isSerialized();

            // スタンプ名がエディタから発行の場合はデフォルトの名称にする
            if (!serialized && stampName.startsWith(FROM_EDITOR_STAMP_NAME)) {
                stampName = DEFAULT_STAMP_NAME;
            } else if (stampName.equals("")) {
                stampName = DEFAULT_STAMP_NAME;
            }
            stampNameField.setText(stampName);

            // Model を表示する
            BundleDolphin bundle = (BundleDolphin) target.getModel();
            if (bundle != null) {
                // 診療行為区分を保存
                classCode = bundle.getClassCode();
                ClaimItem[] items = bundle.getClaimItem();
                //   int count = items.length;
                for (int i = 0; i < items.length; i++) {
                    ClaimItem item = items[i];

                    String val = item.getClassCode();
                    int classCode = 0;
                    try {
                        classCode = Integer.parseInt(val);
                    } catch (Exception e) {
                        LogWriter.error(getClass(), e);
                    }

                    MasterItem mItem = new MasterItem(classCode, item);
                    // 手技・材料・薬品のフラグ

                    val = item.getNumber();
                    if (val != null && (!val.equals(""))) {
                        mItem.setNumber(val);
                        val = item.getUnit();
                        if (val != null) {
                            mItem.setUnit(val);
                        }
                    }

                    if (item.getSstKijunCdSet() != null) {
                        mItem.setSstKijunCdSet(item.getSstKijunCdSet());
                    }

                    String endDate = item.getEndDate();
                    if (endDate != null) {
                        if (!endDate.equals("99999999") && !endDate.equals("")) {
                            SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
                            List<MedicineEntry> collection = new ArrayList<MedicineEntry>();
                            dao.getAlternateEntry(item.getCode(), collection);
                            for (MedicineEntry updateEntry : collection) {
                                if (!mItem.getCode().equals(updateEntry.getCode())) {
                                    MasterItem updatedMasterItem = new MasterItem(Integer.parseInt(item.getClassCode()), updateEntry);
                                    updatedMasterItem.setUnit(updateEntry.getUnit());
                                    updatedMasterItem.setNumber("0");
                                    updatedMasterItem.setYkzKbn(item.getYkzKbn());
                                    tableModel.addRow(updatedMasterItem);
                                }
                            }
                        }
                    }
                    // Show item
                    tableModel.addRow(mItem);
                    scrollToBottom(scroller);
                }
                // Bundle Memo
                String memo = bundle.getMemo();
                if (memo != null) {
                    commentField.setText(memo);
                }

                String number = bundle.getBundleNumber();
                if (number != null && (!number.equals(""))) {
                    numberCombo.setSelectedItem(number);
                }
            }
        }
        // Stateを変更する
        stateMgr.checkState();
    }

    private List<Pair<InteractEntry, SsKijyoEntry>> checkSymptom(SqlMasterDao dao, String importCode) {

        List<Pair<InteractEntry, SsKijyoEntry>> result = new ArrayList<Pair<InteractEntry, SsKijyoEntry>>();
        List<String> onStampCode = new ArrayList<String>();
        for (Object item : tableModel.getObjectList()) {
            onStampCode.add(((MasterItem) item).getCode());
        }
        dao.getSsKijyoEntry(importCode, onStampCode, result);
        return result;
    }

    /**
     * マスターテーブルで選択されたアイテムの通知を受け、
     * セットテーブルへ追加する。
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        boolean hasError = false;
        if (prop.equals("selectedItemProp")) {
            MasterItem item = (MasterItem) e.getNewValue();
            String textVal = stampNameField.getText().trim();
            if (SinryoCode.isComment(item.getCode())) {
                CommentHelper ch = new CommentHelper();
                if (!ch.confirm(item)) {
                    hasError = true;
                }
            }
            // マスターアイテムを判別して自動設定を行う
      /*      if (item.getClassCode() == ClaimConst.SYUGI) {
            } else*/ if (item.getClassCode() == ClaimConst.YAKUZAI) {

                SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
                boolean isCancel = false;
                List<Pair<InteractEntry, SsKijyoEntry>> symptoms = checkSymptom(dao, item.getCode());
                if (symptoms.size() != 0) {
                    SymptomsDialog symptomDialog = new SymptomsDialog((JFrame) this.getRootPane().getParent(), true, dao, symptoms);
                    symptomDialog.setVisible(true);
                    isCancel = symptomDialog.IsCancel();
                    hasError = true;
                }
                if (!isCancel) {
                    String inputNum = "1";
                    if (item.getUnit() != null) {
                        String unit = item.getUnit();
                        if (unit.equals("錠")) {
                            inputNum = GlobalVariables.getPreferences().get("defaultZyozaiNum", "3");
                        } else if (unit.equals("ｇ")) {
                            inputNum = GlobalVariables.getPreferences().get("defaultSanyakuNum", "1.0");
                        } else if (unit.equals("ｍＬ")) {
                            inputNum = GlobalVariables.getPreferences().get("defaultMizuyakuNum", "1");
                        }
                    }
                    item.setNumber(inputNum);
                }

            } else if (item.getClassCode() == ClaimConst.ZAIRYO) {
                item.setNumber(DEFAULT_NUMBER);
            }
            if (textVal.equals("") || textVal.equals(DEFAULT_STAMP_NAME)) {
                stampNameField.setText(item.getName());
            }

            if (!hasError) {

                tableModel.addRow(item);
                scrollToBottom(scroller);
                stateMgr.checkState();
            }
        } else if (prop.equals(RadiologyMethodPanel.RADIOLOGY_MEYTHOD_PROP)) {
            String text = (String) e.getNewValue();
            commentField.setText(text);
        }
    }

    private void notifySelectedRow() {
        int index = setTable.getSelectedRow();
        MasterItem item = (MasterItem) tableModel.getObject(index);
        removeButton.setEnabled(item != null);
        String s = item != null ? item.getCode() : null;
        editButton.setEnabled(SinryoCode.isComment(s));
    }

    /**
     * Clear all items.
     */
    @Override
    public void clear() {
        tableModel.clear();
        stateMgr.checkState();
    }

    /**
     * Clear selected item row.
     */
    private void removeSelectedItem() {
        int row = setTable.getSelectedRow();
        if (tableModel.getObject(row) != null) {
            tableModel.deleteRow(row);
            stateMgr.checkState();

            //先頭のアイテム名がデフォルトのセット名になるので先頭のアイテムが削除された場合、セット名を変える
            if (tableModel.getObject(0) != null) {
                stampNameField.setText(((MasterItem) tableModel.getObject(0)).getName());
            } else {
                stampNameField.setText(DEFAULT_STAMP_NAME);
            }
        }
    }

    private void scrollToBottom(final JScrollPane pane) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
            }
        });
    }

    private void editSelectedItem() {
        int idx = setTable.getSelectedRow();
        MasterItem item = (MasterItem) tableModel.getObject(idx);
        if (item != null) {
            if (SinryoCode.isComment(item.getCode())) {
                CommentHelper ch = new CommentHelper();
                ch.confirm(item);
            }
            tableModel.fireTableDataChanged();
            stateMgr.checkState();
            //先頭のアイテム名がデフォルトのセット名になるので先頭のアイテムが削除された場合、セット名を変える
            if (tableModel.getObject(0) != null) {
                stampNameField.setText(((MasterItem) tableModel.getObject(0)).getName());
            } else {
                stampNameField.setText(DEFAULT_STAMP_NAME);
            }
        }
    }

    /**
     * Returns Claim004 Number Code 21 材料個数 when subclassCode = 1 11
     * 薬剤投与量（１回）when subclassCode = 2
     */
    private String getNumberCode(int subclassCode) {
        return (subclassCode == 1) ? ClaimConst.ZAIRYO_KOSU : ClaimConst.YAKUZAI_TOYORYO_1KAI; // 材料個数 : 薬剤投与量１回
    }
}
