package open.dolphin.client.editor.stamp;

import open.dolphin.infomodel.ModuleModel;

/**
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class StampList implements java.io.Serializable {

    private static final long serialVersionUID = 5919106499806109895L;
    ModuleModel[] stampList;

    /** Creates new StampList */
    public StampList() {
    }

    /**
     *
     * @param stampList
     */
    public void setStampList(ModuleModel[] stampList) {
        this.stampList = stampList;
    }

    /**
     *
     * @return
     */
    public ModuleModel[] getStampList() {
        return stampList;
    }
}
