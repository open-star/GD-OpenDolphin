package open.dolphin.service.remote;

import open.dolphin.service.ISetaService;
import java.util.List;
import open.dolphin.infomodel.LetterModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;

/**
 * 紹介状
 */
public class RemoteSetaService extends DolphinService implements ISetaService {

    /**
     *
     */
    public RemoteSetaService() {
    }

    /**
     * 紹介状データ作成
     * @param model
     * @return
     */
    @Override
    public long saveOrUpdateLetter(LetterModel model) {

        roleAllowed("user");

        try {
            startTransaction();
            getSession().saveOrUpdate(model);
            endTransaction();
        }
        catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return model.getId();
    }

    /**
     *　紹介状データ取り出し
     * @param karteId
     * @param docType
     * @return
     */
    @Override
    public List<LetterModel> getLetters(long karteId, String docType) {

        roleAllowed("user");

        if (docType.equals("TOUTOU")) {

            List<LetterModel>  letters = null;

            try {
                startTransaction();

                letters = (List<LetterModel>)getSession()
                                            .createQuery("from TouTouLetter f where f.karte.id = :karteId")
                                            .setParameter("karteId", karteId)
                                            .list();

                endTransaction();
            }
            catch (HibernateException e) {
                if (getSession().getTransaction().isActive()) {
                    getSession().getTransaction().rollback();
                    LogWriter.error(this.getClass(), "Rollback", e);
                }
            }

            return letters;
        }

        return null;
    }

    /**
     * 紹介状を取得する。
     * @return
     */
    @Override
    public LetterModel getLetter(long letterPk) {

        roleAllowed("user");

        LetterModel ret = null;

        try {
            startTransaction();

            ret = (LetterModel)getSession()
                      .createQuery("from TouTouLetter t where t.id = :id")
                      .setParameter("id", letterPk)
                      .uniqueResult();

            endTransaction();
        }
        catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return ret;
    }
}
