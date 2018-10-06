package com.eusecom.samshopersung.di;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.eusecom.samshopersung.retrofit.ShopperXmlRetrofitService;
import com.eusecom.samshopersung.soap.soappayment.PaymentTerminal;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderListActivityModule {

    @Provides
    @ShopperScope
    public ShopperIDataModel providesShopperIDataModel(ShopperRetrofitService shopperretrofitservice,
                                                       ExampleInterceptor interceptor,
                                                       ShopperXmlRetrofitService shopperxmlretrofitservice) {
        return new ShopperDataModel(shopperretrofitservice
                , interceptor, shopperxmlretrofitservice);
    }


    @Provides
    @ShopperScope
    public ShopperIMvvmViewModel providesShopperIMvvmViewModel(ShopperIDataModel dataModel
            , ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences
            , ConnectivityManager connectivityManager
            , PaymentTerminal paymentTerminal) {
        return new ShopperMvvmViewModel(dataModel, schedulerProvider
                , sharedPreferences, connectivityManager, paymentTerminal);
    }

}
