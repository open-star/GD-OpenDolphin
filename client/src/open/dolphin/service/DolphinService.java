package open.dolphin.service;

import java.security.Principal;

import open.dolphin.exception.SecurityException;
import open.dolphin.infomodel.IInfoModel;
import open.dolphin.project.GlobalVariables;
import open.dolphin.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

// MEMO: セッション管理を考える必要がある

/**
 * DolphinService
 *
 * @author Minagawa,Kazushi
 *
 */
public class DolphinService implements IInfoModel {

    /**
     *
     */
    protected Session session;
    private int transactionCount = 0;

    /**
     *
     */
    public DolphinService() { }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (session.isOpen()) {
            session.close();
        }
    }

    /**
     *
     * @return
     */
    protected Session getSession() {
        if (session == null || !session.isOpen()) {
            session = HibernateUtil.openSession();
        }
        return session;
    }

    /**
     *
     */
    protected synchronized void startTransaction() {
        if (getSession().getTransaction().isActive()) {
            transactionCount += 1;
        }
        else {
            getSession().beginTransaction();
            transactionCount = 0;
        }
    }

    /**
     *
     */
    protected synchronized void endTransaction() {
        if (transactionCount == 0) {
            Transaction transaction = getSession().getTransaction();
            if (transaction.isActive()) {
                getSession().flush();
                transaction.commit();
            }
            HibernateUtil.closeSession(session);
        }
        else {
            transactionCount -= 1;
        }
    }

    /**
     *
     * @param checkId
     * @return
     */
    public String checkIdAsComposite(String checkId) {
        int index = checkId.indexOf(COMPOSITE_KEY_MAKER);
        if (index < 0) {
            throw new SecurityException(checkId);
        }
        String callerId = null;
        String callersFacilityId = null;
        String facilityId = checkId.substring(0, index);
        for (Principal principal : GlobalVariables.getSubject().getPrincipals()) {
            callerId = principal.getName();
            index = callerId.indexOf(COMPOSITE_KEY_MAKER);
            callersFacilityId = callerId.substring(0, index);
            if (facilityId.equals(callersFacilityId)) {
                return checkId;
            }
        }
        throw new SecurityException(checkId);
    }

    /**
     *
     * @param checkId
     * @return
     */
    public String checkFacility(String checkId) {
        String callerKey = getCallersFacilityId();
        String requestKey = getRequestsFacilityId(checkId);
        if (! callerKey.equals(requestKey)) {
            throw new SecurityException(requestKey);
        }
        return checkId;
    }

    /**
     *
     * @param testId
     * @return
     */
    public String getIdAsComposite(String testId) {
        StringBuilder sb = new StringBuilder();
        sb.append(getCallersFacilityId());
        sb.append(COMPOSITE_KEY_MAKER);
        sb.append(getIdAsLocal(testId));
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public String getCallersFacilityId() {
        String callerId = null;
        int index = 0;
        for (Principal principal : GlobalVariables.getSubject().getPrincipals()) {
            callerId = principal.getName();
            index = callerId.indexOf(COMPOSITE_KEY_MAKER);
            if (index != 0) {
                return callerId.substring(0, index);
            }
        }
        return null;
    }
    
    /**
     *
     * @param facilityId
     * @param localIdd
     * @return
     */
    public String getIdAsQualified(String facilityId, String localIdd) {
        StringBuilder buf = new StringBuilder();
        buf.append(facilityId);
        buf.append(COMPOSITE_KEY_MAKER);
        buf.append(localIdd);
        return buf.toString();
    }
    
    /**
     *
     * @param testId
     * @return
     */
    public String getIdAsLocal(String testId) {
        int index = testId.indexOf(COMPOSITE_KEY_MAKER);
        return index > 0 ? testId.substring(index + 1) : testId;
    }
    
    private String getRequestsFacilityId(String checkId) {
        int index = checkId.indexOf(COMPOSITE_KEY_MAKER);
        return index > 0 ? checkId.substring(0, index) : checkId;
    }

    /**
     *
     * @param role
     */
    protected void roleAllowed(String role)  {
        for (Principal principal : GlobalVariables.getSubject().getPrincipals()) {
            if (principal.getName().equalsIgnoreCase(role)) {
                return;
            }
        }
        throw new SecurityException("Not allowed action");
    }
}
