/*
 * DiagnosisCategoryModel.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
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

import javax.persistence.Embeddable;
import open.dolphin.queries.DolphinQuery;

/**
 * 傷病名 MEMO:マッピング
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Embeddable
public class DiagnosisCategoryModel extends InfoModel {//id

    private static final long serialVersionUID = 7606390775489282517L;
    private String diagnosisCategory;
    private String diagnosisCategoryDesc;
    private String diagnosisCategoryCodeSys;

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
     * diagnosisCategoryのSetter
     * @param category The category to set.
     */
    public void setDiagnosisCategory(String category) {
        this.diagnosisCategory = category;
    }

    /**
     * diagnosisCategoryのGetter
     * @return Returns the category.
     */
    public String getDiagnosisCategory() {
        return diagnosisCategory;
    }

    /**
     * diagnosisCategoryDescのSetter
     * @param categoryDesc The categoryDesc to set.
     */
    public void setDiagnosisCategoryDesc(String categoryDesc) {
        this.diagnosisCategoryDesc = categoryDesc;
    }

    /**
     * diagnosisCategoryDescのGetter
     * @return Returns the categoryDesc.
     */
    public String getDiagnosisCategoryDesc() {
        return diagnosisCategoryDesc;
    }

    /**
     * diagnosisCategoryCodeSysのSetter
     * @param categoryCodeSys The categoryCodeSys to set.
     */
    public void setDiagnosisCategoryCodeSys(String categoryCodeSys) {
        this.diagnosisCategoryCodeSys = categoryCodeSys;
    }

    /**
     * diagnosisCategoryCodeSysのGetter
     * @return Returns the categoryCodeSys.
     */
    public String getDiagnosisCategoryCodeSys() {
        return diagnosisCategoryCodeSys;
    }

    /**
     * toString
     * @return diagnosisCategoryDesc
     */
    public String toString() {
        return getDiagnosisCategoryDesc();
    }
}
