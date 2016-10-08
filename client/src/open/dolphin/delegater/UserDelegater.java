/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.util.List;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import open.dolphin.infomodel.UserModel;
import open.dolphin.project.DolphinPrincipal;
import open.dolphin.service.IUserService;

/**
 *
 * @author tomohiro
 */
public abstract class UserDelegater extends DelegaterErrorHandler {

    /**
     *
     * @return
     * @throws NamingException
     */
    protected abstract IUserService getService() throws NamingException;

    /**
     *
     * @return
     */
    public List<UserModel> getAllUser() {
        try {
            return getService().getAllUser();
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param pk
     * @return
     */
    public UserModel getUser(String pk) {
        try {
            return getService().getUser(pk);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param principal
     * @param password
     * @return
     * @throws LoginException
     */
    public abstract UserModel login(DolphinPrincipal principal, String password) throws LoginException;

    /**
     *
     * @param userModel
     * @return
     */
    public int putUser(UserModel userModel) {
        try {
            return getService().addUser(userModel);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param uid
     * @return
     */
    public int removeUser(String uid) {
        try {
            return getService().removeUser(uid);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param user
     * @return
     */
    public int updateFacility(UserModel user) {
        try {
            return getService().updateFacility(user);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param userModel
     * @return
     */
    public int updateUser(UserModel userModel) {
        try {
            return getService().updateUser(userModel);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }
}
