package open.dolphin.sendclaim;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JOptionPane;
import open.dolphin.client.ClaimMessageEvent;
import open.dolphin.client.IMainService;
import open.dolphin.client.IMainWindow;
import open.dolphin.log.LogWriter;

import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalVariables;

import open.dolphin.utils.DebugDump;
import open.dolphin.utils.StringSubstitution;

/**
 * SendClaimPlugin
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SendClaimImpl implements IMainService {//implements IClaimMessageListener {

    // Socket constants
    private final int EOT = 0x04;
    private final int ACK = 0x06;
    private final int NAK = 0x15;
    private final int DEFAULT_TRY_COUNT = 3;		// Socket 接続を試みる回数
    private final long DEFAULT_SLEEP_TIME = 20 * 1000L; 	// Socket 接続が得られなかった場合に次のトライまで待つ時間 msec
    private final int TT_SENDING_TROUBLE = 2;
    private final int TT_CONNECTION_REJECT = 3;
    // Strings
    private final String proceedString = "継続";
    private final String dumpString = "ログへ記録";
    // Properties
    private LinkedBlockingQueue queue;
    private String host;
    private int port;
    private String enc;
    private int tryCount = DEFAULT_TRY_COUNT;
    private long sleepTime = DEFAULT_SLEEP_TIME;
    //   private int alertQueueSize = MAX_QUEU_SIZE;
    private ExecutorService sendService;
    private IMainWindow context;
    private String name;

    /**
     * Creates new ClaimQue 
     */
    public SendClaimImpl() {
        queue = new LinkedBlockingQueue();
        setHost(GlobalVariables.getClaimAddress());
        setPort(GlobalVariables.getClaimPort());
        setEncoding(GlobalVariables.getClaimEncoding());
    }

    /**
     *
     * @return 名前
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
     * @return コンテキスト
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

    //   private void setup() {
//        setHost(GlobalVariables.getClaimAddress());
    //       setPort(GlobalVariables.getClaimPort());
    //      setEncoding(GlobalVariables.getClaimEncoding());
    //     if (queue == null) {
    //          queue = new LinkedBlockingQueue();
    //     }
    //  }
    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {
        //    setup();
        OrcaSocket orcaSocket = new OrcaSocket(getHost(), getPort(), sleepTime, tryCount);
        sendService = Executors.newSingleThreadExecutor();
        sendService.execute(new Consumer(orcaSocket));
    }

    /**
     * プログラムを終了する。
     */
    @Override
    public void stop() {

        try {
            if (sendService != null) {
                sendService.shutdownNow();
            }
            logDump();
        } catch (Exception e) {
              LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     * @return ホスト名
     */
    private String getHost() {
        return host;
    }

    /**
     *
     * @param host
     */
    private void setHost(String host) {
        this.host = host;
    }

    /**
     *
     * @return　ポート番号
     */
    private int getPort() {
        return port;
    }

    /**
     *
     * @param port
     */
    private void setPort(int port) {
        this.port = port;
    }

    /**
     *
     * @param enc
     */
    private void setEncoding(String enc) {
        this.enc = enc;
    }

    /**
     * カルテで CLAIM データが生成されるとこの通知を受ける。
     * @param e
     */
    public void claimMessageEvent(ClaimMessageEvent e) {
        queue.offer(e);
    }

    /**
     * Queue から取り出す。
     */
    private Object getCLAIM() throws InterruptedException {
        return queue.take();
    }

    /**
     * Queue内の CLAIM message をログへ出力する。
     */
    private void logDump() {

        Iterator iter = queue.iterator();
        while (iter.hasNext()) {
            ClaimMessageEvent evt = (ClaimMessageEvent) iter.next();// MEMO;Unused?
            //         LogWriter.warn(SendClaimImpl.class, evt.getClaimInsutance());
        }
        queue.clear();
    }

    /**
     *
     * @param code
     * @return オプション
     */
    private int alertDialog(int code) {

        int option = -1;
        String title = "OpenDolphin: CLAIM 送信";
        StringBuffer buf = null;

        switch (code) {

            case TT_SENDING_TROUBLE:
                buf = new StringBuffer();
                buf.append("CLAIM(レセプト)データの送信中にエラーがおきました。");
                buf.append(System.getProperty("line.separator"));
                buf.append("送信中のデータはログに記録します。診療報酬の自動入力はできません。");
                JOptionPane.showMessageDialog(null, buf.toString(), title, JOptionPane.ERROR_MESSAGE);
                break;

            case TT_CONNECTION_REJECT:
                buf = new StringBuffer();
                buf.append("CLAIM(レセプト)サーバ ");
                buf.append("Host=");
                buf.append(host);
                buf.append(" Port=");
                buf.append(port);
                buf.append(" が ");
                buf.append(tryCount * sleepTime);
                buf.append(" ミリ秒以上応答しません。サーバの電源及び接続を確認してください。");
                buf.append(System.getProperty("line.separator"));
                buf.append("1. このまま接続を待つこともできます。");
                buf.append(System.getProperty("line.separator"));
                buf.append("2. データをログに記録することもできます。");
                buf.append(System.getProperty("line.separator"));
                buf.append("   この場合、データは送信されず、診療報酬は手入力となります。");

                option = JOptionPane.showOptionDialog(null, buf.toString(), title, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{proceedString, dumpString}, proceedString);
                break;
        }
        return option;
    }

    /**
     * MEMO: unused?
     * @param result
     * @param evt
     * @return
     */
    private String getBasicInfo(String result, ClaimMessageEvent evt) {

        String id = evt.getPatientId();
        String nm = evt.getPatientName();
        String sex = evt.getPatientSex();
        String title = evt.getTitle();
        String timeStamp = evt.getConfirmDate();

        StringBuilder buf = new StringBuilder();
        buf.append(result);
        buf.append("[");
        buf.append(id);
        buf.append(" ");
        buf.append(nm);
        buf.append(" ");
        buf.append(sex);
        buf.append(" ");
        buf.append(title);
        buf.append(" ");
        buf.append(timeStamp);
        buf.append("]");

        return buf.toString();
    }

    /**
     * CLAIM 送信スレッド。
     */
    protected class Consumer implements Runnable {

        private OrcaSocket orcaSocket;

        /**
         * 
         * @param orcaSocket
         */
        public Consumer(OrcaSocket orcaSocket) {
            this.orcaSocket = orcaSocket;
        }

        /**
         *
         */
        @Override
        public void run() {
            ClaimMessageEvent claimEvent = null;
            Socket socket = null;
            BufferedOutputStream writer = null;
            BufferedInputStream reader = null;
            String instance = null;
            while (true) {
                try {
                    // CLAIM Event を取得
                    claimEvent = (ClaimMessageEvent) getCLAIM();
                    //ＯＲＣＡは"－"を表示できないため。
                    instance = StringSubstitution.Substitution(claimEvent.getClaimInsutance(), "－", "―");
                    if (GlobalSettings.isKarteDataDump()) {
                        DebugDump.dumpToFile("lastclaim.log", instance);
                    }

                    // Gets connection
                    socket = orcaSocket.getSocket();
                    try {
                        if (socket == null) {
                            int option = alertDialog(TT_CONNECTION_REJECT);
                            if (option == 1) {
                                continue;
                            } else {
                                claimMessageEvent(claimEvent);
                                continue;
                            }
                        }

                        // Gets io stream
                        writer = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));
                        reader = new BufferedInputStream(new DataInputStream(socket.getInputStream()));

                        // Writes UTF8 data

                        writer.write(instance.getBytes(enc));
                        writer.write(EOT);
                        writer.flush();

                        // Reads result
                        int c = reader.read();// MEMO;Unused?
                   //     if (c == ACK) {
                   //    } else if (c == NAK) {
                    //    }
                    } finally {
                        socket.close();
                    }

                } catch (IOException e) {
                    int option = alertDialog(TT_SENDING_TROUBLE);
               //     if (instance != null) {
               //     }
                } catch (Exception e) {
                    break;
                }
            }
        }
    }
}
