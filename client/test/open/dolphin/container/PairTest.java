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
public class PairTest {

    /**
     *
     */
    public PairTest() {
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
    public void testToString() {
        System.out.println("toString");
        Pair instance = new Pair<String, String>("key", "value");
        String expResult = "key";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Pair<String, String> instance = new Pair<String, String>("key", "value");
        String expResult = "value";
        String result = instance.getValue();
        assertEquals(expResult, result);
    }

    /**
     *
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        Pair<String, String> instance = new Pair<String, String>("key", "value");
        String expResult = "key";
        String result = instance.getKey();
        assertEquals(expResult, result);
    }
}
