package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.DomainsViewModelActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@ShopperScope
@Subcomponent(modules = DomainsActivityModule.class)
public interface DomainsActivityComponent extends AndroidInjector<DomainsViewModelActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DomainsViewModelActivity>{}
}
