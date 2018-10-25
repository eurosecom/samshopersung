package com.eusecom.samshopersung.di;

import android.app.Activity;
import com.eusecom.samshopersung.AccountReportsActivity;
import com.eusecom.samshopersung.BasketKtActivity;
import com.eusecom.samshopersung.ChooseCompanyActivity;
import com.eusecom.samshopersung.DomainsViewModelActivity;
import com.eusecom.samshopersung.MainShopperActivity;
import com.eusecom.samshopersung.MapActivity;
import com.eusecom.samshopersung.NewIdcActivity;
import com.eusecom.samshopersung.OfferKtActivity;
import com.eusecom.samshopersung.OrderDetailActivity;
import com.eusecom.samshopersung.OrderListActivity;
import com.eusecom.samshopersung.OrpListActivity;
import com.eusecom.samshopersung.OrpListKtActivity;
import com.eusecom.samshopersung.OrpRequestsActivity;
import com.eusecom.samshopersung.OrpSettingsActivity;
import com.eusecom.samshopersung.ProductDetailActivity;
import com.eusecom.samshopersung.SetImageActivity;
import com.eusecom.samshopersung.SetProductActivity;
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
            , OrderFragmentProvider.class, OrderClosedFragmentProvider.class})
    abstract OrderListActivity bindOrderListActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {ProductDetailActivityModule.class, ProductDetailFragmentProvider.class
            , ProductDetailKtFragmentProvider.class})
    abstract ProductDetailActivity bindProductDetailActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {NewIdcActivityModule.class})
    abstract NewIdcActivity bindNewIdcActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {NewIdcActivityModule.class})
    abstract OrderDetailActivity bindOrderDetailActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {SetImageActivityModule.class})
    abstract SetImageActivity bindSetImageActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {SetImageActivityModule.class})
    abstract SetProductActivity bindSetProductActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {SetImageActivityModule.class})
    abstract AccountReportsActivity bindAccountReportsActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {OrderListActivityModule.class, OrpFragmentProvider.class})
    abstract OrpListActivity bindOrpListActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {OrderListActivityModule.class, OrpFragmentProvider.class})
    abstract OrpListKtActivity bindOrpListKtActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {OrpRequestsActivityModule.class})
    abstract OrpRequestsActivity bindOrpRequestsActivity();

    @ShopperScope
    @ContributesAndroidInjector(modules = {OrpSettingsActivityModule.class})
    abstract OrpSettingsActivity bindOrpSettingsActivity();


}
