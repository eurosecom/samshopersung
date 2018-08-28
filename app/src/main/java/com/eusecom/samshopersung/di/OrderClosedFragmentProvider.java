package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.OrderClosedFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OrderClosedFragmentProvider {

    @ContributesAndroidInjector(modules = OrderFragmentModule.class)
    abstract OrderClosedFragment provideOrderClosedFragmentFactory();
}
