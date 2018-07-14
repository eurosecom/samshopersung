package com.eusecom.samshopersung

/**
 * SumBasketKt POJO model.
 *
 * @author  eurosecom
 * @version 1.0
 * @since   2018-07-11
 *
 * @param smno amount of items in basket
 * @param shdb total sum values of items without VAT in basket
 * @param shdd total sum values of items with VAT in basket
 * @param basketitems products items in basket {@link  com.eusecom.samshopersung.realm.RealmDomain}
 * @param sprm1 parameter 1 of basket
 * @param sprm2 parameter 2 of basket
 * @param sprm3 parameter 3 of basket
 * @param sprm4 parameter 4 of basket
 */
data class SumBasketKt(var smno : String, var shdb: String, var shdd: String
                     , var basketitems: List<BasketKt>
                     , var sprm1: String, var sprm2: String, var sprm3: String, var sprm4: String)
