/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ge.tvera.security.api;

import java.io.Serializable;
import java.util.Set;

/**
 * @author ucha
 */
public abstract class User implements Serializable {

    public Set<String> rights;
    private Object userData;

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public Set<String> getRights() {
        return rights;
    }

    public void setRights(Set<String> rights) {
        this.rights = rights;
    }

}
