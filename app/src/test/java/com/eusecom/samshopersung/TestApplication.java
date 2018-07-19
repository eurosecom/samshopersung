package com.eusecom.samshopersung;

import android.app.Activity;
import android.app.Application;
import javax.inject.Inject;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class TestApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("HEY IM UP TESTING APPLICATION COMPONENT ==========================+>");
        DaggerTestAppComponent.builder()    //name by the TestAppComponent.java
                .application(this)          //name by the TestAppComponent.java builder TestAppComponent.Builder application(Application application);
                .build()
                .inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
