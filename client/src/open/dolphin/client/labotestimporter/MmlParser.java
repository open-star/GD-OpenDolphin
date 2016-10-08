package open.dolphin.client.labotestimporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import open.dolphin.utils.GUIDGenerator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 *
 *
 */
public class MmlParser {

    //private static final Namespace xhtml = Namespace.getNamespace("xhtml","http://www.w3.org/1999/xhtml");
    private static final Namespace mmlCm = Namespace.getNamespace("mmlCm", "http://www.medxml.net/MML/SharedComponent/Common/1.0");
    //private static final Namespace mmlNm = Namespace.getNamespace("mmlNm","http://www.medxml.net/MML/SharedComponent/Name/1.0");
    //private static final Namespace mmlFc = Namespace.getNamespace("mmlFc","http://www.medxml.net/MML/SharedComponent/Facility/1.0");
    //private static final Namespace mmlDp = Namespace.getNamespace("mmlDp","http://www.medxml.net/MML/SharedComponent/Department/1.0");
    //private static final Namespace mmlAd = Namespace.getNamespace("mmlAd","http://www.medxml.net/MML/SharedComponent/Address/1.0");
    //private static final Namespace mmlPh = Namespace.getNamespace("mmlPh","http://www.medxml.net/MML/SharedComponent/Phone/1.0");
    //private static final Namespace mmlPsi = Namespace.getNamespace("mmlPsi","http://www.medxml.net/MML/SharedComponent/PersonalizedInfo/1.0");
    //private static final Namespace mmlCi = Namespace.getNamespace("mmlCi","http://www.medxml.net/MML/SharedComponent/CreatorInfo/1.0");
    //private static final Namespace mmlPi = Namespace.getNamespace("mmlPi","http://www.medxml.net/MML/ContentModule/PatientInfo/1.0");
    //private static final Namespace mmlBc = Namespace.getNamespace("mmlBc","http://www.medxml.net/MML/ContentModule/BaseClinic/1.0");
    //private static final Namespace mmlFcl = Namespace.getNamespace("mmlFcl","http://www.medxml.net/MML/ContentModule/FirstClinic/1.0");
    //private static final Namespace mmlHi = Namespace.getNamespace("mmlHi","http://www.medxml.net/MML/ContentModule/HealthInsurance/1.1");
    //private static final Namespace mmlLs = Namespace.getNamespace("mmlLs","http://www.medxml.net/MML/ContentModule/Lifestyle/1.0");
    //private static final Namespace mmlPc = Namespace.getNamespace("mmlPc","http://www.medxml.net/MML/ContentModule/ProgressCourse/1.0");
    //private static final Namespace mmlRd = Namespace.getNamespace("mmlRd","http://www.medxml.net/MML/ContentModule/RegisteredDiagnosis/1.0");
    //private static final Namespace mmlSg = Namespace.getNamespace("mmlSg","http://www.medxml.net/MML/ContentModule/Surgery/1.0");
    //private static final Namespace mmlSm = Namespace.getNamespace("mmlSm","http://www.medxml.net/MML/ContentModule/Summary/1.0");
    private static final Namespace mmlLb = Namespace.getNamespace("mmlLb", "http://www.medxml.net/MML/ContentModule/test/1.0");
    //private static final Namespace mmlRp = Namespace.getNamespace("mmlRp","http://www.medxml.net/MML/ContentModule/report/1.0");
    //private static final Namespace mmlRe = Namespace.getNamespace("mmlRe","http://www.medxml.net/MML/ContentModule/Referral/1.0");
    //private static final Namespace mmlSc = Namespace.getNamespace("mmlSc","http://www.medxml.net/MML/SharedComponent/Security/1.0");
    //private static final Namespace claim = Namespace.getNamespace("claim","http://www.medxml.net/claim/claimModule/2.1");
    //private static final Namespace claimA = Namespace.getNamespace("claimA","http://www.medxml.net/claim/claimAmountModule/2.1");
    private LaboTestInformation laboTestInformation;
    private String patientId;
    private String patientIdType;
    private String patientIdTypeTableId;
    private String moduleUUID;
    private String confirmDate;

