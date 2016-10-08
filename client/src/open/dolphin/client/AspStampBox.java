package open.dolphin.client;

import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.client.editor.stamp.StampTreeDirector;
import open.dolphin.client.editor.stamp.StampTreePanel;
import open.dolphin.client.editor.stamp.StampBoxFrame;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.log.LogWriter;

/**
 * スタンプボックス
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class AspStampBox extends AbstractStampBox {

    /**
     * スタンプボックス
     * @param plugin
     * @param model 
     */
    public AspStampBox(StampBoxFrame plugin, IStampTreeModel model) {
        super(plugin, model);
    }

    /**
     *
     * @param stampXml スタンプを表すXML
     */
    @Override
    public void buildStampBox(String stampXml) {

        try {
            // Build stampTree
            BufferedReader reader = new BufferedReader(new StringReader(stampXml));
            ASpStampTreeBuilder builder = new ASpStampTreeBuilder();
            StampTreeDirector director = new StampTreeDirector(builder);
            List<StampTree> aspTrees = director.build(reader);
            reader.close();
            //      stampTreeModel.setTreeXml(null);

            // StampTreeに設定するポップアップメニューとトランスファーハンドラーを生成する
            AspStampTreeTransferHandler transferHandler = new AspStampTreeTransferHandler();

            // StampBox(TabbedPane) へリスト順に格納する
            for (StampTree stampTree : aspTrees) {
                stampTree.setTransferHandler(transferHandler);
                stampTree.setAsp(true);
                stampTree.setStampBox(getContext());
                StampTreePanel treePanel = new StampTreePanel(stampTree);
                String treeName = stampTree.getTreeName();
                // <TODO> 既存のスタンプボックスにある「細菌検査」を排除。
                // 既存データに「細菌検査」がなくなれば、無条件に addTab するよう変更。

                if (!(stampTree.getEntity() + "").equals("bacteriaOrder")) {
                    this.addTab(treeName, treePanel);
                }
            }
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * 
     */
    @Override
    protected void buildStampBox() {
        buildStampBox(getStampTreeModel().getTreeXml());
    }
}
