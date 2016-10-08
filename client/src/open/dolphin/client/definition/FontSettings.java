/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.definition;

import java.awt.Font;

/**
 *
 * @author
 */
public class FontSettings {

    private static final int[] FONT_SIZE = {10, 12, 14, 16, 18, 24, 36};

    /**
     *
     * @return
     */
    public static int getKarteDefaultFontSize() {
        return FONT_SIZE[3];
    }

    /**
     * 
     * @param index
     * @return
     */
    public static int getKarteFontSize(int index) {
        return FONT_SIZE[index];
    }

    /**
     *
     * @return
     */
    public static Font getKarteDefaultFont() {
        return new Font("Dialog", Font.PLAIN, getKarteDefaultFontSize());
    }

    /**
     *
     * @param index
     * @return
     */
    public static Font getKarteFont(int index) {
        return new Font("Dialog", Font.PLAIN, getKarteFontSize(index));
    }
}