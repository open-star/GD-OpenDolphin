/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.utils;

/**
 *
 * @param <InputT> 
 * @param <OutputT>
 * @author
 */
public abstract class Adapter<InputT, OutputT> {

    /**
     * 
     * @param input
     * @return
     * @throws Exception
     */
    public abstract OutputT onResult(InputT input) throws Exception;

}
