/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * KarteEditor.java
 *
 * Created on 2010/03/05, 14:58:36
 */
package open.dolphin.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.TooManyListenersException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.exception.DolphinException;
import open.dolphin.infomodel.AccessRightModel;
import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.ExtRefModel;
import open.dolphin.infomodel.ID;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ProgressCourse;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.infomodel.BeanUtils;
import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalSettings;
import open.dolphin.project.GlobalVariables;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Set;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import open.dolphin.client.karte.template.TemplateManager;
import open.dolphin.client.karte.template.error.CantWriteTemplateException;
import open.dolphin.helper.DBTask;
import open.dolphin.helper.KarteHeader;
import open.dolphin.infomodel.BundleMed;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.sendclaim.SendClaimImpl;
import open.dolphin.sendmml.SendMmlImpl;
import open.dolphin.utils.StringTool;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import open.dolphin.infomodel.IStampInfo;
import open.dolphin.log.LogWriter;
import open.dolphin.message.ClaimHelper;
import open.dolphin.message.MMLHelper;
import open.dolphin.message.MessageBuilder;

import open.dolphin.plugin.IPlugin;
import open.dolphin.plugin.PluginWrapper;

/**
 * カルテエディタ　MEMO:画面 ChartWindow
 * @author
 */
public class KarteEditor extends javax.swing.JPanel implements IInfoModel, IChartDocument {

    /**
     *
     */
    public static final int SINGLE_MODE = 1;    // シングルモード
    /**
     *
     */
    public static final int DOUBLE_MODE = 2;    // ２号カルテモード
    private static final String DEFAULT_TITLE = "経過記録";
//    private static final String UPDATE_TAB_TITLE = "更新";
    private int mode = 2;    // このエディタのモード
    private DocumentModel model;    // このエディタのモデル
    private JLabel timeStampLabel;    // このエディタを構成するコンポーネント
    private String timeStamp;    // Timestamp
    private boolean insuranceVisible;    // 健康保険Box
    private KartePane soaPane;    // SOA Pane
    private KartePane pPane;    // P Pane
    private IKartePanel panel2;    // 2号カルテ JPanel
    // 編集可能かどうかのフラグ
    private boolean editable;    // このフラグで KartePane を初期化する
    private boolean modify;    // 修正時に true
    private SendClaimImpl claimListener;    // CLAIM 送信リスナ
    private SendMmlImpl mmlListener;    // MML送信リスナ
    private boolean sendMml;    // MML送信フラグ
    private StateMgr stateMgr;    // State Manager
    private boolean isHospital;
    //元々はAbstractChartDocumentから継承
    private static final String[] CHART_MENUS = {
        GUIConst.ACTION_OPEN_KARTE, GUIConst.ACTION_SAVE, GUIConst.ACTION_DIRECTION, GUIConst.ACTION_DELETE, GUIConst.ACTION_PRINT, GUIConst.ACTION_MODIFY_KARTE,
        GUIConst.ACTION_ASCENDING, GUIConst.ACTION_DESCENDING, GUIConst.ACTION_SHOW_MODIFIED, GUIConst.ACTION_SHOW_UNSEND, GUIConst.ACTION_SHOW_SEND, GUIConst.ACTION_SHOW_NEWEST,
        GUIConst.ACTION_INSERT_TEXT, GUIConst.ACTION_INSERT_SCHEMA, GUIConst.ACTION_INSERT_STAMP, GUIConst.ACTION_SELECT_INSURANCE,
        GUIConst.ACTION_CUT, GUIConst.ACTION_COPY, GUIConst.ACTION_PASTE, GUIConst.ACTION_UNDO, GUIConst.ACTION_REDO
    };
    private IChart parent;
    private String title;
    private boolean isOpenFrame;

    private final static boolean DO_LOGGING = false; //条件付きコンパイル
    private static final String LF = "\n";
    private static final String CRLF = "\r\n"; //WindowsのCRLF


    /** Creates new form KarteEditor
     * @param parent
     */
    public KarteEditor(IChart parent) {
        this.title = GlobalVariables.getDefaultKarteTitle();
        this.parent = parent;

        initComponents();

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(GlobalSettings.karteScrollUnitIncrement());
    }

    /**
     *
     * @return
     */
    @Override
    public IChartDocument.TYPE getType() {
        return IChartDocument.TYPE.KarteEditor;
    }

    /**
     * 経過
     * @param dumper　カルテパネルの内容を出力したインスタンス
     * @param progressInfo　スタンプ
     */
    private void setProgressModules(KartePaneDumper dumper, IStampInfo progressInfo) {

        ProgressCourse progressCourse = new ProgressCourse();
        progressCourse.setFreeText(dumper.getSpec());

        ModuleModel progressModule = new ModuleModel();
        progressModule.setModuleInfo(progressInfo);
        progressModule.setModel(progressCourse);

        model.addModule(progressModule);
    }

    /**
     * スタンプ
     * @param dumper カルテパネルの内容を出力したインスタンス
     * @return
     */
    private boolean setModules(KartePaneDumper dumper) {
        ModuleModel[] modules = dumper.getModules();
        if (modules != null && modules.length > 0) {
            model.addModule(modules);
            return true;
        }
        return false;
    }

    /**
     * シェーマ
     * @param dumper　カルテパネルの内容を出力したインスタンス
     * @return
     */
    private boolean setSchemas(KartePaneDumper dumper) {
        SchemaModel[] schemas = dumper.getSchemas();
        if (schemas != null && schemas.length > 0) {
            for (SchemaModel schema : schemas) {
                // 保存のため Icon を JPEG に変換する
                ImageIcon icon = schema.getIcon();
                schema.setJpegBytes(convertIconToJPEGBytes(icon));
                schema.setIcon(null);
                model.addSchema(schema);
            }
            return true;
        }
        return false;
    }

    /**
     * 
     * @param icon
     * @return
     */
    private byte[] convertIconToJPEGBytes(ImageIcon icon) {
        // Schema を追加する
        int maxImageWidth = 522; //GlobalVariables.getInt("image.max.width");
        int maxImageHeight = 522; //GlobalVariables.getInt("image.max.height");
        Dimension maxSImageSize = new Dimension(maxImageWidth, maxImageHeight);
        icon = adjustImageSize(icon, maxSImageSize);
        return getJPEGByte(icon.getImage());
    }

    /**
     * カルテパネルの内容を出力する
     * @param pane カルテ描画領域
     * @return　カルテパネルの内容を出力したインスタンス
     */
    private KartePaneDumper exportEditPane(KartePane pane) {
        KartePaneDumper dumper = new KartePaneDumper();
        KarteStyledDocument doc = (KarteStyledDocument) pane.getTextPane().getDocument();
        dumper.dump(doc);
        return dumper;
    }

