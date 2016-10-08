/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package open.dolphin.security;

import java.security.Principal;

/**
 *
 * @author tomohiro
 */
public class SecurityPrincipal implements Principal {

    private String name;

    /**
     *
     * @param name
     */
    public SecurityPrincipal(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "SecutiryPrincipal: " + name;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SecurityPrincipal other = (SecurityPrincipal) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
