/*
 * LaboTestItemID.java
 *
 * Created on 2003/08/01, 8:51
 */
package open.dolphin.plugin.labotestdocumentpanel;

/**
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class LaboTestItemID implements Comparable {

    private String itemCodeID;
    private String itemCode;
    private String itemName;

    /** Creates a new instance of LaboTestItemID */
    public LaboTestItemID() {
    }

    /**
     *
     * @param codeID
     * @param code
     * @param name
     */
    public LaboTestItemID(String codeID, String code, String name) {
        this();

        itemCodeID = codeID;
        itemCode = code;
        itemName = name;
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
    @Override
    public int hashCode() {

        return itemCodeID.hashCode() + itemCode.hashCode();
    }
    /**
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {

        if (other != null && getClass() == other.getClass()) {

            LaboTestItemID sp = (LaboTestItemID) other;

            return (itemCodeID.equals(sp.getItemCodeID()) &&
                    itemCode.equals(sp.getItemCode())) ? true : false;
        }

        return false;
    }

    @Override
    public int compareTo(Object obj) {

        LaboTestItemID other = (LaboTestItemID) obj;

        int ret = itemCodeID.compareTo(other.getItemCodeID());

        return ret == 0 ? itemCode.compareTo(other.getItemCode()) : ret;
    }

    @Override
    public String toString() {
        return itemName;
    }
}
