package open.dolphin.client.editor.stamp;

import open.dolphin.project.GlobalConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import open.dolphin.client.DefaultStampTreeXmlBuilder;
import open.dolphin.delegater.remote.RemoteStampDelegater;
import open.dolphin.helper.GridBagBuilder;
import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.IStampTreeModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.StampTreeModel;
import open.dolphin.helper.ComponentMemory;
import open.dolphin.helper.SynchronizedTask;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

/**
 * StampTreePublisher
 *
 * @author Kazushi, Minagawa
 *
 */
public class StampPublisher {

    /**
     *
     */
    public enum PublishedState {

        /**
         * 
         */
        NONE,
        /**
         *
         */
        SAVED_NONE,
        /**
         * 
         */
        LOCAL,
        /**
         *
         */
        GLOBAL
    };
    private static final int TT_NONE = -1;
    private static final int TT_LOCAL = 0;
    private static final int TT_PUBLIC = 1;
    private static final int WIDTH = 845;
    private static final int HEIGHT = 477;
    private StampBoxFrame stampBox;
    private String title = "スタンプ公開";
    private JFrame dialog;
    private JLabel infoLable;
    private JLabel instLabel;
    private JLabel publishedDate;
    private JTextField stampBoxName;
    private JTextField partyName;
    private JTextField contact;
    private JTextField description;
    private JRadioButton local;
    private JRadioButton publc;
    private JButton publish;
    private JButton cancel;
    private JButton cancelPublish;
    private JCheckBox[] entities;
    private JComboBox category;
    private int publishType = TT_NONE;
    private boolean okState;
    private RemoteStampDelegater sdl;
    private PublishedState publishState;
    private ApplicationContext appCtx;
    private Application app;

    /**
     *
     * @param stampBox
     */
    public StampPublisher(StampBoxFrame stampBox) {
        this.stampBox = stampBox;
        appCtx = GlobalConstants.getApplicationContext();
        app = appCtx.getApplication();
    }

