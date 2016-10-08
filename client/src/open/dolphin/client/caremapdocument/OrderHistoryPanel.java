/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OrderHistoryPanel.java
 *
 * Created on 2010/03/08, 16:08:36
 */
package open.dolphin.client.caremapdocument;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import open.dolphin.client.GUIConst;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalSettings;
import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.IStampInfo;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.log.LogWriter;
import open.dolphin.table.ObjectTableModel;
import open.dolphin.table.OddEvenRowRenderer;

/**
 *　履歴パネル　MEMO:画面　リスナー
 * @author
 */
public class OrderHistoryPanel extends javax.swing.JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = -2302784717739085879L;
    private ObjectTableModel tModel;
    private String pid;

    /** Creates new form OrderHistoryPanel
     * @param chartCtx 
     */
    public OrderHistoryPanel(final CareMapDocumentPanel chartCtx) {
        initComponents();

        String[] columnNames = {"実施日", "内   容"};

        // オーダの履歴(確定日|スタンプ名)を表示する TableModel
        // 各行は ModuleModel
        tModel = new ObjectTableModel(columnNames, 12) {

            private static final long serialVersionUID = 1684645192401100170L;

            /**
             *
             */
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }

            /**
             *
             */
            @Override
            public Object getValueAt(int row, int col) {
                String result = null;
                ModuleModel module = (ModuleModel) getObject(row);
                if (module != null) {
                    IStampInfo info = module.getModuleInfo();
                    switch (col) {
                        case 0:
                            //     result = ModelUtils.getDateAsString(module.getStarted());
                            result = ModelUtils.getDateAsFormatString(module.getStarted(), IInfoModel.KARTE_DATE_FORMAT);
                            break;
                        case 1:
                            result = info.getStampName();
                            break;
                        default: LogWriter.fatal(getClass(), "case default");
                    }
                }
                return result;
            }
        };

        table.setModel(tModel);
        table.setDefaultRenderer(Object.class, new OddEvenRowRenderer());

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Popup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Popup(e);
            }

            private void Popup(MouseEvent evnt) {

                if (evnt.isPopupTrigger()) {

                    JTable table = (JTable) evnt.getSource();
                    int selectedIndex = table.rowAtPoint(evnt.getPoint());

                    table.changeSelection(selectedIndex, 1, false, false);
                    ModuleModel stamp = (ModuleModel) tModel.getObject(selectedIndex);
                    ((CareMapDocumentPanel) chartCtx).setSelectedHistoryModel(stamp);

                    JPopupMenu popup = new JPopupMenu();
                    popup.add(chartCtx.getParentContext().getChartMediator().getAction(GUIConst.ACTION_LETTER_PASTE));
                    popup.show(evnt.getComponent(), evnt.getX(), evnt.getY());
                }
            }
        });

        ListSelectionModel m = table.getSelectionModel();
        m.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    contents.setText(displayOrder(table.getSelectedRow()));
                }
            }
        });
        setColumnWidth(new int[]{50, 240});

        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));

        // 内容表示用 TextArea
        contents.setBackground(Color.white);
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
        cs = new javax.swing.JScrollPane();
        contents = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout(0, 5));

        scroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
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
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setName("table"); // NOI18N
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scroller.setViewportView(table);

        add(scroller, java.awt.BorderLayout.CENTER);

        cs.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        cs.setMaximumSize(new java.awt.Dimension(240, 300));
        cs.setName("cs"); // NOI18N
        cs.setPreferredSize(new java.awt.Dimension(240, 300));

        contents.setName("contents"); // NOI18N
        cs.setViewportView(contents);

        add(cs, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel contents;
    private javax.swing.JScrollPane cs;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @param columnWidth
     */
    public void setColumnWidth(int[] columnWidth) {
        int len = columnWidth.length;
        for (int i = 0; i < len; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidth[i]);
        }
    }

    /**
     *
     * @return
     */
    public String getPid() {
        return pid;
    }

    /**
     *
     * @param val
     */
    public void setPid(String val) {
        pid = val;
    }

    /**
     *
     * @param allModules
     */
    public void setModuleList(List allModules) {

        tModel.clear();

        if (allModules == null || allModules.isEmpty()) {
            return;
        }

        int size = allModules.size();
        List<Object> list = new ArrayList<Object>();

        for (int i = size - 1; i >= 0; i--) {
            List l = (List) allModules.get(i);
            if (l != null) {
                for (int j = l.size() - 1; j >= 0; j--) {
                    list.add((Object) l.get(j));
                }
            }
        }
        tModel.setObjectList(list);
    }

    /**
     * カレンダーの日が選択されたときに通知を受け、テーブルで日付が一致するオーダの行を選択する。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(CareMapDocumentPanel.SELECTED_DATE_PROP)) {
            String date = (String) e.getNewValue();
            findDate(date);
        }
    }

    /**
     * オーダ履歴のテーブル行がクリックされたとき、データモデルの ModuleModel を表示する。
     * @param index
     * @return
     */
    private String displayOrder(int index) {
        String result = "";
        ModuleModel stamp = (ModuleModel) tModel.getObject(index);
        if (stamp != null) {
            IInfoModel model = stamp.getModel();
            try {
                VelocityContext context = GlobalConstants.getVelocityContext();
                context.put("model", model);
                context.put("stampName", stamp.getModuleInfo().getStampName());
                context.put("bundleNumber", ((ClaimBundle) model).getBundleNumber());
                context.put("stampStatus", stamp.getModuleInfo().getStampStatus());

                // このスタンプのテンプレートファイルを得る
                String templateFile = stamp.getModel().getClass().getName() + ".vm";
                StringWriter sw = new StringWriter();
                BufferedWriter bw = new BufferedWriter(sw);
                InputStream instream = GlobalConstants.getTemplateAsStream(templateFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "SHIFT_JIS"));
                try {
                    Velocity.evaluate(context, bw, "stmpHolder", reader);
                } finally {
                    bw.flush();
                    bw.close();
                    reader.close();
                }
                result = sw.toString();
            } catch (Exception e) {
                LogWriter.error(getClass(), e);
            }
        }
        return result;
    }

    /**
     *
     * @param date
     */
    public void findDate(String date) {

        int size = tModel.getDataSize();
        for (int i = 0; i < size; i++) {
            String rowDate = (String) tModel.getValueAt(i, 0);
            if (rowDate.equals(date)) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }
    }
}
