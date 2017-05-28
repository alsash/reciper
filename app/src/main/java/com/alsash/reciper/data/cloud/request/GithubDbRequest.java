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
    @GET("{version}/{language}/author_table.json")
    Maybe<List<AuthorTable>> getAuthorTable(@Path("version") String version,
                                            @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/category_table.json")
    Maybe<List<CategoryTable>> getCategoryTable(@Path("version") String version,
                                                @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/food_measure_table.json")
    Maybe<List<FoodMeasureTable>> getFoodMeasureTable(@Path("version") String version,
                                                      @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/food_table.json")
    Maybe<List<FoodTable>> getFoodTable(@Path("version") String version,
                                        @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/food_usda_table.json")
    Maybe<List<FoodUsdaTable>> getFoodUsdaTable(@Path("version") String version,
                                                @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/label_table.json")
    Maybe<List<LabelTable>> getLabelTable(@Path("version") String version,
                                          @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/photo_table.json")
    Maybe<List<PhotoTable>> getPhotoTable(@Path("version") String version,
                                          @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/recipe_food_table.json")
    Maybe<List<RecipeFoodTable>> getRecipeFoodTable(@Path("version") String version,
                                                    @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/recipe_label_table.json")
    Maybe<List<RecipeLabelTable>> getRecipeLabelTable(@Path("version") String version,
                                                      @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/recipe_method_table.json")
    Maybe<List<RecipeMethodTable>> getRecipeMethodTable(@Path("version") String version,
                                                        @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/recipe_photo_table.json")
    Maybe<List<RecipePhotoTable>> getRecipePhotoTable(@Path("version") String version,
                                                      @Path("language") String language);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{version}/{language}/recipe_table.json")
    Maybe<List<RecipeTable>> getRecipeTable(@Path("version") String version,
                                            @Path("language") String language);
}
