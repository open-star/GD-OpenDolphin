package open.dolphin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalConstants;
import open.dolphin.utils.CombinedStringParser;
import open.dolphin.infomodel.DiagnosisOutcomeModel;
import open.dolphin.infomodel.DiagnosisCategoryModel;
import open.dolphin.infomodel.DiseaseEntry;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.log.LogWriter;
import open.dolphin.order.ClaimConst;
import open.dolphin.project.GlobalVariables;
import open.dolphin.utils.DebugDump;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

/**
 * ORCA の入力セットマスタを検索するクラス。　MEMO:DAO
 *
 * @author Minagawa, Kazushi
 */
public class SqlOrcaView extends SqlDaoBean {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final int PORT = 5432;
    private static final String DATABASE = "orca";
    private static final String USER = "orca";
    private static final String PASSWD = "";

    /**
     * Creates a new instance of SqlOrcaSetDao
     */
    public SqlOrcaView() {
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
     * @param patientId 
     * @param from
     * @param isTenki
     * @param to
     * @param ascend
     * @return 入力セットコード(OrcaInputCd)の昇順リスト
     */
    public List<RegisteredDiagnosisModel> getDiseaseFromOrca(String patientId, String from, String to, Boolean isTenki, Boolean ascend) {
        try {
//            semaphore.acquire();
            Connection con = null;
            List<RegisteredDiagnosisModel> collection = null;
            PreparedStatement pt = null;
            String sql = null;
            String ptid = null;
            int hospNum = -1;

            StringBuilder sb = new StringBuilder();
            sb.append("select ptid, ptnum from tbl_ptnum where ");
            if (GlobalVariables.getOrcaVersion().startsWith("4")) {
                hospNum = getHospNumFromOrca();
                sb.append("hospnum=? and ptnum=?");
            } else {
                sb.append("ptnum=?");
            }
            sql = sb.toString();

            try {
                con = getConnection();
                pt = con.prepareStatement(sql);

                if (hospNum > 0) {
                    pt.setInt(1, hospNum);
                    pt.setString(2, patientId);
                } else {
                    pt.setString(1, patientId);
                }

                if (GlobalSettings.isSqlDump()) {
                    DebugDump.dumpToFile("orcaviewquery1.log", pt.toString());
                }

                ResultSet rs = pt.executeQuery();
                if (rs.next()) {
                    ptid = rs.getString(1);
                }
                closeConnection(con);
                closeStatement(pt);

            } catch (Exception e) {
                processError(e);
                closeConnection(con);
                closeStatement(pt);
            }

            if (ptid == null) {
                return null;
            }

            sb = new StringBuilder();

            if (isTenki) {
                if (ascend.booleanValue()) {
                    if (hospNum > 0) {
                        sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where hospnum=?::int2 and ptid=?::int8 and tenkikbn='' and dltflg<'1' order by sryymd asc");
                    } else {
                        sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where ptid=?::int8 and tenkikbn=''and dltflg<'1' order by sryymd asc");
                    }
                } else {
                    if (hospNum > 0) {
                        sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where hospnum=?::int2 and ptid=?::int8 and tenkikbn='' and dltflg<'1' order by sryymd desc");
                    } else {
                        sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where ptid=?::int8 and tenkikbn='' and dltflg<'1' order by sryymd desc");
                    }
                }
            } else if (ascend.booleanValue()) {
                if (hospNum > 0) {
                    sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where hospnum=?::int2 and ptid=?::int8 and sryymd >= ? and sryymd <= ? and dltflg<'1' order by sryymd asc");
                } else {
                    sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where ptid=?::int8 and sryymd >= ? and sryymd <= ? and dltflg<'1' order by sryymd asc");
                }
            } else {
                if (hospNum > 0) {
                    sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where hospnum=?::int2 and ptid=?::int8 and sryymd >= ? and sryymd <= ? and dltflg<'1' order by sryymd desc");
                } else {
                    sb.append("select sryymd,khnbyomeicd,tenkikbn,tenkiymd,byomei,utagaiflg,syubyoflg from tbl_ptbyomei where ptid=?::int8 and sryymd >= ? and sryymd <= ? and dltflg<'1' order by sryymd desc");
                }
            }




            sql = sb.toString();

            try {
                con = getConnection();
                pt = con.prepareStatement(sql);
                if (isTenki) {
                    if (hospNum > 0) {
                        pt.setInt(1, hospNum);
                        pt.setString(2, ptid);
                    } else {
                        pt.setString(1, ptid);
                    }
                } else {
                    if (hospNum > 0) {
                        pt.setInt(1, hospNum);
                        pt.setString(2, ptid);
                        pt.setString(3, from);
                        pt.setString(4, to);
                    } else {
                        pt.setString(1, ptid);
                        pt.setString(2, from);
                        pt.setString(3, to);
                    }
                }

                if (GlobalSettings.isSqlDump()) {
                    DebugDump.dumpToFile("orcaviewquery2.log", pt.toString());
                }

                ResultSet rs = pt.executeQuery();
                collection = new ArrayList<RegisteredDiagnosisModel>();

                while (rs.next()) {

                    RegisteredDiagnosisModel ord = new RegisteredDiagnosisModel();

                    // 疾患開始日
                    ord.setStartDate(toDolphinDateStr(rs.getString(1)));

                    // 病名コード
                    ord.setDiagnosisCode(rs.getString(2));

                    // 転帰
                    DiagnosisOutcomeModel out = new DiagnosisOutcomeModel();
                    ord.setDiagnosisOutcomeModel(out);
                    String data = rs.getString(3);
                    out.setOutcomeDesc(toDolphinOutcome(data));

                    // 疾患終了日（転帰）
                    ord.setEndDate(toDolphinDateStr(rs.getString(4)));

                    // 疾患名
                    ord.setDiagnosis(rs.getString(5));

                    //String[] values = GlobalVariables.getStringArray("diagnosis.category");
                    String[] descs = {"主病名", "疑い病名"}; //GlobalVariables.getStringArray("diagnosis.categoryDesc");
                    String[] codeSys = {"MML0012", "MML0015"}; //GlobalVariables.getStringArray("diagnosis.categoryCodeSys");

                    DiagnosisCategoryModel dcm = new DiagnosisCategoryModel();

                    //疑い病名
                    //if ("1".equals(rs.getString(6))) {
                    //    dcm.setDiagnosisCategoryDesc(descs[1]);
                    //    dcm.setDiagnosisCategoryCodeSys(codeSys[1]);
                    //}
                    //主病名であれば上書き
                    if ("1".equals(rs.getString(7))) {
                        dcm.setDiagnosisCategoryDesc(descs[0]);
                        dcm.setDiagnosisCategoryCodeSys(codeSys[0]);
                    }
                    ord.setDiagnosisCategoryModel(dcm);

                    // 制御のための Status
                    ord.setStatus("ORCA");
                    ord.setDiagnosisCode(search(ord.getDiagnosis()));
                    collection.add(ord);
                }
                rs.close();
                closeStatement(pt);
                closeConnection(con);
                return collection;
            } catch (Exception e) {
                processError(e);
                closeConnection(con);
                closeStatement(pt);
            }
        } catch (Exception ex) {
              LogWriter.error(getClass(), ex);
   //     } finally {
            //         semaphore.release();
        }
        return null;
    }

    private String search(final String text) {

        final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
        final String master = ClaimConst.MasterSet.DIAGNOSIS.getName();
        final String sortBy = "byomeikana";
        final String order = "";

        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();// MEMO;Unused?

        List<DiseaseEntry> result = dao.getByName(master, text, true, ClaimConst.SubTypeSet.NAMES.getName(), sortBy, order);

        CombinedStringParser c = new CombinedStringParser();
        for (DiseaseEntry e : result) {
            c.add(e.getCode());
        }

        //   resultModel.setObjectList((List) result);
        return c.toCombinedString();
    }

    private String toDolphinDateStr(String orcaDate) {
        if (orcaDate != null) {
            boolean digit = true;
            for (int i = 0; i < orcaDate.length(); i++) {
                if (!Character.isDigit(orcaDate.charAt(i))) {
                    digit = false;
                    break;
                }
            }
            if (!digit) {
                return null;
            }
            if (orcaDate.length() < 7){
                return null;
            }
//            StringBuilder sb = new StringBuilder();
//            sb.append(orcaDate.substring(0, 4));
//            sb.append("-");
//            sb.append(orcaDate.substring(4, 6));
//            sb.append("-");
//            sb.append(orcaDate.substring(6));
            StringBuilder sb = new StringBuilder(orcaDate);
            sb.insert(4, "-");
            sb.insert(7, "-");

            return sb.toString();
        }
        return null;
    }

    private String toDolphinOutcome(String orcaOutcome) {
        if (orcaOutcome != null) {
            String outcomeDesc = null;
            if (orcaOutcome.equals("1")) {
                outcomeDesc = IInfoModel.ORCA_OUTCOME_RECOVERED;
            } else if (orcaOutcome.equals("2")) {
                outcomeDesc = IInfoModel.ORCA_OUTCOME_DIED;
            } else if (orcaOutcome.equals("3")) {
                outcomeDesc = IInfoModel.ORCA_OUTCOME_END;
            } else if (orcaOutcome.equals("8")) {
                outcomeDesc = IInfoModel.ORCA_OUTCOME_TRANSFERED;
            }
            return outcomeDesc;
        }
        return null;
    }
}
