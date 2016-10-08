package open.dolphin.dao;

import java.sql.*;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;

/**
 * SqlDaoBean　MEMO:DAO
 * driver, database レベル
 * @author  Kazushi Minagawa
 * @author  masuda
 * @author  星野 雅昭
 */
public class SqlDaoBean extends DaoBean {
//@Add  2012/06/27 星野 雅昭
    private static final String DRIVER = "org.postgresql.Driver";
    private static final int PORT = 5432;
    private static final String DATABASE = "orca";
    private static final String USER = "orca";
    private static final String PASSWD = "";
//@End  2012/06/27 星野 雅昭
    String dataBase;            //データベース名
    String driver;              //ドライバー
    boolean trace = true;

    /** 
     * Creates a new instance of SqlDaoBean 
     */
    public SqlDaoBean() {
        //super(); //@Del 2012/06/27 星野 雅昭
//@Add  2012/06/27 星野 雅昭
        this.setDriver(DRIVER);
        //this.setHost(Project.getClaimAddress());
        //this.setHost(Project.getString(Project.CLAIM_ADDRESS)); //@Rep 2012/06/26 星野 雅昭
        this.setHost(GlobalVariables.getClaimAddress());
        this.setPort(PORT) ;
        this.setDatabase(DATABASE);
        this.setUser(USER);
        this.setPasswd(PASSWD);
        //this.setHospNum();    //@Rep 2012/06/26 星野 雅昭
        this.setHospNumFromOrca();
//@End  2012/06/27 星野 雅昭
    }

//masuda^   ORCA 4.6対応など
    protected static final String ORCA_DB_VER45 = "040500-1";
    protected static final String ORCA_DB_VER46 = "040600-1";
//@Add 2012/06/26 星野 雅昭
    protected static final String ORCA_DB_VER47 = "040700-1";
    protected static final String ORCA_DB_VER48 = "040800-1";
//@End 2012/06/26 星野 雅昭

    private static int hospNum;         // 医療機関識別番号
    private static String dbVersion;
    
    protected String getOrcaDbVersion() {
        return dbVersion;
    }

    //protected int getHospNum() {              //@Rep 2012/06/26 星野 雅昭
    protected int getHospNumFromOrca() {
        return hospNum;
    }
    
    // ORCAのデータベースバージョンとhospNumを取得する
    //protected void setHospNum {               //@Rep 2012/06/26 星野 雅昭
    protected void setHospNumFromOrca() {   

        if (dbVersion != null) { //既にDBバージョンを取得している場合は、何もせずに戻る
            return;
        }

        Connection con = null;
        Statement st = null;
        String sql;
        hospNum = 1;
        //String jmari = Project.getString(Project.JMARI_CODE); ////@Rep 2012/06/26 星野 雅昭
        String jmari = GlobalVariables.getJMARICode();

        StringBuilder sb = new StringBuilder();
        sb.append("select hospnum, kanritbl from tbl_syskanri where kanricd='1001' and kanritbl like '%");
        sb.append(jmari);
        sb.append("%'");
        sql = sb.toString();
        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                hospNum = rs.getInt(1);
            }
        } catch (Exception e) {
            processError(e);
            closeConnection(con);
            closeStatement(st);
        }

        sql = "select version from tbl_dbkanri where kanricd='ORCADB00'";
        try {
            con = getConnection();
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                dbVersion = rs.getString(1);
            }
        } catch (Exception e) {
            processError(e);
            closeConnection(con);
            closeStatement(st);
        }
    }
    //masuda$

    /**
     * jdbc driver のクラス名を返す
     * @return ドライバーのクラス名
     */
    public String getDriver() {
        return driver;
    }

    /**
     * jdbc driver のクラス名をセットして，使えるかどうかチェック
     * @param driver ドライバーのクラス名
     */
    public void setDriver(String driver) {

        this.driver = driver;
        try {
            Class.forName(driver);

        } catch (ClassNotFoundException cnfe) {
             LogWriter.error(getClass(), cnfe);
            // LogWriter.warn(getClass(), "Couldn't find the driver!");
            // LogWriter.warn(getClass(), "Let's print a stack trace, and exit.");
            // System.exit(1);
        }
    }

    /**
     * データベース名を返す
     * @return データベース名
     */
    public String getDatabase() {
        return dataBase;
    }

    /**
     *
     * @param base データベース名
     */
    public void setDatabase(String base) {
        dataBase = base;
    }

    /**
     * URLを作成する
     * @return URL = jdbc:postgresql://host:port/dataBase
     */
    protected String getURL() {
        StringBuffer buf = new StringBuffer();
        buf.append("jdbc:postgresql://");
        buf.append(host);
        buf.append(":");
        buf.append(port);
        buf.append("/");
        buf.append(dataBase);
        return buf.toString();
    }

    /**
     *
     * @return トレース
     */
    public boolean getTrace() {
        return trace;
    }

    /**
     *
     * @param b
     */
    public void setTrace(boolean b) {
        trace = b;
    }

    /**
     * Connectionオブジェクトを得る
     * @return Connectionオブジェクト
     * @throws Exception 接続エラーが発生した場合
     */
    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(getURL(), user, passwd);
    }

    /**
     *
     * @param s
     * @return　's'
     */
    public String addSingleQuote(String s) {
        StringBuilder buf = new StringBuilder();
        buf.append("'");
        buf.append(s);
        buf.append("'");
        return buf.toString();
    }

    /**
     * To make sql statement ('xxxx',)<br>
     * @param s
     * @return
     */
    public String addSingleQuoteComa(String s) {
        StringBuilder buf = new StringBuilder();
        buf.append("'");
        buf.append(s);
        buf.append("',");
        return buf.toString();
    }

    /**
     *
     * @param st
     */
    public void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                LogWriter.error(getClass(), e);
            }
        }
    }

    /**
     * 
     * @param con
     */
    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                LogWriter.error(getClass(), e);
            }
        }
    }
}
