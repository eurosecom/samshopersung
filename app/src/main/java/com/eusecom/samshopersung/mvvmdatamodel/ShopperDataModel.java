package com.eusecom.samshopersung.mvvmdatamodel;

import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.google.firebase.database.DatabaseReference;

public class ShopperDataModel implements ShopperIDataModel {

    DatabaseReference mFirebaseDatabase;
    ShopperRetrofitService mShopperRetrofitService;
    Resources mResources;
    Realm mRealm;
    ExampleInterceptor mInterceptor;

    public ShopperDataModel(@NonNull final DatabaseReference databaseReference,
                                 @NonNull final ShopperRetrofitService shopperRetrofitService,
                                 @NonNull final Resources resources,
                                 @NonNull final Realm realm,
                                 @NonNull final ExampleInterceptor interceptor) {
        mFirebaseDatabase = databaseReference;
        mShopperRetrofitService = shopperRetrofitService;
        mResources = resources;
        mRealm = realm;
        mInterceptor = interceptor;
    }


    //recyclerview method for ChooseCompanyuActivity

    @Override
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid) {

        setRetrofit(servername);
        return mShopperRetrofitService.getCompaniesFromServer(userhash, userid);

    }

    //set retrofit by runtime
    public void setRetrofit(String servername) {

        System.out.println("invxstring servername " + servername);
        String urlname = "http://" + servername;

        mInterceptor.setInterceptor(urlname);

    }
    //end set retrofit by runtime

}
