/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import open.dolphin.client.karte.template.error.CantWriteTemplateException;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import open.dolphin.client.karte.template.error.CantReadTemplateException;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.log.LogWriter;

/**
 *
 * @author tomohiro
 */
public class TemplateManager {

    /**
     *
     */
    public static final String EXTENSION = ".template";

    private File directory;
    private List<File> files;

    private List<Template> templateList;

    private TemplateReadable reader;
    private TemplateWritable writer;

    /**
     *
     */
    public TemplateManager() {
        this(new File("."));
    }

    /**
     *
     * @param directory
     */
    public TemplateManager(File directory) {
        this.directory = directory;
        this.files     = getFiles();

        setReader(new TemplateReader());
        setWriter(new TemplateWriter());

        this.templateList = new ArrayList<Template>();
    }

    /**
     *
     * @param reader
     */
    public void setReader(TemplateReadable reader) {
        this.reader = reader;
    }

    /**
     * 
     * @param writer
     */
    public void setWriter(TemplateWritable writer) {
        this.writer = writer;
    }

    /**
     *
     * @throws CantReadTemplateException
     */
    public void createList() throws CantReadTemplateException {
        for (File file : files) {
            TemplateHeader header = reader.readHeader(file);
            header.setPath(file);
            templateList.add(new Template(this, header));
        }
    }

    /**
     *
     * @throws CantReadTemplateException
     */
    public void updateList() throws CantReadTemplateException {
        updateFileList();
        createList();
    }

    /**
     *
     * @return
     */
    public List<Template> getList() {
        if (templateList.size() == 0) {
            try {
                createList();
            }
            catch (CantReadTemplateException ex) {
                LogWriter.error(this.getClass(), "テンプレートリストの作成に失敗しました" + System.getProperty("line.separator") + ex.getStackTrace());
            }
        }
        return this.templateList;
    }

    /**
     *
     * @param template
     * @throws CantReadTemplateException
     */
    public void readTemplate(Template template) throws CantReadTemplateException {
        template.setBody(reader.readBody(template.getHeader().getPath()));
    }

    /**
     *
     * @param template
     * @throws CantWriteTemplateException
     */
    public void writeTemplate(Template template) throws CantWriteTemplateException {
        writer.beginSession(template.getHeader().getPath());
        writer.writeHeader(template.getHeader());
        try {
            writer.writeBody(template.getBody());
        }
        catch (CantReadTemplateException ex) {
            throw new CantWriteTemplateException(ex);
        }
        writer.endSession();
    }

    /**
     *
     * @param body
     * @return
     * @throws CantWriteTemplateException
     */
    public Template createTemplate(DocumentModel body) throws CantWriteTemplateException {
        Template template = new Template(this);
        TemplateHeader header = new TemplateHeader();
        header.setName(body.getDocInfo().getTitle());
        header.setCreator(body.getCreator().getCommonName());
        String format = "yyyyMMddHHmmss'" + TemplateManager.EXTENSION + "'";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String fileName = formatter.format(new Date());
        header.setPath(new File(directory, fileName));
        template.setHeader(header);
        template.setBody(body);
        writeTemplate(template);
        return template;
    }

    private boolean isTemplateFile(File file) {
        return file.isFile() && file.getName().endsWith(TemplateManager.EXTENSION);
    }

    /**
     *
     * @return
     */
    protected File[] getFilesAsArray() {
        FileFilter templateFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return isTemplateFile(file);
            }
        };
        return directory.listFiles(templateFilter);
    }

    private List<File> getFiles() {
        return new ArrayList<File>(Arrays.asList(getFilesAsArray()));
    }

    /**
     *
     */
    protected void updateFileList() {
        this.files = getFiles();
    }
}
