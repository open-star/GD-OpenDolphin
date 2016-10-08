/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.plugin;

import open.dolphin.plugin.IPlugin.Command;
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
public class RSBasePluginTest {

    public RSBasePluginTest() {
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
     * Test of dispatchCommand method, of class RSBasePlugin.
     */
    @Test
    public void testDispatchCommand() {
        System.out.println("dispatchCommand");
        Command command = Command.writeShokenFile;

        Object[] request = new Object[2];
        //request[0] = parent;
        request[1] = "異常無し";
        Object[] response = new Object[1];

        RSBasePlugin instance = new RSBasePlugin();
        boolean expResult = false;
        boolean result = instance.dispatchCommand(command, request, response);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}