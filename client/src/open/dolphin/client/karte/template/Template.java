/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import open.dolphin.client.karte.template.error.CantReadTemplateException;
import open.dolphin.infomodel.DocumentModel;

/**
 *
 * @author tomohiro
 */
public class Template {

    /**
     *
     */
    public static String VERSION = "1.0";

    TemplateHeader header;
    DocumentModel body;
    TemplateManager manager;

    /**
     * 
     * @param manager
     */
    public Template(TemplateManager manager) {
        this.manager = manager;
        this.header  = null;
        this.body    = null;
    }

    /**
     *
     * @param manager
     * @param header
     */
    public Template(TemplateManager manager, TemplateHeader header) {
        this.manager = manager;
        this.header  = header;
        this.body    = null;
    }

    /**
     *
     * @param manager
     * @param header
     * @param body
     */
    public Template(TemplateManager manager, TemplateHeader header, DocumentModel body) {
        this.manager = manager;
        this.header  = header;
        this.body    = body;
    }

    @Override
    public String toString() {
        if (getHeader() != null) {
            return getHeader().getName();
        }
        return super.toString();
    }

    /**
     *
     * @param manager
     */
    public void setManager(TemplateManager manager) {
        this.manager = manager;
    }

    /**
     *
     * @return
     */
    public TemplateManager getManager() {
        return this.manager;
    }

    /**
     *
     * @param header
     */
    public void setHeader(TemplateHeader header) {
        this.header = header;
    }

    /**
     *
     * @return
     */
    public TemplateHeader getHeader() {
        return this.header;
    }

    /**
     *
     * @param body
     */
    public void setBody(DocumentModel body) {
        this.body = body;
    }

    /**
     *
     * @return
     * @throws CantReadTemplateException
     */
    public DocumentModel getBody() throws CantReadTemplateException {
        if (this.body == null) {
            this.manager.readTemplate(this);
        }
        return this.body;
    }
}
