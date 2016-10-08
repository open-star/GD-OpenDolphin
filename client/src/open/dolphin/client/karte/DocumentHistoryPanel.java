/*
 * DocumentHistoryPanel.java
 *
 * Created on 2007/11/29, 16:10
 */
package open.dolphin.client.karte;

import open.dolphin.project.GlobalConstants;
import java.awt.Cursor;
import open.dolphin.container.NameValuePair;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import open.dolphin.client.AutoKanjiListener;
import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;

import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.dto.DocumentSearchSpec;
import open.dolphin.helper.DBTask;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.project.GlobalVariables;
import open.dolphin.queries.DolphinQuery;
import open.dolphin.table.DocumentHistoryRenderer;
import open.dolphin.table.ObjectReflectTableModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;

/**
 *　カルテの左側に表示されるカルテの履歴　MEMO:画面
 * @author  kazm
 */
public class DocumentHistoryPanel extends JPanel implements IChartDocument {

    /**
     *
     */
    public static final String TITLE = "文書履歴";
    /**
     *
     */
    public static final String DOCUMENT_TYPE = "documentTypeProp";
    /**
     *
     */
    public static final String SELECTED_HISTORIES = "selectedHistories";
    /**
     *
     */
    public static final String SELECTED_KARTES = "selectedKartes";
    /**
     *
     */
    public static final String HITORY_UPDATED = "historyUpdated";
    private ObjectReflectTableModel<DocInfoModel> tableModel;    // 文書履歴テーブル
    private PropertyChangeSupport boundSupport;    // 束縛サポート
    private DocInfoModel[] selectedHistories;    // 選択された文書情報(DocInfo)の配列
    private String extractionContent;    // 抽出コンテント(文書種別)
    private Date extractionPeriod;    // 抽出開始日
    private int autoFetchCount;    // 自動的に取得する文書数
    private boolean ascending;    // 昇順降順のフラグ
    private boolean showModified = true;    // 修正版も表示するかどうかのフラグ
    private boolean showUnsend = true;    // 修正版も表示するかどうかのフラグ
    private boolean showSend = true;    // 修正版も表示するかどうかのフラグ
    private boolean start;    // フラグ
    private NameValuePair[] contentObject;
    private NameValuePair[] extractionObjects;
    //  private BlockKeyListener blockKeyListener;    // Key入力をブロックするリスナ
    private IChart parent;    // context
    private String title;
    private boolean newest;

    /**
     *
     * @return
     */
    @Override
    public IChartDocument.TYPE getType() {
        return IChartDocument.TYPE.DocumentHistoryPanel;
    }

