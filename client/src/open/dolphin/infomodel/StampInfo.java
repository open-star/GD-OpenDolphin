/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.infomodel;

import java.io.IOException;
import java.io.Writer;
import open.dolphin.queries.DolphinQuery;

/**
 * スタンプ情報
 * @author 
 */
public class StampInfo implements IStampInfo {

    private String stampName;
    private String stampRole;
    private int stampNumber;
    private String entity;
    private boolean editable = true;
    private boolean asp;
    private String stampId;
    private String memo;
    private boolean turnIn;
    private String stampStatus;

    /**
     * 検索
     * @param query
     * @return true false
     */
    @Override
    public boolean search(DolphinQuery query) {
        if (stampName != null) {
            if (stampName.indexOf(query.what("keyword")) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * イニシャライズ
     * @param stampId
     * @param name
     * @param text
     * @param entity
     * @param _role
     */
    @Override
    public void initialize(String stampId, String name, String text, String entity, String _role) {
        setStampId(stampId);   // Stamp ID
        setStampName(name);    //
        setStampMemo(text);    // Tooltip
        setEntity(entity);     // カテゴリ
        setStampRole(_role);

        setStampNumber(1);
        setStampStatus("");
        editable = true;
        asp = false;
        turnIn = true;
    }

    /**
     * スタンプ名のGetter
     * @return スタンプ名
     */
    @Override
    public String getStampName() {
        return stampName;
    }

    /**
     * スタンプ名のSetter
     * @param text スタンプ名
     */
    @Override
    public void setStampName(String text) {
        this.stampName = text;
    }

    /**
     * スタンプロールのGetter
     * @return スタンプロール
     */
    @Override
    public String getStampRole() {
        return stampRole;
    }

    /**
     * スタンプロールのSetter
     * @param _role スタンプロール
     */
    @Override
    public void setStampRole(String _role) {
        stampRole = _role;
    }

    /**
     * ステータスのGetter
     * @return ステータス
     */
    @Override
    public String getStampStatus() {
        return stampStatus;
    }

    /**
     * ステータスのSetter
     * @param status ステータス
     */
    @Override
    public void setStampStatus(String status) {
        stampStatus = status;
    }

    /**
     * エントリのGetter
     * @return エントリ
     */
    @Override
    public String getEntity() {
        return entity;
    }

    /**
     * エントリのSetter
     * @param entity エントリ
     */
    @Override
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * シリアライズ可能か
     * @return シリアライズ可能か
     */
    @Override
    public boolean isSerialized() {
        return stampId != null;
    }

    /**
     * ASPのGetter
     * @return ASP
     */
    @Override
    public boolean isASP() {
        return asp;
    }

    /**
     * ASPのSetter
     * @param asp ASP
     */
    @Override
    public void setASP(boolean asp) {
        this.asp = asp;
    }

    /**
     * スタンプIDのGetter
     * @return スタンプID
     */
    @Override
    public String getStampId() {
        return stampId;
    }

    /**
     * スタンプIDのSetter
     * @param id スタンプID
     */
    @Override
    public void setStampId(String id) {
        stampId = id;
    }

    /**
     * スタンプメモのGetter
     * @return スタンプメモ
     */
    @Override
    public String getStampMemo() {
        return memo;
    }

    /**
     * スタンプメモのSetter
     * @param memo スタンプメモ
     */
    @Override
    public void setStampMemo(String memo) {
        this.memo = memo;
    }

    /**
     * エディッタブル
     * @param editable エディッタブル
     */
    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * エディッタブル
     * @return エディッタブル
     */
    @Override
    public boolean isEditable() {
        return editable;
    }

    /**
     * turnInのSetter
     * @param turnIn
     */
    @Override
    public void setTurnIn(boolean turnIn) {
        this.turnIn = turnIn;
    }

    /**
     *
     * @return turnIn
     */
    @Override
    public boolean isTurnIn() {
        return turnIn;
    }

    /**
     * スタンプNOのSetter
     * @param stampNumber スタンプNO
     */
    @Override
    public void setStampNumber(int stampNumber) {
        this.stampNumber = stampNumber;
    }

    /**
     * スタンプNOのGetter
     * @return スタンプNO
     */
    @Override
    public int getStampNumber() {
        return stampNumber;
    }

    /**
     * 比較
     * @param other
     * @return 同じなら真
     */
    @Override
    public int compareTo(Object other) {
        if (other != null && getClass() == other.getClass()) {
            int result = getStampNumber() - ((ModuleInfoBean) other).getStampNumber();
            return result;
        }
        return -1;
    }

    /**
     * NULLなら
     * @param s
     * @return　NULLなら""
     */
    private String nullToString(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

    /**
     * シリアライズ
     * @param result
     * @throws IOException
     */
    @Override
    public void serialize(Writer result) throws IOException {
        result.append("<ModuleInfoBean name='" + nullToString(stampName) + "' role='" + nullToString(stampRole) + "' stampNumber='" + Integer.toString(stampNumber) + "' entity='" + nullToString(entity) + "' />");
        result.append(System.getProperty("line.separator"));
    }
}
