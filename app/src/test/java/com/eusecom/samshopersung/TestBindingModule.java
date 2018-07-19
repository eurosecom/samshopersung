package com.eusecom.samshopersung;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
        TestFlombulatorActivityComponent.class
})
public abstract class TestBindingModule {
    @Binds
    @IntoMap
    @ActivityKey(FlombulatorActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindFlombulatorActivityInjectorFactory(TestFlombulatorActivityComponent.Builder builder);


}