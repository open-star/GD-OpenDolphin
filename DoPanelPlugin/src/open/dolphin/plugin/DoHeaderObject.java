/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

/**
 *
 * @author
 */
public class DoHeaderObject {

    /**
     *
     */
    protected String[] headerData;

    /**
     *
     * @param date
     * @param key
     * @param name
     */
    public DoHeaderObject(String date, String key, String name) {
        headerData = new String[3];

        setDate(date);
        setKey(key);
        setName(name);
    }

    @Override
    public String toString() {
        return headerData[0];
    }

    /**
     *
     * @return
     */
    public boolean valid() {
        return (this.headerData[2] != null);
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.headerData[0] = date;
    }
    /**
     * @return
     */
    public String getDate() {
        return this.headerData[0];
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.headerData[1] = key;
    }

    /**
     *
     * @return
     */
    public String getKey() {
        return this.headerData[1];
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if (name != null) {
            this.headerData[2] = name;
        }
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.headerData[2];
    }
}

