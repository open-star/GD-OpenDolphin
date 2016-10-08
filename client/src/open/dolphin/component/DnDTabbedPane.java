/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

/**
 * 
 * @author
 */
public class DnDTabbedPane extends JTabbedPane {

    private static final int LINEWIDTH = 3;
    private final Rectangle lineRect = new Rectangle();
    /**
     *
     */
    public int dragTabIndex = -1;

    /**
     * 
     */
    public static final class DropLocation extends TransferHandler.DropLocation {

        private final int index;
        private boolean dropable = true;

        /**
         * 
         * @param p
         * @param index
         */
        private DropLocation(Point p, int index) {
            super(p);
            this.index = index;
        }

        /**
         * 
         * @return
         */
        public int getIndex() {
            return index;
        }

        /**
         * 
         * @param flag
         */
        public void setDropable(boolean flag) {
            dropable = flag;
        }

        /**
         * 
         * @return
         */
        public boolean isDropable() {
            return dropable;
        }
    }

    /**
     * 
     * @param actionKey
     */
    private void clickArrowButton(String actionKey) {
        ActionMap map = getActionMap();
        if (map != null) {
            Action action = map.get(actionKey);
            if (action != null && action.isEnabled()) {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null, 0, 0));
            }
        }
    }
    /**
     *
     */
    public static Rectangle rBackward = new Rectangle();
    /**
     *
     */
    public static Rectangle rForward = new Rectangle();
    private static int rwh = 20;
    private static int buttonsize = 30; //MEMO: 30 is magic number of scroll button size

    /**
     * 
     * @param pt
     */
    public void autoScrollTest(Point pt) {
        Rectangle r = getTabAreaBounds();
        int tabPlacement = getTabPlacement();
        if (tabPlacement == TOP || tabPlacement == BOTTOM) {
            rBackward.setBounds(r.x, r.y, rwh, r.height);
            rForward.setBounds(r.x + r.width - rwh - buttonsize, r.y, rwh + buttonsize, r.height);
        } else if (tabPlacement == LEFT || tabPlacement == RIGHT) {
            rBackward.setBounds(r.x, r.y, r.width, rwh);
            rForward.setBounds(r.x, r.y + r.height - rwh - buttonsize, r.width, rwh + buttonsize);
        }
        if (rBackward.contains(pt)) {
            clickArrowButton("scrollTabsBackwardAction");
        } else if (rForward.contains(pt)) {
            clickArrowButton("scrollTabsForwardAction");
        }
    }

    /**
     *
     */
    public DnDTabbedPane() {
        super();
        Handler h = new Handler();
        addMouseListener(h);
        addMouseMotionListener(h);
        addPropertyChangeListener(h);
    }
    private DropMode dropMode = DropMode.INSERT;

    /**
     *
     * @param p
     * @return
     */
    public DropLocation dropLocationForPoint(Point p) {
        DropLocation location = null;
        boolean isTB = getTabPlacement() == JTabbedPane.TOP || getTabPlacement() == JTabbedPane.BOTTOM;
        switch (dropMode) {
            case INSERT:
                Rectangle r;
                for (int i = 0; i < getTabCount(); i++) {
                    r = getBoundsAt(i);
                    if (isTB) {
                        r.translate(-r.width / 2, 0);
                    } else {
                        r.translate(0, -r.height / 2);
                    }
                    if (r.contains(p)) {
                        return new DropLocation(p, i);
                    }
                }
                r = getBoundsAt(getTabCount() - 1);
                if (isTB) {
                    r.translate(r.width / 2, 0);
                } else {
                    r.translate(0, r.height / 2);
                }
                int index = r.contains(p) ? getTabCount() : -1;
                location = new DropLocation(p, index); //, true);
                break;
            case USE_SELECTION:
            case ON:
            case ON_OR_INSERT:
            default:
                assert false : "Unexpected drop mode";
        }
        return location;
    }
    private transient DropLocation dropLocation;

    /**
     * 
     * @return
     */
    public final DropLocation getDropLocation() {
        return dropLocation;
    }

    /**
     * 
     * @param location
     * @param state
     * @param forDrop
     * @return
     */
    public Object setDropLocation(TransferHandler.DropLocation location, Object state, boolean forDrop) {
        Object retVal = null;
        //DropLocation listLocation = (DropLocation)location;
        DropLocation old = dropLocation;
        if (location instanceof DropLocation) {
            dropLocation = (DropLocation) location;
            firePropertyChange("dropLocation", old, dropLocation);
        }
        return retVal;
    }

    /**
     *
     * @param source_index
     * @param target_pane
     * @param target_index
     */
    public void exportTab(int source_index, JTabbedPane target_pane, int target_index) {
        if (target_index < 0) {
            return;
        }

        Component source_component = getComponentAt(source_index);
        if (source_component == target_pane) {
            return; //target==child
        }

        Component source_tab_component = getTabComponentAt(source_index);
        String source_title = getTitleAt(source_index);
        Icon source_icon = getIconAt(source_index);
        String source_tip = getToolTipTextAt(source_index);
        boolean source_is_enabled = isEnabledAt(source_index);

        remove(source_index);

        target_pane.insertTab(source_title, source_icon, source_component, source_tip, target_index);
        target_pane.setEnabledAt(target_index, source_is_enabled);
        target_pane.setTabComponentAt(target_index, source_tab_component);
        target_pane.setSelectedIndex(target_index);
        if (source_tab_component != null && source_tab_component instanceof JComponent) {
            ((JComponent) source_tab_component).scrollRectToVisible(source_tab_component.getBounds());
        }
    }

    /**
     * 
     * @param source_index
     * @param target_index
     */
    public void convertTab(int source_index, int target_index) {
        if (target_index < 0 || source_index == target_index) {
            return;
        }

        Component source_component = getComponentAt(source_index);
        Component source_tab_component = getTabComponentAt(source_index);
        String source_title = getTitleAt(source_index);
        Icon source_icon = getIconAt(source_index);
        String source_tip = getToolTipTextAt(source_index);
        boolean source_is_enabled = isEnabledAt(source_index);
        int distination_index = source_index > target_index ? target_index : target_index - 1;

        remove(source_index);

        insertTab(source_title, source_icon, source_component, source_tip, distination_index);
        setEnabledAt(distination_index, source_is_enabled);
        //When you drag'n'drop a disabled tab, it finishes enabled and selected.
        //pointed out by dlorde
        if (source_is_enabled) {
            setSelectedIndex(distination_index);
        }
        //I have a component in all tabs (jlabel with an X to close the tab) and when i move a tab the component disappear.
        //pointed out by Daniel Dario Morales Salas
        setTabComponentAt(distination_index, source_tab_component);
    }

    /**
     * 
     * @param g
     */
    public void paintDropLine(Graphics g) {
        DropLocation loc = getDropLocation();
        if (loc == null) {
            return; // !loc.isInsert()) return;
        }
        Graphics2D g2 = (Graphics2D) g;
        //g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); //java 1.7.0-ea-b84
        g2.setColor(Color.RED);
        Rectangle r = SwingUtilities.convertRectangle(this, getDropLineRect(loc), getRootPane().getGlassPane());
        g2.fill(r);
    }

    /**
     * 
     * @param loc
     * @return
     */
    public Rectangle getDropLineRect(DnDTabbedPane.DropLocation loc) {
        int index = loc.getIndex();
        if (index < 0) {
            lineRect.setRect(0, 0, 0, 0);
            return lineRect;
        }
        //Point pt = loc.getDropPoint();
        boolean isZero = index == 0;
        Rectangle r = getBoundsAt(isZero ? 0 : index - 1);
        if (getTabPlacement() == JTabbedPane.TOP || getTabPlacement() == JTabbedPane.BOTTOM) {
            lineRect.setRect(r.x - LINEWIDTH / 2 + r.width * (isZero ? 0 : 1), r.y, LINEWIDTH, r.height);
        } else {
            lineRect.setRect(r.x, r.y - LINEWIDTH / 2 + r.height * (isZero ? 0 : 1), r.width, LINEWIDTH);
        }
        return lineRect;
    }

    /**
     * 
     * @return
     */
    public Rectangle getTabAreaBounds() {
        Rectangle tabbedRect = getBounds();
        int xx = tabbedRect.x;
        int yy = tabbedRect.y;
        Component c = getSelectedComponent();
        if (c == null) {
            return tabbedRect;
        }
        Rectangle compRect = getSelectedComponent().getBounds();
        int tabPlacement = getTabPlacement();
        if (tabPlacement == TOP) {
            tabbedRect.height = tabbedRect.height - compRect.height;
        } else if (tabPlacement == BOTTOM) {
            tabbedRect.y = tabbedRect.y + compRect.y + compRect.height;
            tabbedRect.height = tabbedRect.height - compRect.height;
        } else if (tabPlacement == LEFT) {
            tabbedRect.width = tabbedRect.width - compRect.width;
        } else if (tabPlacement == RIGHT) {
            tabbedRect.x = tabbedRect.x + compRect.x + compRect.width;
            tabbedRect.width = tabbedRect.width - compRect.width;
        }
        tabbedRect.translate(-xx, -yy);
        //tabbedRect.grow(2, 2);
        return tabbedRect;
    }

    /**
     * 　MEMO:リスナー
     */
    private class Handler implements MouseInputListener, PropertyChangeListener { 

        /**
         *
         * @param loc
         */
        private void repaintDropLocation(DropLocation loc) {
            Component c = getRootPane().getGlassPane();
            if (c instanceof GhostGlassPane) {
                GhostGlassPane glassPane = (GhostGlassPane) c;
                glassPane.setTargetTabbedPane(DnDTabbedPane.this);
                glassPane.repaint();
            }
        }

        /**
         * PropertyChangeListener
         * @param e
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if ("dropLocation".equals(propertyName)) { //if("dropLocation" == propertyName) {
                repaintDropLocation(getDropLocation());
            }
        }

        /**
         * MouseListener
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
            DnDTabbedPane src = (DnDTabbedPane) e.getSource();
            if (src.getTabCount() <= 1) {
                startPt = null;
                return;
            }
            Point tabPt = e.getPoint(); //e.getDragOrigin();
            int idx = src.indexAtLocation(tabPt.x, tabPt.y);
            //disabled tab, null component problem.
            //pointed out by daryl. NullPointerException: i.e. addTab("Tab",null)
            startPt = (idx < 0 || !src.isEnabledAt(idx) || src.getComponentAt(idx) == null) ? null : tabPt;
        }
        private Point startPt;
        int gestureMotionThreshold = DragSource.getDragThreshold();

        /**
         *
         * @param e
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            Point tabPt = e.getPoint(); //e.getDragOrigin();
            if (startPt != null && Math.sqrt(Math.pow(tabPt.x - startPt.x, 2) + Math.pow(tabPt.y - startPt.y, 2)) > gestureMotionThreshold) {
                DnDTabbedPane src = (DnDTabbedPane) e.getSource();
                TransferHandler th = src.getTransferHandler();
                //if (th==null) return;
                dragTabIndex = src.indexAtLocation(tabPt.x, tabPt.y);
                th.exportAsDrag(src, e, TransferHandler.MOVE);
                lineRect.setRect(0, 0, 0, 0);
                src.getRootPane().getGlassPane().setVisible(true);
                src.setDropLocation(new DropLocation(tabPt, -1), null, true);
                startPt = null;
            }
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseExited(MouseEvent e) {
        }

        /**
         *
         * @param e
         */
        @Override
        public void mouseMoved(MouseEvent e) {
        }

        /**
         * 
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }
}
