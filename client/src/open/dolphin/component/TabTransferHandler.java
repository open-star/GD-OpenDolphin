/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.component;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.awt.image.BufferedImage;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

import open.dolphin.log.LogWriter;

/**
 *
 * @author
 */
public class TabTransferHandler extends TransferHandler {

    private final DataFlavor localObjectFlavor;

    /**
     *
     */
    public TabTransferHandler() {
        localObjectFlavor = new ActivationDataFlavor(DnDTabbedPane.class, DataFlavor.javaJVMLocalObjectMimeType, "DnDTabbedPane");
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        return new DataHandler(c, localObjectFlavor.getMimeType());
    }

    /**
     *
     * @param support
     * @return
     */
    private Component getTarget(TransferHandler.TransferSupport support) {
        return support.getComponent();
    }

    /**
     *
     * @param support
     * @return
     */
    private Component getSource(TransferHandler.TransferSupport support) {
        Component result = null;
        try {
            result = (Component) support.getTransferable().getTransferData(localObjectFlavor);
        } catch (UnsupportedFlavorException e) {
            LogWriter.error(getClass(), e);
        } catch (Exception ex) {
            LogWriter.error(getClass(), ex);
        }
        return result;
    }

    /**
     *
     * @param support
     * @return
     */
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        boolean isDropable = false;
        Object targetObject = getTarget(support);
        Object sourceObject = getSource(support);

        DnDTabbedPane target = null;
        DnDTabbedPane source = null;

        if (targetObject instanceof DnDTabbedPane) {
            target = (DnDTabbedPane) targetObject;
        }

        if (sourceObject instanceof DnDTabbedPane) {
            source = (DnDTabbedPane) sourceObject;
        }

        if (source == null) {
            return false;
        }

        if (source.getRootPane() != target.getRootPane()) {
            return false;
        }

        if (!support.isDrop() || !support.isDataFlavorSupported(localObjectFlavor)) {
            return false;
        }

        support.setDropAction(TransferHandler.MOVE);
        TransferHandler.DropLocation tdl = support.getDropLocation();
        Point pt = tdl.getDropPoint();

        target.autoScrollTest(pt);

        DnDTabbedPane.DropLocation dl = (DnDTabbedPane.DropLocation) target.dropLocationForPoint(pt);
        int tabindex = dl.getIndex();

        if (target == source) {
            isDropable = target.getTabAreaBounds().contains(pt) && tabindex >= 0 && tabindex != target.dragTabIndex && tabindex != target.dragTabIndex + 1;
        } else {
            int srcIdx = source.dragTabIndex;// MEMO;Unused?
            isDropable = target.getTabAreaBounds().contains(pt) && tabindex >= 0;
        }

        Component c = target.getRootPane().getGlassPane();
        if (c instanceof GhostGlassPane) {
            GhostGlassPane gp = (GhostGlassPane) c;
            gp.setCursor(isDropable ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        }

        if (isDropable) {
            support.setShowDropLocation(true);
            dl.setDropable(true);
            target.setDropLocation(dl, null, true);
            return true;
        } else {
            support.setShowDropLocation(false);
            dl.setDropable(false);
            target.setDropLocation(dl, null, false);
            return false;
        }
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    public int getSourceActions(JComponent c) {
        DnDTabbedPane src = (DnDTabbedPane) c;
        if (glassPane == null) {
            glassPane = new GhostGlassPane(src);
        }

        Rectangle rect = src.getBoundsAt(src.dragTabIndex);
        BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        c.paint(g);
        if (rect.x < 0) {
            rect.translate(-rect.x, 0);
        }
        if (rect.y < 0) {
            rect.translate(0, -rect.y);
        }
        if (rect.x + rect.width > image.getWidth()) {
            rect.width = image.getWidth() - rect.x;
        }
        if (rect.y + rect.height > image.getHeight()) {
            rect.height = image.getHeight() - rect.y;
        }
        BufferedImage img = image.getSubimage(rect.x, rect.y, rect.width, rect.height);
        glassPane.setImage(img);
        //setDragImage(img); //java 1.7.0-ea-b84
        c.getRootPane().setGlassPane(glassPane);
        glassPane.setVisible(true);
        return TransferHandler.MOVE;
    }

    /**
     *
     * @param support
     * @return
     */
    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        DnDTabbedPane target = (DnDTabbedPane) getTarget(support);
        DnDTabbedPane source = (DnDTabbedPane) getSource(support);

        DnDTabbedPane.DropLocation dl = target.getDropLocation();

        int index = dl.getIndex(); //boolean insert = dl.isInsert();
        if (target == source) {
            source.convertTab(source.dragTabIndex, index); //getTargetTabIndex(e.getLocation()));
        } else {
            source.exportTab(source.dragTabIndex, target, index);
        }
        return true;
    }

    /**
     *
     * @param c
     * @param data
     * @param action
     */
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
        //     System.out.println("exportDone");
        DnDTabbedPane src = (DnDTabbedPane) c;
        c.getRootPane().getGlassPane().setVisible(false);
        src.setDropLocation(null, null, false);
    }
    private GhostGlassPane glassPane;
}
