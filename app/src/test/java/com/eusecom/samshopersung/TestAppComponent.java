package com.eusecom.samshopersung;

import android.app.Application;

import com.eusecom.samshopersung.di.AppModule;
import com.eusecom.samshopersung.di.TestBindingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

// !!! Some problem with to move TestAppModule and TestAppComponent to /di folder

@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                TestBindingModule.class,
                AppModule.class //!!! To get AppModule.java from real build main folder
        })
interface TestAppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        TestAppComponent.Builder application(Application application);
        TestAppComponent build();
    }

    void inject(TestApplication app);


}