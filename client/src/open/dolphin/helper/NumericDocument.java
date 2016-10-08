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
public class NumericDocument extends VerifierDocument<Double> {

    /**
     *
     */
    public NumericDocument() {
        super();
        currentValue = 0.0;
    }

    @Override
    public Double checkInput(String proposedValue, int offset) throws BadLocationException {
        if (proposedValue.length() > 0) {
            try {
                double newValue = Double.parseDouble(proposedValue);
                return newValue;
            } catch (NumberFormatException e) {
                throw new BadLocationException(proposedValue, offset);
            }
        } else {
            return 0.0;
        }
    }
}
