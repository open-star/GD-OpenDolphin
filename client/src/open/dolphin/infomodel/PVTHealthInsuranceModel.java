/*
 * PVTHealthInsurance.java
 *
 * Created on 2001/10/10, 13:23
 *
 * Last updated on 2002/12/31.
 * Revised on 2003/01/06 for Null Pointer Exception at item.toString() (publicInsurances).
 * Revised on 2003/01/07 for initializing publicInsurances object first time.
 * Revised on 2003/01/08 renamed pvtPublicInsuranceItem from publicInsurances.
 *                                    added 'insuranceClassCodeTableId' in toString()
 *
 */
package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * 保険 MEMO:マッピング
 * Health-Insurance class to be parsed.
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 * Modified by Mirror-I corp for adding 'PvtPublicInsuranceItem' and related function to store/get PvtPublicInsuranceItem name
 */
public class PVTHealthInsuranceModel extends InfoModel {//id

    private static final long serialVersionUID = 6119471803755585233L;
    private String uuid;
    private String insuranceClass;
    private String insuranceClassCode;
    private String insuranceClassCodeSys;
    private String insuranceNumber;
    private String clientGroup;
    private String clientNumber;
    private String familyClass;
    private String startDate;
    private String expiredDate;
    private String[] continuedDisease;
    private String payInRatio;
    private String payOutRatio;
    private PVTPublicInsuranceItemModel[] pvtPublicInsuranceItem;

