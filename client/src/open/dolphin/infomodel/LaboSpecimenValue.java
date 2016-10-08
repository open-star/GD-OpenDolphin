package open.dolphin.infomodel;
// Generated 2010/06/30 10:57:59 by Hibernate Tools 3.2.1.GA

import java.util.LinkedHashSet;
import java.util.Set;
import open.dolphin.queries.DolphinQuery;

/**
 * 検体情報 MEMO:マッピング d_labo_specimen
 * LaboSpecimenValue generated by hbm2java
 */
public class LaboSpecimenValue extends InfoModel {

    private long id;//MEMO:Refrection
    private LaboModuleValue laboModule;//MEMO:Refrection
    private String specimenName;//MEMO:Refrection
    private String specimenCode;//MEMO:Refrection
    private String specimenCodeId;//MEMO:Refrection
    private Set<LaboItemValue> laboItems;//MEMO:Refrection

    /**
     *
     */
    public LaboSpecimenValue() {
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
     * laboModuleのGetter
     * MEMO:Refrection
     * @return
     */
    public LaboModuleValue getLaboModule() {
        return laboModule;
    }

    /**
     * laboModuleのSetter
     * MEMO:Refrection
     * @param laboModule
     */
    public void setLaboModule(LaboModuleValue laboModule) {
        this.laboModule = laboModule;
    }

    /**
     * laboItemsのGetter
     * MEMO:Refrection
     * @return
     */
    public Set<LaboItemValue> getLaboItems() {
        return laboItems;
    }

    /**
     * laboItemsのSetter
     * MEMO:Refrection
     * @param laboItems
     */
    public void setLaboItems(Set<LaboItemValue> laboItems) {
        this.laboItems = laboItems;
    }

    /**
     * laboItemsのAdder
     * MEMO:Refrection
     * @param item
     */
    public void addLaboItem(LaboItemValue item) {
        if (laboItems == null) {
            laboItems = new LinkedHashSet<LaboItemValue>();
        }
        laboItems.add(item);
    }

    /**
     * specimenCodeのGetter
     * MEMO:Refrection
     * @return
     */
    public String getSpecimenCode() {
        return specimenCode;
    }

    /**
     * specimenCodeのSetter
     * MEMO:Refrection
     * @param specimenCode
     */
    public void setSpecimenCode(String specimenCode) {
        this.specimenCode = specimenCode;
    }

    /**
     * specimenCodeIdのGetter
     * MEMO:Refrection
     * @return
     */
    public String getSpecimenCodeId() {
        return specimenCodeId;
    }

    /**
     * specimenCodeIdのSetter
     * MEMO:Refrection
     * @param specimenCodeId
     */
    public void setSpecimenCodeId(String specimenCodeId) {
        this.specimenCodeId = specimenCodeId;
    }

    /**
     * specimenNameのGetter
     * MEMO:Refrection
     * @return
     */
    public String getSpecimenName() {
        return specimenName;
    }

    /**
     * specimenNameのSetter
     * MEMO:Refrection
     * @param specimenName
     */
    public void setSpecimenName(String specimenName) {
        this.specimenName = specimenName;
    }
}