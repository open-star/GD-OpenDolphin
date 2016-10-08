/*
 * ID.java
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

import open.dolphin.queries.DolphinQuery;

/**
 * ID
 * 
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class ID extends InfoModel {//id

    private static final long serialVersionUID = -5070888481802856042L;
    String id;
    String idType;
    String idTypeTableId;

    /** Creates a new instance of ID */
    public ID() {
    }

    /**
     *
     * @param id
     * @param idType
     * @param idTypeTableId
     */
    public ID(String id, String idType, String idTypeTableId) {
        this();
        this.id = id;
        this.idType = idType;
        this.idTypeTableId = idTypeTableId;
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
     * idのGetter
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * idのSetter
     * @param val id
     */
    public void setId(String val) {
        id = val;
    }

    /**
     * idTypeのSetter
     * @param idType
     */
    public void setIdType(String idType) {
        this.idType = idType;
    }

    /**
     * idTypeのGetter
     * @return idType
     */
    public String getIdType() {
        return idType;
    }

    /**
     * idTypeTableIdのSetter
     * @param idTypeTableId
     */
    public void setIdTypeTableId(String idTypeTableId) {
        this.idTypeTableId = idTypeTableId;
    }

    /**
     * idTypeTableIdのGetter
     * @return idTypeTableId
     */
    public String getIdTypeTableId() {
        return idTypeTableId;
    }
}
