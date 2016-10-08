/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RadItemTablePanel.java
 *
 *　スタンプエディタ　画像診断
 * 
 * Created on 2010/03/10, 15:57:49
 */
package open.dolphin.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import open.dolphin.project.GlobalConstants;
import open.dolphin.client.GUIConst;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.InfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.table.ObjectReflectTableModel;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
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
import open.dolphin.project.GlobalVariables;
import open.dolphin.table.OddEvenRowRendererWithExpire;

/**
 *　スタンプエディタ　画像診断処方選択パネル　MEMO:画面　リスナー
 * @author
 */
public class RadItemTablePanel extends javax.swing.JPanel implements IItemTablePanel {

    private static final long serialVersionUID = 4365016271224659707L;
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
    private static final int NUMBER_COLUMN = 2;    // CLAIM 関係
    private boolean findClaimClassCode;     // 診療行為区分を診療行為アイテムから取得するとき true
    private String orderName;               // ドルフィンのオーダ履歴用の名前
    private String classCode;               // 診療行為区分
    private String classCodeId;             // 診療行為区分定義のテーブルID == Claim007
    private String subclassCodeId;          // == Claim003
    private String entity;    // GUI コンポーネント
    private ObjectReflectTableModel<MasterItem> tableModel;
    private RadiologyMethodPanel method;
    private IStampModelEditor parent;
    private boolean validModel;
    private RadSetTableStateMgr stateMgr;
    private Map<String, String[]> relationalCodeSet = new HashMap();

