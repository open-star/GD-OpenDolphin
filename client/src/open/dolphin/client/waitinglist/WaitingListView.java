/*
 * WaitingListView.java
 *
 * Created on 2007/11/25, 15:42
 */
package open.dolphin.client.waitinglist;

import open.dolphin.client.IChart.state;
import open.dolphin.container.NameValuePair;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import open.dolphin.client.ChartWindow;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IMainComponent;
import open.dolphin.client.IMainWindow;
import open.dolphin.delegater.remote.RemotePVTDelegater;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.client.Dolphin.MenuMediator;
import open.dolphin.client.IChart;
import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalSettings;
import open.dolphin.helper.FutureNetII.Patient;
import open.dolphin.helper.FixedLengthFormat.FileWriter;
import open.dolphin.log.LogWriter;
import open.dolphin.table.ObjectReflectTableModel;

/**
 * 受付リストパネル　MEMO:画面
 * @author  kazm
 */
public class WaitingListView extends javax.swing.JPanel implements IMainComponent {

    private String name;
    private String icon;
    private IMainWindow context;
    private static final String NAME = "受付リスト";
    private static final ImageIcon FLAG_ICON = GlobalConstants.getImageIcon("flag_16.gif");    // 診察終了アイコン
    private static final ImageIcon OPEN_ICON = GlobalConstants.getImageIcon("open_16.gif");    // カルテオープンアイコン
    private static final Color FLAG_COLOR = GlobalConstants.getColor("waitingList.color.flag");        // 診察終了アイコン MEMO: unused?
    private static final Color OPEN_COLOR = GlobalConstants.getColor("waitingList.color.open");       // カルテオープンアイコン MEMO: unused?
    private static final Color MALE_COLOR = GlobalConstants.getColor("waitingList.color.male");    // JTableレンダラ用の男性カラー
    private static final Color FEMALE_COLOR = GlobalConstants.getColor("waitingList.color.female");    // JTableレンダラ用の女性カラー
    private static final Color ODD_COLOR = GlobalConstants.getColor("color.odd");    // JTableレンダラ用の奇数カラー
    private static final Color EVEN_COLOR = GlobalConstants.getColor("color.even");    // JTableレンダラ用の偶数カラー
    private static final Color CANCEL_PVT_COLOR = GlobalConstants.getColor("waitingList.color.pvtCancel");    // 受付キャンセルカラー
    private NameValuePair[] intervalObjects = GlobalConstants.getNameValuePair("waitingList.interval");    // 来院情報のチェック間隔オブジェクト
    // デフォルトのチェック間隔
    private int CHECK_INTERVAL = 30; // デフォルト値
    private int STATE_COLUMN = 7;    // 来院情報テーブルのステータスカラム
    private final int AGE_COLUMN = 4;    // 年齢表示カラム
    private final String[] AGE_METHOD = new String[]{"getPatientAgeBirthday", "getPatientBirthday"};    // 年齢生年月日メソッド
    private ObjectReflectTableModel<PatientVisitModel> pvtTableModel;
    private Preferences preferences;    // Preference
    private boolean sexRenderer;    // 性別レンダラフラグ
    private boolean ageDisplay;    // 年齢表示
    private Date operationDate;    // 運転日
    private Date checkedTime;    // 受付 DB をチェックした Date
    private int pvtCount;    // 来院患者数
    private int checkInterval;    // チェック間隔
    private PatientVisitModel selectedPvt;    // 選択されている患者情報
    private int saveSelectedIndex;
    private ScheduledFuture timerHandler;
    private RunnablePvtChecker pvtChecker;

    /** 
     * Creates new form WaitingListView
     */
    public WaitingListView() {
        initComponents();
        jScrollPane1.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        this.name = NAME;
        pvtChecker = new RunnablePvtChecker();
    }

    /**
     * 患者来院情報を追加する。
     * @param item 患者来院情報
     */
    private synchronized void addPvtList(PatientVisitModel item) {
        pvtTableModel.addRow(item);
    }

