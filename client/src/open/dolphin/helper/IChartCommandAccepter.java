/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

/**
 *
 * @author
 */
public interface IChartCommandAccepter {

    /**
     *　カルテのコマンド
     */
    static enum ChartCommand {

        /**
         *
         */
        newKarte,
        /**
         *
         */
        modifyKarte,
        /**
         *
         */
        newDocument,
        /**
         *
         */
        close,
        /**
         *
         */
        save,
        /**
         *
         */
        delete,
        /**
         *
         */
        direction,
        /**
         *
         */
        print,
        /**
         *
         */
        undo,
        /**
         *
         */
        redo,
        /**
         *
         */
        letterPaste,
        /**
         *
         */
        letterPasteFromKarte,
        /**
         *
         */
        quickEdit,
        /**
         *
         */
        showModified,
        /**
         *
         */
        hideModified,
        /**
         *
         */
        showBrowser,
        /**
         *
         */
        showUnsend,
        /**
         *
         */
        hideUnsend,
        /**
         *
         */
        showSend,
        /**
         *
         */
        hideSend,
        /**
         *
         */
        showNewest,
        /**
         *
         */
        hideNewest,
        /**
         *
         */
        ascending,
        /**
         *
         */
        descending,
        /**
         *
         */
        fontLarger,
        /**
         *
         */
        fontSmaller,
        /**
         *
         */
        fontStandard,
        /**
         *
         */
        fontBold,
        /**
         *
         */
        fontItalic,
        /**
         *
         */
        fontUnderline,
        /**
         *
         */
        leftJustify,
        /**
         *
         */
        centerJustify,
        /**
         *
         */
        rightJustify,
        /**
         *
         */
        fontRed,
        /**
         *
         */
        fontOrange,
        /**
         *
         */
        fontYellow,
        /**
         *
         */
        fontGreen,
        /**
         *
         */
        fontBlue,
        /**
         *
         */
        fontPurple,
        /**
         *
         */
        fontGray,
        /**
         *
         */
        fontBlack,
        /**
         *
         */
        resetStyle
    };

    /**
     *
     * @param command
     * @return　ディスパッチ成功なら真
     */
    public boolean dispatchChartCommand(ChartCommand command);
}