    /** Creates new form RadItemTablePanel
     * @param parent
     */
    public RadItemTablePanel(IStampModelEditor parent) {
        super();
        initComponents();

        /**
         * <TODO> ハードコード
         * これら4つの組み合わせは、数量を一致させる必要がある。
         * 注意：(1),(2) は 「単純撮影（撮影）」が同一
         * 注意：(4),(5) は 「単純間接撮影（撮影）」が同一
         * (1)170001910：単純撮影（撮影）
         *    170000410：単純撮影(イ)の写真診断
         * (2)170001910：単純撮影（撮影）
         *    170000510：単純撮影(ロ)の写真診断
         * (3)170002110：造影剤使用撮影（撮影）
         *    170000810：造影剤使用撮影の写真診断
         * (4)170002410：単純間接撮影（撮影）
         *    170000910：単純間接撮影（イ）の写真診断
         * (5)170002410：単純間接撮影（撮影）
         *    170001010：単純間接撮影（ロ）の写真診断
         */
        // (1),(2)
        String[] ary = {"170000410", "170000510"};
        String[] ary2 = {"170001910"};
        relationalCodeSet.put("170001910", ary);
        relationalCodeSet.put("170000410", ary2);
        relationalCodeSet.put("170000510", ary2);
        // (3)
        String[] ary3_1 = {"170000810"};
        String[] ary3_2 = {"170002110"};
        relationalCodeSet.put("170002110", ary3_1);
        relationalCodeSet.put("170000810", ary3_2);
        // (4),(5)
        String[] ary4_1 = {"170000910", "170001010"};
        String[] ary4_2 = {"170002410"};
        relationalCodeSet.put("170002410", ary4_1);
        relationalCodeSet.put("170000910", ary4_2);
        relationalCodeSet.put("170001010", ary4_2);

        this.parent = parent;
        //   setMyParent(parent);
        InitCustomComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        volumeEditor = new javax.swing.JTextField();
        center = new javax.swing.JPanel();
        scroller = new javax.swing.JScrollPane();
        setTable = new javax.swing.JTable();
        south = new javax.swing.JPanel();
        infoP = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        stampNameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        numberCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        commentField = new javax.swing.JTextField();
        bp = new javax.swing.JPanel();
        removeButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        volumeEditor.setText("jTextField1");
        volumeEditor.setName("volumeEditor"); // NOI18N

        setLayout(new java.awt.BorderLayout(5, 11));

        center.setName("center"); // NOI18N
        center.setLayout(new java.awt.BorderLayout());

        scroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
        setTable.setToolTipText("ドラッグ & ドロップで順番を入れ替えることができます。");
        setTable.setName("setTable"); // NOI18N
        setTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        setTable.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                setTableMouseDragged(evt);
            }
        });
        scroller.setViewportView(setTable);

        center.add(scroller, java.awt.BorderLayout.CENTER);

        add(center, java.awt.BorderLayout.CENTER);

        south.setName("south"); // NOI18N
        south.setLayout(new javax.swing.BoxLayout(south, javax.swing.BoxLayout.X_AXIS));

        infoP.setName("infoP"); // NOI18N
        infoP.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setText("セット名");
        jLabel1.setName("jLabel1"); // NOI18N
        infoP.add(jLabel1);

        stampNameField.setBackground(new java.awt.Color(251, 239, 128));
        stampNameField.setColumns(15);
        stampNameField.setName("stampNameField"); // NOI18N
        infoP.add(stampNameField);

        jLabel2.setText("回 数");
        jLabel2.setName("jLabel2"); // NOI18N
        infoP.add(jLabel2);

        numberCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
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
    }// </editor-fold>//GEN-END:initComponents

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        removeSelectedItem();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clear();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void setTableMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setTableMouseDragged
        //TODO エディタから発行の場合だけ、ドロップで順番を入れ替えることが出来ない。
        //TODO エディタから発行の場合だけ、ドラッグで２回呼ばれるようだ。
        int ctrlMask = InputEvent.CTRL_DOWN_MASK;
        int action = ((evt.getModifiersEx() & ctrlMask) == ctrlMask) ? TransferHandler.COPY : TransferHandler.MOVE;
        JComponent c = (JComponent) evt.getSource();
        TransferHandler handler = c.getTransferHandler();
        handler.exportAsDrag(c, evt, action);
    }//GEN-LAST:event_setTableMouseDragged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bp;
    private javax.swing.JPanel center;
    private javax.swing.JButton clearButton;
    private javax.swing.JTextField commentField;
    private javax.swing.JPanel infoP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JComboBox numberCombo;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable setTable;
    private javax.swing.JPanel south;
    private javax.swing.JTextField stampNameField;
    private javax.swing.JTextField volumeEditor;
    // End of variables declaration//GEN-END:variables

    /**
     * 
     */
    private void InitCustomComponents() {
        // セットテーブルのモデルを生成する
        tableModel = new ObjectReflectTableModel<MasterItem>(COLUMN_NAMES, NUM_ROWS, METHOD_NAMES, null) {

            private static final long serialVersionUID = 5162264518307934378L;
            // NUMBER_COLUMN を編集可能にする

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == NUMBER_COLUMN ? true : false;
            }
            // NUMBER_COLUMN に値を設定する

            @Override
            public void setValueAt(Object o, int row, int col) {

                if (o == null || ((String) o).trim().equals("")) {
                    o = new String("0");
                }

                // MasterItem に数量を設定する
                MasterItem mItem = (MasterItem) getObject(row);

                if (col == NUMBER_COLUMN && mItem != null) {
                    mItem.setNumber((String) o);
                    StringBuffer changed = new StringBuffer("");// relationalCodeSet に含まれるコードの場合、関係するアイテムの数量を一致させる。
                    if (relationalCodeSet.containsKey(mItem.getCode())) {
                        String[] vals = relationalCodeSet.get(mItem.getCode());
                        for (String s : vals) {
                            for (MasterItem obj : tableModel.getObjectList()) {
                                MasterItem m = (MasterItem) obj;
                                if (m.getCode().equals(s)) {
                                    m.setNumber(mItem.getNumber());
                                    changed.append(m.getName());
                                    changed.append(System.getProperty("line.separator"));
                                }
                            }
                        }
                    }
                    stateMgr.checkState();
                    if (!changed.toString().isEmpty()) {
                        setTable.repaint();
                        javax.swing.JOptionPane.showMessageDialog(null, "関係する数量も変更しました。" + System.getProperty("line.separator") + changed + "を確認ください。", "", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        };

        // セットテーブルを生成する
        setTable.setModel(tableModel);
        setTable.setTransferHandler(new MasterItemTransferHandler()); // TransferHandler
        ListSelectionModel m = setTable.getSelectionModel();
        m.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    notifySelectedRow();
                }
            }
        });
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
        int ccts = GlobalVariables.getPreferences().getInt("order.table.clickCountToStart", 2);

        // 数量カラムにセルエディタを設定する

        volumeEditor.addFocusListener(AutoRomanListener.getInstance());

        volumeEditor.setDocument(new NumericDocument());
        column = setTable.getColumnModel().getColumn(NUMBER_COLUMN);
        DefaultCellEditor volumeCellEditor = new DefaultCellEditor(volumeEditor);
        volumeCellEditor.setClickCountToStart(ccts);
        column.setCellEditor(volumeCellEditor);

        // コメントエリアを生成する
        commentField.addFocusListener(AutoKanjiListener.getInstance());
        stampNameField.addFocusListener(AutoKanjiListener.getInstance());

        // 画像診断メソッドのリストボックスとセットテーブルをリスナ関係にする
        method = new RadiologyMethodPanel();
        method.addPropertyChangeListener(RadiologyMethodPanel.RADIOLOGY_MEYTHOD_PROP, this);

        if (parent.getContext().getOkButton() != null) {
            bp.add(parent.getContext().getOkButton());
        }

        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));

        center.add(method, BorderLayout.WEST);
        setPreferredSize(new Dimension(GUIConst.DEFAULT_EDITOR_WIDTH, GUIConst.DEFAULT_EDITOR_HEIGHT));

        // StateMgrを生成する
        stateMgr = new RadSetTableStateMgr(this, setTable, removeButton, clearButton, stampNameField);
    }

    //  private boolean isValidModel() {
    //      return validModel;
    // }

    /**
     *
     * @param valid
     */
    @Override
    public void setValidModel(boolean valid) {
        validModel = valid;
        parent.setValidModel(validModel);
    }

    //   private String getOrderName() {
