/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import open.dolphin.client.IChart;
import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.infomodel.HealthInsuranceModel;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.service.IPvtService;

/**
 *
 * @author tomohiro
 */
public abstract class PVTDelegater extends DelegaterErrorHandler {

    /**
     * 受付患者のインスタンスを取得する
     * @return 受付患者のインスタンス
     * @throws NamingException
     */
    protected abstract IPvtService getService() throws NamingException;

    /**
     *
     * @param pvtModel 患者来院情報
     * @return 登録個数
     * 登録できた場合: 1
     * 例外が発生した場合: 0
     */
    public int addPvt(PatientVisitModel pvtModel) {
        try {
            return getService().addPvt(pvtModel);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param date
     * @param firstRecord
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    public List<PatientVisitModel> getPvt(String[] date, int firstRecord) {

        PatientVisitSpec spec = new PatientVisitSpec();
        spec.setDate(date[0]);
        spec.setAppodateFrom(date[1]);
        spec.setAppodateTo(date[2]);
        spec.setSkipCount(firstRecord);

        try {
            List<PatientVisitModel> patients = getService().getPvt(spec);
            if (patients == null) {
                return null;
            }
            for (PatientVisitModel model : patients) {
                PatientModel patient = model.getPatient();
                decodeHealthInsurance(patient);
            }
            return patients;
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }

        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public int removePvt(long id) {
        try {
            return getService().removePvt(id);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param pk
     * @param state
     * @return
     */
    //   public int updatePvtState(long pk, int state) {
    //       try {
    //        return getService().updatePvtState(pk, state);
    //      } catch (Exception e) {
    //          dispatchError(getClass(), e, "");
    //     }
    //     return 0;
    // }
    public int updatePvtState(long pk, IChart.state state) {
        try {
            return getService().updatePvtState(pk, state);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     * バイナリの健康保険データをオブジェクトにデコードする。
     * @param patient 患者モデル
     */
    private void decodeHealthInsurance(PatientModel patient) {

        // Health Insurance を変換をする beanXML2PVT
        Set<HealthInsuranceModel> c = patient.getHealthInsurances();

        if (c != null) {
            List<PVTHealthInsuranceModel> list = new ArrayList<PVTHealthInsuranceModel>(c.size());
            for (HealthInsuranceModel model : c) {
                try {
                    PVTHealthInsuranceModel hModel = model.toInfoModel();
                    list.add(hModel);
                } catch (Exception e) {
                    dispatchError(getClass(), e, "");
                }
            }

            patient.setPvtHealthInsurances(list);
            patient.getHealthInsurances().clear();
            patient.setHealthInsurances(null);
        }
    }
}
