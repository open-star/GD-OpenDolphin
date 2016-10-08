/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;
import open.dolphin.client.KartePane;
import open.dolphin.client.settings.IAbstractSettingPanel;
import open.dolphin.plugin.IPlugin.Type;

/**
 * プラグインのラッパー
 * @author
 */
public class PluginWrapper {

    private open.dolphin.plugin.IPlugin plugin;
    private Exception lastException;
    private boolean isDispatched;

    /**
     *
     * @param plugin
     */
    public PluginWrapper(IPlugin plugin) {
        this.plugin = plugin;
        this.lastException = null;
    }

    /**
     * プラグインの型を返す
     * @return プラグインの型
     */
    public open.dolphin.plugin.IPlugin.Type getType() {
        Object[] request = new Object[0];
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.getType, request, response);
        return (Type) response[0];
    }

    /**
     * プラグインの名前を返す
     * @return プラグインの名前
     */
    public String getName() {
        Object[] request = new Object[0];
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.getName, request, response);
        return (String) response[0];
    }

    /**
     * プラグインの名前を返す
     * @return プラグインの名前
     */
    public String getReadableName() {
        Object[] request = new Object[0];
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.getReadableName, request, response);
        return (String) response[0];
    }

    /**
     * プラグインの型がtoolmenuのとき、そのサブメニューを返す
     * @return プラグインの名前
     */
    public String[] getSubMenuText() {
        Object[] request = new Object[0];
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.getSubMenuText, request, response);
        return (String[])response;
    }

    /**
     * プラグインのDataFlavorを返す
     * @return プラグインのDataFlavor
     */
    public DataFlavor getFlavor() {
        Object[] request = new Object[0];
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.getFlavor, request, response);
        return (DataFlavor) response[0];
    }

    /**
     * 設定パネル オブジェクトを返す
     * @return 設定パネル オブジェクト
     */
    public IAbstractSettingPanel configure() {
        Object[] request = new Object[0];
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.configure, request, response);
        return (IAbstractSettingPanel) response[0];
    }

    /**
     * カルテ文書を返す。
     * @param parent カルテ情報
     * @return カルテ文書
     */
    public IChartDocument panel(IChart parent) {
        Object[] request = new Object[1];
        Object[] response = new Object[1];
        request[0] = parent;
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.panel, request, response);
        return (IChartDocument) response[0];
    }

    /**
     * を返す
     * @param send
     * @return　結果
     */
    public Object update(Object send) {
        Object[] request = new Object[1];
        request[0] = send;
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.update, request, response);
        return response[0];
    }

    /**
     * ドロップし、その結果を返す
     * @param pane カルテパネル
     * @param transferable
     * @return 結果
     */
    public boolean drop(KartePane pane, Transferable transferable) {
        Object[] request = new Object[2];
        request[0] = pane;
        request[1] = transferable;
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.drop, request, response);
        return (Boolean) response[0];
    }

    /**
     *　実行し、その結果を返す
     * @param parent カルテ情報
     * @return 実行結果
     */
//    public boolean execute(IChart parent, int i) {
    public boolean execute(IChart parent) {
//        Object[] request = new Object[2];
        Object[] request = new Object[1];
        request[0] = parent;
        //request[1] = i;
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.execute, request, response);
        return (Boolean) response[0];
    }

    /**
     * ファイルに指定した所見を書き出し、その実行結果を返す
     * @param parent カルテ情報
     * @param text 所見
     * @return 実行結果
     */
    public boolean writeShokenFile(IChart parent, String text) {
        Object[] request = new Object[2];
        request[0] = parent;
        request[1] = text;
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.writeShokenFile, request, response);

        return (Boolean) response[0];
    }

    /**
     * し、その結果を返す
     * @param send
     * @return 
     */
    public Object message(Object send) {
        Object[] request = new Object[1];
        request[0] = send;
        Object[] response = new Object[1];
        dispatchHarnes(open.dolphin.plugin.IPlugin.Command.messaging, request, response);
        return response[0];
    }

    /**
     * プラグインのdispatchCommand()を呼び出します。
     * @param command 命令
     * @param request
     * @param response
     */
    public void dispatchHarnes(open.dolphin.plugin.IPlugin.Command command, Object[] request, Object[] response) {
        try {
            isDispatched = plugin.dispatchCommand(command, request, response);
        } catch (Exception e) {
            lastException = e;
        }
    }

    /**
     * し、その例外を返す
     * @return the lastException
     */
    public Exception getLastException() {
        return lastException;
    }

    /**
     * し、その結果を返す
     * @return the isDispatched
     */
    public boolean IsDispatched() {
        return isDispatched;
    }
}
