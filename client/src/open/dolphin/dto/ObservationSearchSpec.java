package open.dolphin.dto;

import java.util.Date;

/**
 * ObservationSearchSpec　MEMO:DTO
 * 
 * @author Minagawa,kazushi
 *
 */
public class ObservationSearchSpec extends DolphinDTO {

    private static final long serialVersionUID = 1297578145028629411L;
    private long karteId;
    private String observation;
    private String phenomenon;
    private Date firstConfirmed;

    /**
     *　初診
     * @return　初診
     */
    public Date getFirstConfirmed() {
        return firstConfirmed;
    }

    /**
     *　初診
     * @param firstConfirmed　初診
     */
    public void setFirstConfirmed(Date firstConfirmed) {
        this.firstConfirmed = firstConfirmed;
    }

    /**
     *　カルテID
     * @return　カルテID
     */
    public long getKarteId() {
        return karteId;
    }

    /**
     *　カルテID
     * @param karteId　カルテID
     */
    public void setKarteId(long karteId) {
        this.karteId = karteId;
    }

    /**
     *
     * @return
     */
    public String getObservation() {
        return observation;
    }

    /**
     *
     * @param observation
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }

    /**
     *　症状
     * @return　症状
     */
    public String getPhenomenon() {
        return phenomenon;
    }

    /**
     *　症状
     * @param phenomenon　症状
     */
    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }
}
