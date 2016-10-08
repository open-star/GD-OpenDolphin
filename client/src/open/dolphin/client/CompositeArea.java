package open.dolphin.client;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.ActionMap;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * メモエリア　
 * @author kazm
 */
public class CompositeArea extends JTextArea implements IKarteComposite, CaretListener {

    private boolean hasSelection;
    private ActionMap map;

    /** Creates a new instance of CompositeArea */
    public CompositeArea() {
        this.addCaretListener(this);
    }

    /**
     *
     * @param row
     * @param col
     */
    public CompositeArea(int row, int col) {
        super(row, col);
        this.addCaretListener(this);
    }

    /**
     *
     * @param map
     */
    @Override
    public void enter(ActionMap map) {
        this.map = map;
        map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());
    }

    /**
     *
     * @param map
     */
    @Override
    public void exit(ActionMap map) {
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }

    /**
     *
     * @param e
     */
    @Override
    public void caretUpdate(CaretEvent e) {
        boolean newSelection = (e.getDot() != e.getMark()) ? true : false;
        if (newSelection != hasSelection) {
            hasSelection = newSelection;
            map.get(GUIConst.ACTION_PASTE).setEnabled(canPaste());
            map.get(GUIConst.ACTION_CUT).setEnabled(hasSelection);
            map.get(GUIConst.ACTION_COPY).setEnabled(hasSelection);
        }
    }

    /**
     *
     * @return
     */
    private boolean canPaste() {
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (t != null) {
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return true;
            }
        }
        return false;
    }
}
