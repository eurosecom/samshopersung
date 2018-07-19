package com.eusecom.samshopersung;

import android.app.Application;

import com.eusecom.samshopersung.di.TestBindingModule;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.android.AndroidInjectionModule;

@Component(
        modules = {
                AndroidInjectionModule.class,
                TestBindingModule.class,
                TestAppModule.class
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