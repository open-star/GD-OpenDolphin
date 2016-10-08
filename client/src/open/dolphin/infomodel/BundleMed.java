/*
 * BundleMed.java
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
 * スタンプ MEMO:マッピング
 * BundleMed
 * @author Minagawa,Kazushi
 */
public class BundleMed extends BundleDolphin {//id

    private static final long serialVersionUID = -3898329425428401649L;

    /**
     * Creates a new instance of BundleMed
     */
    public BundleMed() {
    }

    /**
     * バンドル内をクエリ。オーバーライドだけ。
     * @param query 条件
     * @return true false 合致すれば真
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (super.search(query)) {
            return true;
        }
        return false;
    }

    /**
     *　表示用文字列を得る
     * @return　表示用文字列
     */
    public String getAdminDisplayString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getDisplayString());
        buf.append(" x ");
        buf.append(getBundleNumber());
        buf.append(getUnit());
        return buf.toString();
    }

    /**
     *　表示用文字列を得る
     * @return　表示用文字列
     */
    public String getDisplayString() {
        StringBuilder buf = new StringBuilder();
        if (admin != null && (!admin.equals(""))) {
            if (admin.startsWith("内服")) {
                buf.append(admin.substring(0, 2));
                buf.append(" ");
                buf.append(admin.substring(4));
            } else {
                buf.append(admin);
            }
        }
        return buf.toString();
    }

    /**
     * 単位の文字列を得る
     * @return 単位の文字列
     */
    public String getUnit() {
        if (admin != null && (!admin.equals(""))) {
            if (admin.startsWith("内服")) {
                if (admin.charAt(3) == '回') {
                    return " 日分";
                }
            }
        }
        return " 回分";
    }

    /**
     * 文字列へシリアライズ
     * @return　文字列
     */
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        buf.append("RP");
        buf.append(System.getProperty("line.separator"));

        ClaimItem[] items = getClaimItem();
        if (items != null) {
            int len = items.length;
            ClaimItem item;
            String number;

            for (int i = 0; i < len; i++) {
                item = items[i];
                buf.append("・");
                buf.append(item.getName());
                buf.append("　");
                number = item.getNumber();

                if (number != null) {
                    buf.append(number);
                    if (item.getUnit() != null) {
                        buf.append(item.getUnit());
                    }
                }
                buf.append(System.getProperty("line.separator"));
            }

            if (admin != null && (!admin.equals(""))) {
                if (admin.startsWith("内服")) {
                    buf.append(admin.substring(0, 2));
                    buf.append(" ");
                    buf.append(admin.substring(4));
                } else {
                    buf.append(admin);
                }
            }

            buf.append(" x ");
            buf.append(getBundleNumber());

            if (admin != null && (!admin.equals(""))) {
                // FIXME
                if (admin.startsWith("内服")) {
                    if (admin.charAt(3) == '回') {
                        buf.append(" 日分");
                    }
                }
                buf.append(System.getProperty("line.separator"));
            }

            // Print admMemo
            if (adminMemo != null) {
                buf.append(adminMemo);
                buf.append(System.getProperty("line.separator"));
            }

            // Print Memo
            if (memo != null) {
                buf.append(memo);
                buf.append(System.getProperty("line.separator"));
            }

            //buf.append("\n");
            return buf.toString();
        } else {
            return "";
        }
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    @Override
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
