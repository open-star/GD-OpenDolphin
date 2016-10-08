package open.dolphin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.BundleMed;
import open.dolphin.infomodel.BundleImage;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.IStampInfo;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.OrcaInputCd;
import open.dolphin.infomodel.OrcaInputSet;
import open.dolphin.infomodel.SinryoCode;
import open.dolphin.log.LogWriter;
import open.dolphin.order.ClaimConst;
import open.dolphin.order.MMLTable;
import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalVariables;

/**
 * ORCA の入力セットマスタを検索するクラス。　MEMO:DAO
 *
 * @author Minagawa, Kazushi
 */
public class SqlOrcaSetDao extends SqlDaoBean {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final int PORT = 5432;
    private static final String DATABASE = "orca";
    private static final String USER = "orca";
    private static final String PASSWD = "";
    private static final String S_SET = "S";
    private static final String P_SET = "P";
    private static final String RP_KBN_START = "2";
    private static final String SHINRYO_KBN_START = ".";
    private static final int SHINRYO_KBN_LENGTH = 3;
    private static final String KBN_RP = "220";
    private static final String KBN_RAD = "700";
    private static final String KBN_GENERAL = "999";

    /** 
     * Creates a new instance of SqlOrcaSetDao 
     */
    public SqlOrcaSetDao() {
        super();
        this.setDriver(DRIVER);
        this.setHost(GlobalVariables.getClaimAddress());
        
        if (GlobalSettings.isTrial()) {
            this.setPort(10329);
        } else {
            this.setPort(PORT);
        }

        this.setDatabase(DATABASE);
        this.setUser(USER);
        this.setPasswd(PASSWD);
    }

    /**
     * ORCA の入力セットコード（約束処方、診療セット）を返す。
     * @return 入力セットコード(OrcaInputCd)の昇順リスト
     */
    public List<OrcaInputCd> getInputSetFromOrca() {
    //    try {
            Connection con = null;
            List<OrcaInputCd> collection = null;
            Statement st = null;
            String sql = null;

            StringBuilder sb = new StringBuilder();
            sb.append("select * from tbl_inputcd where ");
            if (GlobalVariables.getOrcaVersion().startsWith("4")) {
                int hospnum = getHospNumFromOrca();
                sb.append("hospnum=");
                sb.append(hospnum);
                sb.append(" and ");
            }
            sb.append("inputcd like 'P%' or inputcd like 'S%' order by inputcd");

            sql = sb.toString();

            boolean v4 = GlobalVariables.getOrcaVersion().startsWith("4") ? true : false;

            try {
                con = getConnection();
                st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                collection = new ArrayList<OrcaInputCd>();

                while (rs.next()) {

                    OrcaInputCd inputCd = new OrcaInputCd();

                    if (!v4) {
                        inputCd.setHospId(rs.getString(1));
                        inputCd.setCdsyu(rs.getString(2));
                        inputCd.setInputCd(rs.getString(3));
                        inputCd.setSryKbn(rs.getString(4));
                        inputCd.setSryCd(rs.getString(5));
                        inputCd.setDspSeq(rs.getInt(6));
                        inputCd.setDspName(rs.getString(7));
                        inputCd.setTermId(rs.getString(8));
                        inputCd.setOpId(rs.getString(9));
                        inputCd.setCreYmd(rs.getString(10));
                        inputCd.setUpYmd(rs.getString(11));
                        inputCd.setUpHms(rs.getString(12));

                        String cd = inputCd.getInputCd();
                        if (cd.length() > 6) {
                            cd = cd.substring(0, 6);
                            inputCd.setInputCd(cd);
                        }

                    } else {
                        inputCd.setCdsyu(rs.getString(1));
                        inputCd.setInputCd(rs.getString(2));
                        inputCd.setSryKbn(rs.getString(3));
                        inputCd.setSryCd(rs.getString(4));
                        inputCd.setDspSeq(rs.getInt(5));
                        inputCd.setDspName(rs.getString(6));
                        inputCd.setTermId(rs.getString(7));
                        inputCd.setOpId(rs.getString(8));
                        inputCd.setCreYmd(rs.getString(9));
                        inputCd.setUpYmd(rs.getString(10));
                        inputCd.setUpHms(rs.getString(11));

                        String cd = inputCd.getInputCd();
                        if (cd.length() > 6) {
                            cd = cd.substring(0, 6);
                            inputCd.setInputCd(cd);
                        }
                        ModuleInfoBean info = inputCd.getStampInfo();// MEMO;Unused?
                    }
                    collection.add(inputCd);
                }
                rs.close();
                closeStatement(st);
                closeConnection(con);
                return collection;
            } catch (Exception e) {
                processError(e);
                closeConnection(con);
                closeStatement(st);
            }
   //     } finally {
    //    }
        return null;
    }

