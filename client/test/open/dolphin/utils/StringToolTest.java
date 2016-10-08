/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomohiro
 */
public class StringToolTest {

    /**
     * Test of tokenToArray method, of class StringTool.
     */
    // @Test
    public void testTokenToArray() {
        System.out.println("tokenToArray");
        String line = "";
        String delim = "";
        Object[] expResult = null;
        Object[] result = StringTool.tokenToArray(line, delim);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of trimSpace method, of class StringTool.
     */
    @Test
    public void testTrimSpaceWithoutFullWidth() {
        System.out.println("trimSpace without fullwidth");
        String text = " 432ほげ  ";
        String expResult = "432ほげ";
        String result = StringTool.trimSpace(text);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testTrimSpaceWithFullWidth() {
        System.out.println("trimSpace with fullwidth");
        String text = "　 432ほげ 　 ";
        String expResult = "432ほげ";
        String result = StringTool.trimSpace(text);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testTrimSpaceWhenAllSpaces() {
        System.out.println("trimSpace when all spaces");
        String text = "　 　 ";
        String expResult = null;
        String result = StringTool.trimSpace(text);
        assertEquals(expResult, result);
    }

    /**
     * Test of startsWithKatakana method, of class StringTool.
     */
    @Test
    public void testStartsWithKatakana() {
        System.out.println("startsWithKatakana");
        String s = "カたかな";
        boolean expResult = true;
        boolean result = StringTool.startsWithKatakana(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of startsWithHiragana method, of class StringTool.
     */
    @Test
    public void testStartsWithHiragana() {
        System.out.println("startsWithHiragana");
        String s = "ひラガナ";
        boolean expResult = true;
        boolean result = StringTool.startsWithHiragana(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of isKatakana method, of class StringTool.
     */
    @Test
    public void testIsKatakanaOk() {
        System.out.println("isKatakana OK");
        char[] cs = {'ア', 'ン', 'ホ'};
        for (int i = 0; i < cs.length; i++) {
            assert (StringTool.isKatakana(cs[i]));
        }
    }

    /**
     *
     */
    @Test
    public void testIsKatakanaNg() {
        System.out.println("isKatakana NG");
        char[] cs = {'あ', 'ん', 'た'};
        for (int i = 0; i < cs.length; i++) {
            assert (StringTool.isKatakana(cs[i]));
        }
    }

    /**
     * Test of isHiragana method, of class StringTool.
     */
    @Test
    public void testIsHiraganaOk() {
        System.out.println("isHiragana Ok");
        char[] cs = {'あ', 'ん', 'た'};
        for (int i = 0; i < cs.length; i++) {
            assert (StringTool.isHiragana(cs[i]));
        }
    }

    /**
     * Test of hiraganaToKatakana method, of class StringTool.
     */
    @Test
    public void testHiraganaToKatakana() {
        System.out.println("hiraganaToKatakana NG");
        char[] cs = {'ア', 'ン', 'ホ'};
        for (int i = 0; i < cs.length; i++) {
            assert (StringTool.isKatakana(cs[i]));
        }
    }

    /**
     * Test of isAllDigit method, of class StringTool.
     */
    @Test
    public void testIsAllDigitOk() {
        System.out.println("isAllDigitOk");
        String str = "573482795231";
        assert (StringTool.isAllDigit(str));
    }

    /**
     *
     */
    @Test
    public void testIsAllDigitNg() {
        System.out.println("isAllDigitNg");
        String str = "573a82795231";
        assert (!StringTool.isAllDigit(str));
    }

    /**
     * Test of isAllKana method, of class StringTool.
     */
    // @Test
    public void testIsAllKana() {
        System.out.println("isAllKana");
        String str = "";
     //   boolean expResult = false;
        StringTool.isAllKana(str);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of toZenkaku method, of class StringTool.
     */
    // @Test
    public void testToZenkaku() {
        System.out.println("toZenkaku");
        String s = "";
  //      String expResult = "";
         StringTool.toZenkaku(s);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of toKatakana method, of class StringTool.
     */
    // @Test
    public void testToKatakana() {
        System.out.println("toKatakana");
        String text = "";
        boolean b = false;
    //    String expResult = "";
        StringTool.toKatakana(text, b);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of isZenkakuUpper method, of class StringTool.
     */
    // @Test
    public void testIsZenkakuUpper() {
        System.out.println("isZenkakuUpper");
        char c = ' ';
    //    boolean expResult = false;
        StringTool.isZenkakuUpper(c);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of isZenkakuLower method, of class StringTool.
     */
    // @Test
    public void testIsZenkakuLower() {
        System.out.println("isZenkakuLower");
        char c = ' ';
     //   boolean expResult = false;
       StringTool.isZenkakuLower(c);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of isHankakuUpper method, of class StringTool.
     */
    // @Test
    public void testIsHankakuUpper() {
        System.out.println("isHankakuUpper");
        char c = ' ';
  //      boolean expResult = false;
        StringTool.isHankakuUpper(c);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of isHankakuLower method, of class StringTool.
     */
    // @Test
    public void testIsHanakuLower() {
        System.out.println("isHanakuLower");
        char c = ' ';
   //     boolean expResult = false;
         StringTool.isHankakuLower(c);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of toZenkakuUpperLower method, of class StringTool.
     */
    // @Test
    public void testToZenkakuUpperLower() {
        System.out.println("toZenkakuUpperLower");
        String s = "";
      //  String expResult = "";
        StringTool.toZenkakuUpperLower(s);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case has not implemented yet.");
    }

    /**
     * Test of isEmptyString method, of class StringTool.
     */
    @Test
    public void testIsEmptyStringNull() {
        System.out.println("isEmptyString null");
        String s = null;
        boolean expResult = true;
        boolean result = StringTool.isEmptyString(s);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testIsEmptyStringNotNull() {
        System.out.println("isEmptyString not null");
        String s = "";
        boolean expResult = true;
        boolean result = StringTool.isEmptyString(s);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testIsEmptyStringNotEmpty() {
        System.out.println("isEmptyString not empty");
        String s = "     ";
        boolean expResult = false;
        boolean result = StringTool.isEmptyString(s);
        assertEquals(expResult, result);
    }

    /**
     * Test of zenkakuNumToHankaku method, of class StringTool.
     */
    @Test
    public void testZenkakuNumToHankaku() {
        System.out.println("zenkakuNumToHankaku");
        String s = "１２３４５６７８９０";
        String expResult = "1234567890";
        String result = StringTool.zenkakuNumToHankaku(s);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testZenkakuNumToHankakuWithSomeAlphabetic() {
        System.out.println("zenkakuNumToHankaku with some alphabetic");
        String s = "１２３４５６７８９０ｍｇ　．＋ー";
        String expResult = "1234567890mg .+-";
        String result = StringTool.zenkakuNumToHankaku(s);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testPatternCheck() {
        assertEquals(StringTool.patternCheck("aaaaa", 0, 0, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 5, "[A-Za-z0-9_+\\-.#$&@]*"), true);
        assertEquals(StringTool.patternCheck("aaaaa", 4, 5, "[A-Za-z0-9_+\\-.#$&@]*"), true);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 5, "[A-Za-z0-9_+\\-.#$&@]*"), true);
        assertEquals(StringTool.patternCheck("aaaaa", 6, 5, "[A-Za-z0-9_+\\-.#$&@]*"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 0, "[A-Za-z0-9_+\\-.#$&@]*"), true);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 4, "[A-Za-z0-9_+\\-.#$&@]*"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 5, "[A-Za-z0-9_+\\-.#$&@]*"), true);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 6, "[A-Za-z0-9_+\\-.#$&@]*"), true);
        assertEquals(StringTool.patternCheck("aaaaa", 4, 4, "[A-Za-z0-9_+\\-.#$&@]*"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 5, "[A-Za-z0-9_+\\-.#$&@]*"), true);
        assertEquals(StringTool.patternCheck("aaaaa", 6, 6, "[A-Za-z0-9_+\\-.#$&@]*"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 5, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 4, 5, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 5, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 6, 5, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 0, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 4, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 5, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 6, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 4, 4, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 5, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 6, 6, "\\p{Alnum}+@\\p{Alnum}+"), false);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 5, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 4, 5, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 5, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 6, 5, ""), false);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 0, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 4, ""), false);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 5, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 0, 6, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 4, 4, ""), false);
        assertEquals(StringTool.patternCheck("aaaaa", 5, 5, ""), true);
        assertEquals(StringTool.patternCheck("aaaaa", 6, 6, ""), false);
        assertEquals(StringTool.patternCheck("aaaaa", 4, 6, ""), true);
    }
}
