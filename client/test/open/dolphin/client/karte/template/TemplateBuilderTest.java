/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author tomohiro
 */
public class TemplateBuilderTest {

    /**
     *
     */
    public TemplateBuilderTest() {
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
     * Test of setBody method, of class TemplateBuilder.
     */
    @Test
    public void testSetBody() {
        System.out.println("setBody");
        String templateBody = "nyanco";
        TemplateBuilder instance = new TemplateBuilder();
        instance.setBody(templateBody);
        Element body = (Element)instance.getRoot().getElementsByTagName("body").item(0);
        assertNotNull(body);
        assertTrue(body.hasChildNodes());
        assertEquals(templateBody, body.getFirstChild().getTextContent());
    }

    /**
     * Test of getBody method, of class TemplateBuilder.
     */
    @Test
    public void testGetBody() {
        System.out.println("getBody");
        String templateBody = "nyanco";
        TemplateBuilder instance = new TemplateBuilder();
        instance.setBody(templateBody);
        String body = instance.getBody();
        assertEquals(templateBody, body);
    }

    /**
     * Test of setHeader method, of class TemplateBuilder.
     */
    @Test
    public void testSetHeader() {
        System.out.println("setHeader");
        String name    = "nyanco";
        String creator = "wanco";
        TemplateHeader templateHeader = new TemplateHeader(name, creator);
        TemplateBuilder instance = new TemplateBuilder();
        instance.setHeader(templateHeader);
        Element header = (Element)instance.getRoot().getElementsByTagName("header").item(0);
        assertNotNull(header);
        assertTrue(header.hasChildNodes());
        assertEquals(name, header.getElementsByTagName("name").item(0).getTextContent());
        assertEquals(creator, header.getElementsByTagName("creator").item(0).getTextContent());
    }

    /**
     * Test of getHeader method, of class TemplateBuilder.
     */
    @Test
    public void testGetHeader() {
        System.out.println("getHeader");
        String name    = "nyanco";
        String creator = "wanco";
        TemplateHeader templateHeader = new TemplateHeader(name, creator);
        TemplateBuilder instance = new TemplateBuilder();
        instance.setHeader(templateHeader);
        TemplateHeader header = instance.getHeader();
        assertEquals(templateHeader.getName(), header.getName());
        assertEquals(templateHeader.getCreator(), header.getCreator());
    }

    /**
     * Test of setRoot method, of class TemplateBuilder.
     */
    @Test
    public void testSetAndGetRoot() {
        System.out.println("set and get root");
        Document root = null;
        TemplateBuilder instance = new TemplateBuilder();
        Document first = instance.getRoot();
        assertNotNull(first);
        instance.setRoot(root);
        Document second = instance.getRoot();
        assertNotNull(second);
        assertNotSame(first, second);
    }

    /**
     * Test of getString method, of class TemplateBuilder.
     * @throws Exception
     */
    @Test
    public void testGetString() throws Exception {
        System.out.println("getString");
        TemplateBuilder instance = new TemplateBuilder();
        String name = "nyanco";
        String creator = "wanco";
        TemplateHeader header = new TemplateHeader(name, creator);
        instance.setHeader(header);
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            data.append("wanco nyanco usanyanco goronyan");
            data.append(i);
            data.append(System.getProperty("line.separator"));

        }
        instance.setBody(data.toString());
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+ System.getProperty("line.separator") 
                +"<template version=\"1.0\">"+ System.getProperty("line.separator")
                + "<header>"+ System.getProperty("line.separator")
                + "<name>nyanco</name>"+ System.getProperty("line.separator")
                + "<creator>wanco</creator>"+ System.getProperty("line.separator")
                + "</header>"+ System.getProperty("line.separator")
                + "<body><![CDATA[wanco nyanco usanyanco goronyan0"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan1"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan2"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan3"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan4"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan5"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan6"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan7"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan8"+ System.getProperty("line.separator")
                + "wanco nyanco usanyanco goronyan9"+ System.getProperty("line.separator")
                + "]]></body>"+ System.getProperty("line.separator")
                + "</template>"
                + "";
        String result = instance.getString();
        assertEquals(expResult, result);
    }

}