/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.plugin;

import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import open.dolphin.client.settings.IAbstractSettingPanel.State;
import open.dolphin.client.settings.ProjectSettingDialog;
import open.dolphin.utils.Adapter;
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
public class RSBasePluginSettingPanelTest {

    public RSBasePluginSettingPanelTest() {
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
     * Test of getId method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        RSBasePluginSettingPanel instance = null;
        String expResult = "";
        String result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        String id = "";
        RSBasePluginSettingPanel instance = null;
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getTitle method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        RSBasePluginSettingPanel instance = null;
        String expResult = "";
        String result = instance.getTitle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setTitle method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
        String title = "";
        RSBasePluginSettingPanel instance = null;
        instance.setTitle(title);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getIcon method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testGetIcon() {
        System.out.println("getIcon");
        RSBasePluginSettingPanel instance = null;
        String expResult = "";
        String result = instance.getIcon();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setIcon method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testSetIcon() {
        System.out.println("setIcon");
        String icon = "";
        RSBasePluginSettingPanel instance = null;
        instance.setIcon(icon);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getContext method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testGetContext() {
        System.out.println("getContext");
        RSBasePluginSettingPanel instance = null;
        ProjectSettingDialog expResult = null;
        ProjectSettingDialog result = instance.getContext();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setContext method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testSetContext() {
        System.out.println("setContext");
        ProjectSettingDialog context = null;
        RSBasePluginSettingPanel instance = null;
        instance.setContext(context);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isLoginState method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testIsLoginState() {
        System.out.println("isLoginState");
        RSBasePluginSettingPanel instance = null;
        boolean expResult = false;
        boolean result = instance.isLoginState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setLogInState method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testSetLogInState() {
        System.out.println("setLogInState");
        boolean login = false;
        RSBasePluginSettingPanel instance = null;
        instance.setLogInState(login);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getPanel method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testGetPanel() {
        System.out.println("getPanel");
        RSBasePluginSettingPanel instance = null;
        JPanel expResult = null;
        JPanel result = instance.getPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        RSBasePluginSettingPanel instance = null;
        instance.start();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of save method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testSave() {
        System.out.println("save");
        RSBasePluginSettingPanel instance = null;
        instance.save();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setState method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testSetState() {
        System.out.println("setState");
        State state = null;
        RSBasePluginSettingPanel instance = null;
        instance.setState(state);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getState method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
        RSBasePluginSettingPanel instance = null;
        State expResult = null;
        State result = instance.getState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addPropertyChangeListener method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testAddPropertyChangeListener() {
        System.out.println("addPropertyChangeListener");
        String prop = "";
        PropertyChangeListener l = null;
        RSBasePluginSettingPanel instance = null;
        instance.addPropertyChangeListener(prop, l);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of removePropertyChangeListener method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testRemovePropertyChangeListener() {
        System.out.println("removePropertyChangeListener");
        String prop = "";
        PropertyChangeListener l = null;
        RSBasePluginSettingPanel instance = null;
        instance.removePropertyChangeListener(prop, l);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of onChenge method, of class RSBasePluginSettingPanel.
     */
    @Test
    public void testOnChenge() {
        System.out.println("onChenge");
        Adapter<Boolean, Boolean> adptr = null;
        RSBasePluginSettingPanel instance = null;
        instance.onChenge(adptr);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}