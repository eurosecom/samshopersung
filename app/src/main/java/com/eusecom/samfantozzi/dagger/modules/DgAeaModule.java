package com.eusecom.samfantozzi.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;

import com.eusecom.samfantozzi.DgAllEmpsAbsMvvmViewModel;
import com.eusecom.samfantozzi.MCrypt;
import com.eusecom.samfantozzi.SamfantozziApp;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsDataModel;
import com.eusecom.samfantozzi.mvvmdatamodel.DgAllEmpsAbsIDataModel;
import com.eusecom.samfantozzi.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samfantozzi.retrofit.AbsServerService;
import com.eusecom.samfantozzi.retrofit.ExampleInterceptor;
import com.google.firebase.database.DatabaseReference;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

import io.realm.Realm;
import okhttp3.Cache;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = {ApplicationModule.class} )
public class DgAeaModule {

    String mBaseUrl = "";

    public DgAeaModule(String url){
        this.mBaseUrl=url;
    }

    @Provides
    @Singleton
    ExampleInterceptor provideInterceptor() { // This is where the Interceptor object is constructed
        return ExampleInterceptor.get();
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides @Named("cached")
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, ExampleInterceptor interceptor) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache)
                .build();
        return okHttpClient;
    }

    @Provides @Named("non_cached")
    @Singleton
    OkHttpClient provideOkHttpClientNonCached(ExampleInterceptor interceptor) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, @Named("cached") OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public AbsServerService providesAbsServerService(Retrofit retrofit) {
        return retrofit.create(AbsServerService.class);
    }


    @Provides
    @Singleton
    public DatabaseReference providesDatabaseReference(Application application) {

        return ((SamfantozziApp) application).getDatabaseFirebaseReference();
    }

    @Provides
    @Singleton
    public Resources providesResources(Application application) {

        return ((SamfantozziApp) application).getResources();
    }

    @Provides
    @Singleton
    public MCrypt providesMCrypt() {

        return new MCrypt();
    }

    @Provides
    @Singleton
    public DgAllEmpsAbsIDataModel providesDgAllEmpsAbsIDataModel(DatabaseReference databasereference,
                                                                 AbsServerService absServerService,
                                                                 Resources resources, Realm realm,
                                                                 ExampleInterceptor interceptor) {
        return new DgAllEmpsAbsDataModel(databasereference, absServerService
                , resources, realm, interceptor);
    }



    @Provides
    @Singleton
    public ISchedulerProvider providesISchedulerProvider(Application application) {

        return ((SamfantozziApp) application).getSchedulerProvider();
    }

    @Provides
    @Singleton
    public DgAllEmpsAbsMvvmViewModel providesDgAllEmpsAbsMvvmViewModel(DgAllEmpsAbsIDataModel dataModel
            , ISchedulerProvider schedulerProvider,SharedPreferences sharedPreferences, MCrypt mcrypt
            ,ConnectivityManager connectivityManager) {
        return new DgAllEmpsAbsMvvmViewModel(dataModel, schedulerProvider
                , sharedPreferences, mcrypt, connectivityManager);
    }



}