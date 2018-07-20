package com.eusecom.samshopersung;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;

import com.eusecom.samshopersung.mvvmschedulers.ISchedulerProvider;
import com.eusecom.samshopersung.mvvmschedulers.ImmediateSchedulerProvider;
import com.eusecom.samshopersung.mvvmschedulers.SchedulerProvider;

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

    @NonNull
    public ISchedulerProvider getSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
