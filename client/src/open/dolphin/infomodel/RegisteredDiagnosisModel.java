package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import javax.persistence.Transient;
import open.dolphin.log.LogWriter;
import open.dolphin.utils.CombinedStringParser;
import open.dolphin.utils.DiagnosisCode;

/**
 * 傷病名
 * ORCAのマスタからのデータを直接保持する MEMO:マッピング  d_diagnosis  tbl_ptbyomei
 *
 * StartDate   sryymd   疾患開始日
 * OutcomeDesc tenkikbn 転帰
 * EndDate     tenkiymd 疾患終了日（転帰）
 *
 * RegisteredDiagnosisModel generated by hbm2java
 */
public class RegisteredDiagnosisModel extends KarteEntryBean {

    private String diagnosis;      // byomei       疾患名 MEMO:Refrection
    private String diagnosisCode;  // khnbyomeicd  病名コード MEMO:Refrection
    private String diagnosisCodeSystem;//疾患名コード体系コード MEMO:Refrection
    private String firstEncounterDate;//初診日 MEMO:Refrection
    private String relatedHealthInsurance;//関連健康保険 MEMO:Refrection
    private DiagnosisCategoryModel diagnosisCategoryModel;//MEMO:Refrection
    private DiagnosisOutcomeModel diagnosisOutcomeModel;//MEMO:Refrection
    @Transient
    private PatientLiteModel patientLiteModel;
    @Transient
    private UserLiteModel userLiteModel;

    /**
     * コンストラクタ
     */
    public RegisteredDiagnosisModel() {
    }

    /**
     * コピーコンストラクタ
     * @param original
     */
    public RegisteredDiagnosisModel(RegisteredDiagnosisModel original) {
        setDiagnosis(original.getDiagnosis());
        setDiagnosisCode(original.getDiagnosisCode());
        setDiagnosisCodeSystem(original.getDiagnosisCodeSystem());
    }

    /**
     * 疾患名のGetter
     * MEMO:Refrection
     * @return 疾患名
     */
    public String getDiagnosis() {
        return this.diagnosis;
    }

    /**
     * 疾患名のSetter
     * MEMO:Refrection
     * @param diagnosis 疾患名
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * 疾患名コードのGetter
     * MEMO:Refrection
     * @return 疾患名コード
     */
    public String getDiagnosisCode() {
        return this.diagnosisCode;
    }

    /**
     * 疾患名コードのSetter
     * MEMO:Refrection
     * @param diagnosisCode 疾患名コード
     */
    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    /**
     * 代理疾患名コードのGetter
     * @return 代理疾患名コード
     */
    public String getAlternateDiagnosisCode() {
        return new DiagnosisCode(diagnosisCode).getAlternateCode();
    }

    /**
     * 疾患名コード体系コードのGetter
     * MEMO:Refrection
     * @return 疾患名コード体系コード
     */
    public String getDiagnosisCodeSystem() {
        return this.diagnosisCodeSystem;
    }

    /**
     * 疾患名コード体系コードのSetter
     * MEMO:Refrection
     * @param diagnosisCodeSystem 疾患名コード体系コード
     */
    public void setDiagnosisCodeSystem(String diagnosisCodeSystem) {
        this.diagnosisCodeSystem = diagnosisCodeSystem;
    }

    /**
     * 疾患区分のGetter
     * MEMO:Refrection
     * @return 疾患区分
     */
    public String getCategory() {
        return diagnosisCategoryModel != null ? diagnosisCategoryModel.getDiagnosisCategory() : null;
    }

    /**
     * 疾患区分のSetter
     * MEMO:Refrection
     * @param  category 疾患区分
     */
    public void setCategory(String category) {
        if (diagnosisCategoryModel == null) {
            diagnosisCategoryModel = new DiagnosisCategoryModel();
        }
        this.diagnosisCategoryModel.setDiagnosisCategory(category);
    }

    /**
     * 疾患詳細区分のGetter
     * @return 疾患詳細区分
     */
    public String getCategoryDesc() {
        return diagnosisCategoryModel != null ? diagnosisCategoryModel.getDiagnosisCategoryDesc() : null;
    }

    /**
     * 疾患詳細区分のSetter
     * @param categoryDesc 疾患詳細区分
     */
    public void setCategoryDesc(String categoryDesc) {
        if (diagnosisCategoryModel == null) {
            diagnosisCategoryModel = new DiagnosisCategoryModel();
        }
        this.diagnosisCategoryModel.setDiagnosisCategoryDesc(categoryDesc);
    }

