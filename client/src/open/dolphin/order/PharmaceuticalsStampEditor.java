package open.dolphin.order;

import java.awt.BorderLayout;
import java.util.EnumSet;

import javax.swing.*;

import open.dolphin.client.GUIConst;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 処方スタンプエディタ　MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class PharmaceuticalsStampEditor extends StampModelEditor {

    private static final String MEDICINE_TABLETITLE_BORDER = "処方セット";
    private static final String EDITOR_NAME = "処方";
    private PharmaceuticalsTablePanel editorPanel;//TODO 共通インターフェイス　IItemTablePanelから継承したい
    private MasterSetPanel masterPanel;

    /** Creates new PharmaceuticalsStampEditor */
    public PharmaceuticalsStampEditor() {
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {

        setTitle(EDITOR_NAME);

        // Medicine table
        editorPanel = new PharmaceuticalsTablePanel(this);
        editorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), MEDICINE_TABLETITLE_BORDER));


        // 処方で使用するマスタを指定し、マスタセットパネルを生成する
        EnumSet<ClaimConst.MasterSet> set = EnumSet.of(ClaimConst.MasterSet.MEDICAL_SUPPLY, ClaimConst.MasterSet.ADMINISTRATION, ClaimConst.MasterSet.INJECTION_MEDICINE, ClaimConst.MasterSet.TOOL_MATERIAL);

        masterPanel = new MasterSetPanel(set);
        masterPanel.startMedicine(editorPanel);
        //    editorPanel.setParent(this);

        // 上にスタンプのセットパネル、下にマスタのセットパネルを配置する
        // 全てのスタンプエディタに共通
        JPanel top = new JPanel(new BorderLayout());
        top.add(editorPanel, BorderLayout.CENTER);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(top);
        add(masterPanel);
        setPreferredSize(GUIConst.DEFAULT_STAMP_EDITOR_SIZE);
    }

    /**
     * 作成したスタンプを返す。
     * @return 作成したスタンプ
     */
    @Override
    public Object getValue() {
        return editorPanel.getValue();
    }

    /**
     * 編集するスタンプを設定する。
     * @param val 編集するスタンプ
     */
    @Override
    public void setValue(Object val) {
     //   System.err.println("setValue");
        editorPanel.setValue(val);
   //    System.err.println("setValue1");
    }

    /**
     * プログラムを終了する。
     */
    @Override
    public void dispose() {
        masterPanel.stopMedicine(editorPanel);
    }
}
