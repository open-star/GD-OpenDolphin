/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.security;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 *
 * @author tomohiro
 */
public class LoginHandler implements CallbackHandler {

    private String userName;
    private String password;

    /**
     *
     * @param userName
     * @param password
     */
    public LoginHandler(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     *
     * @param callBacks
     * @throws IOException
     * @throws UnsupportedCallbackException
     */
    @Override
    public void handle(Callback[] callBacks) throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callBacks.length; i++) {
            Callback callBack = callBacks[i];
            if (callBack instanceof NameCallback) {
                NameCallback nameCallback = (NameCallback) callBack;
                nameCallback.setName(userName);
            } else if (callBack instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callBack;
                passwordCallback.setPassword(password.toCharArray());
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }
}
