package open.dolphin.order;

import java.awt.BorderLayout;
import java.util.EnumSet;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import open.dolphin.client.GUIConst;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * その他スタンプエディタ　MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class OtherStampEditor extends StampModelEditor {

    private static final long serialVersionUID = -1157611315239742056L;
    private MasterSetPanel masterPanel;

    /** 
     * Creates new InjectionStampEditor 
     */
    public OtherStampEditor() {
    }

    /**
     *
     */
    @Override
    public void start() {

        // その他のCLAIM 仕様を得る
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.OTHER;

        // セットテーブルを生成し CLAIM パラメータを設定する
        testTable = new ItemTablePanel(this);
        testTable.setOrderName(spec.getName());
        testTable.setClassCode(spec.getClassCode());
        testTable.setClassCodeId(ClaimConst.CLASS_CODE_ID);
        testTable.setSubClassCodeId(ClaimConst.SUBCLASS_CODE_ID);

        // その他のマスタセットを生成する
        EnumSet<ClaimConst.MasterSet> set = EnumSet.of(
                ClaimConst.MasterSet.TREATMENT,
                ClaimConst.MasterSet.MEDICAL_SUPPLY,
                ClaimConst.MasterSet.INJECTION_MEDICINE,
                ClaimConst.MasterSet.TOOL_MATERIAL);
        // マスタパネルを生成し、診療行為の検索対象コード範囲を設定する
        masterPanel = new MasterSetPanel(set);
        masterPanel.setSearchClass(spec.getSearchCode());
        masterPanel.startTest(testTable);

        // タイトルを設定しレイアウトする
        setTitle(spec.getName());
        Border b = BorderFactory.createEtchedBorder();
        ((ItemTablePanel)testTable).setBorder(BorderFactory.createTitledBorder(b, spec.getName()));

        setLayout(new BorderLayout(0, GUIConst.DEFAULT_CMP_V_SPACE));
        add(((ItemTablePanel)testTable), BorderLayout.NORTH);
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
        masterPanel.stopTest(testTable);
    }
}
