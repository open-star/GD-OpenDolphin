package open.dolphin.service;

import java.util.Date;
import java.util.List;

import open.dolphin.dto.AppointSpec;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.dto.DocumentSearchSpec;
import open.dolphin.dto.ImageSearchSpec;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.dto.ObservationSearchSpec;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.infomodel.PatientMemoModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.infomodel.SchemaModel;

/**
 *
 * @author oda
 */
public interface IKarteService {

    /**
     * カルテの基礎的な情報をまとめて返す。
     * これはクライアントがカルテをオープンする時、なるべく通信トラフィックを少なくするための手段である。
     * @param patientPk 患者の Database Primary Key
     * @param fromDate 各種エントリの検索開始日
     * @return 基礎的な情報をフェッチした KarteBean
     */
    public KarteBean getKarte(long patientPk, Date fromDate);
    
    /**
     * 文書履歴エントリを取得する。
     * @param spec
     * @return DocInfo のコレクション
     */
    public List getDocumentList(DocumentSearchSpec spec);
    
    /**
     * 文書(DocumentModel Object)を取得する。
     * @param ids DocumentModel の pkコレクション
     * @return DocumentModelのコレクション
     */
    public List<DocumentModel> getDocuments(List<Long> ids);
    
    /**
     * ドキュメント DocumentModel オブジェクトを保存する。
     * @param document 追加するDocumentModel オブジェクト
     * @return 追加した数
     */
    public long putDocument(DocumentModel document);
    
    /**
     * ドキュメントを論理削除する。
     * @param pk 論理削除するドキュメントの primary key
     * @return 削除した件数
     */
    public int deleteDocument(long pk);
    
    /**
     * ドキュメントのタイトルを変更する。
     * @param pk 変更するドキュメントの primary key
     * @param title
     * @return 変更した件数
     */
    public int updateTitle(long pk, String title);
    
    /**
     * ModuleModelエントリを取得する。
     * @param spec モジュール検索仕様
     * @return ModuleModelリストのリスト
     */
    public List<List> getModules(ModuleSearchSpec spec);


    /**
     * 期間を限定しないModuleModelエントリを取得する。
     * @param spec モジュール検索仕様
     * @return ModuleModelリストのリスト
     */
    public List<ModuleModel> getAllModule(open.dolphin.dto.ModuleSearchSpec spec);
    
    /**
     * SchemaModelエントリを取得する。
     * @param spec
     * @return SchemaModelエントリの配列
     */
    public List<List> getImages(ImageSearchSpec spec);
    
    /**
     * 画像を取得する。
     * @param id SchemaModel Id
     * @return SchemaModel
     */
    public SchemaModel getImage(long id);
    
    /**
     * 傷病名リストを取得する。
     * @param spec 検索仕様
     * @return 傷病名のリスト
     */
    public List<RegisteredDiagnosisModel> getDiagnosis(DiagnosisSearchSpec spec);
    
    /**
     * 傷病名を追加する。
     * @param addList 追加する傷病名のリスト
     * @return idのリスト
     */
    public List<Long> addDiagnosis(List<RegisteredDiagnosisModel> addList);
    
    /**
     * 傷病名を更新する。
     * @param updateList
     * @return 更新数 
     */
    public int updateDiagnosis(List<RegisteredDiagnosisModel> updateList);
    
    /**
     * 傷病名を削除する。
     * @param removeList 削除する傷病名のidリスト
     * @return 削除数
     */
    public int removeDiagnosis(List<Long> removeList);
    
    /**
     * Observationを取得する。
     * @param spec 検索仕様
     * @return Observationのリスト
     */
    public List<ObservationModel> getObservations(ObservationSearchSpec spec);
    
    /**
     * Observationを追加する。
     * @param observations 追加するObservationのリスト
     * @return 追加したObservationのIdリスト
     */
    public List<Long> addObservations(List<ObservationModel> observations);
    
    /**
     * Observationを更新する。
     * @param observations 更新するObservationのリスト
     * @return 更新した数
     */
    public int updateObservations(List<ObservationModel> observations);
    
    /**
     * Observationを削除する。
     * @param observations 削除するObservationのリスト
     * @return 削除した数
     */
    public int removeObservations(List<Long> observations);
    
    /**
     * 患者メモを更新する。
     * @param memo 更新するメモ
     * @return
     */
    public int updatePatientMemo(PatientMemoModel memo);
    
    /**
     * 予約を保存、更新、削除する。
     * @param spec 予約情報の DTO
     * @return
     */
    public int putAppointments(AppointSpec spec);
    
    /**
     * 予約を検索する。
     * @param spec 検索仕様
     * @return 予約の Collection
     */
    public List<List> getAppointmentList(ModuleSearchSpec spec);
    
    /**
     *
     * @param model
     * @return
     */
    public long saveOrUpdateLetter(final LetterModel model);
    
    /**
     *
     * @param karteId
     * @param docType
     * @return
     */
    public List<LetterModel> getLetterList(final long karteId, String docType);
    
    /**
     *
     * @param letterPk
     * @return
     */
    public LetterModel getLetter(final long letterPk);
    
    /**
     *
     * @param docType
     * @return
     */
    public List<LetterModel> getRecentLetterModels(final String docType);
    /**
     *
     * @param karteId
     * @param docType
     * @return
     */
    public List<LetterModel> getRecentLetterModels(final long karteId, final String docType);
    
    /**
     *
     * @param letterPk
     * @return
     */
    public LetterModel getLetterReply(final long letterPk);
    
}
