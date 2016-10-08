/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TreatmentMasterPanel.java
 *
 * Created on 2009/08/18, 19:26:06
 */
package open.dolphin.client.editor.treatment;

import open.dolphin.order.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import open.dolphin.project.GlobalConstants;
import open.dolphin.client.MasterRenderer;
import open.dolphin.client.TaskTimerMonitor;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.infomodel.MasterItem;
import open.dolphin.infomodel.TreatmentEntry;
import open.dolphin.log.LogWriter;
import open.dolphin.table.ObjectTableModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

/**
 *　診療行為マスタ　MEMO:画面
 * @author
 */
public class TreatmentMasterPanel extends AbstractMasterPanel {

    private static final long serialVersionUID = -4833490720433639368L;
    private static final String[] treatmentColumns = {"コード", "名  称", "正式名称", "点数識別", "点数/金額", "入外", "社老", "病診", "開始年月日", "終了年月日"}; //GlobalVariables.getStringArray("masterSearch.treatment.columnNames");
    private static final String[] treatmentCostFlags = {"廃", "金", "", "+点", "都", "%加", "%減", "減", "-点"}; //GlobalVariables.getStringArray("masterSearch.treatment.costFlags");
    private static final String[] inOutFlags = {"入外", "入", "外"}; //GlobalVariables.getStringArray("masterSearch.treatment.inOutFlags");
    private static final String[] hospClinicFlags = {"病診", "病", "診"}; //GlobalVariables.getStringArray("masterSearch.treatment.hospitalClinicFlags");
    private static final String[] oldFlags = {"社老", "社", "老"}; //GlobalVariables.getStringArray("masterSearch.treatment.oldFlags");
    //   private static final String[] sortButtonNames = GlobalVariables.getStringArray("masterSearch.treatment.sortButtonNames");
    private static final String[] sortColumnNames = {"srycd", "name", "formalname", "tensikibetu", "ten", "nyugaitekkbn", "routekkbn", "srysyukbn", "yukostymd", "yukoedymd"}; //GlobalVariables.getStringArray("masterSearch.treatment.sortColumnNames");
    /** カテゴリ検索ボタン */
    private JButton categoryButton;
    /** 撮影部位検索ボタン */
    private JButton radLOcationButton;

    //   private boolean radiology;
    /** Creates new form TreatmentMasterPanel
     * @param master
     */
    public TreatmentMasterPanel(String master) {
        super(master);
    }

