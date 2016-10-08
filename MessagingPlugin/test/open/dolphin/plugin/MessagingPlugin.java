/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

import java.util.prefs.Preferences;

import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;
import open.dolphin.client.settings.IAbstractSettingPanel;
import open.socket.Message;
import open.socket.data.RequestObject;
import open.socket.data.ResponseObject;

/**
 *
 * @author
 */
public class MessagingPlugin implements open.dolphin.plugin.IPlugin {

    public final static String NODE_NAME = "/open/dolphin/plugin/Messaging";
    private String address;
    private String place;
    private IChartDocument panel = null;

    public MessagingPlugin() {
        Preferences prefs = Preferences.userRoot().node(NODE_NAME);
        address = prefs.get("MessagingAddress", "localhost");
        place = prefs.get("MessagingName", "name");

    }

    private String getName() {
        return MessagingPanel.TITLE;
    }

    private String getReadableName() {
        return "MessagingAddress";
    }

    private IAbstractSettingPanel configure() {
        return new MessagingSettingPanel(this);
    }

    private IChartDocument panel(IChart parent) {
        if (panel == null) {//初回のみインスタンス作る。
            panel = new MessagingPanel(parent);
        }
        return panel;
    }

    private Type getType() {
        return open.dolphin.plugin.IPlugin.Type.panel;
    }

    private Object messaging(Object obj) {
        ((RequestObject) obj).setPlace(place);
        return (ResponseObject) Message.Send(address, 50000, obj);
    }

    private Boolean update(Object object) {
        if (panel != null) {
            return panel.update(object);
        }
        return false;
    }

    @Override
    public boolean dispatchCommand(open.dolphin.plugin.IPlugin.Command command, Object[] request, Object[] response) {
        switch (command) {
            case getType:
                response[0] = getType();
                break;
            case getName:
                response[0] = getName();
                break;
            case getReadableName:
                response[0] = getReadableName();
                break;
            case panel:
                response[0] = panel(((IChart) request[0]));
                break;
            case getFlavor:
                response[0] = null;
                break;
            case drop:
                break;
            case configure:
                response[0] = configure();
                break;
            case messaging:
                response[0] = messaging((request[0]));
                break;
            case update:
                response[0] = update(request[0]);
                break;
            default:
                return false;
        }
        return true;
    }
}
