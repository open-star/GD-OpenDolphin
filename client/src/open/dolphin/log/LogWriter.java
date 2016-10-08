/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.log;

import java.util.Properties;
import open.dolphin.utils.DebugDump;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author
 */
public class LogWriter {

    /**
     *
     * @param path　パス
     * @param level
     */
    public static void config(String path, String level) {
        Properties property = new Properties();
        property.setProperty("log4j.rootCategory", level + ",dolpinAppender");
        //ログの出力先：ファイル
        property.setProperty("log4j.appender.dolpinAppender", "org.apache.log4j.RollingFileAppender");
        property.setProperty("log4j.appender.dolpinAppender.File", path);
        property.setProperty("log4j.appender.dolpinAppender.MaxFileSize", "1MB");
        property.setProperty("log4j.appender.dolpinAppender.MaxBackupIndex", "20");
        //出力フォーマット：ユーザが指定したフォーマット
        property.setProperty("log4j.appender.dolpinAppender.layout", "org.apache.log4j.PatternLayout");
        property.setProperty("log4j.appender.dolpinAppender.layout.ConversionPattern", "%d %-4r [%t] %-5p %c - %m%n");
        PropertyConfigurator.configure(property);
    }

    /**
     *
     * @param clazz
     * @param text
     */
    public static void debug(Class clazz, String text) {
        Logger logger = Logger.getLogger(clazz);
        logger.log(Level.DEBUG, text);
    }

    /**
     *
     * @param clazz
     * @param text
     */
    public static void error(Class clazz, String text) {
        Logger.getLogger(clazz).log(Level.ERROR, text);
    }

    /**
     *
     * @param clazz
     * @param text
     * @param e
     */
    public static void error(Class clazz, String text, Throwable e) {
        dumpStackTraceToFile(e);
        Logger.getLogger(clazz).log(Level.ERROR, text, e);
    }

    /**
     *
     * @param clazz
     * @param e
     */
    public static void error(Class clazz, Throwable e) {
        dumpStackTraceToFile(e);
        Logger.getLogger(clazz).log(Level.ERROR, "exception ", e);
    }

    /**
     *
     * @param clazz
     * @param text
     */
    public static void fatal(Class clazz, String text) {
        Logger.getLogger(clazz).log(Level.FATAL, text);
    }

    /**
     *
     * @param clazz
     * @param text
     * @param e
     */
    public static void fatal(Class clazz, String text, Throwable e) {
        dumpStackTraceToFile(e);
        Logger.getLogger(clazz).log(Level.FATAL, text, e);
    }

    /**
     *
     * @param clazz
     * @param e
     */
    public static void fatal(Class clazz, Throwable e) {
        dumpStackTraceToFile(e);
        Logger.getLogger(clazz).log(Level.FATAL, "exception ", e);
    }

    /**
     *
     * @param clazz
     * @param text
     */
    public static void info(Class clazz, String text) {
        Logger.getLogger(clazz).log(Level.INFO, text);
    }

    /**
     *
     * @param clazz
     * @param text
     */
    public static void warn(Class clazz, String text) {
        Logger.getLogger(clazz).log(Level.WARN, text);
    }

    /**
     *
     * @param name
     * @param text
     */
    public static void debug(String name, String text) {
        Logger.getLogger(name).log(Level.DEBUG, text);
    }

    /**
     *
     * @param name
     * @param text
     */
    public static void error(String name, String text) {
        Logger.getLogger(name).log(Level.ERROR, text);
    }

    /**
     *
     * @param name
     * @param text
     * @param e
     */
    public static void error(String name, String text, Throwable e) {
        Logger.getLogger(name).log(Level.ERROR, text, e);
    }

    /**
     *
     * @param name
     * @param text
     */
    public static void fatal(String name, String text) {
        Logger.getLogger(name).log(Level.FATAL, text);
    }

    /**
     *
     * @param name
     * @param text
     * @param e
     */
    public static void fatal(String name, String text, Throwable e) {
        Logger.getLogger(name).log(Level.FATAL, text, e);
    }

    /**
     *
     * @param name
     * @param text
     */
    public static void info(String name, String text) {
        Logger.getLogger(name).log(Level.INFO, text);
    }

    /**
     *
     * @param name
     * @param text
     */
    public static void warn(String name, String text) {
        Logger.getLogger(name).log(Level.WARN, text);
    }

    /**
     *
     * @param e
     * @return
     */
    public static String stackTrace(Throwable e) {
        String message = "";
        for (StackTraceElement stackelement : e.getStackTrace()) {
            message = message + stackelement.getFileName() + " " + stackelement.getClassName() + " " + stackelement.getMethodName() + " " + Integer.toString(stackelement.getLineNumber()) + System.getProperty("line.separator");
        }
        return message;
    }

    /**
     *
     * @param e
     */
    public static void dumpStackTraceToFile(Throwable e) {
        DebugDump.dumpToFile("stackDump.log", stackTrace(e));
    }
}
