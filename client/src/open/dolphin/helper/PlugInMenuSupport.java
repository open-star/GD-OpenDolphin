/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import open.dolphin.log.LogWriter;
import open.dolphin.plugin.PluginLoader;
import open.dolphin.plugin.PluginWrapper;

/**
 * キー：プラグイン名、値:プラグイン を対応付けた連想配列型のクラスです。
 */
public class PlugInMenuSupport extends HashMap<String, open.dolphin.plugin.IPlugin> {

    /**
     *
     */
    public PlugInMenuSupport() {
        super();
    }

    /**
     * 指定されたプラグインとその名前をこの連想配列に追加します。
     * @param plugin　プラグイン
     */
    public void add(open.dolphin.plugin.IPlugin plugin) {
        PluginWrapper pluginWrapper = new PluginWrapper(plugin);
        this.put(pluginWrapper.getName(), plugin);
    }

    /**
     * 実行
     * @param name
     * @param request
     * @param response
     * @return
     */
    public boolean execute(String name, Object[] request, Object[] response) {
        if (get(name).dispatchCommand(open.dolphin.plugin.IPlugin.Command.execute, request, response)) {
            return true;
        }
        return false;
    }

    /**
     *  MEMO: unused?
     * @param source
     * @throws Exception
     */
    private void deletePlugins(File source) throws Exception {
        File[] sourceFiles = source.listFiles();
        for (int i = 0; i < sourceFiles.length; i++) {
            File sourceFile = sourceFiles[i];
            String sourceFileName = sourceFile.getName();
            if (sourceFileName.substring(sourceFileName.length() - 4, sourceFileName.length()).equals(".jar")) {
                sourceFile.delete();

            }
        }
    }

    /**
     * 各種プラグインファイル(*.jar)をコピーする。<br>
     * コピー元にjarファイルが無ければ何もしない。
     * @param source installed_plugins
     * @param dist default_plugins
     * @throws Exception
     */
    private void copyPlugins(File source, File dist) throws Exception {
        File[] sourceFiles = source.listFiles();
        for (int i = 0; i < sourceFiles.length; i++) {
            File sourceFile = sourceFiles[i];
            String sourceFileName = sourceFile.getName();
            if (sourceFileName.substring(sourceFileName.length() - 4, sourceFileName.length()).equals(".jar")) {
                FileInputStream sourceStream = null;
                FileOutputStream distStream = null;
                FileChannel sourceChannel = null;
                FileChannel distChannel = null;
                try {
                    sourceStream = new FileInputStream(sourceFile);
                    distStream = new FileOutputStream(dist.getAbsolutePath() + File.separator + sourceFile.getName());
                    sourceChannel = sourceStream.getChannel();
                    distChannel = distStream.getChannel();
                    sourceChannel.transferTo(0, sourceChannel.size(), distChannel);
                } catch (IOException ex) {
                    LogWriter.error(getClass(), ex);
                } finally {
                    sourceStream.close();
                    distStream.close();
                    sourceChannel.close();
                    distChannel.close();
                }
            }
        }
    }

    /**
     * 各種プラグインファイル(*.jar)をコピーする。
     * @param source installed_plugins
     * @param dist default_plugins
     */
    private void movePlugins(File source, File dist) {
        try {
            copyPlugins(source, dist);
            //         deletePlugins(source);  //Move -> Copy
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
    }

    /**
     * 各種プラグインファイル(*.jar)をコピーする。
     * @param sourcePath installed_plugins
     * @param distPath default_plugins
     * @return 正常にコピーできた場合は、{@code true} <br>
     * コピー元ディレクトリが存在しない場合は、{@code false}
     */
    public boolean installPlugins(String sourcePath, String distPath) {

        LogWriter.error(sourcePath, distPath);

        File sourceDir = new File(sourcePath);
        File distDir = new File(distPath);
        if (sourceDir.exists()) {
            if (!distDir.exists()) {
                distDir.mkdir();
            }
            movePlugins(sourceDir, distDir);
            return true;
        }
        return false;
    }

    /**
     * 指定したdir配下のjarファイルからプラグインを読み込み、
     * キー：プラグイン名、値：プラグイン のペアを連想配列に追加します。
     * @param path default_plugins
     * @param classPath IPlugin
     */
    public void loadPlugins(String path, String classPath) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                try {
                    File file = files[i];
                    String fileName = file.getName();
                    if (fileName.substring(fileName.length() - 4, fileName.length()).equals(".jar")) {
                        PluginLoader loader = new PluginLoader(path + "/" + fileName, classPath + fileName.substring(0, fileName.length() - 4));
                        open.dolphin.plugin.IPlugin plugin = loader.getPlugin(); //プラグインのインスタンス
                        PluginWrapper pluginWrapper = new PluginWrapper(plugin);
                        this.put(pluginWrapper.getName(), plugin);
                    }
                } catch (Exception e) {
                    LogWriter.error(getClass(), e);
                }
            }
        }
    }

    /**
     * 指定されたプラグインとその名前をこの連想配列に追加します。
     * @param plugin プラグイン
     */
    public void loadPlugin(open.dolphin.plugin.IPlugin plugin) {
        try {
            PluginWrapper pluginWrapper = new PluginWrapper(plugin);
            this.put(pluginWrapper.getName(), plugin);
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
    }
}
