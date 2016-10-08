package open.dolphin.client;

import java.awt.print.PageFormat;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenuBar;
import open.dolphin.helper.IMainCommandAccepter.MainCommand;
import open.dolphin.helper.MainMenuSupport;
import open.dolphin.helper.PlugInMenuSupport;
import open.dolphin.infomodel.PatientVisitModel;

/**
 * アプリケーションのメインウインドウインターフェイスクラス。
 *
 * @author Minagawa, Kazushi. Digital Globe, Inc.
 */
public interface IMainWindow {

    /**
     *
     * @param command
     * @return
     */
    public boolean dispatchMainCommand(MainCommand command);

    //   public Map<String, IMainService> getProviders();
    //   public IMainService getProvider(String name);
    //   public void putProvider(String name, IMainService value);
    /**
     *
     * @return
     */
    public JMenuBar getMenuBar();

    /**
     *
     * @return
     */
    public MainMenuSupport getMenuSupport();

    /**
     *
     * @param actions
     */
    public void registerActions(ActionMap actions);

    /**
     *
     * @param name
     * @return
     */
    public Action getAction(String name);

    /**
     *
     * @param name
     * @param b
     */
    public void enabledAction(String name, boolean b);

    /**
     *
     * @param pvt
     * @param keyword
     */
    public void openKarte(PatientVisitModel pvt, String keyword);

    /**
     *
     */
    public void block();

    /**
     *
     */
    public void unblock();

    /**
     *
     * @return
     */
    public BlockGlass getGlassPane();

    /**
     * 
     * @param name
     * @return
     */
    public IMainService getPlugin(String name);

    /**
     *
     * @return
     */
    public PageFormat getPageFormat();

    /**
     *
     * @param patientId
     * @return
     */
    public boolean isKarteOpened(long patientId);

    /**
     *
     * @param chart
     */
    public void addChart(IChart chart);

    /**
     *
     * @param chart
     * @return
     */
    public boolean removeChart(IChart chart);

    //  public List<IChart> getAllEditorFrames();
    /**
     *
     * @param chart
     */
    public void addEditorFrame(IChart chart);

    /**
     *
     * @param chart
     */
    public void removeEditorFrame(IChart chart);

    /**
     *
     * @return
     */
    public PlugInMenuSupport getPlugin();
    //  public boolean showStampBox();
    //   public boolean showSchemaBox();
    //  public boolean showTemplateEditor();
}
