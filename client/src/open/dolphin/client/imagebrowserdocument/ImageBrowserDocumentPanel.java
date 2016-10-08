/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImageBrowserDocumentPanel.java
 *
 * Created on 2009/11/10, 11:20:34
 */
package open.dolphin.client.imagebrowserdocument;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import open.dolphin.helper.IChartCommandAccepter.ChartCommand;
import open.dolphin.client.ImageEntry;
import open.dolphin.helper.ImageHelper;
import open.dolphin.helper.TaskProgressMonitor;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.Task;

import open.dolphin.client.ChartMediator;
import open.dolphin.client.GUIConst;
import open.dolphin.project.GlobalConstants;
import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;
import open.dolphin.client.ImageTableModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.TaskMonitor;

/**
 *　イメージ画面　MEMO:画面
 * @author
 */
public class ImageBrowserDocumentPanel extends javax.swing.JPanel implements IChartDocument {//IChartCommandAccepter,  IMainCommandAccepter

    /**
     * 
     */
    public static final String TITLE = "イメージ";
    private static final int MAX_IMAGE_SIZE = 120;
    private static final int CELL_WIDTH_MARGIN = 20;
    private static final int CELL_HEIGHT_MARGIN = 20;
    private ImageTableModel tableModel;
    private int columnCount;
    private boolean showFileName;
    private final int imageSize = MAX_IMAGE_SIZE;
    private final int cellWidth = MAX_IMAGE_SIZE + CELL_WIDTH_MARGIN;
    private final int cellHeight = MAX_IMAGE_SIZE + CELL_HEIGHT_MARGIN;
    private Properties properties;
    private IChart parent;
    private String title;
    private Application app;
    private ApplicationContext appCtx;
    private TaskMonitor taskMonitor;
    //  private TaskService taskService;
    private boolean dirty;
    private String imageLocation;
    private PatientModel patient;
    private static final String[] CHART_MENUS = {
        GUIConst.ACTION_OPEN_KARTE, GUIConst.ACTION_SAVE, GUIConst.ACTION_DIRECTION, GUIConst.ACTION_DELETE, GUIConst.ACTION_PRINT, GUIConst.ACTION_MODIFY_KARTE,
        GUIConst.ACTION_ASCENDING, GUIConst.ACTION_DESCENDING, GUIConst.ACTION_SHOW_MODIFIED, GUIConst.ACTION_SHOW_UNSEND, GUIConst.ACTION_SHOW_SEND, GUIConst.ACTION_SHOW_NEWEST,
        GUIConst.ACTION_INSERT_TEXT, GUIConst.ACTION_INSERT_SCHEMA, GUIConst.ACTION_INSERT_STAMP, GUIConst.ACTION_SELECT_INSURANCE,
        GUIConst.ACTION_CUT, GUIConst.ACTION_COPY, GUIConst.ACTION_PASTE, GUIConst.ACTION_UNDO, GUIConst.ACTION_REDO
    };

    /** Creates new form ImageBrowserDocumentPanel
     * @param parent
     */
    public ImageBrowserDocumentPanel(IChart parent) {
        this.title = TITLE;
        this.parent = parent;
        appCtx = GlobalConstants.getApplicationContext();
        app = appCtx.getApplication();
        taskMonitor = appCtx.getTaskMonitor();
        //   taskService = appCtx.getTaskService();
        //  setTitle(TITLE);
        setDirty(false);
    }

    /**
     *
     * @return　パネルのタイプ
     */
    @Override
    public TYPE getType() {
        return TYPE.ImageBrowserDocumentPanel;
    }

    /**
     *
     * @param res
     * @return　アイコン
     */
    private ImageIcon getIcon(String res) {
        return new ImageIcon(this.getClass().getResource(res));
    }

    /**
     *
     * @return　PDIアイコン
     */
    private ImageIcon getPdfIcon() {
        return getIcon("/open/dolphin/resources/images/pdf.gif");
    }

    /**
     *
     * @param loc
     */
    public void setImageLocation(String loc) {
        if (!loc.isEmpty()) {
            this.imageLocation = loc;
            locationFld.setText(this.imageLocation);
            File image_directory = new File(loc);
            image_directory.mkdir();
            scan();
        }
    }

    /**
     *
     * @param path
     * @return　'.'以降を返す
     */
    private String getSuffix(String path) {

        String test = path.toLowerCase();
        int index = test.lastIndexOf('.');
        if (index > 0) {
            return test.substring(index + 1);
        }
        return null;
    }

