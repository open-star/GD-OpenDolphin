/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.container;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomohiro
 */
public class NameValuePairTest {

    /**
     *
     */
    public NameValuePairTest() {
    }

    /**
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void testGetIndex_nothing() {
        System.out.println("getIndex nothing");
        String test = "test";
        NameValuePair[] cnArray = new NameValuePair[5];
        for (int i = 0; i < 5; i++) {
            cnArray[i] = new NameValuePair(String.valueOf(i), "hoge");
        }
        int expResult = 0;
        int result = NameValuePair.getIndex(test, cnArray);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetIndex_last() {
        System.out.println("getIndex las");
        String test = "test";
        NameValuePair[] cnArray = new NameValuePair[5];
        for (int i = 0; i < 5; i++) {
            cnArray[i] = new NameValuePair(String.valueOf(i), "hoge");
        }
        int expResult = 4;
        cnArray[expResult].setValue("test");
        int result = NameValuePair.getIndex(test, cnArray);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetIndex_first() {
        System.out.println("getIndex first");
        String test = "test";
        NameValuePair[] cnArray = new NameValuePair[5];
        for (int i = 0; i < 5; i++) {
            cnArray[i] = new NameValuePair(String.valueOf(i), "hoge");
        }
        int expResult = 0;
        cnArray[expResult].setValue("test");
        int result = NameValuePair.getIndex(test, cnArray);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetIndex_middle() {
        System.out.println("getIndex middle");
        String test = "test";
        NameValuePair[] cnArray = new NameValuePair[5];
        for (int i = 0; i < 5; i++) {
            cnArray[i] = new NameValuePair(String.valueOf(i), "hoge");
        }
        int expResult = 2;
        cnArray[expResult].setValue("test");
        int result = NameValuePair.getIndex(test, cnArray);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testSetAndGetValue() {
        System.out.println("setValue");
        String code = "key";
        NameValuePair instance = new NameValuePair();
        instance.setValue(code);
        assertEquals(code, instance.getValue());
    }

    /**
     *
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        String expResult = "value";
        NameValuePair instance = new NameValuePair("name", expResult);
        String result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testSetAndGetName() {
        System.out.println("setName");
        String name = "nyanco";
        NameValuePair instance = new NameValuePair();
        instance.setName(name);
        assertEquals(name, instance.getName());
    }

    /**
     *
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        String expResult = "nyanco";
        NameValuePair instance = new NameValuePair(expResult, "value");
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class NameValuePair.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String expResult = "twitter";
        NameValuePair instance = new NameValuePair(expResult, "value");
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        String word = "word";
        NameValuePair instance = new NameValuePair("key", word);
        int expResult = word.hashCode() + 15;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testEquals_same() {
        System.out.println("equals same");
        String name = "name";
        String value = "value";
        NameValuePair other = new NameValuePair(name, value);
        NameValuePair instance = new NameValuePair(name, value);
        boolean expResult = true;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testEquals_notSame() {
        System.out.println("equals not same");
        String name = "name";
        String value = "value";
        NameValuePair other = new NameValuePair(name, "other");
        NameValuePair instance = new NameValuePair(name, value);
        boolean expResult = false;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testEquals_invalid() {
        System.out.println("equals invalid");
        String name = "name";
        String value = "value";
        String other = "hoge";
        NameValuePair instance = new NameValuePair(name, value);
        boolean expResult = false;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testCompareTo_greater() {
        System.out.println("compareTo greater");
        String key = "key";
        NameValuePair instance = new NameValuePair(key, "b");
        NameValuePair other = new NameValuePair(key, "a");
        int expResult = 1;
        int result = instance.compareTo(other);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testCompareTo_less() {
        System.out.println("compareTo less");
        String key = "key";
        NameValuePair instance = new NameValuePair(key, "a");
        NameValuePair other = new NameValuePair(key, "b");
        int expResult = -1;
        int result = instance.compareTo(other);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testCompareTo_equal() {
        System.out.println("compareTo equal");
        String key = "key";
        NameValuePair instance = new NameValuePair(key, "b");
        NameValuePair other = new NameValuePair(key, "b");
        int expResult = 0;
        int result = instance.compareTo(other);
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testCompareTo_invalid() {
        System.out.println("compareTo invalid");
        String key = "key";
        NameValuePair instance = new NameValuePair(key, "b");
        String other = "hoge";
        int expResult = -1;
        int result = instance.compareTo(other);
        assertEquals(expResult, result);
    }
}
