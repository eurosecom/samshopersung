package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.ImageUrl;
import com.eusecom.samshopersung.ProductDetail2Adapter;
import com.eusecom.samshopersung.ProductDetailActivity;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;

@Module
public class ProductDetailKtFragmentModule {

    @Provides
    public ProductDetail2Adapter providesProductDetail2Adapter(ProductDetailActivity activity,
                                                              RxBus rxbus, Picasso picasso, ImageUrl imageurl) {
        return new ProductDetail2Adapter(activity, rxbus, picasso, imageurl);
    }


}
