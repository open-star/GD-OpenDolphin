package open.dolphin.service.remote;

import java.util.ArrayList;
import open.dolphin.service.ILaboService;
import java.util.List;

import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;

/**
 *
 * 検査情報 MEMO:マッピング
 *
 * @author
 */
public class RemoteLaboService extends DolphinService implements ILaboService {

    /**
     *
     */
    public RemoteLaboService() {
    }

    /**
     * カルテの参照
     * @param patientId　患者ID
     * @return
     * @throws HibernateException
     */
    @Override
    public KarteBean getKarteFor(String patientId) throws HibernateException {
        roleAllowed("user");

        KarteBean karte = null;

        try {
            startTransaction();

            String facilityId = this.getCallersFacilityId();

            PatientModel exist = (PatientModel) getSession().createQuery("from PatientModel p where p.facilityId = :fid and p.patientId = :pid").setParameter("fid", facilityId).setParameter("pid", patientId).uniqueResult();
            if (exist == null) {
                throw new HibernateException("No Such Patient");
            }

            karte = (KarteBean) getSession().createQuery("from KarteBean k where k.patient.id = :pk").setParameter("pk", exist.getId()).uniqueResult();

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
            if (e.getMessage().equals("No Such Patient")) {
                throw e;
            }
        }
        return karte;
    }

    /**
     * LaboModuleを保存する。
     * @param laboModuleValue LaboModuleValue
     * @return
     */
    @Override
    public PatientModel putLaboModule(LaboModuleValue laboModuleValue) {

        roleAllowed("user");

        PatientModel exist = null;

        try {
            startTransaction();

            // MMLファイルをパースした結果が登録される
            // 施設IDはコンテキストから取得する
            String facilityId = this.getCallersFacilityId();

            // 施設IDと LaboModule の患者IDで 患者を取得する
            exist = (PatientModel) getSession().createQuery("from PatientModel p where p.facilityId = :fid and p.patientId = :pid").setParameter("fid", facilityId).setParameter("pid", laboModuleValue.getPatientId()).uniqueResult();
            if (exist == null) {
                throw new HibernateException("No Such Patient");
            }

            // 患者のカルテを取得する
            KarteBean karte = (KarteBean) getSession().createQuery("from KarteBean k where k.patient.id = :pk").setParameter("pk", exist.getId()).uniqueResult();

            laboModuleValue.setKarte(karte);

            getSession().persist(laboModuleValue);

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
            if (e.getMessage().equals("No Such Patient")) {
                throw e;
            }
        }
        return exist;
    }

    /**
     * カルテIDから検査データを取り出す
     * @param karteId
     * @return 検査データ
     */
    @Override
    public List<LaboModuleValue> getModulesFor(long karteId) {
        roleAllowed("user");

        List<LaboModuleValue> modules = new ArrayList<LaboModuleValue>();
        try {
            startTransaction();
            modules = getSession().createQuery("from LaboModuleValue where karte_id = :karteId").setParameter("karteId", karteId).list();
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return modules;
    }
}
