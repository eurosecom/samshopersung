package com.eusecom.samshopersung.models;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.SumBasketKt;

/**
 * The Interface Factory for Shopper POJO models.
 *
 * @author  eurosecom
 * @version 1.0
 * @since   2018-07-12
 */
public interface IShopperModelsFactory {

    @NonNull
    ProductKt getProductKt();

    @NonNull
    BasketKt getBasketKt();

    @NonNull
    SumBasketKt getSumBasketKt();


}
