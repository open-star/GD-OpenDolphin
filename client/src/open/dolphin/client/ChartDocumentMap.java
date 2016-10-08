/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client;

import java.util.HashMap;
import open.dolphin.infomodel.GenericAdapter;

/**
 *
 * @author
 */
public class ChartDocumentMap extends HashMap<String, IChartDocument> {

    /**
     *　typeで示されるようなドキュメントは存在するか
     * @param type
     * @return　typeで示されるようなドキュメントがあれば真
     */
    public boolean isExist(IChartDocument.TYPE type) {
        for (IChartDocument document : values()) {
            if (document.getType() == type) {
                return true;
            }
        }
        return false;
    }

    /**
     *　typeで示されるようなドキュメントを返す
     * @param type
     * @return　typeで示されるようなドキュメント
     */
    public IChartDocument getAsType(IChartDocument.TYPE type) {
        for (IChartDocument document : values()) {
            if (document.getType() == type) {
                return document;
            }
        }
        return null;
    }

    /**
     *　ダーティ
     * @return
     */
    public boolean isDirty() {
        for (IChartDocument component : values()) {
            if (component instanceof IChartDocument) {
                if (((IChartDocument) component).isDirty()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param adapter　アダプタ
     * @return　
     * @throws Exception
     */
    public boolean enumerate(GenericAdapter<IChartDocument, Object> adapter) throws Exception {
        for (IChartDocument document : values()) {
            if (adapter.onResult(document, null)) {
                return true;
            }
        }
        return false;
    }

    /**
     *　パネルを追加
     * @param panel　追加すべきパネル
     * @return　追加したパネル
     */
    public IChartDocument addPanel(IChartDocument panel) {
        panel.start();
        put(panel.getTitle(), panel);
        return panel;
    }
}
