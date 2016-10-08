/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StampTreePanel.java
 *
 * Created on 2010/03/05, 18:29:09
 */
package open.dolphin.client.editor.stamp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import open.dolphin.project.GlobalSettings;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModuleInfoBean;

/**
 *　スタンプツリー　MEMO:画面
 * @author
 */
public class StampTreePanel extends javax.swing.JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = -268963413379453444L;
    /**
     *
     */
    protected StampTree stampTree;
    /**
     * 
     */
    protected JTextArea infoArea;

    /**
     *
     * @param tree
     */
    public StampTreePanel(StampTree tree) {
        initComponents();
        this.stampTree = tree;        
        initCustomComponents();
    }

    private void initCustomComponents() {
        JScrollPane scroller = new JScrollPane(stampTree);
        scroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));
        this.setLayout(new BorderLayout());
        this.add(scroller, BorderLayout.CENTER);

        String treeEntity = stampTree.getEntity();
        if (treeEntity != null && (!treeEntity.equals(IInfoModel.ENTITY_TEXT))) {
            infoArea = new JTextArea();
            infoArea.setMargin(new Insets(3, 2, 3, 2));
            infoArea.setLineWrap(true);
            infoArea.setPreferredSize(new Dimension(250, 40));
            Font font = new Font("Dialog", Font.PLAIN, 10);
            infoArea.setFont(font);
            this.add(infoArea, BorderLayout.SOUTH);
            this.stampTree.addTreeSelectionListener(this);
        }
    }

    /**
     * このパネルのStampTreeを返す。
     * @return StampTree
     */
    public StampTree getTree() {
        return stampTree;
    }

    /**
     * スタンプツリーで選択されたスタンプの情報を表示する。
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        StampTree tree = (StampTree) e.getSource();
        StampTreeNode node = (StampTreeNode) tree.getLastSelectedPathComponent();
        if (node != null) {
            if (node.getUserObject() instanceof ModuleInfoBean) {
                ModuleInfoBean info = (ModuleInfoBean) node.getUserObject();
                infoArea.setText(info.getStampMemo());
            } else {
                infoArea.setText("");
            }
        } else {
            infoArea.setText("");
        }
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
}
