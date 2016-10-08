/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client;

import java.awt.print.PageFormat;
import java.util.List;
import open.dolphin.client.IChartDocument.TYPE;
import open.dolphin.helper.IChartCommandAccepter.ChartCommand;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.sendclaim.SendClaimImpl;
import open.dolphin.sendmml.SendMmlImpl;
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
public class KarteEditorTest {

    public KarteEditorTest() {
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
     * Test of getType method, of class KarteEditor.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        KarteEditor instance = null;
        TYPE expResult = null;
        TYPE result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setOpenFrame method, of class KarteEditor.
     */
    @Test
    public void testSetOpenFrame() {
        System.out.println("setOpenFrame");
        boolean isOpenFrame = false;
        KarteEditor instance = null;
        instance.setOpenFrame(isOpenFrame);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMode method, of class KarteEditor.
     */
    @Test
    public void testGetMode() {
        System.out.println("getMode");
        KarteEditor instance = null;
        int expResult = 0;
        int result = instance.getMode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setMode method, of class KarteEditor.
     */
    @Test
    public void testSetMode() {
        System.out.println("setMode");
        int mode = 0;
        KarteEditor instance = null;
        instance.setMode(mode);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setModel method, of class KarteEditor.
     */
    @Test
    public void testSetModel() {
        System.out.println("setModel");
        DocumentModel model = null;
        KarteEditor instance = null;
        instance.setModel(model);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setIsHospital method, of class KarteEditor.
     */
    @Test
    public void testSetIsHospital() {
        System.out.println("setIsHospital");
        boolean isHospital = false;
        KarteEditor instance = null;
        instance.setIsHospital(isHospital);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of printPanel2 method, of class KarteEditor.
     */
    @Test
    public void testPrintPanel2() {
        System.out.println("printPanel2");
        PageFormat format = null;
        KarteEditor instance = null;
        instance.printPanel2(format);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getSOAPane method, of class KarteEditor.
     */
    @Test
    public void testGetSOAPane() {
        System.out.println("getSOAPane");
        KarteEditor instance = null;
        KartePane expResult = null;
        KartePane result = instance.getSOAPane();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getPPane method, of class KarteEditor.
     */
    @Test
    public void testGetPPane() {
        System.out.println("getPPane");
        KarteEditor instance = null;
        KartePane expResult = null;
        KartePane result = instance.getPPane();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setEditable method, of class KarteEditor.
     */
    @Test
    public void testSetEditable() {
        System.out.println("setEditable");
        boolean newState = false;
        KarteEditor instance = null;
        instance.setEditable(newState);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addMMLListner method, of class KarteEditor.
     */
    @Test
    public void testAddMMLListner() throws Exception {
        System.out.println("addMMLListner");
        SendMmlImpl listener = null;
        KarteEditor instance = null;
        instance.addMMLListner(listener);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of addCLAIMListner method, of class KarteEditor.
     */
    @Test
    public void testAddCLAIMListner() throws Exception {
        System.out.println("addCLAIMListner");
        SendClaimImpl listener = null;
        KarteEditor instance = null;
        instance.addCLAIMListner(listener);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of removeCLAIMListener method, of class KarteEditor.
     */
    @Test
    public void testRemoveCLAIMListener() {
        System.out.println("removeCLAIMListener");
        SendClaimImpl listener = null;
        KarteEditor instance = null;
        instance.removeCLAIMListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setModify method, of class KarteEditor.
     */
    @Test
    public void testSetModify() {
        System.out.println("setModify");
        boolean newState = false;
        KarteEditor instance = null;
        instance.setModify(newState);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of enter method, of class KarteEditor.
     */
    @Test
    public void testEnter() {
        System.out.println("enter");
        KarteEditor instance = null;
        instance.enter();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setDirty method, of class KarteEditor.
     */
    @Test
    public void testSetDirty() {
        System.out.println("setDirty");
        boolean dirty = false;
        KarteEditor instance = null;
        instance.setDirty(dirty);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of itLayoutSaved method, of class KarteEditor.
     */
    @Test
    public void testItLayoutSaved() {
        System.out.println("itLayoutSaved");
        KarteEditor instance = null;
        boolean expResult = false;
        boolean result = instance.itLayoutSaved();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isDirty method, of class KarteEditor.
     */
    @Test
    public void testIsDirty() {
        System.out.println("isDirty");
        KarteEditor instance = null;
        boolean expResult = false;
        boolean result = instance.isDirty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of initialize method, of class KarteEditor.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        KarteEditor instance = null;
        instance.initialize();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of start method, of class KarteEditor.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        KarteEditor instance = null;
        instance.start();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of stop method, of class KarteEditor.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        KarteEditor instance = null;
        instance.stop();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of dispatchChartCommand method, of class KarteEditor.
     */
    @Test
    public void testDispatchChartCommand() {
        System.out.println("dispatchChartCommand");
        ChartCommand command = null;
        KarteEditor instance = null;
        boolean expResult = false;
        boolean result = instance.dispatchChartCommand(command);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of prepare method, of class KarteEditor.
     */
    @Test
    public void testPrepare() {
        System.out.println("prepare");
        KarteEditor instance = null;
        boolean expResult = false;
        boolean result = instance.prepare();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of save method, of class KarteEditor.
     */
    @Test
    public void testSave() {
        System.out.println("save");
        KarteEditor instance = null;
        boolean expResult = false;
        boolean result = instance.save();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of writeShokenToFile method, of class KarteEditor.
     */
    @Test
    public void testWriteShokenToFile() {
        System.out.println("writeShokenToFile");
        KarteEditor instance = null;
        boolean expResult = false;
        boolean result = instance.writeShokenToFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getActualModel method, of class KarteEditor.
     */
    @Test
    public void testGetActualModel() {
        System.out.println("getActualModel");
        KarteEditor instance = null;
        DocumentModel expResult = null;
        DocumentModel result = instance.getActualModel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getTitle method, of class KarteEditor.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
        KarteEditor instance = null;
        String expResult = "";
        String result = instance.getTitle();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getParentContext method, of class KarteEditor.
     */
    @Test
    public void testGetParentContext() {
        System.out.println("getParentContext");
        KarteEditor instance = null;
        IChart expResult = null;
        IChart result = instance.getParentContext();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of warning method, of class KarteEditor.
     */
    @Test
    public void testWarning() {
        System.out.println("warning");
        String title = "";
        String message = "";
        KarteEditor instance = null;
        instance.warning(title, message);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getTabbedPanels method, of class KarteEditor.
     */
    @Test
    public void testGetTabbedPanels() {
        System.out.println("getTabbedPanels");
        KarteEditor instance = null;
        List expResult = null;
        List result = instance.getTabbedPanels();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class KarteEditor.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Object o = null;
        KarteEditor instance = null;
        boolean expResult = false;
        boolean result = instance.update(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of setParent method, of class KarteEditor.
     */
    @Test
    public void testSetParent() {
        System.out.println("setParent");
        IChart parent = null;
        KarteEditor instance = null;
        instance.setParent(parent);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

}