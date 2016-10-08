/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

/**
 *
 * @author
 */
public class DiagnosisCode {

    private String diagnosisCode;
    private String alternateCode;
    private boolean Composit;
    private boolean Editing;

    /**
     *
     * @param code
     */
    public DiagnosisCode(String code) {
        diagnosisCode = code;
        CombinedStringParser parser = new CombinedStringParser(diagnosisCode);
        if (parser.size() > 1) {
            alternateCode = "0000999";
            Editing = true;
            Composit = true;
        } else {
            if (diagnosisCode.startsWith("ZZZ")) {
                alternateCode = "0000999";
                Editing = true;
                Composit = false;
            } else {
                alternateCode = diagnosisCode;
                Editing = false;
                Composit = false;
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isComposit() {
        return Composit;
    }

    /**
     * 
     * @return
     */
    public boolean isEditing() {
        return Editing;
    }

    /**
     *
     * @return
     */
    public String getAlternateCode() {
        return alternateCode;
    }

    @Override
    public String toString()
    {
       return diagnosisCode;
    }
}
