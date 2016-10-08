/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ソケットの
 * @author
 */
public class Message {

    /**
     *
     * @param host
     * @param port
     * @param S
     * @return
     */
    public static Object Send(String host, int port, Object S) {
        Object result = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(host, port));
            try {
                out = new ObjectOutputStream(sock.getOutputStream());
                out.writeObject(S);

                in = new ObjectInputStream(sock.getInputStream());
                result = in.readObject();

            } catch (Exception e) {

            }
        } catch (IOException e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
            }
        }
        return result;
    }

    /**
     *
     * @param port
     * @param timeout
     * @param adapter
     */
    public static void Receive(int port, int timeout, final Adapter adapter) {
        try {
            // サーバソケットの作成
            ServerSocket svsock = new ServerSocket(port);
            svsock.setSoTimeout(timeout); // accept() のタイムアウトが必要なときに設定する。
            for (;;) {
                Socket sock = svsock.accept();
                Server server = new Server(sock, adapter);
                Thread thread = new Thread(server);
                thread.start();
            }
        } catch (IOException ex) {
        }
    }
}
