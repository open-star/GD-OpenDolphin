/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import java.io.File;
import open.dolphin.client.karte.template.error.CantWriteTemplateException;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import open.dolphin.log.LogWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author tomohiro
 */
public class TemplateBuilder {

    private Document root;

    /**
     *
     */
    public TemplateBuilder() {
        createNewDocument();
        buildTemplateAsXml();
    }

    /**
     *
     * @param templateBody
     */
    public void setBody(String templateBody) {
        Element body = getElementByTagName("body");
        clearBody(body);
        setDocumentToBody(body, templateBody);
    }

    /**
     *
     * @return
     */
    public String getBody() {
        Element body = getElementByTagName("body");
        return body.getTextContent();
    }

    /**
     * 
     * @param templateHeader
     */
    public void setHeader(TemplateHeader templateHeader) {
        Element header = getElementByTagName("header");
        clearHeader(header);
        setNameToHeader(header, templateHeader.getName());
        setCreatorToHeader(header, templateHeader.getCreator());
    }

    /**
     *
     * @return
     */
    public TemplateHeader getHeader() {
        TemplateHeader header = new TemplateHeader();
        Element templateHeader = getElementByTagName("header");
        header.setName(getNameFromHeader(templateHeader));
        header.setCreator(getCreatorFromHeader(templateHeader));
        return header;
    }

    /**
     *
     * @return
     */
    public Document getRoot() {
        if (this.root == null) {
            createNewDocument();
        }
        return this.root;
    }

    /**
     *
     * @param root
     */
    public void setRoot(Document root) {
        this.root = root;
    }

    /**
     *
     * @return
     * @throws CantWriteTemplateException
     */
    public String getString() throws CantWriteTemplateException {
        StringWriter writer = new StringWriter();
        StreamResult streamResult = outputDocumentTo(writer);
        return streamResult.getWriter().toString();
    }

    /**
     *
     * @param writer
     * @return
     * @throws CantWriteTemplateException
     */
    public StreamResult outputDocumentTo(Writer writer) throws CantWriteTemplateException {

        File xslPath = new File(getClass().getResource("/open/dolphin/resources/style.xsl").getPath());
        StreamSource xslSource = new StreamSource(xslPath);

        StreamResult streamResult = new StreamResult(writer);
        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer former;
        try {
            former = factory.newTransformer(xslSource);
            former.transform(new DOMSource(getRoot().getDocumentElement()), streamResult);
        } catch (TransformerConfigurationException e) {
            throw new CantWriteTemplateException(e);
        } catch (TransformerException e) {
            throw new CantWriteTemplateException(e);
        }

        return streamResult;
    }

    private Element getElementByTagName(String name) {
        return (Element)getRoot().getElementsByTagName(name).item(0);
    }

    private Element getTemplateElement() {
        return getElementByTagName("template");
    }

    private void clearHeader(Element header) {
        if (header.hasChildNodes()) {
            removeChild(header, "name");
            removeChild(header, "creator");
        }
    }

    private void clearBody(Element body) {
        if (body.hasChildNodes()) {
            getTemplateElement().removeChild(body);
            addBodyToXml();
        }
    }

    private void removeChild(Element target, String name) {
        Element element = getElementByTagName(name);
        if (element != null) {
            target.removeChild(element);
        }
    }

    private void setDocumentToBody(Element body, String data) {
        body.appendChild(getRoot().createCDATASection(data));
    }

    private Element createTextChild(String nodeName, String text) {
        Element element = getRoot().createElement(nodeName);
        element.appendChild(getRoot().createTextNode(text));
        return element;
    }

    private String getNameFromHeader(Element header) {
        return getElementByTagName("name").getTextContent();
    }

    private void setNameToHeader(Element header, String name) {
        header.appendChild(createTextChild("name", name));
    }

    private String getCreatorFromHeader(Element header) {
        return getElementByTagName("creator").getTextContent();
    }

    private void setCreatorToHeader(Element header, String creator) {
        header.appendChild(createTextChild("creator", creator));
    }

    private void addBodyToXml() {
        getTemplateElement().appendChild(getRoot().createElement("body"));
    }

    private void addHeaderToXml() {
        getTemplateElement().appendChild(getRoot().createElement("header"));
    }

    private void buildTemplateAsXml() {
        Element template = getRoot().createElement("template");
        template.setAttribute("version", Template.VERSION);
        getRoot().appendChild(template);
        addHeaderToXml();
        addBodyToXml();
    }

    private void createNewDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            LogWriter.error(getClass(), ex);
            // FIXME: need to handle exception
        }
        setRoot(builder.newDocument());
    }
}
