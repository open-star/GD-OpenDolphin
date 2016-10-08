package open.dolphin.project;

import java.awt.Window;
import open.dolphin.client.AboutDialog;
import open.dolphin.client.SaveDialog;
import open.dolphin.client.SaveParams;

import open.dolphin.infomodel.ID;

/**
 * プロジェクトに依存するオブジェクトを生成するファクトリクラス。
 *
 * @author  Kazushi Minagawa, Digital Globe, Inc.
 */
public class DolphinFactory extends AbstractProjectFactory {

    /**
     *
     */
    protected String csgwPath;

    /** Creates new Project */
    public DolphinFactory() {
    }

    /**
     * 地域連携用の患者のMasterIdを返す。
     * @param facilityId
     */
    @Override
    public ID createMasterId(String pid, String facilityId) {
        return new ID(pid, "facility", facilityId);
    }

    /**
     * CSGW(Client Side Gate Way) のパスを返す。
     * 
     * @param  uploaderAddress MMLアップローダのIP Address
     * @param  share Samba 共有ディレクトリ
     * @param  facilityId 連携用の施設ID
     */
    @Override
    public String createCSGWPath(String uploaderAddress, String share, String facilityId) {
        if (csgwPath == null) {
            if (GlobalConstants.isWin()) {
                StringBuilder sb = new StringBuilder();
                sb.append("\\\\");
                sb.append(uploaderAddress);
                sb.append("\\");
                sb.append(share);
                sb.append("\\");
                sb.append(facilityId);
                csgwPath = sb.toString();
            } else if (GlobalConstants.isMac()) {
                StringBuilder sb = new StringBuilder();
                sb.append("smb://");
                sb.append(uploaderAddress);
                sb.append("/");
                sb.append(share);
                sb.append("/");
                sb.append(facilityId);
                csgwPath = sb.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("/");
                sb.append(uploaderAddress);
                sb.append("/");
                sb.append(share);
                sb.append("/");
                sb.append(facilityId);
                csgwPath = sb.toString();
            }
        }
        return csgwPath;
    }

    /**
     * About画面
     * @return About画面
     */
    @Override
    public Object createAboutDialog() {
        return new AboutDialog();
    }

    /**
     * 「ドキュメント保存」ダイアログを生成する。
     * @param parent 親ウィンドウ
     * @param params ドキュメント保存のパラメーターのインスタンス
     * @return 「ドキュメント保存」ダイアログのインスタンス
     */
    @Override
    public Object createSaveDialog(Window parent, SaveParams params) {
        SaveDialog sd = new SaveDialog(parent, params);
        params.setAllowPatientRef(false);    // 患者の参照
        params.setAllowClinicRef(false);     // 診療履歴のある医療機関
        // sd.initializeWith(params);
        return sd;
    }
}
