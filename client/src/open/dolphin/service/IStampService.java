package open.dolphin.service;

import java.util.List;

import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.StampTreeModel;
import open.dolphin.infomodel.SubscribedTreeModel;

/**
 * IStampService
 *
 * @author Minagawa,Kazushi
 */
public interface IStampService {

    /**
     * 個人用のStampTreeを保存する。
     * @param model 個人用のStampTreeModel
     * @return db pk
     */
    public long putTree(StampTreeModel model);

    /**
     * User のスタンプツリーをデータベースから検索して返す。
     * @param userPk ログインユーザの PK
     * @return StampTree 定義のリスト（個人用、共有用、公開されているもの）
     */
    public List<IStampTreeModel> getTrees(long userPk);

    /**
     *
     * @param model
     * @param publishBytes
     * @return
     */
    public long saveAndPublishTree(StampTreeModel model, byte[] publishBytes);

    /**
     *
     * @param model
     * @param publishBytes
     * @return
     */
    public int publishTree(StampTreeModel model, byte[] publishBytes);

    /**
     *
     * @param model
     * @param publishBytes
     * @return
     */
    public int updatePublishedTree(StampTreeModel model, byte[] publishBytes);

    /**
     *
     * @param model
     * @return
     */
    public int cancelPublishedTree(StampTreeModel model);

    /**
     *
     * @return
     */
    public List<PublishedTreeModel> getPublishedTrees();

    /**
     *
     * @param addList
     * @return
     */
    public List<Long> subscribeTrees(List<SubscribedTreeModel> addList);

    /**
     *
     * @param removeList
     * @return
     */
    public int unsubscribeTrees(List<SubscribedTreeModel> removeList);

    /**
     *
     * @param list
     * @return
     */
    public List<String> putStamp(List<StampModel> list);

    /**
     *
     * @param model
     * @return
     */
    public String putStamp(StampModel model);

    /**
     *
     * @param stampId
     * @return
     */
    public StampModel getStamp(String stampId);

    /**
     *
     * @param ids
     * @return
     */
    public List<StampModel> getStamp(List<String> ids);

    /**
     *
     * @param stampId
     * @return
     */
    public int removeStamp(String stampId);

    /**
     *
     * @param ids
     * @return
     */
    public int removeStamp(List<String> ids);

    /**
     *
     * @param userId
     */
    public void cleanStamp(long userId);
}
