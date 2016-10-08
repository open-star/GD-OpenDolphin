/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package open.dolphin.component;

/**
 *
 * 
 */
/*
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.table.*;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import open.dolphin.log.LogWriter;

public class SpinnerCellEditor extends AbstractCellEditor implements TableCellEditor {

    private final JSpinner spinner = new JSpinner(new SpinnerDateModel());
    private final JSpinner.DateEditor editor;
*/
    /**
     *
     */
/*
    public SpinnerCellEditor() {
        editor = new JSpinner.DateEditor(spinner, "yyyy-MM-dd");
        spinner.setEditor(editor);
        setArrowButtonEnabled(false);
        spinner.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                editor.getTextField().requestFocusInWindow();
            }
        });
        editor.getTextField().addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                setArrowButtonEnabled(false);
            }

            @Override
            public void focusGained(FocusEvent e) {
                setArrowButtonEnabled(true);
                EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        editor.getTextField().setCaretPosition(8);
                        editor.getTextField().setSelectionStart(8);
                        editor.getTextField().setSelectionEnd(10);
                    }
                });
            }
        });
        spinner.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    private void setArrowButtonEnabled(boolean flag) {
        for (Component c : spinner.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).setEnabled(flag);
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Date dateValue = new Date();
        if (value != null) {
            if (!((String) value).isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dateValue = format.parse((String) value);
                } catch (ParseException ex) {
                       LogWriter.error(getClass(), ex);
                }
            }
        }
        spinner.setValue(dateValue);
        editor.getTextField().setHorizontalAlignment(JFormattedTextField.LEFT);
        return spinner;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return ((MouseEvent) e).getClickCount() >= 2;
        }
        return true;
    }
}
*/