package open.dolphin.order;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 画像診断スタンプエディタ MEMO:画面
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class LRadiologyStampEditor extends StampModelEditor {

    private static final long serialVersionUID = 2467212598346800512L;
    //  private RadItemTablePanel testTable;
    private MasterSetPanel masterPanel;

    /**
     * Creates new InjectionStampEditor
     * @param context 
     * @param masterPanel
     */
    public LRadiologyStampEditor(IStampEditorDialog context, MasterSetPanel masterPanel) {
        setContext(context);
        this.masterPanel = masterPanel;
        initCustomComponents();
    }

    /**
     *
     */
    @Override
    public void start() {
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.RADIOLOGY;
        masterPanel.setSearchClass(spec.getSearchCode());
        masterPanel.setRadLocationEnabled(true);
        masterPanel.startTest(testTable);
    }

    /**
     *
     */
    private void initCustomComponents() {

        // 画像診断のCLAIM 仕様を得る
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.RADIOLOGY;

        // セットテーブルを生成し CLAIM パラメータを設定する
        testTable = new RadItemTablePanel(this);
        testTable.setOrderName(spec.getName());
        testTable.setClassCode(spec.getClassCode());
        testTable.setClassCodeId(ClaimConst.CLASS_CODE_ID);
        testTable.setSubClassCodeId(ClaimConst.SUBCLASS_CODE_ID);

        // タイトルを設定しレイアウトする
        setTitle(spec.getName());
        Border b = BorderFactory.createEtchedBorder();
        ((RadItemTablePanel) testTable).setBorder(BorderFactory.createTitledBorder(b, spec.getName()));

        this.setLayout(new BorderLayout());
        this.add((RadItemTablePanel) testTable, BorderLayout.CENTER);
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

    /**
     *
     */
    @Override
    public void clear() {
        testTable.clear();
    }
}
