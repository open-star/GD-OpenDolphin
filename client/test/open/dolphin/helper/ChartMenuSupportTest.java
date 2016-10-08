/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

//import open.dolphin.helper.IChartCommandAccepter;
import open.dolphin.helper.IChartCommandAccepter.ChartCommand;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

class MockChartCommandAccepter implements IChartCommandAccepter {

    private boolean dummyResult;
    private ChartCommand command;
    private boolean isCalled;

    public MockChartCommandAccepter() {
        dummyResult = true;
        isCalled = false;
    }

    @Override
    public boolean dispatchChartCommand(ChartCommand command) {

        this.command = command;
        this.isCalled = true;

        return dummyResult;
    }

    public ChartCommand getResult() throws Exception {

        if (isCalled) {
            return command;
        }

        throw new Exception("Not called yet.");
    }
}

/**
 *
 * @author tomohiro
 */
public class ChartMenuSupportTest {

    /**
     *
     */
    public ChartMenuSupportTest() {
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
     * Test of menuSelected method, of class ChartMenuSupport.
     */
    @Test
    public void testMenuSelected() {
//		System.out.println("menuSelected");
//
//		MockChartCommandAccepter mock = new MockChartCommandAccepter();
//		ChartMenuSupport instance = new ChartMenuSupport(mock);
//		instance.menuSelected(null);
//
//		try {
//			mock.getResult();
//		}
//		catch (Exception e) {
//			assert(true);
//			return;
//		}
//
//		fail();
    }

    /**
     * Test of menuDeselected method, of class ChartMenuSupport.
     */
    @Test
    public void testMenuDeselected() {
//		System.out.println("menuDeselected");
//
//		MockChartCommandAccepter mock = new MockChartCommandAccepter();
//		ChartMenuSupport instance = new ChartMenuSupport(mock);
//		instance.menuDeselected(null);
//
//		try {
//			mock.getResult();
//		}
//		catch (Exception e) {
//			assert(true);
//			return;
//		}
//
//		fail();
    }

    /**
     * Test of menuCanceled method, of class ChartMenuSupport.
     */
    @Test
    public void testMenuCanceled() {
//		System.out.println("menuCanceled");
//
//		MockChartCommandAccepter mock = new MockChartCommandAccepter();
//		ChartMenuSupport instance = new ChartMenuSupport(mock);
//		instance.menuCanceled(null);
//
//		try {
//			mock.getResult();
//		}
//		catch (Exception e) {
//			assert(true);
//			return;
//		}
//
//		fail();
    }

    /**
     * Test of registerActions method, of class ChartMenuSupport.
     */
    @Test
    public void testRegisterActions() {
//		System.out.println("registerActions");
//		ActionMap actions = null;
//		ChartMenuSupport instance = null;
//		//instance.registerActions(actions);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of getAction method, of class ChartMenuSupport.
     */
    @Test
    public void testGetAction() {
//		System.out.println("getAction");
//		String name = "";
//		ChartMenuSupport instance = null;
//		Action expResult = null;
//		//Action result = instance.getAction(name);
//		//assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of getActions method, of class ChartMenuSupport.
     */
    @Test
    public void testGetActions() {
//		System.out.println("getActions");
//		ChartMenuSupport instance = null;
//		ActionMap expResult = null;
//		//ActionMap result = instance.getActions();
//		//assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of disableAllMenus method, of class ChartMenuSupport.
     */
    @Test
    public void testDisableAllMenus() {
//		System.out.println("disableAllMenus");
//		ChartMenuSupport instance = null;
//		//instance.disableAllMenus();
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of disableMenus method, of class ChartMenuSupport.
     */
    @Test
    public void testDisableMenus() {
//		System.out.println("disableMenus");
//		String[] menus = null;
//		ChartMenuSupport instance = null;
//		//instance.disableMenus(menus);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of enableMenus method, of class ChartMenuSupport.
     */
    @Test
    public void testEnableMenus() {
//		System.out.println("enableMenus");
//		String[] menus = null;
//		ChartMenuSupport instance = null;
//		///instance.enableMenus(menus);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of enabledAction method, of class ChartMenuSupport.
     */
    @Test
    public void testEnabledAction() {
//		System.out.println("enabledAction");
//		String name = "";
//		boolean enabled = false;
///		ChartMenuSupport instance = null;
//		//instance.enabledAction(name, enabled);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of setAccepter and getLast method, of class ChartMenuSupport.
     */
    @Test
    public void testSetAndGetLast() {
        System.out.println("setLast and getLast");

        IChartCommandAccepter obj = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(null);
        instance.setAccepter(obj);
        assertEquals(obj, instance.getLast());
    }

    /**
     * Test of setAccepter and getLast method, of class ChartMenuSupport.
     */
    @Test
    public void testSetAndGetLastUniq() {
        System.out.println("setLast and getLast");

        IChartCommandAccepter obj1 = new MockChartCommandAccepter();
        IChartCommandAccepter obj2 = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(null);

        instance.setAccepter(obj1);
        assertEquals(2, instance.chainSize());
        instance.setAccepter(obj2);
        assertEquals(3, instance.chainSize());
        instance.setAccepter(obj1);
        assertEquals(3, instance.chainSize());

        assertEquals(obj1, instance.getLast());
    }

    /**
     *
     */
    @Test
    public void testUnsetAccepter() {
        System.out.println("unsetAccepter");

        IChartCommandAccepter obj1 = new MockChartCommandAccepter();
        IChartCommandAccepter obj2 = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(null);

        instance.setAccepter(obj1);
        instance.setAccepter(obj2);
        assertEquals(3, instance.chainSize());
        assertEquals(obj2, instance.getLast());

        assertEquals(true, instance.unsetAccepter(obj1));
        assertEquals(2, instance.chainSize());
        assertEquals(obj2, instance.getLast());

        assertEquals(false, instance.unsetAccepter(obj1));
    }

    /**
     *
     */
    @Test
    public void testChainSize() {
        System.out.println("chainSize");

        // MEMO: Constractor adds thatself two Accepter (owner and this)
        ChartMenuSupport instance = new ChartMenuSupport(null);
        instance.setAccepter(new MockChartCommandAccepter());
        assertEquals(2, instance.chainSize());
        instance.setAccepter(new MockChartCommandAccepter());
        assertEquals(3, instance.chainSize());
    }

    /**
     *
     */
    @Test
    public void testExeuteOrder() {
        System.out.println("test execute orer");

        MockChartCommandAccepter owner = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(owner);

        MockChartCommandAccepter mock1 = new MockChartCommandAccepter();
        MockChartCommandAccepter mock2 = new MockChartCommandAccepter();

        instance.setAccepter(mock1);
        instance.setAccepter(mock2);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.newKarte;

        boolean result = instance.newKarteCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock2.getResult());
        } catch (Exception e) {
        }

        try {
            assertEquals(expCommand, mock1.getResult());
            fail();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * Test of newKarteCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testNewKarteCommandExecute() {
        System.out.println("newKarteCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.newKarte;

        boolean result = instance.newKarteCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of newDocumentCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testNewDocumentCommandExecute() {
        System.out.println("newDocumentCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.newDocument;

        boolean result = instance.newDocumentCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of closeCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testCloseCommandExecute() {
        System.out.println("closeCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.close;

        boolean result = instance.closeCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of saveCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testSaveCommandExecute() {
        System.out.println("saveCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.save;

        boolean result = instance.saveCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of deleteCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testDeleteCommandExecute() {
        System.out.println("deleteCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.delete;

        boolean result = instance.deleteCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of directionCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testDirectionCommandExecute() {
        System.out.println("directionCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.direction;

        boolean result = instance.directionCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of printCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testPrintCommandExecute() {
        System.out.println("printCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.print;

        boolean result = instance.printCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of modifyKarteCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testModifyKarteCommandExecute() {
        System.out.println("modifyKarteCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.modifyKarte;

        boolean result = instance.modifyKarteCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of undoCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testUndoCommandExecute() {
        System.out.println("undoCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.undo;

        boolean result = instance.undoCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of redoCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testRedoCommandExecute() {
        System.out.println("redoCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.redo;

        boolean result = instance.redoCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of letterPasteCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testLetterPasteCommandExecute() {
        System.out.println("letterPasteCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.letterPaste;

        boolean result = instance.letterPasteCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of showModifiedCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testShowModifiedCommandExecute() {
        System.out.println("showModifiedCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.showModified;

        boolean result = instance.showModifiedCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of hideModifiedCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testHideModifiedCommandExecute() {
        System.out.println("hideModifiedCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.hideModified;

        boolean result = instance.hideModifiedCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of showUnsendCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testShowUnsendCommandExecute() {
        System.out.println("showUnsendCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.showUnsend;

        boolean result = instance.showUnsendCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of hideUnsendCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testHideUnsendCommandExecute() {
        System.out.println("hideUnsendCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.hideUnsend;

        boolean result = instance.hideUnsendCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of showSendCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testShowSendCommandExecute() {
        System.out.println("showSendCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.showSend;

        boolean result = instance.showSendCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of hideSendCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testHideSendCommandExecute() {
        System.out.println("hideSendCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.hideSend;

        boolean result = instance.hideSendCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of ascendingCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testAscendingCommandExecute() {
        System.out.println("ascendingCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.ascending;

        boolean result = instance.ascendingCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of descendingCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testDescendingCommandExecute() {
        System.out.println("descendingCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.descending;

        boolean result = instance.descendingCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontLargerCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontLargerCommandExecute() {
        System.out.println("fontLargerCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontLarger;

        boolean result = instance.fontLargerCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontSmallerCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontSmallerCommandExecute() {
        System.out.println("fontSmallerCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontSmaller;

        boolean result = instance.fontSmallerCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontStandardCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontStandardCommandExecute() {
        System.out.println("fontStandardCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontStandard;

        boolean result = instance.fontStandardCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontBoldCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontBoldCommandExecute() {
        System.out.println("fontBoldCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontBold;

        boolean result = instance.fontBoldCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontItalicCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontItalicCommandExecute() {
        System.out.println("fontItalicCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontItalic;

        boolean result = instance.fontItalicCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontUnderlineCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontUnderlineCommandExecute() {
        System.out.println("fontUnderlineCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontUnderline;

        boolean result = instance.fontUnderlineCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of leftJustifyCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testLeftJustifyCommandExecute() {
        System.out.println("leftJustifyCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.leftJustify;

        boolean result = instance.leftJustifyCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of centerJustifyCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testCenterJustifyCommandExecute() {
        System.out.println("centerJustifyCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.centerJustify;

        boolean result = instance.centerJustifyCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of rightJustifyCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testRightJustifyCommandExecute() {
        System.out.println("rightJustifyCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.rightJustify;

        boolean result = instance.rightJustifyCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontRedCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontRedCommandExecute() {
        System.out.println("fontRedCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontRed;

        boolean result = instance.fontRedCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontOrangeCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontOrangeCommandExecute() {
        System.out.println("fontOrangeCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontOrange;

        boolean result = instance.fontOrangeCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontYellowCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontYellowCommandExecute() {
        System.out.println("fontYellowCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontYellow;

        boolean result = instance.fontYellowCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontGreenCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontGreenCommandExecute() {
        System.out.println("fontGreenCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontGreen;

        boolean result = instance.fontGreenCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontBlueCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontBlueCommandExecute() {
        System.out.println("fontBlueCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontBlue;

        boolean result = instance.fontBlueCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontPurpleCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontPurpleCommandExecute() {
        System.out.println("fontPurpleCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontPurple;

        boolean result = instance.fontPurpleCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontGrayCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontGrayCommandExecute() {
        System.out.println("fontGrayCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontGray;

        boolean result = instance.fontGrayCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of fontBlackCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testFontBlackCommandExecute() {
        System.out.println("fontBlackCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.fontBlack;

        boolean result = instance.fontBlackCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of resetStyleCommandExecute method, of class ChartMenuSupport.
     */
    @Test
    public void testResetStyleCommandExecute() {
        System.out.println("resetStyleCommandExecute");

        MockChartCommandAccepter mock = new MockChartCommandAccepter();
        ChartMenuSupport instance = new ChartMenuSupport(mock);

        boolean expResult = true;
        ChartCommand expCommand = ChartCommand.resetStyle;

        boolean result = instance.resetStyleCommandExecute();

        assertEquals(expResult, result);

        try {
            assertEquals(expCommand, mock.getResult());
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test of cut method, of class ChartMenuSupport.
     */
    @Test
    public void testCut() {
//		System.out.println("cut");
//		ChartMenuSupport instance = null;
//		instance.cut();
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of copy method, of class ChartMenuSupport.
     */
    @Test
    public void testCopy() {
//		System.out.println("copy");
//		ChartMenuSupport instance = null;
//		instance.copy();
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of paste method, of class ChartMenuSupport.
     */
    @Test
    public void testPaste() {
//		System.out.println("paste");
//		ChartMenuSupport instance = null;
//		instance.paste();
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }

    /**
     * Test of dispatchChartCommand method, of class ChartMenuSupport.
     */
    @Test
    public void testDispatchChartCommand() {
//		System.out.println("dispatchChartCommand");
//		ChartCommand command = null;
//		ChartMenuSupport instance = null;
//		boolean expResult = false;
//		boolean result = instance.dispatchChartCommand(command);
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case has not implemented yet.");
    }
}
