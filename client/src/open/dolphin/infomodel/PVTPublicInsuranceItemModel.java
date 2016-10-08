/*
 * PublicHealthInsuranceitem.java
 *
 * Created on 2001/10/30, 11:20
 */
package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * 保険 MEMO:マッピング
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class PVTPublicInsuranceItemModel extends InfoModel {//id

    private static final long serialVersionUID = 7141232138488822853L;
    private String priority;
    private String providerName;
    private String provider;
    private String recipient;
    private String startDate;
    private String expiredDate;
    private String paymentRatio;
    private String paymentRatioType;

    /**
     *
     */
    public PVTPublicInsuranceItemModel() {
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
     * paymentRatioTypeのGetter
     * @return
     */
    public String getPaymentRatioType() {
        return paymentRatioType;
    }
    /**
     * paymentRatioTypeのSetter
     * @param val
     */
    public void setPaymentRatioType(String val) {
        paymentRatioType = val;
    }
    /**
     * paymentRatioのGetter
     * @return
     */
    public String getPaymentRatio() {
        return paymentRatio;
    }
    /**
     * paymentRatioのSetter
     * @param val
     */
    public void setPaymentRatio(String val) {
        paymentRatio = val;
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
     * recipientのGetter
     * @return
     */
    public String getRecipient() {
        return recipient;
    }
    /**
     * recipientのSetter
     * @param val
     */
    public void setRecipient(String val) {
        recipient = val;
    }

    /**
     * priorityのGetter
     * @return
     */
    public String getPriority() {
        return priority;
    }

    /**
     * priorityのSetter
     * @param val
     */
    public void setPriority(String val) {
        priority = val;
    }
    /**
     * providerNameのGetter
     * @return
     */
    public String getProviderName() {
        return providerName;
    }
    /**
     * providerNameのSetter
     * @param val
     */
    public void setProviderName(String val) {
        providerName = val;
    }

    /**
     * providerのGetter
     * @return
     */
    public String getProvider() {
        return provider;
    }
    /**
     * providerのSetter
     * @param val
     */
    public void setProvider(String val) {
        provider = val;
    }
    /**
     *
     * @return
     */
    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        if (providerName != null) {
            //buf.append("InsurancePubProviderName: ");
            buf.append(providerName);
            //buf.append("\n");  
        } else if (provider != null) {
            //buf.append("InsurancePubProvider: ");
            buf.append(provider);
            //buf.append("\n");  
        }

        return buf.toString();
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

    /*public String toString() {

    StringBuffer buf = new StringBuffer();

    if (publicInsurancePriority != null) {
    buf.append("InsurancePubPriority: ");
    buf.append(publicInsurancePriority);
    buf.append("\n");
    }

    if (publicInsuranceProviderName != null) {
    buf.append("InsurancePubProviderName: ");
    buf.append(publicInsuranceProviderName);
    buf.append("\n");
    }

    if (publicInsuranceProvider != null) {
    buf.append("InsurancePubProvider: ");
    buf.append(publicInsuranceProvider);
    buf.append("\n");
    }

    if (publicInsuranceRecipient != null) {
    buf.append("InsurancePubRecipient: ");
    buf.append(publicInsuranceRecipient);
    buf.append("\n");
    }

    if (publicInsuranceStartDate != null) {
    buf.append("InsurancePubStartDate: ");
    buf.append(publicInsuranceStartDate);
    buf.append("\n");
    }

    if (publicInsuranceExpiredDate != null) {
    buf.append("InsurancePubExpiredDate: ");
    buf.append(publicInsuranceExpiredDate);
    buf.append("\n");
    }

    if (publicInsurancePaymentRatio != null) {
    buf.append("InsurancePubPaymentRatio: ");
    buf.append(publicInsurancePaymentRatio);
    buf.append("\n");
    }

    if (publicInsurancePaymentRatioType != null) {
    buf.append("insurancePubPaymentRatioType: ");
    buf.append(publicInsurancePaymentRatioType);
    buf.append("\n");
    }

    return buf.toString();
    }  */
}
