/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author
 * @param <T>
 */
public abstract class VerifierDocument<T> extends PlainDocument {

    T currentValue;

    /**
     *
     */
    public VerifierDocument() {
        super();
    }

    /**
     *
     * @return
     */
    public T getValue() {
        return currentValue;
    }

    /**
     *
     * @param offset
     * @param S
     * @param attributes
     * @throws BadLocationException
     */
    @Override
    public void insertString(int offset, String S, AttributeSet attributes) throws BadLocationException {
        if (S == null) {
            return;
        } else {
            String newValue;
            int length = getLength();
            if (length == 0) {
                newValue = S;
            } else {
                String currentContent = getText(0, length);
                StringBuffer currentBuffer = new StringBuffer(currentContent);
                currentBuffer.insert(offset, S);
                newValue = currentBuffer.toString();
            }
            currentValue = checkInput(newValue, offset);
            super.insertString(offset, S, attributes);
        }
    }

    /**
     *
     * @param offset
     * @param length
     * @throws BadLocationException
     */
    @Override
    public void remove(int offset, int length) throws BadLocationException {
        int currentLength = getLength();
        String currentContent = getText(0, currentLength);
        String before = currentContent.substring(0, offset);
        String after = currentContent.substring(length + offset, currentLength);
        String newValue = before + after;
        currentValue = checkInput(newValue, offset);
        super.remove(offset, length);
    }

    /**
     * MEMO: unused?
     * @param S
     * @return
     */
    private String HankakuNumeric(String S) {
        switch (S.charAt(0)) {
            case '０':
                return "0";
            case '１':
                return "1";
            case '２':
                return "2";
            case '３':
                return "3";
            case '４':
                return "4";
            case '５':
                return "5";
            case '６':
                return "6";
            case '７':
                return "7";
            case '８':
                return "8";
            case '９':
                return "9";
            default:
                return S;
        }
    }

    /**
     * 
     * @param proposedValue
     * @param offset
     * @return
     * @throws BadLocationException
     */
    abstract T checkInput(String proposedValue, int offset) throws BadLocationException;
}
