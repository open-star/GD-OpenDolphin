package open.dolphin.pvtclientserver;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;

/**
 *
 * @author 
 */
public interface IHandler {
    /**
     * 
     * @param key
     * @throws ClosedChannelException
     * @throws IOException
     */
    public void handle(SelectionKey key) throws ClosedChannelException, IOException;
}