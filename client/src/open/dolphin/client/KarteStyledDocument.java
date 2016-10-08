package open.dolphin.client;

import open.dolphin.client.editor.stamp.StampHolder;
import open.dolphin.client.editor.stamp.StampHolderHandler;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import open.dolphin.infomodel.BundleDolphin;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariables;

/**
 * KartePane の StyledDocument class。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class KarteStyledDocument extends DefaultStyledDocument {

    // スタンプの先頭を改行する
    private boolean topSpace;
    // stampHolder Style
    private final String STAMP_STYLE = "stampHolder";
    // schemaHolder
    private final String SCHEMA_STYLE = "schemaHolder";
    // KartePane
    private KartePane kartePane;

    /**
     * Creates new TestDocument
     *
     */
    public KarteStyledDocument() {
        topSpace = GlobalVariables.getPreferences().getBoolean("stampSpace", true);
    }

    /**
     *
     * @param kartePane
     */
    public void setParent(KartePane kartePane) {
        this.kartePane = kartePane;
    }

    /**
     *
     * @param str
     */
    public void setLogicalStyle(String str) {
        Style style = this.getStyle(str);
        this.setLogicalStyle(this.getLength(), style);
    }

    /**
     *
     */
    public void clearLogicalStyle() {
        this.setLogicalStyle(this.getLength(), null);
    }

    /**
     *
     */
    public void makeParagraph() {
        try {
            insertString(getLength(), System.getProperty("line.separator"), null);
        } catch (BadLocationException e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * Stamp を挿入する。
     * @param sh 挿入するスタンプホルダ
     */
    public void stamp(final StampHolder sh) {

        try {
            Style runStyle = this.getStyle(STAMP_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(STAMP_STYLE, null);
            }
            StyleConstants.setComponent(runStyle, sh);

            // キャレット位置を取得する
            int start = kartePane.getTextPane().getCaretPosition();

            // Stamp を挿入する
            if (topSpace) {
                insertString(start, System.getProperty("line.separator"), null);
                insertString(start + 1, " ", runStyle);
                insertString(start + 2, System.getProperty("line.separator"), null);                           // 改行をつけないとテキスト入力制御がやりにくくなる
                sh.setEntry(createPosition(start + 1), createPosition(start + 2)); // スタンプの開始と終了位置を生成して保存する
            } else {
                insertString(start, " ", runStyle);
                insertString(start + 1, System.getProperty("line.separator"), null);                           // 改行をつけないとテキスト入力制御がやりにくくなる
                sh.setEntry(createPosition(start), createPosition(start + 1)); // スタンプの開始と終了位置を生成して保存する
            }

        } catch (BadLocationException be) {
            LogWriter.error(getClass(), be);
        } catch (NullPointerException ne) {
            LogWriter.error(getClass(), ne);
        }
    }

    /**
     * Stamp を挿入する。
     * @param sh 挿入するスタンプホルダ
     */
    public void flowStamp(final StampHolder sh) {

        try {
            Style runStyle = this.getStyle(STAMP_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(STAMP_STYLE, null);
            }
            // このスタンプ用のスタイルを動的に生成する

            StyleConstants.setComponent(runStyle, sh);

            // キャレット位置を取得する
            int start = kartePane.getTextPane().getCaretPosition();

            // Stamp を挿入する
            insertString(start, " ", runStyle);

            // スタンプの開始と終了位置を生成して保存する
            sh.setEntry(createPosition(start), createPosition(start + 1));

        } catch (BadLocationException be) {
            LogWriter.error(getClass(), be);
        } catch (NullPointerException ne) {
            LogWriter.error(getClass(), ne);
        }
    }

    /**
     * Stampを削除する。
     * @param start 削除開始のオフセット位置
     * @param len
     */
    public void removeStamp(int start, int len) {

        try {
            // Stamp は一文字で表されている
            remove(start, 1);
        } catch (BadLocationException be) {
            LogWriter.error(getClass(), be);
        }
    }

    /**
     * Stampを指定されたポジションに挿入する。
     * @param inPos　挿入ポジション
     * @param sh　挿入する StampHolder
     */
    public void insertStamp(Position inPos, StampHolder sh) {

        try {
            Style runStyle = this.getStyle(STAMP_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(STAMP_STYLE, null);
            }
            //Style runStyle = this.addStyle(STAMP_STYLE, null);
            StyleConstants.setComponent(runStyle, sh);

            // 挿入位置
            int start = inPos.getOffset();
            insertString(start, " ", runStyle);
            sh.setEntry(createPosition(start), createPosition(start + 1));
        } catch (BadLocationException be) {
            LogWriter.error(getClass(), be);
        }
    }

    /**
     *
     * @param sc
     */
    public void stampSchema(SchemaHolder sc) {

        try {
            Style runStyle = this.getStyle(SCHEMA_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(SCHEMA_STYLE, null);
            }
            // このスタンプ用のスタイルを動的に生成する
            //Style runStyle = addStyle(SCHEMA_STYLE, null);
            StyleConstants.setComponent(runStyle, sc);

            // Stamp同様
            int start = kartePane.getTextPane().getCaretPosition();
            insertString(start, " ", runStyle);
            insertString(start + 1, System.getProperty("line.separator"), null);
            sc.setEntry(createPosition(start), createPosition(start + 1));
        } catch (BadLocationException be) {
            LogWriter.error(getClass(), be);
        }
    }

    /**
     *
     * @param sh
     */
    public void flowSchema(final SchemaHolder sh) {

        try {
            Style runStyle = this.getStyle(SCHEMA_STYLE);
            if (runStyle == null) {
                runStyle = addStyle(SCHEMA_STYLE, null);
            }
            // このスタンプ用のスタイルを動的に生成する
            StyleConstants.setComponent(runStyle, sh);

            // キャレット位置を取得する
            int start = kartePane.getTextPane().getCaretPosition();

            // Stamp を挿入する
            insertString(start, " ", runStyle);

            // スタンプの開始と終了位置を生成して保存する
            sh.setEntry(createPosition(start), createPosition(start + 1));

        } catch (BadLocationException be) {
            LogWriter.error(getClass(), be);
        } catch (NullPointerException ne) {
            LogWriter.error(getClass(), ne);
        }
    }

    /**
     *
     * @param text
     */
    public void insertTextStamp(String text) {

        try {
            clearLogicalStyle();
            setLogicalStyle("default"); // mac 2207-03-31
            int pos = kartePane.getTextPane().getCaretPosition();
            insertString(pos, text, null);
        } catch (BadLocationException e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     * @param text
     * @param a
     */
    public void insertFreeString(String text, AttributeSet a) {
        try {
            insertString(getLength(), text, a);
        } catch (BadLocationException e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     *
     * @param stampHolderHandler
     */
    public void enumerateAllStampHolders(StampHolderHandler stampHolderHandler) {
        enumerateAllStampHolders((javax.swing.text.Element) this.getDefaultRootElement(), stampHolderHandler);
    }

    /**
     *
     * @param element
     * @param stampHolderHandler
     */
    private void enumerateAllStampHolders(javax.swing.text.Element element, StampHolderHandler stampHolderHandler) {

        AttributeSet atts = element.getAttributes().copyAttributes();
        if (atts != null) {
            Enumeration names = atts.getAttributeNames();
            while (names.hasMoreElements()) {
                Object nextName = names.nextElement();
                if (nextName != StyleConstants.ResolveAttribute) {
                    if (!nextName.toString().startsWith("$")) {                    // $enameは除外する
                        if (!nextName.toString().equals("foreground")) {
                            Object attObject = atts.getAttribute(nextName);
                            if (attObject instanceof StampHolder) {
                                stampHolderHandler.onStamp((StampHolder) attObject);
                            }
                        }
                    }
                }
            }
        }
        int children = element.getElementCount();
        for (int i = 0; i < children; i++) {
            enumerateAllStampHolders(element.getElement(i), stampHolderHandler);
        }
    }

    /**
     *
     * @return
     */
    public List<ClaimItem> getClaimItems() {
        final List<ClaimItem> moduleList = new ArrayList<ClaimItem>();
        enumerateAllStampHolders(new StampHolderHandler() {

            @Override
            public void onStamp(StampHolder stampHolder) {
                ClaimItem[] items = ((BundleDolphin) ((ModuleModel) stampHolder.getStamp()).getModel()).getClaimItem();
                for (int index = 0; index < items.length; index++) {
                    moduleList.add(items[index]);
                }
            }
        });
        return moduleList;
    }
}