    /**
     * 指定された入力セットコードから診療セットを Stamp にして返す。
     * @param inputSetInfo 入力セットの StampInfo
     * @return 入力セットのStampリスト
     */
    public List<ModuleModel> getStampFromOrca(ModuleInfoBean inputSetInfo) {
        List<ModuleModel> result = new ArrayList<ModuleModel>();
   //     try {
//            semaphore.acquire();
            String setCd = inputSetInfo.getStampId();
            String stampName = inputSetInfo.getStampName();

            int hospnum = -1;
            if (GlobalVariables.getOrcaVersion().startsWith("4")) {
                hospnum = getHospNumFromOrca();
            }

            Connection con = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            String sql1 = null;
            String sql2 = null;

            StringBuilder sb1 = new StringBuilder();
            if (GlobalVariables.getOrcaVersion().startsWith("4")) {
                sb1.append("select inputcd,suryo1,suryo2,kaisu from tbl_inputset where hospnum=? and setcd=? and yukoedymd='99999999' order by setseq");
                sql1 = sb1.toString();
            } else {
                sb1.append("select inputcd,suryo1,suryo2,kaisu from tbl_inputset where setcd=? and yukoedymd='99999999' order by setseq");
                sql1 = sb1.toString();
            }

            StringBuilder sb2 = new StringBuilder("select srysyukbn,name,taniname,ykzkbn,sstkijuncd1,sstkijuncd2,sstkijuncd3,sstkijuncd4,sstkijuncd5,sstkijuncd6,sstkijuncd7,sstkijuncd8,sstkijuncd9,sstkijuncd10 from tbl_tensu where ");
            if (GlobalVariables.getOrcaVersion().startsWith("4")) {
                sb2.append("hospnum=? and srycd=? and yukoedymd='99999999'");
            } else {
                sb2.append("srycd=? and yukoedymd='99999999'");
            }
            sql2 = sb2.toString();

            try {
                // setCd を検索する
                con = getConnection();
                ps1 = con.prepareStatement(sql1);
                if (hospnum > 0) {
                    ps1.setInt(1, hospnum);
                    ps1.setString(2, setCd);
                } else {
                    ps1.setString(1, setCd);
                }
                //        debug(ps1.toString());

                ResultSet rs = ps1.executeQuery();
                List<OrcaInputSet> list = new ArrayList<OrcaInputSet>();
                while (rs.next()) {
                    OrcaInputSet inputSet = new OrcaInputSet();
                    inputSet.setInputCd(rs.getString(1));       // .210 616130532 ...
                    inputSet.setSuryo1(rs.getFloat(2));         // item の個数
                    inputSet.setSuryo2(rs.getFloat(3));
                    inputSet.setKaisu(rs.getInt(4));            // バンドル数
                    list.add(inputSet);
                }

                rs.close();
                closeStatement(ps1);
                ModuleModel stamp = null;
                BundleDolphin bundle = null;
                ps2 = con.prepareStatement(sql2);
                if (list != null && list.size() > 0) {
                    for (OrcaInputSet inputSet : list) {
                        String inputcd = inputSet.getInputCd();
                        if (inputcd.startsWith(SHINRYO_KBN_START)) {
                            stamp = createStamp(stampName, inputcd);
                            if (stamp != null) {
                                bundle = (BundleDolphin) stamp.getModel();
                                result.add(stamp);
                            }
                        } else {
                            if (hospnum > 0) {
                                ps2.setInt(1, hospnum);
                                ps2.setString(2, inputcd);
                            } else {
                                ps2.setString(1, inputcd);
                            }
                            ResultSet rs2 = ps2.executeQuery();
                            if (rs2.next()) {
                                String code = inputcd;
                                String kbn = rs2.getString(1);
                                String name = rs2.getString(2);
                                String number = String.valueOf(inputSet.getSuryo1());
                                String unit = rs2.getString(3);
                                String ykzkbn = rs2.getString(4);

                                ClaimItem item = new ClaimItem(code, number);
                                //   item.setCode(code);
                                item.setName(name);
                                item.setSuryo1(inputSet.getSuryo1());
                                item.setSuryo2(inputSet.getSuryo2());
                                //   item.setNumber(number);
                                item.setClassCodeSystem(ClaimConst.SUBCLASS_CODE_ID);

                                if (code.startsWith(ClaimConst.SYUGI_CODE_START)) {

                                    // 手技の場合
                                    item.setClassCode(String.valueOf(ClaimConst.SYUGI));

                                    if (bundle == null) {
                                        stamp = createStamp(stampName, kbn);
                                        if (stamp != null) {
                                            bundle = (BundleDolphin) stamp.getModel();
                                            result.add(stamp);
                                        }
                                    }

                                    if (bundle != null) {
                                        bundle.setBundleNumber(inputSet.getKaisu());
                                        bundle.addClaimItem(item);
                                    }

                                } else if (code.startsWith(ClaimConst.YAKUZAI_CODE_START)) {

                                    // 薬剤の場合
                                    item.setClassCode(String.valueOf(ClaimConst.YAKUZAI));
                                    item.setNumberCode(ClaimConst.YAKUZAI_TOYORYO);
                                    item.setNumberCodeSystem(ClaimConst.NUMBER_CODE_ID);
                                    item.setUnit(unit);
                                    item.setYkzKbn(ykzkbn);
                                    if (bundle == null) {
                                        String receiptCode = rs2.getString(4).equals(ClaimConst.YKZ_KBN_NAIYO)
                                                ? ClaimConst.RECEIPT_CODE_NAIYO
                                                : ClaimConst.RECEIPT_CODE_GAIYO;
                                        stamp = createStamp(stampName, receiptCode);
                                        if (stamp != null) {
                                            bundle = (BundleDolphin) stamp.getModel();
                                            result.add(stamp);
                                        }
                                    }

                                    if (bundle != null) {
                                        bundle.setBundleNumber(inputSet.getKaisu());
                                        bundle.addClaimItem(item);
                                    }

                                } else if (code.startsWith(ClaimConst.ZAIRYO_CODE_START)) {

                                    // 材料の場合
                                    item.setClassCode(String.valueOf(ClaimConst.ZAIRYO));
                                    item.setNumberCode(ClaimConst.ZAIRYO_KOSU);
                                    item.setNumberCodeSystem(ClaimConst.NUMBER_CODE_ID);
                                    item.setUnit(unit);
                                    if (bundle == null) {
                                        stamp = createStamp(stampName, KBN_GENERAL);
                                        if (stamp != null) {
                                            bundle = (BundleDolphin) stamp.getModel();
                                            result.add(stamp);
                                        }
                                    }

                                    if (bundle != null) {
                                        bundle.setBundleNumber(inputSet.getKaisu());
                                        bundle.addClaimItem(item);
                                    }

                                } else if (code.startsWith(ClaimConst.ADMIN_CODE_START)) {

                                    // 用法の場合
                                    if (bundle == null) {
                                        stamp = createStamp(stampName, KBN_RP);
                                        if (stamp != null) {
                                            bundle = (BundleDolphin) stamp.getModel();
                                            result.add(stamp);
                                        }
                                    }

                                    if (bundle != null) {
                                        bundle.setAdmin(name);
                                        bundle.setAdminCode(code);
                                        bundle.setBundleNumber(inputSet.getKaisu());
                                    }

                                } else if (inputcd.startsWith(ClaimConst.RBUI_CODE_START)) {

                                    // 画像診断部位の場合
                                    item.setClassCode(String.valueOf(ClaimConst.SYUGI));
                                    if (bundle == null) {
                                        stamp = createStamp(stampName, KBN_RAD);
                                        if (stamp != null) {
                                            bundle = (BundleDolphin) stamp.getModel();
                                            result.add(stamp);
                                        }
                                    }

                                    if (bundle != null) {
                                        bundle.addClaimItem(item);
                                    }

                                } else if (SinryoCode.isComment(code)) {

                                    item.setClassCode(String.valueOf(ClaimConst.SYUGI));
                                    List ary = new ArrayList();
                                    for (int i = 0; i < 10; i++) {
                                        ary.add(rs2.getString(i + 5));
                                    }
                                    item.setSstKijunCdSet(ary);
                                    item.setNumber("");
                                    if (bundle == null) {
                                        stamp = createStamp(stampName, kbn);
                                        if (stamp != null) {
                                            bundle = (BundleDolphin) stamp.getModel();
                                            result.add(stamp);
                                        }
                                    }

                                    if (bundle != null) {
                                        bundle.addClaimItem(item);
                                    }

                                } else if (SinryoCode.isJihi(code) || SinryoCode.isZanryoHaiki(code)) {

                                    item.setClassCode(String.valueOf(ClaimConst.SYUGI));
                                    if (bundle == null) {
                                        stamp = createStamp(stampName, KBN_GENERAL);
                                        if (stamp != null) {
                                            bundle = (BundleDolphin) stamp.getModel();
                                            result.add(stamp);
                                        }
                                    }

                                    if (bundle != null) {
                                        bundle.addClaimItem(item);
                                    }

                                }
                            }
                        }
                    }
                    closeStatement(ps2);
                }
                closeConnection(con);
            } catch (Exception e) {
                processError(e);
                closeConnection(con);
                closeStatement(ps1);
                closeStatement(ps2);
            }
            //    } catch (InterruptedException ex) {
    //    } finally {
            //         semaphore.release();
   //     }
        return result;
    }

