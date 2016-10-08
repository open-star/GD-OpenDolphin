package open.dolphin.service.remote;

import open.dolphin.service.IKarteService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import open.dolphin.exception.CanNotDeleteException;

import open.dolphin.dto.AppointSpec;
import open.dolphin.dto.DiagnosisSearchSpec;
import open.dolphin.dto.DocumentSearchSpec;
import open.dolphin.dto.ImageSearchSpec;
import open.dolphin.dto.ModuleSearchSpec;
import open.dolphin.dto.ObservationSearchSpec;
import open.dolphin.infomodel.AllergyModel;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.infomodel.ModelUtils;
import open.dolphin.infomodel.ModuleModel;
import open.dolphin.infomodel.ObservationModel;
import open.dolphin.infomodel.PatientMemoModel;
import open.dolphin.infomodel.PhysicalModel;
import open.dolphin.infomodel.RegisteredDiagnosisModel;
import open.dolphin.infomodel.SchemaModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import open.dolphin.utils.GUIDGenerator;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;

/**
 * カルテ MEMO:マッピング
 *
 * @author
 */
public class RemoteKarteService extends DolphinService implements IKarteService {

    /**
     *
     */
    public RemoteKarteService() {
    }

    /**
     * カルテの基礎的な情報をまとめて返す。
     * @param patientPk 患者の Database Primary Key
     * @param fromDate 各種エントリの検索開始日
     * @return 基礎的な情報をフェッチした KarteBean
     */
    @Override
    public KarteBean getKarte(long patientPk, Date fromDate) {

        roleAllowed("user");
        KarteBean karte = null;
        try {
            startTransaction();
            // 最初に患者のカルテを取得する
            karte = (KarteBean) getSession().createQuery("from KarteBean where patient_id = :patientPk").setParameter("patientPk", patientPk).uniqueResult();

            // カルテの PK を得る
            long karteId = karte.getId();

            // アレルギーデータを取得する
            List<ObservationModel> observations = getSession().
                    createQuery("from ObservationModel where karte_id=:karteId and observation='Allergy'").
                    setParameter("karteId", karteId).
                    list();

            if (observations != null && observations.size() > 0) {
                List<AllergyModel> allergies = new ArrayList<AllergyModel>(observations.size());
                for (Iterator iter = observations.iterator(); iter.hasNext();) {
                    ObservationModel observation = (ObservationModel) iter.next();
                    AllergyModel allergy = new AllergyModel();
                    allergy.setObservationId(observation.getId());
                    allergy.setFactor(observation.getPhenomenon());
                    allergy.setSeverity(observation.getCategoryValue());
                    allergy.setIdentifiedDate(observation.confirmDateAsString());
                    allergies.add(allergy);
                }
                karte.addEntryCollection("allergy", allergies);
            }

            // 身長データを取得する
            observations = getSession().
                    createQuery("from ObservationModel where karte_id=:karteId and observation='PhysicalExam' and phenomenon='bodyHeight'").
                    setParameter("karteId", karteId).
                    list();

            if (observations != null && observations.size() > 0) {
                List<PhysicalModel> physicals = new ArrayList<PhysicalModel>(observations.size());
                for (Iterator iter = observations.iterator(); iter.hasNext();) {
                    ObservationModel observation = (ObservationModel) iter.next();
                    PhysicalModel physical = new PhysicalModel();
                    physical.setHeightId(observation.getId());
                    physical.setHeight(observation.getValue());
                    physical.setIdentifiedDate(observation.confirmDateAsString());
                    physical.setMemo(ModelUtils.getDateAsString(observation.getRecorded()));
                    physicals.add(physical);
                }
                karte.addEntryCollection("height", physicals);
            }

            // 体重データを取得する
            observations = getSession().
                    createQuery("from ObservationModel where karte_id=:karteId and observation='PhysicalExam' and phenomenon='bodyWeight'").
                    setParameter("karteId", karteId).
                    list();

            if (observations != null && observations.size() > 0) {
                List<PhysicalModel> physicals = new ArrayList<PhysicalModel>(observations.size());
                for (Iterator iter = observations.iterator(); iter.hasNext();) {
                    ObservationModel observation = (ObservationModel) iter.next();
                    PhysicalModel physical = new PhysicalModel();
                    physical.setWeightId(observation.getId());
                    physical.setWeight(observation.getValue());
                    physical.setIdentifiedDate(observation.confirmDateAsString());
                    physical.setMemo(ModelUtils.getDateAsString(observation.getRecorded()));
                    physicals.add(physical);
                }
                karte.addEntryCollection("weight", physicals);
            }

            // 直近の来院日エントリーを取得しカルテに設定する
            List<String> latestVisits = getSession().createQuery("select pvtDate from PatientVisitModel where patient_id = :patientPk and pvtDate >= :fromDate").setParameter("patientPk", patientPk).setParameter("fromDate", ModelUtils.getDateAsString(fromDate)).list();

            if (latestVisits != null && latestVisits.size() > 0) {
                karte.addEntryCollection("visit", latestVisits);
            }

            // 患者Memoを取得する
            List memo = getSession().createQuery("from PatientMemoModel where karte_id = :karteId").setParameter("karteId", karteId).list();

            if (memo != null && memo.size() > 0) {
                karte.addEntryCollection("patientMemo", memo);
            }

            endTransaction();
        } catch (Exception e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return karte;
    }

    /**
     * 文書履歴エントリを取得する。
     * @param spec
     * @return DocInfo のコレクション
     */
    @Override
    public List<DocInfoModel> getDocumentList(DocumentSearchSpec spec) {

        roleAllowed("user");

        StringBuilder optionalQuery = new StringBuilder();
        String condition = "";

        if (spec.isIncludeModifid()) {
            optionalQuery.append("status='M'");
        }

        if (spec.isIncludeUnsend()) {
            if (optionalQuery.length() != 0) {
                optionalQuery.append(" or ");
            }
            optionalQuery.append("status='T'");
        }

        if (spec.isIncludeSend()) {
            if (optionalQuery.length() != 0) {
                optionalQuery.append(" or ");
            }
            optionalQuery.append("status='F'");
        }

        if (optionalQuery.length() != 0) {
            condition = optionalQuery.insert(0, " and (").append(")").toString();
        }

        List<DocInfoModel> result = new ArrayList<DocInfoModel>();

        try {
            startTransaction();

            StringBuilder query = new StringBuilder();
            query.append("select docInfo, id, linkId, confirmed, started, status");
            query.append(" from DocumentModel");
            query.append(" where karte_id = :karteId and started >= :fromDate");
            query.append(condition);
            Query documentsQuery = getSession().createQuery(query.toString()).setParameter("karteId", spec.getKarteId()).setParameter("fromDate", spec.getFromDate());

            ScrollableResults documents = documentsQuery.scroll();

            if (documents.first()) {
                do {
                    DocInfoModel docInfo = (DocInfoModel) documents.get(0);
                    docInfo.setDocType(DOCTYPE_KARTE);
                    docInfo.setDocPk(new Long(documents.get(1).toString()));
                    docInfo.setParentPk(new Long(documents.get(2).toString()));
                    docInfo.setConfirmDate((Date) documents.get(3));
                    docInfo.setFirstConfirmDate((Date) documents.get(4));
                    docInfo.setStatus((String) documents.get(5));
                    docInfo.setDocId(GUIDGenerator.generate(docInfo));
                    result.add(docInfo);
                } while (documents.next());
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
            result.clear();
            return result;
        }

        return result;
    }

    /*
     * 文書(DocumentModel Object)を取得する。
     * @param ids DocumentModel の pkコレクション
     * @return DocumentModelのコレクション
     */
    @Override
    public List<DocumentModel> getDocuments(List<Long> ids) {

        roleAllowed("user");

        List<DocumentModel> ret = new ArrayList<DocumentModel>();

        try {
            startTransaction();
            ret.addAll(getSession().createQuery("from DocumentModel where id in (:ids)").setParameterList("ids", ids).list());
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
            }
        } catch (Exception e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return ret;
    }

    /**
     * ドキュメント DocumentModel オブジェクトを保存する。
     * @param document 追加するDocumentModel オブジェクト
     * @return 追加したDocumentModel オブジェクト数
     */
    @Override
    public long putDocument(DocumentModel document) {

        roleAllowed("user");
        getSession().clear();
        long id = document.getId();

        try {
            startTransaction();
            if (document.getConfirmed() != null) {
                getSession().saveOrUpdate(document);
                long parentPk = document.getDocInfo().getParentPk();
                if (parentPk != 0L) {

                    // 適合終了日を新しい版の確定日にする
                    Date ended = document.getConfirmed();

                    // オリジナルを取得し 終了日と status = M を設定する
                    DocumentModel old = (DocumentModel) getSession().createQuery("from DocumentModel where id = :id").setParameter("id", parentPk).uniqueResult();
                    old.setEnded(ended);
                    //    old.setStatus(STATUS_MODIFIED);

                    // 関連するモジュールとイメージに同じ処理を実行する
                    for (ModuleModel model : old.getModules()) {
                        model.setEnded(ended);
                        //       model.setStatus(STATUS_MODIFIED);
                    }

                    for (SchemaModel model : old.getSchemas()) {
                        model.setEnded(ended);
                        //       model.setStatus(STATUS_MODIFIED);
                    }
                }
            } else {
                LogWriter.error(this.getClass(), "putDocument(DocumentModel document) getConfirmed is null");
            }
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
            return -1;
        }

        return id;
    }

    /**
     * ドキュメントを論理削除する。
     * @param pk 論理削除するドキュメントの primary key
     * @return 削除した件数
     */
    @Override
    public int deleteDocument(long pk) {

        roleAllowed("user");

        try {
            startTransaction();

            // 対象 Document を取得する
            Date ended = new Date();
            DocumentModel delete = (DocumentModel) getSession().createQuery("from DocumentModel where id = :id").setParameter("id", pk).uniqueResult();

            if (delete.getLinkId() != 0L) {
                endTransaction();
                throw new CanNotDeleteException("他のドキュメントを参照しているため削除できません。");
            }

            // 参照されている場合は例外を投げる
            int refs = ((Number) getSession().createQuery("select count(*) from DocumentModel where linkid=:pk").setParameter("pk", pk).uniqueResult()).intValue();

            if (refs > 0) {
                endTransaction();
                throw new CanNotDeleteException("他のドキュメントから参照されているため削除できません。");
            }

            delete.setStatus(STATUS_DELETE);
            delete.setEnded(ended);

            // 関連するモジュールに同じ処理を行う
            //        List<ModuleModel> deleteModules = getSession()
            //                                       .createQuery("from ModuleModel m where m.document.id = :pk")
            //                                       .setParameter("pk", pk)
            //                                       .list();
            for (ModuleModel model : delete.getModules()) {
                model.setStatus(STATUS_DELETE);
                model.setEnded(ended);
            }

            // 関連する画像に同じ処理を行う
            //        Collection deleteImages = getSession()
            //                                      .createQuery("from SchemaModel s where s.document.id = :pk")
            //                                      .setParameter("pk", pk)
            //                                      .list();
            for (SchemaModel model : delete.getSchemas()) {
                model.setStatus(STATUS_DELETE);
                model.setEnded(ended);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return 1;
    }

    /**
     * ドキュメントのタイトルを変更する。
     * @param pk 変更するドキュメントの primary key
     * @param title
     * @return 変更した件数
     */
    @Override
    public int updateTitle(long pk, String title) {

        roleAllowed("user");

        startTransaction();

        DocumentModel update = (DocumentModel) getSession().createQuery("from DocumentModel where id = :id").setParameter("id", pk).uniqueResult();
        update.getDocInfo().setTitle(title);

        endTransaction();

        return 1;
    }

    /**
     * ModuleModelエントリを取得する。
     * @param spec モジュール検索仕様
     * @return ModuleModelリストのリスト
     */
    @Override
    public List<List> getModules(ModuleSearchSpec spec) {

        roleAllowed("user");

        Date[] fromDate = spec.getFromDate();
        Date[] toDate = spec.getToDate();
        int len = fromDate.length;
        List<List> ret = new ArrayList<List>(len);

        try {
            startTransaction();

            for (int i = 0; i < len; i++) {
                List<ModuleModel> modules = getSession().createQuery("from ModuleModel where karte_id = :karteId and entity = :entity and started between :fromDate and :toDate and status='F'").setParameter("karteId", spec.getKarteId()).setParameter("entity", spec.getEntity()).setParameter("fromDate", fromDate[i]).setParameter("toDate", toDate[i]).list();

                ret.add(modules);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return ret;
    }

    /**
     * ModuleModelエントリを取得する。
     * @param spec モジュール検索仕様
     * @return ModuleModelリストのリスト
     */
    @Override
    public List<ModuleModel> getAllModule(ModuleSearchSpec spec) {

        roleAllowed("user");

        List<ModuleModel> modules = null;

        try {
            startTransaction();

            modules = getSession().createQuery("from ModuleModel m where m.karte.id = :karteId and m.moduleInfo.entity = :entity and m.status='F'").setParameter("karteId", spec.getKarteId()).setParameter("entity", spec.getEntity()).list();

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return modules;
    }

    /**
     * SchemaModelエントリを取得する。
     * @param spec
     * @return SchemaModelエントリの配列
     */
    @Override
    public List<List> getImages(ImageSearchSpec spec) {

        roleAllowed("user");

        Date[] fromDate = spec.getFromDate();
        Date[] toDate = spec.getToDate();

        int len = fromDate.length;

        List<List> ret = new ArrayList<List>(len);

        try {
            startTransaction();

            for (int i = 0; i < len; i++) {
                List modules = getSession().createQuery("from SchemaModel i where i.karte.id = :karteId and i.started between :fromDate and :toDate and i.status='F'").setParameter("karteId", spec.getKarteId()).setParameter("fromDate", fromDate[i]).setParameter("toDate", toDate[i]).list();
                ret.add(modules);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return ret;
    }

    /**
     * 画像を取得する。
     * @param id SchemaModel Id
     * @return SchemaModel
     */
    @Override
    public SchemaModel getImage(long id) {

        roleAllowed("user");

        SchemaModel image = null;

        try {
            startTransaction();
            image = (SchemaModel) getSession().createQuery("from SchemaModel where id = :id").setParameter("id", id).uniqueResult();
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return image;
    }

    /**
     * 傷病名リストを取得する。
     * @param spec 検索仕様
     * @return 傷病名のリスト
     */
    @Override
    public List<RegisteredDiagnosisModel> getDiagnosis(DiagnosisSearchSpec spec) {

        roleAllowed("user");

        List<RegisteredDiagnosisModel> result = null;

        try {
            startTransaction();

            if (spec.getFromDate() != null) {
                // 疾患開始日を指定している場合
                result = getSession().createQuery("from RegisteredDiagnosisModel r where r.karte.id = :karteId and r.started >= :fromDate").setParameter("karteId", spec.getKarteId()).setParameter("fromDate", spec.getFromDate()).list();
            } else {
                // 全期間を取得
                result = getSession().createQuery("from RegisteredDiagnosisModel r where r.karte.id = :karteId").setParameter("karteId", spec.getKarteId()).list();
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }

        }

        return result;
    }

    /**
     * 傷病名を追加する。
     * @param addList 追加する傷病名のリスト
     * @return idのリスト
     */
    @Override
    public List<Long> addDiagnosis(List<RegisteredDiagnosisModel> addList) {

        roleAllowed("user");

        List<Long> ret = new ArrayList<Long>(addList.size());

        try {
            startTransaction();

            for (RegisteredDiagnosisModel bean : addList) {
                getSession().persist(bean);
                ret.add(new Long(bean.getId()));
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return ret;
    }

    /**
     * 傷病名を更新する。
     * @param updateList
     * @return 更新数
     */
    @Override
    public int updateDiagnosis(List<RegisteredDiagnosisModel> updateList) {

        roleAllowed("user");

        int cnt = 0;

        try {
            startTransaction();

            for (RegisteredDiagnosisModel bean : updateList) {
                getSession().merge(bean);
                cnt++;
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return cnt;
    }

    /**
     * 傷病名を削除する。
     * @param removeList 削除する傷病名のidリスト
     * @return 削除数
     */
    @Override
    public int removeDiagnosis(List<Long> removeList) {

        roleAllowed("user");

        int cnt = 0;

        try {
            startTransaction();

            for (Long id : removeList) {
                RegisteredDiagnosisModel bean = (RegisteredDiagnosisModel) getSession().createQuery("from RegisteredDiagnosisModel where id = :id").setParameter("id", id).uniqueResult();
                getSession().delete(bean);
                cnt++;
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return cnt;
    }

    /**
     * Observationを取得する。
     * @param spec 検索仕様
     * @return Observationのリスト
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<ObservationModel> getObservations(ObservationSearchSpec spec) {

        roleAllowed("user");

        List<ObservationModel> ret = null;

        try {
            startTransaction();

            String observation = spec.getObservation();
            String phenomenon = spec.getPhenomenon();
            Date firstConfirmed = spec.getFirstConfirmed();
            if (observation != null) {
                if (firstConfirmed != null) {
                    ret = getSession().createQuery("from ObservationModel o where o.karte.id=:karteId and o.observation=:observation and o.started >= :firstConfirmed").setParameter("karteId", spec.getKarteId()).setParameter("observation", observation).setParameter("firstConfirmed", firstConfirmed).list();

                } else {
                    ret = getSession().createQuery("from ObservationModel o where o.karte.id=:karteId and o.observation=:observation").setParameter("karteId", spec.getKarteId()).setParameter("observation", observation).list();
                }
            } else if (phenomenon != null) {
                if (firstConfirmed != null) {
                    ret = getSession().createQuery("from ObservationModel o where o.karte.id=:karteId and o.phenomenon=:phenomenon and o.started >= :firstConfirmed").setParameter("karteId", spec.getKarteId()).setParameter("phenomenon", phenomenon).setParameter("firstConfirmed", firstConfirmed).list();
                } else {
                    ret = getSession().createQuery("from ObservationModel o where o.karte.id=:karteId and o.phenomenon=:phenomenon").setParameter("karteId", spec.getKarteId()).setParameter("phenomenon", phenomenon).list();
                }
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return ret;
    }

    /**
     * Observationを追加する。
     * @param observations 追加するObservationのリスト
     * @return 追加したObservationのIdリスト
     */
    @Override
    public List<Long> addObservations(List<ObservationModel> observations) {

        roleAllowed("user");

        if (observations != null && observations.size() > 0) {

            List<Long> ret = new ArrayList<Long>(observations.size());

            try {
                startTransaction();

                for (ObservationModel model : observations) {
                    getSession().persist(model);
                    ret.add(new Long(model.getId()));
                }

                endTransaction();
            } catch (HibernateException e) {
                if (getSession().getTransaction().isActive()) {
                    getSession().getTransaction().rollback();
                    LogWriter.error(this.getClass(), "Rollback", e);
                }
            }

            return ret;
        }

        return null;
    }

    /**
     * Observationを更新する。
     * @param observations 更新するObservationのリスト
     * @return 更新した数
     */
    @Override
    public int updateObservations(List<ObservationModel> observations) {

        roleAllowed("user");

        if (observations != null && observations.size() > 0) {

            int cnt = 0;

            try {
                startTransaction();

                for (ObservationModel model : observations) {
                    getSession().merge(model);
                    cnt++;
                }

                endTransaction();
            } catch (HibernateException e) {
                if (getSession().getTransaction().isActive()) {
                    getSession().getTransaction().rollback();
                    LogWriter.error(this.getClass(), "Rollback", e);
                }
            }

            return cnt;
        }
        return 0;
    }

    /**
     * Observationを削除する。
     * @param observations 削除するObservationのリスト
     * @return 削除した数
     */
    @Override
    public int removeObservations(List<Long> observations) {

        roleAllowed("user");

        if (observations != null && observations.size() > 0) {

            int cnt = 0;

            try {
                startTransaction();

                for (Long id : observations) {
                    ObservationModel model = (ObservationModel) getSession().createQuery("from ObservationModel where id = :id").setParameter("id", id).uniqueResult();
                    getSession().delete(model);
                    cnt++;
                }

                endTransaction();
            } catch (HibernateException e) {
                if (getSession().getTransaction().isActive()) {
                    getSession().getTransaction().rollback();
                    LogWriter.error(this.getClass(), "Rollback", e);
                }
            }

            return cnt;
        }
        return 0;
    }

    /**
     * 患者メモを更新する。
     * @param memo 更新するメモ
     */
    @Override
    public int updatePatientMemo(PatientMemoModel memo) {

        roleAllowed("user");

        int cnt = 0;

        try {
            startTransaction();

            if (memo.getId() == 0L) {
                getSession().persist(memo);
            } else {
                getSession().merge(memo);
            }
            cnt++;

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return cnt;
    }

    /**
     * 予約を保存、更新、削除する。
     * @param spec 予約情報の DTO
     * @return
     */
    @Override
    public int putAppointments(AppointSpec spec) {

        roleAllowed("user");

        int cnt = 0;

        try {
            startTransaction();

            Collection added = spec.getAdded();
            Collection updated = spec.getUpdared();
            Collection removed = spec.getRemoved();
            AppointmentModel bean = null;

            // 登録する
            if (added != null && added.size() > 0) {
                Iterator iter = added.iterator();
                while (iter.hasNext()) {
                    bean = (AppointmentModel) iter.next();
                    getSession().persist(bean);
                    cnt++;
                }
            }

            // 更新する
            if (updated != null && updated.size() > 0) {
                Iterator iter = updated.iterator();
                while (iter.hasNext()) {
                    bean = (AppointmentModel) iter.next();
                    // av は分離オブジェクトである
                    getSession().merge(bean);
                    cnt++;
                }
            }

            // 削除
            if (removed != null && removed.size() > 0) {
                Iterator iter = removed.iterator();
                while (iter.hasNext()) {
                    bean = (AppointmentModel) iter.next();
                    // 分離オブジェクトは remove に渡せないので対象を検索する
                    AppointmentModel target = (AppointmentModel) getSession().createQuery("from AppointmentModel where id = :id").setParameter("id", bean.getId()).uniqueResult();
                    getSession().delete(target);
                    cnt++;
                }
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return cnt;
    }

    /**
     * 予約を検索する。
     * @param spec 検索仕様
     * @return 予約の Collection
     */
    @Override
    public List<List> getAppointmentList(ModuleSearchSpec spec) {

        roleAllowed("user");

        Date[] fromDate = spec.getFromDate();
        Date[] toDate = spec.getToDate();

        int len = fromDate.length;

        List<List> ret = new ArrayList<List>(len);

        try {

            startTransaction();

            for (int i = 0; i < len; i++) {
                List c = getSession().createQuery("from AppointmentModel a where a.karte.id = :karteId and a.date between :fromDate and :toDate").setParameter("karteId", spec.getKarteId()).setParameter("fromDate", fromDate[i]).setParameter("toDate", toDate[i]).list();
                ret.add(c);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return ret;
    }

    /**
     * 紹介状を保存または更新する。
     * @param model
     * @return
     */
    @Override
    public long saveOrUpdateLetter(final LetterModel model) {

        roleAllowed("user");

        try {
            startTransaction();
            getSession().saveOrUpdate(model);
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return model.getId();
    }

    /**
     * 紹介状のリストを取得する。
     */
    @Override
    public List<LetterModel> getLetterList(final long karteId, final String docType) {

        roleAllowed("user");
        List<LetterModel> letters = null;

        try {
            startTransaction();

            if (docType.equals("TOUTOU")) {
                letters = getSession().createQuery("from TouTouLetter f where f.karte.id = :karteId").setParameter("karteId", karteId).list();
            } else if (docType.equals("TOUTOU_REPLY")) {
                letters = getSession().createQuery("from TouTouReply f where f.karte.id = :karteId").setParameter("karteId", karteId).list();
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return letters;
    }

    /**
     * 最近の紹介状を取得する。
     * @param docType
     * @return
     */
    @Override
    public List<LetterModel> getRecentLetterModels(final String docType) {

        roleAllowed("user");

        List<LetterModel> letters = null;

        try {
            startTransaction();

            final int limit = 30;
            if (docType.equals("TOUTOU")) {
                List<LetterModel> result = null;// MEMO;Unused?
                letters = (List<LetterModel>) getSession().createQuery("from TouTouLetter order by recorded desc").setMaxResults(limit).list();
            } else if (docType.equals("TOUTOU_REPLY")) {
                letters = (List<LetterModel>) getSession().createQuery("from TouTouReply order by recorded desc").setMaxResults(limit).list();
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return letters;
    }

    /**
     *
     * @param karteId
     * @param docType
     * @return
     */
    @Override
    public List<LetterModel> getRecentLetterModels(long karteId, String docType) {

        roleAllowed("user");

        List<LetterModel> letters = null;

        try {
            startTransaction();

            final int limit = 30;
            if (docType.equals("TOUTOU")) {
                letters = (List<LetterModel>) getSession().createQuery("from TouTouLetter f where f.karte.id = :karteId order by recorded desc").setParameter("karteId", karteId).setMaxResults(limit).list();
            } else if (docType.equals("TOUTOU_REPLY")) {
                letters = (List<LetterModel>) getSession().createQuery("from TouTouReply f where f.karte.id = :karteId order by recorded desc").setParameter("karteId", karteId).setMaxResults(limit).list();
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return letters;
    }

    /**
     * 紹介状の返信を取り出す
     * @param letterPk
     * @return　紹介状
     */
    @Override
    public LetterModel getLetter(final long letterPk) {

        roleAllowed("user");

        LetterModel letter = null;

        try {
            startTransaction();

            letter = (LetterModel) getSession().createQuery("from TouTouLetter t where t.id = :id").setParameter("id", letterPk).uniqueResult();

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return letter;
    }

    /**
     * 紹介状の返信を取り出す
     *
     * @param letterPk
     * @return　紹介状の返信
     */
    @Override
    public LetterModel getLetterReply(final long letterPk) {

        roleAllowed("user");

        LetterModel letter = null;

        try {
            startTransaction();

            letter = (LetterModel) getSession().createQuery("from TouTouReply t where t.id = :id").setParameter("id", letterPk).uniqueResult();

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return letter;
    }
}
