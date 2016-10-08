/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StampEditorDialog.java
 *
 * Created on 2010/03/05, 15:46:12
 */
package open.dolphin.client.editor.stamp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import open.dolphin.client.BlockGlass;
import open.dolphin.client.IStampEditorDialog;
import open.dolphin.client.IStampModelEditor;
import open.dolphin.helper.ComponentMemory;
import open.dolphin.log.LogWriter;

/**
 *　スタンプエディタ別画面（カルテから発行）　MEMO:画面　リスナー
 * @author
 */
public class StampEditorDialog extends javax.swing.JDialog implements IStampEditorDialog, PropertyChangeListener {

    /**
     *
     */
    public static final String VALIDA_DATA_PROP = "validData";
    /**
     * 
     */
    public static final String VALUE_PROP = "value";
    /** stampEditor のプラグポイント */
    //   private static final String EDITOR_PLUG_POINT = "karteEditor/stampEditor";
    /** command buttons */
    private static final String OK_ICON_KARTE = "/open/dolphin/resources/images/lgicn_16.gif";
    private static final String OK_ICON_STAMPBOX = "/open/dolphin/resources/images/tools_16.gif";
    private JButton okButton;
    /** target editor */
    private IStampModelEditor editor;
    private PropertyChangeSupport boundSupport;
    private String entity;
    private Object value;
    private boolean toKarte;
    private BlockGlass glass;
    private static final int DEFAULT_X = 159;
    private static final int DEFAULT_Y = 67;
    private static final int DEFAULT_WIDTH = 924;
    private static final int DEFAULT_HEIGHT = 616;

    /** Creates new form StampEditorDialog
     * @param entity
     * @param value
     * @param toKarte
     */
    public StampEditorDialog(String entity, Object value, boolean toKarte) {
        super((JFrame) null);
 //       boundSupport = new PropertyChangeSupport(this);
        
        boundSupport = new PropertyChangeSupport(this);        
        initComponents();

        this.entity = entity;
        this.value = value;
        this.toKarte = toKarte;
    }

    /**
     * Constructor. Use layered inititialization pattern.
     * @param entity
     * @param value
     */
    public StampEditorDialog(String entity, Object value) {
        this(entity, value, true);
    }

    /**
     * エディタを開始する。
     */
    public void start() {
        initialize();
    }

    /**
     * GUIコンポーネントを初期化する。
     */
    private void initialize() {

        // カルテに展開するかスタンプボックスに保存するかで
        // モーダル属性及びボタンのアイコンとツールチップを変える
        setModal(toKarte);
        if (toKarte) {
            okButton = new JButton(createImageIcon(OK_ICON_KARTE));
            okButton.setToolTipText("カルテに展開します");
        } else {
            okButton = new JButton(createImageIcon(OK_ICON_STAMPBOX));
            okButton.setToolTipText("スタンプボックスに保存します");
        }

        // BlockGlass を生成し dialog に設定する
        glass = new BlockGlass();
        setGlassPane(glass);

        // OK ボタンとそのアクションを生成する
        AbstractAction action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                okButtonClicked(e);
            }
        };
        okButton.addActionListener(action);
        okButton.setMnemonic('O');
        okButton.setEnabled(false);

        // 実際の（中味となる）エディタを生成して Dialog に add する
        try {
            editor = createEditor(this.entity);
            editor.setContext(this);
            editor.start();
            editor.addPropertyChangeListener(VALIDA_DATA_PROP, this);
            editor.setValue(value);
        } catch (Exception e) {
            LogWriter.error(getClass(), e);
            return;
        }

        panel.add((Component) editor, BorderLayout.CENTER);

        // CloseBox 処理を登録する
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // CloseBox がクリックされた場合はキャンセルとする
                value = null;
                close();
            }
        });

        setTitle(editor.getTitle());
        ComponentMemory cm = new ComponentMemory(this, new Point(DEFAULT_X, DEFAULT_Y), new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT), this);
        cm.setToPreferenceBounds();
        setVisible(true);
    }

    /**
     * 編集した Stamp を返す。
     * @return
     */
    public Object getValue() {
        return editor.getValue();
    }

    /**
     * プロパティチェンジリスナを登録する。
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.addPropertyChangeListener(prop, listener);
    }

    /**
     * プロパティチェンジリスナを削除する。
     * @param prop プロパティ名
     * @param listener プロパティチェンジリスナ
     */
    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener listener) {
        boundSupport.removePropertyChangeListener(prop, listener);
    }

    /**
     * OKボタンを返す。これはエディタにレイアウトされる。
     * @return OKボタン
     */
    @Override
    public JButton getOkButton() {
        return okButton;
    }

    /**
     * OKボタンをクリックする。
     * エディタで編集したスタンプを要求もとに返す。
     * @param e ActionEvent
     */
    public void okButtonClicked(ActionEvent e) {
        value = getValue();
        if (toKarte) {
            close();
        } else {
            boundSupport.firePropertyChange(VALUE_PROP, null, value);
        }
    }

    /**
     *
     * @param e
     */
    public void addStampButtonClicked(ActionEvent e) {
        value = null;
        close();
    }

    /**
     * 編集中のモデル値が有効な値かどうかの通知を受け、
     * カルテに展開ボタンを enable/disable にする
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Boolean i = (Boolean) evt.getNewValue();
        okButton.setEnabled(i.booleanValue());
    }

    /**
     * ダイアログを閉じる
     */
    @Override
    public void close() {
        editor.dispose();
        setVisible(false);
        dispose();
        boundSupport.firePropertyChange(VALUE_PROP, null, value);
    }

    private ImageIcon createImageIcon(String name) {
        return new ImageIcon(this.getClass().getResource(name));
    }

    private IStampModelEditor createEditor(String entity) {

        IStampModelEditor result = null;
        if (entity.equals("diagnosis")) {
            result = new open.dolphin.order.DiagnosisEditor();
        } else if (entity.equals("medOrder")) {
            result = new open.dolphin.order.PharmaceuticalsStampEditor();
        } else if (entity.equals("injectionOrder")) {
            result = new open.dolphin.order.InjectionStampEditor();
        } else if (entity.equals("testOrder")) {
            result = new open.dolphin.order.TestStampEditor();
        } else if (entity.equals("physiologyOrder")) {
            result = new open.dolphin.order.PhysiologyStampEditor();
        } else if (entity.equals("treatmentOrder")) {
            result = new open.dolphin.order.TreatmentStampEditor();
        } else if (entity.equals("radiologyOrder")) {
            result = new open.dolphin.order.RadiologyStampEditor();
        } else if (entity.equals("baseChargeOrder")) {
            result = new open.dolphin.order.BaseChargeStampEditor();
        } else if (entity.equals("instractionChargeOrder")) {
            result = new open.dolphin.order.InstractionChargeStampEditor();
        } else if (entity.equals("stayOnHomeChargeOrder")) {
            result = new open.dolphin.order.StayOnHomeChargeStampEditor();
        } else if (entity.equals("otherOrder")) {
            result = new open.dolphin.order.OtherStampEditor();
        } else if (entity.equals("generalOrder")) {
            result = new open.dolphin.order.GeneralStampEditor();
        } else if (entity.equals("surgeryOrder")) {
            result = new open.dolphin.order.SurgeryStampEditor();
        }

        return result;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panel.setName("panel"); // NOI18N
        panel.setLayout(new java.awt.BorderLayout(11, 0));
        getContentPane().add(panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
