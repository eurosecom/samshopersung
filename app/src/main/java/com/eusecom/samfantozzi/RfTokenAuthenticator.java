package com.eusecom.samfantozzi;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import java.io.IOException;

/**
 * Problem by regenerate request with token by Authenticator
 * When authentication is requested by an origin server, the response code is 401 and the implementation
 * should respond with a new request that sets the "Authorization" header.
 * if X-RateLimit-Remaining: is 0 before request without credentials token, I get 403 Forbidden and I have gone.....
 *
 */

import static java.lang.String.format;

public class RfTokenAuthenticator implements Authenticator {

    String githubToken, githubToken2;

    public RfTokenAuthenticator(String token, String token2) {
        this.githubToken = token;
        this.githubToken2 = token2;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {

        //prevent infinite loop , if same credentials already tired, no need to try again
        if (response.request().header("Authorization") != null) {
            return null; // Give up, we've already failed to authenticate.
        }

        return response.request().newBuilder()
                .addHeader("Authorization", format("token %s", githubToken2)).build();


    }



}