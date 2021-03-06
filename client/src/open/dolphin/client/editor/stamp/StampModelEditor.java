/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StampModelEditor.java
 *
 * Created on 2009/08/18, 10:30:36
 */
package open.dolphin.client.editor.stamp;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Map;
import javax.swing.JPanel;
import open.dolphin.project.GlobalConstants;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.IStampModelEditor;
import open.dolphin.order.IItemTablePanel;

/**
 *　スタンプエディタ別画面　MEMO:画面
 * @author
 */
public abstract class StampModelEditor extends JPanel implements IStampModelEditor, ComponentListener {

    private PropertyChangeSupport boundSupport;
    private boolean isValidModel;
    private String title;
    private IStampEditorDialog context;
    /**
     *
     */
    protected Map<String, String> updates;
    //  protected ItemTablePanel testTable;
    /**
     * 
     */
    protected IItemTablePanel testTable;

    /** Creates new form StampModelEditor */
    public StampModelEditor() {
        boundSupport = new PropertyChangeSupport(this);
        initComponents();
        addComponentListener(this);
        this.updates = null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * 
     * @return
     */
    @Override
    public IStampEditorDialog getContext() {
        return context;
    }

    /**
     *
     * @param context
     */
    @Override
    public void setContext(IStampEditorDialog context) {
        this.context = context;
    }

    /**
     *
     */
    @Override
    public void start() {
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
     * @return
     */
    @Override
    public boolean isValidModel() {
        return isValidModel;
    }

    /**
     *
     * @param b
     */
    @Override
    public void setValidModel(boolean b) {
        boolean old = isValidModel;
        isValidModel = b;
        boundSupport.firePropertyChange("validData", old, isValidModel);
    }

    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param val
     */
    public void setTitle(String val) {
        StringBuilder buf = new StringBuilder();
        buf.append(val);
        buf.append("エディタ");
        buf.append("-");
        buf.append(GlobalConstants.getString("application.title"));
        this.title = buf.toString();
    }

    /**
     *
     */
    @Override
    public void dispose() {
    }

    /**
     *
     * @return
     */
    @Override
    public abstract Object getValue();

    /**
     *
     * @param value
     */
    @Override
    public abstract void setValue(Object value);

    /**
     *
     * @param e
     */
    @Override
    public void componentHidden(ComponentEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void componentMoved(ComponentEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void componentResized(ComponentEvent e) {
    }

    /**
     *
     * @param e
     */
    @Override
    public void componentShown(ComponentEvent e) {
    }

    /**
     *
     */
    public void clear() {
        if (testTable != null) {
            testTable.clear();
        }
    }
}