    /**
     * 患者来院情報の一覧を返す
     * @return
     */
    private List<PatientVisitModel> getWaitingList() {
        return (List<PatientVisitModel>) new RemotePVTDelegater().getPvt(getSearchDateAsString(new Date()), 0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new RowTipsTable();
        kutuBtn = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        checkedTimeLbl = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        countLbl = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        intervalLbl = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        dateLbl = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

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
        jScrollPane1.setViewportView(table);

        kutuBtn.setText("受付リストの更新");
        kutuBtn.setToolTipText("来院者のチェックを行います。");

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        checkedTimeLbl.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        checkedTimeLbl.setText("jLabel3");
        jPanel1.add(checkedTimeLbl);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setMaximumSize(new java.awt.Dimension(2, 10));
        jPanel2.setMinimumSize(new java.awt.Dimension(2, 10));
        jPanel2.setPreferredSize(new java.awt.Dimension(2, 10));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 4, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2);

        countLbl.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        countLbl.setText("jLabel5");
        jPanel1.add(countLbl);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 0));

        intervalLbl.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        intervalLbl.setText("jLabel3");
        jPanel3.add(intervalLbl);

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setMaximumSize(new java.awt.Dimension(2, 10));
        jPanel4.setMinimumSize(new java.awt.Dimension(2, 10));
        jPanel4.setPreferredSize(new java.awt.Dimension(2, 10));

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 4, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel4);

        dateLbl.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        dateLbl.setText("jLabel5");
        jPanel3.add(dateLbl);

        categoryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "全て" }));
        categoryComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryComboBoxActionPerformed(evt);
            }
        });

        subjectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "全て" }));
        subjectComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setText("診療科");

        jLabel4.setText("内容");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/flag_16.gif"))); // NOI18N
        jLabel2.setText("診察終了");
        jLabel2.setFocusable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/open_16.gif"))); // NOI18N
        jLabel1.setText("診療中");
        jLabel1.setFocusable(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(kutuBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 195, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 214, Short.MAX_VALUE)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(categoryComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(subjectComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 111, Short.MAX_VALUE)
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(categoryComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4)
                    .add(subjectComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(kutuBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param control
     * @param item
     * @return
     */
    private boolean checkItems(javax.swing.JComboBox control, String item) {
        for (int index = 0; index < control.getItemCount(); index++) {
            if (control.getItemAt(index).equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     */
    private synchronized void clearPvtList() {
        pvtTableModel.clear();
    }

    /**
     *
     */
    private void filterWaitingList() {
        String medicalSchool = (String) categoryComboBox.getSelectedItem();
        String subject = (String) subjectComboBox.getSelectedItem();

        if (medicalSchool != null && subject != null) {
            clearPvtList();
            List<PatientVisitModel> waitingList = getWaitingList();
            for (PatientVisitModel item : waitingList) {
                if (item.getDepartmentName().equals(medicalSchool) || (medicalSchool.equals("全て"))) {
                    if (item.getCategory().equals(subject) || (subject.equals("全て"))) {
                        addPvtList(item);
                    }
                }
                String _medicalSchool = item.getDepartmentName();
                if (!checkItems(categoryComboBox, _medicalSchool)) {
                    categoryComboBox.addItem(_medicalSchool);
                }

                String _subject = item.getCategory();
                if (!checkItems(subjectComboBox, _subject)) {
                    subjectComboBox.addItem(_subject);
                }
            }
        }
    }

    /**
     *
     * @param evt
     */
    private void categoryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryComboBoxActionPerformed
        filterWaitingList();
        //選択条件プルダウンの値を初期値として保存
        String selected = (String) categoryComboBox.getSelectedItem();
        preferences.put("WaitingListView_medicalSchoolComboBox_Initial", selected);
    }//GEN-LAST:event_categoryComboBoxActionPerformed
    /**
     *
     * @param evt
     */
    private void subjectComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectComboBoxActionPerformed
        filterWaitingList();
        //選択条件プルダウンの値を初期値として保存
        String selected = (String) categoryComboBox.getSelectedItem();
        preferences.put("WaitingListView_subjectComboBox_Initial", selected);
    }//GEN-LAST:event_subjectComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JComboBox categoryComboBox = new javax.swing.JComboBox();
    private javax.swing.JLabel checkedTimeLbl;
    private javax.swing.JLabel countLbl;
    private javax.swing.JLabel dateLbl;
    private javax.swing.JLabel intervalLbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton kutuBtn;
    private final javax.swing.JComboBox subjectComboBox = new javax.swing.JComboBox();
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @param table
     * @return
     */
    private int getSelectedRow(JTable table) {
        int result = table.getSelectedRow();
        if (result >= 0 && result < table.getRowCount()) {
            result = table.convertRowIndexToModel(result);
        }
        return result;
    }

    /**
     * ロガー等を取得する。
     */
    private void setup() {
        preferences = Preferences.userNodeForPackage(this.getClass());
        sexRenderer = preferences.getBoolean("sexRenderer", false);
        ageDisplay = preferences.getBoolean("ageDisplay", true);
        checkInterval = preferences.getInt("checkInterval", CHECK_INTERVAL);
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {
        setup();
        initCustomComponents();
        connect();
        restartCheckTimer();
    }

    /**
     * メインウインドウのタブで受付リストに切り替わった時
     * コールされる。
     */
    @Override
    public void enter() {
        controlMenu();
    }

    /**
     * プログラムを終了する。
     */
    @Override
    public void stop() {
    }

    /**
     * 選択されている来院情報の患者オブジェクトを返す。
     * @return 患者オブジェクト
     */
    public PatientModel getPatinet() {
        return selectedPvt != null ? selectedPvt.getPatient() : null;
    }

    /**
     * 性別レンダラかどうかを返す。
     * @return 性別レンダラの時 true
     */
    public boolean isSexRenderer() {
        return sexRenderer;
    }

    /**
     *
     */
    private synchronized void pvtTableModelFireTableDataChanged() {
        pvtTableModel.fireTableDataChanged();
    }

    /**
     * レンダラをトグルで切り替える。
     */
    public synchronized void switchRenderere() {
        sexRenderer = !sexRenderer;
        preferences.putBoolean("sexRenderer", sexRenderer);
        if (table != null) {
            pvtTableModelFireTableDataChanged();
        }
    }

    /**
     * 年齢表示をオンオフする。
     */
    public synchronized void switchAgeDisplay() {
        ageDisplay = !ageDisplay;
        preferences.putBoolean("ageDisplay", ageDisplay);
        if (table != null) {
            String method = ageDisplay ? AGE_METHOD[0] : AGE_METHOD[1];
            pvtTableModel.setMethodName(method, AGE_COLUMN);
        }
    }

    /**
     * 来院情報を取得する日を設定する。
     * @param date 取得する日
     */
    public void setOperationDate(Date date) {
        operationDate = date;
        String formatStr = "yyyy-MM-dd (EE)";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr); // 2006-11-20(水)
        dateLbl.setText(sdf.format(operationDate));
    }

    /**
     * 来院情報をチェックした時刻を設定する。
     * @param date チェックした時刻
     */
    public void setCheckedTime(Date date) {
        checkedTime = date;
        String formatStr = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        checkedTimeLbl.setText(sdf.format(checkedTime));
    }

    /**
     * 来院情報のチェック間隔(Timer delay)を設定する。
     * @param interval チェック間隔 sec
     */
    public void setCheckInterval(int interval) {
        checkInterval = interval;
        String intervalSt = String.valueOf(checkInterval);
        for (NameValuePair pair : intervalObjects) {
            if (intervalSt.equals(pair.getValue())) {
                String text = "チェック間隔:";
                text += pair.getName();
                intervalLbl.setText(text);
                break;
            }
        }
    }

    /**
     * 来院数を設定する。
     * @param cnt 来院数
     */
    public void setPvtCount(int cnt) {
        pvtCount = cnt;
        String text = "来院数:";
        text += String.valueOf(pvtCount); // 来院数:20
        countLbl.setText(text);
    }

    /**
     * テーブル及び靴アイコンの enable/diable 制御を行う。
     * @param busy pvt 検索中は true
     */
    public void setBusy(boolean busy) {
        kutuBtn.setEnabled(!busy);
        categoryComboBox.setEnabled(!busy);
        subjectComboBox.setEnabled(!busy);
        if (busy) {
            getContext().block();
            saveSelectedIndex = getSelectedRow(table);
        } else {
            getContext().unblock();
            table.getSelectionModel().addSelectionInterval(saveSelectedIndex, saveSelectedIndex);
        }
    }

    /**
     * 選択されている来院情報を設定返す。
     * @return 選択されている来院情報
     */
    public PatientVisitModel getSelectedPvt() {
        return selectedPvt;
    }

    /**
     * 選択された来院情報を設定する。
     * @param selectedPvt 患者来院情報
     */
    public void setSelectedPvt(PatientVisitModel selectedPvt) {
        PatientVisitModel old = this.selectedPvt;//MEMO: unused?
        this.selectedPvt = selectedPvt;
        controlMenu();
    }

    /**
     * チェックタイマーをリスタートする。
     */
    public void restartCheckTimer() {
        if (timerHandler != null) {
            timerHandler.cancel(false);
        }
        ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();
        timerHandler = schedule.scheduleWithFixedDelay(pvtChecker, 0, checkInterval, TimeUnit.SECONDS);
    }

    /**
     * カルテオープンメニューを制御する。
     */
    private void controlMenu() {
        PatientVisitModel pvt = getSelectedPvt();
        boolean enabled = canOpen(pvt);
        getContext().enabledAction(GUIConst.ACTION_OPEN_KARTE, enabled);
    }

    /**
     * GUI コンポーネントを初期化しレアイアウトする。
     */
    private void initCustomComponents() {
        // 来院テーブル用のパラメータを取得する
        String[] columnNames = {"患者ID", "来院時間", "氏   名", "性別", "生年月日", "診療科", "予約", "状態", "内容", "メモ２"};
      //GlobalVariables.getStringArray("waitingList.columnNames");
        String[] methodNames = {"getPatientId", "getPvtDateTrimDate", "getPatientName", "getPatientGenderDesc", "getPatientAgeBirthday", "getDepartmentName", "getAppointment", "getState", "getCategory","getMemo2"};
      //GlobalVariables.getStringArray("waitingList.methodNames");
        Class[] classes = new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, Integer.class, String.class, String.class};
        int[] columnWidth = {80, 70, 110, 40, 170, 80, 40, 40, 70, 100};
//      String[] columnNames = {"患者ID", "来院時間", "氏   名", "性別", "生年月日", "診療科", "予約", "内容"}; //GlobalVariables.getStringArray("waitingList.columnNames");
//      String[] methodNames = {"getPatientId", "getPvtDateTrimDate", "getPatientName", "getPatientGenderDesc", "getPatientAgeBirthday", "getDepartmentName", "getAppointment", "getCategory"}; //GlobalVariables.getStringArray("waitingList.methodNames");
//      Class[] classes = new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class};
//      int[] columnWidth = {80, 60, 140, 40, 140, 50, 40, 80};

//      Class[] classes = GlobalConstants.getClassArray("waitingList.columnClasses");
//      int[] columnWidth = GlobalConstants.getIntArray("waitingList.columnWidth");
        int startNumRows = 0;
        int rowHeight = 18;

        // 年齢表示をしない場合はメソッドを変更する
        if (!ageDisplay) {
            methodNames[AGE_COLUMN] = AGE_METHOD[1];
        }

        pvtTableModel = new ObjectReflectTableModel<PatientVisitModel>(columnNames, startNumRows, methodNames, classes);
        table.setModel(pvtTableModel);//このテーブルのデータモデルを newModel に設定し、それに新しいデータモデルからのリスナー通知を登録します。


        table.setAutoCreateRowSorter(true);
    //  PatientVisitSorter sorter = new PatientVisitSorter();
    //  table.setRowSorter(sorter);

        // コンテキストメニューを登録する
        table.addMouseListener(new ContextListener());

        // 来院情報テーブルの属性を設定する
        table.setRowHeight(rowHeight);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (int i = 0; i < columnWidth.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
        }

        // 性別レンダラを生成する
        MaleFemaleRenderer sRenderer = new MaleFemaleRenderer();
        table.getColumnModel().getColumn(0).setCellRenderer(sRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(sRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(sRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(sRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(sRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(sRenderer);
        table.getColumnModel().getColumn(9).setCellRenderer(sRenderer);
        // Center Renderer
        CenterRenderer centerRenderer = new CenterRenderer();
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // カルテ状態レンダラ
        KarteStateRenderer renderer = new KarteStateRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(STATE_COLUMN).setCellRenderer(renderer);

        setOperationDate(new Date());        // 日付ラベルに値を設定する
        setCheckInterval(checkInterval);        // チェック間隔情報を設定する
        setPvtCount(0);        // 来院数を設定する

        //選択条件プルダウンの初期値を取り出しセット
        String _medicalSchool = preferences.get("WaitingListView_medicalSchoolComboBox_Initial", "全て");
        if (!checkItems(categoryComboBox, _medicalSchool)) {
            categoryComboBox.addItem(_medicalSchool);
            categoryComboBox.setSelectedIndex(1);
        }

        //選択条件プルダウンの初期値を取り出しセット
        String _subject = preferences.get("WaitingListView_subjectComboBox_Initial", "全て");
        if (!checkItems(subjectComboBox, _subject)) {
            subjectComboBox.addItem(_subject);
            subjectComboBox.setSelectedIndex(1);
        }
    }

    /**
     * コンポーネントにイベントハンドラーを登録し相互に接続する。
     */
    private void connect() {

        // Chart のリスナになる
        // 患者カルテの Open/Save/SaveTemp の通知を受けて受付リストの表示を制御する
        ChartWindow._addPropertyChangeListener(ChartWindow.CHART_STATE, (PropertyChangeListener) EventHandler.create(PropertyChangeListener.class, this, "updateState", "newValue"));
        new EventAdapter(table);

        // 靴のアイコンをクリックした時来院情報を検索する
        kutuBtn.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "checkFullPvt"));
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchMainCommand(open.dolphin.helper.IMainCommandAccepter.MainCommand command) {
        switch (command) {
            case openKarte:
                return openKarte();
            default:
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @Override
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     */
    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
     */
    @Override
    public IMainWindow getContext() {
        return context;
    }

    /**
     *
     * @param context
     */
    @Override
    public void setContext(IMainWindow context) {
        this.context = context;
    }

    /**
     *
     * @return
     */
    @Override
    public Callable<Boolean> getStartingTask() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public Callable<Boolean> getStoppingTask() {
        return null;
    }

    /**
     *
     * @param pvtModel 患者来院情報
     */
    public synchronized void openKarte(PatientVisitModel pvtModel) {

        if (pvtModel != null && canOpen(pvtModel)) {
            context.openKarte(pvtModel, "");
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * チャートステートの状態をデータベースに書き込む。
     * @param updated 患者来院情報
     */
    public synchronized void updateState(final PatientVisitModel updated) {

        // 受付リストの状態カラムを更新する
        List pvtList = pvtTableModel.getObjectList();
        int cnt = pvtList.size();
        boolean found = false;
        for (int i = 0; i < cnt; i++) {
            // カルテをオープンし記録している途中で
            // 受付リストが更新され、要素が別オブジェクトに
            // なっている場合があるため、レコードIDで比較する
            PatientVisitModel test = (PatientVisitModel) pvtList.get(i);
            if (updated.getId() == test.getId()) {
                test.setState(updated.getState());
                pvtTableModel.fireTableRowsUpdated(i, i);
                found = true;
                break;
            }
        }

        // データベースを更新する
        //  if (found && updated.getState() == ChartWindow.CLOSE_SAVE) {
        //      RemotePVTDelegater pdl = new RemotePVTDelegater();
        //      pdl.updatePvtState(updated.getId(), updated.getState());
        //   }

        // データベースを更新する
        if (found && updated.getState() == IChart.state.CLOSE_SAVE) {
            RemotePVTDelegater pdl = new RemotePVTDelegater();
            pdl.updatePvtState(updated.getId(), updated.getState());
        }
    }

    /**
     * 受付の最新を表示する（kutuBtnをクリックしたときの動作）
     */
    public void checkFullPvt() {
        if (timerHandler != null) {
            timerHandler.cancel(false);
        }
        try {
            FutureTask<Integer> task = new FutureTask<Integer>(new CallablePvtChecker());
            new Thread(task).start();
            Integer result = task.get(120, TimeUnit.SECONDS); // 2分//MEMO: unused?
        } catch (Exception e) {
            LogWriter.error(this.getClass(), "", e);
        }
        ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();
        timerHandler = schedule.scheduleWithFixedDelay(pvtChecker, checkInterval, checkInterval, TimeUnit.SECONDS);
    }

    /**
     * カルテを開くことが可能かどうかを返す。
     * @param pvt 患者来院情報
     * @return 開くことが可能な時 true
     */
    private boolean canOpen(PatientVisitModel pvt) {
        if (pvt == null) {
            return false;
        }
        if (isKarteOpened(pvt)) {
            return false;
        }
        if (isKarteCanceled(pvt)) {
            return false;
        }
        return true;
    }

    /**
     * カルテがオープンされているかどうかを返す。
     * @param pvtModel 患者来院情報
     * @return オープンされている時 true
     */
    private boolean isKarteOpened(PatientVisitModel pvtModel) {
        return context.isKarteOpened(pvtModel.getId());
    }

    /**
     * 受付がキャンセルされているかどうかを返す。
     * @param pvtModel 患者来院情報
     * @return キャンセルされている時 true
     */
    private boolean isKarteCanceled(PatientVisitModel pvtModel) {
        if (pvtModel != null) {
            if (pvtModel.getState() == IChart.state.CANCEL_PVT) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param midiator
     */
    @Override
    public void setMenu(MenuMediator midiator) {
        midiator.setLast(this);
    }

    /**
     *
     */
    class EventAdapter implements ListSelectionListener, MouseListener {

        /**
         * 
         * @param tbl
         */
        public EventAdapter(JTable tbl) {

            tbl.getSelectionModel().addListSelectionListener(this);
            tbl.addMouseListener(this);
        }

        /**
         *
         * @param e
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting() == false) {
                ObjectReflectTableModel<PatientVisitModel> tableModel = (ObjectReflectTableModel<PatientVisitModel>) table.getModel();
                int row = getSelectedRow(table);
                PatientVisitModel patient = tableModel.getObject(row);
                setSelectedPvt(patient);
            }
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JTable table = (JTable) e.getSource();
                ObjectReflectTableModel<PatientVisitModel> tableModel = (ObjectReflectTableModel<PatientVisitModel>) table.getModel();
                //     int row = getSelectedRow(table);
                PatientVisitModel value = tableModel.getObject(getSelectedRow(table));
                openKarte(value);
            }
        }

        /**
         *
         * @param arg0
         */
        @Override
        public void mousePressed(MouseEvent arg0) {
        }

        /**
         *
         * @param arg0
         */
        @Override
        public void mouseReleased(MouseEvent arg0) {
        }

        /**
         *
         * @param arg0
         */
        @Override
        public void mouseEntered(MouseEvent arg0) {
        }

        /**
         *
         * @param arg0
         */
        @Override
        public void mouseExited(MouseEvent arg0) {
        }
    }

    /**
     * 受付リストのコンテキストメニュークラス。
     */
    class ContextListener extends MouseAdapter {

        /**
         *
         * @param row
         * @return
         */
        private synchronized Object getPvtTableModelObject(int row) {
            Object obj = pvtTableModel.getObject(row);
            return obj;
        }

        /**
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            mabeShowPopup(e);
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            mabeShowPopup(e);
        }

        /**
         *
         * @param e
         */
        public void mabeShowPopup(MouseEvent e) {

            if (e.isPopupTrigger()) {

                final JPopupMenu contextMenu = new JPopupMenu();
                String pop3 = "偶数奇数レンダラを使用する";
                String pop4 = "性別レンダラを使用する";
                String pop5 = "年齢表示";
                //   String pop6 = "FutureNetII に新患登録する";

                int row = table.rowAtPoint(e.getPoint());
                Object obj = getPvtTableModelObject(row);
                int selected = getSelectedRow(table);//MEMO: unused?

                //     if (row == selected && obj != null) {
                if (obj != null) {
                    //String pop1 = "カルテを開く"; //GlobalVariables.getString("waitingList.popup.openKarte");
                    PatientVisitModel pvt = getSelectedPvt();
                    if (canOpen(pvt)) {
                        String pop1 = "カルテを開く"; //GlobalVariables.getString("waitingList.popup.openKarte");
                        contextMenu.add(new JMenuItem(
                                new AbstractAction(pop1) {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        WaitingListView.this.openKarte();
                                    }
                                }));
                        String pop2 = "受付をキャンセルする"; //GlobalVariables.getString("waitingList.popup.cancelVisit");
                        contextMenu.add(new JMenuItem(
                                new AbstractAction(pop2) {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        WaitingListView.this.cancelVisit();
                                    }
                                }));
                    }
                    /* FutureNetIIのポップアップをディセーブル
                    contextMenu.add(new JMenuItem(
                    new AbstractAction(pop6) {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                    WaitingListView.this.sendNewPatient();
                    }
                    }));
                     */
                    contextMenu.addSeparator();
                }

                JRadioButtonMenuItem oddEven = new JRadioButtonMenuItem(
                        new AbstractAction(pop3) {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                WaitingListView.this.switchRenderere();
                            }
                        });

                JRadioButtonMenuItem sex = new JRadioButtonMenuItem(
                        new AbstractAction(pop4) {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                WaitingListView.this.switchRenderere();
                            }
                        });

                ButtonGroup bg = new ButtonGroup();
                bg.add(oddEven);
                bg.add(sex);
                contextMenu.add(oddEven);
                contextMenu.add(sex);
                if (sexRenderer) {
                    sex.setSelected(true);
                } else {
                    oddEven.setSelected(true);
                }

                JCheckBoxMenuItem item = new JCheckBoxMenuItem(pop5);
                contextMenu.add(item);
                item.setSelected(ageDisplay);
                item.addActionListener((ActionListener) EventHandler.create(ActionListener.class, WaitingListView.this, "switchAgeDisplay"));
                contextMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Popupメニューから選択されている患者のカルテを開く。
     * @return true
     */
    private boolean openKarte() {
        PatientVisitModel pvtModel = getSelectedPvt();
        if (canOpen(pvtModel)) {
            openKarte(pvtModel);
        }
        return true;
    }

    /**
     * 選択した患者の受付をキャンセルする。
     */
    public synchronized void cancelVisit() {

        final int selected = getSelectedRow(table);
        final PatientVisitModel pvtModel = pvtTableModel.getObject(selected);

        // ダイアログを表示し確認する
        Object[] cstOptions = new Object[]{"はい", "いいえ"};

        StringBuilder sb = new StringBuilder(pvtModel.getPatientName());
        sb.append("様の受付を取り消しますか?");

        int select = JOptionPane.showOptionDialog(
                SwingUtilities.getWindowAncestor(table),
                sb.toString(),
                GlobalConstants.getFrameTitle(getName()),
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                GlobalConstants.getImageIcon("cancl_32.gif"),
                cstOptions, "はい");

        // 受付を取り消す
        if (select == 0) {
            //      Runnable r = new Runnable() {

            //      public void run() {
            pvtModel.setState(IChart.state.CANCEL_PVT);
            RemotePVTDelegater pdl = new RemotePVTDelegater();
            pdl.updatePvtState(pvtModel.getId(), pvtModel.getState());
            pvtTableModel.fireTableRowsUpdated(selected, selected);
            //        }
            //     };
            //     Thread t = new Thread(r);
            //    t.setPriority(Thread.NORM_PRIORITY);
            //     t.run();
        }
    }

    /**
     *
     */
    public void sendNewPatient() {
        PatientModel model = getPatinet();
        Patient patient = new Patient(model);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(calendar.getTime());
        try {
            FileWriter writer = new FileWriter("kan." + date, "MS932");
            writer.write(patient);
            writer.close();
            JOptionPane.showMessageDialog(null, "FutureNetIIへ新患情報を送りました。" + System.getProperty("line.separator") + "FutureNetIIに反映されるまで最大10分かかります。");
        } catch (UnsupportedEncodingException uee) {
            LogWriter.error(this.getClass(), "", uee);
        } catch (IOException ioe) {
            LogWriter.error(this.getClass(), "", ioe);
        }
    }

    /**
     * 患者来院情報を定期的にチェックするタイマータスククラス。
     */
    protected class RunnablePvtChecker implements Runnable {

        /**
         * Creates new Task
         */
        public RunnablePvtChecker() {
        }

        /**
         * 患者来院情報を消去する
         */
        private synchronized void clearPvtList() {
            pvtTableModel.clear();
        }

        /**
         * 患者来院情報の一覧を返す
         * @return 患者来院情報の一覧
         */
        private synchronized List getPvtTableModelObjectList() {
            return pvtTableModel.getObjectList();
        }

        /**
         *
         * @param control
         * @param item
         * @return
         */
        private boolean checkItems(javax.swing.JComboBox control, String item) {
            for (int index = 0; index < control.getItemCount(); index++) {
                if (control.getItemAt(index).equals(item)) {
                    return true;
                }
            }
            return false;
        }

        /**
         *
         * @return
         */
        private RemotePVTDelegater getDelegater() {
            return new RemotePVTDelegater();
        }

        /**
         * ＤＢの検索タスク
         */
        @Override
        public void run() {

            Runnable awt1 = new Runnable() {

                @Override
                public void run() {
                    setBusy(true);
                }
            };
            EventQueue.invokeLater(awt1);

            final Date date = new Date();
            try {
                final String[] dateToSerach = getSearchDateAsString(date);

                // Hibernate の firstResult を現在の件数を保存する
                final List dataList = getPvtTableModelObjectList();

                final List result = (List) getDelegater().getPvt(dateToSerach, 0);

                String medicalSchool = (String) categoryComboBox.getSelectedItem();//診療科
                String subject = (String) subjectComboBox.getSelectedItem();       //内容
                clearPvtList();

                // 結果を追加する
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        if (((PatientVisitModel) result.get(i)).getDepartmentName().equals(medicalSchool) || (medicalSchool.equals("全て"))) {
                            if (((PatientVisitModel) result.get(i)).getCategory().equals(subject) || (subject.equals("全て"))) {
                                dataList.add(result.get(i));
                            }
                        }
                        //リストのフィルタリングを行うためのコンボボックスに値をセット
                        PatientVisitModel model = (PatientVisitModel) result.get(i);

                        String _medicalSchool = model.getDepartmentName();
                        if (!checkItems(categoryComboBox, _medicalSchool)) {
                            categoryComboBox.addItem(_medicalSchool);
                        }

                        String _subject = model.getCategory();
                        if (!checkItems(subjectComboBox, _subject)) {
                            subjectComboBox.addItem(_subject);
                        }
                    }
                    pvtTableModelfireTableRowsInserted(dataList);
                }
            } finally {
                Runnable awt2 = new Runnable() {

                    @Override
                    public void run() {
                        setCheckedTime(date);
                        setPvtCount(getPvtCount());
                        setBusy(false);
                    }

                    private synchronized int getPvtCount() {
                        return pvtTableModel.getObjectCount();
                    }
                };
                EventQueue.invokeLater(awt2);
            }
        }

        /**
         *
         * @param dataList
         */
        private synchronized void pvtTableModelfireTableRowsInserted(final List dataList) {
            pvtTableModel.fireTableRowsInserted(0, dataList.size() - 1);
        }
    }

    /**
     * 患者来院情報を定期的にチェックするタイマータスククラス。
     */
    protected class CallablePvtChecker implements Callable<Integer> {

        RemotePVTDelegater delegater;

        /**
         * Creates new Task
         */
        public CallablePvtChecker() {
            delegater = new RemotePVTDelegater();
        }

        /**
         *
         * @param control
         * @param item
         * @return
         */
        private synchronized boolean checkItems(javax.swing.JComboBox control, String item) {
            for (int index = 0; index < control.getItemCount(); index++) {
                if (control.getItemAt(index).equals(item)) {
                    return true;
                }
            }
            return false;
        }

        /**
         *
         */
        private synchronized void clearPvtList() {
            pvtTableModel.clear();
        }

        /**
         *
         * @param dateToSerach
         * @param firstResult
         * @return
         */
        private synchronized List getPvt(final String[] dateToSerach, int firstResult) {
            final List result = (List) delegater.getPvt(dateToSerach, firstResult);
            return result;
        }

        /**
         * 患者来院情報の一覧を返す
         * @return 患者来院情報の一覧
         */
        private synchronized List getPvtTableModelObjectList() {
            return pvtTableModel.getObjectList();
        }

        /**
         * ＤＢの検索タスク
         */
        @Override
        public Integer call() {

            Runnable awt1 = new Runnable() {

                @Override
                public void run() {
                    setBusy(true);
                }
            };
            EventQueue.invokeLater(awt1);

            final Date date = new Date();
            final String[] dateToSerach = getSearchDateAsString(date);
            int checkCount;
            try {
                // 最初に現れる診察未終了レコードを Hibernate の firstResult にする
                // 現在の件数を保存する
                //       List dataList = pvtTableModel.getObjectList();
                List dataList = getPvtTableModelObjectList();
                int firstResult = 0;
                List result = getPvt(dateToSerach, firstResult);
                checkCount = result != null ? result.size() : 0;

                if (checkCount > 0) {
                    String category = (String) categoryComboBox.getSelectedItem();
                    String subject = (String) subjectComboBox.getSelectedItem();

                    clearPvtList();

                    for (int i = 0; i < checkCount; i++) {
                        if (((PatientVisitModel) result.get(i)).getDepartmentName().equals(category) || (category.equals("全て"))) {
                            if (((PatientVisitModel) result.get(i)).getCategory().equals(subject) || (subject.equals("全て"))) {
                                dataList.add(result.get(i));
                            }
                        }
                        //リストのフィルタリングを行うためのコンボボックスに値をセット
                        PatientVisitModel model = (PatientVisitModel) result.get(i);

                        String _medicalSchool = model.getDepartmentName();
                        if (!checkItems(categoryComboBox, _medicalSchool)) {
                            categoryComboBox.addItem(_medicalSchool);
                        }

                        String _subject = model.getCategory();
                        if (!checkItems(subjectComboBox, _subject)) {
                            subjectComboBox.addItem(_subject);
                        }
                    }
                    pvtTableModelFireTableDataChanged();
                }
            } finally {
                Runnable awt2 = new Runnable() {

                    @Override
                    public void run() {
                        setCheckedTime(date);
                        setPvtCount(getPvtCount());
                        setBusy(false);
                    }

                    private synchronized int getPvtCount() {
                        return pvtTableModel.getObjectCount();
                    }
                };
                EventQueue.invokeLater(awt2);
            }
            return new Integer(checkCount);
        }

        /**
         *
         */
        private synchronized void pvtTableModelFireTableDataChanged() {
            pvtTableModel.fireTableDataChanged();
        }
    }

    /**
     *
     * @param date
     * @return
     */
    private synchronized String[] getSearchDateAsString(Date date) {

        String[] ret = new String[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ret[0] = sdf.format(date);

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        gc.add(Calendar.DAY_OF_MONTH, -2);
        date = gc.getTime();
        ret[1] = sdf.format(date);

        gc.add(Calendar.DAY_OF_MONTH, 2);
        date = gc.getTime();
        ret[2] = sdf.format(date);

        return ret;
    }

    /**
     * KarteStateRenderer MEMO:Component
     * カルテ（チャート）の状態をレンダリングするクラス。
     */
    protected class KarteStateRenderer extends DefaultTableCellRenderer {

        /** Creates new IconRenderer */
        public KarteStateRenderer() {
            super();
            setOpaque(true);
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

            //  PatientVisitModel pvt = (PatientVisitModel) pvtTableModel.getObject(row);
            PatientVisitModel pvt = (PatientVisitModel) pvtTableModel.getObject(table.convertRowIndexToModel(row));


            if (isSelected) {
                this.setBackground(table.getSelectionBackground());
                this.setForeground(table.getSelectionForeground());
            } else {
                if (isSexRenderer()) {
                    if (pvt != null && pvt.getPatient().getGender().equals(IInfoModel.MALE)) {
                        this.setBackground(MALE_COLOR);
                    } else if (pvt != null && pvt.getPatient().getGender().equals(IInfoModel.FEMALE)) {
                        this.setBackground(FEMALE_COLOR);
                    } else {
                        this.setBackground(Color.WHITE);
                    }
                } else {
                    if (row % 2 == 0) {
                        this.setBackground(EVEN_COLOR);
                    } else {
                        this.setBackground(ODD_COLOR);
                    }
                }

                Color fore = pvt != null && pvt.getState() == IChart.state.CANCEL_PVT ? CANCEL_PVT_COLOR : table.getForeground();
                this.setForeground(fore);
            }
            /*
            IChart.state o = (state) table.getValueAt(row, 7);
            if (o instanceof IChart.state) {

            switch (o) {
            case CLOSE_NONE:
            break;
            case CLOSE_SAVE:
            this.setBackground(FLAG_COLOR);
            break;
            case OPEN_NONE:
            case OPEN_SAVE:
            this.setBackground(OPEN_COLOR);
            break;
            case CANCEL_PVT:
            default:
            this.setIcon(null);
            break;
            }
             */

            if (value != null && value instanceof IChart.state) {
                IChart.state state = (state) value;

                switch (state) {
                    case CLOSE_NONE:
                        this.setIcon(null);     // アイコンなし
                        break;
                    case CLOSE_SAVE:
                        this.setIcon(FLAG_ICON); // 診察が終了している場合は旗
                        break;
                    case OPEN_NONE:
                    case OPEN_SAVE:
                        this.setIcon(OPEN_ICON); // オープンしている場合はオープン
                        break;
                    case CANCEL_PVT:
                    default:
                        this.setIcon(null);
                        break;
                }

                //   if (value != null && value instanceof Integer) {
                //        int state = ((Integer) value).intValue();
                //         switch (state) {
                //            case ChartWindow.CLOSE_NONE:
                //                 this.setIcon(null);     // アイコンなし
                //                break;
                //            case ChartWindow.CLOSE_SAVE:
                //                this.setIcon(FLAG_ICON); // 診察が終了している場合は旗
                //               break;
                //           case ChartWindow.OPEN_NONE:
                //           case ChartWindow.OPEN_SAVE:
                //               this.setIcon(OPEN_ICON); // オープンしている場合はオープン
                //              break;
                //         default:
                //              this.setIcon(null);
                //              break;
                //      }
                this.setText("");
            } else {
                setIcon(null);
                this.setText(value == null ? "" : value.toString());
            }
            return this;
        }
    }

    /**
     * KarteStateRenderer MEMO:Component
     * カルテ（チャート）の状態をレンダリングするクラス。
     */
    protected class MaleFemaleRenderer extends DefaultTableCellRenderer {

        /** Creates new IconRenderer */
        public MaleFemaleRenderer() {
            super();
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

            //    PatientVisitModel pvt = (PatientVisitModel) pvtTableModel.getObject(row);
            PatientVisitModel pvt = (PatientVisitModel) pvtTableModel.getObject(table.convertRowIndexToModel(row));

            if (isSelected) {
                this.setBackground(table.getSelectionBackground());
                this.setForeground(table.getSelectionForeground());
            } else {
                if (isSexRenderer()) {
                    if (pvt != null && pvt.getPatient().getGender().equals(IInfoModel.MALE)) {
                        this.setBackground(MALE_COLOR);
                    } else if (pvt != null && pvt.getPatient().getGender().equals(IInfoModel.FEMALE)) {
                        this.setBackground(FEMALE_COLOR);
                    } else {
                        this.setBackground(Color.WHITE);
                    }
                } else {
                    if (row % 2 == 0) {
                        this.setBackground(EVEN_COLOR);
                    } else {
                        this.setBackground(ODD_COLOR);
                    }
                }

                Color fore = pvt != null && pvt.getState() == IChart.state.CANCEL_PVT ? CANCEL_PVT_COLOR : table.getForeground();
                this.setForeground(fore);
            }

            if (value != null && value instanceof String) {
                this.setText((String) value);
            } else {
                setIcon(null);
                this.setText(value == null ? "" : value.toString());
            }
            return this;
        }
    }

    /**
     * MEMO:Component
     */
    protected class CenterRenderer extends MaleFemaleRenderer {

        /** Creates new IconRenderer */
        public CenterRenderer() {
            super();
            this.setHorizontalAlignment(JLabel.CENTER);
        }
    }
}
