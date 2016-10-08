package open.dolphin.table;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import open.dolphin.client.AutoKanjiListener;
import open.dolphin.client.AutoRomanListener;

/**
 * IMECellEditor
 * 
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class IMECellEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 6940297554018543284L;

    /** Creates a new instance of DTableCellEditor
     * @param tf 
     * @param on
     * @param clickCount
     */
    public IMECellEditor(final JTextField tf, final int clickCount, final boolean on) {

        super(tf);
        int ccts = clickCount == 1 ? 1 : 2;
        setClickCountToStart(ccts);
        if (on) {
            // IME をONにする Windows のみに有効
            tf.addFocusListener(AutoKanjiListener.getInstance());
        } else {
            // IME をOFFにする Windows のみに有効
            tf.addFocusListener(AutoRomanListener.getInstance());
        }


    }
}
