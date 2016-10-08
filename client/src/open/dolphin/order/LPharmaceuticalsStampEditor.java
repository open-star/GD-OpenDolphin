package open.dolphin.order;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.editor.stamp.StampModelEditor;

/**
 * 処方スタンプエディタ  MEMO:画面
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class LPharmaceuticalsStampEditor extends StampModelEditor {

    private static final long serialVersionUID = 3721140728191931803L;
    private static final String MEDICINE_TABLETITLE_BORDER = "処方セット";
    private static final String EDITOR_NAME = "処方";
    private PharmaceuticalsTablePanel editorPanel;//TODO 共通インターフェイス　IItemTablePanelから継承したい
    private MasterSetPanel masterPanel;

    /** Creates new MedStampEditor2
     * @param context
     * @param masterPanel
     */
    public LPharmaceuticalsStampEditor(IStampEditorDialog context, MasterSetPanel masterPanel) {
        setContext(context);
        this.masterPanel = masterPanel;
        initCustomComponents();
    }

    /**
     *
     */
    @Override
    public void start() {
        masterPanel.startMedicine(editorPanel);
    }

    /**
     *
     */
    private void initCustomComponents() {

        setTitle(EDITOR_NAME);

        // Medicine table
        editorPanel = new PharmaceuticalsTablePanel(this);
        editorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), MEDICINE_TABLETITLE_BORDER));

        // Connects
        //    editorPanel.setParent(this);

        this.setLayout(new BorderLayout());
        this.add(editorPanel, BorderLayout.CENTER);
    }

    /**
     *　パネルクリア
     */
    @Override
    public void clear() {
        editorPanel.clear();
    }

    /**
     *
     * @return　パネルの値
     */
    @Override
    public Object getValue() {
        return editorPanel.getValue();
    }

    /**
     *
     * @param val　パネルにセットする値
     */
    @Override
    public void setValue(Object val) {
        //      editorPanel.setModified(updates);
        editorPanel.setValue(val);
    }

    /**
     *
     */
    @Override
    public void dispose() {
        masterPanel.stopMedicine(editorPanel);
    }
}
