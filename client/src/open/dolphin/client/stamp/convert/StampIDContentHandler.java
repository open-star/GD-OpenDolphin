/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.stamp.convert;

import java.util.ArrayList;
import java.util.List;
import open.dolphin.container.Pair;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * スタンプツリーからスタンプＩＤを取り出す。
 * @author
 */
class StampIDContentHandler extends DefaultHandler {

    private List<Pair<String, String>> ids; //スタンプID

    public StampIDContentHandler() {
        ids = new ArrayList<Pair<String, String>>();
    }

    public List<Pair<String, String>> getIDs() {
        return ids;
    }

    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equals("stampInfo")) {
            String id = attributes.getValue("stampId");
            if (id != null) {
                String entity = attributes.getValue("entity");
                if (entity != null) {
                    ids.add(new Pair<String, String>(id, entity));
                }
                else
                    ids.add(new Pair<String, String>(id, ""));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
    }

    @Override
    public void error(SAXParseException exception) {
    }

    @Override
    public void fatalError(SAXParseException exception) {
    }

    @Override
    public void warning(SAXParseException exception) {
    }
}
