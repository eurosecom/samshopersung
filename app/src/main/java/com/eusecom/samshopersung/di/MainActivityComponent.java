package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.MainActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent extends AndroidInjector<MainActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{}
}
