package open.dolphin.client.settings;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import open.dolphin.utils.Adapter;

/**
 * AbstractSettingPanel
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public interface IAbstractSettingPanel {

    /**
     *
     */
    public static final String STATE_PROP = "stateProp";

    /**
     *
     */
    public enum State {

        /**
         *
         */
        NONE_STATE,
        /**
         * 
         */
        VALID_STATE,
        /**
         *
         */
        INVALID_STATE
    };

    /**
     * Creates a new instance of SettingPanel 
     * @param adapter
     */
    public void onChenge(Adapter<Boolean, Boolean> adapter);

    /**
     *
     * @return
     */
    public String getId();

    /**
     *
     * @param id
     */
    public void setId(String id);

    /**
     *
     * @return
     */
    public String getTitle();

    /**
     *
     * @param title
     */
    public void setTitle(String title);

    /**
     *
     * @return
     */
    public String getIcon();

    /**
     *
     * @param icon
     */
    public void setIcon(String icon);

    /**
     *
     * @return　コンテキスト
     */
    public ProjectSettingDialog getContext();

    /**
     *
     * @param context
     */
    public void setContext(ProjectSettingDialog context);

    /**
     *
     * @return
     */
    public boolean isLoginState();

    /**
     *
     * @param login
     */
    public void setLogInState(boolean login);

    /**
     *
     * @return
     */
    public JPanel getPanel();

    /**
     *
     */
    public abstract void start();

    /**
     *
     */
    public abstract void save();

    /**
     *
     * @param prop
     * @param l
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener l);

    /**
     *
     * @param prop
     * @param l
     */
    public void removePropertyChangeListener(String prop, PropertyChangeListener l);

    /**
     * 
     * @param state
     */
    public void setState(State state);

    /**
     *
     * @return
     */
    public State getState();
}
