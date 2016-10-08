/*
 * Created on 2004/10/13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package open.dolphin.service.remote;

import open.dolphin.service.IMasterService;
import java.util.List;

import open.dolphin.dto.MasterSearchSpec;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;


/**
 * 放射線（レントゲン） MEMO:マッピング
 *
 */
public class RemoteRadiologyMasterService extends DolphinService implements IMasterService {

    /**
     *
     */
    public RemoteRadiologyMasterService() {
    }

    /**
     * レントゲンマスターデータの取り出し
     * @param spec
     * @return　レントゲンデータ
     */
    @Override
    public List<RadiologyMethodValue> getMaster(MasterSearchSpec spec) {

        roleAllowed("user");

        List<RadiologyMethodValue> radiologyMethodValues = null;

        try {
            startTransaction();

            switch (spec.getCode()) {

                case MasterSearchSpec.RADIOLOGY_METHOD:
                    radiologyMethodValues = getSession().createQuery("from RadiologyMethodValue r where r.hierarchyCode1 >= :hc1 order by r.hierarchyCode1").setParameter("hc1", spec.getFrom()).list();
                    break;
                case MasterSearchSpec.RADIOLOGY_COMENT:
                    radiologyMethodValues = getSession().createQuery("from RadiologyMethodValue r where r.hierarchyCode2 like :hc2 order by r.hierarchyCode2").setParameter("hc2", spec.getHierarchyCode1()).list();
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return radiologyMethodValues;
    }
}
