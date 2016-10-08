package open.dolphin.plugin.patientvisitinspector;

import open.dolphin.project.GlobalConstants;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTabbedPane;

import open.dolphin.client.CalendarCardPanel;
import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;
import open.dolphin.client.IChartDocument.TYPE;
import open.dolphin.helper.IChartCommandAccepter.ChartCommand;
import open.dolphin.infomodel.SimpleDate;

/**
 * 　MEMO:画面
 * @author kazm
 */
public final class PatientVisitInspector extends CalendarCardPanel implements IChartDocument {

    /**
     *
     */
    public static final String TITLE = "来院歴";
    private String pvtEvent; // PVT
    private IChart parent;
    private String title;

    /**
     * PatientVisitInspector を生成する。
     * @param parent
     */
    public PatientVisitInspector(IChart parent) {
        super(GlobalConstants.getEventColorTable());
        this.title = TITLE;
        this.parent = parent;
        initCustomComponents();
        update();
    }

    /**
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.Plugin;
        //    return TYPE.PatientVisitInspector;
    }

    /**
     * GUIコンポーネントを初期化する。
     */
    private void initCustomComponents() {
        pvtEvent = "PVT"; //GlobalVariables.getString("eventCode.pvt"); // PVT
        setCalendarRange(new int[]{-12, 0});
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    private void update() {

        // 来院歴を取り出す
        List<String> latestVisit = getParentContext().getKarte().getEntryCollection("visit");

        // 来院歴
        if (latestVisit != null && latestVisit.size() > 0) {
            List<SimpleDate> simpleDates = new ArrayList<SimpleDate>(latestVisit.size());
            for (String pvtDate : latestVisit) {
                SimpleDate sd = SimpleDate.mmlDateToSimpleDate(pvtDate);
                sd.setEventCode(pvtEvent);
                simpleDates.add(sd);
            }
            // CardCalendarに通知する
            setMarkList(simpleDates);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean itLayoutSaved() {
        return true;
    }

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
     *
     */
    @Override
    public void start() {
    }

    /**
     *
     */
    @Override
    public void stop() {
    }

    /**
     *
     */
    @Override
    public void enter() {
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
     *
     * @return
     */
    @Override
    public boolean isDirty() {
        return false;
    }

    /**
     *
     * @param dirty
     */
    @Override
    public void setDirty(boolean dirty) {
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchChartCommand(ChartCommand command) {
        return false;
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
}
