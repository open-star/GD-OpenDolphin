package open.dolphin.helper.FixedLengthFormat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.List;

/**
 *
 * @author
 */
public class FileWriter extends Writer {

    /**
     *
     * @param path
     * @param encoding
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public FileWriter(String path, String encoding)
            throws UnsupportedEncodingException, IOException {
        super(path, encoding);
    }

    /**
     *
     * @param list
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void write(List<IFormat> list)
            throws UnsupportedEncodingException, IOException {
        FileOutputStream out = (FileOutputStream) getOutputStream();
        FileChannel fc = out.getChannel();
        FileLock lock = fc.lock();
        super.write(list);
        lock.release();
        fc.close();
    }
}
