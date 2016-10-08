/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LiteCalendarPanel.java
 *
 * Created on 2010/03/08, 16:29:59
 */
package open.dolphin.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import open.dolphin.project.GlobalSettings;
import open.dolphin.infomodel.SimpleDate;

/**
 *　カレンダーパネル　MEMO:画面　リスナー
 * @author
 */
public class LiteCalendarPanel extends javax.swing.JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = -3472737594106311587L;
    /**
     *
     */
    public static final String SELECTED_DATE_PROP = "selectedDateProp";
    /**
     *
     */
    public static final String MARK_LIST_PROP = "markListProp";
    // 表示のデフォルト設定
    private static final int TITLE_ALIGN = SwingConstants.CENTER;
    private static final int TITLE_FONT_SIZE = 14;
    private static final Font TITLE_FONT = new Font("Dialog", Font.PLAIN, TITLE_FONT_SIZE);
    private static final Font CALENDAR_FONT = new Font("Dialog", Font.PLAIN, 12);
    private static final Font OUTOF_MONTH_FONT = new Font("Dialog", Font.PLAIN, 9);
    // カレンダテーブル
    private int relativeMonth;
    private int year;
    private int month;
    private CalendarTableModel tableModel;
    private PropertyChangeSupport boundSupport;
    private Object selectedDate;
    private JLabel titleLabel;
    private SimpleDate today;
    //  private Map<String, Color> eventColorTable = new HashMap<String, Color>();
    // 表示用の属性
    private Color titleFore = GlobalSettings.getColors(GlobalSettings.Parts.CALENDAR_TITLE_FORE);
    private Color titleBack = GlobalSettings.getColors(GlobalSettings.Parts.CALENDAR_TITLE_BACK);
    private int titleAlign = TITLE_ALIGN;
    private Font titleFont = TITLE_FONT;
    private int cellWidth = 27; //GlobalVariables.getInt("calendar.cell.width");
    private int cellHeight = 18; //GlobalVariables.getInt("calendar.cell.height");
    private int autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS;
    private boolean cellSelectionEnabled = true;
    private Color sundayFore = GlobalSettings.getColors(GlobalSettings.Parts.SUNDAY_FORE);
    private Color saturdayFore = GlobalSettings.getColors(GlobalSettings.Parts.SATURDAY_FORE);
    private Color weekdayFore = GlobalSettings.getColors(GlobalSettings.Parts.WEEKDAY_FORE);
    private Color outOfMothFore = GlobalSettings.getColors(GlobalSettings.Parts.OUTOFMONTH_FORE);
    private Color calendarBack = GlobalSettings.getColors(GlobalSettings.Parts.CALENDAR_BACK);
    private Color todayBack = GlobalSettings.getColors(GlobalSettings.Parts.TODAY_BACK);
    private Color birthdayBack = GlobalSettings.getColors(GlobalSettings.Parts.BIRTHDAY_BACK);
    private Font calendarFont = CALENDAR_FONT;
    private Font outOfMonthFont = OUTOF_MONTH_FONT;

    /**
     * Creates new form LiteCalendarPanel
     */
    public LiteCalendarPanel() {
        super();
        initComponents();
    }

    //  public LiteCalendarPanel(int n, Map<String, Color> colorTable) {
    //       this(n, true, colorTable);
    //  }
    /**
     *
     * @param n
     * @param addTitle
     * @param colorTable
     */
    public LiteCalendarPanel(int n, boolean addTitle, Map<String, Color> colorTable) {

        initComponents();
        // 作成するカレンダの当月を起点とする相対月数（n ケ月前/後)
        relativeMonth = n;
        GregorianCalendar gc = new GregorianCalendar();
        gc.clear(Calendar.MILLISECOND);
        gc.clear(Calendar.SECOND);
        gc.clear(Calendar.MINUTE);
        gc.clear(Calendar.HOUR_OF_DAY);
        gc.add(Calendar.MONTH, relativeMonth);
        year = gc.get(Calendar.YEAR);
        month = gc.get(Calendar.MONTH);

        tableModel = new CalendarTableModel(year, month);
        table.setModel(tableModel);
        setAutoResizeMode(autoResizeMode);
        table.setBackground(calendarBack);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setCellSelectionEnabled(cellSelectionEnabled);

        setCellWidth(cellWidth);
        setCellHeight(cellHeight);

        // Replace DefaultRender
        DateRenderer dateRenderer = new DateRenderer(colorTable);
        dateRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.setDefaultRenderer(java.lang.Object.class, dateRenderer);

        // MouseAdapter
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() != 1) {
                    return;
                }

                Point p = e.getPoint();
                int row = table.rowAtPoint(p);
                int col = table.columnAtPoint(p);
                if (row != -1 && col != -1) {
                    //Object o = table.getValueAt(row, col);
                    Object o = tableModel.getDate(row, col);
                    setSelectedDate(o);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(table.getTableHeader(), BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);

        setTitleLabel(String.format("%04d年%02d月", year, month + 1));
        setTitleAlign(titleAlign);
        setTitleFont(titleFont);
        setTitleFore(titleFore);
        setTitleBack(titleBack);
        getTitleLabel().setOpaque(true);

        // レイアウトする
        this.setLayout(new BorderLayout());
        if (addTitle) {
            this.add(getTitleLabel(), BorderLayout.NORTH);
        }
        this.add(panel, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createEtchedBorder());

        boundSupport = new PropertyChangeSupport(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "1", "1", "1", "1", "1", "1"},
                {"1", "1", "1", "1", "1", "1", "1"},
                {"1", "1", "1", "1", "1", "1", "1"},
                {"1", "1", "1", "1", "1", "1", "1"},
                {"1", "1", "1", "1", "1", "1", "1"}
            },
            new String [] {
                "日", "月", "火", "水", "木", "金", "土"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table.setName("table"); // NOI18N
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables

    /**
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     *
     * @return
     */
    public int getMonth() {
        return month;
    }

    /**
     *
     * @param prop
     * @param l
     */
    @Override
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.addPropertyChangeListener(prop, l);
    }

    /**
     *
     * @param prop
     * @param l
     */
    @Override
    public void removePropertyChangeListener(String prop, PropertyChangeListener l) {
        if (boundSupport == null) {
            boundSupport = new PropertyChangeSupport(this);
        }
        boundSupport.removePropertyChangeListener(prop, l);
    }

    /**
     *
     * @param e
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();
        if (prop.equals(MARK_LIST_PROP)) {
            @SuppressWarnings("unchecked")
            Collection<SimpleDate> list = (Collection<SimpleDate>) e.getNewValue();
            tableModel.setMarkDates(list);
        }
    }

    /**
     * 選択された日を通知する。
     * @param o 
     */
    public void setSelectedDate(Object o) {
        Object old = selectedDate;
        selectedDate = o;
        if (selectedDate instanceof String) {
            SimpleDate sd = new SimpleDate(getYear(), getMonth(), Integer.parseInt((String) selectedDate));
            selectedDate = sd;
        }
        boundSupport.firePropertyChange(SELECTED_DATE_PROP, old, selectedDate);
    }

    /**
     *
     * @return
     */
    public JTable getTable() {
        return table;
    }

    /**
     *
     * @return
     */
    public CalendarTableModel getTableModel() {
        return tableModel;
    }

    /**
     *
     * @return
     */
    public int getRelativeMonth() {
        return relativeMonth;
    }

    /**
     *
     * @return
     */
    public SimpleDate getFirstDate() {
        return tableModel.getFirstDate();
    }

    /**
     *
     * @return
     */
    public SimpleDate getLastDate() {
        return tableModel.getLastDate();
    }

    //  public Map<String, Color> getEventColorTable() {
    //      return eventColorTable;
    //  }
    //   public void setEventColorTable(Map<String, Color> ht) {
    //      eventColorTable = ht;
    //  }
    /**
     * @param titleFore
     *            The titleFore to set.
     */
    public void setTitleFore(Color titleFore) {
        this.titleFore = titleFore;
        getTitleLabel().setForeground(titleFore);
    }

    /**
     * @return Returns the titleFore.
     */
    public Color getTitleFore() {
        return titleFore;
    }

    /**
     * @param titleBack
     *            The titleBack to set.
     */
    private void setTitleBack(Color titleBack) {
        this.titleBack = titleBack;
        getTitleLabel().setBackground(titleBack);
    }

    /**
     * @param titleAlign
     *            The titleAlign to set.
     */
    private void setTitleAlign(int titleAlign) {
        this.titleAlign = titleAlign;
        getTitleLabel().setHorizontalAlignment(titleAlign);
    }

    /**
     * @param titleFont
     *            The titleFont to set.
     */
    private void setTitleFont(Font titleFont) {
        this.titleFont = titleFont;
        getTitleLabel().setFont(titleFont);
    }

    /**
     * @param cellWidth
     *            The cellWidth to set.
     */
    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        TableColumn column = null;
        for (int i = 0; i < 7; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(cellWidth);
        }
    }

    /**
     * @return Returns the cellWidth.
     */
    public int getCellWidth() {
        return cellWidth;
    }

    /**
     * @param cellHeight
     *            The cellHeight to set.
     */
    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
        table.setRowHeight(cellHeight);
    }

    /**
     * @return Returns the cellHeight.
     */
    public int getCellHeight() {
        return cellHeight;
    }

    /**
     * @param mode
     */
    public void setAutoResizeMode(int mode) {
        this.autoResizeMode = mode;
    }

    /**
     * @return Returns the autoResize.
     */
    public int getAutoResizeMode() {
        return autoResizeMode;
    }

    /**
     * @param cellSelectionEnabled
     *            The cellSelectionEnabled to set.
     */
    public void setCellSelectionEnabled(boolean cellSelectionEnabled) {
        this.cellSelectionEnabled = cellSelectionEnabled;
    }

    /**
     * @return Returns the cellSelectionEnabled.
     */
    public boolean isCellSelectionEnabled() {
        return cellSelectionEnabled;
    }

    /**
     * @param sundayFore
     *            The sundayFore to set.
     */
    public void setSundayFore(Color sundayFore) {
        this.sundayFore = sundayFore;
    }

    /**
     * @return Returns the sundayFore.
     */
    public Color getSundayFore() {
        return sundayFore;
    }

    /**
     * @param saturdayFore
     *            The saturdayFore to set.
     */
    public void setSaturdayFore(Color saturdayFore) {
        this.saturdayFore = saturdayFore;
    }

    /**
     * @return Returns the saturdayFore.
     */
    public Color getSaturdayFore() {
        return saturdayFore;
    }

    /**
     * @param weekdayFore
     *            The weekdayFore to set.
     */
    public void setWeekdayFore(Color weekdayFore) {
        this.weekdayFore = weekdayFore;
    }

    /**
     * @return Returns the weekdayFore.
     */
    public Color getWeekdayFore() {
        return weekdayFore;
    }

    /**
     * @param outOfMothFore
     *            The outOfMothFore to set.
     */
    public void setOutOfMothFore(Color outOfMothFore) {
        this.outOfMothFore = outOfMothFore;
    }

    /**
     * @return Returns the outOfMothFore.
     */
    public Color getOutOfMothFore() {
        return outOfMothFore;
    }

    /**
     * @param calendarBack
     *            The calendarBack to set.
     */
    public void setCalendarBack(Color calendarBack) {
        this.calendarBack = calendarBack;
    }

    /**
     * @return Returns the calendarBack.
     */
    public Color getCalendarBack() {
        return calendarBack;
    }

    /**
     * @param todayBack
     *            The todayBack to set.
     */
    public void setTodayBack(Color todayBack) {
        this.todayBack = todayBack;
    }

    /**
     * @return Returns the todayBack.
     */
    public Color getTodayBack() {
        return todayBack;
    }

    /**
     * @param birthdayBack
     *            The birthdayBack to set.
     */
    public void setBirthdayBack(Color birthdayBack) {
        this.birthdayBack = birthdayBack;
    }

    /**
     * @return Returns the birthdayBack.
     */
    public Color getBirthdayBack() {
        return birthdayBack;
    }

    /**
     * @param calendarFont
     *            The calendarFont to set.
     */
    public void setCalendarFont(Font calendarFont) {
        this.calendarFont = calendarFont;
    }

    /**
     * @return Returns the calendarFont.
     */
    public Font getCalendarFont() {
        return calendarFont;
    }

    /**
     * @param outOfMonthFont
     *            The outOfMonthFont to set.
     */
    public void setOutOfMonthFont(Font outOfMonthFont) {
        this.outOfMonthFont = outOfMonthFont;
    }

    /**
     * @return Returns the outOfMonthFont.
     */
    public Font getOutOfMonthFont() {
        return outOfMonthFont;
    }

    /**
     *
     * @param today
     */
    public void setToday(SimpleDate today) {
        this.today = today;
    }

    /**
     * @param titleLabel
     *            The titleLabel to set.
     */
    public void setTitleLabel(JLabel titleLabel) {
        this.titleLabel = titleLabel;
    }

    /**
     * @return Returns the titleLabel.
     */
    public JLabel getTitleLabel() {
        return titleLabel;
    }

    private void setTitleLabel(String labelText) {
        setTitleLabel(new JLabel(labelText));
    }

    /**
     * Custom table cell renderer for the carendar panel.
     */
    protected class DateRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 5817292848730765481L;
        Map<String, Color> event;

        /**
         *
         * @param eventColorTable
         */
        public DateRenderer(Map<String, Color> eventColorTable) {
            super();
            this.setOpaque(true);
            this.setHorizontalAlignment(SwingConstants.RIGHT);
            this.event = eventColorTable;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int col) {

            Component compo = super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, col);
            if (compo != null && value != null) {

                // 日を書く
                String day = null;
                Color color = null;

                if (value instanceof SimpleDate) {
                    day = ((SimpleDate) value).toString();
                    if (today != null && today.compareTo((SimpleDate) value) == 0) {
                        // color = todayBack;
                        color = this.event.get("TODAY");
                    } else {
                        color = this.event.get(((SimpleDate) value).getEventCode());
                        // color = Color.black;
                    }

                } else if (value instanceof String) {
                    day = (String) value;
                    if (today != null && today.equalDate(year, month, Integer.parseInt(day))) {
                        // color = todayBack;
                        color = this.event.get("TODAY");
                    } else {
                        color = getCalendarBack();
                    }
                }

                ((JLabel) compo).setText(day);

                // 曜日によって ForeColor を変える
                if (col == 0) {
                    this.setForeground(getSundayFore());

                } else if (col == 6) {
                    this.setForeground(getSaturdayFore());

                } else {
                    this.setForeground(getWeekdayFore());
                }

                // このカレンダ月内の日かどうかでフォントを変える
                if (tableModel.isOutOfMonth(row, col)) {
                    this.setFont(getOutOfMonthFont());
                    this.setBackground(getCalendarBack());

                } else {
                    this.setFont(getCalendarFont());
                    this.setBackground(color);
                }
            }
            return compo;
        }
    }
}
