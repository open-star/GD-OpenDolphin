/*
 * SimpleLaboTestItem.java
 *
 * Created on 2003/07/30, 10:41
 */
package open.dolphin.plugin.labotestdocumentpanel;

/**
 * SimpleLaboTestItem
 * 
 * @author  Kazushi Minagawa
 */
public class SimpleLaboTestItem implements Comparable {

    private String itemCodeID;
    private String itemCode;
    private String itemName;
    private String itemValue;
    private String itemUnit;
    private String low;
    private String up;
    private String normal;
    private String out;
    private String itemMemo;
    private String itemMemoCodeName;
    private String itemMemoCode;
    private String itemMemoCodeId;
    private String itemFreeMemo;
    private String extRef;

    /** 
     * Creates a new instance of SimpleLaboTestItem
     */
    public SimpleLaboTestItem() {
    }

    /**
     *
     * @return
     */
    public String getItemCodeID() {
        return itemCodeID;
    }

    /**
     *
     * @param val
     */
    public void setItemCodeID(String val) {
        itemCodeID = val;
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
     * @param val
     */
    public void setItemCode(String val) {
        itemCode = val;
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
     * @param val
     */
    public void setItemName(String val) {
        itemName = val;
    }

    /**
     *
     * @return
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     *
     * @param val
     */
    public void setItemValue(String val) {
        itemValue = val;
    }

    /**
     *
     * @return
     */
    public String getItemUnit() {
        return itemUnit;
    }

    /**
     *
     * @param val
     */
    public void setItemUnit(String val) {
        itemUnit = val;
    }

    /**
     *
     * @return
     */
    public String getLow() {
        return low;
    }

    /**
     *
     * @param val
     */
    public void setLow(String val) {
        low = val;
    }

    /**
     *
     * @return
     */
    public String getUp() {
        return up;
    }

    /**
     *
     * @param val
     */
    public void setUp(String val) {
        up = val;
    }

    /**
     *
     * @return
     */
    public String getNormal() {
        return normal;
    }

    /**
     *
     * @param val
     */
    public void setNormal(String val) {
        normal = val;
    }

    /**
     *
     * @return
     */
    public String getOut() {
        return out;
    }

    /**
     *
     * @param val
     */
    public void setOut(String val) {
        out = val;
    }

    /**
     *
     * @return
     */
    public String getItemMemo() {
        return itemMemo;
    }

    /**
     *
     * @param val
     */
    public void setItemMemo(String val) {
        itemMemo = val;
    }

    /**
     *
     * @return
     */
    public String getItemMemoCodeName() {
        return itemMemoCodeName;
    }

    /**
     *
     * @param val
     */
    public void setItemMemoCodeName(String val) {
        itemMemoCodeName = val;
    }

    /**
     *
     * @return
     */
    public String getItemMemoCode() {
        return itemMemoCode;
    }

    /**
     *
     * @param val
     */
    public void setItemMemoCode(String val) {
        itemMemoCode = val;
    }

    /**
     *
     * @return
     */
    public String getItemMemoCodeId() {
        return itemMemoCodeId;
    }

    /**
     *
     * @param val
     */
    public void setItemMemoCodeId(String val) {
        itemMemoCodeId = val;
    }

    /**
     *
     * @return
     */
    public String getItemFreeMemo() {
        return itemFreeMemo;
    }

    /**
     *
     * @param val
     */
    public void setItemFreeMemo(String val) {
        itemFreeMemo = val;
    }

    /**
     *
     * @return
     */
    public String getExtRef() {
        return extRef;
    }

    /**
     *
     * @param val
     */
    public void setExtRef(String val) {
        extRef = val;
    }

    @Override
    public int compareTo(Object o) {
        SimpleLaboTestItem other = (SimpleLaboTestItem) o;
        return itemCode.compareTo(other.getItemCode());
    }

    /**
     *
     * @param testID
     * @return
     */
    public boolean isTest(LaboTestItemID testID) {
        return (itemCodeID.equals(testID.getItemCodeID()) && itemCode.equals(testID.getItemCode()))
                ? true
                : false;
    }
    /**
     * 
     * @return
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(itemValue);
        buf.append(" ");
        if (itemUnit != null) {
            buf.append(itemUnit);
        }
        return buf.toString();
    }
}
