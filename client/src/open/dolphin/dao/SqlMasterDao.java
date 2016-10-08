package open.dolphin.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import open.dolphin.container.Pair;
import open.dolphin.infomodel.AdminEntry;

import open.dolphin.utils.StringTool;
import open.dolphin.infomodel.GenericAdapter;

import open.dolphin.infomodel.DiseaseEntry;
import open.dolphin.infomodel.InteractEntry;
import open.dolphin.infomodel.MedicineEntry;
import open.dolphin.infomodel.SsKijyoEntry;
import open.dolphin.infomodel.ToolMaterialEntry;
import open.dolphin.infomodel.TreatmentEntry;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;
import open.dolphin.order.ClaimConst;
import open.dolphin.utils.DateExpire;

/**
 * SqlMasterDao　MEMO:DAO
 *
 * @author Kazushi Minagawa
 */
public final class SqlMasterDao extends SqlDaoBean {

//    private static final String MEDICINE_CODE = "20";
    private static final String YKZKBN = ClaimConst.YKZ_KBN_INJECTION;	// 薬剤区分
    private int totalCount;
    private static final String CODE_FILLER = "ZZZ";
    private static final String PREFIX_CODE1 = CODE_FILLER + '1';
    private static final String PREFIX_CODE2 = CODE_FILLER + '2';
    private static final String TYPE_CODE1 = CODE_FILLER + '3';
    private static final String TYPE_CODE2 = CODE_FILLER + '4';
    private static final String PART_CODE = CODE_FILLER + '7';
    private static final String POSTFIX_CODE = CODE_FILLER + '8';

    /**
     *
     */
    public enum Availability {

        /**
         *
         */
        AVAILABLE,
        /**
         *
         */
        NO_CODE,
        /**
         * 
         */
        NO_LONGER_AVAILABLE
    }

    // pinusさんのコードを参考
    private static String SQL_TBL_BYOMEI = "select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where ";

    /** 
     * Creates a new instance of SqlMasterDao
     */
    public SqlMasterDao() {
//      super(); //@Del 2012/06/27 星野 雅昭

        // pinusさんのコードを使用、及び変更
        // 病名テーブルは、バージョンの違いに対応必要なので、ここで sql を作成する
        if (super.ORCA_DB_VER45.equals(getOrcaDbVersion())) {
            SQL_TBL_BYOMEI = SQL_TBL_BYOMEI.replace("icd10_1", "icd10");
        }
    }

    /**
     *
     * @return 合計数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     *
     * @param code コード
     * @return コード
     */
    public String toLongCode(String code) {
        if (code.length() == 4) {
            return CODE_FILLER + code;
        }
        return code;
    }

    /**
     * 名称を返す。一致するコードが無ければ、空文字を返す。
     * @param code コード
     * @return 名称
     */
    public String getSerchClass(String code) {
        String longCode = toLongCode(code).substring(0, 4);
        if (longCode.equals(PREFIX_CODE1) || longCode.equals(PREFIX_CODE2)) {
            return ClaimConst.SubTypeSet.PREFIX.getName();
        }

        if (longCode.equals(TYPE_CODE1) || longCode.equals(TYPE_CODE2)) {
            return ClaimConst.SubTypeSet.TYPE.getName();
        }

        if (longCode.equals(PART_CODE)) {
            return ClaimConst.SubTypeSet.PART.getName();
        }

        if (longCode.equals(POSTFIX_CODE)) {
            return ClaimConst.SubTypeSet.POSTFIX.getName();
        }

        return "";
    }

