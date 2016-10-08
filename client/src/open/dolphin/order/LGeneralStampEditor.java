package open.dolphin.order;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 汎用スタンプエディタ  MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class LGeneralStampEditor extends StampModelEditor {

    private static final long serialVersionUID = -8582630871058554554L;
    private MasterSetPanel masterPanel;

    /**
     * Creates new InjectionStampEditor
     * @param context
     * @param masterPanel
     */
    public LGeneralStampEditor(IStampEditorDialog context, MasterSetPanel masterPanel) {
        setContext(context);
        this.masterPanel = masterPanel;
        initCustomComponents();
    }

    /**
     *
     */
    @Override
    public void start() {
        masterPanel.setSearchClass(null); // なし
        masterPanel.startGeneral(testTable);
    }

    /**
     *
     */
    private void initCustomComponents() {

        // 汎用のCLAIM 仕様を得る
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.GENERAL;

        // セットテーブルを生成し CLAIM パラメータを設定する
        testTable = new ItemTablePanel(this);
        testTable.setOrderName(spec.getName());
        testTable.setFindClaimClassCode(true);         // 診療行為区分はマスタアイテムから
        testTable.setClassCodeId(ClaimConst.CLASS_CODE_ID);
        testTable.setSubClassCodeId(ClaimConst.SUBCLASS_CODE_ID);

        // タイトルを設定しレイアウトする
        setTitle(spec.getName());
        Border b = BorderFactory.createEtchedBorder();
        ((ItemTablePanel) testTable).setBorder(BorderFactory.createTitledBorder(b, spec.getName()));

        setLayout(new BorderLayout(0, GUIConst.DEFAULT_CMP_V_SPACE));
        add(((ItemTablePanel) testTable), BorderLayout.CENTER);
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
        masterPanel.stopGeneral(testTable);
    }
}
