/*
 * AccessRight.java
 * Copyright (C) 2003,2004 Digital Globe, Inc. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * AccessRightModel MEMO:マッピング
 *
 * @author  Kazushi Minagawa
 */
public class AccessRightModel extends InfoModel {//id

    private static final long serialVersionUID = -90888255738195101L;
    private String permission;
    private String startDate;
    private String endDate;
    private String licenseeCode;
    private String licenseeName;
    private String licenseeCodeType;

    /** Creates a new instance of AccessRight */
    public AccessRightModel() {
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
     * permissionのGetter
     * @return permission PERMISSION_ALL = "all" PERMISSION_READ = "read"
     */
    public String getPermission() {
        return permission;
    }

    /**
     * permissionのSetter
     * @param val permission PERMISSION_ALL = "all" PERMISSION_READ = "read"
     */
    public void setPermission(String val) {
        permission = val;
    }

    /**
     * 開始日のGetter
     * @return 開始日
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     *　開始日のSetter
     * @param val 開始日
     */
    public void setStartDate(String val) {
        startDate = val;
    }

    /**
     * 終了日のGetter
     * @return 終了日
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 終了日のSetter
     * @param val 終了日
     */
    public void setEndDate(String val) {
        endDate = val;
    }

    /**
     * licenseeCodeのSetter
     * @param licenseeCode ACCES_RIGHT_PATIENT = "patient" ACCES_RIGHT_CREATOR = "creator" ACCES_RIGHT_EXPERIENCE = "experience"
     */
    public void setLicenseeCode(String licenseeCode) {
        this.licenseeCode = licenseeCode;
    }

    /**
     * licenseeCodeのGetter
     * @return licenseeCode ACCES_RIGHT_PATIENT = "patient" ACCES_RIGHT_CREATOR = "creator" ACCES_RIGHT_EXPERIENCE = "experience"
     */
    public String getLicenseeCode() {
        return licenseeCode;
    }

    /**
     * licenseeNameのSetter
     * @param licenseeName ACCES_RIGHT_PATIENT_DISP = "被記載者(患者)" ACCES_RIGHT_CREATOR_DISP = "記載者施設" ACCES_RIGHT_EXPERIENCE_DISP = "診療歴のある施設"
     */
    public void setLicenseeName(String licenseeName) {
        this.licenseeName = licenseeName;
    }

    /**
     * licenseeNameのGetter
     * @return licenseeName ACCES_RIGHT_PATIENT_DISP = "被記載者(患者)" ACCES_RIGHT_CREATOR_DISP = "記載者施設" ACCES_RIGHT_EXPERIENCE_DISP = "診療歴のある施設"
     */
    public String getLicenseeName() {
        return licenseeName;
    }

    /**
     * licenseeCodeTypeのSetter
     * @param licenseeCodeType ACCES_RIGHT_PERSON_CODE = "personCode" ACCES_RIGHT_FACILITY_CODE = "facilityCode" ACCES_RIGHT_EXPERIENCE_CODE = "facilityCode"
     */
    public void setLicenseeCodeType(String licenseeCodeType) {
        this.licenseeCodeType = licenseeCodeType;
    }

    /**
     * licenseeCodeTypeのGetter
     * @return licenseeCodeType ACCES_RIGHT_PERSON_CODE = "personCode" ACCES_RIGHT_FACILITY_CODE = "facilityCode" ACCES_RIGHT_EXPERIENCE_CODE = "facilityCode"
     */
    public String getLicenseeCodeType() {
        return licenseeCodeType;
    }

    /**
     * MEMO:何もしない
     * @param result ライター
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        //TODO serialize
    }
}
