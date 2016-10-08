package open.dolphin.client;

import java.util.concurrent.Callable;

/**
 * MainWindow の Tool プラグインクラス。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public interface IMainTool extends IMainService {

    /**
     * 
     */
    public void enter();

    /**
     *
     * @return
     */
    public Callable<Boolean> getStartingTask();

    /**
     *
     * @return
     */
    public Callable<Boolean> getStoppingTask();
}