    /**
     * マスタを項目の名前で検索し、指定された項目名に合致する項目の一覧を返す。
     * @param master            検索対象のマスタ
     * @param name		項目の名称
     * @param startsWith	前方一致の時 TRUE
     * @param serchClassCode	診療行為マスタ検索の場合の点数集計先コード
     * @param sortBy            ソートするカラム
     * @param order             ASC、または DESC
     * @return                  マスタ項目の一覧
     */
    public List getByName(String master, String name, boolean startsWith, String serchClassCode, String sortBy, String order) {
        // 戻り値のリストを用意する
        List results = null;
        // 半角のローマ字(ユーザ入力)を全角のローマ字(マスタで使用)に変換する
        //   String zenkakuRoman = StringTool.toZenkakuUpperLower(name);

        if (master.equals(ClaimConst.MasterSet.DIAGNOSIS.getName())) {
            if (serchClassCode.equals("")) {
                if (StringTool.isAllDigit(name)) {
                    results = getDiseaseByCode(name, startsWith, sortBy, order);
                } else {
                    results = getDiseaseByName(name, startsWith, sortBy, order);
                }
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.NAMES.getName())) {
                results = getDiseaseListNames(name);
// test                results = getDiseaseNames(name);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.PREFIX.getName())) {
                results = getDiseasePrefixFromOrca(name, sortBy);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.TYPE.getName())) {
                results = getDiseaseTypeFromOrca(name, sortBy);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.PART.getName())) {
                results = getDiseasePartFromOrca(name, sortBy);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.POSTFIX.getName())) {
                results = getDiseasePostfixFromOrca(name, sortBy);
            }
        } else if (master.equals(ClaimConst.MasterSet.MEDICAL_SUPPLY.getName())) {
            if (serchClassCode.equals(ClaimConst.MASTER_FLAG_MEDICICE)) // 医薬品マスタを検索する
            {
                results = getMedicineByName(name, startsWith, sortBy, order);   // 薬剤の検索を行う
            } else if (serchClassCode.equals(ClaimConst.MASTER_FLAG_INJECTION)) {
                results = getInjectionByName(name, startsWith, sortBy, order);  // 注射薬の検索を行う
            }
        } else if (master.equals(ClaimConst.MasterSet.TREATMENT.getName())) {
            results = getTreatmentByName(name, startsWith, serchClassCode, sortBy, order);// 診療行為マスタを検索する
        } else if (master.equals(ClaimConst.MasterSet.TOOL_MATERIAL.getName())) {
            results = getToolMaterialByName(name, startsWith, sortBy, order); // 特定機材マスタを検索する
        } else if (master.equals(ClaimConst.MasterSet.ADMINISTRATION.getName())) {
            results = getAdminByName(name, startsWith, sortBy, order); // 用法マスタを検索する
        } //   else if (master.equals(ClaimConst.MasterSet.INTERACT.getName())) {
        //       results = getInteract(name, true); // 相互作用マスタを検索する
        //  }
        //  else if (master.equals(ClaimConst.MasterSet.SSKIJYO.getName())) {
        //       results = getSsKijyo(name); // 症状処置機序マスタを検索する
        //  }
        //else if (master.equals(ClaimConst.MasterSet.SRYCDCHG.getName())) {
        //        results = getUpdates(name); // 一般老人置換マスタを検索する
        //    }
        else {
            throw new RuntimeException("Unsupported master: " + master);
        }
        return results;
    }

    /**
     * 指定された項目名に合致する傷病名クラスの一覧を返す。
     * @param text 項目名
     * @param startsWith
     * @param sortBy ソートするカラム名
     * @param order ASC、または DESC
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseByName(String text, boolean startsWith, String sortBy, String order) {
// test        String result =  UserByoumeiToCode(text);
        ArrayList<String> result = UserByoumeiToCodes(text);   // test
         
        
        // 前方一致検索を行う。startsWith が true ならいきなり部分検索。
        List<DiseaseEntry> ret = null;
        String sql = null;
        if (!startsWith) {
            sql = buildDiseaseListSqlFromOrca("byomei ~ ", result, sortBy, order, true);
//            sql = buildDiseaseSqlFromOrca("byomei ~ ", text, sortBy, order, true);
            ret = getDiseaseCollection(sql);
        }

        // NoError で結果がないとき部分一致検索を行う
        if (isNoError() && (ret == null || ret.isEmpty())) {
            sql = buildDiseaseListSqlFromOrca("byomei ~ ", result, sortBy, order, false);
//            sql = buildDiseaseSqlFromOrca("byomei ~ ", text, sortBy, order, false);
            ret = getDiseaseCollection(sql);
        }
        return ret;
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @param startsWith
     * @param sortBy ソートするカラム名
     * @param order ASC、または DESC
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseByCode(String text, boolean startsWith, String sortBy, String order) {
        ArrayList<String> result = UserByoumeiToCodes(text);   // test
// test        String r = UserByoumeiToCode(text);

        // 前方一致検索を行う。startsWith が true ならいきなり部分検索。
        List<DiseaseEntry> ret = null;
        String sql = null;
        if (!startsWith) {
            sql = buildDiseaseListSqlFromOrca("byomeicd ~ ", result, sortBy, order, true);
//            sql = buildDiseaseSqlFromOrca("byomeicd ~ ", text, sortBy, order, true);
            ret = getDiseaseCollection(sql);
        }

        // NoError で結果がないとき部分一致検索を行う
        if (isNoError() && (ret == null || ret.isEmpty())) {
            sql = buildDiseaseListSqlFromOrca("byomeicd ~ ", result, sortBy, order, true);
//            sql = buildDiseaseSqlFromOrca("byomeicd ~ ", text, sortBy, order, true);
            ret = getDiseaseCollection(sql);
        }
        return ret;
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseNames(String text) {
        String r =  UserByoumeiToCode(text);

        List<DiseaseEntry> result = new ArrayList<DiseaseEntry>();
        String key = r;

        List<String> keyList = new ArrayList<String>();
        int start_index = 0;
        while (key.length() != 0) {
            int end_index = matchMostLongKeywordFromOrca(key);
            if (end_index == 0) {
                break;
            }
            keyList.add(key.substring(start_index, end_index));
            key = key.substring(end_index, key.length());
        }

        for (String _key : keyList) {

            List<DiseaseEntry> diseases = getDiseaseNameFromOrca(_key);
            result.addAll(diseases);
        }

        return result;
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseListNames(String text) {
        int i = 0;
        boolean start = true;

        ArrayList<String> r = UserByoumeiToCodes(text);   // test

        List<DiseaseEntry> result = new ArrayList<DiseaseEntry>();
        String key;
        List<String> keyList = new ArrayList<String>();

        for(i =0;i < r.size();i++) {
            key = r.get(i);
            int start_index = 0;
            while (key.length() != 0) {
                int end_index = matchMostLongKeywordFromOrca(key);
                if (end_index == 0) {
                    break;
                }
                keyList.add(key.substring(start_index, end_index));
                key = key.substring(end_index, key.length());
            }

            for (String _key : keyList) {

                List<DiseaseEntry> diseases = getDiseaseNameFromOrca(_key);
                result.addAll(diseases);
            }
            keyList.clear();
        }
        return result;
    }

    /**
     * indexを返す
     * @param text 項目名
     * @return index
     */
    private int matchMostLongKeywordFromOrca(String text) {
        List<DiseaseEntry> result = null;
        String key;
        int index;
        for (index = 0; index < text.length(); index++) {
            key = text.substring(0, index + 1);
            result = getDiseaseCollection(SQL_TBL_BYOMEI + "byomei ~ '^" + key + "'");
            if (result.isEmpty()) {
                break;
            }
        }
        return index;
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 病名
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseNameFromOrca(String text) {
        StringBuilder buf = new StringBuilder();
        buf.append(SQL_TBL_BYOMEI);
        buf.append("byomei = '" + text + "'");

        return getDiseaseCollection(buf.toString());
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @return 傷病名クラスの一覧
     */
    public List<DiseaseEntry> getDiseaseCodeFromOrca(String text) {
        StringBuilder buf = new StringBuilder();
        buf.append(SQL_TBL_BYOMEI);
        buf.append("byomeicd = '" + toLongCode(text) + "'");

        return getDiseaseCollection(buf.toString());
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @param sortBy ソートするカラム名
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseasePrefixFromOrca(String text, String sortBy) {
        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        StringBuilder buf = new StringBuilder();
        buf.append(SQL_TBL_BYOMEI);
        if (StringTool.isEmptyString(text)) {
            buf.append("byomeicd ~ '^" + PREFIX_CODE1 + "' or byomeicd ~ '^" + PREFIX_CODE2 + "'" + order);
        } else {
            buf.append("(byomei ~ '" + text + "') and (byomeicd ~ '^" + PREFIX_CODE1 + "' or byomeicd ~ '^" + PREFIX_CODE2 + "')" + order);
        }
        String query = buf.toString();

        return getDiseaseCollection(query);
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @param sortBy ソートするカラム名
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseTypeFromOrca(String text, String sortBy) {
        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        StringBuilder buf = new StringBuilder();
        buf.append(SQL_TBL_BYOMEI);
        buf.append("(byomei ~ '" + text + "') and (byomeicd ~ '^" + TYPE_CODE1 + "' or byomeicd ~ '^" + TYPE_CODE2 + "')" + order);

        return getDiseaseCollection(buf.toString());
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @param sortBy ソートするカラム名
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseasePartFromOrca(String text, String sortBy) {
        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        StringBuilder buf = new StringBuilder();
        buf.append(SQL_TBL_BYOMEI);
        buf.append("(byomei ~ '" + text + "') and byomeicd ~ '^" + PART_CODE + "'" + order);

        return getDiseaseCollection(buf.toString());
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param text 項目名
     * @param sortBy ソートするカラム名
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseasePostfixFromOrca(String text, String sortBy) {
        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        StringBuilder buf = new StringBuilder();
        buf.append(SQL_TBL_BYOMEI);
        buf.append("(byomei ~ '" + text + "') and byomeicd ~ '^" + POSTFIX_CODE + "'" + order);

        return getDiseaseCollection(buf.toString());
    }

    /**
     * SQL文を返す
     * @param field
     * @param text 項目名
     * @param sortBy ソートするカラム名
     * @param order ASC、または DESC
     * @param forward
     * @return SQL文
     */
    private String buildDiseaseSqlFromOrca(String field, String text, String sortBy, String order, boolean forward) {
        String word = null;
        StringBuilder buf = new StringBuilder();

        buf.append(SQL_TBL_BYOMEI);

        word = text;
        buf.append(field);//名称

        if (forward) {
            buf.append(addSingleQuote("^" + word));
        } else {
            buf.append(addSingleQuote(word));
        }

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            //   orderBy = " order by byomeicd";
            orderBy = " order by byomeikana";
        }
        buf.append(orderBy);

        return buf.toString();
    }

    /**
     * 病名テーブルに対するSQL文を返す
     * @param field
     * @param text 項目名
     * @param sortBy
     * @param order ASC、または DESC
     * @param forward
     * @return SQL文
     */
    private String buildDiseaseListSqlFromOrca(String field, ArrayList<String> text, String sortBy, String order, boolean forward) {
        int i = 0;
        boolean start = true;
        String word = null;
        StringBuilder buf = new StringBuilder();

        buf.append(SQL_TBL_BYOMEI);

        buf.append(" ( ");
        for(i =0;i < text.size();i++) {
            word = text.get(i);

            if (start)
                start = false;
            else
                buf.append(" or ");
            buf.append(field);//名称
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
        }
        buf.append(" ) ");

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            //   orderBy = " order by byomeicd";
            orderBy = " order by byomeikana";
        }
        buf.append(orderBy);

        return buf.toString();
    }

    /**
     * 傷病名クラスの一覧を返す
     * @param sql SQL文
     * @return 傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseCollection(String sql) {
        //    try {
        //   semaphore.acquire();
        Connection con = null;
        List<DiseaseEntry> collection = null;
        List<DiseaseEntry> outUse = null;
        Statement st = null;

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            DiseaseEntry de = null;
            collection = new ArrayList<DiseaseEntry>();
            outUse = new ArrayList<DiseaseEntry>();

            while (rs.next()) {
                de = new DiseaseEntry();
                de.setCode(rs.getString(1));        // Code
                de.setName(rs.getString(2));        // Name
                de.setKana(rs.getString(3));         // Kana
                de.setIcdTen(rs.getString(4));      // IcdTen
                de.setDisUseDate(rs.getString(5));  // DisUseDate

                if (de.isInUse()) {
                    collection.add(de);
                } else {
                    outUse.add(de);
                }
            }
            rs.close();
            collection.addAll(outUse);

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeConnection(con);
            closeStatement(st);
        }
        //     } catch (InterruptedException ex) {
        //   } finally {
        //     semaphore.release();
        //   }
        return null;
    }

    /**
     * 医薬品マスタを検索する。
     * @param code コード
     * @return	医薬品リスト
     */
    public List<MedicineEntry> getMedicineFromOrca(String code) {
        final List<MedicineEntry> collection = new ArrayList<MedicineEntry>();
        //  try {
        StringBuilder sql = new StringBuilder();
        sql.append("select srycd,name,kananame,tanicd,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("srycd = ");
        sql.append(addSingleQuote(code));

        executeQuery(sql.toString(), null,
                new GenericAdapter<ResultSet, Object>() {

                    @Override
                    public boolean onResult(ResultSet records, Object result) throws Exception {
                        if (!DateExpire.expire(records.getString(9), records.getString(10))) {
                            MedicineEntry me = new MedicineEntry();
                            me.setCode(records.getString(1));        // Code
                            me.setName(records.getString(2));        // Name
                            me.setKana(records.getString(3));        // Name
                            me.setUnit(OrcaUnitTranslater.toName(records.getString(4)));        // Unit
                            me.setCostFlag(records.getString(5));    // Cost flag
                            me.setCost(records.getString(6));        // Cost
                            me.setYkzKbn(records.getString(7));      // 薬剤区分
                            me.setJNCD(records.getString(8));        // JNCD
                            me.setStartDate(records.getString(9));   // startDate
                            me.setEndDate(records.getString(10));    // endDate
                            collection.add(me);
                        }
                        return true;
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                });
        //    } finally {
        //    }
        return collection;
    }

    /**
     * 自動算定項目かどうかを検索する。
     * @return 0 通常　1 検査　2　自動算定項目 3 日付無効　4 コードなし
     */
    public enum KNSJISKBN {

        /**
         *
         */
        NORMAL,
        /**
         * 
         */
        KENSA,
        /**
         *
         */
        AUTO,
        /**
         * 
         */
        EXPIRE,
        /**
         *
         */
        NOCODE
    };

    /**
     *　区分を返す。
     * @param code コード
     * @return　区分
     */
    public KNSJISKBN IsAutoCulcFromOrca(String code) {
        final List<KNSJISKBN> collection = new ArrayList<KNSJISKBN>();
        //  try {
        StringBuilder sql = new StringBuilder();
        sql.append("select KNSJISKBN,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("srycd = ");
        sql.append(addSingleQuote(code));

        executeQuery(sql.toString(), null,
                new GenericAdapter<ResultSet, Object>() {

                    @Override
                    public boolean onResult(ResultSet records, Object result) throws Exception {
                        if (!DateExpire.expire(records.getString(2), records.getString(3))) {
                            switch (records.getInt(1)) {
                                case 0:
                                    collection.add(KNSJISKBN.NORMAL);
                                    break;
                                case 1:
                                    collection.add(KNSJISKBN.KENSA);
                                    break;
                                case 2:
                                    collection.add(KNSJISKBN.AUTO);
                                    break;
                                default:
                            }
                        } else {
                            collection.add(KNSJISKBN.EXPIRE);

                        }
                        return true;
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                });
        //   } finally {
        //   }

        if (!collection.isEmpty()) {
            return collection.get(0);
        } else {
            return KNSJISKBN.NOCODE;
        }
    }

    /**
     *　一覧
     * @param text
     * @param startsWith
     * @param sortBy
     * @param order
     * @return 一覧
     */
    private List<MedicineEntry> getMedicineByName(String text, boolean startsWith, String sortBy, String order) {
        // 前方一致検索を行う
        ArrayList<String> result = InputCdToCodes(text);   // test
        String sql = getMedicineListSqlFromOrca(result, sortBy, order, startsWith);
// test        String sql = getMedicineSqlFromOrca(StringTool.toZenkakuUpperLower(InputCdToCode(text)), sortBy, order, startsWith);
        List<MedicineEntry> ret = getMedicineCollection(sql);
        return ret;
    }

    /**
     * SQLを返す
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getMedicineSqlFromOrca(String text, String sortBy, String order, boolean forward) {

        // 点数マスタが6で始まり、薬剤区分が注射薬 4 でないものを検索する
        String word = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select srycd,name,kananame,taniname,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("srycd ~ '^6' and ");

        // 全て数字であればコードを検索し、それ以外は名称を検索する
        if (StringTool.isAllDigit(text)) {
            word = text;
            sql.append("srycd ~ ");
            if (forward) {
                sql.append(addSingleQuote("^" + word));
            } else {
                sql.append(addSingleQuote(word));
            }
        } else {
            word = text;
            sql.append("(name ~ ");
            if (forward) {
                sql.append(addSingleQuote("^" + word));
            } else {
                sql.append(addSingleQuote(word));
            }
            sql.append(" or formalname ~ ");
            if (forward) {
                sql.append(addSingleQuote("^" + word));
            } else {
                sql.append(addSingleQuote(word));
            }
            sql.append(") ");
        }

        // 注射薬でない
        sql.append(" and ykzkbn != ");
        sql.append(addSingleQuote(YKZKBN));

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        sql.append(orderBy);

        //  printTrace(sql.toString());

        return sql.toString();
    }

    /**
     *
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getMedicineListSqlFromOrca(ArrayList<String> text, String sortBy, String order, boolean forward) {

        int i = 0;
        boolean start = true;
        // 点数マスタが6で始まり、薬剤区分が注射薬 4 でないものを検索する
        String word = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select srycd,name,kananame,taniname,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("srycd ~ '^6' and ");

        sql.append(" ( ");
        for(i =0;i < text.size();i++) {
            if (start)
                start = false;
            else
                sql.append(" or ");
            // 全て数字であればコードを検索し、それ以外は名称を検索する
            if (StringTool.isAllDigit(text.get(i))) {
                word = text.get(i);
                sql.append("srycd ~ ");
                if (forward) {
                    sql.append(addSingleQuote("^" + word));
                } else {
                    sql.append(addSingleQuote(word));
                }
            } else {
                word = text.get(i);
                sql.append("(name ~ ");
                if (forward) {
                    sql.append(addSingleQuote("^" + word));
                } else {
                    sql.append(addSingleQuote(word));
                }
                sql.append(" or formalname ~ ");
                if (forward) {
                    sql.append(addSingleQuote("^" + word));
                } else {
                    sql.append(addSingleQuote(word));
                }
                sql.append(") ");
            }
        }
        sql.append(" ) ");
            
        // 注射薬でない
        sql.append(" and ykzkbn != ");
        sql.append(addSingleQuote(YKZKBN));

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        sql.append(orderBy);

        //  printTrace(sql.toString());

        return sql.toString();
    }

    /**
     *
     * @param sql
     * @return
     */
    private List<MedicineEntry> getMedicineCollection(String sql) {

        Connection con = null;
        List<MedicineEntry> collection = null;
        List<MedicineEntry> outUse = null;
        Statement st = null;

        // 前方一致を試みる
        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            MedicineEntry medicine = null;
            collection = new ArrayList<MedicineEntry>();
            outUse = new ArrayList<MedicineEntry>();

            while (rs.next()) {
                medicine = new MedicineEntry();
                medicine.setCode(rs.getString(1));        // Code
                medicine.setName(rs.getString(2));        // Name
                medicine.setKana(rs.getString(3));        // Name
                medicine.setUnit(rs.getString(4));        // Unit
                medicine.setCostFlag(rs.getString(5));    // Cost flag
                medicine.setCost(rs.getString(6));        // Cost
                medicine.setYkzKbn(rs.getString(7));      // 薬剤区分
                medicine.setJNCD(rs.getString(8));        // JNCD
                medicine.setStartDate(rs.getString(9));  // startDate
                medicine.setEndDate(rs.getString(10));    // endDate
                if (medicine.isInUse()) {
                    collection.add(medicine);
                } else {
                    outUse.add(medicine);
                }
            }
            rs.close();
            collection.addAll(outUse);

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     * 注射薬を検索する。
     * @param text	検索キーワード
     * @param startsWith no use
     * @param sortBy	ソートするカラム
     * @param order	昇順降順
     * @return		注射薬のリスト
     */
    private List<MedicineEntry> getInjectionByName(String text, boolean startsWith, String sortBy, String order) {
        // 前方一致検索を行う
        ArrayList<String> result = InputCdToCodes(text);   // test
        String sql = getInjectionListSqlFromOrca(result, sortBy, order, startsWith);
// test        String sql = getInjectionSqlFromOrca(StringTool.toZenkakuUpperLower(InputCdToCode(text)), sortBy, order, startsWith);
        List<MedicineEntry> ret = getInjectionCollection(sql);
        return ret;
    }

    /**
     *
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getInjectionSqlFromOrca(String text, String sortBy, String order, boolean forward) {
        String word = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select srycd,name,kananame,taniname,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("(srycd ~ ").append(addSingleQuote("^" + ClaimConst.ZANRYO_HAIKI_CODE_START)).append(" or ");
        sql.append("(srycd ~ '^6' and ykzkbn = ");
        sql.append(addSingleQuote(YKZKBN));
        sql.append(")) and ");

        if (StringTool.isAllDigit(text)) {
            word = text;
            sql.append("srycd ~ ");
            if (forward) {
                sql.append(addSingleQuote("^" + word));
            } else {
                sql.append(addSingleQuote(word));
            }
        } else {
            word = text;
            sql.append("(name ~ ");
            if (forward) {
                sql.append(addSingleQuote("^" + word));
            } else {
                sql.append(addSingleQuote(word));
            }
            sql.append(" or formalname ~ ");
            if (forward) {
                sql.append(addSingleQuote("^" + word));
            } else {
                sql.append(addSingleQuote(word));
            }
            sql.append(") ");
        }

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        sql.append(orderBy);
        //    printTrace(sql.toString());
        return sql.toString();
    }

    /**
     *
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getInjectionListSqlFromOrca(ArrayList<String> text, String sortBy, String order, boolean forward) {
        int i = 0;
        boolean start = true;

        String word = null;
        StringBuilder sql = new StringBuilder();
        sql.append("select srycd,name,kananame,taniname,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("(srycd ~ ").append(addSingleQuote("^" + ClaimConst.ZANRYO_HAIKI_CODE_START)).append(" or ");
        sql.append("(srycd ~ '^6' and ykzkbn = ");
        sql.append(addSingleQuote(YKZKBN));
        sql.append(")) and ");

        sql.append(" ( ");
        for(i =0;i < text.size();i++) {
            if (start)
                start = false;
            else
                sql.append(" or ");
            if (StringTool.isAllDigit(text.get(i))) {
                word = text.get(i);
                sql.append("srycd ~ ");
                if (forward) {
                    sql.append(addSingleQuote("^" + word));
                } else {
                    sql.append(addSingleQuote(word));
                }
            } else {
                word = text.get(i);
                sql.append("(name ~ ");
                if (forward) {
                    sql.append(addSingleQuote("^" + word));
                } else {
                    sql.append(addSingleQuote(word));
                }
                sql.append(" or formalname ~ ");
                if (forward) {
                    sql.append(addSingleQuote("^" + word));
                } else {
                    sql.append(addSingleQuote(word));
                }
                sql.append(") ");
            }
        }
        sql.append(" ) ");

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        sql.append(orderBy);
        //    printTrace(sql.toString());
        return sql.toString();
    }

    /**
     *
     * @param sql
     * @return
     */
    private List<MedicineEntry> getInjectionCollection(String sql) {
        Connection con = null;
        List<MedicineEntry> collection = null;
        List<MedicineEntry> outUse = null;
        Statement st = null;
        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            MedicineEntry me = null;
            collection = new ArrayList<MedicineEntry>();
            outUse = new ArrayList<MedicineEntry>();

            while (rs.next()) {
                me = new MedicineEntry();
                me.setCode(rs.getString(1));        // Code
                me.setName(rs.getString(2));        // Name
                me.setKana(rs.getString(3));        // Name
                me.setUnit(rs.getString(4));        // Unit
                me.setCostFlag(rs.getString(5));    // Cost flag
                me.setCost(rs.getString(6));
                me.setYkzKbn(rs.getString(7));      // 薬剤区分
                me.setJNCD(rs.getString(8));
                me.setStartDate(rs.getString(9));  // start Date
                me.setEndDate(rs.getString(10));    // end Date
                if (me.isInUse()) {
                    collection.add(me);
                } else {
                    outUse.add(me);
                }
            }
            rs.close();
            collection.addAll(outUse);

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     * 診療行為マスタを検索する。
     * @param text	検索キーワード
     * @param startsWith	no use
     * @param orderClassCode	点数集計先
     * @param sortBy	ソートカラム
     * @param order	昇順降順
     * @return	診療行為リスト
     */
    private List<TreatmentEntry> getTreatmentByName(String text, boolean startsWith, String orderClassCode, String sortBy, String order) {
        // 前方一致検索を行う

        //      String code = InputCdToCode(text);
        ArrayList<String> result = InputCdToCodes(text);   // test

        String sql = getTreatmentListSqlFromOrca(result, orderClassCode, sortBy, order, startsWith);
// test        String sql = getTreatmentSqlFromOrca(StringTool.toZenkakuUpperLower(InputCdToCode(text)), orderClassCode, sortBy, order, startsWith);
        List<TreatmentEntry> ret = getTreatmentCollection(sql);
        return ret;
    }

    /*
     * 診療行為
     * */
    private String getTreatmentSqlFromOrca(String text, String orderClassCode, String sortBy, String order, boolean forward) {

        String word = null;
        StringBuilder buf = new StringBuilder();

        //sqlBuffer.append("select srycd,name,kananame,tensikibetu,ten,nyugaitekkbn,routekkbn,srysyukbn,hospsrykbn,yukostymd,yukoedymd from tbl_tensu where ");
        buf.append("select srycd,name,formalname,tensikibetu,ten,nyugaitekkbn,routekkbn,srysyukbn,hospsrykbn,yukostymd,yukoedymd ");
        buf.append(" ,sstkijuncd1,sstkijuncd2,sstkijuncd3,sstkijuncd4,sstkijuncd5,sstkijuncd6,sstkijuncd7,sstkijuncd8,sstkijuncd9,sstkijuncd10 ");
        buf.append(" from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }

        buf.append("( (srycd ~ '^095') or "); // 自費診療対応 2008.08.24
        buf.append("(srycd ~ '^096') or "); // 自費診療対応 2008.08.24

        buf.append("(srycd ~ '^0082') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0083') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0084') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0085') or "); // コメントコード対応 2011.05.06
        buf.append("(srycd ~ '^810000001') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^82') or "); // コメントコード対応 2008.09.29
        buf.append("(srycd ~ '^83') or "); // コメントコード対応 2008.10.06
        buf.append("(srycd ~ '^84') or "); // コメントコード対応 2008.10.06
        buf.append("(srycd ~ '^85') or "); // コメントコード対応 2011.05.06
        buf.append("(srycd ~ '^099') or "); // リハビリコード対応 2008.09.02
        //    buf.append("(srycd ~ '^09930') or "); // 残量廃棄コード対応 2008.09.02
        buf.append("(srycd ~ '^1' or srycd ~ '^00' ");

        StringBuilder sbd = new StringBuilder();
        if (orderClassCode != null) {
            String[] cClass = new String[]{"", ""};
            int index = 0;
            StringTokenizer tokenizer = new StringTokenizer(orderClassCode, "-");
            while (tokenizer.hasMoreTokens()) {
                cClass[index++] = tokenizer.nextToken();
            }
            String min = cClass[0];
            String max = cClass[1];

            if ((!min.equals("")) && max.equals("")) {
                sbd.append(" and srysyukbn = ");
                sbd.append(addSingleQuote(min));

            } else if ((!min.equals("")) && (!max.equals(""))) {
                sbd.append(" and srysyukbn >= ");
                sbd.append(addSingleQuote(min));
                sbd.append(" and srysyukbn <= ");
                sbd.append(addSingleQuote(max));
            }
        }

        String sql2 = sbd.toString();
        buf.append(sql2);
        buf.append(" ) ) and ");
        if (StringTool.isAllDigit(text)) {
            word = text;
            buf.append("srycd ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
        } else {
            word = text;
            buf.append("(name ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
            buf.append(" or formalname ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
            buf.append(") ");
        }
        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        buf.append(orderBy);

        String sql = buf.toString();
        //      printTrace(sql);

        return sql;
    }

    /*
     * 診療行為
     * */
    private String getTreatmentListSqlFromOrca(ArrayList<String> text, String orderClassCode, String sortBy, String order, boolean forward) {
        int i = 0;
        boolean start = true;

        String word = null;
        StringBuilder buf = new StringBuilder();

        //sqlBuffer.append("select srycd,name,kananame,tensikibetu,ten,nyugaitekkbn,routekkbn,srysyukbn,hospsrykbn,yukostymd,yukoedymd from tbl_tensu where ");
        buf.append("select srycd,name,formalname,tensikibetu,ten,nyugaitekkbn,routekkbn,srysyukbn,hospsrykbn,yukostymd,yukoedymd ");
        buf.append(" ,sstkijuncd1,sstkijuncd2,sstkijuncd3,sstkijuncd4,sstkijuncd5,sstkijuncd6,sstkijuncd7,sstkijuncd8,sstkijuncd9,sstkijuncd10 ");
        buf.append(" from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }

        buf.append("( (srycd ~ '^095') or "); // 自費診療対応 2008.08.24
        buf.append("(srycd ~ '^096') or "); // 自費診療対応 2008.08.24

        buf.append("(srycd ~ '^0082') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0083') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0084') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0085') or "); // コメントコード対応 2011.05.06
        buf.append("(srycd ~ '^810000001') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^82') or "); // コメントコード対応 2008.09.29
        buf.append("(srycd ~ '^83') or "); // コメントコード対応 2008.10.06
        buf.append("(srycd ~ '^84') or "); // コメントコード対応 2008.10.06
        buf.append("(srycd ~ '^85') or "); // コメントコード対応 2011.05.06
        buf.append("(srycd ~ '^099') or "); // リハビリコード対応 2008.09.02
        //    buf.append("(srycd ~ '^09930') or "); // 残量廃棄コード対応 2008.09.02
        buf.append("(srycd ~ '^1' or srycd ~ '^00' ");

        StringBuilder sbd = new StringBuilder();
        if (orderClassCode != null) {
            String[] cClass = new String[]{"", ""};
            int index = 0;
            StringTokenizer tokenizer = new StringTokenizer(orderClassCode, "-");
            while (tokenizer.hasMoreTokens()) {
                cClass[index++] = tokenizer.nextToken();
            }
            String min = cClass[0];
            String max = cClass[1];

            if ((!min.equals("")) && max.equals("")) {
                sbd.append(" and srysyukbn = ");
                sbd.append(addSingleQuote(min));

            } else if ((!min.equals("")) && (!max.equals(""))) {
                sbd.append(" and srysyukbn >= ");
                sbd.append(addSingleQuote(min));
                sbd.append(" and srysyukbn <= ");
                sbd.append(addSingleQuote(max));
            }
        }

        String sql2 = sbd.toString();
        buf.append(sql2);
        buf.append(" ) ) and ");

        buf.append(" ( ");
        for(i =0;i < text.size();i++) {
            if (start)
                start = false;
            else
                buf.append(" or ");
           if (StringTool.isAllDigit(text.get(i))) {
                word = text.get(i);
                buf.append("srycd ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
            } else {
                word = text.get(i);
                buf.append("(name ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
                buf.append(" or formalname ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
                buf.append(") ");
            }
        }
        buf.append(" ) ");

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        buf.append(orderBy);

        String sql = buf.toString();
        //      printTrace(sql);

        return sql;
    }

    /**
     *
     * @param sql
     * @return
     */
    private List<TreatmentEntry> getTreatmentCollection(String sql) {

        Connection con = null;
        List<TreatmentEntry> collection = null;
        List<TreatmentEntry> outUse = null;
        Statement st = null;

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            TreatmentEntry te = null;
            collection = new ArrayList<TreatmentEntry>();
            outUse = new ArrayList<TreatmentEntry>();

            while (rs.next()) {
                te = new TreatmentEntry();
                te.setCode(rs.getString(1));            // srycd
                te.setName(rs.getString(2));            // name
                te.setKana(rs.getString(3));            // kana
                //te.setUnit(rs.getString(3));          // Unit
                te.setCostFlag(rs.getString(4));        // tensikibetu
                te.setCost(rs.getString(5));            // ten
                te.setInOutFlag(rs.getString(6));       // nyugaitekkbn
                te.setOldFlag(rs.getString(7));  	// routekkbn									// OldFlag
                te.setClaimClassCode(rs.getString(8));  // srysuykbn
                te.setHospitalClinicFlag(rs.getString(9)); // hospsrykbn
                te.setStartDate(rs.getString(10));     // start
                te.setEndDate(rs.getString(11));     // end

                // TODO SQL 生成と、rs.getString(n) の「n」をシンクロさせる仕組みを作る
                // 10 は sstkijuncd が 10個のため。12 はオフセット
                List ary = new ArrayList();
                for (int i = 0; i < 10; i++) {
                    ary.add(rs.getString(i + 12));
                }
                te.setSstKijunCdSet(ary); // sskkijuncd

                if (te.isInUse()) {
                    collection.add(te);
                } else {
                    outUse.add(te);
                }
            }
            rs.close();
            collection.addAll(outUse);

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     * 診療行為マスタを検索する。
     * @param master
     * @param claimClass 診療行為コード(点数集計先)
     * @param sortBy	ソートカラム
     * @param order	昇順降順
     * @return
     * @return診療行為リスト
     */
    public List<TreatmentEntry> getByClaimClassFromOrca(String master, String claimClass, String sortBy, String order) {

        Connection con = null;
        List<TreatmentEntry> collection = null;
        List<TreatmentEntry> outUse = null;
        Statement st = null;

        // 診療行為コードの範囲を分解する
        // ex. 700-799 等
        String[] cClass = new String[]{"", ""};
        int index = 0;
        StringTokenizer tokenizer = new StringTokenizer(claimClass, "-");
        while (tokenizer.hasMoreTokens()) {
            cClass[index++] = tokenizer.nextToken();
        }
        String min = cClass[0];
        String max = cClass[1];

        StringBuilder buf = new StringBuilder();
        //sqlBuffer.append("select srycd,name,kananame,tensikibetu,ten,nyugaitekkbn,routekkbn,srysyukbn,hospsrykbn,yukostymd,yukoedymd from tbl_tensu where ");
        buf.append("select srycd,name,formalname,tensikibetu,ten,nyugaitekkbn,routekkbn,srysyukbn,hospsrykbn,yukostymd,yukoedymd ");
        buf.append(" ,sstkijuncd1,sstkijuncd2,sstkijuncd3,sstkijuncd4,sstkijuncd5,sstkijuncd6,sstkijuncd7,sstkijuncd8,sstkijuncd9,sstkijuncd10 ");
        buf.append(" from tbl_tensu where ");

        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }

        buf.append("(srycd ~ '^095') or "); // 自費診療対応 2008.08.24
        buf.append("(srycd ~ '^096') or "); // 自費診療対応 2008.08.24
        buf.append("(srycd ~ '^0082') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0083') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0084') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^0085') or "); // コメントコード対応 2011.05.06
        buf.append("(srycd ~ '^810000001') or "); // コメントコード対応 2008.09.02
        buf.append("(srycd ~ '^82') or "); // コメントコード対応 2008.09.29
        buf.append("(srycd ~ '^83') or "); // コメントコード対応 2008.10.06
        buf.append("(srycd ~ '^84') or "); // コメントコード対応 2008.10.06
        buf.append("(srycd ~ '^85') or "); // コメントコード対応 2011.05.06
        buf.append("(srycd ~ '^09930') or "); // 残量廃棄コード対応 2008.09.02
        buf.append("(srycd ~ '^1' ");
        if ((!min.equals("")) && max.equals("")) {
            buf.append(" and srysyukbn = ");
            buf.append(addSingleQuote(min));

        } else if ((!min.equals("")) && (!max.equals(""))) {
            buf.append(" and srysyukbn >= ");
            buf.append(addSingleQuote(min));
            buf.append(" and srysyukbn <= ");
            buf.append(addSingleQuote(max));
        }
        buf.append(" ) ");

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy != null) {
            buf.append(orderBy);
        } else {
            buf.append(" order by srycd");
        }

        String sql = buf.toString();
        //     printTrace(sql);

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            TreatmentEntry te = null;
            collection = new ArrayList<TreatmentEntry>();
            outUse = new ArrayList<TreatmentEntry>();

            while (rs.next()) {
                te = new TreatmentEntry();
                te.setCode(rs.getString(1));            // srycd
                te.setName(rs.getString(2));            // name
                te.setKana(rs.getString(3));            // kana
                //te.setUnit(rs.getString(3));          // Unit
                te.setCostFlag(rs.getString(4));        // tensikibetu
                te.setCost(rs.getString(5));            // ten
                te.setInOutFlag(rs.getString(6));       // nyugaitekkbn
                te.setOldFlag(rs.getString(7));  	// routekkbn									// OldFlag
                te.setClaimClassCode(rs.getString(8));  // srysuykbn
                te.setHospitalClinicFlag(rs.getString(9)); // hospsrykbn
                te.setStartDate(rs.getString(10));     // start
                te.setEndDate(rs.getString(11));     // end

                // TODO SQL 生成と、rs.getString(n) の「n」をシンクロさせる仕組みを作る
                // 10 は sstkijuncd が 10個のため。12 はオフセット
                List ary = new ArrayList();
                for (int i = 0; i < 10; i++) {
                    ary.add(rs.getString(i + 12));
                }
                te.setSstKijunCdSet(ary); // sskkijuncd

                if (te.isInUse()) {
                    collection.add(te);
                } else {
                    outUse.add(te);
                }
            }
            rs.close();
            collection.addAll(outUse);

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     * 画像診断撮影部位の検索を行う。
     * @param master
     * @param sortBy ソートカラム
     * @param order 昇順降順
     * @return 診療行為リスト
     */
    public List<TreatmentEntry> getRadLocationFromOrca(String master, String sortBy, String order) {

        Connection con = null;
        List<TreatmentEntry> collection = null;
        List<TreatmentEntry> outUse = null;
        Statement st = null;

        StringBuilder buf = new StringBuilder();
        buf.append("select srycd,name,kananame,srysyukbn,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }

        buf.append("srycd ~ '^002'");
        String orderBy = getOrderBy(sortBy, order);
        if (orderBy != null) {
            buf.append(orderBy);
        } else {
            buf.append(" order by srycd");
        }
        String sql = buf.toString();
        //      printTrace(sql);

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            TreatmentEntry te = null;
            collection = new ArrayList<TreatmentEntry>();
            outUse = new ArrayList<TreatmentEntry>();

            while (rs.next()) {
                te = new TreatmentEntry();
                te.setCode(rs.getString(1));            // srycd
                te.setName(rs.getString(2));            // name
                te.setKana(rs.getString(3));            // kana								// OldFlag
                te.setClaimClassCode(rs.getString(4));  // srysuykbn
                te.setStartDate(rs.getString(5));     // start
                te.setEndDate(rs.getString(6));     // end

                if (te.isInUse()) {
                    collection.add(te);
                } else {
                    outUse.add(te);
                }
            }
            rs.close();
            collection.addAll(outUse);

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     * 特定機材マスタの検索を行う。
     * @param text 検索キーワード
     * @param startsWith no use
     * @param sortBy ソートカラム
     * @param order 昇順降順
     * @return 特定機材リスト
     */
    private List<ToolMaterialEntry> getToolMaterialByName(String text, boolean startsWith, String sortBy, String order) {
        // 前方一致検索を行う
        ArrayList<String> result = InputCdToCodes(text);   // test
        String sql = getToolMaterialListSqlFromOrca(result, sortBy, order, startsWith);
// test        String sql = getToolMaterialSqlFromOrca(StringTool.toZenkakuUpperLower(InputCdToCode(text)), sortBy, order, startsWith);
        List<ToolMaterialEntry> ret = getToolMaterialCollection(sql);
        return ret;
    }

    /**
     *
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getToolMaterialSqlFromOrca(String text, String sortBy, String order, boolean forward) {

        String word = null;
        StringBuilder buf = new StringBuilder();

        buf.append("select srycd,name,kananame,taniname,tensikibetu,ten,yukostymd,yukoedymd,idokanren from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }
        buf.append("(srycd ~ '^7' or srycd ~ '^058' or srycd ~ '^059') and ");

        if (StringTool.isAllDigit(text)) {
            word = text;
            buf.append("srycd ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
        } else {
            word = text;
            buf.append("(name ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
            buf.append(" or formalname ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
            buf.append(") ");
        }

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        buf.append(orderBy);
        String sql = buf.toString();
        //      printTrace(sql);

        return sql;
    }

    /**
     *
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getToolMaterialListSqlFromOrca(ArrayList<String> text, String sortBy, String order, boolean forward) {
        int i = 0;
        boolean start = true;

        String word = null;
        StringBuilder buf = new StringBuilder();

        buf.append("select srycd,name,kananame,taniname,tensikibetu,ten,yukostymd,yukoedymd,idokanren from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }
        buf.append("(srycd ~ '^7' or srycd ~ '^058' or srycd ~ '^059') and ");

        buf.append(" ( ");
        for(i =0;i < text.size();i++) {
            if (start)
                start = false;
            else
                buf.append(" or ");
            if (StringTool.isAllDigit(text.get(i))) {
                word = text.get(i);
                buf.append("srycd ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
            } else {
                word = text.get(i);
                buf.append("(name ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
                buf.append(" or formalname ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
                buf.append(") ");
            }
        }
        buf.append(" ) ");

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        buf.append(orderBy);
        String sql = buf.toString();
        //      printTrace(sql);

        return sql;
    }

    /**
     *
     * @param sql
     * @return
     */
    private List<ToolMaterialEntry> getToolMaterialCollection(String sql) {

        Connection con = null;
        List<ToolMaterialEntry> collection = null;
        List<ToolMaterialEntry> outUse = null;
        Statement st = null;

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            ToolMaterialEntry te = null;
            collection = new ArrayList<ToolMaterialEntry>();
            outUse = new ArrayList<ToolMaterialEntry>();

            while (rs.next()) {
                te = new ToolMaterialEntry();
                te.setCode(rs.getString(1));        // Code
                te.setName(rs.getString(2));        // name
                te.setKana(rs.getString(3));        // kana
                te.setUnit(rs.getString(4));        // Unit
                te.setCostFlag(rs.getString(5));    // Cost flag
                te.setCost(rs.getString(6));        // Cost
                te.setStartDate(rs.getString(7));   // start
                te.setEndDate(rs.getString(8));        // end
                te.setRequiredMaterialCode(rs.getString(9));
                if (te.isInUse()) {
                    collection.add(te);
                } else {
                    outUse.add(te);
                }
            }
            rs.close();
            collection.addAll(outUse);

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     *
     * @param code
     * @return
     */
    public List<ToolMaterialEntry> getToolMaterialByCodeFromOrca(String code) {

        final List<ToolMaterialEntry> collection = new ArrayList<ToolMaterialEntry>();
        //     try {
        //    semaphore.acquire();
        StringBuilder buf = new StringBuilder();

        buf.append("select srycd,name,kananame,tanicd,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }
        buf.append("srycd = ");
        buf.append(addSingleQuote(code));

        String sql = buf.toString();

        executeQuery(sql, null,
                new GenericAdapter<ResultSet, Object>() {

                    @Override
                    public boolean onResult(ResultSet records, Object result) throws Exception {
                        //     SimpleDateFormat orcaDateFormat = new SimpleDateFormat("yyyyMMDD");
                        //    java.util.Date startDate = orcaDateFormat.parse(records.getString(9).equals("00000000") ? "20000101" : records.getString(9));
                        //     java.util.Date endDate = orcaDateFormat.parse(records.getString(10).equals("99999999") ? "20200101" : records.getString(10));
                        //     java.util.Date now = new java.util.Date();
                        //   if ((startDate.compareTo(now) < 0) && (endDate.compareTo(now) > 0)) {
                        if (!DateExpire.expire(records.getString(9), records.getString(10))) {
                            ToolMaterialEntry me = new ToolMaterialEntry();
                            me.setCode(records.getString(1));        // Code
                            me.setName(records.getString(2));        // Name
                            me.setKana(records.getString(3));        // Name
                            me.setUnit(OrcaUnitTranslater.toName(records.getString(4)));        // Unit
                            me.setCostFlag(records.getString(5));    // Cost flag
                            me.setCost(records.getString(6));        // Cost
                            me.setStartDate(records.getString(9));  // startDate
                            me.setEndDate(records.getString(10));    // endDate
                            collection.add(me);
                        }
                        return true;
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                });

        //     } finally {
        //     semaphore.release();
        //    }
        return collection;
    }

    /**
     * 用法マスタの検索を行う。
     * @param text 検索キーワード
     * @param startsWith no use
     * @param sortBy ソートカラム
     * @param order 昇順降順
     * @return 用法リスト
     */
    private List<AdminEntry> getAdminByName(String text, boolean startsWith, String sortBy, String order) {
        // 前方一致検索を行う
        ArrayList<String> result = InputCdToCodes(text);   // test
        String sql = getAdminByNameListSqlFromOrca(result, sortBy, order, startsWith);
// test        String sql = getAdminByNameSqlFromOrca(StringTool.toZenkakuUpperLower(InputCdToCode(text)), sortBy, order, startsWith);
        List<AdminEntry> ret = getAdminCollection(sql);
        return ret;
    }

    /**
     *
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getAdminByNameSqlFromOrca(String text, String sortBy, String order, boolean forward) {

        String word = null;
        StringBuilder buf = new StringBuilder();

        buf.append("select srycd, name ");
        buf.append(" ,sstkijuncd1,sstkijuncd2,sstkijuncd3,sstkijuncd4,sstkijuncd5,sstkijuncd6,sstkijuncd7,sstkijuncd8,sstkijuncd9,sstkijuncd10 ");
        buf.append(" from tbl_tensu where ");

        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }
        buf.append(" ( ");
        buf.append("srycd ~ '^001' or ");
        buf.append("srycd ~ '^810000001$' or ");
        buf.append("srycd ~ '^0082' or srycd ~ '^82' or ");
        buf.append("srycd ~ '^0083' or srycd ~ '^83' or ");
        buf.append("srycd ~ '^0084' or srycd ~ '^84' or ");
        buf.append("srycd ~ '^0085' or srycd ~ '^85' ");
        buf.append(" ) and ");

        if (StringTool.isAllDigit(text)) {
            word = text;
            buf.append("srycd ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
        } else {
            word = text;
            buf.append("(name ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
            buf.append(" or formalname ~ ");
            if (forward) {
                buf.append(addSingleQuote("^" + word));
            } else {
                buf.append(addSingleQuote(word));
            }
            buf.append(") ");
        }

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        buf.append(orderBy);
        String sql = buf.toString();
        //     printTrace(sql);
        return sql;
    }

    /**
     *
     * @param text
     * @param sortBy
     * @param order
     * @param forward
     * @return
     */
    private String getAdminByNameListSqlFromOrca(ArrayList<String> text, String sortBy, String order, boolean forward) {
        int i = 0;
        boolean start = true;

        String word = null;
        StringBuilder buf = new StringBuilder();

        buf.append("select srycd, name ");
        buf.append(" ,sstkijuncd1,sstkijuncd2,sstkijuncd3,sstkijuncd4,sstkijuncd5,sstkijuncd6,sstkijuncd7,sstkijuncd8,sstkijuncd9,sstkijuncd10 ");
        buf.append(" from tbl_tensu where ");

        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            buf.append("hospnum=");
            buf.append(hospnum);
            buf.append(" and ");
        }
        buf.append(" ( ");
        buf.append("srycd ~ '^001' or ");
        buf.append("srycd ~ '^810000001$' or ");
        buf.append("srycd ~ '^0082' or srycd ~ '^82' or ");
        buf.append("srycd ~ '^0083' or srycd ~ '^83' or ");
        buf.append("srycd ~ '^0084' or srycd ~ '^84' or ");
        buf.append("srycd ~ '^0085' or srycd ~ '^85' ");
        buf.append(" ) and ");

        buf.append(" ( ");
        for(i =0;i < text.size();i++) {
            if (start)
                start = false;
            else
                buf.append(" or ");
            if (StringTool.isAllDigit(text.get(i))) {
                word = text.get(i);
                buf.append("srycd ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
            } else {
                word = text.get(i);
                buf.append("(name ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
                buf.append(" or formalname ~ ");
                if (forward) {
                    buf.append(addSingleQuote("^" + word));
                } else {
                    buf.append(addSingleQuote(word));
                }
                buf.append(") ");
            }
        }
        buf.append(" ) ");

        String orderBy = getOrderBy(sortBy, order);
        if (orderBy == null) {
            orderBy = " order by srycd";
        }
        buf.append(orderBy);
        String sql = buf.toString();
        //     printTrace(sql);
        return sql;
    }

    /**
     *
     * @param sql
     * @return
     */
    private List<AdminEntry> getAdminCollection(String sql) {

        Connection con = null;
        List<AdminEntry> collection = null;
        Statement st = null;

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ValueObject
            AdminEntry te = null;
            collection = new ArrayList<AdminEntry>();

            while (rs.next()) {
                te = new AdminEntry();
                te.setCode(rs.getString(1));        // Code
                te.setName(rs.getString(2));        // name
                // <TODO> SQL 生成と、rs.getString(n) の「n」をシンクロさせる仕組みを作る
                // 10 は sstkijuncd が 10個のため。12 はオフセット
                List ary = new ArrayList();
                for (int i = 0; i < 10; i++) {
                    ary.add(rs.getString(i + 3));
                }
                te.setSstKijunCdSet(ary); // sskkijuncd
                collection.add(te);
            }
            rs.close();
            closeStatement(st);
            closeConnection(con);
            return collection;
        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     *
     * @param category
     * @return
     */
    public List<AdminEntry> getAdminByCategoryFromOrca(String category) {
        Connection con = null;
        List<AdminEntry> collection = null;
        Statement st = null;

        StringBuilder sql = new StringBuilder();
        sql.append("select srycd, name ");
        sql.append(" ,sstkijuncd1,sstkijuncd2,sstkijuncd3,sstkijuncd4,sstkijuncd5,sstkijuncd6,sstkijuncd7,sstkijuncd8,sstkijuncd9,sstkijuncd10 ");
        sql.append(" from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }

        sql.append(" ( srycd ~ '^");

        int index = category.indexOf(' ');
        if (index > 0) {
            String s1 = category.substring(0, index);
            String s2 = category.substring(index + 1);
            sql.append(s1);
            sql.append("' or srycd ~ '^");
            sql.append(s2);
            sql.append("'");

        } else {
            sql.append(category);
            sql.append("'");
        }
        sql.append(" or ");
        sql.append("srycd ~ '^810000001$' or ");
        sql.append("srycd ~ '^0082' or srycd ~'^82' or ");
        sql.append("srycd ~ '^0083' or srycd ~'^83' or ");
        sql.append("srycd ~ '^0084' or srycd ~'^84')");
        sql.append(" order by srycd");

        //      printTrace(sql.toString());

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql.toString());

            // ValueObject
            AdminEntry te = null;
            collection = new ArrayList<AdminEntry>();

            while (rs.next()) {
                te = new AdminEntry();
                te.setCode(rs.getString(1));            // srycd
                te.setName(rs.getString(2));            // name
                collection.add(te);
            }
            rs.close();

            closeStatement(st);
            closeConnection(con);
            return collection;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        return null;
    }

    /**
     *
     * @param sortBy
     * @param order
     * @return
     */
    private String getOrderBy(String sortBy, String order) {
        StringBuilder buf = null;
        if (sortBy != null) {
            buf = new StringBuilder();
            buf.append(" order by ");
            buf.append(sortBy);
        }
        if (order != null) {
            buf.append(" ");
            buf.append(order);
        }
        return (buf != null) ? buf.toString() : null;
    }

    /**
     *
     * @param code
     * @return
     */
    public List<Pair<String, String>> getNameByCodesFromOrca(String code) {
        final List<Pair<String, String>> collection = new ArrayList<Pair<String, String>>();
        String sql = "select srycd, name from tbl_tensu where srycd = " + addSingleQuote(code);
        executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

            @Override
            public boolean onResult(ResultSet records, Object result) throws Exception {
                collection.add(new Pair<String, String>(records.getString(1), records.getString(2)));
                return true;
            }

            @Override
            public void onError(Exception ex) {
            }
        });
        return collection;
    }

    /**
     *
     * @param code
     * @return
     */
    public String getNameByCode(String code) {
        List<Pair<String, String>> codes = getNameByCodesFromOrca(code);
        if (!codes.isEmpty()) {
            Pair<String, String> codePair = codes.get(0);
            if (codePair != null) {
                return codePair.value;
            }
        }
        return "";
    }

    /**
     *   相互作用マスタを検索する
     * @param text コード
     */
    private List<InteractEntry> getInteractFromOrca(String code) {
        final List<InteractEntry> collection = new ArrayList<InteractEntry>();
        String sql = "select drugcd, drugcd2, syojyoucd from tbl_interact where drugcd = " + addSingleQuote(code);

        executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

            @Override
            public boolean onResult(ResultSet records, Object result) throws Exception {
                collection.add(new InteractEntry(records.getString(1), records.getString(2), records.getString(3)));
                return true;
            }

            @Override
            public void onError(Exception ex) {
            }
        });
        return collection;
    }

    /**
     *   症状措置機序マスタを検索する
     * @param code 
     * @return
     */
    public List<SsKijyoEntry> getSsKijyoFromOrca(String code) {
        final List<SsKijyoEntry> collection = new ArrayList<SsKijyoEntry>();
        String sql = "select syojyoucd, syojyou, sayokijyo from tbl_sskijyo where syojyoucd = " + addSingleQuote(code);
        executeQuery(sql, null,
                new GenericAdapter<ResultSet, Object>() {

                    /**
                     *
                     */
                    @Override
                    public boolean onResult(ResultSet records, Object result) throws Exception {
                        collection.add(new SsKijyoEntry(records.getString(1), records.getString(2), records.getString(3)));
                        return true;
                    }

                    /**
                     *
                     */
                    @Override
                    public void onError(Exception ex) {
                    }
                });
        return collection;
    }

    /**
     *
     * @param interact_code
     * @param onKarteCode
     * @param symptoms
     */
    public void getSsKijyoEntry(String interact_code, List<String> onKarteCode, List<Pair<InteractEntry, SsKijyoEntry>> symptoms) {
        List interactsFrom = getInteractFromOrca(interact_code);
        for (Object interact : interactsFrom) {
            for (String code : onKarteCode) {
                if (((InteractEntry) interact).getPharmaceuticalsCodeTo().equals(code)) {
                    List sskijyos = getSsKijyoFromOrca(((InteractEntry) interact).getSymptomsCode());
                    for (Object sskijyo : sskijyos) {
                        symptoms.add(new Pair<InteractEntry, SsKijyoEntry>((InteractEntry) interact, (SsKijyoEntry) sskijyo));
                    }
                }
            }
        }
    }

    /**
     * 医薬品マスタを検索する。
     * @param code
     * @param collection 
     * @return	医薬品リスト
     */
    public boolean getAlternateEntry(String code, List<MedicineEntry> collection) {
        boolean result = false;
        if (!getMedicineEntry(code, collection)) {
            {
                List<String> alternateCodes = new ArrayList<String>();
                if (getUpdatesFromOrca(code, alternateCodes)) {
                    for (String alternateCode : alternateCodes) {
                        if (getMedicineEntry(alternateCode, collection)) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * @param code
     * @param collection
     * @return
     */
    public boolean getMedicineEntry(String code, final List<MedicineEntry> collection) {

        String sql = "select srycd,name,kananame,tanicd,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where srycd= " + addSingleQuote(code) + " order by yukoedymd desc offset 0 limit 1";
        Object result = executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

            /**
             *
             */
            @Override
            public boolean onResult(ResultSet records, Object result) throws Exception {
                if (!DateExpire.expire(records.getString(9), records.getString(10))) {
                    MedicineEntry me = new MedicineEntry();
                    me.setCode(records.getString(1));        // Code
                    me.setName(records.getString(2));        // Name
                    me.setKana(records.getString(3));        // Name
                    me.setUnit(OrcaUnitTranslater.toName(records.getString(4)));        // Unit
                    me.setCostFlag(records.getString(5));    // Cost flag
                    me.setCost(records.getString(6));        // Cost
                    me.setYkzKbn(records.getString(7));      // 薬剤区分
                    me.setJNCD(records.getString(8));        // JNCD
                    me.setStartDate(records.getString(9));   // startDate
                    me.setEndDate(records.getString(10));    // endDate
                    collection.add(me);
                    return true;
                }
                return false;
            }

            /**
             *
             */
            @Override
            public void onError(Exception ex) {
            }
        });
        return (!collection.isEmpty());
        //  return result != null;
    }

    /**
     *
     * @param code
     * @param collection
     * @return
     */
    public boolean getAllMedicineEntry(String code, final List<MedicineEntry> collection) {

        String sql = "select srycd,name,kananame,tanicd,tensikibetu,ten,ykzkbn,yakkakjncd,yukostymd,yukoedymd from tbl_tensu where srycd= " + addSingleQuote(code) + " order by yukoedymd desc offset 0 limit 1";
        Object result = executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

            @Override
            public boolean onResult(ResultSet records, Object result) throws Exception {

                MedicineEntry me = new MedicineEntry();
                me.setCode(records.getString(1));        // Code
                me.setName(records.getString(2));        // Name
                me.setKana(records.getString(3));        // Name
                me.setUnit(OrcaUnitTranslater.toName(records.getString(4)));        // Unit
                me.setCostFlag(records.getString(5));    // Cost flag
                me.setCost(records.getString(6));        // Cost
                me.setYkzKbn(records.getString(7));      // 薬剤区分
                me.setJNCD(records.getString(8));        // JNCD
                me.setStartDate(records.getString(9));   // startDate
                me.setEndDate(records.getString(10));    // endDate
                collection.add(me);
                return true;

            }

            /**
             *
             */
            @Override
            public void onError(Exception ex) {
            }
        });
        return (!collection.isEmpty());
        //  return result != null;
    }

    /**
     *   一般老人置換マスタを検索する
     * @param text コード
     */
    private boolean getUpdatesFromOrca(String code, final List<String> collection) {

        String sql = "select ipnsrycd, rjnsrycd from tbl_srycdchg where ipnsrycd = " + addSingleQuote(code);
        executeQuery(sql, null, new GenericAdapter<ResultSet, List<String>>() {

            @Override
            public boolean onResult(ResultSet records, List<String> result) throws Exception {
                collection.add(records.getString(2));
                return true;
            }

            @Override
            public void onError(Exception ex) {
            }
        });
        return (!collection.isEmpty());
    }

    /**
     *
     * @param code
     * @param now
     * @return
     */
    public Availability isCodeNoLongerAvailableFromOrca(String code, final java.util.Date now) {
        //   final HashSet<String> result = new HashSet<String>();
        Availability result = Availability.AVAILABLE;
        final boolean[] available = new boolean[1];     //finalには代入できないのでfinalの要素に代入するため配列である（*booleanの意)
        final int[] count = new int[1];                 //finalには代入できないのでfinalの要素に代入するため配列である（*intの意)
        available[0] = false;
        count[0] = 0;

        String sql = "select yukostymd, yukoedymd from tbl_tensu where srycd= " + addSingleQuote(code) + " order by yukoedymd desc offset 0 limit 1";
        executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

            @Override
            public boolean onResult(ResultSet records, Object d) throws Exception {
                available[0] = true;
                if (!DateExpire.expire(records.getString(1), records.getString(2))) {
                    count[0] += 1;
                    //            result.add(records.getString(2));
                }
                return false;
            }

            @Override
            public void onError(Exception ex) {
            }
        });

        if (available[0]) {
            if (count[0] > 0) {
                result = Availability.AVAILABLE;
            } else {
                result = Availability.NO_LONGER_AVAILABLE;
            }
        } else {
            result = Availability.NO_CODE;
        }
        return result;
        //      return (result.isEmpty());
    }

    /**
     * @param code
     * @return
     */
    public List<String> getPeriodDatesFromOrca(String code) {
        final List<String> result = new ArrayList<String>();
        String sql = "select srycd,name,kananame,yukostymd,yukoedymd from tbl_tensu where srycd= " + addSingleQuote(code) + " order by yukoedymd desc offset 0 limit 1";
        executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

            @Override
            public boolean onResult(ResultSet records, Object x) throws Exception {
                result.add(records.getString(1));
                result.add(records.getString(2));
                result.add(records.getString(3));
                result.add(records.getString(4));
                result.add(records.getString(5));
                return false;
            }

            @Override
            public void onError(Exception ex) {
            }
        });
        return result;
    }

    /**
     *
     * @param code
     * @return
     */
    public String getClassCodeFromOrca(String code) {

        final List<String> collection = new ArrayList<String>();
        //     try {
        StringBuilder sql = new StringBuilder();
        sql.append("select srysyukbn, yukostymd, yukoedymd from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("srycd = ");
        sql.append(addSingleQuote(code));

        executeQuery(sql.toString(), null,
                new GenericAdapter<ResultSet, Object>() {

                    @Override
                    public boolean onResult(ResultSet records, Object result) throws Exception {
                        collection.add(records.getString(1));
                        return true;
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                });
        //    } finally {
        //    }
        if (collection.size() > 0) {
            return collection.get(0);
        } else {
            return "";
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public String getIdokanrenFromOrca(String code) {

        final List<String> collection = new ArrayList<String>();
        //    try {
        StringBuilder sql = new StringBuilder();
        sql.append("select idokanren from tbl_tensu where ");
        if (GlobalVariables.getOrcaVersion().startsWith("4")) {
            int hospnum = getHospNumFromOrca();
            sql.append("hospnum=");
            sql.append(hospnum);
            sql.append(" and ");
        }
        sql.append("srycd = ");
        sql.append(addSingleQuote(code));

        executeQuery(sql.toString(), null,
                new GenericAdapter<ResultSet, Object>() {

                    @Override
                    public boolean onResult(ResultSet records, Object result) throws Exception {
                        collection.add(records.getString(1));
                        return true;
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                });
        //      } finally {
        //   }
        if (collection.size() > 0) {
            return collection.get(0);
        } else {
            return "";
        }
    }

    /**
     * @param srycd
     * @param connection
     * @param statement
     * @return
     */
    public String[] getTblTensuFromOrca(String srycd, Connection connection, Statement statement) {
        final String[] result = new String[3];
        try {
            String sql = "select NAME,TANINAME,SRYKBN from tbl_tensu where SRYCD=" + addSingleQuote(srycd);
            executeQueryContinueas(sql, null, connection, statement, new GenericAdapter<ResultSet, Object>() {

                @Override
                public boolean onResult(ResultSet records, Object x) throws Exception {
                    result[0] = records.getString(1);
                    result[1] = records.getString(2);
                    result[2] = records.getString(3);
                    return false;
                }

                @Override
                public void onError(Exception ex) {
                }
            });

        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return result;
    }

    /**
     *
     * @param sql
     * @param userObject
     * @param adapter
     * @return
     */
    public Object executeQuery(String sql, Object userObject, GenericAdapter adapter) {
        try {
            //        semaphore.acquire();
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = null;
            try {
                results = statement.executeQuery(sql);
                while (results.next()) {
                    if (!adapter.onResult(results, userObject)) {
                        return userObject;
                    }
                }
            } finally {
                results.close();
                closeStatement(statement);
                closeConnection(connection);
            }
        } catch (Exception ex) {
            adapter.onError(ex);
            //   } finally {
            //       semaphore.release();
        }
        return userObject;
    }

    /**
     *
     * @param sql
     * @param userObject
     * @param connection
     * @param statement
     * @param adapter
     * @return
     */
    public Object executeQueryContinueas(String sql, Object userObject, Connection connection, Statement statement, GenericAdapter adapter) {
        try {
            ResultSet results = null;
            try {
                results = statement.executeQuery(sql);
                while (results.next()) {
                    if (!adapter.onResult(results, userObject)) {
                        return userObject;
                    }
                }
            } finally {
                results.close();
            }
        } catch (Exception ex) {
            adapter.onError(ex);
            //     } finally {
        }
        return userObject;
    }

    /**
     *
     * @param code
     */
    public void setTransactionISolationLevel(String code) {
        final StringBuilder sql = new StringBuilder();
        sql.append("SET TRANSACTION ISOLATION LEVEL ");
        sql.append(code);

        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.execute(sql.toString());
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    /**
     * @param inputcd
     * @return
     */
    public String InputCdToCode(String inputcd) {
        final String[] result = new String[1];
        result[0] = inputcd;
        try {
            String sql = "select srycd from tbl_inputcd where inputcd=" + addSingleQuote(inputcd);
            executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

                @Override
                public boolean onResult(ResultSet records, Object x) throws Exception {
                    result[0] = records.getString(1).trim();
                    return false;
                }

                @Override
                public void onError(Exception ex) {
                }
            });

        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return result[0];
    }
    /**
     *
     * @param inputcd
     * @return
     */
    public ArrayList<String> InputCdToCodes(String inputcd) {
        boolean find = false;
        final ArrayList<String> result = new ArrayList<String>();

        Connection con = null;
        Statement st = null;
        String sql = "select srycd from tbl_inputcd where inputcd ~ '^" + inputcd;
        sql = sql + "'";

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                result.add(rs.getString(1).trim());
                find = true;
            }
            rs.close();
            if (!find)
                result.add(StringTool.toZenkakuUpperLower(inputcd));
            closeStatement(st);
            closeConnection(con);
            return result;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        result.add(StringTool.toZenkakuUpperLower(inputcd));
        return result;
    }

    /**
     * @param inputcd
     * @return
     */
    public String UserByoumeiToCode(String inputcd) {
        final String[] result = new String[1];
        result[0] = inputcd;
        try {
            String sql = "select byomei from tbl_userbyomei where cdsyu!='C' and byomeiinputcd=" + addSingleQuote(inputcd);
            executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

                @Override
                public boolean onResult(ResultSet records, Object x) throws Exception {
                    result[0] = records.getString(1).trim();
                    return false;
                }

                @Override
                public void onError(Exception ex) {
                }
            });

        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return result[0];
    }

    /**
     * @param inputcd
     * @return
     */
    public ArrayList<String> UserByoumeiToCodes(String inputcd) {
        boolean find = false;
        final ArrayList<String> result = new ArrayList<String>();

        Connection con = null;
        Statement st = null;
        String sql = "select byomei from tbl_userbyomei where cdsyu!='C' and byomeiinputcd ~ '^" + inputcd;
        sql = sql + "'";

        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                result.add(rs.getString(1).trim());
                find = true;
            }
            rs.close();
            if (!find)
                result.add(StringTool.toZenkakuUpperLower(inputcd));
            closeStatement(st);
            closeConnection(con);
            return result;

        } catch (Exception e) {
            processError(e);
            closeStatement(st);
            closeConnection(con);
        }
        result.add(StringTool.toZenkakuUpperLower(inputcd));
        return result;
    }
}
