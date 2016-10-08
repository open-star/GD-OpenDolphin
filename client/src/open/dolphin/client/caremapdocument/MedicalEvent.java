package open.dolphin.client.caremapdocument;

import java.util.GregorianCalendar;
import open.dolphin.infomodel.AppointmentModel;

/**
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class MedicalEvent {

    private int year;
    private int month;
    private int day;
    private int dayOfWeek;
    private String displayDate;
    private boolean today;
    private boolean outOfMonth;
    private String medicalCode;
    private AppointmentModel appo;

    /** Creates a new instance of MedicalEventEntry */
    public MedicalEvent() {
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     * @param dayOfWeek
     */
    public MedicalEvent(int year, int month, int day, int dayOfWeek) {
        this();
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
        setDisplayDate();
    }

    /**
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     *
     * @return
     */
    public int getDay() {
        return day;
    }

    /**
     *
     * @return
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     *
     * @return
     */
    public boolean isOutOfMonth() {
        return outOfMonth;
    }

    /**
     *
     * @param b
     */
    public void setOutOfMonth(boolean b) {
        outOfMonth = b;
    }

    /**
     *
     * @return
     */
    public boolean isToday() {
        return today;
    }

    /**
     *
     * @param b
     */
    public void setToday(boolean b) {
        today = b;
    }

    /**
     *
     * @return
     */
    public String getMedicalCode() {
        return medicalCode;
    }

    /**
     *
     * @param val
     */
    public void setMedicalCode(String val) {
        medicalCode = val;
    }

    /**
     *
     * @param gc
     * @return
     */
    public boolean before(GregorianCalendar gc) {
        GregorianCalendar me = new GregorianCalendar(year, month, day);
        return me.before(gc);
    }

    /**
     *
     * @return
     */
    public AppointmentModel getAppointEntry() {
        return appo;
    }

    /**
     *
     * @param val
     */
    public void setAppointEntry(AppointmentModel val) {
        appo = val;
    }

    /**
     *
     * @return
     */
    public String getAppointmentName() {
        return appo == null ? null : appo.getName();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.valueOf(day);
    }

    /**
     *
     * @return
     */
    public String getDisplayDate() {
        return displayDate;
    }

    /**
     *
     */
    private void setDisplayDate() {
        StringBuffer buf = new StringBuffer();
        String val = String.valueOf(year);
        buf.append(val);
        buf.append("-");
        val = String.valueOf(month + 1);
        if (val.length() == 1) {
            buf.append("0");
        }
        buf.append(val);
        buf.append("-");
        val = String.valueOf(day);
        if (val.length() == 1) {
            buf.append("0");
        }
        buf.append(val);

        displayDate = buf.toString();
    }
}
