/*
 * PhysicalEditorPanel.java
 *
 * Created on 2008/01/19, 13:32
 */
package open.dolphin.plugin.physicalpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import open.dolphin.project.GlobalConstants;
import open.dolphin.infomodel.IInfoModel;

import open.dolphin.client.CalendarCardPanel;
import open.dolphin.infomodel.PhysicalModel;
import open.dolphin.infomodel.SimpleDate;

import open.dolphin.client.AutoRomanListener;
import open.dolphin.log.LogWriter;
import open.dolphin.utils.StringTool;

/**
 *　身長体重情報　MEMO:画面
 * @author  kazm
 */
public class PhysicalEditorPanel extends javax.swing.JPanel {

    private PhysicalPanel inspector;
    private JDialog dialog;
    private JButton addBtn;
    private JButton clearBtn;

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        heightFld = new javax.swing.JTextField();
        weightFld = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        identifiedDateFld = new javax.swing.JTextField();
        updateButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(PhysicalEditorPanel.class);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        setName("Form"); // NOI18N

        heightFld.setName("heightFld"); // NOI18N

        weightFld.setName("weightFld"); // NOI18N

        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setName("jLabel3"); // NOI18N

        identifiedDateFld.setEditable(false);
        identifiedDateFld.setName("identifiedDateFld"); // NOI18N

        updateButton.setName("updateButton"); // NOI18N
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateButtonMouseClicked(evt);
            }
        });

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(63, 63, 63))
                    .add(layout.createSequentialGroup()
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(heightFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(layout.createSequentialGroup()
                        .add(jLabel6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(weightFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel3)
                            .add(layout.createSequentialGroup()
                                .add(identifiedDateFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(updateButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(updateButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel2)
                        .add(jLabel1)
                        .add(jLabel3)
                        .add(jLabel4)
                        .add(heightFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel6)
                        .add(weightFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(identifiedDateFld, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void updateButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateButtonMouseClicked
        popupCalendar(evt);
    }//GEN-LAST:event_updateButtonMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField heightFld;
    private javax.swing.JTextField identifiedDateFld;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JButton updateButton;
    private javax.swing.JTextField weightFld;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @param inspector
     */
    public PhysicalEditorPanel(PhysicalPanel inspector) {

        initComponents();

        updateButton.setText("変更...");
        this.inspector = inspector;

        addBtn = new JButton("追加");
        addBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        addBtn.setEnabled(false);

        clearBtn = new JButton("キャンセル");
        clearBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        DocumentListener dl = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkBtn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkBtn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkBtn();
            }
        };

        heightFld.getDocument().addDocumentListener(dl);
        weightFld.getDocument().addDocumentListener(dl);
        identifiedDateFld.getDocument().addDocumentListener(dl);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(IInfoModel.DATE_WITHOUT_TIME);
        String todayString = sdf.format(date);
        identifiedDateFld.setText(todayString);
        //    new PopupListener(identifiedDateFld);

        heightFld.addFocusListener(AutoRomanListener.getInstance());
        weightFld.addFocusListener(AutoRomanListener.getInstance());
        identifiedDateFld.addFocusListener(AutoRomanListener.getInstance());

        //   clearBtn.setEnabled(false);

        Object[] options = new Object[]{addBtn, clearBtn};

        JOptionPane pane = new JOptionPane(this,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options, addBtn);
        dialog = pane.createDialog(inspector.getParentContext().getFrame(), GlobalConstants.getFrameTitle("身長体重登録"));
        dialog.setVisible(true);
    }

    /**
     *
     */
    private void checkBtn() {

        String height = heightFld.getText().trim();
        String weight = weightFld.getText().trim();
        if (!height.equals("") && !weight.equals("")) {
            try {
                //   double _height = Double.valueOf(height);
                //     double _weight = Double.valueOf(weight);
                addBtn.setEnabled(true);
                //     if (StringUtils.isNumeric(height)) {
                //         if (StringUtils.isNumeric(weight)) {
                //              addBtn.setEnabled(true);
                //              return;
                //          }
                //      }
            } catch (Exception e) {
                addBtn.setEnabled(false);
            }
        }
    }

    /**
     *
     */
    private void add() {

        String h = heightFld.getText().trim();
        String w = weightFld.getText().trim();
        final PhysicalModel model = new PhysicalModel();

        try {
            if (!h.equals("")) {
                double _height = Double.valueOf(h);// MEMO;Unused?
                model.setHeight(StringTool.zenkakuNumToHankaku(h));
            }
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }

        try {
            if (!w.equals("")) {
                double _height = Double.valueOf(w);// MEMO;Unused?
                model.setWeight(StringTool.zenkakuNumToHankaku(w));
            }
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }

        /*
        if (!h.equals("")) {
        if (StringUtils.isNumeric(h)) {
        model.setHeight(StringTool.zenkakuNumToHankaku(h));
        }
        }
        if (!w.equals("")) {
        if (StringUtils.isNumeric(w)) {
        model.setWeight(StringTool.zenkakuNumToHankaku(w));
        }
        }
         */

        // 同定日
        String confirmedStr = identifiedDateFld.getText().trim();

        model.setIdentifiedDate(confirmedStr);

        addBtn.setEnabled(false);
        clearBtn.setEnabled(false);
        inspector.add(model);
        dialog.setVisible(false);
    }

    /**
     *
     */
    private void clear() {
        dialog.setVisible(false);
    }
    /*
    class PopupListener extends MouseAdapter implements PropertyChangeListener {

    private JPopupMenu popup;
    private JTextField tf;

    // private LiteCalendarPanel calendar;
    public PopupListener(JTextField tf) {
    this.tf = tf;
    tf.addMouseListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {

    if (e.isPopupTrigger()) {
    popup = new JPopupMenu();
    CalendarCardPanel cc = new CalendarCardPanel(GlobalVariables.getEventColorTable());
    cc.addPropertyChangeListener(CalendarCardPanel.PICKED_DATE, this);
    cc.setCalendarRange(new int[]{-12, 0});
    popup.insert(cc, 0);
    popup.show(e.getComponent(), e.getX(), e.getY());
    }
    }

    public void propertyChange(PropertyChangeEvent e) {
    if (e.getPropertyName().equals(CalendarCardPanel.PICKED_DATE)) {
    SimpleDate sd = (SimpleDate) e.getNewValue();
    tf.setText(SimpleDate.simpleDateToMmldate(sd));
    popup.setVisible(false);
    popup = null;
    }
    }
    }
     */

    /**
     * カレンダーを表示
     * @param e
     */
    public void popupCalendar(MouseEvent e) {
        final JPopupMenu popup = new JPopupMenu();
        CalendarCardPanel cc = new CalendarCardPanel(GlobalConstants.getEventColorTable());
        cc.addPropertyChangeListener(CalendarCardPanel.PICKED_DATE, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(CalendarCardPanel.PICKED_DATE)) {
                    SimpleDate sd = (SimpleDate) e.getNewValue();
                    identifiedDateFld.setText(SimpleDate.simpleDateToMmldate(sd));
                    popup.setVisible(false);
                }

            }
        });
        cc.setCalendarRange(new int[]{-12, 0});
        popup.insert(cc, 0);
        popup.show(e.getComponent(), e.getX(), e.getY());
    }
}