package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * 住所 MEMO:マッピング
 * AddressModel
 * @author Minagawa,kazushi
 */
public class AddressModel extends InfoModel {//id

    private static final long serialVersionUID = 4602230572833538876L;
    private String addressType;//住所区分
    private String addressTypeDesc;//住所区分説明
    private String addressTypeCodeSys;//住所区分体系
    private String countryCode;//国コード
    private String zipCode;//郵便番号
    private String address;//住所

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
     * 国コードを設定する。
     *
     * @param countryCode 国コード
     *            
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * 国コードを返す。
     *
     * @return 国コード
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * 郵便番号を設定する。
     *
     * @param zipCode 郵便番号
     *            
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 郵便番号を返す。
     *
     * @return 郵便番号
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 住所を設定する。
     *
     * @param address 住所
     *            
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 住所を返す。
     *
     * @return 住所
     */
    public String getAddress() {
        return address;
    }

    /**
     * 住所区分を設定する。
     *
     * @param addressType 住所区分
     *            
     */
    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    /**
     * 住所区分を返す。
     *
     * @return 住所区分
     */
    public String getAddressType() {
        return addressType;
    }

    /**
     * 住所区分説明を設定する。
     *
     * @param addressTypeDesc 住所区分説明
     */
    public void setAddressTypeDesc(String addressTypeDesc) {
        this.addressTypeDesc = addressTypeDesc;
    }

    /**
     * 住所区分説明を返す。
     *
     * @return 住所区分説明
     */
    public String getAddressTypeDesc() {
        return addressTypeDesc;
    }

    /**
     * 住所区分体系を設定する。
     *
     * @param addressTypeCodeSys 住所区分体系
     */
    public void setAddressTypeCodeSys(String addressTypeCodeSys) {
        this.addressTypeCodeSys = addressTypeCodeSys;
    }

    /**
     * 住所区分体系を返す。
     *
     * @return 住所区分体系
     */
    public String getAddressTypeCodeSys() {
        return addressTypeCodeSys;
    }

    /**
     * シリアライズ
     * @param result ライター
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        result.append("<AddressModel " + "addressType='" + addressType + "' addressTypeDesc='" + addressTypeDesc + "' addressTypeCodeSys='" + addressTypeCodeSys + "' countryCode='" + countryCode + "' zipCode='" + zipCode + "' address='" + address + "' />" + System.getProperty("line.separator"));
    }

    /**
     * MEMO:何もしない
     * @param result ライター
     * @throws IOException
     */
    public void deserialize(Writer result) throws IOException {
        //TODO deserialize
    }
}
