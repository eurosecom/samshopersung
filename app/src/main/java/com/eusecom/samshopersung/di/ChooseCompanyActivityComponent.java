package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.ChooseCompanyActivity;
import javax.inject.Singleton;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@ShopperScope
@Subcomponent(modules = MainShopperActivityModule.class)
public interface ChooseCompanyActivityComponent extends AndroidInjector<ChooseCompanyActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<ChooseCompanyActivity>{}
}
