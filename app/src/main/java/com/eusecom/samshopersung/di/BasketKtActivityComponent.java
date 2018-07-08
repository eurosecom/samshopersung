package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.BasketKtActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@ShopperScope
@Subcomponent(modules = MainShopperActivityModule.class)
public interface BasketKtActivityComponent extends AndroidInjector<BasketKtActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BasketKtActivity>{}
}
