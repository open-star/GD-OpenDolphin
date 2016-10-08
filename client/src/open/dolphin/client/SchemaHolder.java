package open.dolphin.client;

import javax.swing.*;
import javax.swing.text.*;

import open.dolphin.infomodel.SchemaModel;

import java.beans.*;
import java.awt.*;

import java.awt.event.MouseEvent;

import open.dolphin.client.schemaeditor.SchemaEditorImpl;
import open.dolphin.log.LogWriter;

/**
 * スタンプのデータを保持するコンポーネントで TextPane に挿入される。　MEMO:リスナー
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public final class SchemaHolder extends AbstractComponentHolder implements IComponentHolder {

    private static final long serialVersionUID = 1777560751402251092L;
    private static final Color SELECTED_BORDER = new Color(255, 0, 153);
    private SchemaModel schema;
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    // Junzo SATO
    // to restrict the size of the component,
    // setBounds and setSize are overridden.
    private int fixedSize = 192;//160;/////////////////////////////////////////
    private int fixedWidth = fixedSize;
    private int fixedHeight = fixedSize;
    //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
    private boolean selected;
    private Position start;
    private Position end;
    private KartePane kartePane;
    private Color selectedBorder = SELECTED_BORDER;

    /**
     *
     * @param kartePane
     * @param schema
     */
    public SchemaHolder(KartePane kartePane, SchemaModel schema) {

        this.kartePane = kartePane;

        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
        // Junzo SATO
        // for simplicity, the acpect ratio of the fixed rect is set to 1.
        this.setSize(fixedWidth, fixedHeight);
        this.setMaximumSize(new Dimension(fixedWidth, fixedHeight));
        this.setMinimumSize(new Dimension(fixedWidth, fixedHeight));
        this.setPreferredSize(new Dimension(fixedWidth, fixedHeight));
        // adjustment for printer
        this.setDoubleBuffered(false);
        this.setOpaque(true);
        this.setBackground(Color.white);
        //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

        this.schema = schema;
        this.setImageIcon(schema.getIcon());

    }

    /**
     *　アイコンを設定
     * @param icon　アイコン
     */
    public void setImageIcon(ImageIcon icon) {
        setIcon(adjustImageSize(icon, new Dimension(fixedWidth, fixedHeight)));
    }

    /**
     *　アイコンを参照
     * @return　アイコン
     */
    @Override
    public int getContentType() {
        return IComponentHolder.TT_IMAGE;
    }

    /**
     *　カルテペイン
     * @return　カルテペイン
     */
    @Override
    public KartePane getKartePane() {
        return kartePane;
    }

    /**
     *　シェーマ
     * @return　シェーマ
     */
    public SchemaModel getSchema() {
        return schema;
    }

    /**
     *
     * @return　
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     *
     * @param map
     */
    @Override
    public void enter(ActionMap map) {

        map.get(GUIConst.ACTION_COPY).setEnabled(true);

        if (kartePane.getTextPane().isEditable()) {
            map.get(GUIConst.ACTION_CUT).setEnabled(true);
        } else {
            map.get(GUIConst.ACTION_CUT).setEnabled(false);
        }

        map.get(GUIConst.ACTION_PASTE).setEnabled(false);

        setSelected(true);
    }

    /**
     *
     * @param map
     */
    @Override
    public void exit(ActionMap map) {
        setSelected(false);
    }

    /**
     *
     * @return
     */
    @Override
    public Component getComponent() {
        return this;
    }

    /**
     *
     * @param e
     */
    @Override
    public void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = new JPopupMenu();
            popup.setFocusable(false);
            ChartMediator mediator = kartePane.getMediator();
            popup.add(mediator.getAction(GUIConst.ACTION_CUT));
            popup.add(mediator.getAction(GUIConst.ACTION_COPY));
            popup.add(mediator.getAction(GUIConst.ACTION_PASTE));
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     *
     * @param selected
     */
    @Override
    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        if (old != this.selected) {
            if (this.selected) {
                this.setBorder(BorderFactory.createLineBorder(selectedBorder));
            } else {
                this.setBorder(BorderFactory.createLineBorder(kartePane.getTextPane().getBackground()));
            }
        }
    }

    /**
     *
     */
    @Override
    public void edit() {

        try {
            final SchemaEditorImpl editor = new SchemaEditorImpl();
            editor.setSchema(schema);
            editor.setEditable(kartePane.getTextPane().isEditable());
            editor.addPropertyChangeListener(SchemaHolder.this);
            Runnable awt = new Runnable() {

                @Override
                public void run() {
                    editor.start();
                    kartePane.setDirty(true);
                }
            };
            EventQueue.invokeLater(awt);
        } catch (Exception e) {
            LogWriter.error(getClass(), "", e);
        }
    }

    /**
     *
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        //     LogWriter.warn(getClass(), "SchemaHolder propertyChange");
        SchemaModel newSchema = (SchemaModel) e.getNewValue();
        if (newSchema == null) {
            return;
        }

        schema = newSchema;
        setIcon(adjustImageSize(schema.getIcon(), new Dimension(fixedWidth, fixedHeight)));
    }

    /**
     *
     * @param start
     * @param end
     */
    @Override
    public void setEntry(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    /**
     *
     * @return
     */
    @Override
    public int getStartPos() {
        return start.getOffset();
    }

    /**
     *
     * @return
     */
    @Override
    public int getEndPos() {
        return end.getOffset();
    }

    /**
     * LDAP Programming with Java.
     * @param icon 
     * @param dim
     * @return
     */
    protected ImageIcon adjustImageSize(ImageIcon icon, Dimension dim) {

        if ((icon.getIconHeight() > dim.height)
                || (icon.getIconWidth() > dim.width)) {
            Image img = icon.getImage();
            float hRatio = (float) icon.getIconHeight() / dim.height;
            float wRatio = (float) icon.getIconWidth() / dim.width;
            int h, w;
            if (hRatio > wRatio) {
                h = dim.height;
                w = (int) (icon.getIconWidth() / hRatio);
            } else {
                w = dim.width;
                h = (int) (icon.getIconHeight() / wRatio);
            }
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            return icon;
        }
    }
}
