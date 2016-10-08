/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AdminMasterPanel.java
 *
 * Created on 2009/08/18, 19:43:06
 */
package open.dolphin.client.editor.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import open.dolphin.client.AutoRomanListener;
import open.dolphin.project.GlobalConstants;
import open.dolphin.client.MasterRenderer;
import open.dolphin.client.TaskTimerMonitor;
import open.dolphin.project.GlobalSettings;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.infomodel.AdminEntry;
import open.dolphin.infomodel.MasterItem;
import open.dolphin.log.LogWriter;
import open.dolphin.order.AbstractMasterPanel;
import open.dolphin.table.ObjectTableModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

/**
 *　MEMO:画面
 * @author
 */
public class AdminMasterPanel extends AbstractMasterPanel {

    /**
     *
     */
    public static final String ADMIN_PROP = "adminProp";
    private static final String[] COLUMN_NAMES = {"コード", "名 称"};
    private static final String CUSTOM_CODE = "001";
    //   private static final String[] TONYO_RANGE = {"001000800", "001000899"};
    private static final String[] ADMIN_CATEGORY = {"選択してください", "内服１回等(100)", "内服２回等(200)", "内服３回等(300)", "内服４回等(400)", "点眼等(500,700)", "塗布等(600)", "頓用等(800)", "吸入等(900)", "全て"};
    private static final String[] ADMIN_CODE_RANGE = {"", "0010001", "0010002", "0010003", "0010004", "0010005 0010007", "0010006", "0010008", "0010009", "001"};
    //  private static final int COMMENT_INDEX = 9;
    /** 用法カテゴリ ComboBox */
    private JComboBox adminCombo;
    /** 自医コード入力フィ−ルド */
    private JTextField customCode;

    /** Creates new form AdminMasterPanel
     * @param master
     */
    public AdminMasterPanel(String master) {
        super(master);
    }

    /**
     * 初期化する。
     */
    @Override
    protected void initialize() {

        initComponents();
        //
        // 用法カテゴリ ComboBox を生成する
        // 選択の変化があった場合はカテゴリに該当する用法を検索する
        //
        int index = 0;
        adminCombo = new JComboBox(ADMIN_CATEGORY);
        adminCombo.setToolTipText("括弧内はコードの番号台を表します。");
        adminCombo.setSelectedIndex(index);
        adminCombo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int index = adminCombo.getSelectedIndex();
                    String code = ADMIN_CODE_RANGE[index];
                    if (!code.equals("")) {
                        fetchAdministration(code);
                    }
                }
            }
        });

        // TableModelを生成する
        tableModel = new ObjectTableModel(COLUMN_NAMES, START_NUM_ROWS) {

            private static final long serialVersionUID = 8084360322119845887L;

            @Override
            public Class getColumnClass(int col) {
                return AdminEntry.class;
            }
        };

        // Table を生成する
        //    table = new JTable(tableModel);

        table.setModel(tableModel);
        // シングル選択モードに設定する
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //
        // 行選択が起った時に AdminInfo を生成しリスナへ通知する
        //
        table.setRowSelectionAllowed(true);

        // 列幅を設定する
        TableColumn column = null;
        int[] width = new int[]{50, 250};
        int len = width.length;
        for (int i = 0; i < len; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(width[i]);
        }

        // レンダラーを設定する
        table.setDefaultRenderer(AdminEntry.class, new AdminMasterRenderer());

        //
        // レイアウトする
        //
        // Keyword パネル
        JPanel key = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 5));
        key.add(findLabel);
        key.add(new JLabel("用法:"));
        key.add(keywordField);
        key.add(isForwordCheckBox);
        key.setBorder(BorderFactory.createTitledBorder("用法で検索"));

        // カテゴリ
        JPanel ctp = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        ctp.add(new JLabel("カテゴリ:"));
        ctp.add(adminCombo);
        ctp.setBorder(BorderFactory.createTitledBorder("カテゴリで検索"));

        // 自院コード
        customCode = new JTextField(7);
        customCode.setToolTipText("自院マスタのコード（番台）を入力してください。");
        customCode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String text = customCode.getText().trim();
                if (!text.equals("")) {
                    StringTokenizer st = new StringTokenizer(text, " ");
                    StringBuilder sb = new StringBuilder();
                    int cnt = 0;
                    while (st.hasMoreTokens()) {
                        if (cnt != 0) {
                            sb.append(" ");
                        }
                        sb.append(CUSTOM_CODE);
                        sb.append(st.nextToken());
                        cnt++;
                    }
                    text = sb.toString();
                    fetchAdministration(text);
                }
            }
        });

        customCode.addFocusListener(AutoRomanListener.getInstance());

        JPanel customP = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        customP.add(new JLabel("自院コード: 001"));
        customP.add(customCode);
        customP.setBorder(BorderFactory.createTitledBorder("コードで検索"));

        // トップパネルへ配置する
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
        top.add(key);
        top.add(Box.createHorizontalStrut(11));
        top.add(ctp);
        top.add(Box.createHorizontalGlue());
        top.add(customP);

        JScrollPane scroller = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        this.setLayout(new BorderLayout(0, 11));
        this.add(top, BorderLayout.NORTH);
        this.add(scroller, BorderLayout.CENTER);
    }

    /**
     * 選択されたカテゴリに対応する用法を検索する。
     */
    private void fetchAdministration(final String category) {
        if (category == null) {
            return;
        }

        final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");

        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();

        Task task = new Task<Object, Void>(app) {//検索

            @Override
            protected Object doInBackground() throws Exception {
                Object result = dao.getAdminByCategoryFromOrca(category);
                return result;
            }

            @Override
            protected void succeeded(Object result) {
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
        String message = "用法検索";
        String note = category + "を検索しています...";
        Component c = SwingUtilities.getWindowAncestor(this);
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, note, 200, 60 * 1000);
        taskMonitor.addPropertyChangeListener(w);

        appCtx.getTaskService().execute(task);

    }
    /**
     *
     */
    public void enter() {

        adminCombo.setSelectedIndex(0);
        super.enter();

    }

    /**
     * 用法マスタ Table のレンダラークラス。 MEMO:Component
     */
    protected final class AdminMasterRenderer extends MasterRenderer {

        private static final long serialVersionUID = 8567079934909643686L;
        private final int CODE_COLUMN = 0;
        private final int NAME_COLUMN = 1;

        /**
         * 
         */
        public AdminMasterRenderer() {
        }

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
            if (value != null && value instanceof AdminEntry) {
                AdminEntry entry = (AdminEntry) value;
                switch (col) {
                    case CODE_COLUMN:
                        label.setText(entry.getCode());
                        break;
                    case NAME_COLUMN:
                        label.setText(entry.getName());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

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
        ));
        table.setName("table"); // NOI18N
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(95, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    /**
     * 
     * @param evt
     */
    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked
        if (evt.getClickCount() == 2) {

            int row = table.getSelectedRow();
            AdminEntry o = (AdminEntry) tableModel.getObject(row);
            //       if (o != null) {
            if (o != null && (o.useState(new GregorianCalendar()) != 2)) {//isInUse()は日付がNULL、Emptyで真を返してしまうので
                MasterItem item = new MasterItem(3, o);
                item.setSstKijunCdSet(o.getSstKijunCdSet());
                boundSupport.firePropertyChange(SELECTED_ITEM_PROP, null, item);
            }
        }
    }//GEN-LAST:event_tableMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
