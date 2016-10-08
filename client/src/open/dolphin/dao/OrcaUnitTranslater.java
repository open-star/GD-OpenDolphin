/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.dao;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author
 */
public class OrcaUnitTranslater {

    /**
     *
     */
    private static final Map<String, String> table = new HashMap<String, String>();

    static {
        table.put("001", "分");
        table.put("002", "回");
        table.put("003", "種");
        table.put("004", "箱");
        table.put("005", "巻");
        table.put("006", "枚");
        table.put("007", "本");
        table.put("008", "組");
        table.put("009", "セット");
        table.put("010", "個");
        table.put("011", "裂");
        table.put("012", "方向");
        table.put("013", "トローチ");
        table.put("014", "アンプル");
        table.put("015", "カプセル");
        table.put("016", "錠");
        table.put("017", "丸");
        table.put("018", "包");
        table.put("019", "瓶");
        table.put("020", "袋");
        table.put("021", "瓶（袋） ");
        table.put("022", "管");
        table.put("023", "シリンジ");
        table.put("024", "回分");
        table.put("025", "テスト分");
        table.put("026", "ガラス筒");
        table.put("027", "桿錠");
        table.put("028", "単位");
        table.put("029", "万単位");
        table.put("030", "フィート");
        table.put("031", "滴");
        table.put("032", "ｍｇ ");
        table.put("033", "ｇ ");
        table.put("034", "Ｋｇ ");
        table.put("035", "ｃｃ ");
        table.put("036", "ｍＬ ");
        table.put("037", "Ｌ ");
        table.put("038", "ｍＬＶ ");
        table.put("039", "バイアル");
        table.put("040", "ｃｍ ");
        table.put("041", "ｃｍ２ ");
        table.put("042", "ｍ ");
        table.put("043", "μＣｉ ");
        table.put("044", "ｍＣｉ ");
        table.put("045", "μｇ");
        table.put("046", "管（瓶）");
        table.put("047", "筒");
        table.put("048", "ＧＢｑ");
        table.put("049", "ＭＢｑ");
        table.put("050", "ＫＢｑ");
        table.put("051", "キット");
        table.put("052", "国際単位");
        table.put("053", "患者当り");
        table.put("054", "気圧");
        table.put("055", "缶");
        table.put("056", "手術当り");
        table.put("057", "容器");
        table.put("058", "ｍＬ（ｇ）");
        table.put("059", "ブリスター");
        table.put("060", "シート");
        table.put("101", "分画");
        table.put("102", "染色");
        table.put("103", "種類");
        table.put("104", "株");
        table.put("105", "菌株");
        table.put("106", "照射");
        table.put("107", "臓器");
        table.put("108", "件");
        table.put("109", "部位");
        table.put("110", "肢");
        table.put("111", "局所");
        table.put("112", "種目");
        table.put("113", "スキャン");
        table.put("114", "コマ");
        table.put("115", "処理");
        table.put("116", "指");
        table.put("117", "歯");
        table.put("118", "面");
        table.put("119", "側");
        table.put("120", "個所");
        table.put("121", "日");
        table.put("122", "椎間");
        table.put("123", "筋");
        table.put("124", "菌種");
        table.put("125", "項目");
        table.put("126", "箇所");
        table.put("127", "椎弓");
        table.put("128", "食");
    }

    /**
     *
     * @param code　コード
     * @return
     */
    public static String toName(String code) {
        String result = table.get(code);
        if (result == null) {
            return code;
        }
        return result;
    }
}
