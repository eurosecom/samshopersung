package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.FlombulatorActivity;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@ShopperScope
@Subcomponent(modules = {
        FlombulatorModule.class
})
public interface FlumboratorComponent extends AndroidInjector<FlombulatorActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<FlombulatorActivity> {}
}
