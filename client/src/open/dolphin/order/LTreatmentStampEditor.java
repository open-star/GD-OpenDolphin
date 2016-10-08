package open.dolphin.order;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 処置スタンプエディタ MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class LTreatmentStampEditor extends StampModelEditor {

    private static final long serialVersionUID = -2173356408762423668L;
    //  private ItemTablePanel testTable;
    private MasterSetPanel masterPanel;

    /**
     * Creates new InjectionStampEditor
     * @param context
     * @param masterPanel
     */
    public LTreatmentStampEditor(IStampEditorDialog context, MasterSetPanel masterPanel) {
        setContext(context);
        this.masterPanel = masterPanel;
        initCustomComponents();
    }

    /**
     * 
     */
    @Override
    public void start() {
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.TREATMENT;
        masterPanel.setSearchClass(spec.getSearchCode());
        masterPanel.startTest(testTable);
    }

    /**
     * プログラムを開始する。
     */
    private void initCustomComponents() {

        // 処置の CLAIM 仕様を得る
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.TREATMENT;

        // セットテーブルを生成し CLAIM パラメータを設定する
        testTable = new ItemTablePanel(this);
        testTable.setOrderName(spec.getName());
        testTable.setClassCode(spec.getClassCode());
        testTable.setClassCodeId(ClaimConst.CLASS_CODE_ID);
        testTable.setSubClassCodeId(ClaimConst.SUBCLASS_CODE_ID);

        // タイトルを設定しレイアウトする
        setTitle(spec.getName());
        Border b = BorderFactory.createEtchedBorder();
        ((ItemTablePanel)testTable).setBorder(BorderFactory.createTitledBorder(b, spec.getName()));

        setLayout(new BorderLayout(0, GUIConst.DEFAULT_CMP_V_SPACE));
        add(((ItemTablePanel)testTable), BorderLayout.CENTER);
        //setPreferredSize(GUIConst.DEFAULT_STAMP_EDITOR_SIZE);
    }

    /**
     * エディタで生成した値(ClaimBundle)を返す。
     */
    @Override
    public Object getValue() {
        return testTable.getValue();
    }

    /**
     * エディタで編集する値(ClaimBundle)を設定する。
     * @param val
     */
    @Override
    public void setValue(Object val) {
        //    testTable.setModified(updates);
        testTable.setValue(val);
    }

    /**
     * エディタを終了する。
     */
    @Override
    public void dispose() {
        masterPanel.stopTest(testTable);
    }
}
