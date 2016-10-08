/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.security;

import java.io.*;
import org.jdesktop.application.ApplicationContext;

/**
 *
 * @author
 */
public class SecurityDirectory {

    final static String SECURITY_DIRECTORY = "security";
    final static String SECURITY_CERTIFICATE_FILE = "/dolphin.trustStore";
    private File parentDirectory;
    private File securityDirectory;

    /**
     *
     * @param context
     */
    public SecurityDirectory(ApplicationContext context) {
        parentDirectory = context.getLocalStorage().getDirectory();
        securityDirectory = new File(parentDirectory, SECURITY_DIRECTORY);
    }

    /**
     *
     * @return
     */
    public boolean CertificateFileExists() {
        File file = new File(securityDirectory.getAbsolutePath() + SECURITY_CERTIFICATE_FILE);
        return file.exists();
    }

    /**
     *
     * @return
     */
    public File makeSecurityDirectory() {
        if (!securityDirectory.exists()) {
            if (securityDirectory.canWrite()) {
                securityDirectory.mkdir();
            }
        }
        return securityDirectory;
    }

    /**
     *
     * @param content
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void createCertificateFile(InputStream content) throws FileNotFoundException, IOException {
        OutputStream stream = new FileOutputStream(securityDirectory.getAbsolutePath() + SECURITY_CERTIFICATE_FILE);
        try {
            while (true) {
                int data = content.read();
                if (data == -1) {
                    break;
                }
                stream.write(data);
            }
        } finally {
            stream.close();
        }
    }
}