    /**
     *
     * @return　患者の画像ディレクトリのパス
     */
    private String getPatientDirectoryName() {
        return patient.getFullName() + "(" + patient.getPatientId() + ")";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        updateBtn = new javax.swing.JButton();
        locationFld = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        updateBtn.setText("更新");
        updateBtn.setName("updateBtn"); // NOI18N
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        locationFld.setEditable(false);
        locationFld.setName("locationFld"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table.setName("table"); // NOI18N
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(locationFld, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updateBtn))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     *
     * @param evt
     */
    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        scan();
    }//GEN-LAST:event_updateBtnActionPerformed
    /**
     *
     */
    private void scan() {

        if (imageLocation == null) {
            JOptionPane.showMessageDialog(null, "ベースディレクトリが設定されていません" + System.getProperty("line.separator") + "環境設定-画像より設定してください");
            return;
        }

        File imageDirectory = new File(imageLocation);
        if ((!imageDirectory.exists()) || (!imageDirectory.isDirectory())) {
            return;
        }

        final File[] imageFiles = imageDirectory.listFiles();

        if (imageFiles == null || imageFiles.length == 0) {
            return;
        }

        Task task = new Task<List<ImageEntry>, Integer>(app) {

            @Override
            protected List<ImageEntry> doInBackground() throws Exception {
                List imageList = new ArrayList();
                int cnt = 0;
                int total = imageFiles.length;
                for (File file : imageFiles) {
                    cnt++;
                    String path = file.getPath();
                    String fileName = file.getName();
                    StringBuilder sb = new StringBuilder();
                    sb.append(fileName);
                    sb.append("[");
                    sb.append(cnt);
                    sb.append("/");
                    sb.append(total);
                    sb.append("]を処理しています...");
                    setMessage(sb.toString());

                    String suffix = getSuffix(path);
                    if (suffix == null) {
                        setProgress(new Integer(cnt));
                        continue;
                    }

                    Iterator readers = ImageIO.getImageReadersBySuffix(suffix);

                    if (!readers.hasNext()) {
                        if (suffix.equals("pdf")) {
                            ImageEntry entry = new ImageEntry();
                            URL url = file.toURI().toURL();
                            entry.setUrl(url.toString());
                            entry.setPath(path);
                            entry.setFileName(fileName);
                            entry.setImageIcon(getPdfIcon());
                            imageList.add(entry);
                        }
                        setProgress(new Integer(cnt));
                        continue;
                    }

                    ImageReader reader = (ImageReader) readers.next();
                    try {
                        reader.setInput(new FileImageInputStream(file), true);
                    } catch (Exception e) {
                        continue;
                    }

                    int numImages = 1;
                    if (suffix.equals("dcm")) {
                        try {
                            numImages = reader.getNumImages(true);
                            if (numImages > 1) {
                                sb = new StringBuilder();
                                sb.append(fileName);
                                sb.append(" M");
                                sb.append(numImages);
                                fileName = sb.toString();
                            }
                        } catch (Exception e) {
                            LogWriter.error(getClass(), e);
                        }
                    }

                    try {
                        int width = reader.getWidth(0);
                        int height = reader.getHeight(0);
                        BufferedImage image = reader.read(0);
                        image = ImageHelper.getFirstScaledInstance(image, imageSize);
                        ImageIcon icon = new ImageIcon(image);
                        ImageEntry entry = new ImageEntry();
                        URL url = file.toURI().toURL();
                        entry.setUrl(url.toString());
                        entry.setPath(path);
                        entry.setFileName(fileName);
                        entry.setImageIcon(icon);
                        entry.setNumImages(numImages);
                        entry.setWidth(width);
                        entry.setHeight(height);
                        imageList.add(entry);
                    } catch (Exception e) {
                        LogWriter.error(getClass(), e);
                    }
                    setProgress(new Integer(cnt));
                }
                return imageList;
            }

            @Override
            protected void succeeded(List<ImageEntry> result) {
                tableModel.setImageList(result);
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

        String message = "イメージブラウザ";
        String note = imageLocation + "ディレクトリをスキャンしています...";
        int min = 0;
        int max = imageFiles.length;
        new TaskProgressMonitor(task, taskMonitor, null, message, note, min, max);
        appCtx.getTaskService().execute(task);
    }

    /**
     *
     * @return　レイアウトを保存すべきか
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
     * @return　コンテキスト
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
        initComponents();
        initCustomComponents();
    }

    /**
     *
     */
    @Override
    public void stop() {
        LocalStorage ls = appCtx.getLocalStorage();//MEMO: unused?
    }

    /**
     *
     * @param entry
     * @throws IOException
     */
    private void openImage(ImageEntry entry) throws IOException {

        String path = entry.getPath();
        String ext = path.toLowerCase();

        String appli = null;

        if (ext.endsWith(".pdf")) {
            appli = properties.getProperty("pdfView");

        } else if (ext.endsWith(".dcm")) {
            appli = properties.getProperty("dicomView");

        } else {
            appli = properties.getProperty("jpegView");
        }

        if (appli == null || appli.equals("")) {
            return;
        }
        if (GlobalConstants.isMac()) {
            new ProcessBuilder("open", "-a", appli, path).start();
        } else {
            new ProcessBuilder(appli, path).start();
        }

    }

    /**
     *
     */
    private void initCustomComponents() {

        patient = getParentContext().getPatient();

        try {
            LocalStorage ls = appCtx.getLocalStorage();
            properties = (Properties) ls.load("imageBrowserProp.xml");

        } catch (Exception e) {
             LogWriter.error(getClass(), e);
        }

        if (properties == null) {
            properties = new Properties();
            properties.setProperty("columnCount", "5");
            properties.setProperty("showFileName", "true");
            properties.setProperty("baseDir", "");
            properties.setProperty("jpegView", "");
            properties.setProperty("pdfView", "");

            properties.setProperty("dicomView", "");
        }

        String baseDir = properties.getProperty("baseDir");
        if (baseDir != null && (!baseDir.equals(""))) {
            setImageLocation(baseDir + "/" + getPatientDirectoryName());
        }

        columnCount = Integer.parseInt(properties.getProperty("columnCount"));
        showFileName = Boolean.parseBoolean(properties.getProperty("showFileName"));
        tableModel = new ImageTableModel(null, columnCount);
        table.setModel(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn column = null;
        for (int i = 0; i < columnCount; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(cellWidth);
        }
        table.setRowHeight(cellHeight);

        ImageRenderer imageRenderer = new ImageRenderer();
        imageRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(java.lang.Object.class, imageRenderer);

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    int col = table.getSelectedColumn();
                    if (row != -1 && col != -1) {
                        ImageEntry entry = (ImageEntry) tableModel.getValueAt(row, col);
                        if (entry != null) {
                            try {
                                openImage(entry);
                            } catch (IOException ex) {
                              LogWriter.error(getClass(), ex);
                            }
                        }
                    }
                }
            }
        });

