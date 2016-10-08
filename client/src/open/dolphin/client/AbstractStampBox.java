package open.dolphin.client;

import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.client.editor.stamp.StampTreePanel;
import open.dolphin.client.editor.stamp.StampTreeNode;
import open.dolphin.client.editor.stamp.StampBoxFrame;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.component.DnDTabbedPane;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * スタンプ箱
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public abstract class AbstractStampBox extends DnDTabbedPane {

    private IStampTreeModel stampTreeModel;
    private StampBoxFrame context;

    /**
     *
     * @param plugin
     * @param model
     */
    public AbstractStampBox(StampBoxFrame plugin, IStampTreeModel model) {
        super();
        setContext(plugin);
        setStampTreeModel(model);
        buildStampBox();
    }

    /**
     *
     * @return
     */
    public StampBoxFrame getContext() {
        return context;
    }

    /**
     *
     * @param plugin
     */
    private void setContext(StampBoxFrame plugin) {
        context = plugin;
    }

    /**
     *
     * @return
     */
    public IStampTreeModel getStampTreeModel() {
        return stampTreeModel;
    }

    /**
     *
     * @param stampTreeModel
     */
    private void setStampTreeModel(IStampTreeModel stampTreeModel) {
        this.stampTreeModel = stampTreeModel;
    }

    /**
     *
     */
    protected abstract void buildStampBox();

    /**
     *
     * @param stampXml
     */
    protected abstract void buildStampBox(String stampXml);

    /**
     * 引数のカテゴリに対応するTreeを返す。
     * @param entity 
     * @return カテゴリにマッチするStampTree
     */
    public StampTree getStampTree(String entity) {
        int count = this.getTabCount();
        boolean found = false;
        StampTree tree = null;
        for (int i = 0; i < count; i++) {
            StampTreePanel panel = (StampTreePanel) this.getComponentAt(i);
            tree = panel.getTree();
            if (entity.equals(tree.getEntity())) {
                found = true;
                break;
            }
        }

        return found ? tree : null;
    }

    /**
     *
     * @param index
     * @return
     */
    public StampTree getStampTree(int index) {
        if (index >= 0 && index < this.getTabCount()) {
            StampTreePanel panel = (StampTreePanel) this.getComponentAt(index);
            return panel.getTree();
        }
        return null;
    }

    /**
     *
     * @param index
     * @return
     */
    public boolean isHasEditor(int index) {
        return false;
    }

    /**
     *
     * @param b
     */
    public void setHasNoEditorEnabled(boolean b) {
    }

    /**
     * スタンプボックスに含まれる全treeのTreeInfoリストを返す。
     * @return TreeInfoのリスト
     */
    public List<TreeInfo> getAllTreeInfos() {
        List<TreeInfo> ret = new ArrayList<TreeInfo>();
        int cnt = this.getTabCount();
        for (int i = 0; i < cnt; i++) {
            StampTreePanel tp = (StampTreePanel) this.getComponent(i);
            StampTree tree = tp.getTree();
            TreeInfo info = tree.getTreeInfo();
            ret.add(info);
        }
        return ret;
    }

    /**
     * スタンプボックスに含まれる全treeを返す。
     * @return StampTreeのリスト
     */
    public List<StampTree> getAllTrees() {
        List<StampTree> ret = new ArrayList<StampTree>();
        int cnt = this.getTabCount();
        for (int i = 0; i < cnt; i++) {
            StampTreePanel tp = (StampTreePanel) this.getComponent(i);
            StampTree tree = tp.getTree();
            ret.add(tree);
        }
        return ret;
    }

    /**
     * スタンプボックスに含まれる病名以外のStampTreeを返す。
     * @return StampTreeのリスト
     */
    public List<StampTree> getAllPTrees() {

        List<StampTree> ret = new ArrayList<StampTree>();
        int cnt = this.getTabCount();

        for (int i = 0; i < cnt; i++) {
            StampTreePanel tp = (StampTreePanel) this.getComponent(i);
            StampTree tree = tp.getTree();
            // 病名StampTree はスキップする
            if (tree.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS)) {
                continue;
            } else {
                ret.add(tree);
            }
        }

        return ret;
    }

    /**
     * 引数のエンティティ配下にある全てのスタンプを返す。
     * これはメニュー等で使用する。
     * @param entity Treeのエンティティ
     * @return 全てのスタンプのリスト
     */
    public List<ModuleInfoBean> getAllStamps(String entity) {

        StampTree tree = getStampTree(entity);
        if (tree != null) {
            List<ModuleInfoBean> ret = new ArrayList<ModuleInfoBean>();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
            Enumeration e = rootNode.preorderEnumeration();
            while (e.hasMoreElements()) {
                StampTreeNode node = (StampTreeNode) e.nextElement();
                if (node.isLeaf()) {
                    ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                    ret.add(info);
                }
            }
            return ret;
        }

        return null;
    }

    /**
     *
     * @return
     */
    public List<String> getEntities() {
        List<String> ret = new ArrayList<String>();
        List<TreeInfo> infos = getAllTreeInfos();
        for (TreeInfo ti : infos) {
            ret.add(ti.getEntity());
        }
        return ret;
    }

    /**
     *
     * @return
     */
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStampTreeModel().getName());
        sb.append(" ");
        sb.append(getStampTreeModel().getPartyName());
        if (sb.length() > 16) {
            sb.setLength(12);
            sb.append("...");
        }
        return sb.toString();
    }

    /**
     *
     */
    public void removeAllTab() {
        //    for (int index = getTabCount(); index >= 0 ; index--)
        while (getTabCount() != 1) {
            this.removeTabAt(0);
        }
    }
}
