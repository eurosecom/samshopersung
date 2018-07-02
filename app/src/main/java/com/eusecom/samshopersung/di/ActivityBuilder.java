package com.eusecom.samshopersung.di;

import android.app.Activity;

import com.eusecom.samshopersung.ChooseCompanyActivity;
import com.eusecom.samshopersung.DomainsViewModelActivity;
import com.eusecom.samshopersung.MainActivity;
import com.eusecom.samshopersung.MainShopperActivity;
import com.eusecom.samshopersung.OfferKtActivity;
import com.eusecom.samshopersung.di.MainActivityComponent;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class ActivityBuilder {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(MainShopperActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainShopperActivity(MainShopperActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(ChooseCompanyActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindChooseCompanyActivity(ChooseCompanyActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(DomainsViewModelActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDomainsActivity(DomainsActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(OfferKtActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindOfferActivity(OfferKtActivityComponent.Builder builder);
}
