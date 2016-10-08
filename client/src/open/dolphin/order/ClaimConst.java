package open.dolphin.order;

/**
 *
 * @author
 */
public class ClaimConst {

    /**
     *
     */
    public enum ClaimSpec {

        /**
         *
         */
        BASE_CHARGE("診断料", "110-125", null),
        /**
         *
         */
        INSTRACTION_CHARGE("指導", "130-139", "130"),
        /**
         *
         */
        STAY_ON_HOME("在宅", "140-149", "140"),
        /**
         *
         */
        INJECTION("注 射", "300-331", null),
        /**
         *
         */
        TREATMENT("処 置", "400-499", "400"),
        /**
         *
         */
        SURGERY("手術", "500-599", "500"),
        /**
         *
         */
        LABO_TEST("検体検査", "600-699", "600"),
        /**
         *
         */
        PHYSIOLOGY("生理・内視鏡検査", "600-699", "600"),
        /**
         *
         */
        RADIOLOGY("画像診断", "700-799", "700"),
        /**
         *
         */
        OTHER("その他", "800-899", "800"),
        /**
         *
         */
        GENERAL("汎 用", null, null);
        private String name;
        private String searchCode;
        private String classCode;

        /**
         *
         * @param name
         * @param searchCode
         * @param classCode
         */
        ClaimSpec(String name, String searchCode, String classCode) {
            setName(name);
            setSearchCode(searchCode);
            setClassCode(classCode);
        }

        /**
         *
         * @return
         */
        public String getClassCode() {
            return classCode;
        }

        /**
         *
         * @param classCode
         */
        public void setClassCode(String classCode) {
            this.classCode = classCode;
        }

        /**
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         *
         * @return
         */
        public String getSearchCode() {
            return searchCode;
        }

        /**
         *
         * @param searchCode
         */
        public void setSearchCode(String searchCode) {
            this.searchCode = searchCode;
        }
    }

    /**
     *
     */
    public enum MasterSet {

        /**
         *
         */
        DIAGNOSIS("disease", "傷病名"),
        /**
         *
         */
        TREATMENT("treatment", "診療行為"),
        /**
         *
         */
        MEDICAL_SUPPLY("medicine", "内用・外用薬"),
        /**
         *
         */
        ADMINISTRATION("admin", "用法"),
        /**
         *
         */
        INJECTION_MEDICINE("medicine", "注射薬"),
        /**
         *
         */
        TOOL_MATERIAL("tool_material", "特定器材"),
        //   INTERACT("interact", "相互作用"),
        //  SSKIJYO("sskijyo", "症状処置機序"),
        /**
         *
         */
        SRYCDCHG("srycdchg", "一般老人置換");
        private String name;
        private String dispName;

        /**
         *
         * @param name
         * @param dispName
         */
        MasterSet(String name, String dispName) {
            setName(name);
            setDispName(dispName);
        }

        /**
         *
         * @return
         */
        public String getDispName() {
            return dispName;
        }

        /**
         *
         * @param dispName
         */
        private void setDispName(String dispName) {
            this.dispName = dispName;
        }

        /**
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @param name
         */
        private void setName(String name) {
            this.name = name;
        }
    }

    /**
     *
     */
    public enum SubTypeSet {

        /**
         *
         */
        NAMES("names", "組み合わせ"),
        /**
         *
         */
        PREFIX("prefix", "接頭語"),
        /**
         *
         */
        TYPE("type", "タイプ"),
        /**
         *
         */
        PART("part", "部位"),
        /**
         *
         */
        POSTFIX("postfix", "接尾語");
        private String name;
        private String dispName;

        /**
         *
         * @param name
         * @param dispName
         */
        SubTypeSet(String name, String dispName) {
            setName(name);
            setDispName(dispName);
        }

        /**
         *
         * @param dispName
         */
        private void setDispName(String dispName) {
            this.dispName = dispName;
        }

        /**
         * 
         * @param name
         */
        private void setName(String name) {
            this.name = name;
        }

        /**
         * 
         * @return
         */
        public String getDispName() {
            return dispName;
        }

