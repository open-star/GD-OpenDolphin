/*
 * ClaimBundle.java
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
import java.util.GregorianCalendar;
import java.util.List;
import open.dolphin.log.LogWriter;
import open.dolphin.queries.DolphinQuery;
import open.dolphin.utils.StringTool;

/**
 * カルテ要素 MEMO:マッピング
 * ClaimBundle
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class ClaimBundle extends InfoModel {

    private static final long serialVersionUID = -7332175271243905003L;
    /**
     *
     */
    public static final String DEFAULT_BUNDLE_NUMBER = "1";

    /**
     *
     */
    public static enum Enabled {

        /**
         *
         */
        NORMAL,
        /**
         *
         */
        OUT_OF_DATE,
        /**
         *
         */
        UPDATE_AVAILABLE,
        
        DELETED
    };
    /**
     *
     */
    protected String className;           // 診療行為名
    /**
     *
     */
    protected String classCode;           // 診療行為コード
    /**
     *
     */
    protected String classCodeSystem;     // コード体系
    /**
     *
     */
    protected String admin;               // 用法
    /**
     *
     */
    protected String adminCode;           // 用法コード
    /**
     *
     */
    protected String adminCodeSystem;     // 用法コード体系
    /**
     *
     */
    protected String adminMemo;           // 用法メモ
    private String bundleNumber; // バンドル数
    /**
     *
     */
    protected ClaimItem[] claimItems;      // バンドル構成品目
    /**
     *
     */
    protected String memo;                // メモ
    /**
     *
     */
    protected String status;

    /**
     * コンストラクタ
     */
    public ClaimBundle() {
    }

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (className != null) {
            if (className.indexOf(query.what("keyword")) != -1) {
                return true;
            }
        }
        if (memo != null) {
            if (memo.indexOf(query.what("keyword")) != -1) {
                return true;
            }
        }
        for (ClaimItem item : getClaimItem()) {
            if (item.search(query)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 診療行為名のGetter
     * @return 診療行為名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 診療行為名のSetter
     * @param val 診療行為名
     */
    public void setClassName(String val) {
        className = val;
    }

    /**
     * 診療行為コードのGetter
     * @return 診療行為コード
     */
    public String getClassCode() {
        return classCode;
    }

    /**
     * 診療行為コードのSetter
     * @param val 診療行為コード
     */
    public void setClassCode(String val) {
        classCode = val;
    }

    /**
     * コード体系のGetter
     * @return コード体系
     */
    public String getClassCodeSystem() {
        return classCodeSystem;
    }

    /**
     * コード体系のSetter
     * @param val コード体系
     */
    public void setClassCodeSystem(String val) {
        classCodeSystem = val;
    }

    /**
     * 用法のGetter
     * @return 用法
     */
    public String getAdmin() {
        return admin;
    }

    /**
     * 用法のSetter
     * @param val 用法
     */
    public void setAdmin(String val) {
        admin = val;
    }

    /**
     * 用法コードのGetter
     * @return 用法コード
     */
    public String getAdminCode() {
        return adminCode;
    }

    /**
     * 用法コードのSetter
     * @param val 用法コード
     */
    public void setAdminCode(String val) {
        adminCode = val;
    }

    /**
     * 用法コード体系のGetter
     * @return 用法コード体系
     */
    public String getAdminCodeSystem() {
        return adminCodeSystem;
    }

    /**
     * 用法コード体系のSetter
     * @param val 用法コード体系
     */
    public void setAdminCodeSystem(String val) {
        adminCodeSystem = val;
    }

    /**
     * 用法メモのGetter
     * @return 用法メモ
     */
    public String getAdminMemo() {
        return adminMemo;
    }

    /**
     * 用法メモのSetter
     * @param val 用法メモ
     */
    public void setAdminMemo(String val) {
        adminMemo = val;
    }

    /**
     * 個数のGetter
     * @return バンドル数
     */
    public String getBundleNumber() {

        if (bundleNumber == null) {
            return DEFAULT_BUNDLE_NUMBER;
        } else if (bundleNumber.equals("")) {
            return DEFAULT_BUNDLE_NUMBER;
        } else {
            return bundleNumber;
        }
    }

    /**
     * 個数のSetter
     * @param val バンドル数
     */
    public void setBundleNumber(String val) {
        try {
            setBundleNumber(Integer.parseInt(StringTool.zenkakuNumToHankaku(val.trim())));
        } catch (NumberFormatException nfe) {
            LogWriter.error(getClass(), nfe);
        }
    }

    /**
     * 個数のSetter
     * @param n バンドル数
     */
    public void setBundleNumber(int n) {
        if (n > 0) {
            bundleNumber = String.valueOf(n);
        }
    }

    /**
     * バンドル構成品目のGetter
     * @return バンドル構成品目
     */
    public ClaimItem[] getClaimItem() {
        return claimItems;
    }

    /**
     * バンドル構成品目のSetter
     * @param val バンドル構成品目
     */
    public void setClaimItem(ClaimItem[] val) {
        claimItems = val;
    }

    /**
     * claimItemを追加
     * @param val claimItem
     */
    public void addClaimItem(ClaimItem val) {
        if (getClaimItem() == null) {
            setClaimItem(new ClaimItem[1]);
            getClaimItem()[0] = val;
            return;
        }
        int len = getClaimItem().length;
        ClaimItem[] dest = new ClaimItem[len + 1];
        System.arraycopy(getClaimItem(), 0, dest, 0, len);
        setClaimItem(dest);
        getClaimItem()[len] = val;
    }

    /**
     * スタンプのメモを返す。
     * @return スタンプのメモ
     */
    public String getMemo() {
        return memo;
    }

    /**
     * スタンプのメモを設定する。
     * @param memo スタンプのメモ
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * スタンプのロールを返す。
     * @return スタンプのロール
     */
    public String getStatus() {
        return status;
    }

    /**
     * スタンプのステータスを設定する。
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *　CLaimItemが有効な日付のものか
     * @param resultSet　未使用
     * @param gc　カレンダー
     * @return
     */
    public Enabled getStampEnabled(List<MedicineEntry> resultSet, GregorianCalendar gc) {
        ClaimItem[] importClaims = getClaimItem();
        if (importClaims != null) {
            for (ClaimItem item : importClaims) {
                //     if (item.isExpired()) {
                if (item.useState(gc) != 1) {
                    return Enabled.UPDATE_AVAILABLE;
                }
            }
        }
        return Enabled.NORMAL;
    }

    /**
     * シリアライズ
     * @param result ライター。シリアライズ先。
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        result.append("<ClaimBundle " + "className='" + className + "' classCode='" + classCode + "' classCodeSystem='" + classCodeSystem + "' admin='" + admin + "' adminCode='" + adminCode + "' adminCodeSystem='" + adminCodeSystem + "' adminMemo='" + adminMemo + "' bundleNumber='" + bundleNumber + "' memo='" + memo + "'>" + System.getProperty("line.separator"));
        for (ClaimItem item : getClaimItem()) {
            item.serialize(result);
        }
        result.append("</ClaimBundle>");
    }

    /**
     * 何もしない
     * @param result ライター。シリアライズ先。
     * @throws IOException
     */
    public void deserialize(Writer result) throws IOException {
        //TODO deserialize
    }
}
