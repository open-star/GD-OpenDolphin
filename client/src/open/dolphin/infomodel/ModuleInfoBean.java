/*
 * ModuleInfo.java
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

import java.io.Serializable;
import open.dolphin.utils.CombinedStringParser;
import java.io.IOException;
import java.io.Writer;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import open.dolphin.queries.DolphinQuery;

/**
 * Stamp 及び Module の属性を保持する MEMO:マッピング
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
@Embeddable
public class ModuleInfoBean extends InfoModel implements IStampInfo, Comparable, Serializable {//id

    @Column(nullable = false)
    private String stampName;    // Module 名: StampTree、 オーダ履歴当に表示する名前
    @Column(nullable = false)
    private String stampRole;    // SOA または P の役割
    @Column(nullable = false)
    private int stampNumber;     // ドキュメントに出現する順番
    @Column(nullable = false)
    private String entity;       // 情報の実体名
    @Transient
    private boolean editable = true;    // 編集可能かどうか
    @Transient
    private boolean asp;       // ASP 提供か
    @Transient
    private String stampId;    // DB 保存されている場合、そのキー
    @Transient
    private String memo;       // Memo の内容説明
    @Transient
    private boolean turnIn;    // 折り返し表示するかどうか

    /**
     * ModuleInfoオブジェクトを生成する。
     */
    public ModuleInfoBean() {

        if (stampRole == null) {
            CombinedStringParser line = new CombinedStringParser('|', "");
            line.limit(2);
            line.set(0, "");
            line.set(1, "");
            stampRole = line.toCombinedString();
        } else {
            CombinedStringParser line = new CombinedStringParser('|', stampRole);
            line.limit(2);
            line.set(1, "");
            stampRole = line.toCombinedString();
        }
    }

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (stampName != null) {
            if (stampName.indexOf(query.what("keyword")) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param stampId
     * @param name
     * @param text
     * @param entity
     * @param _role
     */
    @Override
    public void initialize(String stampId, String name, String text, String entity, String _role) {
        setStampId(stampId);                   // Stamp ID
        setStampName(name);                    //
        setStampMemo(text);                    // Tooltip
        setEntity(entity);     // カテゴリ
        setStampRole(_role);
    }

    /**
     * スタンプ名を返す。
     * @return スタンプ名
     */
    @Override
    public String getStampName() {
        return stampName;
    }

    /**
     * スタンプ名を設定する。
     * @param text
     */
    @Override
    public void setStampName(String text) {
        this.stampName = text;
    }

    /**
     * スタンプのロールを返す。
     * @return スタンプのロール
     */
    @Override
    public String getStampRole() {
        CombinedStringParser line = new CombinedStringParser('|', stampRole);
        line.limit(2);
        return line.get(0);
    }

    /**
     * スタンプのロールを設定する。
     * @param _role
     */
    @Override
    public void setStampRole(String _role) {
        CombinedStringParser line = new CombinedStringParser('|', stampRole);
        line.limit(2);
        line.set(0, _role);
        stampRole = line.toCombinedString();
    }

    /**
     * スタンプのステータスを返す。
     * @return スタンプのロール
     */
    public String getStampStatus() {
        CombinedStringParser line = new CombinedStringParser('|', stampRole);
        line.limit(2);
        return line.get(1);
    }

    /**
     * スタンプのステータスを設定する。
     * @param status
     */
    @Override
    public void setStampStatus(String status) {

        CombinedStringParser line = new CombinedStringParser('|', stampRole);
        line.limit(2);
        line.set(1, status);
        stampRole = line.toCombinedString();
    }

    /**
     * スタンプのエンティティ名を返す。
     * @return エンティティ名
     */
    @Override
    public String getEntity() {
        return entity;
    }

    /**
     * スタンプのエンティティ名を設定する。
     * @param entity エンティティ名
     */
    @Override
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * シリアライズされているかどうかを返す。
     * @return シリアライズされている時 true
     */
    @Override
    public boolean isSerialized() {
        return stampId != null;
    }

    /**
     * ASP提供かどうかを返す。
     * @return ASP提供の時 true
     */
    @Override
    public boolean isASP() {
        return asp;
    }

    /**
     * ASP提供を設定する。
     * @param asp ASP提供の真偽値
     */
    @Override
    public void setASP(boolean asp) {
        this.asp = asp;
    }

    /**
     * Databseに保存されている時の PK を変えす。
     * @return Primary Key
     */
    @Override
    public String getStampId() {
        return stampId;
    }

    /**
     * Databseに保存される時の PK を設定する。
     * @param id Primary Key
     */
    @Override
    public void setStampId(String id) {
        stampId = id;
    }

    /**
     * スタンプのメモを返す。
     * @return スタンプのメモ
     */
    @Override
    public String getStampMemo() {
        return memo;
    }

    /**
     * スタンプのメモを設定する。
     * @param memo スタンプのメモ
     */
    @Override
    public void setStampMemo(String memo) {
        this.memo = memo;
    }

    /**
     * このスタンプが編集可能かどうかを設定する。
     * @param editable 編集可能かどうかの真偽値
     */
    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * このスタンプが編集可能かどうかを返す。
     * @return 編集可能の時 true
     */
    @Override
    public boolean isEditable() {
        return editable;
    }

    /**
     * turnInのSetter
     * @param turnIn
     */
    @Override
    public void setTurnIn(boolean turnIn) {
        this.turnIn = turnIn;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isTurnIn() {
        return turnIn;
    }

    /**
     * 文字列表現を返す。
     * @return スタンプ名
     */
    @Override
    public String toString() {
        // 病名でエイリアスがあればそれを返す
        if (this.entity.equals(ENTITY_DIAGNOSIS)) {
            String alias = ModelUtils.getDiagnosisAlias(stampName);
            return alias != null ? alias : stampName;
        }

        return stampName;
    }

    /**
     * ドキュメント内の出現番号を設定する。
     * @param stampNumber　出現する番号
     */
    @Override
    public void setStampNumber(int stampNumber) {
        this.stampNumber = stampNumber;
    }

    /**
     * ドキュメント内の出現番号を返す。
     * @return ドキュメント内の出現番号
     */
    @Override
    public int getStampNumber() {
        return stampNumber;
    }

    /**
     * スタンプ番号で比較する。
     * @param other
     * @return 比較値
     */
    @Override
    public int compareTo(Object other) {
        if (other != null && getClass() == other.getClass()) {
            int result = getStampNumber() - ((ModuleInfoBean) other).getStampNumber();
            return result;
        }

        return -1;
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
        result.append("<ModuleInfoBean name='" + nullToString(stampName) + "' role='" + nullToString(stampRole) + "' stampNumber='" + Integer.toString(stampNumber) + "' entity='" + nullToString(entity) + "' />" + System.getProperty("line.separator"));
    }
}
