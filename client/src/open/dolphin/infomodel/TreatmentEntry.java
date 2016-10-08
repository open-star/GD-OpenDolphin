/*
 * TreatmentEntry.java
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

import java.util.List;

/**
 * 診療行為　MEMO:ORCAマスター
 * TreatmentEntry
 *
 * @author  aniruddha
 */
public final class TreatmentEntry extends MasterEntry {//id

    private static final long serialVersionUID = 6147020634071271583L;
    private String unit;//単位
    private String costFlag;//価格フラグ 0 = "廃" 1 = "金" 2 = "" 3 = "+点" 4 = "都"5 = "%加" 6 = "%減" 7 = "減" 8 = "-点"
    private String cost;//価格
    private String inOutFlag;// 0 = "入外" 1 = "入" 2 = "外"
    private String oldFlag;//   0 = "社老" 1 = "社" 2 = "老"
    private String claimClassCode;//コード
    private String hospitalClinicFlag;//病院医院フラグ
    private String claimClassCodeInHospital;//院内クレームクラス
    private List<String> sstKijunCdSet;//SST基準コード

    /**
     * コンストラクタ
     * Creates a new instance of TreatmentEntry
     */
    public TreatmentEntry() {
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
     * 価格フラグ 0 = "廃" 1 = "金" 2 = "" 3 = "+点" 4 = "都"5 = "%加" 6 = "%減" 7 = "減" 8 = "-点"
     * @return 価格フラグ
     */
    public String getCostFlag() {
        return costFlag;
    }

    /**
     * 価格フラグ 0 = "廃" 1 = "金" 2 = "" 3 = "+点" 4 = "都"5 = "%加" 6 = "%減" 7 = "減" 8 = "-点"
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
     * inOutFlag  0 = "入外" 1 = "入" 2 = "外"
     * @return inOutFlag
     */
    public String getInOutFlag() {
        return inOutFlag;
    }

    /**
     * inOutFlag  0 = "入外" 1 = "入" 2 = "外"
     * @param val inOutFlag
     */
    public void setInOutFlag(String val) {
        inOutFlag = val;
    }

    /**
     * oldFlag    0 = "社老" 1 = "社" 2 = "老"
     * @return oldFlag
     */
    public String getOldFlag() {
        return oldFlag;
    }

    /**
     * oldFlag    0 = "社老" 1 = "社" 2 = "老"
     * @param val oldFlag
     */
    public void setOldFlag(String val) {
        oldFlag = val;
    }

    /**
     * コードのGetter
     * @return コード
     */
    public String getClaimClassCode() {
        return claimClassCode;
    }

    /**
     * コードのSetter
     * @param val コード
     */
    public void setClaimClassCode(String val) {
        claimClassCode = val;
    }

    /**
     * 病院医院フラグ  0 = "病診" 1 = "病" 2 = "診"
     * @return 病院医院フラグ
     */
    public String getHospitalClinicFlag() {
        return hospitalClinicFlag;
    }

    /**
     * 病院医院フラグ  0 = "病診" 1 = "病" 2 = "診"
     * @param val 病院医院フラグ
     */
    public void setHospitalClinicFlag(String val) {
        hospitalClinicFlag = val;
    }

    /**
     * 院内クレームクラスのGetter
     * @return 院内クレームクラス
     */
    public String getClaimClassCodeInHospital() {
        return claimClassCodeInHospital;
    }

    /**
     * 院内クレームクラスのSetter
     * @param val 院内クレームクラス
     */
    public void setClaimClassCodeInHospital(String val) {
        claimClassCodeInHospital = val;
    }

    /**
     * SST基準コードのGetter
     * @return SST基準コード
     */
    public List<String> getSstKijunCdSet() {
        return sstKijunCdSet;
    }

    /**
     * SST基準コードのSetter
     * @param val SST基準コード
     */
    public void setSstKijunCdSet(List<String> val) {
        sstKijunCdSet = val;
    }
}
