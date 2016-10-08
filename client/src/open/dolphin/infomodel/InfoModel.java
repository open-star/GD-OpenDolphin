package open.dolphin.infomodel;

import open.dolphin.queries.DolphinQuery;

/**
 *  MEMO:マッピング
 * @author
 */
public class InfoModel implements IInfoModel {

    /**
     *
     * @return
     */
    public boolean isValidModel() {
        return true;
    }

    /**
     * 検索 MEMO:何もしない
     * @param query
     * @return false
     */
    public boolean search(DolphinQuery query) {
        return false;
    }
}
