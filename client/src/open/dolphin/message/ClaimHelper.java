package open.dolphin.message;

import java.util.Set;

import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.order.OutOfServiceTable;
import open.dolphin.project.GlobalVariables;

/**
 * ClaimHelper
 *
 * @author Minagawa,Kazushi
 *
 */
public class ClaimHelper {

    /** 確定日 */
    private String confirmDate;
    /** Creator ID */
    private String creatorId;
    /** Creator 名 */
    private String creatorName;
    /** 診療科コード */
    private String creatorDept;
    /** 診療科名 */
    private String creatorDeptDesc;
    /** 医療資格 */
    private String creatorLicense;
    /** 患者ID */
    private String patientId;
    /** 生成目的 */
    private String generationPurpose;
    /** 文書ID */
    private String docId;
    /** 健康保険 GUID */
    private String healthInsuranceGUID;
    /** 健康保険コード値 */
    private String healthInsuranceClassCode;
    /** 健康保険説明 */
    private String healthInsuranceDesc;
    private String timeClass;
    /** ClaimBundle 配列 */
    private ClaimBundle[] claimBundle;
    private String deptName;
    private String deptCode;
    private String doctorName;
    private String doctorId;
    private String jmariCode;
    private String facilityName;
    private boolean admitFlag;

    /*
    Claim001    時間外区分

    Value   	Description 		解説
    0       	時間内
    1       	時間外
    2       	休日
    3       	深夜

    Claim002	診療行為区分コード

    Value		Description		解説
    110		初診料                  医科点数表の解釈第１章 Ａ０００「初診料」の各項目
    120		再診料                  医科点数表の解釈第１章 Ａ００１「再診料」 及び Ａ００１－２「特定機能病院外来診療料」の各項目
    130		指導料                  医科点数表の解釈第２章第１部「指導管理等」の各項目
    140		在宅料                  医科点数表の解釈第２章第２部「在宅医療」の各項目

    210			内服				医科点数表の解釈第２章第５部第１節「調剤料」及び第３節「薬剤料」の内服に関する項目。 但し麻薬、毒薬に関する調剤料の項目は除く。
    220			頓服				医科点数表の解釈第２章第５部第１節「調剤料」及び第３節「薬剤料」の頓服に関する項目。 但し麻薬、毒薬に関する調剤料の項目は除く。
    230			外用				医科点数表の解釈第２章第５部第１節「調剤料」及び第３節「薬剤料」の外用に関する項目。 但し麻薬、毒薬に関する調剤料の項目は除く。
    240			処方料				医科点数表の解釈第２章第５部第２節「処方料」の各項目 但し麻薬、毒薬に関する項目は除く。
    250			麻薬，毒薬加算 		医科点数表の解釈第２章第５部第１節「調剤料」 及び 第２節「処方料」の麻薬、毒薬に関連する項目
    260			調剤技術基本料 		医科点数表の解釈第２章第５部第６節「調剤技術基本料」の各項目
    270			処方箋料 			医科点数表の解釈第２章第５部第５節「処方せん料」の各項目

    310			皮下筋注 			医科点数表の解釈第２章第６部第１節「注射料」 の Ｇ０００ 「皮下、筋肉内注射」 及び 使用した第 ２節「薬剤料」の各項目
    320			静注 				医科点数表の解釈第２章第６部第１節「注射料」 の Ｇ００１ 「静脈内注射」 及び 使用した 第２節「薬剤料」の各項目
    330			その他注射 			医科点数表の解釈第２章第６部第１節「注射料」 の Ｇ０００、Ｇ００１ 以外 及び 使用した 第２節
    340			自己注射 			医科点数表の解釈第２章第６部（注射薬品の投与）にあるインシュリン製剤等の自己注射薬剤

    410			処置料 				医科点数表の解釈第２章第９部第１節「処置料」の各項目
    480			処置材料 			医科点数表の解釈第２章第９部第３節「特定保険医療材料料」の各項目
    490			処置薬剤 			医科点数表の解釈第２章第９部第２節「薬剤料」の各項目

    510			手術料 				医科点数表の解釈第２章第１０部第１節の各項目
    520			輸血料 				医科点数表の解釈第２章第１０部第２節の各項目
    530			ギプス料 				医科点数表の解釈第２章第１０部第３節の各項目
    540			麻酔料 				医科点数表の解釈第２章第１１部第１節と第２節の各項目
    580			手術材料 			医科点数表の解釈第２章第１０部第５節の各項目 及び 第１１部第４節の各項目
    590			手術薬剤 			医科点数表の解釈第２章第１０部第４節の各項目 及び 第１１部第３節の各項目

    610			検体検査料 			医科点数表の解釈第２章第３部第１節「検体検査料」の各項目
    620			病理学的検査料 		医科点数表の解釈第２章第３部第２節「病理学的検査料」の各項目
    630			生体検査料 			医科点数表の解釈第２章第３部第３節「生体検査料」の各項目
    640			その他検査料 			医科点数表の解釈第２章第３部第４節「診断穿刺・検体採取料」の各項目
    680			検査材料 			医科点数表の解釈第２章第３部第６節「特定保険医療材料料」の各項目
    690			検査薬剤 			医科点数表の解釈第２章第３部第５節「薬剤料」の各項目

    710			Ｘ線診断料 			医科点数表の解釈第２章第４部第１節「エックス線診断料」の各項目 但し、同節 Ｅ００３ 造影剤注入手技 は除く
    720			核医学診断料 		医科点数表の解釈第２章第４部第２節「核医学診断料」の各項目
    730			コンピュータ断層診断料 	医科点数表の解釈第２章第４部第３節「コンピュータ断層診断料」の各項目
    740			手技料その他 			医科点数表の解釈第２章第４部第１節「エックス線診断料」の Ｅ００３ 造影剤注入手技
    780			Ｘ線材料 			医科点数表の解釈第２章第４部第５節「特定保険医療材料料」の各項目
    790			Ｘ線薬剤 			医科点数表の解釈第２章第４部第４節「薬剤料」の各項目

    810			理学療法 			医科点数表の解釈第２章第７部「リハビリテーション」の各項目
    820			精神療法 			医科点数表の解釈第２章第８部「精神科専門療法料」の各項目
    830			放射線治療料 		医科点数表の解釈第２章第１２部「放射線治療」の各項目
    840			その他 				その他

    910			室料 				医科点数表の解釈第１章 Ａ００２「入院料」の入院環境料
    920			看護料 				医科点数表の解釈第１章 Ａ００２「入院料」の看護料
    930			給食料 				食事療養負担金
    940			医学管理料 			医科点数表の解釈第１章 Ａ００３「入院時医学管理料」の各項目
    950			入院時一部負担金 	入院時一部負担金
    951			薬剤一部負担金 		薬剤一部負担金
    952			食餌一部負担金 		食餌一部負担金
    953			その他一部負担金 		その他一部負担金
    960			室料差額 			室料差額
    970			重症者加算 			医科点数表の解釈第１章 Ａ００２「入院料」の重症者加算
    980			ICU加算 			医科点数表の解釈第１章 Ａ００４「特定入院料」の各項目
    990			入院料その他（病衣貸与料など） 	その他

    11			分娩介助料 	　
    12			文書料 	　
    13			容器代 	　
    14			自費診療 	　
    15			高度先進医療 	　
    19			その他の自費 	　

    Claim003 診療種別区分
    Value		Description			解説
    0		手技
    1		材料
    2		薬剤

    Claim004 数量コード
    Value		Description			解説
    10		薬剤投与量
    11		薬剤投与量（1回）
    12		薬剤投与量（1日）
    21		材料個数                        材料個数，本数など
    22		材料単価（円）                  claim:unitに記載される単位量あたりの値段
    30		医療用ガス投与量

    Claim005 フイルムサイズコード
    Value		Description			解説
    1		八切
    2		六切
    3		四切
    4		大四切
    5		大角
    6		半切
    7		半切（コンピュータ断層撮影用）

    Claim006 用法コード
    外用
    Value		Description			解説
    G001 		医師の指示通り 	　
    G002 		「　　　　　　」使用 	　
    G003 		就寝前使用 	　
    G004 		朝使用 	　
    G005 		朝・昼使用 	　
    G006 		朝・夕使用 	　
    G007 		夕・就寝前使用 	　
    G008 		朝・昼・夕使用 	　
    G009 		朝・昼・夕・就寝前使用 	　
    G010 		朝・夕・就寝前使用 	　
    G011 		混合 	　
    G012 		１日１回使用 	　
    G013 		１日２回使用 	　
    G014 		１日３回使用 	　
    G015 		１日４回使用 	　
    G016 		１日５回使用 	　
    G017 		適宜使用 	　
    G018 		くすり変更有り 	　
    G020 		隔日使用
    　
    内服
    Value		Description			解説
    J1 		分１、適宜服用 	　
    J100 		分１、「　　　」服用 	　
    J11 		分１、頓用 	　
    J111 		分１、朝食後服用 	　
    J112 		分１、朝食前服用 	　
    J113 		分１、昼食後服用 	　
    J114 		分１、昼食前服用 	　
    J131 		分１、夕食後服用 	　
    J132 		分１、夕食前服用 	　
    J18 		分１、就寝前服用 	　
    J19 		分１、起床時服用 	　
    J2 		分２、１２時間毎服用 	　
    J20 		分２、適宜服用 	　
    J200 		分２、「　　　　　　　　　　　　」服用 	　
    J204 		分２、起床時・就寝前服用 	　
    J21 		分２、朝２錠・昼１錠服用 	　
    J211 		分２、朝・昼食後服用 	　
    J212 		分２、朝・昼食前服用 	　
    J221 		分２、朝・夕食後服用 	　
    J222 		分２、朝・夕食前服用 	　
    J223 		分２、朝食前・夕食後服用 	　
    J231 		分２、朝食後・就寝前服用 	　
    J232 		分２、朝食前・就寝前服用 	　
    J233 		分２、起床時・夕食後服用 	　
    J234 		分２、夕食後・就寝前 	　
    J3 		分３、８時間毎服用 	　
    J30 		分３、適宜服用 	　
    J300 		分３、「　　　　　　　　　　　　」服用 	　
    J3034 		分３、起床時・夕食後・就寝前服用 	　
    J311 		分３、毎食後服用 	　
    J312 		分３、毎食前服用 	　
    J313 		分３、朝食前・昼夕食後服用 	　
    J314 		分３、朝食後・夕食後・就寝前服用 	　
    J33 		分３、毎食間服用 	　
    J4 		分４、６時間毎服用 	　
    J40 		分４、適宜服用 	　
    J400 		分４、「　　　　　　　　　　　　」服用 	　
    J404 		分４、早朝空腹時・毎食間・就寝前服用 	　
    J412 		分４、毎食前・就寝前服用 	　
    J413 		分４、毎食後・就寝前服用 	　
    J5 		分５、毎食前・午後３時・就寝前服用 	　
    J500 		分１、朝 	　
    J501 		分２、朝・昼 	　
    J502 		分２、朝・夕 	　
    J503 		分２、夕・就寝前 	　
    J504 		分３、朝・昼・夕 	　
    J505 		分４、朝・昼・夕・就寝前 	　

    臨時薬剤
    Value		Description			解説
    T11 		頓用 	　
    T18 		発熱時服用 	　
    T19 		疼痛時服用 	　
    T20 		発作時服用 	　
    T21 		嘔吐時服用 	　
    T22 		便秘時服用 	　
    T23 		不眠時服用 	　
    T24 		腹痛時服用 	　
    T25 		頭痛時服用 	　
    T26 		不安時服用 	　
    T27 		下痢時服用 	　
    T28 		咳がひどい時服用 	　
    T29 		（頓服）分１、朝食後服用 	　
    T30 		（頓服）分２、朝・夕食後服用 	　
    T31 		高血圧時服用 	　
    T32 		胸やけのする時服用 	　
    T33 		食欲不振時服用 	　
    T34 		いらいらする時服用 	　
    T35 		めまいのする時服用 	　
    T36 		ふらつきの強い時服用 	　
    T37 		かゆい時に服用 	　
    T38 		嘔気時服用 	　
    T39 		頻尿時服用 	　
    T40 		夜半不眠時服用 	　
    T41 		「　　　　　　　　　　　　　　　」時服用 	　


    Claim007 レセ電算診療行為区

    コード          点数集計先識別（入院） 	点数集計先識別（入院外）
    区分            医　　　　科
    000		未使用
    110		初診
    120                                         再診（再診）
    122                                         再診（外来管理加算）
    123                                         再診（時間外）
    124                                         再診（休日）
    125                                         再診（深夜）
    130		指導 	　
    140		在宅 	　
    210                                         投薬（内服・屯服・調剤）（入院外）
    230                                         投薬（外用・調剤）（入院外）
    240		投薬（調剤）（入院） 	　
    250                                         投薬（処方）
    260		投薬（麻毒） 	　
    270		投薬（調基） 	　
    300		注射（生物学的製剤・精密持続点滴・麻薬） 	　
    311                                         注射（皮下筋肉内）
    321                                         注射（静脈内）
    331		注射（その他）
    400		処置
    500		手術（手術）
    502		手術（輸血）
    503		手術（ギプス）
    540		麻酔
    600		検査
    700		画像診断
    800		その他
    903		入院（入院料） 	　
    906		入院（外泊） 	　
    910		入院（入院時医学管理料） 	　
    920		入院（特定入院料・その他） 	　
    970		食事（食事療養） 	　
    971		食事（標準負担額） 	　


    Claim008 	状態
    Value		Description		解説
    appoint 	予約
    regist 		受付
    perform 	実施
    account 	会計終了


    Claim009 	予約
    Value		Description		解説
    consult 	診察
    doctor 		診察（医師指定） 	具体的な医師名をmemoに入れる
    rehabilitation 	リハビリ
    medication 	くすり
    injection 	注射
    test 		検査
    bloodTest 	血液検査
    radTest 	レントゲン
    treatment 	処置
    urgent 		急ぎ
    nextConsult 	次回受診指定            “来春”等の自由文をmemoに入れる
     */
    /**
     *
     * @param sendModel
     * @param admitFlag
     */
    public ClaimHelper(DocumentModel sendModel, boolean admitFlag) {
        DocInfoModel docInfo = sendModel.getDocInfo();

        // ヘルパークラスを生成しVelocityが使用するためのパラメータを設定する

        Set<ModuleModel> modules = sendModel.getModules();


        String confirmedStr = ModelUtils.getDateTimeAsString(docInfo.getConfirmDate());
        setConfirmDate(confirmedStr);

        setCreatorDeptDesc(docInfo.getDepartmentName());
        setCreatorDept(docInfo.getDepartmentCode());

        String _doctorName = docInfo.getAssignedDoctorName();
        if (_doctorName == null) {
            _doctorName = GlobalVariables.getUserModel().getCommonName();
        }
        setCreatorName(_doctorName);

        String _doctorId = docInfo.getAssignedDoctorId();
        if (_doctorId == null) {
            _doctorId = GlobalVariables.getUserModel().getUserId();
        }
        setCreatorId(_doctorId);

        String _jamriCode = docInfo.getJMARICode();
        if (_jamriCode == null) {
            _jamriCode = GlobalVariables.getJMARICode();
        }

        setJmariCode(_jamriCode);

        setCreatorId(sendModel.getCreator().getUserId());
        setCreatorName(sendModel.getCreator().getCommonName());


        setCreatorDept(docInfo.getDepartment());
        setCreatorDeptDesc(docInfo.getDepartmentDesc());


        setCreatorLicense(sendModel.getCreator().getLicenseModel().getLicense());

        setGenerationPurpose(docInfo.getPurpose());
        setDocId(docInfo.getDocId());
        setHealthInsuranceGUID(docInfo.getHealthInsuranceGUID());
        setHealthInsuranceClassCode(docInfo.getHealthInsurance());
        setHealthInsuranceDesc(docInfo.getHealthInsuranceDesc());

        setFacilityName(GlobalVariables.getUserModel().getFacility().getFacilityName());
        setPatientId(sendModel.getKarte().getPatient().getPatientId());

        setTimeClass("");

        setAdmitFlag(admitFlag);
    }

