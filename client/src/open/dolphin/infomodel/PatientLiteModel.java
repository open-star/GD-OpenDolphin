package open.dolphin.infomodel;

import open.dolphin.queries.DolphinQuery;

/**
 * PatientLiteModel MEMO:マッピング
 * @author Minagawa, kazushi
 */
public class PatientLiteModel extends InfoModel implements IPatientModel {//id

    private static final long serialVersionUID = 2257606235838636648L;
    private String patientId;//患者ID
    private String name;
    private String gender;//性別
    private String genderDesc;//性別説明
    private String genderCodeSys;//性別説明体系
    private String birthday;//生年月日 yyyy-MM-dd

    /**
     * 簡易患者情報オブジェクトを生成する。
     */
    public PatientLiteModel() {
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
     * 患者IDを設定する。
     * @param patientId 患者ID
     */
    @Override
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * 患者IDを返す。
     * @return 患者ID
     */
    @Override
    public String getPatientId() {
        return patientId;
    }

    /**
     * フルネームを設定する。
     * @param name フルネーム
     */
    @Override
    public void setFullName(String name) {
        this.name = name;
    }

    /**
     * フルネームを返す。
     * @return フルネーム
     */
    @Override
    public String getFullName() {
        return name;
    }

    /**
     * 性別を設定する。
     * @param gender 性別 MALE = "male" FEMALE = "female"
     */
    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 性別を返す。
     * @return 性別 MALE = "male" FEMALE = "female"
     */
    @Override
    public String getGender() {
        return gender;
    }

    /**
     * 性別説明を設定する。
     * @param genderDesc 性別説明
     */
    @Override
    public void setGenderDesc(String genderDesc) {
        this.genderDesc = genderDesc;
    }

    /**
     * 性別説明を返す。
     * @return 性別説明
     */
    @Override
    public String getGenderDesc() {
        return genderDesc;
    }

    /**
     * 性別説明体系を設定する。
     * @param genderCodeSys 性別説明体系
     */
    @Override
    public void setGenderCodeSys(String genderCodeSys) {
        this.genderCodeSys = genderCodeSys;
    }

    /**
     * 性別説明体系を返す。
     * @return 性別説明体系
     */
    @Override
    public String getGenderCodeSys() {
        return genderCodeSys;
    }

    /**
     * 生年月日を設定する。
     * @param birthday 生年月日 yyyy-MM-dd
     */
    @Override
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 生年月日を返す。
     * @return 生年月日 yyyy-MM-dd
     */
    @Override
    public String getBirthday() {
        return birthday;
    }
}
