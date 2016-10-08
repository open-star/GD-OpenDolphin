package open.dolphin.infomodel;

import java.io.Serializable;

/**
 * LaboImportSummary
 * 
 * @author Minagawa,Kazushi
 */
public class LaboImportSummary implements Serializable {

    private static final long serialVersionUID = 8730078673332969884L;
    private String patientId;//患者ID
    private PatientModel patient;
    private String setName;
    private String specimenName;
    private String sampleTime;
    private String reportTime;
    private String reportStatus;
    private String laboratoryCenter;
    private String result;

    /**
     * patientのGetter
     * @return patient
     */
    public PatientModel getPatient() {
        return patient;
    }

    /**
     * patientのSetter
     * @param patient
     */
    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    /**
     * patientBirthdayのGetter
     * @return patientBirthday
     */
    public String getPatientBirthday() {
        return this.getPatient().getBirthday();
    }

    /**
     * patientGenderのGetter
     * @return patientGender
     */
    public String getPatientGender() {
        return this.getPatient().getGenderDesc();
    }

    /**
     * patientNameのGetter
     * @return patientName
     */
    public String getPatientName() {
        return this.getPatient().getFullName();
    }

    /**
     * specimenNameのGetter
     * @return specimenName
     */
    public String getSpecimenName() {
        return specimenName;
    }

    /**
     * specimenNameのSetter
     * @param specimenName
     */
    public void setSpecimenName(String specimenName) {
        this.specimenName = specimenName;
    }

    /**
     * laboratoryCenterのGetter
     * @return laboratoryCenter
     */
    public String getLaboratoryCenter() {
        return laboratoryCenter;
    }

    /**
     * laboratoryCenterのSetter
     * @param laboratoryCenter
     */
    public void setLaboratoryCenter(String laboratoryCenter) {
        this.laboratoryCenter = laboratoryCenter;
    }

    /**
     * 患者IDのGetter
     * @return 患者ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * 患者IDのSetter
     * @param id 患者ID
     */
    public void setPatientId(String id) {
        patientId = id;
    }

    /**
     * reportStatusのGetter
     * @return reportStatus
     */
    public String getReportStatus() {
        return reportStatus;
    }

    /**
     * reportStatusのSetter
     * @param reportStatus
     */
    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    /**
     * reportTimeのGetter
     * @return reportTime
     */
    public String getReportTime() {
        int index = reportTime.indexOf('T');
        return index > 0 ? reportTime.substring(0, index) : reportTime;
    }

    /**
     * reportTimeのSetter
     * @param reportTime
     */
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    /**
     * sampleTimeのGetter
     * @return sampleTime
     */
    public String getSampleTime() {
        int index = sampleTime.indexOf('T');
        return index > 0 ? sampleTime.substring(0, index) : sampleTime;
    }

    /**
     * sampleTimeのSetter
     * @param sampleTime
     */
    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    /**
     * setNameのGetter
     * @return setName
     */
    public String getSetName() {
        return setName;
    }

    /**
     * setNameのSetter
     * @param setName
     */
    public void setSetName(String setName) {
        this.setName = setName;
    }

    /**
     * resultのGetter
     * @return result
     */
    public String getResult() {
        return result;
    }

    /**
     * resultのSetter
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }
}
