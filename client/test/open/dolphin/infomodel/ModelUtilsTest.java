/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.infomodel;


import open.dolphin.utils.AgeCalculator;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kenji
 */
public class ModelUtilsTest {
    
    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test of trimTime method, of class ModelUtils.
     */
    // @Test
    public void testTrimTime() {
        System.out.println("trimTime");
        String mmlDate = "";
        String expResult = "";
        String result = ModelUtils.trimTime(mmlDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of trimDate method, of class ModelUtils.
     */
    // @Test
    public void testTrimDate() {
        System.out.println("trimDate");
        String mmlDate = "";
        String expResult = "";
        String result = ModelUtils.trimDate(mmlDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAgeBirthday method, of class ModelUtils.
     */
    // @Test
    public void testGetAgeBirthday() {
        System.out.println("getAgeBirthday");
        String mmlBirthday = "";
        String expResult = "";
        String result = ModelUtils.getAgeBirthday(mmlBirthday);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAge method, of class ModelUtils.
     */
    // @Test
    public void testGetAge() {
        System.out.println("getAge");
        String mmlBirthday = "";
        String expResult = "";
        String result = AgeCalculator.getAge(mmlBirthday);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCalendar method, of class ModelUtils.
     */
    // @Test
    public void testGetCalendar() {
        System.out.println("getCalendar");
        String mmlDate = "";
        GregorianCalendar expResult = null;
        GregorianCalendar result = ModelUtils.getCalendar(mmlDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateAsObject method, of class ModelUtils.
     */
    // @Test
    public void testGetDateAsObject() {
        System.out.println("getDateAsObject");
        String mmlDate = "";
        Date expResult = null;
        Date result = ModelUtils.getDateAsObject(mmlDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateTimeAsObject method, of class ModelUtils.
     */
    // @Test
    public void testGetDateTimeAsObject() {
        System.out.println("getDateTimeAsObject");
        String mmlDate = "";
        Date expResult = null;
        Date result = ModelUtils.getDateTimeAsObject(mmlDate);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateAsString method, of class ModelUtils.
     */
    // @Test
    public void testGetDateAsString() {
        System.out.println("getDateAsString");
        Date date = null;
        String expResult = "";
        String result = ModelUtils.getDateAsString(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateTimeAsString method, of class ModelUtils.
     */
    // @Test
    public void testGetDateTimeAsString() {
        System.out.println("getDateTimeAsString");
        Date date = null;
        String expResult = "";
        String result = ModelUtils.getDateTimeAsString(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateAsFormatString method, of class ModelUtils.
     */
    // @Test
    public void testGetDateAsFormatString() {
        System.out.println("getDateAsFormatString");
        Date date = null;
        String format = "";
        String expResult = "";
        String result = ModelUtils.getDateAsFormatString(date, format);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDateFromString method, of class ModelUtils.
     */
    @Test
    public void testGetDateFromString() {
        System.out.println("getDateFromString");
        String source = "20090909";
        String format = "yyyyMMdd";
        Calendar cal = new GregorianCalendar(2009, Calendar.SEPTEMBER, 9);
        Date expected = cal.getTime();
        Date result = ModelUtils.getDateFromString(source, format);
        assertEquals(expected, result);
    }

    /**
     * Test of getGenderDesc method, of class ModelUtils.
     */
    // @Test
    public void testGetGenderDesc() {
        System.out.println("getGenderDesc");
        String gender = "";
        String expResult = "";
        String result = ModelUtils.getGenderDesc(gender);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMemo method, of class ModelUtils.
     */
    // @Test
    public void testGetMemo() {
        System.out.println("getMemo");
        String memo = "";
        String expResult = "";
        String result = ModelUtils.getMemo(memo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValidModel method, of class ModelUtils.
     */
    // @Test
    public void testIsValidModel() {
        System.out.println("isValidModel");
        ModelUtils instance = new ModelUtils();
        boolean expResult = false;
        boolean result = instance.isValidModel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of splitDiagnosis method, of class ModelUtils.
     */
    // @Test
    public void testSplitDiagnosis() {
        System.out.println("splitDiagnosis");
        String diagnosis = "";
        String[] expResult = null;
        String[] result = ModelUtils.splitDiagnosis(diagnosis);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDiagnosisName method, of class ModelUtils.
     */
    // @Test
    public void testGetDiagnosisName() {
        System.out.println("getDiagnosisName");
        String hasAlias = "";
        String expResult = "";
        String result = ModelUtils.getDiagnosisName(hasAlias);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDiagnosisAlias method, of class ModelUtils.
     */
    // @Test
    public void testGetDiagnosisAlias() {
        System.out.println("getDiagnosisAlias");
        String hasAlias = "";
        String expResult = "";
        String result = ModelUtils.getDiagnosisAlias(hasAlias);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
