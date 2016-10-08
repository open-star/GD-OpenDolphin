/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater.error;

/**
 *
 * @author tomohiro
 */
public class CantFindPatentExcepion extends Exception {

    /**
     *
     * @param ex
     */
    public CantFindPatentExcepion(Exception ex) {
        super(ex);
    }
}