        /**
         * 
         * @return
         */
        public String getName() {
            return name;
        }
    }
//    public static final String DISEASE_MASTER_SYSTEM = "mml.codeSystem.diseaseMaster=ICD10_2001-10-03MEDIS";
    /**
     *
     */
    public static final String MASTER_FLAG_MEDICICE = "20";
    /**
     *
     */
    public static final String MASTER_FLAG_INJECTION = "40";
    /**
     *
     */
    public static final String CLASS_CODE_ID = "Claim007";	// 診療行為区分テーブルID
    /**
     *
     */
    public static final String SUBCLASS_CODE_ID = "Claim003";	// 手技、材料、薬剤区分テーブルID
    /**
     *
     */
    public static final String NUMBER_CODE_ID = "Claim004";	// 数量コードテーブルID
    /** 手技 */
    public static final int SYUGI = 0;
    /** 材料 */
    public static final int ZAIRYO = 1;
    /** 薬剤 */
    public static final int YAKUZAI = 2;
    /** 用法 */
    public static final int ADMIN = 3;
    /** 薬剤区分 内用 */
    public static final String YKZ_KBN_NAIYO = "1";
    /** 薬剤区分 注射 */
    public static final String YKZ_KBN_INJECTION = "4";
    /** 薬剤区分 外用 */
    public static final String YKZ_KBN_GAIYO = "6";
    /** レセ電算コード 内用 */
    public static final String RECEIPT_CODE_NAIYO = "210";
    /**
     *
     */
    public static final String RECEIPT_CODE_NAIYO_INNAI = "211";
    /**
     *
     */
    public static final String RECEIPT_CODE_NAIYO_INGAI = "212";
    /** レセ電算コード 頓用 */
    public static final String RECEIPT_CODE_TONYO = "220";
    /**
     *
     */
    public static final String RECEIPT_CODE_TONYO_INNAI = "221";
    /**
     *
     */
    public static final String RECEIPT_CODE_TONYO_INGAI = "222";
    /** レセ電算コード 外用*/
    public static final String RECEIPT_CODE_GAIYO = "230";
    /**
     *
     */
    public static final String RECEIPT_CODE_GAIYO_INNAI = "231";
    /**
     *
     */
    public static final String RECEIPT_CODE_GAIYO_INGAI = "232";
    /**
     *
     */
    public static final String YAKUZAI_TOYORYO = "10";     // 薬剤投与量
    /**
     *
     */
    public static final String YAKUZAI_TOYORYO_1KAI = "11";	// 薬剤投与量１回
    /**
     *
     */
    public static final String YAKUZAI_TOYORYO_1NICHI = "12";	// 薬剤投与量１日
    /**
     *
     */
    public static final String ZAIRYO_KOSU = "21";		// 材料個数
    /**
     *
     */
    public static final String INJECTION_310 = "310";
    /**
     *
     */
    public static final String INJECTION_320 = "320";
    /**
     *
     */
    public static final String INJECTION_330 = "330";
    /**
     *
     */
    public static final String INJECTION_311 = "311";
    /**
     *
     */
    public static final String INJECTION_321 = "321";
    /**
     *
     */
    public static final String INJECTION_331 = "331";
    /** 自費（課税、非課税）
     * 本来の claim では 950,960 は別の意味で
     * 割り当てられているが、ORCA 専用に 950,960 を割り当てている。
     * OSC からも 950,960 を利用してよいと回答を得ている。
     */
    public static final String JIHI_HIKAZEI = "950";
    /**
     *
     */
    public static final String JIHI_KAZEI = "960";
    /** 手技（診療行為）コードの頭番号 */
    public static final String SYUGI_CODE_START = "1";
    /** 薬剤コードの頭番号 */
    public static final String YAKUZAI_CODE_START = "6";
    /** 材料コードの頭番号 */
    public static final String ZAIRYO_CODE_START = "7";
    /** 用法コードの頭番号 */
    public static final String ADMIN_CODE_START = "001";
    /** 画像診断部位コードの頭番号 */
    public static final String RBUI_CODE_START = "002";
    /** 院内処方 */
    public static final String IN_MEDICINE = "院内処方";
    /** 院外処方 */
    public static final String EXT_MEDICINE = "院外処方";
    /** 特定器材コード */
    public static final String[] TOOL_MATERIAL_CODE_START_ARRAY = {"058", "059"};
    /** 残量廃棄コード */
    public static final String ZANRYO_HAIKI_CODE_START = "09930";
}
