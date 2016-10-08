package open.dolphin.helper;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import open.dolphin.project.GlobalConstants;

/**
 * Window Menu をサポートするためのクラス。
 * Factory method で WindowMenu をもつ JFrame を生成する。
 *
 * @author Minagawa,Kazushi
 */
public class WindowSupport implements MenuListener {

    private static List<WindowSupport> allWindows = new ArrayList<WindowSupport>();
    private static final String WINDOW_MWNU_NAME = "ウインドウ";
    // Window support が提供するスタッフ
    private JFrame frame;    // フレーム
    private JMenuBar menuBar;    // メニューバー
    private JMenu windowMenu;    // ウインドウメニュー
    private Action windowAction;    // Window Action

    /**
     * WindowSupportを生成する。
     * @param frame
     * @param title フレームタイトル
     * @return WindowSupport
     */
    public static WindowSupport create(final JFrame frame, String title) {

        frame.setTitle(title);

        //<TODO>アイコンを適切な物に変更
        ImageIcon icon = GlobalConstants.getImageIcon("web_32.gif");
        frame.setIconImage(icon.getImage());
        JMenuBar menuBar = new JMenuBar();   // メニューバーを生成する
        JMenu windowMenu = new JMenu(WINDOW_MWNU_NAME);  // Window メニューを生成する
        menuBar.add(windowMenu);   // メニューバーへWindow メニューを追加する
        frame.setJMenuBar(menuBar);   // フレームにメニューバーを設定する

        // Windowメニューのアクション
        // 選択されたらフレームを全面にする
        Action windowAction = new AbstractAction(title) {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.toFront();
            }
        };

        // インスタンスを生成する
        final WindowSupport ret = new WindowSupport(frame, menuBar, windowMenu, windowAction);

        // WindowEvent をこのクラスに通知しリストの管理を行う
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                WindowSupport.windowOpened(ret);
            }

            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                WindowSupport.windowClosed(ret);
            }
        });

        // windowMenu にメニューリスナを設定しこのクラスで処理をする
        windowMenu.addMenuListener(ret);
        return ret;
    }

    //   public static List<WindowSupport> getAllWindows() {
    //       return allWindows;
    //  }
    
    /**
     *
     * @param opened
     */
    public static void windowOpened(WindowSupport opened) {
        // リストに追加する
        allWindows.add(opened);
    }

    /**
     *
     * @param closed
     */
    public static void windowClosed(WindowSupport closed) {
        // リストから削除する
        allWindows.remove(closed);
        closed = null;
    }

    /**
     *
     * @param toCheck
     * @return
     */
    public static boolean contains(WindowSupport toCheck) {
        return allWindows.contains(toCheck);
    }

    /**
     *  プライベートコンストラクタ
     * @param frame
     * @param menuBar
     * @param windowMenu
     * @param windowAction
     */
    private WindowSupport(JFrame frame, JMenuBar menuBar, JMenu windowMenu, Action windowAction) {
        this.frame = frame;
        this.menuBar = menuBar;
        this.windowMenu = windowMenu;
        this.windowAction = windowAction;
    }

    /**
     *
     * @return
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     *
     * @return　メニューバー
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     *
     * @return
     */
    public JMenu getWindowMenu() {
        return windowMenu;
    }

    /**
     *
     * @return
     */
    public Action getWindowAction() {
        return windowAction;
    }

    /**
     * ウインドウメニューが選択された場合、現在オープンしているウインドウのリストを使用し、
     * それらを選択するための MenuItem を追加する。
     */
    @Override
    public void menuSelected(MenuEvent e) {

        // 全てリムーブする
        JMenu wm = (JMenu) e.getSource();
        wm.removeAll();

        // リストから新規に生成する
        for (WindowSupport ws : allWindows) {
            Action action = ws.getWindowAction();
            wm.add(action);
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void menuDeselected(MenuEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void menuCanceled(MenuEvent e) {
    }
}
