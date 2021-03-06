package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import open.dolphin.queries.DolphinQuery;

/**
 * 患者メモ MEMO:マッピング  d_patient_memo
 * PatientMemoModel generated by hbm2java
 */
public class PatientMemoModel extends KarteEntryBean {

    private String memo;//MEMO:Refrection

    /**
     * コンストラクタ
     */
    public PatientMemoModel() {
    }

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (memo != null) {
            return (memo.indexOf(query.what("keyword")) != -1);
        }
        return false;
    }
    /**
     * memoのGetter
     * MEMO:Refrection
     * @return
     */
    public String getMemo() {
        return this.memo;
    }

    /**
     * memoのSetter
     * MEMO:Refrection
     * @param memo
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
}


