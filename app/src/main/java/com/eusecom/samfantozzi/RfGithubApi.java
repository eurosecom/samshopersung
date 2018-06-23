package com.eusecom.samfantozzi;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RfGithubApi {

    /**
     * See https://developer.github.com/v3/repos/#list-contributors
     */
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<RfContributor>> contributors(@Path("owner") String owner,
                                               @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    List<RfContributor> getContributors(@Path("owner") String owner, @Path("repo") String repo);

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    Observable<RfUser> user(@Path("user") String user);

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    RfUser getUser(@Path("user") String user);
}