package com.eusecom.samshopersung.mvvmdatamodel;


import android.support.annotation.NonNull;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.realm.RealmDomain;

import java.util.List;
import rx.Observable;
public interface ShopperIDataModel {

    //method for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid);

    @NonNull
    public Observable<RealmDomain> saveDomainToRealm(RealmDomain domx);

    //method for DomainsActivity
    @NonNull
    public Observable<List<RealmDomain>> getDomainsFromRealm();


}
