/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AbstractMasterPanel.java
 *
 * Created on 2010/03/08, 16:52:33
 */
package open.dolphin.order;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import open.dolphin.client.AutoKanjiListener;
import open.dolphin.project.GlobalConstants;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.infomodel.MasterItem;
import open.dolphin.project.GlobalVariables;
import open.dolphin.table.ObjectTableModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

/**
 * マスタ項目パネル　MEMO:画面
 * @author
 */
public abstract class AbstractMasterPanel extends javax.swing.JPanel {

    /** マスタ項目選択プロパティ名 */
    public static final String SELECTED_ITEM_PROP = "selectedItemProp";
    /** 検索中プロパティ名 */
    public static final String BUSY_PROP = "busyProp";
    /** 件数プロパティ名 */
    public static final String ITEM_COUNT_PROP = "itemCount";
    /** キーワードフィールド用の tooltip text */
    protected static final String TOOLTIP_KEYWORD = "漢字が使用できます";
    /** キーワードフィールドの長さ */
    protected static final int KEYWORD_FIELD_LENGTH = 12;
    /** 検索アイコン */
    protected static final String FIND_ICON = "/open/dolphin/resources/images/srch_16.gif";
    /** キーワード部分のボーダタイトル */
    protected static final String keywordBorderTitle = "キーワード検索"; //GlobalVariables.getString("masterSearch.text.keywordBorderTitle");
    /**
     *
     */
    protected static final Color[] masterColors = GlobalConstants.getColorArray("masterSearch.masterColors");
    /**
     *
     */
    protected static final String[] masterNames = {"disease", "medicine", "medicine", "tool_material", "treatment"};//GlobalVariables.getStringArray("masterSearch.masterNames");
    /**
     *
     */
    protected static final String[] masterTabNames = {"傷病名", "内用・外用薬", "注射薬", "特定器材", "診療行為"}; //GlobalVariables.getStringArray("masterSearch.masterTabNames");
    /** 検索結果テーブルの開始行数 */
    protected final int START_NUM_ROWS = 20;
    /** キーワードフィールド */
    protected JTextField keywordField;
    /** 検索アイコン */
    protected ImageIcon findIcon = new ImageIcon(this.getClass().getResource(FIND_ICON));
    /** 検索アイコンを表示するラベル */
    protected JLabel findLabel = new JLabel(findIcon);
    /** ソートボタン配列 */
    protected JRadioButton[] sortButtons;
    /** 検索結果テーブル */
    protected JTable table;
    /** 検索結果テーブルの table model */
    protected ObjectTableModel tableModel;
    /** 検索するマスタ名 */
    protected String master;
    /** 検索するクラス */
    protected String searchClass;
    /** ソート節 */
    protected String sortBy;
    /** order by 節 */
    protected String order;
    /** 選択されたマスタ項目 */
    protected MasterItem selectedItem;
    /** 検索中のフラグ */
    protected boolean busy;
    /** 検索結果件数 */
    protected int itemCount;
    /** タスク用のタイマ */
    protected javax.swing.Timer taskTimer;
    /** 割り込み時間 */
    protected static final int TIMER_DELAY = 200;
    /** 束縛サポート */
    protected PropertyChangeSupport boundSupport;
    /** プレファレンス */
    protected Preferences prefs = Preferences.userNodeForPackage(this.getClass());
    /**
     *
     */
    protected JCheckBox isForwordCheckBox;

    /**
     * MasterPanelオブジェクトを生成する。
     */
    public AbstractMasterPanel() {
        boundSupport = new PropertyChangeSupport(this);
        initComponents();
    }

