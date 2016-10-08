package open.dolphin.infomodel;

import javax.persistence.Embeddable;
import open.dolphin.queries.DolphinQuery;

/**
 * 資格情報 MEMO:マッピング
 *
 * LicenseModel
 *
 * @author Minagawa,Kazushi
 *
 */
@Embeddable
public class LicenseModel extends InfoModel {//id

    private static final long serialVersionUID = 5120402348495916132L;
    private String license;
    private String licenseDesc;
    private String licenseCodeSys;

    /**
     * licenseのSetter
     * @param license The license to set.
     */
    public void setLicense(String license) {
        this.license = license;
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
     * licenseのGetter
     * @return Returns the license.
     */
    public String getLicense() {
        return license;
    }

    /**
     * licenseDescのSetter
     * @param licenseDesc The licenseDesc to set.
     */
    public void setLicenseDesc(String licenseDesc) {
        this.licenseDesc = licenseDesc;
    }

    /**
     * licenseDescのGetter
     * @return Returns the licenseDesc.
     */
    public String getLicenseDesc() {
        return licenseDesc;
    }

    /**
     * licenseCodeSysのSetter
     * @param licenseCodeSys The licenseCodeSys to set.
     */
    public void setLicenseCodeSys(String licenseCodeSys) {
        this.licenseCodeSys = licenseCodeSys;
    }

    /**
     * licenseCodeSysのGetter
     * @return Returns the licenseCodeSys.
     */
    public String getLicenseCodeSys() {
        return licenseCodeSys;
    }

    /**
     *
     * @return
     */
    public String toString() {
        return licenseDesc;
    }
}
