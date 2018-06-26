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
import com.eusecom.samshopersung.realm.RealmDomain;
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


    //method for ChooseCompanyuActivity

    @Override
    public Observable<List<CompanyKt>> getCompaniesFromMysqlServer(String servername, String userhash, String userid) {

        setRetrofit(servername);
        return mShopperRetrofitService.getCompaniesFromServer(userhash, userid);

    }

    //save domains to realm
    @NonNull
    @Override
    public Observable<RealmDomain> saveDomainToRealm(@NonNull final RealmDomain domx) {

        //System.out.println("existRealmDomain " + domx.getDomain());
        //does exist invoice in Realm?
        RealmDomain domainexists = existRealmDomain( domx );

        if(domainexists != null){
            //System.out.println("existRealmDomain " + true);
            deleteRealmDomainData( domx );
        }else{
            //System.out.println("existRealmDomain " + false);
        }
        //save to realm and get String OK or ERROR
        setRealmDomainData( domx );

        return Observable.just(domx);

    }

    public RealmDomain existRealmDomain(@NonNull final RealmDomain domx) {

        String dokx = domx.getDomain();
        return mRealm.where(RealmDomain.class).equalTo("domain", dokx).findFirst();
    }

    private void setRealmDomainData(@NonNull final RealmDomain domx) {

        mRealm.beginTransaction();
        mRealm.copyToRealm(domx);
        mRealm.commitTransaction();

    }

    private void deleteRealmDomainData(@NonNull final RealmDomain domx) {

        String dokx = domx.getDomain();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmDomain> result = realm.where(RealmDomain.class).equalTo("domain", dokx).findAll();
                result.clear();
            }
        });

    }


    //method for DomainsActivity
    @NonNull
    @Override
    public Observable<List<RealmDomain>> getDomainsFromRealm() {

        Log.d("DomainsViewModel dom ", "read Realm");
        List<RealmDomain> results = null;
        results = mRealm.where(RealmDomain.class).findAll();

        return Observable.just(results);
    }

    //set retrofit by runtime
    public void setRetrofit(String servername) {

        System.out.println("invxstring servername " + servername);
        String urlname = "http://" + servername;

        mInterceptor.setInterceptor(urlname);

    }
    //end set retrofit by runtime

}
