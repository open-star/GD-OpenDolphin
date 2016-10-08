/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.connections;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author
 */
public class HttpClient {

    private URI uri;
    private URLConnection connection;
    private Map headers;

    /**
     *
     */
    public HttpClient() {
    }

    /**
     *
     * @param uriString
     * @throws Exception
     */
    public void connect(String uriString) throws Exception {
        uri = new URI(uriString);
        connection = uri.toURL().openConnection();
        headers = connection.getHeaderFields();
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public InputStream getContent() throws IOException {
        return connection.getInputStream();
    }

    /**
     *
     * @return
     */
    public Map getHeaders() {
        return Collections.unmodifiableMap(headers);
    }
}
