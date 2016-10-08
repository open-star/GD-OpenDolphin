package open.dolphin.service;

import java.util.List;
import open.dolphin.infomodel.KarteBean;
import open.dolphin.infomodel.LaboModuleValue;
import open.dolphin.infomodel.PatientModel;
import org.hibernate.HibernateException;

/**
 * ILaboService
 *
 * @author Minagawa,Kazushi
 */
public interface ILaboService {
    
    /**
     * LaboModuleを保存する。
     * @param laboModuleValue LaboModuleValue
     * @return
     */
    public PatientModel putLaboModule(LaboModuleValue laboModuleValue);
    
    /**
     *
     * @param patientId
     * @return
     * @throws HibernateException
     */
    public KarteBean getKarteFor(String patientId) throws HibernateException;

    /**
     *
     * @param karteId
     * @return
     */
    public List<LaboModuleValue> getModulesFor(long karteId);
}
