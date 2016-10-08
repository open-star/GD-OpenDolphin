package open.dolphin.helper;

import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.ActionMap;
import open.dolphin.helper.IMainCommandAccepter.MainCommand;

/**
 * MenuSupport
 *
 * @author Minagawa,Kazushi
 *
 */
public class MainMenuSupport {

    private ActionMap actions;
    private ArrayList<IMainCommandAccepter> chains;

    /**
     *
     * @param owner
     */
    public MainMenuSupport(IMainCommandAccepter owner) {
        chains = new ArrayList<IMainCommandAccepter>();
        chains.add(owner);
        //   chains.add(this);
    }

    /**
     *
     * @param actions
     */
    public void registerActions(ActionMap actions) {
        this.actions = actions;
    }

    /**
     *
     * @param name
     * @return
     */
    public Action getAction(String name) {
        if (actions != null) {
            return actions.get(name);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public ActionMap getActions() {
        return actions;
    }

    /**
     *
     */
    public void disableAllMenus() {
        if (actions != null) {
            Object[] keys = actions.keys();
            for (Object o : keys) {
                actions.get(o).setEnabled(false);
            }
        }
    }

    /**
     *
     * @param menus
     */
    public void disableMenus(String[] menus) {
        if (actions != null && menus != null) {
            for (String name : menus) {
                Action action = actions.get(name);
                if (action != null) {
                    action.setEnabled(false);
                }
            }
        }
    }

    /**
     *
     * @param menus
     */
    public void enableMenus(String[] menus) {
        if (actions != null && menus != null) {
            for (String name : menus) {
                Action action = actions.get(name);
                if (action != null) {
                    action.setEnabled(true);
                }
            }
        }
    }

    /**
     *
     * @param name
     * @param enabled
     */
    public void enabledAction(String name, boolean enabled) {
        if (actions != null) {
            Action action = actions.get(name);
            if (action != null) {
                action.setEnabled(enabled);
            }
        }
    }

    /**
     *
     * @param obj
     */
    public void setLast(IMainCommandAccepter obj) {
        // 最初のターゲットに設定する
        chains.add(obj);
    }

    /**
     *
     * @return　
     */
    public Object getLast() {
        // 最初のターゲットを返す
        return chains.get(chains.size() - 1);
    }

    /**
     *
     * @param command
     * @return
     */
    private boolean execute(MainCommand command) {
        for (int index = chains.size() - 1; index >= 0; index--) {
            if (chains.get(index).dispatchMainCommand(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean openKarteCommandExecute() {
        return execute(MainCommand.openKarte);
    }

    /**
     *
     * @return
     */
    public boolean printerSetupCommandExecute() {
        return execute(MainCommand.printerSetup);
    }

    /**
     *
     * @return
     */
    public boolean setKarteEnviromentCommandExecute() {
        return execute(MainCommand.setKarteEnviroment);
    }

    /**
     *
     * @return
     */
    public boolean showStampBoxCommandExecute() {
        return execute(MainCommand.showStampBox);
    }

    /**
     *
     * @return
     */
    public boolean showSchemaBoxCommandExecute() {
        return execute(MainCommand.showSchemaBox);
    }

    /**
     *
     * @return
     */
    public boolean showTemplateEditorCommandExecute() {
        return execute(MainCommand.showTemplateEditor);
    }

    /**
     *
     * @return
     */
    public boolean changePasswordCommandExecute() {
        return execute(MainCommand.changePassword);
    }

    /**
     *
     * @return
     */
    public boolean addUserCommandExecute() {
        return execute(MainCommand.addUser);
    }

    /**
     *
     * @return
     */
    public boolean browseDolphinSupportCommandExecute() {
        return execute(MainCommand.browseDolphinSupport);
    }

    /**
     *
     * @return
     */
    public boolean browseDolphinProjectCommandExecute() {
        return execute(MainCommand.browseDolphinProject);
    }

    /**
     *
     * @return
     */
    public boolean browseMedXmlCommandExecute() {
        return execute(MainCommand.browseMedXml);
    }

    /**
     *
     * @return
     */
    public boolean showAboutCommandExecute() {
        return execute(MainCommand.showAbout);
    }

    /**
     *
     * @return
     */
    public boolean closeCommandExecute() {
        return execute(MainCommand.close);
    }

    /**
     *
     * @return
     */
    public boolean close() {
        return false;
    }

    /**
     *
     * @return
     */
    public boolean update() {
        return false;
    }
}
