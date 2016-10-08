package open.dolphin.infomodel;

import open.dolphin.utils.CombinedStringParser;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import open.dolphin.queries.DolphinQuery;

/**
 * カルテ基本情報 MEMO:マッピング
 * Serializable
 * DocInfoModel
 *　
 * @author Minagawa,kazushi
 *
 */
@Embeddable
public class DocInfoModel extends InfoModel implements Comparable {

    private static final long serialVersionUID = 5082783120126942636L;
    @Transient
    private long docPk;
    @Transient
    private long parentPk;
    @Column(nullable = false, length = 32)
    private String docId;
    @Column(nullable = false)
    private String docType;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String purpose;
    @Transient
    private String purposeDesc;
    @Transient
    private String purposeCodeSys;
    @Transient
    private Date firstConfirmDate;
    @Transient
    private Date confirmDate;
    private String department;
    private String departmentDesc;
    @Transient
    private String departmentCodeSys;
    private String healthInsurance;
    private String healthInsuranceDesc;
    @Transient
    private String healthInsuranceCodeSys;
    private String healthInsuranceGUID;
    private boolean hasMark;
    private boolean hasImage;
    private boolean hasRp;
    private boolean hasTreatment;
    private boolean hasLaboTest;
    private String versionNumber;
    @Transient
    private String versionNotes;
    private String parentId;
    private String parentIdRelation;
    @Transient
    private String parentIdDesc;
    @Transient
    private String parentIdCodeSys;
    @Transient
    private Collection<AccessRightModel> accessRights;
    @Transient
    private String status;

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (title != null) {
            return (title.indexOf(query.what("keyword")) != -1);
        }
        return false;
    }

    /**
     * Document の Database Primary Key を返す。
     * @return Primary Key
     */
    public long getDocPk() {
        return docPk;
    }

    /**
     * Document の Database Primary Key を設定する。
     * @param docPk Database Primary Key
     */
    public void setDocPk(long docPk) {
        this.docPk = docPk;
    }

    /**
     * 親分文書のPrimaryKeyを返す。
     * @return 親分文書のPrimaryKey
     */
    public long getParentPk() {
        return parentPk;
    }

    /**
     * 親分文書のPrimaryKeyを設定する。
     * @param parentPk 親分文書のPrimaryKey
     */
    public void setParentPk(long parentPk) {
        this.parentPk = parentPk;
    }

    /**
     * 文書IDを設定する。
     *
     * @param docId 文書ID
     *            
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * 文書IDを返す。
     *
     * @return 文書ID
     */
    public String getDocId() {
        return docId;
    }

    /**
     * 文書タイプを設定する。
     *
     * @param docType 文書タイプ
     *            
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * 文書タイプを返す。
     *
     * @return 文書タイプ.
     */
    public String getDocType() {
        return docType;
    }

    /**
     * タイトルを設定する。
     *
     * @param title タイトル
     *            
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * タイトルを返す。
     *
     * @return タイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * 生成目的を設定する。
     *
     * @param purpose 生成目的
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     * 生成目的を返す。
     *
     * @return 生成目的
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * 生成目的説明を設定する。
     *
     * @param purposeDesc 生成目的説明
     *            
     */
    public void setPurposeDesc(String purposeDesc) {
        this.purposeDesc = purposeDesc;
    }

    /**
     * 生成目的説明を返す。
     *
     * @return 生成目的説明
     */
    public String getPurposeDesc() {
        return purposeDesc;
    }

    /**
     * 生成目的コード体系を設定する。
     *
     * @param purposeCodeSys 生成目的コード体系
     *            
     */
    public void setPurposeCodeSys(String purposeCodeSys) {
        this.purposeCodeSys = purposeCodeSys;
    }

    /**
     * 生成目的コード体系を返す。
     *
     * @return 生成目的コード体系
     */
    public String getPurposeCodeSys() {
        return purposeCodeSys;
    }

    /**
     * 最初の確定日を設定する。
     *
     * @param firstConfirmDate 最初の確定日
     *            
     */
    public void setFirstConfirmDate(Date firstConfirmDate) {
        this.firstConfirmDate = firstConfirmDate;
    }

    /**
     * 最初の確定日を返す。
     *
     * @return 最初の確定日
     */
    public Date getFirstConfirmDate() {
        return firstConfirmDate;
    }

    /**
     * 最初の確定日の日付部分を返す。
     *
     * @return 最初の確定日の日付部分
     */
    @Transient
    public String getFirstConfirmDateTrimTime() {
        //     return ModelUtils.getDateAsString(getFirstConfirmDate());
        return ModelUtils.getDateAsFormatString(getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT);
    }

    /**
     * 確定日を設定する。
     *
     * @param confirmDate 確定日
     */
    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    /**
     * 確定日を返す。
     *
     * @return 確定日
     */
    public Date getConfirmDate() {
        return confirmDate;
    }

    /**
     * 確定日の日付部分を返す。
     *
     * @return 確定日の日付部分
     */
    @Transient
    public String getConfirmDateTrimTime() {
        //  return ModelUtils.getDateAsString(getConfirmDate());
        return ModelUtils.getDateAsFormatString(getConfirmDate(), IInfoModel.KARTE_DATE_FORMAT);
    }

    /**
     * 診療科を設定する。
     *
     * @param department 診療科
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * 診療科を返す。
     *
     * @return 診療科
     */
    public String getDepartment() {
        return department;
    }

    /**
     * 診療科説明を設定する。
     *
     * @param departmentDesc 診療科説明
     */
    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    /**
     * 診療科説明を返す。
     *
     * @return 診療科説明
     */
    public String getDepartmentDesc() {
        return departmentDesc;
    }

    /**
     * departmentNameのGetter
     * @return departmentName
     */
    public String getDepartmentName() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[0];
    }

    /**
     * departmentCodeのGetter
     * @return departmentCode
     */
    public String getDepartmentCode() {
        String[] tokens = tokenizeDept(departmentDesc);
        if (tokens[1] != null) {
            return tokens[1];
        }
        return department;
    }

    /**
     * assignedDoctorNameのGetter
     * @return assignedDoctorName
     */
    public String getAssignedDoctorName() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[2];
    }

    /**
     * assignedDoctorIdのGetter
     * @return assignedDoctorId
     */
    public String getAssignedDoctorId() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[3];
    }

    /**
     * JMARICodeのGetter
     * @return JMARICode
     */
    public String getJMARICode() {
        String[] tokens = tokenizeDept(departmentDesc);
        return tokens[4];
    }
