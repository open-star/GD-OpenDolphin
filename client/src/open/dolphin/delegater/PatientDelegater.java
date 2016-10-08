/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.HealthInsuranceModel;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.service.IPatientService;

/**
 *
 * @author tomohiro
 */
public abstract class PatientDelegater extends DelegaterErrorHandler {

    /**
     * 
     * @return
     * @throws NamingException
     */
    protected abstract IPatientService getService() throws NamingException;

    /**
     *
     * @param qId
     * @return
     */
    public PatientModel getPatient(String qId) {
        try {
            PatientModel ret = getService().getPatient(qId);
            decodeHealthInsurance(ret);
            return ret;
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param spec
     * @return
     */
    public List<PatientModel> getPatients(PatientSearchSpec spec) {
        try {
            List<PatientModel> patients = getService().getPatients(spec);
            if (patients != null) {
                for (PatientModel patient : patients) {
                    decodeHealthInsurance(patient);
                }
                return patients;
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param patientValue
     * @return
     */
    public long putPatient(PatientModel patientValue) {
        try {
            return getService().addPatient(patientValue);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0L;
    }

    /**
     *
     * @param patient
     * @return
     */
    public int updatePatient(PatientModel patient) {
        try {
            return getService().update(patient);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return -1;
    }

    /**
     * バイナリの健康保険データをオブジェクトにデコードする。
     * @param patient 患者モデル
     */
    private void decodeHealthInsurance(PatientModel patient) {
        Set<HealthInsuranceModel> insurances;
        try {
            // Health Insurance を変換をする beanXML2PVT
            insurances = patient.getHealthInsurances();
            if (insurances != null) {
                for (HealthInsuranceModel model : insurances) {
                    PVTHealthInsuranceModel hModel = (PVTHealthInsuranceModel) model.toInfoModel();
                    patient.addPvtHealthInsurance(hModel);
                }
                insurances.clear();
                patient.setHealthInsurances(null);
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
    }
}
