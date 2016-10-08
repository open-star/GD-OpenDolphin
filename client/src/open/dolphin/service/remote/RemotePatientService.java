package open.dolphin.service.remote;

import open.dolphin.service.IPatientService;
import java.util.ArrayList;
import java.util.List;

import open.dolphin.infomodel.PatientVisitModel;
import open.dolphin.dto.PatientSearchSpec;
import open.dolphin.infomodel.PatientModel;
import open.dolphin.log.LogWriter;
import open.dolphin.service.DolphinService;

import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * 患者情報検索
 * 
 * @author
 */
abstract class AbstractPatientModelFinder {

    protected Session session;

    /**
     *
     * @param session
     */
    public AbstractPatientModelFinder(Session session) {
        this.session = session;
    }

    /**
     * 患者情報検索
     * @param fid
     * @param spec
     * @return 患者情報
     */
    public List<PatientModel> find(String fid, PatientSearchSpec spec) {
        return find(fid, spec, false);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    public abstract List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict);
}

/**
 * 全患者検索
 * @author
 */
class AllSerach extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public AllSerach(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {
        return session.createQuery("from PatientModel p where p.facilityId = :fid").setParameter("fid", fid).list();
    }
}

/**
 * 日付検索
 * @author
 */
class DateSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public DateSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String date = spec.getDigit();
        if (!strict) {
            if (!date.endsWith("%")) {
                date += "%";
            }
        }

        List<PatientVisitModel> patientVisitModels = session.createQuery("from PatientVisitModel p where p.facilityId = :fid and p.pvtDate like :date").setParameter("fid", fid).setParameter("date", date).list();

        List<PatientModel> patients = new ArrayList();
        for (PatientVisitModel pvt : patientVisitModels) {
            PatientModel patient = pvt.getPatient();
            if (pvt != null) {
                patients.add(patient);
            }
        }
        return patients;
    }
}

/*
 * ID検索
 */
class IdSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public IdSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String pid = spec.getPatientId();
        if (!strict) {
            if (!pid.endsWith("%")) {
                pid += "%";
            }
        }

        return session.createQuery("from PatientModel p where p.facilityId = :fid and p.patientId like :pid").setMaxResults(50).setParameter("fid", fid).setParameter("pid", pid).list();
    }
}

/**
 * 名前検索
 */
class NameSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public NameSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String name = spec.getName();
        if (!strict) {
            if (!name.endsWith("%")) {
                name += "%";
            }
        }

        List<PatientModel> patients = session.createQuery("from PatientModel p where p.facilityId = :fid and p.fullName like :name").setParameter("fid", fid).setParameter("name", name).list();
        if (patients.isEmpty()) {
            patients = session.createQuery("from PatientModel p where p.facilityId = :fid and p.fullName like :name").setParameter("fid", fid).setParameter("name", "%" + name).list();
        }

        return patients;
    }
}

/**
 * カナ検索
 *
 */
class KanaSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public KanaSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String name = spec.getName();
        if (!strict) {
            if (!name.endsWith("%")) {
                name += "%";
            }
        }

        List<PatientModel> patients = session.createQuery("from PatientModel p where p.facilityId = :fid and p.kanaName like :name").setParameter("fid", fid).setParameter("name", name).list();
        if (patients.isEmpty()) {
            patients = session.createQuery("from PatientModel p where p.facilityId = :fid and p.kanaName like :name").setParameter("fid", fid).setParameter("name", "%" + name).list();
        }

        return patients;
    }
}

/**
 * ローマ字検索
 *
 */
class RomanSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public RomanSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String name = spec.getName();
        if (!strict) {
            if (!name.endsWith("%")) {
                name += "%";
            }
        }

        List<PatientModel> patients = session.createQuery("from PatientModel p where p.facilityId = :fid and p.romanName like :name").setParameter("fid", fid).setParameter("name", name).list();
        if (patients.isEmpty()) {
            patients = session.createQuery("from PatientModel p where p.facilityId = :fid and p.romanName like :name").setParameter("fid", fid).setParameter("name", "%" + name).list();
        }

        return patients;
    }
}

