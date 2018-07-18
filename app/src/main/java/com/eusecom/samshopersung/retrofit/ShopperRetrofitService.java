package com.eusecom.samshopersung.retrofit;


import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.CategoryKt;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.SumBasketKt;
import com.eusecom.samshopersung.models.InvoiceList;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ShopperRetrofitService {

    //method for ChooseCompanyActivity
    @GET("/androidfantozzi/get_all_firmy.php")
    Observable<List<CompanyKt>> getCompaniesFromServer(@Query("userhash") String userhash, @Query("userid") String userid);

    //methods for OfferKtActivity
    @GET("/androidshopper/get_products.php")
    Observable<List<ProductKt>> getProductsFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("ume") String ume, @Query("dokx") String dokx);

    @GET("/androidshopper/get_categories.php")
    Observable<List<CategoryKt>> getCatFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("ume") String ume, @Query("dokx") String dokx);

    //methods for BasketKtActivity
    @GET("/androidshopper/get_basket.php")
    Observable<List<BasketKt>> getBasketFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("prodx") String prodx, @Query("dokx") String dokx);

    @GET("/androidshopper/get_sumbasket.php")
    Observable<SumBasketKt> getSumBasketFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("prodx") String prodx, @Query("dokx") String dokx);

    //methods for OrderListActivity
    @GET("/androidshopper/get_orders.php")
    Observable<InvoiceList> getCashDocsFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("ume") String ume, @Query("dokx") String dokx);

}
