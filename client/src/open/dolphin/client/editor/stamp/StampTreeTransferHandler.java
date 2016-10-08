package open.dolphin.client.editor.stamp;

import java.sql.SQLException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import open.dolphin.client.LocalStampTreeNodeTransferable;
import open.dolphin.client.OrderList;
import open.dolphin.client.OrderListTransferable;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.InfoModelTransferable;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.log.LogWriter;

/**
 * StampTreeTransferHandler
 *
 * @author Minagawa,Kazushi
 *
 */
public class StampTreeTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1205897976539749194L;// Drag元のStampTree
    private StampTree sourceTree;// Dragされているノード
    private StampTreeNode dragNode;// StampTreeNode Flavor
    private DataFlavor stampTreeNodeFlavor = LocalStampTreeNodeTransferable.localStampTreeNodeFlavor;// KartePaneからDropされるオーダのFlavor
    private DataFlavor orderFlavor = OrderListTransferable.orderListFlavor;// KartePaneからDropされるテキストFlavor
    private DataFlavor stringFlavor = DataFlavor.stringFlavor;
    private DataFlavor infoModelFlavor = InfoModelTransferable.infoModelFlavor;// 病名エディタからDropされるRegisteredDiagnosis Flavor

    /**
     * 選択されたノードでDragを開始する。
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        sourceTree = (StampTree) c;
        dragNode = (StampTreeNode) sourceTree.getLastSelectedPathComponent();
        return new LocalStampTreeNodeTransferable(dragNode);
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /**
     * ツリーの中でのドラッグ＆ドロップで移動
     * INFORMATION ツリーの中での移動
     * @param model
     * @param targetTree
     * @param sourceNode
     * @param index
     * @param newParent
     */
    private void moveTo(DefaultTreeModel model, StampTree targetTree, StampTreeNode sourceNode, int index, StampTreeNode newParent) {
        try {
            model.removeNodeFromParent(sourceNode);
            model.insertNodeInto(sourceNode, newParent, index);
            TreeNode[] path = model.getPathToRoot(sourceNode);
            ((JTree) targetTree).setSelectionPath(new TreePath(path));
        } catch (Exception e1) {
            LogWriter.error(getClass(), e1);
        }
    }

    /**
     * ツリーの中でのドラッグ＆ドロップで移動
     * INFORMATION ツリーの中での移動
     * @param model
     * @param targetTree
     * @param sourceNode
     * @param newParent
     */
    private void moveToLast(DefaultTreeModel model, StampTree targetTree, StampTreeNode sourceNode, StampTreeNode newParent) {
        try {
            model.removeNodeFromParent(sourceNode);
            model.insertNodeInto(sourceNode, newParent, newParent.getChildCount());
            TreeNode[] path = model.getPathToRoot(sourceNode);
            ((JTree) targetTree).setSelectionPath(new TreePath(path));
        } catch (Exception e1) {
            LogWriter.error(getClass(), e1);
        }
    }

    /**
     * ツリーの中でのドラッグ＆ドロップで移動
     *  rootまでの親のパスのなかに自分がいるかどうかを判定する
     * Drop先が DragNode の子である時は DnD できない i.e 親が自分の子になることはできない
     * INFORMATION 親のパスのなかに自分がいるか
     * @param model
     * @param sourceNode
     * @param targetNode
     * @return
     */
    private boolean isInPath(DefaultTreeModel model, StampTreeNode sourceNode, StampTreeNode targetNode) {
        boolean result = false;
        TreeNode[] parents = model.getPathToRoot(targetNode);
        for (TreeNode parent : parents) {
            if (parent == (TreeNode) sourceNode) {
                result = true;
                Toolkit.getDefaultToolkit().beep();
                break;
            }
        }
        return result;
    }

    /*
     * Drag元のStampTreeとDropされるTreeが同じかどうかを判定する
     * shouldRemove = (sourceTree == targetTree) ? true : false;
     * Tree内のDnDはLocalTransferable(参照)の故、挿入時点で元のスタンプを
     * 常に削除する。DnD後の削除は行わない。
     * shouldRemove = false;
     * @param model
     * @param targetTree
     * @param sourceNode
     * @param targetNode
     * @param newParent
     * @return
     */
    private boolean dorpTo(DefaultTreeModel model, StampTree targetTree, StampTreeNode sourceNode, StampTreeNode targetNode, StampTreeNode newParent) {
        if (!isInPath(model, sourceNode, targetNode)) {
            if (newParent != sourceNode) // newChild is ancestor のケース
            {
                if (targetNode.isLeaf()) // Drop位置のノードが葉の場合、その前に挿入する
                {
                    moveTo(model, targetTree, sourceNode, newParent.getIndex(targetNode), newParent);
                } else if (sourceNode != targetNode) // Drop位置のノードが子を持つ時、最後の子として挿入する
                {
                    moveToLast(model, targetTree, sourceNode, targetNode);
                }
            }
        }
        return true;
    }

    /**
     * Fixなノードへのドロップ
     * INFORMATION Fixなノードへのドロップ
     * @param model
     * @param targetTree
     * @param sourceNode
     * @param targetNode
     * @param newParent
     * @return
     */
    private boolean dorpToFixNode(DefaultTreeModel model, StampTree targetTree, StampTreeNode sourceNode, StampTreeNode targetNode, StampTreeNode newParent) {
        int index = newParent.getIndex(targetNode);
        if (index > 0) {
            if (index == newParent.getChildCount() - 1) {
                moveTo(model, targetTree, sourceNode, --index, newParent);//通常はFixなノードの直後へ
            } else {
                moveTo(model, targetTree, sourceNode, ++index, newParent);//Fixなノードが最後なら前へ
            }
        }
        return true;
    }

    /**
     * ツリーの中でのドラッグ＆ドロップで移動
     * INFORMATION ツリーの中で移動
     * @param targetTree
     * @param sourceNode
     * @param targetNode
     * @param newParent
     * @return
     */
    private boolean innerDragAndDrop(StampTree targetTree, StampTreeNode sourceNode, StampTreeNode targetNode, StampTreeNode newParent) {
        DefaultTreeModel model = (DefaultTreeModel) targetTree.getModel();
        if (targetNode != null && targetNode.isMoveable()) {
            if (sourceNode != null && sourceNode.isMoveable()) {
                return dorpTo(model, targetTree, sourceNode, targetNode, newParent);
            }
        } else {
            return dorpToFixNode(model, targetTree, sourceNode, targetNode, newParent);
        }
        return false;
    }

    /**
     * KartePaneからDropされたオーダをインポートする
     * @param transferData
     * @param selected
     * @param targetTree
     * @param targetEntity
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private boolean importOrder(Transferable transferData, StampTreeNode selected, StampTree targetTree, String targetEntity) throws IOException, SQLException {
        boolean result = false;
        try {
            Object list = transferData.getTransferData(OrderListTransferable.orderListFlavor);

            //   OrderList
            //     ModuleModel droppedStamp = list.orderList[0];
            if (list instanceof OrderList) {

                ModuleModel droppedStamp = ((OrderList) list).getOrderList()[0];

                // 同一エンティティの場合、選択は必ず起っている
                if (droppedStamp.getModuleInfo().getEntity().equals(targetEntity)) {
                    return targetTree.addStamp(droppedStamp, selected);
                } else if (targetEntity.equals(IInfoModel.ENTITY_PATH)) {
                    if (selected == null) // パス Tree の場合
                    {
                        selected = (StampTreeNode) targetTree.getModel().getRoot();
                    }
                    result = targetTree.addStamp(droppedStamp, selected);
                } else {
                    result = targetTree.addStamp(droppedStamp, null); // Rootの最後に追加する
                }
            }
        } catch (UnsupportedFlavorException e) {
            LogWriter.error(getClass(), e);
        }
        return result;
    }

    /**
     * KartePaneからDropされたテキストをインポートする
     * @param transferData
     * @param selected
     * @param targetTree
     * @param targetEntity
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private boolean importString(Transferable transferData, StampTreeNode selected, StampTree targetTree, String targetEntity) throws IOException, SQLException {
        boolean result = false;
        try {
            String text = (String) transferData.getTransferData(DataFlavor.stringFlavor);
            if (targetEntity.equals(IInfoModel.ENTITY_TEXT)) {
                result = targetTree.addTextStamp(text, selected);
            } else {
                result = targetTree.addTextStamp(text, null);
            }
        } catch (UnsupportedFlavorException e) {
            LogWriter.error(getClass(), e);
        }
        return result;
    }

    /**
     * DiagnosisEditorからDropされた病名をインポートする
     * @param transferData
     * @param selected
     * @param targetTree
     * @param targetEntity
     * @return
     * @throws UnsupportedFlavorException
     * @throws IOException
     * @throws SQLException
     */
    private boolean importinfoModel(Transferable transferData, StampTreeNode selected, StampTree targetTree, String targetEntity) throws UnsupportedFlavorException, IOException, SQLException {
        boolean result = false;
        try {
            RegisteredDiagnosisModel diagnosis = (RegisteredDiagnosisModel) transferData.getTransferData(InfoModelTransferable.infoModelFlavor);
            if (targetEntity.equals(IInfoModel.ENTITY_DIAGNOSIS)) {
                result = targetTree.addDiagnosis(diagnosis, selected);
            } else {
                result = targetTree.addDiagnosis(diagnosis, null);
            }
        } catch (UnsupportedFlavorException e) {
            LogWriter.error(getClass(), e);
        }
        return result;
    }

    /**
     * DropされたFlavorをStampTreeにインポートする。
     * INFORMATION スタンプ箱へのドロップはここ。
     * @param targetNode
     * @param transferData 
     */
    @Override
    public boolean importData(JComponent targetNode, Transferable transferData) {
        if (canImport(targetNode, transferData.getTransferDataFlavors())) {
            try {
                StampTree target = (StampTree) targetNode;// Dropを受けるStampTreeを取得する
                String targetEntity = target.getEntity();

                boolean result = false;

                // Drop位置のノードを取得する
                StampTreeNode selected = (StampTreeNode) target.getLastSelectedPathComponent(); // DnD によって選択状態になっている
                StampTreeNode newParent = null;
                if (selected != null) {
                    newParent = (StampTreeNode) selected.getParent();    // Drop位置の親を取得する
                }
                if (transferData.isDataFlavorSupported(stampTreeNodeFlavor) && (selected != null)) {
                    // FlavorがStampTreeNodeの時
                    // StampTree 内の DnD
                    StampTreeNode dropNode = (StampTreeNode) transferData.getTransferData(stampTreeNodeFlavor);
                    result = innerDragAndDrop(target, dropNode, selected, newParent);
                } else if (transferData.isDataFlavorSupported(orderFlavor)) {
                    result = importOrder(transferData, selected, target, targetEntity);
                } else if (transferData.isDataFlavorSupported(stringFlavor)) {
                    result = importString(transferData, selected, target, targetEntity);
                } else if (transferData.isDataFlavorSupported(infoModelFlavor)) {
                    result = importinfoModel(transferData, selected, target, targetEntity);
                } else {
                    return false;
                }
                target.saveStampTree();
                return result;

            } catch (UnsupportedFlavorException e) {
                LogWriter.error(getClass(), e);
            } catch (Exception ioe) {
                LogWriter.error(getClass(), ioe);
            }
        }
        return false;
    }

    /**
     * DnD後、Dragしたノードを元のStamptreeから削除する。
     * @param c
     * @param data
     * @param action
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
    }

    /**
     * インポート可能かどうかを返す。
     * @param c
     * @param flavors
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {

        for (DataFlavor flavor : flavors) {
            if (stampTreeNodeFlavor.equals(flavor)) {
                return true;
            }
            if (orderFlavor.equals(flavor)) {
                return true;
            }
            if (stringFlavor.equals(flavor)) {
                return true;
            }
            if (infoModelFlavor.equals(flavor)) {
                return true;
            }
        }
        return false;
    }
}
