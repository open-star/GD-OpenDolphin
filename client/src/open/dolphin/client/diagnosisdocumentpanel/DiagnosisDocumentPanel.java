/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DiagnosisDocument.java
 *
 * Created on 2010/03/09, 16:57:26
 */
package open.dolphin.client.diagnosisdocumentpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.EventHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import open.dolphin.helper.IChartCommandAccepter;
import open.dolphin.client.editor.stamp.StampEditorDialog;
import open.dolphin.container.NameValuePair;
import open.dolphin.client.AutoRomanListener;
import open.dolphin.client.CalendarCardPanel;
import open.dolphin.client.ChartMediator;
import open.dolphin.client.ChartWindow;
import open.dolphin.client.ClaimMessageEvent;
import open.dolphin.client.DiagnosisTransferHandler;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;
import open.dolphin.client.RegexConstrainedDocument;
import open.dolphin.client.editor.diagnosis.DiagnosisEditorDialog;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.infomodel.DiagnosisCategoryModel;
import open.dolphin.infomodel.DiagnosisOutcomeModel;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.PatientLiteModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.infomodel.SimpleDate;
import open.dolphin.infomodel.StampModel;
import open.dolphin.message.DiseaseHelper;
import open.dolphin.message.MessageBuilder;
import open.dolphin.dao.SqlOrcaView;
import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.delegater.remote.RemoteStampDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.log.LogWriter;

import open.dolphin.message.DiagnosisModuleItem;
import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalVariables;
import open.dolphin.sendclaim.SendClaimImpl;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.utils.CombinedStringParser;
import open.dolphin.utils.GUIDGenerator;
import open.dolphin.utils.MMLDate;
import open.dolphin.utils.ReflectMonitor;
import open.dolphin.utils.StringSubstitution;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.TaskService;

/**
 *　傷病名画面　MEMO:画面　リスナー
 * @author
 */
public final class DiagnosisDocumentPanel extends javax.swing.JPanel implements PropertyChangeListener, IChartDocument, IChartCommandAccepter {//, IMainCommandAccepter

