/*
 * ClaimItem.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
 * Copyright (C) 2003,2004 Digital Globe, Inc. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *	
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *	
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package open.dolphin.infomodel;

import open.dolphin.utils.CombinedStringParser;
import java.io.IOException;
import java.io.Writer;
import java.util.GregorianCalendar;
import java.util.List;
import open.dolphin.queries.DolphinQuery;
import open.dolphin.utils.DateExpire;
import open.dolphin.utils.StringTool;

/**
 * カルテ要素 ORCAマスターのデータを元にする MEMO:マッピング
 * ClaimItem 
 *
 * @author Kazushi Minagawa, Digital Globe,Inc. 
 */
public class ClaimItem extends InfoModel {

    private String name;//名前
    private String code;//コード
    private String codeSystem;//コードシステム
    private String classCode;//クラスコード
    private String classCodeSystem;//クラスコードシステム
    private String number;//数量
    private String unit;//単位
    private String numberCode;//ナンバーコード
    private String numberCodeSystem;//ナンバーコードシステム
    private String memo;//メモ
    private String ykzkbn;//薬剤区分
    private List<String> sstKijunCdSet;//SST基準コード
    private float suryo1;
    private float suryo2;
    private String startDate;//開始日
    private String endDate;//終了日

    /**
     * Creates new ClaimItem
     * 本来レコードが持っていないフィールドを追加するため
     * numberCodeSystemフィールドにシリアライズ
     * 
     * 0 setNumberCodeSystem
     * 1 memo
     * 2 kana
     * 3 disUseDate
     */
    public ClaimItem() {

        if (numberCodeSystem == null) {
            CombinedStringParser line = new CombinedStringParser('|', "");
            line.limit(4);
            line.set(0, "");
            line.set(1, "");
            line.set(2, "");
            line.set(3, "");

            numberCodeSystem = line.toCombinedString();
        } else {
            CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);
            line.limit(4);
            //  line.set(0, numberCodeSystem);
            line.set(1, "");
            line.set(2, "");
            line.set(3, "");

            numberCodeSystem = line.toCombinedString();
        }
    }

    /**
     *
     * ORCAマスターのデータを元にするコンストラクタ
     *
     * @param item
     */
    public ClaimItem(MasterItem item) {
        this();

        code = item.getCode();
        name = item.getName();
        setKana(item.getKana());
        startDate = item.getStartDate();
        endDate = item.getEndDate();
        setDisUseDate(item.getDisUseDate());
        classCode = Integer.toString(item.getClassCode());
    }

    /**
     *
     * @param code
     * @param number
     */
    public ClaimItem(String code, String number) {
        this();
        this.code = code;
        setNumber(number);
    }

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (name != null) {
            if (name.indexOf(query.what("keyword")) != -1) {
                return true;
            }
        }
        if (memo != null) {
            if (memo.indexOf(query.what("keyword")) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     *　名前のGetter
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前のSetter
     * @param val 名前
     */
    public void setName(String val) {
        name = val;
    }

    /**
     * コードのGettter
     * @return コード
     */
    public String getCode() {
        return code;
    }

    /**
     * コードのSetter
     * @param val コード
     */
    public void setCode(String val) {
        code = val;
    }

    /**
     * コードシステムのGetter
     * @return コードシステム
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    /**
     * コードシステムのSetter
     * @param val コードシステム
     */
    public void setCodeSystem(String val) {
        codeSystem = val;
    }

    /**
     * クラスコードのGetter
     * @return クラスコード
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * クラスコードのSetter
     * @param val クラスコード
     */
    public void setClassCode(String val) {
        classCode = val;
    }

    /**
     * クラスコードシステムのGetter
     * @return クラスコードシステム SUBCLASS_CODE_ID = "Claim003" 手技、材料、薬剤区分テーブルID
     */
    public String getClassCodeSystem() {
        return classCodeSystem;
    }

    /**
     * クラスコードシステムのSetter
     * @param val クラスコードシステム SUBCLASS_CODE_ID = "Claim003" 手技、材料、薬剤区分テーブルID
     */
    public void setClassCodeSystem(String val) {
        classCodeSystem = val;
    }

    /**
     * 数量のGetter
     * @return 数量
     */
    public String getNumber() {
        return number;
    }

    /**
     * 画像撮影の場合、ORCAが勝手にフィルム枚数と撮影回数を一致させてしまうバグに対応。
     * なぜか、2切なら"1-2"のように枚数を送る必要があるらしい。
     * なので、Claimを送る際に以下のようにするためのメソッド
     *
     * >${item.getNumber()+item.getFilmCutNumber()}</claim:number>
     *
     * @return
     */
    public String getFilmCutNumber() {
        /*
        "700060000"//大角２切
        "700070000"//大角４切
        "700040000"//大角２切
        "700020000"//半角２切
        "700090000"//四つ切２切
        "700120000"//六つ切２切
        "700100000"//四つ切４切
        "700130000"//六つ切４切
        "700150000"//八つ切２切
         * */
        if (code.trim().equals("700060000") || code.trim().equals("700040000") || code.trim().equals("700020000") || code.trim().equals("700090000") || code.trim().equals("700120000") || code.trim().equals("700150000")) {
            return "-2";
        }
        if (code.trim().equals("700070000") || code.trim().equals("700100000") || code.trim().equals("700130000")) {
            return "-4";
        }
        return "";
    }

    /**
     * 数量のSetter
     * @param val 数量
     */
    public final void setNumber(String val) {
        number = StringTool.zenkakuNumToHankaku(val);
    }

    /**
     * 単位のGetter
     * @return 単位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 単位のSetter
     * @param val 単位
     */
    public void setUnit(String val) {
        unit = val;
    }

    /**
     * ナンバーコードのGetter
     * @return ナンバーコード
     */
    public String getNumberCode() {
        if (number == null) {
            return "";
        }
        return numberCode;
    }

    /**
     * ナンバーコードのSetter
     * @param val ナンバーコード
     */
    public void setNumberCode(String val) {
        numberCode = val;
    }

    /**
     * ナンバーコードシステムのGetter
     * @return ナンバーコードシステム
     */
    public String getNumberCodeSystem() {
        CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);
        //   line.limit(6);
        line.limit(4);
        return line.get(0);
    }

    /**
     * ナンバーコードシステムのSetter
     * @param val ナンバーコードシステム
     */
    public void setNumberCodeSystem(String val) {
        CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);
        // line.limit(6);
        line.limit(4);
        line.set(0, val);
        numberCodeSystem = line.toCombinedString();
    }

    /**
     * メモのGetter
     * @return メモ
     */
    public String getMemo() {
        CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);
        //  line.limit(6);
        line.limit(4);
        return line.get(1);
    }

    /**
     * メモのSetter
     * @param val メモ
     */
    public void setMemo(String val) {
        CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);
        //  line.limit(6);
        line.limit(4);
        line.set(1, val);
        memo = line.toCombinedString();
    }

    /**
     * 薬剤区分のGetter
     * @return 薬剤区分
     */
    public String getYkzKbn() {
        return ykzkbn;
    }

    /**
     * 薬剤区分のSetter
     * @param ykzkbn 薬剤区分
     */
    public void setYkzKbn(String ykzkbn) {
        this.ykzkbn = ykzkbn;
    }

    /**
     * SST基準コードのGetter
     * @return SST基準コード
     */
    public List<String> getSstKijunCdSet() {
        return sstKijunCdSet;
    }

    /**
     * SST基準コードのSetter
     * @param val SST基準コード
     */
    public void setSstKijunCdSet(List<String> val) {
        sstKijunCdSet = val;
    }

    /**
     * suryo1のGetter
     * @return suryo1
     */
    public float getSuryo1() {
        return suryo1;
    }

    /**
     * suryo1のSetter
     * @param val suryo1
     */
    public void setSuryo1(float val) {
        suryo1 = val;
    }

    /**
     * suryo2のGetter
     * @return val suryo2
     */
    public float getSuryo2() {
        return suryo2;
    }

    /**
     * suryo2のSetter
     * @param val suryo2
     */
    public void setSuryo2(float val) {
        suryo2 = val;
    }

    /**
     * 全角スペースを半角スペースに置き換えた名前
     * @return　全角スペースを半角スペースに置き換えた名前
     */
    public String getNameReplaceToHankakuSpace() {
        return name.replaceAll("　", " ");
    }

    /**
     *　コメントコードが複合コメントか
     * @return 複合コメントなら真
     */
    public boolean isComplexCommentCode() {
        // TODO ハードコード
        boolean rtn;
        if (code.startsWith("84") || code.startsWith("0084")) {
            rtn = true;
        } else {
            rtn = false;
        }
        return rtn;
    }

    /**
     *　カナのGetter
     * @return　カナ
     */
    public String getKana() {
        CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);

        line.limit(4);
        return line.get(2);
    }

    /**
     *　カナのSetter
     * @param kana カナ
     */
    public final void setKana(String kana) {
        if (kana != null) {
            CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);

            line.limit(4);
            line.set(2, kana);
            numberCodeSystem = line.toCombinedString();
        }
    }

    /**
     * 開始日のGetter
     * @return 開始日
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 開始日のSetter
     * @param startDate 開始日
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 終了日のGetter
     * @return 終了日
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     *　終了日のSetter
     * @param endDate 終了日
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 使用終了日のGetter
     * @return 使用終了日
     */
    public String getDisUseDate() {
        CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);
        line.limit(4);
        return line.get(3);
    }

    /**
     *　使用終了日のSetter
     * @param disUseDate 使用終了日
     */
    public final void setDisUseDate(String disUseDate) {
        if (disUseDate != null) {
            CombinedStringParser line = new CombinedStringParser('|', numberCodeSystem);
            line.limit(4);
            line.set(3, disUseDate);
            numberCodeSystem = line.toCombinedString();
        }
    }

    /**
     *　期限切れチェック
     * @param gc カレンダー
     * @return 今日が開始日より前なら0、開始日より後で終了日より前なら1、終了日より後なら2、終了日が無効なら1
     */
    protected int useState(GregorianCalendar gc) {
        return DateExpire.expireState(gc, startDate, endDate);
    }

    /**
     * 期限切れチェック
     * @return　期限切れでなければ真
     */
    protected boolean isExpired() {
        return DateExpire.expire(startDate, endDate);
    }

    /**
     * 更新レコードがあるか
     * @param resultSet
     * @param adapter
     * @return
     */
    public boolean isUpdateAvailable(List<MedicineEntry> resultSet, GenericAdapter adapter) {
        try {
            return adapter.onResult(getCode(), resultSet);
        } catch (Exception ex) {
            adapter.onError(ex);
        }
        return false;
    }

    /**
     * XMLにシリアライズ
     * @param result ライター
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        result.append("<ClaimItem " + "name='" + name + "code='" + code + "' codeSystem='" + codeSystem + "' classCode='" + classCode + "' classCodeSystem='" + classCodeSystem + "' number='" + number + "' unit='" + unit + "' numberCode='" + numberCode + "' numberCodeSystem='" + numberCodeSystem + "' memo='" + memo + "ykzkbn='" + ykzkbn + "suryo1='" + Float.toString(suryo1) + "suryo2='" + Float.toString(suryo2) + "'>" + System.getProperty("line.separator"));
        for (String item : sstKijunCdSet) {
            result.append("<item>");
            result.append(item);
            result.append("</item>");
        }
        result.append("</ClaimItem>");
    }
}
