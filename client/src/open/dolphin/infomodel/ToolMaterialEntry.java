/*
 * ToolMaterialEntry.java
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

import java.util.GregorianCalendar;
import java.util.List;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;

/**
 * 器材 MEMO:ORCAマスター tbl_tensu
 * ToolMaterialEntry
 * @author aniruddha
 */
public final class ToolMaterialEntry extends MasterEntry {//id

    private static final long serialVersionUID = -1536990563817129012L;
    private String unit;//単位
    private String costFlag;//価格フラグ
    private String cost;//価格
    private String requiredMaterialCode;

    /** 
     * Creates a new instance of ToolMaterialEntry
     */
    public ToolMaterialEntry() {
    }

    /**
     * 単位のGetter
     * @return 単位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 単位のSetter
     * @param val 単位
     */
    public void setUnit(String val) {
        unit = val;
    }

    /**
     * 価格フラグのGetter
     * @return 価格フラグ
     */
    public String getCostFlag() {
        return costFlag;
    }

    /**
     * 価格フラグのSetter
     * @param val 価格フラグ
     */
    public void setCostFlag(String val) {
        costFlag = val;
    }

    /**
     * 価格のGetter
     * @return 価格
     */
    public String getCost() {
        return cost;
    }

    /**
     * 価格のSetter
     * @param val 価格
     */
    public void setCost(String val) {
        cost = val;
    }

    /**
     * requiredMaterialCodeのSetter
     * @param requiredMaterialCode
     */
    public void setRequiredMaterialCode(String requiredMaterialCode) {
        this.requiredMaterialCode = requiredMaterialCode;
    }

    /**
     * requiredMaterialCodeのGetter
     * @return requiredMaterialCode
     */
    public String getRequiredMaterialCode() {
        return this.requiredMaterialCode;
    }

    /**
     * 
     * 特定器材の場合、"058"で始まるコードのものには付加的な器材が必須なので、それを追加する。
     * @param alternateCode
     * @return
     */
    private ToolMaterialEntry getAlternateToolMaterial(String alternateCode) {
        SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
        if (alternateCode != null) {
            if (!alternateCode.isEmpty()) {
                if (!alternateCode.equals("000000000")) {
                    List<ToolMaterialEntry> alternateEntry = dao.getToolMaterialByCodeFromOrca(alternateCode);//すべての付加的な器材を取り出す。
                    if (!alternateEntry.isEmpty()) {
                        for (ToolMaterialEntry entry : alternateEntry) {
                            if (entry != null && (entry.useState(new GregorianCalendar()) != 2)) {//付加的な器材中、有効な期限のもので最初に見つかったものを返す。
                                return entry;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * この機材を使用するのに必要な追加機材があれば返す。
     * @return
     */
    public ToolMaterialEntry getRequiredToolMaterial() {
        if (getCode().startsWith("058")) {
            return getAlternateToolMaterial(getRequiredMaterialCode());
        }
        return null;
    }
}
