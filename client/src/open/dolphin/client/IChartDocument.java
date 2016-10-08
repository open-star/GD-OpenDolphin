package open.dolphin.client;

import java.util.List;
import javax.swing.JTabbedPane;
import open.dolphin.helper.IChartCommandAccepter;

/**
 * チャートドキュメントが実装するインターフェイス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public interface IChartDocument extends IChartCommandAccepter {

    /**
     *
     */
    public static enum TYPE {

        /**
         *
         */
        KarteViewerSingle,
        /**
         *
         */
        DocumentHistoryPanel,
    //    PatientInfoDocumentPanel,
        /**
         *
         */
        DiagnosisDocumentPanel,
    //    DoPanel,
     //   PhysicalPanel,
    //    PatientVisitInspector,
        /**
         *
         */
        KarteViewerDouble,
        /**
         *
         */
        DocumentBridgeImpl,
     //   AllergyPanel,
        /**
         *
         */
        CareMapDocumentPanel,
        /**
         *
         */
        PatientInspector,
        /**
         *
         */
        LetterViewer,
        /**
         *
         */
        LetterReplyImpl,
        /**
         *
         */
        ImageBrowserDocumentPanel,
     //   LaboTestDocumentPanel,
     //   MemoPanel,
        /**
         *
         */
        LetterImpl,
        /**
         *
         */
        KarteEditor,
        /**
         *
         */
        LetterReplyViewer,
        /**
         *
         */
        KarteDocumentViewer,
    //    MessagingPanel,
        /**
         *
         */
        Plugin
    };

    /**
     *
     * @return　パネルのタイプ
     */
    public TYPE getType();

    /**
     *
     * @return　タイトル
     */
    public String getTitle();

    /**
     *
     * @return
     */
    public IChart getParentContext();

    /**
     * 
     */
    public void start();

    /**
     *
     */
    public void stop();

    /**
     *
     */
    public void enter();

    /**
     *
     * @return　命令実行可能か
     */
    public boolean prepare();

    /**
     *
     * @param o
     * @return　更新したか
     */
    public boolean update(Object o);

    /**
     *
     * @return　ダーティ
     */
    public boolean isDirty();

    /**
     *
     * @param dirty
     */
    public void setDirty(boolean dirty);

    /**
     *
     * @return　レイアウトを保存すべきか
     */
    public boolean itLayoutSaved();

    /**
     *
     * @return　タブのコンテンツ
     */
    public List<JTabbedPane> getTabbedPanels();
}
