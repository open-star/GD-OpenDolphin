/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.queries;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author
 */
public class DolphinQuery {

    private String who;//誰の
    private Map<String, String> what;//何を
    private Date when;//いつの
    private String where;//どこにある
    private String why;//なぜ
    private String how;//どのように

    /**
     *
     */
    public DolphinQuery() {
        what = new LinkedHashMap<String, String>();
    }

    /**
     *
     * @return
     */
    public String who() {
        return who;
    }

    /**
     *
     * @param keyword
     * @return
     */
    public String what(String keyword) {
        String result = what.get(keyword);
        if (result == null) {
            return "";
        }
        return result;
    }

    /**
     *
     * @return
     */
    public Date when() {
        return when;
    }

    /**
     *
     * @return
     */
    public String where() {
        return where;
    }

    /**
     *
     * @return
     */
    public String why() {
        return why;
    }

    /**
     *
     * @return
     */
    public String how() {
        return how;
    }

    /**
     *
     * @param keyword
     * @param value
     */
    public void addWhat(String keyword, String value) {
        this.what.put(keyword, value);
    }
    //  @Override
    //  public boolean isEmpty() {
    //      return what.isEmpty();// && super.isEmpty();
    //  }
}
