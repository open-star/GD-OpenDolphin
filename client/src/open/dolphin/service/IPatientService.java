package open.dolphin.service;

import java.util.List;
import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.PatientModel;

/**
 * IPatientService
 *
 * @author Minagawa,Kazushi
 *
 */
public interface IPatientService {
    
    /**
     * 患者オブジェクトを取得する。
     * @param spec PatientSearchSpec 検索仕様
     * @return 患者オブジェクトの List
     */
    public List<PatientModel> getPatients(PatientSearchSpec spec);
    
    /**
     * 患者ID(BUSINESS KEY)を指定して患者オブジェクトを返す。
     *
     * @param patientId 施設内患者ID
     * @return 該当するPatientModel
     */
    public PatientModel getPatient(String patientId);
    
    /**
     * 患者を登録する。
     * @param patient PatientModel
     * @return データベース Primary Key
     */
    public long addPatient(PatientModel patient);
    
    /**
     * 患者情報を更新する。
     * @param patient 更新する「患者オブジェクト
     * @return 更新数
     */
    public int update(PatientModel patient);
}
