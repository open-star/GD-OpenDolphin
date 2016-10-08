package open.dolphin.master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.File;

import open.dolphin.infomodel.DepartmentModel;
import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.LicenseModel;
import open.dolphin.infomodel.UserModel;

import open.dolphin.service.ISystemService;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;
import open.dolphin.security.EncryptUtil;
import open.dolphin.service.remote.RemoteSystemService;
import open.dolphin.utils.HibernateUtil;

/**
 *
 *  @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class DatabaseInitializer {

    private static final String MEMBER_TYPE = "FACILITY_USER";
    private static final String DEFAULT_FACILITY_OID = "1.3.6.1.4.1.9414.10.1";
    private static final String PROFILE_RESOURCE = "/open/dolphin/master/profile_ja.properties";
    private static final String RAD_METHOD_RESOURCE = "/open/dolphin/master/radiology_method_data.cvs";
    private final String delimiter = ",";
    private ISystemService service;
    // License
    private final String licenseName = "doctor";
    private final String licenseDesc = "医師";
    private final String licenseCodeSys = "MML0026";
    // Department
    private final String department = "01";
    private final String departmentDesc = "内科";
    private final String departmentCodeSys = "MML0028";
    // Email Address
    private final String defaultEmailAddress = "someone@example.com";

    /**
     *
     * @param userId
     * @param password
     */
    public DatabaseInitializer(String userId, String password) {

        LogWriter.info(getClass(), "データベーススキーマを設定しています");
        createSchema();
        LogWriter.info(getClass(), "データベースの初期化を開始します");
        addDatabaseAdmin(userId, password);
        addDolphinMaster();
        LogWriter.info(getClass(), "データベースを初期化しました");
    }

    /**
     *
     */
    private void createSchema() {
        HibernateUtil.createSchema();
    }

    /**
     *
     * @return
     */
    private Properties importProfileResoruce() {

        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream(PROFILE_RESOURCE));
        } catch (IOException ex) {
            LogWriter.error(getClass(), "管理者情報ファイルの読み込みに失敗しました: " + ex.toString());
            System.exit(-1);
        }
        LogWriter.info(getClass(), "管理者情報ファイルを読み込みました");

        return properties;
    }

    /**
     * MEMO: unused?
     * @param str
     * @return
     */
    private boolean isEmpty(String str) {
        return str == null | str.equals("");
    }

    /**
     *
     * @param userId
     * @param password
     */
    public void addDatabaseAdmin(String userId, String password) {
        try {
            Properties properties = importProfileResoruce();

            Date date = new Date();

            FacilityModel facility = new FacilityModel();
            setupFacility(facility, properties);
            facility.setRegisteredDate(date);

            UserModel admin = new UserModel();
            seupAdminUser(admin, properties, userId, password);
            admin.setRegisteredDate(date);
            admin.setFacility(facility);

            this.service = new RemoteSystemService();
            service.addFacilityAdmin(admin);
            LogWriter.info(getClass(), "管理者を登録しました。");

        } catch (Exception ex) {
            LogWriter.error(getClass(), "管理者情報の登録に失敗しました。", ex);
        }
    }

    private void seupAdminUser(UserModel admin, Properties properties, String userId, String password) {
        admin.setUserId(properties.getProperty("admin.login.id", userId));
        password = properties.getProperty("admin.login.password", password);
        admin.setPassword(EncryptUtil.createPasswordHash(password));
        admin.setSirName(properties.getProperty("admin.sir.name"));
        admin.setGivenName(properties.getProperty("admin.given.name"));
        admin.setCommonName(admin.getSirName() + " " + admin.getGivenName());
        setLicenseToUser(admin);
        setDepartmentToUser(admin);
        admin.setEmail(properties.getProperty("admin.email", defaultEmailAddress));
        admin.setMemberType(MEMBER_TYPE);
    }

    private void setDepartmentToUser(UserModel admin) {
        DepartmentModel dept = new DepartmentModel();
        dept.setDepartment(department);
        dept.setDepartmentDesc(departmentDesc);
        dept.setDepartmentCodeSys(departmentCodeSys);
        admin.setDepartmentModel(dept);
    }

    private void setLicenseToUser(UserModel admin) {
        LicenseModel license = new LicenseModel();
        license.setLicense(licenseName);
        license.setLicenseDesc(licenseDesc);
        license.setLicenseCodeSys(licenseCodeSys);
        admin.setLicenseModel(license);
    }

    private void setupFacility(FacilityModel facility, Properties properties) {
        facility.setFacilityId(DEFAULT_FACILITY_OID);
        facility.setFacilityName(properties.getProperty("facility.name"));
        facility.setZipCode(properties.getProperty("facility.zipcode"));
        facility.setAddress(properties.getProperty("facility.address"));
        facility.setTelephone(properties.getProperty("facility.telephone"));
        facility.setUrl(properties.getProperty("facility.url"));
        facility.setMemberType(MEMBER_TYPE);
    }

    /**
     *
     */
    public void addDolphinMaster() {
        try {
            addRdMethod(RAD_METHOD_RESOURCE);
        } catch (IOException e) {
            LogWriter.error(getClass(), e);
        } catch (Exception ee) {
            LogWriter.error(getClass(), ee);
        }

    }

    private void addRdMethod(String name) throws IOException {

        List<RadiologyMethodValue> list = new ArrayList<RadiologyMethodValue>();

        for (List<String> data : importRadiologyData()) {

            RadiologyMethodValue radiologyMethodValue = new RadiologyMethodValue();
            radiologyMethodValue.setHierarchyCode1(data.get(0));
            radiologyMethodValue.setHierarchyCode2(data.get(1));
            radiologyMethodValue.setMethodName(data.get(2));

            list.add(radiologyMethodValue);
        }

        if (list.isEmpty()) {
            return;
        }

        service.putRadMethodMaster(list);
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    public List<List<String>> importRadiologyData() throws IOException {
        List<List<String>> result = new ArrayList<List<String>>();
        InputStreamReader ir = new InputStreamReader(getClass().getResourceAsStream(RAD_METHOD_RESOURCE));
        BufferedReader reader = new BufferedReader(ir);
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                result.add(parseLine(line));
            }
        } catch (IOException ex) {
            LogWriter.error(getClass(), "画像診断マスタファイルの読み込みに失敗しました");
        } finally {
            reader.close();
            ir.close();
        }
        return result;
    }

    private List<String> parseLine(String line) {
        List<String> result = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(line, delimiter, true);
        String buf = "";
        while (tokenizer.hasMoreTokens()) {
            String now = tokenizer.nextToken();
            if (now.equals(delimiter)) {
                result.add(buf.trim());
                buf = "";
                continue;
            }
            buf = now;
        }
        result.add(buf.trim());
        return result;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        GlobalVariables.createGlobalVariables();
        GlobalVariables.setDbAddress("localhost");
        GlobalVariables.setDbPassword("nyanco");
        GlobalVariables.setDbSSLState(false);

        StringBuilder logFilePath = new StringBuilder();
        logFilePath.append(File.separator);
        logFilePath.append("var");
        logFilePath.append(File.separator + "log");
        logFilePath.append(File.separator + "OpenDolphin");
        logFilePath.append(File.separator + "install.log");
        LogWriter.config(logFilePath.toString(), "DEBUG");

        if (args.length > 0) {
            new DatabaseInitializer(args[0], args[1]);
        } else {
            new DatabaseInitializer(null, null);
        }
    }
}
