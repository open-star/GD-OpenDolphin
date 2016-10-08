/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.socket.data;

import java.io.Serializable;

/**
 *　OpenDolphinServerへのリクエストデータ
 * @author
 */
public class RequestObject implements Serializable {

    private long patientId;             //対象の患者ID
    private String patientName;         //対象の患者名
    private long userId;                //使用者のID
    private String userName;            //使用者名

    private String place;               //使用場所
    private Command command;

    /**
     * @return the id
     */
    public long getPatientId() {
        return patientId;
    }

    /**
     * @param id the id to set
     */
    public void setPatientId(long id) {
        this.patientId = id;
    }

    /**
     * @return the command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * @return the data
     */
    public String getPlace() {
        return place;
    }

    /**
     * @param data the data to set
     */
    public void setPlace(String name) {
        this.place = name;
    }

    /**
     * @return the data
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param data the data to set
     */
    public void setPatientName(String data) {
        this.patientName = data;
    }

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