    /**
     * 文書履歴オブジェクトを生成する。
     * @param parent
     * @param keyword
     */
    public DocumentHistoryPanel(IChart parent, String keyword) {
        this.title = TITLE;
        this.newest = false;
        this.parent = parent;
        boundSupport = new PropertyChangeSupport(this);
        initComponents();
        initCustomComponents();

        connect();
        start = true;
        searchField.setText(keyword);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        cntLbl = new javax.swing.JLabel();
        docTypeCombo = new javax.swing.JComboBox();
        periodCombo = new javax.swing.JComboBox();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

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
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        jScrollPane1.setViewportView(table);

        cntLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(DocumentHistoryPanel.class);
        cntLbl.setText(resourceMap.getString("cntLbl.text")); // NOI18N
        cntLbl.setName("cntLbl"); // NOI18N

        docTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "カルテ", "紹介状", "紹介状返書" }));
        docTypeCombo.setName("docTypeCombo"); // NOI18N
        docTypeCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                docTypeComboItemStateChanged(evt);
            }
        });

        periodCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1ヶ月", "3ヶ月", "半年", "1年", "2年", "3年", "全て" }));
        periodCombo.setSelectedIndex(3);
        periodCombo.setName("periodCombo"); // NOI18N
        periodCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                periodComboItemStateChanged(evt);
            }
        });

        searchField.setText(resourceMap.getString("searchField.text")); // NOI18N
        searchField.setName("searchField"); // NOI18N
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        searchButton.setText(resourceMap.getString("searchButton.text")); // NOI18N
        searchButton.setName("searchButton"); // NOI18N
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(docTypeCombo, 0, 133, Short.MAX_VALUE)
                        .add(18, 18, 18)
                        .add(periodCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, searchField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(searchButton)
                    .add(layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(cntLbl, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(searchField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(searchButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(docTypeCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(cntLbl)
                    .add(periodCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 
     * @param evt
     */
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        String keyword = searchField.getText();
        if (keyword.equals("")) {
            getDocumentHistory();
        } else {
            Search(keyword);
        }
    }//GEN-LAST:event_searchButtonActionPerformed
    /**
     * 
     * @param evt
     */
    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        String keyword = searchField.getText();
        if (keyword.equals("")) {
            getDocumentHistory();
        } else {
            Search(keyword);
        }
    }//GEN-LAST:event_searchFieldActionPerformed
    /**
     * 
     * @param evt
     */
    private void docTypeComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_docTypeComboItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            int index = docTypeCombo.getSelectedIndex();
            NameValuePair pair = contentObject[index];
            setExtractionContent(pair.getValue());
        }
    }//GEN-LAST:event_docTypeComboItemStateChanged
    /**
     * 
     * @param evt
     */
    private void periodComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_periodComboItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            int index = periodCombo.getSelectedIndex();
            NameValuePair pair = extractionObjects[index];
            String value = pair.getValue();
            int addValue = Integer.parseInt(value);
            GregorianCalendar today = new GregorianCalendar();
            today.add(GregorianCalendar.MONTH, addValue);
            today.clear(Calendar.HOUR_OF_DAY);
            today.clear(Calendar.MINUTE);
            today.clear(Calendar.SECOND);
            today.clear(Calendar.MILLISECOND);
            setExtractionPeriod(today.getTime());
        }
    }//GEN-LAST:event_periodComboItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cntLbl;
    private javax.swing.JComboBox docTypeCombo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox periodCombo;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    /**
     * GUI コンポーネントを生成する。
     */
    private void initCustomComponents() {
        // 履歴テーブルのパラメータを取得する
        String[] columnNames = {"診療日", "更新日", "内容", "状態"};
        String[] methodNames = {"getFirstConfirmDateTrimTime", "getConfirmDateTrimTime", "getTitle", "getStatusByName"};
        Class[] columnClasses = {String.class, String.class, String.class, String.class};
        int[] columnWidth = {110, 110, 110, 110};
        int startNumRows = 0;

        extractionObjects = GlobalConstants.getNameValuePair("docHistory.combo.period");

        // 文書履歴テーブルを生成する
        tableModel = new ObjectReflectTableModel<DocInfoModel>(columnNames, startNumRows, methodNames, columnClasses) {

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 2);
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col == 2) {
                    if (value != null) {
                        if (!value.equals("")) {
                            Object docInfoModel = getObject(row);
                            if (docInfoModel != null) {
                                // 文書タイトルを変更し通知する
                                DocInfoModel docInfo = (DocInfoModel) docInfoModel;
                                docInfo.setTitle((String) value);
                                titleChanged(docInfo);
                            }
                        }
                    }
                }
            }
        };

        table.setModel(tableModel);
        // カラム幅を調整する
        for (int i = 0; i < columnWidth.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidth[i]);
        }

        // タイトルカラムに IME ON を設定する
        JTextField tf = new JTextField();

        tf.addFocusListener(AutoKanjiListener.getInstance());

        TableColumn column = table.getColumnModel().getColumn(1);

        column.setCellEditor(new DefaultCellEditor(tf));

        // 奇数偶数レンダラを設定する
        table.setDefaultRenderer(Object.class, new DocumentHistoryRenderer());

        // 文書種別(コンテントタイプ) ComboBox を生成する
        contentObject = new NameValuePair[3];
        contentObject[0] = new NameValuePair("カルテ", IInfoModel.DOCTYPE_KARTE);
        contentObject[1] = new NameValuePair("紹介状", IInfoModel.DOCTYPE_LETTER);
        contentObject[2] = new NameValuePair("紹介状返書", IInfoModel.DOCTYPE_LETTER_REPLY);
    }

    /**
     * カルテヒストリの選択時のハンドラを登録
     */
    private void connect() {
        // 履歴テーブルで選択された行の文書を表示する
        ListSelectionModel slm = table.getSelectionModel();
        slm.addListSelectionListener(new ListSelectionListener() {

            /**
             * カルテヒストリを選択されると動く
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    //     JTable table = view.getTable();
                    int[] selectedRows = table.getSelectedRows();
                    if (selectedRows.length > 0) {
                        List<DocInfoModel> list = new ArrayList<DocInfoModel>(1);
                        for (int i = 0; i < selectedRows.length; i++) {
                            DocInfoModel obj = tableModel.getObject(selectedRows[i]);
                            if (obj != null) {
                                list.add(obj);
                            }
                        }
                        DocInfoModel[] selected = (DocInfoModel[]) list.toArray(new DocInfoModel[list.size()]);
                        if (selected != null && selected.length > 0) {
                            setSelectedHistories(selected);
                        } else {
                            setSelectedHistories((DocInfoModel[]) null);
                        }
                    }
                }
            }
        });

        // Preference から文書種別を設定する
        extractionContent = IInfoModel.DOCTYPE_KARTE;

        // Preference から抽出期間を設定する
        int past = GlobalVariables.getPreferences().getInt(GlobalVariables.DOC_HISTORY_PERIOD, -12);	//past 12 months
        int index = NameValuePair.getIndex(String.valueOf(past), extractionObjects);
        periodCombo.setSelectedIndex(index);
        GregorianCalendar today = new GregorianCalendar();
        today.add(GregorianCalendar.MONTH, past);
        today.clear(Calendar.HOUR_OF_DAY);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);
        setExtractionPeriod(today.getTime());

        // Preference から自動文書取得数を設定する
        autoFetchCount = GlobalVariables.getFetchKarteCount();

        // Preference から昇順降順を設定する
        ascending = GlobalVariables.getAscendingKarte();
    }

    /**
     * 
     * @param keyword
     */
    private void Search(String keyword) {
        Application app = GlobalConstants.getApplicationContext().getApplication();
        KarteSearchTask task = new KarteSearchTask(app, keyword);
        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        appCtx.getTaskService().execute(task);
    }

    /**
     * 選択された文書履歴(複数)を設定する。イベント通知を行う。
     * @param newSelected 選択された文書履歴(複数)
     */
    private void setSelectedHistories(DocInfoModel[] newSelected) {
        DocInfoModel[] old = selectedHistories;
        selectedHistories = newSelected;

        // リスナへ通知を行う
        if (selectedHistories != null) {
            boundSupport.firePropertyChange(SELECTED_HISTORIES, old, selectedHistories);
        }
    }

    /**
     * 
     * @param list
     * @param pk　
     * @return　ドキュメントインフォ
     */
    private Object findChild(List list, long pk) {
        for (Object docInfo : list) {
            if (((DocInfoModel) docInfo).getParentPk() == pk) {
                return docInfo;
            }
        }
        return null;
    }

    /**
     * 最新表示　自分のｐｋを親のｐｋとするレコードが無ければ自分は末端の子供。
     * @param list　リスト
     * @return　リスト
     */
    private List filtering(List list) {
        List result = new ArrayList();
        for (Object docInfo : list) {
            if (findChild(list, ((DocInfoModel) docInfo).getDocPk()) == null) {
                result.add(docInfo);
            }
        }
        return result;
    }

    /**
     * 抽出期間等が変化し、履歴を再取得した場合等の処理で、履歴テーブルの更新、 最初の行の自動選択、束縛プロパティの変化通知を行う。
     * @param paramHistory
     * @param newest
     */
    private void updateHistory(List paramHistory, boolean newest) {
        // ソーティングする
        if (paramHistory != null && paramHistory.size() > 0) {
            if (isAscending()) {
                Collections.sort(paramHistory);
            } else {
                Collections.sort(paramHistory, Collections.reverseOrder());
            }
        }

        List newHistory = null;
        if (newest) {//最新表示か
            newHistory = filtering(paramHistory);
        } else {
            newHistory = paramHistory;
        }

        tableModel.setObjectList(newHistory);  // 文書履歴テーブルにデータの Arraylist を設定する

        boundSupport.firePropertyChange(HITORY_UPDATED, false, true);        // 束縛プロパティの通知を行う

        if (newHistory != null && newHistory.size() > 0) {

            int cnt = newHistory.size();
            cntLbl.setText(String.valueOf(cnt) + " 件");
            int fetchCount = cnt > autoFetchCount ? autoFetchCount : cnt;

            // テーブルの最初の行の自動選択を行う
            int first = 0;
            int last = 0;

            if (isAscending()) {
                last = cnt - 1;
                first = cnt - fetchCount;
            } else {
                first = 0;
                last = fetchCount - 1;
            }

            // 自動選択
            table.getSelectionModel().addSelectionInterval(first, last);

            // 選択した行が表示されるようにスクロールする
            Rectangle r = table.getCellRect(first, last, true);
            table.scrollRectToVisible(r);

        } else {
            cntLbl.setText("0 件");
        }
    }

    /**
     * 文書履歴のタイトルを変更する。
     * @param docInfo
     */
    private void titleChanged(DocInfoModel docInfo) {
        if (docInfo != null && docInfo.getTitle() != null) {
            ChangeTitleTask task = new ChangeTitleTask(getParentContext(), docInfo, new RemoteDocumentDelegater());
            task.execute();
        }
    }

    /**
     * 検索パラメータの文書タイプを設定する。。
     * @param extractionContent 文書タイプ
     */
    private void setExtractionContent(String extractionContent) {
        String old = this.extractionContent;
        this.extractionContent = extractionContent;
        boundSupport.firePropertyChange(DOCUMENT_TYPE, old, this.extractionContent);
        getDocumentHistory();
    }

    /**
     * 検索パラメータの抽出期間を設定する。
     * @param extractionPeriod 抽出期間
     */
    private void setExtractionPeriod(Date extractionPeriod) {
        this.extractionPeriod = extractionPeriod;
        getDocumentHistory();
    }

    /**
     * 文書履歴表示の昇順/降順を設定する。
     * @param ascending 昇順の時 true
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
        getDocumentHistory();
    }

    /**
     * 文書履歴表示の昇順/降順を返す。
     * @return 昇順の時 true
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * 修正版を表示するかどうかを設定する。
     * @param showModified
     */
    public void setShowModified(boolean showModified) {
        setShowModified(showModified, true);
    }

    /**
     *
     * @param showModified
     * @param rewrite
     */
    public void setShowModified(boolean showModified, boolean rewrite) {
        this.showModified = showModified;
        if (rewrite) {
            getDocumentHistory();
        }
    }

    /**
     *
     * @param showUnsend
     */
    public void setShowUnsend(boolean showUnsend) {
        setShowUnsend(showUnsend, true);
    }

    /**
     *
     * @param showUnsend
     * @param rewrite
     */
    public void setShowUnsend(boolean showUnsend, boolean rewrite) {
        this.showUnsend = showUnsend;
        if (rewrite) {
            getDocumentHistory();
        }
    }

    /**
     *
     * @param showSend
     */
    public void setShowSend(boolean showSend) {
        setShowSend(showSend, true);
    }

    /**
     *
     * @param showSend
     * @param rewrite
     */
    public void setShowSend(boolean showSend, boolean rewrite) {
        this.showSend = showSend;
        if (rewrite) {
            getDocumentHistory();
        }
    }

    /**
     *
     * @param newest
     */
    public void setShowNewest(boolean newest) {
        setShowNewest(newest, true);
    }

    /**
     *
     * @param newest
     * @param rewrite
     */
    public void setShowNewest(boolean newest, boolean rewrite) {
        this.newest = newest;
        if (rewrite) {
            getDocumentHistory();
        }
    }

    /**
     * 履歴テーブルのコレクションを clear する。
     */
    public void clear() {
        tableModel.clear();
    }

    /**
     * 文書履歴を取得する。
     * 取得するパラメータ(患者ID、文書タイプ、抽出期間)はこのクラスの属性として
     * 定義されている。これらのパラメータは comboBox等で選択される。値が変化する度に
     * このメソッドがコールされる。
     */
    public void getDocumentHistory() {
        if (start && extractionPeriod != null && extractionContent != null) {

            // 検索パラメータセットのDTOを生成する
            DocumentSearchSpec spec = new DocumentSearchSpec();
            spec.setKarteId(getParentContext().getKarte().getId());	// カルテID
            spec.setDocType(extractionContent);			// 文書タイプ
            spec.setFromDate(extractionPeriod);			// 抽出期間開始
            spec.setIncludeModifid(showModified);		// 修正履歴
            spec.setIncludeUnsend(showUnsend);		// 修正履歴
            spec.setIncludeSend(showSend);		// 修正履歴

            spec.setLines(200);

            spec.setCode(DocumentSearchSpec.DOCTYPE_SEARCH);	// 検索タイプ
            spec.setAscending(ascending);

            RemoteDocumentDelegater delegater = new RemoteDocumentDelegater();

            DocInfoTask task = new DocInfoTask(getParentContext(), spec, delegater, newest);
            task.execute();
        }
    }

    /**
     * 束縛プロパティリスナを登録する。
     * @param propName プロパティ名
     * @param listener リスナ
     */
    @Override
    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
        boundSupport.addPropertyChangeListener(propName, listener);
    }

    /**
     * 束縛プロパティを削除する。
     * @param propName プロパティ名
     * @param listener リスナ
     */
    @Override
    public void removePropertyChangeListener(String propName, PropertyChangeListener listener) {
        boundSupport.removePropertyChangeListener(propName, listener);
    }

    @Override
    public void requestFocus() {
        table.requestFocusInWindow();
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

    /**
     *
     */
    @Override
    public void start() {
    }

    /**
     *
     */
    @Override
    public void stop() {
    }

    /**
     * 
     */
    @Override
    public void enter() {
    }

    /**
     *
     * @return
     */
    @Override
    public boolean prepare() {
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void setDirty(boolean dirty) {
    }

    /**
     *
     * @param command　コマンド
     * @return　ディスパッチできたか
     */
    @Override
    public boolean dispatchChartCommand(ChartCommand command) {
        return false;
    }

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

    class KarteSearchTask extends Task<List, Void> {

        private RemoteDocumentDelegater ddl;
        private Cursor currentCursor;
        private String FKeyword;

        public KarteSearchTask(Application app, String keyword) {
            super(app);
            ddl = new RemoteDocumentDelegater();
            FKeyword = keyword;
        }

        @Override
        protected List<DocInfoModel> doInBackground() throws Exception {
            currentCursor = getCursor();
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            List<DocInfoModel> result = new ArrayList<DocInfoModel>();
            DolphinQuery query = new DolphinQuery();
            query.addWhat("keyword", FKeyword);
            HashMap<Long, DocInfoModel> docIds = new HashMap<Long, DocInfoModel>();
            for (int index = 0; index < tableModel.getRowCount(); index++) {
                DocInfoModel item = tableModel.getObject(index);
                if (item != null) {
                    docIds.put(new Long(item.getDocPk()), item);
                }
            }
            List<DocumentModel> documents = ddl.getDocuments(new ArrayList(docIds.keySet()));
            for (DocumentModel document : documents) {
                DocInfoModel docInfo = docIds.get(document.getId());
                if (docInfo.search(query)) {
                    boolean found = false;
                    for (DocInfoModel doc : result) {
                        if (doc.getDocPk() == docInfo.getDocPk()) {
                            found = true;
                        }
                    }
                    if (!found) {
                        result.add(docInfo);
                    }
                    continue;
                }
                Set<ModuleModel> modules = document.getModules();
                for (ModuleModel module : modules) {
                    if (module.search(query)) {
                        boolean found = false;
                        for (DocInfoModel doc : result) {
                            if (doc.getDocPk() == docInfo.getDocPk()) {
                                found = true;
                            }
                        }
                        if (!found) {
                            result.add(docInfo);
                        }
                    }
                }
            }
            return result;
        }

        @Override
        protected void succeeded(List result) {
            ObjectReflectTableModel<DocInfoModel> tableModel = (ObjectReflectTableModel<DocInfoModel>) table.getModel();
            tableModel.setObjectList(result);
            int cnt = result != null ? result.size() : 0;
            String cntStr = String.valueOf(cnt);
            cntLbl.setText(cntStr + " 件");
            setCursor(currentCursor);
        }

        @Override
        protected void failed(Throwable cause) {
            setCursor(currentCursor);
        }

        @Override
        protected void cancelled() {
            setCursor(currentCursor);
        }
    }

    /**
     * キーボード入力をブロックするリスナクラス。
     */
    class BlockKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            e.consume();
        }

        /** Handle the key-pressed event from the text field. */
        @Override
        public void keyPressed(KeyEvent e) {
            e.consume();
        }

        /** Handle the key-released event from the text field. */
        @Override
        public void keyReleased(KeyEvent e) {
            e.consume();
        }
    }

    /**
     * 検索タスク。
     */
    class DocInfoTask extends DBTask<List<DocInfoModel>> {

        private RemoteDocumentDelegater ddl;// 検索パラメータを保持するオブジェクト
        private DocumentSearchSpec spec;
        private boolean newest;

        public DocInfoTask(IChart ctx, DocumentSearchSpec spec, RemoteDocumentDelegater ddl, boolean newest) {
            super(ctx);
            this.spec = spec;
            this.ddl = ddl;
            this.newest = newest;
        }

        @Override
        protected List<DocInfoModel> doInBackground() {
            List<DocInfoModel> result = (List<DocInfoModel>) ddl.getDocumentHistory(spec);
            if (!ddl.isError()) {
                return result;
            } else {
                return null;
            }
        }

        @Override
        protected void succeeded(List<DocInfoModel> result) {
            if (result != null) {
                updateHistory(result, newest);
            }
        }
    }

    /**
     * タイトル変更タスククラス。
     */
    class ChangeTitleTask extends DBTask<Boolean> {

        private DocInfoModel docInfo;        // DocInfo
        private RemoteDocumentDelegater ddl;        // Delegator

        public ChangeTitleTask(IChart ctx, DocInfoModel docInfo, RemoteDocumentDelegater ddl) {
            super(ctx);
            this.docInfo = docInfo;
            this.ddl = ddl;
        }

        @Override
        protected Boolean doInBackground() {
            ddl.updateTitle(docInfo);
            return !ddl.isError();
        }

        @Override
        protected void succeeded(Boolean result) {
        }
    }
}
