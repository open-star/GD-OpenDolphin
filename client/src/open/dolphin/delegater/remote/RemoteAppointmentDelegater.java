package open.dolphin.delegater.remote;

import open.dolphin.delegater.AppointmentDelegater;
import javax.naming.NamingException;
import open.dolphin.service.IAppointmentService;
import open.dolphin.service.remote.RemoteAppoService;

/**
 *
 * @author 
 */
public class RemoteAppointmentDelegater extends AppointmentDelegater {

    private RemoteAppoService service;

    /**
     *
     */
    public RemoteAppointmentDelegater() {
        service = new RemoteAppoService();
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected IAppointmentService getService() throws NamingException {
        //     if (service == null) {
        //       service = new RemoteAppoService();
        //   }
        return service;
    }
}
