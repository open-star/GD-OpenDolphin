package open.dolphin.service.remote;

import java.util.ArrayList;
import open.dolphin.service.IPvtService;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import open.dolphin.client.IChart;

import open.dolphin.dto.PatientVisitSpec;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.HealthInsuranceModel;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;

/**
 * 受付患者
 * RemotePvtService
 * @author Minagawa,Kazushi
 *
 */
public class  RemotePvtService extends DolphinService implements IPvtService {

    /**
     *
     */
    public RemotePvtService() {
    }
    
    /**
     * 患者来院情報を登録する。
     * @param pvt 患者来院情報
     * @return 登録個数(常に 1)
     * @exception NoResultException 指定した患者IDの来院情報が存在しない場合（新患者の場合）
     */
    @Override
    public int addPvt(PatientVisitModel pvt) {

        roleAllowed("user");

        PatientModel patient = pvt.getPatient();
        
        // CLAIM 送信の場合 facilityID がデータベースに登録されているものと異なる場合がある
        // 施設IDを認証にパスしたユーザの施設IDに設定する。
        String facilityId = getCallersFacilityId();
        pvt.setFacilityId(facilityId);
        patient.setFacilityId(facilityId);

        try {
            startTransaction();

            // 既存の患者かどうか調べる
            try {
                Object existModel = getSession()
                                         .createQuery("from PatientModel where facilityid = :fid and patientid = :pid")
                                         .setParameter("fid", facilityId)
                                         .setParameter("pid", patient.getPatientId())
                                         .uniqueResult();

                if (existModel == null) {
                    throw new NoResultException();
                }

                PatientModel existPatient = (PatientModel)existModel;

                //
                // 健康保険情報を更新する
                //
                Set<HealthInsuranceModel> ins = patient.getHealthInsurances();
                if (ins != null && ins.size() > 0) {

                    // 健康保険を更新する
                    List<HealthInsuranceModel> olds = new ArrayList<HealthInsuranceModel>(existPatient.getHealthInsurances());

                    // 現在の保険情報を削除する
                    for (HealthInsuranceModel model : olds) {
                        getSession().delete(model);
                    }

                    // 新しい健康保険情報を登録する
                    existPatient.setHealthInsurances(patient.getHealthInsurances());
                }

                // 名前を更新する 2007-04-12
                existPatient.setFamilyName(patient.getFamilyName());
                existPatient.setGivenName(patient.getGivenName());
                existPatient.setFullName(patient.getFullName());
                existPatient.setKanaFamilyName(patient.getKanaFamilyName());
                existPatient.setKanaGivenName(patient.getKanaGivenName());
                existPatient.setKanaName(patient.getKanaName());
                existPatient.setRomanFamilyName(patient.getRomanFamilyName());
                existPatient.setRomanGivenName(patient.getRomanGivenName());
                existPatient.setRomanName(patient.getRomanName());

                // 性別
                existPatient.setGender(patient.getGender());
                existPatient.setGenderDesc(patient.getGenderDesc());
                existPatient.setGenderCodeSys(patient.getGenderCodeSys());

                // Birthday
                existPatient.setBirthday(patient.getBirthday());

                // 住所、電話を更新する
                existPatient.setAddress(patient.getAddress());
                existPatient.setTelephone(patient.getTelephone());
                //exist.setMobilePhone(patient.getMobilePhone());

                // PatientVisit との関係を設定する
                pvt.setPatient(existPatient);

            } catch (NoResultException e) {
                // 新規患者であれば登録する
                // 患者属性は cascade=PERSIST で自動的に保存される
                getSession().persist(patient);

                // この患者のカルテを生成する
                KarteBean karte = new KarteBean();
                karte.setPatient(patient);
                karte.setCreated(new Date());
                getSession().persist(karte);
            }

            // 来院情報を登録する
            // CLAIM の仕様により患者情報のみを登録し、来院情報はない場合がある
            // それを pvtDate の属性で判断している
            if (pvt.getPvtDate() != null) {
                getSession().persist(pvt);
            }

            endTransaction();
        }
        catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        
        return 1;
    }
    
    /**
     * 施設の患者来院情報を取得する。
     * @param spec 検索仕様DTOオブジェクト
     * @return 来院情報のList
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PatientVisitModel> getPvt(PatientVisitSpec spec) {

        String date = spec.getDate();
        if (!date.endsWith("%")) {
            date = date + "%";
        }
        int index = date.indexOf('%');
        Date theDate = ModelUtils.getDateAsObject(date.substring(0, index));
        int firstResult = spec.getSkipCount();
        String fid = getCallersFacilityId();
        
        String appoDateFrom = spec.getAppodateFrom();
        String appoDateTo = spec.getAppodateTo();
        boolean searchAppo = (appoDateFrom != null && appoDateTo != null) ? true : false;

        List<PatientVisitModel> result = null;

        try {
            startTransaction();

            result = getSession()
                              .createQuery("from PatientVisitModel where facilityid = :fid and pvtdate >= :date order by pvtdate")
                              .setParameter("fid", fid)
                              .setParameter("date", date)
                              .setFirstResult(firstResult)
                              .list();

            // 患者の基本データを取得する
            // 来院情報と患者は ManyToOne の関係である
            for (PatientVisitModel pvt : result) {

                PatientModel patient = pvt.getPatient();

                // 患者の健康保険を取得する
                List<HealthInsuranceModel> insurances = getSession()
                                                            .createQuery("from HealthInsuranceModel where patient_id = :pk")
                                                            .setParameter("pk", patient.getId())
                                                            .list();
                patient.setHealthInsurances(new LinkedHashSet(insurances));

                // 予約を検索する
                if (searchAppo) {
                    KarteBean karte = (KarteBean)getSession()
                                          .createQuery("from KarteBean where patient_id = :pk")
                                          .setParameter("pk", patient.getId())
                                          .uniqueResult();
                    // カルテの PK を得る
                    long karteId = karte.getId();

                    List<AppointmentModel> c = getSession()
                                                   .createQuery("from AppointmentModel where karte_id = :karteId and date = :date")
                                                   .setParameter("karteId", karteId)
                                                   .setParameter("date", theDate)
                                                   .list();
                    if (c != null && c.size() > 0) {
                        AppointmentModel appo = (AppointmentModel) c.get(0);
                        pvt.setAppointment(appo.getName());
                    }
                }
            }

            endTransaction();
        }
        catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        
        return result;
    }
    
    /**
     * 受付情報を削除する。
     * @param id 受付レコード
     * @return 削除件数
     */
    @Override
    public int removePvt(long id) {

        roleAllowed("user");

        int ret = 0;
        
        try {
            startTransaction();

            PatientVisitModel exist = (PatientVisitModel)getSession()
                                          .createQuery("from PatientVisitModel where id = :id")
                                          .setParameter("id", id)
                                          .uniqueResult();
            getSession().delete(exist);

            ret = 1;

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return ret;
    }
    
    /**
     * 診察終了情報を書き込む。
     * @param pk レコードID
     * @param state 診察終了の時 1
     * @return
     */
    @Override
    public int updatePvtState(long pk, IChart.state state) {

        roleAllowed("user");

        try {
            startTransaction();

            PatientVisitModel exist = (PatientVisitModel)getSession()
                                          .createQuery("from PatientVisitModel where id = :id")
                                          .setParameter("id", pk)
                                          .uniqueResult();
            exist.setState(state);

            endTransaction();
        }
        catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return 1;
    }
}
