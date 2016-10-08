package open.dolphin.container;

/**
 * @author Kazushi Minagawa Digital Globe, Inc.
 *
 */
public class NameValuePair {

    private String key;
    private String value;

    /**
     *
     * @param test
     * @param cnArray
     * @return
     */
    public static int getIndex(String test, NameValuePair[] cnArray) {
        int index = 0;
        for (int i = 0; i < cnArray.length; i++) {
            if (test.equals(cnArray[i].getValue())) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     *
     */
    public NameValuePair() {
    }

    /**
     *
     * @param name
     * @param value
     */
    public NameValuePair(String name, String value) {
        this();
        setName(name);
        setValue(value);
    }

    /**
     *
     * @param code
     */
    public void setValue(String code) {
        this.value = code;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.key = name;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return key;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return key;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return value.hashCode() + 15;
    }

    /**
     *
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (other != null && getClass() == other.getClass()) {
            String otherValue = ((NameValuePair) other).getValue();
            return value.equals(otherValue);
        }
        return false;
    }

    /**
     *
     * @param other
     * @return
     */
    public int compareTo(Object other) {
        if (other != null && getClass() == other.getClass()) {
            String otherValue = ((NameValuePair) other).getValue();
            return value.compareTo(otherValue);
        }
        return -1;
    }
}
