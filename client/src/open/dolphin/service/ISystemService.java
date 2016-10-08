package open.dolphin.service;

import java.util.List;
import open.dolphin.infomodel.RadiologyMethodValue;

import open.dolphin.infomodel.UserModel;

/**
 * ISystemService
 *
 * @author Minagawa, Kazushi
 */
public interface ISystemService {
    
    /**
     * 次のOIDを取得する。
     *
     * @return OID
     */
    public String helloDolphin();
    
    /**
     * 施設と管理者情報を登録する。
     * @param user 施設管理者
     */
    public void addFacilityAdmin(UserModel user);
    
    /**
     * 画像診断メソッドマスタを登録する。
     * @param c
     */
    public void putRadMethodMaster(List<RadiologyMethodValue> c);

}
