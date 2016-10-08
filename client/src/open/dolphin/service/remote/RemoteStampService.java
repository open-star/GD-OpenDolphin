package open.dolphin.service.remote;

import open.dolphin.service.IStampService;
import java.util.ArrayList;
import java.util.List;

import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.StampTreeModel;
import open.dolphin.infomodel.SubscribedTreeModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;

/**
 * スタンプ
 *
 */
public class RemoteStampService extends DolphinService implements IStampService {

    /**
     *
     */
    public RemoteStampService() {
    }

    /**
     * スタンプツリー保存
     * @param model
     * @return
     */
    @Override
    public long putTree(StampTreeModel model) {
        roleAllowed("user");
        try {
            startTransaction();
            long userId = model.getUser().getId();
            int personalTreeCount = ((Number) getSession().createQuery("select count (*) from StampTreeModel where user_id = :uid").setParameter("uid", userId).uniqueResult()).intValue();

            if (personalTreeCount == 0) {
                getSession().save(model);
            } else {
                getSession().saveOrUpdate(model);
            }
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return model.getId();
    }

    /**
     * User個人及びサブスクライブしているTreeを取得する。
     * @param userPk userId(DB key)
     * @return User個人及びサブスクライブしているTreeのリスト
     */
    @Override
    public List<IStampTreeModel> getTrees(long userPk) {
        roleAllowed("user");
        List<IStampTreeModel> ret = new ArrayList<IStampTreeModel>();
        try {
            startTransaction();
            StampTreeModel personal = (StampTreeModel) getSession().createQuery("from StampTreeModel where user_id=:userPk").setParameter("userPk", userPk).uniqueResult();

            if (personal == null) {
                endTransaction();
                return ret;
            }

            ret.add(personal);

            List<SubscribedTreeModel> subscribed = getSession().createQuery("from SubscribedTreeModel where user_id=:userPk").setParameter("userPk", userPk).list();

            for (SubscribedTreeModel sm : subscribed) {

                PublishedTreeModel published = (PublishedTreeModel) getSession().createQuery("from PublishedTreeModel where id = :treeId").setParameter("treeId", sm.getTreeId()).uniqueResult();

                if (published == null) {
                    getSession().delete(sm);
                    continue;
                }

                ret.add(published);
            }

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
     * まだ保存されていない個人用のTreeを保存し公開する。
     * @return
     */
    @Override
    public long saveAndPublishTree(StampTreeModel model, byte[] publishBytes) {
        roleAllowed("user");
        try {
            startTransaction();

            getSession().persist(model);

            //
            // 公開用Treeモデルを生成し値をコピーする
            // 公開Treeのid=個人用TreeのId
            //
            PublishedTreeModel publishedModel = new PublishedTreeModel();
            publishedModel.setId(model.getId());
            publishedModel.setUser(model.getUser());
            publishedModel.setName(model.getName());
            publishedModel.setPublishType(model.getPublishType());
            publishedModel.setCategory(model.getCategory());
            publishedModel.setPartyName(model.getPartyName());
            publishedModel.setUrl(model.getUrl());
            publishedModel.setDescription(model.getDescription());
            publishedModel.setPublishedDate(model.getPublishedDate());
            publishedModel.setLastUpdated(model.getLastUpdated());
            publishedModel.setTreeBytes(publishBytes);

            getSession().persist(publishedModel);

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return model.getId();
    }

    /**
     * ツリー公開
     * @param model
     * @param publishBytes
     * @return
     */
    @Override
    public int publishTree(StampTreeModel model, byte[] publishBytes) {

        roleAllowed("user");

        try {
            startTransaction();

            getSession().merge(model);

            PublishedTreeModel publishedModel = new PublishedTreeModel();
            publishedModel.setId(model.getId());
            publishedModel.setUser(model.getUser());
            publishedModel.setName(model.getName());
            publishedModel.setPublishType(model.getPublishType());
            publishedModel.setCategory(model.getCategory());
            publishedModel.setPartyName(model.getPartyName());
            publishedModel.setUrl(model.getUrl());
            publishedModel.setDescription(model.getDescription());
            publishedModel.setPublishedDate(model.getPublishedDate());
            publishedModel.setLastUpdated(model.getLastUpdated());
            publishedModel.setTreeBytes(publishBytes);

            getSession().persist(publishedModel);

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
     * 公開しているTreeを更新する。
     * @param model 公開しているTree
     * @return 更新した数
     */
    @Override
    public int updatePublishedTree(StampTreeModel model, byte[] publishBytes) {

        roleAllowed("user");

        try {
            startTransaction();

            getSession().merge(model);

            PublishedTreeModel publishedModel = new PublishedTreeModel();
            publishedModel.setId(model.getId());
            publishedModel.setUser(model.getUser());
            publishedModel.setName(model.getName());
            publishedModel.setPublishType(model.getPublishType());
            publishedModel.setCategory(model.getCategory());
            publishedModel.setPartyName(model.getPartyName());
            publishedModel.setUrl(model.getUrl());
            publishedModel.setDescription(model.getDescription());
            publishedModel.setPublishedDate(model.getPublishedDate());
            publishedModel.setLastUpdated(model.getLastUpdated());
            publishedModel.setTreeBytes(publishBytes);

            getSession().merge(publishedModel);

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
     * 公開したTreeを削除する。
     * @return 削除した数
     */
    @Override
    public int cancelPublishedTree(StampTreeModel model) {

        roleAllowed("user");

        try {
            startTransaction();

            getSession().merge(model);

            PublishedTreeModel exist = (PublishedTreeModel) getSession().createQuery("from PublishedTreeModel where id = :id").setParameter("id", model.getId()).uniqueResult();
            getSession().delete(exist);

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
     * 公開されているStampTreeのリストを取得する。
     * @return ローカル及びパブリックTreeのリスト
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PublishedTreeModel> getPublishedTrees() {

        roleAllowed("user");

        List<PublishedTreeModel> ret = new ArrayList<PublishedTreeModel>();

        try {
            startTransaction();

            // ログインユーザの施設IDを取得する
            String fid = this.getCallersFacilityId();

            // local に公開されているTreeを取得する
            // publishType=施設ID
            List locals = getSession().createQuery("from PublishedTreeModel p where p.publishType=:fid").setParameter("fid", fid).list();
            ret.addAll((List<PublishedTreeModel>) locals);

            // パブリックTeeを取得する
            List publics = getSession().createQuery("from PublishedTreeModel p where p.publishType='global'").list();
            ret.addAll((List<PublishedTreeModel>) publics);

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
     * 公開Treeにサブスクライブする。
     * @param addList サブスクライブする
     * @return
     */
    @Override
    public List<Long> subscribeTrees(List<SubscribedTreeModel> addList) {

        roleAllowed("user");

        List<Long> ret = new ArrayList<Long>();

        try {
            startTransaction();

            for (SubscribedTreeModel model : addList) {
                getSession().persist(model);
                ret.add(new Long(model.getId()));
            }

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
     * 公開Treeにアンサブスクライブする。
     * @return
     */
    @Override
    public int unsubscribeTrees(List<SubscribedTreeModel> removeList) {

        roleAllowed("user");

        int cnt = 0;

        try {
            startTransaction();

            for (SubscribedTreeModel model : removeList) {
                SubscribedTreeModel remove = (SubscribedTreeModel) getSession().createQuery("from SubscribedTreeModel s where s.user.id=:userPk and s.treeId=:treeId").setParameter("userPk", model.getUser().getId()).setParameter("treeId", model.getTreeId()).uniqueResult();
                getSession().delete(remove);
                cnt++;
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return cnt;
    }

    /**
     * スタンプ保存
     * @param list
     * @return
     */
    @Override
    public List<String> putStamp(List<StampModel> list) {

        roleAllowed("user");

        List<String> ret = new ArrayList<String>();

        try {
            startTransaction();

            for (StampModel model : list) {
                getSession().persist(model);
                ret.add(model.getId());
            }

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
     * スタンプ保存
     * @param model スタンプ
     * @return スタンプID
     */
    @Override
    public String putStamp(StampModel model) {

        roleAllowed("user");

        try {
            startTransaction();
            getSession().persist(model);
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return model.getId();
    }

    /**
     * スタンプ保存
     * @param stampId スタンプID
     * @return　スタンプ
     */
    @Override
    public StampModel getStamp(String stampId) {

        roleAllowed("user");

        StampModel result = null;

        try {
            startTransaction();

            result = (StampModel) getSession().createQuery("from StampModel where id = :id").setParameter("id", stampId).uniqueResult();

            endTransaction();

            if (result == null) {
                LogWriter.error(this.getClass(), "No such stamp: " + stampId);
            }

        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return result;
    }

    /**
     * スタンプ取得
     * @return　スタンプ
     */
    @Override
    public List<StampModel> getStamp(List<String> ids) {

        roleAllowed("user");

        List<StampModel> ret = new ArrayList<StampModel>();

        try {
            startTransaction();

            for (String stampId : ids) {
                StampModel test = (StampModel) getSession().createQuery("from StampModel where id = :id").setParameter("id", stampId).uniqueResult();
                ret.add(test);
            }

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
     * スタンプ削除
     * @param stampId
     * @return
     */
    @Override
    public int removeStamp(String stampId) {

        roleAllowed("user");

        try {
            startTransaction();

            StampModel exist = (StampModel) getSession().createQuery("from StampModel whwre id = :id").setParameter("id", stampId).uniqueResult();
            getSession().delete(exist);

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
     *　スタンプ削除
     * @param ids
     * @return
     */
    @Override
    public int removeStamp(List<String> ids) {

        roleAllowed("user");

        int cnt = 0;

        try {
            startTransaction();

            for (String stampId : ids) {
                StampModel exist = (StampModel) getSession().createQuery("from StampModel where id = :id").setParameter("id", stampId).uniqueResult();
                getSession().delete(exist);
                cnt++;
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return cnt;
    }

    /**
     * User の Stamp を全て削除する。
     * @param userId 削除する User の User ID
     */
    @Override
    public void cleanStamp(long userId) {
        roleAllowed("user");

        try {
            startTransaction();

            List<StampModel> stamps = (List<StampModel>) getSession().createQuery("from StampModel where userid=:uid").setParameter("uid", userId).list();

            for (StampModel stamp : stamps) {
                getSession().delete(stamp);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
    }
}
