/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

/**
 *
 * @author
 */
public interface IMainCommandAccepter {

    /**
     *
     */
    static enum MainCommand {

        /**
         *
         */
        openKarte,
        /**
         *
         */
        printerSetup,
        /**
         *
         */
        setKarteEnviroment,
        /**
         *
         */
        showStampBox,
        /**
         *
         */
        showSchemaBox,
        /**
         *
         */
        showBrowser,
        /**
         *
         */
        showTemplateEditor,
        /**
         *
         */
        changePassword,
        /**
         *
         */
        addUser,
        /**
         *
         */
        close,
        /**
         *
         */
        browseDolphinSupport,
        /**
         *
         */
        browseDolphinProject,
        /**
         *
         */
        browseMedXml,
        /**
         *
         */
        showAbout
    };

    /**
     *
     * @param command
     * @return
     */
    public boolean dispatchMainCommand(MainCommand command);
}
