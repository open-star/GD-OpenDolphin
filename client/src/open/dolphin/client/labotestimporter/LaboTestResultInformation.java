/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.labotestimporter;

/**
 *
 */
public class LaboTestResultInformation {

    private String itemName;
    private String itemCode;
    private String itemCodeId;
    private String code;//MEMO: unused?
    private String value;
    private String valueForm;
    private String comment1;
    private String comment2;

    /**
     *
     */
    public LaboTestResultInformation() {
    }

    /**
     *
     * @param itemCode
     * @param value
     * @param valueForm
     * @param comment1
     * @param comment2
     */
    public LaboTestResultInformation(String itemCode, String value, String valueForm, String comment1, String comment2) {
        this.itemCode = itemCode.trim();
        this.value = value.trim();
        this.valueForm = valueForm.trim();
        this.comment1 = comment1.trim();
        this.comment2 = comment2.trim();
    }

    /**
     *
     * @return　
     */
    public boolean isEmpty() {
        return this.itemCode.trim().isEmpty();
    }

    /**
     *
     * @return
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     *
     * @param itemCode
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode.trim();
    }

    /**
     *
     * @return
     */
    public String getItemCodeId() {
        return itemCodeId;
    }

    /**
     *
     * @param itemCodeId
     */
    public void setItemCodeId(String itemCodeId) {
        this.itemCodeId = itemCodeId;
    }

    /**
     *
     * @return
     */
    public String getItemName() {
        return itemName;
    }

    /**
     *
     * @param itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     *
     * @return
     */
    public String getComment1() {
        return comment1;
    }

    /**
     *
     * @param comment1
     */
    public void setComment1(String comment1) {
        this.comment1 = comment1.trim();
    }

    /**
     *
     * @return
     */
    public String getComment2() {
        return comment2;
    }

    /**
     *
     * @param comment2
     */
    public void setComment2(String comment2) {
        this.comment2 = comment2.trim();
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value.trim();
    }

    /**
     *
     * @return
     */
    public String getValueForm() {
        return valueForm;
    }

    /**
     *
     * @param valueForm
     */
    public void setValueForm(String valueForm) {
        this.valueForm = valueForm.trim();
    }
}
