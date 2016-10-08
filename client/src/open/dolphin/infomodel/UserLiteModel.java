package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * UserLiteModel MEMO:マッピング
 * @author Minagawa,Kazushi
 */
public class UserLiteModel extends InfoModel {//id

    private static final long serialVersionUID = 6256812305377957756L;
    private String userId;//ユーザID
    private String commonName;//名前
    private LicenseModel licenseModel;//資格

    /**
     * コンストラクタ
     */
    public UserLiteModel() {
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
     * ユーザIDのSetter
     * @param creatorId ユーザID
     */
    public void setUserId(String creatorId) {
        this.userId = creatorId;
    }

    /**
     * ユーザIDのGetter
     * @return userId.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 名前のSetter
     * @param name 名前
     */
    public void setCommonName(String name) {
        this.commonName = name;
    }

    /**
     * 名前のGetter
     * @return 名前
     */
    public String getCommonName() {
        return commonName;
    }

    /**
     * 資格のSetter
     * @param licenseModel 資格
     */
    public void setLicenseModel(LicenseModel licenseModel) {
        this.licenseModel = licenseModel;
    }

    /**
     * 資格のGetter
     * @return 資格
     */
    public LicenseModel getLicenseModel() {
        return licenseModel;
    }

    /**
     * MEMO:何もしない
     * @param result ライター
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        //TODO serialize
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
