/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InjectionMedicineMasterPanel.java
 *
 * Created on 2009/08/18, 19:39:09
 */
package open.dolphin.client.editor.injectionmedicine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import open.dolphin.client.MasterRenderer;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.infomodel.MasterItem;
import open.dolphin.infomodel.MedicineEntry;
import open.dolphin.log.LogWriter;
import open.dolphin.order.AbstractMasterPanel;
import open.dolphin.order.ClaimConst;
import open.dolphin.table.ObjectTableModel;

/**
 *　注射マスタ画面　MEMO:画面
 * @author
 */
public class InjectionMedicineMasterPanel extends AbstractMasterPanel {

    private static final long serialVersionUID = -2421788488403402829L;
    private static final String[] medicineColumns = {"コード", "名  称", "カ ナ", "単位", "点数識別", "点数/金額", "薬価基準", "開始年月日", "終了年月日"}; //GlobalVariables.getStringArray("masterSearch.medicine.columnNames");
    private static final String[] costFlags = {"廃", "金", "都", "", "", "", "", "減", "不"}; //GlobalVariables.getStringArray("masterSearch.medicine.costFlags");
    //   private static final String[] sortButtonNames = GlobalVariables.getStringArray("masterSearch.medicine.sortButtonNames");
    private static final String[] sortColumnNames = {"srycd", "name", "kananame", "taniname", "tensikibetu", "ten", "yakkakjncd", "yukostymd", "yukoedymd"}; //GlobalVariables.getStringArray("masterSearch.medicine.sortColumnNames");

    /** Creates new form InjectionMedicineMasterPanel
     * @param master 
     */
    public InjectionMedicineMasterPanel(String master) {
        super(master);
        setSearchClass(ClaimConst.MASTER_FLAG_INJECTION);
    }

