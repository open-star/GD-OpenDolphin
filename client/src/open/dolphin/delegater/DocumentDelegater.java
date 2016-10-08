/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.awt.Dimension;
import java.awt.Image;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import open.dolphin.client.ImageEntry;
import open.dolphin.project.GlobalSettings;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.dto.DocumentSearchSpec;
import open.dolphin.dto.ImageSearchSpec;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.dto.ObservationSearchSpec;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.infomodel.PatientMemoModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.infomodel.TouTouLetter;
import open.dolphin.infomodel.TouTouReply;
import open.dolphin.service.IKarteService;
import open.dolphin.utils.DebugDump;

/**
 *
 * @author tomohiro
 */
public abstract class DocumentDelegater extends DelegaterErrorHandler {

    /**
     *
     * @return
     * @throws NamingException
     */
    protected abstract IKarteService getService() throws NamingException;

    /**
     * ドキュメントを論理削除する。
     * @param pk 論理削除するドキュメントの prmary key
     * @return 削除件数
     */
    public int deleteDocument(long pk) {
        try {
            return getService().deleteDocument(pk);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param templateName
     * @return
     */
    public DocumentModel getDocument(final String templateName) {
        final List<String> templateNames = getDocumentList(null);
        List<Long> ids = new ArrayList<Long>() {

            {
                super.add(new Long(templateNames.indexOf(templateName)));
            }
        };
        DocumentModel document = null;
        try {
            List<DocumentModel> documents = getDocuments(ids);
            if (!documents.isEmpty()) {
                document = documents.get(0);
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return document;
    }

    /**
     *
     * @param spec
     * @return
     */
    public List getDocumentList(DocumentSearchSpec spec) {
        try {
            return getService().getDocumentList(spec);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     * 複数のIDから、それに相当するドキュメントモデル（カルテ）を返す。
     *
     * @param ids IDのリスト
     * @return ドキュメントモデルのリスト。例外発生時はNULL
     */
    public List<DocumentModel> getDocuments(List<Long> ids) {
        try {
            List<DocumentModel> result = getService().getDocuments(ids);
            for (DocumentModel doc : result) {
                // Module byte をオブジェクトへ戻す
                Set<ModuleModel> mc = doc.getModules();
                for (ModuleModel module : mc) {
                    module.setModel(module.toInfoModel());
                }
                // JPEG byte をアイコンへ戻す
                Set<SchemaModel> sc = doc.getSchemas();
                if (sc != null) {
                    for (SchemaModel schema : sc) {
                        ImageIcon icon = new ImageIcon(schema.getJpegBytes());
                        schema.setIcon(icon);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     * 確定日、適合開始日、記録日、ステータスを DocInfo から DocumentModel(KarteEntry) に移す<br>
     * カルテコンテンツを lastkarte.log に書き出す
     * @param docModel カルテコンテンツ
     * @return 保存したカルテコンテンツの数
     */
    public long putDocument(DocumentModel docModel) {
        try {
            //   try {
            // 確定日、適合開始日、記録日、ステータスを
            // DocInfo から DocumentModel(KarteEntry) に移す
            docModel.toPersist();
            if (GlobalSettings.isKarteDataDump()) {
                debugDump(docModel); //カルテコンテンツを lastkarte.log に書き出す
            }
            try {
                return getService().putDocument(docModel);
            } catch (PersistenceException e) {
                showErrorMessage("他のマシンで同じカルテを編集している可能性があります。" + System.getProperty("line.separator") + e.getMessage());
                dispatchError(getClass(), e, "");
            }
            //   } catch (javax.transaction.RollbackException e) {
            //   }
        } catch (Exception e) {
            showErrorMessage("サーバ保存時のエラー。" + System.getProperty("line.separator") + e.getMessage());
            dispatchError(getClass(), e, "");
        }
        return -1;
    }

    /**
     * 患者のカルテを取得する。
     * @param patientPk 患者PK
     * @param fromDate 履歴の検索開始日
     * @return カルテ
     */
    public KarteBean getKarte(long patientPk, Date fromDate) {
        try {
            return getService().getKarte(patientPk, fromDate);
        } catch (Exception e) {
            showErrorMessage(e.toString());
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     * 文書履歴を検索して返す。
     * @param spec DocumentSearchSpec 検索仕様
     * @return DocInfoModel の Collection
     */
    public List getDocumentHistory(DocumentSearchSpec spec) {
        if (spec.getDocType().equals(IInfoModel.DOCTYPE_KARTE)) {
            return getDocumentList(spec);
        } else if (spec.getDocType().equals(IInfoModel.DOCTYPE_LETTER)) {
            return getLetterList(spec);
        } else if (spec.getDocType().equals(IInfoModel.DOCTYPE_LETTER_REPLY)) {
            return getLetterReplyList(spec);
        }
        return null;
    }

    /**
     *
     * @param models
     * @return
     * @throws SQLException
     */
    private List<LetterModel> getLetterModelsFromModels(List<LetterModel> models) throws SQLException {
        List<LetterModel> result = new ArrayList<LetterModel>();
        LetterModel temp = null;
        for (LetterModel model : models) {
            temp = model.toInfoModel();
            temp.setId(model.getId());
            temp.setBeanBytes(null);
            result.add(temp);
        }
        return result;
    }

    /**
     *
     * @return
     */
    public List<LetterModel> getRecentLetterModels() {
        try {
            List<LetterModel> models = getService().getRecentLetterModels("TOUTOU");
            return getLetterModelsFromModels(models);
        } catch (Exception ex) {
            dispatchError(getClass(), ex, "");
        }
        return null;
    }

    /**
     *
     * @param letterPkL
     * @return
     */
    public List<LetterModel> getRecentLetterModels(final long letterPkL) {
        try {
            List<LetterModel> models = getService().getRecentLetterModels(letterPkL, "TOUTOU");
            return getLetterModelsFromModels(models);
        } catch (Exception ex) {
            dispatchError(getClass(), ex, "");
        }
        return null;
    }

    /**
     *
     * @param spec
     * @return
     */
    private List<DocInfoModel> getLetterList(DocumentSearchSpec spec) {
        List result = new ArrayList<DocInfoModel>();
        try {
            List<LetterModel> models = (List<LetterModel>) getService().getLetterList(spec.getKarteId(), "TOUTOU");
            for (LetterModel model : models) {
                TouTouLetter letter = (TouTouLetter) model;
                DocInfoModel docInfo = new DocInfoModel();
                docInfo.setDocPk(letter.getId());
                docInfo.setDocType(IInfoModel.DOCTYPE_LETTER);
                docInfo.setDocId(String.valueOf(letter.getId()));
                docInfo.setConfirmDate(letter.getConfirmed());
                docInfo.setFirstConfirmDate(letter.getConfirmed());
                docInfo.setTitle(letter.getConsultantHospital());
                result.add(docInfo);
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     *
     * @return
     */
    public List<LetterModel> getRecentLetterReplyModels() {
        try {
            List<LetterModel> models = getService().getRecentLetterModels("TOUTOU_REPLY");
            return getLetterModelsFromModels(models);
        } catch (Exception ex) {
            dispatchError(getClass(), ex, "");
        }
        return null;
    }

    /**
     *
     * @param letterPkL
     * @return
     */
    public List<LetterModel> getRecentLetterReplyModels(final long letterPkL) {
        try {
            List<LetterModel> models = getService().getRecentLetterModels(letterPkL, "TOUTOU_REPLY");
            return getLetterModelsFromModels(models);
        } catch (Exception ex) {
            dispatchError(getClass(), ex, "");
        }
        return null;
    }

    /**
     *
     * @param spec
     * @return
     */
    private List<DocInfoModel> getLetterReplyList(DocumentSearchSpec spec) {
        List result = new ArrayList<DocInfoModel>();
        try {
            List<LetterModel> models = (List<LetterModel>) getService().getLetterList(spec.getKarteId(), "TOUTOU_REPLY");
            for (LetterModel model : models) {
                TouTouReply letter = (TouTouReply) model;
                DocInfoModel docInfo = new DocInfoModel();
                docInfo.setDocPk(letter.getId());
                docInfo.setDocType(IInfoModel.DOCTYPE_LETTER_REPLY);
                docInfo.setDocId(String.valueOf(letter.getId()));
                docInfo.setConfirmDate(letter.getConfirmed());
                docInfo.setFirstConfirmDate(letter.getConfirmed());
                docInfo.setTitle(letter.getClientHospital());
                result.add(docInfo);
            }
        } catch (Exception e) {
            showErrorMessage(e.toString());
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     *
     * @param letterPk
     * @return
     */
    public LetterModel getLetter(long letterPk) {
        LetterModel result = null;

        try {
            LetterModel model = getService().getLetter(letterPk);
            result = model.toInfoModel();
            result.setId(model.getId());
            result.setBeanBytes(null);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     *
     * @param letterPk
     * @return
     */
    public LetterModel getLetterReply(long letterPk) {
        LetterModel result = null;
        try {
            LetterModel model = getService().getLetterReply(letterPk);
            result = model.toInfoModel();
            result.setId(model.getId());
            result.setBeanBytes(null);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     * 文書履歴のタイトルを変更する。
     * @param docInfo
     * @return 変更した件数
     */
    public int updateTitle(DocInfoModel docInfo) {
        try {
            return getService().updateTitle(docInfo.getDocPk(), docInfo.getTitle());
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     * Moduleを検索して返す。
     * @param spec ModuleSearchSpec 検索仕様
     * @return Module の Collection
     */
    public List getModuleList(ModuleSearchSpec spec) {
        List<List> result = null;

        try {
            result = getService().getModules(spec);
            for (List list : result) {
                for (Iterator iter = list.iterator(); iter.hasNext();) {
                    ModuleModel module = (ModuleModel) iter.next();
                    module.setModel(module.toInfoModel());
                }
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     * 全期間の Module を返します
     * @param spec ModuleSearchSpec 検索仕様
     * @return Module の Collection
     */
    public List getAllModuleList(ModuleSearchSpec spec) {
        List<ModuleModel> result = null;
        try {
            result = getService().getAllModule(spec);
            for (ModuleModel module : result) {
                module.setModel(module.toInfoModel());
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     * イメージを取得する。
     * @param id 画像のId
     * @return SchemaModel
     */
    public SchemaModel getImage(long id) {
        SchemaModel result = null;
        try {
            result = getService().getImage(id);
            if (result != null) {
                byte[] bytes = result.getJpegBytes();
                ImageIcon icon = new ImageIcon(bytes);
                if (icon != null) {
                    result.setIcon(icon);
                }
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     * Imageを検索して返す。
     * @param spec ImageSearchSpec 検索仕様
     * @return Imageリストのリスト
     */
    public List getImageList(ImageSearchSpec spec) {
        List<List> result = new ArrayList<List>(3);
        try {
            List images = getService().getImages(spec);                              // 検索結果
            for (Iterator iter = images.iterator(); iter.hasNext();) {               // 抽出期間毎のリスト
                List periodList = (List) iter.next();                                // 抽出期間毎のリスト
                List<ImageEntry> el = new ArrayList<ImageEntry>();                   // ImageEntry 用のリスト
                for (Iterator iter2 = periodList.iterator(); iter2.hasNext();) {     // 抽出期間をイテレートする
                    // シェーマモデルをエントリに変換しリストに加える
                    SchemaModel model = (SchemaModel) iter2.next();
                    ImageEntry entry = getImageEntry(model, spec.getIconSize());
                    el.add(entry);
                } // リターンリストへ追加する
                result.add(el);
            }
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return result;
    }

    /**
     * シェーマモデルをエントリに変換する。
     * @param schema シェーマモデル
     * @param iconSize アイコンのサイズ
     * @return ImageEntry
     */
    private ImageEntry getImageEntry(SchemaModel schema, Dimension iconSize) {
        ImageEntry result = new ImageEntry();
        result.setId(schema.getId());
        //model.setConfirmDate(ModelUtils.getDateTimeAsString(schema.getConfirmDate()));  // First?
        result.setConfirmDate(ModelUtils.getDateTimeAsString(schema.getConfirmed()));  // First?
        result.setContentType(schema.getExtRef().getContentType());
        result.setTitle(schema.getExtRef().getTitle());
        result.setMedicalRole(schema.getExtRef().getMedicalRole());

        byte[] bytes = schema.getJpegBytes();
        // Create ImageIcon
        ImageIcon icon = new ImageIcon(bytes);

        if (icon != null) {
            result.setImageIcon(adjustImageSize(icon, iconSize));
        }
        return result;
    }

    /**
     *
     * @param beans
     * @return
     */
    public List<Long> putDiagnosis(List<RegisteredDiagnosisModel> beans) {
        try {
            return getService().addDiagnosis(beans);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param beans
     * @return
     */
    public int updateDiagnosis(List<RegisteredDiagnosisModel> beans) {
        try {
            return getService().updateDiagnosis(beans);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param ids
     * @return
     */
    public int removeDiagnosis(List<Long> ids) {
        try {
            return getService().removeDiagnosis(ids);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     * Diagnosisを検索して返す。
     * @param spec DiagnosisSearchSpec 検索仕様
     * @return DiagnosisModel の Collection
     */
    public List<RegisteredDiagnosisModel> getDiagnosisList(DiagnosisSearchSpec spec) {
        try {
            return getService().getDiagnosis(spec);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param observations
     * @return
     */
    public List<Long> addObservations(List<ObservationModel> observations) {
        try {
            return getService().addObservations(observations);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param spec
     * @return
     */
    public List<ObservationModel> getObservations(ObservationSearchSpec spec) {
        try {
            return getService().getObservations(spec);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param observations
     * @return
     */
    public int updateObservations(List<ObservationModel> observations) {
        try {
            return getService().updateObservations(observations);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param ids
     * @return
     */
    public int removeObservations(List<Long> ids) {
        try {
            return getService().removeObservations(ids);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param pm
     * @return
     */
    public int updatePatientMemo(PatientMemoModel pm) {
        try {
            return getService().updatePatientMemo(pm);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return 0;
    }

    /**
     *
     * @param spec
     * @return
     */
    public List getAppoinmentList(ModuleSearchSpec spec) {
        try {
            return getService().getAppointmentList(spec);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param icon
     * @param dim
     * @return
     */
    private ImageIcon adjustImageSize(ImageIcon icon, Dimension dim) {
        if ((icon.getIconHeight() > dim.height) || (icon.getIconWidth() > dim.width)) {
            Image img = icon.getImage();
            float hRatio = (float) icon.getIconHeight() / dim.height;
            float wRatio = (float) icon.getIconWidth() / dim.width;

            int h, w;
            if (hRatio > wRatio) {
                h = dim.height;
                w = (int) (icon.getIconWidth() / hRatio);
            } else {
                w = dim.width;
                h = (int) (icon.getIconHeight() / wRatio);
            }
            img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);

            return new ImageIcon(img);

        } else {
            return icon;
        }
    }

    /**
     *
     * @param message
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "エラー", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * カルテコンテンツをlastkarte.log に書き出す
     * @param docModel カルテコンテンツ
     */
    private void debugDump(DocumentModel docModel) {
        StringWriter writer = new StringWriter();
        try {
            docModel.serialize(writer);
            DebugDump.dumpToFile("lastkarte.log", writer.toString());
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                dispatchError(getClass(), e, "");
            }
        }
    }
}
