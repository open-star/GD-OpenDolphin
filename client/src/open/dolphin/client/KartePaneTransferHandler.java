package open.dolphin.client;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import open.dolphin.client.editor.stamp.StampTreeNode;

import java.util.ArrayList;
import java.util.Enumeration;

import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import open.dolphin.helper.PlugInMenuSupport;

import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.log.LogWriter;
import open.dolphin.plugin.PluginWrapper;

/**
 * KartePaneTransferHandler
 * @author Minagawa,Kazushi
 */
public class KartePaneTransferHandler extends TransferHandler {

    private static final long serialVersionUID = -7891004155072724783L;
    private DataFlavor stringFlavor = DataFlavor.stringFlavor;
    // KartePane
    private KartePane pPane;
    private IChart parent;
    private JTextPane source;
    private boolean shouldRemove;
    // Start and end position in the source text.
    // We need this information when performing a MOVE
    // in order to remove the dragged text from the source.
    Position p0 = null, p1 = null;

    /**
     *
     * @param parent
     * @param pPane
     */
    public KartePaneTransferHandler(IChart parent, KartePane pPane) {
        this.pPane = pPane;
        this.parent = parent;
    }

    /**
     * DropされたFlavorをインポートする。
     * INFORMATION カルテにドロップはここ
     * @param distination
     * @param dropped
     */
    @Override
    public boolean importData(JComponent distination, Transferable dropped) {

        JTextPane distinationTextPanel = (JTextPane) distination;

        if (!canImport(distination, dropped.getTransferDataFlavors())) {
            return false;
        }

        if (distinationTextPanel.equals(source)
                && (distinationTextPanel.getCaretPosition() >= p0.getOffset())
                && (distinationTextPanel.getCaretPosition() <= p1.getOffset())) {
            shouldRemove = false;
            return true;
        }

        try {
            // プラグイン
            PlugInMenuSupport plugins = parent.getPlugins();
            for (final Map.Entry entry : plugins.entrySet()) {
                open.dolphin.plugin.IPlugin plugin = ((open.dolphin.plugin.IPlugin) entry.getValue());
                PluginWrapper pluginWrapper = new PluginWrapper(plugin);
                if (pluginWrapper.getType() == open.dolphin.plugin.IPlugin.Type.panel) {
                    DataFlavor supported_flavor = pluginWrapper.getFlavor();
                    if (supported_flavor != null) {
                        if (dropped.isDataFlavorSupported(supported_flavor)) {
                            return pluginWrapper.drop(pPane, dropped);
                        }
                    }
                }
            }

            if (dropped.isDataFlavorSupported(LocalStampTreeNodeTransferable.localStampTreeNodeFlavor)) {
                // スタンプボックスからのスタンプをインポートする
                shouldRemove = false;
                return stampInfoDrop(dropped);
            } else if (dropped.isDataFlavorSupported(OrderListTransferable.orderListFlavor)) {
                // KartePaneからのオーダスタンプをインポートする
                return doStampDrop(dropped);
            } else if (dropped.isDataFlavorSupported(stringFlavor)) {
                String str = (String) dropped.getTransferData(stringFlavor);
                distinationTextPanel.replaceSelection(str);
                shouldRemove = distinationTextPanel == source ? true : false;
                return true;
            }
        } catch (UnsupportedFlavorException ufe) {
            LogWriter.error(getClass(), ufe);
        } catch (IOException ioe) {
            LogWriter.error(getClass(), ioe);
        }
        return false;
    }

    // Create a Transferable implementation that contains the
    // selected text.
    @Override
    protected Transferable createTransferable(JComponent c) {
        source = (JTextPane) c;
        int start = source.getSelectionStart();
        int end = source.getSelectionEnd();
        Document doc = source.getDocument();
        if (start == end) {
            return null;
        }
        try {
            p0 = doc.createPosition(start);
            p1 = doc.createPosition(end);
        } catch (BadLocationException e) {
           LogWriter.error(getClass(), e);
        }
        String data = source.getSelectedText();
        return new StringSelection(data);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    // Remove the old text if the action is a MOVE.
    // However, we do not allow dropping on top of the selected text,
    // so in that case do nothing.
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        JTextComponent tc = (JTextComponent) c;
        if (tc.isEditable() && (shouldRemove == true) && (action == MOVE)) {
            if ((p0 != null) && (p1 != null) && (p0.getOffset() != p1.getOffset())) {
                try {
                    tc.getDocument().remove(p0.getOffset(), p1.getOffset() - p0.getOffset());
                } catch (BadLocationException e) {
                     LogWriter.error(getClass(), e);
                }
            }
        }
        shouldRemove = false;
        source = null;
    }

