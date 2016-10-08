/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.stamp.convert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import open.dolphin.delegater.remote.RemoteStampDelegater;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import open.dolphin.container.Pair;
import open.dolphin.dao.SqlDaoFactory;
import open.dolphin.dao.SqlMasterDao;
import open.dolphin.infomodel.MedicineEntry;
import open.dolphin.infomodel.StampModel;
import open.dolphin.log.LogWriter;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *　スタンプのインポートとエクスポート
 * @author
 */
public class StampConverter {

    static String nullStamp = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<java version=\"1.6.0_14\" class=\"java.beans.XMLDecoder\">"
            + "<object class=\"open.dolphin.infomodel.BundleDolphin\">"
            + "<void property=\"claimItem\">"
            + "<array class=\"open.dolphin.infomodel.ClaimItem\" length=\"0\">"
            + "<void index=\"0\">"
            + "<object class=\"open.dolphin.infomodel.ClaimItem\">"
            + "</object>"
            + "</void>"
            + "</array>"
            + "</void>"
            + "<void property=\"classCode\">"
            + "<string></string>"
            + "</void>"
            + "<void property=\"classCodeSystem\">"
            + "<string>Claim007</string>"
            + "</void>"
            + "<string></string>"
            + "</void>"
            + "</object>"
            + "</java>";
    /*
     * スタンプの内容をXMLとしてダンプする。
     * */

    static private void setStringValue(Element node, String s) {
        List child_nodes = node.getChildren();
        for (int index = 0; index < child_nodes.size(); index++) {
            Element string_node = (Element) child_nodes.get(index);
            String node_name = string_node.getName();
            if (node_name != null) {
                if (node_name.equals("string")) {
                    string_node.setText(s);
                }
            }
        }
    }

    /**
     *
     * @param node
     * @return
     */
    static private String getStringValue(Element node) {
        String result = "";
        List child_nodes = node.getChildren();
        for (int index = 0; index < child_nodes.size(); index++) {
            Element string_node = (Element) child_nodes.get(index);
            if (string_node.getName().equals("string")) {
                result = ((Element) string_node).getValue();
            }
        }
        return result;
    }

    /**
     *
     * @param object_node
     * @param attribute
     * @param value
     */
    static private void createDateNode(Element object_node, String attribute, String value) {
        Element property_node = new Element("void");
        Attribute property_attribute = new Attribute("property", attribute);
        property_node.getAttributes().add(property_attribute);
        Element string_node = new Element("string");
        string_node.setText(value);
        property_node.getChildren().add(string_node);
        object_node.getChildren().add(property_node);
    }

    /**
     *
     * @param dao
     * @param object_node
     */
    static private void appendStratAndEndDate(SqlMasterDao dao, Element object_node) {

        boolean have_enddate = false;
        boolean have_startdate = false;
        String code = "";
        String startdate = "";
        String enddate = "";

        List child_nodes = object_node.getChildren();

        for (int index = 0; index < child_nodes.size(); index++) {

            Element target_node = (Element) child_nodes.get(index);
            Attribute attribute = target_node.getAttribute("property");
            if (attribute != null) {
                String attribute_value = attribute.getValue();//名前

                if (attribute_value.equals("code")) {
                    code = getStringValue(target_node);
                }

                List<MedicineEntry> collection = new ArrayList<MedicineEntry>();
                dao.getAllMedicineEntry(code, collection);
                if (!collection.isEmpty()) {
                    startdate = collection.get(0).getStartDate();
                    enddate = collection.get(0).getEndDate();
                }

                if (attribute_value.equals("startDate")) {
                    setStringValue(target_node, startdate);
                    have_startdate = true;
                }

                if (attribute_value.equals("endDate")) {
                    setStringValue(target_node, enddate);
                    have_enddate = true;
                }
            }
        }

        if (!have_startdate) {
            if (!code.equals("")) {
                createDateNode(object_node, "startDate", startdate);
            }
        }

        if (!have_enddate) {
            if (!code.equals("")) {
                createDateNode(object_node, "endDate", enddate);
            }
        }
    }

    /**
     *
     * @param dao
     * @param object_node
     */
    static private void transformClaimItemObject(SqlMasterDao dao, Element object_node) {
        if (object_node.getName().equals("object")) {
            String object_class = object_node.getAttributeValue("class");
            if (object_class.equals("open.dolphin.infomodel.ClaimItem")) {
                appendStratAndEndDate(dao, object_node);
            }
        }
    }

    /**
     *
     * @param dao
     * @param node
     */
    static private void transformClaimItemArray(SqlMasterDao dao, Element node) {
        if (node.getName().equals("array")) {
            List array_enveropes = node.getChildren();
            for (int index = 0; index < array_enveropes.size(); index++) {
                List objects = ((Element) array_enveropes.get(index)).getChildren();
                for (int index1 = 0; index1 < objects.size(); index1++) {
                    transformClaimItemObject(dao, (Element) objects.get(index1));
                }
            }
        }
    }

