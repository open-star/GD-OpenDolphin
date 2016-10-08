package open.dolphin.order;

import java.util.EnumSet;
import java.awt.BorderLayout;

import javax.swing.border.Border;
import javax.swing.BorderFactory;

import open.dolphin.client.GUIConst;
import open.dolphin.client.editor.diagnosis.DiagnosisTablePanel;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 傷病名エディタ MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class DiagnosisEditor extends StampModelEditor {

    private static final String EDITOR_NAME = "傷病名"; // エディタ名
    private DiagnosisTablePanel diagnosisTable;  // 傷病名編集テーブル//TODO 共通インターフェイス　IItemTablePanelから継承したい
    private MasterSetPanel masterPanel;  // マスターセットパネル

    /** 
     * Creates new DiagnosisEditor
     */
    public DiagnosisEditor() {
        initCustomComponents();
    }

    /**
     * プログラムを開始する。
     */
    @Override
    public void start() {

        setTitle(EDITOR_NAME);

        // 傷病名編集テーブルを生成する
        diagnosisTable = new DiagnosisTablePanel(this);
        Border b = BorderFactory.createEtchedBorder();
        diagnosisTable.setBorder(BorderFactory.createTitledBorder(b, EDITOR_NAME));

        // 傷病名で使用するマスタのセットを生成する
        EnumSet<ClaimConst.MasterSet> set = EnumSet.of(ClaimConst.MasterSet.DIAGNOSIS);

        // マスターセットを生成する
        masterPanel = new MasterSetPanel(set);
        masterPanel.startDiagnosis(diagnosisTable);

        // 全体をレイアウトする
        setLayout(new BorderLayout(0, GUIConst.DEFAULT_CMP_V_SPACE));
        add(diagnosisTable, BorderLayout.NORTH);
        add(masterPanel, BorderLayout.CENTER);
        setPreferredSize(GUIConst.DEFAULT_STAMP_EDITOR_SIZE);
    }

    /**
     *
     */
    private void initCustomComponents() {
    }

    /**
     * 編集した傷病名を返す。
     * @return 編集した RegisteredDiagnosisModel
     */
    @Override
    public Object getValue() {
        return diagnosisTable.getValue();
    }

    /**
     * 編集する傷病名を設定する。
     * @param val 編集する RegisteredDiagnosisModel
     */
    @Override
    public void setValue(Object val) {
        diagnosisTable.setValue((Object[]) val);
    }

    /**
     * リソースを解放する。
     */
    @Override
    public void dispose() {
        masterPanel.stopDiagnosis(diagnosisTable);
    }
}
