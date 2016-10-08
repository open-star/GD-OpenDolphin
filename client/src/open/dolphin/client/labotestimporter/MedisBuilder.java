package open.dolphin.client.labotestimporter;

import java.util.Date;
import java.util.List;
import java.util.Set;
import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.LaboItemValue;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.LaboSpecimenValue;

import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.UserModel;
import open.dolphin.labotestimporter.translater.HkkInspectionStatus;
import open.dolphin.labotestimporter.translater.HkkInspectionSubjectName;
import open.dolphin.project.GlobalVariables;

import open.dolphin.utils.GUIDGenerator;

/**
 * MedisBuilder
 */
public class MedisBuilder {

    private List<LaboModuleValue> allModules;

    /**
     *
     * @param allModules
     */
    public MedisBuilder(List<LaboModuleValue> allModules) {
        this.allModules = allModules;
    }
    /*
    public void build(List<LaboTestInformation> laboTestInformations) throws Exception {

    String currentID = "";
    LaboModuleValue currentLaboModuleValue = null;
    for (LaboTestInformation laboTestInformation : laboTestInformations) {
    String id = laboTestInformation.getSampleTime().substring(8);
    if (!currentID.equals(id)) {
    currentID = id;
    currentLaboModuleValue = new LaboModuleValue();
    allModules.add(currentLaboModuleValue);
    }
    buildOneModule(currentLaboModuleValue, laboTestInformation);
    }
    }
     */

    /**
     *
     * @param information
     * @return
     */
    private boolean updateModule(LaboTestInformation information) {
        String id = information.getSampleTime().substring(8);
        for (LaboModuleValue value : allModules) {
            if (value.getPatientId().equals(id)) {
                buildOneModule(value, information);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param information
     */
    private void insertModule(LaboTestInformation information) {
        LaboModuleValue value = new LaboModuleValue();
        if (buildOneModule(value, information)) {
            allModules.add(value);
        }
    }

    /**
     *
     * @param laboTestInformations
     */
    public void build(List<LaboTestInformation> laboTestInformations) {
        for (LaboTestInformation laboTestInformation : laboTestInformations) {
            if (!updateModule(laboTestInformation)) {
                insertModule(laboTestInformation);
            }
        }
    }

    /**
     *
     * @param yyyymmdd
     * @return
     */
    private String formatDate(String yyyymmdd) {
        return yyyymmdd.substring(0, 4) + "-" + yyyymmdd.substring(4, 6) + "-" + yyyymmdd.substring(6, 8) + "T00:00:00";
    }

    /**
     *
     * @param laboModuleValue
     * @param laboTestInformation
     * @return
     */
    private boolean buildOneModule(LaboModuleValue laboModuleValue, LaboTestInformation laboTestInformation) {

        //  boolean result = false;
        if (laboTestInformation.getStatus().equals("E")) {
            UserModel user = GlobalVariables.getUserModel();
            FacilityModel facility = user.getFacility();
            String facilityName = facility.getFacilityName();
            String facilityId = facility.getFacilityId();

            laboModuleValue.setSetName("");
            laboModuleValue.setSetCode("");
            laboModuleValue.setSetCodeId("");

            laboModuleValue.setCreator(user);
            String sampleTime = laboTestInformation.getSampleTime().substring(0, 8);
            String patientId = laboTestInformation.getSampleTime().substring(8);

            laboModuleValue.setPatientId(patientId);

            laboModuleValue.setPatientIdType("local"); //FIX

            laboModuleValue.setPatientIdTypeCodeSys("MML0024");  //FIX

            laboModuleValue.setDocId(GUIDGenerator.generate(this));
            Date confirmed = ModelUtils.getDateFromString(laboTestInformation.getConfirmedDate(), "yyyyMMdd");

            laboModuleValue.setConfirmed(confirmed);

            laboModuleValue.setStarted(confirmed);

            laboModuleValue.setRecorded(new Date());
            laboModuleValue.setStatus("F");
            laboModuleValue.setRegistId("");

            laboModuleValue.setSampleTime(formatDate(sampleTime));
            laboModuleValue.setRegistTime(formatDate(laboTestInformation.getRegistTime()));
            laboModuleValue.setReportTime(formatDate(laboTestInformation.getRegistTime()));

            laboModuleValue.setReportStatus(HkkInspectionStatus.toName(laboTestInformation.getStatus()));
            laboModuleValue.setReportStatusCode("final");  //FIX
            laboModuleValue.setReportStatusCodeId("mmlLb0001"); // ???

            laboModuleValue.setClientFacility(facilityName);
            laboModuleValue.setClientFacilityCode(facilityId);
            laboModuleValue.setClientFacilityCodeId("JMARI");

            laboModuleValue.setLaboratoryCenter(laboTestInformation.getCenterCode());
            laboModuleValue.setLaboratoryCenterCode(laboTestInformation.getCenterCode());//FIX

            //    laboModuleValue.setLaboratoryCenter("保健科学研究所");
            //     laboModuleValue.setLaboratoryCenterCode("HKK");//FIX
            laboModuleValue.setLaboratoryCenterCodeId("JMARI");

            //Medisには検体自体に関する情報が含まれていないため、Specimenは固定の値としている。
            LaboSpecimenValue laboSpecimenValue;
            if (laboModuleValue.getLaboSpecimens() == null) {
                laboSpecimenValue = new LaboSpecimenValue();
                laboSpecimenValue.setSpecimenName("血清"); //FIX
                laboSpecimenValue.setSpecimenCode("99");//FIX
                laboSpecimenValue.setSpecimenCodeId("Nihonrinsyo_ZaiCode"); //FIX
                laboSpecimenValue.setLaboModule(laboModuleValue);	// 関係を設定する
                laboModuleValue.addLaboSpecimen(laboSpecimenValue);
            } else {
                Set<LaboSpecimenValue> c = laboModuleValue.getLaboSpecimens();
                laboSpecimenValue = c.iterator().next();
            }

            int laboTestInformationSize = laboTestInformation.laboTestResultInformationSize();
            for (int index = 0; index < laboTestInformationSize; index++) {

                LaboItemValue laboItemValue = new LaboItemValue();

                LaboTestResultInformation item = laboTestInformation.getLaboTestResultInformation(index);
                laboItemValue.setItemName(HkkInspectionSubjectName.toName(item.getItemCode()));
                laboItemValue.setItemCode(item.getItemCode());
                laboItemValue.setItemCodeId("HokenKagaku_ItCode"); //FIX
                laboItemValue.setItemValue(item.getValue());
                laboItemValue.setAcode(item.getComment1());
                laboItemValue.setIcode(item.getComment2());

                AverageInformation average = laboTestInformation.getAverageInformation(index);
                laboItemValue.setUp(average.getUp());
                laboItemValue.setLow(average.getLow());
                laboItemValue.setNormal(average.getNormal());
                laboItemValue.setNout("N");    // FIX
                laboItemValue.setUnit(average.getUnit());
                laboItemValue.setUnitCode(average.getUnitCode());
                laboItemValue.setUnitCodeId(average.getUnitCodeId());

                laboItemValue.setLaboSpecimen(laboSpecimenValue);
                laboSpecimenValue.addLaboItem(laboItemValue);
            }
            return true;
        }
        return false;
    }
}
