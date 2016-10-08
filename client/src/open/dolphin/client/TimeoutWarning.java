package open.dolphin.client;

import open.dolphin.project.GlobalConstants;
import java.awt.Component;

import javax.swing.JOptionPane;

/**
 *
 * @author
 */
public class TimeoutWarning {

    private Component parent;
    private String title;
    private String message;

    /**
     *
     * @param parent
     * @param title
     * @param message
     */
    public TimeoutWarning(Component parent, String title, String message) {
        this.parent = parent;
        this.parent = parent;
        this.message = message;
    }

    /**
     *
     */
    public void start() {
        StringBuilder sb = new StringBuilder();
        if (message != null) {
            sb.append(message);
        }
        sb.append("コマンドの実行中にタイムアウトが生じました。"); //GlobalVariables.getString("task.timeoutMsg1"));
        sb.append(System.getProperty("line.separator"));
        sb.append("サーバが混み合っている、ネットワーク機器等に障害が発生している、等の可能性があります。"); //GlobalVariables.getString("task.timeoutMsg2"));
        JOptionPane.showMessageDialog(parent,sb.toString(),GlobalConstants.getFrameTitle(title),JOptionPane.WARNING_MESSAGE);
    }
}
