/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.dao;

import open.dolphin.utils.StringTool;
import java.sql.*;
import java.util.*;

import open.dolphin.infomodel.DiseaseEntry;
import open.dolphin.log.LogWriter;
import open.dolphin.order.ClaimConst;

/**
 * SqlMasterDao　MEMO:DAO
 *
 * @author Kazushi Minagawa
 */
public final class SqlDiseaseMasterDao extends SqlDaoBean {

    //   private static final String MEDICINE_CODE = "20";
    //  private static final String YKZKBN = ClaimConst.YKZ_KBN_INJECTION;	// 薬剤区分
    private int totalCount;
    private static final String CODE_FILLER = "ZZZ";
    private static final String PREFIX_CODE1 = CODE_FILLER + '1';
    private static final String PREFIX_CODE2 = CODE_FILLER + '2';
    private static final String TYPE_CODE1 = CODE_FILLER + '3';
    private static final String TYPE_CODE2 = CODE_FILLER + '4';
    private static final String PART_CODE = CODE_FILLER + '7';
    private static final String POSTFIX_CODE = CODE_FILLER + '8';

    /** Creates a new instance of SqlMasterDao */
    public SqlDiseaseMasterDao() {
        super();
    }

    /**
     *
     * @return　合計数
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     *
     * @param code
     * @return　コード
     */
    public String toLongCode(String code) {

        if (code.length() == 4) {
            return CODE_FILLER + code;
        }
        return code;
    }

    /**
     *
     * @param code　コード
     * @return
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
        List results = null;

        // 戻り値のリストを用意する
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
                results = getDiseaseNames(name);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.PREFIX.getName())) {
                results = getDiseasePrefixFromOrca(name, sortBy);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.TYPE.getName())) {
                results = getDiseaseTypeFromOrca(name, sortBy);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.PART.getName())) {
                results = getDiseasePartFromOrca(name, sortBy);
            } else if (serchClassCode.equals(ClaimConst.SubTypeSet.POSTFIX.getName())) {
                results = getDiseasePostfixFromOrca(name, sortBy);
            }
        } else {
            throw new RuntimeException("Unsupported master: " + master);
        }
        return results;
    }

    /**
     * <p>指定された項目名に合致する傷病名クラスの一覧を返す。
     * @param text　項目名
     * @param startsWith
     * @param sortBy ソートするカラム名
     * @param order ASC、または DESC
     * @return　傷病名クラスの一覧
     */
    private List<DiseaseEntry> getDiseaseByName(String text, boolean startsWith, String sortBy, String order) {
        List<DiseaseEntry> result = null;

        // 前方一致検索を行う。startsWith が true ならいきなり部分検索。

        String sql = null;
        if (!startsWith) {
            sql = buildDiseaseSql("byomei ~ ", text, sortBy, order, true);
            result = getDiseaseCollection(sql);
        }

        // NoError で結果がないとき部分一致検索を行う
        if (isNoError() && (result == null || result.size() == 0)) {
            sql = buildDiseaseSql("byomei ~ ", text, sortBy, order, false);
            result = getDiseaseCollection(sql);
        }

        return result;
    }

    /**
     *
     * @param text　項目名
     * @param startsWith
     * @param sortBy ソートするカラム
     * @param order ASC、または DESC
     * @return 病名一覧
     */
    private List<DiseaseEntry> getDiseaseByCode(String text, boolean startsWith, String sortBy, String order) {

        // 前方一致検索を行う。startsWith が true ならいきなり部分検索。
        List<DiseaseEntry> ret = null;
        String sql = null;
        if (!startsWith) {
            sql = buildDiseaseSql("byomeicd ~ ", text, sortBy, order, true);
            ret = getDiseaseCollection(sql);
        }

        // NoError で結果がないとき部分一致検索を行う
        if (isNoError() && (ret == null || ret.size() == 0)) {
            sql = buildDiseaseSql("byomeicd ~ ", text, sortBy, order, true);
            ret = getDiseaseCollection(sql);
        }
        return ret;
    }

    /**
     *
     * @param text　項目名
     * @return 病名一覧
     */
    private List<DiseaseEntry> getDiseaseNames(String text) {
        List<DiseaseEntry> result = new ArrayList<DiseaseEntry>();
        String key = text;
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
            result.addAll(getDiseaseNameFromOrca(_key));
        }

