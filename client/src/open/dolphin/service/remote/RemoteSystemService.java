package open.dolphin.service.remote;

import open.dolphin.service.ISystemService;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityExistsException;

import open.dolphin.infomodel.FacilityModel;
import open.dolphin.infomodel.RadiologyMethodValue;
import open.dolphin.infomodel.RoleModel;
import open.dolphin.infomodel.UserModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;
import org.hibernate.HibernateException;

/**
 * MEMO:マッピング
 * @author
 */
public class RemoteSystemService extends DolphinService implements ISystemService {

    private static final String DEFAULT_FACILITY_OID = "1.3.6.1.4.1.9414.10.1";

    /**
     * 通信を確認する。
     * @return Hello, OpenDolphin文字列
     */
    @Override
    public String helloDolphin() {
        return "Hello, OpenDolphin";
    }

    /**
     * 施設と管理者情報を登録する。
     *
     * @param user 施設管理者
     */
    @Override
    public void addFacilityAdmin(UserModel user) {

        FacilityModel facility = user.getFacility();
        String facilityId = facility.getFacilityId();
        if (facilityId == null || facilityId.equals("")) {
            facilityId = DEFAULT_FACILITY_OID;
            facility.setFacilityId(facilityId);
        }

        FacilityModel facilityModel = (FacilityModel) getSession().createQuery("from FacilityModel f where f.facilityId = :fid").setParameter("fid", facilityId).uniqueResult();
        if (facilityModel != null) {
            throw new EntityExistsException();
        }

        try {
            startTransaction();

            getSession().persist(facility);

            StringBuilder sb = new StringBuilder();
            sb.append(facilityId);
            sb.append(COMPOSITE_KEY_MAKER);
            sb.append(user.getUserId());
            user.setUserId(sb.toString());

            getSession().persist(user);

            boolean hasAdminRole = false;
            boolean hasUserRole = false;

            Set<RoleModel> roles = user.getRoles();
            if (roles != null) {
                for (RoleModel val : roles) {
                    if (val.getRole().equals(ADMIN_ROLE)) {
                        hasAdminRole = true;
                        continue;
                    }
                    if (val.getRole().equals(USER_ROLE)) {
                        hasUserRole = true;
                        continue;
                    }
                }
            }

            if (!hasAdminRole) {
                RoleModel role = new RoleModel();
                role.setRole(ADMIN_ROLE);
                role.setUser(user);
                role.setUserId(user.getUserId());
                getSession().persist(role);
                user.addRole(role);
            }


            if (!hasUserRole) {
                RoleModel role = new RoleModel();
                role.setRole(USER_ROLE);
                role.setUser(user);
                role.setUserId(user.getUserId());
                getSession().persist(role);
                user.addRole(role);
            }

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
    }

    /**
     * 画像診断メソッドマスタを登録する。
     *
     * @param methods メソッド
     */
    @Override
    public void putRadMethodMaster(List<RadiologyMethodValue> methods) {

        if (methods == null) {
            return;
        }

        try {
            startTransaction();
            for (RadiologyMethodValue method : methods) {
                getSession().persist(method);
            }
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
    }
}
