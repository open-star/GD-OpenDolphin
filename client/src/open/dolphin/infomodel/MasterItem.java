package open.dolphin.infomodel;

import java.util.List;
import java.util.Map;
import open.dolphin.utils.DateExpire;
import open.dolphin.utils.StringTool;

/**
 * ORCAマスターのデータのコピー
 *
 * Class to hold selected master item information.
 *
 * @author  Kazuhi Minagawa, Digital Globe, Inc.
 */
public class MasterItem implements java.io.Serializable {

    private static final long serialVersionUID = -6359300744722498857L;
    /** Claim subclass code マスタ項目の種別 */
    private int classCode; // 0: 手技  1: 材料  2: 薬剤 3: 用法
    private String name; // 項目名
    private String code; // 項目コード 
    private String masterTableId;  // コード体系名 
    private String number; // 数量 
    private String unit;   //単位 
    private String claimDiseaseCode;   // 医事用病名コード 
    private String claimClassCode;  // 診療行為区分(007)・点数集計先 
    private String ykzKbn; // 薬剤の場合の区分 内用1、外用6、注射薬4 
    private String dummy;
    private String bundleNumber;
    private List<String> sstKijunCdSet; //SST基準コード
    private String kana;
    private String startDate;
    private String endDate;
    private String disUseDate;
    private static final String ADMIN_MARK = "[用法] ";

    /**
     * 
     * ORCAマスター
     * Creates new MasterItem
     * @param classCode 
     * @param entry
     */
    public MasterItem(int classCode, MasterEntry entry) {
        setClassCode(classCode);
        setCode(entry.getCode());
        setName(entry.getName());
        setKana(entry.getKana());
        setStartDate(entry.getStartDate());
        setEndDate(entry.getEndDate());
        setDisUseDate(entry.getDisUseDate());
    }

    /**
     *
     * @param classCode
     * @param item
     */
    public MasterItem(int classCode, ClaimItem item) {
        setClassCode(classCode);
        setCode(item.getCode());
        setName(item.getName());
        setKana(item.getKana());
        setStartDate(item.getStartDate());
        setEndDate(item.getEndDate());
        setDisUseDate(item.getDisUseDate());
    }

    /**
     *
     * @param classCode
     * @param item
     */
    public MasterItem(int classCode, BundleMed item) {
        setClassCode(classCode);
        setCode(item.getAdminCode());
        setName(ADMIN_MARK + item.getAdmin());
        setDummy("X");
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * classCodeのSetter
     * @param classCode The classCode to set.
     */
    public void setClassCode(int classCode) {
        this.classCode = classCode;
    }

    /**
     * classCodeのGetter
     * @return Returns the classCode.
     */
    public int getClassCode() {
        return classCode;
    }

    /**
     * nameのSetter
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * nameのGetter
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * codeのSetter
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * codeのGetter
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }

    /**
     * masterTableIdのSetter
     * @param masterTableId The masterTableId to set.
     */
    public void setMasterTableId(String masterTableId) {
        this.masterTableId = masterTableId;
    }

    /**
     * masterTableIdのGetter
     * @return Returns the masterTableId.
     */
    public String getMasterTableId() {
        return masterTableId;
    }

    /**
     * numberのSetter
     * @param number The number to set.
     */
    public void setNumber(String number) {
        this.number = StringTool.zenkakuNumToHankaku(number.trim());
    }

    /**
     * numberのGetter
     * @return Returns the number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * unitのSetter
     * @param unit The unit to set.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * unitのGetter
     * @return Returns the unit.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * claimDiseaseCodeのSetter
     * @param claimDiseaseCode The claimDiseaseCode to set.
     */
    public void setClaimDiseaseCode(String claimDiseaseCode) {
        this.claimDiseaseCode = claimDiseaseCode;
    }

    /**
     * claimDiseaseCodeのGetter
     * @return Returns the claimDiseaseCode.
     */
    public String getClaimDiseaseCode() {
        return claimDiseaseCode;
    }

    /**
     * claimClassCodeのSetter
     * @param claimClassCode The claimClassCode to set.
     */
    public void setClaimClassCode(String claimClassCode) {
        this.claimClassCode = claimClassCode;
    }

    /**
     * claimClassCodeのGetter
     * @return Returns the claimClassCode.
     */
    public String getClaimClassCode() {
        return claimClassCode;
    }

    /**
     * ykzKbnのGetter
     * @return
     */
    public String getYkzKbn() {
        return ykzKbn;
    }

    /**
     * ykzKbnのSetter
     * @param ykzKbn
     */
    public void setYkzKbn(String ykzKbn) {
        this.ykzKbn = ykzKbn;
    }

    /**
     * dummyのGetter
     * @return
     */
    public String getDummy() {
        return dummy;
    }

    /**
     * dummyのSetter
     * @param dummy
     */
    public void setDummy(String dummy) {
        this.dummy = dummy;
    }

    /**
     * bundleNumberのGetter
     * @return
     */
    public String getBundleNumber() {
        return bundleNumber;
    }

    /**
     * bundleNumberのSetter
     * @param bundleNumber
     */
    public void setBundleNumber(String bundleNumber) {
        this.bundleNumber = bundleNumber;
    }

    /**
     * SST基準コードのGetter
     * @return SST基準コード
     */
    public List<String> getSstKijunCdSet() {
        return sstKijunCdSet;
    }

    /**
     * SST基準コードのSetter
     * @param ary SST基準コード
     */
    public void setSstKijunCdSet(List<String> ary) {
        this.sstKijunCdSet = ary;
    }

    /**
     * kanaのGetter
     * @return
     */
    public String getKana() {
        return kana;
    }

    /**
     * kanaのSetter
     * @param kana
     */
    private void setKana(String kana) {
        this.kana = kana;
    }

    /**
     * startDateのGetter
     * @return
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * startDateのSetter
     * @param startDate
     */
    private void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * endDateのGetter
     * @return
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * endDateのSetter
     * @param endDate
     */
    private void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * disUseDateのGetter
     * @return
     */
    public String getDisUseDate() {
        return disUseDate;
    }

    /**
     * disUseDateのSetter
     * @param disUseDate
     */
    private void setDisUseDate(String disUseDate) {
        this.disUseDate = disUseDate;
    }

    /**
     *
     * @return
     */
    protected boolean isExpired() {
        return DateExpire.expire(startDate, endDate);
    }

    /*
    public boolean isExpired(Date now) {
    return isExpired(now, new GenericAdapter<Object, Object>() {

    @Override
    public boolean onResult(Object a, Object b) throws Exception {
    return true;
    }

    @Override
    public void onError(Exception ex) {
    }
    });
    }

    public boolean isExpired(Date now, GenericAdapter adapter) {
    try {
    DateFormat format = new SimpleDateFormat("yyyyMMdd");
    String _endDate = getEndDate();
    if (!_endDate.equals("")) {
    if (!_endDate.equals("99999999")) {
    if (format.parse(_endDate).before(now)) {
    return true;
    }
    }
    }
    } catch (Exception ex) {
    adapter.onError(ex);
    }
    return false;
    }
     */
    /**
     *
     * @param resultSet
     * @param adapter
     * @return
     */
    public boolean isUpdateAvailable(Map<String, String> resultSet, GenericAdapter adapter) {
        try {
            adapter.onResult(getCode(), resultSet);
            if (resultSet != null) {
                if (!resultSet.isEmpty()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            adapter.onError(ex);
        }

        return false;
    }
}
