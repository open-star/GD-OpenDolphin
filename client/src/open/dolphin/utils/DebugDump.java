/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import java.io.File;
import java.io.FileOutputStream;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalConstants;

/**
 *　デバッグユーティリティ
 * @author
 */
public class DebugDump {

    /**
     * 
     * @param filename　ファイル名
     * @param message ファイルに書き出す内容
     */
    static public void dumpToFile(String filename, String message) {
        FileOutputStream stream = null;
        try {
            try {
                File fileName = new File(GlobalConstants.getLogDirectory() + File.separator + filename);
                stream = new FileOutputStream(fileName);
                stream.write(message.getBytes());
            } finally {
                stream.close();
            }
        } catch (Exception ex) {
            LogWriter.error("DebugDump", "dumpToFile");
        }
    }
}