/**
 * 電話検索
 */
class TelephoneSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public TelephoneSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec) {

        String number = spec.getTelephone();
        if (!number.endsWith("%")) {
            number += "%";
        }

        return session.createQuery("from PatientModel p where p.facilityId = :fid and (p.telephone like :number or p.mobilePhone like :number)").setParameter("fid", fid).setParameter("number", number).list();
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String number = spec.getTelephone();
        if (!strict) {
            if (!number.endsWith("%")) {
                number += "%";
            }
        }

        return session.createQuery("from PatientModel p where p.facilityId = :fid and (p.telephone like :number or p.mobilePhone like :number)").setParameter("fid", fid).setParameter("number", number).list();
    }
}

/**
 * 郵便番号検索
 */
class ZipCodeSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public ZipCodeSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String zipCode = spec.getZipCode();
        if (!strict) {
            if (!zipCode.endsWith("%")) {
                zipCode += "%";
            }
        }
        return session.createQuery("from PatientModel p where p.facilityId = :fid and p.address.zipCode like :zipCode").setParameter("fid", fid).setParameter("zipCode", zipCode).list();
    }
}

/**
 * アドレス検索
 */
class AddressSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public AddressSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String address = spec.getAddress();
        if (!strict) {
            if (!address.endsWith("%")) {
                address += "%";
            }
        }
        return session.createQuery("from PatientModel p where p.facilityId = :fid and p.address.address like :address").setParameter("fid", fid).setParameter("address", address).list();
    }
}

/**
 * メアド検索
 */
class EmailSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public EmailSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String address = spec.getEmail();
        if (!strict) {
            if (!address.endsWith("%")) {
                address += "%";
            }
        }
        return session.createQuery("from PatientModel p where p.facilityId = :fid and p.email like :address").setParameter("fid", fid).setParameter("email", address).list();
    }
}

/**
 *
 * @author
 */
class DigitSearch extends AbstractPatientModelFinder {

    /**
     *
     * @param session
     */
    public DigitSearch(Session session) {
        super(session);
    }

    /**
     *
     * @param fid
     * @param spec
     * @param strict
     * @return
     */
    @Override
    public List<PatientModel> find(String fid, PatientSearchSpec spec, boolean strict) {

        String digit = spec.getDigit();
        if (!strict) {
            if (!digit.endsWith("%")) {
                digit += "%";
            }
        }

        List<PatientModel> patients = null;

        // Try ID search
        spec.setCode(PatientSearchSpec.ID_SEARCH);
        spec.setPatientId(digit);
        patients = findPatient(fid, spec);
        if (patients != null) {
            return patients;
        }

        // Try Telephone search
        spec.setCode(PatientSearchSpec.TELEPHONE_SEARCH);
        spec.setTelephone(digit);
        patients = findPatient(fid, spec);
        if (patients != null) {
            return patients;
        }

        // Try ZipCode search
        spec.setCode(PatientSearchSpec.ZIPCODE_SEARCH);
        spec.setZipCode(digit);
        return findPatient(fid, spec);
    }

    private List<PatientModel> findPatient(String fid, PatientSearchSpec spec) {
        PatientModelFinder finder = new PatientModelFinder(spec, session);
        return finder.find(fid, true);
    }
}

/**
 *
 * @author
 */
class PatientModelFinder {

    private AbstractPatientModelFinder finder;
    private PatientSearchSpec spec;

    /**
     *
     * @param spec
     * @param session
     */
    public PatientModelFinder(final PatientSearchSpec spec, final Session session) {
        this.spec = spec;
        this.finder = createFinder(spec.getCode(), session);
    }

