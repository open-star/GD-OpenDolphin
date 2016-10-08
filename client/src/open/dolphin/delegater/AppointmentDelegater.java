/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import open.dolphin.dto.AppointSpec;
import open.dolphin.infomodel.AppointmentModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.IAppointmentService;

/**
 *
 * @author tomohiro
 */
public abstract class AppointmentDelegater extends DelegaterErrorHandler {

    /**
     *
     * @return
     * @throws NamingException
     */
    protected abstract IAppointmentService getService() throws NamingException;

        /**
         * 
         * @param results
         * @return
         */
    public int putAppointments(List results) {

        int size = results.size();

        List<AppointmentModel> added = new ArrayList<AppointmentModel>();
        List<AppointmentModel> updated = new ArrayList<AppointmentModel>();
        List<AppointmentModel> removed = new ArrayList<AppointmentModel>();

        for (int i = 0; i < size; i++) {
            AppointmentModel model = (AppointmentModel) results.get(i);
            int state = model.getState();
            String appoName = model.getName();
            if (state == AppointmentModel.TT_NEW) {
                // 新規予約
                added.add(model);
            } else if (state == AppointmentModel.TT_REPLACE && appoName != null) {
                // 変更された予約
                updated.add(model);
            } else if (state == AppointmentModel.TT_REPLACE && appoName == null) {
                // 取り消された予約
                removed.add(model);
            }
        }

        int retCode = 0;
        AppointSpec spec = new AppointSpec();
        spec.setAdded(added);
        spec.setUpdared(updated);
        spec.setRemoved(removed);

        try {
            getService().putAppointments(spec);
        } catch (CommunicationException e) {
            isError = true;
            LogWriter.error(this.getClass(), "サーバに接続できません。ネットワーク環境をお確かめください。", e);
            error = "サーバに接続できません。ネットワーク環境をお確かめください。";
        } catch (UndeclaredThrowableException e) {
            isError = true;
            LogWriter.error(this.getClass(), "処理を実行できません。クライアントのバージョンが古い可能性があります。", e);
            error = "クライアントのバージョンが古い可能性があります";
        } catch (NamingException e) {
            isError = true;
            LogWriter.error(this.getClass(), "適切なデリゲータを見つけられませんでした", e);
            error = "適切なデリゲータを見つけられませんでした";
        } catch (Exception e) {
            isError = true;
            LogWriter.error(this.getClass(), "不明なエラーです。念のためしばらく間をおいて再度実行してみてください。", e);
            error = "不明なエラーです";
        }

        return retCode;
    }
}
