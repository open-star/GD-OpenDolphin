package open.dolphin.delegater.remote;

import open.dolphin.delegater.SetaDelegater;
import javax.naming.NamingException;
import open.dolphin.service.ISetaService;
import open.dolphin.service.remote.RemoteSetaService;

/**
 *
 * @author
 */
public class RemoteSetaDelegater extends SetaDelegater {

    private RemoteSetaService service;

    /**
     *
     */
    public RemoteSetaDelegater() {
        service = new RemoteSetaService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected ISetaService getService() throws NamingException {
        //    if (service == null) {
        //         service = new RemoteSetaService();
        //    }
        return service;
    }
}
