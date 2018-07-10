package com.eusecom.samshopersung.realm;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;

public interface IRealmController {

    //get realm
    public Realm getRealm();

    //Refresh the realm istance
    public void refresh();

    //methods for DomainsActivity

    //get domains from RealmDomain
    @NonNull
    public List<RealmDomain> getDomainsFromRealmDomain();

}
