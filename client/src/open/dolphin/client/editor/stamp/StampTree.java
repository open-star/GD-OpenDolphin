package open.dolphin.client.editor.stamp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.sql.SQLException;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import open.dolphin.client.MouseDragDetecter;
import open.dolphin.client.TreeInfo;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.ClaimBundle.Enabled;
import open.dolphin.infomodel.IStampInfo;
import open.dolphin.utils.CombinedStringParser;
import open.dolphin.infomodel.MedicineEntry;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalVariables;
import open.dolphin.delegater.remote.RemoteStampDelegater;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.infomodel.StampModel;
import open.dolphin.infomodel.TextStampModel;
import open.dolphin.utils.GUIDGenerator;

/**
 * StampTree
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class StampTree extends JTree implements TreeModelListener {

    /**
     *
     */
    public static final String SELECTED_NODE_PROP = "selectedNodeProp";
    private static final long serialVersionUID = -4651151848166376384L;
    private static final int TOOLTIP_LENGTH = 35;
    private static final ImageIcon ASP_ICON = GlobalConstants.getImageIcon("move2_16.gif");
    private static final ImageIcon LOCAL_ICON = GlobalConstants.getImageIcon("move2_16.gif");
    private static final String NEW_FOLDER_NAME = "新規フォルダ";
    private static final String STAMP_SAVE_TASK_NAME = "スタンプ保存";
    private boolean asp;    // ASP Tree かどうかのフラグ 
    private boolean userTree;    // 個人用Treeかどうかのフラグ 
    private StampBoxFrame stampBox;    // StampBox

    /**
     * StampTreeオブジェクトを生成する。
     *
     * @param model
     */
    public StampTree(TreeModel model) {
        super(model);

        this.putClientProperty("JTree.lineStyle", "Angled"); // 水平及び垂直線を使用する
        this.setEditable(false); // ノード名を編集不可にする
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // Single Selection// にする
        this.setRootVisible(false);

        this.addMouseMotionListener(new MouseDragDetecter());

        final RemoteStampDelegater delegater = new RemoteStampDelegater();

        // デフォルトのセルレンダラーを置き換える
        final TreeCellRenderer oldRenderer = this.getCellRenderer();
        TreeCellRenderer r = new TreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component cellRenderer = oldRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                if (leaf && cellRenderer instanceof JLabel) {
                    JLabel label = (JLabel) cellRenderer;
                    Object moduleInfo = ((StampTreeNode) value).getUserObject();
                    if (moduleInfo instanceof ModuleInfoBean) {

                        // 固有のアイコンを設定する
                        if (isAsp()) {
                            label.setIcon(ASP_ICON);
                        } else {
                            label.setIcon(LOCAL_ICON);
                        }
                        // ToolTips を設定する
                        label.setToolTipText(((ModuleInfoBean) moduleInfo).getStampMemo());
                        CombinedStringParser combinedName = new CombinedStringParser(((ModuleInfoBean) moduleInfo).getStampName());
                        label.setText(combinedName.toPlainString());
                        List<MedicineEntry> updates = new ArrayList<MedicineEntry>();
                        GregorianCalendar gc = new GregorianCalendar();

                        // MEMO: エディタから発行の場合はスルーする
                        ModuleInfoBean infoBean = (ModuleInfoBean) moduleInfo;
                        if (infoBean.getStampId() != null && !infoBean.getStampRole().equals("orcaSet")) {
                            StampModel stampModel = delegater.getStamp(infoBean.getStampId());
                            if (stampModel != null) {
                                if (stampModel.getStampBytes().length != 0) {
                                    IInfoModel stamp = stampModel.toInfoModel();
                                    if ((stamp instanceof BundleDolphin)) {
                                        Enabled enabled = ((BundleDolphin) stamp).getStampEnabled(updates, gc);
                                        label.setForeground(getStampColor(enabled));
                                    }
                                }
                            }else{
                                 label.setForeground(getStampColor(Enabled.DELETED));
                            }
                        }
                    }
                }
                return cellRenderer;
            }
        };
        this.setCellRenderer(r);
        model.addTreeModelListener(this);  // Listens TreeModelEvent
        enableToolTips(true);              // Enable ToolTips
    }

    /**
     *
     * @param enabled
     * @return
     */
    private Color getStampColor(Enabled enabled) {
        Color result;
        switch (enabled) {
            case NORMAL:
                result = Color.black;
                break;
            case OUT_OF_DATE:
                result = Color.gray;
                break;
            case UPDATE_AVAILABLE:
                result = Color.red;//GlobalSettings.Parts.RED
                break;
            case DELETED:
                result = Color.magenta;//GlobalSettings.Parts.RED
                break;
            default:
                result = Color.black;
        }
        return result;
    }

    /**
     * このStampTreeのTreeInfoを返す。
     * @return Tree情報
     */
    public TreeInfo getTreeInfo() {
        StampTreeNode node = (StampTreeNode) this.getModel().getRoot();
        TreeInfo info = (TreeInfo) node.getUserObject();
        return info;
    }

    /**
     * このStampTreeのエンティティを返す。
     * @return エンティティ
     */
    public String getEntity() {
        return getTreeInfo().getEntity();
    }

    /**
     * このStampTreeの名前を返す。
     * @return 名前
     */
    public String getTreeName() {
        return getTreeInfo().getName();
    }

    /**
     * UserTreeかどうかを返す。
     * @return UserTreeの時true
     */
    private boolean isUserTree() {
        return userTree;
    }

    /**
     * UserTreeかどうかを設定する。
     * @param userTree UserTreeの時true
     */
    public void setUserTree(boolean userTree) {
        this.userTree = userTree;
    }

    /**
     * ASP提供Treeかどうかを返す。
     * @return ASP提供の時 true
     */
    private boolean isAsp() {
        return asp;
    }

    /**
     * ASP提供Treeかどうかを設定する。
     * @param asp ASP提供の時 true
     */
    public void setAsp(boolean asp) {
        this.asp = asp;
    }

    /**
     * Enable or disable tooltip

     * @param state
     */
    private void enableToolTips(boolean state) {
        ToolTipManager mgr = ToolTipManager.sharedInstance();
        if (state) {
            mgr.registerComponent(this);            // Enable tooltips
        } else {
            mgr.unregisterComponent(this);
        }
    }

    /**
     * Set StampBox reference
     * @param stampBox
     */
    public void setStampBox(StampBoxFrame stampBox) {
        this.stampBox = stampBox;
    }

    /**
     * 選択されているノードを返す。
     * @return
     */
    private StampTreeNode getSelectedNode() {
        return (StampTreeNode) this.getLastSelectedPathComponent();
    }

    /**
     * 引数のポイント位置のノードを返す。
     * MEMO: unused?
     * @param p
     * @return
     */
    private StampTreeNode getNode(Point p) {
        TreePath path = this.getPathForLocation(p.x, p.y);
        return (path != null) ? (StampTreeNode) path.getLastPathComponent() : null;
    }

    /**
     * このStampTreeにenter()する。
     */
    public void enter() {
    }

    /**
     * KartePaneから drop されたスタンプをツリーに加える。
     * @param droppedStamp
     * @param selected
     * @return
     * @throws SQLException
     */
    public boolean addStamp(ModuleModel droppedStamp, final StampTreeNode selected) throws SQLException {
        if (droppedStamp != null) {
            IStampInfo droppedInfo = droppedStamp.getModuleInfo();      // Drop された Stamp の ModuleInfoを得る
            String extent = "";

            final StampModel stampModel = new StampModel();                 // データベースへ droppedStamp のデータモデルを保存する
            final String stampId = GUIDGenerator.generate(stampModel);      // Entityを生成する
            stampModel.initialize(stampId, GlobalVariables.getUserModel().getId(), droppedInfo.getEntity(), getXMLBytes(droppedStamp.getModel()));

            // Tree に加える新しい StampInfo を生成する
            final ModuleInfoBean info = new ModuleInfoBean();
            info.initialize(stampId, droppedInfo.getStampName() + extent, constractToolTip(droppedStamp), droppedInfo.getEntity(), droppedInfo.getStampRole());
            final RemoteStampDelegater stampDelegater = new RemoteStampDelegater();     // Delegator を生成する
            String result = stampDelegater.putStamp(stampModel);
            if ((!stampDelegater.isError()) && result.equals(stampId)) {
                addInfoToTree(info, selected);
            }
            return true;
        }
        return false;
    }

    /**
     *
     * @param root
     * @param name
     * @return
     */
    private StampTreeNode findNode(StampTreeNode root, String name) {
        int childCount = root.getChildCount();
        for (int index = 0; index < childCount; index++) {
            StampTreeNode currentNode = (StampTreeNode) root.getChildAt(index);
            if (currentNode.isLeaf()) {
                if (name.equals(currentNode.getStampInfo().getStampName())) {
                    return currentNode;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * @param index
     * @return
     */
    private String createAlternateName(String name, int index) {
        return name + '(' + Integer.toString(index) + ')';
    }

    /**
     *
     * @param root
     * @param node
     */
    private void setAlternateName(StampTreeNode root, StampTreeNode node) {
        int nameExtent = 0;
        String initialNodeName = ((ModuleInfoBean) node.getUserObject()).getStampName();
        String nodeName = initialNodeName;
        while (true) {
            if (findNode(root, nodeName) != null) {
                nameExtent++;
                nodeName = createAlternateName(initialNodeName, nameExtent);
            } else {
                return;
            }
            ((ModuleInfoBean) node.getUserObject()).setStampName(nodeName);
        }
    }

    /**
     * StampTree に新しいノードを加える。
     * @param info 追加するノードの情報
     * @param selected カーソルの下にあるノード(Drop 位置のノード）
     */
    private void addInfoToTree(ModuleInfoBean info, StampTreeNode selected) {
        // StampInfo から新しい StampTreeNode を生成する
        StampTreeNode node = new StampTreeNode(info);
        // Drop 位置のノードによって追加する位置を決める
        if (selected != null && selected.isLeaf()) {  // Drop位置のノードが葉の場合、その前に挿入する
            StampTreeNode newParent = (StampTreeNode) selected.getParent();
            int index = newParent.getIndex(selected);
            DefaultTreeModel model = (DefaultTreeModel) this.getModel();
            StampTreeNode root = (StampTreeNode) model.getRoot();
            setAlternateName(root, node);
            model.insertNodeInto(node, newParent, index);
            TreeNode[] path = model.getPathToRoot(node);           // 追加したノードを選択する
            ((JTree) this).setSelectionPath(new TreePath(path));
        } else if (selected != null && (!selected.isLeaf())) { // Drop位置のノードが子を持つ時、最後の子として挿入する
            DefaultTreeModel model = (DefaultTreeModel) this.getModel();
            StampTreeNode root = (StampTreeNode) model.getRoot();
            setAlternateName(root, node);
            model.insertNodeInto(node, selected, selected.getChildCount());
            TreeNode[] path = model.getPathToRoot(node);        // 追加したノードを選択する
            ((JTree) this).setSelectionPath(new TreePath(path));
        } else {
            // Drop 位置のノードが null でコールされるケースがある
            // 1. このtreeのスタンプではない場合、該当するTreeのルートに加える
            // 2. パス Tree など、まだノードを持たない初期状態の時
            //
            // Stamp ボックスから entity に対応する tree を得る
            StampTree another = stampBox.getStampTree(info.getEntity());
            boolean myTree = (another == this) ? true : false;
            final String treeName = another.getTreeName();
            DefaultTreeModel model = (DefaultTreeModel) another.getModel();
            StampTreeNode root = (StampTreeNode) model.getRoot();
            setAlternateName(root, node);
            root.add(node);
            model.reload(root);
            // 追加したノードを選択する
            TreeNode[] path = model.getPathToRoot(node);
            ((JTree) this).setSelectionPath(new TreePath(path));
            // メッセージを表示する
            if (!myTree) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        StringBuilder buf = new StringBuilder();
                        buf.append("スタンプは個人用の ");
                        buf.append(treeName);
                        buf.append(" に保存しました。");
                        JOptionPane.showMessageDialog(StampTree.this, buf.toString(), GlobalConstants.getFrameTitle(STAMP_SAVE_TASK_NAME), JOptionPane.INFORMATION_MESSAGE);
                    }
                });
            }
        }
    }

    /**
     * Diagnosis Table から Drag & Drop されたRegisteredDiagnosisをスタンプ化する。
     * @param rd
     * @param selected
     * @return
     * @throws SQLException 
     */
    public boolean addDiagnosis(RegisteredDiagnosisModel rd, final StampTreeNode selected) throws SQLException {
        if (rd != null) {
            rd.setId(0L);
            rd.setKarte(null);
            rd.setCreator(null);
            rd.setDiagnosisCategoryModel(null);
            rd.setDiagnosisOutcomeModel(null);
            rd.setFirstEncounterDate(null);
            rd.setStartDate(null);
            rd.setEndDate(null);
            rd.setRelatedHealthInsurance(null);
            rd.setFirstConfirmDate(null);
            rd.setConfirmDate(null);
            rd.setStatus(null);
            rd.setPatientLiteModel(null);
            rd.setUserLiteModel(null);
            RegisteredDiagnosisModel add = new RegisteredDiagnosisModel();
            add.setDiagnosis(rd.getDiagnosis());
            add.setDiagnosisCode(rd.getDiagnosisCode());
            add.setDiagnosisCodeSystem(rd.getDiagnosisCodeSystem());
            ModuleModel stamp = new ModuleModel();
            stamp.setModel(add);
            // データベースへ Stamp のデータモデルを永続化する
            final StampModel addStamp = new StampModel();
            final String stampId = GUIDGenerator.generate(addStamp);
            addStamp.initialize(stampId, GlobalVariables.getUserModel().getId(), IInfoModel.ENTITY_DIAGNOSIS, getXMLBytes(stamp.getModel()));
            // Tree に加える 新しい StampInfo を生成する
            final ModuleInfoBean info = new ModuleInfoBean();
            info.initialize(stampId, add.getDiagnosis(), null, IInfoModel.ENTITY_DIAGNOSIS, IInfoModel.ENTITY_DIAGNOSIS);
            StringBuilder buf = new StringBuilder();
            buf.append(add.getDiagnosis());
            String cd = add.getDiagnosisCode();
            if (cd != null) {
                buf.append("(");
                buf.append(cd);
                buf.append(")"); // Tooltip
            }
            info.setStampMemo(buf.toString());
            final RemoteStampDelegater sdl = new RemoteStampDelegater();
            String result = sdl.putStamp(addStamp);
            if ((!sdl.isError()) && result.equals(stampId)) {
                addInfoToTree(info, selected);
            }
            return true;
        }
        return false;
    }

    /**
     * エディタで生成した病名リストを登録する。
     * @param list
     * @throws SQLException
     */
    public void addDiagnosis(List<RegisteredDiagnosisModel> list) throws SQLException {
        if (list == null || list.isEmpty()) {
            return;
        }
        final List<StampModel> stampList = new ArrayList<StampModel>();
        final List<ModuleInfoBean> infoList = new ArrayList<ModuleInfoBean>();
        for (RegisteredDiagnosisModel rd : list) {
            // クリア
            rd.clear();
            RegisteredDiagnosisModel diagnosisModel = new RegisteredDiagnosisModel(rd);
            ModuleModel stamp = new ModuleModel(diagnosisModel);
            // データベースへ Stamp のデータモデルを永続化する
            StampModel addStamp = new StampModel();
            String stampId = GUIDGenerator.generate(addStamp);
            addStamp.initialize(stampId, GlobalVariables.getUserModel().getId(), IInfoModel.ENTITY_DIAGNOSIS, getXMLBytes(stamp.getModel()));
            stampList.add(addStamp);
            // Tree に加える 新しい StampInfo を生成する
            ModuleInfoBean info = new ModuleInfoBean();
            info.initialize(stampId, diagnosisModel.getDiagnosis(), null, IInfoModel.ENTITY_DIAGNOSIS, IInfoModel.ENTITY_DIAGNOSIS);
            StringBuilder buf = new StringBuilder();
            buf.append(diagnosisModel.getDiagnosis());
            String cd = diagnosisModel.getDiagnosisCode();
            if (cd != null) {
                buf.append("(");
                buf.append(cd);
                buf.append(")"); // Tooltip
            }
            info.setStampMemo(buf.toString());
            infoList.add(info);
        }
        final RemoteStampDelegater sdl = new RemoteStampDelegater();
        sdl.putStamp(stampList);
        if (!sdl.isError()) {
            for (ModuleInfoBean info : infoList) {
                addInfoToTree(info, null);
            }
        }
    }

    /**
     * テキストスタンプを追加する。
     * @param text 
     * @param selected 
     * @return
     * @throws SQLException
     */
    public boolean addTextStamp(String text, final StampTreeNode selected) throws SQLException {
        if ((text == null) || (text.length() == 0) || text.equals("")) {
            return false;
        }
        TextStampModel stamp = new TextStampModel();
        stamp.initialize(text);
        // データベースへ Stamp のデータモデルを永続化する
        final StampModel newStamp = new StampModel();
        final String stampId = GUIDGenerator.generate(newStamp);
        newStamp.initialize(stampId, GlobalVariables.getUserModel().getId(), IInfoModel.ENTITY_TEXT, getXMLBytes((IInfoModel) stamp));
        // Tree へ加える 新しい StampInfo を生成する
        final ModuleInfoBean info = new ModuleInfoBean();
        int len = text.length() > 16 ? 16 : text.length();
        String name = text.substring(0, len);
        len = name.indexOf("\n");
        if (len > 0) {
            name = name.substring(0, len);
        }
        info.initialize(stampId, name, text, IInfoModel.ENTITY_TEXT, IInfoModel.ENTITY_TEXT);
        final RemoteStampDelegater delegater = new RemoteStampDelegater();
        String result = delegater.putStamp(newStamp);
        if ((!delegater.isError()) && result.equals(stampId)) {
            addInfoToTree(info, selected);
        }
        return true;
    }

    /**
     * スタンプの情報を表示するための文字列を生成する。
     * @param stamp 情報を生成するスタンプ
     * @return スタンプの情報文字列
     */
    protected String constractToolTip(ModuleModel stamp) {
        String result = null;
        try {
            StringBuilder buf = new StringBuilder();
            BufferedReader reader = new BufferedReader(new StringReader(stamp.getModel().toString()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
                if (buf.length() < TOOLTIP_LENGTH) {
                    buf.append(",");
                } else {
                    break;
                }
            }
            reader.close();
            if (buf.length() > TOOLTIP_LENGTH) {
                buf.setLength(TOOLTIP_LENGTH);
            }
            buf.append("...");
            result = buf.toString();
        } catch (IOException e) {
            e.toString();
        }
        return result;
    }

    /**
     * スタンプタスク共通の warning ダイアログを表示する。
     * MEMO: unused?
     * @param title  ダイアログウインドウに表示するタイトル
     * @param message　エラーメッセージ
     */
    private void warning(String message) {
        JOptionPane.showMessageDialog(StampTree.this, message, GlobalConstants.getFrameTitle("スタンプ"), JOptionPane.WARNING_MESSAGE);
    }

    /**
     * ノードの名前を変更する。
     */
    public void renameNode() {
        if (isUserTree()) {
// Root へのパスを取得する
            StampTreeNode node = getSelectedNode();
            if (node == null) {
                return;
            }

            TreeNode[] nodes = node.getPath();
            TreePath path = new TreePath(nodes);
            // 編集を開始する
            this.setEditable(true);
            this.startEditingAtPath(path);
        }
    }

    /**
     * ノードを削除する。
     */
    public void deleteNode() {
        if (isUserTree()) {// 削除するノードを取得する
            final StampTreeNode theNode = getSelectedNode();// 右クリックで選択されている
            if (theNode == null) {
                return;
            }
// このノードをルートにするサブツリーを前順走査する列挙を生成して返します。
// 列挙の nextElement() メソッドによって返される最初のノードは、この削除するノードです。
            Enumeration e = theNode.preorderEnumeration();
            final List<String> deleteList = new ArrayList<String>();  // このリストのなかに削除するノードとその子を含める
            boolean hasEditor = false;  // エディタから発行があるかどうかのフラグ
            while (e.hasMoreElements()) {
                StampTreeNode node = (StampTreeNode) e.nextElement();
                if (node.isLeaf()) {
                    ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                    String stampId = info.getStampId();
                    // エディタから発行がある場合は中止する
                    if (info.getStampName().equals("エディタから発行") && (!info.isSerialized())) {
                        hasEditor = true;
                        break;
                    }
                    // IDが付いているもののみを加える
                    if (stampId != null) {
                        deleteList.add(stampId);
                    }
                }
            }

            // エディタから発行が有った場合はダイアログを表示し
            // リターンする
            if (hasEditor) {
                String msg0 = "エディタから発行は消去できません。フォルダに含まれている";
                String msg1 = "場合は Drag & Drop で移動後、再度実行してください。";
                String taskTitle = "スタンプ"; //GlobalVariables.getString("stamptree.title");
                JOptionPane.showMessageDialog((Component) null, new Object[]{msg0, msg1}, GlobalConstants.getFrameTitle(taskTitle), JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            // 削除するフォルダが空の場合は削除してリターンする
            // リストのサイズがゼロかつ theNode が葉でない時
         //   if (deleteList.isEmpty() && (!theNode.isLeaf())) {
          //      DefaultTreeModel model = (DefaultTreeModel) (StampTree.this).getModel();
         //       model.removeNodeFromParent(theNode);
         //       return;
        //    }
            //MEMO: スタンプの実体は共用される可能性があるため削除しない。
            // データベースのスタンプを削除するデリゲータを生成する
       //     final RemoteStampDelegater sdl = new RemoteStampDelegater();
      //      sdl.removeStamp(deleteList);
        //    if ((!sdl.isError())) {
                DefaultTreeModel model = (DefaultTreeModel) (StampTree.this).getModel();
                model.removeNodeFromParent(theNode);
         //   }
        }
    }

    /**
     * 新規のフォルダを追加する
     */
    public void createNewFolder() {
        if (isUserTree()) {// フォルダノードを生成する
            StampTreeNode folder = new StampTreeNode(NEW_FOLDER_NAME);
            // 生成位置となる選択されたノードを得る
            StampTreeNode selected = getSelectedNode();
            if (selected != null && selected.isLeaf()) {
                // 選択位置のノードが葉の場合、その前に挿入する
                StampTreeNode newParent = (StampTreeNode) selected.getParent();
                int index = newParent.getIndex(selected);
                DefaultTreeModel model = (DefaultTreeModel) this.getModel();
                model.insertNodeInto(folder, newParent, index);
            } else if (selected != null && (!selected.isLeaf())) {
                // 選択位置のノードが子を持つ時、最後の子として挿入する
                DefaultTreeModel model = (DefaultTreeModel) this.getModel();
                model.insertNodeInto(folder, selected, selected.getChildCount());
            }
        }
    }

    /**
     * ノードを移動する。
     * INFORMATION ノードを移動する。
     */
    private void moveTo(StampTreeNode parentNode, StampTreeNode sourceNode, StampTreeNode targetNode) {
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        if ((parentNode != null) && (sourceNode != null) && (targetNode != null)) {
            if ((sourceNode.isMoveable()) && (targetNode.isMoveable())) {
                int index = parentNode.getIndex(targetNode);
                model.removeNodeFromParent(sourceNode);
                model.insertNodeInto(sourceNode, parentNode, index);
            }
        }
    }

    /**
     * 選択されたノードをひとつ手前に移動する。
     * INFORMATION
     */
    public void moveUp() {
        if (isUserTree()) {
            StampTreeNode selectedNode = getSelectedNode();
            StampTreeNode parentNode = (StampTreeNode) selectedNode.getParent();
            StampTreeNode prevNode = (StampTreeNode) parentNode.getChildBefore(selectedNode);
            moveTo(parentNode, selectedNode, prevNode);
        }
    }

    /**
     * 選択されたノードをひとつ後ろに移動する。
     * INFORMATION
     */
    public void moveDown() {
        if (isUserTree()) {
            StampTreeNode selectedNode = getSelectedNode();
            StampTreeNode parentNode = (StampTreeNode) selectedNode.getParent();
            StampTreeNode nextNode = (StampTreeNode) parentNode.getChildAfter(selectedNode);
            moveTo(parentNode, selectedNode, nextNode);
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        this.setEditable(false);
    }

    /**
     *
     * @param e
     */
    @Override
    public void treeNodesInserted(TreeModelEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void treeStructureChanged(TreeModelEvent e) {
    }

    /**
     *
     * @param bean
     * @return
     */
    private byte[] getXMLBytes(Object bean) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(bo));
        e.writeObject(bean);
        e.close();
        return bo.toByteArray();
    }

    /**
     *
     */
    public void saveStampTree() {
        RemoteStampDelegater sdl = new RemoteStampDelegater();
        IStampTreeModel _treeModel = stampBox.getUserStampBox().getStampTreeModel();
        _treeModel.setTreeXml(stampBox.getStampTree());
        sdl.putTree(_treeModel);
        if (sdl.isError()) {
            LogWriter.error(getClass(), sdl.getErrorMessage());
        }
    }
}