/**
 *
 * @param dept
 * @return
 */
    private String[] tokenizeDept(String dept) {

        // 診療科名、コード、担当医名、担当医コード、JMARI コード, 診療内容
        // を格納する配列を生成する
        CombinedStringParser line = new CombinedStringParser(',', dept);
        String[] result = new String[line.size()];
        for (int index = 0; index < line.size(); index++) {
            result[index] = line.get(index);
        }
        return result;
    }

    /********************************************/
    /**
     * 診療科体系を設定する。
     *
     * @param departmentCodeSys
     *            診療科体系
     */
    public void setDepartmentCodeSys(String departmentCodeSys) {
        this.departmentCodeSys = departmentCodeSys;
    }

    /**
     * 診療科体系を返す。
     *
     * @return 診療科体系
     */
    public String getDepartmentCodeSys() {
        return departmentCodeSys;
    }

    /**
     * 健康保険を設定する。
     *
     * @param healthInsurance 診療科体系
     */
    public void setHealthInsurance(String healthInsurance) {
        this.healthInsurance = healthInsurance;
    }

    /**
     * 健康保険を返す。
     *
     * @return 健康保険
     */
    public String getHealthInsurance() {
        return healthInsurance;
    }

    /**
     * 健康保険説明を設定する。
     *
     * @param healthInsuranceDesc 健康保険説明
     */
    public void setHealthInsuranceDesc(String healthInsuranceDesc) {
        this.healthInsuranceDesc = healthInsuranceDesc;
    }

    /**
     * 健康保険説明を返す。
     *
     * @return 健康保険説明
     */
    public String getHealthInsuranceDesc() {
        return healthInsuranceDesc;
    }

    /**
     * 健康保険体系を設定する。
     *
     * @param healthInsuranceCodeSys 健康保険体系
     */
    public void setHealthInsuranceCodeSys(String healthInsuranceCodeSys) {
        this.healthInsuranceCodeSys = healthInsuranceCodeSys;
    }

    /**
     * 健康保険体系を返す。
     *
     * @return 健康保険体系
     */
    public String getHealthInsuranceCodeSys() {
        return healthInsuranceCodeSys;
    }

    /**
     * 健康保険GUIDを設定する。
     *
     * @param healthInsuranceGUID 健康保険UUID
     */
    public void setHealthInsuranceGUID(String healthInsuranceGUID) {
        this.healthInsuranceGUID = healthInsuranceGUID;
    }

    /**
     * 健康保険GUIDを返す。
     *
     * @return 健康保険UUID
     */
    public String getHealthInsuranceGUID() {
        return healthInsuranceGUID;
    }

    /**
     * 注記があるかどうかを設定する。
     *
     * @param hasMark 注記がある時 true
     */
    public void setHasMark(boolean hasMark) {
        this.hasMark = hasMark;
    }

    /**
     * 注記があるかどうかを返す。
     *
     * @return 注記がある時 true
     */
    public boolean isHasMark() {
        return hasMark;
    }

    /**
     * 画像があるかどうかを設定する。
     *
     * @param hasImage 画像がある時 true
     */
    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    /**
     * 画像があるかどうかを返す。
     *
     * @return 画像がある時 true
     */
    public boolean isHasImage() {
        return hasImage;
    }

    /**
     *
     * @return
     */
    public Boolean isHasImageBoolean() {
        return new Boolean(hasImage);
    }

    /**
     * 処方があるかどうかを設定する。
     *
     * @param hasRp 処方がある時 true
     */
    public void setHasRp(boolean hasRp) {
        this.hasRp = hasRp;
    }

    /**
     * 処方があるかどうかを返す。
     *
     * @return 処方がある時 true
     */
    public boolean isHasRp() {
        return hasRp;
    }

    /**
     *
     * @return
     */
    public Boolean isHasRpBoolean() {
        return new Boolean(hasRp);
    }

    /**
     * 処置があるかどうかを設定する。
     *
     * @param hasTreatment 処置がある時 true
     */
    public void setHasTreatment(boolean hasTreatment) {
        this.hasTreatment = hasTreatment;
    }

    /**
     * 処置があるかどうかを返す。
     *
     * @return 処置がある時 true
     */
    public boolean isHasTreatment() {
        return hasTreatment;
    }

    /**
     *
     * @return
     */
    public Boolean isHasTreatmentBoolean() {
        return new Boolean(hasTreatment);
    }

    /**
     * ラボテストがあるかどうかを設定する。
     *
     * @param hasLaboTest ラボテストがある時 true
     */
    public void setHasLaboTest(boolean hasLaboTest) {
        this.hasLaboTest = hasLaboTest;
    }

    /**
     *
     * @return
     */
    public boolean isHasLaboTest() {
        return hasLaboTest;
    }

    /**
     * ラボテストがあるかどうかを返す。
     *
     * @return ラボテストがある時 true
     */
    public Boolean isHasLaboTestBoolean() {
        return new Boolean(hasLaboTest);
    }

    /**
     * バージョン番号を設定する。
     *
     * @param version バージョン番号
     */
    public void setVersionNumber(String version) {
        this.versionNumber = version;
    }

    /**
     * バージョン番号を返す。
     *
     * @return バージョン番号
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * バージョンノートを設定する。
     *
     * @param versionNotes バージョンノート
     */
    public void setVersionNotes(String versionNotes) {
        this.versionNotes = versionNotes;
    }

    /**
     * バージョンノートを返す。
     *
     * @return バージョンノート
     */
    public String getVersionNotes() {
        return versionNotes;
    }

    /**
     * 親文書IDを設定する。
     *
     * @param parentId 親文書ID
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 親文書IDを返す。
     *
     * @return 親文書ID
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 親文書との関係を設定する。
     *
     * @param parentIdRelation 親文書との関係
     */
    public void setParentIdRelation(String parentIdRelation) {
        this.parentIdRelation = parentIdRelation;
    }

    /**
     * 親文書との関係を返す。
     *
     * @return 親文書との関係
     */
    public String getParentIdRelation() {
        return parentIdRelation;
    }

    /**
     * 親文書との関係説明を設定する。
     *
     * @param relationDesc 親文書との関係説明
     */
    public void setParentIdDesc(String relationDesc) {
        this.parentIdDesc = relationDesc;
    }

    /**
     * 親文書との関係説明を返す。
     *
     * @return 親文書との関係説明
     */
    public String getParentIdDesc() {
        return parentIdDesc;
    }

    /**
     * 親文書との関係体系を設定する。
     *
     * @param relationCodeSys 親文書との関係体系を設定する。
     */
    public void setParentIdCodeSys(String relationCodeSys) {
        this.parentIdCodeSys = relationCodeSys;
    }

    /**
     * 親文書との関係体系を返す。
     *
     * @return 親文書との関係体系
     */
    public String getParentIdCodeSys() {
        return parentIdCodeSys;
    }

    /**
     * アクセス権を返す。
     *
     * @return AccessRightModelのコレクション
     */
    public Collection<AccessRightModel> getAccessRights() {
        return accessRights;
    }

    /**
     * アクセス権を設定する。
     *
     * @param accessRights
     */
    public void setAccessRights(Collection<AccessRightModel> accessRights) {
        this.accessRights = accessRights;
    }

    /**
     * アクセス権を追加する。
     *
     * @param accessRight 追加するアクセス権
     */
    public void addAccessRight(AccessRightModel accessRight) {
        if (accessRights == null) {
            accessRights = new ArrayList<AccessRightModel>(3);
        }
        accessRights.add(accessRight);
    }

    /**
     * この文書のステータスを設定する。
     *
     * @param status この文書のステータス
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * この文書のステータスを返す。
     *
     * @return この文書のステータス
     */
    public String getStatus() {
        return status;
    }

    /**
     * この文書のステータスを返す。
     *
     * @return この文書のステータス
     */
    @Transient
    public String getStatusByName() {
        if (status != null) {
            if (status.equals(IInfoModel.STATUS_FINAL)) {
                return IInfoModel.STATUS_FINAL_READABLE;
            } else if (status.equals(IInfoModel.STATUS_TMP)) {
                return IInfoModel.STATUS_TMP_READABLE;
            } else if (status.equals(IInfoModel.STATUS_MODIFIED)) {
                return IInfoModel.STATUS_MODIFIED_READABLE;
            }
        }
        return "";
    }

    /**
     * ハッシュ値を返す。
     */
    @Override
    public int hashCode() {
        return docId.hashCode() + 11;
    }

    /**
     * 文書IDで eqaul かどうかを返す。
     *
     * @param other
     * @return equal の時 true
     */
    @Override
    public boolean equals(Object other) {
        if (other != null && getClass() == other.getClass()) {
            return getDocId().equals(((DocInfoModel) other).getDocId());
        }
        return false;
    }

    /**
     * 最初の確定日及び確定日で比較する。
     *
     * @param other
     * @return 比較値
     */
    @Override
    public int compareTo(Object other) {
        if (other != null && getClass() == other.getClass()) {
            Date val1 = getFirstConfirmDate();
            Date val2 = ((DocInfoModel) other).getFirstConfirmDate();
            int result = val1.compareTo(val2);
            if (result == 0) {
                val1 = getConfirmDate();
                val2 = ((DocInfoModel) other).getConfirmDate();
                result = val1.compareTo(val2);
            }
            return result;
        }
        return -1;
    }

    private String nullToString(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    /**
     * シリアライズ
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        result.append("<DocInfoModel" + " docId='" + nullToString(docId) + "' docType='" + nullToString(docType) + "' title='" + nullToString(title) + "' purpose='" + nullToString(purpose) + "' />" + System.getProperty("line.separator"));
    }
}
