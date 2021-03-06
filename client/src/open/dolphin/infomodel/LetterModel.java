package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import java.sql.Blob;
import java.sql.SQLException;
import org.hibernate.lob.BlobImpl;

/**
 * 紹介状データ MEMO:d_letter
 * LetterModel generated by hbm2java
 */
public class LetterModel extends KarteEntryBean {

    private byte[] beanBytes;//MEMO:Refrection
    private Blob beanBlob;//MEMO:Refrection

    /**
     *
     */
    public LetterModel() {
    }

    /**
     *
     * @return
     */
    ////  private byte[] getBeanBytes() {
    //     return beanBytes;
    //  }

    /**
     * BeanBytesのSetter
     * MEMO:Refrection
     * @param beanBytes
     */
    public void setBeanBytes(byte[] beanBytes) {
        this.beanBytes = beanBytes;
        if (beanBytes == null) {
            this.beanBlob = null;
        } else {
            this.beanBlob = new BlobImpl(beanBytes);
        }
    }

    /**
     * beanBlobのGetter
     * MEMO:Refrection
     * @return beanBlob
     */
    public Blob getBeanBlob() {
        return this.beanBlob;
    }

    /**
     * beanBlobのSetter
     * MEMO:Refrection
     * @param beanBlob
     * @throws SQLException
     */
    public void setBeanBlob(Blob beanBlob) throws SQLException {
        if (beanBlob != null) {
            this.beanBlob = beanBlob;
            this.beanBytes = beanBlob.getBytes(1, (int) beanBlob.length());
        }
    }

    /**
     *
     * @return
     */
    public LetterModel toInfoModel() {
        return ((LetterModel) BeanUtils.xmlDecode(beanBytes));
    }
}
