package open.dolphin.infomodel;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * 紹介状返信フォーム。 MEMO:マッピング
 *
 */
@Entity
@DiscriminatorValue("TOUTOU_REPLY")
public class TouTouReply extends LetterModel {

    private String clientHospital;//医療機関
    private String clientDept;//部署
    private String clientDoctor;//医師
    private String consultantHospital;//対象医療機関
    private String consultantDept;//対象部署
    private String consultantDoctor;//対象医師
    private String patientName;//患者名
    private String patientGender;//患者性別
    private String patientBirthday;//患者誕生日
    @Transient
    private String patientAge;//年齢
    private String visited;//訪問
    @Transient
    private String informedContent;//

    /**
     * コンストラクタ
     * Creates a new instance of TouTouLetter
     */
    public TouTouReply() {
    }

    /**
     * 医療機関のGetter
     * @return
     */
    public String getClientHospital() {
        return clientHospital;
    }

    /**
     * 医療機関のSetter
     * @param clientHospital
     */
    public void setClientHospital(String clientHospital) {
        this.clientHospital = clientHospital;
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
     * 年齢のGetter
     * @return 年齢
     */
    public String getPatientAge() {
        return patientAge;
    }

    /**
     * 年齢のSetter
     * @param patientAge 年齢
     */
    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    /**
     * 部署のGetter
     * @return 部署
     */
    public String getClientDept() {
        return clientDept;
    }

    /**
     * 部署のSetter
     * @param clientDept 部署
     */
    public void setClientDept(String clientDept) {
        this.clientDept = clientDept;
    }

    /**
     * 医師のGetter
     * @return 医師
     */
    public String getClientDoctor() {
        return clientDoctor;
    }

    /**
     * 医師のSetter
     * @param clientDoctor 医師
     */
    public void setClientDoctor(String clientDoctor) {
        this.clientDoctor = clientDoctor;
    }

    /**
     * informedContentのGetter
     * @return informedContent
     */
    public String getInformedContent() {
        return informedContent;
    }

    /**
     * informedContentのSetter
     * @param informedContent
     */
    public void setInformedContent(String informedContent) {
        this.informedContent = informedContent;
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
     * @param consultantHospital 対象医療機関
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
     * visitedのGetter
     * @return visited
     */
    public String getVisited() {
        return visited;
    }

    /**
     * visitedのSetter
     * @param visited
     */
    public void setVisited(String visited) {
        this.visited = visited;
    }
}
