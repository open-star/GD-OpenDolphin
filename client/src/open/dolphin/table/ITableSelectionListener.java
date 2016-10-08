/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.table;

/**
 *
 * @author kazm
 */
public interface ITableSelectionListener {
    
    /**
     * 
     * @param selected
     */
    public void rowSelectionChanged(Object[] selected);
    
    /**
     *
     * @param obj
     */
    public void rowDoubleClicked(Object obj);

}