    /**
     * MasterPanelオブジェクトを生成する。
     * @param master マスタ名
     */
    public AbstractMasterPanel(final String master) {
        //public AbstractMasterPanel(final String master, UltraSonicProgressLabel pulse) {

        this();
        //     setMaster(master);
        this.master = master;
        this.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

        // キーワードフィールドを生成する
        keywordField = new JTextField(KEYWORD_FIELD_LENGTH);
        keywordField.setToolTipText(TOOLTIP_KEYWORD);
        keywordField.setMaximumSize(keywordField.getPreferredSize());
        keywordField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keywordField.getText().trim();
                if (!key.equals("")) {
                    setSortBy("srycd asc, yukoedymd");//キーワードでのマスターの検索で、初期の並びを設定（期限切れのものを下位に表示させるために終了年月日で降順にソート）
                    //   setOrder("desc");
                    order = "desc";
                    search(key);
                }
            }
        });

        isForwordCheckBox = new JCheckBox("部分一致");
        isForwordCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keywordField.getText().trim();
                if (!key.equals("")) {
                    setSortBy("srycd asc, yukoedymd");//キーワードでのマスターの検索で、初期の並びを設定（期限切れのものを下位に表示させるために終了年月日で降順にソート）
                    //    setOrder("desc");
                    order = "desc";
                    search(key);
                }
            }
        });

        // フォーカスがあたった時 IME をオンにする

        keywordField.addFocusListener(AutoKanjiListener.getInstance());

        // 初期化する
        initialize();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * サブクラスが実装する初期化メソッド。
     */
    protected abstract void initialize();

    /**
     * 束縛リスナを登録する。
     * @param prop プロパティ名
     * @param l リスナ
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     * 束縛リスナを削除する。
     * @param prop プロパティ名
     * @param l リスナ
     */
    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     * 選択されたマスタ項目プロパティをセットしリスナへ通知する。
     * @param item 
     */
    public void setSelectedItem(MasterItem item) {
        MasterItem oldItem = selectedItem;
        selectedItem = item;
        boundSupport.firePropertyChange(SELECTED_ITEM_PROP, oldItem, selectedItem);
    }

    /**
     * 検索中プロパティを返す。
     * @return 検索中の時 true
     */
    //   public boolean isBusy() {
    //      return busy;
    //  }

    /**
     * 検索中プロパティを設定しリスナへ通知する。
     * @param newBusy 検索中の時 true
     */
    public void setBusy(boolean newBusy) {
        boolean oldBusy = busy;
        busy = newBusy;
        boundSupport.firePropertyChange(BUSY_PROP, oldBusy, busy);
    }

    /**
     * 件数を返す。
     * @return マスタ検索の結果件数
     */
    //  public int getCount() {
    //       return itemCount;
    //   }

    /**
     * 検索結果件数をセットしリスナへ通知する。
     * @param count マスタ検索の結果件数
     */
    public void setItemCount(int count) {
        itemCount = count;
        boundSupport.firePropertyChange(ITEM_COUNT_PROP, -1, itemCount);
    }

    /**
     * 検索クラスを設定する。
     * @param searchClass 検索クラス
     */
    public void setSearchClass(String searchClass) {
        this.searchClass = searchClass;
    }

    /**
     * ソート項目を設定する。
     * @param sortBy ソート項目
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * マスタ名を返す。
     * @return マスタ名
     */
    private String getMaster() {
        return master;
    }

    /**
     * マスタ名を設定する。
     * @param master マスタ名
     */
   // private void setMaster(String master) {
  //      this.master = master;
   // }

    /**
     * order by 項目を設定する。
     * @param order order by 項目
     */
    //  private void setOrder(String order) {
    //     this.order = order;
    // }
    
    /**
     * このマスタパネルのタブが選択された時コールされる。
     * 検索結果件数を書き換える。
     */
    public void enter() {
        setItemCount(tableModel.getObjectCount());

        //タブを切り替えると検索結果もクリアする
        tableModel.clear();

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                keywordField.requestFocusInWindow();
            }
        });
    }

    /**
     * 終了処理を行う。
     */
    public void dispose() {
        if (tableModel != null) {
            tableModel.clear();
        }
    }

    /**
     * 引数のキーワードからマスタを検索する。
     * @param text キーワード
     */
    protected void search(final String text) {
        search(text, !isForwordCheckBox.isSelected());
    }

    /**
     * 引数のキーワードからマスタを検索する。
     * @param text キーワード
     * @param startsWith
     */
    protected void search(final String text, final boolean startsWith) {
        // CLAIM(Master) Address が設定されていない場合に警告する
        String address = GlobalVariables.getClaimAddress();
        if (address == null || address.equals("")) {
            String msg0 = "レセコンのIPアドレスが設定されていないため、マスターを検索できません。";
            String msg1 = "環境設定メニューからレセコンのIPアドレスを設定してください。";
            Object message = new String[]{msg0, msg1};
            Window parent = SwingUtilities.getWindowAncestor(AbstractMasterPanel.this);
            String title = GlobalConstants.getFrameTitle(master);
            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
            return;
        }

        // DAOを生成する
        final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();// MEMO;Unused?

        processResult(dao.isNoError(), dao.getByName(master, text, startsWith, searchClass, sortBy, order), dao.getErrorMessage());
    }

    /**
     * 引数のキーワードからマスタを検索する。
     * @param text キーワード
     * @param startsWith
     */
    protected void searchByCode(final String text, final boolean startsWith) {

        // CLAIM(Master) Address が設定されていない場合に警告する
        String address = GlobalVariables.getClaimAddress();
        if (address == null || address.equals("")) {
            String msg0 = "レセコンのIPアドレスが設定されていないため、マスターを検索できません。";
            String msg1 = "環境設定メニューからレセコンのIPアドレスを設定してください。";
            Object message = new String[]{msg0, msg1};
            Window parent = SwingUtilities.getWindowAncestor(AbstractMasterPanel.this);
            String title = GlobalConstants.getFrameTitle(master);
            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
            return;
        }

        // DAOを生成する
        final SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");

        processResult(dao.isNoError(), dao.getByName(master, text, startsWith, searchClass, sortBy, order), dao.getErrorMessage());
        keywordField.setText("");
    }

    /**
     * 検索結果をテーブルへ表示する。
     * @param noErr 
     * @param result
     * @param message
     */
    protected void processResult(boolean noErr, Object result, String message) {
        if (noErr) {
            tableModel.setObjectList((List) result);
            setItemCount(tableModel.getObjectCount());
        } else {
            String title = GlobalConstants.getFrameTitle(master);
            JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * ソートボタンへ登録するアクションリスナクラス。
     */
    protected class SortActionListener implements ActionListener {

        private AbstractMasterPanel target;
        private String sortBy;
        private int btnIndex;

        /**
         *
         * @param target
         * @param sortBy
         * @param btnIndex
         */
        public SortActionListener(AbstractMasterPanel target, String sortBy, int btnIndex) {
            this.target = target;
            this.sortBy = sortBy;
            this.btnIndex = btnIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            prefs.putInt("masterSearch." + target.getMaster() + ".sort", btnIndex);
            target.setSortBy(sortBy);
        }
    }

    /**
     *
     */
    public void clear() {
        tableModel.clear();
        keywordField.setText("");
    }
}
