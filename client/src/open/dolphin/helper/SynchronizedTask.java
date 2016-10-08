/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.jdesktop.application.Application;

/**
 * 無用な非同期動作をとりあえず同期させるための過渡的クラス。
 * 最終的には不要となる
 * @param <T>
 * @param <V>
 */
public abstract class SynchronizedTask<T extends Object, V extends Object> {

    /**
     *
     * @param application
     */
    public SynchronizedTask(Application application) {
    }

    /**
     *
     * @return
     * @throws Exception
     */
    protected abstract T doInBackground() throws Exception;

    /**
     *
     * @param result
     */
    protected void succeeded(T result) {
    }

    /**
     *
     */
    protected void cancelled() {
    }

    /**
     *
     */
    protected void timeout() {
    }

    /**
     *
     * @param cause
     */
    protected void failed(Throwable cause) {
    }

    /**
     *
     * @param e
     */
    protected void interrupted(java.lang.InterruptedException e) {
    }

    /**
     *
     */
    private void startProgress() {
    }

    /**
     *
     */
    private void stopProgress() {
    }

    /**
     *
     */
    public void execute() {
        try {
            startProgress();
            succeeded(doInBackground());
        } catch (InterruptedException ex) {
        } catch (ExecutionException ex) {
            failed(ex);
        } catch (TimeoutException ex) {
            timeout();
        } catch (Exception ex) {
            failed(ex);
        } finally {
            stopProgress();
        }
    }
}
