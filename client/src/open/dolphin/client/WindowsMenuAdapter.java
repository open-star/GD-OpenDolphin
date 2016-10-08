package open.dolphin.client;

import java.awt.event.ActionEvent;
import javax.swing.event.MenuListener;
import open.dolphin.project.GlobalConstants;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import open.dolphin.helper.ChartMenuSupport;
import open.dolphin.helper.MainMenuSupport;
import open.dolphin.helper.PlugInMenuSupport;
import open.dolphin.plugin.PluginWrapper;
import open.dolphin.project.GlobalVariables;
import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

/**
 * Menu Factory for Mac. 
 * 
 * @author Minagawa, Kazushi
 */
public class WindowsMenuAdapter implements IMenuAdapter {

    private MainMenuSupport main;
    private ChartMenuSupport chart;
    private PlugInMenuSupport plugin; //名称とプラグインのペアを格納する連想配列
    private JMenuBar menuBar;
    private JPanel toolPanel;
    private ActionMap actionMap; //キーまたは Action 名と呼ばれる Object から Action へのマップを提供します。
    private IChart chartContext;

    /** Creates a new instance of ApplicationMenu */
    public WindowsMenuAdapter() {
    }

    /**
     *
     * @param chartContext
     * @param main
     * @param chart
     * @param plugin 名称とプラグインのペアを格納する連想配列
     */
    @Override
    public void setMenuSupports(IChart chartContext, MainMenuSupport main, ChartMenuSupport chart, PlugInMenuSupport plugin) {
        this.chartContext = chartContext;
        this.main = main;
        this.chart = chart;
        this.plugin = plugin;
    }

    /**
     *
     * @return
     */
    @Override
    public JMenuBar getMenuBarProduct() {
        return menuBar;
    }

    /**
     *
     * @return
     */
    @Override
    public JPanel getToolPanelProduct() {
        return toolPanel;
    }

    /**
     *
     * @return
     */
    @Override
    public ActionMap getActionMap() {
        return actionMap;
    }

    /**
     *
     */
    @Action
    public void newKarte() {
        chart.newKarteCommandExecute();
    }

    /**
     *
     */
    @Action
    public void newDocument() {
        chart.newDocumentCommandExecute();
    }

    /**
     *
     */
    @Action
    public void openKarte() {
        main.openKarteCommandExecute();
    }

    /**
     *
     */
    @Action
    public void close() {
        chart.closeCommandExecute();
    }

    /**
     *
     */
    @Action
    public void save() {
        chart.saveCommandExecute();
    }

    /**
     *
     */
    @Action
    public void delete() {
        chart.deleteCommandExecute();
    }

    /**
     *
     */
    @Action
    public void direction() {
        chart.directionCommandExecute();
    }

    /**
     *
     */
    @Action
    public void printerSetup() {
        main.printerSetupCommandExecute();
    }

    /**
     *
     */
    @Action
    public void print() {
        chart.printCommandExecute();
    }

    /**
     *
     */
    @Action
    public void processExit() {
        //     main.sendToChain("processExit");
        main.closeCommandExecute();
    }

    /**
     *
     */
    @Action
    public void modifyKarte() {
        chart.modifyKarteCommandExecute();
    }

    /**
     *
     */
    @Action
    public void undo() {
        chart.undoCommandExecute();
    }

    /**
     *
     */
    @Action
    public void redo() {
        chart.redoCommandExecute();
    }

    /**
     *
     */
    @Action
    public void cut() {
        chart.cut();
    }

    /**
     *
     */
    @Action
    public void copy() {
        chart.copy();
    }

    /**
     *
     */
    @Action
    public void paste() {
        chart.paste();
    }

    /**
     *
     */
    @Action
    public void letterPaste() {
        chart.letterPasteCommandExecute();
    }

    /**
     *
     */
    @Action
    public void letterPasteFromKarte() {
        chart.letterPasteFromKarteCommandExecute();
    }

    /**
     *
     */
    @Action
    public void quickEdit() {
        chart.quickEditExcecute();
    }

    //  @Action
    //  public void insertTosekiStamp() {
    //     chart.insertTosekiStampCommand();
    //  }
    /**
     *
     */
    @Action
    public void ascending() {
        chart.ascendingCommandExecute();
    }

