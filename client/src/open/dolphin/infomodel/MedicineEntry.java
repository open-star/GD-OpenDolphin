/*
 * MedicineEntry.java
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

/**
 * 医療行為 MEMO:ORCAマスター tbl_tensu
 * MedicineEntry
 * @author  aniruddha
 */
public final class MedicineEntry extends MasterEntry {//id

    private static final long serialVersionUID = -1890098260519019476L;
    private String unit;
    private String costFlag;
    private String cost;
    private String jncd;
    private String ykzKbn;

    /**
     * Creates a new instance of MedicineEntry
     */
    public MedicineEntry() {
    }

    /**
     * unitのGetter
     * @return
     */
    public String getUnit() {
        return unit;
    }

    /**
     * unitのSetter
     * @param val
     */
    public void setUnit(String val) {
        unit = val;
    }

    /**
     * costFlagのGetter
     * @return
     */
    public String getCostFlag() {
        return costFlag;
    }

    /**
     * costFlagのSetter
     * @param val
     */
    public void setCostFlag(String val) {
        costFlag = val;
    }

    /**
     * costのGetter
     * @return
     */
    public String getCost() {
        return cost;
    }

    /**
     * costのSetter
     * @param val
     */
    public void setCost(String val) {
        cost = val;
    }

    /**
     * jncdのGetter
     * @return
     */
    public String getJNCD() {
        return jncd;
    }

    /**
     * jncdのSetter
     * @param val
     */
    public void setJNCD(String val) {
        jncd = val;
    }

    /**
     * ykzKbnのGetter
     * @return
     */
    public String getYkzKbn() {
        return ykzKbn;
    }

    /**
     * ykzKbnのSetter
     * @param ykzKbn
     */
    public void setYkzKbn(String ykzKbn) {
        this.ykzKbn = ykzKbn;
    }
}