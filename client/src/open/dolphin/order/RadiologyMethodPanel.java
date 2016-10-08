/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *
 * Created on 2010/03/08, 15:30:42
 */
package open.dolphin.order;

import javax.swing.event.*;

import open.dolphin.delegater.remote.RemoteRadiologyMasterDelegater;
import open.dolphin.infomodel.RadiologyMethodValue;

import java.beans.*;
import java.util.*;
import open.dolphin.project.GlobalSettings;

/**
 *　スタンプエディタ　画像診断　撮影方法　MEMO:画面
 * @author
 */
public final class RadiologyMethodPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 7002106454090449477L;
    /**
     *
     */
    public static final String RADIOLOGY_MEYTHOD_PROP = "radiologyProp";
    private List<RadiologyMethodValue> v2;
    private PropertyChangeSupport boundSupport;

    /** Creates new form RadiologyMethod */
    public RadiologyMethodPanel() {
        boundSupport = new PropertyChangeSupport(this);
        initComponents();
        initCustomComponents();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        p1 = new javax.swing.JPanel();
        methodScroller = new javax.swing.JScrollPane();
        methodList = new javax.swing.JList();
        p2 = new javax.swing.JPanel();
        commentScroller = new javax.swing.JScrollPane();
        commentList = new javax.swing.JList();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        p1.setBorder(javax.swing.BorderFactory.createTitledBorder("撮影方法"));
        p1.setName("p1"); // NOI18N
        p1.setLayout(new java.awt.BorderLayout());

        methodScroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        methodScroller.setName("methodScroller"); // NOI18N

        methodList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        methodList.setFixedCellWidth(120);
        methodList.setName("methodList"); // NOI18N
        methodScroller.setViewportView(methodList);

        p1.add(methodScroller, java.awt.BorderLayout.CENTER);

        add(p1);

        p2.setBorder(javax.swing.BorderFactory.createTitledBorder("撮影コメント"));
        p2.setName("p2"); // NOI18N
        p2.setLayout(new java.awt.BorderLayout());

        commentScroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        commentScroller.setName("commentScroller"); // NOI18N

        commentList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        commentList.setFixedCellWidth(140);
        commentList.setName("commentList"); // NOI18N
        commentScroller.setViewportView(commentList);

        p2.add(commentScroller, java.awt.BorderLayout.CENTER);

        add(p2);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList commentList;
    private javax.swing.JScrollPane commentScroller;
    private javax.swing.JList methodList;
    private javax.swing.JScrollPane methodScroller;
    private javax.swing.JPanel p1;
    private javax.swing.JPanel p2;
    // End of variables declaration//GEN-END:variables

    /**
     *
     */
    private void initCustomComponents() {
        RemoteRadiologyMasterDelegater mdl = new RemoteRadiologyMasterDelegater();
        methodList.setListData(mdl.getRadiologyMethod().toArray());
        methodList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting() == false) {
                    RadiologyMethodValue entry = (RadiologyMethodValue) methodList.getSelectedValue();
                    if (entry == null) {
                        return;
                    }
                    fetchComments(entry.getHierarchyCode1());
                }
            }
        });
        methodScroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        commentList.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (e.getValueIsAdjusting() == false) {

                    RadiologyMethodValue entry = (RadiologyMethodValue) commentList.getSelectedValue();
                    if (entry == null) {
                        return;
                    }
                    notifyComment(entry.getMethodName());
                }
            }
        });
        commentScroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
    }

    /**
     *
     * @param prop
     * @param l
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     *
     * @param prop
     * @param l
     */
    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     *
     * @param cm
     */
    private void notifyComment(String cm) {
        boundSupport.firePropertyChange(RADIOLOGY_MEYTHOD_PROP, null, cm);
    }

    /**
     *
     * @param h1
     */
    private void fetchComments(String h1) {
        if (v2 != null) {
            v2.clear();
        }
        RemoteRadiologyMasterDelegater mdl = new RemoteRadiologyMasterDelegater();
        v2 = mdl.getRadiologyComments(h1);
        commentList.setListData(v2.toArray());
    }
}