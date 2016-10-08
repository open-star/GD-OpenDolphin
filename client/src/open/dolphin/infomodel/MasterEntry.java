/*
 * MasterEntry.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
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

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import open.dolphin.queries.DolphinQuery;
import open.dolphin.utils.DateExpire;

/**
 * ORCAマスター基本クラス MEMO:MasterEntry
 * @author  Minagawa,Kazushi
 */
public class MasterEntry extends InfoModel implements java.lang.Comparable {//id

    private static final long serialVersionUID = -6170839610525955077L;
    /**
     *
     */
    protected static String refDate;

    static {
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        refDate = f.format(gc.getTime()).toString();
    }
    /**
     *
     */
    protected String code;
    /**
     *
     */
    protected String name;
    /**
     *
     */
    protected String kana;
    /**
     *
     */
    protected String startDate;
    /**
     *
     */
    protected String endDate;
    /**
     *
     */
    protected String disUseDate;

    /** Creates a new instance of DeseaseEntry */
    public MasterEntry() {
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
     * codeのGetter
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * codeのSetter
     * @param val
     */
    public void setCode(String val) {
        code = val;
    }

    /**
     * nameのGetter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * nameのSetter
     * @param val
     */
    public void setName(String val) {
        name = val;
    }

    /**
     * kanaのGetter
     * @return
     */
    public String getKana() {
        return kana;
    }

    /**
     * kanaのSetter
     * @param val
     */
    public void setKana(String val) {
        kana = val;
    }

    /**
     * startDateのGetter
     * @return
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * startDateのSetter
     * @param val
     */
    public void setStartDate(String val) {
        startDate = val;
    }

    /**
     * endDateのGetter
     * @return
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * endDateのSetter
     * @param val
     */
    public void setEndDate(String val) {
        endDate = val;
    }

    /**
     * disUseDateのGetter
     * @return
     */
    public String getDisUseDate() {
        return disUseDate;
    }

    /**
     * disUseDateのSetter
     * @param val
     */
    public void setDisUseDate(String val) {
        disUseDate = val;
    }

    @Override
    public int compareTo(Object obj) {
        MasterEntry other = (MasterEntry) obj;
        int myUse = DateExpire.compare(startDate, endDate);
        int otherUse = DateExpire.compare(other.getStartDate(), other.getEndDate());
        int result = 0;
        switch (myUse) {
            case -1:
                switch (otherUse) {
                    case -1:
                        result = code.compareTo(other.getCode());
                        break;
                    case 0:
                        result = 1;
                        break;
                    case 1:
                        result = -1;
                        break;
                    default:
                }
                break;
            case 0:
                switch (otherUse) {
                    case -1:
                        result = -1;
                        break;
                    case 0:
                        result = code.compareTo(other.getCode());
                        break;
                    case 1:
                        result = -1;
                        break;
                    default:
                }
                break;
            case 1:
                switch (otherUse) {
                    case -1:
                        result = 1;
                        break;
                    case 0:
                        result = 1;
                        break;
                    case 1:
                        result = code.compareTo(other.getCode());
                        break;
                    default:
                }
                break;
            default:
        }
        return result;
    }

    /**
     *
     * @param gc
     * @return
     */
    public int useState(GregorianCalendar gc) {
        return DateExpire.expireState(gc, startDate, endDate);
    }

    /**
     *
     * @return
     */
    public boolean isInUse() {
        return !DateExpire.expire(startDate, endDate);
    }

    /**
     *
     * @return
     */
    protected boolean isExpired() {
        return DateExpire.expire(startDate, endDate);
    }

    @Override
    public String toString() {
        return name;
    }
}
