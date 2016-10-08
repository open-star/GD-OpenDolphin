package open.dolphin.plugin;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;

import open.dolphin.client.IChart;
import open.dolphin.client.settings.IAbstractSettingPanel;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalConstants;
import open.dolphin.project.GlobalVariables;

/**
 * RS_Baseの画面の表示、RS_Baseへのファイル出力を提供するためのクラスです。
 * @author
 */
public class RSBasePlugin implements IPlugin {

    private String address;
    //private final String PATIENT = "患者";
    //private final String BLOOD_DATA = "血液データ";
    //private final String ALL_INFO = "全情報";
    //private final String strSubMenu[] = {PATIENT, BLOOD_DATA, ALL_INFO};

    /**
     * 
     */
    public RSBasePlugin() {
        this.address = GlobalVariables.getRsbAddress();
    }

    private String getName() {
        return IPlugin.RS_BASE_PLUGIN_NAME;
    }

    private String getReadableName() {
        return "RS_Base表示";
    }

    /**
     *
     * @return サブメニューに表示するテキスト配列
     */
     private String[] getSubMenuText() {
        final String PATIENT = "患者";
        final String BLOOD_DATA = "血液データ";
        final String ALL_INFO = "全情報";
        final String subMenuText[] = {PATIENT, BLOOD_DATA, ALL_INFO};

        return subMenuText;
    }

    /**
     * 「RS_Base設定」画面オブジェクトを返す
     * @return「 RS_Base設定」画面オブジェクト
     */
    private IAbstractSettingPanel configure() {
        return new RSBasePluginSettingPanel(this);
    }

    /**
     * http://localhost/~rsn/2000.cgi?show=ID番号 を表示させる。
     * @param chartContext カルテ情報
     * @return 常に{@code true}
     */
    private boolean execute(IChart chartContext) {
        ApplicationContext appCtx = GlobalConstants.getApplicationContext();
        Application app = appCtx.getApplication();
        if (chartContext != null) {
            String IDString = chartContext.getPatientVisit().getPatientId(); //患者ID
            final String PatentID = Integer.toString(Integer.parseInt(IDString));
            final Desktop desktop = Desktop.getDesktop();
            Task task = new Task<Object, Void>(app) {

                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        try {
                            desktop.browse(new URI("http://" + address + "/~rsn/2000.cgi?show=" + PatentID));
                        } catch (URISyntaxException ex) {
                            LogWriter.error(getClass(), ex);
                        }
                    } catch (IOException ex) {
                        LogWriter.error(getClass(), ex);
                    }
                    return true;
                }

                @Override
                protected void succeeded(Object result) {
                }

                @Override
                protected void cancelled() {
                }

                @Override
                protected void failed(java.lang.Throwable cause) {
                }

                @Override
                protected void interrupted(java.lang.InterruptedException e) {
                }
            };

            appCtx.getTaskService().execute(task);
            return true;
        }
        return true;
    }

    /**
     * プラグインの型を返す
     * @return toolmenu
     */
    private Type getType() {
        return open.dolphin.plugin.IPlugin.Type.toolmenu;
    }
    
    /**
     *
     * @return 常に{@code false}
     */
    private Boolean update(Object object) {
        return false;
    }

    /**
     *　コマンドの切り替え
     * @param command　コマンド
     * @param request　コマンドの実行に必要な情報
     * @param response　コマンドの実行結果情報
     * @return コマンドの実行結果が、正常の場合は　{@code true}
     */
    @Override
    public boolean dispatchCommand(IPlugin.Command command, Object[] request, Object[] response) {
        boolean blRet = true;
        switch (command) {
            case execute:
                blRet = execute(((IChart) request[0])); // カルテ情報
                response[0]= blRet;
                break;
            case getType:
                response[0] = getType();
                break;
            case getName:
                response[0] = getName();
                break;
            case getReadableName:
                response[0] = getReadableName();
                break;
            case configure:
                response[0] = configure();          // RS_Base設定画面オブジェクト
                break;
            case update:
                blRet = update(request[0]);
                response[0]= blRet;
                break;
            case getSubMenuText:
                response[0] = getSubMenuText();
                break;
            case writeShokenFile:
                blRet = writeShokenFile(((IChart) request[0]), (String)(request[1])); // カルテ情報
                response[0]= blRet;
                break;
            default:
                blRet  = false;
        }

        return blRet;
    }

    /**
     * ファイルに指定した患者の所見を書き出す。
     * @param chartContext カルテ情報
     * @param text 所見
     * @return 所見が正常に書き出された場合は、{@code true}
     */
    private Boolean writeShokenFile(IChart chartContext, String text) {
        boolean blRet = true;

        String patientID = chartContext.getPatientVisit().getPatientId(); //患者ID
        WriteShokenFile wsf = new WriteShokenFile(patientID);
        blRet = wsf.witeFileToShare(text);

        return blRet;
    }
}
