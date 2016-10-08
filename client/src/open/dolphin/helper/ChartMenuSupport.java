package open.dolphin.helper;

import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import open.dolphin.helper.IChartCommandAccepter.ChartCommand;

/**
 * MenuSupport
 *
 * @author Minagawa,Kazushi
 *
 */
public class ChartMenuSupport implements MenuListener, IChartCommandAccepter {

    private ActionMap actions;
    private LinkedList<IChartCommandAccepter> chains;

    /**
     *
     * @param owner
     */
    public ChartMenuSupport(IChartCommandAccepter owner) {
        chains = new LinkedList<IChartCommandAccepter>();
        chains.add(owner);
        chains.add(this);
    }

    /**
     *
     * @param e
     */
    @Override
    public void menuSelected(MenuEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void menuDeselected(MenuEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void menuCanceled(MenuEvent e) {
    }

    /**
     *
     * @param actions
     */
    public void registerActions(ActionMap actions) {
        this.actions = actions;
    }

    /**
     *
     * @param name
     * @return
     */
    public Action getAction(String name) {
        if (actions != null) {
            return actions.get(name);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public ActionMap getActions() {
        return actions;
    }

    /**
     *
     */
    public void disableAllMenus() {
        if (actions != null) {
            Object[] keys = actions.keys();
            for (Object o : keys) {
                actions.get(o).setEnabled(false);
            }
        }
    }

    /**
     *
     * @param menus
     */
    public void disableMenus(String[] menus) {
        if (actions != null) {
            if (menus != null) {
                for (String name : menus) {
                    Action action = actions.get(name);
                    if (action != null) {
                        action.setEnabled(false);
                    }
                }
            }
        }
    }

    /**
     *
     * @param menus
     */
    public void enableMenus(String[] menus) {
        if (actions != null) {
            if (menus != null) {
                for (String name : menus) {
                    Action action = actions.get(name);
                    if (action != null) {
                        action.setEnabled(true);
                    }
                }
            }
        }
    }

    /**
     *
     * @param name
     * @param enabled
     */
    public void enabledAction(String name, boolean enabled) {
        if (actions != null) {
            Action action = actions.get(name);
            if (action != null) {
                action.setEnabled(enabled);
            }
        }
    }

    /**
     *
     * @param obj
     */
    public void setAccepter(IChartCommandAccepter obj) {
        if (chains.contains(obj)) {
            unsetAccepter(obj);
        }
        chains.add(obj);
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean unsetAccepter(IChartCommandAccepter obj) {
        return chains.remove(obj);
    }

    /**
     *
     * @return
     */
    public Object getLast() {
        return chains.getLast();
    }

    /**
     *
     * @return
     */
    public int chainSize() {
        return chains.size();
    }

    /**
     *
     * @param command
     * @return 実行結果が正常の場合は{@code true}
     */
    private boolean execute(ChartCommand command) {
        Iterator<IChartCommandAccepter> it = chains.descendingIterator();
        while (it.hasNext()) {
            if (it.next().dispatchChartCommand(command)) {
                return true;
            }
        }
        return false;
    }

    /**
     *　新たなカルテ
     * @return　ディスパッチ成功なら真
     */
    public boolean newKarteCommandExecute() {
        return execute(ChartCommand.newKarte);
    }

    /**
     *　新規ドキュメント
     * @return　ディスパッチ成功なら真
     */
    public boolean newDocumentCommandExecute() {
        return execute(ChartCommand.newDocument);
    }

    /**
     *　閉じる
     * @return　ディスパッチ成功なら真
     */
    public boolean closeCommandExecute() {
        return execute(ChartCommand.close);
    }

    /**
     *　保存
     * @return　ディスパッチ成功なら真
     */
    public boolean saveCommandExecute() {
        return execute(ChartCommand.save);
    }

    /**
     *　削除
     * @return　ディスパッチ成功なら真
     */
    public boolean deleteCommandExecute() {
        return execute(ChartCommand.delete);
    }

    /**
     *　指示箋
     * @return　ディスパッチ成功なら真
     */
    public boolean directionCommandExecute() {
        return execute(ChartCommand.direction);
    }

    /**
     *　プリントコマンド
     * @return　ディスパッチ成功なら真
     */
    public boolean printCommandExecute() {
        return execute(ChartCommand.print);
    }

    /**
     *　カルテ変更コマンド
     * @return　ディスパッチ成功なら真
     */
    public boolean modifyKarteCommandExecute() {
        return execute(ChartCommand.modifyKarte);
    }

    /**
     *　アンドゥコマンド
     * @return　ディスパッチ成功なら真
     */
    public boolean undoCommandExecute() {
        return execute(ChartCommand.undo);
    }

    /**
     *　再実行
     * @return　ディスパッチ成功なら真
     */
    public boolean redoCommandExecute() {
        return execute(ChartCommand.redo);
    }

    /**
     *　ペースト
     * @return　ディスパッチ成功なら真　
     */
    public boolean letterPasteCommandExecute() {
        return execute(ChartCommand.letterPaste);
    }

    /**
     *
     * @return　ディスパッチ成功なら真
     */
    public boolean letterPasteFromKarteCommandExecute() {
        return execute(ChartCommand.letterPasteFromKarte);
    }

    /**
     *
     * @return　ディスパッチ成功なら真
     */
    public boolean quickEditExcecute() {
        return execute(ChartCommand.quickEdit);
    }

    /**
     *　変更履歴表示
     * @return　ディスパッチ成功なら真　
     */
    public boolean showModifiedCommandExecute() {
        return execute(ChartCommand.showModified);
    }

    /**
     *　変更履歴非表示
     * @return　ディスパッチ成功なら真
     */
    public boolean hideModifiedCommandExecute() {
        return execute(ChartCommand.hideModified);
    }

    /**
     *　非送信済み履歴表示
     * @return　ディスパッチ成功なら真
     */
    public boolean showUnsendCommandExecute() {
        return execute(ChartCommand.showUnsend);
    }

    /**
     *　非送信済み履歴非表示
     * @return　ディスパッチ成功なら真
     */
    public boolean hideUnsendCommandExecute() {
        return execute(ChartCommand.hideUnsend);
    }

    /**
     *　送信済み履歴表示
     * @return　ディスパッチ成功なら真
     */
    public boolean showSendCommandExecute() {
        return execute(ChartCommand.showSend);
    }

    /**
     *　送信済み履歴非表示
     * @return　ディスパッチ成功なら真
     */
    public boolean hideSendCommandExecute() {
        return execute(ChartCommand.hideSend);
    }

    /**
     *　最新履歴表示
     * @return　ディスパッチ成功なら真　
     */
    public boolean showNewestCommandExecute() {
        return execute(ChartCommand.showNewest);
    }

    /**
     *　最新履歴非表示
     * @return　ディスパッチ成功なら真
     */
    public boolean hideNewestCommandExecute() {
        return execute(ChartCommand.hideNewest);
    }

    /**
     *　昇順
     * @return　ディスパッチ成功なら真
     */
    public boolean ascendingCommandExecute() {
        return execute(ChartCommand.ascending);
    }

    /**
     *　降順
     * @return　ディスパッチ成功なら真
     */
    public boolean descendingCommandExecute() {
        return execute(ChartCommand.descending);
    }

    /**
     *　フォント大きく
     * @return　ディスパッチ成功なら真
     */
    public boolean fontLargerCommandExecute() {
        return execute(ChartCommand.fontLarger);
    }

    /**
     *　フォント小さく
     * @return　ディスパッチ成功なら真
     */
    public boolean fontSmallerCommandExecute() {
        return execute(ChartCommand.fontSmaller);
    }

    /**
     *　フォント普通に
     * @return　ディスパッチ成功なら真
     */
    public boolean fontStandardCommandExecute() {
        return execute(ChartCommand.fontStandard);
    }

    /**
     *　フォントボールド
     * @return　ディスパッチ成功なら真
     */
    public boolean fontBoldCommandExecute() {
        return execute(ChartCommand.fontBold);
    }

    /**
     *　フォントイタリック
     * @return　ディスパッチ成功なら真
     */
    public boolean fontItalicCommandExecute() {
        return execute(ChartCommand.fontItalic);
    }

    /**
     *　アンダーライン
     * @return　ディスパッチ成功なら真
     */
    public boolean fontUnderlineCommandExecute() {
        return execute(ChartCommand.fontUnderline);
    }

    /**
     *　左揃え
     * @return　ディスパッチ成功なら真
     */
    public boolean leftJustifyCommandExecute() {
        return execute(ChartCommand.leftJustify);
    }

    /**
     *　真ん中ぞろえ
     * @return　ディスパッチ成功なら真
     */
    public boolean centerJustifyCommandExecute() {
        return execute(ChartCommand.centerJustify);
    }

    /**
     *　右揃え
     * @return　ディスパッチ成功なら真
     */
    public boolean rightJustifyCommandExecute() {
        return execute(ChartCommand.rightJustify);
    }

    /**
     *　赤
     * @return　ディスパッチ成功なら真
     */
    public boolean fontRedCommandExecute() {
        return execute(ChartCommand.fontRed);
    }

    /**
     *　オレンジ
     * @return　ディスパッチ成功なら真
     */
    public boolean fontOrangeCommandExecute() {
        return execute(ChartCommand.fontOrange);
    }

    /**
     *　黄色
     * @return　ディスパッチ成功なら真
     */
    public boolean fontYellowCommandExecute() {
        return execute(ChartCommand.fontYellow);
    }

    /**
     *　緑
     * @return　ディスパッチ成功なら真
     */
    public boolean fontGreenCommandExecute() {
        return execute(ChartCommand.fontGreen);
    }

    /**
     *　青
     * @return　ディスパッチ成功なら真
     */
    public boolean fontBlueCommandExecute() {
        return execute(ChartCommand.fontBlue);
    }

    /**
     *　紫
     * @return　ディスパッチ成功なら真
     */
    public boolean fontPurpleCommandExecute() {
        return execute(ChartCommand.fontPurple);
    }

    /**
     *　グレー
     * @return　ディスパッチ成功なら真
     */
    public boolean fontGrayCommandExecute() {
        return execute(ChartCommand.fontGray);
    }

    /**
     *　黒
     * @return　ディスパッチ成功なら真
     */
    public boolean fontBlackCommandExecute() {
        return execute(ChartCommand.fontBlack);
    }

    /**
     *
     * @return　ディスパッチ成功なら真
     */
    public boolean resetStyleCommandExecute() {
        return execute(ChartCommand.resetStyle);
    }

    /**
     *
     */
    public void cut() {
    }

    /**
     *
     */
    public void copy() {
    }

    /**
     *
     */
    public void paste() {
    }

    /**
     *
     * @param command
     * @return
     */
    @Override
    public boolean dispatchChartCommand(IChartCommandAccepter.ChartCommand command) {
        return false;
    }
}
