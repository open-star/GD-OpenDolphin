/*
 * AllergyView.java
 *
 * Created on 2007/11/29, 9:11
 */
package open.dolphin.plugin.physicalpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;
import open.dolphin.client.IChartDocument.TYPE;
import open.dolphin.project.GlobalSettings;
import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.helper.DBTask;
import open.dolphin.helper.IChartCommandAccepter.ChartCommand;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.infomodel.PhysicalModel;
import open.dolphin.project.GlobalVariables;
import open.dolphin.table.ObjectReflectTableModel;
import open.dolphin.table.OddEvenRowRenderer;

/**
 *　身長体重情報　MEMO:画面
 * @author  kazm
 */
public class PhysicalPanel extends javax.swing.JPanel implements IChartDocument {

    /**
     *
     */
    public static final String TITLE = "身長体重";
    private ObjectReflectTableModel<PhysicalModel> tableModel;
    private IChart parent;
    private String title;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroller = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(242, 86));

        scroller.setName("scroller"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "身長", "体重", "BMI", "測定日"
            }
        ));
        table.setName("table"); // NOI18N
        scroller.setViewportView(table);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(scroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.Plugin;
        //      return TYPE.PhysicalPanel;
    }

    /**
     *
     * @return
     */
    public javax.swing.JTable getTable() {
        return table;
    }

    /**
     * PhysicalInspectorオブジェクトを生成する。
     * @param parent
     */
    public PhysicalPanel(IChart parent) {
        this.title = TITLE;
        this.parent = parent;
        initComponents();
        initCustomComponents();
        update();
    }

    /**
     *
     */
    public void clear() {
        tableModel.clear();
    }

    /**
     * GUIコンポーネントを初期化する。
     */
    private void initCustomComponents() {

        String[] columnNames = {"身長", "体重", "BMI", "測定日"};  // カラム名
        int startNumRows = 1; // テーブルの初期行数
        String[] methodNames = {"getHeight", "getWeight", "getBmi", "getIdentifiedFormatDate"};// 属性値を取得するためのメソッド名

        // 身長体重テーブルを生成する
        tableModel = new ObjectReflectTableModel<PhysicalModel>(columnNames, startNumRows, methodNames, null);
        table.setModel(tableModel);
        table.setDefaultRenderer(Object.class, new OddEvenRowRenderer());
        table.getColumnModel().getColumn(2).setCellRenderer(new BMIRenderer());
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        // 列幅を調整する カット&トライ
        int[] cellWidth = new int[]{50, 50, 50, 110};
        for (int i = 0; i < cellWidth.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(cellWidth[i]);
        }

        // 右クリックによる追加削除のメニューを登録する
        table.addMouseListener(new MouseAdapter() {

            private void mabeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    JPopupMenu pop = new JPopupMenu();
                    JMenuItem item = new JMenuItem("追加");
                    pop.add(item);
                    item.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new PhysicalEditorPanel(PhysicalPanel.this);
                        }
                    });
                    final int row = table.rowAtPoint(e.getPoint());
                    if (tableModel.getObject(row) != null) {
                        pop.add(new JSeparator());
                        JMenuItem item2 = new JMenuItem("削除");
                        pop.add(item2);
                        item2.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                delete(row);
                            }
                        });
                    }
                    pop.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mabeShowPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mabeShowPopup(e);
            }
        });
    }

    /**
     *
     * @param ascending
     */
    private void scroll(boolean ascending) {

        int cnt = tableModel.getObjectCount();
        if (cnt > 0) {
            int row = 0;
            if (ascending) {
                row = cnt - 1;
            }
            Rectangle r = table.getCellRect(row, row, true);
            table.scrollRectToVisible(r);
        }
    }

    /**
     * 身長体重データを表示する。
     */
    public void update() {

        List listH = getParentContext().getKarte().getEntryCollection("height");
        List listW = getParentContext().getKarte().getEntryCollection("weight");

        List list = new ArrayList();

        // 身長体重ともある場合
        if (listH != null && listW != null) {

            for (int i = 0; i < listH.size(); i++) {

                PhysicalModel h = (PhysicalModel) listH.get(i);
                String memo = h.getMemo();
                if (memo == null) {
                    memo = h.getIdentifiedDate();
                }

                // 体重のメモが一致するものを見つける
                Object found = null;
                for (int j = 0; j < listW.size(); j++) {
                    PhysicalModel w = (PhysicalModel) listW.get(j);
                    String memo2 = w.getMemo();
                    if (memo2 == null) {
                        memo2 = w.getIdentifiedDate();
                    }
                    if (memo2.equals(memo)) {
                        found = w;
                        PhysicalModel m = new PhysicalModel();
                        m.setHeightId(h.getHeightId());
                        m.setHeight(h.getHeight());
                        m.setWeightId(w.getWeightId());
                        m.setWeight(w.getWeight());
                        m.setIdentifiedDate(h.getIdentifiedDate());
                        m.setMemo(memo);
                        list.add(m);
                        break;
                    }
                }

                if (found != null) {
                    // 一致する体重はリストから除く
                    listW.remove(found);
                } else {
                    // なければ身長のみを加える
                    list.add(h);
                }
            }

            // 体重のリストが残っていればループする
            if (listW.size() > 0) {
                for (int i = 0; i < listW.size(); i++) {
                    list.add(listW.get(i));
                }
            }

        } else if (listH != null) {
            // 身長だけの場合
            for (int i = 0; i < listH.size(); i++) {
                list.add(listH.get(i));
            }

        } else if (listW != null) {
            // 体重だけの場合
            for (int i = 0; i < listW.size(); i++) {
                list.add(listW.get(i));
            }
        }

        if (list.isEmpty()) {
            return;
        }

        boolean asc = GlobalVariables.getPreferences().getBoolean(GlobalVariables.DOC_HISTORY_ASCENDING, false);
        if (asc) {
            Collections.sort(list);
        } else {
            Collections.sort(list, Collections.reverseOrder());
        }

        tableModel.setObjectList(list);
        scroll(asc);
    }

    /**
     * 身長体重データを追加する。
     * @param model
     */
    public void add(final PhysicalModel model) {

        // 同定日
        String confirmedStr = model.getIdentifiedDate();
        Date confirmed = ModelUtils.getDateTimeAsObject(confirmedStr + "T00:00:00");

        // 記録日
        Date recorded = new Date();

        final List<ObservationModel> addList = new ArrayList<ObservationModel>(2);

        if (model.getHeight() != null) {
            ObservationModel observation = new ObservationModel();
            observation.setKarte(getParentContext().getKarte());
            observation.setCreator(GlobalVariables.getUserModel());
            observation.setObservation(IInfoModel.OBSERVATION_PHYSICAL_EXAM);
            observation.setPhenomenon(IInfoModel.PHENOMENON_BODY_HEIGHT);
            observation.setValue(model.getHeight());
            observation.setUnit(IInfoModel.UNIT_BODY_HEIGHT);
            observation.setConfirmed(confirmed);        // 確定（同定日）
            observation.setStarted(confirmed);          // 適合開始日
            observation.setRecorded(recorded);          // 記録日
            observation.setStatus(IInfoModel.STATUS_FINAL);
            //observation.setMemo(model.getMemo());
            addList.add(observation);
        }

        if (model.getWeight() != null) {

            ObservationModel observation = new ObservationModel();
            observation.setKarte(getParentContext().getKarte());
            observation.setCreator(GlobalVariables.getUserModel());
            observation.setObservation(IInfoModel.OBSERVATION_PHYSICAL_EXAM);
            observation.setPhenomenon(IInfoModel.PHENOMENON_BODY_WEIGHT);
            observation.setValue(model.getWeight());
            observation.setUnit(IInfoModel.UNIT_BODY_WEIGHT);
            observation.setConfirmed(confirmed);        // 確定（同定日）
            observation.setStarted(confirmed);          // 適合開始日
            observation.setRecorded(recorded);          // 記録日
            observation.setStatus(IInfoModel.STATUS_FINAL);
            //observation.setMemo(model.getMemo());
            addList.add(observation);
        }

        if (addList.isEmpty()) {
            return;
        }

        DBTask task = new DBTask<List<Long>>(getParentContext()) {

            @Override
            protected List<Long> doInBackground() throws Exception {
                RemoteDocumentDelegater pdl = new RemoteDocumentDelegater();
                List<Long> ids = pdl.addObservations(addList);
                return ids;
            }

            @Override
            protected void succeeded(List<Long> result) {
                if (model.getHeight() != null && model.getWeight() != null) {
                    model.setHeightId(result.get(0));
                    model.setWeightId(result.get(1));
                } else if (model.getHeight() != null) {
                    model.setHeightId(result.get(0));
                } else {
                    model.setWeightId(result.get(0));
                }
                boolean asc = GlobalVariables.getPreferences().getBoolean(GlobalVariables.DOC_HISTORY_ASCENDING, false);
                if (asc) {
                    tableModel.addRow(model);
                } else {
                    tableModel.addRow(0, model);
                }
                scroll(asc);
            }
        };

        task.execute();
    }

    /**
     * テーブルで選択した身長体重データを削除する。
     * @param row
     */
    public void delete(final int row) {

        PhysicalModel model = tableModel.getObject(row);
        if (model == null) {
            return;
        }

        final List<Long> list = new ArrayList<Long>(2);

        if (model.getHeight() != null) {
            list.add(new Long(model.getHeightId()));
        }

        if (model.getWeight() != null) {
            list.add(new Long(model.getWeightId()));
        }

        RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
        ddl.removeObservations(list);
        tableModel.deleteRow(row);

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
     * @return　タイトル
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
     * @return　ダーティ
     */
    @Override
    public boolean isDirty() {
        return false;
    }

    /**
     *　ダーティ
     * @param dirty　ダーティ
     */
    @Override
    public void setDirty(boolean dirty) {
    }

    /**
     *
     * @param command
     * @return　ディスパッチ成功で真
     */
    @Override
    public boolean dispatchChartCommand(ChartCommand command) {
        return false;
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
     * @return　更新
     */
    @Override
    public boolean update(Object o) {
        return true;
    }

    /**
     * BMI値 を表示するレンダラクラス。
     */
    protected class BMIRenderer extends DefaultTableCellRenderer {

        /**
         * Creates new IconRenderer
         */
        public BMIRenderer() {
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

            Component component = super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, col);

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setForeground(table.getForeground());
                if (row % 2 == 0) {
                    setBackground(GlobalSettings.getColors(GlobalSettings.Parts.EVEN));
                } else {
                    setBackground(GlobalSettings.getColors(GlobalSettings.Parts.ODD));
                }
            }

            PhysicalModel h = tableModel.getObject(row);

            Color fore = (h != null && h.calcBmi() != null && h.calcBmi().compareTo("25") > 0) ? Color.RED : Color.BLACK;
            this.setForeground(fore);

            ((JLabel) component).setText(value == null ? "" : (String) value);

            if (h != null && h.getStandardWeight() != null) {
                this.setToolTipText("標準体重 = " + h.getStandardWeight());
            }

            return component;
        }
    }
}
