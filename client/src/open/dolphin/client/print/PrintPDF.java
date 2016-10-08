/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.client.print;

import open.dolphin.project.GlobalConstants;
import open.dolphin.client.PDFLetterMaker;
import open.dolphin.helper.FileExecutor;
import open.dolphin.infomodel.TouTouLetter;
import open.dolphin.project.GlobalVariables;

import java.awt.Component;
import java.awt.EventQueue;

/**
 *
 * @author taka
 */
public class PrintPDF {
    private Component parent;
    /**
     * 
     * @param parent
     */
    public PrintPDF(Component parent) {
        this.parent = parent;
    }

    /**
     *
     * @param model
     */
    public void exec(final TouTouLetter model) {
        Runnable r = new Runnable() {

            public void run() {

                final PDFLetterMaker pdf = new PDFLetterMaker(GlobalVariables.getLetterGreetings());
                String pdfDir = GlobalVariables.getPreferences().get("pdfStore", System.getProperty("user.dir"));
                pdf.setDocumentDir(pdfDir);
                pdf.setModel(model);
                final boolean result = pdf.create();

                Runnable awt = new Runnable() {

                    public void run() {
                        if (result) {
                            StringBuilder b = new StringBuilder();
                            b.append(pdf.getDocumentDir());
                            b.append(java.io.File.separator);
                            b.append(pdf.getFileName());
                            FileExecutor.execute(b.toString());
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(
                                    parent,
                                    "紹介状PDFファイルを生成することができません。",
                                    GlobalConstants.getFrameTitle("紹介状作成"),
                                    javax.swing.JOptionPane.WARNING_MESSAGE);
                        }
                    }
                };
                EventQueue.invokeLater(awt);
            }
        };
        Thread t = new Thread(r);
        t.setPriority(Thread.NORM_PRIORITY);
        t.start();
    }
}