    /**
     * 初期化する。
     */
    @Override
    protected void initialize() {

        initComponents();

        // Table Model を生成する
        tableModel = new ObjectTableModel(medicineColumns, START_NUM_ROWS) {

            @Override
            public Class getColumnClass(int col) {
                return MedicineEntry.class;
            }
        };

        table.setModel(tableModel);

        JTableHeader header = table.getTableHeader();
        header.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                doSort(e);
            }
        });

        // 列幅を設定する
        TableColumn column = null;
        int[] width = new int[]{50, 200, 200, 40, 30, 50, 50};
        int len = width.length;
        for (int i = 0; i < len; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width[i]);
        }

        // レンダラーを設定する
        MedicineMasterRenderer mr = new MedicineMasterRenderer();
        mr.setBeforStartColor(masterColors[0]);
        mr.setInUseColor(masterColors[1]);
        mr.setAfterEndColor(masterColors[2]);
        mr.setCostFlag(costFlags);
        table.setDefaultRenderer(MedicineEntry.class, mr);

        // Layout
        // Keyword
        key.setLayout(new FlowLayout(FlowLayout.LEFT, 7, 5));
        key.add(findLabel);
        key.add(new JLabel(masterTabNames[2] + ":"));
        key.add(keywordField);
        key.add(isForwordCheckBox);
        key.setBorder(BorderFactory.createTitledBorder(keywordBorderTitle));

        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        top.add(Box.createHorizontalGlue());

        // Command
        this.setLayout(new BorderLayout(0, 11));
        this.add(top, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
    }
    /**
     *
     * @param e
     */
    private void doSort(MouseEvent e) {

        int viewColumn = table.getTableHeader().columnAtPoint(e.getPoint());
        int modelColumn = table.convertColumnIndexToModel(viewColumn);
        setSortBy(sortColumnNames[modelColumn]);
        String _key = keywordField.getText().trim();
        search(_key, !isForwordCheckBox.isSelected());
    }

    /**
     * 医薬品マスタ Table のレンダラー MEMO:Component
     */
    protected final class MedicineMasterRenderer extends MasterRenderer {

        private static final long serialVersionUID = 8567079934909643686L;
        private final int CODE_COLUMN = 0;
        private final int NAME_COLUMN = 1;
        private final int KANA_COLUMN = 2;
        private final int UNIT_COLUMN = 3;
        private final int COST_FLAG_COLUMN = 4;
        private final int COST_COLUMN = 5;
        private final int JNCD_COLUMN = 6;
        private final int START_COLUMN = 7;
        private final int END_COLUMN = 8;
        private String[] costFlags;

        /**
         *
         */
        public MedicineMasterRenderer() {
        }

        /**
         *
         * @return
         */
        public String[] getCostFlag() {
            return costFlags;
        }

        /**
         *
         * @param val
         */
        public void setCostFlag(String[] val) {
            costFlags = val;
        }
    /**
     *
     * @param table
     * @param value
     * @param isSelected
     * @param isFocused
     * @param row
     * @param col
     * @return
     */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int col) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, col);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {

                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            JLabel label = (JLabel) c;

            if (value != null && value instanceof MedicineEntry) {

                MedicineEntry entry = (MedicineEntry) value;

                String startDate = entry.getStartDate();
                String endDate = entry.getEndDate();

                setColor(label, startDate, endDate);

                switch (col) {

                    case CODE_COLUMN:
                        label.setText(entry.getCode());
                        break;

                    case NAME_COLUMN:
                        label.setText(entry.getName());
                        break;

                    case KANA_COLUMN:
                        label.setText(entry.getKana());
                        break;

                    case UNIT_COLUMN:
                        label.setText(entry.getUnit());
                        break;

                    case COST_FLAG_COLUMN:
                        try {
                            int index = Integer.parseInt(entry.getCostFlag());
                            label.setText(costFlags[index]);
                        } catch (Exception e) {
                            label.setText("");
                        }
                        break;

                    case COST_COLUMN:
                        label.setText(entry.getCost());
                        break;

                    case JNCD_COLUMN:
                        label.setText(entry.getJNCD());
                        break;

                    case START_COLUMN:
                        if (startDate.startsWith("0")) {
                            label.setText("");
                        } else {
                            label.setText(startDate);
                        }
                        break;

                    case END_COLUMN:
                        if (endDate.startsWith("9")) {
                            label.setText("");
                        } else {
                            label.setText(endDate);
                        }
                        break;
                default: LogWriter.fatal(getClass(), "case default");
                }

            } else {
                label.setBackground(Color.white);
                label.setText(value == null ? "" : value.toString());
            }
            return c;
        }
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
        top = new javax.swing.JPanel();
        key = new javax.swing.JPanel();

        scroller.setName("scroller"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
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
        table.setName("table"); // NOI18N
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        scroller.setViewportView(table);

        top.setName("top"); // NOI18N

        key.setName("key"); // NOI18N

        javax.swing.GroupLayout keyLayout = new javax.swing.GroupLayout(key);
        key.setLayout(keyLayout);
        keyLayout.setHorizontalGroup(
            keyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
        );
        keyLayout.setVerticalGroup(
            keyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout topLayout = new javax.swing.GroupLayout(top);
        top.setLayout(topLayout);
        topLayout.setHorizontalGroup(
            topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(key, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        topLayout.setVerticalGroup(
            topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * 
     * @param evt
     */
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked

        if (evt.getClickCount() == 2) {
            int row = table.getSelectedRow();
            MedicineEntry o = (MedicineEntry) tableModel.getObject(row);
            // if (o != null && o.isInUse()) {
            if (o != null && (o.useState(new GregorianCalendar()) != 2)) {//isInUse()は日付がNULL、Emptyで真を返してしまうので
                MasterItem mItem = new MasterItem(2, o);
                mItem.setUnit(o.getUnit());
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
        }
    }//GEN-LAST:event_tableMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel key;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable table;
    private javax.swing.JPanel top;
    // End of variables declaration//GEN-END:variables
}
