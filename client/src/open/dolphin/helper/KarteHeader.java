/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.helper;

import open.dolphin.infomodel.DocInfoModel;
import open.dolphin.infomodel.DocumentModel;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.infomodel.ModelUtils;

/**
 *
 * @author
 */
public class KarteHeader {

    private DocumentModel model;
    private StringBuilder buffer;
    private String title;

    /**
     *
     * @param model
     * @param title
     */
    public KarteHeader(DocumentModel model, String title) {
        super();
        this.buffer = new StringBuilder();
        this.model = model;
        this.title = title;
    }

    /*
    public String toString() {
    buffer.append(title);
    buffer.append(" ");
    //   buffer.append(ModelUtils.getDateAsFormatString(new Date(), IInfoModel.KARTE_DATE_FORMAT));
    DocInfoModel docInfo = model.getDocInfo();
    buffer.append(" 作成日[");
    buffer.append(ModelUtils.getDateAsFormatString(docInfo.getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT));
    buffer.append(" ]");
    if (docInfo.getParentId() != null) {
    buffer.append(" 更新日[");
    buffer.append(ModelUtils.getDateAsFormatString(docInfo.getConfirmDate(), IInfoModel.KARTE_DATE_FORMAT));
    buffer.append(" ]");
    }

    if (model.getCreator() != null) {
    buffer.append(" ");
    buffer.append(model.getCreator().getCommonName());
    buffer.append(" ");
    buffer.append(model.getCreator().getDepartmentModel().getDepartmentDesc());
    buffer.append(" ");
    buffer.append(model.getCreator().getLicenseModel().getLicenseDesc());
    }
    if (docInfo.getStatus().equals(IInfoModel.STATUS_TMP)) {
    buffer.append(" - 仮保存中");
    }
    return buffer.toString();
    }
     */
    /**
     *
     * @return　カルテヘッダストリング
     */
    @Override
    public String toString() {
        buffer.append(title);
        buffer.append(" ");
        //   buffer.append(ModelUtils.getDateAsFormatString(new Date(), IInfoModel.KARTE_DATE_FORMAT));
        DocInfoModel docInfo = model.getDocInfo();
        String createDate = ModelUtils.getDateAsFormatString(docInfo.getFirstConfirmDate(), IInfoModel.KARTE_DATE_FORMAT);
        if (createDate != null) {
            buffer.append(" 作成日[");
            buffer.append(createDate);
            buffer.append(" ]");
        }
        if (docInfo.getParentId() != null) {
            String modifyDate = ModelUtils.getDateAsFormatString(docInfo.getConfirmDate(), IInfoModel.KARTE_DATE_FORMAT);
            if (modifyDate != null) {
                buffer.append(" 更新日[");
                buffer.append(modifyDate);
                buffer.append(" ]");
            }
        }

        if (model.getCreator() != null) {
            buffer.append(" ");
            buffer.append(model.getCreator().getCommonName());
            buffer.append(" ");
            buffer.append(model.getCreator().getDepartmentModel().getDepartmentDesc());
            buffer.append(" ");
            buffer.append(model.getCreator().getLicenseModel().getLicenseDesc());
        }
        if (docInfo.getStatus().equals(IInfoModel.STATUS_TMP)) {
            buffer.append(" - 仮保存中");
        }
        return buffer.toString();
    }
}
