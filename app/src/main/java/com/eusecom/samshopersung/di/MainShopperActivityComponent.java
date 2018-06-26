package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.MainShopperActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@ShopperScope
@Subcomponent(modules = MainShopperActivityModule.class)
public interface MainShopperActivityComponent extends AndroidInjector<MainShopperActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainShopperActivity>{}
}
