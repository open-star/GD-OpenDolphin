/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.karte.template;

import open.dolphin.log.LogWriter;
import open.dolphin.client.karte.template.error.CantReadTemplateException;
import open.dolphin.infomodel.DocumentModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

class MockDocumentModel extends DocumentModel {
}

class MockTemplateManager extends TemplateManager {

    public Template caller;

    @Override
    public void readTemplate(Template template) {
        this.caller = template;
    }
}

/**
 *
 * @author tomohiro
 */
public class TemplateTest {

    /**
     *
     */
    public TemplateTest() {
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
    public void testGetBody_null() {
        System.out.println("getBody null");
        MockTemplateManager manager = new MockTemplateManager();
        Template template = new Template(manager);
        Template expected = template;
        try {
            template.getBody();
        } catch (CantReadTemplateException ex) {
            LogWriter.error(this.getClass(), "", ex);
            fail();
        }
        assertEquals(expected, manager.caller);
    }

    /**
     *
     */
    @Test
    public void testGetBody_not_null() {
        System.out.println("getBody not null");
        MockTemplateManager manager = new MockTemplateManager();
        Template template = new Template(manager);
        template.setBody(new MockDocumentModel());
        try {
            template.getBody();
        } catch (CantReadTemplateException ex) {
            LogWriter.error(this.getClass(), "", ex);
            fail();
        }
        assertNull(manager.caller);
    }

    /**
     *
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        MockTemplateManager manager = new MockTemplateManager();
        Template template = new Template(manager);
        TemplateHeader header = new TemplateHeader();
        String expected = "nyanco";
        header.setName(expected);
        template.setHeader(header);
        assertEquals(expected, template.toString());
    }
}
