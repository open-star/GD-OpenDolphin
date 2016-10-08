/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.infomodel;

//import open.dolphin.infomodel.MasterEntry;

/*
 * InteractEntry.java
 * Copyright (C) 2007 Dolphin Project. All rights reserved.
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

/**
 * 相互作用マスタ MEMO:マッピング
 * ORCAマスター
 * TBL_INTERACT
 * @author
 */
public class InteractEntry extends MasterEntry {

    private String PharmaceuticalsCodeFrom;
    private String PharmaceuticalsCodeTo;
    private String SymptomsCode;

    /**
     *
     * @param PharmaceuticalsCodeFrom
     * @param PharmaceuticalsCodeTo
     * @param SymptomsCode
     */
    public InteractEntry(String PharmaceuticalsCodeFrom, String PharmaceuticalsCodeTo, String SymptomsCode) {
        this.PharmaceuticalsCodeFrom = PharmaceuticalsCodeFrom;
        this.PharmaceuticalsCodeTo = PharmaceuticalsCodeTo;
        this.SymptomsCode = SymptomsCode;
    }

    /**
     * PharmaceuticalsCodeFromのGetter
     * @return
     */
    public String getPharmaceuticalsCodeFrom() {
        return PharmaceuticalsCodeFrom;
    }

    /**
     * PharmaceuticalsCodeToのGetter
     * @return
     */
    public String getPharmaceuticalsCodeTo() {
        return PharmaceuticalsCodeTo;
    }

    /**
     * SymptomsCodeのGetter
     * @return
     */
    public String getSymptomsCode() {
        return SymptomsCode;
    }
}
