package open.dolphin.order;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import open.dolphin.infomodel.RegisteredDiagnosisModel;

/**
 * 疾患 Transferable クラス。
 * @author kazm
 */
public class RegisteredDiagnosisTransferable implements Transferable {

    /**
     *
     */
    public static DataFlavor registeredDiagnosisFlavor = new DataFlavor(open.dolphin.infomodel.RegisteredDiagnosisModel.class, "RegisteredDiagnosis");
    /**
     *
     */
    public static final DataFlavor[] flavors = {registeredDiagnosisFlavor};
    private RegisteredDiagnosisModel diagnosis;

    /**
     *
     * @param diagnosis
     */
    public RegisteredDiagnosisTransferable(RegisteredDiagnosisModel diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     *
     * @return
     */
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    /**
     *
     * @param flavor
     * @return
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(registeredDiagnosisFlavor) ? true : false;
    }

    /**
     *
     * @param flavor
     * @return
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {

        if (flavor.equals(registeredDiagnosisFlavor)) {
            return diagnosis;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
