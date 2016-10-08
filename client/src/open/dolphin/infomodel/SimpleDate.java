/*
 * Created on 2005/02/23
 *
 */
package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import open.dolphin.queries.DolphinQuery;

/**
 * 日付基本 MEMO:マッピング
 * SimpleDate
 * @author Kazushi Minagawa
 */
public class SimpleDate extends InfoModel implements Comparable {//id

    private static final long serialVersionUID = 2137848922359964710L;
    private int year;
    private int month;
    private int day;
    private List timeFrames;
    private String eventCode;

    /**
     * コンストラクタ
     */
    public SimpleDate() {
    }

    /**
     * 検索 MEMO:何もしない
     * @param query
     * @return false
     */
    @Override
    public boolean search(DolphinQuery query) {
        return false;
    }

    /**
     * 今日
     * @return 今日
     */
    public static SimpleDate today() {
        return new SimpleDate(new GregorianCalendar());
    }

    /**
     *
     * @param mmlDate MMLで使用する日付
     * @return  日付
     */
    public static SimpleDate mmlDateToSimpleDate(String mmlDate) {
        // mmlDate = YYYY-MM-DDThh:mm:ss
        int year = Integer.parseInt(mmlDate.substring(0, 4));
        int month = Integer.parseInt(mmlDate.substring(5, 7)) - 1; // for
        // Calendar
        int date = Integer.parseInt(mmlDate.substring(8, 10));
        return new SimpleDate(year, month, date);
    }

    /**
     * MMLで使用する日付
     * @param sd 日付
     * @return MMLで使用する日付
     */
    public static String simpleDateToMmldate(SimpleDate sd) {

        StringBuffer buf = new StringBuffer();
        buf.append(sd.getYear());
        buf.append("-");
        int month = sd.getMonth() + 1;
        if (month < 10) {
            buf.append("0");
        }
        buf.append(month);
        buf.append("-");
        int day = sd.getDay();
        if (day < 10) {
            buf.append("0");
        }
        buf.append(day);
        return buf.toString();
    }

    /**
     * 日付コンストラクタ
     * @param year 年
     * @param month 月
     * @param day 日
     */
    public SimpleDate(int year, int month, int day) {
        this();
        setYear(year);
        setMonth(month);
        setDay(day);
    }

    /**
     * 日付コンストラクタ
     * @param spec spec[0] 年　spec[0] 月 spec[0] 日
     */
    public SimpleDate(int[] spec) {
        this();
        setYear(spec[0]);
        setMonth(spec[1]);
        setDay(spec[2]);
    }

    /**
     * コンストラクタ
     * @param gc カレンダー
     */
    public SimpleDate(GregorianCalendar gc) {
        this();
        setYear(gc.get(Calendar.YEAR));
        setMonth(gc.get(Calendar.MONTH));
        setDay(gc.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 年のSetter
     * @param year 年
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * 年のGetter
     * @return 年
     */
    public int getYear() {
        return year;
    }

    /**
     * 月のSetter
     * @param month 月
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * 月のGetter
     * @return 月
     */
    public int getMonth() {
        return month;
    }

    /**
     * 日のSetter
     * @param day 日
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * 日のGetter
     * @return 日
     */
    public int getDay() {
        return day;
    }

    /**
     * 日付が同一か
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 同一なら真
     */
    public boolean equalDate(int year, int month, int day) {
        return (year == this.year && month == this.month && day == this.day) ? true
                : false;
    }

    /**
     * インスタンスを文字列にシリアライズ
     * @return 文字列
     */
    @Override
    public String toString() {
        return String.valueOf(day);
        /*
         * StringBuffer buf = new StringBuffer(); buf.append(year);
         * buf.append("/"); buf.append(month+1); buf.append("/");
         * buf.append(day); return buf.toString();
         */
    }

    /**
     * イベントコード
     * @param c イベントコード
     */
    public void setEventCode(String c) {
        this.eventCode = c;
    }

    /**
     * イベントコード
     * @return イベントコード
     */
    public String getEventCode() {
        return eventCode;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {

        if (o != null && o.getClass() == this.getClass()) {
            SimpleDate other = (SimpleDate) o;
            int oYear = other.getYear();
            int oMonth = other.getMonth();
            int oDay = other.getDay();

            if (year != oYear) {
                return year < oYear ? -1 : 1;

            } else if (month != oMonth) {
                return month < oMonth ? -1 : 1;

            } else if (day != oDay) {
                return day < oDay ? -1 : 1;

            } else {
                return 0;
            }
        }
        return -1;
    }

    /**
     * 日月比較
     * @param o SimpleDate
     * @return 日月が同じなら真
     */
    public int compareMonthDayTo(Object o) {

        if (o != null && o.getClass() == this.getClass()) {
            SimpleDate other = (SimpleDate) o;
            int oMonth = other.getMonth();
            int oDay = other.getDay();

            if (month != oMonth) {
                return month < oMonth ? -1 : 1;

            } else if (day != oDay) {
                return day < oDay ? -1 : 1;

            } else {
                return 0;
            }
        }
        return -1;
    }

    /**
     * タイムフレームのSetter
     * @param timeFrames タイムフレーム
     */
    public void setTimeFrames(List timeFrames) {
        this.timeFrames = timeFrames;
    }

    /**
     * タイムフレームのGetter
     * @return タイムフレーム
     */
    public List getTimeFrames() {
        return timeFrames;
    }

    /**
     * MEMO:何もしない
     * @param result ライター
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        //TODO serialize
    }

    /**
     * MEMO:何もしない
     * @param result ライター
     * @throws IOException
     */
    public void deserialize(Writer result) throws IOException {
        //TODO deserialize
    }
}
