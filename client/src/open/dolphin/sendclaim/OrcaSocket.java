/*
 * Created on 2005/06/09
 *
 */
package open.dolphin.sendclaim;

import java.io.IOException;
import java.net.Socket;
import open.dolphin.log.LogWriter;

/**
 * @author Kazushi Minagawa Digital Globe, Inc.
 *
 */
public class OrcaSocket {

    private String host;
    private int port;
    private long period;
    private int tryCnt;

    /**
     *
     * @param host
     * @param port
     * @param period
     * @param tryCnt
     */
    public OrcaSocket(String host, int port, long period, int tryCnt) {
        this.host = host;
        this.port = port;
        this.period = period;
        this.tryCnt = tryCnt;
    }

    /**
     * 
     * @return ソケット
     */
    public Socket getSocket() {

        Socket socket = null;
        int cnt = 0;
        while (true) {
            try {
                socket = new Socket(host, port);
                break;
            } catch (IOException e) {
                cnt++;
                if ((cnt % tryCnt) != 0) {
                    sleep(period);
                } else {
                    break;
                }
            }
        }
        return socket;
    }

    /**
     *
     * @param msec
     */
    private void sleep(long msec) {

        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            LogWriter.error(getClass(), e);
        }
    }
}
