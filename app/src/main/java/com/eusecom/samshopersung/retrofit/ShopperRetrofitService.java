package com.eusecom.samshopersung.retrofit;


import com.eusecom.samshopersung.CompanyKt;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ShopperRetrofitService {

    //recyclerview method for ChooseCompanyActivity
    @GET("/androidfantozzi/get_all_firmy.php")
    Observable<List<CompanyKt>> getCompaniesFromServer(@Query("userhash") String userhash, @Query("userid") String userid);

}
