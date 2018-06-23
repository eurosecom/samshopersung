package com.eusecom.samfantozzi;

import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static java.lang.String.format;

public class RfGithubService {

    private RfGithubService() {
    }

    public static RfGithubApi createGithubService(final String githubToken, final String githubToken2) {
        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
              .addConverterFactory(GsonConverterFactory.create())
              .baseUrl("https://api.github.com");

        if (!TextUtils.isEmpty(githubToken)) {

            HttpLoggingInterceptor interceptorLogging = new HttpLoggingInterceptor();
            interceptorLogging.setLevel(HttpLoggingInterceptor.Level.BODY);

            RfBasicAuthInterceptor interceptorAuth = new RfBasicAuthInterceptor(githubToken, githubToken2);
            RfTokenAuthenticator tokenAuthenticator = new RfTokenAuthenticator(githubToken, githubToken2);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptorLogging)
                    .addInterceptor(interceptorAuth)
                    //.authenticator(tokenAuthenticator) //response 403 Forbidden problem
                    .build();

            builder.client(client);
        }

        return builder.build().create(RfGithubApi.class);
    }
}
