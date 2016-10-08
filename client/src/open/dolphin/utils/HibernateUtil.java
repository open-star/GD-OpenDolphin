/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalVariables;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 *
 * @author tomohiro
 */
public class HibernateUtil {

    private static Configuration config = null;
    private static SessionFactory sessionFactory = null;
    private static String currentDbAddress;
    private static String currentDbPassword;
    private static boolean currentDbSSLState;

    /**
     *
     */
    private static void createSessionFactory() {
        try {
            currentDbAddress = GlobalVariables.getDbAddress();
            currentDbPassword = GlobalVariables.getDbPasswordWithoutHash();
            currentDbSSLState = GlobalVariables.getDbSSLState();

            String dbAddress = "";

            if (GlobalSettings.isTrial()) {
                dbAddress = "jdbc:postgresql://" + currentDbAddress + ":10329/dolphin";
            } else {
                dbAddress = "jdbc:postgresql://" + currentDbAddress + ":5432/dolphin";
            }

            if (currentDbSSLState) {
                dbAddress += "?ssl";
            }

            config = new Configuration().setProperty("hibernate.connection.url", dbAddress).setProperty("hibernate.connection.password", currentDbPassword);
            sessionFactory = config.configure().buildSessionFactory();
        } catch (Throwable ex) {
            LogWriter.error(HibernateUtil.class, "Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     *
     * @return
     */
    private static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            createSessionFactory();
        }
        return sessionFactory;
    }

    /**
     *
     * @return
     */
    private static boolean isStatusChange() {
        return !GlobalVariables.getDbAddress().equals(currentDbAddress)
                || !GlobalVariables.getDbPasswordWithoutHash().equals(currentDbPassword)
                || !(GlobalVariables.getDbSSLState() == currentDbSSLState);
    }

    /**
     *
     */
    public static void createSchema() {
        if (config == null) {
            createSessionFactory();
        }
        (new SchemaExport(config)).create(false, true);
    }

    /**
     *
     * @return
     */
    public static Session openSession() {
        if (isStatusChange()) {
            createSessionFactory();
        }
        return getSessionFactory().openSession();
    }

    /**
     *
     * @param session
     */
    public static void closeSession(Session session) {
        if (session == null || !session.isOpen()) {
            return;
        }
        session.close();
    }
}
