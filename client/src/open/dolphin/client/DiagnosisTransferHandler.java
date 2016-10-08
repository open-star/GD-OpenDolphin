package open.dolphin.client;

import open.dolphin.client.diagnosisdocumentpanel.DiagnosisDocumentPanel;
import open.dolphin.client.editor.stamp.StampTreeNode;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.InfoModelTransferable;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.log.LogWriter;
import open.dolphin.table.ObjectReflectTableModel;

/**
 * DiagnosisTransferHandler
 *
 * @author Minagawa,Kazushi
 *
 */
public class DiagnosisTransferHandler extends TransferHandler {

    private JTable sourceTable;
    private RegisteredDiagnosisModel dragItem;
    private boolean shouldRemove;
    private DiagnosisDocumentPanel parent;

    /**
     * 
     * @param parent
     */
    public DiagnosisTransferHandler(DiagnosisDocumentPanel parent) {
        super();
        this.parent = parent;
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        sourceTable = (JTable) c;
        ObjectReflectTableModel<Object> tableModel = (ObjectReflectTableModel<Object>) sourceTable.getModel();
        dragItem = (RegisteredDiagnosisModel) tableModel.getObject(sourceTable.getSelectedRow());
        return dragItem != null ? new InfoModelTransferable(dragItem) : null;
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
     *
     * @param c
     * @param t
     * @return
     */
    @Override
    public boolean importData(JComponent c, Transferable t) {
        if (canImport(c, t.getTransferDataFlavors())) {
            try {
                // 病名の挿入位置を決めておく
                JTable dropTable = (JTable) c;
                int index = dropTable.getSelectedRow();
                index = 0;
                if (index < 0) {
                    index = 0;
                }

                // Dropされたノードを取得する
                StampTreeNode droppedNode = (StampTreeNode) t.getTransferData(LocalStampTreeNodeTransferable.localStampTreeNodeFlavor);

                // Import するイストを生成する
                List<ModuleInfoBean> importList = new ArrayList<ModuleInfoBean>(3);

                // 葉の場合
                if (droppedNode.isLeaf()) {
                    ModuleInfoBean stampInfo = (ModuleInfoBean) droppedNode.getStampInfo();
                    if (stampInfo.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS)) {
                        if (stampInfo.isSerialized()) {
                            importList.add(stampInfo);
                        } else {
                            parent.openEditor2();
                            shouldRemove = false;
                            return true;
                        }
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                        return false;
                    }
                } else {
                    // Dropされたノードの葉を列挙する
                    Enumeration e = droppedNode.preorderEnumeration();
                    while (e.hasMoreElements()) {
                        StampTreeNode node = (StampTreeNode) e.nextElement();
                        if (node.isLeaf()) {
                            ModuleInfoBean stampInfo = (ModuleInfoBean) node.getStampInfo();
                            if (stampInfo.isSerialized() && (stampInfo.getEntity().equals(IInfoModel.ENTITY_DIAGNOSIS))) {
                                importList.add(stampInfo);
                            }
                        }
                    }
                }
                // まとめてデータベースからフェッチしインポートする
                if (importList.size() > 0) {
                    parent.importStampList(importList, index);
                    return true;
                } else {
                    return false;
                }

            } catch (Exception ioe) {
                LogWriter.error(getClass(), ioe);
            }
        }
        return false;
    }

    /**
     *
     * @param c
     * @param data
     * @param action
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        if (action == MOVE && shouldRemove) {
            ObjectReflectTableModel<Object> tableModel = (ObjectReflectTableModel<Object>) sourceTable.getModel();
            tableModel.deleteRow(dragItem);
        }
    }

    /**
     *
     * @param c
     * @param flavors
     * @return
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (LocalStampTreeNodeTransferable.localStampTreeNodeFlavor.equals(flavors[i])) {
                JTable t = (JTable) c;
                t.getSelectionModel().setSelectionInterval(0, 0);
                return true;
            }
        }
        return false;
    }
}