    /** 
     * Creates new PVTHealthInsurance
     */
    public PVTHealthInsuranceModel() {
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
     * uuidのGetter
     * @return
     */
    public String getGUID() {
        return uuid;
    }

    /**
     * uuidのSetter
     * @param val
     */
    public void setGUID(String val) {
        uuid = val;
    }

    /**
     * insuranceClassのGetter
     * @return
     */
    public String getInsuranceClass() {
        return insuranceClass;
    }

    /**
     * insuranceClassのSetter
     * @param val
     */
    public void setInsuranceClass(String val) {
        insuranceClass = val;
    }

    /**
     * insuranceClassCodeのGetter
     * @return
     */
    public String getInsuranceClassCode() {
        return insuranceClassCode;
    }

    /**
     * insuranceClassCodeのSetter
     * @param val
     */
    public void setInsuranceClassCode(String val) {
        insuranceClassCode = val;
    }

    /**
     * insuranceClassCodeSysのGetter
     * @return
     */
    public String getInsuranceClassCodeSys() {
        return insuranceClassCodeSys;
    }

    /**
     * insuranceClassCodeSysのSetter
     * @param val
     */
    public void setInsuranceClassCodeSys(String val) {
        insuranceClassCodeSys = val;
    }

    /**
     * insuranceNumberのGetter
     * @return
     */
    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    /**
     * insuranceNumberのSetter
     * @param val
     */
    public void setInsuranceNumber(String val) {
        insuranceNumber = val;
    }

    /**
     * clientGroupのGetter
     * @return
     */
    public String getClientGroup() {
        return clientGroup;
    }

    /**
     * clientGroupのSetter
     * @param val
     */
    public void setClientGroup(String val) {
        clientGroup = val;
    }

    /**
     * clientNumberのGetter
     * @return
     */
    public String getClientNumber() {
        return clientNumber;
    }

    /**
     * clientNumberのSetter
     * @param val
     */
    public void setClientNumber(String val) {
        clientNumber = val;
    }

    /**
     * familyClassのGetter
     * @return
     */
    public String getFamilyClass() {
        return familyClass;
    }

    /**
     * familyClassのSetter
     * @param val
     */
    public void setFamilyClass(String val) {
        familyClass = val;
    }

    /**
     * startDateのGetter
     * @return
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * startFormatDateのGetter
     * @return
     */
    public String getStartFormatDate() {
        return ModelUtils.Convert(IInfoModel.DATE_WITHOUT_TIME, IInfoModel.KARTE_DATE_FORMAT, startDate);
    }

    /**
     * startDateのSetter
     * @param val
     */
    public void setStartDate(String val) {
        startDate = val;
    }

    /**
     * expiredDateのGetter
     * @return
     */
    public String getExpiredDate() {
        return expiredDate;
    }

    /**
     * expiredFormatDateのGetter
     * @return
     */
    public String getExpiredFormatDate() {
        return ModelUtils.Convert(IInfoModel.DATE_WITHOUT_TIME, IInfoModel.KARTE_DATE_FORMAT, expiredDate);
    }

    /**
     * expiredDateのSetter
     * @param val
     */
    public void setExpiredDate(String val) {
        expiredDate = val;
    }

    /**
     * continuedDiseaseのGetter
     * @return
     */
    public String[] getContinuedDisease() {
        return continuedDisease;
    }

    /**
     * continuedDiseaseのSetter
     * @param val
     */
    public void setContinuedDisease(String[] val) {
        continuedDisease = val;
    }

    /**
     * continuedDiseaseのAdder
     * @param val
     */
    public void addContinuedDisease(String val) {

        int len = 0;

        if (continuedDisease == null) {
            continuedDisease = new String[1];
        } else {
            len = continuedDisease.length;
            String[] dest = new String[len + 1];
            System.arraycopy(continuedDisease, 0, dest, 0, len);
            continuedDisease = dest;
        }
        continuedDisease[len] = val;
    }

    /**
     * payInRatioのGetter
     * @return
     */
    public String getPayInRatio() {
        return payInRatio;
    }

    /**
     * payInRatioのSetter
     * @param val
     */
    public void setPayInRatio(String val) {
        payInRatio = val;
    }

    /**
     * payOutRatioのGetter
     * @return
     */
    public String getPayOutRatio() {
        return payOutRatio;
    }

    /**
     * payOutRatioのSetter
     * @param val
     */
    public void setPayOutRatio(String val) {
        payOutRatio = val;
    }

    /**
     * pvtPublicInsuranceItemのGetter
     * @return
     */
    public PVTPublicInsuranceItemModel[] getPVTPublicInsuranceItem() {
        return pvtPublicInsuranceItem;
    }

    /**
     * pvtPublicInsuranceItemのSetter
     * @param val
     */
    public void setPVTPublicInsuranceItem(PVTPublicInsuranceItemModel[] val) {
        pvtPublicInsuranceItem = val;
    }

    /**
     * pvtPublicInsuranceItemのAdder
     * @param value
     */
    public void addPvtPublicInsuranceItem(PVTPublicInsuranceItemModel value) {
        if (pvtPublicInsuranceItem == null) {
            pvtPublicInsuranceItem = new PVTPublicInsuranceItemModel[1];
            pvtPublicInsuranceItem[0] = value;
            return;
        }
        int len = pvtPublicInsuranceItem.length;
        PVTPublicInsuranceItemModel[] dest = new PVTPublicInsuranceItemModel[len + 1];
        System.arraycopy(pvtPublicInsuranceItem, 0, dest, 0, len);
        pvtPublicInsuranceItem = dest;
        pvtPublicInsuranceItem[len] = value;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        if (insuranceNumber != null && insuranceClass != null) {
            buf.append(insuranceNumber);
            buf.append("  ");
            buf.append(insuranceClass);
        } else if (insuranceNumber != null) {
            buf.append(insuranceNumber);
        } else if (insuranceClass != null) {
            buf.append(insuranceClass);
        } else {
            buf.append("自費");
        }

        if (pvtPublicInsuranceItem != null) {
            int len = pvtPublicInsuranceItem.length;
            buf.append(" ");
            for (int i = 0; i < len; i++) {
                PVTPublicInsuranceItemModel item = pvtPublicInsuranceItem[i];
                if (item != null) {
                    if (i != 0) {
                        buf.append("・");
                    }
                    buf.append(item.toString());
                }
            }
            buf.append(" ");
        }

        return buf.toString();
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void serialize(Writer result) throws IOException {
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    public void deserialize(Writer result) throws IOException {
    }
}
