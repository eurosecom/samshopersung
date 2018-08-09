package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.ProductDetailKtFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Created by mertsimsek on 02/06/2017.
 */
@Module
public abstract class ProductDetailKtFragmentProvider {

    @ContributesAndroidInjector(modules = ProductDetailKtFragmentModule.class)

    abstract ProductDetailKtFragment provideProductDetailKtFragmentFactory();
}
