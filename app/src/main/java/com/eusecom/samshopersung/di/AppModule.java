package com.eusecom.samshopersung.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import com.eusecom.samshopersung.EventsStatesKt;
import com.eusecom.samshopersung.SamshopperApp;
import com.eusecom.samshopersung.models.IShopperModelsFactory;
import com.eusecom.samshopersung.models.ShopperModelsFactory;
import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.realm.IRealmController;
import com.eusecom.samshopersung.realm.RealmController;
import com.eusecom.samshopersung.retrofit.ExampleInterceptor;
import com.eusecom.samshopersung.retrofit.ShopperRetrofitService;
import com.eusecom.samshopersung.retrofit.ShopperXmlRetrofitService;
import com.eusecom.samshopersung.roomdatabase.MyDatabase;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.eusecom.samshopersung.soap.ISoapRequestFactory;
import com.eusecom.samshopersung.soap.SoapRequestFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import javax.inject.Named;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


/**
 * SamShopper AppModule
 */
@Module(subcomponents = {
        MainActivityComponent.class, MainShopperActivityComponent.class
        , ChooseCompanyActivityComponent.class, DomainsActivityComponent.class
        , OfferKtActivityComponent.class, MapActivityComponent.class
        , BasketKtActivityComponent.class})
public class AppModule {

    String mBaseUrl = "http:\\www.eshoptest.sk";

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    EventsStatesKt provideEventsStatesKt() {
        return new EventsStatesKt("",0, false,"", "","","");
    }

    @Provides
    @Singleton
    IShopperModelsFactory provideIShopperModelsFactory() {
        return new ShopperModelsFactory();
    }

    @Provides
    @Singleton
    RxBus providesRxBus(Application application) {
        return ((SamshopperApp) application).getRxBusSingleton();
    }

    @Provides
    @Singleton
    public Picasso providesPicasso(Context context){
        return new Picasso.Builder(context).
                build();
    }

    @Provides
    @Singleton
    IRealmController providesIRealmConroller(Application application) {
        return new RealmController(application);
    }

    @Provides
    @Singleton
    Realm providesRealm(IRealmController realmcontroller) {
        return realmcontroller.getRealm();
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

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptorLogging = new HttpLoggingInterceptor();
        interceptorLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptorLogging;
    }

    @Provides @Named("cached")
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, ExampleInterceptor interceptor, HttpLoggingInterceptor interceptorLogging) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(interceptorLogging)
                .cache(cache)
                .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    Strategy provideStrategy() {
        return new AnnotationStrategy();
    }

    @Provides
    @Singleton
    Serializer provideSerializer(Strategy strategy) {
        return new Persister(strategy);
    }

    @Provides @Named("non_cached")
    @Singleton
    OkHttpClient provideOkHttpClientNonCached(ExampleInterceptor interceptor, HttpLoggingInterceptor interceptorLogging) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(interceptorLogging)
                .build();
        return okHttpClient;
    }

    @Provides @Named("gson")
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

    @Provides @Named("simplexml")
    @Singleton
    Retrofit provideXmlRetrofit(@Named("cached") OkHttpClient okHttpClient, Serializer serializer) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public ShopperRetrofitService providesShopperRetrofitService(@Named("gson") Retrofit retrofit) {
        return retrofit.create(ShopperRetrofitService.class);
    }

    @Provides
    @Singleton
    public ShopperXmlRetrofitService providesShopperXmlRetrofitService(@Named("simplexml") Retrofit retrofit) {
        return retrofit.create(ShopperXmlRetrofitService.class);
    }

    @Provides
    @Singleton
    public ISchedulerProvider providesISchedulerProvider(Application application) {

        return ((SamshopperApp) application).getSchedulerProvider();
    }


    @Provides
    @Singleton
    public DatabaseReference providesDatabaseReference(Application application) {

        return ((SamshopperApp) application).getDatabaseFirebaseReference();
    }

    @Provides
    @Singleton
    public Resources providesResources(Application application) {

        return ((SamshopperApp) application).getResources();
    }

    @Provides
    @Singleton
    public MyDatabase providesMyDatabase(Application application) {
        return ((SamshopperApp) application).get().getDB();
    }

    @Provides
    @Singleton
    public ISoapRequestFactory provideSoapRequestFactory() {
        return new SoapRequestFactory();
    }



}
