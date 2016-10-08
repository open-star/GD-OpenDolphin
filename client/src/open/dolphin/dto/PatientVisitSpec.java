package open.dolphin.dto;

import open.dolphin.infomodel.PatientVisitModel;

/**
 * PatientVisitSpec　MEMO:DTO
 * 
 * @author Minagawa, Kazushi
 *
 */
public class PatientVisitSpec extends DolphinDTO {

    private static final long serialVersionUID = 1477781855533185098L;
    private PatientVisitModel patientVisit;
    private String date;
    private int skipCount;
    private String patientId;
    private String appodateFrom;
    private String appodateTo;

    /**
     *
     * @return
     */
    public String getAppodateFrom() {
        return appodateFrom;
    }

    /**
     *
     * @param appodateFrom
     */
    public void setAppodateFrom(String appodateFrom) {
        this.appodateFrom = appodateFrom;
    }

    /**
     *
     * @return　
     */
    public String getAppodateTo() {
        return appodateTo;
    }

    /**
     *
     * @param appodateTo
     */
    public void setAppodateTo(String appodateTo) {
        this.appodateTo = appodateTo;
    }

    /**
     * @param patientVisitValue The patientVisitValue to set.
     */
    public void setPatientVisit(PatientVisitModel patientVisitValue) {
        this.patientVisit = patientVisitValue;
    }

    /**
     * @return Returns the patientVisitValue.
     */
    public PatientVisitModel getPatientVisit() {
        return patientVisit;
    }

    /**
     * @param date The date to set.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return Returns the date.
     */
    public String getDate() {
        return date;
    }

    /**
     * @param skipCount The skipCount to set.
     */
    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    /**
     * @return Returns the skipCount.
     */
    public int getSkipCount() {
        return skipCount;
    }

    /**
     *
     * @param patientId　患者ID
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return　患者ID
     */
    public String getPatientId() {
        return patientId;
    }
}
