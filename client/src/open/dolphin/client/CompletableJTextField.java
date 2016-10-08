/*package open.dolphin.client;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalSettings;

public class CompletableJTextField extends JTextField implements ListSelectionListener {

    Completer completer;
    JList completionList;
    DefaultListModel completionListModel;
    JScrollPane listScroller;

    public CompletableJTextField(int col) {
        super(col);
        completer = new Completer();
        completionListModel = new DefaultListModel();
        completionList = new JList(completionListModel);
        completionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completionList.addListSelectionListener(this);
        listScroller = new JScrollPane(completionList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScroller.setVisible(false);
        listScroller.getViewport().setBackground(GlobalSettings.getColors(GlobalSettings.Parts.TABLE_BACKGROUND));

    }

    protected void addCompletion(String s) throws BadLocationException {
        completer.addCompletion(s);
    }

    private void removeCompletion(String s) throws BadLocationException {
        completer.removeCompletion(s);
    }

    private void clearCompletions(String s) {
        completer.clearCompletions();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }
        if (completionList.getModel().getSize() == 0) {
            return;
        }
        listScroller.setVisible(false);


        final String completionString = (String) completionList.getSelectedValue();

        Thread worker = new Thread() {

            @Override
            public void run() {
                setText(completionString);
            }
        };
        SwingUtilities.invokeLater(worker);
    }


    class Completer implements DocumentListener {

        private Pattern pattern;
        private List completions;

        private Completer() {
            completions = new ArrayList();
            getDocument().addDocumentListener(this);
        }

        private void addCompletion(String s) throws BadLocationException {
            completions.add(s);
            buildAndShowPopup();
        }

        private void removeCompletion(String s) throws BadLocationException {
            completions.remove(s);
            buildAndShowPopup();
        }

        private void clearCompletions() {
            completions.clear();
            buildPopup();
            listScroller.setVisible(false);
        }

        private void buildPopup() {
            completionListModel.clear();
            Iterator it = completions.iterator();
            pattern = Pattern.compile(getText() + ".+");
            while (it.hasNext()) {
                // check if match
                String completion = (String) it.next();
                Matcher matcher = pattern.matcher(completion);
                if (matcher.matches()) {
                    // add if match
                    completionListModel.add(completionListModel.getSize(), completion);
                }
            }
        }

        private void showPopup() throws BadLocationException {
            if (completionListModel.getSize() == 0) {
                listScroller.setVisible(false);
                return;
            }

            int pos = getCaretPosition();

            Rectangle r = modelToView(pos);
            listScroller.setBounds(r.x, r.y, 200, 300);

            listScroller.setVisible(true);
            listScroller.requestFocus();

        }

        private void buildAndShowPopup() throws BadLocationException {
            if (getText().length() < 1) {
                return;
            }
            buildPopup();
            showPopup();
        }

        // DocumentListener implementation
        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                buildAndShowPopup();
            } catch (BadLocationException ex) {
                LogWriter.error(getClass(), ex);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            try {
                buildAndShowPopup();
            } catch (BadLocationException ex) {
                LogWriter.error(getClass(), ex);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            try {
                buildAndShowPopup();
            } catch (BadLocationException ex) {
                LogWriter.error(getClass(), ex);
            }
        }
    }
}
*/