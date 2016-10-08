/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.order;

import open.dolphin.infomodel.MasterItem;
import open.dolphin.infomodel.SinryoCode;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author taka
 */
public class CommentHelper {

    // <TODO> 80バイトは日レセ仕様。
    // <TODO> 40文字でいいのかも検討。UTF8 だと1文字3バイトも考慮。
    private final int NAME_LENGTH = 40;

    /**
     *
     * @param item
     * @return
     */
    public boolean confirm(MasterItem item) {
        String original_name = item.getName();
        String modified_name = original_name;
        while (true) {
            if (SinryoCode.isNoFormatComment(item.getCode())) {
                modified_name = editNoFormatComment(modified_name);
                if (modified_name != null && !modified_name.trim().isEmpty()) {
                    if (isValidName(modified_name)) {
                        item.setName(modified_name.trim());
                        return true;
                    } else {
                        showErrorMessage();
                    }
                } else {
                    return false;
                }
            } else if (SinryoCode.isPrefixComment(item.getCode())) {
                modified_name = editPrefixComment(modified_name);
                if (modified_name != null && !modified_name.trim().isEmpty()) {
                    if (isValidName(modified_name)) {
                        item.setName(modified_name.trim());
                        return true;
                    } else {
                        showErrorMessage();
                    }
                } else {
                    return false;
                }
            } else if (SinryoCode.isComplexComment(item.getCode())) {
                String msg = makeSstKijunCdFormat(item.getSstKijunCdSet(), "0");
                String value = editComplexComment(modified_name +System.getProperty("line.separator") + "フォーマット：" + msg, item.getNumber() != null ? item.getNumber() : "");
                // <TODO> value の validation 入れる
                if (value != null && !value.trim().isEmpty()) {
                    item.setNumber(value.trim());
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     *
     * @param s
     * @return
     */
    private String editNoFormatComment(String s) {
        return JOptionPane.showInputDialog(null, "編集してください（先頭から４０文字までが入力されます。)"+ System.getProperty("line.separator"), s);
    }

    /**
     *
     * @param s
     * @return
     */
    private String editPrefixComment(String s) {
        String sep = "：";
        String format = s.replaceFirst(sep + ".*", "") + sep;
        String value = s.replaceFirst(".*" + sep, "");
        StringBuffer sb = new StringBuffer();
        sb.append("編集してください（フォーマット文併せて最大40文字）"+ System.getProperty("line.separator"));
        sb.append(format);
        String rtn = JOptionPane.showInputDialog(null, sb.toString(), value);
        return rtn != null ? format + rtn : null;
    }

    /**
     *
     * @param format
     * @param value
     * @return
     */
    private String editComplexComment(String format, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append("編集してください。"+ System.getProperty("line.separator"));
        sb.append(format);
        String rtn = JOptionPane.showInputDialog(null, sb.toString(), value != null ? value : "");
        return rtn == null ? value : rtn;
    }

    /**
     *
     * @param ary
     * @param s
     * @return
     */
    private String makeSstKijunCdFormat(List ary, String s) {
        StringBuffer sb = new StringBuffer();
        if (ary != null) {
            Iterator<String> it = ary.iterator();
            while (it.hasNext()) {
                String pos = it.next();
                if (pos == null || pos.trim().isEmpty() || pos.trim().equals("0")) {
                    break;
                }
                if (!it.hasNext()) {
                    break;
                }
                String num = it.next();
                if (num == null || num.trim().isEmpty() || pos.trim().equals("0")) {
                    break;
                }
                int numi = Integer.parseInt(num);
                if (!sb.toString().trim().isEmpty()) {
                    sb.append("-");
                }
                for (int i = 0; i < numi; i++) {
                    sb.append(s);
                }
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param name
     * @return
     */
    private boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        if (name.trim().isEmpty()) {
            return false;
        }
        if (NAME_LENGTH < name.length()) {
            return false;
        }
        return true;
    }

    /**
     * 
     */
    private void showErrorMessage() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(NAME_LENGTH));
        sb.append("文字以下にしてください。");
        JOptionPane.showMessageDialog(null, sb.toString());
    }
}
