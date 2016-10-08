package open.dolphin.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import open.dolphin.project.GlobalConstants;

import open.dolphin.log.LogWriter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

/**
 * DML を 任意のMessage に翻訳するクラス。
 *
 * @author Kazushi Minagawa, Digital Globe, Inc.
 *
 */
public class MessageBuilder {//implements IMessageBuilder {

    private static final String ENCODING = "SHIFT_JIS";
    /** テンプレートファイル */
    private String templateFile;
    /** テンプレートファイルのエンコーディング */
    private String encoding = ENCODING;

    /**
     * 
     */
    public MessageBuilder() {
    }

    /**
     * MEMO: unused?
     * @return
     */
    private String getTemplateFile() {
        return templateFile;
    }

    /**
     * MEMO: unused?
     * @param templateFile
     */
    private void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    /**
     * MEMO: unused?
     * @return
     */
    private String getEncoding() {
        return encoding;
    }

    /**
     * MEMO: unused?
     * @param encoding
     */
    private void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * 
     * @param dml
     * @return
     */
    private String build(String dml) {
        String result = null;
        try {
            // Document root をVelocity 変数にセットする
            SAXBuilder sbuilder = new SAXBuilder();
            Document root = sbuilder.build(new BufferedReader(new StringReader(dml)));
            VelocityContext context = GlobalConstants.getVelocityContext();
            context.put("root", root);

            // Merge する
            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);
            InputStream instream = GlobalConstants.getTemplateAsStream(templateFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, encoding));
            Velocity.evaluate(context, bw, "MessageBuilder", reader);
            bw.flush();
            bw.close();

            result = sw.toString();

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
        return result;
    }

    /**
     * Claim送信
     * @param helper
     * @return
     */
    public String build(Object helper) {
        String result = null;
        String name = helper.getClass().getName();
        int index = name.lastIndexOf('.');
        name = name.substring(index + 1);
        StringBuilder sb = new StringBuilder();
        sb.append(name.substring(0, 1).toLowerCase());
        sb.append(name.substring(1));
        name = sb.toString();

        try {
            VelocityContext context = GlobalConstants.getVelocityContext();
            context.put(name, helper);

            // このスタンプのテンプレートファイルを得る
            String tFile = name + ".vm";

            // Merge する
            StringWriter sw = new StringWriter();
            BufferedWriter bw = new BufferedWriter(sw);

            InputStream instream = GlobalConstants.getTemplateAsStream(tFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "SHIFT_JIS"));
            Velocity.evaluate(context, bw, name, reader);

            bw.flush();
            bw.close();
            reader.close();

            result = sw.toString();

        } catch (Exception e) {
            LogWriter.error(getClass(), e);
        }
        return result;
    }
}
