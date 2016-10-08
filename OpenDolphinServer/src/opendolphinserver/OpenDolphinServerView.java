/*
 * OpenDolphinServerView.java
 */
package opendolphinserver;

import java.util.HashSet;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import open.socket.Message;
import open.socket.data.ResponseObject;
import open.socket.data.RequestObject;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;


/**
 * The application's main frame.
 */
public class OpenDolphinServerView extends FrameView {

    TableModel model;
    HashSet<Long> loginIds;
    HashSet<Long> patientIds;

    public OpenDolphinServerView(SingleFrameApplication app) {
        super(app);
        initComponents();

        model = jTable1.getModel();
        loginIds = new HashSet<Long>();
        patientIds = new HashSet<Long>();

        Task task = new Task<ResponseObject, RequestObject>(getContext().getApplication()) {

            @Override
            protected ResponseObject doInBackground() throws Exception {

                try {
                    Message.Receive(50000, 0, new open.socket.Adapter<RequestObject, ResponseObject>() {

                        public ResponseObject onResult(RequestObject request) throws Exception {

                            ResponseObject response = new ResponseObject();
                            response.setId(request.getPatientId());
                            response.setCommand(request.getCommand());
                         //   response.setData(request.getPlace());

                            switch (request.getCommand()) {

                                case KARTE_OPEN:
                                    if (!patientIds.contains(request.getPatientId())) {
                                        for (int index = 0; index < model.getRowCount(); index++) {
                                            String id = ((String) ((DefaultTableModel) model).getValueAt(index, 0));
                                            if (Long.parseLong(id) == request.getUserId()) {
                                                model.setValueAt(request.getPatientId(), index, 3);
                                                model.setValueAt(request.getPatientName(), index, 4);
                                                patientIds.add(new Long(request.getPatientId()));
                                            }
                                        }
                                    }
                                    break;
                                case KARTE_CLOSE:
                                    if (patientIds.contains(request.getPatientId())) {
                                        for (int index = 0; index < model.getRowCount(); index++) {
                                            Long id = ((Long) ((DefaultTableModel) model).getValueAt(index, 3));
                                            if (id != null) {
                                                if (id == request.getPatientId()) {
                                                    model.setValueAt("", index, 3);
                                                    model.setValueAt("", index, 4);
                                                    patientIds.remove(request.getPatientId());
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case LOGIN:
                                    if (!loginIds.contains(request.getUserId())) {
                                        Object[] row = new Object[6];
                                        row[0] = Long.toString(request.getUserId());
                                        row[1] = request.getUserName();
                                        row[2] = request.getPlace();

                                        ((DefaultTableModel) model).addRow(row);
                                        loginIds.add(new Long(request.getUserId()));
                                    }
                                    break;
                                case LOGOUT:
                                    if (loginIds.contains(request.getUserId())) {
                                        for (int index = 0; index < model.getRowCount(); index++) {
                                            String id = ((String) ((DefaultTableModel) model).getValueAt(index, 0));
                                            if (Long.parseLong(id) == request.getUserId()) {
                                                ((DefaultTableModel) model).removeRow(index);
                                                loginIds.remove(request.getUserId());
                                            }
                                        }
                                    }
                                    break;
                                default:
                            }
                            response.setData(Serialize(model));
                            return response;
                        }

                        public void onError(Exception ex) {
                        }
                    });

                } catch (Exception e) {

                }
                return null;
            }

            @Override
            protected void succeeded(ResponseObject karteBean) {
            }

            @Override
            protected void cancelled() {
            }

            @Override
            protected void failed(java.lang.Throwable cause) {
            }

            @Override
            protected void interrupted(java.lang.InterruptedException e) {
            }
        };
        task.execute();
    }

    private String Serialize(TableModel model) {
        StringBuilder result = new StringBuilder();
        result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        result.append("<table>");
        for (int rows = 0; rows < ((DefaultTableModel) model).getRowCount(); rows++) {
            result.append("<columns>");
            for (int colunms = 0; colunms < ((DefaultTableModel) model).getColumnCount(); colunms++) {
                result.append("<rows>");
                result.append(((DefaultTableModel) model).getValueAt(rows, colunms));
                result.append("</rows>");
            }
            result.append("</columns>");
        }
        result.append("</table>");
        return result.toString();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(opendolphinserver.OpenDolphinServerApp.class).getContext().getResourceMap(OpenDolphinServerView.class);
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
        jTextField2.setName("jTextField2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "タイトル 3", "Title 3", "null", "null"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N
        jTable1.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable1.columnModel.title2")); // NOI18N
        jTable1.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable1.columnModel.title3")); // NOI18N
        jTable1.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTable1.columnModel.title4")); // NOI18N
        jTable1.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jTable1.columnModel.title5")); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addContainerGap())
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                        .addGap(105, 105, 105))))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(opendolphinserver.OpenDolphinServerApp.class).getContext().getActionMap(OpenDolphinServerView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        ResponseObject response = new ResponseObject();

        TableModel m = response;// MEMO;Unused?
        //    jTextField1.setText((String) Message.Send("localhost", 50000, jTextField2.getText()));
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
}
