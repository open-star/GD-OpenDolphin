package open.dolphin.helper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import open.dolphin.client.IChart;

/**
 * @param <T>
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public abstract class DBTask<T> {

    /**
     *
     */
    protected IChart context;

    /**
     *
     * @param context
     */
    public DBTask(IChart context) {
        this.context = context;
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
