/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

import java.io.IOException;
import open.dolphin.project.GlobalConstants;
import open.dolphin.log.LogWriter;

/**
 *
 * @author taka
 */
public class FileExecutor {

    private static final String[] LINUX_PDF_VIEWERS = {
        "acroread",
        "evince",
        "epdfview",
        "xpdf",};

    /**
     *
     */
    public void FileExecutor() {
    }

    /**
     *
     * @param path
     */
    static public void execute(final String path) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    java.io.File target = new java.io.File(path);
                    if (target.exists()) {
                        if (GlobalConstants.isMac()) {
                            new ProcessBuilder("open", path).start();
                        } else if (GlobalConstants.isWin()) {
                            new ProcessBuilder("cmd.exe", "/c", path).start();
                        } else if (GlobalConstants.isLinux()) {
                            for (String command : LINUX_PDF_VIEWERS) {
                                try {
                                    new ProcessBuilder(command, path).start();
                                    break;
                                } catch (IOException ex) {
                                    continue;
                                }
                            }
                        }/* else {
                            // do nothing
                        }*/
                    }
                } catch (Exception e) {
                    LogWriter.error(getClass(), e);
                }
            }
        };
        java.awt.EventQueue.invokeLater(r);
    }
}
