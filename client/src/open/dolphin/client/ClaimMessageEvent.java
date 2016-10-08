package open.dolphin.client;

import open.dolphin.infomodel.IPatientModel;

/**
 * CLAIM インスタンスを通知するイベント。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class ClaimMessageEvent extends java.util.EventObject {

    private String patientId;
    private String patientName;
    private String patientSex;
    private String title;
    private String instance;
    private int number;
    private String confirmDate;

    /**
     * Creates new ClaimEvent
     * @param source
     * @param patient
     * @param claimMessage 
     */
    public ClaimMessageEvent(Object source, IPatientModel patient, String claimMessage) {
        super(source);
        setClaimInstance(claimMessage);
        setPatientId(patient.getPatientId());
        setPatientName(patient.getFullName());
        setPatientSex(patient.getGender());
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
     * @param val　患者ID
     */
    private void setPatientId(String val) {
        patientId = val;
    }

    /**
     *
     * @return 患者名
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     *
     * @param val 患者名
     *
     */
    public void setPatientName(String val) {
        patientName = val;
    }

    /**
     *　性別
     * @return　性別
     */
    public String getPatientSex() {
        return patientSex;
    }

    /**
     *　性別
     * @param val　性別
     */
    public void setPatientSex(String val) {
        patientSex = val;
    }

    /**
     *　タイトル
     * @return　タイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     *　タイトル
     * @param val　タイトル
     */
    public void setTitle(String val) {
        title = val;
    }

    /**
     *　保険
     * @return　保険
     */
    public String getClaimInsutance() {
        return instance;
    }

    /**
     *　保険
     * @param val　保険
     */
    private void setClaimInstance(String val) {
        instance = val;
    }

    /**
     *
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     *
     * @param val
     */
    public void setNumber(int val) {
        number = val;
    }

    /**
     *　承認日
     * @return　承認日
     */
    public String getConfirmDate() {
        return confirmDate;
    }

    /**
     *　承認日
     * @param val　承認日
     */
    public void setConfirmDate(String val) {
        confirmDate = val;
    }
}