    /**
     * インポート可能かどうかを返す。
     * @param c 
     * @param flavors
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        JTextPane tc = (JTextPane) c;
        if (tc.isEditable() && hasFlavor(flavors)) {
            return true;
        }
        return false;
    }

    /**
     * Flavorリストのなかに受け入れられものがあるかどうかを返す。
     * @param flavors 
     * @return
     */
    protected boolean hasFlavor(DataFlavor[] flavors) {

        for (DataFlavor flavor : flavors) {
            // String OK
            if (stringFlavor.equals(flavor)) {
                return true;
            }
            // StampTreeNode(FromStampTree) OK
            if (LocalStampTreeNodeTransferable.localStampTreeNodeFlavor.equals(flavor)) {
                return true;
            }
            // OrderStamp List OK
            if (OrderListTransferable.orderListFlavor.equals(flavor)) {
                return true;
            }

            // プラグイン
            PlugInMenuSupport plugins = parent.getPlugins();
            for (final Map.Entry entry : plugins.entrySet()) {
                open.dolphin.plugin.IPlugin plugin = ((open.dolphin.plugin.IPlugin) entry.getValue());
                PluginWrapper pluginWrapper = new PluginWrapper(plugin);
                if (pluginWrapper.getType() == open.dolphin.plugin.IPlugin.Type.panel) {
                    DataFlavor plugin_supported_flavor = pluginWrapper.getFlavor();
                    if (plugin_supported_flavor != null) {
                        if (plugin_supported_flavor.equals(flavor)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * DropされたModuleInfo(StampInfo)をインポートする。
     * @param tr Transferable
     * @return 成功した時 true
     */
    private boolean stampInfoDrop(Transferable tr) throws IOException {
        try {
            // DropされたTreeNodeを取得する
            StampTreeNode droppedNode = (StampTreeNode) tr.getTransferData(LocalStampTreeNodeTransferable.localStampTreeNodeFlavor);
            // 葉の場合
            if (droppedNode.isLeaf()) {
                ModuleInfoBean stampInfo = (ModuleInfoBean) droppedNode.getStampInfo();
                String role = stampInfo.getStampRole();
                if (role.equals(IInfoModel.ROLE_P)) {
                    pPane.stampInfoDropped(stampInfo);
                } else if (role.equals(IInfoModel.ROLE_TEXT)) {
                    pPane.stampInfoDropped(stampInfo);
                } else if (role.equals(IInfoModel.ROLE_ORCA_SET)) {
                    pPane.stampInfoDropped(stampInfo);
                }
                return true;
            }
            Enumeration e = droppedNode.preorderEnumeration();
            List<ModuleInfoBean> addList = new ArrayList<ModuleInfoBean>();
            String role = null;
            while (e.hasMoreElements()) {
                StampTreeNode node = (StampTreeNode) e.nextElement();
                if (node.isLeaf()) {
                    ModuleInfoBean stampInfo = (ModuleInfoBean) node.getStampInfo();
                    role = stampInfo.getStampRole();
                    if (stampInfo.isSerialized() && (role.equals(IInfoModel.ROLE_P) || (role.equals(IInfoModel.ROLE_TEXT)))) {
                        addList.add(stampInfo);
                    }
                }
            }
            if (role == null) {
                return true;
            }
            if (role.equals(IInfoModel.ROLE_TEXT)) {
                pPane.textStampInfoDropped(addList);
            } else if (role.equals(IInfoModel.ROLE_P)) {
                pPane.stampInfoDropped(addList);
            }
            return true;
        } catch (UnsupportedFlavorException ex) {
              LogWriter.error(getClass(), ex);
        }
        return false;
    }

    /**
     * DropされたStamp(ModuleModel)をインポートする。
     * @param tr Transferable
     * @return インポートに成功した時 true
     */
    private boolean doStampDrop(Transferable tr) throws IOException {
        try {
            // スタンプのリストを取得する
            OrderList list = (OrderList) tr.getTransferData(OrderListTransferable.orderListFlavor);
            ModuleModel[] stamps = list.getOrderList();
            // pPaneにスタンプを挿入する
            for (int i = 0; i < stamps.length; i++) {
                pPane.stamp(stamps[i]);
            }
            // dragggされたスタンプがあるときdroppした数を設定する
            // これで同じpane内でのDnDを判定している
            if (pPane.getDraggedCount() > 0 && pPane.getDrragedStamp() != null) {
                pPane.setDroppedCount(stamps.length);
            }
            return true;
        } catch (UnsupportedFlavorException ex) {
             LogWriter.error(getClass(), ex);
        }
        return false;
    }

    /**
     * クリップボードへデータを転送する。
     */
    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        super.exportToClipboard(comp, clip, action);
        // cut の場合を処理する
        if (action == MOVE) {
            JTextPane pane = (JTextPane) comp;
            if (pane.isEditable()) {
                pane.replaceSelection("");
            }
        }
    }
}
