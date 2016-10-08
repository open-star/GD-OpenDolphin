package open.dolphin.infomodel;

/**
 * 傷病名
 * ORCAマスター MEMO:マッピング
 * DiseaseEntry
 * @author  Minagawa, Kazushi
 */
public final class DiseaseEntry extends MasterEntry {//id

    private static final long serialVersionUID = 9088599523647351403L;
    private String icdTen;

    /** 
     * Creates a new instance of DeseaseEntry
     */
    public DiseaseEntry() {
    }

    /**
     *
     * icdTenのGetter
     * @return icdTen
     */
    public String getIcdTen() {
        return icdTen;
    }

    /**
     *　フロム
     * @param from　フロム
     */
    public void copyFrom(DiseaseEntry from) {
        if (from != null) {
            this.setCode(from.getCode());
            this.setName(from.getName());
            this.setKana(from.getKana());
            this.setIcdTen(from.getIcdTen());
            this.setDisUseDate(from.getDisUseDate());
            this.setEndDate(from.getEndDate());
            this.setStartDate(from.getStartDate());
        }
    }

    /**
     *　フロム
     * @param from　フロム
     */
    public void swapEntry(DiseaseEntry from) {
        if (from != null) {
            DiseaseEntry temp = new DiseaseEntry();
            temp.copyFrom(this);
            this.copyFrom(from);
            from.copyFrom(temp);
        }
    }

    /**
     * icdTenのSetter
     * @param val icdTen
     */
    public void setIcdTen(String val) {
        icdTen = val;
    }

    /**
     *
     * @return　使用中
     */
    @Override
    public boolean isInUse() {
        if (disUseDate != null) {
            return refDate.compareTo(disUseDate) <= 0 ? true : false;
        }
        return false;
    }
}
