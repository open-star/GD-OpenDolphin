/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import open.dolphin.client.karte.template.error.CantReadTemplateException;
import open.dolphin.client.karte.template.error.CantWriteTemplateException;
import java.io.File;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.UserModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

class MockDocumentModel extends DocumentModel {
}

class MockTemplateReader implements TemplateReadable {

    public int readHeaderCallCount;
    public int readBodyCallCount;

    public MockTemplateReader() {
        super();
        readHeaderCallCount = 0;
        readBodyCallCount = 0;
    }

    @Override
    public TemplateHeader readHeader(File path) {
        readHeaderCallCount++;
        return new TemplateHeader();
    }

    @Override
    public DocumentModel readBody(File path) {
        readBodyCallCount++;
        return new MockDocumentModel();
    }
}

class MockTemplateWriter implements TemplateWritable {

    public int writeHeaderCallCount;
    public int writeBodyCallCount;
    public int beginSessionCount;
    public int endSessionCount;

    public MockTemplateWriter() {
        writeHeaderCallCount = 0;
        writeBodyCallCount = 0;
        beginSessionCount = 0;
        endSessionCount = 0;
    }

    @Override
    public void writeHeader(TemplateHeader header) {
        writeHeaderCallCount++;
    }

    @Override
    public void writeBody(DocumentModel body) {
        writeBodyCallCount++;
    }

    @Override
    public void beginSession(File path) {
        beginSessionCount++;
    }

    @Override
    public void endSession() throws CantWriteTemplateException {
        endSessionCount++;
    }
}

class MockTemplateManager extends TemplateManager {

    private int size;
    public int callCount;

    public MockTemplateManager(File directory) {
        super(directory);
        this.callCount = 0;
    }

    public void setFileSize(int size) {
        this.size = size;
    }

    @Override
    protected File[] getFilesAsArray() {
        callCount++;

        File[] result = new File[this.size];
        for (int i = 0; i < this.size; i++) {
            result[i] = new File("hoge"+i);
        }
        return result;
    }
}

/**
 *
 * @author tomohiro
 */
public class TemplateManagerTest {

    MockTemplateManager manager;
    MockTemplateReader reader;
    MockTemplateWriter writer;

