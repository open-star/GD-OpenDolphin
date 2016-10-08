package open.dolphin.client.labotestimporter;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import open.dolphin.delegater.remote.RemoteLaboDelegater;
import open.dolphin.infomodel.LaboImportSummary;
import open.dolphin.log.LogWriter;
import open.dolphin.table.ObjectReflectTableModel;

/**
 * LaboTestFileTransferHandler
 *
 * @author kazushi Minagawa
 *
 */
class LaboTestFileTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 2942768324728994019L;
    private DataFlavor fileFlavor;
    private LaboTestImporter context;
    private LinkedList<List<File>> queue;
    private ImportThread importThread;

    public LaboTestFileTransferHandler(LaboTestImporter context) {
        fileFlavor = DataFlavor.javaFileListFlavor;
        this.context = context;
        queue = new LinkedList<List<File>>();
        importThread = new ImportThread();
        importThread.start();
    }

    /**
     *
     * @param component
     * @param transfer
     * @return
     */
    @Override
    public boolean importData(JComponent component, Transferable transfer) {
        if (canImport(component, transfer.getTransferDataFlavors())) {
            try {
                importFiles((List<File>) transfer.getTransferData(fileFlavor));
                return true;
            } catch (UnsupportedFlavorException ufe) {
                LogWriter.error(getClass(), ufe);
            } catch (IOException ieo) {
                LogWriter.error(getClass(), ieo);
            }
        }
        return false;
    }

    /**
     *
     * @param files
     */
    private void importFiles(List<File> files) {
        List<File> xmlFiles = new ArrayList<File>(files.size());
        for (File file : files) {
            if (isLaboTestFile(file)) {
                xmlFiles.add(file);
            }
        }
        addFiles(xmlFiles);
    }

    /**
     *
     * @param file
     * @return
     */
    private boolean isLaboTestFile(File file) {
        if (file.isDirectory()) {
            return false;
        }
        return file.getName().toLowerCase().endsWith(".xml") || file.getName().toLowerCase().endsWith(".txt");
    }

    /**
     *
     * @param c
     * @param flavors
     * @return
     */
    @Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return (hasFileFlavor(flavors));
    }

    /**
     *
     * @param flavors
     * @return
     */
    private boolean hasFileFlavor(DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (fileFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Queueへドロップされたファイルを加える。
     * @param xmlFiles ドロップされたファイルのリスト
     */
    public synchronized void addFiles(List<File> xmlFiles) {
        if (xmlFiles != null && xmlFiles.size() > 0) {
            queue.addLast(xmlFiles);
            notify();
        }
    }

    /**
     * Queueからファイルリストを取り出す。
     * @return ドロップされたファイルのリスト
     */
    public synchronized List<File> getFiles() {
        while (queue.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        return (List<File>) queue.removeFirst();
    }

    /**
     * ファイルをパースしデータベースへ登録するコンシューマスレッドクラス。
     */
    class ImportThread extends Thread {

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    List<File> files = getFiles();
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            context.getProgressBar().setIndeterminate(true);
                        }
                    });
                    LaboModuleBuilder builder = new LaboModuleBuilder();
                    builder.setLaboDelegater(new RemoteLaboDelegater());
                    final List<LaboImportSummary> result = builder.build(files);
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            context.getProgressBar().setIndeterminate(false);
                            context.getProgressBar().setValue(0);
                            ((ObjectReflectTableModel<Object>) context.getLaboListTable().getModel()).addRows(result);
                            context.updateCount();
                        }
                    });

                } catch (Exception e) {
                    LogWriter.error(getClass(), e);
                }
            }
        }
    }
}