    /**
     *
     */
    @Action
    public void descending() {
        chart.descendingCommandExecute();
    }

    /**
     *
     */
    @Action
    public void showModified() {
        if (isShowModified()) {
            chart.showModifiedCommandExecute();
        } else {
            chart.hideModifiedCommandExecute();
        }
    }

    /**
     *
     */
    @Action
    public void showUnsend() {
        if (isShowUnsend()) {
            chart.showUnsendCommandExecute();
        } else {
            chart.hideUnsendCommandExecute();
        }
    }

    /**
     *
     */
    @Action
    public void showSend() {
        if (isShowSend()) {
            chart.showSendCommandExecute();
        } else {
            chart.hideSendCommandExecute();
        }
    }

    /**
     *
     */
    @Action
    public void showNewest() {
        if (isShowNewest()) {
            chart.showNewestCommandExecute();
        } else {
            chart.hideNewestCommandExecute();
        }
    }

    /**
     *
     */
    @Action
    public void setKarteEnviroment() {
        main.setKarteEnviromentCommandExecute();
    }

    /**
     *
     */
    @Action
    public void insertDisease() {
    }

    /**
     *
     */
    @Action
    public void insertText() {
    }

    /**
     *
     */
    @Action
    public void insertSchema() {
    }

    /**
     *
     */
    @Action
    public void insertStamp() {
    }

    /**
     *
     */
    @Action
    public void selectInsurance() {
    }

    /**
     *
     */
    @Action
    public void size() {
    }

