package open.dolphin.plugin;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import open.dolphin.client.IChart;
import open.dolphin.log.LogWriter;

/**
 *
 * @author oda
 */
public class PluginLoader {

    private URLClassLoader loader; // クラスローダー
    private Class clazz; // Class オブジェクト
    private Object object; // インスタンス

    /**
     * url(jarファイル)から指定したクラス名のプラグインクラスを読み込みます。
     * @param url　Uniform Resource Locator
     * @param className クラス名
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public PluginLoader(String url, String className) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            loader = new URLClassLoader(new URL[]{new File(url).toURI().toURL()});
            clazz = Class.forName(className, true, loader);
            object = clazz.newInstance();
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * MEMO:Invakeを使用
     * @param methodName
     * @param parameterType
     * @param parameter
     * @return
     */
    public Object Execute(String methodName, Class[] parameterType, Object[] parameter) {
        Object result = null;
        try {
            Method method = getClazz().getMethod(methodName, parameterType);
            result = method.invoke(getPlugin(), parameter);
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return result;
    }

    /**
     *
     * @param methodName
     * @param chartContext
     * @return
     */
    public boolean MethodCall(String methodName, IChart chartContext) {
        Class[] parameterType = new Class[1];
        parameterType[0] = IChart.class;
        Object[] parameter = new Object[1];
        parameter[0] = chartContext;
        return (Boolean) Execute(methodName, parameterType, parameter);
    }

    /**
     * Class オブジェクトを返します。
     * @return Class オブジェクト
     */
    public Class getClazz() {
        return clazz;
    }

    /**
     * プラグインのインスタンスを返します。
     * @return プラグインのインスタンス
     */
    public open.dolphin.plugin.IPlugin getPlugin() {
        return (open.dolphin.plugin.IPlugin) object;
    }
}
