package com.eusecom.samshopersung.mvvmdatamodel;


import android.support.annotation.NonNull;
import com.eusecom.samshopersung.CompanyKt;
import java.util.List;
import rx.Observable;
public interface ShopperIDataModel {

    //recyclerview method for ChooseCompanyActivity
    @NonNull
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid);


}
