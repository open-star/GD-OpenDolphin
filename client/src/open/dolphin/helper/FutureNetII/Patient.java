package open.dolphin.helper.FutureNetII;

import java.util.ArrayList;
import java.util.List;

import open.dolphin.infomodel.PatientModel;
import open.dolphin.helper.FixedLengthFormat.Param;
import open.dolphin.helper.FixedLengthFormat.IFormat;

/**
 *
 * @author
 */
public class Patient implements IFormat {

    private static final int SIZE_PATIENT_ID = 12;
    private static final int SIZE_NAME_KANA = 40;
    private static final int SIZE_NAME_KANJI = 20;
    private static final int SIZE_SEX = 1;
    private static final int SIZE_BIRTHDAY = 8;
    private static final int SIZE_ZIP = 8;
    private static final int SIZE_ADDRESS = 40;
    private static final int SIZE_TEL = 12;
    private static final int SIZE_RESERVED = 371;

    private Param patientId;
    private Param nameKana;
    private Param nameKanji;
    private Param sex;
    private Param birthday;
    private Param zip;
    private Param address;
    private Param tel;
    private Param reserved;

    /**
     *
     */
    public Patient() {
        initialize();
    }

    /**
     *
     * @param patientModel
     */
    public Patient(PatientModel patientModel) {
        initialize();
        setPatientId(patientModel.getPatientId());
        setNameKana(patientModel.getKanaName());
        setNameKanji(patientModel.getFullName());
        setSex(patientModel.getGender().substring(0, SIZE_SEX).toUpperCase());
        setBirthday(patientModel.getBirthday().replaceAll("-", ""));
        setZip(patientModel.contactZipCode());
        setAddress(patientModel.contactAddress());
        setTel(patientModel.getTelephone());
    }

    private void initialize() {
        patientId = new Param(SIZE_PATIENT_ID);
        nameKana  = new Param(SIZE_NAME_KANA);
        nameKanji = new Param(SIZE_NAME_KANJI);
        sex       = new Param(SIZE_SEX);
        birthday  = new Param(SIZE_BIRTHDAY);
        zip       = new Param(SIZE_ZIP);
        address   = new Param(SIZE_ADDRESS);
        tel       = new Param(SIZE_TEL);
        reserved  = new Param(SIZE_RESERVED);
    }

    /**
     *
     * @return
     */
    public String getPatientId() {
        return patientId.getValue();
    }

    /**
     *
     * @param patientId
     */
    public void setPatientId(String patientId) {
        String value;
        int len = patientId.length();
        int rest = SIZE_PATIENT_ID - len;
        if (rest == 0) {
            value = patientId;
        } else if (rest > 0) {
            value = String.format("%0" + rest + "d", 0) + patientId;
        } else {
            value = patientId.substring(- rest, len);
        }
        this.patientId.setValue(value);
    }

    /**
     *
     * @return
     */
    public String getNameKana() {
        return nameKana.getValue();
    }

    /**
     *
     * @param nameKana
     */
    public void setNameKana(String nameKana) {
        this.nameKana.setValue(nameKana);
    }

    /**
     *
     * @return
     */
    public String getNameKanji() {
        return nameKanji.getValue();
    }

    /**
     *
     * @param nameKanji
     */
    public void setNameKanji(String nameKanji) {
        this.nameKanji.setValue(nameKanji);
    }

    /**
     *
     * @return
     */
    public String getSex() {
        return sex.getValue();
    }

    /**
     *
     * @param sex
     */
    public void setSex(String sex) {
        this.sex.setValue(sex);
    }

    /**
     *
     * @return
     */
    public String getBirthday() {
        return birthday.getValue();
    }

    /**
     *
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday.setValue(birthday);
    }

    /**
     * 
     * @return
     */
    public String getZip() {
        return zip.getValue();
    }

    /**
     *
     * @param zip
     */
    public void setZip(String zip) {
        this.zip.setValue(zip);
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address.getValue();
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address.setValue(address);
    }

    /**
     *
     * @return
     */
    public String getTel() {
        return tel.getValue();
    }

    /**
     *
     * @param tel
     */
    public void setTel(String tel) {
        this.tel.setValue(tel);
    }

    /**
     *
     * @return
     */
    public String getReserved() {
        return reserved.getValue();
    }

    public List getParam() {
        List<Param> arr = new ArrayList();
        arr.add(patientId);
        arr.add(nameKana);
        arr.add(nameKanji);
        arr.add(sex);
        arr.add(birthday);
        arr.add(zip);
        arr.add(address);
        arr.add(tel);
        arr.add(reserved);
        return arr;
    }
}