    /**
     * 疾患区分体系コードのGetter
     * @return 疾患区分体系コード
     */
    public String getCategoryCodeSys() {
        return diagnosisCategoryModel != null ? diagnosisCategoryModel.getDiagnosisCategoryCodeSys() : null;
    }

    /**
     * 疾患区分体系コードのSetter
     * @param categoryTable 疾患区分体系コード
     */
    public void setCategoryCodeSys(String categoryTable) {
        if (diagnosisCategoryModel == null) {
            diagnosisCategoryModel = new DiagnosisCategoryModel();
        }
        this.diagnosisCategoryModel.setDiagnosisCategoryCodeSys(categoryTable);
    }

    /**
     * 初診日のGetter
     * MEMO:Refrection
     * @return 初診日
     */
    public String getFirstEncounterDate() {
        return this.firstEncounterDate;
    }

    /**
     * 初診日のSetter
     * MEMO:Refrection
     * @param firstEncounterDate 初診日
     */
    public void setFirstEncounterDate(String firstEncounterDate) {
        this.firstEncounterDate = firstEncounterDate;
    }

    /**
     * 開始日のGetter
     * @return 開始日
     */
    public String getStartDate() {
        if (getStarted() != null) {
            return ModelUtils.getDateAsString(getStarted());
        }
        return null;
    }

    /**
     * 開始日のSetter
     * @param startDate 開始日
     */
    public void setStartDate(String startDate) {
        if (startDate != null) {
            int index = startDate.indexOf('T');
            if (index < 0) {
                startDate += "T00:00:00";
            }
            //System.out.println(startDate);
            setStarted(ModelUtils.getDateTimeAsObject(startDate));
        }
    }

    /**
     * 終了日のGetter
     * @return 終了日
     */
    public String getEndDate() {
        if (getEnded() != null) {
            return ModelUtils.getDateAsString(getEnded());
        }
        return null;
    }

    /**
     * 終了日のSetter
     * @param endDate 終了日
     */
    public void setEndDate(String endDate) {
        if (endDate != null) {
            if (!endDate.isEmpty()) {
                int index = endDate.indexOf('T');
                if (index < 0) {
                    endDate += "T00:00:00";
                }
                setEnded(ModelUtils.getDateTimeAsObject(endDate));
            } else {
                setEnded(null);
            }
        }
    }

    /**
     * 転帰のGetter
     * MEMO:Refrection
     * @return 転帰
     */
    public String getOutcome() {
        return diagnosisOutcomeModel != null ? diagnosisOutcomeModel.getOutcome() : null;
    }

    /**
     * 転帰のSetter
     * MEMO:Refrection
     * @param outcome 転帰
     */
    public void setOutcome(String outcome) {
        if (diagnosisOutcomeModel == null) {
            diagnosisOutcomeModel = new DiagnosisOutcomeModel();
        }
        this.diagnosisOutcomeModel.setOutcome(outcome);
    }

    /**
     * 転帰詳細のGetter
     * MEMO:Refrection
     * @return 転帰詳細
     */
    public String getOutcomeDesc() {
        return diagnosisOutcomeModel != null ? diagnosisOutcomeModel.getOutcomeDesc() : null;
    }

    /**
     * 転帰詳細のSetter
     * MEMO:Refrection
     * @param outcomeDesc 転帰詳細
     */
    public void setOutcomeDesc(String outcomeDesc) {
        if (diagnosisOutcomeModel == null) {
            diagnosisOutcomeModel = new DiagnosisOutcomeModel();
        }
        this.diagnosisOutcomeModel.setOutcomeDesc(outcomeDesc);
    }

    /**
     * 転帰体系コードのGetter
     * MEMO:Refrection
     * @return 転帰体系コード
     */
    public String getOutcomeCodeSys() {
        return diagnosisOutcomeModel != null ? diagnosisOutcomeModel.getOutcomeCodeSys() : null;
    }

    /**
     * 転帰体系コードのSetter
     * MEMO:Refrection
     * @param outcomeTable 転帰体系コード
     */
    public void setOutcomeCodeSys(String outcomeTable) {
        if (diagnosisOutcomeModel == null) {
            diagnosisOutcomeModel = new DiagnosisOutcomeModel();
        }
        this.diagnosisOutcomeModel.setOutcomeCodeSys(outcomeTable);
    }

    /**
     * 保険のGetter
     * MEMO:Refrection
     * @return 保険
     */
    public String getRelatedHealthInsurance() {
        return this.relatedHealthInsurance;
    }

