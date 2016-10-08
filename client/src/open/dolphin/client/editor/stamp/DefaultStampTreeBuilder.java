package open.dolphin.client.editor.stamp;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import open.dolphin.client.AbstractStampTreeBuilder;
import open.dolphin.client.OrcaTree;
import open.dolphin.client.TreeInfo;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * StampTree Builder クラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class DefaultStampTreeBuilder extends AbstractStampTreeBuilder {

    /** XML文書で置換が必要な文字 */
    private static final String[] REPLACES = new String[]{"<", ">", "&", "'", "\""};
    /** 置換文字 */
    private static final String[] MATCHES = new String[]{"&lt;", "&gt;", "&amp;", "&apos;", "&quot;"};
    /** エディタから発行のスタンプ名 */
    public static final String FROM_EDITOR = "エディタから発行";
    /** rootノードの名前 */
    private String rootName;
    /** エディタから発行があったかどうかのフラグ */
    private boolean hasEditor;
    /** StampTree のルートノード*/
    private StampTreeNode rootNode;
    /** StampTree のノード*/
    private StampTreeNode node;
    /** ノードの UserObject になる StampInfo */
    private ModuleInfoBean info;
    /** 制御用のリスト */
    private LinkedList<StampTreeNode> linkedList;
    /** 生成物 */
    private List<StampTree> products;

    /** 
     * Creates new DefaultStampTreeBuilder 
     */
    protected DefaultStampTreeBuilder() {
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
     * build を開始する。
     */
    @Override
    public void buildStart() {
        products = new ArrayList<StampTree>();
        //     if (logger != null) {
        //         logger.debug("Build StampTree start");
        //     }

    }

    /**
     * Root を生成する。
     * @param name root名
     * @param entity
     */
    @Override
    public void buildRoot(String name, String entity) {

        linkedList = new LinkedList<StampTreeNode>();

        TreeInfo treeInfo = new TreeInfo(); // TreeInfo を 生成し rootNode に保存する
        treeInfo.setName(name);
        treeInfo.setEntity(entity);
        rootNode = new StampTreeNode(treeInfo);

        hasEditor = false;
        rootName = name;
        linkedList.addFirst(rootNode);
    }

    /**
     * ノードを生成する。
     * @param name ノード名
     */
    @Override
    public void buildNode(String name) {

        node = new StampTreeNode(toXmlText(name));   // Node を生成し現在のノードに加える
        getCurrentNode().add(node);

        linkedList.addFirst(node);        // このノードを first に加える
    }

    /**
     * StampInfo を UserObject にするノードを生成する。
     * @param name ノード名
     * @param entity エンティティ
     * @param editable 編集可能かどうかのフラグ
     * @param memo メモ
     * @param id DB key
     */
    @Override
    public void buildStampInfo(String name,
            String role,
            String entity,
            String editable,
            String memo,
            String id) {

        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(",");
        sb.append(role);
        sb.append(",");
        sb.append(entity);
        sb.append(",");
        sb.append(editable);
        sb.append(",");
        sb.append(memo);
        sb.append(",");
        sb.append(id);

        info = new ModuleInfoBean();        // StampInfo を生成する
        info.initialize(null, toXmlText(name), null, entity, role);

        if (editable != null) {
            info.setEditable(Boolean.valueOf(editable).booleanValue());
        }
        if (memo != null) {
            info.setStampMemo(toXmlText(memo));
        }
        if (id != null) {
            info.setStampId(id);
        }

        node = new StampTreeNode(info);        // StampInfo から TreeNode を生成し現在のノードへ追加する
        getCurrentNode().add(node);

        // エディタから発行を持っているか
        if (info.getStampName().equals(FROM_EDITOR) && (!info.isSerialized())) {
            hasEditor = true;
            info.setEditable(false);
        }
    }

    /**
     * Node の生成を終了する。
     */
    @Override
    public void buildNodeEnd() {

        linkedList.removeFirst();
    }

    /**
     * Root Node の生成を終了する。 
     */
    @Override
    public void buildRootEnd() {

        // エディタから発行を削除された場合に追加する処置
        if ((!hasEditor) && (getEntity(rootName) != null)) {

            if (getEntity(rootName).equals(IInfoModel.ENTITY_TEXT) || getEntity(rootName).equals(IInfoModel.ENTITY_PATH)) {
                // テキストスタンプとパススタンプにはエディタから発行はなし
            } else {
                ModuleInfoBean si = new ModuleInfoBean();
                si.initialize(null, FROM_EDITOR, null, getEntity(rootName), IInfoModel.ROLE_P);
                si.setEditable(false);
                StampTreeNode sn = new StampTreeNode(si);
                rootNode.add(sn);
            }
        }

        // StampTree を生成しプロダクトリストへ加える
        StampTree tree = new StampTree(new StampTreeModel(rootNode));
        products.add(tree);
    }

    /**
     * build を終了する。
     */
    @Override
    public void buildEnd() {
        boolean hasOrca = false;        // ORCAセットを加える
        for (StampTree st : products) {
            String entity = st.getTreeInfo().getEntity();
            if (entity.equals(IInfoModel.ENTITY_ORCA)) {
                hasOrca = true;
            }
        }

        if (!hasOrca) {
            TreeInfo treeInfo = new TreeInfo();
            treeInfo.setName(IInfoModel.TABNAME_ORCA);
            treeInfo.setEntity(IInfoModel.ENTITY_ORCA);
            rootNode = new StampTreeNode(treeInfo);
            OrcaTree tree = new OrcaTree(new StampTreeModel(rootNode));
            products.add(IInfoModel.TAB_INDEX_ORCA, tree);
        }
    }

    /**
     * リストから先頭の StampTreeNode を取り出す。
     */
    private StampTreeNode getCurrentNode() {
        return (StampTreeNode) linkedList.getFirst();
    }

    /**
     * 特殊文字を変換する。
     */
    private String toXmlText(String text) {
        for (int i = 0; i < REPLACES.length; i++) {
            text = text.replaceAll(MATCHES[i], REPLACES[i]);
        }
        return text;
    }
}
