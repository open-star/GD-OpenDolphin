/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client;

import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import open.dolphin.infomodel.ClaimBundle;
import open.dolphin.infomodel.ClaimItem;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.infomodel.UserModel;
import open.dolphin.log.LogWriter;
import open.dolphin.project.GlobalVariablesImplement;

/**
 *　カルテ印刷
 * @author
 */
public class PrintKarte implements Printable {

    private List<ModuleModel> models;
    private String assignment;
    private GlobalVariablesImplement globalVariables;
    private String description;//書類の種別。右上に印刷されるのみ。
    private List<Integer> modelIndexPerPage; //ページ単位に印刷可能なモデルのインデックス。ページ区切りに使用。
    private int top_margin;
    private int left_margin;

    /**
     * 
     * @param job
     * @param assignment
     * @param models
     * @param grobalVariables
     * @param description
     * @param top_margin
     * @param left_margin
     */
    public PrintKarte(DocPrintJob job, String assignment, List<ModuleModel> models, GlobalVariablesImplement grobalVariables, String description, int top_margin, int left_margin) {
        this.globalVariables = grobalVariables;
        this.models = models;
        this.assignment = assignment;
        this.description = description;
        this.top_margin = top_margin;
        this.left_margin = left_margin;

        modelIndexPerPage = new ArrayList<Integer>();

        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(new JobName(assignment, null));
        attributes.add(new Copies(1));
        //     attributes.add(MediaName.ISO_A4_WHITE);
        attributes.add(MediaSizeName.ISO_A4);
        attributes.add(OrientationRequested.PORTRAIT);

        try {
            Doc document = new SimpleDoc(this, DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);
            job.print(document, attributes);
        } catch (PrintException e) {
            LogWriter.error(getClass(), e);
        }
    }

    /**
     * 
     * @return
     */
    private String getUserID() {
        return globalVariables.getUserId();
    }

    /**
     * 
     * @return
     */
    private UserModel getUserModel() {
        return globalVariables.getUserModel();
    }

    /**
     * 
     * @return
     */
    private boolean hasKarte() {
        return (models.size() > 0);
    }

    /**
     * 
     * @return
     */
    private KarteBean getKarte() {
        KarteBean result = null;
        if (hasKarte()) {
            result = models.get(0).getKarte();
        }
        return result;
    }

    /**
     * 
     * @return
     */
    private PatientModel getPatient() {
        PatientModel result = null;
        KarteBean karte = getKarte();
        if (karte != null) {
            result = getKarte().getPatient();
        }
        return result;
    }

    /**
     * 
     * @param item
     * @return
     */
    private String getItemName(ClaimItem item) {
        return item.getName();
    }

    /**
     * 
     * @param item
     * @return
     */
    private String getItemCount(ClaimItem item) {
        String result = "";
        String item_count = item.getNumber();
        String item_unit = item.getUnit();
        if (item_count != null) {
            result += item_count;
        }
        if (item_unit != null) {
            result += item_unit;
        }
        return result;
    }

    /**
     * 
     * @param page
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param thickness
     */
    private void drawLine(Graphics2D page, int x1, int y1, int x2, int y2, float thickness) {
        Stroke current = page.getStroke();
        page.setStroke(new BasicStroke(thickness));
        page.drawLine(x1, y1, x2, y2);
        page.setStroke(current);
    }

    /**
     * 
     * @param page
     * @param x
     * @param y
     * @param width
     * @param height
     * @param thickness
     */
    private void drawRect(Graphics2D page, int x, int y, int width, int height, float thickness) {
        Stroke current = page.getStroke();
        page.setStroke(new BasicStroke(thickness));
        page.drawRect(x, y, width, height);
        page.setStroke(current);
    }

    /**
     * 
     * @param page
     * @param x
     * @param y
     * @param str
     * @param font
     */
    private void drawString(Graphics2D page, int x, int y, String str, Font font) {
        Font current = page.getFont();
        page.setFont(font);
        page.drawString(str, x, y);
        page.setFont(current);
    }

