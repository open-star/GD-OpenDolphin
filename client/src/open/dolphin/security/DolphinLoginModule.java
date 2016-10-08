/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import open.dolphin.utils.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author tomohiro
 */
public class DolphinLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map sharedState;
    private Map options;
    private boolean succeeded = false;
    private boolean commitSucceeded = false;
    private String username = null;
    private String password = null;
    private char[] credential;
    private SecurityPrincipal userPrincipal;
    private List<SecurityPrincipal> rolePrincipals = new ArrayList<SecurityPrincipal>();

    /**
     *
     *
     * @param subject
     * @param callbackHandler
     * @param sharedState
     * @param options
     */
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
    }

    /**
     *
     * @throws LoginException
     */
    private void getUserNamePassword() throws LoginException {
        NameCallback nameCallback = new NameCallback("Username: ");
        PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
        Callback[] callBacks = {nameCallback, passwordCallback};

        try {
            callbackHandler.handle(callBacks);
            username = nameCallback.getName();
            char[] passwordChars = passwordCallback.getPassword();
            if (passwordChars != null) {
                password = new String(passwordChars);
            }
            for (int i = 0; i < passwordChars.length; i++) {
                passwordChars[i] = '*';
            }
            passwordChars = null;
            passwordCallback.clearPassword();
        } catch (IOException exp) {
            LoginException loginException = new LoginException("Failed to get username/password.");
            loginException.initCause(exp);
            throw loginException;
        } catch (UnsupportedCallbackException exp) {
            LoginException loginException = new LoginException("Callback Handler does not support.");
            loginException.initCause(exp);
            throw loginException;
        }
        if (password == null) {
            throw new LoginException("Failed to get username/password.");
        }
    }

    /**
     *
     */
    private void createPasswordHash() {
        password = EncryptUtil.createPasswordHash(password);
    }

    /**
     *
     * @return
     * @throws LoginException
     */
    @Override
    public boolean login() throws LoginException {

        if (callbackHandler == null) {
            throw new LoginException("Error: No callback handler available.");
        }
        getUserNamePassword();
        createPasswordHash();
        String expectedPassword = "";
        Session session = HibernateUtil.openSession();
        try {
            expectedPassword = (String) session.createSQLQuery("select password from d_users where userid = :userId").setParameter("userId", username).uniqueResult();
            session.flush();
        } finally {
            HibernateUtil.closeSession(session);
        }
        //     session.close();
        succeeded = expectedPassword.equals(password);
        expectedPassword = null;
        return succeeded;
    }

    /**
     *
     * @return
     */
    private List<String> getRoles() {
        List<String> role = null;
        Session session = HibernateUtil.openSession();
        try {
            role = (List<String>) session.createSQLQuery("select c_role from d_roles where user_id = :userId").setParameter("userId", username).list();
            session.flush();
        } finally {
            HibernateUtil.closeSession(session);
        }
        //    session.close();
        return role;
    }

    /**
     *
     * @return
     * @throws LoginException
     */
    @Override
    public boolean commit() throws LoginException {
        if (succeeded == false) {
            return false;
        }

        userPrincipal = new SecurityPrincipal(username);

        if (!subject.getPrincipals().contains(userPrincipal)) {
            subject.getPrincipals().add(userPrincipal);
        }

        for (String role : getRoles()) {
            SecurityPrincipal rolePrincipal = new SecurityPrincipal(role);

            if (!subject.getPrincipals().contains(rolePrincipal)) {
                subject.getPrincipals().add(rolePrincipal);
            }
            rolePrincipals.add(rolePrincipal);
        }
        username = null;
        password = null;

        commitSucceeded = true;
        return true;
    }

    /**
     *
     * @return
     * @throws LoginException
     */
    @Override
    public boolean abort() throws LoginException {
        // throw new UnsupportedOperationException("Not supported yet.");
        if (succeeded == false) {
            return false;
        } else if (succeeded == true && commitSucceeded == false) {
            succeeded = false;
            username = null;
            if (password != null) {
                password = null;
            }
            userPrincipal = null;
        } else {
            logout();
        }
        return true;
    }

    /**
     *
     * @return
     * @throws LoginException
     */
    @Override
    public boolean logout() throws LoginException {

        subject.getPrincipals().remove(userPrincipal);
        for (SecurityPrincipal rolePrincipal : rolePrincipals) {
            subject.getPrincipals().remove(rolePrincipal);
        }

        succeeded = false;
        commitSucceeded = false;

        username = null;
        password = null;

        userPrincipal = null;
        rolePrincipals = null;

        return true;
    }
}
