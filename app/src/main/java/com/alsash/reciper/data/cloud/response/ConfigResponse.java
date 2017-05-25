package com.alsash.reciper.data.cloud.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * A Config data response from Github service
 */
public class ConfigResponse {

    public Db db;

    public static class Db {
        public String path;
        public List<Locale> locales;

        public static class Locale {
            public String locale;
            @SerializedName("update_date_unix")
            public Date updateDate;
        }
    }
}
