package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.ProductDetail2Adapter;
import com.eusecom.samshopersung.ProductDetailActivity;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;

@Module
public class ProductDetailKtFragmentModule {

    @Provides
    public ProductDetail2Adapter providesProductDetailAdapter(ProductDetailActivity activity,
                                                              RxBus rxbus, Picasso picasso) {
        return new ProductDetail2Adapter(activity, rxbus, picasso);
    }


}
