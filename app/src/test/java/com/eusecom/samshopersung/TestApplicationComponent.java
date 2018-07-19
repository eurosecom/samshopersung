package com.eusecom.samshopersung;

import android.app.Application;

import com.eusecom.samshopersung.di.AppComponent;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

@Component(
        modules = {
                AndroidInjectionModule.class,
                TestBindingModule.class,
                TestApplicationComponent.TestAppModule.class
        })
interface TestApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        TestApplicationComponent.Builder application(Application application);
        TestApplicationComponent build();
    }

    void inject(TestApplication app);

    @Module
    class TestAppModule {
    }
}