package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import java.io.IOException;
import java.io.Writer;
import java.sql.Blob;
import java.sql.SQLException;
import javax.persistence.Transient;
import javax.swing.ImageIcon;
import org.hibernate.lob.BlobImpl;

/**
 * シェーマ MEMO:マッピング d_image
 * SchemaModel generated by hbm2java
 */
public class SchemaModel extends KarteEntryBean {

    private DocumentModel document;//document MEMO:Refrection
    private transient Blob jpegBlob;//jpegBlob MEMO:Refrection
    private byte[] jpegBytes;
    private ExtRefModel extRef;//MEMO:Refrection
    @Transient
    private String fileName;
    @Transient
    private ImageIcon icon;
    @Transient
    private int imageNumber;

    /**
     * コンストラクタ
     */
    public SchemaModel() {
    }

    /**
     * 外部参照要素
     * MEMO:Refrection
     * @return 外部参照要素
     */
    public ExtRefModel getExtRef() {
        return extRef;
    }

    /**
     * 外部参照要素
     * MEMO:Refrection
     * @param val 外部参照要素
     */
    public void setExtRef(ExtRefModel val) {
        extRef = val;
    }

    /**
     * 親ドキュメント
     * MEMO:Refrection
     * @return 親ドキュメント
     */
    public DocumentModel getDocument() {
        return document;
    }

    /**
     * 親ドキュメントの参照をセット
     * MEMO:Refrection
     * @param document 親ドキュメント
     */
    public void setDocument(DocumentModel document) {
        this.document = document;
    }

    /**
     * JPEG
     * @return JPEGのバイト列
     */
    public byte[] getJpegBytes() {
        return jpegBytes;
    }

    /**
     * JPEG
     * @param jpegBytes JPEGのバイト列
     */
    public void setJpegBytes(byte[] jpegBytes) {
        this.jpegBytes = jpegBytes;
        if (jpegBytes == null) {
            jpegBytes = null;
        } else {
            this.jpegBlob = new BlobImpl(jpegBytes);
        }
    }

    /**
     * 画像データ
     * MEMO:Refrection
     * @return JPEGのBlob
     */
    public Blob getJpegBlob() {
        return this.jpegBlob;
    }

    /**
     * 画像データ
     * MEMO:Refrection
     * @param jpegBlob JPEGのBlob
     * @throws SQLException
     */
    public void setJpegBlob(Blob jpegBlob) throws SQLException {
        if (jpegBlob != null) {
            this.jpegBlob = jpegBlob;
            this.jpegBytes = jpegBlob.getBytes(1, (int) jpegBlob.length());
        }
    }

    /**
     * アイコン
     * @return アイコン
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * アイコン
     * @param val アイコン
     */
    public void setIcon(ImageIcon val) {
        icon = val;
    }

    /**
     * 番号
     * @return 番号
     */
    public int getImageNumber() {
        return imageNumber;
    }

    /**
     * 番号
     * @param imageNumber 番号
     */
    public void setImageNumber(int imageNumber) {
        this.imageNumber = imageNumber;
    }

    /**
     * ファイル名
     * @return ファイル名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * ファイル名
     * @param val ファイル名
     */
    public void setFileName(String val) {
        fileName = val;
    }

    /**
     * InfoModel
     * @return InfoModel
     */
    public IInfoModel getModel() {
        return (IInfoModel) getExtRef();
    }

    /**
     * InfoModel
     * @param val InfoModel
     */
    public void setModel(IInfoModel val) {
        setExtRef((ExtRefModel) val);
    }

    /**
     * 比較
     * @param other
     * @return 同じなら真
     */
    public int compareTo(Object other) {
        int result = super.compareTo(other);
        if (result == 0) {
            int no1 = getImageNumber();
            int no2 = ((SchemaModel) other).getImageNumber();
            result = no1 - no2;
        }
        return result;
    }

    /**
     * ドキュメントモデルをシリアライズ
     * @param ドキュメントモデル
     * @return　ドキュメントモデルのXML
     */
    private String DocumentModelToString(DocumentModel d) {
        if (d == null) {
            return "";
        }
        return Long.toString(d.getId());
    }

    /**
     * シリアライズ
     * @param result
     * @throws IOException
     */
    @Override
    public void serialize(Writer result) throws IOException {
        result.append("<SchemaModel" + " document_id='" + DocumentModelToString(document) + "'>" + System.getProperty("line.separator"));
        super.serialize(result);
        if (extRef != null) {
            extRef.serialize(result);
        }
        result.append("<jpegByte>");
        result.append(System.getProperty("line.separator"));
        result.append(jpegBytes.toString());
        result.append("</jpegByte>");
        result.append(System.getProperty("line.separator"));
        result.append("</SchemaModel>");
        result.append(System.getProperty("line.separator"));
    }
}
