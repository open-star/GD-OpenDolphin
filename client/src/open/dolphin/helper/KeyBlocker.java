package open.dolphin.helper;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author kazm
 */
public class KeyBlocker implements KeyListener {

    private Component target;

    /**
     *
     * @param target
     */
    public KeyBlocker(Component target) {
        this.target = target;
    }

    /**
     *
     */
    public void block() {
        target.addKeyListener(this);
    }

    /**
     *
     */
    public void unblock() {
        target.removeKeyListener(this);
    }

    /**
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    /**
     * Handle the key-pressed event from the text field. 
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        e.consume();
    }

    /**
     * Handle the key-released event from the text field. 
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        e.consume();
    }
}
