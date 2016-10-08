package open.dolphin.infomodel;

import open.dolphin.queries.DolphinQuery;

/**
 * ProgressCourse MEMO:マッピング
 * Serializable
 * @author Kazushi Minagawa, Digital Globe, Inc.
 *
 */
public class ProgressCourse extends InfoModel {//id

    private static final long serialVersionUID = -8741238869253717241L;
    private String freeText;

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (freeText != null) {
            return (freeText.indexOf(query.what("keyword")) != -1);
        }
        return false;
    }

    /**
     * freeTextのSetter
     * @param freeText
     */
    public void setFreeText(String freeText) {
        this.freeText = freeText;
    }

    /**
     * freeTextのGetter
     * @return
     */
    public String getFreeText() {
        return freeText;
    }
}