    /**
     *
     * @param dao
     * @param node
     */
    static private void transformClaimItem(SqlMasterDao dao, Element node) {
        Attribute attribute_node = node.getAttribute("property");
        if (attribute_node != null) {
            if (attribute_node.getValue().equals("claimItem")) {
                List child_nodes = node.getChildren();
                for (int index = 0; index < child_nodes.size(); index++) {
                    transformClaimItemArray(dao, (Element) child_nodes.get(index));
                }
            }
        }

    }

    /**
     *
     * @param dao
     * @param object_node
     */
    static private void transformObject(SqlMasterDao dao, Element object_node) {
        if (object_node.getName().equals("object")) {
            List content_of_object = object_node.getChildren();
            for (int index = 0; index < content_of_object.size(); index++) {
                transformClaimItem(dao, (Element) content_of_object.get(index));
            }
        }
    }

    /**
     *
     * @param stamp
     * @return
     */
    static private String transform(String stamp) {
        String result = "";
        if (stamp != null) {
            if (!stamp.equals("")) {
                try {
                    SqlMasterDao dao = (SqlMasterDao) SqlDaoFactory.create("dao.master");
                    BufferedReader reader = new BufferedReader(new StringReader(stamp));
                    SAXBuilder docBuilder = new SAXBuilder();
                    Document document = docBuilder.build(reader);
                    Element root = document.getRootElement();
                    List children = root.getChildren();
                    for (int index = 0; index < children.size(); index++) {
                        Element child = (Element) children.get(index);
                        transformObject(dao, child);
                    }
                    XMLOutputter out = new XMLOutputter();
                    result = out.outputString(document);
                } catch (Exception ex) {
                     LogWriter.error("StampConverter", "transform");
                }
            }
        }
        return result;
    }

    /**
     *
     * @param delegator
     * @param stampTree
     * @return
     */
    static public String buildStampXml(RemoteStampDelegater delegator, String stampTree) {

        StringBuilder result = new StringBuilder();
        result.append("<?xml version='1.0' encoding='UTF-8'?>");
        result.append(System.getProperty("line.separator"));
        result.append("<stampset>");
        result.append(System.getProperty("line.separator"));
        result.append("<stamptree>");
        result.append(System.getProperty("line.separator"));
        result.append("<![CDATA[");
        result.append(stampTree);
        result.append("]]>");
        result.append("</stamptree>");
        result.append(System.getProperty("line.separator"));
        List<Pair<String, String>> idList = extractStampIds(stampTree);
        result.append("<stamps>");
        result.append(System.getProperty("line.separator"));
        for (Pair<String, String> id : idList) {
            result.append("<stamp " + "id='").append(id.key).append("' entity='").append(id.value).append("'>");
            result.append(System.getProperty("line.separator"));
            result.append("<![CDATA[");
            String stamp = transform(getStamp(delegator, id.key));
            result.append(stamp);
            result.append("]]>");
            result.append("</stamp>");
            result.append(System.getProperty("line.separator"));
        }
        result.append("</stamps>");
        result.append(System.getProperty("line.separator"));
        result.append("</stampset>");
        result.append(System.getProperty("line.separator"));
        return result.toString();
    }

    /*
     * 対象のＸＭＬドキュメントのルート要素を返す。
     * */
    static private org.w3c.dom.Element getRootElement(String stampSetXml) throws org.xml.sax.SAXException, IOException {
        try {
            StringReader reader = new StringReader(stampSetXml);
            org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
            source.setEncoding("UTF-8");
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(source);
            return doc.getDocumentElement();
        } catch (ParserConfigurationException ex) {
            LogWriter.error(StampConverter.class, "", ex);
        }
        return null;
    }

    /**
     * 対象のＸＭＬドキュメントをインポート内容として、そのスタンプツリーを返す。
     * @param stampSetXml
     * @return
     * @throws org.xml.sax.SAXException
     * @throws IOException
     */
    static public String extractStampTree(String stampSetXml) throws org.xml.sax.SAXException, IOException {
        org.w3c.dom.Element root = getRootElement(stampSetXml);
        org.w3c.dom.NodeList stamptree = root.getElementsByTagName("stamptree");
        if (stamptree.getLength() != 0) {
            org.w3c.dom.Element current = (org.w3c.dom.Element) stamptree.item(0);
            String tree = current.getTextContent().trim();
            if (checkStampTree(tree)) {
                return tree;
            }

        }
        throw new org.xml.sax.SAXException();
    }

