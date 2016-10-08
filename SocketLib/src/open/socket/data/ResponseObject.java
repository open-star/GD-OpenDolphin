/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.socket.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * OpenDolphinServerからのレスポンスデータ
 *
 * @author
 */
public class ResponseObject implements TableModel, Serializable {

    private long id;
    private Command command;                                 // Command
    private ArrayList stringArray = new ArrayList();         //for tableModel
    private int rowCount = 6;                                //for tableModel
    private String[] columnNames = {"", "", "", "", "", ""}; //for tableModel

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the command
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        build(data);
    }

    /**
     *
     * @param stampXml
     */
    private void build(String stampXml) {
        SAXBuilder docBuilder = new SAXBuilder();
        try {
            BufferedReader reader = new BufferedReader(new StringReader(stampXml));
            Document doc = docBuilder.build(reader);
            Element root = doc.getRootElement();
            parseChildren(root);
        } catch (IOException e) {

        } catch (Exception e) {
        }
    }

    /**
     *
     * @param current
     */
    private void parseChildren(Element current) {
        String hoge = "";// MEMO;Unused?
        List children = current.getChildren();
        if (!children.isEmpty()) {
            Iterator iterator = children.iterator();
            while (iterator.hasNext()) {
                Element child = (Element) iterator.next();
                parseChildren(child);
            }
        } else {
            hoge = current.getValue();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public int getRowCount() {
        return stringArray.size();
    }

    /**
     *
     * @return
     */
    @Override
    public int getColumnCount() {
        return rowCount;
    }

    /**
     *
     * @param columnIndex
     * @return
     */
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    /**
     *
     * @param columnIndex
     * @return
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ArrayList.class;
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((ArrayList) stringArray.get(rowIndex)).get(columnIndex);
    }

    /**
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param l
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param l
     */
    @Override
    public void removeTableModelListener(TableModelListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
