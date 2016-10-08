/*
 * PVTClaim.java
 *
 * Created on 2001/10/10, 13:57
 *
 * Last updated on 2002/12/31
 *
 */
package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import open.dolphin.queries.DolphinQuery;

/**
 * Simple Claim　Class used for PVT. MEMO:マッピング
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 * Modified by Mirror-I corp for adding 'claimDeptName' and related function to store/get Department name
 */
public class PVTClaim extends InfoModel {//id

    private static final long serialVersionUID = -8573272136025043849L;
    private String claimStatus; //appoint(予約)、regist(受付)、perform(実施)
    private String claimRegistTime;
    private String claimAdmitFlag;
    private String claimDeptName;
    private String claimDeptCode;
    private String assignedDoctorId;
    private String assignedDoctorName;
    private List claimAppName;
    private String claimAppMemo;
    private String claimItemCode;
    private String claimItemName;
    private String insuranceUid;
    private String jmariCode;
    private String claimBundleMemo;

    /** Creates new PVTClaim */
    public PVTClaim() {
        super();
    }

    /**
     * 検索 MEMO:何もしない
     * @param query
     * @return false
     */
    @Override
    public boolean search(DolphinQuery query) {
        return false;
    }

    /**
     * claimStatusのGetter
     * @return
     */
    public String getClaimStatus() {
        return claimStatus;
    }

    /**
     * claimStatusのSetter
     * @param val
     */
    public void setClaimStatus(String val) {
        claimStatus = val;
    }

    /**
     * claimRegistTimeのGetter
     * @return
     */
    public String getClaimRegistTime() {
        return claimRegistTime;
    }

    /**
     * claimRegistTimeのSetter
     * @param val
     */
    public void setClaimRegistTime(String val) {
        claimRegistTime = val;
    }

    /**
     * claimAdmitFlagのGetter
     * @return
     */
    public String getClaimAdmitFlag() {
        return claimAdmitFlag;
    }

    /**
     * claimAdmitFlagのSetter
     * @param val
     */
    public void setClaimAdmitFlag(String val) {
        claimAdmitFlag = val;
    }

    /**
     * claimDeptNameのGetter
     * @return
     */
    public String getClaimDeptName() {
        return claimDeptName;
    }

    /**
     * claimDeptNameのSetter
     * @param val
     */
    public void setClaimDeptName(String val) {
        claimDeptName = val;
    }

    /**
     * claimDeptCodeのGetter
     * @return
     */
    public String getClaimDeptCode() {
        return claimDeptCode;
    }

    /**
     * claimDeptCodeのSetter
     * @param val
     */
    public void setClaimDeptCode(String val) {
        claimDeptCode = val;
    }

    /**
     * claimAppNameのGetter
     * @return
     */
    public List getClaimAppName() {
        return claimAppName;
    }

    /**
     * claimAppNameのAdder
     * @param val
     */
    @SuppressWarnings("unchecked")
    public void addClaimAppName(String val) {
        if (claimAppName == null) {
            claimAppName = new ArrayList(3);
        }
        claimAppName.add(val);
    }

    /**
     * claimAppMemoのGetter
     * @return
     */
    public String getClaimAppMemo() {
        return claimAppMemo;
    }

    /**
     * claimAppMemoのSetter
     * @param val
     */
    public void setClaimAppMemo(String val) {
        claimAppMemo = val;
    }

    /**
     * claimBundleMemoのGetter
     * @return
     */
    public String getClaimBundleMemo() {
        return claimBundleMemo;
    }

    /**
     * claimBundleMemoのSetter
     * @param val
     */
    public void setClaimBundleMemo(String val) {
        claimBundleMemo = val;
    }

    /**
     * claimItemCodeのGetter
     * @return
     */
    public String getClaimItemCode() {
        return claimItemCode;
    }

    /**
     * claimItemCodeのSetter
     * @param val
     */
    public void setClaimItemCode(String val) {
        claimItemCode = val;
    }

    /**
     * claimItemNameのGetter
     * @return
     */
    public String getClaimItemName() {
        return claimItemName;
    }

    /**
     * claimItemNameのSetter
     * @param val
     */
    public void setClaimItemName(String val) {
        claimItemName = val;
    }

    /**
     *
     * @return
     */
    public String toString() {

        StringBuffer buf = new StringBuffer();

        if (claimStatus != null) {
            buf.append("ClaimStatus: ");
            buf.append(claimStatus);
            buf.append(System.getProperty("line.separator"));
        }

        if (claimRegistTime != null) {
            buf.append("ClaimRegistTime: ");
            buf.append(claimRegistTime);
            buf.append(System.getProperty("line.separator"));
        }

        if (claimAdmitFlag != null) {
            buf.append("ClaimAdmitFlag: ");
            buf.append(claimAdmitFlag);
            buf.append(System.getProperty("line.separator"));
        }

        // Mirror-I start
        if (claimDeptName != null) {
            buf.append("claimDeptName: ");
            buf.append(claimDeptName);
            buf.append(System.getProperty("line.separator"));
        }
        // Mirror-I end

        if (claimAppName != null) {
            int len = claimAppName.size();
            for (int i = 0; i < len; i++) {
                buf.append("ClaimAppName: ");
                buf.append((String) claimAppName.get(i));
                buf.append(System.getProperty("line.separator"));
            }
        }

        if (claimAppMemo != null) {
            buf.append("ClaimAppointMemo: ");
            buf.append(claimAppMemo);
            buf.append(System.getProperty("line.separator"));
        }

        if (claimItemCode != null) {
            buf.append("ClaimItemCode: ");
            buf.append(claimItemCode);
            buf.append(System.getProperty("line.separator"));
        }

        if (claimItemName != null) {
            buf.append("ClaimItemName: ");
            buf.append(claimItemName);
            buf.append(System.getProperty("line.separator"));
        }

        return buf.toString();
    }

    /**
     * insuranceUidのSetter
     * @param insuranceUid The insuranceUid to set.
     */
    public void setInsuranceUid(String insuranceUid) {
        this.insuranceUid = insuranceUid;
    }

    /**
     * insuranceUidのGetter
     * @return Returns the insuranceUid.
     */
    public String getInsuranceUid() {
        return insuranceUid;
    }

    /**
     * jmariCodeのGetter
     * @return
     */
    public String getJmariCode() {
        return jmariCode;
    }

    /**
     * jmariCodeのSetter
     * @param jmariCode
     */
    public void setJmariCode(String jmariCode) {
        this.jmariCode = jmariCode;
    }

    /**
     * assignedDoctorNameのGetter
     * @return
     */
    public String getAssignedDoctorName() {
        return assignedDoctorName;
    }

    /**
     * assignedDoctorNameのSetter
     * @param assignedDoctorName
     */
    public void setAssignedDoctorName(String assignedDoctorName) {
        this.assignedDoctorName = assignedDoctorName;
    }

    /**
     * assignedDoctorIdのGetter
     * @return
     */
    public String getAssignedDoctorId() {
        return assignedDoctorId;
    }

    /**
     * assignedDoctorIdのSetter
     * @param assignedDoctorId
     */
    public void setAssignedDoctorId(String assignedDoctorId) {
        this.assignedDoctorId = assignedDoctorId;
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
        //TODO serialize
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void deserialize(Writer result) throws IOException {
        //TODO deserialize
    }
}
