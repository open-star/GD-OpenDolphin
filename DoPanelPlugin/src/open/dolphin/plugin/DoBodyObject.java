/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

/**
 *
 * @author
 */
public class DoBodyObject extends DoHeaderObject {

    private Object[] bodyData;

    /**
     *
     * @param code
     * @param date
     * @param key
     * @param name
     * @param count
     * @param cycle
     * @param unit
     */
    public DoBodyObject(String code, String date, String key, String name, Double count, int cycle, String unit) {
        super(date, key, name);
        bodyData = new Object[4];

        setCode(code);
        setUnit(unit);
        setCount(count);
        setCycle(cycle);
    }

    @Override
    public String toString() {
        String result = bodyData[3] + " " + headerData[2];

        if (!bodyData[2].equals("")) {
            result = result + " " + bodyData[0] + bodyData[2];
        }
        if (((Integer) bodyData[1]) != 0) {
            result = result + " " + bodyData[1] + "回";
        }
        return result;
    }

    /**
     *
     * @return
     */
    public boolean valid() {
        return (this.headerData[2] != null);
    }

    /**
     * @param count the count to set
     */
    public void setCount(Double count) {
        if (count != null) {
            this.bodyData[0] = count;
        }
    }

    /**
     *
     * @return
     */
    public Double getCount() {
        return (Double) this.bodyData[0];
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.bodyData[3] = code;
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return ((String) this.bodyData[3]).trim();
        //     String code = ((String) this.bodyData[3]).trim();
        //      try {
        //      return Integer.parseInt(code);
        //     }
        //   catch(Exception e) {}
        //   return 0;
    }

    /**
     * @param cycle the cycle to set
     */
    public void setCycle(Integer cycle) {
        this.bodyData[1] = cycle;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        if (unit != null) {
            this.bodyData[2] = unit;
        }
    }

    /**
     * @return
     */
    public String getUnit() {
         return (String) this.bodyData[2];
    }

    /**
     *
     * @return
     */
    public DoHeaderObject toDoHeaderObject() {
        return new DoHeaderObject(headerData[0], headerData[1], headerData[2]);
    }
}
