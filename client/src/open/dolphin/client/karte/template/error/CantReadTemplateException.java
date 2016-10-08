/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.karte.template.error;

/**
 *
 * @author tomohiro
 */
public class CantReadTemplateException extends Exception {

    /**
     *
     */
    public CantReadTemplateException() {
        super();
    }

    /**
     *
     * @param message
     */
    public CantReadTemplateException(String message) {
        super(message);
    }

    /**
     *
     * @param ex
     */
    public CantReadTemplateException(Exception ex) {
        super(ex.getMessage(), ex);
    }
}
