package open.dolphin.infomodel;

import javax.persistence.Embeddable;
import open.dolphin.queries.DolphinQuery;

/**
 * 住所基本 MEMO:マッピング
 * SimpleAddressModel
 * @author kazm
 */
@Embeddable
public class SimpleAddressModel extends InfoModel {//id

    private static final long serialVersionUID = 6193406550378317758L;
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
     * 郵便番号のGetter
     * @return 郵便番号
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 郵便番号のSetter
     * @param zipCode 郵便番号
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 住所のGetter
     * @return 住所
     */
    public String getAddress() {
        return address;
    }

    /**
     * 住所のSetter
     * @param address 住所
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
