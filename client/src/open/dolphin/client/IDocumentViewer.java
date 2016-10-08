package open.dolphin.client;

import javax.swing.JScrollPane;
import open.dolphin.infomodel.DocInfoModel;

/**
 *
 * @author kazm
 */
public interface IDocumentViewer extends IChartDocument {

    /**
     * 
     */
    public void historyPeriodChanged();

    /**
     *
     * @param docs
     * @param scroller
     */
    public void showDocuments(DocInfoModel[] docs, JScrollPane scroller);
}
