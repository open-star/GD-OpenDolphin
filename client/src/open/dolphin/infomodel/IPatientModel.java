/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.infomodel;

/**
 *
 * @author
 */
public interface IPatientModel {

    /**
     * 生年月日を返す。
     * @return 生年月日 yyyy-MM-dd
     */
    String getBirthday();

    /**
     * 性別を返す。
     * @return 性別
     */
    String getGender();

    /**
     * 性別説明体系を返す。
     * @return 性別説明体系
     */
    String getGenderCodeSys();

    /**
     * 性別説明を返す。
     * @return 性別説明
     */
    String getGenderDesc();

     /**
     * フルネームを返す。
      * @return フルネーム
     */
    String getFullName();

    /**
     * 患者IDを返す。
     * @return 患者ID
     */
    String getPatientId();

    /**
     * 生年月日を設定する。
     * @param birthday 生年月日 yyyy-MM-dd
     */
    void setBirthday(String birthday);

    /**
     * 性別を設定する。
     * @param gender 性別
     */
    void setGender(String gender);

    /**
     * 性別説明体系を設定する。
     * @param genderCodeSys 性別説明体系
     */
    void setGenderCodeSys(String genderCodeSys);

    /**
     * 性別説明を設定する。
     * @param genderDesc 性別説明
     */
    void setGenderDesc(String genderDesc);

    /**
     * フルネームを設定する。
     * @param name フルネーム
     */
    void setFullName(String name);

    /**
     * 患者IDを設定する。
     * @param patientId 患者ID
     */
    void setPatientId(String patientId);

}
