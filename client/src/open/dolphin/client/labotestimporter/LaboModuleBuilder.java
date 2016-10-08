package open.dolphin.client.labotestimporter;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.transaction.NotSupportedException;
import open.dolphin.delegater.error.CantFindPatentExcepion;

import open.dolphin.delegater.remote.RemoteLaboDelegater;
import open.dolphin.infomodel.LaboImportSummary;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.LaboSpecimenValue;
import open.dolphin.infomodel.PatientModel;

import open.dolphin.log.LogWriter;
import org.jdom.JDOMException;

/**
 * LaboModuleBuilder
 *
 * @author Kazushi Minagawa
 * @authro tomohiro
 */
public class LaboModuleBuilder {

    private RemoteLaboDelegater laboDelegater;
    private static final String MML_EXTENSION = ".xml";
    private static final String MEDIS_EXTENSION = ".txt";

    /**
     *
     */
    public LaboModuleBuilder() {
    }

    /**
     *
     * @return
     */
    public RemoteLaboDelegater getLaboDelegater() {
        return laboDelegater;
    }

    /**
     *
     * @param laboDelegater
     */
    public void setLaboDelegater(RemoteLaboDelegater laboDelegater) {
        this.laboDelegater = laboDelegater;
    }

    /**
     * MML検査結果ファイルをパースする。
     * @param files MML検査結果ファイルの配列
     * @return 
     */
    public List<LaboImportSummary> build(List<File> files) {

        if (files == null || files.size() == 0) {
            //       LogWriter.warn(LaboModuleBuilder.class, "パースするファイルがありません");
            return null;
        }
        if (laboDelegater == null) {
            //       LogWriter.warn(LaboModuleBuilder.class, "ラボテスト用のデリゲータが設定されていません");
            return null;
        }

        // パース及び登録に成功したデータの情報リストを生成する
        List<LaboImportSummary> result = new ArrayList<LaboImportSummary>(files.size());

        for (File file : files) {
            List<LaboModuleValue> modules = new ArrayList<LaboModuleValue>(1);
            try {
                String name = file.getName();//MEMO: unused?
                //             LogWriter.info(LaboModuleBuilder.class, name + " のパースを開始します");
                buildFromFile(modules, file);
            } catch (IOException ex) {
                //              LogWriter.warn(LaboModuleBuilder.class, "パース中に例外が生じました。");
                String message = "不正な検査データである可能性があります。データを確認してください。";
                JOptionPane.showMessageDialog(null, message, "読み込みエラー", JOptionPane.WARNING_MESSAGE);
                continue;
            } catch (NotSupportedException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "読み込みエラー", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "読み込みエラー", JOptionPane.WARNING_MESSAGE);
                if (ex.getCause() != null) {
                    LogWriter.error(LaboModuleBuilder.class, "原因: " + ex.getCause());
                }
                if (ex.getMessage() != null) {
                    LogWriter.error(LaboModuleBuilder.class, "内容: " + ex.getMessage());
                }
                continue;
            }
            if (isDuplicatedLaboTest(modules)) {
                StringBuilder message = new StringBuilder();
                message.append("すでに採取日、報告日、登録日が同じ検査データが登録されています" + System.getProperty("line.separator"));
                message.append("重複する可能性がありますが本当に登録しますか?" + System.getProperty("line.separator"));
                int selected = JOptionPane.showConfirmDialog(null, message.toString(), "確認", JOptionPane.YES_NO_OPTION);
                if (selected == JOptionPane.NO_OPTION) {
                    return result;
                }
            }
            finnalization(modules, result);
        }
        return result;
    }

    /**
     *
     * @param modules
     * @param file
     * @throws NotSupportedException
     * @throws IOException
     */
    private void buildFromFile(List<LaboModuleValue> modules, File file) throws NotSupportedException, IOException {
        String fileName = file.getName();
        if (fileName.toLowerCase().endsWith(LaboModuleBuilder.MML_EXTENSION)) {
            try {
                readMmlFile(modules, file);
            } catch (JDOMException ex) {
                throw new IOException(ex.getMessage(), ex.getCause());
            }
            return;
        }
        if (fileName.toLowerCase().endsWith(LaboModuleBuilder.MEDIS_EXTENSION)) {
            readMedisFile(modules, file);
            return;
        }
        throw new NotSupportedException("対応していない形式です");
    }

    /**
     *
     * @param modules
     * @param file
     * @throws IOException
     */
    private void readMedisFile(List<LaboModuleValue> modules, File file) throws IOException {
        MedisParser medisParser = new MedisParser();
        List<LaboTestInformation> result = medisParser.parse(new FileInputStream(file));
        MedisBuilder medisBuilder = new MedisBuilder(modules);
        medisBuilder.build(result);
    }

    /**
     *
     * @param modules
     * @param file
     * @throws IOException
     * @throws JDOMException
     */
    private void readMmlFile(List<LaboModuleValue> modules, File file) throws IOException, JDOMException {
        MmlParser mmlParser = new MmlParser();
        List<LaboTestInformation> result = mmlParser.parse(new FileInputStream(file));
        MmlBuilder mmlBuilder = new MmlBuilder(modules);
        try {
            mmlBuilder.build(result);
        } catch (LaboTestIsNotFinalReportException ex) {
            String status = ex.getLaboTestInformation().getReportStatus();
            String message = "この検査データが最終報告ではありません：" + status + " (" + file.getName() + ")";
            JOptionPane.showMessageDialog(null, message, "取り込みエラー", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     *
     * @param modules
     * @return
     */
    private boolean isDuplicatedLaboTest(List<LaboModuleValue> modules) {
        boolean result = false;
        for (LaboModuleValue module : modules) {
            try {
                result = laboDelegater.isDuplicatedLaboTest(module);
            } catch (CantFindPatentExcepion ex) {
                JOptionPane.showMessageDialog(null, "患者ID: " + module.getPatientId() + " の患者が存在しません");
            }
        }
        return result;
    }

    /**
     *
     * @param modules
     * @param result
     */
    private void finnalization(List<LaboModuleValue> modules, List<LaboImportSummary> result) {
        for (LaboModuleValue module : modules) {

            LaboImportSummary summary = new LaboImportSummary();
            summary.setPatientId(module.getPatientId());

            if (module.getSetName() == null) {
                for (LaboSpecimenValue specimen : module.getLaboSpecimens()) {
                    summary.setSetName(specimen.getSpecimenName());
                    //            LogWriter.info(LaboModuleBuilder.class, "検体名=" + specimen.getSpecimenName());
                }
            } else {
                summary.setSetName(module.getSetName());
            }

            summary.setSampleTime(module.getSampleTime());
            summary.setReportTime(module.getReportTime());
            summary.setLaboratoryCenter(module.getLaboratoryCenter());
            summary.setReportStatus(module.getReportStatus());

            PatientModel reply = writeToDatabase(module, summary);

            if (!laboDelegater.isError()) {
                summary.setPatient(reply);
                summary.setResult("成功");
                result.add(summary);
                //              LogWriter.info(LaboModuleBuilder.class, "LaboModuleを登録しました。患者ID :" + module.getPatientId());
                //    return;
            } else {
                //      LogWriter.warn(LaboModuleBuilder.class, "LaboModule を登録できませんでした。患者ID :" + module.getPatientId());
                //        LogWriter.warn(LaboModuleBuilder.class, laboDelegater.getErrorMessage());
                summary.setResult("エラー");
            }
        }
    }

    /**
     *
     * @param module
     * @param summary
     * @return
     * @throws HeadlessException
     */
    private PatientModel writeToDatabase(LaboModuleValue module, LaboImportSummary summary) throws HeadlessException {
        PatientModel reply = null;
        try {
            reply = laboDelegater.putLaboModule(module);
        } catch (CantFindPatentExcepion ex) {
            JOptionPane.showMessageDialog(null, "患者ID: " + module.getPatientId() + " の患者が存在しません");
            summary.setResult("患者が存在しません");
        }
        return reply;
    }
}
