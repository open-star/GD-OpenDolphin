package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * 電話番号 MEMO:マッピング
 * TelephoneModel
 * @author Minagawa,Kazushi
 * 
 */
public class TelephoneModel extends InfoModel {//id

    private static final long serialVersionUID = -3520256828672499135L;
    private String telephoneType;
    private String telephoneTypeDesc;
    private String telephoneTypeCodeSys;
    private String country;
    private String area;
    private String city;
    private String number;
    // さすがに不要では？
    private String extension;
    private String memo;

    /**
     * コンストラクタ
     */
    public TelephoneModel() {
    }

    /**
     * 検索 MEMO:何もしない
     * @param query
     * @return false
     */
    @Override
    public boolean search(DolphinQuery query) {
        return false;
    }

    /**
     * 電話タイプのSetter
     * @param telephoneClass 電話タイプ
     */
    public void setTelephoneType(String telephoneClass) {
        this.telephoneType = telephoneClass;
    }

    /**
     * 電話タイプのGetter
     * @return 電話タイプ
     */
    public String getTelephoneType() {
        return telephoneType;
    }

    /**
     * telephoneClassDescのSetter
     * @param telephoneClassDesc
     */
    public void setTelephoneTypeDesc(String telephoneClassDesc) {
        this.telephoneTypeDesc = telephoneClassDesc;
    }

    /**
     * telephoneClassDescのGetter
     * @return telephoneClassDesc
     */
    public String getTelephoneTypeDesc() {
        return telephoneTypeDesc;
    }

    /**
     * telephoneClassCodeSysのSetter
     * @param telephoneClassCodeSys
     *            
     */
    public void setTelephoneTypeCodeSys(String telephoneClassCodeSys) {
        this.telephoneTypeCodeSys = telephoneClassCodeSys;
    }

    /**
     * telephoneClassCodeSysのGetter
     * @return telephoneClassCodeSys
     */
    public String getTelephoneTypeCodeSys() {
        return telephoneTypeCodeSys;
    }

    /**
     * 国コードのSetter
     * @param country 国コード
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 国コードのGetter
     * @return 国コード
     */
    public String getCountry() {
        return country;
    }

    /**
     * エリアコードのSetter
     * @param area エリアコード
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * エリアコードのGetter
     * @return エリアコード
     */
    public String getArea() {
        return area;
    }

    /**
     * 局番のSetter
     * @param city 局番
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 局番のGetter
     * @return 局番
     */
    public String getCity() {
        return city;
    }

    /**
     * 電話番号のSetter
     * @param number 電話番号
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 電話番号のGetter
     * @return 電話番号
     */
    public String getNumber() {
        return number;
    }

    /**
     * エクステンションのSetter
     * @param extension エクステンション
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }

    /**
     * エクステンションのGetter
     * @return エクステンション
     */
    public String getExtension() {
        return extension;
    }

    /**
     * メモのSetter
     * @param memo メモ
     *       
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * メモのGetter
     * @return メモ
     */
    public String getMemo() {
        return memo;
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        //TODO serialize
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void deserialize(Writer result) throws IOException {
        //TODO deserialize
    }
}
