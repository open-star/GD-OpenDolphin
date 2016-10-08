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

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * AllergyModel MEMO:マッピング
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class AllergyModel extends InfoModel implements Comparable {//id

    private static final long serialVersionUID = -6327488237646390391L;
    private long observationId;
    private String factor;   // 要因
    private String severity;  // 反応程度
    private String severityTableId; // コード体系
    private String identifiedDate;// 同定日
    private String memo; // メモ

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (factor != null) {
            return (factor.indexOf(query.what("keyword")) != -1);
        }
        return false;
    }

    /**
     * 要因のGetter
     * @return 要因
     */
    public String getFactor() {
        return factor;
    }

    /**
     * 要因のSetter
     * @param factor 要因
     */
    public void setFactor(String factor) {
        this.factor = factor;
    }

    /**
     * 同定日のGetter
     * @return 同定日
     */
    public String getIdentifiedDate() {
        return identifiedDate;
    }

    /**
     * 書式化された同定日のGetter
     * @return 書式化された同定日
     */
    public String getIdentifiedFormatDate() {
        return ModelUtils.Convert(IInfoModel.DATE_WITHOUT_TIME, IInfoModel.KARTE_DATE_FORMAT_WITHOUT_TIME, identifiedDate);
    }

    /**
     * 同定日のSetter
     * @param identifiedDate 同定日
     */
    public void setIdentifiedDate(String identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    /**
     * メモのGetter
     * @return メモ
     */
    public String getMemo() {
        return memo;
    }

    /**
     * メモのSetter
     * @param memo メモ
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 反応程度のGetter
     * @return 反応程度
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * 反応程度のSetter
     * @param severity 反応程度
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * コード体系のGetter
     * @return コード体系
     */
    public String getSeverityTableId() {
        return severityTableId;
    }

    /**
     * コード体系のSetter
     * @param severityTableId コード体系
     */
    public void setSeverityTableId(String severityTableId) {
        this.severityTableId = severityTableId;
    }

    /**
     * 同定日で比較する。
     * @param other 比較対象オブジェクト
     * @return other 比較値
     */
    @Override
    public int compareTo(Object other) {
        if (other != null && getClass() == other.getClass()) {
            String val1 = getIdentifiedDate();
            String val2 = ((AllergyModel) other).getIdentifiedDate();
            return val1.compareTo(val2);
        }
        return 1;
    }

    /**
     * observationIdのGetter
     * @return observationId
     */
    public long getObservationId() {
        return observationId;
    }

    /**
     * observationIdのSetter
     * @param observationId
     */
    public void setObservationId(long observationId) {
        this.observationId = observationId;
    }

    /**
     * シリアライズ
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        result.append("<AllergyModel " + "observationId='" + Long.toString(observationId) + "' factor='" + factor + "' severity='" + severity + "' severityTableId='" + severityTableId + "' identifiedDate='" + identifiedDate + "' memo='" + memo + "' />" + System.getProperty("line.separator"));
    }
}
