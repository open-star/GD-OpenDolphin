package open.dolphin.client.editor.stamp;

import open.dolphin.project.GlobalConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import open.dolphin.client.ObjectListTable;
import open.dolphin.client.TaskTimerMonitor;

import open.dolphin.project.GlobalSettings;
import open.dolphin.delegater.remote.RemoteStampDelegater;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.SubscribedTreeModel;
import open.dolphin.helper.ComponentMemory;
import open.dolphin.helper.SynchronizedTask;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;
import open.dolphin.table.ObjectReflectTableModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

/**
 * StampImporter
 *
 * @author Minagawa,Kazushi
 */
public class StampImporter {

    private static final String[] COLUMN_NAMES = {
        "名  称", "カテゴリ", "公開者", "説  明", "公開先", "インポート"
    };
    private static final String[] METHOD_NAMES = {
        "getName", "getCategory", "getPartyName", "getDescription", "getPublishType", "isImported"
    };
    private static final Class[] CLASSES = {
        String.class, String.class, String.class, String.class, String.class, Boolean.class
    };
    private static final int[] COLUMN_WIDTH = {
        120, 90, 170, 270, 40, 40
    };
    private static final Color ODD_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.ODD);
    private static final Color EVEN_COLOR = GlobalSettings.getColors(GlobalSettings.Parts.EVEN);
    private static final ImageIcon WEB_ICON = GlobalConstants.getImageIcon("web_16.gif");
    private static final ImageIcon HOME_ICON = GlobalConstants.getImageIcon("home_16.gif");
    private static final ImageIcon FLAG_ICON = GlobalConstants.getImageIcon("flag_16.gif");
    private static final int WIDTH = 780;
    private static final int HEIGHT = 380;
    private String title = "スタンプインポート";
    private JFrame frame;
    private ObjectListTable browseTable;
    private JButton importBtn;
    private JButton deleteBtn;
    private JButton cancelBtn;
    private JLabel publicLabel;
    private JLabel localLabel;
    private JLabel importedLabel;
    private StampBoxFrame stampBox;
    private List<Long> importedTreeList;
    private RemoteStampDelegater sdl;
    private ApplicationContext appCtx;
    private Application app;

    /**
     *
     * @param stampBox
     */
    public StampImporter(StampBoxFrame stampBox) {
        this.stampBox = stampBox;
        importedTreeList = stampBox.getImportedTreeList();
        appCtx = GlobalConstants.getApplicationContext();
        app = appCtx.getApplication();
    }

    /**
     * 公開されているTreeのリストを取得しテーブルへ表示する。
     */
    public void start() {
        sdl = new RemoteStampDelegater();

        //   int delay = 200;
        //  int maxEstimation = 60 * 1000;
        //  String mmsg = "公開スタンプを取得しています...";

        SynchronizedTask task = new SynchronizedTask<List<PublishedTreeModel>, Void>(app) {//公開スタンプの取得

            @Override
            protected List<PublishedTreeModel> doInBackground() {
                List<PublishedTreeModel> result = sdl.getPublishedTrees();
                return result;
            }

            @Override
            protected void succeeded(List<PublishedTreeModel> result) {

                if ((!sdl.isError()) && result != null) {
                    // DBから取得が成功したらGUIコンポーネントを生成する
                    initCustomComponents();
                    if (importedTreeList != null && importedTreeList.size() > 0) {
                        for (Iterator iter = result.iterator(); iter.hasNext();) {
                            PublishedTreeModel model = (PublishedTreeModel) iter.next();
                            for (Long id : importedTreeList) {
                                if (id.longValue() == model.getId()) {
                                    model.setImported(true);
                                    break;
                                }
                            }
                        }
                    }
                    browseTable.setObjectList((List) result);
                } else {
                    //      LogWriter.error(getClass(), "");
                    JOptionPane.showMessageDialog(frame, "公開スタンプの取得に失敗しました.", GlobalConstants.getFrameTitle(title), JOptionPane.WARNING_MESSAGE);
                }
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
        /*
        TaskMonitor taskMonitor = appCtx.getTaskMonitor();
        String message = "スタンプ取り込み";
        Component c = null;
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, mmsg, delay, maxEstimation);
        taskMonitor.addPropertyChangeListener(w);

        appCtx.getTaskService().execute(task);
         */
        task.execute();
    }

    /**
     * GUIコンポーネントを初期化する。
     */
    public void initCustomComponents() {
        frame = new JFrame(GlobalConstants.getFrameTitle(title));

        //<TODO>アイコンを適切な物に変更
        ImageIcon icon = GlobalConstants.getImageIcon("web_32.gif");
        frame.setIconImage(icon.getImage());


        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int n = GlobalConstants.isMac() ? 3 : 2;
        int x = (screen.width - WIDTH) / 2;
        int y = (screen.height - HEIGHT) / n;
        ComponentMemory cm = new ComponentMemory(frame, new Point(x, y), new Dimension(new Dimension(WIDTH, HEIGHT)), this);
        cm.setToPreferenceBounds();

        JPanel contentPane = createBrowsePane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        frame.setVisible(true);
    }

    /**
     * 終了する。
     */
    public void stop() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * 公開スタンプブラウズペインを生成する。
     */
    private JPanel createBrowsePane() {

        JPanel browsePane = new JPanel();

        browseTable = new ObjectListTable(COLUMN_NAMES, 10, METHOD_NAMES, CLASSES);
        browseTable.setColumnWidth(COLUMN_WIDTH);
        importBtn = new JButton("インポート");
        importBtn.setEnabled(false);
        cancelBtn = new JButton("閉じる");
        deleteBtn = new JButton("削除");
        deleteBtn.setEnabled(false);
        publicLabel = new JLabel("グローバル", WEB_ICON, SwingConstants.CENTER);
        localLabel = new JLabel("院内", HOME_ICON, SwingConstants.CENTER);
        importedLabel = new JLabel("インポート済", FLAG_ICON, SwingConstants.CENTER);

        // レイアウトする
        browsePane.setLayout(new BorderLayout(0, 17));
        JPanel flagPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 5));
        flagPanel.add(localLabel);
        flagPanel.add(publicLabel);
        flagPanel.add(importedLabel);

        JPanel cmdPanel = new JPanel();
        cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.X_AXIS));
        cmdPanel.add(Box.createHorizontalGlue());
        cmdPanel.add(cancelBtn);
        cmdPanel.add(Box.createHorizontalStrut(5));
        cmdPanel.add(deleteBtn);
        cmdPanel.add(Box.createHorizontalStrut(5));
        cmdPanel.add(importBtn);

        //    JPanel cmdPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{cancelBtn, deleteBtn, importBtn});
        browsePane.add(flagPanel, BorderLayout.NORTH);
        browsePane.add(browseTable.getScroller(), BorderLayout.CENTER);
        browsePane.add(cmdPanel, BorderLayout.SOUTH);

        // レンダラを設定する
        PublishTypeRenderer pubTypeRenderer = new PublishTypeRenderer();
        browseTable.getColumnModel().getColumn(4).setCellRenderer(pubTypeRenderer);
        ImportedRenderer importedRenderer = new ImportedRenderer();
        browseTable.getColumnModel().getColumn(5).setCellRenderer(importedRenderer);

        // BrowseTableをシングルセレクションにする
        browseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // コンポーネント間のイベント接続を行う
        PropertyChangeListener pl = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {

                Object[] selected = (Object[]) e.getNewValue();
                if (selected != null && selected.length > 0) {
                    PublishedTreeModel model = (PublishedTreeModel) selected[0];
                    if (model.isImported()) {
                        importBtn.setEnabled(false);
                        deleteBtn.setEnabled(true);
                    } else {
                        importBtn.setEnabled(true);
                        deleteBtn.setEnabled(false);
                    }

                } else {
                    importBtn.setEnabled(false);
                    deleteBtn.setEnabled(false);
                }
            }
        };
        browseTable.addPropertyChangeListener(ObjectListTable.SELECTED_OBJECT, pl);

        importBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                importPublishedTree();
            }
        });

        deleteBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeImportedTree();
            }
        });

        cancelBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });

        return browsePane;
    }

    /**
     * ブラウザテーブルで選択した公開Treeをインポートする。
     */
    public void importPublishedTree() {
        Object[] objects = (Object[]) browseTable.getSelectedObject();
        if (objects == null || objects.length == 0) {
            return;
        }
        // テーブルはシングルセレクションである
        // TODO ブラウズ時にbyte[]を取得している...
        final PublishedTreeModel importTree = (PublishedTreeModel) objects[0];

        // サブスクライブリストに追加する
        SubscribedTreeModel sm = new SubscribedTreeModel();
        sm.setUser(GlobalVariables.getUserModel());
        sm.setTreeId(importTree.getId());
        final List<SubscribedTreeModel> subscribeList = new ArrayList<SubscribedTreeModel>(1);
        subscribeList.add(sm);

        // デリゲータを生成する
        sdl = new RemoteStampDelegater();

        // Worker, Timer を実行する
        int delay = 200;
        int maxEstimation = 60 * 1000;
        String mmsg = "公開スタンプをインポートしています...";

        Task task = new Task<Boolean, Void>(app) {//スタンプインポート

            @Override
            protected Boolean doInBackground() {
                sdl.subscribeTrees(subscribeList);
                return !sdl.isError();
            }

            @Override
            protected void succeeded(Boolean result) {
                if (result.booleanValue()) {
                    // スタンプボックスへインポートする
                    stampBox.importPublishedTree(importTree);
                    // Browser表示をインポート済みにする
                    importTree.setImported(true);
                    ((ObjectReflectTableModel<Object>) browseTable.getModel()).fireTableDataChanged();
                    stop();
                } else {
                    JOptionPane.showMessageDialog(frame, "スタンプのインポートに失敗しました.", GlobalConstants.getFrameTitle(title), JOptionPane.WARNING_MESSAGE);
                    stop();
                }
            }

            @Override
            protected void cancelled() {
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
                LogWriter.error(getClass(), cause.getMessage());
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
                LogWriter.error(getClass(), e.getMessage());
            }
        };

        TaskMonitor taskMonitor = appCtx.getTaskMonitor();
        String message = "スタンプ取り込み";
        Component c = frame;
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, mmsg, delay, maxEstimation);
        taskMonitor.addPropertyChangeListener(w);

        appCtx.getTaskService().execute(task);
    }

    /**
     * インポートしているスタンプを削除する。
     */
    public void removeImportedTree() {
        Object[] objects = (Object[]) browseTable.getSelectedObject();
        if (objects == null || objects.length == 0) {
            return;
        }

        // 削除するTreeを取得する
        final PublishedTreeModel removeTree = (PublishedTreeModel) objects[0];
        SubscribedTreeModel sm = new SubscribedTreeModel();
        sm.setTreeId(removeTree.getId());
        sm.setUser(GlobalVariables.getUserModel());
        final List<SubscribedTreeModel> list = new ArrayList<SubscribedTreeModel>(1);
        list.add(sm);

        // DeleteTaskを実行する
        sdl = new RemoteStampDelegater();

        // Unsubscribeタスクを実行する
        int delay = 200;
        int maxEstimation = 60 * 1000;
        String mmsg = "インポート済みスタンプを削除しています...";

        Task task = new Task<Boolean, Void>(app) {//スタンプ取り込み

            @Override
            protected Boolean doInBackground() throws Exception {
                sdl.unsubscribeTrees(list);
                return !sdl.isError();
            }

            @Override
            protected void succeeded(Boolean result) {
                if (result.booleanValue()) {
                    // スタンプボックスから削除する
                    stampBox.removeImportedTree(removeTree.getId());
                    stampBox.getFrame().repaint();

                    // ブラウザ表示を変更する
                    removeTree.setImported(false);
                    ((ObjectReflectTableModel<Object>) browseTable.getModel()).fireTableDataChanged();
                    stop();
                } else {
                    LogWriter.error(getClass(), sdl.getErrorMessage());
                    JOptionPane.showMessageDialog(frame, "スタンプの削除に失敗しました", GlobalConstants.getFrameTitle(title), JOptionPane.WARNING_MESSAGE);
                    stop();
                }
            }

            @Override
            protected void cancelled() {
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
                LogWriter.error(getClass(), cause.getMessage());
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
                LogWriter.error(getClass(), e.getMessage());
            }
        };

        TaskMonitor taskMonitor = appCtx.getTaskMonitor();
        String message = "スタンプ取り込み";
        Component c = frame;
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, mmsg, delay, maxEstimation);
        taskMonitor.addPropertyChangeListener(w);

        appCtx.getTaskService().execute(task);
    }

    /**
     * MEMO:Component
     */
    class PublishTypeRenderer extends DefaultTableCellRenderer {

        /** Creates new IconRenderer */
        public PublishTypeRenderer() {
            super();
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
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
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setForeground(table.getForeground());
                if (row % 2 == 0) {
                    setBackground(EVEN_COLOR);
                } else {
                    setBackground(ODD_COLOR);
                }
            }

            if (value != null && value instanceof String) {
                String pubType = (String) value;
                if (pubType.equals(IInfoModel.PUBLISHED_TYPE_GLOBAL)) {
                    setIcon(WEB_ICON);
                } else {
                    setIcon(HOME_ICON);
                }
                this.setText("");

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
    class ImportedRenderer extends DefaultTableCellRenderer {

        /** Creates new IconRenderer */
        public ImportedRenderer() {
            super();
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
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
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setForeground(table.getForeground());
                if (row % 2 == 0) {
                    setBackground(EVEN_COLOR);
                } else {
                    setBackground(ODD_COLOR);
                }
            }

            if (value != null && value instanceof Boolean) {

                Boolean imported = (Boolean) value;

                if (imported.booleanValue()) {
                    this.setIcon(FLAG_ICON);
                } else {
                    this.setIcon(null);
                }
                this.setText("");

            } else {
                setIcon(null);
                this.setText(value == null ? "" : value.toString());
            }
            return this;
        }
    }
}
