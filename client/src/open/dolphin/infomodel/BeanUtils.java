package open.dolphin.infomodel;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import open.dolphin.log.LogWriter;
import org.hibernate.lob.BlobImpl;

/**
 * シリアライザ
 *
 * @author
 */
public class BeanUtils {

    /**
     * BeanをXMLにシリアライズ
     * @param bean オブジェクト
     * @return シリアライズされたオブジェクト
     */
    public static String beanToXml(Object bean) {
        String result = null;
        try {
            result = new String(getXMLBytes(bean), "UTF-8");
        } catch (Exception e) {
            LogWriter.error("beanToXml", "", e);
        }
        return result;
    }

    /**
     * シリアライズされたXMLをBean
     * @param beanXml シリアライズされたオブジェクト
     * @return オブジェクト
     */
    public static Object xmlToBean(String beanXml) {
        Object result = null;
        try {
            byte[] bytes = beanXml.getBytes("UTF-8");
            XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new ByteArrayInputStream(bytes)));
            result = decoder.readObject();
        } catch (Exception e) {
            LogWriter.error("xmlToBean", "", e);
            result = null;
        }
        return result;
    }

    /**
     * XMLをByteArrayで得る
     * @param bean オブジェクト
     * @return シリアライズされたオブジェクト
     */
    public static byte[] getXMLBytes(Object bean) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(stream));
        try {
            encoder.writeObject(bean);
        } finally {
            encoder.close();
        }
        return stream.toByteArray();
    }

    /**
     * MEMO:getXMLBytesと同じ
     * @param bean オブジェクト
     * @return シリアライズされたオブジェクト
     */
    public static byte[] xmlEncode(Object bean) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(stream));
        try {
            encoder.writeObject(bean);
        } finally {
            encoder.close();
        }
        return stream.toByteArray();
    }

    /**
     * BeanをBlobで得る
     * @param bean オブジェクト
     * @return シリアライズされたオブジェクト
     */
    public static Blob getXMLBlob(Object bean) {
        return new BlobImpl(getXMLBytes(bean));
    }

    /**
     * MEMO:getXMLBlobと同じ
     * @param bean オブジェクト
     * @return シリアライズされたオブジェクト
     */
    public static Blob xmlEncodeBlob(Object bean) {
        return new BlobImpl(xmlEncode(bean));
    }

    /**
     *　XMLをオブジェクトに
     * @param bytes シリアライズされたオブジェクト
     * @return オブジェクト
     */
    public static Object xmlDecode(byte[] bytes) {
        XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new ByteArrayInputStream(bytes)));
        if (decoder != null) {
            return decoder.readObject();
        }
        return null;
    }
}
