package open.dolphin.client;

import java.util.*;

/**
 * Period
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class Period extends EventObject {

    private static final long serialVersionUID = -8572561462807732975L;
    private String startDate;
    private String endDate;

    /** 
     * Creates a new instance of Period
     * @param source 
     */
    public Period(Object source) {
        super(source);
    }

    /**
     *
     * @return
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     *
     * @param val
     */
    public void setStartDate(String val) {
        startDate = val;
    }

    /**
     *
     * @return
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     *
     * @param val
     */
    public void setEndDate(String val) {
        endDate = val;
    }
}
