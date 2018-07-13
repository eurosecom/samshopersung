package com.eusecom.samshopersung.realm;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;

public interface IRealmController {

    //get realm
    Realm getRealm();

    //Refresh the realm istance
    void refresh();

    //methods for DomainsActivity

    //get domains from RealmDomain
    List<RealmDomain> getDomainsFromRealmDomain();


    //methods for ChooseCompanyActivity

    //try if exist domain in RealmDomain
    RealmDomain existRealmDomain(@NonNull final RealmDomain domx);

    //to save domain into RealmDomain
    void setRealmDomainData(@NonNull final RealmDomain domx);

    //delete domain from RealmDomain
    void deleteRealmDomainData(@NonNull final RealmDomain domx);

}
