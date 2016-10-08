/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import javax.naming.NamingException;
import open.dolphin.infomodel.BeanUtils;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.ISetaService;

/**
 *
 * @author tomohiro
 */
public abstract class SetaDelegater extends DelegaterErrorHandler {

    /**
     *
     * @return
     * @throws NamingException
     */
    protected abstract ISetaService getService() throws NamingException;

    /**
     *
     * @param model
     * @return
     */
    public long saveOrUpdateLetter(LetterModel model) {
        try {
            model.setBeanBytes(BeanUtils.xmlEncode(model));
            return getService().saveOrUpdateLetter(model);
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
            dispatchError(getClass(), e, "");
        }
        return 0L;
    }
}
