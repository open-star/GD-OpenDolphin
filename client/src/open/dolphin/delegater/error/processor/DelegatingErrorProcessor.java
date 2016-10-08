/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package open.dolphin.delegater.error.processor;

//import open.dolphin.delegater.DelegaterErrorHandler;
//import open.dolphin.log.LogWriter;

/**
 *
 * @author tomohiro
 */
/*
public abstract class DelegatingErrorProcessor {

    private String errorMessages;
    private final Exception originalException;
    private DelegaterErrorHandler ownerDelegater;
*/
    /**
     *
     * @param owner
     * @param exception
     */
/*
    public DelegatingErrorProcessor(final DelegaterErrorHandler owner, final Exception exception) {
        originalException = exception;
        ownerDelegater = owner;
    }

    public DelegaterErrorHandler getOwnerDelegater() {
        return ownerDelegater;
    }
*/
    /**
     *
     * @param message
     * @param logMessage
     */
/*
    public void process(String message, String logMessage) {
        LogWriter.error(getOwnerDelegater().getClass(), logMessage, getOriginalException());
        setErrorMessage(message);
    }
*/
    /**
     *
     * @return
     */
/*
    public Exception getOriginalException() {
        return originalException;
    }

    public boolean isError() {
        return true;
    }

    public String getErrorMessage() {
        return errorMessages;
    }

    public void setErrorMessage(String message) {
        errorMessages = message;
    }
}
*/