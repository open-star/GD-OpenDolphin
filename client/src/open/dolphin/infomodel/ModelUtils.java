package open.dolphin.infomodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import open.dolphin.log.LogWriter;
import open.dolphin.utils.AgeCalculator;

/**
 * InfoModel
 *
 * @author Minagawa,Kazushi
 */
public class ModelUtils implements IInfoModel {

    /**
     * 
     * @param mmlDate
     * @return
     */
    public static String trimTime(String mmlDate) {

        if (mmlDate != null) {
            int index = mmlDate.indexOf('T');
            if (index > -1) {
                return mmlDate.substring(0, index);
            } else {
                return mmlDate;
            }
        }
        return null;
    }

    /**
     *
     * @param mmlDate
     * @return
     */
    public static String trimDate(String mmlDate) {

        if (mmlDate != null) {
            int index = mmlDate.indexOf('T');
            if (index > -1) {
                // THH:mm:ss -> HH:mm
                return mmlDate.substring(index + 1, index + 6);
            } else {
                return mmlDate;
            }
        }
        return null;
    }

    /**
     * 年齢＋（誕生日）を返す
     * @param mmlBirthday 誕生日（YYYY-MM-DD）
     * @return　年齢＋（誕生日）
     */
    public static String getAgeBirthday(String mmlBirthday) {
        String age = AgeCalculator.getAge(mmlBirthday);
        if (age != null) {
            StringBuffer buf = new StringBuffer();
            buf.append(age);
         //   buf.append(" ");
         //   buf.append(AGE);
            buf.append(" (");
            buf.append(mmlBirthday);
            buf.append(")");
            return buf.toString();
        }
        return null;
    }

    /**
     * ageのGetter
     * @param mmlBirthday
     * @return
     */
    /*
    public static String getAge(String mmlBirthday) {

        try {
            GregorianCalendar gc1 = getCalendar(mmlBirthday);
            GregorianCalendar gc2 = new GregorianCalendar(); // Today
            int years = 0;

            gc1.clear(Calendar.MILLISECOND);
            gc1.clear(Calendar.SECOND);
            gc1.clear(Calendar.MINUTE);
            gc1.clear(Calendar.HOUR_OF_DAY);

            gc2.clear(Calendar.MILLISECOND);
            gc2.clear(Calendar.SECOND);
            gc2.clear(Calendar.MINUTE);
            gc2.clear(Calendar.HOUR_OF_DAY);

            while (gc1.before(gc2)) {
                gc1.add(Calendar.YEAR, 1);
                years++;
            }
            years--;

            int month = 12;

            while (gc1.after(gc2)) {
                gc1.add(Calendar.MONTH, -1);
                month--;
            }

            StringBuffer buf = new StringBuffer();
            buf.append(years);
            //小児のみ月を表示（小児は6歳まで）
            if (years <= 6) {
                if (month != 0) {
                    buf.append(".");
                    buf.append(month);
                }
            }
            return buf.toString();

        } catch (Exception e) {
            return null;
        }
    }
*/
    /**
     * calendarのGetter
     * @param mmlDate
     * @return
     */
    public static GregorianCalendar getCalendar(String mmlDate) {

        try {
            // Trim time if contains
            mmlDate = trimTime(mmlDate);
            StringTokenizer st = new StringTokenizer(mmlDate, "-");
            String yearSt = st.nextToken();
            String monthSt = st.nextToken();
            if (monthSt.startsWith("0")) {
                monthSt = monthSt.substring(1);
            }
            String daySt = st.nextToken();
            if (daySt.startsWith("0")) {
                daySt = daySt.substring(1);
            }
            int year = Integer.parseInt(yearSt);
            int month = Integer.parseInt(monthSt);
            month--;
            int day = Integer.parseInt(daySt);

            return new GregorianCalendar(year, month, day);

        } catch (Exception e) {
            LogWriter.error(ModelUtils.class, e);
        }
        return null;
    }

