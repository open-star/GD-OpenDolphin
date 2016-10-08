/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin.allergypanel;

import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;

/**
 * 「アレルギ」パネル プラグイン
 * @author
 * NEW CODE
 */
public class AllergyPanelPlugin implements open.dolphin.plugin.IPlugin {

    /**
     *
     * @return
     */
    private Type getType() {
        return open.dolphin.plugin.IPlugin.Type.panel;
    }

    /**
     *
     * @return アレルギ
     */
    private String getName() {
        return AllergyPanel.TITLE;
    }

    /**
     *
     * @return
     */
    private String getReadableName() {
        return "AllergyPanel";
    }

    /**
     *
     * @param parent
     * @return
     */
    private IChartDocument panel(IChart parent) {
        return new AllergyPanel(parent);
    }

    /**
     *
     * @param object
     * @return
     */
    private Boolean update(Object object) {
        return false;
    }

    /**
     *
     * @param command
     * @param request
     * @param response
     * @return
     */
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
            case update:
                response[0] = update(request[0]);
                break;
            default:
                return false;
        }
        return true;
    }
}
