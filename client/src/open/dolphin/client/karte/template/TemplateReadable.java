/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import java.io.File;
import open.dolphin.client.karte.template.error.CantReadTemplateException;
import open.dolphin.infomodel.DocumentModel;

/**
 *
 * @author tomohiro
 */
public interface TemplateReadable {

    /**
     *
     * @param path
     * @return
     * @throws CantReadTemplateException
     */
    DocumentModel readBody(File path) throws CantReadTemplateException;

    /**
     * 
     * @param path
     * @return
     * @throws CantReadTemplateException
     */
    TemplateHeader readHeader(File path) throws CantReadTemplateException;

}
