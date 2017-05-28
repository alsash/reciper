package com.alsash.reciper.data.cloud.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * A configuration of the cloud database, that persist at the Github repository
 */
public class GithubDbConfigResponse {
    public String dbPath;
    @SerializedName("db_update_date_unix")
    public Date dbUpdateDate;
    public List<String> dbLocales;
}
