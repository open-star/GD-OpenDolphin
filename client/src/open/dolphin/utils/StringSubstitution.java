/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.utils;

/**
 *
 * @author
 */
public class StringSubstitution {

    /**
     * 
     * @param s
     * @param from
     * @param to
     * @return
     */
    public static String Substitution(String s, String from, String to){
        return s.replaceAll(from, to);
    }
}
