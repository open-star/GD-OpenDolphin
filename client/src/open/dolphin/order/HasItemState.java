package open.dolphin.order;

import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import open.dolphin.infomodel.MasterItem;

/**
 * ValidState
 * 
 * @author Minagawa,Kazushi
 */
public class HasItemState extends AbstractSetTableState<MasterItem> {

    /**
     *
     * @param setTable
     * @param deleteBtn
     * @param clearBtn
     * @param stampNameField
     */
    public HasItemState(JTable setTable, JButton deleteBtn, JButton clearBtn, JTextField stampNameField) {
        super(setTable, deleteBtn, clearBtn, stampNameField);
    }

    /**
     *
     */
    @Override
    public void enter() {
        clearBtn.setEnabled(true);
        int index = setTable.getSelectedRow();
        Object obj = getTableModel().getObject(index);
        if (obj != null && (!deleteBtn.isEnabled())) {
            deleteBtn.setEnabled(true);
        } else if (obj == null && deleteBtn.isEnabled()) {
            deleteBtn.setEnabled(false);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isValidModel() {
        List list = getTableModel().getObjectList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            MasterItem mItem = (MasterItem) iter.next();
            // TODO ハードコードを直す
            if (mItem.getClassCode() == ClaimConst.SYUGI && (mItem.getCode().startsWith("0084") || mItem.getCode().startsWith("84"))) {
                return true;
            }
        }
        return isNumberOk();
    }

    /**
     *
     * @param test
     * @return
     */
    private boolean isNumber(String test) {
        boolean result = true;
        try {
            Float num = Float.parseFloat(test);
            if (num < 0F || num == 0F) {
                result = false;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * 
     * @return
     */
    private boolean isNumberOk() {
        boolean result = true;
        List list = getTableModel().getObjectList();
        for (Iterator iter = list.iterator(); iter.hasNext();) // テーブルをイテレートする
        {
            MasterItem mItem = (MasterItem) iter.next();            // マスターアイテムを取り出す
            if (mItem.getClassCode() == ClaimConst.SYUGI) // 手技の場合
            {
                // null "" ok
                if (mItem.getNumber() == null || mItem.getNumber().equals("")) {
                    continue;
                } else if (!isNumber(mItem.getNumber())) {
                    result = false;
                    break;
                }
            } else {
                // 医薬品及び器材の場合は数量をチェックする
                if (!isNumber(mItem.getNumber())) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}
