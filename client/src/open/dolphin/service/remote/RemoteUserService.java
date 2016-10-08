package open.dolphin.service.remote;

import open.dolphin.service.IUserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.infomodel.PatientMemoModel;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.StampTreeModel;
import open.dolphin.infomodel.SubscribedTreeModel;

import open.dolphin.infomodel.UserModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;

/*
 * ユーザ MEMO:マッピング
 *
 * @author 
 */
/**
 *
 * @author oda
 */
public class RemoteUserService extends DolphinService implements IUserService {

    /**
     *
     */
    public RemoteUserService() {
    }

    /**
     * UserModelを追加
     * @param add 追加すべきUserModel
     * @return　定数の1
     */
    @Override
    public int addUser(UserModel add) {

        roleAllowed("admin");

        try {
            startTransaction();

            try {
                getUser(add.getUserId());
                endTransaction();
                throw new EntityExistsException();
            } catch (NoResultException e) {
                LogWriter.error(getClass(), e);
            }

            getSession().persist(add);
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return 1;
    }

    /**
     * UserModelの参照
     * @param userId　ユーザID
     * @return　UserModel
     */
    @Override
    public UserModel getUser(String userId) {

        roleAllowed("user");

        UserModel user = null;

        try {
            startTransaction();

            checkIdAsComposite(userId);
            user = (UserModel) getSession().createQuery("from UserModel where userid = :uid").setParameter("uid", userId).uniqueResult();

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        if (user == null) {
            throw new NoResultException();
        }

        if (user.getMemberType() != null && user.getMemberType().equals("EXPIRED")) {
            throw new SecurityException("Expired User");
        }

        return user;
    }

    /**
     * 全てのUserModelを参照
     * @return 全てのUserModel
     */
    @Override
    public List<UserModel> getAllUser() {

        roleAllowed("admin");

        List<UserModel> users = null;

        try {
            startTransaction();

            users = getSession().createQuery("from UserModel u where u.userId like :fid").setParameter("fid", getCallersFacilityId() + "%").list();
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        List<UserModel> ret = new ArrayList<UserModel>();
        for (UserModel user : users) {
            if (user != null && user.getMemberType() != null && (!user.getMemberType().equals("EXPIRED"))) {
                ret.add(user);
            }
        }

        return ret;
    }

    /**
     *　UserModelを更新する
     * @param update 更新済みUserModel
     * @return 定数の１
     */
    @Override
    public int updateUser(UserModel update) {

        roleAllowed("user");

        try {
            startTransaction();

            UserModel current = (UserModel) getSession().createQuery("from UserModel where id = :id").setParameter("id", update.getId()).uniqueResult();
            update.setMemberType(current.getMemberType());
            update.setRegisteredDate(current.getRegisteredDate());
            getSession().merge(update);

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return 1;
    }

    /**
     *　UserModelを削除
     *  関連する以下の情報も削除する
     *
     *  Stamp               StampModel
     *  Subscribed Tree     SubscribedTreeModel
     *  PublishedTree       PublishedTreeModel
     *  PersonalTree        StampTreeModel
     *  Document, Module, Image (Cascade)　　 DocumentModel
     *  Diagnosis           RegisteredDiagnosisModel
     *  Observation         ObservationModel
     *  患者メモ            PatientMemoModel
     *  予約                AppointmentModel
     *  ラボ                LaboModuleValue
     *
     * @param removeId　削除すべきUserのID
     * @return 定数の１
     */
    @Override
    public int removeUser(String removeId) {

        roleAllowed("admin");

        try {
            startTransaction();

            UserModel remove = getUser(removeId);
            long removePk = remove.getId();

            // Stamp を削除する
            List<StampModel> stamps = (List<StampModel>) getSession().createQuery("from StampModel s where s.userId = :pk").setParameter("pk", removePk).list();
            for (StampModel stamp : stamps) {
                getSession().delete(stamp);
            }

            // Subscribed Tree を削除する
            List<SubscribedTreeModel> subscribedTrees = (List<SubscribedTreeModel>) getSession().createQuery("from SubscribedTreeModel s where s.user.id = :pk").setParameter("pk", removePk).list();
            for (SubscribedTreeModel tree : subscribedTrees) {
                getSession().delete(tree);
            }

            // PublishedTree を削除する
            List<PublishedTreeModel> publishedTrees = (List<PublishedTreeModel>) getSession().createQuery("from PublishedTreeModel p where p.user.id = :pk").setParameter("pk", removePk).list();
            for (PublishedTreeModel tree : publishedTrees) {
                getSession().delete(tree);
            }

            // PersonalTreeを削除する
            try {
                StampTreeModel stampTree = (StampTreeModel) getSession().createQuery("from StampTreeModel s where s.user.id = :pk").setParameter("pk", removePk).list();
                getSession().delete(stampTree);

            } catch (Exception e) {
                LogWriter.error(getClass(), e);
            }

            if (remove.getLicenseModel().getLicense().equals("doctor")) {
                StringBuilder sb = new StringBuilder();
                sb.append(new Date());
                String note = sb.toString();
                remove.setMemo(note);
                remove.setPassword("c9dbeb1de83e60eb1eb3675fa7d69a02");
                remove.setMemberType("EXPIRED");
            } else {
                getSession().delete(remove);
            }

            boolean deleteDoc = false;
            if (deleteDoc) {

                //
                // Document, Module, Image (Cascade)
                //
                List<DocumentModel> documents = (List<DocumentModel>) getSession().createQuery("from DocumentModel d where d.creator.id = :removeId").setParameter("removeId", removePk).list();

                System.out.println(documents.size() + " 件のドキュメントを削除します。");
                //
                // Document を削除すれば ModuleとImageはカスケード削除される
                //
                for (DocumentModel document : documents) {
                    getSession().delete(document);
                }

                //
                // Diagnosis
                //
                List<RegisteredDiagnosisModel> rds = (List<RegisteredDiagnosisModel>) getSession().createQuery("from RegisteredDiagnosisModel d where d.creator.id = :removeId").setParameter("removeId", removePk).list();
                System.out.println(rds.size() + " 件の傷病名を削除します。");
                for (RegisteredDiagnosisModel rd : rds) {
                    getSession().delete(rd);
                }

                //
                // Observation
                //
                List<ObservationModel> observations = (List<ObservationModel>) getSession().createQuery("from ObservationModel o where o.creator.id = :removeId").setParameter("removeId", removePk).list();
                System.out.println(observations.size() + " 件の観測を削除します。");
                for (ObservationModel observation : observations) {
                    getSession().delete(observation);
                }

                //
                // 患者メモ
                //
                List<PatientMemoModel> memos = (List<PatientMemoModel>) getSession().createQuery("from PatientMemoModel o where o.creator.id = :removeId").setParameter("removeId", removePk).list();
                System.out.println(memos.size() + " 件の患者メモを削除します。");
                for (PatientMemoModel memo : memos) {
                    getSession().delete(memo);
                }

                //
                // 予約
                //
                List<AppointmentModel> appos = (List<AppointmentModel>) getSession().createQuery("from AppointmentModel o where o.creator.id = :removeId").setParameter("removeId", removePk).list();
                System.out.println(appos.size() + " 件の予約を削除します。");
                for (AppointmentModel appo : appos) {
                    getSession().delete(appo);
                }

                //
                // ラボ
                //
                List<LaboModuleValue> labos = (List<LaboModuleValue>) getSession().createQuery("from LaboModuleValue o where o.creator.id = :removeId").setParameter("removeId", removePk).list();
                System.out.println(labos.size() + " 件のラボを削除します。");
                for (LaboModuleValue lb : labos) {
                    getSession().delete(lb);
                }

                getSession().delete(remove);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return 1;
    }

    /**
     *　施設情報を更新する
     * @param update
     * @return
     */
    @Override
    public int updateFacility(UserModel update) {

        roleAllowed("admin");

        try {
            startTransaction();

            FacilityModel updateFacility = update.getFacility();
            FacilityModel current = (FacilityModel) getSession().createQuery("from FacilityModel where id = :id").setParameter("id", updateFacility.getId()).uniqueResult();
            updateFacility.setMemberType(current.getMemberType());
            updateFacility.setRegisteredDate(current.getRegisteredDate());
            getSession().merge(updateFacility);

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return 1;
    }
}
