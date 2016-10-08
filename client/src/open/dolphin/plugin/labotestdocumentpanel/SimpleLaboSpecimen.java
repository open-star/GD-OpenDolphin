/*
 * SimpleLaboSpecimen.java
 *
 * Created on 2003/07/29, 21:12
 */
package open.dolphin.plugin.labotestdocumentpanel;

/**
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class SimpleLaboSpecimen implements Comparable {

    private String specimenName;
    private String specimenCode;
    private String specimenCodeID;
    private String specimenMemo;
    private String specimenMemoCodeName;
    private String specimenMemoCode;
    private String specimenMemoCodeId;
    private String specimenFreeMemo;

    /**
     * Creates a new instance of SimpleLaboSpecimen
     */
    public SimpleLaboSpecimen() {
    }

    /**
     *
     * @param name
     * @param code
     * @param codeID
     */
    public SimpleLaboSpecimen(String name, String code, String codeID) {
        this();

        this.specimenName = name;
        this.specimenCode = code;
        this.specimenCodeID = codeID;
    }

    /**
     *
     * @return
     */
    public String getSpecimenName() {
        return specimenName;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenName(String val) {
        specimenName = val;
    }

    /**
     *
     * @return
     */
    public String getSpecimenCode() {
        return specimenCode;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenCode(String val) {
        specimenCode = val;
    }

    /**
     *
     * @return
     */
    public String getSpecimenCodeID() {
        return specimenCodeID;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenCodeID(String val) {
        specimenCodeID = val;
    }

    /**
     *
     * @return
     */
    public String getSpecimenMemo() {
        return specimenMemo;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenMemo(String val) {
        specimenMemo = val;
    }

    /**
     *
     * @return
     */
    public String getSpecimenMemoCodeName() {
        return specimenMemoCodeName;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenMemoCodeName(String val) {
        specimenMemoCodeName = val;
    }

    /**
     *
     * @return
     */
    public String getSpecimenMemoCode() {
        return specimenMemoCode;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenMemoCode(String val) {
        specimenMemoCode = val;
    }

    /**
     *
     * @return
     */
    public String getSpecimenMemoCodeId() {
        return specimenMemoCodeId;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenMemoCodeId(String val) {
        specimenMemoCodeId = val;
    }

    /**
     *
     * @return
     */
    public String getSpecimenFreeMemo() {
        return specimenFreeMemo;
    }

    /**
     *
     * @param val
     */
    public void setSpecimenFreeMemo(String val) {
        specimenFreeMemo = val;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return specimenName;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {

        return specimenCodeID.hashCode() + specimenCode.hashCode();
    }

    /**
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {

        if (other != null && getClass() == other.getClass()) {

            SimpleLaboSpecimen sp = (SimpleLaboSpecimen) other;

            return (specimenCodeID.equals(sp.getSpecimenCodeID())
                    && specimenCode.equals(sp.getSpecimenCode())) ? true : false;
        }

        return false;
    }

    /**
     * 
     * @param obj
     * @return
     */
    @Override
    public int compareTo(Object obj) {

        SimpleLaboSpecimen other = (SimpleLaboSpecimen) obj;

        // コード体系を比較
        int ret = specimenCodeID.compareTo(other.getSpecimenCodeID());

        // コード体系が等しい場合はコードを比較
        return ret == 0 ? specimenCode.compareTo(other.getSpecimenCode()) : ret;
    }
}
