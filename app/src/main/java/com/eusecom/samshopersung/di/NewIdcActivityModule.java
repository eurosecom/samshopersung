package com.eusecom.samshopersung.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.realm.IRealmController;
import com.eusecom.samshopersung.realm.IdcController;
import com.eusecom.samshopersung.realm.RealmController;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class NewIdcActivityModule {

    @Provides
    @ShopperScope
    IdcController providesIdcConroller(Application application) {
        return new RealmController(application);
    }

    @Provides
    @ShopperScope
    public ShopperIDataModel providesShopperIDataModel(DatabaseReference databasereference,
                                                       ShopperRetrofitService shopperretrofitservice,
                                                       Resources resources, Realm realm,
                                                       ExampleInterceptor interceptor,
                                                       IdcController realmcontroller) {
        return new ShopperDataModel(databasereference, shopperretrofitservice
                , resources, realm, interceptor, realmcontroller);
    }


    @Provides
    @ShopperScope
    public ShopperIMvvmViewModel providesShopperIMvvmViewModel(ShopperIDataModel dataModel
            , ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences
            , ConnectivityManager connectivityManager) {
        return new ShopperMvvmViewModel(dataModel, schedulerProvider
                , sharedPreferences, connectivityManager);
    }

}
