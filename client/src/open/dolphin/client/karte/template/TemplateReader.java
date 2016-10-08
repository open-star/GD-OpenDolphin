/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import com.sun.mail.util.BASE64DecoderStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import open.dolphin.client.karte.template.error.CantReadTemplateException;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.SchemaModel;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author tomohiro
 */
public class TemplateReader implements TemplateReadable {

    /**
     * 
     * @param path
     * @return
     * @throws CantReadTemplateException
     */
    @Override
    public TemplateHeader readHeader(File path) throws CantReadTemplateException {
        TemplateBuilder builder = buildXMLFromFile(path);
        return builder.getHeader();
    }

    /**
     *
     * @param path
     * @return
     * @throws CantReadTemplateException
     */
    @Override
    public DocumentModel readBody(File path) throws CantReadTemplateException {
        TemplateBuilder builder = buildXMLFromFile(path);
        return deserializeDocument(builder.getBody());
    }

    private TemplateBuilder buildXMLFromFile(File path) throws CantReadTemplateException {
        Document root = readFromFile(path);
        TemplateBuilder builder = new TemplateBuilder();
        builder.setRoot(root);
        return builder;
    }

    private Document readFromFile(File path) throws CantReadTemplateException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new CantReadTemplateException(ex);
        }
        Document document = null;
        try {
            document = builder.parse(path);
        } catch (SAXException ex) {
            throw new CantReadTemplateException(ex);
        } catch (IOException ex) {
            throw new CantReadTemplateException(ex);
        }
        return document;
    }

    private DocumentModel deserializeDocument(String body) throws CantReadTemplateException {
        ObjectInputStream reader = null;
        try {
            byte[] bytes = BASE64DecoderStream.decode(body.getBytes("UTF-8"));
            reader = new ObjectInputStream(new ByteArrayInputStream(bytes));
        } catch (IOException ex) {
            throw new CantReadTemplateException(ex);
        }
        DocumentModel document = null;
        try {
            document = (DocumentModel) reader.readObject();
        } catch (IOException ex) {
            throw new CantReadTemplateException(ex);
        } catch (ClassNotFoundException ex) {
            throw new CantReadTemplateException(ex);
        }
        Set<ModuleModel> mc = document.getModules();
        for (ModuleModel module : mc) {
            module.setModel(module.toInfoModel());
        }
        Set<SchemaModel> sc = document.getSchemas();
        if (sc != null) {
            for (SchemaModel schema : sc) {
                ImageIcon icon = new ImageIcon(schema.getJpegBytes());
                schema.setIcon(icon);
            }
        }
        return document;
    }
}
