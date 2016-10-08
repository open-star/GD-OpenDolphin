package open.dolphin.delegater.remote;

import open.dolphin.delegater.LaboDelegater;
import javax.naming.NamingException;
import open.dolphin.service.ILaboService;
import open.dolphin.service.remote.RemoteLaboService;

/**
 *
 * @author
 */
public class RemoteLaboDelegater extends LaboDelegater {

    private RemoteLaboService service;

    /**
     *
     */
    public RemoteLaboDelegater() {
        service = new RemoteLaboService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected ILaboService getService() throws NamingException {
        //     if (service == null) {
        //       service = new RemoteLaboService();
        //    }
        return service;
    }
}
