package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import open.dolphin.queries.DolphinQuery;

/**
 * 画像診断データ MEMO:マッピング
 * d_radiology_method
 * RadiologyMethodValue generated by hbm2java
 */
public class RadiologyMethodValue extends InfoModel {

    private int id;//ID MEMO:Refrection
    private String hierarchyCode1;//MEMO:Refrection
    private String hierarchyCode2;//MEMO:Refrection
    private String methodName;//メソッド名 MEMO:Refrection

    /**
     * コンストラクタ
     */
    public RadiologyMethodValue() {
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
     * @return id
     */
    public int getId() {
        return this.id;
    }

    /**
     * idのSetter
     * MEMO:Refrection
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * hierarchyCode1のGetter
     * MEMO:Refrection
     * @return hierarchyCode1
     */
    public String getHierarchyCode1() {
        return this.hierarchyCode1;
    }

    /**
     * hierarchyCode1のSetter
     * MEMO:Refrection
     * @param hierarchyCode1
     */
    public void setHierarchyCode1(String hierarchyCode1) {
        this.hierarchyCode1 = hierarchyCode1;
    }

    /**
     * hierarchyCode2のGetter
     * MEMO:Refrection
     * @return hierarchyCode2
     */
    public String getHierarchyCode2() {
        return this.hierarchyCode2;
    }

    /**
     * hierarchyCode2のSetter
     * MEMO:Refrection
     * @param hierarchyCode2
     */
    public void setHierarchyCode2(String hierarchyCode2) {
        this.hierarchyCode2 = hierarchyCode2;
    }

    /**
     * methodNameのGetter
     * MEMO:Refrection
     * @return methodName
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * methodNameのSetter
     * MEMO:Refrection
     * @param methodName
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     *
     * @return methodName
     */
    public String toString() {
        return methodName;
    }

    /**
     *
     * @return Hash
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + id;
        return result;
    }

    /**
     *
     * @param obj
     * @return equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RadiologyMethodValue other = (RadiologyMethodValue) obj;
        if (getId() != other.getId()) {
            return false;
        }
        return true;
    }
}
