/*
 * Created on 2004/10/13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package open.dolphin.service;

import java.util.List;
import open.dolphin.dto.MasterSearchSpec;
import open.dolphin.infomodel.RadiologyMethodValue;

/**
 * IMasterService
 *
 * @author Minagawa,Kazushi
 *
 */
public interface IMasterService {

    // MEMO: What is the マスタ ?
    /**
     * マスタを検索する。
     *
     * @param spec 検索仕様
     * @return 該当マスタのコレクション
     */
    public List<RadiologyMethodValue> getMaster(MasterSearchSpec spec);
}
