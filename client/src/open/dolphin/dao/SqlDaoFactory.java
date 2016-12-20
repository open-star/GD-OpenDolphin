package open.dolphin.dao;

import open.dolphin.log.LogWriter;
import open.dolphin.project.*;

/**
 * 【SqlDaoBean】オブジェクトのファクトリです。
 * orca、または dolphin のオブジェクトを生成する
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

    private static final String DAO_MASTER = "dao.master"; //ORCAのマスタ

    /**
     * Creates a new instance of DaoFactory
     */
    public SqlDaoFactory() {
    }

    /**
     * <p> Creates DataAccessObject
     * SqlDaoBean オブジェクトを生成するファクトリメソッド
     * orca、または dolphin のオブジェクトを生成する。
     * ただし、dolphin のテーブルは、Hibernate を使ってアクセスするので
     * dolphin のオブジェクトを生成するケースは無い。
     * @param keyString 生成するオブジェクトの種類（orca/dolphin）
     * @return SqlDaoBean オブジェクト（orca/dolphin）
     */
    public static SqlDaoBean create(String keyString) {
        SqlDaoBean dao = null;
        
        try {

            if (keyString.equals(DAO_MASTER)) {
                // ORCA の場合
                dao = new open.dolphin.dao.SqlMasterDao();

                dao.setDriver(POSTGRESQL_DRIVER);
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
            }
//            else {
//                //Dolphin の場合
//                dao = new open.dolphin.dao.SqlDolphinDao();
//                
//               dao.setDriver(POSTGRESQL_DRIVER);
//                dao.setHost(GlobalVariables.getDbAddress());
//
//                if (GlobalSettings.isTrial()) {
//                    dao.setPort(TRIAL_PORT);
//                } else {
//                    dao.setPort(POSTGRESQL_PORT);
//                }
//
//                dao.setDatabase(DB_DOLPHIN);
//                dao.setUser(USER_DOPHIN);
//                dao.setPasswd("");
//            }

        } catch (Exception e) {
            LogWriter.error(SqlDaoBean.class, e);
        }
        return dao;
    }
}
