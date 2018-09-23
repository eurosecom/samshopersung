package com.eusecom.samshopersung.soap.soaphello;

import android.text.TextUtils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class HelloService {

    private HelloService() {
    }

    public static HelloApi createHelloService(final String githubToken, final String githubToken2) {

        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);

        Retrofit.Builder builder = new Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.wsdl2code.com/");

        if (!TextUtils.isEmpty(githubToken)) {

            HttpLoggingInterceptor interceptorLogging = new HttpLoggingInterceptor();
            interceptorLogging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptorLogging)
                    .build();

            builder.client(client);
        }

        return builder.build().create(HelloApi.class);
    }
}
