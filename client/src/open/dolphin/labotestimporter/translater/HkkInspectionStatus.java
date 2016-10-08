/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.labotestimporter.translater;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author
 */
public class HkkInspectionStatus {

    /**
     *
     */
    public static String END = "最終報告";
    /**
     * 
     */
    public static String MIDDLE = "中間報告";
    /**
     * 
     */
    private static final Map<String, String> table = new HashMap<String, String>();

    static {
        table.put("E", END);
        table.put("M", MIDDLE);
    }

    /**
     *
     * @param code
     * @return
     */
    public static String toName(String code) {
        String result = table.get(code);
        if (result == null) {
            return code;
        }
        return result;
    }
}
