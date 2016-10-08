package open.dolphin.delegater.remote;

import open.dolphin.delegater.PVTDelegater;
import javax.naming.NamingException;
import open.dolphin.service.IPvtService;
import open.dolphin.service.remote.RemotePvtService;

/**
 * 
 * @author
 */
public class RemotePVTDelegater extends PVTDelegater {

    private RemotePvtService service;

    /**
     *
     */
    public RemotePVTDelegater() {
        service = new RemotePvtService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected IPvtService getService() throws NamingException {
        return service;
    }
}
