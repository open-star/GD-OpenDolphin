package open.dolphin.order;

import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

import open.dolphin.infomodel.MasterItem;
import open.dolphin.infomodel.SinryoCode;

/**
 * ValidState
 *
 * @author Minagawa,Kazushi
 */
public class MedHasItemState extends AbstractMedTableState {

    /**
     *
     * @param medTable
     * @param deleteBtn
     * @param clearBtn
     * @param stampNameField
     * @param stateLabel
     */
    public MedHasItemState(JTable medTable, JButton deleteBtn, JButton clearBtn,
            JTextField stampNameField, JLabel stateLabel) {
        super(medTable, deleteBtn, clearBtn, stampNameField, stateLabel);
    }

    /**
     *　
     */
    @Override
    public void enter() {

        clearBtn.setEnabled(true);
        int index = medTable.getSelectedRow();
        Object obj = getTableModel().getObject(index);
        if (obj != null && (!deleteBtn.isEnabled())) {
            deleteBtn.setEnabled(true);
        } else if (obj == null && deleteBtn.isEnabled()) {
            deleteBtn.setEnabled(false);
        }

        if (!hasMedicine()) {
            stateLabel.setText("医薬品を入力してください。");
            return;
        }

        if (!hasAdmin()) {
            stateLabel.setText("用法を入力してください。");
            return;
        }

        if (!isNumberOk()) {
            stateLabel.setText("数量が正しくありません。");
            return;
        }

        stateLabel.setText("カルテに展開できます。");
    }

    /**
     *
     * @return　モデルが正しいか
     */
    @Override
    public boolean isValidModel() {
        boolean result = false;
        if (isAllGaiyo()) {
            result = true;
        } else {
            result = (hasMedicine() && hasAdmin() && isNumberOk());
        }
        return result;
    }

    /**
     *
     * @return　外用
     */
    private boolean isAllGaiyo() {
        List list = getTableModel().getObjectList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            MasterItem mItem = (MasterItem) iter.next();
            String yakuzai_kubun = mItem.getYkzKbn();
            if (yakuzai_kubun != null) {
                if (!yakuzai_kubun.equals(ClaimConst.YKZ_KBN_GAIYO)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * @return　
     */
    private boolean hasMedicine() {
        boolean result = false;
        List list = getTableModel().getObjectList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            MasterItem mItem = (MasterItem) iter.next();
            if (mItem.getClassCode() == ClaimConst.YAKUZAI) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     *
     * @return
     */
    private boolean hasAdmin() {

        boolean adminOk = false;
        List list = getTableModel().getObjectList();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            MasterItem mItem = (MasterItem) iter.next();
            if (mItem.getClassCode() == ClaimConst.ADMIN) {
                adminOk = true;
                break;
            }
        }

        return adminOk;
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
     * @return　ナンバーが正しいか
     */
    private boolean isNumberOk() {

        boolean numberOk = true;
        List list = getTableModel().getObjectList();

        // テーブルをイテレートする
        for (Iterator iter = list.iterator(); iter.hasNext();) {

            // マスターアイテムを取り出す
            MasterItem mItem = (MasterItem) iter.next();

            if (SinryoCode.isComment(mItem.getCode())) {
                // コメントコードの時は number 無しでも OK
                // <TODO> ここにもコメントコード用の validator 入れる？
                continue;
            } else if (mItem.getClassCode() == ClaimConst.YAKUZAI || mItem.getClassCode() == ClaimConst.ZAIRYO) {
                // 器材または医薬品の場合、数量を調べる

                if (!isNumber(mItem.getNumber().trim())) {
                    numberOk = false;
                    break;
                }

            } else if (mItem.getClassCode() == ClaimConst.ADMIN) {
                // バンドル数を調べる
                if (!isNumber(mItem.getBundleNumber().trim())) {
                    numberOk = false;
                    break;
                }

            } else if (mItem.getClassCode() == ClaimConst.SYUGI) {

                // 手技の場合 null "" 可
                if (mItem.getNumber() == null || mItem.getNumber().equals("")) {
                    continue;
                }

                if (!isNumber(mItem.getNumber().trim())) {
                    numberOk = false;
                    break;
                }
            }
        }
        return numberOk;
    }
}
