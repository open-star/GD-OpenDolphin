package open.dolphin.dao;

import open.dolphin.log.LogWriter;

/**
 * <p>DaoBean　MEMO:DAO
 * <p>DAO(Data Access Object)の部品
 * <p>DBに対する操作を提供する
 * @author  Kazushi Minagawa、星野 雅昭
 */
public class DaoBean {
    public static final int TT_NONE = 10;
    public static final int TT_NO_ERROR = 0;
    public static final int TT_CONNECTION_ERROR = -1;
    public static final int TT_DATABASE_ERROR = -2;
    public static final int TT_UNKNOWN_ERROR = -3;
//
    protected String host;
    protected int port;
    protected String user;
    protected String passwd;
    protected int errorCode;
    protected String errorMessage;
//  protected Semaphore semaphore;

    /**
     * Creates a new instance of DaoBean
     */
    public DaoBean() {
        //   semaphore = new Semaphore(1, true);
    }

    /**
     *
     * @return ホスト名
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @param host ホスト名
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     *
     * @return ポート番号
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @param port ポート番号
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     *
     * @return ユーザー名
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user ユーザー名
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     *
     * @return パスワード
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     *
     * @param passwd　パスワード
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     *
     * @return エラーコード
     */
    public boolean isNoError() {
        return errorCode == TT_NO_ERROR ? true : false;
    }

    /**
     *
     * @return エラーコード
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     *
     * @param errorCode エラーコード
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     *
     * @return エラーメッセージ
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 
     * @param errorMessage エラーメッセージ
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 例外を解析しエラーコードとエラーメッセージを設定する。
     *
     * @param e 例外
     */
    protected void processError(Exception e) {
        StringBuilder sb = new StringBuilder();
        if (e instanceof org.postgresql.util.PSQLException) {
            LogWriter.error(DaoBean.class, "サーバに接続できません。ネットワーク環境をお確かめください。", e);
            setErrorCode(TT_CONNECTION_ERROR);
            sb.append("サーバに接続できません。ネットワーク環境をお確かめください。");
            setErrorMessage(sb.toString());
        } else if (e instanceof java.sql.SQLException) {
            LogWriter.error(DaoBean.class, "データベースアクセスに問題がありました。", e);
            setErrorCode(TT_DATABASE_ERROR);
            sb.append("データベースアクセスに問題がありました。" + System.getProperty("line.separator") + "もう一度実行してみてください。");
            setErrorMessage(sb.toString());
        } else {
            LogWriter.error(DaoBean.class, "不明なエラーです(DaoBean)", e);
            setErrorCode(TT_UNKNOWN_ERROR);
            sb.append("不明なエラーです。念のためしばらく間をおいて再度実行してみてください。(DaoBean)");
            setErrorMessage(sb.toString());
        }
    }

    /**
     * 例外の持つ情報を加える。
     * @param e 例外
     * @return 例外の原因と内容
     */
    protected String appenExceptionInfo(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("例外クラス: ");
        sb.append(e.getClass().getName());
        sb.append(System.getProperty("line.separator"));
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            sb.append("原因: ");
            sb.append(e.getCause().getMessage());
            sb.append(System.getProperty("line.separator"));
        }
        if (e.getMessage() != null) {
            sb.append("内容: ");
            sb.append(e.getMessage());
        }
        return sb.toString();
    }
}
