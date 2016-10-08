package open.dolphin.delegater.remote;

import javax.naming.NamingException;
import open.dolphin.delegater.DocumentDelegater;
import open.dolphin.service.IKarteService;
import open.dolphin.service.remote.RemoteKarteService;

/**
 * 
 * @author
 */
public class RemoteDocumentDelegater extends DocumentDelegater {

    private RemoteKarteService service;

    /**
     *
     */
    public RemoteDocumentDelegater() {
        service = new RemoteKarteService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected IKarteService getService() throws NamingException {
        //    if (service == null) {
        //        service = new RemoteKarteService();
        //   }
        return service;
    }
}
