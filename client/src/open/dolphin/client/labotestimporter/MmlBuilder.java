package open.dolphin.client.labotestimporter;

import java.util.Date;
import java.util.List;

import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.LaboItemValue;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.LaboSpecimenValue;
import open.dolphin.infomodel.ModelUtils;

import open.dolphin.infomodel.UserModel;
import open.dolphin.labotestimporter.translater.HkkInspectionStatus;
import open.dolphin.project.GlobalVariables;

/**
 * LaboModuleBuilder
 *
 * 
 */
public class MmlBuilder {

    private final List<LaboModuleValue> allModules;

    /**
     *
     * @param allModules
     */
    public MmlBuilder(List<LaboModuleValue> allModules) {
        this.allModules = allModules;
    }

    /**
     *
     * @param information
     * @return
     */
    private boolean updateModule(LaboTestInformation information) {
        //  String id = information.getSampleTime().substring(8);
        String id = information.getPatientId();
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
     * @throws LaboTestIsNotFinalReportException
     */
    private void insertModule(LaboTestInformation information) throws LaboTestIsNotFinalReportException {
        LaboModuleValue value = new LaboModuleValue();
        if (buildOneModule(value, information)) {
            allModules.add(value);
            return;
        }
        throw new LaboTestIsNotFinalReportException(information);
    }

    /**
     *
     * @param laboTestInformations
     * @throws LaboTestIsNotFinalReportException
     */
    public void build(List<LaboTestInformation> laboTestInformations) throws LaboTestIsNotFinalReportException {
        for (LaboTestInformation laboTestInformation : laboTestInformations) {
            if (!updateModule(laboTestInformation)) {
                insertModule(laboTestInformation);
            }
        }
    }

    /**
     *
     * @param laboModuleValue
     * @param laboTestInformation
     * @return
     */
    private boolean buildOneModule(LaboModuleValue laboModuleValue, LaboTestInformation laboTestInformation) {

        boolean result = false;
        if (laboTestInformation.getReportStatus().equals(HkkInspectionStatus.END)) {
            UserModel user = GlobalVariables.getUserModel();
            FacilityModel facility = user.getFacility();
            String facilityName = facility.getFacilityName();
            String facilityId = facility.getFacilityId();

            laboModuleValue.setSetName("");
            laboModuleValue.setSetCode("");
            laboModuleValue.setSetCodeId("");

            laboModuleValue.setCreator(user);
            String sampleTime = laboTestInformation.getSampleTime();
            String patientId = laboTestInformation.getPatientId();

            laboModuleValue.setPatientId(patientId);

            laboModuleValue.setPatientIdType(laboTestInformation.getPatientIdType());

            laboModuleValue.setPatientIdTypeCodeSys(laboTestInformation.getPatientIdTypeTableId());

            laboModuleValue.setDocId(laboTestInformation.getModuleUUID());
            Date confirmed = ModelUtils.getDateTimeAsObject(laboTestInformation.getConfirmedDate());

            laboModuleValue.setConfirmed(confirmed);

            laboModuleValue.setStarted(confirmed);

            laboModuleValue.setRecorded(new Date());
            laboModuleValue.setStatus("F");
            laboModuleValue.setRegistId(laboTestInformation.getRegistId());

            laboModuleValue.setSampleTime(sampleTime);
            laboModuleValue.setRegistTime(laboTestInformation.getRegistTime());
            laboModuleValue.setReportTime(laboTestInformation.getReportTime());

            laboModuleValue.setReportStatus(laboTestInformation.getReportStatus());

            laboModuleValue.setReportStatusCode(laboTestInformation.getReportStatusCode());
            laboModuleValue.setReportStatusCodeId(laboTestInformation.getReportStatusCodeType());

            laboModuleValue.setClientFacility(facilityName);//laboTestInformation.getClientFacility());
            laboModuleValue.setClientFacilityCode(facilityId);//laboTestInformation.getClientFacilityCode());
            laboModuleValue.setClientFacilityCodeId(laboTestInformation.getClientFacilityCodeId());

            laboModuleValue.setLaboratoryCenter(laboTestInformation.getLaboratoryCenter());
            laboModuleValue.setLaboratoryCenterCode(laboTestInformation.getLaboratoryCenterCode());
            laboModuleValue.setLaboratoryCenterCodeId(laboTestInformation.getLaboratoryCenterCodeId());

            LaboSpecimenValue laboSpecimenValue = new LaboSpecimenValue();
            laboSpecimenValue.setSpecimenName(laboTestInformation.getSpecimenName());
            laboSpecimenValue.setSpecimenCode(laboTestInformation.getSpecimenCode());
            laboSpecimenValue.setSpecimenCodeId(laboTestInformation.getSpecimenCodeId());
            laboSpecimenValue.setLaboModule(laboModuleValue);	// 関係を設定する
            laboModuleValue.addLaboSpecimen(laboSpecimenValue);

            int laboTestInformationSize = laboTestInformation.laboTestResultInformationSize();
            for (int index = 0; index < laboTestInformationSize; index++) {

                LaboItemValue laboItemValue = new LaboItemValue();
                LaboTestResultInformation item = laboTestInformation.getLaboTestResultInformation(index);
                laboItemValue.setItemName(item.getItemName());
                laboItemValue.setItemCode(item.getItemCode());
                laboItemValue.setItemCodeId(item.getItemCodeId());
                laboItemValue.setItemValue(item.getValue());
                laboItemValue.setAcode(item.getComment1());
                laboItemValue.setIcode(item.getComment2());

                AverageInformation average = laboTestInformation.getAverageInformation(index);
                laboItemValue.setUp(average.getUp());
                laboItemValue.setLow(average.getLow());
                laboItemValue.setNormal(average.getNormal());
                laboItemValue.setNout(average.getOut());
                laboItemValue.setUnit(average.getUnit());
                laboItemValue.setUnitCode(average.getUnitCode());
                laboItemValue.setUnitCodeId(average.getUnitCodeId());

                laboItemValue.setLaboSpecimen(laboSpecimenValue);
                laboSpecimenValue.addLaboItem(laboItemValue);
            }
            result = true;
        }
        return result;
    }
}
