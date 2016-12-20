package open.dolphin.project;

import java.awt.Window;
import java.io.OutputStream;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.security.auth.Subject;
import open.dolphin.client.GUIConst;
import open.dolphin.client.SaveParams;

import open.dolphin.infomodel.ID;
import open.dolphin.infomodel.RoleModel;
import open.dolphin.infomodel.UserModel;
import open.dolphin.utils.CombinedStringParser;

/**
 * プロジェクト情報管理クラス。
 *　大域変数
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class GlobalVariables {

    // Prpject Name
    /**
     *
     */
    public static final String PROJECT_NAME = "name";
    // USER
    /**
     *
     */
    public static final String USER_TYPE = "userType";
    /**
     *
     */
    public static final String USER_ID = "userId";
    /**
     *
     */
    public static final String FACILITY_ID = "facilityId";
    // SERVER
    /**
     *
     */
    public static final String DB_ADDRESS = "dbAddress";
    /**
     *
     */
    public static final String DB_PASSWORD = "dbPassword";
    /**
     *
     */
    public static final String DB_SSLSTATE = "dbSSLState";
    /**
     *
     */
    public static final String HOST_PORT = "hostPort";
    // CLAIM
    /**
     *
     */
    public static final String SEND_CLAIM = "sendClaim";
    /**
     *
     */
    public static final String SEND_DIAGNOSIS = "sendDiagnosis";
    /**
     *
     */
    public static final String CLAIM_HOST_NAME = "claimHostName";
    /**
     *
     */
    public static final String CLAIM_VERSION = "claimVersion";
    /**
     *
     */
    public static final String CLAIM_ENCODING = "claimEncoding";
    /**
     *
     */
    public static final String CLAIM_ADDRESS = "claimAddress";
    /**
     *
     */
    public static final String CLAIM_PORT = "claimPort";
    /**
     *
     */
    public static final String USE_AS_PVT_SERVER = "useAsPVTServer";
    /**
     *
     */
    public static final String IS_HOSPITAL = "isHospital";
    /**
     *
     */
    public static final String GREETINGS = "greetings";
    /**
     *
     */
    public static final String ORCA_CHARACTER_CODE = "orcacharactercode";
    // Area Network
    /**
     *
     */
    public static final String JOIN_AREA_NETWORK = "joinAreaNetwork";
    /**
     *
     */
    public static final String AREA_NETWORK_NAME = "jareaNetworkName";
    /**
     *
     */
    public static final String AREA_NETWORK_FACILITY_ID = "jareaNetworkFacilityId";
    /**
     *
     */
    public static final String AREA_NETWORK_CREATOR_ID = "jareaNetworkCreatorId";
    // MML
    /**
     *
     */
    public static final String SEND_MML = "mml.send";
    /**
     *
     */
    public static final String MML_VERSION = "mml.version";
    /**
     *
     */
    public static final String MML_ENCODING = "mml.encoding";
    /**
     *
     */
    public static final String SEND_MML_ADDRESS = "mml.address";
    /**
     *
     */
    public static final String SEND_MML_DIRECTORY = "mml.directory";
    /**
     *
     */
    public static final String SEND_MML_PROTOCOL = "mml.protocol";
    // ソフトウェア更新
    /**
     *
     */
    public static final String USE_PROXY = "useProxy";
    /**
     *
     */
    public static final String PROXY_HOST = "proxyHost";
    /**
     *
     */
    public static final String PROXY_PORT = "proxyPort";
    /**
     *
     */
    public static final String LAST_MODIFIED = "lastModify";
    // インスペクタのメモ位置
    /**
     *
     */
    public static final String INSPECTOR_MEMO_LOCATION = "inspectorMemoLocation";
    // 文書履歴
    /**
     *
     */
    public static final String DOC_HISTORY_ASCENDING = "docHistory.ascending";
    /**
     *
     */
    public static final String DOC_HISTORY_SHOWMODIFIED = "docHistory.showModified";
    /**
     *
     */
    public static final String DOC_HISTORY_SHOWUNSEND = "docHistory.showUnsend";
    /**
     *
     */
    public static final String DOC_HISTORY_SHOWSEND = "docHistory.showSend";
    /**
     *
     */
    public static final String DOC_HISTORY_SHOWNEWEST = "docHistory.showNewest";
    /**
     *
     */
    public static final String DOC_HISTORY_FETCHCOUNT = "docHistory.fetchCount";
    /**
     *
     */
    public static final String DOC_HISTORY_PERIOD = "docHistory.period";
    /**
     *
     */
    public static final String KARTE_SCROLL_DIRECTION = "karte.scroll.direction";
    /**
     *
     */
    public static final String DOUBLE_KARTE = "karte.double";
    // 病名
    /**
     *
     */
    public static final String DIAGNOSIS_ASCENDING = "diagnosis.ascending";
    /**
     *
     */
    public static final String DIAGNOSIS_PERIOD = "diagnosis.period";
    /**
     *
     */
    public static final String OFFSET_OUTCOME_DATE = "diagnosis.offsetOutcomeDate";
    // 検体検査
    /**
     *
     */
    public static final String LABOTEST_PERIOD = "laboTest.period";
    // 処方
    /**
     *
     */
    public static final String RP_OUT = "rp.out";
    // 確認ダイアログ
    /**
     * 確認ダイアログ
     */
    public static final String KARTE_SHOW_CONFIRM_AT_NEW = "karte.showConfirmAtNew";
    /**
     *
     */
    public static final String KARTE_CREATE_MODE = "karte.createMode";
    /**
     *
     */
    public static final String KARTE_PLACE_MODE = "karte.placeMode";
    /**
     *
     */
    public static final String KARTE_SHOW_CONFIRM_AT_SAVE = "karte.showConfirmAtSave";
    /**
     *
     */
    public static final String KARTE_PRINT_COUNT = "karte.printCount";
    /**
     *
     */
    public static final String KARTE_SAVE_ACTION = "karte.saveAction";
    /**
     *
     */
    public static final String DIRECTIONS = "karte.directions";
    /**
     *
     */
    public static final String CC_DIRECTIONS = "karte.cc_directions";
    /**
     *
     */
    public static final String FEP_CONTROL = "karte.fep_control";

    /**
     * RS_Baseプラグイン用
     */
    public static final String RSB_ADDRESS = "rsb.address";
    public static final String RSB_OUTPUT_SHOKEN = "rsb.output_shoken";
    public static final String RSB_FOLDER = "rsb.folder";
    public static final String RSB_ACCESS_CTRL = "rsb.access_ctrl";
    public static final String RSB_USER_NAME = "rsb.user_name";
    public static final String RSB_PASSWORD = "rsb.password";

    // ユーザの利用形式

    /**
     *
     */
    public enum UserType {

        /**
         *
         */
        ASP_MEMBER,
        /**
         * 
         */
        ASP_TESTER,
        /**
         *
         */
        ASP_DEV,
        /**
         *
         */
        FACILITY_USER,
        /**
         * 
         */
        UNKNOWN,
        /**
         *
         */
        EXPIRED
    };
    private static GlobalVariablesImplement stub;

    /**
     * 大域変数を生成する
     */
    public static void createGlobalVariables() {
        stub = new GlobalVariablesImplement();
    }

    /**
     *
     * @return
     */
    public static UserType getUserType() {
        return stub.getUserType();
    }

    /**
     *
     * @param userType
     */
    public static void setUserType(UserType userType) {
        stub.setUserType(userType);
    }

    /**
     *
     * @return
     */
    public static boolean isValid() {
        return stub.isValid();
    }

    /**
     * 設定ノードを返します
     * @return 設定ノード（ちょっとしたデータ）
     */
    public static Preferences getPreferences() {
        return stub.getPreferences();
    }

    /**
     *
     * @return
     */
    public static DolphinPrincipal getDolphinPrincipal() {
        return stub.getDolphinPrincipal();
    }

    /**
     *
     * @param principal
     */
    public static void setDolphinPrincipal(DolphinPrincipal principal) {
        stub.setDolphinPrincipal(principal);
    }

    /**
     *
     * @return
     */
    public static UserModel getUserModel() {
        return stub.getUserModel();
    }

    /**
     *
     * @param value
     */
    public static void setUserModel(UserModel value) {
        stub.setUserModel(value);
    }

    /**
     *
     * @return
     */
    public static boolean isReadOnly() {
        String licenseCode = stub.getUserModel().getLicenseModel().getLicense();
        String userId = stub.getUserModel().getUserId();
        return (licenseCode.equals("doctor") || userId.equals("lasmanager")) ? false : true;
    }

    /**
     *
     * @return
     */
    public static String getUserId() {
        return stub.getUserId();
    }

    /**
     *
     * @param userid
     */
    public static void setUserId(String userid) {
        stub.setUserId(userid);
    }

    /**
     *
     * @return
     */
    public static String getFacilityId() {
        return stub.getFacilityId();
    }

    /**
     *
     * @param facilityId
     */
    public static void setFacilityId(String facilityId) {
        stub.setFacilityId(facilityId);
    }

    /**
     *
     * @return
     */
    public static String getOrcaVersion() {
        return stub.getOrcaVersion();
    }

    /**
     *
     * @param version
     */
    public static void setOrcaVersion(String version) {
        stub.setOrcaVersion(version);
    }

    /**
     *
     * @return
     */
    public static String getJMARICode() {
        return stub.getJMARICode();
    }

    /**
     *
     * @param jmariCode
     */
    public static void setJMARICode(String jmariCode) {
        stub.setJMARICode(jmariCode);
    }

    /**
     *
     * @return
     */
    public static String getDbAddress() {
        return stub.getDbAddress();
    }

    /**
     *
     * @param dbAddress
     */
    public static void setDbAddress(String dbAddress) {
        stub.setDbAddress(dbAddress);
    }

    /**
     *
     * @return
     */
    public static String getDbPasswordWithoutHash() {
        return stub.getDbPasswordWithoutHash();
    }

    /**
     *
     * @param dbPass
     */
    public static void setDbPassword(String dbPass) {
        stub.setDbPassword(dbPass);
    }

    /**
     *
     * @return
     */
    public static boolean getDbSSLState() {
        return stub.getDbSSLState();
    }

    /**
     *
     * @param state
     */
    public static void setDbSSLState(boolean state) {
        stub.setDbSSLState(state);
    }

    /**
     *
     * @return
     */
    public static int getHostPort() {
        return stub.getHostPort();
    }

    /**
     *
     * @param port
     */
    public static void setHostPort(int port) {
        stub.setHostPort(port);
    }

    // Directions
    /**
     *
     * @return
     */
    public static CombinedStringParser getDirections() {
        return stub.getDirections();
    }

    /**
     *
     * @param lines
     */
    public static void setDirections(CombinedStringParser lines) {
        stub.setDirections(lines);
    }

    /**
     *
     * @return
     */
    public static CombinedStringParser getCcDirections() {
        return stub.getCcDirections();
    }

    /**
     *
     * @param cc_lines
     */
    public static void setCcDirections(CombinedStringParser cc_lines) {
        stub.setCcDirections(cc_lines);
    }

    //
    // CLAIM
    //
    /**
     * 診療行為の送信を行うかどうかを返す。
     * @return 行うとき true
     */
    public static boolean getSendClaim() {
        return stub.getSendClaim();
    }

    /**
     *
     * @param sendClaim
     */
    public static void setSendClaim(boolean sendClaim) {
        stub.setSendClaim(sendClaim);
    }

    /**
     * 病名の送信を行うかどうかを返す。
     * @return 行うとき true
     */
    public static boolean getSendDiagnosis() {
        return stub.getSendDiagnosis();
    }

    /**
     * CLAIM のホスト名を返す。
     * @return return CLAIM のホスト名
     */
    public static String getClaimHostName() {
        return stub.getClaimHostName();
    }

    /**
     *
     * @param claimHostName
     */
    public static void setClaimHostName(String claimHostName) {
        stub.setClaimHostName(claimHostName);
    }

    /**
     * 受付情報を受信するかどうかを返す。
     * @return 行うとき true
     */
    public static boolean getUseAsPVTServer() {
        return stub.getUseAsPVTServer();
    }

    /**
     * 「環境設定」画面の「レセコン」タブの[受付情報の受信]チェックボックスの内容を設定する。
     * @param useAsPVTServer [受付情報の受信]チェックボックスの内容
     */
    public static void setUseAsPVTServer(boolean useAsPVTServer) {
        stub.setUseAsPVTServer(useAsPVTServer);
    }

    /**
     * 入院か否か
     * @return
     */
    public static boolean getIsHospital() {
        return stub.getIsHospital();
    }

    /**
     *
     * @param val
     */
    public static void setIsHospital(boolean val) {
        stub.setIsHospital(val);
    }

    /**
     *
     * @return
     */
    public static String getLetterGreetings() {
        return stub.getLetterGreetings();
    }

    /**
     *
     * @param val
     */
    public static void setLetterGreetings(String val) {
        stub.setLetterGreetings(val);
    }

    /**
     * CLAIM ホストの IP アドレスを返す。
     * @return CLAIM ホストの IP アドレス
     */
    public static String getClaimAddress() {
        return stub.getClaimAddress();
    }

    /**
     *
     * @param claimAddress
     */
    public static void setClaimAddress(String claimAddress) {
        stub.setClaimAddress(claimAddress);
    }

    /**
     * CLAIM ホストの診療行為送信先ポート番号を返す。
     * @return CLAIM ホスト名の診療行為送信先ポート番号
     */
    public static int getClaimPort() {
        return stub.getClaimPort();
    }

    /**
     *
     * @param claimPort
     */
    public static void setClaimPort(int claimPort) {
        stub.setClaimPort(claimPort);
    }

    /**
     * CLAIM 送信時のXMLエンコーディングを返す。
     * @return CLAIM エンコーディング
     */
    public static String getClaimEncoding() {
        return stub.getClaimEncoding();
    }

    /**
     *
     * @return
     */
    public static String getProxyHost() {
        return stub.getProxyHost();
    }

    /**
     *
     * @return
     */
    public static int getProxyPort() {
        return stub.getProxyPort();
    }

    /**
     *
     * @return
     */
    public static long getLastModify() {
        return stub.getLastModify();
    }

    /**
     *
     * @param val
     */
    public static void setLastModify(long val) {
        stub.setLastModify(val);
    }

    /**
     * ProjectFactoryを返す。
     * @return Project毎に異なる部分の情報を生成するためのFactory
     */
    public static AbstractProjectFactory getProjectFactory() {
        return AbstractProjectFactory.getProjectFactory(stub.getName());
    }

    /**
     * 地域連携に参加するかどうかを返す。
     * @return 参加する時 true
     */
    public static boolean getJoinAreaNetwork() {
        return stub.getJoinAreaNetwork();
    }

    /**
     *
     * @param join
     */
    public static void setJoinAreaNetwork(boolean join) {
        stub.setJoinAreaNetwork(join);
    }

    /**
     *
     * @return
     */
    public static String getAreaNetworkName() {
        return stub.getAreaNetworkName();
    }

    /**
     *
     * @param areaNetworkName
     */
    public static void setAreaNetworkName(String areaNetworkName) {
        stub.setAreaNetworkName(areaNetworkName);
    }

    /**
     * 地域連携用の施設IDを返す。
     * @return 地域連携で使用する施設ID
     */
    public static String getAreaNetworkFacilityId() {
        return stub.getAreaNetworkFacilityId();
    }

    /**
     *
     * @param areaNetworkFacilityId
     */
    public static void setAreaNetworkFacilityId(String areaNetworkFacilityId) {
        stub.setAreaNetworkFacilityId(areaNetworkFacilityId);
    }

    /**
     * 地域連携用のCreatorIDを返す。
     * @return 地域連携で使用するCreatorId
     */
    public static String getAreaNetworkCreatorId() {
        return stub.getAreaNetworkCreatorId();
    }

    /**
     *
     * @param areaNetworkCreatorId
     */
    public static void setAreaNetworkCreatorId(String areaNetworkCreatorId) {
        stub.setAreaNetworkCreatorId(areaNetworkCreatorId);
    }

    /**
     * 地域連携用の患者MasterIdを返す。
     * @param pid
     * @return 地域連携で使用する患者MasterId
     */
    public static ID getMasterId(String pid) {
        String fid = stub.getAreaNetworkFacilityId();
        return getProjectFactory().createMasterId(pid, fid);
    }

    /**
     * CLAIM送信に使用する患者MasterIdを返す。
     * 地域連携ルールと異なるため。
     * @param pid
     * @return
     */
    public static ID getClaimMasterId(String pid) {
        return new ID(pid, "facility", "MML0024");
    }

    /**
     *　「ドキュメント保存」ダイアログを返します。
     * @param parent 親ウィンドウ
     * @param params ドキュメント保存のパラメーターのインスタンス
     * @return 「ドキュメント保存」ダイアログのインスタンス
     */
    public static Object createSaveDialog(Window parent, SaveParams params) {
        return getProjectFactory().createSaveDialog(parent, params);
    }

    /**
     * CSGW(Client Side Gate Way)へのパスを返す。
     * @return
     */
    public static String getCSGWPath() {
        String uploader = getUploaderIPAddress();
        String share = getUploadShareDirectory();
        String id = stub.getAreaNetworkFacilityId() != null
                ? stub.getAreaNetworkFacilityId()
                : stub.getUserModel().getFacility().getFacilityId();
        return getProjectFactory().createCSGWPath(uploader, share, id);
    }

    // HOT
    /**
     *
     * @return
     */
    public static boolean getSendMML() {
        return stub.getSendMML();
    }

    /**
     *
     * @param sendMML
     */
    public static void setSendMML(boolean sendMML) {
        stub.setSendMML(sendMML);
    }

    /**
     *
     * @return
     */
    public static String getMMLVersion() {
        return stub.getMMLVersion();
    }

    /**
     *
     * @param version
     */
    public static void setMMLVersion(String version) {
        stub.setMMLVersion(version);
    }

    /**
     *
     * @return
     */
    public static String getMMLEncoding() {
        return stub.getMMLEncoding();
    }

    /**
     *
     * @return
     */
    public static String getUploaderIPAddress() {
        return stub.getUploaderIPAddress();
    }

    /**
     *
     * @param ipAddress
     */
    public static void setUploaderIPAddress(String ipAddress) {
        stub.setUploaderIPAddress(ipAddress);
    }

    /**
     *
     * @return
     */
    public static String getUploadShareDirectory() {
        return stub.getUploadShareDirectory();
    }

    /**
     *
     * @param directory
     */
    public static void setUploadShareDirectory(String directory) {
        stub.setUploadShareDirectory(directory);
    }

    /**
     *
     * @return
     */
    public static boolean getShowModifiedKarte() {
        return stub.getShowModifiedKarte();
    }

    /**
     *
     * @return
     */
    public static boolean getShowUnsendKarte() {
        return stub.getShowUnsendKarte();
    }

    /**
     *
     * @return
     */
    public static boolean getShowSendKarte() {
        return stub.getShowSendKarte();
    }

    /**
     *
     * @return
     */
    public static boolean getShowNewestKarte() {
        return stub.getShowNewestKarte();
    }

    /**
     *
     * @return
     */
    public static int getSaveKarteMode() {
        return stub.getSaveKarteMode();
    }

    /**
     *
     * @param mode
     */
    public static void setSaveKarteMode(int mode) {
        stub.setSaveKarteMode(mode);
    }

    /**
     *
     * @return
     */
    public static int getPrintKarteCount() {
        return stub.getPrintKarteCount();
    }

    /**
     *
     * @param cnt
     */
    public static void setPrintKarteCount(int cnt) {
        stub.setPrintKarteCount(cnt);
    }

    /**
     *
     * @return
     */
    public static String getPDFStore() {
        return stub.getPDFStore();
    }

    /**
     *
     * @param pdfDir
     */
    public static void setPDFStore(String pdfDir) {
        stub.setPDFStore(pdfDir);
    }

    /**
     *
     * @return
     */
    public static int getFetchKarteCount() {
        return stub.getFetchKarteCount();
    }

    /**
     *
     * @return
     */
    public static boolean getScrollKarteV() {
        return stub.getScrollKarteV();
    }

    /**
     *
     * @return
     */
    public static boolean getAscendingKarte() {
        return stub.getAscendingKarte();
    }

    /**
     *
     * @return
     */
    public static int getKarteExtractionPeriod() {
        return stub.getKarteExtractionPeriod();
    }

    /**
     *
     * @return
     */
    public static boolean getAscendingDiagnosis() {
        return stub.getAscendingDiagnosis();
    }

    /**
     *
     * @return
     */
    public static int getDiagnosisExtractionPeriod() {
        return stub.getDiagnosisExtractionPeriod();
    }

    /**
     *
     * @return
     */
    public static boolean isAutoOutcomeInput() {
        return stub.isAutoOutcomeInput();
    }

    /**
     *
     * @return
     */
    public static int getLaboTestExtractionPeriod() {
        return stub.getLaboTestExtractionPeriod();
    }

    /**
     *
     * @return
     */
    public static boolean isReplaceStamp() {
        return stub.isReplaceStamp();
    }

    /**
     *
     * @return
     */
    public static boolean isStampSpace() {
        return stub.isStampSpace();
    }

    /**
     *
     * @return
     */
    public static boolean isLaboFold() {
        return stub.isLaboFold();
    }

    /**
     *
     * @return
     */
    public static String getDefaultZyozaiNum() {
        return stub.getDefaultZyozaiNum();
    }

    /**
     *
     * @return
     */
    public static String getDefaultMizuyakuNum() {
        return stub.getDefaultMizuyakuNum();
    }

    /**
     *
     * @return
     */
    public static String getDefaultSanyakuNum() {
        return stub.getDefaultSanyakuNum();
    }

    /**
     *
     * @return
     */
    public static String getDefaultRpNum() {
        return stub.getDefaultRpNum();
    }

    /**
     *
     * @return
     */
    public static boolean isUseTop15AsTitle() {
        return stub.isUseTop15AsTitle();
    }

    /**
     *
     * @return
     */
    public static boolean getConfirmAtNew() {
        return stub.getConfirmAtNew();
    }

    /**
     *
     * @return
     */
    public static int getCreateKarteMode() {
        return stub.getCreateKarteMode();
    }

    /**
     *
     * @return
     */
    public static boolean getPlaceKarteMode() {
        return stub.getPlaceKarteMode();
    }

    /**
     *
     * @return
     */
    public static boolean getConfirmAtSave() {
        return stub.getConfirmAtSave();
    }

    /**
     *
     * @param fetchKarteCount
     */
    public static void setFetchKarteCount(int fetchKarteCount) {
        stub.setFetchKarteCount(fetchKarteCount);
    }

    /**
     *
     * @param scrollKarteV
     */
    public static void setScrollKarteV(boolean scrollKarteV) {
        stub.setScrollKarteV(scrollKarteV);
    }

    /**
     *
     * @param ascendingKarte
     */
    public static void setAscendingKarte(boolean ascendingKarte) {
        stub.setAscendingKarte(ascendingKarte);
    }

    /**
     *
     * @param karteExtractionPeriod
     */
    public static void setKarteExtractionPeriod(int karteExtractionPeriod) {
        stub.setKarteExtractionPeriod(karteExtractionPeriod);
    }

    /**
     *
     * @param showModifiedKarte
     */
    public static void setShowModifiedKarte(boolean showModifiedKarte) {
        stub.setShowModifiedKarte(showModifiedKarte);
    }

    /**
     *
     * @param showUnsendKarte
     */
    public static void setShowUnsendKarte(boolean showUnsendKarte) {
        stub.setShowUnsendKarte(showUnsendKarte);
    }

    /**
     *
     * @param showSendKarte
     */
    public static void setShowSendKarte(boolean showSendKarte) {
        stub.setShowSendKarte(showSendKarte);
    }

    /**
     *
     * @param showNewestKarte
     */
    public static void setShowNewestKarte(boolean showNewestKarte) {
        stub.setShowNewestKarte(showNewestKarte);
    }

    /**
     *
     * @param ascendingDiagnosis
     */
    public static void setAscendingDiagnosis(boolean ascendingDiagnosis) {
        stub.setAscendingDiagnosis(ascendingDiagnosis);
    }

    /**
     *
     * @param diagnosisExtractionPeriod
     */
    public static void setDiagnosisExtractionPeriod(int diagnosisExtractionPeriod) {
        stub.setDiagnosisExtractionPeriod(diagnosisExtractionPeriod);
    }

    /**
     *
     * @param autoOutcomeInput
     */
    public static void setAutoOutcomeInput(boolean autoOutcomeInput) {
        stub.setAutoOutcomeInput(autoOutcomeInput);
    }

    /**
     *
     * @param laboTestExtractionPeriod
     */
    public static void setLaboTestExtractionPeriod(int laboTestExtractionPeriod) {
        stub.setLaboTestExtractionPeriod(laboTestExtractionPeriod);
    }

    /**
     *
     * @param replaceStamp
     */
    public static void setReplaceStamp(boolean replaceStamp) {
        stub.setReplaceStamp(replaceStamp);
    }

    /**
     *
     * @param stampSpace
     */
    public static void setStampSpace(boolean stampSpace) {
        stub.setStampSpace(stampSpace);
    }

    /**
     *
     * @param laboFold
     */
    public static void setLaboFold(boolean laboFold) {
        stub.setLaboFold(laboFold);
    }

    /**
     *
     * @param num
     */
    public static void setDefaultZyozaiNum(String num) {
        stub.setDefaultZyozaiNum(num);
    }

    /**
     *
     * @param num
     */
    public static void setDefaultMizuyakuNum(String num) {
        stub.setDefaultMizuyakuNum(num);
    }

    /**
     *
     * @param test
     */
    public static void setDefaultSanyakuNum(String test) {
        stub.setDefaultSanyakuNum(test);
    }

    /**
     *
     * @param test
     */
    public static void setDefaultRpNum(String test) {
        stub.setDefaultRpNum(test);
    }

    /**
     *
     * @param useTop15AsTitle
     */
    public static void setUseTop15AsTitle(boolean useTop15AsTitle) {
        stub.setUseTop15AsTitle(useTop15AsTitle);
    }

    /**
     *
     * @param sendDiagnosis
     */
    public static void setSendDiagnosis(boolean sendDiagnosis) {
        stub.setSendDiagnosis(sendDiagnosis);
    }

    /**
     *
     * @param confirmAtNew
     */
    public static void setConfirmAtNew(boolean confirmAtNew) {
        stub.setConfirmAtNew(confirmAtNew);
    }

    /**
     *
     * @param createKarteMode
     */
    public static void setCreateKarteMode(int createKarteMode) {
        stub.setCreateKarteMode(createKarteMode);
    }

    /**
     *
     * @param placeKarteMode
     */
    public static void setPlaceKarteMode(boolean placeKarteMode) {
        stub.setPlaceKarteMode(placeKarteMode);
    }

    /**
     *
     * @param confirmAtSave
     */
    public static void setConfirmAtSave(boolean confirmAtSave) {
        stub.setConfirmAtSave(confirmAtSave);
    }

    /**
     *
     * @param os
     */
    public static void exportSubtree(OutputStream os) {
        stub.exportSubtree(os);
    }

    /**
     *
     */
    public static void clear() {
        stub.clear();
    }

    /**
     *
     * @return
     */
    public static boolean isAdmin() {
        Set<RoleModel> roles = GlobalVariables.getUserModel().getRoles();
        for (RoleModel model : roles) {
            if (model.getRole().equals(GUIConst.ROLE_ADMIN)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public static String getDefaultKarteTitle() {
        return stub.getDefaultKarteTitle();
    }

    /**
     *
     * @param defaultKarteTitle
     */
    public static void setDefaultKarteTitle(String defaultKarteTitle) {
        stub.setDefaultKarteTitle(defaultKarteTitle);
    }

    /**
     *
     * @param subject
     */
    public static void setSubject(Subject subject) {
        stub.setSubject(subject);
    }

    /**
     *
     * @return
     */
    public static Subject getSubject() {
        return stub.getSubject();
    }

    /**
     *
     * @return
     */
    public static String getPreferencesPath() {
        return stub.getPreferencesPath();
    }

    /**
     *
     * @param code
     */
    public static void setCharacterCode(int code) {
        stub.setCharacterCode(code);
    }

    /**
     *
     * @return
     */
    public static int getCharacterCode() {
        return stub.getCharacterCode();
    }

    /**
     *
     * @return
     */
    public static boolean getFepControl() {
        return stub.getFepControl();
    }

    /**
     *
     * @param value
     */
    public static void setFepControl(boolean value) {
        stub.setFepControl(value);
    }

    /**
     * RS_BaseサーバのIPアドレスを設定する。
     * @param ipAddress IPアドレス　
     */
    public static void setRsbAddress(String ipAddress) {
        stub.setRsbAddress(ipAddress);
    }

    /**
     * RS_BaseサーバのIPアドレスを返す。
     * @return RS_BaseサーバのIPアドレス
     */
    public static String getRsbAddress() {
        return stub.getRsbAddress();
    }

    /**
     * RS_Baseサーバへの所見出力の有無を設定する。
     * @param value 所見出力の有無
     */
    public static void setOutputShoken(boolean value) {
        stub.setOutputShoken(value);
    }

    /**
     * RS_Baseサーバへの所見出力の有無を返す。
     * @return 所見を出力する場合は、{@code true}
     */
    public static boolean getOutputShoken() {
        return stub.getOutputShoken();
    }

    /**
     * RS_Baseサーバの共有フォルダ名を設定する。
     * @param folder 共有フォルダ名
     */
    public static void setRsbFolder(String folderName) {
        stub.setRsbFolder(folderName);
    }

    /**
     * RS_Baseサーバの共有フォルダ名を返す。
     * @return 共有フォルダ名
     */
    public static String getRsbFolder() {
        return stub.getRsbFolder();
    }

    /**
     * RS_Baseサーバの共有フォルダへのアクセス制限の有無を設定する。
     * @param value アクセス制限の有無
     */
    public static void setRsbAcsCtrl(boolean value) {
        stub.setRsbAcsCtrl(value);
    }

    /**
     * RS_Baseサーバの共有フォルダへのアクセス制限の有無を返す。
     * @return アクセスを制限する場合は、{@code true}
     */
    public static boolean getRsbAcsCtrl() {
        return stub.getRsbAcsCtrl();
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザ名を設定する。
     * @param userName 共有フォルダへアクセスできるWindowsのユーザ名
     */
    public static void setRsbUserName(String userName) {
        stub.setRsbUserName(userName);
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザ名を返す。
     * @return 共有フォルダへアクセスできるWindowsのユーザ名
     */
    public static String getRsbUserName() {
        return stub.getRsbUserName();
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザのパスワードを設定する。
     * @param password パスワード
     */
    public static void setRsbPassword(String password) {
        stub.setRsbPassword(password);
    }

    /**
     * RS_Baseサーバの共有フォルダへアクセスできるWindowsのユーザのパスワードを返す。
     * @return パスワード
     */
    public static String getRsbPassword() {
        return stub.getRsbPassword();
    }
}
