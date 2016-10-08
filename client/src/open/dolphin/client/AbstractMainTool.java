package open.dolphin.client;

import java.util.concurrent.Callable;

/**
 * IMainWindow Toolプラグインの抽象クラス。
 * 具象クラスは start()、stop() を実装する。
 */
public abstract class AbstractMainTool implements IMainTool {

    private String name;
    private IMainWindow context;

    /**
     *
     */
    public AbstractMainTool() {
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @Override
    public IMainWindow getContext() {
        return context;
    }

    /**
     *
     * @param context　コンテキスト
     */
    @Override
    public void setContext(IMainWindow context) {
        this.context = context;
    }

    /**
     *
     */
    @Override
    public void enter() {
    }

    /**
     *
     * @return
     */
    @Override
    public Callable<Boolean> getStartingTask() {
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public Callable<Boolean> getStoppingTask() {
        return null;
    }

    /**
     *
     */
    @Override
    public abstract void start();

    /**
     *
     */
    @Override
    public abstract void stop();
}
