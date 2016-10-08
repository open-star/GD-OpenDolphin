package open.dolphin.service.remote;

import open.dolphin.service.IAppointmentService;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import open.dolphin.dto.AppointSpec;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;

/**
 * 予約情報
 */
public class RemoteAppoService extends DolphinService implements IAppointmentService {

    /**
     *
     */
    public RemoteAppoService() {
    }

    /**
     * 予約を保存、更新、削除する。
     * @param spec 予約情報の DTO
     */
    @Override
    public void putAppointments(AppointSpec spec) {

        roleAllowed("user");

        Collection added = spec.getAdded();
        Collection updated = spec.getUpdared();
        Collection removed = spec.getRemoved();
        AppointmentModel av = null;

        try {
            startTransaction();

            // 登録する
            if (added != null && added.size() > 0) {
                Iterator iter = added.iterator();


                while (iter.hasNext()) {
                    av = (AppointmentModel) iter.next();
                    checkIdAsComposite(av.getPatientId());
                    getSession().persist(av);
                }

            }

            // 更新する
            if (updated != null && updated.size() > 0) {
                Iterator iter = updated.iterator();
                while (iter.hasNext()) {
                    av = (AppointmentModel) iter.next();
                    checkIdAsComposite(av.getPatientId());
                    // av は分離オブジェクトである
                    getSession().merge(av);
                }
            }

            // 削除
            if (removed != null && removed.size() > 0) {
                Iterator iter = removed.iterator();
                while (iter.hasNext()) {
                    av = (AppointmentModel) iter.next();
                    checkIdAsComposite(av.getPatientId());
                    // 分離オブジェクトは remove に渡せないので対象を検索する
                    AppointmentModel target = (AppointmentModel) getSession().createQuery("from AppointmentModel where id = :id").setParameter("id", av.getId()).uniqueResult();
                    getSession().delete(target);
                }
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
            }
        }
    }

    /**
     * 予約を検索する。
     * @param spec 検索仕様
     * @return 予約の Collection
     */
    @Override
    public Collection getAppointmentList(ModuleSearchSpec spec) {

        roleAllowed("user");

        String pcid = checkIdAsComposite(spec.getPatientId());

        Date[] fromDate = spec.getFromDate();
        Date[] toDate = spec.getToDate();

        int len = fromDate.length;

        List<Collection> ret = new ArrayList<Collection>(len);

        try {
            startTransaction();

            for (int i = 0; i < len; i++) {

                Collection c = getSession().createQuery("appoByPatient").setParameter("pid", pcid).setParameter("from", fromDate[i]).setParameter("to", toDate[i]).list();
                ret.add(c);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
            }
        }
        return ret;
    }
}
