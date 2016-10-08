package open.dolphin.delegater.remote;

import open.dolphin.delegater.UserDelegater;
import javax.naming.NamingException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import open.dolphin.infomodel.UserModel;
import open.dolphin.project.DolphinPrincipal;
import open.dolphin.project.GlobalVariables;
import open.dolphin.service.IUserService;
import open.dolphin.service.remote.RemoteUserService;
import open.dolphin.security.LoginHandler;

/**
 *
 * @author
 */
public class RemoteUserDelegater extends UserDelegater {

    private RemoteUserService service;

    /**
     *
     */
    public RemoteUserDelegater() {
        service = new RemoteUserService();
    }

    /**
     *
     * @param principal
     * @param password
     * @return
     * @throws LoginException
     */
    @Override
    public UserModel login(DolphinPrincipal principal, String password) throws LoginException {
        String pk = principal.getFacilityId() + ":" + principal.getUserId();

        LoginHandler handler = new LoginHandler(pk, password);
        LoginContext lc = new LoginContext("openDolphin", handler);
        lc.login();

        GlobalVariables.setSubject(lc.getSubject());

        return getUser(pk);
    }

    /**
     *
     * @return
     * @throws NamingException
     */
    @Override
    protected IUserService getService() throws NamingException {
        //       if (service == null) {
        //            service = new RemoteUserService();
        //         }
        return service;
    }
}
