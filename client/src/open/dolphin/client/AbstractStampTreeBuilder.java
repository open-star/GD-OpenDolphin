package open.dolphin.client;

import java.util.List;

import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.infomodel.IInfoModel;

/**
 * スタンプツリー
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public abstract class AbstractStampTreeBuilder {

    /** Creates new DefaultStampTreeBuilder */
    public AbstractStampTreeBuilder() {
    }

    /**
     *
     * @return
     */
    public abstract List<StampTree> getProduct();

    /**
     *
     */
    public abstract void buildStart();

    /**
     *
     * @param name
     * @param entity
     */
    public abstract void buildRoot(String name, String entity);

    /**
     *
     * @param name
     */
    public abstract void buildNode(String name);

    /**
     *
     * @param name
     * @param role
     * @param entity
     * @param editable
     * @param memo
     * @param id
     */
    public abstract void buildStampInfo(String name, String role, String entity, String editable, String memo, String id);

    /**
     *
     */
    public abstract void buildNodeEnd();

    /**
     *
     */
    public abstract void buildRootEnd();

    /**
     *
     */
    public abstract void buildEnd();

    /**
     *
     * @param rootName
     * @return
     */
    protected static String getEntity(String rootName) {
        String result = null;
        if (rootName != null) {
            for (int i = 0; i < IInfoModel.STAMP_ENTITIES.length; i++) {
                if (IInfoModel.STAMP_NAMES[i].equals(rootName)) {
                    result = IInfoModel.STAMP_ENTITIES[i];
                    break;
                }
            }
        }
        return result;
    }
}
