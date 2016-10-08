/*
 * IStampInfo.java
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
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
import open.dolphin.queries.DolphinQuery;

/**
 *
 * @author oda
 */
public interface IStampInfo {

    /**
     * 検索
     * @param query
     * @return true false
     */
    public boolean search(DolphinQuery query);

    /**
     *
     * @param stampId
     * @param name
     * @param text
     * @param entity
     * @param _role
     */
    public void initialize(String stampId, String name, String text, String entity, String _role);

    /**
     * stampNameのGetter
     * @return
     */
    public String getStampName();

    /**
     * stampNameのSetter
     * @param text
     */
    public void setStampName(String text);

    /**
     * StampRoleのGetter
     * @return
     */
    public String getStampRole();

    /**
     * stampRoleのSetter
     * @param _role
     */
    public void setStampRole(String _role);

    /**
     * stampStatusのGetter
     * @return
     */
    public String getStampStatus();

    /**
     * stampStatusのSetter
     * @param status
     */
    public void setStampStatus(String status);

    /**
     * entityのGetter
     * @return
     */
    public String getEntity();

    /**
     * entityのSetter
     * @param entity
     */
    public void setEntity(String entity);

    /**
     *
     * @return
     */
    public boolean isSerialized();

    /**
     *
     * @return
     */
    public boolean isASP();

    /**
     * ASPのSetter
     * @param asp
     */
    public void setASP(boolean asp);

    /**
     * stampIdのGetter
     * @return
     */
    public String getStampId();

    /**
     * stampIdのSetter
     * @param id
     */
    public void setStampId(String id);

    /**
     * stampMemoのGetter
     * @return
     */
    public String getStampMemo();

    /**
     * stampMemoのSetter
     * @param memo
     */
    public void setStampMemo(String memo);

    /**
     * editableのSetter
     * @param editable
     */
    public void setEditable(boolean editable);

    /**
     *
     * @return
     */
    public boolean isEditable();

    /**
     * turnInのSetter
     * @param turnIn
     */
    public void setTurnIn(boolean turnIn);

    /**
     *
     * @return
     */
    public boolean isTurnIn();

    /**
     *
     * @return
     */
    public String toString();

    /**
     * stampNumberのSetter
     * @param stampNumber
     */
    public void setStampNumber(int stampNumber);

    /**
     * stampNumberのGetter
     * @return
     */
    public int getStampNumber();

    /**
     *
     * @param other
     * @return
     */
    public int compareTo(Object other);

    /**
     * シリアライズ
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException;
}
