/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import open.dolphin.log.LogWriter;

/**
 *
 * @author
 */
public class DateExpire {

    /**
     *
     */
    public static final String ORCA_DATE_FORMAT = "yyyyMMdd";
    /**
     *
     */
    public static final String ORCA_BEGIN_TOKEN = "00000000";
    /**
     *
     */
    public static final String ORCA_FINAL_TOKEN = "99999999";
    /**
     *
     */
    public static final String BEGIN_DATE = "20000101";
    /**
     *
     */
    public static final String FINAL_DATE = "20200101";

    /**
     *　コンストラクタ
     */
    public DateExpire() {
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    static public boolean expire(String startDate, String endDate) {
        return expire(new Date(), startDate, endDate);
    }

    /**
     *　期限切れステータスを返す
     * @param gc カレンダー
     * @param startDate から
     * @param endDate 終了日
     * @return 今日が開始日より前なら0、開始日より後で終了日より前なら1、終了日より後なら2、終了日が無効なら1
     *
     */
    static public int expireState(GregorianCalendar gc, String startDate, String endDate) {
        if (endDate != null) {
            if (!endDate.isEmpty()) {
                SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
                String refDate = f.format(gc.getTime()).toString();
                if (startDate != null && refDate.compareTo(startDate) < 0) {
                    return 0;
                } else if (refDate.compareTo(endDate) > 0) {
                    return 2;
                }
            }
        }
        return 1;
    }

    /**
     *
     * @param now
     * @param startDate
     * @param endDate
     * @return
     */
    static public boolean expire(Date now, String startDate, String endDate) {
        try {
            if ((startDate != null) || (endDate != null)) {
                SimpleDateFormat orcaDateFormat = new SimpleDateFormat(ORCA_DATE_FORMAT);
                java.util.Date start = orcaDateFormat.parse(startDate.equals(ORCA_BEGIN_TOKEN) ? BEGIN_DATE : startDate);
                java.util.Date end = orcaDateFormat.parse(endDate.equals(ORCA_FINAL_TOKEN) ? FINAL_DATE : endDate);
                return expire(now, start, end);
            }
        } catch (ParseException ex) {
            LogWriter.error("DateExpire", "");
        }
        return false;
    }

    /**
     * 日付けチェック
     * @param now 現在
     * @param startDate 開始日
     * @param endDate 終了日
     * @return　現在が開始日と終了日の間なら真
     */
    static public boolean expire(Date now, Date startDate, Date endDate) {

        int s = startDate.compareTo(now);
        int e = endDate.compareTo(now);

        if (s > 0) {
            return true;
        }
        if (e < 0) {
            return true;
        }
        return false;
        //     return !(startDate.compareTo(now) < 0) && (endDate.compareTo(now) > 0);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    static public int compare(String startDate, String endDate) {
        return compare(new Date(), startDate, endDate);
    }

    /**
     *
     * @param now
     * @param startDate
     * @param endDate
     * @return
     */
    static public int compare(Date now, String startDate, String endDate) {
        try {
            if ((startDate != null) && (endDate != null)) {
                SimpleDateFormat orcaDateFormat = new SimpleDateFormat(ORCA_DATE_FORMAT);
                java.util.Date start = orcaDateFormat.parse(startDate.equals(ORCA_BEGIN_TOKEN) ? BEGIN_DATE : startDate);
                java.util.Date end = orcaDateFormat.parse(endDate.equals(ORCA_FINAL_TOKEN) ? FINAL_DATE : endDate);
                return compare(now, start, end);
            }
        } catch (ParseException ex) {
            LogWriter.error("", "");
        }
        return -1;
    }

    /**
     *
     * @param now
     * @param startDate
     * @param endDate
     * @return
     */
    static public int compare(Date now, Date startDate, Date endDate) {
        if (now.compareTo(startDate) < 0) {    // 期限前
            return -1;
        } else if (now.compareTo(endDate) > 0) {// 期限後
            return 1;
        }
        return 0; // 期限内
    }
}