    /**
     *
     */
    public MmlParser() {
    }

    /**
     *
     * @param is
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    public List<LaboTestInformation> parse(InputStream is) throws IOException, JDOMException {
        SAXBuilder docBuilder = new SAXBuilder();
        Document doc = docBuilder.build(is);
        Element root = doc.getRootElement();

        parseHeader(root.getChild("MmlHeader"));
        return parseBody(root.getChild("MmlBody"));
    }

    /**
     *
     * @param header
     */
    private void parseHeader(Element header) {
        Element masterId = header.getChild("masterId");
        Element id = masterId.getChild("Id", mmlCm);
        if (id == null) {
            //   logger.info("id is null");
            //        LogWriter.info(MmlParser.class, "id is null");
            return;
        }
        patientId = id.getTextTrim();
        patientIdType = id.getAttributeValue("type", mmlCm);
        patientIdTypeTableId = id.getAttributeValue("tableId", mmlCm);
    }

    /**
     * 
     * @param body
     * @return
     */
    private List<LaboTestInformation> parseBody(Element body) {
        @SuppressWarnings("unchecked")
        List<Element> mmlModuleItemList = body.getChildren("MmlModuleItem");
        Iterator<Element> ite = mmlModuleItemList.iterator();
        //   List<LaboTestInformation> laboTestinformationList;
        List<LaboTestInformation> result = new ArrayList<LaboTestInformation>();
        while (ite.hasNext()) {
            Element mmlModuleItem = ite.next();
            Element docInfo = mmlModuleItem.getChild("docInfo");
            Element content = mmlModuleItem.getChild("content");
            if (!docInfo.getAttributeValue("contentModuleType").equals("test")) {
                continue;
            }

            moduleUUID = docInfo.getChild("docId").getChildTextTrim("uid");
            if (moduleUUID == null || moduleUUID.length() != 32) {
                moduleUUID = GUIDGenerator.generate(this);
            }

            confirmDate = docInfo.getChildTextTrim("confirmDate");
            if (confirmDate.indexOf("T") < 0) {
                confirmDate.concat("T00:00:00");
            }

            Element testModule = content.getChild("TestModule", mmlLb);
            //     laboTestinformationList = ;
            //    for (LaboTestInformation info : laboTestinformationList) {
            result.add(parseTestModule(testModule));
            //     }
        }
        return result;
    }

    /**
     * mmlLb:TestModule (1)
     *   - mmlLb:information (1)
     *   - mmlLb:laboTest (n)
     *     - mmlLb:specimen (1)
     *     - mmlLb:item (n)
     *
     * @param testModule
     */
    private LaboTestInformation parseTestModule(Element testModule) {
        laboTestInformation = new LaboTestInformation();
        Namespace ns = testModule.getNamespace();
        Element info = testModule.getChild("information", ns);
        @SuppressWarnings("unchecked")
        List<Element> laboTestList = testModule.getChildren("laboTest", ns);
        Iterator<Element> ite = laboTestList.iterator();
        while (ite.hasNext()) {
            // TODO LaboTestInformation を作って基本的な情報をセットする
            //       laboTestInformation = new LaboTestInformation();
            //   result.add(laboTestInformation);
            parseInformation(info, ns);
            laboTestInformation.setPatientId(patientId);
            laboTestInformation.setPatientIdType(patientIdType);
            laboTestInformation.setPatientIdTypeTableId(patientIdTypeTableId);
            laboTestInformation.setModuleUUID(moduleUUID);
            laboTestInformation.setConfirmedDate(confirmDate);
            Element laboTest = ite.next();
            parseLaboTest(laboTest, ns);
        }
        return laboTestInformation;
    }

