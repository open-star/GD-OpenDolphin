package open.dolphin.client;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.im.InputContext;
import javax.swing.JTextField;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;

/**
 * IM切り替え
 * @author kazm
 */
public class AutoRomanListener implements FocusListener {

    /**
     *
     */
    private static AutoRomanListener instance = new AutoRomanListener();

    /** Creates a new instance of AutoRomanListener */
    private AutoRomanListener() {
    }

    /**
     *
     * @return
     */
    public static AutoRomanListener getInstance() {
        return instance;
    }

    /**
     *
     * @param e
     */
    @Override
    public void focusGained(FocusEvent e) {
        //     e.getComponent().getInputContext().setCharacterSubsets(new Character.Subset[]{InputSubset.LATIN});
        if (GlobalVariables.getFepControl()) {
            InputContext ic = e.getComponent().getInputContext();
            try {
                if (ic != null) {
                    if (ic.isCompositionEnabled()) {
                        ic.setCompositionEnabled(false);
                    }
                }
            } catch (UnsupportedOperationException error) {
                LogWriter.error(getClass(), error);
            }
            ic.setCharacterSubsets(null);
            e.getComponent().enableInputMethods(false);
        }
        JTextField f = (JTextField) e.getComponent();
        f.setSelectionStart(0);
        f.setSelectionEnd(f.getText().length());
    }

    /**
     *
     * @param e
     */
    @Override
    public void focusLost(FocusEvent e) {
        //     e.getComponent().getInputContext().setCompositionEnabled(true);
        if (GlobalVariables.getFepControl()) {
            e.getComponent().enableInputMethods(true);
        }
    }
}
