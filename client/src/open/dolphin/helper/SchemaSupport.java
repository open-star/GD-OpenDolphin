/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import open.dolphin.log.LogWriter;

/**
 * PlugInMenuSupport
 */
public class SchemaSupport extends PlugInMenuSupport {

    /**
     *
     */
    public SchemaSupport() {
        super();
    }

    /**
     *
     * @param source
     * @param dist
     * @throws Exception
     */
    private void copySchemas(File source, File dist) throws Exception {

        File[] sourceDirs = source.listFiles();
        for (int i = 0; i < sourceDirs.length; i++) {
            File sourceDir = sourceDirs[i];
            if (sourceDir.isDirectory()) {
                File distDir = new File(dist.getAbsolutePath() + File.separator + sourceDir.getName());
                distDir.mkdir();
                File[] sourceFiles = sourceDir.listFiles();
                for (int files_index = 0; files_index < sourceFiles.length; files_index++) {
                    File sourceFile = sourceFiles[files_index];
                    if (sourceFile.isFile()) {
                        FileInputStream sourceStream = null;
                        FileOutputStream distStream = null;
                        FileChannel sourceChannel = null;
                        FileChannel distChannel = null;
                        try {
                            sourceStream = new FileInputStream(sourceFile);
                            distStream = new FileOutputStream(dist.getAbsolutePath() + File.separator + sourceDir.getName() + File.separator + sourceFile.getName());
                            sourceChannel = sourceStream.getChannel();
                            distChannel = distStream.getChannel();
                            sourceChannel.transferTo(0, sourceChannel.size(), distChannel);
                        } catch (IOException ex) {
                            LogWriter.error(getClass(), ex);
                        } finally {
                            sourceStream.close();
                            distStream.close();
                            sourceChannel.close();
                            distChannel.close();
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param source
     * @param dist
     */
    private void moveSchemas(File source, File dist) {
        try {
            copySchemas(source, dist);
            //         deletePlugins(source);  //Move -> Copy
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
    }

    /**
     *
     * @param sourcePath
     * @param distPaht
     * @return
     */
    public boolean installSchemas(String sourcePath, String distPaht) {
        File sourceDir = new File(sourcePath);
        File distDir = new File(distPaht);
        if (sourceDir.exists()) {
            if (!distDir.exists()) {
                distDir.mkdir();
            }
            moveSchemas(sourceDir, distDir);
            return true;
        }
        return false;
    }
}
