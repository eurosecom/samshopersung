package com.eusecom.samshopersung.di;

import android.app.Application;
import com.eusecom.samshopersung.SamshopperApp;
import javax.inject.Singleton;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;


/**
 * SamShopper AppComponent
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        AppComponent build();
    }

    void inject(SamshopperApp app);
}
