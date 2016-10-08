/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client;

import javax.swing.JLabel;
import javax.swing.JTextPane;

/**
 *
 * @author tomohiro
 */
public interface IKartePanel {

    /**
     * 
     * @return
     */
    JTextPane getPTextPane();

    /**
     *
     * @return
     */
    JTextPane getSoaTextPane();

    /**
     *
     * @return
     */
    JLabel getTimeStampLabel();
}
