/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.infomodel;

import open.dolphin.utils.CombinedStringParser;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tomohiro
 */
public class CombinedStringParserTest {
    
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
	 * Test of limit method, of class CombinedStringParser.
	 * FIXME: It should not use toCombinedString
	 */
    @Test
	public void testSetLimit() {
		System.out.println("setlimit");
		int number = 3;
		CombinedStringParser instance = new CombinedStringParser("5.4.3.2.1");
		assertEquals(5, instance.size());
		instance.limit(number);
		assertEquals(3, instance.size());
		assertEquals("5.4.3", instance.toCombinedString());

	}

	/**
	 * Test of get method, of class CombinedStringParser.
	 */
    @Test
	public void testGet() {
		System.out.println("get");
		CombinedStringParser instance = new CombinedStringParser("5.4.3.2.1");
		String[] expResults = {"5", "4", "3", "2", "1"};
		for (int index = 0; index < instance.size(); index++) {
			String result = instance.get(index);
			assertEquals(expResults[index], result);
		}
	}

	/**
	 * Test of get method, of class CombinedStringParser.
	 * Get from out of index
	 */
    @Test
	public void testGetOut() {
		System.out.println("get");
		int index = 50;
		String expResult = "";
		CombinedStringParser instance = new CombinedStringParser("5.4.3.2.1");
		String result = instance.get(index);
		assertEquals(expResult, result);
	}

	/**
	 * Test of set method, of class CombinedStringParser.
	 * FIXME: It should not use toCombinedString
	 */
    @Test
	public void testSet() {
		System.out.println("set");
		int index = 2;
		String value = "9";
		CombinedStringParser instance = new CombinedStringParser("5.4.3.2.1");
		String expResult = "3";
		String result = instance.set(index, value);
		assertEquals(expResult, result);
		assertEquals("5.4.9.2.1", instance.toCombinedString());
	}

	/**
	 * Test of set method, of class CombinedStringParser.
	 * FIXME: It should not use toCombinedString
	 */
    @Test
	public void testSetOut() {
		System.out.println("set into out of size");
		int index = 9;
		String value = "8";
		CombinedStringParser instance = new CombinedStringParser("5.4.3.2.1");
		String expResult = "";
		String result = instance.set(index, value);
		assertEquals(expResult, result);
		assertEquals("5.4.3.2.1.....8", instance.toCombinedString());
	}

	/**
	 * Test of toPlainString method, of class CombinedStringParser.
	 */
    @Test
	public void testToPlainString() {
		System.out.println("toPlainString");
		CombinedStringParser instance = new CombinedStringParser("5.4.3.2.1");
		String expResult = "54321";
		String result = instance.toPlainString();
		assertEquals(expResult, result);
	}

	/**
	 * Test of toCombinedString method, of class CombinedStringParser.
	 */
    @Test
	public void testToCombinedString() {
		System.out.println("toCombinedString");
		CombinedStringParser instance = new CombinedStringParser("5.4.3.2.1");
		String expResult = "5.4.3.2.1";
		String result = instance.toCombinedString();
		assertEquals(expResult, result);
	}

}
