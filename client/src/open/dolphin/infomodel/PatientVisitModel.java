package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.persistence.Transient;
import open.dolphin.client.IChart;
import open.dolphin.utils.AgeCalculator;
import open.dolphin.utils.CombinedStringParser;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * 患者来院情報　 MEMO:マッピング d_patient_visit
 * 患者
 * 施設ID
 * 来訪日
 * 科
 * 保険ID
 *
 * データベースレコードマッピングクラス
 * リフレクションを多数含むため、メソッド名等を変更不可
 * d_patient_visit
 * PatientVisitModel generated by hbm2java
 */
public class PatientVisitModel extends InfoModel {

    /**
     *　Drag&Drop用
     */
    public static final DataFlavor PVT_FLAVOR = new DataFlavor(open.dolphin.infomodel.PatientVisitModel.class, "Patient Visit");
    /**
     *　Drag&Drop用
     */
    public static DataFlavor flavors[] = {PVT_FLAVOR};
    private long id;             //MEMO:Refrection
    private PatientModel patient;//d_patient（患者）
    private String facilityId;   //施設ID MEMO:Refrection
    private String pvtDate;      //来訪日 MEMO:Refrection
    private String department;   //科     MEMO:Refrection
    private int status;          //       MEMO:Refrection
    private String insuranceUid; //保険ID MEMO:Refrection
    @Transient
    private String appointment;  //予約
    @Transient
    private int number;

    /**
     * コンストラクタ
     */
    public PatientVisitModel() {
    }

    /**
     * IDを取得
     * MEMO:Refrection
     * @return id
     */
    public long getId() {
        return this.id;
    }

    /**
     * IDをセット
     * MEMO:Refrection
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * d_patient（患者）を取得
     * @return d_patient（患者）
     */
    public PatientModel getPatient() {
        return this.patient;
    }

    /**
     * 予約をセット
     * MEMO:Reflection
     * @param appointment
     */
    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    /**
     * appointmentのGetter
     * MEMO:Reflection
     * @return appointment
     */
    public String getAppointment() {
        return this.appointment;
    }

    /**
     * d_patient（患者）のSetter
     * MEMO:Reflection
     * @param patient d_patient（患者）
     */
    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    /**
     * 施設IDのGetter
     * MEMO:Reflection
     * @return 施設ID
     */
    public String getFacilityId() {
        return this.facilityId;
    }

    /**
     * 施設IDのSetter
     * MEMO:Reflection
     * @param facilityId 施設ID
     */
    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * numberのSetter
     * MEMO:Reflection
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * numberのGetter
     * MEMO:Reflection
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * pvtDateのGetter
     * MEMO:Reflection
     * @return 来訪日
     */
    public String getPvtDate() {
        return this.pvtDate;
    }

    /**
     * pvtDateのSetter
     * MEMO:Reflection
     * @param pvtDate 来訪日
     */
    public void setPvtDate(String pvtDate) {
        this.pvtDate = pvtDate;
    }

    /**
     * pvtDateTrimTimeのGetter
     * MEMO:Reflection
     * @return 来訪日
     */
    public String getPvtDateTrimTime() {
        return ModelUtils.trimTime(pvtDate);
    }

    /**
     * pvtDateTrimDateのGetter
     * MEMO:Reflection
     * @return 来訪日
     */
    public String getPvtDateTrimDate() {
        return ModelUtils.trimDate(pvtDate);
    }

    /**
     * departmentNameのGetter
     * MEMO:Reflection
     * @return DepartmentName 診療科名
     */
    public String getDepartmentName() {
        // MEMO: Need to check this method
        //       Obsolute method for 1.3
        String[] tokens = tokenizeDept(department);
        return tokens[0];
    }

    /**
     * departmentのGetter
     * MEMO:Reflection
     * @return department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * departmentのSetter
     * MEMO:Reflection
     * @param department 診療科情報
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * departmentCategoryのGetter
     * MEMO:Reflection
     * @return 診療内容
     */
    public String getCategory() {
        // MEMO: Need to check this method
        //       Obsolute method for 1.3
        String[] tokens = tokenizeDept(department);
        return tokens[5];
    }

    /**
     * departmentCodeのGetter
     * MEMO:Reflection
     * @return DepartmentCode
     */
    public String getDepartmentCode() {
        // MEMO: Need to check this method
        //       Obsolute method for 1.3
        String[] tokens = tokenizeDept(department);
        return tokens[1];
    }

    /**
     * 担当医名のGetter
     * MEMO:Reflection
     * @return 担当医名
     */
    public String getAssignedDoctorName() {
        // MEMO: Need to check this method
        //       Obsolute method for 1.3
        String[] tokens = tokenizeDept(department);
        return tokens[2];
    }

