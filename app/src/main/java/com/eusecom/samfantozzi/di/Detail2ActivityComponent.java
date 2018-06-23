package com.eusecom.samfantozzi.di;

import com.eusecom.samfantozzi.Detail2Activity;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;


@Subcomponent(modules = DetailActivityModule.class)
public interface Detail2ActivityComponent extends AndroidInjector<Detail2Activity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<Detail2Activity>{}
}
