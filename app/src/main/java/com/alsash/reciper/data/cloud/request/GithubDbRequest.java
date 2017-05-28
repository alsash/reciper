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
    @GET("{db_language}/food_measure.json")
    Maybe<List<FoodMeasureTable>> getFoodMeasureTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/food.json")
    Maybe<List<FoodTable>> getFoodTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/food_usda.json")
    Maybe<List<FoodUsdaTable>> getFoodUsdaTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/label.json")
    Maybe<List<LabelTable>> getLabelTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/photo.json")
    Maybe<List<PhotoTable>> getPhotoTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/recipe_food.json")
    Maybe<List<RecipeFoodTable>> getRecipeFoodTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/recipe_label.json")
    Maybe<List<RecipeLabelTable>> getRecipeLabelTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/recipe_method.json")
    Maybe<List<RecipeMethodTable>> getRecipeMethodTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/recipe_photo.json")
    Maybe<List<RecipePhotoTable>> getRecipePhotoTable(@Path("db_language") String pathDbLanguage);

    @Headers({HEADER_ACCEPT, HEADER_AGENT})
    @GET("{db_language}/recipe.json")
    Maybe<List<RecipeTable>> getRecipeTable(@Path("db_language") String pathDbLanguage);
}
