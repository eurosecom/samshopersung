package com.eusecom.samshopersung.di;

import android.app.Activity;

import com.eusecom.samshopersung.BasketKtActivity;
import com.eusecom.samshopersung.ChooseCompanyActivity;
import com.eusecom.samshopersung.DetailActivity;
import com.eusecom.samshopersung.DomainsViewModelActivity;
import com.eusecom.samshopersung.FlombulatorActivity;
import com.eusecom.samshopersung.MainActivity;
import com.eusecom.samshopersung.MainShopperActivity;
import com.eusecom.samshopersung.MapActivity;
import com.eusecom.samshopersung.OfferKtActivity;
import com.eusecom.samshopersung.OrderListActivity;
import com.eusecom.samshopersung.ProductDetailActivity;
import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
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
    abstract AndroidInjector.Factory<? extends Activity> bindOfferKtActivity(OfferKtActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(MapActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMapActivity(MapActivityComponent.Builder builder);

    /**
     * Old dagger2 2.11 annotation.
     */
    @Binds
    @IntoMap
    @ActivityKey(BasketKtActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindBasketKtActivity(BasketKtActivityComponent.Builder builder);

    /**
     * New dagger2 2.11 annotation. @ContributesAndroidInjector
     */
    @ShopperScope
    @ContributesAndroidInjector(modules = {OrderListActivityModule.class, InvoiceFragmentProvider.class
            , OrderFragmentProvider.class})
    abstract OrderListActivity bindOrderListActivity();

    @ContributesAndroidInjector(modules = {OrderListActivityModule.class, DetailFragmentProvider.class})
    abstract DetailActivity bindDetailActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {FlombulatorModule.class })
    abstract FlombulatorActivity bindFlombulatorActivity();

    @ContributesAndroidInjector(modules = {ProductDetailActivityModule.class, ProductDetailFragmentProvider.class})
    abstract ProductDetailActivity bindProductDetailActivity();

}
