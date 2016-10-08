/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.dolphinpeer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import open.dolphin.client.ModelSender;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.log.LogWriter;
import open.dolphin.utils.DebugDump;

/**
 *
 * @author
 */
public class PeerMessageReader {

    private final static int BUFFER_SIZE = 8192;
    private static final int EOT = 0x04;
    private static final int ACK = 0x06;
    private byte[] data;
    private int length;
    private String encode;

    /**
     *
     * @param encode
     */
    public PeerMessageReader(String encode) {
        data = new byte[BUFFER_SIZE * 10];
        this.encode = encode;
    }

    /**
     *
     * @param key
     * @throws ClosedChannelException
     * @throws IOException
     */
    public void handle(SelectionKey key) throws ClosedChannelException, IOException {

        if (key.isReadable()) {
            try {
                read(key);
            } catch (SQLException ex) {
                LogWriter.error(getClass(), ex);
            }
        }
    }

    /**
     *
     * @param key
     * @throws ClosedChannelException
     * @throws IOException
     * @throws SQLException
     */
    private void read(SelectionKey key) throws ClosedChannelException, IOException, SQLException {

        SocketChannel channel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.clear();

        int len = channel.read(buffer);

        if (len < 0) {

            channel.close();

            String pvtXml = new String(data, 0, length - 1, encode);// "UTF8"
            DebugDump.dumpToFile("receivePeer.log", pvtXml);
            BufferedReader r = new BufferedReader(new StringReader(pvtXml));
            PeerBuilder builder = new PeerBuilder();

            builder.parse(r);
            PatientVisitModel model = builder.getProduct();
            ModelSender.getInstance().offer(model);

        } else {

            buffer.flip();
            buffer.get(data, length, len);
            length += len;

            if (buffer.get(len - 1) == EOT) {
                ByteBuffer ackBuf2 = ByteBuffer.wrap(new byte[]{ACK});
                channel.write(ackBuf2);
            }
        }
    }
}
