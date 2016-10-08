/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import open.dolphin.dao.SqlDaoBean;
import open.dolphin.infomodel.GenericAdapter;
import open.dolphin.log.LogWriter;

/**
 *
 * @author
 */
public class DoMasterDao extends SqlDaoBean {

    /**
     * @param srycd
     * @param connection
     * @param statement
     * @return
     */
    public String[] get_TBL_TENSU(String srycd, Connection connection, Statement statement) {
        final String[] result = new String[3];
        try {
            String sql = "select NAME,TANINAME,SRYKBN from tbl_tensu where SRYCD=" + addSingleQuote(srycd);
            executeQueryContinueas(sql, null, connection, statement, new GenericAdapter<ResultSet, Object>() {

                @Override
                public boolean onResult(ResultSet records, Object x) throws Exception {
                    result[0] = records.getString(1);
                    result[1] = records.getString(2);
                    result[2] = records.getString(3);
                    return false;
                }

                @Override
                public void onError(Exception ex) {
                }
            });

        } catch (Exception ex) {
             LogWriter.error(getClass(), ex);
        }
        return result;
    }

    /**
     * @param ptid
     * @return
     */
    public List<DoBodyObject> get_TBL_SRYACT(String ptid) {
        final List<DoBodyObject> result = new ArrayList<DoBodyObject>();
        try {
            final Connection connection = getConnection();
            final Statement statement = connection.createStatement();
            try {
                String sql = "select ptid,creymd,upymd,zainum,rennum,"
                        + "SRYCD1,SRYSURYO1,SRYKAISU1,"
                        + "SRYCD2,SRYSURYO2,SRYKAISU2,"
                        + "SRYCD3,SRYSURYO3,SRYKAISU3,"
                        + "SRYCD4,SRYSURYO4,SRYKAISU4,"
                        + "SRYCD5,SRYSURYO5,SRYKAISU5 from tbl_sryact where ptid=" + addSingleQuote(ptid);

                executeQuery(sql, null, new GenericAdapter<ResultSet, Object>() {

                    @Override
                    public boolean onResult(ResultSet records, Object x) throws Exception {
                        for (int index = 6; index <= 20; index += 3) {
                            String[] tensu = get_TBL_TENSU(records.getString(index), connection, statement);
                            if (tensu.length == 3) {
                                DoBodyObject record = new DoBodyObject(tensu[2], records.getString(3), records.getString(4), tensu[0], records.getDouble(index + 1), records.getInt(index + 2), tensu[1]);
                                result.add(record);
                            }
                        }
                        return true;
                    }

                    @Override
                    public void onError(Exception ex) {
                    }
                });
            } finally {
                closeStatement(statement);
                closeConnection(connection);
            }
        } catch (Exception ex) {
             LogWriter.error(getClass(), ex);
        }
        return result;
    }

    /**
     *
     * @param sql
     * @param userObject
     * @param adapter
     * @return
     */
    public Object executeQuery(String sql, Object userObject, GenericAdapter adapter) {
        try {
        //    semaphore.acquire();
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet results = null;
            try {
                results = statement.executeQuery(sql);
                while (results.next()) {
                    if (!adapter.onResult(results, userObject)) {
                        return userObject;
                    }
                }
            } finally {
                results.close();
                closeStatement(statement);
                closeConnection(connection);
            }
        } catch (Exception ex) {
            adapter.onError(ex);
   //     } finally {

      //      semaphore.release();
        }
        return userObject;
    }
    
    /**
     *
     * @param sql
     * @param userObject
     * @param connection
     * @param statement
     * @param adapter
     * @return
     */
    public Object executeQueryContinueas(String sql, Object userObject, Connection connection, Statement statement, GenericAdapter adapter) {
        try {
            ResultSet results = null;
            try {
                results = statement.executeQuery(sql);
                while (results.next()) {
                    if (!adapter.onResult(results, userObject)) {
                        return userObject;
                    }
                }
            } finally {
                results.close();
            }
        } catch (Exception ex) {
            adapter.onError(ex);
     //   } finally {
        }
        return userObject;
    }

}