    /**
     * printXXXXメソッドには、印刷と計測の2つの機能を持つ必要がある。
     * pageをnullとすると計測、その他は印刷。
     * @param page
     * @param top
     * @return
     */
    private int printUser(Graphics2D page, int top) {
        top += 15;
        if (page != null) {//page == nullならば計測のみを行う。
            UserModel user = getUserModel();
            drawString(page, 10 + left_margin, top, getUserID() + " " + user.getLicenseModel().getLicenseDesc() + " " + user.getDepartmentModel().getDepartmentDesc() + " " + user.getFacility().getFacilityName(), new Font(null, 0, 10));
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param top
     * @return
     */
    private int printDate(Graphics2D page, int top) {
        top += 15;
        if (page != null) {//page == nullならば計測のみを行う。
            Date now = new Date();
            drawString(page, 10 + left_margin, top, now.toString(), new Font(null, 0, 10));
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param top
     * @return
     */
    private int printPatient(Graphics2D page, int top) {
        top += 10;
        if (page != null) {//page == nullならば計測のみを行う。
            drawString(page, 60 + left_margin, top, getPatient().getKanaName(), new Font("SansSerif", Font.BOLD, 6));
        }
        top += 14;
        if (page != null) {//page == nullならば計測のみを行う。
            drawString(page, 15 + left_margin, top, getPatient().getPatientId(), new Font("SansSerif", Font.BOLD, 12));
            drawString(page, 60 + left_margin, top, getPatient().getFullName() + "  " + getPatient().getGenderDesc() + "  " + getPatient().getAgeBirthday(), new Font("SansSerif", Font.BOLD, 12));
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param length
     * @param top
     * @return
     */
    private int printLine(Graphics2D page, int length, int top) {
        top += 8;
        if (page != null) {//page == nullならば計測のみを行う。
            page.drawLine(10 + left_margin, top, length, top);
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param top
     * @return
     */
    private int printSignatureRect(Graphics2D page, int top) {
        if (page != null) {//page == nullならば計測のみを行う。
            drawRect(page, 390 + left_margin, 15 + top_margin, 56, 56, 2.0f);
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param top
     * @param currentPage
     * @param pageCount
     * @return
     */
    private int printTitle(Graphics2D page, int top, int currentPage, int pageCount) {
        if (page != null) {//page == nullならば計測のみを行う。
            String pageName = "";
            if (pageCount != 1) {//１ページならページ数を印刷しない
                pageName = "(" + Integer.toString(currentPage + 1) + "/" + Integer.toString(pageCount) + ")";
            }
            drawString(page, 10 + left_margin, top, assignment + "箋 " + pageName, new Font(null, 0, 20));
            drawString(page, 400 + left_margin, top, description, new Font(null, 0, 8));
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param top
     * @param currentPage
     * @param pageCount
     * @return
     */
    private int printHeader(Graphics2D page, int top, int currentPage, int pageCount) {
        top = printTitle(page, top, currentPage, pageCount);
        top = printLine(page, 460 + left_margin, top);
        top = printUser(page, top);
        top = printDate(page, top);
        top = printLine(page, 380 + left_margin, top);
        top = printPatient(page, top);
        top = printSignatureRect(page, top);
        top = printLine(page, 460 + left_margin, top);
        top += 22;
        return top;
    }

    /**
     * 
     * @param page
     * @param model
     * @param top
     * @return
     */
    private int printStampName(Graphics2D page, ModuleModel model, int top) {
        top += 25;
        if (page != null) {//page == nullならば計測のみを行う。
            String stampName = model.getModuleInfo().getStampName();
            drawRect(page, 20 + left_margin, top - 9, 9, 9, 1.2f);
            drawString(page, 30 + left_margin, top, stampName, new Font("SansSerif", Font.BOLD, 10));
            drawLine(page, 20 + left_margin, top + 4, 440 + left_margin, top + 4, 0.5f);
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param item
     * @param top
     * @return
     */
    private int printItem(Graphics2D page, ClaimItem item, int top) {
        top += 15;
        if (page != null) {//page == nullならば計測のみを行う。
            if (getItemName(item) != null) {
                drawRect(page, 35 + left_margin, top - 8, 8, 8, 0.8f);
                drawString(page, 45 + left_margin, top, getItemName(item), new Font(null, 0, 10));
                drawString(page, 400 + left_margin, top, getItemCount(item), new Font(null, 0, 10));
            }
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param bundle
     * @param top
     * @return
     */
    private int printAdmin(Graphics2D page, ClaimBundle bundle, int top) {
        String admin = bundle.getAdmin();
        String bundleNumber = bundle.getBundleNumber();

        if (admin != null) {
            if (bundleNumber == null) {
                bundleNumber = "";
            }

            top += 6;

            if (page != null) {//page == nullならば計測のみを行う。
                drawRect(page, 35 + left_margin, top, 390, 20, 0.2f);
            }

            top += 14;

            if (page != null) {//page == nullならば計測のみを行う。
                drawString(page, 45 + left_margin, top, admin, new Font(null, 0, 10));
                drawString(page, 400 + left_margin, top, bundleNumber, new Font(null, 0, 10));
            }
        }
        return top;
    }

    /**
     * 
     * @param page
     * @param modelIndex
     * @param top
     * @return
     */
    private int printStamp(Graphics2D page, int modelIndex, int top) {
        top = printStampName(page, models.get(modelIndex), top);
        ClaimBundle bundle = (ClaimBundle) models.get(modelIndex).getModel();
        ClaimItem[] item = bundle.getClaimItem();
        for (int itemIndex = 0; itemIndex < item.length; itemIndex++) {
            top = printItem(page, item[itemIndex], top);
        }
        top = printAdmin(page, bundle, top);
        return top;
    }

    /**
     * 
     * @param top
     * @param currentPage
     * @param pageCount
     * @return
     */
    private int measureHeaderSize(int top, int currentPage, int pageCount) {
        return printHeader(null, top, currentPage, pageCount);
    }

    /**
     * 
     * @param modelIndex
     * @param top
     * @return
     */
    private int measureStampSize(int modelIndex, int top) {
        return printStamp(null, modelIndex, top);
    }

    /**
     * 
     * @param page
     * @param pageLength
     * @param currentPage
     * @param pageCount
     * @return
     */
    private boolean printPage(Graphics2D page, double pageLength, int currentPage, int pageCount) {
        if (currentPage < modelIndexPerPage.size()) {
            if (modelIndexPerPage.get(currentPage) < models.size()) {
                int top = top_margin;
                top = printHeader(page, top, currentPage, pageCount);       //ヘッダ印刷。
                int modelCount = modelIndexPerPage.get(currentPage);        //そのページに印刷するモデルのインデックス（先頭）
                for (; modelCount < models.size(); modelCount++) {          //モデルのインデックスがモデル数を上回らるなら終わり
                    if (measureStampSize(modelCount, top) < pageLength) {   //スタンプがそのページに収まるなら
                        top = printStamp(page, modelCount, top);            //スタンプを印刷
                    } else {
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * modelIndexPerPageにモデルのインデックスを設定
     * @param currentIndex
     * @param pageLength
     * @param currentPage
     * @return
     */
    private int measurePageSize(int currentIndex, double pageLength, int currentPage) {
        int top = top_margin;
        top = measureHeaderSize(top, currentPage, 0);
        for (; currentIndex < models.size(); currentIndex++) {
            top = measureStampSize(currentIndex, top);
            if (top >= pageLength) {
                modelIndexPerPage.add(currentIndex);
                break;
            }
        }
        return currentIndex;
    }

    /**
     * ページ数を計測し、同時にmodelIndexPerPageにモデルのインデックスを設定
     * @param pageLength
     * @return
     */
    private int measure(double pageLength) {
        int currentIndex = 0;
        int currentPage = 0;
        modelIndexPerPage.clear();
        modelIndexPerPage.add(0);
        while (true) {
            currentIndex = measurePageSize(currentIndex, pageLength, currentPage);
            currentPage++;
            if (currentIndex >= models.size()) {
                break;
            }
        }
        return currentPage;
    }

    /**
     *
     * @param graphics
     * @param format
     * @param pageIndex
     * @return
     */
    @Override
    public int print(Graphics graphics, PageFormat format, int pageIndex) {
        Graphics2D page = (Graphics2D) graphics;
        page.translate(format.getImageableX(), format.getImageableY());
        page.setColor(Color.black);
        int pages = measure(format.getImageableHeight() - top_margin);//ページ数計測
        if (printPage(page, format.getImageableHeight() - top_margin, pageIndex, pages)) {
            return Printable.PAGE_EXISTS;
        } else {
            return Printable.NO_SUCH_PAGE;
        }
    }
}
