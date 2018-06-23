package com.eusecom.samshopersung.di;

import android.app.Activity;

import com.eusecom.samshopersung.MainActivity;
import com.eusecom.samshopersung.di.MainActivityComponent;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ActivityBuilder {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);


}