        table.setTransferHandler(new ImageTableTransferHandler());

        table.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int ctrlMask = InputEvent.CTRL_DOWN_MASK;
                int action = ((e.getModifiersEx() & ctrlMask) == ctrlMask) ? TransferHandler.COPY : TransferHandler.MOVE;
                JComponent c = (JComponent) e.getSource();
                TransferHandler handler = c.getTransferHandler();
                handler.exportAsDrag(c, e, action);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
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
        //      stateMgr.controlMenu();
    }

    /**
     *
     * @return　命令実行可能か
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
     * @param command
     * @return　ディスパッチ成功なら真
     */
    @Override
    public boolean dispatchChartCommand(ChartCommand command) {
        return false;
    }

    /**
     *
     * @return　タブのコンテンツ
     */
    @Override
    public List<JTabbedPane> getTabbedPanels() {
        return null;
    }

    /**
     *
     * @param o
     * @return　インターフェイス
     */
    @Override
    public boolean update(Object o) {
        return true;
    }

    /**
     *
     */
    class ImageRenderer extends DefaultTableCellRenderer {

        /**
         *
         */
        public ImageRenderer() {
            setVerticalTextPosition(JLabel.BOTTOM);
            setHorizontalTextPosition(JLabel.CENTER);
        }

        /**
         *
         * @param table
         * @param value
         * @param isSelected
         * @param isFocused
         * @param row
         * @param col
         * @return　テーブルセルとして使われるコンポーネント
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int col) {
            Component compo = super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, col);
            JLabel l = (JLabel) compo;
            if (value != null) {
                ImageEntry entry = (ImageEntry) value;
                l.setIcon(entry.getImageIcon());
                if (showFileName) {
                    l.setText(entry.getFileName());
                } else {
                    l.setText(null);
                }
            } else {
                l.setIcon(null);
                l.setText(null);
            }
            return compo;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField locationFld;
    private javax.swing.JTable table;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
