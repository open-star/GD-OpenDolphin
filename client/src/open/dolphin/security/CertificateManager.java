/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.security;

import java.io.InputStream;
import open.dolphin.connections.HttpClient;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalVariables;
import org.jdesktop.application.ApplicationContext;

/**
 *
 * @author
 */
public class CertificateManager {

    /**
     * 
     * @param context
     */
    static public void createCertificateFile(ApplicationContext context) {
        if (!GlobalSettings.isTrial()) {
            if (GlobalVariables.getDbSSLState()) {
                String address = GlobalVariables.getDbAddress();
                if (address != null) {
                    if (!address.isEmpty()) {
                        try {
                            SecurityDirectory target = new SecurityDirectory(context);
                            if (!target.CertificateFileExists()) {
                                HttpClient client = new HttpClient();
                                client.connect("http://" + address + ":1026/cert");//ssl-cert-snakeoil.pem
                                target.makeSecurityDirectory();
                                InputStream content = client.getContent();
                                target.createCertificateFile(content);
                            }
                        } catch (Exception e) {
                              LogWriter.error("CertificateManager", "createCertificateFile");
                        }
                    }
                }
            }
        }
    }
}
