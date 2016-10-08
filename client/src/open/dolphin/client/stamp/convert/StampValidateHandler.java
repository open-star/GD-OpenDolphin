/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.stamp.convert;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

enum Status {

    NONE, JAVA, OBJECT
};

/**
 * スタンプツリーからスタンプＩＤを取り出す。
 * @author
 */
class StampValidateHandler extends DefaultHandler {

    private boolean hasError;
    private Status currentStatus;

    public StampValidateHandler() {
        hasError = false;
        currentStatus = Status.NONE;
    }

    public boolean hasError() {
        return hasError;
    }

    @Override
    public void startDocument() {
        hasError = false;
        currentStatus = Status.NONE;
    }

    @Override
    public void endDocument() {
        if (currentStatus != Status.OBJECT) {
            hasError = true;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (localName.equals("java")) {
            currentStatus = Status.JAVA;
        }

        if (currentStatus == Status.JAVA) {
            if (localName.equals("object")) {
                currentStatus = Status.OBJECT;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
    }

    @Override
    public void error(SAXParseException exception) {
        hasError = true;
    }

    @Override
    public void fatalError(SAXParseException exception) {
        hasError = true;
    }

    @Override
    public void warning(SAXParseException exception) {
        hasError = true;
    }
}
