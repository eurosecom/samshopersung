package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.MapActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@ShopperScope
@Subcomponent(modules = MainShopperActivityModule.class)
public interface MapActivityComponent extends AndroidInjector<MapActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MapActivity>{}
}
