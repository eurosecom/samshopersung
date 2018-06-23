package com.eusecom.samshopersung;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.eusecom.samshopersung.rxbus.RxBus;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SamshopperApp extends MultiDexApplication implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    public RxBus _rxBus;

    @NonNull
    private DatabaseReference mDatabaseReference;
    private Realm mRealm;
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("SamshopperApp", " onCreate");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @NonNull
    public Realm getRealm() {
        return Realm.getDefaultInstance();
    }


    @NonNull
    public DatabaseReference getDatabaseFirebaseReference() {
        return mDatabaseReference;
    }


    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }
        return _rxBus;
    }




}
