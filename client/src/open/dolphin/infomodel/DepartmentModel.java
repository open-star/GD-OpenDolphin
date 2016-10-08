package open.dolphin.infomodel;

import javax.persistence.Embeddable;
import open.dolphin.queries.DolphinQuery;

/**
 * 科情報
 *
 * DepartmentModel MEMO:マッピング
 *
 * @author Minagawa,Kazushi
 *
 */
@Embeddable
public class DepartmentModel extends InfoModel {//id

    private static final long serialVersionUID = -920243869556556218L;
    private String department;
    private String departmentDesc;
    private String departmentCodeSys;

    /**
     * コンストラクタ
     */
    public DepartmentModel() {
    }

    /**
     * 検索 MEMO:何もしない
     * @param query 検索条件
     * @return false
     *
     */
    @Override
    public boolean search(DolphinQuery query) {
        return false;
    }

    /**
     * 科の種別（01...)のSetter
     * @param department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * 科の種別（01...)のGetter
     * @return Returns the departmentCode.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * 科の名前(内科...)のSetter
     * @param departmentDesc The departmentDesc to set.
     */
    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
    }

    /**
     * 科の名前(内科...)のGetter
     * @return Returns the departmentDesc.
     */
    public String getDepartmentDesc() {
        return departmentDesc;
    }

    /**
     *  コード体系"MML0028"固定？のSetter
     * @param departmentCodeSys "MML0028"
     */
    public void setDepartmentCodeSys(String departmentCodeSys) {
        this.departmentCodeSys = departmentCodeSys;
    }

    /**
     *  コード体系"MML0028"固定？のGetter
     * @return "MML0028"
     */
    public String getDepartmentCodeSys() {
        return departmentCodeSys;
    }

    /**
     * ToString
     * @return departmentDesc
     */
    public String toString() {
        return departmentDesc;
    }
}
