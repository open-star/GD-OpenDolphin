package open.dolphin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Utilities to handel String.
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public final class StringTool {

    private static final char[] komoji = {'ぁ', 'ぃ', 'ぅ', 'ぇ', 'ぉ', 'っ', 'ゃ', 'ゅ', 'ょ', 'ゎ', 'ァ', 'ィ', 'ゥ', 'ェ', 'ォ', 'ッ', 'ャ', 'ュ', 'ョ', 'ヮ'};
    private static final Character[] HIRAGANA = {new Character('あ'), new Character('ん')};
    private static final Character[] KATAKANA = {new Character('ア'), new Character('ン')};
    private static final Character[] ZENKAKU_UPPER = {new Character('Ａ'), new Character('Ｚ')};
    private static final Character[] ZENKAKU_LOWER = {new Character('ａ'), new Character('ｚ')};
    private static final Character[] HANKAKU_UPPER = {new Character('A'), new Character('Z')};
    private static final Character[] HANKAKU_LOWER = {new Character('a'), new Character('z')};

    /**
     * Creates new StringTool
     */
    public StringTool() {
    }

    /**
     *
     * @param S
     * @param min
     * @param max
     * @param pattern
     * @return
     */
    public static boolean patternCheck(String S, int min, int max, String pattern) {
        int length = S.length();
        if (length >= min || min == 0) {
            if (length <= max || max == 0) {
                if (pattern.isEmpty()) {
                    return true;
                } else {
                    return Pattern.matches(pattern, S);
                }
            }
        }
        return false;
    }

    /**
     *
     * @param line
     * @param delim
     * @return
     */
    public static Object[] tokenToArray(String line, String delim) {
        StringTokenizer st = new StringTokenizer(line, delim, true);
        List<String> list = new ArrayList<String>(10);
        int state = 0;
        String token;

        while (st.hasMoreTokens()) {

            token = st.nextToken();
            switch (state) {
                case 0:
                    // VALUE_STATE
                    if (token.equals(",")) {
                        token = null;
                    } else {
                        state = 1;
                    }
                    list.add(token);
                    break;

                case 1:
                    // DELIM_STATE
                    state = 0;
                    break;
            }
        }

        return list.toArray();
    }

    /**
     *
     * @param text
     * @return
     */
    public static String trimSpace(String text) {
        int start = 0;
        char[] chars = text.toCharArray();

        while (start < chars.length) {
            if (chars[start] != ' ' && chars[start] != '　') {
                break;
            }
            start++;
        }
        if (start == chars.length) {
            return null;
        }
        int end = chars.length - 1;
        while (end > start) {
            if (chars[end] != ' ' && chars[end] != '　') {
                break;
            }
            end--;
        }
        return text.substring(start, end + 1);
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean startsWithKatakana(String s) {
        return isKatakana(s.charAt(0));
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean startsWithHiragana(String s) {
        return isHiragana(s.charAt(0));
    }

    /**
     * 
     * @param c
     * @return
     */
    public static boolean isKatakana(char c) {
        // ア 12449 12353 半角
        // ン 12531
        // return ((int)c >= 12449) && ((int)c <= 12531) ? true : false;
        Character test = new Character(c);
        return (test.compareTo(KATAKANA[0]) >= 0 && test.compareTo(KATAKANA[1]) <= 0) ? true : false;
    }

    /**
     *
     * @param c
     * @return
     */
    public static boolean isHiragana(char c) {
        // あ 12354
        // ん 12435
        Character test = new Character(c);
        return (test.compareTo(HIRAGANA[0]) >= 0 && test.compareTo(HIRAGANA[1]) <= 0) ? true : false;
    }

    /**
     *
     * @param c
     * @return
     */
    private static char toKatakana(char c) {
        return isHiragana(c) ? (char) ((int) c + 96) : c;
    }

    /**
     *
     * @param s
     * @return
     */
    public static String hiraganaToKatakana(String s) {
        int len = s.length();
        char[] src = new char[len];
        s.getChars(0, s.length(), src, 0);

        char[] dst = new char[len];
        for (int i = 0; i < len; i++) {
            dst[i] = toKatakana(src[i]);
        }
        return new String(dst);
    }

    /**
     *
     * @param str
     * @return
     */
    public static boolean isAllDigit(String str) {
        boolean ret = true;
        int len = str.length();

        for (int i = 0; i < len; i++) {

            char c = str.charAt(i);

            if (!Character.isDigit(c)) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    /**
     *
     * @param str
     * @return
     */
    public static boolean isAllKana(String str) {
        boolean ret = true;
        int len = str.length();

        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (isKatakana(c) || isHiragana(c)) {
                continue;
            } else {
                ret = false;
                break;
            }
        }
        return ret;
    }

    /**
     * Convert to Zenkaku
     * @param s
     * @return
     */
    public static String toZenkaku(String s) {
        if (s != null) {
            for (int i = 0; i < komoji.length; i++) {
                // s = s.replace(komoji[i], ohomoji[i]);
                s = s.replace(komoji[i], '.');
            }
        }
        return s;
    }

    /**
     *
     * @param text
     * @param b
     * @return
     */
    public static String toKatakana(String text, boolean b) {
        if (b) {
            text = toZenkaku(text);
        }
        return hiraganaToKatakana(text);
    }

    /**
     *
     * @param c
     * @return
     */
    public static boolean isZenkakuUpper(char c) {
        Character test = new Character(c);
        return (test.compareTo(ZENKAKU_UPPER[0]) >= 0 && test.compareTo(ZENKAKU_UPPER[1]) <= 0) ? true : false;
    }

    /**
     *
     * @param c
     * @return
     */
    public static boolean isZenkakuLower(char c) {
        Character test = new Character(c);
        return (test.compareTo(ZENKAKU_LOWER[0]) >= 0 && test.compareTo(ZENKAKU_LOWER[1]) <= 0) ? true : false;
    }

    /**
     *
     * @param c
     * @return
     */
    public static boolean isHankakuUpper(char c) {
        Character test = new Character(c);
        return (test.compareTo(HANKAKU_UPPER[0]) >= 0 && test.compareTo(HANKAKU_UPPER[1]) <= 0) ? true : false;
    }

    /**
     *
     * @param c
     * @return
     */
    public static boolean isHankakuLower(char c) {
        Character test = new Character(c);
        return (test.compareTo(HANKAKU_LOWER[0]) >= 0 && test.compareTo(HANKAKU_LOWER[1]) <= 0) ? true : false;
    }

    /**
     *
     * @param s
     * @return
     */
    public static String toZenkakuUpperLower(String s) {
        int len = s.length();
        char[] src = new char[len];
        s.getChars(0, s.length(), src, 0);

        StringBuilder sb = new StringBuilder();
        for (char c : src) {
            if (isHankakuUpper(c) || isHankakuLower(c)) {
                sb.append((char) ((int) c + 65248));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param s
     * @return
     */
    public static boolean isEmptyString(String s) {

        if (s != null) {
            return (s.length() == 0);
        } else {
            return true;
        }
    }

    /**
     *
     * @param s
     * @return
     */
    public static String zenkakuNumToHankaku(String s) {
        char[] cs = s.toCharArray();

        for (int i = 0; i < cs.length; i++) {

            if (cs[i] >= '０' && cs[i] <= '９') {
                cs[i] = (char) (cs[i] - '０' + '0');
                continue;
            }

            switch (cs[i]) {
                case '　':
                    cs[i] = ' ';
                    break;
                case 'ｍ':
                    cs[i] = 'm';
                    break;
                case 'ｇ':
                    cs[i] = 'g';
                    break;
                case '．':
                    cs[i] = '.';
                    break;
                case '＋':
                    cs[i] = '+';
                    break;
                case 'ー':
                    cs[i] = '-';
                    break;
            }
        }
        return String.valueOf(cs);
    }
}
