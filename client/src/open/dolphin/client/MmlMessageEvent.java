package open.dolphin.client;

import java.util.List;
import open.dolphin.infomodel.SchemaModel;

/**
 * MML インスタンスを通知するイベントクラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class MmlMessageEvent extends java.util.EventObject {

    private static final long serialVersionUID = -5163032502414937817L;
    private String patientId;
    private String patientName;
    private String patientSex;
    private String title;
    private String groupId;
    private String mmlInstance;
    private List<SchemaModel> schemas;
    private int number;
    private String content;
    private String confirmDate;

    /**
     * Creates new MmlPackage
     * @param source
     */
    public MmlMessageEvent(Object source) {
        super(source);
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
     * @param val
     */
    public void setPatientId(String val) {
        patientId = val;
    }

    /**
     *
     * @return
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     *
     * @param val
     */
    public void setPatientName(String val) {
        patientName = val;
    }

    /**
     *
     * @return
     */
    public String getPatientSex() {
        return patientSex;
    }

    /**
     *
     * @param val
     */
    public void setPatientSex(String val) {
        patientSex = val;
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
    public String getGroupId() {
        return groupId;
    }

    /**
     *
     * @param val
     */
    public void setGroupId(String val) {
        groupId = val;
    }

    /**
     *
     * @return
     */
    public String getMmlInstance() {
        return mmlInstance;
    }

    /**
     *
     * @param val
     */
    public void setMmlInstance(String val) {
        mmlInstance = val;
    }

    /**
     *
     * @return
     */
    public List<SchemaModel> getSchema() {
        return schemas;
    }

    /**
     *
     * @param val
     */
    public void setSchema(List<SchemaModel> val) {
        schemas = val;
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
     *
     * @return
     */
    public String getContentInfo() {
        return content;
    }

    /**
     *
     * @param val
     */
    public void setContentInfo(String val) {
        content = val;
    }

    /**
     *
     * @return
     */
    public String getConfirmDate() {
        return confirmDate;
    }

    /**
     *
     * @param val
     */
    public void setConfirmDate(String val) {
        confirmDate = val;
    }
}
