package open.dolphin.project;

import java.io.Serializable;

/**
 *
 * @author oda
 */
public class DolphinPrincipal implements Serializable {
	
	private String userId;
	private String facilityId;

        /**
         *
         * @return
         */
        public String getUserId() {
		return userId;
	}
	
        /**
         *
         * @param uid
         */
        public void setUserId(String uid) {
		this.userId = uid;
	}
	
        /**
         *
         * @param facilityId
         */
        public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}
	
        /**
         *
         * @return
         */
        public String getFacilityId() {
		return facilityId;
	}
}
