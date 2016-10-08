package open.dolphin.infomodel;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 * 紹介状フォーム。
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "docType",
discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("TOUTOU")
public class TouTouLetter extends LetterModel {

    private String consultantHospital;//対象医療機関
    private String consultantDept;//対象部署
    private String consultantDoctor;//対象医師
    private String clientHospital;//医療機関
    @Transient
    private String clientName;//名前
    @Transient
    private String clientAddress;//住所
    @Transient
    private String clientTelephone;//電話番号
    @Transient
    private String clientFax;//FAX
    private String patientName;//患者名
    private String patientGender;//患者性別
    private String patientBirthday;//患者誕生日
    @Transient
    private String patientAge;//患者年齢
    private String disease;
    private String purpose;
    @Transient
    private String pastFamily;
    @Transient
    private String clinicalCourse;
    @Transient
    private String medication;
    @Transient
    private String remarks;

    /**
     * コンストラクタ
     * Creates a new instance of TouTouLetter
     */
    public TouTouLetter() {
    }

    /**
     * 対象医療機関のGetter
     * @return
     */
    public String getConsultantHospital() {
        return consultantHospital;
    }

    /**
     * 対象医療機関のSetter
     * @param consultantHospital
     */
    public void setConsultantHospital(String consultantHospital) {
        this.consultantHospital = consultantHospital;
    }

    /**
     * 対象部署のGetter
     * @return 対象部署
     */
    public String getConsultantDept() {
        return consultantDept;
    }

    /**
     * 対象部署のSetter
     * @param consultantDept 対象部署
     */
    public void setConsultantDept(String consultantDept) {
        this.consultantDept = consultantDept;
    }

    /**
     * 対象医師のGetter
     * @return 対象医師
     */
    public String getConsultantDoctor() {
        return consultantDoctor;
    }

    /**
     * 対象医師のSetter
     * @param consultantDoctor 対象医師
     */
    public void setConsultantDoctor(String consultantDoctor) {
        this.consultantDoctor = consultantDoctor;
    }

    /**
     * 医療機関のGetter
     * @return 医療機関
     */
    public String getClientHospital() {
        return clientHospital;
    }

    /**
     * 医療機関のSetter
     * @param clientHospital 医療機関
     */
    public void setClientHospital(String clientHospital) {
        this.clientHospital = clientHospital;
    }

    /**
     * 名前のGetter
     * @return 名前
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 名前のSetter
     * @param clientName 名前
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * 住所のGetter
     * @return 住所
     */
    public String getClientAddress() {
        return clientAddress;
    }

    /**
     * 住所のSetter
     * @param clientAddress 住所
     */
    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    /**
     * 電話番号のGetter
     * @return 電話番号
     */
    public String getClientTelephone() {
        return clientTelephone;
    }

    /**
     * 電話番号のSetter
     * @param clientTelephone 電話番号
     */
    public void setClientTelephone(String clientTelephone) {
        this.clientTelephone = clientTelephone;
    }

    /**
     * FAXのGetter
     * @return FAX
     */
    public String getClientFax() {
        return clientFax;
    }

    /**
     * FAXのSetter
     * @param clientFax FAX
     */
    public void setClientFax(String clientFax) {
        this.clientFax = clientFax;
    }

    /**
     * 患者名のGetter
     * @return 患者名
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * 患者名のSetter
     * @param patientName 患者名
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * 患者性別のGetter
     * @return 患者性別
     */
    public String getPatientGender() {
        return patientGender;
    }

    /**
     * 患者性別のSetter
     * @param patientGender 患者性別
     */
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    /**
     * 患者誕生日のGetter
     * @return 患者誕生日
     */
    public String getPatientBirthday() {
        return patientBirthday;
    }

    /**
     * 患者誕生日のSetter
     * @param patientBirthday 患者誕生日
     */
    public void setPatientBirthday(String patientBirthday) {
        this.patientBirthday = patientBirthday;
    }

    /**
     * 患者年齢のGetter
     * @return 患者年齢
     */
    public String getPatientAge() {
        return patientAge;
    }

    /**
     * 患者年齢のSetter
     * @param patientAge 患者年齢
     */
    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    /**
     * diseaseのGetter
     * @return disease
     */
    public String getDisease() {
        return disease;
    }

    /**
     * diseaseのSetter
     * @param disease
     */
    public void setDisease(String disease) {
        this.disease = disease;
    }

    /**
     * purposeのGetter
     * @return purpose
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * purposeのSetter
     * @param purpose
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * pastFamilyのGetter
     * @return pastFamily
     */
    public String getPastFamily() {
        return pastFamily;
    }

    /**
     * pastFamilyのSetter
     * @param pastFamily
     */
    public void setPastFamily(String pastFamily) {
        this.pastFamily = pastFamily;
    }

    /**
     * clinicalCourseのGetter
     * @return clinicalCourse
     */
    public String getClinicalCourse() {
        return clinicalCourse;
    }

    /**
     * clinicalCourseのSetter
     * @param clinicalCourse
     */
    public void setClinicalCourse(String clinicalCourse) {
        this.clinicalCourse = clinicalCourse;
    }

    /**
     * medicationのGetter
     * @return medication
     */
    public String getMedication() {
        return medication;
    }

    /**
     * medicationのSetter
     * @param medication
     */
    public void setMedication(String medication) {
        this.medication = medication;
    }

    /**
     * remarksのGetter
     * @return remarks
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * remarksのSetter
     * @param remarks
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
