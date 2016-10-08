/*
 * BundleDolphin.java
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
 * 複数Claimを収める MEMO:マッピング
 * BundleDolphin
 *
 * @author  Minagawa,Kazushi
 */
public class BundleDolphin extends ClaimBundle {

    private static final long serialVersionUID = -8747202550129389855L;
    private String orderName;

    /** 
     * Creates a new instance of BundleDolphin
     */
    public BundleDolphin() {
    }

    /**
     * バンドル内をクエリ
     * @param query 条件
     * @return true false 合致すれば真
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (super.search(query)) {
            return true;
        }
        if (orderName != null) {
            return (orderName.indexOf(query.what("keyword")) != -1);
        }
        return false;
    }

    /**
     * orderNameのSetter
     * @param orderName
     */
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    /**
     * orderNameのGetter
     * @return orderName
     */
    public String getOrderName() {
        return orderName;
    }

    /**
     * バンドルされたオブジェクトの名前をカンマ区切りで得る
     * @return カンマ区切り
     */
    public String getItemNames() {
        if (claimItems != null && claimItems.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < claimItems.length; i++) {
                if (!sb.toString().isEmpty()) {
                    sb.append(", ");
                }
                sb.append(claimItems[i].getName());
                if (SinryoCode.isComplexComment(claimItems[i].getCode())) {
                    sb.append(" " + claimItems[i].getNumber());
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * ストリングにシリアライズ
     * @return ストリング
     */
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        // Print order name
        buf.append(orderName);
        buf.append(System.getProperty("line.separator"));
        ClaimItem[] items = getClaimItem();
        if (items != null) {
            int len = items.length;
            ClaimItem item;
            String number;

            for (int i = 0; i < len; i++) {
                item = items[i];

                // Print item name
                buf.append("・");
                buf.append(item.getName());

                // Print item number
                number = item.getNumber();
                if (number != null) {
                    buf.append("　");
                    buf.append(number);
                    if (item.getUnit() != null) {
                        buf.append(item.getUnit());
                    }
                }
                buf.append(System.getProperty("line.separator"));
            }

            // Print bundleNumber
            if (!getBundleNumber().equals("1")) {
                buf.append("X　");
                buf.append(getBundleNumber());
                buf.append(System.getProperty("line.separator"));
            }

            // Print admMemo
            if (adminMemo != null) {
                buf.append(adminMemo);
                buf.append(System.getProperty("line.separator"));
            }

            // Print bundleMemo
            if (memo != null) {
                buf.append(memo);
                buf.append(System.getProperty("line.separator"));
            }

            return buf.toString();
        } else {
            return "";
        }
    }

    /**
     *　MEMO:何もしない
     * @param result
     * @throws IOException
     */
    @Override
    public void serialize(Writer result) throws IOException {
        //TODO serialize
    }
}
