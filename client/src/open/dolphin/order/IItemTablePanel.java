/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.order;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *　MEMO:リスナー
 * @author
 */
public interface IItemTablePanel extends PropertyChangeListener {

    /**
     * Clear all items.
     */
    void clear();

    /**
     * エディタで編集したスタンプの値を返す。
     * @return スタンプ(ModuleMode = ModuleInfo + InfoModel)
     */
    Object getValue();

    /**
     * マスターテーブルで選択されたアイテムの通知を受け、
     * セットテーブルへ追加する。
     * @param e
     */
    void propertyChange(PropertyChangeEvent e);

    /**
     *
     * @param val
     */
    void setClassCode(String val);

    /**
     *
     * @param val
     */
    void setClassCodeId(String val);

    /**
     *
     * @param b
     */
    void setFindClaimClassCode(boolean b);

    /**
     *
     * @param val
     */
    void setOrderName(String val);

    /**
     *
     * @param val
     */
    void setSubClassCodeId(String val);

    /**
     *
     * @param valid
     */
    void setValidModel(boolean valid);

    /**
     * 編集するスタンプの内容を表示する。
     * @param theStamp 編集するスタンプ、戻り値は常に新規スタンプである。
     */
    void setValue(Object theStamp);

}
