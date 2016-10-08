package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * 画像のバンドル MEMO:マッピング
 * @author
 */
public class BundleImage extends BundleDolphin {//id

    private static final long serialVersionUID = -8747202550129389856L;//2/26
    private ClaimItem radItem;

    /**
     * コンストラクタ
     */
    public BundleImage() {
    }

    /**
     * 検索 MEMO:何もしない
     * @param query
     * @return false
     */
    @Override
    public boolean search(DolphinQuery query) {
        return false;
    }

    /**
     * claimItemの追加
     * @param val
     */
    @Override
    public void addClaimItem(ClaimItem val) {
        if (val instanceof ClaimItem) {
            if (val.getCode().startsWith("700")) {
                radItem = val;
                for (int i = 0; i < claimItems.length; i++) {
                    if (!claimItems[i].getCode().startsWith("002")) {
                        claimItems[i].setNumber(radNumber());
                    }
                }
            } else if (radItem instanceof ClaimItem) {
                if (!val.getCode().startsWith("002")) {
                    val.setNumber(radNumber());
                }
            }
        }
        super.addClaimItem(val);
    }

    /**
     *
     * @return
     */
    private String radNumber() {
        float suryo1 = radItem.getSuryo1();
        float suryo2 = radItem.getSuryo2();
        return Float.toString(suryo1 * suryo2);
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    @Override
    public void serialize(Writer result) throws IOException {
        //TODO serialize
    }

    /**
     * MEMO:何もしない
     * @param result
     * @throws IOException
     */
    @Override
    public void deserialize(Writer result) throws IOException {
        //TODO deserialize
    }
}
