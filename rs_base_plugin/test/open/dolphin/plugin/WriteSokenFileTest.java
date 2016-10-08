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
public class WriteSokenFileTest {

    public WriteSokenFileTest() {
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
     * Test of editSmbUrl method, of class WriteSokenFile.
     */
    @Test
    public void testEditSmbUrl() {
        System.out.println("editSmbUrl");
        WriteShokenFile instance = new WriteShokenFile("123");
        String expResult = "smb://10.0.1.8/tmp/123_shoken.dat";
        String result = instance.editSmbUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of editContent method, of class WriteSokenFile.
     */
    @Test
    public void testEditContent() {
        System.out.println("editContent");
        String text = "異常無し";
        WriteShokenFile instance = new WriteShokenFile("123");
        String expResult = "123¥n2013/05/24¥n異常無し";
        String result = instance.editContent(text);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}