    /**
     *
     */
    public TemplateManagerTest() {
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
        reader = new MockTemplateReader();
        writer = new MockTemplateWriter();
        File directory = new File(".");
        manager = new MockTemplateManager(directory);
        manager.setReader(reader);
        manager.setWriter(writer);
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
    public void testCreateList_5() {
        System.out.println("createList 5");
        int expectedSize = 5;
        manager.setFileSize(expectedSize);
        manager.updateFileList();
        try {
            manager.createList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        assertEquals(expectedSize, manager.getList().size());
        assertEquals(expectedSize, reader.readHeaderCallCount);
        assertEquals(0, reader.readBodyCallCount);
    }

    /**
     *
     */
    @Test
    public void testCreateList_0() {
        System.out.println("createList 0");
        int expectedSize = 0;
        manager.setFileSize(expectedSize);
        manager.updateFileList();
        try {
            manager.createList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        assertEquals(expectedSize, manager.getList().size());
        assertEquals(expectedSize, reader.readHeaderCallCount);
        assertEquals(0, reader.readBodyCallCount);
    }

    /**
     *
     */
    @Test
    public void testCreateList_is_template() {
        System.out.println("createList is certain template");
        int expectedSize = 1;
        manager.setFileSize(expectedSize);
        manager.updateFileList();
        try {
            manager.createList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        Template template = manager.getList().get(0);
        assertNotNull(template);
        assertSame(Template.class, template.getClass());
        assertNotNull(template.getHeader());
        try {
            assertNull(template.getClass().getDeclaredField("body").get(template));
        }
        catch (IllegalArgumentException ex) {assert(true);}
        catch (IllegalAccessException ex) {assert(true);}
        catch (NoSuchFieldException ex) {assert(true);}
        catch (SecurityException ex) {assert(true);}
    }

    /**
     *
     */
    @Test
    public void testCreateList_template_knows_manager() {
        System.out.println("createList - template knows manager");
        int expectedSize = 1;
        manager.setFileSize(expectedSize);
        manager.updateFileList();
        try {
            manager.createList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        assertNotNull(manager.getList().get(0));
        assertSame(manager, manager.getList().get(0).getManager());
    }

    /**
     *
     */
    @Test
    public void testCreateList() {
        System.out.println("createList is certain template");
        int expectedSize = 1;
        int fileSize = 5;
        manager.setFileSize(fileSize);
        manager.updateFileList();
        try {
            manager.createList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        assertEquals(expectedSize, manager.callCount);
    }

    /**
     *
     */
    @Test
    public void testCreateList_zero_file() {
        System.out.println("createList is certain template when file is nothing");
        int fileSize = 0;
        manager.setFileSize(fileSize);
        manager.updateFileList();
        try {
            manager.createList();
        }
        catch (CantReadTemplateException ex) {
            fail();
        }
        catch (NullPointerException ex) {
            fail("Must not be null");
        }
        assertTrue(true);
    }

    /**
     *
     */
    @Test
    public void testUpdateList() {
        System.out.println("updateList");
        int expectedSize = 2;
        int fileSize = 5;
        manager.setFileSize(fileSize);
        manager.updateFileList();
        try {
            manager.updateList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        assertEquals(expectedSize, manager.callCount);
    }

    /**
     *
     */
    @Test
    public void testCreateList_path() {
        System.out.println("createList path");
        int expectedSize = 3;
        manager.setFileSize(expectedSize);
        manager.updateFileList();
        try {
            manager.createList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        int counter = 0;
        for (Template template : manager.getList()) {
            assertEquals("hoge" + counter, template.getHeader().getPath().toString());
            counter++;
        }
    }

    /**
     *
     */
    @Test
    public void testDefaultConstractor() {
        System.out.println("default constractor");
        TemplateManager templateManager = new TemplateManager();
        int expected = 0;
        assertEquals(expected, templateManager.getList().size());
    }

    /**
     *
     */
    @Test
    public void testBefoerReadTemplate() {
        System.out.println("before read template");
        int expectedSize = 3;
        manager.setFileSize(expectedSize);
        manager.updateFileList();
        try {
            manager.createList();
        } catch (CantReadTemplateException ex) {
            fail();
        }
        for (Template template : manager.getList()) {
            try {
                assertNull(template.getClass().getDeclaredField("body").get(template));
            }
            catch (IllegalArgumentException ex) {         assert(true);}
            catch (IllegalAccessException ex) {         assert(true);}
            catch (NoSuchFieldException ex) {        assert(true); }
            catch (SecurityException ex) {         assert(true);}
        }
    }

    /**
     *
     */
    @Test
    public void testAfterReadTemplate() {
        System.out.println("after read template");
        TemplateHeader header = new TemplateHeader();
        File path = new File("nyanco");
        header.setPath(path);
        Template template = new Template(null, header);
        try {
            assertNull(template.getClass().getDeclaredField("body").get(template));
        }
        catch (IllegalArgumentException ex) {        assert(true); }
        catch (IllegalAccessException ex) {        assert(true); }
        catch (NoSuchFieldException ex) {        assert(true); }
        catch (SecurityException ex) {         assert(true);}
        try {
            manager.readTemplate(template);
        } catch (CantReadTemplateException ex) {
            fail();
        }
        try {
            assertNotNull(template.getBody());
        }
        catch (CantReadTemplateException ex) {
            fail();
        }
        int expected = 1;
        assertEquals(expected, reader.readBodyCallCount);
    }

    /**
     *
     */
    @Test
    public void testWriteTemplate() {
        System.out.println("write template");
        final int expectedCallCount = 1;
        TemplateHeader header = new TemplateHeader();
        MockDocumentModel body = new MockDocumentModel();
        Template template = new Template(manager, header, body);
        try {
            manager.writeTemplate(template);
        } catch (Exception e) {         assert(true);}
        assertEquals(expectedCallCount, writer.writeHeaderCallCount);
        assertEquals(expectedCallCount, writer.writeBodyCallCount);
        assertEquals(expectedCallCount, writer.beginSessionCount);
        assertEquals(expectedCallCount, writer.endSessionCount);
    }

    /**
     *
     */
    @Test
    public void testCantWriteTemplate() {
        System.out.println("can't write template");
        try {
            manager.setWriter(new TemplateWritable() {

                @Override
                public void writeBody(DocumentModel document) throws CantWriteTemplateException {
                    throw new CantWriteTemplateException();
                }

                @Override
                public void writeHeader(TemplateHeader info) throws CantWriteTemplateException {
                    throw new CantWriteTemplateException();
                }

                @Override
                public void beginSession(File path) {
                }

                @Override
                public void endSession() throws CantWriteTemplateException {
                }
            });
            manager.writeTemplate(new Template(manager, new TemplateHeader()));
            fail();
        }
        catch (CantWriteTemplateException e) {
            assert(true);
        }
    }

    /**
     *
     */
    @Test
    public void testCreateTemplate() {
        System.out.println("create template");
        MockDocumentModel document = new MockDocumentModel();
        document.setCreator(new UserModel());
        Template template = null;
        try {
            template = manager.createTemplate(document);
        } catch (CantWriteTemplateException ex) {
            fail();
        }
        try {
            assertEquals(document, template.getBody());
        }
        catch (CantReadTemplateException ex) {
            fail();
        }
    }
}
