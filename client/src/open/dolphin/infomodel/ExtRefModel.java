/*
 * ExtRef.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
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
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import open.dolphin.queries.DolphinQuery;

/**
 * 外部参照要素 MEMO:マッピング
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
@Embeddable
public class ExtRefModel extends InfoModel {//id

    private static final long serialVersionUID = -3408876454565957708L;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false)
    private String medicalRole;
    @Transient
    private String medicalRoleTableId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String href;

    /**
     * コンストラクタ
     */
    public ExtRefModel() {
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
     * contentTypeのGetter
     * @return contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * contentTypeのSetter
     * @param value contentType
     */
    public void setContentType(String value) {
        contentType = value;
    }

    /**
     * titleのGetter
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * titleのSetter
     * @param value title
     */
    public void setTitle(String value) {
        title = value;
    }

    /**
     * hrefのGetter
     * @return href
     */
    public String getHref() {
        return href;
    }

    /**
     * hrefのSetter
     * @param value href
     */
    public void setHref(String value) {
        href = value;
    }

    /**
     * medicalRoleのSetter
     * @param medicalRole
     */
    public void setMedicalRole(String medicalRole) {
        this.medicalRole = medicalRole;
    }

    /**
     * medicalRoleのGetter
     * @return medicalRole
     */
    public String getMedicalRole() {
        return medicalRole;
    }

    /**
     * medicalRoleTableIdのSetter
     * @param medicalRoleTableId
     */
    public void setMedicalRoleTableId(String medicalRoleTableId) {
        this.medicalRoleTableId = medicalRoleTableId;
    }

    /**
     * medicalRoleTableIdのGetter
     * @return medicalRoleTableId
     */
    public String getMedicalRoleTableId() {
        return medicalRoleTableId;
    }

    /**
     *
     * @param s
     * @return
     */
    private String nullToString(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    /**
     * シリアライズ
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        result.append("<ExtRefModel " + "contentType='" + nullToString(contentType) + "' medicalRole='" + nullToString(medicalRole) + "' title='" + nullToString(title) + "' href='" + nullToString(href) + "' />" + System.getProperty("line.separator"));
    }
}
