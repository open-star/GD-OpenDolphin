/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.utils;

import java.lang.reflect.UndeclaredThrowableException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import open.dolphin.log.LogWriter;

/**
 *
 * @author
 * @param <InputT>
 * @param <OutputT>
 */
public class ExceptionHandler<InputT, OutputT> {

    /**
     *
     */
    protected String error;
    /**
     *
     */
    protected boolean isError;
    /**
     *
     */
    public Adapter<InputT, OutputT> adapter;

    /**
     * コンストラクタ
     * @param adapter
     */
    public ExceptionHandler(Adapter<InputT, OutputT> adapter) {
        this.adapter = adapter;
    }

    /**
     * クライアントに対し文字列を送信する。
     * @param in 
     * @return
     */
    public OutputT handle(InputT in) {
        OutputT result = null;
        try {
            result = adapter.onResult(in);
        } catch (CommunicationException e) {
            isError = true;
            LogWriter.error(this.getClass(), "サーバに接続できません。ネットワーク環境をお確かめください。", e);
            error = "サーバに接続できません。ネットワーク環境をお確かめください。";
        } catch (UndeclaredThrowableException e) {
            isError = true;
            LogWriter.error(this.getClass(), "処理を実行できません。" + System.getProperty("line.separator") + "クライアントのバージョンが古い可能性があります。", e);
            error = "クライアントのバージョンが古い可能性があります";
        } catch (NamingException e) {
            isError = true;
            LogWriter.error(this.getClass(), "適切なデリゲータを見つけられませんでした", e);
            error = "適切なデリゲータを見つけられませんでした";
        } catch (Exception e) {
            isError = true;
            LogWriter.error(this.getClass(), "不明なエラーです。念のためしばらく間をおいて再度実行してみてください。(BusinessDelegater)", e);
            error = "不明なエラーです";
        }
        return result;
    }
}
