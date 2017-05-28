package com.alsash.reciper.data.cloud.request;

import com.alsash.reciper.data.cloud.response.GithubDbConfigResponse;
import com.alsash.reciper.data.db.table.AuthorTable;
import com.alsash.reciper.data.db.table.CategoryTable;
import com.alsash.reciper.data.db.table.FoodMeasureTable;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.data.db.table.FoodUsdaTable;
import com.alsash.reciper.data.db.table.LabelTable;
import com.alsash.reciper.data.db.table.PhotoTable;
import com.alsash.reciper.data.db.table.RecipeFoodTable;
import com.alsash.reciper.data.db.table.RecipeLabelTable;
import com.alsash.reciper.data.db.table.RecipeMethodTable;
import com.alsash.reciper.data.db.table.RecipePhotoTable;
import com.alsash.reciper.data.db.table.RecipeTable;

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
    @GET("{db_language}/author.json")
    Maybe<List<AuthorTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/category.json")
    Maybe<List<CategoryTable>> getCategoryTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<FoodMeasureTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<FoodTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<FoodUsdaTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<LabelTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<PhotoTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<RecipeFoodTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<RecipeLabelTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<RecipeMethodTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<RecipePhotoTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/author.json")
    Maybe<List<RecipeTable>> getAuthorTable(@Path("db_language") String pathDbLanguage);
}
