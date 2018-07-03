package com.eusecom.samshopersung.mvvmdatamodel;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.realm.RealmDomain;
import java.util.List;
import rx.Observable;

public interface ShopperIDataModel {

    //methods for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid);

    @NonNull
    public Observable<RealmDomain> saveDomainToRealm(RealmDomain domx);

    //methods for DomainsActivity
    @NonNull
    public Observable<List<RealmDomain>> getDomainsFromRealm();

    //methods for OfferKtActivity
    @NonNull
    public Observable<List<Album>> prepareAlbumsList();

    //methods for MapActivity
    public Observable<List<Employee>> prepareEmployeeList();


}
