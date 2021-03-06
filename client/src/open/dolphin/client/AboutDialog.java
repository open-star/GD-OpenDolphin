/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AboutDialog.java
 *
 * Created on 2010/03/05, 15:35:49
 */
package open.dolphin.client;

/**
 *　About画面　MEMO:画面 DolphinFactory AbstractProjectFactory Dolphin
 * @author
 */
public class AboutDialog extends javax.swing.JDialog {

    /**
     * 
     */
    public AboutDialog() {
        super();
        this.initComponents();
        this.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        about = new javax.swing.JLabel();
        icon = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        copyright1 = new javax.swing.JLabel();
        copyright2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("情報"); // NOI18N
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));

        about.setText("このプログラムについて");
        about.setName("about"); // NOI18N

        icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open/dolphin/resources/images/opendolphin2.JPG"))); // NOI18N
        icon.setName("icon"); // NOI18N

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("open/dolphin/resources/Dolphin"); // NOI18N
        version.setText(bundle.getString("Application.title")); // NOI18N
        version.setName("version"); // NOI18N

        copyright1.setText(bundle.getString("Application.copyright1")); // NOI18N
        copyright1.setName("copyright1"); // NOI18N

        copyright2.setText(bundle.getString("Application.copyright2")); // NOI18N
        copyright2.setName("copyright2"); // NOI18N

        jButton1.setText("閉じる");
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(193, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(182, 182, 182))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(copyright2)
                    .addComponent(copyright1)
                    .addComponent(version)
                    .addComponent(icon)
                    .addComponent(about))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(about)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(icon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(version)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(copyright1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(copyright2)
                .addGap(30, 30, 30)
                .addComponent(jButton1)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-444)/2, (screenSize.height-379)/2, 444, 379);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        close();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void close() {
        this.setVisible(false);
        this.dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel about;
    private javax.swing.JLabel copyright1;
    private javax.swing.JLabel copyright2;
    private javax.swing.JLabel icon;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel version;
    // End of variables declaration//GEN-END:variables
}
