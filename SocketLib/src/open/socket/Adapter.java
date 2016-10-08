/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.socket;

/**
 *
 * @author
 * NEW CODE
 */
public abstract class Adapter<InputT, OutputT> {

    public abstract OutputT onResult(InputT input) throws Exception;

    public abstract void onError(Exception ex);
}