    /**
     * Stampを生成する。
     * @param stampName Stamp名
     * @param code 診療区分コード
     * @return Stamp
     */
    public ModuleModel createStamp(String stampName, String code) {
        ModuleModel result = null;
  //      try {
            //         semaphore.acquire();
            if (code != null) {
                if (code.startsWith(SHINRYO_KBN_START)) {
                    code = code.substring(1);
                }
                if (code.length() > SHINRYO_KBN_LENGTH) {
                    code = code.substring(0, SHINRYO_KBN_LENGTH);
                }
                result = new ModuleModel();
                IStampInfo stampInfo = result.getModuleInfo();
                stampInfo.setStampName(stampName);
                stampInfo.setStampRole(IInfoModel.ROLE_P);
                BundleDolphin bundle = null;
                if (code.startsWith(RP_KBN_START)) {
                    bundle = new BundleMed();
                    result.setModel(bundle);
                    String inOut = GlobalVariables.getPreferences().getBoolean(GlobalVariables.RP_OUT, true) ? ClaimConst.EXT_MEDICINE : ClaimConst.IN_MEDICINE;
                    bundle.setMemo(inOut);
                } else if (code.equals(KBN_RAD)) {
                    bundle = new BundleImage();
                    result.setModel(bundle);
                } else {
                    bundle = new BundleDolphin();
                    result.setModel(bundle);
                }
                bundle.setClassCode(code);
                bundle.setClassCodeSystem(ClaimConst.CLASS_CODE_ID);
                bundle.setClassName(MMLTable.getClaimClassCodeName(code));
                String[] entityOrder = getEntityOrderName(code);
                if (entityOrder != null) {
                    stampInfo.setEntity(entityOrder[0]);
                    bundle.setOrderName(entityOrder[1]);
                }
            }
            //     } catch (InterruptedException ex) {
    //    } finally {
            //           semaphore.release();
     //   }
        return result;
    }

