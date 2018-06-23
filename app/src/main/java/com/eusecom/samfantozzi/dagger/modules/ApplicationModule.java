package com.eusecom.samfantozzi.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import com.eusecom.samfantozzi.SamfantozziApp;
import com.eusecom.samfantozzi.realm.RealmController;
import com.eusecom.samfantozzi.rxbus.RxBus;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;


@Module
public class ApplicationModule {

    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    Context provideContext(){
        return mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    RxBus providesRxBus(Application application) {
        return ((SamfantozziApp) application).getRxBusSingleton();
    }

    @Provides
    @Singleton
    RealmController providesRealmConroller(Application application) {
        return new RealmController(application);
    }

    @Provides
    @Singleton
    Realm providesRealm(RealmController realmcontroller) {
        return realmcontroller.getRealm();
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager() {
        return (ConnectivityManager) mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
    }



}
