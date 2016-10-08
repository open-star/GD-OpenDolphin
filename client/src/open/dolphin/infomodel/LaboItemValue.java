package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import open.dolphin.queries.DolphinQuery;

/**
 * 検査データ MEMO:マッピング d_labo_item
 * LaboItemValue generated by hbm2java
 */
public class LaboItemValue extends InfoModel {

    private long id;//MEMO:Refrection
    private LaboSpecimenValue laboSpecimen;//MEMO:Refrection
    private String itemName;//MEMO:Refrection
    private String itemCode;//MEMO:Refrection
    private String itemCodeId;//MEMO:Refrection
    private String acode;//Analyte Code MEMO:Refrection
    private String icode;//Identification Code MEMO:Refrection
    private String scode;//Specimen Code MEMO:Refrection
    private String mcode;//Methodology Code MEMO:Refrection
    private String rcode;//Result Identifying Code MEMO:Refrection
    private String itemValue;//MEMO:Refrection
    private String up;//MEMO:Refrection
    private String low;//MEMO:Refrection
    private String normal;//MEMO:Refrection
    private String nout;//MEMO:Refrection
    private String unit;//MEMO:Refrection
    private String unitCode;//MEMO:Refrection
    private String unitCodeId;//MEMO:Refrection

    /**
     *
     */
    public LaboItemValue() {
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
     * idのGetter
     * MEMO:Refrection
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * idのSetter
     * MEMO:Refrection
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * laboSpecimenのGetter
     * MEMO:Refrection
     * @return
     */
    public LaboSpecimenValue getLaboSpecimen() {
        return laboSpecimen;
    }

    /**
     * laboSpecimenのSetter
     * MEMO:Refrection
     * @param laboSpecimen
     */
    public void setLaboSpecimen(LaboSpecimenValue laboSpecimen) {
        this.laboSpecimen = laboSpecimen;
    }

    /**
     * Analyte CodeのGetter
     * MEMO:Refrection
     * @return Analyte Code
     */
    public String getAcode() {
        return acode;
    }

    /**
     * Analyte CodeのSetter
     * MEMO:Refrection
     * @param acode Analyte Code
     */
    public void setAcode(String acode) {
        this.acode = acode;
    }

    /**
     * Identification CodeのGetter
     * MEMO:Refrection
     * @return Identification Code
     */
    public String getIcode() {
        return icode;
    }

    /**
     * Identification CodeのSetter
     * MEMO:Refrection
     * @param icode Identification Code
     */
    public void setIcode(String icode) {
        this.icode = icode;
    }

    /**
     * itemCodeのGetter
     * MEMO:Refrection
     * @return
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * itemCodeのSetter
     * MEMO:Refrection
     * @param itemCode itemCode
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * itemCodeIdのGetter
     * MEMO:Refrection
     * @return
     */
    public String getItemCodeId() {
        return itemCodeId;
    }

    /**
     * itemCodeIdのSetter
     * MEMO:Refrection
     * @param itemCodeId itemCodeId
     */
    public void setItemCodeId(String itemCodeId) {
        this.itemCodeId = itemCodeId;
    }

    /**
     * itemNameのGetter
     * MEMO:Refrection
     * @return
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * itemNameのSetter
     * MEMO:Refrection
     * @param itemName itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * itemValueのGetter
     * MEMO:Refrection
     * @return
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     * itemValueのSetter
     * MEMO:Refrection
     * @param itemValue itemValue
     */
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    /**
     * lowのGetter
     * MEMO:Refrection
     * @return
     */
    public String getLow() {
        return low;
    }

    /**
     * lowのSetter
     * MEMO:Refrection
     * @param low low
     */
    public void setLow(String low) {
        this.low = low;
    }

    /**
     * Methodology CodeのGetter
     * MEMO:Refrection
     * @return Methodology Code
     */
    public String getMcode() {
        return mcode;
    }

    /**
     * Methodology CodeのSetter
     * MEMO:Refrection
     * @param mcode Methodology Code
     */
    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    /**
     * normalのGetter
     * MEMO:Refrection
     * @return
     */
    public String getNormal() {
        return normal;
    }

    /**
     * normalのSetter
     * MEMO:Refrection
     * @param normal normal
     */
    public void setNormal(String normal) {
        this.normal = normal;
    }

    /**
     * noutのGetter
     * MEMO:Refrection
     * @return
     */
    public String getNout() {
        return nout;
    }

    /**
     * noutのSetter
     * MEMO:Refrection
     * @param nout nout
     */
    public void setNout(String nout) {
        this.nout = nout;
    }

    /**
     * Result Identifying CodeのGetter
     * MEMO:Refrection
     * @return Result Identifying Code
     */
    public String getRcode() {
        return rcode;
    }

    /**
     * Result Identifying CodeのSetter
     * MEMO:Refrection
     * @param rcode Result Identifying Code
     */
    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    /**
     * Specimen CodeのGetter
     * MEMO:Refrection
     * @return Specimen Code
     */
    public String getScode() {
        return scode;
    }

    /**
     * Specimen CodeのSetter
     * MEMO:Refrection
     * @param scode Specimen Code
     */
    public void setScode(String scode) {
        this.scode = scode;
    }

    /**
     * unitのGetter
     * MEMO:Refrection
     * @return
     */
    public String getUnit() {
        return unit;
    }

    /**
     * unitのSetter
     * MEMO:Refrection
     * @param unit unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * unitCodeのGetter
     * MEMO:Refrection
     * @return
     */
    public String getUnitCode() {
        return unitCode;
    }

    /**
     * unitCodeのSetter
     * MEMO:Refrection
     * @param unitCode unitCode
     */
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    /**
     * unitCodeIdのGetter
     * MEMO:Refrection
     * @return
     */
    public String getUnitCodeId() {
        return unitCodeId;
    }

    /**
     * unitCodeIdのSetter
     * MEMO:Refrection
     * @param unitCodeId unitCodeId
     */
    public void setUnitCodeId(String unitCodeId) {
        this.unitCodeId = unitCodeId;
    }

    /**
     * upのGetter
     * MEMO:Refrection
     * @return
     */
    public String getUp() {
        return up;
    }

    /**
     * upのSetter
     * MEMO:Refrection
     * @param up up
     */
    public void setUp(String up) {
        this.up = up;
    }
}