    /**
     * Change IDs in original stamp tree xml with id hashmap.
     * @param originalStampXml
     * @param convertIdMap
     * @return
     * @throws org.xml.sax.SAXException 
     * @throws IOException
     * @throws TransformerException
     */
    static public String changeStampIds(String originalStampXml, HashMap<String, String> convertIdMap)
            throws org.xml.sax.SAXException, IOException, TransformerException {
        org.w3c.dom.Element root = getRootElement(originalStampXml);
        org.w3c.dom.NodeList stampInfos = root.getElementsByTagName("stampInfo");
        if (stampInfos.getLength() != 0) {
            for (int idx = 0; idx < stampInfos.getLength(); idx++) {
                org.w3c.dom.Element stampInfo = (org.w3c.dom.Element) stampInfos.item(idx);
                String stampId = stampInfo.getAttribute("stampId");
                if (convertIdMap.containsKey(stampId)) {
                    stampInfo.setAttribute("stampId", convertIdMap.get(stampId));
                }
            }
            return elementToString(root);
        }
        throw new org.xml.sax.SAXException();
    }

    /**
     * Change DOM node to String converter
     * @param root
     * @return String
     * @throws TransformerException
     */
    private static String elementToString(org.w3c.dom.Element root) throws TransformerException {
        DOMSource source = new DOMSource(root);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(source, result);
        return writer.toString();
    }

    /**
     * 対象のＸＭＬドキュメントをインポート内容として、スタンプとそのIDを返す。
     *
     * @param stampSetXml
     * @return
     * @throws org.xml.sax.SAXException
     * @throws IOException
     */
    static public List<Pair<Pair<String, String>, String>> extractStamps(String stampSetXml) throws org.xml.sax.SAXException, IOException {
        List<Pair<Pair<String, String>, String>> result = new ArrayList<Pair<Pair<String, String>, String>>();
        org.w3c.dom.Element root = getRootElement(stampSetXml);
        org.w3c.dom.NodeList stamps = root.getElementsByTagName("stamp");
        for (int index = 0; index < stamps.getLength(); index++) {
            org.w3c.dom.Element current = (org.w3c.dom.Element) stamps.item(index);
            String tree = ((org.w3c.dom.Element) current).getTextContent().trim();
            String id = ((org.w3c.dom.Element) current).getAttribute("id");
            String entity = ((org.w3c.dom.Element) current).getAttribute("entity");
            if (checkStampId(id)) {
                if (checkStamp(tree)) {
                    result.add(new Pair<Pair<String, String>, String>(new Pair<String, String>(id, entity), tree));
                } else {
                    throw new org.xml.sax.SAXException();
                }

            } else {
                throw new org.xml.sax.SAXException();
            }

        }
        return result;
    }

    /**
     *
     * @param id
     * @return
     */
    static private boolean checkStampId(String id) {
        return true;
    }

    /**
     *
     * @param stampTree
     * @return
     * @throws org.xml.sax.SAXException
     * @throws IOException
     */
    static private boolean checkStampTree(String stampTree) throws org.xml.sax.SAXException, IOException {

        StampTreeValidateHandler contentHandler = new StampTreeValidateHandler();
        org.xml.sax.XMLReader parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
        StringReader reader = new StringReader(stampTree);
        org.xml.sax.InputSource rource = new org.xml.sax.InputSource(reader);
        parser.setContentHandler(contentHandler);
        parser.parse(rource);
        return !contentHandler.hasError();
    }

    /**
     *
     * @param stamp
     * @return
     * @throws org.xml.sax.SAXException
     * @throws IOException
     */
    static private boolean checkStamp(String stamp) throws org.xml.sax.SAXException, IOException {

        if (!stamp.equals("")) {
            StampValidateHandler contentHandler = new StampValidateHandler();
            org.xml.sax.XMLReader parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
            StringReader reader = new StringReader(stamp);
            org.xml.sax.InputSource rource = new org.xml.sax.InputSource(reader);
            parser.setContentHandler(contentHandler);
            parser.parse(rource);
            return !contentHandler.hasError();
        }
        return true;
    }

    /**
     * 対象のＸＭＬドキュメントをスタンプツリーとして、含まれるスタンプIDを返す。
     *
     * @param stampTree
     * @return
     */
    static public List<Pair<String, String>> extractStampIds(String stampTree) {
        try {
            StampIDContentHandler contentHandler = new StampIDContentHandler();
            org.xml.sax.XMLReader parser = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
            StringReader reader = new StringReader(stampTree);
            org.xml.sax.InputSource rource = new org.xml.sax.InputSource(reader);
            parser.setContentHandler(contentHandler);
            parser.parse(rource);
            return contentHandler.getIDs();
        } catch (IOException ex) {
            LogWriter.error(StampConverter.class, null, ex);
        } catch (org.xml.sax.SAXException ex) {
            LogWriter.error(StampConverter.class, null, ex);
        }
        return null;
    }

    /**
     *
     * @param delegator
     * @param id
     * @return
     */
    static public String getStamp(
            RemoteStampDelegater delegator, String id) {
        try {
            StampModel result = delegator.getStamp(id);
            if (result != null) {
                return result.getXml();
            } else {
                return "";
            }

        } catch (Exception e) {
            LogWriter.error("StampConverter", "getStamp");
        }
        return "";
    }
}
