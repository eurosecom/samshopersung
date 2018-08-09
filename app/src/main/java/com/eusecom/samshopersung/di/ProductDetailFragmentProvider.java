package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.ProductDetailFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ProductDetailFragmentProvider {

    @ContributesAndroidInjector(modules = ProductDetailFragmentModule.class)

    abstract ProductDetailFragment provideProductDetailFragmentFactory();
}
