package open.dolphin.infomodel;

/**
 * 相互作用マスタ MEMO:ORCAマスター tbl_sskijyo
 * @author
 */
public class SsKijyoEntry extends MasterEntry {

    private String Symptoms;
    private String Interact;
    private String SymptomsCode;

    /**
     *
     * @param SymptomsCode syojyoucd
     * @param Symptoms syojyou
     * @param Interact sayokijyo
     */
    public SsKijyoEntry(String SymptomsCode, String Symptoms, String Interact) {
        this.SymptomsCode = SymptomsCode;
        this.Symptoms = Symptoms;
        this.Interact = Interact;
    }

    /**
     *
     * @return
     */
    public String getSymptomsCode() {
        return SymptomsCode;
    }

    /**
     *
     * @return
     */
    public String getSymptoms() {
        return Symptoms;
    }

    /**
     *
     * @return
     */
    public String getInteract() {
        return Interact;
    }
}
