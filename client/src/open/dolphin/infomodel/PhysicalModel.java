/*
 * AllergyItem.java
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

import open.dolphin.log.LogWriter;
import open.dolphin.queries.DolphinQuery;
import open.dolphin.utils.StringTool;

/**
 * 測定値 MEMO:マッピング
 * PhysicalModel
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class PhysicalModel extends InfoModel implements Comparable {//id

    private static final long serialVersionUID = 5923780180643179995L;
    private long heightId;
    private long weightId;
    private String height;    // 身長
    private String weight;  // 体重
    private int bmi;  // BMI MEMO:Unused?
    private String identifiedDate;    // 同定日
    private String memo;  // メモ

    /**
     * デフォルトコンストラクタ
     */
    public PhysicalModel() {
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
     * heightIdのGetter
     * @return
     */
    public long getHeightId() {
        return heightId;
    }

    /**
     * heightIdのSetter
     * @param heightId
     */
    public void setHeightId(long heightId) {
        this.heightId = heightId;
    }

    /**
     * weightIdのGetter
     * @return
     */
    public long getWeightId() {
        return weightId;
    }

    /**
     * weightIdのSetter
     * @param weightId
     */
    public void setWeightId(long weightId) {
        this.weightId = weightId;
    }

    // factor
    /**
     * heightのGetter
     * @return
     */
    public String getHeight() {
        return height;
    }

    /**
     * heightのSetter
     * @param value
     */
    public void setHeight(String value) {
        height = StringTool.zenkakuNumToHankaku(value);
    }

    /**
     * identifiedDateのGetter
     * @return
     */
    public String getIdentifiedDate() {
        return identifiedDate;
    }

    /**
     * identifiedFormatDateのGetter
     * @return
     */
    public String getIdentifiedFormatDate() {
        return ModelUtils.Convert(IInfoModel.DATE_WITHOUT_TIME, IInfoModel.KARTE_DATE_FORMAT_WITHOUT_TIME, identifiedDate);
    }

    /**
     * identifiedDateのSetter
     * @param value
     */
    public void setIdentifiedDate(String value) {
        identifiedDate = value;
    }

    /**
     * memoのGetter
     * @return
     */
    public String getMemo() {
        return memo;
    }

    /**
     * memoのSetter
     * @param value
     */
    public void setMemo(String value) {
        memo = value;
    }

    /**
     * weightのSetter
     * @param severity
     */
    public void setWeight(String severity) {
        //   this.weight = severity;
        weight = StringTool.zenkakuNumToHankaku(severity);
    }

    /**
     * weightのGetter
     * @return
     */
    public String getWeight() {
        return weight;
    }

    /**
     * bmiのGetter
     * @return
     */
    public String getBmi() {
//        if (bmi == null) {
//            bmi = calcBmi();
//        }
//        return bmi;
        return calcBmi();
    }

    /**
     * @return Returns the bmi.
     */
    public String calcBmi() {
        if (height != null && weight != null) {

            //       weight = StringTool.zenkakuNumToHankaku(weight);
            //      height = StringTool.zenkakuNumToHankaku(height);

            float fw = new Float(weight).floatValue();
            float fh = new Float(height).floatValue() / 100f;

            //    float bmif = (10000f * fw) / (fh * fh);

            float bmif = fw / (fh * fh);

            String bmiS = String.valueOf(bmif);
            int index = bmiS.indexOf('.');
            int len = bmiS.length();
            if (index > 0 && (index + 2 < len)) {
                bmiS = bmiS.substring(0, index + 2);
            }
            return bmiS;
        }
        return null;
    }

    /**
     * standardWeightのGetter
     * @return
     */
    public String getStandardWeight() {
        if (getHeight() == null) {
            return null;
        }
        try {
            float h = Float.parseFloat(getHeight());
            h /= 100.0f;
            float stW = 22.0f * (h * h);
            String stWS = String.valueOf(stW);
            int index = stWS.indexOf('.');
            if (index > 0) {
                stWS = stWS.substring(0, index + 2);
            }
            return stWS;

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
        return null;
    }

    /**
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(Object other) {
        if (other != null && getClass() == other.getClass()) {
            String val1 = getIdentifiedDate();
            String val2 = ((PhysicalModel) other).getIdentifiedDate();
            return val1.compareTo(val2);
        }
        return 1;
    }
}
