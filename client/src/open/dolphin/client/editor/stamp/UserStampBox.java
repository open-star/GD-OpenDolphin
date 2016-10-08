package open.dolphin.client.editor.stamp;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import open.dolphin.client.AbstractStampBox;

import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.log.LogWriter;

/**
 * UserStampBox MEMO:Component
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class UserStampBox extends AbstractStampBox {

    private static final String BOX_INFO = "個人用スタンプボックス";
    private int textIndex;// テキストスタンプのタブ番号 
    private int pathIndex;// パススタンプのタブ番号 
    private int orcaIndex;// ORCA セットのタブ番号

    /**
     *
     * @param plugin
     * @param model
     */
    public UserStampBox(StampBoxFrame plugin, IStampTreeModel model) {
        super(plugin, model);
    }

    /**
     * StampBox を構築する。
     */
    @Override
    protected void buildStampBox(String stampXml) {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(stampXml));
            DefaultStampTreeBuilder builder = new DefaultStampTreeBuilder();
            StampTreeDirector director = new StampTreeDirector(builder);
            List<StampTree> userTrees = director.build(reader);
            reader.close();

            // StampTreeへ設定するPopupMenuとTransferHandlerを生成する
            StampTreePopupAdapter popAdapter = new StampTreePopupAdapter();
            StampTreeTransferHandler transferHandler = new StampTreeTransferHandler();

            // StampBox(TabbedPane) へリスト順に格納する
            // 一つのtabへ一つのtreeが対応
            int index = 0;
            for (StampTree stampTree : userTrees) {
                stampTree.setUserTree(true);
                stampTree.setTransferHandler(transferHandler);
                stampTree.setStampBox(getContext());
                StampTreePanel treePanel = new StampTreePanel(stampTree);
                String treeName = stampTree.getTreeName();
                // <TODO> 既存のスタンプボックスにある「細菌検査」を排除
                // 既存データに「細菌検査」がなくなれば、無条件に addTab するよう変更。

                if (!(stampTree.getEntity() + "").equals("bacteriaOrder")) {
                    this.addTab(treeName, treePanel);
                }

                // Text、Path、ORCA のタブ番号を保存する
                if (stampTree.getEntity().equals(IInfoModel.ENTITY_TEXT)) {
                    textIndex = index;
                    stampTree.addMouseListener(popAdapter);
                } else if (stampTree.getEntity().equals(IInfoModel.ENTITY_PATH)) {
                    pathIndex = index;
                    stampTree.addMouseListener(popAdapter);
                } else if (stampTree.getEntity().equals(IInfoModel.ENTITY_ORCA)) {
                    orcaIndex = index;
                } else {
                    stampTree.addMouseListener(popAdapter);
                }
                index++;
            }
        } catch (Exception e) {
            LogWriter.error(this.getClass(), "", e);
        }
    }

    /**
     *
     */
    @Override
    protected void buildStampBox() {
        buildStampBox(getStampTreeModel().getTreeXml());
    }

    /**
     * 引数のタブ番号に対応するStampTreeにエディタから発行があるかどうかを返す。
     * @param index タブ番号
     * @return エディタから発行がある場合に true 
     */
    @Override
    public boolean isHasEditor(int index) {
        return (index == textIndex || index == pathIndex || index == orcaIndex) ? false : true;
    }

    /**
     *
     * @param enabled
     */
    @Override
    public void setHasNoEditorEnabled(boolean enabled) {
        this.setEnabledAt(textIndex, enabled);
        this.setEnabledAt(pathIndex, enabled);
        this.setEnabledAt(orcaIndex, enabled);
    }

    /**
     *
     * @return
     */
    @Override
    public String getInfo() {
        return BOX_INFO;
    }
}
