package open.dolphin.client;

/**
 * ツリーインフォ
 * TreeInfo
 * 
 * @author Minagawa,Kazushi
 *
 */
public class TreeInfo {

    private String name;
    private String entity;

    /**
     *
     * @return　エンティティ
     */
    public String getEntity() {
        return entity;
    }

    /**
     *　エンティティ
     * @param entity　エンティティ
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     *
     * @return　名前
     */
    public String getName() {
        return name;
    }

    /**
     *　名前
     * @param name　名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }
}
