package open.dolphin.client;

import open.dolphin.client.karte.template.Template;
import open.dolphin.infomodel.PVTHealthInsuranceModel;

/**
 * NewKarteParams
 *
 * @author  Kazushi Minagawa
 */
public final class NewKarteParams {

    private IChart.NewKarteOption option;    // ベースのカルテがあるかどうか、タブ及びEditorFrameの別、修正かどうか
    private IChart.NewKarteMode createMode;    // 空白、全コピー、前回処方適用のフラグ
    private String department;    // 診療科
    private String departmentCode;    // 診療科コード
    private Object[] insurances;    // 健康保険
    private int initialSelectedInsurance;    // 初期化時に選択する保険
    private PVTHealthInsuranceModel insurance;    // ダイアログでユーザが選択した保険
    private boolean openFrame;    // EditorFrame で編集するかどうかのフラグ 
    private String docType;    // 生成するドキュメントの種類    // 2号カルテ、シングル、紹介状等
    private String groupId;    // 不明
    private String confirmDate;    // 診療日
    private boolean isHospital;    //入院
    private Template selectedTemplate;

    /**
     * Creates a new instance of NewKarteParams
     * @param option
     */
    public NewKarteParams(IChart.NewKarteOption option) {
        this.option = option;

        // MEMO: テンプレートを使用しないときは null にしておく
        selectedTemplate = null;
    }

    /**
     *
     * @return
     */
    public IChart.NewKarteOption getOption() {
        return option;
    }

    /**
     *
     * @return
     */
    public String getDocType() {
        return docType;
    }

    /**
     *
     * @param docType
     * @return
     */
    public String setDocType(String docType) {
        return this.docType = docType;
    }

    /**
     *
     * @return
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     *
     * @param val
     */
    public void setGroupId(String val) {
        groupId = val;
    }

    /**
     *
     * @return
     */
    public String getDepartment() {
        return department;
    }

    /**
     *
     * @param val
     */
    public void setDepartment(String val) {
        department = val;
    }

    /**
     *
     * @return
     */
    public String getDepartmentCode() {
        return departmentCode;
    }

    /**
     *
     * @param departmentCode
     */
    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    /**
     *
     * @return
     */
    public Object[] getInsurances() {
        return insurances;
    }

    /**
     *
     * @param ins
     */
    public void setInsurances(Object[] ins) {
        insurances = ins;
    }

    /**
     *
     * @return
     */
    public PVTHealthInsuranceModel getPVTHealthInsurance() {
        return insurance;
    }

    /**
     *
     * @param val
     */
    public void setPVTHealthInsurance(PVTHealthInsuranceModel val) {
        insurance = val;
    }

    /**
     *
     * @param openFrame
     */
    public void setOpenFrame(boolean openFrame) {
        this.openFrame = openFrame;
    }

    /**
     *
     * @return
     */
    public boolean isOpenFrame() {
        return openFrame;
    }

    /**
     *
     * @return
     */
    public IChart.NewKarteMode getCreateMode() {
        return createMode;
    }

    /**
     *
     * @param createMode
     */
    public void setCreateMode(IChart.NewKarteMode createMode) {
        this.createMode = createMode;
    }

    /**
     *
     * @return
     */
    public int getInitialSelectedInsurance() {
        return initialSelectedInsurance;
    }

    /**
     *
     * @param index
     */
    public void setInitialSelectedInsurance(int index) {
        initialSelectedInsurance = index;
    }

    /**
     *
     * @return
     */
    public String getConfirmDate() {
        return confirmDate;
    }

    /**
     *
     * @param s
     */
    public void setConfirmDate(String s) {
        confirmDate = s;
    }

    /**
     *
     * @return
     */
    public boolean isHospital() {
        return isHospital;
    }

    /**
     *
     * @param s
     */
    public void setHospital(boolean s) {
        isHospital = s;
    }

    /**
     *
     * @return
     */
    public Template getSelectedTemplate() {
        return selectedTemplate;
    }

    /**
     *
     * @param template
     */
    public void setSelectedTemplate(Template template) {
        selectedTemplate = template;
    }
}
