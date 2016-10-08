package open.dolphin.message;

import java.util.List;

import open.dolphin.client.IChart;
import open.dolphin.utils.GUIDGenerator;
import open.dolphin.project.GlobalVariables;

/**
 * StringBuilder
 *
 * @author Kazushi Minagawa
 *
 */
public class DiseaseHelper {

    private String patientId;
    private String confirmDate;
    private String groupId;
    private String department;
    private String departmentDesc;
    private String creatorName;
    private String creatorId;
    private String creatorLicense;
    private String facilityName;
    private String jmariCode;
    private List diagnosisModuleItems;

    /**
     *
     * @param context
     * @param patientID
     * @param confirmDate
     * @param moduleItems
     */
    public DiseaseHelper(IChart context, String patientID, String confirmDate, List<DiagnosisModuleItem> moduleItems) {
        setPatientId(patientID);
        setConfirmDate(confirmDate);
        setDiagnosisModuleItems(moduleItems);
        setGroupId(GUIDGenerator.generate(this));
        setDepartment(context.getPatientVisit().getDepartmentCode());
        setDepartmentDesc(context.getPatientVisit().getDepartmentName());
        setCreatorName(context.getPatientVisit().getAssignedDoctorName());
        setCreatorId(context.getPatientVisit().getAssignedDoctorId());
        setCreatorLicense(GlobalVariables.getUserModel().getLicenseModel().getLicense());
        setFacilityName(GlobalVariables.getUserModel().getFacility().getFacilityName());
        setJmariCode(context.getPatientVisit().getJmariCode());
    }

    /**
     *
     * @return
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId
     */
    private void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return
     */
    public String getConfirmDate() {
        return confirmDate;
    }

    /**
     *
     * @param confirmDate
     */
    private void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    /**
     *
     * @return
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     *
     * @param groupId
     */
    private void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     *
     * @return
     */
    public String getDepartment() {
        return department;
    }

    /**
     *
     * @param department
     */
    private void setDepartment(String department) {
        this.department = department;
    }

    /**
     *
     * @return
     */
    public String getDepartmentDesc() {
        return departmentDesc;
    }

    /**
     *
     * @param departmentDesc
     */
    private void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    /**
     *
     * @return　
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     *
     * @param creatorName
     */
    private void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     *
     * @return
     */
    public String getCreatorId() {
        return creatorId;
    }

    /**
     *
     * @param creatorId
     */
    private void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     *
     * @return
     */
    public String getCreatorLicense() {
        return creatorLicense;
    }

    /**
     *
     * @param creatorLicense
     */
    private void setCreatorLicense(String creatorLicense) {
        this.creatorLicense = creatorLicense;
    }

    /**
     *
     * @return
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     *
     * @param facilityName
     */
    private void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    /**
     *
     * @return
     */
    public String getJmariCode() {
        return jmariCode;
    }

    /**
     *
     * @param jmariCode
     */
    private void setJmariCode(String jmariCode) {
        this.jmariCode = jmariCode;
    }

    /**
     *
     * @return
     */
    public List getDiagnosisModuleItems() {
        return diagnosisModuleItems;
    }

    /**
     *
     * @param diagnosisModuleItems
     */
    private void setDiagnosisModuleItems(List diagnosisModuleItems) {
        this.diagnosisModuleItems = diagnosisModuleItems;
    }
}
