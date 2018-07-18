package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.OrderFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by mertsimsek on 02/06/2017.
 */
@Module
public abstract class OrderFragmentProvider {

    @ShopperScope
    @ContributesAndroidInjector(modules = OrderFragmentModule.class)
    abstract OrderFragment provideOrderFragmentFactory();
}
