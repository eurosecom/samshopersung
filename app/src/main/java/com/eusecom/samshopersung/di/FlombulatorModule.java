package com.eusecom.samshopersung.di;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;

import com.eusecom.samshopersung.Flombulator;
import com.eusecom.samshopersung.FlombulatorI;
import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.realm.IRealmController;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.google.firebase.database.DatabaseReference;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class FlombulatorModule {

    @Provides
    FlombulatorI provideFlombulatorI() {
        System.out.println("Flombulated real implementation of FlombulatorModule");
        return new Flombulator();
    }

    @Provides
    @ShopperScope
    public ShopperIDataModel providesShopperIDataModel(DatabaseReference databasereference,
                                                       ShopperRetrofitService shopperretrofitservice,
                                                       Resources resources, Realm realm,
                                                       ExampleInterceptor interceptor,
                                                       IRealmController realmcontroller) {
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
