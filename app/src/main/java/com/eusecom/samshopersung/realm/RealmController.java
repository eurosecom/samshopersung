package com.eusecom.samshopersung.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
 
 
public class RealmController {
 
    private static RealmController instance;
    private final Realm realm;
 
    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {

        return instance;
    }
 
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }


    /**
     * methods for DomainsActivity
     */

    //get domains from RealmDomain
    public List<RealmDomain> getDomainsFromRealmDomain() {

        Log.d("DomainsViewModelRealm", realm.toString());
        List<RealmDomain> domains = null;
        domains = realm.where(RealmDomain.class).findAll();
        return domains;
    }
    //end get domains from RealmDomain


    /**
     * end methods for DomainsActivity
     */

}