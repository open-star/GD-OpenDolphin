/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.stamp.convert;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * スタンプツリーからスタンプＩＤを取り出す。
 * @author
 */
class StampTreeValidateHandler extends DefaultHandler {

    private boolean hasError = false;

    public StampTreeValidateHandler() {

    }

    public boolean hasError()
    {
        return hasError;
    }
    
    @Override
    public void startDocument() {
    }

    @Override
    public void endDocument() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

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
