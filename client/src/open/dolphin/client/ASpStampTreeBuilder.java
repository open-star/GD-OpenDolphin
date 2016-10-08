package open.dolphin.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.client.editor.stamp.StampTreeNode;
import open.dolphin.client.editor.stamp.StampTreeModel;
import open.dolphin.client.editor.stamp.DefaultStampTreeBuilder;

import open.dolphin.infomodel.ModuleInfoBean;

/**
 * DefaultStampTreeBuilder
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class ASpStampTreeBuilder extends AbstractStampTreeBuilder {

    /** Control staffs */
    private StampTreeNode rootNode;
    private StampTreeNode node;
    private ModuleInfoBean info;
    private LinkedList<StampTreeNode> nodes;
    private List<StampTree> products;

    /** Creates new DefaultStampTreeBuilder */
    public ASpStampTreeBuilder() {
    }

    /**
     * Returns the product of this builder
     * @return vector that contains StampTree instances
     */
    @Override
    public List<StampTree> getProduct() {
        return products;
    }

    /**
     *
     */
    @Override
    public void buildStart() {
        products = new ArrayList<StampTree>();
    }

    /**
     *
     * @param name
     * @param entity
     */
    @Override
    public void buildRoot(String name, String entity) {
        nodes = new LinkedList<StampTreeNode>();

        // TreeInfo を rootNode に保存する
        TreeInfo treeInfo = new TreeInfo();
        treeInfo.setName(name);
        treeInfo.setEntity(entity);
        rootNode = new StampTreeNode(treeInfo);
        nodes.addFirst(rootNode);
    }

    /**
     *
     * @param name
     */
    @Override
    public void buildNode(String name) {
        node = new StampTreeNode(name);
        getCurrentNode().add(node);

        // Add the new node to be current node
        nodes.addFirst(node);
    }

    /**
     *
     * @param name
     * @param role
     * @param entity
     * @param editable
     * @param memo
     * @param id
     */
    @Override
    public void buildStampInfo(String name, String role, String entity, String editable, String memo, String id) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(",").append(role).append(",").append(entity).append(",");
        sb.append(editable).append(",").append(memo).append(",").append(id);

        // ASP Tree なのでエディタから発行を無視する
        if (name.equals(DefaultStampTreeBuilder.FROM_EDITOR) && (id == null) && (role.equals("p"))) {
            return;
        }

        info = new ModuleInfoBean();
        info.initialize(null, name, null, entity, role);

        if (editable != null) {
            info.setEditable(Boolean.valueOf(editable).booleanValue());
        }
        if (memo != null) {
            info.setStampMemo(memo);
        }
        if (id != null) {
            info.setStampId(id);
        }

        // StampInfo から TreeNode を生成し現在のノードへ追加する
        node = new StampTreeNode(info);
        getCurrentNode().add(node);
    }

    /**
     *
     */
    @Override
    public void buildNodeEnd() {
        nodes.removeFirst();
    }

    /**
     *
     */
    @Override
    public void buildRootEnd() {
        StampTree tree = new StampTree(new StampTreeModel(rootNode));
        products.add(tree);
    }

    /**
     *
     */
    @Override
    public void buildEnd() {
    }

    /**
     *
     * @return
     */
    private StampTreeNode getCurrentNode() {
        return (StampTreeNode) nodes.getFirst();
    }
}
