package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.OrderListKtFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Created by mertsimsek on 02/06/2017.
 */
@Module
public abstract class OrderListKtFragmentProvider {

    @ContributesAndroidInjector(modules = OrderListKtFragmentModule.class)
    abstract OrderListKtFragment provideOrderListKtFragmentFactory();
}
