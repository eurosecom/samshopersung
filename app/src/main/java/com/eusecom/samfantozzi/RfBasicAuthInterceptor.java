package com.eusecom.samfantozzi;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.String.format;

public class RfBasicAuthInterceptor implements Interceptor {

    private String githubToken, githubToken2;

    public RfBasicAuthInterceptor(String token, String token2) {
        this.githubToken = token;
        this.githubToken2 = token2;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
            .header("Authorization", format("token %s", githubToken)).build();

        Response response = chain.proceed(authenticatedRequest);

        boolean unauthorized = response.code() == 401;
        if (unauthorized) {

            Request modifiedRequest = request.newBuilder()
                    .header("Authorization", format("token %s", githubToken2)).build();
            response = chain.proceed(modifiedRequest);
        }

        return response;
    }

}