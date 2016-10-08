/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.container;

/**
 *
 * @param <Tk>
 * @param <Tv> 
 * @author
 */
public class Pair<Tk, Tv> implements java.io.Serializable {

    /**
     *
     */
    public Tk key;
    /**
     *
     */
    public Tv value;

    /**
     *
     * @param key
     * @param value
     */
    public Pair(Tk key, Tv value) {
        this.key = key;
        this.value = value;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return key.toString();
    }

    /**
     *
     * @return
     */
    public Tv getValue() {
        return value;
    }

    /**
     *
     * @return
     */
    public Tk getKey() {
        return key;
    }
}