    /**
     * 保険のSetter
     * MEMO:Refrection
     * @param relatedHealthInsurance 保険
     */
    public void setRelatedHealthInsurance(String relatedHealthInsurance) {
        this.relatedHealthInsurance = relatedHealthInsurance;
    }

    /**
     * diagnosisCategoryModelのGetter
     * @return
     */
    public DiagnosisCategoryModel getDiagnosisCategoryModel() {
        return diagnosisCategoryModel;
    }

    /**
     * diagnosisCategoryModelのSetter
     * @param diagnosisCategoryModel
     */
    public void setDiagnosisCategoryModel(
            DiagnosisCategoryModel diagnosisCategoryModel) {
        this.diagnosisCategoryModel = diagnosisCategoryModel;
    }

    /**
     * diagnosisOutcomeModelのGetter
     * MEMO:Refrection
     * @return diagnosisOutcomeModel
     */
    public DiagnosisOutcomeModel getDiagnosisOutcomeModel() {
        return diagnosisOutcomeModel;
    }

    /**
     * diagnosisOutcomeModelのSetter
     * MEMO:Refrection
     * @param diagnosisOutcomeModel
     */
    public void setDiagnosisOutcomeModel(
            DiagnosisOutcomeModel diagnosisOutcomeModel) {
        this.diagnosisOutcomeModel = diagnosisOutcomeModel;
    }

    /**
     * 一時患者情報のGetter
     * @return 一時患者情報
     */
    public PatientLiteModel getPatientLiteModel() {
        return patientLiteModel;
    }

    /**
     * 一時患者情報のSetter
     * @param patientLiteModel 一時患者情報
     */
    public void setPatientLiteModel(PatientLiteModel patientLiteModel) {
        this.patientLiteModel = patientLiteModel;
    }

    /**
     * 一時ユーザ情報のGetter
     * @return　一時ユーザ情報
     */
    public UserLiteModel getUserLiteModel() {
        return userLiteModel;
    }

    /**
     * 一時ユーザ情報のSetter
     * @param userLiteModel 一時ユーザ情報
     */
    public void setUserLiteModel(UserLiteModel userLiteModel) {
        this.userLiteModel = userLiteModel;
    }

    /**
     * カンマ区切りの病名を分割
     * @param diagnosis カンマ区切りの病名
     * @return 分割された病名
     */
    private static String[] splitDiagnosis(String diagnosis) {
        if (diagnosis == null) {
            return null;
        }
        String[] ret = null;
        try {
            ret = diagnosis.split("\\s*,\\s*");
        } catch (Exception e) {
            LogWriter.error("RegisteredDiagnosisModel", "splitDiagnosis");
        }
        return ret;
    }

    /**
     * 傷病名のGetter
     * @return 傷病名
     */
    public String getDiagnosisName() {
        String[] splits = splitDiagnosis(this.diagnosis);
        return (splits != null && splits.length == 2 && splits[0] != null) ? splits[0] : this.diagnosis;
    }

    /**
     * 代理傷病名のGetter
     * @return 代理傷病名
     */
    public String getAlternateDiagnosisName() {
        CombinedStringParser parser = new CombinedStringParser(getDiagnosisName());
        if (parser.size() > 1) {
            return parser.toPlainString();
        } else {
            return getDiagnosisName();
        }
    }

    /**
     * 傷病名別名のGetter
     * @return 傷病名別名
     */
    public String getDiagnosisAlias() {
        String[] splits = splitDiagnosis(this.diagnosis);
        return (splits != null && splits.length == 2 && splits[1] != null) ? splits[1] : null;
    }

    /**
     * aliasOrNameのGetter
     * @return aliasOrName
     */
    public String getAliasOrName() {
        String[] aliasOrName = splitDiagnosis(this.diagnosis);
        if (aliasOrName != null && aliasOrName.length == 2 && aliasOrName[1] != null) {
            return aliasOrName[1];
        }
        return this.diagnosis;
    }

    /**
     * クリア
     */
    public void clear() {
        setId(0L);
        setKarte(null);
        setCreator(null);
        setDiagnosisCategoryModel(null);
        setDiagnosisOutcomeModel(null);
        setFirstEncounterDate(null);
        setStartDate(null);
        setEndDate(null);
        setRelatedHealthInsurance(null);
        setFirstConfirmDate(null);
        setConfirmDate(null);
        setStatus(null);
        setPatientLiteModel(null);
        setUserLiteModel(null);
    }
}
