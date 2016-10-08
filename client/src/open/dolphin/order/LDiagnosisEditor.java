package open.dolphin.order;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import open.dolphin.client.GUIConst;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.editor.diagnosis.DiagnosisTablePanel;

import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 検査スタンプエディタ  MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class LDiagnosisEditor extends StampModelEditor {

    private static final String EDITOR_NAME = "傷病名";
    /** 傷病名編集テーブル */
    private DiagnosisTablePanel diagnosisTable;//TODO 共通インターフェイス　IItemTablePanelから継承したい
    /** マスターセットパネル */
    private MasterSetPanel masterPanel;

    /** 
     * Creates new DiagnosisEditor 
     * @param context
     * @param masterPanel
     */
    public LDiagnosisEditor(IStampEditorDialog context, MasterSetPanel masterPanel) {

        setContext(context);
        this.masterPanel = masterPanel;
        initCustomComponents();
    }

    /**
     * エディタを開始する。
     */
    @Override
    public void start() {
        masterPanel.startDiagnosis(diagnosisTable);
    }

    /**
     * Componentを初期化する。
     */
    private void initCustomComponents() {

        setTitle(EDITOR_NAME);

        // 傷病名編集テーブル
        // マスターセットパネルを生成しレイアウトする
        diagnosisTable = new DiagnosisTablePanel(this);
        Border b = BorderFactory.createEtchedBorder();
        diagnosisTable.setBorder(BorderFactory.createTitledBorder(b, EDITOR_NAME));

        setLayout(new BorderLayout(0, GUIConst.DEFAULT_CMP_V_SPACE));

        //    add(new JButton("a"), BorderLayout.CENTER);

        add(diagnosisTable, BorderLayout.CENTER);
    }

    /**
     * 編集した傷病名を返す。
     * @return RegisteredDiagnosisModel
     */
    @Override
    public Object getValue() {
        return diagnosisTable.getValue();
    }

    /**
     * 編集する傷病名を設定する。
     * @param val RegisteredDiagnosisModel
     */
    @Override
    public void setValue(Object val) {
        //     diagnosisTable.setModified(updates);
        diagnosisTable.setValue((Object[]) val);
    }

    /**
     * リソースを解放する。
     */
    @Override
    public void dispose() {
        masterPanel.stopDiagnosis(diagnosisTable);
    }

    /**
     *
     */
    @Override
    public void clear() {
        diagnosisTable.clear();
    }
}
