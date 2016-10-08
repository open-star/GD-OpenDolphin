/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.labotestimporter;

/**
 * 基準値情報一件を表現するクラス
 */
public class AverageInformation {

    private String normal;
    private String out;
    private String unitCode;
    private String unitCodeId;
    private String valueType;
    private String up;
    private String low;
    private String unit;

    /**
     *
     * @param valueType
     * @param up
     * @param low
     * @param unit
     */
    public AverageInformation(String valueType, String up, String low, String unit) {
        this.valueType = valueType.trim();
        this.up = up.trim();
        this.low = low.trim();
        this.unit = unit.trim();
    }

    /**
     *
     */
    AverageInformation() {
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
     * @param normal
     */
    public void setNormal(String normal) {
        this.normal = normal;
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
     * @param out
     */
    public void setOut(String out) {
        this.out = out;
    }

    /**
     *
     * @return
     */
    public String getUnitCode() {
        return unitCode;
    }

    /**
     *
     * @param unitCode
     */
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    /**
     *
     * @return
     */
    public String getUnitCodeId() {
        return unitCodeId;
    }

    /**
     *
     * @param unitCodeId
     */
    public void setUnitCodeId(String unitCodeId) {
        this.unitCodeId = unitCodeId;
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
     * @param up
     */
    public void setUp(String up) {
        if (up == null) {
            this.up = null;
        } else {
            this.up = up.trim();
        }
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
     * @param low
     */
    public void setLow(String low) {
        if (low == null) {
            this.low = null;
        } else {
            this.low = low.trim();
        }
    }

    /**
     *
     * @return
     */
    public String getUnit() {
        return unit;
    }

    /**
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit.trim();
    }

    /**
     *
     * @return
     */
    public String getValueType() {
        return valueType;
    }

    /**
     *
     * @param valueType
     */
    public void setValueType(String valueType) {
        this.valueType = valueType.trim();
    }
}
