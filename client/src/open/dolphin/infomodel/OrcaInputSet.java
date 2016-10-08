/*
 * OrcaInputSet.java
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
import open.dolphin.queries.DolphinQuery;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ORCA の tbl_inputset エンティティクラス。
 *
 * @author Minagawa, Kazushi
 */
public class OrcaInputSet extends InfoModel {//id

    private String hospId;
    private String setCd;
    private String yukostYmd;
    private String yukoedYmd;
    private int setSeq;
    private String inputCd;
    private float suryo1;
    private float suryo2;
    private int kaisu;
    private String comment;
    private String atai1;
    private String atai2;
    private String atai3;
    private String atai4;
    private String termId;
    private String opId;
    private String creYmd;
    private String upYmd;
    private String upHms;

    /** 
     * Creates a new instance of OrcaInputSet 
     */
    public OrcaInputSet() {
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
     * setCdのGetter
     * @return
     */
    public String getSetCd() {
        return setCd;
    }

    /**
     * setCdのSetter
     * @param setCd
     */
    public void setSetCd(String setCd) {
        this.setCd = setCd;
    }

    /**
     * yukostYmdのGetter
     * @return
     */
    public String getYukostYmd() {
        return yukostYmd;
    }

    /**
     * yukostYmdのSetter
     * @param yukostYmd
     */
    public void setYukostYmd(String yukostYmd) {
        this.yukostYmd = yukostYmd;
    }

    /**
     * yukoedYmdのGetter
     * @return
     */
    public String getYukoedYmd() {
        return yukoedYmd;
    }

    /**
     * yukoedYmdのSetter
     * @param yukoedYmd
     */
    public void setYukoedYmd(String yukoedYmd) {
        this.yukoedYmd = yukoedYmd;
    }

    /**
     * setSeqのGetter
     * @return
     */
    public int getSetSeq() {
        return setSeq;
    }

    /**
     * setSeqのSetter
     * @param setSeq
     */
    public void setSetSeq(int setSeq) {
        this.setSeq = setSeq;
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
     * suryo1のGetter
     * @return
     */
    public float getSuryo1() {
        return suryo1;
    }

    /**
     * suryo1のSetter
     * @param suryo1
     */
    public void setSuryo1(float suryo1) {
        this.suryo1 = suryo1;
    }

    /**
     * suryo2のGetter
     * @return
     */
    public float getSuryo2() {
        return suryo2;
    }

    /**
     * suryo2のSetter
     * @param suryo2
     */
    public void setSuryo2(float suryo2) {
        this.suryo2 = suryo2;
    }

    /**
     * kaisuのGetter
     * @return
     */
    public int getKaisu() {
        return kaisu;
    }

    /**
     * kaisuのSetter
     * @param kaisu
     */
    public void setKaisu(int kaisu) {
        this.kaisu = kaisu;
    }

    /**
     * commentのGetter
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * commentのSetter
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * atai1のGetter
     * @return
     */
    public String getAtai1() {
        return atai1;
    }

    /**
     * atai1のSetter
     * @param atai1
     */
    public void setAtai1(String atai1) {
        this.atai1 = atai1;
    }

    /**
     * atai2のGetter
     * @return
     */
    public String getAtai2() {
        return atai2;
    }

    /**
     * atai2のSetter
     * @param atai2
     */
    public void setAtai2(String atai2) {
        this.atai2 = atai2;
    }

    /**
     * atai3のGetter
     * @return
     */
    public String getAtai3() {
        return atai3;
    }

    /**
     * atai3のSetter
     * @param atai3
     */
    public void setAtai3(String atai3) {
        this.atai3 = atai3;
    }

    /**
     * atai4のGetter
     * @return
     */
    public String getAtai4() {
        return atai4;
    }

    /**
     * atai4のSetter
     * @param atai4
     */
    public void setAtai4(String atai4) {
        this.atai4 = atai4;
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
     * claimItemのGetter
     * @return
     */
    public ClaimItem getClaimItem() {
        ClaimItem ret = new ClaimItem(getInputCd(), String.valueOf(getSuryo1()));
        //     ret.setCode(getInputCd());
        //    ret.setNumber(String.valueOf(getSuryo1()));
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
