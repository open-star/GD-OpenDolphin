package open.dolphin.client;

import open.dolphin.client.karte.DocumentHistoryPanel;
import javax.swing.JFrame;
import open.dolphin.helper.PlugInMenuSupport;

import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.PVTHealthInsuranceModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.PatientVisitModel;

/**
 *
 * @author
 */
public interface IChart extends IMainTool {

    /**
     *
     */
    public enum NewKarteOption {

        /**
         *
         */
        BROWSER_NEW,
        /**
         * 
         */
        BROWSER_COPY_NEW,
        /**
         *
         */
        BROWSER_MODIFY,
        /**
         *
         */
        EDITOR_NEW,
        /**
         * 
         */
        EDITOR_COPY_NEW,
        /**
         *
         */
        EDITOR_MODIFY
    };

    /**
     *
     */
    public enum NewKarteMode {

        /**
         *
         */
        EMPTY_NEW,
        /**
         * 
         */
        APPLY_RP,
        /**
         *
         */
        ALL_COPY,
        /**
         *
         */
        FROM_TEMPLATE
    };

    /**
     *
     */
    public enum state {

        /**
         *
         */
        CLOSE_NONE,
        /**
         * 
         */
        CLOSE_SAVE,
        /**
         *
         */
        OPEN_NONE,
        /**
         *
         */
        OPEN_SAVE,
        /**
         *
         */
        CANCEL_PVT
    };

    /**
     *
     * @param keyword
     */
    public void setKeyword(String keyword);

    /**
     *
     * @return
     */
    public KarteBean getKarte();

    /**
     *
     * @param karte
     */
    public void setKarte(KarteBean karte);

    /**
     *
     * @return
     */
    public PatientModel getPatient();

    /**
     *
     * @return
     */
    public PatientVisitModel getPatientVisit();

    /**
     *
     * @param model
     */
    public void setPatientVisit(PatientVisitModel model);

    /**
     *
     * @return
     */
    public IChart.state getChartState();

    /**
     *
     * @param state
     */
    public void setChartState(IChart.state state);

    /**
     *
     * @return
     */
    public boolean isReadOnly();

    /**
     *
     * @param b
     */
    public void setReadOnly(boolean b);

    /**
     *
     * @return
     */
    public boolean close();

    /**
     *
     * @return
     */
    public JFrame getFrame();

    /**
     *
     * @return
     */
    public StatusPanel getStatusPanel();

    /**
     *
     * @param statusPanel
     */
    public void setStatusPanel(StatusPanel statusPanel);

    /**
     *
     * @return
     */
    public ChartMediator getChartMediator();

    /**
     *
     * @param name
     * @param enabled
     */
    public void enabledAction(String name, boolean enabled);

    /**
     *
     * @return
     */
    public DocumentHistoryPanel getDocumentHistory();

    /**
     *
     * @param tabName
     */
    public void showDocument(String tabName);

    /**
     *
     * @return
     */
    public boolean isDirty();

    /**
     *
     * @return
     */
    public boolean isActivated();

    /**
     *
     * @return
     */
    public PVTHealthInsuranceModel[] getHealthInsurances();

//    public void setNewButtonEnabled(boolean enable);
    //   public void setDirectionButtonEnabled(boolean enable);
    //   public Map<String, IChartDocument> getChartDocuments();
    /**
     *
     * @param name
     * @return
     */
    public IChartDocument getChartDocument(String name);

    /**
     *
     * @param name
     * @param document
     */
    public void setChartDocument(String name, IChartDocument document);

    /**
     *
     * @param title
     * @param document
     */
    public void closeChartDocument(String title, IChartDocument document);

    /**
     *
     * @return 名称とプラグインのペアを格納する連想配列
     */
    public PlugInMenuSupport getPlugins();
}
