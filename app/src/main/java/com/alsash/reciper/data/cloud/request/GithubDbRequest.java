package com.alsash.reciper.data.cloud.request;

import com.alsash.reciper.data.cloud.response.GithubDbConfigResponse;
import com.alsash.reciper.data.db.table.AuthorTable;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

import static com.alsash.reciper.app.AppContract.Cloud.Github.Db.HEADER_ACCEPT;
import static com.alsash.reciper.app.AppContract.Cloud.Github.Db.HEADER_AGENT;

/**
 * A Request to Github REST service for cloud database, that persist in github repository
 * Documentation at https://developer.github.com/v3/
 */
public interface GithubDbRequest {
    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("config.json")
    Maybe<GithubDbConfigResponse> getConfig();

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_locale}/author.json")
    Maybe<List<AuthorTable>> getAuthorTable(@Path("db_locale") String dbLocale);
}
