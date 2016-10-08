/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.dolphinpeer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import open.dolphin.client.IMainService;
import open.dolphin.client.IMainWindow;
import open.dolphin.client.MmlMessageEvent;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;

/**
 * MML 送信サービス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class DolphinPeerClient implements IMainService {

    // CSGW への書き込みパス
    private String csgwPath;
    // MML Encoding
    private String encoding;
    // Work Queue
    private LinkedBlockingQueue queue;
    private Kicker kicker;
    private Thread sendThread;
    private IMainWindow context;
    private String name;

    /** Creates new SendMmlService */
    public DolphinPeerClient() {
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
     * @return
     */
    public String getCSGWPath() {
        return csgwPath;
    }

    /**
     *
     * @param val
     */
    public void setCSGWPath(String val) {
        csgwPath = val;
        File directory = new File(csgwPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
            } //else {
          //  }
        }
    }

    /**
     *
     */
    @Override
    public void stop() {
        try {
            Thread moribund = sendThread;
            sendThread = null;
            moribund.interrupt();
            logDump();
            //         LogWriter.info(getClass(), "Peer Client stopped");

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     */
    @Override
    public void start() {

        // CSGW 書き込みパスを設定する
        setCSGWPath(GlobalVariables.getCSGWPath());
        encoding = GlobalVariables.getMMLEncoding();

        // 送信キューを生成する
        queue = new LinkedBlockingQueue();
        kicker = new Kicker();
        sendThread = new Thread(kicker);
        sendThread.start();
    }

    /**
     *
     * @param e
     */
    public void mmlMessageEvent(MmlMessageEvent e) {
        queue.offer(e);
    }

    /**
     *
     * @return
     * @throws InterruptedException
     */
    public Object getMML() throws InterruptedException {
        return queue.take();
    }

    /**
     *
     */
    public void logDump() {

        synchronized (queue) {

            int size = queue.size();

            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    try {
                        MmlMessageEvent evt = (MmlMessageEvent) queue.take();// MEMO;Unused?
                    } catch (Exception e) {
                        LogWriter.error(getClass(), e);
                    }
                }
            }
        }
    }

    /**
     *
     * @param fileName
     * @param ext
     * @return
     */
    protected String getCSGWPathname(String fileName, String ext) {
        StringBuffer buf = new StringBuffer();
        buf.append(csgwPath);
        buf.append(File.separator);
        buf.append(fileName);
        buf.append(".");
        buf.append(ext);
        return buf.toString();
    }

    /**
     *
     */
    protected class Kicker implements Runnable {

        /**
         *
         */
        @Override
        public void run() {


            Thread thisThread = Thread.currentThread();
            BufferedOutputStream writer = null;

            while (thisThread == sendThread) {

                try {
                    // MML パッケージを取得
                    MmlMessageEvent mevt = (MmlMessageEvent) getMML();
                    String groupId = mevt.getGroupId();
                    String instance = mevt.getMmlInstance();
                    List<SchemaModel> schemas = mevt.getSchema();

                    // ファイル名を生成する
                    String dest = getCSGWPathname(groupId, "xml");
                    String temp = getCSGWPathname(groupId, "xml.tmp");
                    File f = new File(temp);

                    // インスタンスをUTF8で書き込む
                    writer = new BufferedOutputStream(new FileOutputStream(f));
                    byte[] bytes = instance.getBytes(encoding);
                    writer.write(bytes);
                    writer.flush();
                    writer.close();

                    // 書き込み終了後にリネームする (.tmp -> .xml)
                    f.renameTo(new File(dest));

                    // 画像を送信する
                    if (schemas != null) {
                        for (SchemaModel schema : schemas) {
                            dest = csgwPath + File.separator + schema.getExtRef().getHref();
                            temp = dest + ".tmp";
                            f = new File(temp);
                            writer = new BufferedOutputStream(new FileOutputStream(f));
                            writer.write(schema.getJpegBytes());
                            writer.flush();
                            writer.close();

                            // Renameする
                            f.renameTo(new File(dest));
                        }
                    }

                } catch (IOException e) {
                    LogWriter.error(getClass(), e);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
    }
}
