package open.dolphin.client;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

/**
 *　MEMO:リスナー
 * @author
 */
public class TaskTimerMonitor implements PropertyChangeListener, ActionListener {

    private Task task;
    private TaskMonitor taskMonitor;
    private ProgressMonitor monitor;
    private Timer timer;
    private int current = 0;
    private int max;

    /**
     *
     * @param task
     * @param taskMonitor
     * @param c
     * @param message
     * @param note
     * @param delay
     * @param maxEsitimation
     */
    public TaskTimerMonitor(Task task, TaskMonitor taskMonitor, Component c, Object message, String note, int delay, int maxEsitimation) {
        this.task = task;
        this.taskMonitor = taskMonitor;
        max = maxEsitimation / delay;
        monitor = new ProgressMonitor(c, message, note, 0, max);
        timer = new Timer(delay, this);
    }

    /**
     *
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String propertyName = e.getPropertyName();

        if ("started".equals(propertyName)) {
            if (!timer.isRunning()) {
                timer.start();
            }

        } else if ("done".equals(propertyName)) {
            timer.stop();
            monitor.close();
            taskMonitor.removePropertyChangeListener(this);

        } else if ("message".equals(propertyName)) {
            String text = (String) (e.getNewValue());//MEMO: unused?

        } else if ("progress".equals(propertyName)) {
            int value = (Integer) (e.getNewValue());
            monitor.setProgress(value);
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (monitor.isCanceled()) {
            task.cancel(true);
        } else if (current > max) {
            task.cancel(true);
        } else {
            monitor.setProgress(current++);
        }
    }
}
