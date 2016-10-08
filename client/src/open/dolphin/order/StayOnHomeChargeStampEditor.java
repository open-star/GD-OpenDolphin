package open.dolphin.order;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.util.EnumSet;
import open.dolphin.client.GUIConst;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 在宅スタンプエディタ　MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class StayOnHomeChargeStampEditor extends StampModelEditor {

    private static final long serialVersionUID = -6666962160916099255L;
    //   private ItemTablePanel testTable;
    //   private MasterTabPanel masterPanel;
    private MasterSetPanel masterPanel;

    /** 
     * Creates new InjectionStampEditor 
     */
    public StayOnHomeChargeStampEditor() {
    }

    /**
     *
     */
    @Override
    public void start() {

        // 指導在宅のCLAIM 仕様を得る
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.STAY_ON_HOME;

        // セットテーブルを生成し CLAIM パラメータを設定する
        testTable = new ItemTablePanel(this);
        testTable.setOrderName(spec.getName());

        testTable.setClassCode(spec.getClassCode());

        testTable.setFindClaimClassCode(true);         // 診療行為区分はマスタアイテムから
        testTable.setClassCodeId(ClaimConst.CLASS_CODE_ID);
        testTable.setSubClassCodeId(ClaimConst.SUBCLASS_CODE_ID);

        // 指導在宅のマスタセットを生成する
        EnumSet<ClaimConst.MasterSet> set = EnumSet.of(
                ClaimConst.MasterSet.TREATMENT,
                ClaimConst.MasterSet.MEDICAL_SUPPLY,
                ClaimConst.MasterSet.INJECTION_MEDICINE,
                ClaimConst.MasterSet.TOOL_MATERIAL);
        // マスタパネルを生成し、診療行為の検索対象コード範囲を設定する
        masterPanel = new MasterSetPanel(set);
        masterPanel.setSearchClass(spec.getSearchCode());
        masterPanel.startTest(testTable);  // 2003-04-17

        // タイトルを設定しレイアウトする
        setTitle(spec.getName());
        Border b = BorderFactory.createEtchedBorder();
        ((ItemTablePanel) testTable).setBorder(BorderFactory.createTitledBorder(b, spec.getName()));

        setLayout(new BorderLayout(0, GUIConst.DEFAULT_CMP_V_SPACE));
        add(((ItemTablePanel) testTable), BorderLayout.NORTH);
        add(masterPanel, BorderLayout.CENTER);
        setPreferredSize(GUIConst.DEFAULT_STAMP_EDITOR_SIZE);
    }

    /**
     *
     * @return
     */
    @Override
    public Object getValue() {
        return testTable.getValue();
    }

    /**
     * 
     * @param val
     */
    @Override
    public void setValue(Object val) {
        testTable.setValue(val);
    }

    /**
     *
     */
    @Override
    public void dispose() {
        //masterPanel.stopCharge(testTable);
        masterPanel.stopTest(testTable);  // 2003-04-17
    }
}
