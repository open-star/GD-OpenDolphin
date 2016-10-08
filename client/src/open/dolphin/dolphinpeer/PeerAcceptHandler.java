/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.dolphinpeer;

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
public class PeerAcceptHandler {

    /**
     *
     */
    public PeerAcceptHandler() {
    }

    /**
     *
     * @param key
     * @throws ClosedChannelException
     * @throws IOException
     */
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

        PeerMessageReader handler = new PeerMessageReader(code);

        // PVTMessageReader handler = new PVTMessageReader("Shift_JIS");
        channel.register(key.selector(), SelectionKey.OP_READ, handler);
    }
}
