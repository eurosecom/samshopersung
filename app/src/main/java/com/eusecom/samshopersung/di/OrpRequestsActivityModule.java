package com.eusecom.samshopersung.di;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import com.eusecom.samshopersung.OrpRequestsAdapter;
import com.eusecom.samshopersung.ShopperIMvvmViewModel;
import com.eusecom.samshopersung.ShopperMvvmViewModel;
import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperDataModel;
import com.eusecom.samshopersung.mvvmdatamodel.ShopperIDataModel;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.eusecom.samshopersung.roomdatabase.MyDatabase;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;

@Module
public class OrpRequestsActivityModule {


    @Provides
    @ShopperScope
    public ShopperIDataModel providesShopperIDataModel(ShopperRetrofitService shopperretrofitservice,
                                                       ExampleInterceptor interceptor,
                                                       MyDatabase roomDatabase,
                                                       IShopperModelsFactory modelsFactory) {
        return new ShopperDataModel(shopperretrofitservice, interceptor, roomDatabase, modelsFactory);
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
    public OrpRequestsAdapter providesOrpRequestsAdapter(@NonNull final RxBus rxbus, @NonNull final Picasso picasso) {
        return new OrpRequestsAdapter(rxbus, picasso);
    }

}
