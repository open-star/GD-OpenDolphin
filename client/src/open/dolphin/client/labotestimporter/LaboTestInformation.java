package open.dolphin.client.labotestimporter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class LaboTestInformation {

    private String centerCode;
    private String requestKey;
    private String sampleTime;
    private String reportTime;
    private String patientId;
    private String patientIdType;
    private String patientIdTypeTableId;
    private String moduleUUID;
    private String confirmedDate;
    private String registId;
    private String reportStatus;
    private String reportStatusCode;
    private String reportStatusCodeType;
    private String clientFacility;
    private String clientFacilityCode;
    private String clientFacilityCodeId;
    private String laboratoryCenter;
    private String laboratoryCenterCode;
    private String laboratoryCenterCodeId;
    private String specimenName;
    private String specimenCode;
    private String specimenCodeId;
    private String commitionedKey;
    private String patiantName;
    private String status;
    private String milk;
    private String hemolytic;
    private String bilirubin;
    private String registTime;
    private String abnormalValueFlag;
    private List<String> abnormalValues;
    private List<LaboTestResultInformation> laboTestResultInformations;
    private List<AverageInformation> averageInformations;

    /**
     *
     */
    public LaboTestInformation() {
        laboTestResultInformations = new ArrayList<LaboTestResultInformation>();
        abnormalValues = new ArrayList<String>();
        averageInformations = new ArrayList<AverageInformation>();
    }

    /**
     *
     * @return
     */
    public int laboTestResultInformationSize() {
        return this.laboTestResultInformations.size();
    }

    /**
     *
     * @return
     */
    public int averageInformationSize() {
        return this.averageInformations.size();
    }

    /**
     *
     * @param testResultInformation
     */
    public void addTestResultInformation(LaboTestResultInformation testResultInformation) {
        this.laboTestResultInformations.add(testResultInformation);
    }

    /**
     *
     * @param index
     * @return
     */
    public LaboTestResultInformation getLaboTestResultInformation(int index) {
        return this.laboTestResultInformations.get(index);
    }

    /**
     *
     * @param abnormalValue
     */
    public void addAbnormalValue(String abnormalValue) {
        this.abnormalValues.add(abnormalValue);
    }

    /**
     *
     * @param index
     * @return
     */
    public String getAbnormalValue(int index) {
        return this.abnormalValues.get(index);
    }

    /**
     *
     * @param averageInformation
     */
    public void addAverageInformation(AverageInformation averageInformation) {
        this.averageInformations.add(averageInformation);
    }

    /**
     *
     * @param index
     * @return
     */
    public AverageInformation getAverageInformation(int index) {
        return this.averageInformations.get(index);
    }

    /**
     *
     * @return
     */
    public String getConfirmedDate() {
        return confirmedDate;
    }

    /**
     *
     * @param confirmedDate
     */
    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    /**
     *
     * @return
     */
    public String getModuleUUID() {
        return moduleUUID;
    }

    /**
     *
     * @param moduleUUID
     */
    public void setModuleUUID(String moduleUUID) {
        this.moduleUUID = moduleUUID;
    }

    /**
     *
     * @return　患者ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId 患者ID
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return 患者IDタイプ
     */
    public String getPatientIdType() {
        return patientIdType;
    }

    /**
     *
     * @param patientIdType 患者IDタイプ
     */
    public void setPatientIdType(String patientIdType) {
        this.patientIdType = patientIdType;
    }

    /**
     *
     * @return 患者IDタイプテーブルID
     */
    public String getPatientIdTypeTableId() {
        return patientIdTypeTableId;
    }

    /**
     *
     * @param patientIdTypeTableId  患者IDタイプテーブルID
     */
    public void setPatientIdTypeTableId(String patientIdTypeTableId) {
        this.patientIdTypeTableId = patientIdTypeTableId;
    }

    /**
     *
     * @return　設備
     */
    public String getClientFacility() {
        return clientFacility;
    }

    /**
     *
     * @param clientFacility
     */
    public void setClientFacility(String clientFacility) {
        this.clientFacility = clientFacility;
    }

    /**
     *
     * @return
     */
    public String getClientFacilityCode() {
        return clientFacilityCode;
    }

    /**
     *
     * @param clientFacilityCode
     */
    public void setClientFacilityCode(String clientFacilityCode) {
        this.clientFacilityCode = clientFacilityCode;
    }

    /**
     *
     * @return
     */
    public String getClientFacilityCodeId() {
        return clientFacilityCodeId;
    }

    /**
     *
     * @param clientFacilityCodeId
     */
    public void setClientFacilityCodeId(String clientFacilityCodeId) {
        this.clientFacilityCodeId = clientFacilityCodeId;
    }

    /**
     *
     * @return
     */
    public String getRegistId() {
        return registId;
    }

    /**
     *
     * @param registId
     */
    public void setRegistId(String registId) {
        this.registId = registId;
    }

    /**
     *
     * @return　レポートステータス
     */
    public String getReportStatus() {
        return reportStatus;
    }

    /**
     *　レポートステータス
     * @param reportStatus　レポートステータス
     */
    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    /**
     *
     * @return
     */
    public String getReportStatusCode() {
        return reportStatusCode;
    }

    /**
     *
     * @param reportStatusCode
     */
    public void setReportStatusCode(String reportStatusCode) {
        this.reportStatusCode = reportStatusCode;
    }

    /**
     *
     * @return
     */
    public String getReportStatusCodeType() {
        return reportStatusCodeType;
    }

    /**
     *
     * @param reportStatusCodeType
     */
    public void setReportStatusCodeType(String reportStatusCodeType) {
        this.reportStatusCodeType = reportStatusCodeType;
    }

    /**
     *
     * @return　
     */
    public String getLaboratoryCenter() {
        return laboratoryCenter;
    }

    /**
     *
     * @param laboratoryCenter
     */
    public void setLaboratoryCenter(String laboratoryCenter) {
        this.laboratoryCenter = laboratoryCenter;
    }

    /**
     *
     * @return
     */
    public String getLaboratoryCenterCode() {
        return laboratoryCenterCode;
    }

    /**
     *
     * @param laboratoryCenterCode
     */
    public void setLaboratoryCenterCode(String laboratoryCenterCode) {
        this.laboratoryCenterCode = laboratoryCenterCode;
    }

    /**
     *
     * @return
     */
    public String getLaboratoryCenterCodeId() {
        return laboratoryCenterCodeId;
    }

    /**
     *
     * @param laboratoryCenterCodeId
     */
    public void setLaboratoryCenterCodeId(String laboratoryCenterCodeId) {
        this.laboratoryCenterCodeId = laboratoryCenterCodeId;
    }

    /**
     *
     * @return
     */
    public String getReportTime() {
        return reportTime;
    }

    /**
     *
     * @param reportTime
     */
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime.trim();
    }

    /**
     *
     * @return
     */
    public String getSampleTime() {
        return sampleTime;
    }

    /**
     *
     * @param sampleTime
     */
    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime.trim();
    }

    /**
     *
     * @return
     */
    public String getMilk() {
        return milk;
    }

    /**
     *
     * @param milk
     */
    public void setMilk(String milk) {
        this.milk = milk.trim();
    }

    /**
     *
     * @return
     */
    public String getAbnormalValueFlag() {
        return abnormalValueFlag;
    }

    /**
     *
     * @param abnormalValueFlag
     */
    public void setAbnormalValueFlag(String abnormalValueFlag) {
        this.abnormalValueFlag = abnormalValueFlag.trim();
    }

    /**
     *
     * @return
     */
    public String getRegistTime() {
        return registTime;
    }

    /**
     *
     * @param registTime
     */
    public void setRegistTime(String registTime) {
        this.registTime = registTime.trim();
    }

    /**
     *
     * @return
     */
    public String getBilirubin() {
        return bilirubin;
    }

    /**
     *
     * @param bilirubin
     */
    public void setBilirubin(String bilirubin) {
        this.bilirubin = bilirubin.trim();
    }

    /**
     *
     * @return
     */
    public String getCenterCode() {
        return centerCode;
    }

    /**
     *
     * @param centerCode
     */
    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode.trim();
    }

    /**
     *
     * @return
     */
    public String getCommitionedKey() {
        return commitionedKey;
    }

    /**
     *
     * @param commitionedKey
     */
    public void setCommitionedKey(String commitionedKey) {
        this.commitionedKey = commitionedKey.trim();
    }

    /**
     *
     * @return
     */
    public String getHemolytic() {
        return hemolytic;
    }

    /**
     *
     * @param hemolytic
     */
    public void setHemolytic(String hemolytic) {
        this.hemolytic = hemolytic.trim();
    }

    /**
     *
     * @return　患者名
     */
    public String getPatiantName() {
        return patiantName;
    }

    /**
     *　患者名
     * @param patiantName　患者名
     */
    public void setPatiantName(String patiantName) {
        this.patiantName = patiantName.trim();
    }

    /**
     *
     * @return　登録キー
     */
    public String getRequestKey() {
        return requestKey;
    }

    /**
     *　登録キー
     * @param requestKey　登録キー
     */
    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey.trim();
    }

    /**
     *
     * @return　ステータス
     */
    public String getStatus() {
        return status;
    }

    /**
     *　ステータス
     * @param status　ステータス
     */
    public void setStatus(String status) {
        this.status = status.trim();
    }

    /**
     *
     * @return　検体コード
     */
    public String getSpecimenCode() {
        return specimenCode;
    }

    /**
     *　検体コード
     * @param specimenCode　検体コード
     */
    public void setSpecimenCode(String specimenCode) {
        this.specimenCode = specimenCode;
    }

    /**
     *
     * @return　検体コードID
     */
    public String getSpecimenCodeId() {
        return specimenCodeId;
    }

    /**
     *　検体コードID
     * @param specimenCodeId　検体コードID
     */
    public void setSpecimenCodeId(String specimenCodeId) {
        this.specimenCodeId = specimenCodeId;
    }

    /**
     *
     * @return　検体名
     */
    public String getSpecimenName() {
        return specimenName;
    }

    /**
     *　検体名
     * @param specimenName　検体名
     */
    public void setSpecimenName(String specimenName) {
        this.specimenName = specimenName;
    }
}
