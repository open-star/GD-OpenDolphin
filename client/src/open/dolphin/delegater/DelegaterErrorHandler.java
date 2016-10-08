package open.dolphin.delegater;

import java.lang.reflect.UndeclaredThrowableException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import open.dolphin.log.LogWriter;

/**
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public abstract class DelegaterErrorHandler {

    /**
     *
     */
    protected String error;
    /**
     *
     */
    protected boolean isError;

    /**
     *
     */
    public DelegaterErrorHandler() {
        error = "";
        isError = false;
    }

    /**
     *
     * @return
     */
    public boolean isError() {
        return isError;
    }

    /**
     *
     * @return
     */
    public String getErrorMessage() {
        return error;
    }

    /**
     *
     * @param clazz
     * @param exception
     * @param message
     */
    public void dispatchError(Class clazz, Exception exception, String message) {
        if (exception instanceof CommunicationException) {
            isError = true;
            LogWriter.error(clazz, "サーバに接続できません。ネットワーク環境をお確かめください。", exception);
            error = "サーバに接続できません。ネットワーク環境をお確かめください。";
        } else if (exception instanceof LoginException) {
            isError = true;
            LogWriter.error(clazz, "ログインできません。ユーザ名もしくはパスワードをお確かめください。クライアントの環境が実行を許可されない設定になっている可能性があります。", exception);
            error = "ログインできません。ユーザ名もしくはパスワードをお確かめください。";
        } else if (exception instanceof UndeclaredThrowableException) {
            isError = true;
            LogWriter.error(clazz, "処理を実行できません。クライアントのバージョンが古い可能性があります。", exception);
            error = "クライアントのバージョンが古い可能性があります";
        } else if (exception instanceof NamingException) {
            isError = true;
            LogWriter.error(clazz, "適切なデリゲータを見つけられませんでした", exception);
            error = "適切なデリゲータを見つけられませんでした";
        } else {
            isError = true;
            LogWriter.error(clazz, "不明なエラーです。念のためしばらく間をおいて再度実行してみてください。(BusinessDelegater)", exception);
            error = "不明なエラーです";
        }
    }
}
