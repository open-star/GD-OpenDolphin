/*
 * ProjectFactory.java
 *
 * Copyright (C) 2002 Dolphin Project. All rights reserved.
 * Copyright (C) 2003 Digital Globe, Inc. All rights reserved.
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
package open.dolphin.project;

import java.awt.Window;
import open.dolphin.client.SaveParams;

import open.dolphin.infomodel.ID;

/**
 * プロジェクトに依存するオブジェクトを生成するファクトリクラス。????  Dolphin
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public abstract class AbstractProjectFactory {

    private static DolphinFactory dolphin;

    /**
     * Creates new ProjectFactory
     */
    public AbstractProjectFactory() {
    }

    /**
     *
     * @param proj
     * @return
     */
    public static AbstractProjectFactory getProjectFactory(String proj) {

        if (dolphin == null) {
            dolphin = new DolphinFactory();
        }
        return dolphin;
    }

    /**
     *
     * @param uploaderAddress
     * @param share
     * @param facilityId
     * @return
     */
    public abstract String createCSGWPath(String uploaderAddress, String share, String facilityId);

    /**
     *
     * @return
     */
    public abstract Object createAboutDialog();

    /**
     *
     * @param pid
     * @param facilityId
     * @return
     */
    public abstract ID createMasterId(String pid, String facilityId);

    /**
     *
     * @param parent
     * @param params
     * @return
     */
    public abstract Object createSaveDialog(Window parent, SaveParams params);
}
