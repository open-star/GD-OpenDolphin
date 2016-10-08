package open.dolphin.delegater.remote;

import open.dolphin.delegater.MasterDelegater;
import javax.naming.NamingException;
import open.dolphin.service.IMasterService;
import open.dolphin.service.remote.RemoteRadiologyMasterService;

/**
 *
 * @author
 */
public final class RemoteRadiologyMasterDelegater extends MasterDelegater {

    private RemoteRadiologyMasterService service;

    /**
     * 
     */
    public RemoteRadiologyMasterDelegater() {
        service = new RemoteRadiologyMasterService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected IMasterService getService() throws NamingException {
        //     if (service == null) {
        //          service = new RemoteRadiologyMasterService();
        //    }
        return service;
    }
}
