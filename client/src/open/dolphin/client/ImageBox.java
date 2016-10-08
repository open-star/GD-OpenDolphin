/*
 package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.EventHandler;
import java.io.File;
import java.io.FileFilter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import open.dolphin.helper.ComponentMemory;


public class ImageBox extends AbstractMainTool {

    private static final int DEFAULT_COLUMN_COUNT = 3;
    private static final int DEFAULT_IMAGE_WIDTH = 120;
    private static final int DEFAULT_IMAGE_HEIGHT = 120;
    private static final String[] DEFAULT_IMAGE_SUFFIX = {".jpg"};
    private String imageLocation = GlobalConstants.getLocation("schema");
    private JTabbedPane tabbedPane;
    private JButton refreshBtn;
    private int columnCount = DEFAULT_COLUMN_COUNT;
    private int imageWidth = DEFAULT_IMAGE_WIDTH;
    private int imageHeight = DEFAULT_IMAGE_HEIGHT;
    private String[] suffix = DEFAULT_IMAGE_SUFFIX;
    private int defaultWidth = 406;
    private int defaultHeight = 587;
    private int defaultLocX = 537;
    private int defaultLocY = 22;
    private JDialog frame;
    private String title = "シェーマボックス";
  //  private static final int TIMER_DELAY = 200;		// 200 msec 毎にチェック
  //  private static final int MAX_ESTIMATION = 5000;		// 全体の見積もり時間
  //  private static final String PROGRESS_NOTE = "画像をロードしています...";

    @Override
    public void start() {
        initCustomComponents();
        connect();
        setImageLocation(imageLocation);
    }

    @Override
    public void stop() {
        if (tabbedPane != null) {
            int cnt = tabbedPane.getTabCount();
            for (int i = 0; i < cnt; i++) {
                ImagePalette ip = (ImagePalette) tabbedPane.getComponentAt(i);
                if (ip != null) {
                    ip.dispose();
                }
            }
        }
        frame.setVisible(false);
        frame.dispose();
    }

    public JDialog getFrame() {
        return frame;
    }

    public void toFront() {
        if (frame != null) {
            if (!frame.isVisible()) {
                frame.setVisible(true);
            }
            frame.toFront();
        }
    }


    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String loc) {

        this.imageLocation = loc;

        createImagePalettes();

        if (!frame.isVisible()) {
            frame.setVisible(true);
        }

    }

    public void refresh() {
        //(TODO)以下2行は上記のコードが問題有る為に一時的な処理として実装した
        tabbedPane.removeAll();
        this.createImagePalettes();

    }

    private void initCustomComponents() {

        // TabbedPane を生成する
        tabbedPane = new JTabbedPane();

        // 更新ボタンを生成する
        refreshBtn = new JButton(GlobalConstants.getImageIcon("ref_24.gif"));
        refreshBtn.addActionListener((ActionListener) EventHandler.create(ActionListener.class, this, "refresh"));
        refreshBtn.setToolTipText("シェーマリストを更新します");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(refreshBtn);

        // 全体を配置する
        JPanel p = new JPanel(new BorderLayout());
        p.add(btnPanel, BorderLayout.NORTH);
        p.add(tabbedPane, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

        frame = new JDialog((JFrame) null, title, false);
        frame.setResizable(false);

        ComponentMemory cm = new ComponentMemory(frame, new Point(defaultLocX, defaultLocY), new Dimension(defaultWidth, defaultHeight), this);
        cm.setToPreferenceBounds();
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });
        frame.getContentPane().add(p);
    }

    private void connect() {
    }

    public void createImagePalettes() {

        File baseDir = new File(imageLocation);
        if ((!baseDir.exists()) || (!baseDir.isDirectory())) {
            return;
        }

        File[] directories = listDirectories(baseDir);
        if (directories == null || directories.length == 0) {
            return;
        }

        //"基本"タブは必ず先頭に出る
        for (int i = 0; i < directories.length; i++) {
            String tabName = directories[i].getName();
            if (tabName.equals("基本")) {
                ImagePalette imageTable = new ImagePalette(null, columnCount, imageWidth, imageHeight);
                imageTable.setImageSuffix(suffix);
                imageTable.setImageDirectory(directories[i]);
                tabbedPane.addTab(tabName, imageTable);
                break;
            }
        }

        //”基本”タブ以外を表示する
        for (int i = 0; i < directories.length; i++) {
            String tabName = directories[i].getName();
            if (!tabName.equals("基本")) {
                ImagePalette imageTable = new ImagePalette(null, columnCount, imageWidth, imageHeight);
                imageTable.setImageSuffix(suffix);
                imageTable.setImageDirectory(directories[i]);
                tabbedPane.addTab(tabName, imageTable);
            }
        }
    }

    private File[] listDirectories(File dir) {
        DirectoryFilter filter = new DirectoryFilter();
        File[] directories = dir.listFiles(filter);
        return directories;
    }


    public void processWindowClosing() {
        stop();
    }


    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }


    public int getColumnCount() {
        return columnCount;
    }


    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }


    public int getImageWidth() {
        return imageWidth;
    }


    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }


    public int getImageHeight() {
        return imageHeight;
    }


    public void setSuffix(String[] suffix) {
        this.suffix = suffix;
    }


    public String[] getSuffix() {
        return suffix;
    }

    class DirectoryFilter implements FileFilter {

        @Override
        public boolean accept(File path) {
            return path.isDirectory() && !path.isHidden();
        }
    }
}
*/