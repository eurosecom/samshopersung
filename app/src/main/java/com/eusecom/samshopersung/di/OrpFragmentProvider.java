package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.OrpFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Created by mertsimsek on 02/06/2017.
 */
@Module
public abstract class OrpFragmentProvider {

    @ContributesAndroidInjector(modules = OrderFragmentModule.class)
    abstract OrpFragment provideOrpFragmentFactory();
}
