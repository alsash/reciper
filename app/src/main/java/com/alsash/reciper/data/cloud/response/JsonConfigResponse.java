package com.alsash.reciper.data.cloud.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * A Config data response from Github service
 */
public class JsonConfigResponse {
    public String dbPath;
    @SerializedName("db_update_date_unix")
    public Date dbUpdateDate;
    public List<String> dbLocales;
}