    /**
     * information から情報を取得する
     * @param information
     * @param ns
     */
    private void parseInformation(Element information, Namespace ns) {
        laboTestInformation.setRegistId(information.getAttributeValue("registId", ns));
        laboTestInformation.setSampleTime(information.getAttributeValue("sampleTime", ns));
        laboTestInformation.setRegistTime(information.getAttributeValue("registTime", ns));
        laboTestInformation.setReportTime(information.getAttributeValue("reportTime", ns));
        Element reportStatus = information.getChild("reportStatus", ns);
        laboTestInformation.setReportStatus(reportStatus.getTextTrim());
        laboTestInformation.setReportStatusCode(reportStatus.getAttributeValue("statusCode", ns));
        laboTestInformation.setReportStatusCodeType(reportStatus.getAttributeValue("statusCodeId", ns));
        Element facility = information.getChild("facility", ns);
        laboTestInformation.setClientFacility(facility.getTextTrim());
        laboTestInformation.setClientFacilityCode(facility.getAttributeValue("facilityCode", ns));
        laboTestInformation.setClientFacilityCodeId(facility.getAttributeValue("facilityCodeId", ns));
        Element laboratoryCenter = information.getChild("laboratoryCenter", ns);
        laboTestInformation.setLaboratoryCenter(laboratoryCenter.getTextTrim());
        laboTestInformation.setLaboratoryCenterCode(laboratoryCenter.getAttributeValue("centerCode", ns));
        laboTestInformation.setLaboratoryCenterCodeId(laboratoryCenter.getAttributeValue("centerCodeId", ns));
    }

    /**
     * 検査情報
     * @param laboTest
     * @param ns
     */
    private void parseLaboTest(Element laboTest, Namespace ns) {
        Element specimen = laboTest.getChild("specimen", ns);
        parseSpecimen(specimen, ns);
        @SuppressWarnings("unchecked")
        List<Element> itemList = laboTest.getChildren("item", ns);
        Iterator<Element> ite = itemList.iterator();
        while (ite.hasNext()) {
            Element item = ite.next();
            parseItem(item, ns);
        }
    }

    /**
     * 検体情報
     * @param specimen
     * @param ns
     */
    private void parseSpecimen(Element specimen, Namespace ns) {
        Element specimenName = specimen.getChild("specimenName", ns);
        laboTestInformation.setSpecimenName(specimenName.getTextTrim());
        laboTestInformation.setSpecimenCode(specimenName.getAttributeValue("spCode", ns));
        laboTestInformation.setSpecimenCodeId(specimenName.getAttributeValue("spCodeId", ns));
    }

    /**
     * 検査結果,基準値,単位
     * @param item
     * @param ns
     */
    private void parseItem(Element item, Namespace ns) {
        LaboTestResultInformation laboTestResultInformation = new LaboTestResultInformation();
        laboTestInformation.addTestResultInformation(laboTestResultInformation);
        Element itemName = item.getChild("itemName", ns);
        laboTestResultInformation.setItemName(itemName.getTextTrim());
        laboTestResultInformation.setItemCode(itemName.getAttributeValue("itCode", ns));
        laboTestResultInformation.setItemCodeId(itemName.getAttributeValue("itCodeId", ns));
        laboTestResultInformation.setValue(item.getChildTextTrim("value", ns));
        AverageInformation averageInformation = new AverageInformation();
        laboTestInformation.addAverageInformation(averageInformation);
        Element numValue = item.getChild("numValue", ns);
        if (numValue != null) {
            numValue.getTextTrim(); // ???
            averageInformation.setUp(numValue.getAttributeValue("up", ns));
            averageInformation.setLow(numValue.getAttributeValue("low", ns));
            averageInformation.setNormal(numValue.getAttributeValue("normal", ns));
            averageInformation.setOut(numValue.getAttributeValue("out", ns));
            Element unit = item.getChild("unit", ns);
            averageInformation.setUnit(unit.getTextTrim());
            averageInformation.setUnitCode(unit.getAttributeValue("uCode", ns));
            averageInformation.setUnitCodeId(unit.getAttributeValue("uCodeId", ns));
        }
    }
}
