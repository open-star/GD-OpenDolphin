/*
 * AgeCalculator.java
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
package open.dolphin.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * Utility to calculate Gregorian Time.
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class AgeCalculator {

    /** Creates new AgeCalculator */
    public AgeCalculator() {
    }

    /**
     * MML日付形式（YYYY-MM-DD）の誕生日から年齢を返す
     * @param mmlBirthday 誕生日（YYYY-MM-DD）
     * @return 年齢（歳）。６歳以下の場合は、ヶ月も返す。
     */
    public static String getAge(String mmlBirthday) {
        try {
            GregorianCalendar birthday = getCalendar(mmlBirthday);
            GregorianCalendar today = new GregorianCalendar(); // Today

            int todaymonth = today.get(Calendar.MONTH) + (today.get(Calendar.YEAR) * 12);
            int birthmonth = birthday.get(Calendar.MONTH) + (birthday.get(Calendar.YEAR) * 12);
            int pastmonth = todaymonth - birthmonth;

            //誕生日を迎えていなければ、経過月数から１を引く。
            String birthdayDate = mmlBirthday.substring(8);
            int birthDate = Integer.parseInt(birthdayDate);
            Calendar cal = Calendar.getInstance();
            int todayDate = cal.get(Calendar.DATE);
            if (todayDate < birthDate) {
                pastmonth--;
            }

            int month = pastmonth % 12;
            int years = pastmonth / 12;
          
            StringBuffer buf = new StringBuffer();

            if (years < 10) {
                buf.append("  ");
            } else {
                buf.append(" ");
            }

            buf.append(years);
            buf.append("歳");
            //小児のみ月を表示（小児は6歳まで）
            if (years <= 6) {
                if (month != 0) {
                    if (month < 10) {
                        buf.append("0");
                    }
                    buf.append(month);
                    buf.append("ヶ月");
                }
            }
            return buf.toString();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns GregorianCalendar from MML Date format.
     * @params mmlDate (YYYY-MM-DD)
     * @return GregorianCalendar of birthday
     */
    private static GregorianCalendar getCalendar(String mmlDate) {

        // Trim time if contains
        int index = mmlDate.indexOf('T');
        if (index != -1) {
            mmlDate.substring(0, index);
        }
        StringTokenizer st = new StringTokenizer(mmlDate, "-");
        String yearSt = st.nextToken();
        String monthSt = st.nextToken();
        if (monthSt.startsWith("0")) {
            monthSt = monthSt.substring(1);
        }
        String daySt = st.nextToken();
        if (daySt.startsWith("0")) {
            daySt = daySt.substring(1);
        }
        int year = Integer.parseInt(yearSt);
        int month = Integer.parseInt(monthSt);
        month--;
        int day = Integer.parseInt(daySt);

        return new GregorianCalendar(year, month, day);
    }
}
