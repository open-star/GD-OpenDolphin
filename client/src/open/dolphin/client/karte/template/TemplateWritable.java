/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import java.io.File;
import open.dolphin.client.karte.template.error.CantWriteTemplateException;
import open.dolphin.infomodel.DocumentModel;

/**
 *
 * @author tomohiro
 */
public interface TemplateWritable {

    /**
     *
     * @param path
     */
    void beginSession(File path);

    /**
     *
     * @param document
     * @throws CantWriteTemplateException
     */
    void writeBody(DocumentModel document) throws CantWriteTemplateException;

    /**
     *
     * @param info
     * @throws CantWriteTemplateException
     */
    void writeHeader(TemplateHeader info) throws CantWriteTemplateException;

    /**
     * 
     * @throws CantWriteTemplateException
     */
    void endSession() throws CantWriteTemplateException;
}
