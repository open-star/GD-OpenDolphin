/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.infomodel;

/**
 * 診療コード
 *
 * @author taka
 */
public class SinryoCode {

    // TODO ハードコード
    private static final String[] sinryoCodeRegExp = {"^\\d{9}$"};
    private static final String[] noFormatCommentRegExp = {"^810000001$", "^0082\\d{5}$", "^82\\d{7}$", "^0085\\d{5}$", "^85\\d{7}$"};
    private static final String[] prefixCommentRegExp = {"^0083\\d{5}$", "^83\\d{7}$"};
    private static final String[] complexCommentRegExp = {"^0084\\d{5}$", "^84\\d{7}$"};
    private static final String[] tonyoRegExp = {"^0010008\\d\\d$"};
    private static final String[] JIHI_HIKAZEI_REGEXP = {"^095\\d{6}$", "^950\\d{6}$"};
    private static final String[] JIHI_KAZEI_REGEXP = {"^096\\d{6}$", "^960\\d{6}$"};
    private static final String[] ZANRYO_HAIKI_REGEXP = {"^09930\\d{4}"};

    /**
     * コメント？
     * @param s
     * @return
     */
    public static boolean isComment(String s) {
        return s != null && (isNoFormatComment(s) || isPrefixComment(s) || isComplexComment(s));
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean isNoFormatComment(String s) {
        return s != null && isValid(s) && matches(s, noFormatCommentRegExp);
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean isPrefixComment(String s) {
        return s != null && isValid(s) && matches(s, prefixCommentRegExp);
    }

    /**
     * 複合コメント？
     * @param s
     * @return
     */
    public static boolean isComplexComment(String s) {
        return s != null && isValid(s) && matches(s, complexCommentRegExp);
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean isTonyo(String s) {
        return s != null && isValid(s) && matches(s, tonyoRegExp);
    }

    /**
     * 自費？
     * @param s
     * @return
     */
    public static boolean isJihi(String s) {
        return isValid(s) && (isJihiHikazei(s) || isJihiKazei(s));
    }

    /**
     * 自費非課税？
     * @param s
     * @return
     */
    public static boolean isJihiHikazei(String s) {
        return isValid(s) && matches(s, JIHI_HIKAZEI_REGEXP);
    }

    /**
     * 自費課税？
     * @param s
     * @return
     */
    public static boolean isJihiKazei(String s) {
        return isValid(s) && matches(s, JIHI_KAZEI_REGEXP);
    }

    /**
     * 全量廃棄？
     * @param s
     * @return
     */
    public static boolean isZanryoHaiki(String s) {
        return isValid(s) && matches(s, ZANRYO_HAIKI_REGEXP);
    }

    /**
     * 有効？
     * @param s
     * @return
     */
    public static boolean isValid(String s) {
        return s != null && matches(s, sinryoCodeRegExp);
    }

    /**
     *
     * @param s
     * @param formats
     * @return
     */
    public static boolean matches(String s, String[] formats) {
        for (String f : formats) {
            if (s.matches(f)) {
                return true;
            }
        }
        return false;
    }
}