    private String[] getEntityOrderName(String receiptCode) {
        try {
            int number = Integer.parseInt(receiptCode);
            if (number >= 110 && number <= 125) {
                return new String[]{IInfoModel.ENTITY_BASE_CHARGE_ORDER, "診断料"};
            } else if (number >= 130 && number <= 139) {
                return new String[]{IInfoModel.ENTITY_INSTRACTION_CHARGE_ORDER, "指導"};
            } else if (number >= 140 && number <= 149) {
                return new String[]{IInfoModel.ENTITY_STAY_ON_HOME_CHARGE_ORDER, "在宅"};
            } else if (number >= 200 && number <= 299) {
                return new String[]{IInfoModel.ENTITY_MED_ORDER, "RP"};
            } else if (number >= 300 && number <= 331) {
                return new String[]{IInfoModel.ENTITY_INJECTION_ORDER, "注 射"};
            } else if (number >= 400 && number <= 499) {
                return new String[]{IInfoModel.ENTITY_TREATMENT, "処 置"};
            } else if (number >= 500 && number <= 599) {
                return new String[]{IInfoModel.ENTITY_SURGERY_ORDER, "手術"};
            } else if (number >= 600 && number <= 699) {
                return new String[]{IInfoModel.ENTITY_LABO_TEST, "検査"};
            } else if (number >= 700 && number <= 799) {
                return new String[]{IInfoModel.ENTITY_RADIOLOGY_ORDER, "画像診断"};
            } else if (number >= 800 && number <= 899) {
                return new String[]{IInfoModel.ENTITY_OTHER_ORDER, "その他"};
            } else {
                return new String[]{IInfoModel.ENTITY_GENERAL_ORDER, "汎 用"};
            }
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
        return null;
    }
}