    /**
     *
     */
    public void start() {

        //dialog = new JDialog((JFrame) null, GlobalVariables.getFrameTitle(title), true);
        dialog = new JFrame(GlobalConstants.getFrameTitle(title));

        //<TODO>アイコンを適切な物に変更
        ImageIcon icon = GlobalConstants.getImageIcon("web_32.gif");
        dialog.setIconImage(icon.getImage());

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                stop();
            }
        });
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int n = GlobalConstants.isMac() ? 3 : 2;
        int x = (screen.width - WIDTH) / 2;
        int y = (screen.height - HEIGHT) / n;
        ComponentMemory cm = new ComponentMemory(dialog, new Point(x, y), new Dimension(WIDTH, HEIGHT), this);
        cm.setToPreferenceBounds();

        JPanel contentPane = createContentPane();
        contentPane.setOpaque(true);
        dialog.setContentPane(contentPane);

        stampBox.getBlockGlass().block();

        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    /**
     *
     */
    public void stop() {
        dialog.setVisible(false);
        dialog.dispose();
        stampBox.getBlockGlass().unblock();
    }

    /**
     *
     * @return
     */
    private JPanel createContentPane() {

        JPanel contentPane = new JPanel();
        // GUIコンポーネントを生成する
        infoLable = new JLabel(GlobalConstants.getImageIcon("about_16.gif"));
        instLabel = new JLabel("");
        instLabel.setFont(new Font("Dialog", Font.PLAIN, 10)); //GlobalVariables.getInt("waitingList.state.font.size")));
        publishedDate = new JLabel("");
        stampBoxName = new JTextField();
        stampBoxName.setColumns(15);
        //GUIFactory.createTextField(, 15, null, null, null);
        partyName = new JTextField();
        partyName.setColumns(20);
        //   partyName = GUIFactory.createTextField(new JTextField(), 20, null, null, null);
        contact = new JTextField();
        contact.setColumns(30);
        //  contact = GUIFactory.createTextField(new JTextField(), 30, null, null, null);
        description = new JTextField();
        description.setColumns(30);
        //   description = GUIFactory.createTextField(new JTextField(), 30, null, null, null);

        local = new JRadioButton(IInfoModel.PUBLISH_TREE_LOCAL);
        publc = new JRadioButton(IInfoModel.PUBLISH_TREE_PUBLIC);
        publish = new JButton("");
        publish.setEnabled(false);
        cancelPublish = new JButton("公開を止める");
        cancelPublish.setEnabled(false);
        cancel = new JButton("ダイアログを閉じる");
        entities = new JCheckBox[IInfoModel.STAMP_NAMES.length];
        for (int i = 0; i < IInfoModel.STAMP_NAMES.length; i++) {
            entities[i] = new JCheckBox(IInfoModel.STAMP_NAMES[i]);
            if (IInfoModel.STAMP_NAMES[i].equals(IInfoModel.TABNAME_ORCA)) {
                entities[i].setEnabled(false);
            }
        }

        JPanel chkPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chkPanel1.add(entities[0]);
        chkPanel1.add(entities[1]);
        chkPanel1.add(entities[2]);
        chkPanel1.add(entities[3]);
        chkPanel1.add(entities[4]);
        chkPanel1.add(entities[5]);
        chkPanel1.add(entities[6]);
        chkPanel1.add(entities[7]);
        //   JPanel chkPanel1 = GUIFactory.createCheckBoxPanel(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)), new JCheckBox[]{entities[0], entities[1], entities[2], entities[3], entities[4], entities[5], entities[6], entities[7]});

        JPanel chkPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        chkPanel2.add(entities[8]);
        chkPanel2.add(entities[9]);
        chkPanel2.add(entities[10]);
        chkPanel2.add(entities[11]);
        chkPanel2.add(entities[12]);
        chkPanel2.add(entities[13]);
        chkPanel2.add(entities[14]);
        chkPanel2.add(entities[15]);
        //   chkPanel2.add(entities[16]);
        //     JPanel chkPanel2 = GUIFactory.createCheckBoxPanel(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)), new JCheckBox[]{entities[8], entities[9], entities[10], /* entities[11],*/ entities[12], entities[13], entities[14], entities[15], entities[16]});

        String[] categories = {"医薬品", "ジェネリック医薬品", "検査", "器材", "パス", "地域連携", "調査", "治験", "院内シェア"}; //GlobalVariables.getStringArray("stamp.publish.categories");
        category = new JComboBox(categories);
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        categoryPanel.add(category);

        // 公開先RadioButtonパネル

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        radioPanel.add(local);
        radioPanel.add(publc);

        //  JPanel radioPanel = GUIFactory.createRadioPanel(new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0)), new JRadioButton[]{local, publc});

        // 属性設定パネル
        JPanel attributePanel = new JPanel(new BorderLayout());
        GridBagBuilder gbl = new GridBagBuilder(attributePanel, "スタンプ公開設定");

        int y = 0;
        gbl.add(infoLable, 0, y, GridBagConstraints.EAST);
        gbl.add(instLabel, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開スタンプセット名"), 0, y, GridBagConstraints.EAST);
        gbl.add(stampBoxName, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開先"), 0, y, GridBagConstraints.EAST);
        gbl.add(radioPanel, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("カテゴリ"), 0, y, GridBagConstraints.EAST);
        gbl.add(categoryPanel, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開するスタンプ"), 0, y, GridBagConstraints.EAST);
        gbl.add(chkPanel1, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel(" "), 0, y, GridBagConstraints.EAST);
        gbl.add(chkPanel2, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開者名"), 0, y, GridBagConstraints.EAST);
        gbl.add(partyName, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("URL等"), 0, y, GridBagConstraints.EAST);
        gbl.add(contact, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("利用者への説明"), 0, y, GridBagConstraints.EAST);
        gbl.add(description, 1, y, GridBagConstraints.WEST);

        y++;
        gbl.add(new JLabel("公開日"), 0, y, GridBagConstraints.EAST);
        gbl.add(publishedDate, 1, y, GridBagConstraints.WEST);

        // コマンドパネル
        JPanel cmdPanel = null;
        if (GlobalConstants.isMac()) {

            cmdPanel = new JPanel();
            cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.X_AXIS));
            cmdPanel.add(Box.createHorizontalGlue());
            cmdPanel.add(cancel);
            cmdPanel.add(Box.createHorizontalStrut(5));
            cmdPanel.add(cancelPublish);
            cmdPanel.add(Box.createHorizontalStrut(5));
            cmdPanel.add(publish);
            //         cmdPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{cancel, cancelPublish, publish});
        } else {
            cmdPanel = new JPanel();
            cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.X_AXIS));
            cmdPanel.add(Box.createHorizontalGlue());
            cmdPanel.add(publish);
            cmdPanel.add(Box.createHorizontalStrut(5));
            cmdPanel.add(cancelPublish);
            cmdPanel.add(Box.createHorizontalStrut(5));
            cmdPanel.add(cancel);
            //        cmdPanel = GUIFactory.createCommandButtonPanel(new JPanel(), new JButton[]{publish, cancelPublish, cancel});
        }

        // 配置する
        contentPane.setLayout(new BorderLayout(0, 17));
        contentPane.add(attributePanel, BorderLayout.CENTER);
        contentPane.add(cmdPanel, BorderLayout.SOUTH);
        contentPane.setBorder(BorderFactory.createEmptyBorder(12, 12, 11, 11));

        // PublishState に応じて振り分ける
        IStampTreeModel stmpTree = stampBox.getUserStampBox().getStampTreeModel();
        FacilityModel facility = GlobalVariables.getUserModel().getFacility();
        String facilityId = facility.getFacilityId();
        long treeId = stmpTree.getId();
        String publishTypeStr = stmpTree.getPublishType();

        if (treeId == 0L && publishTypeStr == null) {
            //
            // Stamptree非保存（最初のログイン時）
            //
            publishState = PublishedState.NONE;
        } else if (treeId != 0L && publishTypeStr == null) {
            //
            // 保存されているStamptreeで非公開のケース
            //
            publishState = PublishedState.SAVED_NONE;
        } else if (treeId != 0L && publishTypeStr != null && publishTypeStr.equals(facilityId)) {
            //
            // publishType=facilityId ローカルに公開されている
            //
            publishState = PublishedState.LOCAL;
        } else if (treeId != 0L && publishTypeStr != null && publishTypeStr.equals(IInfoModel.PUBLISHED_TYPE_GLOBAL)) {
            //
            // publishType=global グローバルに公開されている
            //
            publishState = PublishedState.GLOBAL;
        }

        // GUIコンポーネントに初期値を入力する
        switch (publishState) {

            case NONE:
                instLabel.setText("このスタンプは公開されていません。");
                partyName.setText(facility.getFacilityName());
                String url = facility.getUrl();
                if (url != null) {
                    contact.setText(url);
                }
                String dateStr = ModelUtils.getDateAsString(new Date());
                publishedDate.setText(dateStr);
                publish.setText("公開する");
                break;

            case SAVED_NONE:
                instLabel.setText("このスタンプは公開されていません。");
                partyName.setText(stmpTree.getPartyName());
                url = facility.getUrl();
                if (url != null) {
                    contact.setText(url);
                }
                dateStr = ModelUtils.getDateAsString(new Date());
                publishedDate.setText(dateStr);
                publish.setText("公開する");
                break;

            case LOCAL:
                instLabel.setText("このスタンプは院内に公開されています。");
                stampBoxName.setText(stmpTree.getName());
                local.setSelected(true);
                publc.setSelected(false);
                publishType = TT_LOCAL;

                //
                // Publish している Entity をチェックする
                //
                String published = ((StampTreeModel) stmpTree).getPublished();
                if (published != null) {
                    StringTokenizer st = new StringTokenizer(published, ",");
                    while (st.hasMoreTokens()) {
                        String entity = st.nextToken();
                        for (int i = 0; i < IInfoModel.STAMP_ENTITIES.length; i++) {
                            if (entity.equals(IInfoModel.STAMP_ENTITIES[i])) {
                                entities[i].setSelected(true);
                                break;
                            }
                        }
                    }
                }

                category.setSelectedItem(stmpTree.getCategory());
                partyName.setText(stmpTree.getPartyName());
                contact.setText(stmpTree.getUrl());
                description.setText(stmpTree.getDescription());
                StringBuilder sb = new StringBuilder();
                sb.append(ModelUtils.getDateAsString(stmpTree.getPublishedDate()));
                sb.append("  最終更新日( ");
                sb.append(ModelUtils.getDateAsString(stmpTree.getLastUpdated()));
                sb.append(" )");
                publishedDate.setText(sb.toString());
                publish.setText("更新する");
                publish.setEnabled(true);
                cancelPublish.setEnabled(true);
                break;

            case GLOBAL:
                instLabel.setText("このスタンプはグローバルに公開されています。");
                stampBoxName.setText(stmpTree.getName());
                local.setSelected(false);
                publc.setSelected(true);
                category.setSelectedItem(stmpTree.getCategory());
                partyName.setText(stmpTree.getPartyName());
                contact.setText(stmpTree.getUrl());
                description.setText(stmpTree.getDescription());
                publishType = TT_PUBLIC;

                published = ((StampTreeModel) stmpTree).getPublished();
                if (published != null) {
                    StringTokenizer st = new StringTokenizer(published, ",");
                    while (st.hasMoreTokens()) {
                        String entity = st.nextToken();
                        for (int i = 0; i < IInfoModel.STAMP_ENTITIES.length; i++) {
                            if (entity.equals(IInfoModel.STAMP_ENTITIES[i])) {
                                entities[i].setSelected(true);
                                break;
                            }
                        }
                    }
                }

                sb = new StringBuilder();
                sb.append(ModelUtils.getDateAsString(stmpTree.getPublishedDate()));
                sb.append("  最終更新日( ");
                sb.append(ModelUtils.getDateAsString(stmpTree.getLastUpdated()));
                sb.append(" )");
                publishedDate.setText(sb.toString());
                publish.setText("更新する");
                publish.setEnabled(true);
                cancelPublish.setEnabled(true);
                break;
             default: LogWriter.fatal(getClass(), "case default");
        }

        // コンポーネントのイベント接続を行う
        // Text入力をチェックする
        //   ReflectDocumentListener dl = new ReflectDocumentListener(this, "checkButton");

        DocumentListener dl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };

        stampBoxName.getDocument().addDocumentListener(dl);
        partyName.getDocument().addDocumentListener(dl);
        contact.getDocument().addDocumentListener(dl);
        description.getDocument().addDocumentListener(dl);

        // RadioButton
        ButtonGroup bg = new ButtonGroup();
        bg.add(local);
        bg.add(publc);
        PublishTypeListener pl = new PublishTypeListener();
        local.addActionListener(pl);
        publc.addActionListener(pl);

        ActionListener cbListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                checkButton();
            }
        };

        for (JCheckBox cb : entities) {
            cb.addActionListener(cbListener);
        }

        // publish & cancel
        publish.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                publish();
            }
        });

        cancelPublish.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancelPublish();
            }
        });

        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        return contentPane;
    }

    /**
     *
     */
    class PublishTypeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (local.isSelected()) {
                publishType = TT_LOCAL;
                //   category.setSelectedIndex(GlobalConstants.getInt("8"));
                category.setSelectedIndex(8);
            } else if (publc.isSelected()) {
                publishType = TT_PUBLIC;
            }
            checkButton();
        }
    }

    /**
     * スタンプを公開する。
     */
    public void publish() {

        // 公開するStampTreeを取得する
        List<StampTree> publishList = new ArrayList<StampTree>(IInfoModel.STAMP_ENTITIES.length);
        // Entity のカンマ連結用 StringBuilder 
        StringBuilder sb = new StringBuilder();
        boolean nonSelected = true;
        for (int i = 0; i < IInfoModel.STAMP_ENTITIES.length; i++) {
            if (entities[i].isSelected()) {
                nonSelected = false;
                // Entity チェックボックスがチェックされている時
                // 対応するEntity名を取得する
                String entity = IInfoModel.STAMP_ENTITIES[i];

                // StampBox からEmtityに対応するStampTreeを得る
                StampTree st = stampBox.getStampTreeFromUserBox(entity);

                // 公開リストに加える
                publishList.add(st);

                // Entity 名をカンマで連結する
                sb.append(",");
                sb.append(entity);
            }
        }

        if (nonSelected) {
            JOptionPane.showMessageDialog(dialog,
                    "公開するスタンプを選択してください",
                    GlobalConstants.getFrameTitle(title),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String published = sb.toString();
        published = published.substring(1);

        // 公開する StampTree の XML データを生成する
        DefaultStampTreeXmlBuilder builder = new DefaultStampTreeXmlBuilder();
        StampTreeXmlDirector director = new StampTreeXmlDirector(builder);
        String publishXml = director.build(publishList);
        byte[] bytes = null;
        try {
            bytes = publishXml.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LogWriter.error(getClass(), ex);
        }
        final byte[] publishBytes = bytes;

        // 公開時の自分（個人用）の StampTree と同期をとる
        // 公開時の自分（個人用）Stamptreeを保存/更新する
        List<StampTree> personalTree = stampBox.getUserStampBox().getAllTrees();
        builder = new DefaultStampTreeXmlBuilder();
        director = new StampTreeXmlDirector(builder);
        String treeXml = director.build((List<StampTree>) personalTree);

        // 個人用のStampTreeModelに公開時のXMLをセットする
        final open.dolphin.infomodel.StampTreeModel stmpTree = (open.dolphin.infomodel.StampTreeModel) stampBox.getUserStampBox().getStampTreeModel();
        stmpTree.setTreeXml(treeXml);

        // 公開情報を設定する
        stmpTree.setName(stampBoxName.getText().trim());
        String pubType = publc.isSelected() ? IInfoModel.PUBLISHED_TYPE_GLOBAL : GlobalVariables.getUserModel().getFacility().getFacilityId();
        stmpTree.setPublishType(pubType);
        stmpTree.setCategory((String) category.getSelectedItem());
        stmpTree.setPartyName(partyName.getText().trim());
        stmpTree.setUrl(contact.getText().trim());
        stmpTree.setDescription(description.getText().trim());
        stmpTree.setPublished(published);

        // 公開及び更新日を設定する
        switch (publishState) {
            case NONE:
            case SAVED_NONE:
                Date date = new Date();
                stmpTree.setPublishedDate(date);
                stmpTree.setLastUpdated(date);
                break;
            case LOCAL:
            case GLOBAL:
                stmpTree.setLastUpdated(new Date());
                break;
            default: LogWriter.fatal(getClass(), "case default");
        }

        // Delegator を生成する
        sdl = new RemoteStampDelegater();
        //   int delay = 200;
        //   int maxEstimation = 30 * 1000;

        SynchronizedTask task = new SynchronizedTask<Boolean, Void>(app) {

            @Override
            protected Boolean doInBackground() throws Exception {
                switch (publishState) {
                    case NONE:
                        // 最初のログイン時、まだ自分のStamptreeが保存されていない状態の時
                        // 自分（個人用）StampTreeModelを保存し公開する
                        long id = sdl.saveAndPublishTree(stmpTree, publishBytes);
                        stmpTree.setId(id);
                        break;
                    case SAVED_NONE:
                        // 自分用のStampTreeがあって新規に公開する場合
                        sdl.publishTree(stmpTree, publishBytes);
                        break;
                    case LOCAL:
                        // Localに公開されていて更新する場合
                        sdl.updatePublishedTree(stmpTree, publishBytes);
                        break;
                    case GLOBAL:
                        // Global に公開されていて更新する場合
                        sdl.updatePublishedTree(stmpTree, publishBytes);
                        break;
                default: LogWriter.fatal(getClass(), "case default");
                }
                return new Boolean(!sdl.isError());
            }

            @Override
            protected void succeeded(Boolean result) {
                if (result.booleanValue()) {
                    JOptionPane.showMessageDialog(dialog,
                            "スタンプを公開しました.",
                            GlobalConstants.getFrameTitle(title),
                            JOptionPane.INFORMATION_MESSAGE);
                    stop();

                } else {
                    //            LogWriter.warn(getClass(), sdl.getErrorMessage());
                    JOptionPane.showMessageDialog(dialog,
                            "スタンプの公開に失敗しました.",
                            GlobalConstants.getFrameTitle(title),
                            JOptionPane.WARNING_MESSAGE);
                }
            }

            @Override
            protected void cancelled() {
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
                //           LogWriter.warn(getClass(), cause.getMessage());
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
                //          LogWriter.warn(getClass(), e.getMessage());
            }
        };
        /*
        TaskMonitor taskMonitor = appCtx.getTaskMonitor();
        String message = "スタンプ公開";
        String note = "公開しています...";
        Component c = dialog;
        TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, note, delay, maxEstimation);
        taskMonitor.addPropertyChangeListener(w);
        appCtx.getTaskService().execute(task);
         */
        task.execute();
    }

    /**
     * 公開しているTreeを取り消す。
     */
    public void cancelPublish() {

        // 確認を行う
        JLabel msg1 = new JLabel("公開を取り消すとサブスクライブしているユーザがあなたの");
        JLabel msg2 = new JLabel("スタンプを使用できなくなります。公開を取り消しますか?");
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        p1.add(msg1);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        p2.add(msg2);
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.add(p1);
        box.add(p2);
        box.setBorder(BorderFactory.createEmptyBorder(0, 0, 11, 11));
        int option = JOptionPane.showConfirmDialog(dialog,
                new Object[]{box},
                GlobalConstants.getFrameTitle(title),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                GlobalConstants.getImageIcon("sinfo_32.gif"));
        if (option != JOptionPane.YES_OPTION) {
            return;
        }

        // StampTree を表す XML データを生成する
        List<StampTree> list = stampBox.getUserStampBox().getAllTrees();
        DefaultStampTreeXmlBuilder builder = new DefaultStampTreeXmlBuilder();
        StampTreeXmlDirector director = new StampTreeXmlDirector(builder);
        String treeXml = director.build((List<StampTree>) list);

        // 個人用のStampTreeModelにXMLをセットする
        final open.dolphin.infomodel.StampTreeModel stmpTree = (open.dolphin.infomodel.StampTreeModel) stampBox.getUserStampBox().getStampTreeModel();

        // 公開データをクリアする
        stmpTree.setTreeXml(treeXml);
        stmpTree.setPublishType(null);
        stmpTree.setPublishedDate(null);
        stmpTree.setLastUpdated(null);
        stmpTree.setCategory(null);
        stmpTree.setName("個人用"); //stmpTree.setName(GlobalVariables.getString("stampTree.personal.box.name"));
        stmpTree.setDescription("個人用のスタンプセットです"); //stmpTree.setDescription(GlobalVariables.getString("stampTree.personal.box.tooltip"));
        sdl = new RemoteStampDelegater();
        //   int delay = 200;
        //  int maxEstimation = 60 * 1000;

        SynchronizedTask task = new SynchronizedTask<Boolean, Void>(app) {//スタンプの公開取り消し

            @Override
            protected Boolean doInBackground() throws Exception {
                sdl.cancelPublishedTree(stmpTree);
                return new Boolean(!sdl.isError());
            }

            @Override
            protected void succeeded(Boolean result) {
                //        LogWriter.debug(getClass(), "Task succeeded");
                if (result.booleanValue()) {
                    JOptionPane.showMessageDialog(dialog, "公開を取り消しました。", GlobalConstants.getFrameTitle(title), JOptionPane.INFORMATION_MESSAGE);
                    //スタンプ箱の該当するタブを削除する
                    JTabbedPane tp = stampBox.getParentBox();
                    for (int i = 0; i < tp.getComponentCount(); i++) {
                        String tst = tp.getTitleAt(i);
                        String dst = stampBoxName.getText().trim();
                        if (dst.equals(tst)) {
                            tp.removeTabAt(i);
                        }
                    }
                    stop();

                } else {
                    //              LogWriter.warn(getClass(), sdl.getErrorMessage());
                    JOptionPane.showMessageDialog(dialog, "公開の取り消しに失敗しました.", GlobalConstants.getFrameTitle(title), JOptionPane.WARNING_MESSAGE);
                }
            }

            @Override
            protected void cancelled() {
                //      LogWriter.debug(getClass(), "Task cancelled");
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
                //         LogWriter.warn(getClass(), cause.getMessage());
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
                //        LogWriter.warn(getClass(), e.getMessage());
            }
        };

        //   TaskMonitor taskMonitor = appCtx.getTaskMonitor();
        //     String message = "スタンプ公開";
        //     String note = "公開を取り消しています...";
        //   Component c = dialog;
        //    TaskTimerMonitor w = new TaskTimerMonitor(task, taskMonitor, c, message, note, delay, maxEstimation);
        //   taskMonitor.addPropertyChangeListener(w);

        //     appCtx.getTaskService().execute(task);
        task.execute();

    }

    /**
     *
     */
    public void checkButton() {
        switch (publishType) {
            case TT_NONE:
                break;
            case TT_LOCAL:
                boolean stampNameOk = stampBoxName.getText().trim().equals("") ? false : true;
                boolean partyNameOk = partyName.getText().trim().equals("") ? false : true;
                boolean descriptionOk = description.getText().trim().equals("") ? false : true;
                boolean checkOk = false;
                for (JCheckBox cb : entities) {
                    if (cb.isSelected()) {
                        checkOk = true;
                        break;
                    }
                }
                boolean newOk = (stampNameOk && partyNameOk && descriptionOk && checkOk) ? true : false;
                if (newOk != okState) {
                    okState = newOk;
                    publish.setEnabled(okState);
                }
                break;
            case TT_PUBLIC:
                stampNameOk = stampBoxName.getText().trim().equals("") ? false : true;
                partyNameOk = partyName.getText().trim().equals("") ? false : true;
                boolean urlOk = contact.getText().trim().equals("") ? false : true;
                descriptionOk = description.getText().trim().equals("") ? false : true;
                checkOk = false;
                for (JCheckBox cb : entities) {
                    if (cb.isSelected()) {
                        checkOk = true;
                        break;
                    }
                }
                newOk = (stampNameOk && partyNameOk && urlOk && descriptionOk && checkOk) ? true : false;
                if (newOk != okState) {
                    okState = newOk;
                    publish.setEnabled(okState);
                }
                break;
            default: LogWriter.fatal(getClass(), "case default");
        }
    }
}
