/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.labotestimporter;

/**
 *
 * @author tomohiro
 */
class LaboTestIsNotFinalReportException extends Exception {

    final LaboTestInformation laboTestInformation;

    /**
     *
     * @param information
     */
    public LaboTestIsNotFinalReportException(LaboTestInformation information) {
        super("");
        this.laboTestInformation = information;
    }

    /**
     *
     * @param information
     * @param message
     */
    public LaboTestIsNotFinalReportException(LaboTestInformation information, String message) {
        super(message);
        this.laboTestInformation = information;
    }

    /**
     *
     * @return
     */
    public LaboTestInformation getLaboTestInformation() {
        return this.laboTestInformation;
    }
}
