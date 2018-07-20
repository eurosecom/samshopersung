package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.FlombulatorActivity;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = {
        TestFlombulatorModule.class
})
public interface TestFlombulatorActivityComponent extends AndroidInjector<FlombulatorActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<FlombulatorActivity> {}
}
