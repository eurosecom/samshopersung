package com.eusecom.samshopersung.models;

import android.support.annotation.NonNull;
import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.Invoice;
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
     * @return int This returns new ProductKt POJO.
     */
    @NonNull
    public ProductKt getProductKt(){

        return new ProductKt("","","","","","","","","",""
                ,"", "", "");
    }

    /**
     * This method is used to get BasketKt POJO
     * @return int This returns new BasketKt POJO.
     */
    @NonNull
    public BasketKt getBasketKt(){

        return new BasketKt("","","","","","","","",""
                ,"","","","","", "");
    }


    /**
     * This method is used to get SumBasketKt POJO
     * @return int This returns new SumBasketKt POJO.
     * @see SumBasketKt
     */
    @NonNull
    public SumBasketKt getSumBasketKt(){

        return new SumBasketKt("","","", Collections.emptyList(),"","","", "");
    }

    /**
     * This method is used to get Invoice POJO
     * @return int This returns new Invoice POJO.
     * @see Invoice
     */
    @NonNull
    public Invoice getInvoice(){

        return new Invoice("", "","","","","","","","","","","",""
                ,"","","","","","",false,0l,"","","","");
    }

}