    /**
     * dateAsObjectのGetter
     * @param mmlDate
     * @return
     */
    public static Date getDateAsObject(String mmlDate) {
        if (mmlDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_WITHOUT_TIME);
                return sdf.parse(mmlDate);
            } catch (Exception e) {
                LogWriter.error(ModelUtils.class, e);
            }
        }
        return null;
    }

    /**
     * dateTimeAsObjectのGetter
     * @param mmlDate
     * @return
     */
    public static Date getDateTimeAsObject(String mmlDate) {
        if (mmlDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
                return sdf.parse(mmlDate);
            } catch (Exception e) {
                LogWriter.error(ModelUtils.class, e);
            }
        }
        return null;
    }

    /**
     * dateAsStringのGetter
     * @param date
     * @return
     */
    public static String getDateAsString(Date date) {
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_WITHOUT_TIME);
                return sdf.format(date);
            } catch (Exception e) {
                LogWriter.error(ModelUtils.class, e);
            }
        }
        return null;
    }

    /**
     * dateTimeAsStringのGetter
     * @param date
     * @return
     */
    public static String getDateTimeAsString(Date date) {
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
                return sdf.format(date);
            } catch (Exception e) {
                LogWriter.error(ModelUtils.class, e);
            }
        }
        return null;
    }

    /**
     * dateAsFormatStringのGetter
     * @param date
     * @param format
     * @return
     */
    public static String getDateAsFormatString(Date date, String format) {
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format(date);
            } catch (Exception e) {
                LogWriter.error(ModelUtils.class, e);
            }
        }
        return null;
    }

    /**
     * dateFromStringのGetter
     * @param source
     * @param format
     * @return
     */
    public static Date getDateFromString(String source, String format) {
        if (source == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(source);
        } catch (ParseException ex) {
            LogWriter.error(ModelUtils.class, ex);
        }
        return null;
    }

    /**
     *
     * @param formatFrom
     * @param formatTo
     * @param dateString
     * @return
     */
    public static String Convert(String formatFrom, String formatTo, String dateString) {
        SimpleDateFormat from = new SimpleDateFormat(formatFrom);
        SimpleDateFormat to = new SimpleDateFormat(formatTo);
        try {
            return to.format(from.parse((String) dateString));
        } catch (ParseException ex) {
              LogWriter.error("ModelUtils", "Convert");
        }
        return "";
    }

    /**
     * 性別説明のGetter
     * @param gender 性別 MALE = "male" FEMALE = "female"
     * @return 性別説明 MALE_DISP = "男" FEMALE_DISP = "女" UNKNOWN = "不明"
     */
    public static String getGenderDesc(String gender) {

        if (gender != null) {
            String test = gender.toLowerCase();
            if (test.equals(MALE)) {
                return MALE_DISP;
            } else if (test.equals(FEMALE)) {
                return FEMALE_DISP;
            } else {
                return UNKNOWN;
            }
        }
        return UNKNOWN;
    }

    /**
     * memoのGetter
     * @param memo
     * @return
     */
    public static String getMemo(String memo) {

        if (memo == null) {
            return "";
        } else {
            return memo;
        }
    }

    /**
     *
     * @return
     */
    public boolean isValidModel() {
        return true;
    }

    /**
     *
     * @param diagnosis
     * @return
     */
    public static String[] splitDiagnosis(String diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        String[] ret = null;
        try {
            ret = diagnosis.split("\\s*,\\s*");
        } catch (Exception e) {
              LogWriter.error("ModelUtils", "splitDiagnosis");
        }
        return ret;
    }

    /**
     * diagnosisNameのGetter
     * @param hasAlias
     * @return
     */
    public static String getDiagnosisName(String hasAlias) {
        String[] splits = splitDiagnosis(hasAlias);
        return (splits != null && splits.length == 2 && splits[0] != null) ? splits[0] : hasAlias;
    }

    /**
     * diagnosisAliasのGetter
     * @param hasAlias
     * @return
     */
    public static String getDiagnosisAlias(String hasAlias) {
        String[] splits = splitDiagnosis(hasAlias);
        return (splits != null && splits.length == 2 && splits[1] != null) ? splits[1] : null;
    }
}
