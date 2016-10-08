package open.dolphin.client.settings;

import open.dolphin.project.GlobalConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import open.dolphin.helper.PlugInMenuSupport;
import open.dolphin.log.LogWriter;
import open.dolphin.plugin.PluginWrapper;
import open.dolphin.project.GlobalVariables;

/**
 * 環境設定ダイアログ。
 * IAbstractSettingPanelインターフェイスの設定パネルを実行する器。　MEMO:リスナー
 *
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class ProjectSettingDialog implements PropertyChangeListener {

    // GUI
    private JDialog dialog;
    private JPanel itemPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton okButton;
    private JButton cancelButton;
    //
    // 全体のモデル
    //
    private Map<String, IAbstractSettingPanel> settingMap;
    private List<IAbstractSettingPanel> allSettings;
    private List<JToggleButton> allBtns;
    private String startSettingName;
    private boolean loginState;
    private PropertyChangeSupport boundSupport;
    private static final String SETTING_PROP = "SETTING_PROP";
    private boolean okState;
    private final int DEFAULT_WIDTH = 830;
    private final int DEFAULT_HEIGHT = 600;
    private PlugInMenuSupport plugins; //名称とプラグインのペアを格納する連想配列

    /**
     * Creates new ProjectSettingDialog
     * @param plugins 名称とプラグインのペアを格納する連想配列
     */
    public ProjectSettingDialog(PlugInMenuSupport plugins) {
        this.plugins = plugins;
        boundSupport = new PropertyChangeSupport(this);
        allSettings = new ArrayList<IAbstractSettingPanel>();
        // 設定パネル(AbstractSettingPanel)を格納する Hashtableを生成する
        // key=設定プラグインの名前 value=設定プラグイン
        settingMap = new HashMap<String, IAbstractSettingPanel>();
    }

    /**
     *
     * @param prop
     * @param l
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     *
     * @param prop
     * @param l
     */
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     *
     * @return
     */
    public boolean getLoginState() {
        return loginState;
    }

    /**
     *
     * @param b
     */
    public void setLoginState(boolean b) {
        loginState = b;
    }

    /**
     *
     * @return
     */
    public boolean getValue() {
        return GlobalVariables.isValid();
    }

    /**
     *
     */
    public void notifyResult() {
        boolean valid = GlobalVariables.isValid() ? true : false;
        boundSupport.firePropertyChange(SETTING_PROP, !valid, valid);
    }

    /**
     * オープン時に表示する設定画面をセットする。
     * @param startSettingName
     */
    public void setProject(String startSettingName) {
        this.startSettingName = startSettingName;
    }

    /**
     * 設定画面を開始する。
     */
    public void start() {

        try {
            allSettings.add(new HostSettingPanel());
            allSettings.add(new ClaimSettingPanel());
            allSettings.add(new AreaNetWorkSettingPanel());
            allSettings.add(new SendMmlSettingPanel());
            allSettings.add(new KarteSettingPanel());
            allSettings.add(new CodeHelperSettingPanel());
            allSettings.add(new BrowserSettingPanel());
            allSettings.add(new PluginsSettingPanel());
            allSettings.add(new DirectionSettingPanel());

            for (open.dolphin.plugin.IPlugin plugin : getPlugins().values()) {
                PluginWrapper pluginWrapper = new PluginWrapper(plugin);
                IAbstractSettingPanel panel = pluginWrapper.configure();
                if (panel != null) {
                    allSettings.add(panel);
                }
            }

        } catch (Exception e) {
            return;
        }

        // GUI を構築しモデルをバインドする
        initCustomComponents();

        // オープン時に表示する設定画面を決定する
        int index = 0;
        if (startSettingName != null) {
            for (IAbstractSettingPanel setting : allSettings) {
                if (startSettingName.equals(setting.getId())) {
                    break;
                }
                index++;
            }
        }
        index = (index >= 0 && index < allSettings.size()) ? index : 0;
        // ボタンを押して表示する
        allBtns.get(index).doClick();
    }

    /**
     * GUI を構築する。
     */
    private void initCustomComponents() {
        itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // 設定プラグインを起動するためのトグルボタンを生成し
        // パネルへ加える
        allBtns = new ArrayList<JToggleButton>();
        ButtonGroup bg = new ButtonGroup();
        for (IAbstractSettingPanel setting : allSettings) {
            String id = setting.getId();
            String text = setting.getTitle();
            String iconStr = setting.getIcon();
            ImageIcon icon = GlobalConstants.getImageIcon(iconStr);
            JToggleButton tb = new JToggleButton(text, icon);
            if (GlobalConstants.isWin()) {
                tb.setMargin(new Insets(0, 0, 0, 0));
            }
            tb.setHorizontalTextPosition(SwingConstants.CENTER);
            tb.setVerticalTextPosition(SwingConstants.BOTTOM);
            itemPanel.add(tb);
            bg.add(tb);
            tb.setActionCommand(id);
            allBtns.add(tb);
        }
        // 設定パネルのコンテナとなるカードパネル
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        okButton = new JButton();
        okButton.setText("保 存");
        okButton.setEnabled(false);

        cancelButton = new JButton();
        cancelButton.setText("閉じる");

        // 全体ダイアログのコンテントパネル
        JPanel panel = new JPanel(new BorderLayout(11, 0));

        JScrollPane scroller = new JScrollPane();
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        scroller.setViewportView(itemPanel);

        panel.add(scroller, BorderLayout.NORTH);
        panel.add(cardPanel, BorderLayout.CENTER);

        // ダイアログを生成する
        String title = "環境設定";
        Object[] options = new Object[]{okButton, cancelButton};
        JOptionPane jop = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, okButton);
        dialog = jop.createDialog((Frame) null, GlobalConstants.getFrameTitle(title));
        dialog.setResizable(false);
        dialog.setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - DEFAULT_WIDTH) / 2;
        int y = (size.height - DEFAULT_HEIGHT) / 3;
        dialog.setLocation(x, y);

        // イベント接続を行う
        connect();
    }

    /**
     * GUI コンポーネントのイベント接続を行う。
     */
    private void connect() {
        // 設定項目ボタンに追加するアクションリスナを生成する
        ActionListener al = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                IAbstractSettingPanel theSetting = null;
                String name = event.getActionCommand();
                for (IAbstractSettingPanel setting : allSettings) {
                    String id = setting.getId();
                    if (id.equals(name)) {
                        theSetting = setting;
                        break;
                    }
                }
                if (theSetting != null) {
                    startSetting(theSetting);
                }
            }
        };

        // 全てのボタンにリスナを追加する
        for (JToggleButton btn : allBtns) {
            btn.addActionListener(al);
        }

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doOk();
            }
        });
        okButton.setEnabled(false);

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                doCancel();
            }
        });
    }

    /**
     * 選択された項目(SettingPanel)の編集を開始する.
     */
    private void startSetting(final IAbstractSettingPanel sp) {
        // 既に生成されている場合はそれを表示する
        if (sp.getContext() != null) {
            cardLayout.show(cardPanel, sp.getTitle());
            return;
        }

        try {
            settingMap.put(sp.getId(), sp);
            sp.setContext(ProjectSettingDialog.this);
            sp.start();
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    cardPanel.add(sp.getPanel(), sp.getTitle());
                    cardLayout.show(cardPanel, sp.getTitle());
                    if (!dialog.isVisible()) {
                        dialog.setVisible(true);
                    }
                }
            });
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * SettingPanel の state が変化した場合に通知を受け、
     * 全てのカードをスキャンして OK ボタンをコントロールする。
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();
        if (!prop.equals(IAbstractSettingPanel.STATE_PROP)) {
            return;
        }
        // 全てのカードをスキャンして OK ボタンをコントロールする
        boolean newOk = true;
        Iterator<IAbstractSettingPanel> iter = settingMap.values().iterator();
        int cnt = 0;//MEMO: unused?
        while (iter.hasNext()) {
            cnt++;
            IAbstractSettingPanel p = iter.next();
            if (p.getState().equals(IAbstractSettingPanel.State.INVALID_STATE)) {
                newOk = false;
                break;
            }
        }
        if (okState != newOk) {
            okState = newOk;
            okButton.setEnabled(okState);
        }
    }

    /**
     *
     */
    public void doOk() {
        Iterator<IAbstractSettingPanel> iter = settingMap.values().iterator();
        while (iter.hasNext()) {
            IAbstractSettingPanel p = iter.next();
            p.save();
        }
        dialog.setVisible(false);
        dialog.dispose();
        notifyResult();
    }

    /**
     *
     */
    public void doCancel() {
        dialog.setVisible(false);
        dialog.dispose();
        notifyResult();
    }

    /**
     * @return the plugins
     */
    public PlugInMenuSupport getPlugins() {
        return plugins;
    }
}
