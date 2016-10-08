package open.dolphin.client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import open.dolphin.delegater.remote.RemoteDocumentDelegater;
import open.dolphin.delegater.remote.RemotePVTDelegater;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.log.LogWriter;

/**
 *　
 * @author kazm
 */
public class ModelSender {

    private static ModelSender instance = new ModelSender();
    private LinkedBlockingQueue queue;
    private Thread sender;
    private ModelConsumer consumer;

    /**
     *
     * @return
     */
    public static ModelSender getInstance() {
        return instance;
    }

    /**
     *
     */
    private ModelSender() {
        queue = new LinkedBlockingQueue();
        consumer = new ModelConsumer();
        sender = new Thread(consumer);
        sender.setPriority(Thread.NORM_PRIORITY);
        sender.start();
    }

    /**
     *
     * @param model
     */
    public void offer(Object model) {
        queue.offer(model);
    }

    /**
     *
     * @return
     */
    public Object take() {
        try {
            return queue.take();
        } catch (InterruptedException ex) {
            LogWriter.error(getClass(), ex);
        }
        return null;
    }

    /**
     *
     */
    public void stop() {
        Thread moribound = sender;
        sender = null;
        moribound.interrupt();
    }

    /**
     *
     * @param model
     * @return
     */
    private Callable<Long> getDocumentTask(final DocumentModel model) {

        Callable<Long> c = new Callable() {

            /**
             *
             */
            @Override
            public Long call() {
                RemoteDocumentDelegater ddl = new RemoteDocumentDelegater();
                long result = ddl.putDocument(model);
                return new Long(result);
            }
        };
        return c;
    }

    /**
     *
     * @param model
     * @return
     */
    private Callable<Integer> getPvtTask(final PatientVisitModel model) {

        Callable<Integer> c = new Callable() {

            @Override
            public Integer call() {
                RemotePVTDelegater pdl = new RemotePVTDelegater();
                int result = pdl.addPvt(model);
                return new Integer(result);
            }
        };
        return c;
    }

    /**
     *
     */
    class ModelConsumer implements Runnable {

        /**
         *
         */
        @Override
        public void run() {
            while (true) {
                final Object model = take();
                if (model != null) {
                    try {
                        FutureTask task = null;
                        if (model instanceof PatientVisitModel) {
                            task = new FutureTask(getPvtTask((PatientVisitModel) model));
                        } else if (model instanceof DocumentModel) {
                            task = new FutureTask(getDocumentTask((DocumentModel) model));
                        }
                        new Thread(task).start();
                        task.get(120, TimeUnit.SECONDS);
                    } catch (InterruptedException ex) {
                        LogWriter.error(getClass(), ex);
                    } catch (ExecutionException ex) {
                        LogWriter.error(getClass(), ex);
                    } catch (TimeoutException ex) {
                        LogWriter.error(getClass(), "タイムアウトしました: " + ex);
                        offer(model);
                    }
                }
            }
        }
    }
}
