package com.eusecom.samshopersung;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shaon on 12/3/2016.
 */

public class SetImageApiClient {
    public static final String BASE_URL = "http://www.eshoptest.sk";
    private static Retrofit retrofit = null;
    public static int unique_id;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}