//        return orderName;
    //  }

    /**
     *
     * @param val
     */
    @Override
    public void setOrderName(String val) {
        orderName = val;
    }

    // private String getEntity() {
    //     return entity;
    //  }
    // private void setEntity(String val) {
    //     entity = val;
    //  }
    //   private String getClassCode() {
    //       return classCode;
    //   }

    /**
     *
     * @param val
     */
    @Override
    public void setClassCode(String val) {
        classCode = val;
    }

    //   private String getClassCodeId() {
    //       return classCodeId;
    //   }

    /**
     *
     * @param val
     */
    @Override
    public void setClassCodeId(String val) {
        classCodeId = val;
    }

    //   private String getSubClassCodeId() {
    //       return subclassCodeId;
    //   }

    /**
     *
     * @param val
     */
    @Override
    public void setSubClassCodeId(String val) {
        subclassCodeId = val;
    }

    //   private IStampModelEditor getMyParent() {
    //     return parent;
    //  }
    /// private void setMyParent(IStampModelEditor parent) {
    //     this.parent = parent;
    // }
    //   private String getBundleNumber() {
    //       return (String) numberCombo.getSelectedItem();
    //  }
    //   private void setBundleNumber(String val) {
    //       numberCombo.setSelectedItem(val);
    //   }
    //  private boolean isFindClaimClassCode() {
    //      return findClaimClassCode;
    // }

    /**
     *
     * @param b
     */
    @Override
    public void setFindClaimClassCode(boolean b) {
        findClaimClassCode = true;
    }

    /**
     * スタンプ名を設定する
     * @param moduleInfo
     */
    private void setStampName(IStampInfo moduleInfo) {
        String text = stampNameField.getText().trim();
        if (!text.equals("")) {
            moduleInfo.setStampName(text);
        } else {
            moduleInfo.setStampName(DEFAULT_STAMP_NAME);
        }
    }

    /**
     *  診療行為コードを取得する
     *  最初に見つかった手技の診療行為コードをCLAIMに設定する
     *  Dolphin Project の決定事項
     * @param mItem
     * @return
     */
    private String getClassCodeFromItems(MasterItem mItem) {
        String result = null;
        if (findClaimClassCode && (mItem.getClassCode() == 0)) {
            if (mItem.getClaimClassCode() != null) {
                if (!mItem.getClaimClassCode().trim().isEmpty()) {
                    // 注射の場合、点数集計先コードから新たに診療行為コードを生成する
                    // Kirishima ver. より
                    if (mItem.getClaimClassCode().equals(ClaimConst.INJECTION_311)) {
                        classCode = ClaimConst.INJECTION_310;
                    } else if (mItem.getClaimClassCode().equals(ClaimConst.INJECTION_321)) {
                        classCode = ClaimConst.INJECTION_320;
                    } else if (mItem.getClaimClassCode().equals(ClaimConst.INJECTION_331)) {
                        classCode = ClaimConst.INJECTION_330;
                    } else {
                        // 注射以外のケース
                        classCode = mItem.getClaimClassCode();
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
        result.setClassCode(String.valueOf(masterRecord.getClassCode()));
        result.setClassCodeSystem(subclassCodeId); // == Claom003
        // 材料もしくは薬剤の時、数量と単位を取得する
        String number = masterRecord.getNumber();
        if (number != null) {
            number = number.trim();
            if (!number.equals("")) {
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
            boolean found = false;
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
            }
        }
    }

    /**
     *
     * @param bundle
     */
    private void setBandle(BundleDolphin bundle) {
        bundle.setOrderName(orderName); // StampHolder で使用される
        setMemo(bundle);// バンドルメモ
        bundle.setBundleNumber((String) numberCombo.getSelectedItem());
        bundle.setClassCode(classCode); // 診療行為コード
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
        // 常に新規のモデルとして返す
        ModuleModel result = new ModuleModel();
        IStampInfo moduleInfo = result.getModuleInfo();
        moduleInfo.setEntity(entity);
        moduleInfo.setStampRole("p");
        setStampName(moduleInfo);

        BundleDolphin bundle = new BundleDolphin(); // BundleDolphin を生成する

        java.util.List itemList = tableModel.getObjectList(); // セットテーブルのマスターアイテムを取得する
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
        Date now = new Date();
        // 連続して編集される場合があるのでテーブル内容等をクリアする
        clear();

        // null であればリターンする
        if (theStamp == null) {
            // Stateを変更する
            stateMgr.checkState();
            return;
        }

        // 引数で渡された Stamp をキャストする
        ModuleModel target = (ModuleModel) theStamp;

        // Entityを保存する
        this.entity = target.getModuleInfo().getEntity();

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
        if (bundle == null) {
            return;
        }

        // 診療行為区分を保存
        classCode = bundle.getClassCode();

        ClaimItem[] items = bundle.getClaimItem();
        int count = items.length;

        for (int i = 0; i < count; i++) {

            ClaimItem item = items[i];
            String val = item.getClassCode();
            MasterItem mItem = new MasterItem(Integer.parseInt(val), item);

            // 手技・材料・薬品のフラグ
            val = item.getNumber();
            if (val != null) {
                val = val.trim();
                if (!val.equals("")) {
                    //    val = ZenkakuUtils.toHankuNumber(val);
                    mItem.setNumber(val);
                }
                val = item.getUnit();
                if (val != null) {
                    mItem.setUnit(val);
                }
            }
            if (item.getEndDate() != null && !item.getEndDate().equals("99999999") && !item.getEndDate().equals("")) {
                //    if (updates != null) {
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
            //   number = ZenkakuUtils.toHankuNumber(number);
            numberCombo.setSelectedItem(number);
        }

        // Stateを変更する
        stateMgr.checkState();
    }

    /**
     *
     * @param dao
     * @param importCode
     * @return
     */
    private List<Pair<InteractEntry, SsKijyoEntry>> checkSymptom(SqlMasterDao dao, String importCode) {

        List<Pair<InteractEntry, SsKijyoEntry>> result = new ArrayList<Pair<InteractEntry, SsKijyoEntry>>();
        List<String> onStampCode = new ArrayList<String>();
        for (MasterItem item : tableModel.getObjectList()) {
            onStampCode.add(item.getCode());
        }
        dao.getSsKijyoEntry(importCode, onStampCode, result);
        return result;
    }

    /**
     *
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals("selectedItemProp")) {
            MasterItem item = (MasterItem) e.getNewValue();
            String textVal = stampNameField.getText().trim();
            // マスターアイテムを判別して自動設定を行う
            switch (item.getClassCode()) {
                case ClaimConst.SYUGI:
                    // 材料及び薬剤の場合は数量1を設定する
                    //item.setNumber(DEFAULT_NUMBER);
                    if (textVal.equals("") || textVal.equals(DEFAULT_STAMP_NAME)) {
                        // 手技の場合はスタンプ名フィールドに名前を設定する
                        stampNameField.setText(item.getName());
                    }
                    tableModel.addRow(item);
                    stateMgr.checkState();
                    scrollToBottom(scroller);
                    break;
                case ClaimConst.YAKUZAI:
                    SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
                    boolean isCancel = false;
                    List<Pair<InteractEntry, SsKijyoEntry>> symptoms = checkSymptom(dao, item.getCode());
                    if (symptoms.size() != 0) {
                        SymptomsDialog symptomDialog = new SymptomsDialog((JFrame) this.getRootPane().getParent(), true, dao, symptoms);
                        symptomDialog.setVisible(true);
                        isCancel = symptomDialog.IsCancel();
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
                        tableModel.addRow(item);
                        stateMgr.checkState();
                        scrollToBottom(scroller);
                    }
                    break;
                case ClaimConst.ZAIRYO:
                    item.setNumber(DEFAULT_NUMBER);
                    tableModel.addRow(item);
                    stateMgr.checkState();
                    scrollToBottom(scroller);
                    break;
                default:
                    break;
            }
        } else if (prop.equals(RadiologyMethodPanel.RADIOLOGY_MEYTHOD_PROP)) {
            String text = (String) e.getNewValue();
            commentField.setText(text);
        }
    }

    /**
     *
     */
    private void notifySelectedRow() {
        int index = setTable.getSelectedRow();
        boolean b = tableModel.getObject(index) != null ? true : false;
        removeButton.setEnabled(b);
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
        }
    }

    /**
     * Returns Claim004 Number Code 21 材料個数 when subclassCode = 1 11
     * 薬剤投与量（１回）when subclassCode = 2
     */
    private String getNumberCode(int subclassCode) {
        return (subclassCode == 1) ? ClaimConst.ZAIRYO_KOSU : ClaimConst.YAKUZAI_TOYORYO_1KAI; // 材料個数 : 薬剤投与量１回
    }

    /**
     * MEMO: unused?
     */
    private ImageIcon createImageIcon(String name) {
        return GlobalConstants.getImageIcon(name);
    }

    /**
     *
     * @param pane
     */
    private void scrollToBottom(final JScrollPane pane) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
            }
        });
    }
}
