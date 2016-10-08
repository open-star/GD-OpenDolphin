/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.infomodel;

/**
 *
 * @param <InputT>
 * @param <OutputT>
 * @author
 */
public abstract class GenericAdapter<InputT, OutputT> {

    /**
     *
     * @param input
     * @param output
     * @return
     * @throws Exception
     */
    public abstract boolean onResult(InputT input, OutputT output) throws Exception;

    /**
     * 
     * @param ex
     */
    public abstract void onError(Exception ex);
}
