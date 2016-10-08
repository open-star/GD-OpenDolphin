package open.dolphin.service;

import java.util.List;
import open.dolphin.client.IChart;

import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.infomodel.PatientVisitModel;

/**
 * IPvtService
 *
 * @author Minagawa,Kazushi
 *
 */
public interface IPvtService {
    
    /**
     * 患者来院情報を保存する。
     * @param model 
     * @return
     */
    public int addPvt(PatientVisitModel model);
    
    /**
     * 施設の患者来院情報を取得する。
     * @param spec 検索仕様オブジェクト
     * @return List
     */
    public List<PatientVisitModel> getPvt(PatientVisitSpec spec);
    
        /**
     * 来院情報を削除する。
     * @param id レコード ID
     * @return 削除件数
     */
    public int removePvt(long id);
    
    /**
     * 診察終了の状態を書き込む。
     * @param pk レコードID
     * @param state
     * @return
     * @state 診察終了フラグ 1 の時終了
     */
    public int updatePvtState(long pk, IChart.state state);
}
