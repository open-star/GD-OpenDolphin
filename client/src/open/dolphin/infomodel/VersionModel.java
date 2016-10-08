/*
 * Version.java
 * Copyright (C) 2004 Digital Globe, Inc. All rights reserved.
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
import open.dolphin.log.LogWriter;
import open.dolphin.queries.DolphinQuery;

/**
 * バージョン情報 MEMO:マッピング
 * VersionModel
 * 
 * @author Kazushi Minagawa
 *
 */
public class VersionModel extends InfoModel {//id

    private static final long serialVersionUID = 5196580420531493579L;
    private int number; //バージョン番号
    private int revision; //リビジョン
    private String releaseNote;//リリースノート

    /**
     *　初期値としてバージョンは１
     */
    public void initialize() {
        number = 1;
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
     * バージョンナンバーを得る
     * @return バージョン.リビジョン
     */
    public String getVersionNumber() {
        StringBuilder buf = new StringBuilder();
        buf.append(String.valueOf(number));
        buf.append(".");
        buf.append(String.valueOf(revision));
        return buf.toString();
    }

    /**
     * バージョン.リビジョンというフォーマットの文字列から値を設定
     * @param vn バージョンナンバー
     */
    public void setVersionNumber(String vn) {
        int index = vn.indexOf('.');
        try {
            if (index >= 0) {
                String n = vn.substring(0, index);
                String r = vn.substring(index + 1);
                number = Integer.parseInt(n);
                revision = Integer.parseInt(r);
            } else {
                number = Integer.parseInt(vn);
            }
        } catch (NumberFormatException e) {
            LogWriter.error(this.getClass(), "", e);
        }
    }

    /**
     * バージョンナンバーを加算
     */
    public void incrementNumber() {
        number++;
    }

    /**
     * リビジョンナンバーの加算
     */
    public void incrementRevision() {
        revision++;
    }

    /**
     * リリースノートのSetter
     * @param releaseNote releaseNoteリリースノート
     */
    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    /**
     *　リリースノートのGetter
     * @return リリースノート
     */
    public String getReleaseNote() {
        return releaseNote;
    }

    /**
     * XMLとしてシリアライズ
     * @param result ライター
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        result.append("<VersionModel " + "number='" + Integer.toString(number) + "' revision='" + Integer.toString(revision) + "' releaseNote='" + releaseNote + "' />" + System.getProperty("line.separator"));
    }
}
