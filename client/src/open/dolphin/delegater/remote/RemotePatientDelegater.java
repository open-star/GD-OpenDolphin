package open.dolphin.delegater.remote;

import open.dolphin.delegater.PatientDelegater;
import javax.naming.NamingException;
import open.dolphin.service.IPatientService;
import open.dolphin.service.remote.RemotePatientService;

/**
 *
 * @author
 */
public class RemotePatientDelegater extends PatientDelegater {

    private RemotePatientService service;

    /**
     *
     */
    public RemotePatientDelegater() {
        service = new RemotePatientService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected IPatientService getService() throws NamingException {
        //     if (service == null) {
        //         service = new RemotePatientService();
        //    }
        return service;
    }
}
