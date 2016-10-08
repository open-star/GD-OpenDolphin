/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
package open.dolphin.service.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import open.dolphin.dto.AppointSpec;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.dto.DocumentSearchSpec;
import open.dolphin.dto.ImageSearchSpec;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.dto.ObservationSearchSpec;
import open.dolphin.service.IKarteService;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.infomodel.PatientMemoModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.log.LogWriter;
*/
/**
 *
 * @author tomohiro
 */
/*
public class LocalKarteService implements IKarteService {

    private final String localStoragepath;
    private final String separator = File.separator;

    public LocalKarteService(File root) {
        localStoragepath = root.getPath() + separator + "templates";
        File templateDir = new File(localStoragepath);
        if (!templateDir.exists()) {
            templateDir.mkdir();
        }
    }

    private String baseName(String fileName) {
        if (fileName == null) {
            return null;
        }

        int place = fileName.lastIndexOf('.');
        if (place != -1) {
            return fileName.substring(0, place);
        }

        return fileName;
    }

    private String addPreffix(String fileName, String preffix) {
        if (fileName == null) {
            return null;
        }
        if (preffix == null) {
            return fileName;
        }

        return fileName + '.' + preffix;
    }

    @Override
    public KarteBean getKarte(long patientPk, Date fromDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List getDocumentList(DocumentSearchSpec spec) {

        File templateDir = new File(localStoragepath);
        List<String> list = new ArrayList<String>();

        String fileName = null;
        for (String file : templateDir.list()) {
            fileName = localStoragepath + separator + baseName(file);
            File listed = new File(fileName);
            if (!listed.isHidden()) {
                if (listed.isFile()) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    @Override
    public List<DocumentModel> getDocuments(List<Long> ids) {

        List<String> fileList = getDocumentList(null);

        List<DocumentModel> documents = new ArrayList<DocumentModel>();
        for (Long id : ids) {
            ObjectInputStream ooi = null;
            try {
                try {
                    ooi = new ObjectInputStream(new FileInputStream(localStoragepath + separator + fileList.get(id.intValue())));
                    DocumentModel document = (DocumentModel) ooi.readObject();
                    documents.add(document);
                } finally {
                    try {
                        ooi.close();
                    } catch (IOException e) {
                        LogWriter.error(getClass(), e);
                    }
                }
            } catch (Exception ex) {
                return null;
            }
        }

        return documents;
    }

    @Override
    public long putDocument(DocumentModel document) {

        ObjectOutputStream stream = null;
        try {
            try {
                String fileName = localStoragepath + separator + document.getDocInfo().getTitle();
                stream = new ObjectOutputStream(new FileOutputStream(fileName));
                stream.writeObject(document);
            } finally {
                stream.close();
            }
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    @Override
    public int deleteDocument(long pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int updateTitle(long pk, String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<List> getModules(ModuleSearchSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ModuleModel> getAllModule(ModuleSearchSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<List> getImages(ImageSearchSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SchemaModel getImage(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<RegisteredDiagnosisModel> getDiagnosis(DiagnosisSearchSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Long> addDiagnosis(List<RegisteredDiagnosisModel> addList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int updateDiagnosis(List<RegisteredDiagnosisModel> updateList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int removeDiagnosis(List<Long> removeList) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ObservationModel> getObservations(ObservationSearchSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Long> addObservations(List<ObservationModel> observations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int updateObservations(List<ObservationModel> observations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int removeObservations(List<Long> observations) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int updatePatientMemo(PatientMemoModel memo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int putAppointments(AppointSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<List> getAppointmentList(ModuleSearchSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long saveOrUpdateLetter(LetterModel model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LetterModel> getLetterList(long karteId, String docType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/
    /**
     *
     * @param letterPk
     * @return
     */
/*
    @Override
    public LetterModel getLetter(long letterPk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LetterModel getLetterReply(long letterPk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LetterModel> getRecentLetterModels(String docType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<LetterModel> getRecentLetterModels(long karteId, String docType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
*/