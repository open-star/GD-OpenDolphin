/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.caremapdocument;

import java.awt.Color;
import open.dolphin.project.GlobalSettings;

/**
 *
 * @author
 */
public class AppointColors {

    /**
     * 
     */
    public static final String[] appointNames = {"再診", "検体検査", "画像診断", "その他"};
    /**
     *
     */
    public static final Color[] appointColors = {
        GlobalSettings.getColors(GlobalSettings.Parts.EXAM_APPO),
        GlobalSettings.getColors(GlobalSettings.Parts.TEST),
        GlobalSettings.getColors(GlobalSettings.Parts.IMAGE),
        new Color(251, 239, 128)};
    /**
     * 
     * @param appoint
     * @return
     */
    public static Color getAppointColor(String appoint) {

        if (appoint == null) {
            return Color.white;
        }

        Color ret = null;
        for (int i = 0; i < appointNames.length; i++) {
            if (appoint.equals(appointNames[i])) {
                ret = appointColors[i];
            }
        }
        return ret;
    }

    /**
     *
     * @param order
     * @return
     */
    public static Color getOrderColor(String order) {
        Color ret = Color.PINK;
        /*for (int i = 0; i < orderCodes.length; i++) {
        if (order.equals(orderCodes[i])) {
        ret = orderColors[i];
        }
        }*/
        return ret;
    }
}
