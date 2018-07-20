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

import org.mockito.Mockito;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

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


}
