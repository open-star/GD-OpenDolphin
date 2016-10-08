package open.dolphin.client;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.im.InputContext;
import java.awt.im.InputSubset;
import open.dolphin.project.GlobalVariables;

/**
 * IM切り替え
 * @author Minagawa, Kazushi
 */
public class AutoKanjiListener implements FocusListener {

    /**
     *
     */
    private static AutoKanjiListener instance = new AutoKanjiListener();

    /** Creates a new instance of AutoIMEListener */
    private AutoKanjiListener() {
    }

    /**
     *
     * @return
     */
    public static AutoKanjiListener getInstance() {
        return instance;
    }

    /**
     *
     * @param e
     */
    @Override
    public void focusGained(FocusEvent e) {
        if (GlobalVariables.getFepControl()) {
            e.getComponent().getInputContext().setCharacterSubsets(new Character.Subset[]{InputSubset.KANJI});
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void focusLost(FocusEvent e) {
        if (GlobalVariables.getFepControl()) {
            Component component = e.getComponent();
            if (component != null) {
                InputContext inputContext = component.getInputContext();
                if (inputContext != null) {
                    e.getComponent().getInputContext().setCharacterSubsets(null);
                }
            }
        }
    }
}
