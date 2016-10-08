/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package open.dolphin.client.karte.template;

import com.sun.mail.util.BASE64EncoderStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import open.dolphin.client.karte.template.error.CantWriteTemplateException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.log.LogWriter;

/**
 *
 * @author tomohiro
 */
public class TemplateWriter implements TemplateWritable {

    private TemplateBuilder builder;
    private File path;
    private boolean session;

    /**
     *
     * @param header
     * @throws CantWriteTemplateException
     */
    @Override
    public void writeHeader(TemplateHeader header) throws CantWriteTemplateException {
        sessionGuard();

        getBuilder().setHeader(header);
    }

    /**
     * 
     * @param document
     * @throws CantWriteTemplateException
     */
    @Override
    public void writeBody(DocumentModel document) throws CantWriteTemplateException {
        sessionGuard();

        getBuilder().setBody(serializeDocument(document));
    }

    /**
     *
     * @param path
     */
    @Override
    public void beginSession(File path) {
        this.path = path;
        this.session = true;
    }

    /**
     *
     * @throws CantWriteTemplateException
     */
    @Override
    public void endSession() throws CantWriteTemplateException {
        try {
            save();
        }
        finally {
            clearSessionState();
        }
    }

    private void save() throws CantWriteTemplateException {
        FileOutputStream foStream = null;
        OutputStreamWriter writer = null;
        try {
            foStream = new FileOutputStream(this.path);
            writer = new OutputStreamWriter(foStream, "UTF-8");
            getBuilder().outputDocumentTo(writer);
        } catch (UnsupportedEncodingException ex) {
            throw new CantWriteTemplateException(ex);
        } catch (FileNotFoundException ex) {
            throw new CantWriteTemplateException(ex);
        } catch (IOException ex) {
            throw new CantWriteTemplateException(ex);
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception ex) {            LogWriter.error(this.getClass(), "", ex);
                // IOException or NullPointerExceptin
                // Nothing
            }
        }
    }

    private void clearSessionState() {
        this.path = null;
        this.session = false;
    }

    private boolean isInSession() {
        return this.session;
    }

    private void sessionGuard() throws CantWriteTemplateException {
        if (isInSession()) {
            return;
        }
        throw new CantWriteTemplateException("セッションが開かれていません");
    }

    private TemplateBuilder getBuilder() {
        if (this.builder == null) {
            this.builder = new TemplateBuilder();
        }
        return this.builder;
    }

    private String serializeDocument(DocumentModel document) throws CantWriteTemplateException {

        String serialized = null;

        ObjectOutputStream stream = null;
        try {
            ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
            try {
                stream = new ObjectOutputStream(baoStream);
                stream.writeObject(document);
            } finally {
                stream.close();
            }

            byte[] bytes = BASE64EncoderStream.encode(baoStream.toByteArray());
            serialized = new String(bytes);

        } catch (IOException ex) {
            throw new CantWriteTemplateException(ex);
        }
        finally {
            try {
                stream.close();
            }
            catch (Exception ex) {
                            LogWriter.error(this.getClass(), "", ex);
                // IOException or NullPointerExceptin
                // Nothing
            }
        }

        return serialized;
    }
}
