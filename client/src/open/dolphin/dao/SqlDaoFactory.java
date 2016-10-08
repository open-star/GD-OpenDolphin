package open.dolphin.dao;

import open.dolphin.log.LogWriter;
import open.dolphin.project.*;

/**
 * 【SqlDaoBean】オブジェクトのファクトリです。
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SqlDaoFactory {
    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private static final int POSTGRESQL_PORT = 5432;
    private static final int TRIAL_PORT = 10329;
    private static final String DB_ORCA = "orca";
    private static final String USER_ORCA = "orca";
    private static final String PASSWORD_ORCA = "";
    private static final String DB_DOLPHIN = "dolphin";
    private static final String USER_DOPHIN = "dolphin";
    private static final String PASSWORD_TRIAL = "UQ6nRcOa";

    private static final String DAO_MASTER = "dao.master";

    /**
     * Creates a new instance of DaoFactory
     */
    public SqlDaoFactory() {
    }

    /**
     * <p> Creates DataAccessObject
     * SqlDaoBean オブジェクトを返す
     * @param keyString
     * @return SqlDaoBean オブジェクト
     */
    public static SqlDaoBean create(String keyString) {
        SqlDaoBean dao = null;
        try {
            dao = new open.dolphin.dao.SqlMasterDao();

            dao.setDriver(POSTGRESQL_DRIVER);

            if (keyString.equals(DAO_MASTER)) {
                dao.setHost(GlobalVariables.getClaimAddress());

                if (GlobalSettings.isTrial()) {
                    dao.setPort(TRIAL_PORT);
                    dao.setUser(USER_DOPHIN);
                    dao.setPasswd(PASSWORD_TRIAL);
                } else {
                    dao.setPort(POSTGRESQL_PORT);
                    dao.setUser(USER_ORCA);
                    dao.setPasswd(PASSWORD_ORCA);
                }
                dao.setDatabase(DB_ORCA);
            } else {
                dao.setHost(GlobalVariables.getDbAddress());

                if (GlobalSettings.isTrial()) {
                    dao.setPort(TRIAL_PORT);
                } else {
                    dao.setPort(POSTGRESQL_PORT);
                }

                dao.setDatabase(DB_DOLPHIN);
                dao.setUser(USER_DOPHIN);
                dao.setPasswd("");
            }

        } catch (Exception e) {
            LogWriter.error(SqlDaoBean.class, e);
        }
        return dao;
    }
}