    /**
     * P（右側） をダンプし model に modules を追加する
     * @param progressInfo
     */
    private void exportPPane(IStampInfo progressInfo) {
        // PPane をダンプし model に modules を追加する
        KartePaneDumper dumper = exportEditPane(pPane);
        setModules(dumper);
        setProgressModules(dumper, progressInfo);
    }

    /**
     * SOA(左側) をダンプし model に modules, schemas を追加する
     * @param progressInfo
     */
    private void exportSOAPane(IStampInfo progressInfo) {
        // SOAPane をダンプし model に modules, schemas を追加する
        KartePaneDumper dumper = exportEditPane(soaPane);
        setModules(dumper);
        setSchemas(dumper);
        setProgressModules(dumper, progressInfo);
    }

    /**
     * 
     */
    private void buildTimeStampLabel() {
        initTimeStamp();
        StringBuilder sb = new StringBuilder();
        if (isHospital) {
            sb.append("入院    ");
        }
        sb.append(timeStamp);
        String selecteIns = getHealthInsurance();
        if (selecteIns != null) {
            sb.append(" (");
            sb.append(selecteIns);
            sb.append(")");
        }

        timeStampLabel.setText(sb.toString());
        timeStampLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 1) {
                    setInsuranceVisible(!isInsuranceVisible());
                }
                e.consume();
            }
        });

        setInsuranceVisible(true);
    }

    /**
     * 
     * @return
     */
    private String getHealthInsurance() {
        PVTHealthInsuranceModel[] insurances = null;
        // コンテキストが EditotFrame の場合と IChart の場合がある
        if (getParentContext() instanceof ChartWindow) {
            insurances = ((ChartWindow) getParentContext()).getHealthInsurances();
        } else if (getParentContext() instanceof EditorFrame) {
            EditorFrame frame = (EditorFrame) getParentContext();
            ChartWindow chart = (ChartWindow) frame.getChart();
            insurances = chart.getHealthInsurances();
        }
        String insGUID = getModel().getDocInfo().getHealthInsuranceGUID();

        if (insGUID == null) {
            return null;
        }

        // Model に設定してある健康保険を選択する

        for (PVTHealthInsuranceModel insurance : insurances) {
            if (insurance.getGUID().equals(insGUID)) {
                return insurance.toString();
            }
        }

        return null;
    }

    /**
     * 文書タイトルを取得する
     * @return　文書タイトル
     */
    private String getKarteTitle() {
        // Title が設定されているか
        String text = model.getDocInfo().getTitle();
        if (StringTool.isEmptyString(text)) {
            if (GlobalVariables.getPreferences().getBoolean("useTop15AsTitle", true)) {
                // SOAPane から最初の１５文字を文書タイトルとして取得する
                text = soaPane.getTitle();
            } else {
                text = GlobalVariables.getDefaultKarteTitle();
            }
            if (StringTool.isEmptyString(text)) {
                text = DEFAULT_TITLE;
            }
        }
        return text;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        kartePanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        kartePanel.setName("kartePanel"); // NOI18N
        kartePanel.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setViewportView(kartePanel);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel kartePanel;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @param isOpenFrame
     */
    public void setOpenFrame(boolean isOpenFrame) {
        this.isOpenFrame = isOpenFrame;
    }

    /**
     * SINGLE か DOUBLE
     *
     * @return
     */
    public int getMode() {
        return mode;
    }

    /**
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * DocumentModelを返す。
     * @return DocumentModel
     */
    private DocumentModel getModel() {
        return model;
    }

    /**
     * DocumentModelを設定する。
     * @param model DocumentModel
     */
    public void setModel(DocumentModel model) {
        this.model = model;
    }

    /**
     * 入院ならTrue、でなければFalse。 Claim送信時に
     * @param isHospital
     */
    public void setIsHospital(boolean isHospital) {
        this.isHospital = isHospital;
    }

    /**
     *
     * @return
     */
    private int getActualHeight() {
        try {
            JTextPane pane = soaPane.getTextPane();
            int pos = pane.getDocument().getLength();
            Rectangle r = pane.modelToView(pos);
            int hsoa = r.y;

            if (pPane == null) {
                return hsoa;
            }

            pane = pPane.getTextPane();
            pos = pane.getDocument().getLength();
            r = pane.modelToView(pos);
            int hp = r.y;

            return Math.max(hsoa, hp);

        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return 0;
    }

    /**
     *
     * @param format
     */
    public void printPanel2(final PageFormat format) {
        String name = getParentContext().getPatient().getFullName();
        ((PrintablePanel) panel2).printPanel(format, 1, false, name, getActualHeight() + 30);
    }

    /**
     *
     * @param format
     * @param copies
     * @param useDialog
     */
    private void printPanel2(final PageFormat format, final int copies, final boolean useDialog) {
        String name = getParentContext().getPatient().getFullName();
        ((PrintablePanel) panel2).printPanel(format, copies, useDialog, name, getActualHeight() + 30);
    }

    /**
     * 
     */
    private void initTimeStamp() {
        Date now = new Date();//MEMO: unused?
        if (model.getConfirmDate() != null) {
            try {
                now = model.getConfirmed();
            } catch (Exception e) {
                   LogWriter.error(getClass(), e);
            }
        }

        if (modify) {
            KarteHeader header = new KarteHeader(model, "更新");
            timeStamp = header.toString();
        } else {
            KarteHeader header = new KarteHeader(model, "新規");
            timeStamp = header.toString();
        }
    }

    /**
     * 
     * @return
     */
    private AccessRightModel setCreatorAccessRight() {
        AccessRightModel ar = new AccessRightModel();
        ar.setPermission(PERMISSION_ALL);
        ar.setLicenseeCode(ACCES_RIGHT_CREATOR);
        ar.setLicenseeName(ACCES_RIGHT_CREATOR_DISP);
        ar.setLicenseeCodeType(ACCES_RIGHT_FACILITY_CODE);
        return ar;
    }

    /**
     * 
     * @return
     */
    private AccessRightModel setExperienceAccessRight() {
        AccessRightModel ar = new AccessRightModel();
        ar.setPermission(PERMISSION_READ);
        ar.setLicenseeCode(ACCES_RIGHT_EXPERIENCE);
        ar.setLicenseeName(ACCES_RIGHT_EXPERIENCE_DISP);
        ar.setLicenseeCodeType(ACCES_RIGHT_EXPERIENCE_CODE);
        return ar;
    }

    /**
     * 
     * @return
     */
    private AccessRightModel setPatientAccessRight() {
        AccessRightModel ar = new AccessRightModel();
        ar.setPermission(PERMISSION_READ);
        ar.setLicenseeCode(ACCES_RIGHT_PATIENT);
        ar.setLicenseeName(ACCES_RIGHT_PATIENT_DISP);
        ar.setLicenseeCodeType(ACCES_RIGHT_PERSON_CODE);
        return ar;
    }

    /**
     * 
     * @param bean
     */
    private void toHankakuNumFor(ModuleModel bean) {
        // 全角を半角文字に変換する
        // FIXME: really to need this?
        ClaimBundle bundle = null;

        if (bean.getModel() instanceof BundleMed) {
            bundle = (BundleMed) bean.getModel();
        } else if (bean.getModel() instanceof ClaimBundle) {
            bundle = (ClaimBundle) bean.getModel();
        }

        if (bundle != null) {
            ClaimItem[] items = bundle.getClaimItem();
            if (items != null && items.length > 0) {
                for (ClaimItem item : items) {
                    String num = item.getNumber();
                    if (num != null) {
                        item.setNumber(num);
                    }
                }
            }
            String bNum = bundle.getBundleNumber();
            if (bNum != null) {
                bundle.setBundleNumber(bNum);
            }
        }
    }

    /**
     *
     * @return {@code true}
     */
    private boolean print() {
        PageFormat pageFormat = getParentContext().getContext().getPageFormat();
        this.printPanel2(pageFormat);
        return true;
    }

    /**
     * 
     * @return {@code true}
     */
    private boolean direction() {
        DirectionDialog direction = new DirectionDialog(null, true, getActualModel());
        direction.setVisible(true);
        return true;
    }

    /**
     * SOAPaneを返す。
     * @return SOAPane
     */
    protected KartePane getSOAPane() {
        return soaPane;
    }

    /**
     * PPaneを返す。
     * @return PPane
     */
    protected KartePane getPPane() {
        return pPane;
    }

    /**
     * 編集可能属性を設定する。
     * @param newState 編集可能な時true
     */
    public void setEditable(boolean newState) {
        editable = newState;
    }

    /**
     * MMLリスナを追加する。
     * @param listener MMLリスナリスナ
     * @throws TooManyListenersException
     */
    public void addMMLListner(SendMmlImpl listener) throws TooManyListenersException {
        if (mmlListener != null) {
            throw new TooManyListenersException();
        }
        mmlListener = listener;
    }

    /**
     * CLAIMリスナを追加する。
     * @param listener CLAIMリスナ
     * @throws TooManyListenersException
     */
    public void addCLAIMListner(SendClaimImpl listener) throws TooManyListenersException {
        if (claimListener != null) {
            throw new TooManyListenersException();
        }
        claimListener = listener;
    }

    /**
     * CLAIMリスナを削除する。
     * @param listener 削除するCLAIMリスナ
     */
    public void removeCLAIMListener(SendClaimImpl listener) {
        if (claimListener != null && claimListener == listener) {
            claimListener = null;
        }
    }

    /**
     * 修正属性を設定する。
     * @param newState 修正する時true
     */
    protected void setModify(boolean newState) {
        modify = newState;
    }

    /**
     *
     */
    @Override
    public void enter() {
        getParentContext().getStatusPanel().setMessage("");
        getParentContext().getChartMediator().setAccepter(this);
        disableMenus();
        //       enabledAction(GUIConst.ACTION_NEW_KARTE, true);
        enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
        enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        stateMgr.controlMenu();
    }

    /**
     *
     * @param dirty
     */
    @Override
    public void setDirty(boolean dirty) {
        if (getMode() == SINGLE_MODE) {
            stateMgr.setDirty(soaPane.isDirty());
        } else {
            stateMgr.setDirty(soaPane.isDirty() || pPane.isDirty());
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean itLayoutSaved() {
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isDirty() {
        return stateMgr.isDirty();
    }

    /**
     * 初期化する。
     */
    public void initialize() {
        stateMgr = new StateMgr();
        switch (getMode()) {
            case SINGLE_MODE:
                initializeSingleMode();
                break;
            case DOUBLE_MODE:
                initializeDoubleMode();
                break;
            default:
        }
    }

    /**
     * 
     * @param textPane
     * @param role
     * @return
     */
    private KartePane createKartePane(JTextPane textPane, String role) {
        KartePane pane = new KartePane(this);
        pane.setTextPane(textPane);
        pane.setRole(role);
        return pane;
    }

    /**
     * SOA Pane を生成してイベントハンドラを関連付ける
     * @param textPane
     */
    private void createSOAPane(JTextPane textPane) {
        if (textPane != null) {
            // SOA Pane を生成してイベントハンドラを関連付ける
            soaPane = createKartePane(textPane, ROLE_SOA);
            soaPane.getTextPane().setTransferHandler(new SOATransferHandler(soaPane));
            if (model != null) {
                // Schema 画像にファイル名を付けるのために必要
                String docId = model.getDocInfo().getDocId();
                soaPane.setDocId(docId);
            }
        } else {
            LogWriter.error(getClass(), "createSOAPane(JTextPane textPane) textPane is null");
        }
    }

    /**
     * P Pane を生成してイベントハンドラを関連付ける
     * @param textPane
     */
    private void createPPane(JTextPane textPane) {
        if (textPane != null) {
            // P Pane を生成してイベントハンドラを関連付ける
            pPane = createKartePane(textPane, ROLE_P);
            pPane.getTextPane().setTransferHandler(new KartePaneTransferHandler(parent, pPane));
        } else {
            LogWriter.error(getClass(), "createPPane(JTextPane textPane) textPane is null");
        }
    }

    /**
     * 
     * @param panel
     */
    private void initializePanel(IKartePanel panel) {
        timeStampLabel = panel.getTimeStampLabel();      // TimeStampLabel を生成する
        createSOAPane(panel.getSoaTextPane());      // SOA Pane を生成する
        createPPane(panel.getPTextPane());     // P Pane を生成する Singleモードの場合は存在しない
        kartePanel.add((PrintablePanel) panel, BorderLayout.CENTER);
        displayModel(); // 初期化の前にモデルがセットしてある。  // Model を表示する
    }

    /**
     * シングルモードで初期化する。
     */
    private void initializeSingleMode() {
        panel2 = new KartePanelSingle(isHospital);
        initializePanel((IKartePanel) panel2);
    }

    /**
     * 2号カルテモードで初期化する。
     */
    private void initializeDoubleMode() {
        panel2 = new KartePanelDouble(isHospital);
        initializePanel((IKartePanel) panel2);
    }

    /**
     *
     */
    @Override
    public void start() {
        ChartMediator mediator = getParentContext().getChartMediator();
        switch (getMode()) {
            case SINGLE_MODE:
                startSingleMode(mediator);
                break;
            case DOUBLE_MODE:
                startDoubleMode(mediator);
                break;
            default:LogWriter.error(getClass(), "case default");
        }
    }

    /**
     *
     */
    @Override
    public void stop() {
    }

    /**
     * 
     * @param docInfo
     * @param params
     */
    private void setAccessRight(final DocInfoModel docInfo, final SaveParams params) {
        docInfo.addAccessRight(setCreatorAccessRight());
        // 患者のアクセス権を設定をする
        if (params != null && params.isAllowPatientRef()) {
            docInfo.addAccessRight(setPatientAccessRight());
        }
        // 診療履歴のある施設のアクセス権を設定をする
        if (params != null && params.isAllowClinicRef()) {
            docInfo.addAccessRight(setExperienceAccessRight());
        }
    }

    /**
     * 
     * @param karte
     * @param docInfo
     * @return
     */
    private int setModuleInfos(KarteBean karte, final DocInfoModel docInfo) {
        // Moduleとの関係を設定する
        Set<ModuleModel> moduleBeans = model.getModules();

        int number = 0;
        int totalSize = 0;
        for (ModuleModel bean : moduleBeans) {
            bean.setId(0L); // unsaved-value
            bean.setKarte(karte); // Karte
            bean.setCreator(GlobalVariables.getUserModel()); // 記録者
            bean.setDocument(model); // Document
            bean.setConfirmed(docInfo.getConfirmDate()); // 確定日
            bean.setFirstConfirmed(docInfo.getFirstConfirmDate()); // 適合開始日
            bean.setRecorded(docInfo.getConfirmDate()); // 記録日
            bean.setStatus(STATUS_FINAL); // status
            toHankakuNumFor(bean);
            bean.setBeanBytes(BeanUtils.getXMLBytes(bean.getModel()));

            // ModuleInfo を設定する
            // Name, Role, Entity は設定されている
            IStampInfo mInfo = bean.getModuleInfo();
            mInfo.setStampNumber(number++);
            int size = bean.getBeanBytes().length / 1024;
            totalSize += size;
        }
        return totalSize;
    }

    /**
     * 画像との関係を設定する
     * @param karte
     * @param docInfo
     * @return
     */
    private int setSchemaInfos(KarteBean karte, final DocInfoModel docInfo) {
        int number = 0;
        int totalSize = 0;

        Set<SchemaModel> imagesimages = model.getSchemas();

        if (imagesimages != null && imagesimages.size() > 0) {
            for (SchemaModel bean : imagesimages) {
                bean.setId(0L);                                        // unsaved
                bean.setKarte(karte);                                  // Karte
                bean.setCreator(GlobalVariables.getUserModel());       // Creator
                bean.setDocument(model);                               // Document
                bean.setConfirmed(docInfo.getConfirmDate());           // 確定日
                bean.setFirstConfirmed(docInfo.getFirstConfirmDate()); // 適合開始日
                bean.setRecorded(docInfo.getConfirmDate());            // 記録日
                bean.setStatus(STATUS_FINAL);                          // Status
                bean.setImageNumber(number);

                ExtRefModel ref = bean.getExtRef();
                StringBuilder sb = new StringBuilder();
                sb.append(model.getDocInfo().getDocId());
                sb.append("-");
                sb.append(number);
                sb.append(".jpg");
                ref.setHref(sb.toString());
                int size = bean.getJpegBytes().length / 1024;
                totalSize += size;
                number++;
            }
        }

        return totalSize;
    }

    /**
     *
     * @param docInfo
     */
    private void setupEJBModels(final DocInfoModel docInfo) {
        // EJB3.0 Model の関係を構築する
        // confirmed, firstConfirmed は設定済み
        KarteBean karte = getParentContext().getKarte();
        model.setKarte(karte); // karte
        model.setCreator(GlobalVariables.getUserModel()); // 記録者
        model.setRecorded(docInfo.getConfirmDate()); // 記録日
        setModuleInfos(karte, docInfo);   // モジュールとの関係を設定する
        setSchemaInfos(karte, docInfo);   // シェーマとの関係を設定する
    }

    /**
     * シングルモードを開始する。初期化の後コールされる。
     * @param mediator
     */
    private void startSingleMode(ChartMediator mediator) {
        // モデル表示後にリスナ等を設定する
        soaPane.init(editable, mediator);
        enter();
    }

    /**
     * ２号カルテモードを開始する。初期化の後コールされる。
     * @param mediator
     */
    private void startDoubleMode(ChartMediator mediator) {
        // モデル表示後にリスナ等を設定する
        soaPane.init(editable, mediator);
        pPane.init(editable, mediator);
        enter();
    }

    /**
     * DocumentModelを表示する。
     */
    private void displayModel() {
        buildTimeStampLabel();
        // 内容を表示する
        if (model.getModules() != null) {
            KarteRenderer renderer = new KarteRenderer(soaPane, pPane);
            renderer.render(model);
        }
    }

    /**
     *
     * @param newState
     */
    private void setInsuranceVisible(boolean newState) {
        if (insuranceVisible != newState) {
            insuranceVisible = newState;
            StringBuilder sb = new StringBuilder();
            sb.append(timeStamp);
            if (newState) {
                sb.append(" (");
                sb.append(getModel().getDocInfo().getHealthInsuranceDesc());
                sb.append(")");
            }
            timeStampLabel.setText(sb.toString());
            timeStampLabel.revalidate();
        }
    }

    /**
     *
     * @return
     */
    private boolean isInsuranceVisible() {
        return insuranceVisible;
    }

    /**
     * カルテのコマンドへの対応処理
     * @param command　カルテのコマンド
     * @return コマンドが正常に実行された場合は、{@code true}
     */
    @Override
    public boolean dispatchChartCommand(ChartCommand command) {
        boolean blRet = true;

        switch (command) {
            case save:          //保存
                blRet = writeShokenToFile();
                if (blRet == false) {
                    break;
                }
                
                blRet = save();
                break;
            case direction:     //指示箋
                blRet = direction();
                break;
            case print:         //印刷
                blRet = print();
                break;
            default:
                blRet = false;
        }
        
        return blRet;
    }

    /**
     *
     * @param params 「ドキュメント保存」のパラメーターのインスタンス
     */
    private void setSaveMode(SaveParams params) {
        switch (GlobalVariables.getSaveKarteMode()) {
            case 0:
                if (GlobalVariables.getSendClaim()) {
                    params.setStatus(IInfoModel.STATUS_FINAL);
                } else {
                    params.setStatus(IInfoModel.STATUS_MODIFIED);
                }
                break;
            case 1:
                params.setStatus(IInfoModel.STATUS_TMP);
                break;
            default:LogWriter.error(getClass(), "case default");
        }
        // 0=save, 1=saveTmp
    }

    /**
     *
     * @return　「ドキュメント保存」のパラメーターのインスタンス
     */
    private SaveParams createDefaultSaveParams() {

        // 地域連携に参加する場合のみに変更する
        SaveParams params = new SaveParams(GlobalVariables.getJoinAreaNetwork());
        params.setTitle(getKarteTitle());
        params.setDepartment(model.getDocInfo().getDepartmentDesc());
        params.setPrintCount(GlobalVariables.getPrintKarteCount()); // 印刷枚数をPreferenceから取得する
        params.setDisableSendClaim(getMode() == SINGLE_MODE); // CLAIM 送信 に関する設定をする
        params.setSendClaim(GlobalVariables.getSendClaim());
        params.setAllowPatientRef(false); // 患者の参照
        params.setAllowClinicRef(false); // 診療履歴のある医療機関
        setSaveMode(params);

        return params;
    }

    /**
     * 「ドキュメント保存」ダイアログを表示し、保存時のパラメータを取得する。
     * sendMML MML送信フラグ 送信するとき true
     * @return 「ドキュメント保存」のパラメーターのインスタンス
     */
    private SaveParams getSaveParams() {

        SaveParams params = createDefaultSaveParams();//「ドキュメント保存」のパラメーター
        // MML送信用のマスタIDを取得する
        // ケース１ HANIWA 方式 facilityID + patientID
        // ケース２ HIGO 方式 地域ID を使用
        ID masterID = GlobalVariables.getMasterId(getParentContext().getPatient().getPatientId());
        sendMml = (GlobalVariables.getSendMML() && masterID != null && mmlListener != null);

        // 「ドキュメント保存」ダイアログを表示し、アクセス権等の保存時のパラメータを取得する
        if (GlobalVariables.getConfirmAtSave()) { //「ドキュメント保存」ダイアログを表示するか？
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            SaveDialog dialog = new SaveDialog(parentWindow, params);
            params = dialog.open();

            if (params != null) {
                GlobalVariables.setPrintKarteCount(params.getPrintCount());//印刷部数
            }
        }
        return params;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean prepare() {
        return true;
    }

    /**
     * カルテの保存<br>
     * カルテに何も書かれていない時は、そのままリターンする
     * @return 常に{@code true}
     */
    public boolean save() {
        // 何も書かれていない時はリターンする
        if (stateMgr.isDirty()) {
            try {
                // キャンセルの時は null が返る
                SaveParams params = getSaveParams();
                if (params != null) {
                    switch (getMode()) {
                        case SINGLE_MODE:
                            saveSingleMode(params);
                            break;
                        case DOUBLE_MODE:
                            saveDoubleMode(params);
                            break;
                        default:LogWriter.error(getClass(), "case default");
                    }
                }
            } catch (DolphinException e) {
                 LogWriter.error(getClass(), e);
            }
        }
        return true;
    }

   /**
    * SOAパネルよりドキュメントを取得し、RS_Base プラグインを使って所見をファイルに書き出す。
    * @return 正常に書き出された場合は{@code true}
    */
    public boolean writeShokenToFile() {
        boolean blRet = true;

        // RS_Base プラグインの存在確認
        for (IPlugin plugin : parent.getPlugins().values()) {
            PluginWrapper pluginWrapper = new PluginWrapper(plugin);
            if (pluginWrapper.getName().equals(IPlugin.RS_BASE_PLUGIN_NAME)) {
                if (GlobalVariables.getOutputShoken() ==  false) {
                    JOptionPane.showMessageDialog(null, "[所見を出力する]にチェックが入っていません。");
                    blRet = false;
                    break;
                }

                KarteStyledDocument doc = (KarteStyledDocument)soaPane.getTextPane().getDocument();
                Element element = (Element)doc.getDefaultRootElement();
                String text = getContent(element); //コンテンツの取得
                blRet = pluginWrapper.writeShokenFile(parent, text);
                break;
            }
        }

        return blRet;
    }

    /**
     * コンテンツを取得しテキストを返す。
     * @param element 要素
     * @return テキスト
     */
    private String getContent(javax.swing.text.Element element) {
         // 要素の開始及び終了のオフセット値を保存する
        int start = element.getStartOffset();
        int end = element.getEndOffset();
        int len = end - start;
        String text = "";

        // content要素の場合はテキストを抽出する
        if (element.getName().equals("content")) {
            try {
                text = element.getDocument().getText(start, len);
                text = text.replaceAll(LF, CRLF);
            } catch (BadLocationException ex) {
                Logger.getLogger(KarteEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // 子要素について再帰する
        int childreSize = element.getElementCount();
        for (int i = 0; i < childreSize; i++) {
            text = text + getContent(element.getElement(i));
        }
        return text;
    }

    /**
     *
     * @return
     */
    public DocumentModel getActualModel() {
        return model;
        /*     DocumentModel result = null;
        try {
        switch (getMode()) {
        case SINGLE_MODE:
        result = getActualModelAtSingleMode(null);
        case DOUBLE_MODE:
        result = getActualModelAtDoubleMode(null);
        default:
        ;
        }
        } catch (DolphinException e) {
        }
        return result;*/
    }

    /*
     * ウィンドウまたはタブを閉じる。新規ボタンをEnableする。
     * */
    private void closeEditor() {
        if (isOpenFrame) {
            JFrame frame = (JFrame) this.getRootPane().getParent();
            frame.setVisible(false);
            frame.dispose();
        } else {
            getParentContext().closeChartDocument(title, this);
        }
        if (canNewEditor()) {
            enabledAction(GUIConst.ACTION_NEW_DOCUMENT, true);
            enabledAction(GUIConst.ACTION_DIRECTION, false);
        }
        getParentContext().getChartMediator().unsetAccepter(this);
    }

    /**
     *
     * @return
     */
    private boolean canNewEditor() {
        IChart chart = getParentContext();
        if (chart instanceof EditorFrame) {
            return !((ChartWindow) ((EditorFrame) getParentContext()).getChart()).existEditorPane() || getMode() == DOUBLE_MODE;
        }
        return !((ChartWindow) getParentContext()).existEditorPane() || getMode() == DOUBLE_MODE;
    }

    /**
     *
     * @param params
     * @return
     * @throws DolphinException
     */
    private DocumentModel getActualModelAtSingleMode(final SaveParams params) throws DolphinException {
        // DocInfoに値を設定する
        final DocInfoModel docInfo = model.getDocInfo();
        // 現在時刻を ConfirmDate にする
        docInfo.setConfirmDate(new Date());

        // 修正でない場合は FirstConfirmDate = ConfirmDate にする
        // 修正の場合は FirstConfirmDate は既に設定されている
        // 修正でない新規カルテは parentId = null である
        if (docInfo.getParentId() == null) {
            docInfo.setFirstConfirmDate(new Date());            // プレイン文書のため。
        }
        if (params != null) {        // titleを設定する
            docInfo.setTitle(params.getTitle());
        }

        // アクセス権限の設定をする
        setAccessRight(docInfo, params);
        IStampInfo[] progressInfo = model.getModuleInfo(MODULE_PROGRESS_COURSE);
        // 存在しない場合は新規に作成する
        if (progressInfo == null) {
            progressInfo = new ModuleInfoBean[1];
            ModuleInfoBean mi = new ModuleInfoBean();
            mi.initialize(null, MODULE_PROGRESS_COURSE, null, MODULE_PROGRESS_COURSE, ROLE_SOA_SPEC);
            progressInfo[0] = mi;
        }

        // モデルのモジュールをヌルに設定する
        // エディタの画面をダンプして生成したモジュールを設定する
        model.clearModules();
        model.clearSchema();
        exportSOAPane(progressInfo[0]);        // SOAPane をダンプし model に追加する
        // FLAGを設定する
        // image があるかどうか
        docInfo.setHasImage(model.getSchemas() != null);
        // EJB3.0 Model の関係を構築する
        // confirmed, firstConfirmed は設定済み
        setupEJBModels(docInfo);
        return model;
    }

    /**
     * シングルモードの保存を行う。
     **/
    private void saveSingleMode(final SaveParams params) throws DolphinException {
        final DocumentModel saveModel = getActualModelAtSingleMode(params);
        final DocInfoModel docInfo = saveModel.getDocInfo();
        final IChart chart = parent;
        final boolean admitFlag = isHospital;//MEMO: unused?

        if (params.getStatus().equals(IInfoModel.STATUS_TEMPLATE)) {
            saveAsTemplateSingle(saveModel);
            return;
        }

        DBTask task = new DBTask<String>(chart) {

            final RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();

            @Override
            protected String doInBackground() throws Exception {
                ddl.putDocument(saveModel);
                if (!ddl.isError()) {
                    return "";
                }
                return ddl.getErrorMessage();
            }

            @Override
            protected void succeeded(String errMsg) {
                if (!ddl.isError()) {
                    // 印刷
                    int copies = params.getPrintCount();
                    if (copies > 0) {
                        printPanel2(chart.getContext().getPageFormat(), copies, false);
                    }

                    // 編集不可に設定する
                    soaPane.setEditableProp(false);

                    // 状態遷移する
                    stateMgr.beSaved();

                    // IChart の状態を設定する
                    if (docInfo.getStatus().equals(STATUS_TMP)) {
                        chart.setChartState(IChart.state.OPEN_NONE);
                    } else if (docInfo.getStatus().equals(STATUS_FINAL)) {
                        chart.setChartState(IChart.state.OPEN_SAVE);
                    }
                    // 文書履歴の更新を通知する
                    chart.getDocumentHistory().getDocumentHistory();
                    closeEditor();
                } else {
                    // errMsg を処理する
                    // エラーを表示する
                    JOptionPane.showMessageDialog(chart.getFrame(), errMsg, GlobalConstants.getFrameTitle("カルテ保存"), JOptionPane.WARNING_MESSAGE);
                }
            }
        };
        task.execute();
    }

    /**
     *
     * @param params 「ドキュメント保存」のパラメーターのインスタンス
     * @return カルテコンテンツ
     * @throws DolphinException
     */
    private DocumentModel getActualModelAtDoubleMode(final SaveParams params) throws DolphinException {
        // DocInfoに値を設定する
        final DocInfoModel docInfo = model.getDocInfo();

        // 現在時刻を ConfirmDate にする
        docInfo.setConfirmDate(new Date()); //確定日を設定する。

        // 修正でない場合は FirstConfirmDate = ConfirmDate にする
        // 修正の場合は FirstConfirmDate は既に設定されている
        // 修正でない新規カルテは parentId = null である

        if (DO_LOGGING) {
            LogWriter.info("docInfo.getParentId()=", docInfo.getParentId());
        }

        if (docInfo.getParentId() == null) {
            docInfo.setFirstConfirmDate(model.getConfirmed());
        }
        // titleを設定する
        if (params != null) {

            if (DO_LOGGING) {
                LogWriter.info("params.getTitle()=", params.getTitle());
            }
            
            docInfo.setTitle(params.getTitle());
        }
        // アクセス権限の設定をする
        setAccessRight(docInfo, params);

        // ProgressCourseModule の ModuleInfo を保存しておく
        IStampInfo soaProgressInfo = null; //SOA
        IStampInfo pProgressInfo = null;   //P
        IStampInfo[] progressInfos = model.getModuleInfo(MODULE_PROGRESS_COURSE);

        if (progressInfos == null) {
            // 存在しない場合は新規に作成する
            soaProgressInfo = new ModuleInfoBean();
            soaProgressInfo.initialize(null, MODULE_PROGRESS_COURSE, null, MODULE_PROGRESS_COURSE, ROLE_SOA_SPEC);
            pProgressInfo = new ModuleInfoBean();
            pProgressInfo.initialize(null, MODULE_PROGRESS_COURSE, null, MODULE_PROGRESS_COURSE, ROLE_P_SPEC);
        } else {
            if (progressInfos[0].getStampRole().equals(ROLE_SOA_SPEC)) {
                soaProgressInfo = progressInfos[0];
                pProgressInfo = progressInfos[1];

            } else if (progressInfos[1].getStampRole().equals(ROLE_SOA_SPEC)) {
                pProgressInfo = progressInfos[0];
                soaProgressInfo = progressInfos[1];
            }
        }

        // モデルのモジュールをヌルに設定する
        // エディタの画面をダンプして生成したモジュールを設定する
        model.clearModules();
        model.clearSchema();

        exportSOAPane(soaProgressInfo);       // SOAPane をダンプし model に追加する
        exportPPane(pProgressInfo);        // PPane をダンプし model に追加する

        // FLAGを設定する
        docInfo.setHasImage(model.getSchemas() != null);        // image があるかどうか
        docInfo.setHasRp(model.getModule(ENTITY_MED_ORDER) != null);        // RP があるかどうか
        docInfo.setHasTreatment(model.getModule(ENTITY_TREATMENT) != null);        // 処置があるかどうか
        docInfo.setHasLaboTest(model.getModule(ENTITY_LABO_TEST) != null);        // LaboTest があるかどうか

        // EJB3.0 Model の関係を構築する
        // confirmed, firstConfirmed は設定済み
        setupEJBModels(docInfo);
        return model;
    }

    /**
     * ２号カルテモードの保存を行う
     * @param params 「ドキュメント保存」のパラメーターのインスタンス
     * @throws DolphinException
     */
    private void saveDoubleMode(final SaveParams params) throws DolphinException {

        final DocumentModel saveModel = getActualModelAtDoubleMode(params);

        //　テンプレートの保存

        if (DO_LOGGING) {
            LogWriter.info("params.getStatus()=", params.getStatus());
        }

        if (params.getStatus().equals(IInfoModel.STATUS_TEMPLATE)) {
            saveAsTemplateDouble(saveModel, params);
            return;
        }

        // タスク
        final DocInfoModel docInfo = saveModel.getDocInfo();
        docInfo.setStatus(params.getStatus());

        final IChart chart = parent;
        final boolean admitFlag = isHospital;

        DBTask task = new DBTask<String>(chart) {

            final RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
            Cursor currentCursor;

            @Override
            protected String doInBackground() throws Exception {
                currentCursor = getCursor();
                setCursor(new Cursor(Cursor.WAIT_CURSOR));//待ち状態をカーソルに設定します。

                //ddl.putDocument(saveModel); //カルテコンテンツの保存

                long lngRet = ddl.putDocument(saveModel); //カルテコンテンツの保存

                if (DO_LOGGING) {
                    LogWriter.info("保存したカルテコンテンツの数=", Long.toString(lngRet));
                    LogWriter.info("ddl.getErrorMessage()=", ddl.getErrorMessage());
                }

                if (!ddl.isError()) {
                    if (params.isSendClaim()) {
                        sendClaim(saveModel, admitFlag);
                    }
                    if (sendMml) {
                        sendMml(saveModel);
                    }
                    return "";
                }
                return ddl.getErrorMessage();
            }

            @Override
            protected void succeeded(String errMsg) {
                if (!ddl.isError()) {
                    // 印刷
                    int copies = params.getPrintCount();
                    if (copies > 0) {
                        printPanel2(chart.getContext().getPageFormat(), copies, false);
                    }

                    // 編集不可に設定する
                    soaPane.setEditableProp(false);
                    pPane.setEditableProp(false);

                    // 状態遷移する
                    stateMgr.beSaved();

                    // IChart の状態を設定する
                    if (docInfo.getStatus().equals(STATUS_TMP)) {
                        chart.setChartState(IChart.state.OPEN_NONE);
                    } else if (docInfo.getStatus().equals(STATUS_FINAL)) {
                        chart.setChartState(IChart.state.OPEN_SAVE);
                    }
                    // 文書履歴の更新を通知する
                    chart.getDocumentHistory().getDocumentHistory();
                    closeEditor();
                } else {
                    // errMsg を処理する
                    // エラーを表示する
                    JOptionPane.showMessageDialog(chart.getFrame(), errMsg, GlobalConstants.getFrameTitle("カルテ保存"), JOptionPane.WARNING_MESSAGE);
                }
                setCursor(currentCursor);
            }
        };
        task.execute();
    }

    /**
     *
     * @param saveModel
     */
    private void saveAsTemplateSingle(DocumentModel saveModel) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * テンプレートの保存
     * @param saveModel
     * @param params　「ドキュメント保存」のパラメーターのインスタンス
     */
    private void saveAsTemplateDouble(DocumentModel saveModel, SaveParams params) {

        File localStorage = GlobalConstants.getApplicationContext().getLocalStorage().getDirectory();
        File directory = new File(localStorage, "templates");
        TemplateManager manager = new TemplateManager(directory);
        try {
            manager.createTemplate(saveModel);
        } catch (CantWriteTemplateException ex) {
            JOptionPane.showMessageDialog(getParentContext().getFrame(), "テンプレートの保存に失敗しました", GlobalConstants.getFrameTitle("カルテ保存"), JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 印刷
        int copies = params.getPrintCount();
        if (copies > 0) {
            printPanel2(getParentContext().getContext().getPageFormat(), copies, false);
        }

        // 編集不可に設定する
        soaPane.setEditableProp(false);
        pPane.setEditableProp(false);

        // 状態遷移する
        stateMgr.beSaved();

        // IChart の状態を設定する
        getParentContext().setChartState(IChart.state.OPEN_NONE);
        // 文書履歴の更新を通知する
        //getParentContext().getDocumentHistory();
        closeEditor();
    }

    /**
     * Courtesy of Junzo SATO
     * @param image
     * @return
     */
    private byte[] getJPEGByte(Image image) {

        byte[] ret = null;
        BufferedOutputStream writer = null;

        try {
            Dimension d = new Dimension(image.getWidth(this), image.getHeight(this));
            BufferedImage bf = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);
            Graphics g = bf.getGraphics();
            g.setColor(Color.white);
            g.drawImage(image, 0, 0, d.width, d.height, this);

            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            writer = new BufferedOutputStream(bo);
            ImageIO.write(bf, "jpg", writer);
            writer.flush();
            writer.close();
            ret = bo.toByteArray();

        } catch (IOException e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e2) {
                      LogWriter.error(getClass(), e2);
                }
            }
        }
        return ret;
    }

    /**
     *
     * @param icon
     * @param dim
     * @return
     */
    private ImageIcon adjustImageSize(ImageIcon icon, Dimension dim) {

        if ((icon.getIconHeight() > dim.height) || (icon.getIconWidth() > dim.width)) {
            Image img = icon.getImage();
            float hRatio = (float) icon.getIconHeight() / dim.height;
            float wRatio = (float) icon.getIconWidth() / dim.width;
            int h, w;
            if (hRatio > wRatio) {
                h = dim.height;
                w = (int) (icon.getIconWidth() / hRatio);
            } else {
                w = dim.width;
                h = (int) (icon.getIconHeight() / wRatio);
            }
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            return icon;
        }
    }

    /**
     * CLAIM 送信を行う。
     * INFORMATION カルテの送信についてはここを見るべし。
     */
    private void sendClaim(DocumentModel sendModel, boolean admitFlag) {

        // ヘルパークラスを生成しVelocityが使用するためのパラメータを設定する
        ClaimHelper helper = new ClaimHelper(sendModel, admitFlag);

        Set<ModuleModel> modules = sendModel.getModules();


        // 保存する KarteModel の全モジュールをチェックし
        // それが ClaimBundle ならヘルパーへ追加する
        for (ModuleModel module : modules) {
            IInfoModel infoModel = module.getModel();
            if (infoModel instanceof ClaimBundle) {
                helper.addClaimBundle((ClaimBundle) infoModel);
            }
        }
        //カルテの診療内容（claim）をＯＲＣＡへ送信。
        MessageBuilder mb = new MessageBuilder();
        String claimMessage = mb.build(helper);
        ClaimMessageEvent claim = new ClaimMessageEvent(this, sendModel.getKarte().getPatient(), claimMessage);

        claim.setTitle(sendModel.getDocInfo().getTitle());
        claim.setConfirmDate(helper.getConfirmDate());

        if (claimListener != null) {
            claimListener.claimMessageEvent(claim);
        }
    }

    /**
     * MML送信を行う
     */
    private void sendMml(DocumentModel sendModel) {

        IChart chart = (KarteEditor.this).getParentContext();
        MMLHelper mb = new MMLHelper(sendModel, chart.getPatient().getPatientId());

        try {
            VelocityContext context = GlobalConstants.getVelocityContext();
            context.put("mmlHelper", mb);

            // このスタンプのテンプレートファイルを得る
            String templateFile = "mml2.3Helper.vm";

            // Merge する
            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);
            InputStream instream = GlobalConstants.getTemplateAsStream(templateFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "SHIFT_JIS"));
            Velocity.evaluate(context, bw, "mml", reader);
            bw.flush();
            bw.close();
            reader.close();
            String mml = sw.toString();

            if (sendMml && mmlListener != null) {
                MmlMessageEvent mevt = new MmlMessageEvent(this);
                mevt.setGroupId(mb.getDocId());
                mevt.setMmlInstance(mml);
                if (mb.getSchema() != null) {
                    mevt.setSchema(mb.getSchema());
                }
                mmlListener.mmlMessageEvent(mevt);
            }
      //      if (GlobalVariables.getJoinAreaNetwork()) {
      //      }
        } catch (Exception e) {
               LogWriter.error(getClass(), e);
        }
    }

    //元々はAbstractChartDocumentから継承
    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    @Override
    public IChart getParentContext() {
        return parent;
    }

    /**
     * MEMO: unused?
     * @return
     */
    private boolean isReadOnly() {
        return getParentContext().isReadOnly();
    }

    /**
     *
     */
    private void disableMenus() {
        // このウインドウに関連する全てのメニューをdisableにする
        ChartMediator mediator = getParentContext().getChartMediator();
        mediator.disableMenus(CHART_MENUS);
    }

    /**
     *
     * @param name
     * @param enabled
     */
    private void enabledAction(String name, boolean enabled) {
        getParentContext().getChartMediator().enabledAction(name, enabled);
        //  parent.enabledAction(name, enabled);
    }

    /**
     * 共通の警告表示を行う。
     * @param title
     * @param message
     */
    protected void warning(String title, String message) {
        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), message, GlobalConstants.getFrameTitle(title), JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @return
     */
    @Override
    public List<JTabbedPane> getTabbedPanels() {
        return null;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean update(Object o) {
        return true;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(IChart parent) {
        this.parent = parent;
    }

    /**
     * 状態クラスのインタフェース
     */
    protected interface EditorState {

        /**
         *
         */
        public void controlMenu();

        /**
         *
         * @return
         */
        public boolean isDirty();
    }

    /**
     * No dirty 状態クラス
     */
    protected final class NoDirtyState implements EditorState {

        /**
         *
         */
        public NoDirtyState() {
        }

        /**
         *
         */
        @Override
        public void controlMenu() {
            enabledAction(GUIConst.ACTION_SAVE, false); // 保存
            enabledAction(GUIConst.ACTION_PRINT, false); // 印刷
            enabledAction(GUIConst.ACTION_DIRECTION, false);
            enabledAction(GUIConst.ACTION_CUT, false);
            enabledAction(GUIConst.ACTION_COPY, false);
            enabledAction(GUIConst.ACTION_PASTE, false);
            enabledAction(GUIConst.ACTION_UNDO, false);
            enabledAction(GUIConst.ACTION_REDO, false);
            enabledAction(GUIConst.ACTION_INSERT_TEXT, false);
            enabledAction(GUIConst.ACTION_INSERT_SCHEMA, false);
            enabledAction(GUIConst.ACTION_INSERT_STAMP, false);
            enabledAction(GUIConst.ACTION_SELECT_INSURANCE, !modify);
            enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        }

        /**
         *
         * @return
         */
        @Override
        public boolean isDirty() {
            return false;
        }
    }

    /**
     * Dirty 状態クラス
     */
    protected final class DirtyState implements EditorState {

        /**
         *
         */
        public DirtyState() {
        }

        /**
         *
         */
        @Override
        public void controlMenu() {
            enabledAction(GUIConst.ACTION_SAVE, true);
            enabledAction(GUIConst.ACTION_PRINT, true);
            enabledAction(GUIConst.ACTION_DIRECTION, true);
            enabledAction(GUIConst.ACTION_SELECT_INSURANCE, !modify);
            enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        }

        /**
         *
         * @return
         */
        @Override
        public boolean isDirty() {
            return true;
        }
    }

    /**
     * EmptyNew 状態クラス
     */
    protected final class SavedState implements EditorState {

        /**
         *
         */
        public SavedState() {
        }

        /**
         *
         */
        @Override
        public void controlMenu() {
            enabledAction(GUIConst.ACTION_SAVE, false);
            enabledAction(GUIConst.ACTION_PRINT, true);
            enabledAction(GUIConst.ACTION_DIRECTION, true);

            //       enabledAction(GUIConst.ACTION_NEW_KARTE, true);

            enabledAction(GUIConst.ACTION_CUT, false);
            enabledAction(GUIConst.ACTION_COPY, false);
            enabledAction(GUIConst.ACTION_PASTE, false);
            enabledAction(GUIConst.ACTION_UNDO, false);
            enabledAction(GUIConst.ACTION_REDO, false);
            enabledAction(GUIConst.ACTION_INSERT_TEXT, false);
            enabledAction(GUIConst.ACTION_INSERT_SCHEMA, false);
            enabledAction(GUIConst.ACTION_INSERT_STAMP, false);
            enabledAction(GUIConst.ACTION_SELECT_INSURANCE, false);
            enabledAction(GUIConst.ACTION_ADD_USER, GlobalVariables.isAdmin());
        }

        /**
         *
         * @return
         */
        @Override
        public boolean isDirty() {
            return false;
        }
    }

    /**
     * 状態マネージャ
     */
    protected final class StateMgr {

        private EditorState noDirtyState = new NoDirtyState();
        private EditorState dirtyState = new DirtyState();
        private EditorState savedState = new SavedState();
        private EditorState currentState;

        /**
         *
         */
        public StateMgr() {
            currentState = noDirtyState;
        }

        private boolean isDirty() {
            return currentState.isDirty();
        }

        /**
         *
         * @param dirty
         */
        private void setDirty(boolean dirty) {
            currentState = dirty ? dirtyState : noDirtyState;
            currentState.controlMenu();
        }

        /**
         *
         */
        private void beSaved() {
            currentState = savedState;
            currentState.controlMenu();
        }

        /**
         *
         */
        private void controlMenu() {
            currentState.controlMenu();
        }
    }
}
