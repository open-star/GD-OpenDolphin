/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.karte.template.error;

/**
 *
 * @author tomohiro
 */
public class CantWriteTemplateException extends Exception {

    /**
     *
     */
    public CantWriteTemplateException() {
        super();
    }

    /**
     *
     * @param message
     */
    public CantWriteTemplateException(String message) {
        super(message);
    }

    /**
     *
     * @param ex
     */
    public CantWriteTemplateException(Exception ex) {
        super(ex.getMessage(), ex);
    }
}
