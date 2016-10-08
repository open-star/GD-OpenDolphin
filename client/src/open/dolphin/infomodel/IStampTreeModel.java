/*
 * StatusPanel.java
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

import java.util.Date;

/**
 * StampTreeModel
 * Userのパーソナルツリークラス。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public interface IStampTreeModel {
    
    /**
     * idのGetter
     * @return
     */
    public long getId();
    
    /**
     * idのSetter
     * @param id
     */
    public void setId(long id);
    
    /**
     * userのGetter
     * @return
     */
    public UserModel getUser();
    
    /**
     * nameのGettrt
     * @return
     */
    public String getName();
    
    /**
     * publishTypeのGetter
     * @return
     */
    public String getPublishType();
    
    /**
     * publishTypeのSetter
     * @param publishType
     */
    public void setPublishType(String publishType);
    
    /**
     * categoryのGetter
     * @return
     */
    public String getCategory();
    
    /**
     * categoryのSetter
     * @param category
     */
    public void setCategory(String category);
    
    /**
     * partyNameのGetter
     * @return
     */
    public String getPartyName();
    
    /**
     * urlのGetter
     * @return
     */
    public String getUrl();
    
    /**
     * descriptionのGetter
     * @return
     */
    public String getDescription();
    
    /**
     * publishedDateのGetter
     * @return
     */
    public Date getPublishedDate();
    
    /**
     * PublishedDateのSetter
     * @param lastUpdated
     */
    public void setPublishedDate(Date lastUpdated);
    
    /**
     * lastUpdatedのGetter
     * @return
     */
    public Date getLastUpdated();
    
    /**
     * lastUpdatedのSetter
     * @param lastUpdated
     */
    public void setLastUpdated(Date lastUpdated);
    
    /**
     * treeBytesのGetter
     * @return
     */
    public byte[] getTreeBytes();
    
    /**
     * treeXmlのGetter
     * @return
     */
    public String getTreeXml();
    
    /**
     * treeXmlのSetter
     * @param treeXml
     */
    public void setTreeXml(String treeXml);
}
