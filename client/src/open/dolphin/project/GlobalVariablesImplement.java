package open.dolphin.project;

import java.io.OutputStream;
import java.util.prefs.Preferences;
import javax.security.auth.Subject;
import open.dolphin.infomodel.UserModel;
import open.dolphin.utils.CombinedStringParser;
import open.dolphin.log.LogWriter;
import open.dolphin.security.EncryptUtil;

/**
 * プロジェクト情報管理クラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class GlobalVariablesImplement implements java.io.Serializable {

    private Preferences prefs;
//    private boolean valid;
    private DolphinPrincipal principal;
    private UserModel userModel;
    private Subject subject;
    // Preferences のノード名
    private final String NODE_NAME = "/open/dolphin/project";
    // デフォルトのプロジェクト名
    private final String DEFAULT_PROJECT_NAME = "OpenDolphin";
    // User ID
    private final String DEFAULT_USER_ID = null;
    private final String DEFAULT_FACILITY_ID = null;
    // Server
    private final String DEFAULT_DB_ADDRESS = "localhost";
    private final int JBOSS_J2EE_PORT = 1099;
    // Claim
    private final boolean DEFAULT_SEND_CLAIM = true;
    private final boolean DEFAULT_SEND_DIAGNOSIS = true;
    private final String DEFAULT_CLAIM_HOST_NAME = "日医標準レセプト(ORCA)";
    private final String DEFAULT_CLAIM_ADDRESS = null;
    private final int DEFAULT_CLAIM_PORT = 8210;
    private final String DEFAULT_CLAIM_ENCODING = "UTF-8";
    private final boolean DEFAULT_USE_AS_PVTSERVER = true;
    private int DEFAULT_ORCA_CHARACTER_CODE = 0;
    // MML
    private final boolean DEFAULT_SEND_MML = false;
    private final String DEFAULT_MML_VERSION = "2.3";
    private final String DEFAULT_MML_ENCODING = "UTF-8";
    private final String DEFAULT_SEND_MML_ADDRESS = null;
    private final String DEFAULT_SEND_MML_DIRECTORY = null;
    // Update
    private final boolean DEFAULT_USE_PROXY = false;
    private final String DEFAULT_PROXY_HOST = null;
    private final int DEFAULT_PROXY_PORT = 8080;
    private final long DEFAULT_LAST_MODIFIED = 0L;
    private final String DEFAULT_DIRECTIONS = "なし,なし,なし,なし,なし,なし,なし,なし,なし,なし,なし,なし,なし,なし";
    // RS_Base
    private final String DEFAULT_ADDRESS = "localhost";// デフォルトのIPアドレス
    private final String DEFAULT_FOLDER = "tmp";// デフォルトのフォルダー
    private final String DEFAULT_USER_NAME = "Administrator";
    private final String DEFAULT_PASSWORD = "";

    /** ProjectStub を生成する。 */
    public GlobalVariablesImplement() {
        //Windows  HKEY_CURRENT_USER\Software\JavaSoft\Prefs\open
        //Mac      com.apple.java.util.prefs in users /Library/Preferences/
        prefs = Preferences.userRoot().node(NODE_NAME);
    }

    /**
     * Preferencesを返す。
     * @return 設定ノード
     */
    public Preferences getPreferences() {
        return prefs;
    }

    /**
     * 設定ファイルが有効かどうかを返す。
     * @return 有効な時 true
     */
    public boolean isValid() {

        // UserTypeを判定する
        if (getUserType().equals(GlobalVariables.UserType.UNKNOWN)) {
            return false;
        }

        // UserIdとFacilityIdを確認する
        if (getUserId() == null || getFacilityId() == null) {
            return false;
        }

        // MML送信を行う場合の確認をする
        if (getSendMML() && (getUploaderIPAddress() == null || getUploadShareDirectory() == null)) {
            return false;
        }

        return true;
    }

    /**
     *
     * @return
     */
    public DolphinPrincipal getDolphinPrincipal() {
        return principal;
    }

    /**
     *
     * @param principal
     */
    public void setDolphinPrincipal(DolphinPrincipal principal) {
        this.principal = principal;
    }

    /**
     * プロジェクト名を返す。
     * @return プロジェクト名 (Dolphin ASP, HOT, MAIKO, HANIWA ... etc)
     */
    public String getName() {
        return prefs.get(GlobalVariables.PROJECT_NAME, DEFAULT_PROJECT_NAME);
    }

    /**
     * プロジェクト名を返す。
     * @param projectName
     */
    public void setName(String projectName) {
        prefs.put(GlobalVariables.PROJECT_NAME, projectName);
    }

    /**
     * ログインユーザ情報を返す。
     * @return Dolphinサーバに登録されているユーザ情報
     */
    public UserModel getUserModel() {
        return userModel;
    }

    /**
     * ログインユーザ情報を設定する。
     * @param userModel ログイン時にDolphinサーバから取得した情報
     */
    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    /**
     * ログイン画面用のUserIDを返す。
     * @return ログイン画面に表示するUserId
     */
    public String getUserId() {
        if (GlobalSettings.isTrial()) {
            return "dolphintrial";
        } else {
            return prefs.get(GlobalVariables.USER_ID, DEFAULT_USER_ID);
        }

    }

    /**
     * ログイン画面用のUserIDを設定する。
     * @param val
     */
    public void setUserId(String val) {
        prefs.put(GlobalVariables.USER_ID, val);
    }

    /**
     * ログイン画面用のFacilityIDを返す。
     * @return ログイン画面に表示するFacilityID
     */
    public String getFacilityId() {
        if (GlobalSettings.isTrial()) {
            return "1.3.6.1.4.1.9414.10.1";
        } else {
            return prefs.get(GlobalVariables.FACILITY_ID, DEFAULT_FACILITY_ID);
        }
    }

    /**
     * ログイン画面用のFacilityIDを設定する。
     * @param val
     */
    public void setFacilityId(String val) {
        prefs.put(GlobalVariables.FACILITY_ID, val);
    }

    /**
     * ORCA バージョンを返す。
     * @return ORCA バージョン
     */
    public String getOrcaVersion() {
        if (GlobalSettings.isTrial()) {
            return "40";
        } else {
            return prefs.get("orcaVersion", "40");
        }
    }

    /**
     * ORCA バージョンを設定する。
     * @param version
     */
    public void setOrcaVersion(String version) {
        prefs.put("orcaVersion", version);
    }

    /**
     * JMARICode を返す。
     * @return JMARI Code
     */
    public String getJMARICode() {
        if (GlobalSettings.isTrial()) {
            return "JPN123456789101";
        } else {
            return prefs.get("jmariCode", "JPN000000000000");
        }
    }

    /**
     * JMARICode を設定する。
     * @param jamriCode
     */
    public void setJMARICode(String jamriCode) {
        prefs.put("jmariCode", jamriCode);
    }

    // UserType
    /**
     *
     * @return
     */
    public GlobalVariables.UserType getUserType() {

        if (GlobalSettings.isTrial()) {
            return GlobalVariables.UserType.FACILITY_USER;
        } else {
            String userType = prefs.get(GlobalVariables.USER_TYPE, GlobalVariables.UserType.UNKNOWN.toString());
            return GlobalVariables.UserType.valueOf(userType);
        }

        // Preference 情報がない場合は　UNKNOWN を返す
        // これは Project.isValid() で必ずテストされる

    }

    /**
     *
     * @param userType
     */
    public void setUserType(GlobalVariables.UserType userType) {
        prefs.put(GlobalVariables.USER_TYPE, userType.toString());
    }

    /**
     *
     * @return
     */
    public String getDbAddress() {
        if (GlobalSettings.isTrial()) {
            return "dolphin.good-day.co.jp";
        } else {
            return prefs.get(GlobalVariables.DB_ADDRESS, DEFAULT_DB_ADDRESS);
        }
    }

    /**
     *
     * @param val
     */
    public void setDbAddress(String val) {
        //  if (GlobalSettings.isTrial()) {
        //      prefs.put(GlobalVariables.DB_ADDRESS, "dolphin.good-day.co.jp");
        //  } else {
        prefs.put(GlobalVariables.DB_ADDRESS, val);
        //  }
    }

    private byte[] getDbPassword() {
        if (GlobalSettings.isTrial()) {
            return EncryptUtil.encryptWithBlowfish("UQ6nRcOa");
            //       return EncryptUtil.encryptWithBlowfish("onas+tro");
        } else {
            return prefs.getByteArray(GlobalVariables.DB_PASSWORD, null);
        }
    }

    /**
     *
     * @param pass
     */
    public void setDbPassword(String pass) {
        //     if (GlobalSettings.isTrial()) {
        //       prefs.putByteArray(GlobalVariables.DB_PASSWORD, EncryptUtil.encryptWithBlowfish("UQ6nRcOa"));
        //   prefs.putByteArray(GlobalVariables.DB_PASSWORD, EncryptUtil.encryptWithBlowfish("onas+tro"));
        //   } else {
        if (pass.equals("")) {
            return;
        }
        prefs.putByteArray(GlobalVariables.DB_PASSWORD, EncryptUtil.encryptWithBlowfish(pass));
        //     }
    }

    /**
     *
     * @return
     */
    public String getDbPasswordWithoutHash() {

        if (GlobalSettings.isTrial()) {
            return "UQ6nRcOa";
        } else {
            byte[] encryptedPass = getDbPassword();

            if (encryptedPass == null) {
                return "";
            }
            return EncryptUtil.decodeWithBlowfish(encryptedPass);
        }
    }

    /**
     *
     * @return
     */
    public boolean getDbSSLState() {

        if (GlobalSettings.isTrial()) {
            return false;
        } else {
            return prefs.getBoolean(GlobalVariables.DB_SSLSTATE, false);
        }
    }

    /**
     *
     * @param state
     */
    public void setDbSSLState(boolean state) {
        prefs.putBoolean(GlobalVariables.DB_SSLSTATE, state);
    }

    /**
     *
     * @return
     */
    public int getHostPort() {

        if (GlobalSettings.isTrial()) {
            return 1099;
        } else {
            return prefs.getInt(GlobalVariables.HOST_PORT, JBOSS_J2EE_PORT);
        }
    }

    /**
     *
     * @param val
     */
    public void setHostPort(int val) {
        prefs.putInt(GlobalVariables.HOST_PORT, val);
    }

    /**
     *
     * @return
     */
    public String getPDFStore() {
        String defaultStore = GlobalConstants.getPDFDirectory();
        return prefs.get("pdfStore", defaultStore);
    }

    /**
     *
     * @param pdfStore
     */
    public void setPDFStore(String pdfStore) {
        prefs.put("pdfStore", pdfStore);
    }

    /**
     *
     * @return
     */
    public int getFetchKarteCount() {
        return prefs.getInt(GlobalVariables.DOC_HISTORY_FETCHCOUNT, 1);
    }

    /**
     *
     * @param cnt
     */
    public void setFetchKarteCount(int cnt) {
        prefs.putInt(GlobalVariables.DOC_HISTORY_FETCHCOUNT, cnt);
    }

    /**
     *
     * @return
     */
    public boolean getScrollKarteV() {
        return prefs.getBoolean(GlobalVariables.KARTE_SCROLL_DIRECTION, true);
    }

    /**
     *
     * @param b
     */
    public void setScrollKarteV(boolean b) {
        prefs.putBoolean(GlobalVariables.KARTE_SCROLL_DIRECTION, b);
    }

    /**
     *
     * @return
     */
    public boolean getAscendingKarte() {
        return prefs.getBoolean(GlobalVariables.DOC_HISTORY_ASCENDING, false);
    }

    /**
     *
     * @param b
     */
    public void setAscendingKarte(boolean b) {
        prefs.putBoolean(GlobalVariables.DOC_HISTORY_ASCENDING, b);
    }

    /**
     *
     * @return
     */
    public int getKarteExtractionPeriod() {
        return prefs.getInt(GlobalVariables.DOC_HISTORY_PERIOD, -12);
    }

    /**
     *
     * @param period
     */
    public void setKarteExtractionPeriod(int period) {
        prefs.putInt(GlobalVariables.DOC_HISTORY_PERIOD, period);
    }

    /**
     *
     * @return
     */
    public boolean getShowModifiedKarte() {
        return prefs.getBoolean(GlobalVariables.DOC_HISTORY_SHOWMODIFIED, false);
    }

    /**
     *
     * @param b
     */
    public void setShowModifiedKarte(boolean b) {
        prefs.putBoolean(GlobalVariables.DOC_HISTORY_SHOWMODIFIED, b);
    }

    /**
     *
     * @return
     */
    public boolean getShowUnsendKarte() {
        return prefs.getBoolean(GlobalVariables.DOC_HISTORY_SHOWUNSEND, false);
    }

    /**
     *
     * @param b
     */
    public void setShowUnsendKarte(boolean b) {
        prefs.putBoolean(GlobalVariables.DOC_HISTORY_SHOWUNSEND, b);
    }

    /**
     *
     * @return
     */
    public boolean getShowSendKarte() {
        return prefs.getBoolean(GlobalVariables.DOC_HISTORY_SHOWSEND, false);
    }

    /**
     *
     * @param b
     */
    public void setShowSendKarte(boolean b) {
        prefs.putBoolean(GlobalVariables.DOC_HISTORY_SHOWSEND, b);
    }

    /**
     *
     * @return
     */
    public boolean getShowNewestKarte() {
        return prefs.getBoolean(GlobalVariables.DOC_HISTORY_SHOWNEWEST, false);
    }

    /**
     *
     * @param b
     */
    public void setShowNewestKarte(boolean b) {
        prefs.putBoolean(GlobalVariables.DOC_HISTORY_SHOWNEWEST, b);
    }

    /**
     *
     * @return
     */
    public boolean getAscendingDiagnosis() {
        return prefs.getBoolean(GlobalVariables.DIAGNOSIS_ASCENDING, false);
    }

    /**
     *
     * @param b
     */
    public void setAscendingDiagnosis(boolean b) {
        prefs.putBoolean(GlobalVariables.DIAGNOSIS_ASCENDING, b);
    }

    /**
     *
     * @return
     */
    public int getDiagnosisExtractionPeriod() {
        return prefs.getInt(GlobalVariables.DIAGNOSIS_PERIOD, 0);
    }

    /**
     *
     * @param period
     */
    public void setDiagnosisExtractionPeriod(int period) {
        prefs.putInt(GlobalVariables.DIAGNOSIS_PERIOD, period);
    }

    /**
     *
     * @param code
     */
    public void setCharacterCode(int code) {
        prefs.putInt(GlobalVariables.ORCA_CHARACTER_CODE, code);
    }

    /**
     *
     * @return
     */
    public int getCharacterCode() {

        if (GlobalSettings.isTrial()) {
            return 0;
        } else {
            return prefs.getInt(GlobalVariables.ORCA_CHARACTER_CODE, DEFAULT_ORCA_CHARACTER_CODE);
        }
    }

    /**
     *
     * @return
     */
    public boolean isAutoOutcomeInput() {
        return prefs.getBoolean("autoOutcomeInput", false);
    }

    /**
     *
     * @param b
     */
    public void setAutoOutcomeInput(boolean b) {
        prefs.putBoolean("autoOutcomeInput", b);
    }

    /**
     *
     * @return
     */
    public boolean isReplaceStamp() {
        return prefs.getBoolean("replaceStamp", false);
    }

    /**
     *
     * @param b
     */
    public void setReplaceStamp(boolean b) {
        prefs.putBoolean("replaceStamp", b);
    }

    /**
     *
     * @return
     */
    public boolean isStampSpace() {
        return prefs.getBoolean("stampSpace", true);
    }

    /**
     *
     * @param b
     */
    public void setStampSpace(boolean b) {
        prefs.putBoolean("stampSpace", b);
    }

    /**
     *
     * @return
     */
    public boolean isLaboFold() {
        return prefs.getBoolean("laboFold", true);
    }

    /**
     *
     * @param b
     */
    public void setLaboFold(boolean b) {
        prefs.putBoolean("laboFold", b);
    }

    /**
     *
     * @return
     */
    public String getDefaultZyozaiNum() {
        return prefs.get("defaultZyozaiNum", "3");
    }

    /**
     *
     * @param defaultZyozaiNum
     */
    public void setDefaultZyozaiNum(String defaultZyozaiNum) {
        prefs.put("defaultZyozaiNum", defaultZyozaiNum);
    }

    /**
     *
     * @return
     */
    public String getDefaultMizuyakuNum() {
        return prefs.get("defaultMizuyakuNum", "1");
    }

    /**
     *
     * @param defaultMizuyakuNum
     */
    public void setDefaultMizuyakuNum(String defaultMizuyakuNum) {
        prefs.put("defaultMizuyakuNum", defaultMizuyakuNum);
    }

    /**
     *
     * @return
     */
    public String getDefaultSanyakuNum() {
        return prefs.get("defaultSanyakuNum", "1.0");
    }

    /**
     *
     * @param defaultSanyakuNum
     */
    public void setDefaultSanyakuNum(String defaultSanyakuNum) {
        prefs.put("defaultSanyakuNum", defaultSanyakuNum);
    }

    /**
     *
     * @return
     */
    public String getDefaultRpNum() {
        return prefs.get("defaultRpNum", "3");
    }

    /**
     *
     * @param defaultRpNum
     */
    public void setDefaultRpNum(String defaultRpNum) {
        prefs.put("defaultRpNum", defaultRpNum);
    }

    /**
     *
     * @return
     */
    public int getLaboTestExtractionPeriod() {
        return prefs.getInt(GlobalVariables.LABOTEST_PERIOD, -6);
    }

    /**
     *
     * @param period
     */
    public void setLaboTestExtractionPeriod(int period) {
        prefs.putInt(GlobalVariables.LABOTEST_PERIOD, period);
    }

    /**
     *
     * @return
     */
    public boolean getConfirmAtNew() {
        return prefs.getBoolean(GlobalVariables.KARTE_SHOW_CONFIRM_AT_NEW, true);
    }

    /**
     *
     * @param b
     */
    public void setConfirmAtNew(boolean b) {
        prefs.putBoolean(GlobalVariables.KARTE_SHOW_CONFIRM_AT_NEW, b);
    }

    /**
     *
     * @return
     */
    public int getCreateKarteMode() {
        return prefs.getInt(GlobalVariables.KARTE_CREATE_MODE, 0); // 0=emptyNew, 1=applyRp, 2=copyNew
    }

    /**
     *
     * @param mode
     */
    public void setCreateKarteMode(int mode) {
        prefs.putInt(GlobalVariables.KARTE_CREATE_MODE, mode);
    }

    /**
     *
     * @return
     */
    public boolean getPlaceKarteMode() {
        return prefs.getBoolean(GlobalVariables.KARTE_PLACE_MODE, true);
    }

    /**
     *
     * @param mode
     */
    public void setPlaceKarteMode(boolean mode) {
        prefs.putBoolean(GlobalVariables.KARTE_PLACE_MODE, mode);
    }

    /**
     *
     * @return
     */
    public boolean getConfirmAtSave() {
        return prefs.getBoolean(GlobalVariables.KARTE_SHOW_CONFIRM_AT_SAVE, true);
    }

    /**
     *
     * @param b
     */
    public void setConfirmAtSave(boolean b) {
        prefs.putBoolean(GlobalVariables.KARTE_SHOW_CONFIRM_AT_SAVE, b);
    }

    /**
     *
     * @return
     */
    public int getPrintKarteCount() {
        return prefs.getInt(GlobalVariables.KARTE_PRINT_COUNT, 0);
    }

    /**
     *
     * @param cnt
     */
    public void setPrintKarteCount(int cnt) {
        prefs.putInt(GlobalVariables.KARTE_PRINT_COUNT, cnt);
    }

    /**
     *
     * @return
     */
    public int getSaveKarteMode() {
        return prefs.getInt(GlobalVariables.KARTE_SAVE_ACTION, 0); // 0=save 1=saveTmp
    }

    /**
     *
     * @param mode
     */
    public void setSaveKarteMode(int mode) {
        prefs.putInt(GlobalVariables.KARTE_SAVE_ACTION, mode); // 0=save 1=saveTmp
    }

    // CLAIM関連情報
    /**
     * CLAIM 送信全体への設定を返す。
     * デフォルトが false になっているのは新規インストールの場合で ORCA 接続なしで
     * 使えるようにするため。
     * @return
     */
    public boolean getSendClaim() {
        return prefs.getBoolean(GlobalVariables.SEND_CLAIM, DEFAULT_SEND_CLAIM);
    }

    /**
     *
     * @param b
     */
    public void setSendClaim(boolean b) {
        prefs.putBoolean(GlobalVariables.SEND_CLAIM, b);
    }

    /**
     * 保存時に CLAIM 送信を行うかどうかを返す。
     * @param 行う時 true
     */
    //  public boolean getSendClaimSave() {
    //      return prefs.getBoolean(GrobalVariables.SEND_CLAIM_SAVE, DEFAULT_SEND_CLAIM_SAVE);
    //  }
    //   public void setSendClaimSave(boolean b) {
    //       prefs.putBoolean(GrobalVariables.SEND_CLAIM_SAVE, b);
    //   }
    /**
     * 仮保存時に CLAIM 送信を行うかどうかを返す。
     * @param 行う時 true
     */
    //   public boolean getSendClaimTmp() {
    //       return prefs.getBoolean(GrobalVariables.SEND_CLAIM_TMP, DEFAULT_SEND_CLAIM_TMP);
    //   }
    //  public void setSendClaimTmp(boolean b) {
    //      prefs.putBoolean(GrobalVariables.SEND_CLAIM_TMP, b);
    //  }
    /**
     * 修正時に CLAIM 送信を行うかどうかを返す。
     * @return
     */
    //  public boolean getSendClaimModify() {
    //      return prefs.getBoolean(GrobalVariables.SEND_CLAIM_MODIFY, DEFAULT_SEND_CLAIM_MODIFY);
    //  }
    //  public void setSendClaimModify(boolean b) {
    //      prefs.putBoolean(GrobalVariables.SEND_CLAIM_MODIFY, b);
    //   }
    public String getDefaultKarteTitle() {
        return prefs.get("defaultKarteTitle", "経過記録");
    }

    /**
     *
     * @param defaultKarteTitle
     */
    public void setDefaultKarteTitle(String defaultKarteTitle) {
        prefs.put("defaultKarteTitle", defaultKarteTitle);
    }

    /**
     *
     * @return
     */
    public boolean isUseTop15AsTitle() {
        return prefs.getBoolean("useTop15AsTitle", true);
    }

    /**
     *
     * @param useTop15AsTitle
     */
    public void setUseTop15AsTitle(boolean useTop15AsTitle) {
        prefs.putBoolean("useTop15AsTitle", useTop15AsTitle);
    }

    /**
     * 病名 CLAIM 送信を行うかどうかを返す。
     * @return
     */
    public boolean getSendDiagnosis() {
        return prefs.getBoolean(GlobalVariables.SEND_DIAGNOSIS, DEFAULT_SEND_DIAGNOSIS);
    }

    /**
     *
     * @param b
     */
    public void setSendDiagnosis(boolean b) {
        prefs.putBoolean(GlobalVariables.SEND_DIAGNOSIS, b);
    }

    /**
     *
     * @return
     */
    public String getClaimHostName() {

        if (GlobalSettings.isTrial()) {
            return "日医標準レセコン(ORCA)";
        } else {
            return prefs.get(GlobalVariables.CLAIM_HOST_NAME, DEFAULT_CLAIM_HOST_NAME);
        }
    }

    /**
     *
     * @param b
     */
    public void setClaimHostName(String b) {
        prefs.put(GlobalVariables.CLAIM_HOST_NAME, b);
    }

    /**
     *
     * @return
     */
    public String getClaimEncoding() {
        return prefs.get(GlobalVariables.CLAIM_ENCODING, DEFAULT_CLAIM_ENCODING);
    }

    /**
     *
     * @param val
     */
    public void setClaimEncoding(String val) {
        prefs.put(GlobalVariables.CLAIM_ENCODING, val);
    }

    /**
     *
     * @return
     */
    public String getClaimAddress() {

        if (GlobalSettings.isTrial()) {
           return "dolphin.good-day.co.jp";
        } else {
            return prefs.get(GlobalVariables.CLAIM_ADDRESS, DEFAULT_CLAIM_ADDRESS);
        }
    }

    /**
     *
     * @param val
     */
    public void setClaimAddress(String val) {
        prefs.put(GlobalVariables.CLAIM_ADDRESS, val);
    }

    /**
     *
     * @return
     */
    public int getClaimPort() {

        if (GlobalSettings.isTrial()) {
            return 10329;
        } else {
            return prefs.getInt(GlobalVariables.CLAIM_PORT, DEFAULT_CLAIM_PORT);
        }
    }

    /**
     *
     * @param val
     */
    public void setClaimPort(int val) {
        prefs.putInt(GlobalVariables.CLAIM_PORT, val);
    }

    /**
     *
     * @param b
     */
    public void setUseAsPVTServer(boolean b) {
        prefs.putBoolean(GlobalVariables.USE_AS_PVT_SERVER, b);
    }

    /**
     *
     * @return
     */
    public boolean getUseAsPVTServer() {

        if (GlobalSettings.isTrial()) {
            return true;
        } else {
            return prefs.getBoolean(GlobalVariables.USE_AS_PVT_SERVER, DEFAULT_USE_AS_PVTSERVER);
        }
    }

    //入院か否か
    /**
     *
     * @return
     */
    public boolean getIsHospital() {
        return prefs.getBoolean(GlobalVariables.IS_HOSPITAL, false);
    }

    /**
     *
     * @param val
     */
    public void setIsHospital(boolean val) {
        prefs.putBoolean(GlobalVariables.IS_HOSPITAL, val);
    }

    /**
     *
     * @return
     */
    public String getLetterGreetings() {
        return prefs.get(GlobalVariables.GREETINGS, "");
    }

    /**
     *
     * @param val
     */
    public void setLetterGreetings(String val) {
        prefs.put(GlobalVariables.GREETINGS, val);
    }

    //
    // AreaNetwork関連情報
    //
    /**
     *
     * @return
     */
    public boolean getJoinAreaNetwork() {
        return prefs.getBoolean(GlobalVariables.JOIN_AREA_NETWORK, false);		// 地域連携参加
    }

    /**
     *
     * @param b
     */
    public void setJoinAreaNetwork(boolean b) {
        prefs.putBoolean(GlobalVariables.JOIN_AREA_NETWORK, b);				// 地域連携参加
    }

    /**
     *
     * @return
     */
    public String getAreaNetworkName() {
        return prefs.get(GlobalVariables.AREA_NETWORK_NAME, null);			// 地域連携名
    }

    /**
     *
     * @param name
     */
    public void setAreaNetworkName(String name) {
        prefs.put(GlobalVariables.AREA_NETWORK_NAME, name);				// 地域連携名
    }

    /**
     *
     * @return
     */
    public String getAreaNetworkFacilityId() {
        return prefs.get(GlobalVariables.AREA_NETWORK_FACILITY_ID, null);		// 地域連携施設ID
    }

    /**
     *
     * @param id
     */
    public void setAreaNetworkFacilityId(String id) {
        prefs.put(GlobalVariables.AREA_NETWORK_FACILITY_ID, id);			// 地域連携施設ID
    }

    /**
     *
     * @return
     */
    public String getAreaNetworkCreatorId() {
        return prefs.get(GlobalVariables.AREA_NETWORK_CREATOR_ID, null);		// 地域連携CreatorID
    }

    /**
     *
     * @param id
     */
    public void setAreaNetworkCreatorId(String id) {
        prefs.put(GlobalVariables.AREA_NETWORK_CREATOR_ID, id);                         // 地域連携CreatorID
    }

    //
    // MML送信関連の情報
    //
    /**
     *
     * @return
     */
    public boolean getSendMML() {
        return prefs.getBoolean(GlobalVariables.SEND_MML, DEFAULT_SEND_MML);
    }

    /**
     *
     * @param b
     */
    public void setSendMML(boolean b) {
        prefs.putBoolean(GlobalVariables.SEND_MML, b);
    }

    /**
     *
     * @return
     */
    public String getMMLVersion() {
        return prefs.get(GlobalVariables.MML_VERSION, DEFAULT_MML_VERSION);
    }

    /**
     *
     * @param b
     */
    public void setMMLVersion(String b) {
        prefs.put(GlobalVariables.MML_VERSION, b);
    }

    /**
     *
     * @return
     */
    public String getMMLEncoding() {
        return prefs.get(GlobalVariables.MML_ENCODING, DEFAULT_MML_ENCODING);
    }

    /**
     *
     * @param val
     */
    public void setMMLEncoding(String val) {
        prefs.put(GlobalVariables.MML_ENCODING, val);
    }

    /**
     *
     * @return
     */
    public boolean getMIMEEncoding() {
        return prefs.getBoolean("mimeEncoding", false);
    }

    /**
     *
     * @param val
     */
    public void setMIMEEncoding(boolean val) {
        prefs.putBoolean("mimeEncoding", val);
    }

    /**
     *
     * @return
     */
    public String getUploaderIPAddress() {
        return prefs.get(GlobalVariables.SEND_MML_ADDRESS, DEFAULT_SEND_MML_ADDRESS);
    }

    /**
     *
     * @param val
     */
    public void setUploaderIPAddress(String val) {
        prefs.put(GlobalVariables.SEND_MML_ADDRESS, val);
    }

    /**
     *
     * @return
     */
    public String getUploadShareDirectory() {
        return prefs.get(GlobalVariables.SEND_MML_DIRECTORY, DEFAULT_SEND_MML_DIRECTORY);
    }

    /**
     *
     * @param val
     */
    public void setUploadShareDirectory(String val) {
        prefs.put(GlobalVariables.SEND_MML_DIRECTORY, val);
    }

    //
    // Software Update 関連
    //
    /**
     *
     * @return
     */
    public boolean getUseProxy() {
        return prefs.getBoolean(GlobalVariables.USE_PROXY, DEFAULT_USE_PROXY);
    }

    /**
     *
     * @param b
     */
    public void setUseProxy(boolean b) {
        prefs.putBoolean(GlobalVariables.USE_PROXY, b);
    }

    /**
     *
     * @return
     */
    public String getProxyHost() {
        return prefs.get(GlobalVariables.PROXY_HOST, DEFAULT_PROXY_HOST);
    }

    /**
     *
     * @param val
     */
    public void setProxyHost(String val) {
        prefs.put(GlobalVariables.PROXY_HOST, val);
    }

    /**
     *
     * @return
     */
    public int getProxyPort() {
        return prefs.getInt(GlobalVariables.PROXY_PORT, DEFAULT_PROXY_PORT);
    }

    /**
     *
     * @param val
     */
    public void setProxyPort(int val) {
        prefs.putInt(GlobalVariables.PROXY_PORT, val);
    }

    /**
     *
     * @return
     */
    public long getLastModify() {
        return prefs.getLong(GlobalVariables.LAST_MODIFIED, DEFAULT_LAST_MODIFIED);
    }

    /**
     *
     * @param val
     */
    public void setLastModify(long val) {
        prefs.putLong(GlobalVariables.LAST_MODIFIED, val);
    }

    /**
     *
     * @return
     */
    public CombinedStringParser getDirections() {
        String line_string = prefs.get(GlobalVariables.DIRECTIONS, DEFAULT_DIRECTIONS);
        return new CombinedStringParser(',', line_string);
    }

    /**
     *
     * @param lines
     */
    public void setDirections(CombinedStringParser lines) {
        prefs.put(GlobalVariables.DIRECTIONS, lines.toCombinedString());
    }

    /**
     *
     * @param index
     * @return
     */
    public String getDirection(int index) {
        String line_string = prefs.get(GlobalVariables.DIRECTIONS, DEFAULT_DIRECTIONS);
        CombinedStringParser line = new CombinedStringParser(',', line_string);
        return line.get(index);
    }

    /**
     *
     * @param index
     * @param direction
     */
    public void setDirection(int index, String direction) {
        CombinedStringParser line = new CombinedStringParser(',', prefs.get(GlobalVariables.DIRECTIONS, DEFAULT_DIRECTIONS));
        line.set(index, direction);
        prefs.put(GlobalVariables.DIRECTIONS, line.toCombinedString());
    }

    /**
     *
     * @return
     */
    public CombinedStringParser getCcDirections() {
        String line_string = prefs.get(GlobalVariables.CC_DIRECTIONS, DEFAULT_DIRECTIONS);
        return new CombinedStringParser(',', line_string);
    }

    /**
     *
     * @param lines
     */
    public void setCcDirections(CombinedStringParser lines) {
        prefs.put(GlobalVariables.CC_DIRECTIONS, lines.toCombinedString());
    }

    /**
     *
     * @param index
     * @return
     */
    public String getCcDirection(int index) {
        String line_string = prefs.get(GlobalVariables.CC_DIRECTIONS, DEFAULT_DIRECTIONS);
        CombinedStringParser line = new CombinedStringParser(',', line_string);
        return line.get(index);
    }

    /**
     *
     * @param index
     * @param direction
     */
    public void setCcDirection(int index, String direction) {
        CombinedStringParser line = new CombinedStringParser(',', prefs.get(GlobalVariables.CC_DIRECTIONS, DEFAULT_DIRECTIONS));
        line.set(index, direction);
        prefs.put(GlobalVariables.CC_DIRECTIONS, line.toCombinedString());
    }

    /**
     *
     * @return
     */
    public boolean getFepControl() {
        return prefs.getBoolean(GlobalVariables.FEP_CONTROL, true);
    }

    /**
     *
     * @param value
     */
    public void setFepControl(boolean value) {
        prefs.putBoolean(GlobalVariables.FEP_CONTROL, value);
    }

    /**
     *
     * @param os
     */
    public void exportSubtree(OutputStream os) {
        try {
            prefs.exportSubtree(os);
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     */
    public void clear() {
        try {
            prefs.clear();
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     * @param subject
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     *
     * @return
     */
    public Subject getSubject() {
        return this.subject;
    }

    /**
     *
     * @return
     */
    public String getPreferencesPath() {
        return prefs.absolutePath();
    }

    /**
     * RS_BaseサーバのIPアドレスを設定する。
     * @param ipAddress IPアドレス
     */
    public void setRsbAddress(String ipAddress) {
        prefs.put(GlobalVariables.RSB_ADDRESS, ipAddress);
    }

    /**
     * RS_BaseサーバのIPアドレスを返す。
     * @return RS_BaseサーバのIPアドレス
     */
    public String getRsbAddress() {
        return prefs.get(GlobalVariables.RSB_ADDRESS, DEFAULT_ADDRESS);
    }

    /**
     * RS_Baseサーバへの所見出力の有無を設定する。
     * @param value 所見出力の有無
     */
    public void setOutputShoken(boolean value) {
        prefs.putBoolean(GlobalVariables.RSB_OUTPUT_SHOKEN, value);
    }

    /**
     * RS_Baseサーバへの所見出力の有無を返す。
     * @return 所見を出力する場合は、{@code true}
     */
    public boolean getOutputShoken() {
        return prefs.getBoolean(GlobalVariables.RSB_OUTPUT_SHOKEN, true);
    }

    /**
     * RS_Baseサーバの共有フォルダ名を設定する。
     * @param folderName RS_Baseサーバの共有フォルダ名
     */
    public void setRsbFolder(String folderName) {
        prefs.put(GlobalVariables.RSB_FOLDER, folderName);
    }

    /**
     * RS_Baseサーバの共有フォルダ名を返す。
     * @return RS_Baseサーバの共有フォルダ名
     */
    public String getRsbFolder() {
        return prefs.get(GlobalVariables.RSB_FOLDER, DEFAULT_FOLDER);
    }

    /**
     * RS_Baseサーバの共有フォルダへのアクセス制限の有無を設定する。
     * @param value アクセス制限の有無
     */
    public void setRsbAcsCtrl(boolean value) {
        prefs.putBoolean(GlobalVariables.RSB_ACCESS_CTRL, value);
    }

    /**
     * RS_Baseサーバの共有フォルダへのアクセス制限の有無を返す。
     * @return アクセスを制限する場合は、{@code true}
     */
    public boolean getRsbAcsCtrl() {
        return prefs.getBoolean(GlobalVariables.RSB_ACCESS_CTRL, false);
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザ名を設定する。
     * @param userName ユーザ名
     */
    public void setRsbUserName(String userName) {
        prefs.put(GlobalVariables.RSB_USER_NAME, userName);
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザ名を返す。
     * @return ユーザ名
     */
    public String getRsbUserName() {
        return prefs.get(GlobalVariables.RSB_USER_NAME, DEFAULT_USER_NAME);
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザのパスワードを設定する。
     * @param password パスワード
     */
    public void setRsbPassword(String password) {
        prefs.put(GlobalVariables.RSB_PASSWORD, password);
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザのパスワードを返す。
     * @return パスワード
     */
    public String getRsbPassword() {
        return prefs.get(GlobalVariables.RSB_PASSWORD, DEFAULT_PASSWORD);
    }
}
