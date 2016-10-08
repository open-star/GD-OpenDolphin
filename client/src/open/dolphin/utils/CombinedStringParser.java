/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import java.util.ArrayList;

/**
 *  組文字列パーサ
 *
 *  "|element1|element2|element3|"のような文字列を扱う。
 *
 */
public class CombinedStringParser extends ArrayList<String> {

    private static final long serialVersionUID = 1L;
    private char delimmiter;

    /**
     *
     */
    public CombinedStringParser() {
        this.delimmiter = '.';
    }

    /**
     *
     * @param set_string
     */
    public CombinedStringParser(String set_string) {
        this.delimmiter = '.';
        parse(set_string);
    }

    /**
     *
     * @param delimmiter
     */
    public CombinedStringParser(char delimmiter) {
        this.delimmiter = delimmiter;
    }

    /**
     * 
     * @param delimmiter
     * @param set_string
     */
    public CombinedStringParser(char delimmiter, String set_string) {
        this.delimmiter = delimmiter;
        parse(set_string);
    }

    /**
     *
     * @param set_string
     */
    private void parse(String set_string) {

        StringBuilder element = new StringBuilder();

        for (char c : set_string.toCharArray()) {

            if (c == delimmiter) {
                add(element.toString());
                element = new StringBuilder();
            } else {
                element.append(c);
            }
        }

        add(element.toString());
    }

    /**
     *
     * @param number
     */
    public void limit(int number) {
        for (int index = this.size() - 1; index >= number; index--) {
            this.remove(index);
        }
    }

    /**
     *
     * @param index
     * @return
     */
    @Override
    public String get(int index) {
        return _get(index);
    }

    /**
     *
     * @param index
     * @param value
     * @return
     */
    @Override
    public String set(int index, String value) {
        return _set(index, value);
    }

    /**
     *
     * @param index
     * @return
     */
    private String _get(int index) {
        if (size() <= index) {
            return "";
        } else {
            return super.get(index);
        }
    }

    /**
     *
     * @param index
     * @param value
     * @return
     */
    private String _set(int index, String value) {
        if (size() <= index) {
            for (int _index = size(); index >= _index; _index++) {
                add("");
            }
        }
        return super.set(index, value);
    }

    /**
     *
     * @return
     */
    public String toPlainString() {
        StringBuilder result = new StringBuilder();
        for (String element : this) {
            result.append(element);
        }
        return result.toString();
    }

    /**
     *
     * @return
     */
    public String toCombinedString() {
        StringBuilder result = new StringBuilder();
        for (String element : this) {
            if (result.length() == 0) {
                result.append(element);
            } else {
                result.append(delimmiter);
                result.append(element);
            }
        }
        return result.toString();
    }
}