    /**
     *
     * @param code
     * @param session
     * @return
     */
    private AbstractPatientModelFinder createFinder(final int code, final Session session) {
        switch (code) {
            case PatientSearchSpec.ADDRESS_SEARCH:
                return new AddressSearch(session);
            case PatientSearchSpec.ALL_SEARCH:
                return new AllSerach(session);
            case PatientSearchSpec.DATE_SEARCH:
                return new DateSearch(session);
            case PatientSearchSpec.DIGIT_SEARCH:
                return new DigitSearch(session);
            case PatientSearchSpec.EMAIL_SEARCH:
                return new EmailSearch(session);
            case PatientSearchSpec.ID_SEARCH:
                return new IdSearch(session);
            case PatientSearchSpec.KANA_SEARCH:
                return new KanaSearch(session);
            case PatientSearchSpec.NAME_SEARCH:
                return new NameSearch(session);
            case PatientSearchSpec.ROMAN_SEARCH:
                return new RomanSearch(session);
            case PatientSearchSpec.TELEPHONE_SEARCH:
                return new TelephoneSearch(session);
            case PatientSearchSpec.ZIPCODE_SEARCH:
                return new ZipCodeSearch(session);
        }
        return new AllSerach(session);
    }

    /**
     *
     * @param fid
     * @return
     */
    public List<PatientModel> find(String fid) {
        return finder.find(fid, spec, false);
    }

    /**
     *
     * @param fid
     * @param strict
     * @return
     */
    public List<PatientModel> find(String fid, boolean strict) {
        List<PatientModel> result = finder.find(fid, spec, strict);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result;
    }
}

/**
 * 患者情報 MEMO:マッピング 
 *
 */
public class RemotePatientService extends DolphinService implements IPatientService {

    /**
     *
     */
    public RemotePatientService() {
    }

    /**
     * 患者オブジェクトを取得する。
     * @param spec PatientSearchSpec 検索仕様
     * @return 患者オブジェクトの List
     */
    @Override
    public List<PatientModel> getPatients(PatientSearchSpec spec) {

        roleAllowed("user");

        List<PatientModel> patients = null;
        try {
            startTransaction();

            PatientModelFinder finder = new PatientModelFinder(spec, getSession());
            // List<PatientModel> patients = finder.find(getCallersFacilityId(ctx));
            patients = finder.find(getCallersFacilityId());

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return patients;
    }

    /**
     * 患者ID(BUSINESS KEY)を指定して患者オブジェクトを返す。
     *
     * @param patientId 施設内患者ID
     * @return 該当するPatientModel
     */
    @SuppressWarnings({"unchecked", "unchecked"})
    @Override
    public PatientModel getPatient(String patientId) {

        roleAllowed("user");

        List<PatientModel> patients = null;

        try {
            startTransaction();

            // String facilityId = getCallersFacilityId(ctx);
            String facilityId = getCallersFacilityId();

            PatientSearchSpec spec = new PatientSearchSpec();
            spec.setCode(PatientSearchSpec.ID_SEARCH);
            spec.setPatientId(patientId);

            PatientModelFinder finder = new PatientModelFinder(spec, getSession());
            patients = finder.find(facilityId, true);

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        if (patients == null) {
            return null;
        }

        return patients.get(0);
    }

    /**
     * 患者を登録する。
     * @param patient PatientModel
     * @return データベース Primary Key
     */
    @Override
    public long addPatient(PatientModel patient) {

        roleAllowed("user");

        try {
            startTransaction();

            String facilityId = getCallersFacilityId();
            patient.setFacilityId(facilityId);
            getSession().persist(patient);

            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }

        return patient.getId();
    }

    /**
     * 患者情報を更新する。
     * @param patient 更新する患者
     * @return 更新数
     */
    @Override
    public int update(PatientModel patient) {

        roleAllowed("user");

        try {
            startTransaction();
            getSession().merge(patient);
            endTransaction();
        } catch (HibernateException e) {
            if (getSession().getTransaction().isActive()) {
                getSession().getTransaction().rollback();
                LogWriter.error(this.getClass(), "Rollback", e);
            }
        }
        return 1;
    }
}
