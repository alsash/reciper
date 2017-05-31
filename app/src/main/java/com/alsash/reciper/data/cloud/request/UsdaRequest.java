package com.alsash.reciper.data.cloud.request;

import com.alsash.reciper.data.cloud.response.UsdaFoodsResponse;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.alsash.reciper.app.AppContract.Cloud.Usda.HEADER_CONTENT;

/**
 * A Request to Usda service.
 * Documentation at https://ndb.nal.usda.gov/ndb/doc/index
 */
public interface UsdaRequest {
    @Headers(HEADER_CONTENT)
    @GET("ndb/reports/V2")
    Maybe<UsdaFoodsResponse> getFood(@Query("api_key") String apiKey,
                                     @Query("ndbno") String... ndbNo);
}
