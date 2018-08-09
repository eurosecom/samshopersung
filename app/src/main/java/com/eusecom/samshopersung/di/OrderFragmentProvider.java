package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.OrderFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OrderFragmentProvider {

    @ContributesAndroidInjector(modules = OrderFragmentModule.class)
    abstract OrderFragment provideOrderFragmentFactory();
}
