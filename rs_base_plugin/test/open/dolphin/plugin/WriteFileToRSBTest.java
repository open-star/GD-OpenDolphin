/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.plugin;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mahoshi
 */
public class WriteFileToRSBTest {

    public WriteFileToRSBTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of editSmbUrl method, of class WriteFileToRSB.
     */
    @Test
    public void testEditSmbUrl() {
        System.out.println("editSmbUrl");
        WriteFileToRSB instance = null;
        String expResult = "";
        String result = instance.editSmbUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of editContent method, of class WriteFileToRSB.
     */
    @Test
    public void testEditContent() {
        System.out.println("editContent");
        String text = "";
        WriteFileToRSB instance = null;
        String expResult = "";
        String result = instance.editContent(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of witeFileToShare method, of class WriteFileToRSB.
     */
    @Test
    public void testWiteFileToShare() {
        System.out.println("witeFileToShare");
        String text = "";
        WriteFileToRSB instance = null;
        boolean expResult = false;
        boolean result = instance.witeFileToShare(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public class WriteFileToRSBImpl extends WriteFileToRSB {

        public WriteFileToRSBImpl() {
            super("");
        }

        public String editSmbUrl() {
            return "";
        }

        public String editContent(String text) {
            return "";
        }
    }

}