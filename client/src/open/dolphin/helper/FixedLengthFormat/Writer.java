package open.dolphin.helper.FixedLengthFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author
 */
public class Writer {

    private static final byte DEFAULT_PADDING = 0x20;
    private static final String DEFAULT_SEPARATOR = System.getProperty("line.separator");
    private byte padding;
    private String separator;
    private OutputStream out;
    private String encoding;

    /**
     *
     * @param st
     * @param encoding
     * @throws UnsupportedEncodingException
     */
    public Writer(OutputStream st, String encoding) throws UnsupportedEncodingException {
        init(st, encoding);
    }

    /**
     *
     * @param path
     * @param encoding
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public Writer(String path, String encoding)
            throws UnsupportedEncodingException, IOException {
        File file = new File(path);
        file.createNewFile();
        init(new FileOutputStream(file, true), encoding);
    }

    /**
     *
     * @param st
     * @param encoding
     * @throws UnsupportedEncodingException
     */
    private void init(OutputStream st, String encoding)
            throws UnsupportedEncodingException {
        this.out = st;
        this.encoding = encoding;
        this.padding = DEFAULT_PADDING;
        this.separator = DEFAULT_SEPARATOR;
    }

    /**
     *
     * @return
     */
    protected OutputStream getOutputStream() {
        return out;
    }

    /**
     *
     * @param format
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private void writeInternal(IFormat format)
            throws UnsupportedEncodingException, IOException {
        List<Param> params = format.getParam();
        for (Param p : params) {
            byte[] bytes = makeBytes(p);
            out.write(bytes);
        }
        out.write(separator.getBytes(encoding));
    }

    /**
     *
     * @param format
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void write(IFormat format)
            throws UnsupportedEncodingException, IOException {
        writeInternal(format);
        out.flush();
    }

    /**
     *
     * @param list
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public void write(List<IFormat> list)
            throws UnsupportedEncodingException, IOException {
        for (IFormat format : list) {
            writeInternal(format);
        }
        out.flush();
    }

    /**
     *
     * @throws IOException
     */
    public void close() throws IOException {
        out.close();
    }

    /**
     *
     * @param p
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    private byte[] makeBytes(Param p)
            throws UnsupportedEncodingException, IOException {
        int size = p.getSize();
        byte[] ret = new byte[size];
        String value = p.getValue();
        int i = 0;
        if (value != null) {
            byte[] val = value.getBytes(encoding);
            if (val.length > size) {
                throw new IOException("value exceeds of size " + size + ": " + value);
            }
            for (; i < val.length; i++) {
                ret[i] = val[i];
            }
        }
        for (; i < ret.length; i++) {
            ret[i] = padding;
        }
        return ret;
    }
}
