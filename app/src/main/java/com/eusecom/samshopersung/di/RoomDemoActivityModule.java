package com.eusecom.samshopersung.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.eusecom.samshopersung.RoomDemoAdapter;
import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.realm.IRealmController;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class RoomDemoActivityModule {


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

    @Provides
    @ShopperScope
    public RoomDemoAdapter providesRoomDemoAdapter(@NonNull final RxBus rxbus, @NonNull final Picasso picasso) {
        return new RoomDemoAdapter(rxbus, picasso);
    }

}
