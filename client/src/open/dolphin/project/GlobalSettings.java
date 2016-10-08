/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.project;

import java.awt.Color;
import open.dolphin.log.LogWriter;

/**
 * 広域なセッティングはここ。
 * @author
 */
public final class GlobalSettings {

    /**
     *
     */
    public static enum ColorSet {

        /**
         *
         */
        DEFAULT
    };

    /**
     *
     */
    public static enum Parts {

        /**
         *
         */
        TABLE_BACKGROUND,
        /**
         *
         */
        SEND_HISTORY,
        /**
         *
         */
        TEMPORARY_HISTORY,
        /**
         *
         */
        TODAY,
        /**
         *
         */
        MALE,
        /**
         *
         */
        FEMALE,
        /**
         *
         */
        UNKNOWN,
        /**
         *
         */
        ODD,
        /**
         *
         */
        EVEN,
        /**
         *
         */
        SUNDAY_FORE,
        /**
         *
         */
        SATURDAY_FORE,
        /**
         *
         */
        WEEKDAY_FORE,
        /**
         *
         */
        OUTOFMONTH_FORE,
        /**
         *
         */
        CALENDAR_BACK,
        /**
         *
         */
        TODAY_BACK,
        /**
         *
         */
        BIRTHDAY_BACK,
        /**
         *
         */
        PVT,
        /**
         *
         */
        EXAM_APPO,
        /**
         *
         */
        RP,
        /**
         *
         */
        TREATMENT,
        /**
         *
         */
        TEST,
        /**
         *
         */
        IMAGE,
        /**
         *
         */
        RED,
        /**
         *
         */
        ORANGE,
        /**
         *
         */
        YELLOW,
        /**
         *
         */
        GREEN,
        /**
         *
         */
        BLUE,
        /**
         *
         */
        PURPLE,
        /**
         *
         */
        GRAY,
        /**
         *
         */
        BLACK,
        /**
         *
         */
        CALENDAR_TITLE_BACK,
        /**
         *
         */
        CALENDAR_TITLE_FORE
    };

    /**
     * 体験版ならばtrue。
     * 体験版はホストのアドレスの設定ができなくなる。
     * INFORMATION　体験版と通常版の切り替えここ。
     * @return
     */
    public static boolean isTrial() {
        return false;
    }

    /*
     * カルテのスクロール速度
     */
    /**
     *
     * @return
     */
    public static int karteScrollUnitIncrement() {
        return 50;
    }

    /*
     * MacOS上でスクリーンメニューバーを使う場合はtrue
     * ウインドウメニューバーを使う場合はfalse
     */
    /**
     *
     * @return
     */
    public static boolean useScreenMenuBarOnMac() {
        return false;
    }

    /**
     *
     * @param part
     * @return
     */
    public static Color getColors(Parts part) {
        switch (part) {
            case TABLE_BACKGROUND:
                return new Color(112, 128, 144);
            case SEND_HISTORY:
                return new Color(227, 250, 207);
            case TEMPORARY_HISTORY:
                return new Color(255, 192, 203);
            case TODAY:
                return new Color(237, 157, 173);
            case MALE:
                return new Color(118, 166, 212);
            case FEMALE:
                return new Color(226, 191, 212);
            case UNKNOWN:
                return new Color(200, 200, 200);
            case ODD:
                return new Color(255, 255, 255);
            case EVEN:
                return new Color(237, 243, 254);
            case SUNDAY_FORE:
                return new Color(255, 0, 130);
            case SATURDAY_FORE:
                return new Color(0, 0, 255);
            case WEEKDAY_FORE:
                return new Color(20, 20, 70);
            case OUTOFMONTH_FORE:
                return new Color(125, 125, 125);
            case CALENDAR_BACK:
                return new Color(227, 250, 207);
            case TODAY_BACK:
                return new Color(255, 255, 0);
            case BIRTHDAY_BACK:
                return new Color(0, 255, 255);
            case PVT:
                return new Color(255, 192, 203);
            case EXAM_APPO:
                return new Color(255, 165, 0);
            case RP:
                return new Color(255, 140, 0);
            case TREATMENT:
                return new Color(255, 140, 0);
            case TEST:
                return new Color(255, 69, 0);
            case IMAGE:
                return new Color(119, 200, 211);
            case RED:
                return new Color(255, 43, 58);
            case ORANGE:
                return new Color(255, 148, 44);
            case YELLOW:
                return new Color(242, 207, 43);
            case GREEN:
                return new Color(139, 209, 40);
            case BLUE:
                return new Color(10, 140, 211);
            case PURPLE:
                return new Color(223, 61, 154);
            case GRAY:
                return new Color(130, 130, 130);
            case BLACK:
                return new Color(0, 0, 0);
            case CALENDAR_TITLE_BACK:
                return new Color(220, 220, 220);
            case CALENDAR_TITLE_FORE:
                return new Color(0, 0, 255);
            default:LogWriter.error("", "case default");
        }
        return Color.BLACK;
    }

    /**
     *
     * @return
     */
    public static boolean isKarteDataDump() {
        return true;
    }

    /**
     *
     * @return
     */
    public static boolean isStampDump() {
        return true;
    }

    /**
     *
     * @return
     */
    public static boolean isSqlDump() {
        return true;
    }

    /**
     *
     * @return
     */
    public static boolean isSynchronizePatientSearchView() {
        return false;
    }
}
