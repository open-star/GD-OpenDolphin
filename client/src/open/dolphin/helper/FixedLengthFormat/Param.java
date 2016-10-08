/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper.FixedLengthFormat;

/**
 *
 * @author taka
 */
public class Param {

    /**
     * 
     */
    protected int size;
    /**
     *
     */
    protected String method;
    private String value;

    /**
     *
     * @param size　サイズ
     */
    public Param(int size) {
        this.size = size;
    }

    /**
     *
     * @param size
     * @param method
     */
    public Param(int size, String method) {
        this.size = size;
        this.method = method;
    }

    /**
     *
     * @return
     */
    public String getMethod() {
        return method;
    }

    /**
     *
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size サイズ
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     *
     * @return　サイズ
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value　値
     */
    public void setValue(String value) {
        this.value = value;
    }
}
