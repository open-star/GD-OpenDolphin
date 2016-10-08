/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

import javax.swing.text.BadLocationException;

/**
 *
 * @author oda
 */
public class IntegerDocument extends VerifierDocument<Integer> {

    /**
     *
     */
    public IntegerDocument() {
        super();
        currentValue = 0;
    }

    /**
     *
     * @param proposedValue
     * @param offset
     * @return
     * @throws BadLocationException
     */
    @Override
    public Integer checkInput(String proposedValue, int offset) throws BadLocationException {
        if (proposedValue.length() > 0) {
            try {
                int newValue = Integer.parseInt(proposedValue);
                return newValue;
            } catch (NumberFormatException e) {
                throw new BadLocationException(proposedValue, offset);
            }
        } else {
            return 0;
        }
    }
}
