/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PluginsSettingPanel.java
 *
 * Created on 2010/10/08, 14:12:34
 */
package open.dolphin.client.settings;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import open.dolphin.helper.PlugInMenuSupport;
import open.dolphin.log.LogWriter;
import open.dolphin.plugin.PluginLoader;
import open.dolphin.plugin.PluginWrapper;
import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalSettings;
import open.dolphin.utils.Adapter;

/**
* プラグイン設定画面　MEMO:画面
 * @author
 */
public class PluginsSettingPanel extends javax.swing.JPanel implements IAbstractSettingPanel {

    private ProjectSettingDialog context;
    private PlugInMenuSupport plugins;
    private PropertyChangeSupport boundSupport;
    /**
     * 
     */
    protected State state = State.NONE_STATE;
    private boolean loginState;
    private String title;
    private String icon;
    private String id;
    private PluginsModel model;
    private String classPath;
    private String path;
    private String settingPath;
    private Adapter<Boolean, Boolean> adapter;//MEMO: unused?

    /**
     * Creates new form PluginsSettingPanel
     */
    public PluginsSettingPanel() {

        boundSupport = new PropertyChangeSupport(this);
        initComponents();

        setId("Plugins");
        setTitle("プラグイン設定");
        setIcon("preferences-plugin.png");

        classPath = "open.dolphin.plugin.";

        path = GlobalConstants.getPluginsDirectory();

        pathField.setText(path);
        pathField.setToolTipText(path);

        settingPath = GlobalConstants.getApplicationContext().getLocalStorage().getDirectory().getAbsolutePath();

        settingField.setText(settingPath);
        settingField.setToolTipText(settingPath);

        String schemaPath = GlobalConstants.getSchemasDirectory();

        schemaField.setText(schemaPath);
        schemaField.setToolTipText(schemaPath);

        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));

    }

    /**
     *
     */
    private void ImportPlugin() {
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isFile()) {
                    String fileName = f.getName();
                    if (fileName.length() > 5) {
                        if (fileName.substring(fileName.length() - 4, fileName.length()).equals(".jar")) {
                            //ファイル名を根拠に、すでにインポートされたプラグインをファイルリストに表示しない。
                            return !findFile(getInstalled(path), f.getName());
                        }
                    }
                } else {
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Only .Jar";
            }
        };

        jFileChooser1.setFileFilter(filter);
        int selected = jFileChooser1.showOpenDialog(this);
        if (selected == JFileChooser.APPROVE_OPTION) {
            File[] files = jFileChooser1.getSelectedFiles();
            for (File file : files) {
                try {
                    copyTransfer(file.getAbsolutePath(), path + File.separator + file.getName());
                } catch (IOException ex) {
                    LogWriter.error(getClass(), ex);
                }
            }
            JOptionPane.showMessageDialog(null, "プラグインのインポート後はOpenDolphinを再起動してください。");
        }// else if (selected == JFileChooser.CANCEL_OPTION) {
    //    } else if (selected == JFileChooser.ERROR_OPTION) {
     //   }

        list();

        plugins.loadPlugins(GlobalConstants.getPluginsDirectory(), "open.dolphin.plugin.");

        //     Persistent.deleteLayout("PatientInspector", "tabLocation");
        //    Persistent.deleteLayout("PatientInspector", "layout");
        //   Persistent.deleteLayout("EditorFrame", "layout");
    }

    /**
     *
     * @param adapter
     */
    @Override
    public void onChenge(Adapter<Boolean, Boolean> adapter) {
        this.adapter = adapter;
    }

    /**
     *
     * @param srcPath
     * @param destPath
     * @throws IOException
     */
    private void copyTransfer(String srcPath, String destPath) throws IOException {
        FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
        FileChannel destChannel = new FileOutputStream(destPath).getChannel();
        try {
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
        } finally {
            srcChannel.close();
            destChannel.close();
        }
    }

    /**
     *　指定されたディレクトリのファイルの一覧
     * ファイル名を根拠に、すでにインポートされたプラグインをファイルリストに表示しない仕掛けで、
     * すでにインストールされているプラグインの一覧を見るのに使用。
     */
    private File[] getInstalled(String path) {
        File[] result = null;
        File directory = new File(path);
        if (directory.isDirectory()) {
            result = directory.listFiles();
        }
        return result;
    }

    /**
     * ファイルの一覧から名前がパラメータと一致するファイルがあるかどうか判定
     * ファイル名を根拠に、すでにインポートされたプラグインをファイルリストに表示しない仕掛けで、
     * すでにインストールされているプラグインの一覧を見るのに使用。
     */
    private boolean findFile(File[] files, String name) {
        for (File file : files) {
            if (file.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    @Override
    public void setId(String id) {
        this.id = id;
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
     * @param title
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
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
    public ProjectSettingDialog getContext() {
        return context;
    }

    /**
     *
     * @param context
     */
    @Override
    public void setContext(ProjectSettingDialog context) {
        this.context = context;
        this.plugins = context.getPlugins();
        this.addPropertyChangeListener(STATE_PROP, context);
        this.setLogInState(context.getLoginState());
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isLoginState() {
        return loginState;
    }

    /**
     *
     * @param login
     */
    @Override
    public void setLogInState(boolean login) {
        loginState = login;
    }

    /**
     *
     * @return
     */
    @Override
    public JPanel getPanel() {
        return this;
    }

    /**
     *
     */
    @Override
    public void start() {
        model = new PluginsModel();
        model.populate();
        list();
    }

    /**
     *
     * @param dir
     * @return
     */
    private Exception install(File dir) {

        Exception result = null;
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String fileName = file.getName();
            if (fileName.substring(fileName.length() - 4, fileName.length()).equals(".jar")) {
                try {
                    String packageName = classPath + fileName.substring(0, fileName.length() - 4);
                    PluginLoader loader = new PluginLoader(path + File.separator + fileName, packageName);
                    open.dolphin.plugin.IPlugin plugin = loader.getPlugin();
                    PluginWrapper pluginWrapper = new PluginWrapper(plugin);
                    Object[] row = new Object[4];
                    row[0] = fileName;
                    row[1] = packageName;
                    row[2] = pluginWrapper.getName();
                    row[3] = pluginWrapper.getReadableName();
                    result = pluginWrapper.getLastException();
                    ((DefaultTableModel) PluginsTable.getModel()).addRow(row);
                } catch (Exception ex) {
                    LogWriter.error(getClass(), ex);
                }
            }
        }
        return result;
    }

    /**
     *
     */
    @Override
    public void list() {
        ((DefaultTableModel) PluginsTable.getModel()).setRowCount(0);
        File dir = new File(path);
        if (dir.exists()) {
            install(dir);
        } else {
            dir.mkdir();
            install(dir);
        }
    }

    /**
     *
     */
    @Override
    public void save() {
        model.restore();
    }

    /**
     *
     * @param state
     */
    @Override
    public void setState(State state) {
        this.state = state;
        boundSupport.firePropertyChange(STATE_PROP, null, this.state);
    }

    /**
     *
     * @return
     */
    @Override
    public State getState() {
        return state;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        scroller = new javax.swing.JScrollPane();
        PluginsTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pathField = new javax.swing.JTextField();
        settingField = new javax.swing.JTextField();
        schemaField = new javax.swing.JTextField();
        schemaF = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        jFileChooser1.setMultiSelectionEnabled(true);
        jFileChooser1.setName("jFileChooser1"); // NOI18N

        scroller.setName("scroller"); // NOI18N

        PluginsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ファイル名", "パッケージ名", "機能拡張名", "詳細"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PluginsTable.setName("PluginsTable"); // NOI18N
        scroller.setViewportView(PluginsTable);

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText("プラグインの保存場所");
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText("設定ファイルの保存場所"); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        pathField.setEditable(false);
        pathField.setName("pathField"); // NOI18N
        pathField.setOpaque(false);

        settingField.setEditable(false);
        settingField.setName("settingField"); // NOI18N
        settingField.setOpaque(false);

        schemaField.setEditable(false);
        schemaField.setName("schemaField"); // NOI18N

        schemaF.setText("シェーマの保存場所");
        schemaF.setName("schemaF"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(schemaF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pathField, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(schemaField, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(settingField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(settingField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(schemaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(schemaF))
                .addContainerGap())
        );

        jPanel2.setName("jPanel2"); // NOI18N

        jButton1.setText("機能拡張の取り込み");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(169, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(169, 169, 169))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(scroller, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scroller, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ImportPlugin();
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable PluginsTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField pathField;
    private javax.swing.JLabel schemaF;
    private javax.swing.JTextField schemaField;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTextField settingField;
    // End of variables declaration//GEN-END:variables

    /**
     *
     */
    class PluginsModel {

        public void populate() {
        }

        public void restore() {
        }
    }

    /**
     *
     */
    class StateMgr {

        public void checkState() {
        }

        public void controlSendMml() {
            this.checkState();
        }

        protected boolean isValid() {
            return true;
        }
    }
}
