package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import open.dolphin.client.editor.stamp.StampTree;
import open.dolphin.client.editor.stamp.StampTreeNode;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import open.dolphin.dao.SqlOrcaSetDao;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.OrcaInputCd;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;
import open.dolphin.utils.ReflectMonitor;

/**
 * ORCA StampTree クラス。
 *
 * @author Kazushi Minagawa
 */
public class OrcaTree extends StampTree {

    //private static final String MONITOR_TITLE = "ORCAセット検索";
    /** ORCA 入力セットをフェッチしたかどうかのフラグ */
    private boolean fetched;

    /** 
     * Creates a new instance of OrcaTree 
     * @param model 
     */
    public OrcaTree(TreeModel model) {
        super(model);
    }

    /**
     * ORCA 入力セットをフェッチしたかどうかを返す。
     * @return 取得済みのとき true
     */
    public boolean isFetched() {
        return fetched;
    }

    /**
     * ORCA 入力セットをフェッチしたかどうかを設定する。
     * @param fetched 取得済みのとき true
     */
    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    /**
     * StampBox のタブでこのTreeが選択された時コールされる。
     */
    @Override
    public void enter() {

            if (!fetched) {
                // CLAIM(Master) Address が設定されていない場合に警告する
                String address = GlobalVariables.getClaimAddress();
                if (address == null || address.equals("")) {
                    return;
                }
                if (SwingUtilities.isEventDispatchThread()) {
                    fetchOrcaSet();
                } else {
                    fetchOrcaSet2();
                }
            }
        
    }

    /**
     * ORCA の入力セットを取得しTreeに加える。
     */
    private void fetchOrcaSet2() {
        try {
            SqlOrcaSetDao dao = new SqlOrcaSetDao();
            List<OrcaInputCd> inputSet = dao.getInputSetFromOrca();
            StampTreeNode root = (StampTreeNode) this.getModel().getRoot();
            for (OrcaInputCd set : inputSet) {
                ModuleInfoBean stampInfo = set.getStampInfo();
                StampTreeNode node = new StampTreeNode(stampInfo);
                root.add(node);
            }
            DefaultTreeModel model = (DefaultTreeModel) this.getModel();
            model.reload(root);
            setFetched(true);
        } catch (Exception e) {
            LogWriter.error(getClass(), "KarteSaveTask succeeded", e);
        }
    }

    /**
     * ORCA の入力セットを取得しTreeに加える。
     */
    private void fetchOrcaSet() {
        // DAOを生成する
        final SqlOrcaSetDao dao = new SqlOrcaSetDao();
        // ReflectMonitor を生成する
        final ReflectMonitor rm = new ReflectMonitor();

        rm.setReflection(dao, "getInputSetFromOrca", (Class[]) null, (Object[]) null);
        rm.setMonitor(SwingUtilities.getWindowAncestor(this), "ORCAセット検索", "入力セットを検索しています...  ", 200, 60 * 1000);

        // ReflectMonitor の結果State property の束縛リスナを生成する
        PropertyChangeListener pl = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {

                int state = ((Integer) e.getNewValue()).intValue();
                switch (state) {

                    case ReflectMonitor.DONE:
                        processResult(dao.isNoError(), rm.getResult(), dao.getErrorMessage());
                        break;

                    case ReflectMonitor.TIME_OVER:
                        Window parent = SwingUtilities.getWindowAncestor(OrcaTree.this);
                        String title = "ORCAセット検索"; //GlobalVariables.getString(MONITOR_TITLE);
                        new TimeoutWarning(parent, title, null).start();
                        break;

                    case ReflectMonitor.CANCELED:
                        break;
                    default: LogWriter.fatal(getClass(), "case default");
                }
            }
        };
        rm.addPropertyChangeListener(pl);
        rm.start();
    }

    /**
     * ORCAセットのStampTreeを構築する。
     */
    private void processResult(boolean noErr, Object result, String message) {

        if (noErr) {

            List<OrcaInputCd> inputSet = (List<OrcaInputCd>) result;
            StampTreeNode root = (StampTreeNode) this.getModel().getRoot();

            for (OrcaInputCd set : inputSet) {
                ModuleInfoBean stampInfo = set.getStampInfo();
                StampTreeNode node = new StampTreeNode(stampInfo);
                root.add(node);
            }

            DefaultTreeModel model = (DefaultTreeModel) this.getModel();
            model.reload(root);
            setFetched(true);

        } else {
            String title = GlobalConstants.getFrameTitle("ORCAセット検索");
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
        }
    }
}
