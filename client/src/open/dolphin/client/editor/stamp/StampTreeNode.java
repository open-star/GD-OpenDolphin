package open.dolphin.client.editor.stamp;

import javax.swing.tree.DefaultMutableTreeNode;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 * StampTree のノードクラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class StampTreeNode extends DefaultMutableTreeNode {

    /**
     * コンストラクタ
     * @param userObject
     */
    public StampTreeNode(Object userObject) {
        super(userObject);
        // StampInfo で初期化された場合は葉ノードにする
        if (userObject instanceof open.dolphin.infomodel.ModuleInfoBean) {
            this.allowsChildren = false;
        }
    }

    /**
     * 葉かどうかを返す
     */
    @Override
    public boolean isLeaf() {
        return (!this.allowsChildren);
    }

    /**
     * StampInfo を返す
     * @return 
     */
    public ModuleInfoBean getStampInfo() {
        return (ModuleInfoBean) userObject;
    }

    /**
     * 自分はフォルダか？
     * TODO ものすごくアドホックです。　
     * フォルダの場合、TreeNode中のユーザオブジェクトがStringなのでこんな感じ。
     * 本来は、フォルダのユーザオブジェクトをModuleInfoBeanとキャスト可能な物に
     * するのが正解なのかな。
     * @return
     */
    public boolean isFolder() {
        return (userObject instanceof String);
    }

    /**
     * 自分はエディタから発行ノードか？
     * TODO 同様に、ものすごくアドホックです。　
     * エディタから発行という項目の名前に依存してます。
     * @return
     */
    public boolean isFromEditor() {
        return getStampInfo().getStampName().equals(DefaultStampTreeBuilder.FROM_EDITOR);
    }

    /**
     * 自分は移動可能か？
     * TODO 同様に,エディタから発行という項目の名前に依存してます。
     * @return
     */
    public boolean isMoveable() {
        if (!isFolder()) {
            return !isFromEditor();
        }
        return true;
    }
}
