/*
 * DolphinException.java
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
package open.dolphin.exception;

/**
 * DolphinException
 * 
 * @author  Kazushi Minagawa
 */
public class DolphinException extends java.lang.Exception {

    /**
     *
     */
    private static final long serialVersionUID = 5819686319960053569L;

    /**
     *
     */
    public DolphinException() {
    }

    /**
     *
     * @param msg
     */
    public DolphinException(String msg) {
        super(msg);
    }

    /**
     *
     * @param t
     */
    public DolphinException(java.lang.Throwable t) {
        super(t);
    }

    /**
     *
     * @param s
     * @param t
     */
    public DolphinException(String s, java.lang.Throwable t) {
        super(s, t);
    }
}
