                                                                                                                /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

/**
 *
 * @author
 */
public interface IPlugin {

    String RS_BASE_PLUGIN_NAME = "show_RS_Base";

    /**
     *
     */
    enum Type {

        /**
         *
         */
        toolmenu,
        /**
         *
         */
        panel,
        /**
         *
         */
        container,

        COOPERATION //連携
    }

    /**
     * Type=連携でのサブタイプ
     */
    enum CooperType {
        RS_BASE,    //RS_Baseと連携する
        OSIRIX      //Osirixと連携する
    }

    /**
     * CooperType=RS_BASEでのサブタイプ
     */
    enum RS_Base_Type {
        URL,        // URL連携
        FILE_OUTPUT //ファイル出力連携
    }

    /**
     * RS_Base_Type=URLでのサブタイプ
     */
    enum RS_Base_URL_Type {
        ID,     // 当該患者画面
        LABO,   //血液データ画面
        TOTAL   //当該患者の全情報
    }

    /**
     *
     */
    enum Command {

        /**
         *
         */
        getType,
        /**
         *
         */
        getName,
        /**
         *
         */
        getReadableName,
        /**
         *
         */
        getFlavor,
        /**
         *
         */
        drop,
        /**
         *
         */
        panel,
        /**
         *
         */
        update,
        /**
         *
         */
        execute,
        /**
         *
         */
        configure,
        /**
         *
         */
        messaging,

        getSubMenuText,

        writeShokenFile;
    };

    /**
     *
     * @param command
     * @param request
     * @param response
     * @return
     */
    public boolean dispatchCommand(IPlugin.Command command, Object[] request, Object[] response);
}
