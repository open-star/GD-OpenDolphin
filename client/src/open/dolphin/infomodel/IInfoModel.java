package open.dolphin.infomodel;

import java.io.Serializable;

/**
 * IInfoModel
 *
 * @athor Minagawa, Kazushi
 *
 */
public interface IInfoModel extends Serializable {

    /**
     *
     */
    public static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";    // ISO 8601 style date format
    //   public static final String KARTE_DATE_FORMAT = "yyyy年M月d日'（'EEE'）'H時m分";    // カルテの確定日表示用のフォーマット
    /**
     *
     */
    public static final String KARTE_DATE_FORMAT = "yyyy-MM-dd'('EEE')'HH:mm";    // カルテの確定日表示用のフォーマット
    /**
     *
     */
    public static final String KARTE_DATE_FORMAT_WITHOUT_TIME = "yyyy-MM-dd'('EEE')'";    // カルテの確定日表示用のフォーマット
    /**
     *
     */
    public static final String DATE_WITHOUT_TIME = "yyyy-MM-dd";    // 時間部分のない Date
    /**
     *
     */
    public static final String LIKE_OPERATOR = "%";    // Oersistence Query の LIKE 演算子
    /**
     *
     */
    public static final String COMPOSITE_KEY_MAKER = ":";    // 複合キーにするための結合子
    /**
     *
     */
    public static final String ADMIN_ROLE = "admin";    // 管理者ロール
    /**
     *
     */
    public static final String USER_ROLE = "user";    // 利用者ロール
    /**
     *
     */
    public static final String MARITAL_STATUS = "maritalStatus";    // 婚姻状況
    /**
     *
     */
    public static final String NATIONALITY = "nationality";    // 国籍
    /**
     *
     */
    public static final String MEMO = "memo";    // メモ
    /**
     *
     */
    public static final String MALE = "male";
    /**
     *
     */
    public static final String MALE_DISP = "男";
    /**
     *
     */
    public static final String FEMALE = "female";
    /**
     *
     */
    public static final String FEMALE_DISP = "女";
    /**
     *
     */
    public static final String UNKNOWN = "不明";
    /**
     *
     */
    public static final String AGE = "歳";
    /**
     *
     */
    public static final String DOCTYPE_S_KARTE = "s_karte";
    /**
     *
     */
    public static final String DOCTYPE_KARTE = "karte";
    /**
     *
     */
    public static final String DOCTYPE_LETTER = "letter";
    /**
     *
     */
    public static final String DOCTYPE_LETTER_REPLY = "letterReply";
    /**
     *
     */
    public static final String PURPOSE_RECORD = "recode";
    /**
     *
     */
    public static final String PARENT_OLD_EDITION = "oldEdition";
    /**
     *
     */
    public static final String RELATION_NEW = "newVersion";
    /**
     *
     */
    public static final String RELATION_OLD = "oldVersion";
    /**
     *
     */
    public static final String DEFAULT_DIAGNOSIS_TITLE = "病名登録";
    /**
     *
     */
    public static final String DEFAULT_DIAGNOSIS_CATEGORY = "mainDiagnosis";
    /**
     *
     */
    public static final String DEFAULT_DIAGNOSIS_CATEGORY_DESC = "主病名";
    /**
     *
     */
    public static final String DEFAULT_DIAGNOSIS_CATEGORY_CODESYS = "MML0012";
    /**
     *
     */
    public static final String ORCA_OUTCOME_RECOVERED = "治癒";
    /**
     *
     */
    public static final String ORCA_OUTCOME_DIED = "死亡";
    /**
     *
     */
    public static final String ORCA_OUTCOME_END = "中止";
    /**
     *
     */
    public static final String ORCA_OUTCOME_TRANSFERED = "移行";
    // Stamp Roles
    /**
     *
     */
    public static final String MODULE_PROGRESS_COURSE = "progressCourse";    // ProgessCourse
    /**
     *
     */
    public static final String ROLE_SOA = "soa";    // SOA stamp
    /**
     *
     */
    public static final String ROLE_P = "p";   // P stamp
    /**
     *
     */
    public static final String ROLE_SOA_SPEC = "soaSpec";    // SOA spec
    /**
     *
     */
    public static final String ROLE_P_SPEC = "pSpec";    // P spec
    /**
     *
     */
    public static final String ROLE_TEXT = "text";    // Text stamp
    /**
     *
     */
    public static final String ROLE_DIAGNOSIS = "diagnosis";    // 傷病名
    /**
     *
     */
    public static final String ROLE_ORCA_SET = "orcaSet";    // ORCA 入力セット
    /**
     *
     */
    public static final String STATUS_FINAL = "F";
    /**
     *
     */
    public static final String STATUS_MODIFIED = "M";
    /**
     *
     */
    public static final String STATUS_TMP = "T";
    /**
     *
     */
    public static final String STATUS_TEMPLATE = "A";
    /**
     *
     */
    public static final String STATUS_NONE = "N";
    /**
     *
     */
    public static final String STATUS_DELETE = "D";
    /**
     *
     */
    public static final String STATUS_FINAL_READABLE = "送信済み";
    /**
     *
     */
    public static final String STATUS_MODIFIED_READABLE = "保存";
    /**
     *
     */
    public static final String STATUS_TMP_READABLE = "仮保存";
    /**
     *
     */
    public static final String STATUS_NONE_READABLE = "初期";
    /**
     *
     */
    public static final String STATUS_DELETE_READABLE = "削除";
    /**
     *
     */
    public static final String PERMISSION_ALL = "all";
    /**
     *
     */
    public static final String PERMISSION_READ = "read";
    /**
     *
     */
    public static final String ACCES_RIGHT_PATIENT = "patient";
    /**
     *
     */
    public static final String ACCES_RIGHT_CREATOR = "creator";
    /**
     *
     */
    public static final String ACCES_RIGHT_EXPERIENCE = "experience";
    /**
     *
     */
    public static final String ACCES_RIGHT_PATIENT_DISP = "被記載者(患者)";
    /**
     *
     */
    public static final String ACCES_RIGHT_CREATOR_DISP = "記載者施設";
    /**
     *
     */
    public static final String ACCES_RIGHT_EXPERIENCE_DISP = "診療歴のある施設";
    /**
     *
     */
    public static final String ACCES_RIGHT_PERSON_CODE = "personCode";
    /**
     *
     */
    public static final String ACCES_RIGHT_FACILITY_CODE = "facilityCode";
    /**
     *
     */
    public static final String ACCES_RIGHT_EXPERIENCE_CODE = "facilityCode";
    /**
     *
     */
    public static final String CLAIM_210 = "210";    // 処方コード
    /**
     *
     */
    public static final String INSURANCE_SELF = "自費";
    /**
     *
     */
    public static final String INSURANCE_SELF_CODE = "Z1";
    /**
     *
     */
    public static final String INSURANCE_SYS = "MML031";
    // StampTreeのエンティティ（情報の実体）名
    /**
     *
     */
    public static final String ENTITY_DIAGNOSIS = "diagnosis";    // 傷病名
    /**
     *
     */
    public static final String ENTITY_TEXT = "text";    // テキスト
    /**
     *
     */
    public static final String ENTITY_PATH = "path";    // パ ス
    /**
     *
     */
    public static final String ENTITY_GENERAL_ORDER = "generalOrder";    // 汎用
    /**
     *
     */
    public static final String ENTITY_OTHER_ORDER = "otherOrder";    // その他
    /**
     *
     */
    public static final String ENTITY_TREATMENT = "treatmentOrder";    // 処 置
    /**
     *
     */
    public static final String ENTITY_SURGERY_ORDER = "surgeryOrder";    // 手 術
    /**
     *
     */
    public static final String ENTITY_RADIOLOGY_ORDER = "radiologyOrder";    // 画像診断
    /**
     *
     */
    public static final String ENTITY_LABO_TEST = "testOrder";    // ラボテスト
    /**
     *
     */
    public static final String ENTITY_PHYSIOLOGY_ORDER = "physiologyOrder";    //生体検査
    //  public static final String ENTITY_BACTERIA_ORDER = "bacteriaOrder";    /** 細菌検査 */
    /**
     *
     */
    public static final String ENTITY_INJECTION_ORDER = "injectionOrder";    //注 射
    /**
     *
     */
    public static final String ENTITY_MED_ORDER = "medOrder";    // 処 方
    /**
     *
     */
    public static final String ENTITY_BASE_CHARGE_ORDER = "baseChargeOrder";    // 診 断
    /**
     *
     */
    public static final String ENTITY_INSTRACTION_CHARGE_ORDER = "instractionChargeOrder";    //指 導
    /**
     *
     */
    public static final String ENTITY_STAY_ON_HOME_CHARGE_ORDER = "stayOnHomeChargeOrder";    // 在 宅
    /**
     *
     */
    public static final String ENTITY_ORCA = "orcaSet";    // ORCA セット
    /**
     *
     */
    public static final String[] STAMP_ENTITIES = new String[]{ /** Entity の配列 */
        ENTITY_DIAGNOSIS, ENTITY_TEXT, ENTITY_PATH, ENTITY_ORCA, ENTITY_GENERAL_ORDER, ENTITY_OTHER_ORDER, ENTITY_TREATMENT,
        ENTITY_SURGERY_ORDER, ENTITY_RADIOLOGY_ORDER, ENTITY_LABO_TEST, ENTITY_PHYSIOLOGY_ORDER,
        /*ENTITY_BACTERIA_ORDER,*/ ENTITY_INJECTION_ORDER, ENTITY_MED_ORDER, ENTITY_BASE_CHARGE_ORDER, ENTITY_INSTRACTION_CHARGE_ORDER,
        ENTITY_STAY_ON_HOME_CHARGE_ORDER
    };
    // StampTreeのタブ名
    /**
     *
     */
    public static final String TABNAME_DIAGNOSIS = "傷病名"; // 傷病名
    /**
     *
     */
    public static final String TABNAME_TEXT = "テキスト"; // テキスト
    /**
     *
     */
    public static final String TABNAME_PATH = "パ ス";    // パ ス
    /**
     *
     */
    public static final String TABNAME_ORCA = "ORCA";    // ORCA セット
    /**
     *
     */
    public static final String TABNAME_GENERAL = "汎 用";    // 汎 用
    /**
     *
     */
    public static final String TABNAME_OTHER = "その他";    // その他
    /**
     *
     */
    public static final String TABNAME_TREATMENT = "処 置";    // 処 置
    /**
     *
     */
    public static final String TABNAME_SURGERY = "手 術";    // 手 術
    /**
     *
     */
    public static final String TABNAME_RADIOLOGY = "画像診断";    // 画像診断
    /**
     *
     */
    public static final String TABNAME_LABO = "検体検査";    // 検体検査
    /**
     *
     */
    public static final String TABNAME_PHYSIOLOGY = "生体検査";    // 生体検査
    //   public static final String TABNAME_BACTERIA = "細菌検査";    // 細菌検査
    /**
     *
     */
    public static final String TABNAME_INJECTION = "注 射";    // 注 射
    /**
     *
     */
    public static final String TABNAME_MED = "処 方";    // 処 方
    /**
     *
     */
    public static final String TABNAME_BASE_CHARGE = "初診・再診";    // 初診・再診
    /**
     *
     */
    public static final String TABNAME_INSTRACTION = "指導";    // 指導
    /**
     *
     */
    public static final String TABNAME_STAY_ON_HOME = "在宅";    // 在宅
    /**
     *
     */
    public static final int TAB_INDEX_ORCA = 3;    // ORCA のタブ番号
    /**
     *
     */
    public static String[] STAMP_NAMES = { // スタンプのタブ名配列
        TABNAME_DIAGNOSIS, TABNAME_TEXT, TABNAME_PATH, TABNAME_ORCA,
        TABNAME_GENERAL, TABNAME_OTHER, TABNAME_TREATMENT, TABNAME_SURGERY,
        TABNAME_RADIOLOGY, TABNAME_LABO, TABNAME_PHYSIOLOGY, /* TABNAME_BACTERIA,*/
        TABNAME_INJECTION, TABNAME_MED, TABNAME_BASE_CHARGE, TABNAME_INSTRACTION,
        TABNAME_STAY_ON_HOME
    };
    /** スタンプのCLAIM版点数集計先 */
    public static String[] CLAIM_CLASS_CODE = {
        "", "", "", "", "", "800-899", "400-499", "500-599", "700-799", "600-699", "600-699",
        "600", "300-331", "210-230", "110-125", "130-139", "140-149"
    };
    /**
     *
     */
    public static final String OBSERVATION_ALLERGY = "Allergy";
    /**
     *
     */
    public static final String OBSERVATION_BLOODTYPE = "Bloodtype";
    /**
     *
     */
    public static final String OBSERVATION_INFECTION = "Infection";
    /**
     *
     */
    public static final String OBSERVATION_LIFESTYLE = "Lifestyle";
    /**
     *
     */
    public static final String OBSERVATION_PHYSICAL_EXAM = "PhysicalExam";
    /**
     *
     */
    public static final String PHENOMENON_BODY_HEIGHT = "bodyHeight";
    /**
     *
     */
    public static final String PHENOMENON_BODY_WEIGHT = "bodyWeight";
    /**
     *
     */
    public static final String PHENOMENON_TOBACCO = "tobacco";
    /**
     *
     */
    public static final String PHENOMENON_ALCOHOL = "alcohol";
    /**
     *
     */
    public static final String PHENOMENON_OCCUPATION = "occupation";
    /**
     *
     */
    public static final String UNIT_BODY_WEIGHT = "Kg";
    /**
     *
     */
    public static final String UNIT_BODY_HEIGHT = "cm";
    /**
     *
     */
    public static final String PUBLISH_TREE_LOCAL = "院内";
    /**
     *
     */
    public static final String PUBLISH_TREE_PUBLIC = "グローバル";
    /**
     *
     */
    public static final String PUBLISHED_TYPE_GLOBAL = "global";
}
