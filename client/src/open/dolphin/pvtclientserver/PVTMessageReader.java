package open.dolphin.pvtclientserver;

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
public class PVTMessageReader implements IHandler {

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
    public PVTMessageReader(String encode) {
        data = new byte[BUFFER_SIZE * 10];
        this.encode = encode;
    }

    /**
     * 
     * @param key
     * @throws ClosedChannelException
     * @throws IOException
     */
    @Override
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

            String pvtXml = new String(data, 0, length - 1, encode);// encode = "UTF8"
            DebugDump.dumpToFile("receivePvt.log", pvtXml);

            BufferedReader patientInfo = new BufferedReader(new StringReader(pvtXml));
            PVTBuilder builder = new PVTBuilder();
            builder.parse(patientInfo);
            PatientVisitModel model = builder.getProduct();

            LogWriter.info(getClass(), model.getPatientName() + "さんの来院情報を Queue へ登録しました");
            ModelSender.getInstance().offer(model);

        } else {

            buffer.flip();
            buffer.get(data, length, len);
            length += len;

            if (buffer.get(len - 1) == EOT) {
                LogWriter.debug(getClass(), "Received EOT");
                ByteBuffer ackBuf2 = ByteBuffer.wrap(new byte[]{ACK});
                channel.write(ackBuf2);
                LogWriter.debug(getClass(), "ACK を返ししました");
            }
        }
    }
}