    /**
     * TimeClassがあるかどうかを返す。
     *
     * @return TimeClassががある時 true
     */
    public Boolean hasTimeClass() {
        if (timeClass != null) {
            return (!timeClass.equals(""));
        }
        return false;
    }

    /**
     *
     * @param timeClass
     */
    public void setTimeClass(String timeClass) {
        this.timeClass = timeClass;
    }

    /**
     *
     * @return
     */
    public String getTimeClass() {
        return timeClass;
    }

    /**
     *
     * @param confirmDate
     */
    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
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
     * @param creatorId
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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
     * @param creatorName
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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
     * @param creatorLicense
     */
    public void setCreatorLicense(String creatorLicense) {
        this.creatorLicense = creatorLicense;
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
     * @param patientId
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
     * @param generationPurpose
     */
    public void setGenerationPurpose(String generationPurpose) {
        this.generationPurpose = generationPurpose;
    }

    /**
     *
     * @return
     */
    public String getGenerationPurpose() {
        return generationPurpose;
    }

    /**
     *
     * @param docId
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     *
     * @return ドキュメントID
     */
    public String getDocId() {
        return docId;
    }

    /**
     *
     * @param healthInsuranceGUID
     */
    public void setHealthInsuranceGUID(String healthInsuranceGUID) {
        this.healthInsuranceGUID = healthInsuranceGUID;
    }

    /**
     *
     * @return
     */
    public String getHealthInsuranceGUID() {
        return healthInsuranceGUID;
    }

    /**
     *
     * @param healthInsuranceClassCode
     */
    public void setHealthInsuranceClassCode(String healthInsuranceClassCode) {
        this.healthInsuranceClassCode = healthInsuranceClassCode;
    }

    /**
     *
     * @return　
     */
    public String getHealthInsuranceClassCode() {
        return healthInsuranceClassCode;
    }

    /**
     *
     * @param healthInsuranceDesc
     */
    public void setHealthInsuranceDesc(String healthInsuranceDesc) {
        this.healthInsuranceDesc = healthInsuranceDesc;
    }

    /**
     *
     * @return
     */
    public String getHealthInsuranceDesc() {
        return healthInsuranceDesc;
    }

    /**
     *
     * @param claimBundle
     */
    public void setClaimBundle(ClaimBundle[] claimBundle) {
        this.claimBundle = claimBundle;
    }

    /**
     *
     * @return
     */
    public ClaimBundle[] getClaimBundle() {
        return claimBundle;
    }

    /**
     *
     * @param val
     */
    public void addClaimBundle(ClaimBundle val) {
        if (claimBundle == null) {
            claimBundle = new ClaimBundle[1];
            claimBundle[0] = val;
        } else {
            int len = claimBundle.length;
            ClaimBundle[] dest = new ClaimBundle[len + 1];
            System.arraycopy(claimBundle, 0, dest, 0, len);
            claimBundle = dest;
            claimBundle[len] = val;
        }
        //小児科夜間診療が加えられた場合、BundleにTimeClassを付加する。
        ClaimItem[] claims = val.getClaimItem();
        OutOfServiceTable outOfService = new OutOfServiceTable();
        for (ClaimItem claim : claims) {
            String code = outOfService.timeClass(claim.getCode());
            if (code != null) {
                setTimeClass(code);
                break;
            }
        }

        //麻酔がある場合、Bundleのclaim:classCodeを麻酔のコードに変更する。
        for (ClaimItem claim : claims) {
            if (claim.getClassCode().equals("540")) {
                val.setClassCode("540");
                break;
            }
        }
    }

    /**
     *
     * @return
     */
    public String getCreatorDept() {
        return creatorDept;
    }

    /**
     *
     * @param creatorDept
     */
    public void setCreatorDept(String creatorDept) {
        this.creatorDept = creatorDept;
    }

    /**
     *
     * @return
     */
    public String getCreatorDeptDesc() {
        return creatorDeptDesc;
    }

    /**
     *
     * @param creatorDeptDesc
     */
    public void setCreatorDeptDesc(String creatorDeptDesc) {
        this.creatorDeptDesc = creatorDeptDesc;
    }

    /**
     *
     * @return　科
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     *
     * @param deptName　: 科
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     *
     * @return 科コード
     */
    public String getDeptCode() {
        return deptCode;
    }

    /**
     *
     * @param deptCode　科コード
     */
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    /**
     *
     * @return　医師名
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     *
     * @param doctorName　医師名
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    /**
     *
     * @return　医師ID
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     *
     * @param doctorId 医師ID
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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
    public void setJmariCode(String jmariCode) {
        this.jmariCode = jmariCode;
    }

    /**
     *
     * @return　施設名
     */
    public String getFacilityName() {
        return facilityName;
    }

    /**
     *
     * @param facilityName
     */
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    /**
     *
     * @return
     */
    public boolean getAdmitFlag() {
        return admitFlag;
    }

    /**
     *
     * @param admitFlag
     */
    public void setAdmitFlag(boolean admitFlag) {
        this.admitFlag = admitFlag;
    }
}
