/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.plugin;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.UUID;
import javax.swing.tree.DefaultMutableTreeNode;

import open.dolphin.client.IChart;
import open.dolphin.client.IChartDocument;
import open.dolphin.client.KartePane;
import open.dolphin.infomodel.BundleMed;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.IStampInfo;
import open.dolphin.infomodel.ModuleInfoBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.log.LogWriter;

/**
 *
 * @author
 */
public class DoPanelPlugin implements open.dolphin.plugin.IPlugin {

    private Type getType() {
        return open.dolphin.plugin.IPlugin.Type.panel;
    }

    private String getName() {
        return DoPanel.TITLE;
    }

    private String getReadableName() {
        return "DoPanel";
    }

    /**
     * パネルオブジェクトを返す
     * @param  parent カルテ
     * @return パネルオブジェクト
     */
    private IChartDocument panel(IChart parent) {
        return new DoPanel(parent);
    }

    /**
     * データの種別を返す
     * @return データの種別
     */
    private DataFlavor getFlavor() {
        try {
            return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=javax.swing.tree.TreeNode");
        } catch (ClassNotFoundException ex) {
            LogWriter.error(getClass(), ex);
        }
        return null;
    }

    private void textToStamp(KartePane pane, DoHeaderObject header, ArrayList<DoBodyObject> body) {
        if (body != null) {
            IStampInfo stampInfo = new ModuleInfoBean();

            String s = UUID.randomUUID().toString();
            String id = s.substring(0, 8) + s.substring(9, 4 + 9) + s.substring(14, 4 + 14) + s.substring(19, 4 + 19) + s.substring(24, 12 + 24);
            stampInfo.initialize(id, header.getName(), "", "medOrder", "p");

            stampInfo.setStampStatus("");
            stampInfo.setASP(false);
            stampInfo.setEditable(true);
            stampInfo.setTurnIn(false);
            stampInfo.setStampNumber(0);

            BundleMed model = new BundleMed();
            model.setAdmin("");//１回１個医師の指示通りに
            model.setAdminCode("001000801");
            model.setAdminCodeSystem(null);
            model.setAdminMemo(null);
            model.setClassCode("230");
            model.setClassCodeSystem("Claim007");
            model.setClassName("");//投薬(外用・調剤)(入院外)
            model.setMemo("");//院外処方
            model.setOrderName(null);
            model.setStatus(null);

            model.setBundleNumber(1);
            ClaimItem[] items = new ClaimItem[body.size()];
            int count = 0;
            for (DoBodyObject element : body) {
                ClaimItem item = new ClaimItem();
                item.setYkzKbn("1");
                item.setNumber(Double.toString(element.getCount()));
                item.setNumberCode("10");
                item.setClassCode("2");
                item.setStartDate("99999999");
                item.setEndDate("99999999");
                item.setCode(element.getCode());
                item.setClassCodeSystem("Claim003");
                item.setNumberCodeSystem("Claim004");
                item.setName(element.getName());
                item.setUnit(element.getUnit());
                item.setSuryo1(0.0f);
                item.setSuryo2(0.0f);
                item.setCodeSystem(null);
                item.setMemo(null);
                item.setSstKijunCdSet(null);
                items[count] = item;
                count++;
            }
            model.setClaimItem(items);

            if (model != null) {
                ModuleModel stamp = new ModuleModel();
                stamp.setModel(model);
                stamp.setModuleInfo(stampInfo);
                pane.stamp(stamp);
            }
        }
    }

    private boolean drop(KartePane pane, Transferable transferable) {
        try {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) transferable.getTransferData(DoStampTreeNodeTransferable.doStampTreeNodeFlavor);
            DoHeaderObject header = (DoHeaderObject) root.getUserObject();
            Enumeration<DefaultMutableTreeNode> e;
            DefaultMutableTreeNode currentNode = null;
            ArrayList<DoBodyObject> bodies = new ArrayList<DoBodyObject>();
            for (e = root.children(); e.hasMoreElements();) {
                currentNode = e.nextElement();
                DoBodyObject body = (DoBodyObject) currentNode.getUserObject();
                bodies.add(body);
            }
            textToStamp(pane, header, bodies);
        } catch (UnsupportedFlavorException ex) {
            LogWriter.info(getClass(), "");
        } catch (IOException ex) {
            LogWriter.error(getClass(), ex);
        }
        return true;
    }

    private Boolean update(Object object) {
        return false;
    }

    /**
     *
     * @param command
     * @param request
     * @param response
     * @return
     */
    @Override
    public boolean dispatchCommand(open.dolphin.plugin.IPlugin.Command command, Object[] request, Object[] response) {
        switch (command) {
            case getType:
                response[0] = getType();
                break;
            case getName:
                response[0] = getName();
                break;
            case getReadableName:
                response[0] = getReadableName();
                break;
            case panel:
                response[0] = panel(((IChart) request[0]));
                break;
            case getFlavor:
                response[0] = getFlavor();
                break;
            case drop:
                boolean res = drop(((KartePane) request[0]), ((Transferable) request[1]));
                response[0] = res;
                break;
            case update:
                response[0] = update(request[0]);
                break;
            default:
                return false;
        }
        return true;
    }
}
