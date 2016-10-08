package open.dolphin.pvtclientserver;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.ClosedSelectorException;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import open.dolphin.client.IMainWindow;
import open.dolphin.log.LogWriter;

/**
 *
 */
public class PVTClientServer implements open.dolphin.server.IPVTServer, Runnable {

    /**
     *
     */
    public static final int EOT = 0x04;
    /**
     *
     */
    public static final int ACK = 0x06;
    /**
     *
     */
    public static final int NAK = 0x15;
    /**
     *
     */
    public static final String UTF8 = "UTF8";
    /**
     *
     */
    public static final String SJIS = "SHIFT_JIS";
    /**
     *
     */
    public static final String EUC = "EUC_JIS";
    private static final int DEFAULT_PORT = 5002;
    private int port = DEFAULT_PORT;
    private String encoding = UTF8;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService service;
    private IMainWindow context;
    private String name;

    /**
     * Creates new ClaimServer
     */
    public PVTClientServer() {
    }

    /**
     *
     * @return
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     *
     * @param port
     */
    @Override
    public void setPort(int port) {
        this.port = port;
    }

    /**
     *
     * @return
     */
    @Override
    public String getEncoding() {
        return encoding;
    }

    /**
     *
     * @param enc
     */
    @Override
    public void setEncoding(String enc) {
        encoding = enc;
    }

    /**
     *
     */
    private void setup() {
        try {
            selector = SelectorProvider.provider().openSelector();
            serverSocketChannel = SelectorProvider.provider().openServerSocketChannel();
            serverSocketChannel.configureBlocking(false);
            InetAddress wildcard = null;
            InetSocketAddress address = new InetSocketAddress(wildcard, port);
            serverSocketChannel.socket().bind(address);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new AcceptHandler());
        } catch (IOException e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * ソケットサーバを開始する。
     */
    public void startService() {
        try {
            setup();
            service = Executors.newSingleThreadExecutor();
            service.execute(this);
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * ソケットサーバを終了する。
     */
    public void stopService() {

        try {
            service.shutdownNow();
            serverSocketChannel.close();
            selector.close();
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * ソケットサーバを開始する。
     */
    @Override
    public void run() {
        try {
            while (selector.select() > 0) {
                Set keys = selector.selectedKeys();
                for (Iterator it = keys.iterator(); it.hasNext();) {
                    SelectionKey key = (SelectionKey) it.next();
                    it.remove();
                    IHandler handler = (IHandler) key.attachment();
                    handler.handle(key);
                }
            }
        } catch (ClosedSelectorException cse) {
            LogWriter.error(getClass(), cse);
        } catch (IOException e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @Override
    public IMainWindow getContext() {
        return context;
    }

    /**
     *
     * @param context
     */
    @Override
    public void setContext(IMainWindow context) {
        this.context = context;
    }

    /**
     * 
     */
    @Override
    public void start() {
        startService();
    }

    /**
     *
     */
    @Override
    public void stop() {
        stopService();
    }
}
