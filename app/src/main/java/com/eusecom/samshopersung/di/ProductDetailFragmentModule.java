package com.eusecom.samshopersung.di;

import com.eusecom.samshopersung.ImageUrl;
import com.eusecom.samshopersung.ProductDetailActivity;
import com.eusecom.samshopersung.ProductDetailAdapter;
import com.eusecom.samshopersung.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;

@Module
public class ProductDetailFragmentModule {

    @Provides
    public ProductDetailAdapter providesProductDetailAdapter(ProductDetailActivity activity,
                  RxBus rxbus, Picasso picasso, ImageUrl imageurl) {
        return new ProductDetailAdapter(activity, rxbus, picasso, imageurl);
    }

}