    /**
     * 初期化する。
     */
    @Override
    protected void initialize() {

        initComponents();

        //
        // カテゴリ全検索ボタンを生成する
        //
        categoryButton = new JButton("全検索");
        categoryButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getByClaimClass();
            }
        });

        // 画像診断部位検索ボタンを生成する
        radLOcationButton = new JButton("画像診断部位");
        radLOcationButton.setEnabled(false);
        radLOcationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                getRadLocation();
            }
        });

        // TableModel を生成する
        tableModel = new ObjectTableModel(treatmentColumns, START_NUM_ROWS) {

            private static final long serialVersionUID = 8084360322119845887L;

            @Override
            public Class getColumnClass(int col) {
                return TreatmentEntry.class;
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
        int[] width = new int[]{50, 150, 150, 30, 50, 30, 30, 30, 50};
        int len = width.length;
        for (int i = 0; i < len; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width[i]);
        }

        // レンダラを生成し設定する
        TreatmentMasterRenderer tr = new TreatmentMasterRenderer();
        tr.setBeforStartColor(masterColors[0]);
        tr.setInUseColor(masterColors[1]);
        tr.setAfterEndColor(masterColors[2]);
        tr.setCostFlag(treatmentCostFlags);
        tr.setInOutFlag(inOutFlags);
        tr.setOldFlag(oldFlags);
        tr.setHospitalClinicFlag(hospClinicFlags);
        table.setDefaultRenderer(TreatmentEntry.class, tr);

        // レイアウトする

        key.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        key.add(findLabel);
        key.add(new JLabel(masterTabNames[4] + ":"));
        key.add(keywordField);
        key.add(isForwordCheckBox);
        key.setBorder(BorderFactory.createTitledBorder(keywordBorderTitle));

        category.setLayout(new BoxLayout(category, BoxLayout.X_AXIS));
        category.add(categoryButton);
        category.add(Box.createHorizontalStrut(5));
        category.add(radLOcationButton);
        category.setBorder(BorderFactory.createTitledBorder("カテゴリ"));

        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        top.add(Box.createHorizontalStrut(7));

        this.setLayout(new BorderLayout(0, 11));
        this.add(top, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
    }

    /**
     * 検索する診療行為コードの範囲を設定する。
     * @param searchClass
     */
    @Override
    public void setSearchClass(String searchClass) {

        this.searchClass = searchClass;

        if (this.searchClass == null) {
            categoryButton.setEnabled(false);
        } else {
            categoryButton.setEnabled(true);
        }
    }

    /**
     * 撮影部位検索ボタンを enabled にする。
     * @param enabled
     */
    public void setRadLocationEnabled(boolean enabled) {
//        radiology = enabled;
        radLOcationButton.setEnabled(enabled);
    }

    /**
     * 診療行為コードで点数マスタを検索する。
     */
    private void getByClaimClass() {

        final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");

        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();

        Task task = new Task<List<TreatmentEntry>, Void>(app) {

            @Override
            protected List<TreatmentEntry> doInBackground() throws Exception {
                List<TreatmentEntry> result = dao.getByClaimClassFromOrca(master, searchClass, sortBy, order);
                return result;
            }

            @Override
            protected void succeeded(List<TreatmentEntry> result) {
                processResult(dao.isNoError(), result, dao.getErrorMessage());
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
        String message = "診療行為検索";
        String note = searchClass + "を検索しています...";
        Component c = SwingUtilities.getWindowAncestor(this);
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, note, 200, 60 * 1000);
        taskMonitor.addPropertyChangeListener(w);

        appCtx.getTaskService().execute(task);
    }

    /**
     * 撮影部位を検索する。
     */
    private void getRadLocation() {

        // DAO を生成する
        final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");

        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();

        Task task = new Task<List<TreatmentEntry>, Void>(app) {

            @Override
            protected List<TreatmentEntry> doInBackground() throws Exception {
                List<TreatmentEntry> result = dao.getRadLocationFromOrca(master, sortBy, order);
                return result;
            }

            @Override
            protected void succeeded(List<TreatmentEntry> result) {
                processResult(dao.isNoError(), result, dao.getErrorMessage());
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
        String message = "画像診断部位検索";
        String note = searchClass + "検索しています...";
        Component c = SwingUtilities.getWindowAncestor(this);
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, note, 200, 60 * 1000);
        taskMonitor.addPropertyChangeListener(w);

        appCtx.getTaskService().execute(task);
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
     * 診療行為マスタ Table のレンダラー MEMO:Component
     */
    protected final class TreatmentMasterRenderer extends MasterRenderer {

        private static final long serialVersionUID = -23933027994436326L;
        private final int CODE_COLUMN = 0;
        private final int NAME_COLUMN = 1;
        private final int KANA_COLUMN = 2;
        private final int COST_FLAG_COLUMN = 3;
        private final int COST_COLUMN = 4;
        private final int INOUT_COLUMN = 5;
        private final int OLD_COLUMN = 6;
        private final int HOSP_CLINIC_COLUMN = 7;
        private final int START_COLUMN = 8;
        private final int END_COLUMN = 9;
        private String[] costFlags;
        private String[] inOutFlags;
        private String[] oldFlags;
        private String[] hospitalClinicFlags;

        /**
         *
         */
        public TreatmentMasterRenderer() {
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
         * @return
         */
        public String[] getInOutFlag() {
            return inOutFlags;
        }

        /**
         *
         * @param val
         */
        public void setInOutFlag(String[] val) {
            inOutFlags = val;
        }

        /**
         *
         * @return
         */
        public String[] getOldFlag() {
            return oldFlags;
        }

        /**
         *
         * @param val
         */
        public void setOldFlag(String[] val) {
            oldFlags = val;
        }

        /**
         *
         * @return
         */
        public String[] getHospitalClinicFlag() {
            return hospitalClinicFlags;
        }

        /**
         *
         * @param val
         */
        public void setHospitalClinicFlag(String[] val) {
            hospitalClinicFlags = val;
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
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean isFocused,
                int row, int col) {
            Component c = super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    isFocused,
                    row, col);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {

                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            JLabel label = (JLabel) c;

            if (value != null && value instanceof TreatmentEntry) {

                TreatmentEntry entry = (TreatmentEntry) value;
                String startDate = entry.getStartDate();
                String endDate = entry.getEndDate();
                setColor(label, startDate, endDate);
                String tmp = null;

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

                    case COST_FLAG_COLUMN:
                        tmp = entry.getCostFlag();
                        if (tmp != null) {
                            try {
                                int index = Integer.parseInt(tmp);
                                label.setText(costFlags[index]);
                            } catch (Exception e) {
                                LogWriter.error(this.getClass(), "", e);
                                label.setText("");
                            }
                        } else {
                            label.setText("");
                        }
                        break;

                    case COST_COLUMN:
                        label.setText(entry.getCost());
                        break;

                    case INOUT_COLUMN:
                        tmp = entry.getInOutFlag();
                        if (tmp != null) {
                            try {
                                int index = Integer.parseInt(tmp);
                                label.setText(inOutFlags[index]);
                            } catch (Exception e) {
                                label.setText("");
                                LogWriter.error(this.getClass(), "", e);
                            }
                        } else {
                            label.setText("");
                        }
                        break;

                    case OLD_COLUMN:
                        tmp = entry.getOldFlag();
                        if (tmp != null) {
                            try {
                                int index = Integer.parseInt(tmp);
                                label.setText(oldFlags[index]);
                            } catch (Exception e) {
                                label.setText("");
                                LogWriter.error(this.getClass(), "", e);
                            }
                        } else {
                            label.setText("");
                        }
                        break;

                    case HOSP_CLINIC_COLUMN:
                        tmp = entry.getHospitalClinicFlag();
                        if (tmp != null) {
                            try {
                                int index = Integer.parseInt(tmp);
                                label.setText(hospitalClinicFlags[index]);
                            } catch (Exception e) {
                                label.setText("");
                                LogWriter.error(this.getClass(), "", e);
                            }
                        } else {
                            label.setText("");
                        }
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
                //label.setBackground(Color.white);
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
        category = new javax.swing.JPanel();
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

        category.setName("category"); // NOI18N

        javax.swing.GroupLayout categoryLayout = new javax.swing.GroupLayout(category);
        category.setLayout(categoryLayout);
        categoryLayout.setHorizontalGroup(
            categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 174, Short.MAX_VALUE)
        );
        categoryLayout.setVerticalGroup(
            categoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        key.setName("key"); // NOI18N

        javax.swing.GroupLayout keyLayout = new javax.swing.GroupLayout(key);
        key.setLayout(keyLayout);
        keyLayout.setHorizontalGroup(
            keyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 297, Short.MAX_VALUE)
        );
        keyLayout.setVerticalGroup(
            keyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout topLayout = new javax.swing.GroupLayout(top);
        top.setLayout(topLayout);
        topLayout.setHorizontalGroup(
            topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(key, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        topLayout.setVerticalGroup(
            topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(category, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(top, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(top, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked

        if (evt.getClickCount() == 2) {
            int row = table.getSelectedRow();
            TreatmentEntry o = (TreatmentEntry) tableModel.getObject(row);
            //  if (o != null && o.isInUse()) {
            if (o != null && (o.useState(new GregorianCalendar()) != 2)) {//isInUse()は日付がNULL、Emptyで真を返してしまうので
                MasterItem mItem = new MasterItem(0, o);
                mItem.setClaimClassCode(o.getClaimClassCode());
                mItem.setSstKijunCdSet(o.getSstKijunCdSet());
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
    private javax.swing.JPanel category;
    private javax.swing.JPanel key;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable table;
    private javax.swing.JPanel top;
    // End of variables declaration//GEN-END:variables
}
