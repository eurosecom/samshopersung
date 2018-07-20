package com.eusecom.samshopersung.di;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.eusecom.samshopersung.Flombulator;
import com.eusecom.samshopersung.FlombulatorI;
import com.eusecom.samshopersung.SamshopperApp;
import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.TestApplication;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.mvvmschedulers.ImmediateSchedulerProvider;
import com.eusecom.samshopersung.realm.IRealmController;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.google.firebase.database.DatabaseReference;

import org.mockito.Mockito;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.internal.schedulers.ImmediateScheduler;

import static org.mockito.Mockito.when;

@Module
public class TestFlombulatorModule {

    @Provides
    FlombulatorI provideTestFlombulatorI() {
        System.out.println("I am the mocked flombulator");
        FlombulatorI flum = Mockito.mock(Flombulator.class);
        when(flum.flombulateMe()).thenReturn("flombulated test");
        return flum;
    }

    @Provides
    public ShopperIDataModel providesShopperIDataModel() {

        System.out.println("I am the mocked datamodel");
        ShopperIDataModel datamodel = Mockito.mock(ShopperDataModel.class);
        when(datamodel.getStringFromDataModel()).thenReturn("Mocked String from DataModel");
        return datamodel;

    }

    @Provides @Named("testing")
    public ISchedulerProvider providesISchedulerProvider(Application application) {

        return ((TestApplication) application).getSchedulerProvider();
    }

    @Provides
    public ShopperIMvvmViewModel providesShopperIMvvmViewModel(ShopperIDataModel dataModel
            , @Named("testing") ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences
            , ConnectivityManager connectivityManager) {
        return new ShopperMvvmViewModel(dataModel, schedulerProvider
                , sharedPreferences, connectivityManager);
    }


}