        return result;
    }

    /**
     *　"select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomei ~ '^"
     * 　　+ key + "'"
     * @param text 
     * @return
     */
    private int matchMostLongKeywordFromOrca(String text) {
        List<DiseaseEntry> result = null;
        String key;
        int index;
        for (index = 0; index < text.length(); index++) {
            key = text.substring(0, index + 1);
            result = getDiseaseCollection("select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomei ~ '^" + key + "'");
            if (result.size() == 0) {
                break;
            }
        }
        return index;
    }

    /**
     * "select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomei = '"
     *    + text + "'"
     * @param text 病名
     * @return 病名一覧
     */
    private List<DiseaseEntry> getDiseaseNameFromOrca(String text) {
        return getDiseaseCollection("select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomei = '" + text + "'");
    }

    /**
     * "select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomeicd = '"
     *   + toLongCode(text) + "'"
     * @param text 病名コード
     * @return 病名一覧
     */
    public List<DiseaseEntry> getDiseaseCodeFromOrca(String text) {
        return getDiseaseCollection("select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomeicd = '" + toLongCode(text) + "'");
    }

    /**
     * "select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomeicd ~ '^"
     *   + PREFIX_CODE1 + "' or byomeicd ~ '^" + PREFIX_CODE2 + "'" + order
     * @param text 項目名
     * @param sortBy ソートするカラム
     * @return 病名一覧
     */
    private List<DiseaseEntry> getDiseasePrefixFromOrca(String text, String sortBy) {

        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        String query = "select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where byomeicd ~ '^" + PREFIX_CODE1 + "' or byomeicd ~ '^" + PREFIX_CODE2 + "'" + order;
        if (!StringTool.isEmptyString(text)) {
            query = "select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where (byomei ~ '" + text + "') and (byomeicd ~ '^" + PREFIX_CODE1 + "' or byomeicd ~ '^" + PREFIX_CODE2 + "')" + order;
        }

        return getDiseaseCollection(query);
    }

    /**
     * select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where (byomei ~ '"
     *   + text + "') and (byomeicd ~ '^" + TYPE_CODE1 + "' or byomeicd ~ '^" + TYPE_CODE2 + "')" + order
     * @param text 病名
     * @param sortBy ソートするカラム
     * @return 病名一覧
     */
    private List<DiseaseEntry> getDiseaseTypeFromOrca(String text, String sortBy) {

        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        return getDiseaseCollection("select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where (byomei ~ '" + text + "') and (byomeicd ~ '^" + TYPE_CODE1 + "' or byomeicd ~ '^" + TYPE_CODE2 + "')" + order);
    }

    /**
     * select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where (byomei ~ '"
     * 　　+ text + "') and byomeicd ~ '^" + PART_CODE + "'" + order
     * @param text 病名
     * @param sortBy ソートするカラム名
     * @return 病名一覧
     */
    private List<DiseaseEntry> getDiseasePartFromOrca(String text, String sortBy) {

        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        return getDiseaseCollection("select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where (byomei ~ '" + text + "') and byomeicd ~ '^" + PART_CODE + "'" + order);
    }

    /**
     * "select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei
     *   where (byomei ~ '" + text + "') and byomeicd ~ '^" + POSTFIX_CODE + "'" + order
     * @param text 病名
     * @param sortBy ソートするカラム名
     * @return 病名一覧
     */
    private List<DiseaseEntry> getDiseasePostfixFromOrca(String text, String sortBy) {

        String order = "";
        if (!StringTool.isEmptyString(sortBy)) {
            order = " order by " + sortBy;
        }

        return getDiseaseCollection("select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where (byomei ~ '" + text + "') and byomeicd ~ '^" + POSTFIX_CODE + "'" + order);
    }

    /**
     *　病名SQL文を作成する
     * select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where
     * @param field　フィールド名
     * @param text 項目名
     * @param sortBy ソートするカラム名
     * @param order ASC、または DESC
     * @param forward
     * @return　SQL文
     */
    private String buildDiseaseSql(String field, String text, String sortBy, String order, boolean forward) {
        String word = null;
        StringBuilder buf = new StringBuilder();

        buf.append("select byomeicd, byomei, byomeikana, icd10_1, haisiymd from tbl_byomei where ");

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

        String sql = buf.toString();

        return sql;
    }

    /**
     *
     * @param sql　SQL文
     * @return 傷病名の一覧
     */
    private List<DiseaseEntry> getDiseaseCollection(String sql) {
        try {
            //        semaphore.acquire();
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
                    de.setKana(rs.getString(3));         // カナ
                    de.setIcdTen(rs.getString(4));      // ICD10
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
        } catch (Exception ex) {
             LogWriter.error(getClass(), ex);
  //      } finally {

        }
        return null;
    }

    /**
     * 文字列にORDER BY 句を追加する
     * @param sortBy ソートするカラム名
     * @param order ASC、または DESC
     * @return ORDER BY 句が追加された文字列
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
}
