/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.delegater;

import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import open.dolphin.dto.MasterSearchSpec;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.service.IMasterService;

/**
 *
 * @author tomohiro
 */
public abstract class MasterDelegater extends DelegaterErrorHandler {

    /**
     *
     * @return
     * @throws NamingException
     */
    protected abstract IMasterService getService() throws NamingException;

    /**
     *
     * @return
     */
    public List<RadiologyMethodValue> getAdminClass() {
        try {
            MasterSearchSpec spec = new MasterSearchSpec();
            spec.setCode(MasterSearchSpec.ADMIN_CLASS);
            spec.setFrom("0");
            return getService().getMaster(spec);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }

    /**
     *
     * @param h1
     * @return
     */
    public List<RadiologyMethodValue> getRadiologyComments(String h1) {

        try {
            MasterSearchSpec spec = new MasterSearchSpec();
            spec.setCode(MasterSearchSpec.RADIOLOGY_COMENT);
            spec.setHierarchyCode1(h1 + "%");

            List<RadiologyMethodValue> collection = new ArrayList<RadiologyMethodValue>();

            for (RadiologyMethodValue value : getService().getMaster(spec)) {
                collection.add(value);
            }
            return collection;

        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }

        return null;
    }

    /**
     *
     * @return
     */
    public List<RadiologyMethodValue> getRadiologyMethod() {
        try {
            MasterSearchSpec spec = new MasterSearchSpec();
            spec.setCode(MasterSearchSpec.RADIOLOGY_METHOD);
            spec.setFrom("0");
            return getService().getMaster(spec);
        } catch (Exception e) {
            dispatchError(getClass(), e, "");
        }
        return null;
    }
}
