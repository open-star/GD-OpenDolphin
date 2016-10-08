/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author
 */
public class SelectListener implements FocusListener {

    private static SelectListener instance = new SelectListener();

    /** Creates a new instance of AutoRomanListener */
    private SelectListener() {
    }

    /**
     *
     * @return
     */
    public static SelectListener getInstance() {
        return instance;
    }

    /**
     *
     * @param e
     */
    @Override
    public void focusGained(FocusEvent e) {
        //     e.getComponent().getInputContext().setCharacterSubsets(new Character.Subset[]{InputSubset.LATIN});
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
        e.getComponent().enableInputMethods(true);
    }
}
