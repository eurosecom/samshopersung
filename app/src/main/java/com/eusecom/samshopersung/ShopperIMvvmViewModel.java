package com.eusecom.samshopersung;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.models.Album;
import com.eusecom.samshopersung.models.Employee;
import com.eusecom.samshopersung.realm.RealmDomain;
import java.util.List;
import rx.Observable;

public interface ShopperIMvvmViewModel {

    //methods for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getMyCompaniesFromServer();

    @NonNull
    public void emitSaveDomainToRealm(RealmDomain domx);

    @NonNull
    public Observable<RealmDomain> getMyObservableSaveDomainToRealm();

    public void clearObservableSaveDomainToRealm();

    //methods for OfferKtActivity
    @NonNull
    public Observable<List<Album>> getMyObservableAlbumsFromList();


    //methods for MapActivity
    @NonNull
    public Observable<List<Employee>> getMyObservableEmployeesFromList();



}
