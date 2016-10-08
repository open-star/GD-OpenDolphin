package open.dolphin.pvtclientserver;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;

/**
 *
 * @author
 */
public class AcceptHandler implements IHandler {

    /**
     * 
     */
    public AcceptHandler() {
    }

    /**
     *
     * @param key
     * @throws ClosedChannelException
     * @throws IOException
     */
    @Override
    public void handle(SelectionKey key) throws ClosedChannelException, IOException {
        String code = "";
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);

        switch (GlobalVariables.getCharacterCode()) {
            case 0:
                code = "UTF8";
                break;
            case 1:
                code = "Shift_JIS";
                break;
            case 2:
                code = "EUC-JP";
                break;
            default:LogWriter.error(getClass(), "case default");
        }

        PVTMessageReader handler = new PVTMessageReader(code);

        channel.register(key.selector(), SelectionKey.OP_READ, handler);
    }
}
