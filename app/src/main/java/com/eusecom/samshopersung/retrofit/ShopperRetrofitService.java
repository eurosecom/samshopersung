package com.eusecom.samshopersung.retrofit;


import com.eusecom.samshopersung.BasketKt;
import com.eusecom.samshopersung.CategoryKt;
import com.eusecom.samshopersung.CompanyKt;
import com.eusecom.samshopersung.IdCompanyKt;
import com.eusecom.samshopersung.Invoice;
import com.eusecom.samshopersung.ProductKt;
import com.eusecom.samshopersung.SetImageServerResponse;
import com.eusecom.samshopersung.SumBasketKt;
import com.eusecom.samshopersung.models.InvoiceList;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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
    Observable<InvoiceList> getOrdersFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("ume") String ume, @Query("dokx") String dokx, @Query("ustp") String ustp);

    //methods for NewIdcActivity
    @GET("/androidfantozzi/control_idcompany.php")
    Observable<List<IdCompanyKt>> controlIdCompanyOnSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh, @Query("queryx") String queryx);

    //methods for InvoiceFragment
    @GET("/androidshopper/get_invoices.php")
    Observable<List<Invoice>> getInvoicesFromSqlServer(@Query("userhash") String userhash
            , @Query("userid") String userid, @Query("fromfir") String fromfir
            , @Query("vyb_rok") String vyb_rok, @Query("drh") String drh
            , @Query("uce") String uce, @Query("ume") String ume, @Query("dokx") String dokx, @Query("ustp") String ustp);

    //setimageactivity
    @Multipart
    @POST("/androidshopper/uploadimage.php")
    Observable<SetImageServerResponse> uploadImageFile(@Part MultipartBody.Part file, @Part("desc") RequestBody desc);

    @Multipart
    @POST("/androidshopper/uploadimage.php")
    Observable<SetImageServerResponse> uploadImageFileWithMap(@Part MultipartBody.Part file,
                                                             @PartMap() Map<String, RequestBody> partMap);

}
