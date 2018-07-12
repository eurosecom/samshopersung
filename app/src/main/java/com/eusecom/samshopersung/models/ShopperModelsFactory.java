package com.eusecom.samshopersung.models;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.SumBasketKt;
import java.util.Collections;

/**
 * The Implementation of Factory for Shopper POJO models.
 *
 * @author  eurosecom
 * @version 1.0
 * @since   2018-07-12
 */
public class ShopperModelsFactory implements IShopperModelsFactory {

    /**
     * This method is used to get ProductKt POJO
     * @param --numA This is the first paramter to addNum method
     * @param --numB  This is the second parameter to addNum method
     * @return int This returns sum of numA and numB.
     */
    @NonNull
    public ProductKt getProductKt(){

        return new ProductKt("","","","","","","","","","","", "");
    }

    /**
     * This method is used to get BasketKt POJO
     * @param --args Unused.
     * @return BasketKt POJO.
     * @exception --IOException On input error.
     * @see --IOException
     */
    @NonNull
    public BasketKt getBasketKt(){

        return new BasketKt("","","","","","","","",""
                ,"","","","","", "");
    }

    /**
     * This method is used to get SumBasketKt POJO
     * @param --args Unused.
     * @return SumBasketKt POJO.
     * @exception --IOException On input error.
     * @see --IOException
     */
    @NonNull
    public SumBasketKt getSumBasketKt(){

        return new SumBasketKt("","","", Collections.emptyList(),"","","", "");
    }


}
