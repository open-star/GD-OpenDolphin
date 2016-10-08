package open.dolphin.delegater.remote;

import open.dolphin.delegater.StampDelegater;
import javax.naming.NamingException;
import open.dolphin.service.IStampService;
import open.dolphin.service.remote.RemoteStampService;

/**
 *
 * @author
 */
public class RemoteStampDelegater extends StampDelegater {

    private RemoteStampService service;

    /**
     *
     */
    public RemoteStampDelegater() {
        service = new RemoteStampService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected IStampService getService() throws NamingException {
        return service;
    }
}
