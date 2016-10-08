package open.dolphin.client.caremapdocument;

import java.awt.Cursor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import javax.swing.Icon;
import javax.swing.JLabel;

import open.dolphin.infomodel.AppointmentModel;

/**
 * AppointLabel MEMO:Component
 * 
 * @author  Kauzshi Minagawa, Digital Globe, Inc.
 */
public class AppointLabel extends JLabel implements DragGestureListener, DragSourceListener {

    private static final long serialVersionUID = 2843710174202998473L;
    private DragSource dragSource;

    /** Creates a new instance of AppointLabel
     * @param text
     * @param icon 
     * @param align
     */
    public AppointLabel(String text, Icon icon, int align) {

        super(text, icon, align);

        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
    }

    /**
     *
     * @param event
     */
    @Override
    public void dragGestureRecognized(DragGestureEvent event) {

        AppointmentModel appo = new AppointmentModel();
        appo.setName(this.getText());
        Transferable t = new AppointEntryTransferable(appo);
        Cursor cursor = DragSource.DefaultCopyDrop;

        // Starts the drag
        dragSource.startDrag(event, cursor, t, this);
    }

    /**
     *
     * @param event
     */
    @Override
    public void dragDropEnd(DragSourceDropEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void dragEnter(DragSourceDragEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void dragOver(DragSourceDragEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void dragExit(DragSourceEvent event) {
    }

    /**
     *
     * @param event
     */
    @Override
    public void dropActionChanged(DragSourceDragEvent event) {
    }
}
