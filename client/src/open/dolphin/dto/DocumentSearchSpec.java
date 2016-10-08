package open.dolphin.dto;

import java.util.Date;

/**
 * DocumentSearchSpec　MEMO:DTO
 * 
 * 
 * @author Minagawa,Kazushi
 */
public class DocumentSearchSpec extends DolphinDTO {

    private static final long serialVersionUID = 8297575731862377052L;
    /**
     *
     */
    public static final int DOCTYPE_SEARCH = 0;
    /**
     *
     */
    public static final int PURPOSE_SEARCH = 1;
    /**
     *
     */
    public static final int CREATOR_SEARCH = 2;
    /**
     *
     */
    public static final int LICENSE_SEARCH = 3;
    private int code;
    private long karteId;
    private int docId;
    private String facilityId;
    private String patientId;
    private String docType;
    private String purpose;
    private String creator;
    private String license;
    private Date fromDate;
    private Date toDate;
    private String status;
    private boolean includeModifid;
    private boolean includeUnsend;
    private boolean includeSend;
    private boolean ascending;
    private int lines;

    /**
     *　コンストラクタ
     */
    public DocumentSearchSpec() {
        lines = 100;
    }

    /**
     *
     * @return　ライン
     */
    public int getLines() {
        return lines;
    }

    /**
     *
     * @param lines
     */
    public void setLines(int lines) {
        this.lines = lines;
    }

    /**
     *
     * @return　昇順
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     *
     * @param ascending　昇順
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    /**
     *
     * @return　コード
     */
    public int getCode() {
        return code;
    }

    /**
     *
     * @param code　コード
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     *
     * @return クリエイター
     */
    public String getCreator() {
        return creator;
    }

    /**
     *
     * @param creator クリエイター
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     *
     * @return ドキュメントID
     */
    public int getDocId() {
        return docId;
    }

    /**
     *
     * @param docId　ドキュメントID
     */
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     *
     * @return ドキュメントタイプ
     *
     */
    public String getDocType() {
        return docType;
    }

    /**
     *
     * @param docType　ドキュメントタイプ
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     *
     * @return
     */
    public String getFacilityId() {
        return facilityId;
    }

    /**
     *
     * @param facilityId
     */
    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    /**
     *
     * @return
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     *
     * @param fromDate
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     *
     * @return
     */
    public long getKarteId() {
        return karteId;
    }

    /**
     *
     * @param karteId
     */
    public void setKarteId(long karteId) {
        this.karteId = karteId;
    }

    /**
     *
     * @return
     */
    public String getLicense() {
        return license;
    }

    /**
     *
     * @param license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     *
     * @return
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     *
     * @param patientId
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     *
     * @return
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     *
     * @param purpose
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     *
     * @param toDate
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     *
     * @return
     */
    public boolean isIncludeModifid() {
        return includeModifid;
    }

    /**
     *
     * @param includeModifid
     */
    public void setIncludeModifid(boolean includeModifid) {
        this.includeModifid = includeModifid;
    }

    /**
     *
     * @return
     */
    public boolean isIncludeUnsend() {
        return includeUnsend;
    }

    /**
     *
     * @param includeUnsend
     */
    public void setIncludeUnsend(boolean includeUnsend) {
        this.includeUnsend = includeUnsend;
    }

    /**
     *
     * @return
     */
    public boolean isIncludeSend() {
        return includeSend;
    }

    /**
     *
     * @param includeSend
     */
    public void setIncludeSend(boolean includeSend) {
        this.includeSend = includeSend;
    }
}
