package open.dolphin.service;

import java.util.List;
import open.dolphin.infomodel.LetterModel;

/**
 * 瀬田クリニックサービス。
 */
public interface ISetaService {

    /**
     *
     * @param model
     * @return
     */
    public long saveOrUpdateLetter(LetterModel model);

    /**
     *
     * @param karteId
     * @param docType
     * @return
     */
    public List<LetterModel> getLetters(long karteId, String docType);

    /**
     *
     * @param letterPk
     * @return
     */
    public LetterModel getLetter(long letterPk);
}
