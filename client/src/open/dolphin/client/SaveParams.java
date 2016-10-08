package open.dolphin.client;

/**
 * Parametrs to save document.
 *　「ドキュメント保存」のパラメーター
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SaveParams {

    // MML送信するかどうかのフラグ 送信する時 true
    private boolean sendMML;
    // 文書タイトル
    private String title;
    // 診療科情報
    private String department;
    // 印刷部数
    private int printCount = -1;
    // 患者への参照を許可するかどうかのフラグ 許可するとき true
    private boolean allowPatientRef;
    // 診療歴のある施設への参照許可フラグ 許可する時 true
    private boolean allowClinicRef;
    // 仮保存の時 true
    private String status;
    // CLAIM 送信フラグ
    private boolean sendClaim;
    // CLAIM 送信を disable にする
    private boolean disableSendClaim;

    /** 
     * Creates new SaveParams 
     */
    public SaveParams() {
        super();
    }

    /**
     *
     * @param sendMML
     */
    public SaveParams(boolean sendMML) {
        this();
        this.sendMML = sendMML;
    }

    /**
     *
     * @return
     */
    public boolean getSendMML() {
        return sendMML;
    }

    /**
     *
     * @param b
     */
    public void setSendMML(boolean b) {
        sendMML = b;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param val
     */
    public void setTitle(String val) {
        title = val;
    }

    /**
     *
     * @return
     */
    public String getDepartment() {
        return department;
    }

    /**
     *
     * @param val
     */
    public void setDepartment(String val) {
        department = val;
    }

    /**
     *
     * @return 印刷部数
     */
    public int getPrintCount() {
        return printCount;
    }

    /**
     *
     * @param val
     */
    public void setPrintCount(int val) {
        printCount = val;
    }

    /**
     *
     * @return
     */
    public boolean isAllowPatientRef() {
        return allowPatientRef;
    }

    /**
     *
     * @param b
     */
    public void setAllowPatientRef(boolean b) {
        allowPatientRef = b;
    }

    /**
     *
     * @return
     */
    public boolean isAllowClinicRef() {
        return allowClinicRef;
    }

    /**
     *
     * @param b
     */
    public void setAllowClinicRef(boolean b) {
        allowClinicRef = b;
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
     * @return CLAIM 送信フラグ
     */
    public boolean isSendClaim() {
        return sendClaim;
    }

    /**
     *
     * @param sendClaim CLAIM 送信フラグ
     */
    public void setSendClaim(boolean sendClaim) {
        this.sendClaim = sendClaim;
    }

    /**
     *
     * @return
     */
    public boolean isDisableSendClaim() {
        return disableSendClaim;
    }

    /**
     *
     * @param disableSendClaim
     */
    public void setDisableSendClaim(boolean disableSendClaim) {
        this.disableSendClaim = disableSendClaim;
    }
}
