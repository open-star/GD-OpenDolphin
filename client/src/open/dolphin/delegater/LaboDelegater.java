/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import open.dolphin.delegater.error.CantFindPatentExcepion;
import java.util.List;
import javax.naming.NamingException;
import open.dolphin.dto.LaboSearchSpec;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.ILaboService;
import org.hibernate.HibernateException;

/**
 *
 * @author tomohiro
 */
public abstract class LaboDelegater extends DelegaterErrorHandler {

    /**
     *
     * @return
     * @throws NamingException
     */
    protected abstract ILaboService getService() throws NamingException;

    /**
     *
     * @param spec
     * @return
     */
    public List<LaboModuleValue> getLaboModules(LaboSearchSpec spec) {

        try {
            long karteId = spec.getKarteId();
            List<LaboModuleValue> result = new ArrayList<LaboModuleValue>();

            long from = DateStringToLong(spec.getFromDate(), "yyyy-MM-dd");
            long to = DateStringToLong(spec.getToDate(), "yyyy-MM-dd");

            List<LaboModuleValue> modules = getService().getModulesFor(karteId);

            if (modules == null) {
                throw new Exception();
            }

            for (LaboModuleValue module : modules) {
                long sampleTime = DateStringToLong(module.getSampleTime(), "yyyy-MM-dd'T'HH:mm:ss");
                if ((sampleTime > from) && (sampleTime < to)) {
                    result.add(module);
                }
            }

            return result;
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }

        return null;
    }

    /**
     *
     * @param dateString
     * @param formatString
     * @return
     */
    private long DateStringToLong(String dateString, String formatString) {
        long result = 0;
        DateFormat deteformatter = new SimpleDateFormat(formatString);
        try {
            Date date = deteformatter.parse(dateString);
            result = date.getTime();
        } catch (ParseException ex) {
            LogWriter.error(getClass(), ex);
        }
        return result;
    }

    /**
     *
     * @param value
     * @return
     * @throws CantFindPatentExcepion
     */
    public PatientModel putLaboModule(LaboModuleValue value) throws CantFindPatentExcepion {

        try {
            return getService().putLaboModule(value);
        } catch (HibernateException ex) {
            dispatchError(getClass(), ex, "");
            throw new CantFindPatentExcepion(ex);
        } catch (NamingException ex) {
            dispatchError(getClass(), ex, "");
        }

        return null;
    }

    /**
     *
     * @param module
     * @return
     * @throws CantFindPatentExcepion
     */
    public boolean isDuplicatedLaboTest(LaboModuleValue module) throws CantFindPatentExcepion {
        boolean result = false;
        KarteBean karte = getKarteFor(module.getPatientId());
        List<LaboModuleValue> modules = new ArrayList<LaboModuleValue>();
        try {
            modules = getService().getModulesFor(karte.getId());
        } catch (NamingException ex) {
              LogWriter.error(getClass(), ex);
        }

        for (LaboModuleValue target : modules) {
            if (!target.getSampleTime().equals(module.getSampleTime())) {
                continue;
            }
            if (!target.getRegistTime().equals(module.getRegistTime())) {
                continue;
            }
            if (!target.getReportTime().equals(module.getReportTime())) {
                continue;
            }
            result = true;
            break;
        }
        return result;
    }

    /**
     *
     * @param patientId
     * @return
     * @throws CantFindPatentExcepion
     */
    public KarteBean getKarteFor(String patientId) throws CantFindPatentExcepion {
        try {
            return getService().getKarteFor(patientId);
        } catch (HibernateException ex) {
            dispatchError(getClass(), ex, "");
            throw new CantFindPatentExcepion(ex);
        } catch (NamingException ex) {
            dispatchError(getClass(), ex, "");
        }
        return null;
    }
}
