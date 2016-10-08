/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import java.io.File;
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
public class TemplateInformationTest {

    /**
     *
     */
    public TemplateInformationTest() {
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
    public void testSetAndGetName() {
        System.out.println("set and get name");
        TemplateHeader info = new TemplateHeader();
        String name = "nyanco";
        info.setName(name);
        assertEquals(name, info.getName());
    }

    /**
     *
     */
    @Test
    public void testSetAndGetName_constractor() {
        System.out.println("set and get name with constractor");
        String defaultName = "nyanco";
        TemplateHeader info = new TemplateHeader(defaultName);
        assertEquals(defaultName, info.getName());
        String setName = "conyanco";
        info.setName(setName);
        assertEquals(setName, info.getName());
    }

    /**
     *
     */
    @Test
    public void testSetName() {
        System.out.println("set name");
        TemplateHeader info = new TemplateHeader();
        String name = "nyanco";
        info.setName(name);
        assertEquals(name, info.getName());
        String otherName = "tama";
        info.setName(otherName);
        assertEquals(otherName, info.getName());
    }

    /**
     *
     */
    @Test
    public void testGetName() {
        System.out.println("get name");
        TemplateHeader info = new TemplateHeader();
        String nullName = null;
        assertEquals(nullName, info.getName());
        String name = "kuro";
        info.setName(name);
        assertEquals(name, info.getName());
    }

    /**
     *
     */
    @Test
    public void testSetAndGetCreator() {
        System.out.println("set and get Creator");
        TemplateHeader info = new TemplateHeader();
        String Creator = "nyanco";
        info.setCreator(Creator);
        assertEquals(Creator, info.getCreator());
    }

    /**
     *
     */
    @Test
    public void testSetAndGetCreator_constractor() {
        System.out.println("set and get Creator with constractor");
        String defaultCreator = "nyanco";
        TemplateHeader info = new TemplateHeader(null, defaultCreator);
        assertEquals(defaultCreator, info.getCreator());
        String setCreator = "conyanco";
        info.setCreator(setCreator);
        assertEquals(setCreator, info.getCreator());
    }

    /**
     *
     */
    @Test
    public void testSetCreator() {
        System.out.println("set Creator");
        TemplateHeader info = new TemplateHeader();
        String creator = "nyanco";
        info.setCreator(creator);
        assertEquals(creator, info.getCreator());
        String otherCreator = "tama";
        info.setCreator(otherCreator);
        assertEquals(otherCreator, info.getCreator());
    }

    /**
     *
     */
    @Test
    public void testGetCreator() {
        System.out.println("get Creator");
        TemplateHeader info = new TemplateHeader();
        String nullCreator = null;
        assertEquals(nullCreator, info.getCreator());
        String creator = "kuro";
        info.setCreator(creator);
        assertEquals(creator, info.getCreator());
    }

    /**
     *
     */
    @Test
    public void testSetAndGetPath() {
        System.out.println("set and get path with constractor");
        File defaultPath = new File("nyanco");
        TemplateHeader info = new TemplateHeader(null, null, defaultPath);
        assertEquals(defaultPath, info.getPath());
        File setCreator = new File("conyanco");
        info.setPath(setCreator);
        assertEquals(setCreator, info.getPath());
    }

    /**
     *
     */
    @Test
    public void testSetPath() {
        System.out.println("set file path");
        TemplateHeader info = new TemplateHeader();
        File path = new File("nyanco");
        info.setPath(path);
        assertEquals(path, info.getPath());
        File otherPath = new File("tama");
        info.setPath(otherPath);
        assertEquals(otherPath, info.getPath());
    }

    /**
     *
     */
    @Test
    public void testGetPath() {
        System.out.println("get file path");
        TemplateHeader info = new TemplateHeader();
        File nullPath = null;
        assertEquals(nullPath, info.getPath());
        File path = new File("kuro");
        info.setPath(path);
        assertEquals(path, info.getPath());
    }
}
