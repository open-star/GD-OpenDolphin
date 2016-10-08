package open.dolphin.client;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.TouTouReply;

/**
 * 紹介状の PDF メーカー。
 * @author Kazushi Minagawa, Digital Globe, Inc.
 */
public class PDFReplyMaker {

    private static final String DOC_TITLE = "紹介患者経過報告書";
    private static final String HEISEI_MIN_W3 = "HeiseiMin-W3";
    private static final String UNIJIS_UCS2_HW_H = "UniJIS-UCS2-HW-H";
    private static final int TOP_MARGIN = 75;
    private static final int LEFT_MARGIN = 75;
    private static final int BOTTOM_MARGIN = 75;
    private static final int RIGHT_MARGIN = 75;
    private static final int TITLE_FONT_SIZE = 14;
    private static final int BODY_FONT_SIZE = 12;
    private String documentDir;
    private String fileName;
    private TouTouReply model;
    private int marginLeft = LEFT_MARGIN;
    private int marginRight = RIGHT_MARGIN;
    private int marginTop = TOP_MARGIN;
    private int marginBottom = BOTTOM_MARGIN;
    private BaseFont baseFont;
    private Font titleFont;
    private Font bodyFont;
    private int titleFontSize = TITLE_FONT_SIZE;
    private int bodyFontSize = BODY_FONT_SIZE;

    /**
     *
     * @return
     */
    public boolean create() {

        boolean result = true;

        try {

            Document document = new Document(
                    PageSize.A4,
                    getMarginLeft(),
                    getMarginRight(),
                    getMarginTop(),
                    getMarginBottom());

            if (documentDir == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(System.getProperty("user.dir"));
                sb.append(File.separator);
                sb.append("pdf");
                setDocumentDir(sb.toString());
            }
            File dir = new File(getDocumentDir());
            dir.mkdir();

            String name = model.getPatientName();
            name = name.replaceAll(" ", "");
            name = name.replaceAll("　", "");
            StringBuilder sb = new StringBuilder();
            sb.append("紹介患者経過報告書-");
            sb.append(name);
            sb.append("様-");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sb.append(sdf.format(new Date()));
            sb.append(".pdf");
            setFileName(sb.toString());

            sb = new StringBuilder();
            if (getDocumentDir() != null) {
                sb.append(getDocumentDir());
                sb.append(File.separator);
            }
            sb.append(getFileName());

            PdfWriter.getInstance(document, new FileOutputStream(sb.toString()));
            document.open();

            // Font
            baseFont = BaseFont.createFont(HEISEI_MIN_W3, UNIJIS_UCS2_HW_H, false);
            titleFont = new Font(baseFont, getTitleFontSize());
            bodyFont = new Font(baseFont, getBodyFontSize());

            // タイトル
            Paragraph para = new Paragraph(DOC_TITLE, titleFont);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);

            // 日付
            String dateStr = getDateString(model.getConfirmed());
            para = new Paragraph(dateStr, bodyFont);
            para.setAlignment(Element.ALIGN_RIGHT);
            document.add(para);

            document.add(new Paragraph("　"));

            // 紹介元病院
            Paragraph para2 = new Paragraph(model.getClientHospital(), bodyFont);
            para2.setAlignment(Element.ALIGN_LEFT);
            document.add(para2);

            // 紹介元診療科
            para2 = new Paragraph(model.getClientDept(), bodyFont);
            para2.setAlignment(Element.ALIGN_LEFT);
            document.add(para2);

            // 紹介元医師
            sb = new StringBuilder();
            sb.append(model.getClientDoctor());
            sb.append(" 先生");
            para2 = new Paragraph(sb.toString(), bodyFont);
            para2.setAlignment(Element.ALIGN_LEFT);
            document.add(para2);

            // 紹介先病院
            para2 = new Paragraph(model.getConsultantHospital(), bodyFont);
            para2.setAlignment(Element.ALIGN_RIGHT);
            document.add(para2);

            document.add(new Paragraph("　"));

            // 挨拶
            sb = new StringBuilder();
            sb.append("ご紹介いただきました ");
            sb.append(model.getPatientName());
            sb.append(" 殿(生年月日: ");
            sb.append(getDateString(model.getPatientBirthday()));
            sb.append(")、");
            sb.append(getDateString(model.getVisited()));
            sb.append(" 受診され、");
            sb.append("拝見し、下記のとおり説明いたしました。");
            para2 = new Paragraph(sb.toString(), bodyFont);
            para2.setAlignment(Element.ALIGN_LEFT);
            document.add(para2);

            document.add(new Paragraph("　"));

            para2 = new Paragraph(model.getInformedContent(), bodyFont);
            para2.setAlignment(Element.ALIGN_LEFT);
            document.add(para2);

            document.add(new Paragraph("　"));
            document.add(new Paragraph("　"));

            sb = new StringBuilder();
            sb.append("ご紹介いただき、ありがとうございました。");
            para2 = new Paragraph(sb.toString(), bodyFont);
            para2.setAlignment(Element.ALIGN_LEFT);
            document.add(para2);

            sb = new StringBuilder();
            sb.append("取り急ぎ返信まで。");
            para2 = new Paragraph(sb.toString(), bodyFont);
            para2.setAlignment(Element.ALIGN_LEFT);
            document.add(para2);

            para2 = new Paragraph(model.getConsultantHospital(), bodyFont);
            para2.setAlignment(Element.ALIGN_RIGHT);
            document.add(para2);

            document.close();

        } catch (IOException ex) {
            //       GlobalVariables.getBootLogger().warn(ex);
            result = false;
        } catch (DocumentException ex) {
            //      GlobalVariables.getBootLogger().warn(ex);
            result = false;
        }