    /**
     * 担当医コードのGetter
     * MEMO:Reflection
     * @return 担当医コード
     */
    public String getAssignedDoctorId() {
        // MEMO: Need to check this method
        //       Obsolute method for 1.3
        String[] tokens = tokenizeDept(department);
        return tokens[3];
    }

    /**
     * departmentJmariCodeのGetter
     * MEMO:Reflection
     * @return JmariCode
     */
    public String getJmariCode() {
        // MEMO: Need to check this method
        //       Obsolute method for 1.3
        String[] tokens = tokenizeDept(department);
        return tokens[4];
    }

    /**
     * deptNoTokenizeのGetter
     * MEMO:Reflection
     * @return DeptNoTokenize
     */
    public String getDeptNoTokenize() {
        return department;
    }

    /**
     * Department を分割して配列にする
     * 
     * MEMO:Reflection
     * @param "診療科名,コード,担当医名,担当医コード,JMARI コード,診療内容,メモ２"
     * @return Department を分割した配列
     */
    private String[] tokenizeDept(String dept) {
      
        CombinedStringParser line = new CombinedStringParser(',', dept);
        String[] result = new String[line.size()];
        for (int index = 0; index < line.size(); index++) {
            result[index] = line.get(index);
        }
        return result;
    }

    /**
     * statusのGetter
     * MEMO:Reflection
     * @return
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * statusのSetter
     * MEMO:Reflection
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * stateのSetter
     * MEMO:Reflection
     * @param status
     */
    public void setState(IChart.state status) {
        switch (status) {
            case CLOSE_NONE:
                this.status = 0;
                break;
            case CLOSE_SAVE:
                this.status = 1;
                break;
            case OPEN_NONE:
                this.status = 2;
                break;
            case OPEN_SAVE:
                this.status = 3;
                break;
            case CANCEL_PVT:
                this.status = -1;
                break;
        }
    }

    /**
     * stateのGetter
     * MEMO:Reflection
     * @return　状態
     */
    //   public Integer getState() {
//        return new Integer(status);
    //  }
    public IChart.state getState() {
        switch (status) {
            case -1:
                return IChart.state.CANCEL_PVT;
            case 0:
                return IChart.state.CLOSE_NONE;
            case 1:
                return IChart.state.CLOSE_SAVE;
            case 2:
                return IChart.state.OPEN_NONE;
            case 3:
                return IChart.state.OPEN_SAVE;
        }
        return IChart.state.CLOSE_NONE;
    }

    /**
     * patientIdのGetter
     * MEMO:Reflection
     * @return 患者ID
     */
    public String getPatientId() {
        return getPatient().getPatientId();
    }

    /**
     * patientNameのGetter
     * MEMO:Reflection
     * @return
     */
    public String getPatientName() {
        return getPatient().getFullName();
    }

    /**
     * patientGenderDescのGetter
     * MEMO:Reflection
     * @return
     */
    public String getPatientGenderDesc() {
        return ModelUtils.getGenderDesc(getPatient().getGender());
    }

    /**
     * memoのGetter
     * MEMO:Reflection
     * @return
     */
    public String getMemo() {
        return ModelUtils.getMemo(getPatient().getMemo());
    }

    /**
     * patientAgeBirthdayのGetter
     * MEMO:Reflection
     * @return
     */
    public String getPatientAgeBirthday() {
        return ModelUtils.getAgeBirthday(getPatient().getBirthday());
    }

    /**
     * patientBirthdayのGetter
     * MEMO:Reflection
     * @return
     */
    public String getPatientBirthday() {
        return getPatient().getBirthday();
    }

    /**
     * patientAgeのGetter
     * MEMO:Reflection
     * @return
     */
    public String getPatientAge() {
        return AgeCalculator.getAge(getPatient().getBirthday());
    }

    /**
     * insuranceUidのGetter
     * @return
     */
    public String getInsuranceUid() {
        return this.insuranceUid;
    }

    /**
     * insuranceUidのSetter
     * MEMO:Reflection
     * @param insuranceuid
     */
    public void setInsuranceUid(String insuranceuid) {
        this.insuranceUid = insuranceuid;
    }

    /**
     *
     * @param df
     * @return
     */
    public boolean isDataFlavorSupported(DataFlavor df) {
        return df.equals(PVT_FLAVOR);
    }

    /**
     * transferDataのGetter
     * @param df
     * @return
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
        if (df.equals(PVT_FLAVOR)) {
            return this;
        } else {
            throw new UnsupportedFlavorException(df);
        }
    }

    /**
     * transferDataFlavorsのGetter
     * @return
     */
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * メモ２のGetter
     * @return メモ２
     */
    public String getMemo2() {
        String strRet = "";
        String[] tokens = tokenizeDept(department);
        if (6 < tokens.length) {
            strRet =tokens[6];
        }

        return strRet;
    }
}
