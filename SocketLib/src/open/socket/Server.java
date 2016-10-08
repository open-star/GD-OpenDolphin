/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author
 */
public class Server implements Runnable {

    public Adapter adapter;
    private Socket sock_ = null;

    /**
     * コンストラクタ
     * @param sock クライアントと接続済みのソケットを渡す。
     */
    public Server(Socket sock, Adapter<Object, Object> adapter) {
        this.sock_ = sock;
        this.adapter = adapter;
    }

    /**
     *
     */
    protected void finalize() {
        try {
            sock_.close();
        } catch (Exception e) {
        }
    }

    /**
     * 
     */
    public void run() {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            in = new ObjectInputStream(sock_.getInputStream());
            Object result = adapter.onResult(in.readObject());
            out = new ObjectOutputStream(sock_.getOutputStream());
            out.writeObject(result);
        } catch (Exception e) {
            adapter.onError(e);
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
    }
}