        return result;
    }

    /**
     *
     * @param d
     * @return
     */
    private String getDateString(Date d) {
        return ModelUtils.getDateAsFormatString(d, "yyyy年M月d日");
    }

    /**
     *
     * @param date
     * @return
     */
    private String getDateString(String date) {
        Date d = ModelUtils.getDateAsObject(date);
        return ModelUtils.getDateAsFormatString(d, "yyyy年M月d日");
    }

    /**
     * MEMO: unused?
     * @param sex
     * @return
     */
    private String getSexString(String sex) {
        //return ModelUtils.getGenderDesc(sex);
        return sex;
    }

    /**
     *
     * @return
     */
    public LetterModel getModel() {
        return model;
    }

    /**
     *
     * @param model
     */
    public void setModel(TouTouReply model) {
        this.model = model;
    }

    /**
     *
     * @return
     */
    public int getMarginLeft() {
        return marginLeft;
    }

    /**
     *
     * @param marginleft
     */
    public void setMarginLeft(int marginleft) {
        this.marginLeft = marginleft;
    }

    /**
     *
     * @return
     */
    public int getMarginRight() {
        return marginRight;
    }

    /**
     *
     * @param marginRight
     */
    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    /**
     *
     * @return
     */
    public int getMarginTop() {
        return marginTop;
    }

    /**
     *
     * @param marginTop
     */
    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    /**
     *
     * @return
     */
    public int getMarginBottom() {
        return marginBottom;
    }

    /**
     *
     * @param marginBottom
     */
    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    /**
     *
     * @return
     */
    public int getTitleFontSize() {
        return titleFontSize;
    }

    /**
     *
     * @param titleFontSize
     */
    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    /**
     *
     * @return
     */
    public int getBodyFontSize() {
        return bodyFontSize;
    }

    /**
     *
     * @param bodyFontSize
     */
    public void setBodyFontSize(int bodyFontSize) {
        this.bodyFontSize = bodyFontSize;
    }

    /**
     *
     * @return
     */
    public String getDocumentDir() {
        return documentDir;
    }

    /**
     *
     * @param documentDir
     */
    public void setDocumentDir(String documentDir) {
        this.documentDir = documentDir;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