    /**
     * 
     */
    @Action
    public void fontLarger() {
        chart.fontLargerCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontSmaller() {
        chart.fontSmallerCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontStandard() {
        chart.fontStandardCommandExecute();
    }

    /**
     *
     */
    @Action
    public void style() {
    }

    /**
     *
     */
    @Action
    public void fontBold() {
        chart.fontBoldCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontItalic() {
        chart.fontItalicCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontUnderline() {
        chart.fontUnderlineCommandExecute();
    }

    /**
     *
     */
    @Action
    public void justify() {
    }

    /**
     *
     */
    @Action
    public void leftJustify() {
        chart.leftJustifyCommandExecute();
    }

    /**
     *
     */
    @Action
    public void centerJustify() {
        chart.centerJustifyCommandExecute();
    }

    /**
     *
     */
    @Action
    public void rightJustify() {
        chart.rightJustifyCommandExecute();
    }

    /**
     *
     */
    @Action
    public void color() {
    }

    /**
     *
     */
    @Action
    public void fontRed() {
        chart.fontRedCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontOrange() {
        chart.fontOrangeCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontYellow() {
        chart.fontYellowCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontGreen() {
        chart.fontGreenCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontBlue() {
        chart.fontBlueCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontPurple() {
        chart.fontPurpleCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontGray() {
        chart.fontGrayCommandExecute();
    }

    /**
     *
     */
    @Action
    public void fontBlack() {
        chart.fontBlackCommandExecute();
    }

    /**
     *
     */
    @Action
    public void resetStyle() {
        chart.resetStyleCommandExecute();
    }

    /**
     *
     */
    @Action
    public void showStampBox() {
        main.showStampBoxCommandExecute();
    }

    /**
     *
     */
    @Action
    public void showSchemaBox() {
        main.showSchemaBoxCommandExecute();
    }

    /**
     *
     */
    @Action
    public void showTemplateEditor() {
        main.showTemplateEditorCommandExecute();
    }

    /**
     *
     */
    @Action
    public void changePassword() {
        main.changePasswordCommandExecute();
    }

    /**
     *
     */
    @Action
    public void addUser() {
        main.addUserCommandExecute();
    }

    /**
     *
     */
    @Action
    public void browseDolphinSupport() {
        main.browseDolphinSupportCommandExecute();
    }

    /**
     *
     */
    @Action
    public void browseDolphinProject() {
        main.browseDolphinProjectCommandExecute();
    }

    /**
     *
     */
    /**
     *
     */
    @Action
    public void browseMedXml() {
        main.browseMedXmlCommandExecute();
    }

    /**
     *
     */
    @Action
    public void showAbout() {
        main.showAboutCommandExecute();
    }

    /**
     *
     * @return
     */
    private boolean isShowModified() {
        return menuBar.getMenu(2).getItem(2).isSelected();
    }

    /**
     *
     * @return
     */
    private boolean isShowUnsend() {
        return menuBar.getMenu(2).getItem(3).isSelected();
    }

    /**
     *
     * @return
     */
    private boolean isShowSend() {
        return menuBar.getMenu(2).getItem(4).isSelected();
    }

    /**
     *
     * @return
     */
    private boolean isShowNewest() {
        return menuBar.getMenu(2).getItem(6).isSelected();
    }

    /**
     *
     * @param menuBar メニューバー
     * @param enabled
     */
    @Override
    public void build(JMenuBar menuBar, boolean enabled) {

        this.menuBar = menuBar;
        ApplicationContext ctx = GlobalConstants.getApplicationContext();
        ResourceMap resMap = ctx.getResourceMap(WindowsMenuAdapter.class);
        actionMap = ctx.getActionMap(this);

        // ToolBar
        JToolBar fileBar = null;
        JToolBar editBar = null;
        JToolBar textBar1 = null;
        JToolBar textBar2 = null;

        if (chart != null) {
            fileBar = new JToolBar();
            fileBar.setName("fileBar");
            editBar = new JToolBar();
            editBar.setName("editBar");

            textBar1 = new JToolBar();
            textBar1.setName("文字サイズ");
            textBar2 = new JToolBar();
            textBar2.setName("文字変形");

            toolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            toolPanel.add(fileBar);
            toolPanel.add(editBar);
            toolPanel.add(textBar1);
            toolPanel.add(textBar2);
        }

        // File
        JMenu fileMenu = new JMenu();
        fileMenu.setName("fileMenu");

        // 新規カルテ
        JMenuItem newKarte = new JMenuItem();
        newKarte.setName("newKarte");
        newKarte.setAction(actionMap.get("newKarte"));
        setAccelerator(newKarte, KeyEvent.VK_N);
        fileMenu.add(newKarte);
        if (chart != null) {
            JButton newKarteBtn = new JButton();
            newKarteBtn.setAction(actionMap.get("newKarte"));
            newKarteBtn.setText(null);
            newKarteBtn.setToolTipText("新しいカルテを作成します");
            fileBar.add(newKarteBtn);
        }

        // 新規文書
        JMenuItem newDocument = new JMenuItem();
        newDocument.setName("newDocument");
        newDocument.setAction(actionMap.get("newDocument"));
        fileMenu.add(newDocument);
        if (chart != null) {
            JButton newDocBtn = new JButton();
            newDocBtn.setAction(actionMap.get("newDocument"));
            newDocBtn.setText(null);
            newDocBtn.setToolTipText("新しい文書を作成します");
            fileBar.add(newDocBtn);
        }

        // 開く
        JMenuItem openKarte = new JMenuItem();
        openKarte.setName("openKarte");
        openKarte.setAction(actionMap.get("openKarte"));
        setAccelerator(openKarte, KeyEvent.VK_O);
        fileMenu.add(openKarte);

        fileMenu.add(new JSeparator());

        // 閉じる
        JMenuItem close = new JMenuItem();
        close.setName("close");
        close.setAction(actionMap.get("close"));
        setAccelerator(close, KeyEvent.VK_W);
        fileMenu.add(close);

        // 保存
        JMenuItem save = new JMenuItem();
        save.setName("save");
        save.setAction(actionMap.get("save"));
        setAccelerator(save, KeyEvent.VK_S);
        fileMenu.add(save);
        if (chart != null) {
            JButton saveBtn = new JButton();
            saveBtn.setAction(actionMap.get("save"));
            saveBtn.setText(null);
            saveBtn.setToolTipText("保存します");
            fileBar.add(saveBtn);
        }

        fileMenu.add(new JSeparator());


        // 指示
        JMenuItem direction = new JMenuItem();
        direction.setName("direction");
        direction.setAction(actionMap.get("direction"));
        direction.setEnabled(enabled);
        fileMenu.add(direction);
        if (chart != null) {
            JButton directionBtn = new JButton();
            directionBtn.setAction(actionMap.get("direction"));
            directionBtn.setText(null);
            directionBtn.setEnabled(enabled);
            directionBtn.setToolTipText("指示箋印刷");
            fileBar.add(directionBtn);
        }

        fileMenu.add(new JSeparator());

        /*
        for (final Map.Entry entry : plugin.entrySet()) {
        PluginWrapper pluginWrapper = new PluginWrapper((IPlugin) entry.getValue());
        if (pluginWrapper.getType() == IPlugin.Type.menu) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setName((String) entry.getKey());

        javax.swing.Action action = new AbstractAction((String) entry.getKey()) {

        @Override
        public void actionPerformed(ActionEvent e) {
        Object[] request = new Object[1];
        request[0] = chartContext;
        Object[] response = new Object[1];
        plugin.execute((String) entry.getKey(), request, response);
        }
        };

        menuItem.setAction(action);
        menuItem.setText(pluginWrapper.getReadableName());
        toolMenu.add(menuItem);
        }
        }
         */

        // 印刷設定
        JMenuItem printerSetup = new JMenuItem();
        printerSetup.setName("printerSetup");
        printerSetup.setAction(actionMap.get("printerSetup"));
        fileMenu.add(printerSetup);

        // 印刷
        JMenuItem print = new JMenuItem();
        print.setName("print");
        print.setAction(actionMap.get("print"));
        setAccelerator(print, KeyEvent.VK_P);
        fileMenu.add(print);
        if (chart != null) {
            JButton printBtn = new JButton();
            printBtn.setAction(actionMap.get("print"));
            printBtn.setText(null);
            printBtn.setToolTipText("印刷します");
            fileBar.add(printBtn);
        }

        fileMenu.add(new JSeparator());

        // 終了
        JMenuItem exit = new JMenuItem();
        exit.setName("processExit");
        exit.setAction(actionMap.get("processExit"));
        fileMenu.add(exit);
        setAccelerator(exit, KeyEvent.VK_Q);
        /******************************************************/
        // Edit
        JMenu editMenu = new JMenu();
        editMenu.setName("editMenu");

        // 修正
        JMenuItem modifyKarte = new JMenuItem();
        modifyKarte.setName("modifyKarte");
        modifyKarte.setAction(actionMap.get("modifyKarte"));
        setAccelerator(modifyKarte, KeyEvent.VK_M);
        editMenu.add(modifyKarte);
        if (chart != null) {
            JButton modifyKarteBtn = new JButton();
            modifyKarteBtn.setAction(actionMap.get("modifyKarte"));
            modifyKarteBtn.setText(null);
            modifyKarteBtn.setToolTipText("修正します");
            editBar.add(modifyKarteBtn);
        }

        editMenu.add(new JSeparator());

        // Undo
        JMenuItem undo = new JMenuItem();
        undo.setName("undo");
        undo.setAction(actionMap.get("undo"));
        setAccelerator(undo, KeyEvent.VK_Z);
        editMenu.add(undo);
        if (chart != null) {
            JButton undoBtn = new JButton();
            undoBtn.setAction(actionMap.get("undo"));
            undoBtn.setText(null);
            undoBtn.setToolTipText("元に戻します");
            editBar.add(undoBtn);
        }

        // Redo
        JMenuItem redo = new JMenuItem();
        redo.setName("redo");
        redo.setAction(actionMap.get("redo"));
        setAccelerator(redo, KeyEvent.VK_Z, true);
        editMenu.add(redo);
        if (chart != null) {
            JButton redoBtn = new JButton();
            redoBtn.setAction(actionMap.get("redo"));
            redoBtn.setText(null);
            redoBtn.setToolTipText("再実行します");
            editBar.add(redoBtn);
        }

        editMenu.add(new JSeparator());

        // Cut
        JMenuItem cut = new JMenuItem();
        cut.setName("cut");
        cut.setAction(actionMap.get("cut"));
        setAccelerator(cut, KeyEvent.VK_X);
        editMenu.add(cut);
        if (chart != null) {
            JButton cutBtn = new JButton();
            cutBtn.setAction(actionMap.get("cut"));
            cutBtn.setText(null);
            cutBtn.setToolTipText("切り取ります");
            editBar.add(cutBtn);
        }

        // Copy
        JMenuItem copy = new JMenuItem();
        copy.setName("copy");
        copy.setAction(actionMap.get("copy"));
        setAccelerator(copy, KeyEvent.VK_C);
        editMenu.add(copy);
        if (chart != null) {
            JButton copyBtn = new JButton();
            copyBtn.setAction(actionMap.get("copy"));
            copyBtn.setText(null);
            copyBtn.setToolTipText("コピーします");
            editBar.add(copyBtn);
        }

        // Paste
        JMenuItem paste = new JMenuItem();
        paste.setName("paste");
        paste.setAction(actionMap.get("paste"));
        setAccelerator(paste, KeyEvent.VK_V);
        editMenu.add(paste);
        if (chart != null) {
            JButton pasteBtn = new JButton();
            pasteBtn.setAction(actionMap.get("paste"));
            pasteBtn.setText(null);
            pasteBtn.setToolTipText("貼り付けます");
            editBar.add(pasteBtn);
        }

        //テキストバー
        if (chart != null) {
            //文字拡大
            JButton bigTextBtn = new JButton();
            bigTextBtn.setAction(actionMap.get("fontLarger"));
            bigTextBtn.setText("");
            bigTextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/big.gif")));
            bigTextBtn.setToolTipText("文字を拡大します");
            textBar1.add(bigTextBtn);

            //標準文字
            JButton stdTextBtn = new JButton();
            stdTextBtn.setAction(actionMap.get("fontStandard"));
            stdTextBtn.setText("");
            stdTextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/std.gif")));
            stdTextBtn.setToolTipText("文字を標準サイズにします");
            textBar1.add(stdTextBtn);

            //文字縮小
            JButton smlTextBtn = new JButton();
            smlTextBtn.setAction(actionMap.get("fontSmaller"));
            smlTextBtn.setText("");
            smlTextBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/sml.gif")));
            smlTextBtn.setToolTipText("文字を縮小します");
            textBar1.add(smlTextBtn);


            //ボールド
            JButton bldStyleBtn = new JButton();
            bldStyleBtn.setAction(actionMap.get("fontBold"));
            bldStyleBtn.setText("");
            bldStyleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/bold.png")));
            bldStyleBtn.setToolTipText("文字を太字にします");
            textBar2.add(bldStyleBtn);

            //イタリック
            JButton itkStyleBtn = new JButton();
            itkStyleBtn.setAction(actionMap.get("fontItalic"));
            itkStyleBtn.setText("");
            itkStyleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/italic.png")));
            itkStyleBtn.setToolTipText("文字をイタリックにします");
            textBar2.add(itkStyleBtn);

            //アンダーライン
            JButton udlStyleBtn = new JButton();
            udlStyleBtn.setAction(actionMap.get("fontUnderline"));
            udlStyleBtn.setText("");
            udlStyleBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/underline.png")));
            udlStyleBtn.setToolTipText("文字にアンダーラインを引きます");
            textBar2.add(udlStyleBtn);

            //左揃え
            JButton lftLayoutBtn = new JButton();
            lftLayoutBtn.setAction(actionMap.get("leftJustify"));
            lftLayoutBtn.setText("");
            lftLayoutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/left.png")));
            lftLayoutBtn.setToolTipText("文字を左揃えにします");
            textBar2.add(lftLayoutBtn);

            //中央揃え
            JButton cntLayoutBtn = new JButton();
            cntLayoutBtn.setAction(actionMap.get("centerJustify"));
            cntLayoutBtn.setText("");
            cntLayoutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/center.png")));
            cntLayoutBtn.setToolTipText("文字を中央揃えにします");
            textBar2.add(cntLayoutBtn);

            //右揃え
            JButton ritLayoutBtn = new JButton();
            ritLayoutBtn.setAction(actionMap.get("rightJustify"));
            ritLayoutBtn.setText("");
            ritLayoutBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/client/resources/right.png")));
            ritLayoutBtn.setToolTipText("文字を右揃えにします");
            textBar2.add(ritLayoutBtn);

        }


        /******************************************************/
        // Karte
        JMenu karteMenu = new JMenu();
        karteMenu.setName("karteMenu");

        // 昇順
        JRadioButtonMenuItem ascending = new JRadioButtonMenuItem();
        ascending.setName("ascending");
        ascending.setAction(actionMap.get("ascending"));
        actionMap.get("ascending").putValue("menuItem", ascending);
        karteMenu.add(ascending);

        // 降順
        JRadioButtonMenuItem descending = new JRadioButtonMenuItem();
        descending.setName("descending");
        descending.setAction(actionMap.get("descending"));
        actionMap.get("descending").putValue("menuItem", descending);
        karteMenu.add(descending);

        // RadiButtonGroup
        ButtonGroup bg = new ButtonGroup();
        bg.add(ascending);
        bg.add(descending);


        // 修正履歴表示 
        JCheckBoxMenuItem showModified = new JCheckBoxMenuItem();
        showModified.setSelected(GlobalVariables.getShowModifiedKarte());
        showModified.setName("showModified");
        showModified.setAction(actionMap.get("showModified"));
        actionMap.get("showModified").putValue("menuItem", showModified);
        karteMenu.add(showModified);

        //    if (true) {
        // 修正履歴表示
        JCheckBoxMenuItem showUnsend = new JCheckBoxMenuItem();
        showUnsend.setSelected(GlobalVariables.getShowUnsendKarte());
        showUnsend.setName("showUnsend");
        showUnsend.setAction(actionMap.get("showUnsend"));
        actionMap.get("showUnsend").putValue("menuItem", showUnsend);
        karteMenu.add(showUnsend);

        // 修正履歴表示
        JCheckBoxMenuItem showSend = new JCheckBoxMenuItem();
        showSend.setSelected(GlobalVariables.getShowSendKarte());
        showSend.setName("showSend");
        showSend.setAction(actionMap.get("showSend"));
        actionMap.get("showSend").putValue("menuItem", showSend);
        karteMenu.add(showSend);
        //    }
        karteMenu.add(new JSeparator());
        // 修正履歴表示
        JCheckBoxMenuItem showNewest = new JCheckBoxMenuItem();
        showNewest.setSelected(GlobalVariables.getShowNewestKarte());
        showNewest.setName("showNewest");
        showNewest.setAction(actionMap.get("showNewest"));
        actionMap.get("showNewest").putValue("menuItem", showNewest);

        karteMenu.add(showNewest);

        karteMenu.add(new JSeparator());
        // 環境設定 
        JMenuItem setKarteEnviroment = new JMenuItem();
        setKarteEnviroment.setName("setKarteEnviroment");
        setKarteEnviroment.setAction(actionMap.get("setKarteEnviroment"));
        setAccelerator(setKarteEnviroment, KeyEvent.VK_E);
        karteMenu.add(setKarteEnviroment);

        /******************************************************/
        // Insert
        JMenu insertMenu = new JMenu();
        insertMenu.setName("insertMenu");
        if (chart != null) {
            insertMenu.addMenuListener((MenuListener) chart);
        }

        JMenu insertDisease = new JMenu();
        insertDisease.setName("insertDisease");
        insertDisease.setAction(actionMap.get("insertDisease"));
        insertMenu.add(insertDisease);

        JMenu insertText = new JMenu();
        insertText.setName("insertText");
        insertText.setAction(actionMap.get("insertText"));
        insertMenu.add(insertText);

        JMenu insertSchema = new JMenu();
        insertSchema.setName("insertSchema");
        insertSchema.setAction(actionMap.get("insertSchema"));
        insertMenu.add(insertSchema);

        JMenu insertStamp = new JMenu();
        insertStamp.setName("insertStamp");
        insertStamp.setAction(actionMap.get("insertStamp"));
        insertMenu.add(insertStamp);

        /******************************************************/
        // Text
        JMenu textMenu = new JMenu();
        textMenu.setName("textMenu");
        if (chart != null) {
            textMenu.addMenuListener((MenuListener) chart);
        }

        //// size ////
        JMenu size = new JMenu();
        size.setName("size");
        size.setAction(actionMap.get("size"));
        textMenu.add(size);

        JMenuItem fontLarger = new JMenuItem();
        fontLarger.setName("fontLarger");
        fontLarger.setAction(actionMap.get("fontLarger"));
        //setAccelerator(fontLarger, KeyEvent.VK_PLUS, true);
        size.add(fontLarger);

        JMenuItem fontSmaller = new JMenuItem();
        fontSmaller.setName("fontSmaller");
        fontSmaller.setAction(actionMap.get("fontSmaller"));
        //setAccelerator(fontSmaller, KeyEvent.VK_MINUS);
        size.add(fontSmaller);

        JMenuItem fontStandard = new JMenuItem();
        fontStandard.setName("fontStandard");
        fontStandard.setAction(actionMap.get("fontStandard"));
        //setAccelerator(fontStandard, KeyEvent.VK_NUMBER_SIGN, true);
        size.add(fontStandard);

        //// style ////
        JMenu style = new JMenu();
        style.setName("style");
        style.setAction(actionMap.get("style"));
        textMenu.add(style);

        JMenuItem fontBold = new JMenuItem();
        fontBold.setName("fontBold");
        fontBold.setAction(actionMap.get("fontBold"));
        setAccelerator(fontBold, KeyEvent.VK_B);
        style.add(fontBold);

        JMenuItem fontItalic = new JMenuItem();
        fontItalic.setName("fontItalic");
        fontItalic.setAction(actionMap.get("fontItalic"));
        setAccelerator(fontItalic, KeyEvent.VK_I);
        style.add(fontItalic);

        JMenuItem fontUnderline = new JMenuItem();
        fontUnderline.setName("fontUnderline");
        fontUnderline.setAction(actionMap.get("fontUnderline"));
        setAccelerator(fontUnderline, KeyEvent.VK_U);
        style.add(fontUnderline);

        //// justify ////
        JMenu justify = new JMenu();
        justify.setName("justify");
        justify.setAction(actionMap.get("justify"));
        textMenu.add(justify);

        JMenuItem leftJustify = new JMenuItem();
        leftJustify.setName("leftJustify");
        leftJustify.setAction(actionMap.get("leftJustify"));
        //setAccelerator(leftJustify, KeyEvent.VK_OPEN_BRACKET);
        justify.add(leftJustify);

        JMenuItem centerJustify = new JMenuItem();
        centerJustify.setName("centerJustify");
        centerJustify.setAction(actionMap.get("centerJustify"));
        //setAccelerator(centerJustify, KeyEvent.VK_CIRCUMFLEX);
        justify.add(centerJustify);

        JMenuItem rightJustify = new JMenuItem();
        rightJustify.setName("rightJustify");
        rightJustify.setAction(actionMap.get("rightJustify"));
        //setAccelerator(rightJustify, KeyEvent.VK_CLOSE_BRACKET);
        justify.add(rightJustify);

        //// Color ////
        JMenu color = new JMenu();
        color.setName("color");
        color.setAction(actionMap.get("color"));
        textMenu.add(color);

        JMenuItem fontRed = new JMenuItem();
        fontRed.setName("fontRed");
        fontRed.setAction(actionMap.get("fontRed"));
        color.add(fontRed);

        JMenuItem fontOrange = new JMenuItem();
        fontOrange.setName("fontOrange");
        fontOrange.setAction(actionMap.get("fontOrange"));
        color.add(fontOrange);

        JMenuItem fontYellow = new JMenuItem();
        fontYellow.setName("fontYellow");
        fontYellow.setAction(actionMap.get("fontYellow"));
        color.add(fontYellow);

        JMenuItem fontGreen = new JMenuItem();
        fontGreen.setName("fontGreen");
        fontGreen.setAction(actionMap.get("fontGreen"));
        color.add(fontGreen);

        JMenuItem fontBlue = new JMenuItem();
        fontBlue.setName("fontBlue");
        fontBlue.setAction(actionMap.get("fontBlue"));
        color.add(fontBlue);

        JMenuItem fontPurple = new JMenuItem();
        fontPurple.setName("fontPurple");
        fontPurple.setAction(actionMap.get("fontPurple"));
        color.add(fontPurple);

        JMenuItem fontGray = new JMenuItem();
        fontGray.setName("fontGray");
        fontGray.setAction(actionMap.get("fontGray"));
        color.add(fontGray);

        JMenuItem fontBlack = new JMenuItem();
        fontBlack.setName("fontBlack");
        fontBlack.setAction(actionMap.get("fontBlack"));
        color.add(fontBlack);

        /******************************************************/
        // Tool メニュー
        JMenu toolMenu = new JMenu();
        toolMenu.setName("toolMenu");
        // メニュー項目
        JMenuItem showStampBox = new JMenuItem();
        showStampBox.setName("showStampBox");
        showStampBox.setAction(actionMap.get("showStampBox"));
        toolMenu.add(showStampBox);

        JMenuItem showSchemaBox = new JMenuItem();
        showSchemaBox.setName("showSchemaBox");
        showSchemaBox.setAction(actionMap.get("showSchemaBox"));
        toolMenu.add(showSchemaBox); // 指定されたメニュー項目をこのメニューに追加します。

        toolMenu.add(new JSeparator());
        // プラグイン
        for (final Map.Entry entry : plugin.entrySet()) {
            PluginWrapper pluginWrapper = new PluginWrapper((open.dolphin.plugin.IPlugin) entry.getValue());
            if (pluginWrapper.getType() == open.dolphin.plugin.IPlugin.Type.toolmenu) {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setName((String) entry.getKey()); // プラグインの名前
                javax.swing.Action action = new AbstractAction((String) entry.getKey()) {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PluginWrapper pluginwrapper = new PluginWrapper(plugin.get((String) entry.getKey()));
                        pluginwrapper.execute(chartContext);
                    }
                };

                menuItem.setAction(action);
                menuItem.setText(pluginWrapper.getReadableName());
                toolMenu.add(menuItem);
            }
        }

        toolMenu.add(new JSeparator());

        JMenuItem changePassword = new JMenuItem();
        changePassword.setName("changePassword");
        changePassword.setAction(actionMap.get("changePassword"));
        toolMenu.add(changePassword);

        JMenuItem addUser = new JMenuItem();
        addUser.setName("addUser");
        addUser.setAction(actionMap.get("addUser"));
        toolMenu.add(addUser);


        /******************************************************/
        // Help
        JMenu help = new JMenu();
        help.setName("helpMenu");

        JMenuItem browseDolphinSupport = new JMenuItem();
        browseDolphinSupport.setName("browseDolphinSupport");
        browseDolphinSupport.setAction(actionMap.get("browseDolphinSupport"));
        help.add(browseDolphinSupport);

        JMenuItem browseDolphinProject = new JMenuItem();
        browseDolphinProject.setName("browseDolphinProject");
        browseDolphinProject.setAction(actionMap.get("browseDolphinProject"));
        help.add(browseDolphinProject);

        JMenuItem browseMedXml = new JMenuItem();
        browseMedXml.setName("browseMedXml");
        browseMedXml.setAction(actionMap.get("browseMedXml"));
        help.add(browseMedXml);

        help.add(new JSeparator());

        JMenuItem showAbout = new JMenuItem();
        showAbout.setName("showAbout");
        showAbout.setAction(actionMap.get("showAbout"));
        help.add(showAbout);

        /******************************************************/
        menuBar.add(fileMenu, 0);
        menuBar.add(editMenu, 1);
        menuBar.add(karteMenu, 2);
        menuBar.add(insertMenu, 3);
        menuBar.add(textMenu, 4);
        menuBar.add(toolMenu, 5);
        // 6 = Window
        menuBar.add(help, 7);

        /******************************************************/
        resMap.injectComponents(menuBar);
    }

    /**
     *
     * @param item
     * @param key
     */
    private void setAccelerator(JMenuItem item, int key) {
        item.setAccelerator(KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    /**
     *
     * @param item
     * @param key
     * @param shiftMask
     */
    private void setAccelerator(JMenuItem item, int key, boolean shiftMask) {
        item.setAccelerator(
                KeyStroke.getKeyStroke(key, (java.awt.event.InputEvent.SHIFT_MASK | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))));
    }
}