    /**
     *
     */
    public static final String TITLE = "傷病名";
    // 傷病名テーブルのカラム番号定義
    private static final int DIAGNOSIS_COL = 0;
    private static final int CATEGORY_COL = 1;
    private static final int OUTCOME_COL = 2;
    private static final int START_DATE_COL = 3;
    private static final int END_DATE_COL = 4;
    // 抽出期間コンボボックスデータ
    private NameValuePair[] extractionObjects = GlobalConstants.getNameValuePair("diagnosis.combo.period");
    // GUI コンポーネント定義
    private static final String RESOURCE_BASE = "/open/dolphin/resources/images/";
    private static final String DELETE_BUTTON_IMAGE = "del_16.gif";
    private static final String ADD_BUTTON_IMAGE = "add_16.gif";
    private static final String UPDATE_BUTTON_IMAGE = "save_16.gif";
    private static final String ORCA_IMPORT_IMAGE = "move_16.gif";
    private static final String ORCA_VIEW_IMAGE = "impt_16.gif";
    private static final String TABLE_BORDER_TITLE = "傷病歴";
    private static final String ORCA_VIEW = "ORCA View";
    private static final String ORCA_RECORD = "ORCA";
    // GUI Component
    /** JTableレンダラ用の奇数カラー */
    private static final Color ODD_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.ODD);
    /** JTableレンダラ用の偶数カラー */
    private static final Color EVEN_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.EVEN);
    private static final Color ORCA_BACK = GlobalSettings.getColors(GlobalSettings.Parts.CALENDAR_BACK);
    private JButton addButton;                  // 新規病名エディタボタン
    private JButton updateButton;               // 既存傷病名の転帰等の更新ボタン
    private JButton deleteButton;               // 既存傷病名の削除ボタン
    private JButton importButton;                 // ORCAからの実データ取り込みボタン
    private JButton orcaButton;                 // ORCA View ボタン
    private JTable diagTable;                   // 病歴テーブル
    private ObjectReflectTableModel<RegisteredDiagnosisModel> tableModel; // TableModel
    private JComboBox extractionCombo;          // 抽出期間コンボ
    private JTextField countField;              // 件数フィールド
    // 抽出期間内で Dolphin に最初に病名がある日
    // ORCA の病名は抽出期間〜dolphinFirstDate
    private String dolphinFirstDate;//MEMO: unused?
    // 昇順降順フラグ
    private boolean ascend;
    // 新規に追加された傷病名リスト
    List<RegisteredDiagnosisModel> addedDiagnosis;
    // 更新された傷病名リスト
    List<RegisteredDiagnosisModel> updatedDiagnosis;
    // 傷病名件数
    private int diagnosisCount;
    // 分類・転帰コンボボックス
    //  private JComboBox categoryCombo;
    //  private JComboBox outcomeCombo;
    //元々はAbstractChartDocumentから継承
    private static final String[] CHART_MENUS = {
        GUIConst.ACTION_OPEN_KARTE, GUIConst.ACTION_SAVE, GUIConst.ACTION_DIRECTION, GUIConst.ACTION_DELETE, GUIConst.ACTION_PRINT, GUIConst.ACTION_MODIFY_KARTE,
        GUIConst.ACTION_ASCENDING, GUIConst.ACTION_DESCENDING, GUIConst.ACTION_SHOW_MODIFIED, GUIConst.ACTION_SHOW_UNSEND, GUIConst.ACTION_SHOW_SEND, GUIConst.ACTION_SHOW_NEWEST,
        GUIConst.ACTION_INSERT_TEXT, GUIConst.ACTION_INSERT_SCHEMA, GUIConst.ACTION_INSERT_STAMP, GUIConst.ACTION_SELECT_INSURANCE,
        GUIConst.ACTION_CUT, GUIConst.ACTION_COPY, GUIConst.ACTION_PASTE, GUIConst.ACTION_UNDO, GUIConst.ACTION_REDO
    };
    private IChart parent;
    private String title;
    private boolean dirty;
    /**
     *
     */
    protected Application app;
    /**
     *
     */
    protected ApplicationContext appCtx;
    /**
     *
     */
    protected TaskMonitor taskMonitor;
    /**
     *
     */
    protected TaskService taskService;

    /**
     *
     * @throws NumberFormatException
     */
    private void applyPeriodFilter() throws NumberFormatException {
        NameValuePair pair = (NameValuePair) extractionCombo.getSelectedItem();
        int past = Integer.parseInt(pair.getValue());
        Date date = new Date(0l);
        ListFilter filter = new ListFilter();
        switch (past) {
            case 0:
                break;
            case 1:
                filter = new OutcomeFilter();
                break;
            default:
                GregorianCalendar today = new GregorianCalendar();
                today.add(GregorianCalendar.MONTH, past);
                today.clear(Calendar.HOUR_OF_DAY);
                today.clear(Calendar.MINUTE);
                today.clear(Calendar.SECOND);
                today.clear(Calendar.MILLISECOND);
                date = today.getTime();
                break;
        }
        getDiagnosisHistory(date, filter);
    }

    /**
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return IChartDocument.TYPE.DiagnosisDocumentPanel;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean itLayoutSaved() {
        return true;
    }

    /**
     *
     */
    class ListFilter {

        public List filter(List source) {
            return source;
        }
    }

    /**
     *
     */
    class OutcomeFilter extends ListFilter {

        @Override
        public List filter(List source) {
            List result = new ArrayList();
            for (Object rd : source) {
                String outcomeDesc = ((RegisteredDiagnosisModel) rd).getOutcomeDesc();
                if (isEmpty(outcomeDesc) || isContinuance(outcomeDesc)) {
                    result.add(rd);
                }
            }
            return result;
        }

        private boolean isEmpty(String target) {
            return target == null || target.equals("");
        }

        private boolean isContinuance(String target) {
            return target.equals("継続");
        }
    }

    /** Creates new form DiagnosisDocument
     * @param parent
     */
    public DiagnosisDocumentPanel(IChart parent) {
        this.title = TITLE;
        this.parent = parent;
        initComponents();
        appCtx = GlobalConstants.getApplicationContext();
        app = appCtx.getApplication();
        taskMonitor = appCtx.getTaskMonitor();
        taskService = appCtx.getTaskService();
        // setTitle(TITLE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        outcomeCombo = new javax.swing.JComboBox();
        categoryCombo = new javax.swing.JComboBox();

        outcomeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        outcomeCombo.setName("outcomeCombo"); // NOI18N
        outcomeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outcomeComboActionPerformed(evt);
            }
        });

        categoryCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        categoryCombo.setName("categoryCombo"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void outcomeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outcomeComboActionPerformed
        //     tableModel.setValueAt("", diagTable.getSelectedRow(), END_DATE_COL);
    }//GEN-LAST:event_outcomeComboActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox categoryCombo;
    private javax.swing.JComboBox outcomeCombo;
    // End of variables declaration//GEN-END:variables

    /**
     * GUI コンポーネントを生成初期化する。
     */
    private void initialize() {

        JPanel cmdPanel = createButtonPanel2();     // コマンドボタンパネルを生成する
        JPanel dolphinPanel = createDignosisPanel();    // Dolphin 傷病歴パネルを生成する
        JPanel filterPanel = createFilterPanel();   // 抽出期間パネルを生成する
        JPanel content = new JPanel(new BorderLayout(0, 7));

        content.add(cmdPanel, BorderLayout.NORTH);
        content.add(dolphinPanel, BorderLayout.CENTER);
        content.add(filterPanel, BorderLayout.SOUTH);
        content.setBorder(BorderFactory.createTitledBorder(TABLE_BORDER_TITLE));

        // 全体をレイアウトする
        //   JPanel myPanel = getPanel();
        setLayout(new BorderLayout(0, 7));
        add(content);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

        // Preference から昇順降順を設定する
        ascend = GlobalVariables.getPreferences().getBoolean(GlobalVariables.DIAGNOSIS_ASCENDING, false);
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchChartCommand(IChartCommandAccepter.ChartCommand command) {
        switch (command) {
            case save:
                return save();
            case delete:
                return delete();
            case print:
                return print();
            case letterPaste:
                break;
            default:
        }
        return false;
    }

    /**
     * コマンドボタンパネルをする。
     */
    private JPanel createButtonPanel2() {

        // 更新ボタン (ActionListener) EventHandler.create(ActionListener.class, this, "save")
        updateButton = new JButton(createImageIcon(UPDATE_BUTTON_IMAGE));
        updateButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "onSave"));
        updateButton.setEnabled(false);
        updateButton.setToolTipText("追加変更した傷病名をデータベースに反映します。");

        // 削除ボタン
        deleteButton = new JButton(createImageIcon(DELETE_BUTTON_IMAGE));
        deleteButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "delete"));
        deleteButton.setEnabled(false);
        deleteButton.setToolTipText("選択した傷病名を削除します。");

        // 新規登録ボタン
        addButton = new JButton(createImageIcon(ADD_BUTTON_IMAGE));
        addButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isPopupTrigger()) {
                    openEditor2();
                }
            }
        });

        // Depends on readOnly prop
        addButton.setEnabled(!isReadOnly());
        addButton.setToolTipText("傷病名を追加します。");

        importButton = new JButton(createImageIcon(ORCA_IMPORT_IMAGE));
        importButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "importFromOrca"));
        importButton.setToolTipText("ORCAに登録してある病名をインポートします。");
        importButton.setEnabled(false);

        // ORCA View
        orcaButton = new JButton(createImageIcon(ORCA_VIEW_IMAGE));
        orcaButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "viewOrca"));
        orcaButton.setToolTipText("ORCAに登録してある病名を取り込みます。");

        // ボタンパネル
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panel.add(importButton);
        panel.add(orcaButton);
        panel.add(deleteButton);
        panel.add(addButton);
        panel.add(updateButton);
        return panel;
    }

    /**
     * 既傷病歴テーブルを生成する。
     */
    private JPanel createDignosisPanel() {

        String[] columnNames = {"疾患名/修飾語", "分 類", "転 帰", "疾患開始日", "疾患終了日"};
        String[] methodNames = {"getAliasOrName", "getCategoryDesc", "getOutcomeDesc", "getStartDate", "getEndDate"}; 
        Class[] columnClasses = new Class[]{String.class, String.class, String.class, String.class, String.class};
        int startNumRows = 1; //GlobalVariables.getInt("diagnosis.startNumRows");

        // Diagnosis テーブルモデルを生成する
        tableModel = new ObjectReflectTableModel<RegisteredDiagnosisModel>(columnNames, startNumRows, methodNames, columnClasses) {

            // Diagnosisは編集不可
            @Override
            public boolean isCellEditable(int row, int col) {

                // licenseCodeで制御
                if (isReadOnly()) {
                    return false;
                }

                String old_val = (String) this.getValueAt(row, col);
                switch (col) {
                    case CATEGORY_COL:
                        String[] category = {"主病名", "疑い病名"}; //GlobalVariables.getStringArray("diagnosis.categoryDesc");
                        for (int i = 0; i < category.length; ++i) {
                            if (category[i].equals(old_val)) {
                                categoryCombo.setSelectedIndex(i + 1);
                                break;
                            }
                        }
                        break;
                    case OUTCOME_COL:
                        String[] outcome = {"回復", "全治", "続発症(の発生)", "終了", "中止", "継続", "死亡", "悪化", "不変", "転医", "転医(急性病院へ)", "転医(慢性病院へ)", "自宅へ退院", "不明"}; //GlobalVariables.getStringArray("diagnosis.outcomeDesc");
                        for (int i = 0; i < outcome.length; ++i) {
                            if (outcome[i].equals(old_val)) {
                                outcomeCombo.setSelectedIndex(i + 1);
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }

                // outcomeCombo.setSelectedItem((String) this.getValueAt(row, col));
                // 病名レコードが存在しない場合は false
                RegisteredDiagnosisModel entry = (RegisteredDiagnosisModel) getObject(row);

                // ORCA に登録されている病名の場合
                if (entry != null && entry.getStatus() != null && entry.getStatus().equals(ORCA_RECORD)) {
                    importButton.setEnabled(true);
                    return false;
                } else {
                    importButton.setEnabled(false);
                }

                // それ以外はカラムに依存する
                return ((col == CATEGORY_COL || col == OUTCOME_COL || col == START_DATE_COL || col == END_DATE_COL))
                        ? true
                        : false;
            }

            // オブジェクトの値を設定する
            @Override
            public void setValueAt(Object value, int row, int col) {

                RegisteredDiagnosisModel entry = getObject(row);
                if (value == null || entry == null) {
                    return;
                }


                switch (col) {

                    case DIAGNOSIS_COL:
                        break;

                    case CATEGORY_COL:
                        // JComboBox から選択
                        String saveCategory = entry.getCategory();
                        DiagnosisCategoryModel dcm = (DiagnosisCategoryModel) value;
                        String test = dcm.getDiagnosisCategory();
                        test = test != null && (!test.equals("")) ? test : null;
                        if (saveCategory != null) {
                            if (test != null) {
                                if (!test.equals(saveCategory)) {
                                    entry.setCategory(dcm.getDiagnosisCategory());
                                    entry.setCategoryDesc(dcm.getDiagnosisCategoryDesc());
                                    entry.setCategoryCodeSys(dcm.getDiagnosisCategoryCodeSys());
                                    fireTableRowsUpdated(row, row);
                                    addUpdatedList(entry);
                                }
                            } else {
                                entry.setDiagnosisCategoryModel(null);
                                fireTableRowsUpdated(row, row);
                                addUpdatedList(entry);
                            }

                        } else {
                            if (test != null) {
                                entry.setCategory(dcm.getDiagnosisCategory());
                                entry.setCategoryDesc(dcm.getDiagnosisCategoryDesc());
                                entry.setCategoryCodeSys(dcm.getDiagnosisCategoryCodeSys());
                                fireTableRowsUpdated(row, row);
                                addUpdatedList(entry);
                            }
                        }
                        break;

                    case OUTCOME_COL:
                        // JComboBox から選択
                        String saveOutcome = entry.getOutcome();
                        DiagnosisOutcomeModel dom = (DiagnosisOutcomeModel) value;
                        test = dom.getOutcome();
                        test = (test != null && !test.equals("")) ? test : null;
                        if (saveOutcome != null) {
                            if (test != null) {
                                if (!saveOutcome.equals(test)) {
                                    setEndDate(entry, dom, row);
                                }
                            } else {
                                entry.setDiagnosisOutcomeModel(null);
                                fireTableRowsUpdated(row, row);
                                addUpdatedList(entry);
                            }
                        } else {
                            if (test != null) {
                                setEndDate(entry, dom, row);
                            }
                        }

                        break;

                    case START_DATE_COL:
                        String startDate = (String) value;
                        if (!startDate.trim().equals("")) {
                            entry.setStartDate(startDate);
                            fireTableRowsUpdated(row, row);
                            addUpdatedList(entry);
                        }
                        break;

                    case END_DATE_COL:
                        String endDate = (String) value;

                        //  if (!endDate.trim().equals("")) {
                        //          if (entry.getOutcome() == null || entry.getOutcome().equals("")) {
                        entry.setEndDate(endDate);
                        fireTableRowsUpdated(row, row);
                        addUpdatedList(entry);
                        //           }
//     }

                        break;
                default: LogWriter.fatal(getClass(), "case default");
                }
            }

            private void setEndDate(RegisteredDiagnosisModel entry, DiagnosisOutcomeModel dom, int row) {
                entry.setOutcome(dom.getOutcome());
                entry.setOutcomeDesc(dom.getOutcomeDesc());
                entry.setOutcomeCodeSys(dom.getOutcomeCodeSys());
                // 疾患終了日を入れる
                if (GlobalVariables.isAutoOutcomeInput()) {
                    String val = entry.getEndDate();
                    if (val == null || val.equals("")) {
                        GregorianCalendar gc = new GregorianCalendar();
                        int offset = GlobalVariables.getPreferences().getInt(GlobalVariables.OFFSET_OUTCOME_DATE, -7);
                        gc.add(Calendar.DAY_OF_MONTH, offset);
                        String today = MMLDate.getDate(gc);
                        entry.setEndDate(today);
                    }
                }
                fireTableRowsUpdated(row, row);
                addUpdatedList(entry);
            }
        };

        // 傷病歴テーブルを生成する
        diagTable = new JTable(tableModel);

        // 奇数、偶数行の色分けをする
        diagTable.setDefaultRenderer(Object.class, new DolphinOrcaRenderer());

        diagTable.setSurrendersFocusOnKeystroke(true);

        // 行選択が起った時のリスナを設定する
        diagTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        diagTable.setRowSelectionAllowed(true);
        ListSelectionModel m = diagTable.getSelectionModel();
        m.addListSelectionListener((ListSelectionListener) EventHandler.create(ListSelectionListener.class, this, "rowSelectionChanged", ""));

        diagTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                if (event.getClickCount() == 2) {
                    JTable source = (JTable) event.getSource();
                    RegisteredDiagnosisModel entry = (RegisteredDiagnosisModel) tableModel.getObject(source.getSelectedRow());
                    if (entry != null) {
                        if (entry.getStatus() != null) {
                            if (!entry.getStatus().equals("ORCA")) {
                                openDiagnosisEditor(entry);
                            }
                        } else {
                            openDiagnosisEditor(entry);
                        }
                    }
                }
            }
        });

        // Category comboBox 入力を設定する
        String[] values = {"mainDiagnosis", "suspectedDiagnosis"}; //GlobalVariables.getStringArray("diagnosis.category");
        String[] descs = {"主病名", "疑い病名"}; //GlobalVariables.getStringArray("diagnosis.categoryDesc");
        String[] codeSys = {"MML0012", "MML0015"}; //GlobalVariables.getStringArray("diagnosis.categoryCodeSys");
        DiagnosisCategoryModel[] categoryList = new DiagnosisCategoryModel[values.length + 1];
        DiagnosisCategoryModel dcm = new DiagnosisCategoryModel();
        dcm.setDiagnosisCategory("");
        dcm.setDiagnosisCategoryDesc("");
        dcm.setDiagnosisCategoryCodeSys("");
        categoryList[0] = dcm;
        for (int i = 0; i < values.length; i++) {
            dcm = new DiagnosisCategoryModel();
            dcm.setDiagnosisCategory(values[i]);
            dcm.setDiagnosisCategoryDesc(descs[i]);
            dcm.setDiagnosisCategoryCodeSys(codeSys[i]);
            categoryList[i + 1] = dcm;
        }
        //    categoryCombo = new JComboBox(categoryList);
        categoryCombo.setModel(new DefaultComboBoxModel(categoryList));
        TableColumn column = diagTable.getColumnModel().getColumn(CATEGORY_COL);
        column.setCellEditor(new DefaultCellEditor(categoryCombo));

        // Outcome comboBox 入力を設定する
        String[] ovalues = {"recovering", "fullyRecovered", "sequelae", "end", "pause", "continued", "died", "worsening", "unchanged", "transfer", "transferAcute", "transferChronic", "home", "unknown"}; //GlobalVariables.getStringArray("diagnosis.outcome");
        String[] odescs = {"回復", "全治", "続発症(の発生)", "終了", "中止", "継続", "死亡", "悪化", "不変", "転医", "転医(急性病院へ)", "転医(慢性病院へ)", "自宅へ退院", "不明"}; //GlobalVariables.getStringArray("diagnosis.outcomeDesc");
        String ocodeSys = "MML0016"; //GlobalVariables.getString("diagnosis.outcomeCodeSys");
        DiagnosisOutcomeModel[] outcomeList = new DiagnosisOutcomeModel[ovalues.length + 1];
        DiagnosisOutcomeModel dom = new DiagnosisOutcomeModel();
        dom.setOutcome("");
        dom.setOutcomeDesc("");
        dom.setOutcomeCodeSys("");
        outcomeList[0] = dom;
        //
        // 主病名は使用しないらしい
        //
        //outcomeList[0] = null;
        for (int i = 0; i < ovalues.length; i++) {
            dom = new DiagnosisOutcomeModel();
            dom.setOutcome(ovalues[i]);
            dom.setOutcomeDesc(odescs[i]);
            dom.setOutcomeCodeSys(ocodeSys);
            outcomeList[i + 1] = dom;
        }
        //  outcomeCombo = new JComboBox(outcomeList);
        outcomeCombo.setModel(new DefaultComboBoxModel(outcomeList));
        column = diagTable.getColumnModel().getColumn(OUTCOME_COL);
        column.setCellEditor(new DefaultCellEditor(outcomeCombo));

        setCellEditor(diagTable.getColumnModel().getColumn(START_DATE_COL));
        setCellEditor(diagTable.getColumnModel().getColumn(END_DATE_COL));

        // TransferHandler を設定する
        diagTable.setTransferHandler(new DiagnosisTransferHandler(this));
        diagTable.setDragEnabled(true);

        // Layout
        JScrollPane scroller = new JScrollPane(diagTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        JPanel p = new JPanel(new BorderLayout());
        p.add(scroller, BorderLayout.CENTER);
        return p;
    }

    /**
     *
     * @param column
     */
    private void setCellEditor(TableColumn column) {
        int clickCountToStart = GlobalVariables.getPreferences().getInt("diagnosis.table.clickCountToStart", 2);
        String datePattern = "[0-9\\-]*";

        JTextField editorControl = new JTextField();

        editorControl.addFocusListener(AutoRomanListener.getInstance());

        editorControl.setDocument(new RegexConstrainedDocument(datePattern));

        new PopupListener(editorControl);
        DefaultCellEditor cellEditor = new DefaultCellEditor(editorControl);

        column.setCellEditor(cellEditor);
        cellEditor.setClickCountToStart(clickCountToStart);
    }

    /**
     * 抽出期間パネルを生成する。
     */
    private JPanel createFilterPanel() {

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(Box.createHorizontalStrut(7));

        // 抽出期間コンボボックス
        p.add(new JLabel("抽出期間(過去)"));
        p.add(Box.createRigidArea(new Dimension(5, 0)));
        extractionCombo = new JComboBox(extractionObjects);
        Preferences prefs = GlobalVariables.getPreferences();
        int currentDiagnosisPeriod = prefs.getInt(GlobalVariables.DIAGNOSIS_PERIOD, 0);
        int selectIndex = NameValuePair.getIndex(String.valueOf(currentDiagnosisPeriod), extractionObjects);
        extractionCombo.setSelectedIndex(selectIndex);
        extractionCombo.addItemListener((ItemListener) EventHandler.create(ItemListener.class, this, "extPeriodChanged", ""));

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        comboPanel.add(extractionCombo);
        p.add(comboPanel);

        p.add(Box.createHorizontalGlue());

        // 件数フィールド
        countField = new JTextField(2);
        countField.setEditable(false);
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        countPanel.add(new JLabel("件数"));
        countPanel.add(countField);

        p.add(countPanel);
        p.add(Box.createHorizontalStrut(7));
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 7, 0));

        return p;
    }

    /**
     * 行選択が起った時のボタン制御を行う。
     * @param e
     */
    public void rowSelectionChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting() == false) {
            // 削除ボタンをコントロールする
            // licenseCode 制御を追加
            if (isReadOnly()) {
                return;
            }

            // 選択された行のオブジェクトを得る
            int row = diagTable.getSelectedRow();
            RegisteredDiagnosisModel rd = tableModel.getObject(row);

            // ヌルの場合
            if (rd == null) {
                if (deleteButton.isEnabled()) {
                    deleteButton.setEnabled(false);
                }
                return;
            }

            // ORCA の場合
            if (rd.getStatus() != null && rd.getStatus().equals(ORCA_RECORD)) {
                if (deleteButton.isEnabled()) {
                    deleteButton.setEnabled(false);
                }
                return;
            }

            // Dolphin の場合
            if (!deleteButton.isEnabled()) {
                deleteButton.setEnabled(true);
            }
        }
    }

    /**
     * 抽出期間を変更した場合に再検索を行う。
     * ORCA 病名ボタンが disable であれば検索後に enable にする。
     * @param e
     */
    public void extPeriodChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            applyPeriodFilter();
        }
    }

    /**
     *
     * @return
     */
    public JTable getDiagnosisTable() {
        return diagTable;
    }

    /**
     *
     */
    @Override
    public void start() {
        initialize();
        applyPeriodFilter();
        enter();
    }

    /**
     *
     */
    @Override
    public void stop() {
        if (tableModel != null) {
            tableModel.clear();
        }
    }

    /**
     *
     */
    @Override
    public void enter() {
        getParentContext().getStatusPanel().setMessage("");
        getParentContext().getChartMediator().setAccepter(this);
        disableMenus();
        getParentContext().enabledAction(GUIConst.ACTION_NEW_KARTE, true);
        getParentContext().enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
        getParentContext().enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
    }

    /**
     * 新規傷病名リストに追加する。
     * @param added 追加されたRegisteredDiagnosisModel
     */
    private void addAddedList(RegisteredDiagnosisModel added) {
        if (addedDiagnosis == null) {
            addedDiagnosis = new ArrayList<RegisteredDiagnosisModel>(5);
        }
        addedDiagnosis.add(added);
        enableUpdate();
    }

    /**
     * 更新リストに追加する。
     * @param updated 更新されたRegisteredDiagnosisModel
     */
    private void addUpdatedList(RegisteredDiagnosisModel updated) {

        // ディタッチオブジェクトの時
        if (updated.getId() != 0L) {
            // 更新リストに追加する
            if (updatedDiagnosis == null) {
                updatedDiagnosis = new ArrayList<RegisteredDiagnosisModel>(5);
            }
            // 同じものが再度更新されているケースを除く
            if (!updatedDiagnosis.contains(updated)) {
                updatedDiagnosis.add(updated);
            }
            enableUpdate();
        }
    }

    /**
     * 追加及び更新リストをクリアする。
     */
    private void clearDiagnosisList() {
        if (addedDiagnosis != null && addedDiagnosis.size() > 0) {
            int index = 0;
            while (addedDiagnosis.size() > 0) {
                addedDiagnosis.remove(index);
            }
        }

        if (updatedDiagnosis != null && updatedDiagnosis.size() > 0) {
            int index = 0;
            while (updatedDiagnosis.size() > 0) {
                updatedDiagnosis.remove(index);
            }
        }
    }

    /**
     *
     */
    private void disableUpdate() {
        setDirty(false);
        updateButton.setEnabled(false);
    }

    /**
     *
     */
    private void enableUpdate() {
        setDirty(true);
        updateButton.setEnabled(true);
    }

    /**
     * 傷病名件数を返す。
     * @return 傷病名件数
     */
    public int getDiagnosisCount() {
        return diagnosisCount;
    }

    /**
     * 傷病名件数を設定する。
     * @param cnt 傷病名件数
     */
    public void setDiagnosisCount(int cnt) {
        diagnosisCount = cnt;
        try {
            String val = String.valueOf(diagnosisCount);
            countField.setText(val);
        } catch (RuntimeException e) {
            countField.setText("");
        }
    }

    /**
     * ImageIcon を返す
     */
    private ImageIcon createImageIcon(String name) {
        String res = RESOURCE_BASE + name;
        return new ImageIcon(this.getClass().getResource(res));
    }

    /**
     * 傷病名スタンプを取得する worker を起動する。
     * @param stampList
     * @param insertRow
     */
    public void importStampList(final List<ModuleInfoBean> stampList, final int insertRow) {

        final RemoteStampDelegater sdl = new RemoteStampDelegater();

        DBTask task = new DBTask<List<StampModel>>(getParentContext()) {

            @Override
            protected List<StampModel> doInBackground() throws Exception {
                List<StampModel> result = sdl.getStamp(stampList);
                return result;
            }

            @Override
            protected void succeeded(List<StampModel> list) {
                if ((!sdl.isError()) && list != null) {
                    for (int i = list.size() - 1; i > -1; i--) {
                        insertStamp((StampModel) list.get(i), insertRow);
                    }
                }
            }
        };
        task.execute();
    }

    /**
     * 傷病名スタンプをデータベースから取得しテーブルへ挿入する。
     * Worker Thread で実行される。
     * @param stampInfo
     */
    private void insertStamp(StampModel sm, int row) {

        if (sm != null) {
            //  RegisteredDiagnosisModel module = (RegisteredDiagnosisModel) BeanUtils.xmlDecode(sm.getStampBytes());
            RegisteredDiagnosisModel module = (RegisteredDiagnosisModel) sm.toInfoModel();
            // 今日の日付を疾患開始日として設定する
            GregorianCalendar gc = new GregorianCalendar();
            String today = MMLDate.getDate(gc);
            module.setStartDate(today);

            row = tableModel.getObjectCount() == 0 ? 0 : row;
            int cnt = tableModel.getObjectCount();
            if (row == 0 && cnt == 0) {
                tableModel.addRow(module);
            } else if (row < cnt) {
                tableModel.insertRow(row, module);
            } else {
                tableModel.addRow(module);
            }

            //
            // row を選択する
            //
            diagTable.getSelectionModel().setSelectionInterval(row, row);
            addAddedList(module);
        }
    }

    /**
     * 傷病名エディタを開く。
     */
    public void openEditor2() {
        StampEditorDialog stampEditor = new StampEditorDialog("diagnosis", null);

        // 編集終了、値の受け取りにこのオブジェクトを設定する
        stampEditor.addPropertyChangeListener(StampEditorDialog.VALUE_PROP, this);
        stampEditor.start();
    }

    /**
     * 傷病名エディタを開く。
     */
    private void openDiagnosisEditor(RegisteredDiagnosisModel entry) {

        DiagnosisEditorDialog diagnosisEditor = new DiagnosisEditorDialog(null, true, entry);
        diagnosisEditor.setVisible(true);
        if (diagnosisEditor.isDirty()) {
            addUpdatedList(entry);
            enableUpdate();
        } else {
            disableUpdate();
        }
    }

    /**
     * 傷病名エディタからデータを受け取りテーブルへ追加する。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        ArrayList list = (ArrayList) e.getNewValue();
        if (list == null || list.size() == 0) {
            return;
        }
        int len = list.size();
        // 今日の日付を疾患開始日として設定する
        GregorianCalendar gc = new GregorianCalendar();
        String today = MMLDate.getDate(gc);
        if (ascend) {
            // 昇順なのでテーブルの最後へ追加する
            for (int i = 0; i < len; i++) {
                RegisteredDiagnosisModel module = (RegisteredDiagnosisModel) list.get(i);
                module.setStartDate(today);
                tableModel.addRow(module);
                addAddedList(module);
            }
        } else {
            // 降順なのでテーブルの先頭へ追加する
            for (int i = len - 1; i > -1; i--) {
                RegisteredDiagnosisModel module = (RegisteredDiagnosisModel) list.get(i);
                module.setStartDate(today);
                tableModel.insertRow(0, module);
                addAddedList(module);
            }
        }
    }

    /**
     *
     * @param message
     */
    private void showDialogForOutcome(String message) {
        JOptionPane.showMessageDialog(
                getParentContext().getFrame(),
                message,
                GlobalConstants.getFrameTitle("病名チェック"),
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @param rd
     * @return
     */
    private boolean isValidOutcome(RegisteredDiagnosisModel rd) {

        if (rd.getOutcome() == null) {
            return true;
        }
        String start = rd.getStartDate();
        String end = rd.getEndDate();
        if (start == null) {
            showDialogForOutcome("疾患の開始日がありません。");
            return false;
        }
        if (end == null) {
            showDialogForOutcome("疾患の終了日がありません。");
            return false;
        }

        Date startDate = null;
        Date endDate = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            start = start.replaceAll("/", "-");
            end = end.replaceAll("/", "-");
            startDate = sdf.parse(start);
            endDate = sdf.parse(end);
        } catch (ParseException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("日付のフォーマットが正しくありません。");
            sb.append("\n");
            sb.append("「yyyy-MM-dd」の形式で入力してください。");
            sb.append("\n");
            sb.append("右クリックでカレンダが使用できます。");
            showDialogForOutcome(sb.toString());
            return false;
        }

        if (endDate.before(startDate)) {
            showDialogForOutcome("疾患の終了日が開始日以前になっています。");
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean prepare() {
        //     final boolean sendDiagnosis = GlobalVariables.getSendDiagnosis() && ((ChartWindow) getParentContext()).getCLAIMListener() != null ? true : false;

        // continue to save
        Date confirmed = new Date();

        if (addedDiagnosis != null && addedDiagnosis.size() > 0) {
            for (RegisteredDiagnosisModel rd : addedDiagnosis) {
                // 開始日、終了日はテーブルから取得している
                // TODO confirmed, recorded
                rd.setKarte(getParentContext().getKarte());           // Karte
                rd.setCreator(GlobalVariables.getUserModel());          // Creator
                rd.setConfirmed(confirmed);                     // 確定日
                rd.setRecorded(confirmed);                      // 記録日
                rd.setStatus(IInfoModel.STATUS_FINAL);

                // 開始日=適合開始日 not-null
                if (rd.getStarted() == null) {
                    rd.setStarted(confirmed);
                }

                // TODO トラフィック
                rd.setPatientLiteModel(getParentContext().getPatient().patientAsLiteModel());
                rd.setUserLiteModel(GlobalVariables.getUserModel().getLiteModel());

                // 転帰をチェックする
                if (!isValidOutcome(rd)) {
                    return false;
                }
            }
        }

        if (updatedDiagnosis != null && updatedDiagnosis.size() > 0) {
            for (RegisteredDiagnosisModel rd : updatedDiagnosis) {
                // 現バージョンは上書きしている
                rd.setCreator(GlobalVariables.getUserModel());
                rd.setConfirmed(confirmed);
                rd.setRecorded(confirmed);
                rd.setStatus(IInfoModel.STATUS_FINAL);

                // TODO トラフィック
                rd.setPatientLiteModel(getParentContext().getPatient().patientAsLiteModel());
                rd.setUserLiteModel(GlobalVariables.getUserModel().getLiteModel());

                // 転帰をチェックする
                if (!isValidOutcome(rd)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 新規及び変更された傷病名を保存する。
     */
    private boolean save() {
        final boolean sendDiagnosis = GlobalVariables.getSendDiagnosis() && ((ChartWindow) getParentContext()).getCLAIMListener() != null ? true : false;
        RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
        DiagnosisPutTask task = new DiagnosisPutTask(getParentContext(), addedDiagnosis, updatedDiagnosis, sendDiagnosis, ddl);
        task.execute();
        disableUpdate();
        return true;
    }

    /**
     *
     */
    public void onSave() {
        if (prepare()) {
            save();
        }
    }

    /**
     * 指定期間以降の傷病名を検索してテーブルへ表示する。
     * バッググランドスレッドで実行される。
     *
     * MEMO: 日付は開始日を基準にしている
     */
    private void getDiagnosisHistory(Date past, final ListFilter filter) {

        final DiagnosisSearchSpec spec = new DiagnosisSearchSpec();
        spec.setCode(DiagnosisSearchSpec.PATIENT_SEARCH);
        spec.setKarteId(getParentContext().getKarte().getId());
        if (past != null) {
            spec.setFromDate(past);
        }

        final RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();

        DBTask task = new DBTask<List>(getParentContext()) {

            @Override
            protected List doInBackground() throws Exception {
                List source = ddl.getDiagnosisList(spec);
                List result = filter.filter(source);
                return result;
            }

            @Override
            protected void succeeded(List list) {
                if (!ddl.isError() && list != null && list.size() > 0) {
                    if (ascend) {
                        Collections.sort(list);
                        RegisteredDiagnosisModel rd = (RegisteredDiagnosisModel) list.get(0);
                        dolphinFirstDate = rd.getStartDate();
                    } else {
                        Collections.sort(list, Collections.reverseOrder());
                        int index = list.size() - 1;
                        RegisteredDiagnosisModel rd = (RegisteredDiagnosisModel) list.get(index);
                        dolphinFirstDate = rd.getStartDate();
                    }
                    //  tableModel.setObjectList(list);
                    //  setDiagnosisCount(list.size());
                }
                tableModel.setObjectList(list);
                setDiagnosisCount(list.size());
            }
        };
        task.execute();
    }

    /**
     * 選択された行のデータを削除する。
     * @return
     */
    public boolean delete() {
        // 選択された行のオブジェクトを取得する
        final int row = diagTable.getSelectedRow();
        final RegisteredDiagnosisModel model = tableModel.getObject(row);
        if (model == null) {
            return true;
        }

        // まだデータベースに登録されていないデータの場合
        // テーブルから削除してリターンする
        if (model.getId() == 0L) {
            if (addedDiagnosis != null && addedDiagnosis.contains(model)) {
                tableModel.deleteRow(row);
                setDiagnosisCount(tableModel.getObjectCount());
                addedDiagnosis.remove(model);
                enableUpdate();
                return true;
            }
        }

        // ディタッチオブジェクトの場合はデータベースから削除する
        // 削除の場合はその場でデータベースの更新を行う 2006-03-25
        final List<Long> list = new ArrayList<Long>(1);
        list.add(new Long(model.getId()));

        final RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();

        DBTask task = new DBTask<Void>(getParentContext()) {

            @Override
            protected Void doInBackground() throws Exception {
                ddl.removeDiagnosis(list);
                return null;
            }

            @Override
            protected void succeeded(Void result) {
                if (!ddl.isError()) {
                    tableModel.deleteRow(row);
                    setDiagnosisCount(tableModel.getObjectCount());
                    // 更新リストにある場合
                    // 更新リストから取り除く
                    if (updatedDiagnosis != null) {
                        updatedDiagnosis.remove(model);
                    }
                    enableUpdate();
                }
            }
        };
        task.execute();
        return true;
    }

    /**
     *
     * @param diagnosis
     * @return
     */
    private boolean checkOutcome(RegisteredDiagnosisModel diagnosis) {
        if (diagnosis.getOutcome() != null) {
            if (!diagnosis.getOutcome().equals("")) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param diagnosis
     * @return
     */
    private boolean findDiagnosis(RegisteredDiagnosisModel diagnosis) {
        for (Object model : tableModel.getObjectList()) {
            RegisteredDiagnosisModel findDiagnosis = (RegisteredDiagnosisModel) model;
            if (findDiagnosis != null) {
                if (!findDiagnosis.getStatus().equals(ORCA_RECORD)) {
                    if (findDiagnosis.getDiagnosis().equals(diagnosis.getDiagnosis())) {
                        if (!checkOutcome(findDiagnosis)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     */
    public void importFromOrca() {
        final RegisteredDiagnosisModel model = (RegisteredDiagnosisModel) tableModel.getObject(diagTable.getSelectedRow());
        if (model != null) {
            if (findDiagnosis(model) && (!checkOutcome(model))) {
                diagTable.changeSelection(diagTable.getSelectedRow() + 1, 0, false, false);
            } else {
                //        final boolean sendDiagnosis = GlobalVariables.getSendDiagnosis() && ((ChartWindow) getParentContext()).getCLAIMListener() != null ? true : false;
                // continue to save
                Date confirmed = new Date();
                // 開始日、終了日はテーブルから取得している
                // TODO confirmed, recorded
                model.setKarte(getParentContext().getKarte());           // Karte
                model.setCreator(GlobalVariables.getUserModel());          // Creator
                model.setConfirmed(confirmed);                     // 確定日
                model.setRecorded(confirmed);                      // 記録日
                model.setStatus(IInfoModel.STATUS_FINAL);

                // 開始日=適合開始日 not-null
                if (model.getStarted() == null) {
                    model.setStarted(confirmed);
                }

                model.getDiagnosis();
                model.getDiagnosisCode();
                // TODO トラフィック
                model.setPatientLiteModel(getParentContext().getPatient().patientAsLiteModel());
                model.setUserLiteModel(GlobalVariables.getUserModel().getLiteModel());

                // 転帰をチェックする
                if (isValidOutcome(model)) {
                    ArrayList<RegisteredDiagnosisModel> _addedDiagnosis = new ArrayList<RegisteredDiagnosisModel>();
                    _addedDiagnosis.add(model);

                    DocumentDelegater ddl = new RemoteDocumentDelegater();
                    DiagnosisPutTask task = new DiagnosisPutTask(getParentContext(), _addedDiagnosis, null, false, ddl);
                    task.execute();

                    diagTable.doLayout();
                    diagTable.changeSelection(diagTable.getSelectedRow() + 1, 0, false, false);
                }
            }
        }
    }

    /**
     * ORCAに登録されている病名を取り込む。（テーブルへ追加する）
     * 検索後、ボタンを disabled にする。
     */
    public void viewOrca() {
        // 患者IDを取得する
        String patientId = getParentContext().getPatient().getPatientId();
        // 抽出期間から検索範囲の最初の日を取得する
        NameValuePair pair = (NameValuePair) extractionCombo.getSelectedItem();
        int past = Integer.parseInt(pair.getValue());
        Date date = null;
        boolean isTenki = false;
        if (past != 0) {
            if (past == 1) {
                isTenki = true;
            }
            GregorianCalendar today = new GregorianCalendar();
            today.add(GregorianCalendar.MONTH, past);
            today.clear(Calendar.HOUR_OF_DAY);
            today.clear(Calendar.MINUTE);
            today.clear(Calendar.SECOND);
            today.clear(Calendar.MILLISECOND);
            date = today.getTime();

        } else {
            date = new Date(0l);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String from = sdf.format(date);

        String to = sdf.format(new Date());
        final Component comp = (Component) this;
        // DAOを生成する
        final SqlOrcaView dao = new SqlOrcaView();

        // ReflectMonitor を生成する
        final ReflectMonitor rm = new ReflectMonitor();
        rm.setReflection(dao, "getDiseaseFromOrca", new Class[]{String.class, String.class, String.class, Boolean.class, Boolean.class}, new Object[]{patientId, from, to, new Boolean(isTenki), new Boolean(ascend)});
        rm.setMonitor(SwingUtilities.getWindowAncestor(comp), ORCA_VIEW, "病名を検索しています...  ", 200, 60 * 1000);

        // ReflectMonitor の結果State property の束縛リスナを生成する
        PropertyChangeListener pl = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                int state = ((Integer) e.getNewValue()).intValue();
                switch (state) {
                    case ReflectMonitor.DONE:
                        if (dao.isNoError()) {
                            List list = (List) rm.getResult();
                            if (list != null && list.size() > 0) {
                                if (ascend) {
                                    Collections.sort(list);
                                } else {
                                    Collections.sort(list, Collections.reverseOrder());
                                }
                                tableModel.addRows(list);
                            }
                        } else {
                            String errMsg = dao.getErrorMessage();
                            String title = GlobalConstants.getFrameTitle(ORCA_VIEW);
                            JOptionPane.showMessageDialog(comp, errMsg, title, JOptionPane.WARNING_MESSAGE);
                        }

                        break;

                    case ReflectMonitor.TIME_OVER:
                        orcaButton.setEnabled(true);
                        break;

                    case ReflectMonitor.CANCELED:
                        orcaButton.setEnabled(true);
                        break;
                default: LogWriter.fatal(getClass(), "case default");
                }
            }
        };
        rm.addPropertyChangeListener(pl);

        // Block し、メソッドの実行を開始する
        orcaButton.setEnabled(false);
        rm.start();
    }

    //元々はAbstractChartDocumentから継承
    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    @Override
    public IChart getParentContext() {
        return parent;
    }

    // @Override
    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     */
    private boolean print() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isDirty() {
        return dirty;
    }

    /**
     *
     * @param dirty
     */
    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     *
     * @return
     */
    public boolean isReadOnly() {
        return parent.isReadOnly();
    }

    /**
     *
     */
    public void disableMenus() {
        // このウインドウに関連する全てのメニューをdisableにする
        ChartMediator mediator = getParentContext().getChartMediator();
        mediator.disableMenus(CHART_MENUS);
    }

    /**
     * 共通の警告表示を行う。
     * @param title
     * @param message
     */
    protected void warning(String title, String message) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        JOptionPane.showMessageDialog(parent, message, GlobalConstants.getFrameTitle(title), JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @return
     */
    @Override
    public List<JTabbedPane> getTabbedPanels() {
        return null;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean update(Object o) {
        return true;
    }

    /**
     * PopupListener　MEMO:リスナー
     */
    class PopupListener extends MouseAdapter implements PropertyChangeListener {

        private JPopupMenu popup;
        private JTextField tf;

        /**
         *
         * @param tf
         */
        public PopupListener(JTextField tf) {
            this.tf = tf;
            tf.addMouseListener(this);
        }

        /**
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                showPopup(e);
            }
        }

        /**
         *
         * @param e
         */
        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup = new JPopupMenu();
                CalendarCardPanel cc = new CalendarCardPanel(GlobalConstants.getEventColorTable());
                cc.addPropertyChangeListener(CalendarCardPanel.PICKED_DATE, this);
                cc.setCalendarRange(new int[]{-960, 960});
                popup.insert(cc, 0);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }

        /**
         *
         * @param e
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals(CalendarCardPanel.PICKED_DATE)) {
                SimpleDate sd = (SimpleDate) e.getNewValue();
                tf.setText(SimpleDate.simpleDateToMmldate(sd));
                popup.setVisible(false);
                popup = null;
            }
        }
    }

    /**
     * DiagnosisPutTask
     */
    class DiagnosisPutTask extends DBTask<List<Long>> {

        //private IChart chart;
        private List<RegisteredDiagnosisModel> added;
        private List<RegisteredDiagnosisModel> updated;
        private boolean sendClaim;
        private DocumentDelegater ddl;

        /**
         *
         * @param chart
         * @param added
         * @param updated
         * @param sendClaim
         * @param ddl
         */
        public DiagnosisPutTask(IChart chart, List<RegisteredDiagnosisModel> added, List<RegisteredDiagnosisModel> updated, boolean sendClaim, DocumentDelegater ddl) {
            super(chart);
            this.added = added;
            this.updated = updated;
            this.sendClaim = sendClaim;
            this.ddl = ddl;
        }

        /**
         *
         * @return
         * @throws Exception
         */
        @Override
        protected List<Long> doInBackground() throws Exception {
            if (updated != null && updated.size() > 0) {
                ddl.updateDiagnosis(updated);
            }
            List<Long> result = null;
            // 保存する
            if (added != null && added.size() > 0) {
                result = ddl.putDiagnosis(added);
                if (!ddl.isError()) {
                    for (int i = 0; i < added.size(); i++) {
                        long pk = result.get(i).longValue();
                        RegisteredDiagnosisModel rd = (RegisteredDiagnosisModel) added.get(i);
                        rd.setId(pk);
                    }
                }
            }

            //
            // 追加病名を CLAIM 送信する
            //
            if (sendClaim && added != null && added.size() > 0) {
                //             LogWriter.debug(getClass(), "sendClaim Diagnosis");
                // DocInfo & RD をカプセル化したアイテムを生成する
                ArrayList<DiagnosisModuleItem> moduleItems = new ArrayList<DiagnosisModuleItem>();
                for (RegisteredDiagnosisModel rd : added) {
                    DocInfoModel docInfo = new DocInfoModel();
                    docInfo.setDocId(GUIDGenerator.generate(docInfo));
                    docInfo.setTitle(IInfoModel.DEFAULT_DIAGNOSIS_TITLE);
                    docInfo.setPurpose(IInfoModel.PURPOSE_RECORD);

                    docInfo.setFirstConfirmDate(ModelUtils.getDateTimeAsObject(rd.getFirstConfirmDate()));
                    docInfo.setConfirmDate(ModelUtils.getDateTimeAsObject(rd.getConfirmDate()));

                    //     docInfo.setFirstConfirmDate(ModelUtils.getDateTimeAsObject(rd.getConfirmDate()));
                    //       docInfo.setConfirmDate(ModelUtils.getDateTimeAsObject(rd.getFirstConfirmDate()));

                    DiagnosisModuleItem mItem = new DiagnosisModuleItem();
                    mItem.setDocInfo(docInfo);
                    mItem.setRegisteredDiagnosisModule(rd);
                    moduleItems.add(mItem);
                }

                // ヘルパー用の値を生成する
                String confirmDate = added.get(0).getConfirmDate();
                PatientLiteModel patient = added.get(0).getPatientLiteModel();
                // ヘルパークラスを生成する
                DiseaseHelper dhl = new DiseaseHelper(context, patient.getPatientId(), confirmDate, moduleItems);
                MessageBuilder mb = new MessageBuilder();
                //ＯＲＣＡは"－"を表示できないため。
                String claimMessage = StringSubstitution.Substitution(mb.build(dhl), "－", "―");
                //          DebugDump.dump("diagnosisClaim.log", claimMessage);
                ClaimMessageEvent event = new ClaimMessageEvent(this, patient, claimMessage);
                event.setTitle(IInfoModel.DEFAULT_DIAGNOSIS_TITLE);
                event.setConfirmDate(confirmDate);
                SendClaimImpl claimListener = ((ChartWindow) context).getCLAIMListener();
                if (claimListener != null) {
                    claimListener.claimMessageEvent(event);
                }
            }

            //
            // 更新された病名を CLAIM 送信する
            //
            if (sendClaim && updated != null && updated.size() > 0) {
                ArrayList<DiagnosisModuleItem> moduleItems = new ArrayList<DiagnosisModuleItem>();
                for (RegisteredDiagnosisModel rd : updated) {
                    DocInfoModel docInfo = new DocInfoModel();
                    docInfo.setDocId(GUIDGenerator.generate(docInfo));
                    docInfo.setTitle(IInfoModel.DEFAULT_DIAGNOSIS_TITLE);
                    docInfo.setPurpose(IInfoModel.PURPOSE_RECORD);
                    docInfo.setFirstConfirmDate(ModelUtils.getDateTimeAsObject(rd.getConfirmDate()));
                    docInfo.setConfirmDate(ModelUtils.getDateTimeAsObject(rd.getFirstConfirmDate()));
                    DiagnosisModuleItem mItem = new DiagnosisModuleItem();
                    mItem.setDocInfo(docInfo);
                    mItem.setRegisteredDiagnosisModule(rd);
                    moduleItems.add(mItem);
                }

                // ヘルパー用の値を生成する
                String confirmDate = updated.get(0).getConfirmDate();
                PatientLiteModel patient = updated.get(0).getPatientLiteModel();
                // ヘルパークラスを生成する
                DiseaseHelper diseaseHelper = new DiseaseHelper(context, patient.getPatientId(), confirmDate, moduleItems);
                MessageBuilder messageBuilder = new MessageBuilder();
                //      String claimMessage = messageBuilder.build(diseaseHelper);
                //ＯＲＣＡは"－"を表示できないため。
                String claimMessage = StringSubstitution.Substitution(messageBuilder.build(diseaseHelper), "－", "―");
                //     DebugDump.dump("diagnosisClaim.log", claimMessage);
                ClaimMessageEvent event = new ClaimMessageEvent(this, patient, claimMessage);
                event.setTitle(IInfoModel.DEFAULT_DIAGNOSIS_TITLE);
                event.setConfirmDate(confirmDate);
                SendClaimImpl claimListener = ((ChartWindow) context).getCLAIMListener();
                if (claimListener != null) {
                    claimListener.claimMessageEvent(event);
                }
            }
            return result;
        }

        @Override
        protected void succeeded(List<Long> list) {
            clearDiagnosisList();
        }
    }

    /**
     *
     */
    class DolphinOrcaRenderer extends DefaultTableCellRenderer {

        /** Creates new IconRenderer */
        public DolphinOrcaRenderer() {
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
            RegisteredDiagnosisModel rd = tableModel.getObject(row);
            // ORCA レコードかどうかを判定する
            boolean orca = (rd != null && rd.getStatus() != null && rd.getStatus().equals(ORCA_RECORD)) ? true : false;
            if (isSelected) {
                // 選択されている時はデフォルトの表示を行う
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                // 選択されていない時
                // Foreground をデフォルトにする
                // ORCA データの時は背景を変える
                // それ以外は奇数と偶数で色分けする
                setForeground(table.getForeground());
                if (orca) {
                    setBackground(ORCA_BACK);
                } else {
                    if (row % 2 == 0) {
                        setBackground(EVEN_COLOR);
                    } else {
                        setBackground(ODD_COLOR);
                    }
                }
            }

            if (value != null) {
                if (value instanceof String) {
                    if (col == 0) {
                        CombinedStringParser combindDisease = new CombinedStringParser((String) value);
                        this.setText(combindDisease.toPlainString());
                    } else {
                        this.setText((String) value);
                    }
                } else {
                    this.setText(value.toString());
                }
            } else {
                this.setText("");
            }
            return this;
        }
    }
}
