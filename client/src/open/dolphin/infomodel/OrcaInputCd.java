/*
 * OrcaInputCd.java
 * Copyright (C) 2007 Digital Globe, Inc. All rights reserved.
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
import java.util.ArrayList;
import java.util.List;
import open.dolphin.queries.DolphinQuery;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  MEMO:ORCA の tbl_inputcd エンティティクラス。
 *
 * @author Minagawa, Kazushi
 */
public class OrcaInputCd extends InfoModel {//id

    private String hospId;
    private String cdsyu;
    private String inputCd;
    private String sryKbn;
    private String sryCd;
    private int dspSeq;
    private String dspName;
    private String termId;
    private String opId;
    private String creYmd;
    private String upYmd;
    private String upHms;
    private List<OrcaInputSet> inputSet;

    /** 
     * Creates a new instance of OrcaInputCd 
     */
    public OrcaInputCd() {
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
     * hospIdのGetter
     * @return
     */
    public String getHospId() {
        return hospId;
    }

    /**
     * hospIdのSetter
     * @param hospId
     */
    public void setHospId(String hospId) {
        this.hospId = hospId;
    }

    /**
     * cdsyuのGetter
     * @return
     */
    public String getCdsyu() {
        return cdsyu;
    }

    /**
     * cdsyuのSetter
     * @param cdsyu
     */
    public void setCdsyu(String cdsyu) {
        this.cdsyu = cdsyu;
    }

    /**
     * inputCdのGetter
     * @return
     */
    public String getInputCd() {
        return inputCd;
    }

    /**
     * inputCdのSetter
     * @param inputCd
     */
    public void setInputCd(String inputCd) {
        this.inputCd = inputCd;
    }

    /**
     * sryKbnのGetter
     * @return
     */
    public String getSryKbn() {
        return sryKbn;
    }

    /**
     * sryKbnのSetter
     * @param sryKbn
     */
    public void setSryKbn(String sryKbn) {
        this.sryKbn = sryKbn;
    }

    /**
     * sryCdのGetter
     * @return
     */
    public String getSryCd() {
        return sryCd;
    }

    /**
     * sryCdのSetter
     * @param sryCd
     */
    public void setSryCd(String sryCd) {
        this.sryCd = sryCd;
    }

    /**
     * dspSeqのGetter
     * @return
     */
    public int getDspSeq() {
        return dspSeq;
    }

    /**
     * dspSeqのSetter
     * @param dspSeq
     */
    public void setDspSeq(int dspSeq) {
        this.dspSeq = dspSeq;
    }

    /**
     * dspNameのGetter
     * @return
     */
    public String getDspName() {
        return dspName;
    }

    /**
     * dspNameのSetter
     * @param dspName
     */
    public void setDspName(String dspName) {
        this.dspName = dspName;
    }

    /**
     * termIdのGetter
     * @return
     */
    public String getTermId() {
        return termId;
    }

    /**
     * termIdのSetter
     * @param termId
     */
    public void setTermId(String termId) {
        this.termId = termId;
    }

    /**
     * opIdのGetter
     * @return
     */
    public String getOpId() {
        return opId;
    }

    /**
     * opIdのSetter
     * @param opId
     */
    public void setOpId(String opId) {
        this.opId = opId;
    }

    /**
     * creYmdのGetter
     * @return
     */
    public String getCreYmd() {
        return creYmd;
    }

    /**
     * creYmdのSetter
     * @param creYmd
     */
    public void setCreYmd(String creYmd) {
        this.creYmd = creYmd;
    }

    /**
     * upYmdのGetter
     * @return
     */
    public String getUpYmd() {
        return upYmd;
    }

    /**
     * upYmdのSetter
     * @param upYmd
     */
    public void setUpYmd(String upYmd) {
        this.upYmd = upYmd;
    }

    /**
     * upHmsのGetter
     * @return
     */
    public String getUpHms() {
        return upHms;
    }

    /**
     * upHmsのSetter
     * @param upHms
     */
    public void setUpHms(String upHms) {
        this.upHms = upHms;
    }

    /**
     * inputSetのGetter
     * @return
     */
    public List<OrcaInputSet> getInputSet() {
        return inputSet;
    }

    /**
     * inputSetのSetter
     * @param list
     */
    public void setInputSet(List<OrcaInputSet> list) {
        inputSet = list;
    }

    /**
     * inputSetのAdder
     * @param set
     */
    public void addInputSet(OrcaInputSet set) {
        if (inputSet == null) {
            inputSet = new ArrayList<OrcaInputSet>();
        }
        inputSet.add(set);
    }

    /**
     * stampInfoのGetter
     * @return
     */
    public ModuleInfoBean getStampInfo() {
        ModuleInfoBean ret = new ModuleInfoBean();
        ret.initialize(getInputCd(), getDspName(), null, ENTITY_MED_ORDER, ROLE_ORCA_SET);

        //    ret.setStampName(getDspName());
        //    ret.setStampRole(ROLE_ORCA_SET);
        //    ret.setEntity(ENTITY_MED_ORDER);
        //    ret.setStampId(getInputCd());
        return ret;
    }

    /**
     * bundleMedのGetter
     * @return
     */
    public BundleMed getBundleMed() {

        BundleMed ret = new BundleMed();

        for (OrcaInputSet set : inputSet) {
            ret.addClaimItem(set.getClaimItem());
        }
        return ret;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        //TODO serialize
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void deserialize(Writer result) throws IOException {
        //TODO deserialize
    }
}
