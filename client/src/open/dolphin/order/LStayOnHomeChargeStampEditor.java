package open.dolphin.order;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 在宅スタンプエディタ MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class LStayOnHomeChargeStampEditor extends StampModelEditor {

    private static final long serialVersionUID = -6666962160916099255L;
    //   private ItemTablePanel testTable;
    private MasterSetPanel masterPanel;

    /** 
     * Creates new InjectionStampEditor 
     * @param context
     * @param masterPanel
     */
    public LStayOnHomeChargeStampEditor(IStampEditorDialog context, MasterSetPanel masterPanel) {
        setContext(context);
        this.masterPanel = masterPanel;
        initCustomComponents();
    }

    /**
     *
     */
    @Override
    public void start() {
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.STAY_ON_HOME;
        masterPanel.setSearchClass(spec.getSearchCode());
        masterPanel.startTest(testTable);  // 2003-04-17
    }

    /**
     *
     */
    private void initCustomComponents() {

        // 指導在宅のCLAIM 仕様を得る
        ClaimConst.ClaimSpec spec = ClaimConst.ClaimSpec.STAY_ON_HOME;

        // セットテーブルを生成し CLAIM パラメータを設定する
        testTable = new ItemTablePanel(this);
        testTable.setOrderName(spec.getName());

        testTable.setClassCode(spec.getClassCode());

        testTable.setFindClaimClassCode(true);         // 診療行為区分はマスタアイテムから
        testTable.setClassCodeId(ClaimConst.CLASS_CODE_ID);
        testTable.setSubClassCodeId(ClaimConst.SUBCLASS_CODE_ID);

        // タイトルを設定しレイアウトする
        setTitle(spec.getName());
        Border b = BorderFactory.createEtchedBorder();
        ((ItemTablePanel) testTable).setBorder(BorderFactory.createTitledBorder(b, spec.getName()));

        setLayout(new BorderLayout(0, GUIConst.DEFAULT_CMP_V_SPACE));
        add(((ItemTablePanel) testTable), BorderLayout.CENTER);
        //setPreferredSize(GUIConst.DEFAULT_STAMP_EDITOR_SIZE);
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
        // testTable.setModified(updates);
        testTable.setValue(val);
    }

    /**
     *
     */
    @Override
    public void dispose() {
        masterPanel.stopTest(testTable);  // 2003-04-17
    }
}
