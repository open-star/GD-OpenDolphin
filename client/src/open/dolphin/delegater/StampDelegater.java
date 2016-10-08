/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.PublishedTreeModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.StampTreeModel;
import open.dolphin.infomodel.SubscribedTreeModel;
import open.dolphin.project.GlobalVariables;
import open.dolphin.service.IStampService;
import open.dolphin.utils.GUIDGenerator;

/**
 *
 * @author tomohiro
 */
public abstract class StampDelegater extends DelegaterErrorHandler {

    /**
     *
     * @return
     * @throws NamingException
     */
    protected abstract IStampService getService() throws NamingException;

    /**
     *
     * @param model
     * @return
     */
    public int cancelPublishedTree(StampTreeModel model) {
        try {
            return getService().cancelPublishedTree(model);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public List<PublishedTreeModel> getPublishedTrees() {
        try {
            return getService().getPublishedTrees();
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param stampId
     * @return
     */
    public StampModel getStamp(String stampId) {
        try {
            return getService().getStamp(stampId);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param list
     * @return
     */
    public List<StampModel> getStamp(List<ModuleInfoBean> list) {
        List<String> ids = new ArrayList<String>(list.size());
        for (ModuleInfoBean info : list) {
            ids.add(info.getStampId());
        }
        try {
            return getService().getStamp(ids);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param userPk
     * @return
     */
    public List<IStampTreeModel> getTrees(long userPk) {
        try {
            return getService().getTrees(userPk);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param stampId
     * @param entity
     * @param stamp
     * @return
     */
    public String newStamp(String stampId, String entity, String stamp) {
        final StampModel stampModel = new StampModel();
        stampModel.initialize(stampId, GlobalVariables.getUserModel().getId(), entity, stamp);
        try {
            return getService().putStamp(stampModel);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param entity
     * @param stamp
     * @return
     */
    public String newStamp(String entity, String stamp) {
        final StampModel stampModel = new StampModel();
        final String stampId = GUIDGenerator.generate(stampModel);
        stampModel.initialize(stampId, GlobalVariables.getUserModel().getId(), entity, stamp);
        try {
            return getService().putStamp(stampModel);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param model
     * @param publishBytes
     * @return
     */
    public int publishTree(StampTreeModel model, byte[] publishBytes) {
        try {
            return getService().publishTree(model, publishBytes);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param list
     * @return
     */
    public List<String> putStamp(List<StampModel> list) {
        try {
            return getService().putStamp(list);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param model
     * @return
     */
    public String putStamp(StampModel model) {
        try {
            return getService().putStamp(model);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param model
     * @return
     */
    public long putTree(IStampTreeModel model) {
        try {
            return getService().putTree((StampTreeModel) model);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param stampId
     * @return
     */
    public int removeStamp(String stampId) {
        try {
            return getService().removeStamp(stampId);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param ids
     * @return
     */
    public int removeStamp(List<String> ids) {
        try {
            return getService().removeStamp(ids);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param userId
     */
    public void cleanStamp(long userId) {
        try {
            getService().cleanStamp(userId);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
    }

    /**
     *
     * @param model
     * @param publishBytes
     * @return
     */
    public long saveAndPublishTree(StampTreeModel model, byte[] publishBytes) {
        try {
            return getService().saveAndPublishTree(model, publishBytes);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param subscribeList
     * @return
     */
    public List<Long> subscribeTrees(List<SubscribedTreeModel> subscribeList) {
        try {
            return getService().subscribeTrees(subscribeList);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param removeList
     * @return
     */
    public int unsubscribeTrees(List<SubscribedTreeModel> removeList) {
        try {
            return getService().unsubscribeTrees(removeList);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param model
     * @param publishBytes
     * @return
     */
    public int updatePublishedTree(StampTreeModel model, byte[] publishBytes) {
        try {
            return getService().updatePublishedTree(model, publishBytes);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param model
     * @param stamp
     * @return
     */
    public String updateStamp(StampModel model, String stamp) {
        String targetID = model.getId();
        try {
            IStampService service = getService();

            StampModel currentModel = service.getStamp(targetID);
            if (currentModel != null) {
                currentModel.setXml(stamp);
                service.removeStamp(targetID);
                return service.putStamp(currentModel);
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }
}
