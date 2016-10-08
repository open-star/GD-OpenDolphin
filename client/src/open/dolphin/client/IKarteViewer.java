/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client;

import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import open.dolphin.infomodel.DocumentModel;

/**
 *
 * @author
 */
public interface IKarteViewer extends Comparable, IChartDocument {

    /**
     *
     * @param selected
     */
    public void setSelected(boolean selected);

    /**
     * 
     * @return
     */
    public String getDocType();

    /**
     *
     * @param b
     */
    public void setAvoidEnter(boolean b);

    /**
     *
     * @return
     */
    public int getActualHeight();

    /**
     *
     * @return
     */
    public DocumentModel getModel();

    /**
     *
     * @param model
     */
    public void setModel(DocumentModel model);

    /**
     *
     * @param ml
     */
    public void addMouseListener(MouseListener ml);

    /**
     *
     * @param format
     */
    public void printPanel2(final PageFormat format);

    /**
     *
     * @param format
     * @param copies
     * @param useDialog
     */
    public void printPanel2(final PageFormat format, final int copies, final boolean useDialog);
}
