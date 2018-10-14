package com.eusecom.samshopersung.di;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.eusecom.samshopersung.retrofit.ShopperXmlRetrofitService;
import com.eusecom.samshopersung.roomdatabase.MyDatabase;
import com.eusecom.samshopersung.soap.soappayment.PaymentStrategy;
import com.eusecom.samshopersung.soap.soappayment.PaymentTerminal;
import dagger.Module;
import dagger.Provides;

@Module
public class OrderListActivityModule {

    @Provides
    @ShopperScope
    public ShopperIDataModel providesShopperIDataModel(ShopperRetrofitService shopperretrofitservice,
                                                       ExampleInterceptor interceptor,
                                                       @NonNull MyDatabase roomDatabase,
                                                       ShopperXmlRetrofitService shopperxmlretrofitservice,
                                                       IShopperModelsFactory modelsFactory) {
        return new ShopperDataModel(shopperretrofitservice
                , interceptor, roomDatabase, shopperxmlretrofitservice, modelsFactory);
    }


    @Provides
    @ShopperScope
    public ShopperIMvvmViewModel providesShopperIMvvmViewModel(ShopperIDataModel dataModel
            , ISchedulerProvider schedulerProvider, SharedPreferences sharedPreferences
            , ConnectivityManager connectivityManager
            , PaymentTerminal paymentTerminal, PaymentStrategy ekassastrategy) {
        return new ShopperMvvmViewModel(dataModel, schedulerProvider
                , sharedPreferences, connectivityManager, paymentTerminal, ekassastrategy);
    }

}
