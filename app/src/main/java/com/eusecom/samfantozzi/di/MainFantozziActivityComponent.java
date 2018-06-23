package com.eusecom.samfantozzi.di;

import com.eusecom.samfantozzi.MainFantozziActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by mertsimsek on 25/05/2017.
 */
@Subcomponent(modules = MainFantozziActivityModule.class)
public interface MainFantozziActivityComponent extends AndroidInjector<MainFantozziActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainFantozziActivity>{}
}
