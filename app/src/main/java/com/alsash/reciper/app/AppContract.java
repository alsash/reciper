package com.alsash.reciper.app;

import com.alsash.reciper.BuildConfig;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.concurrent.TimeUnit;

/**
 * Public keys for all elements in the Application
 */
public final class AppContract {
    private static final String TAG = "com.alsash.reciper.app.AppContract";

    // Cloud service urls
    public static final class Cloud {
        public static final int CONNECT_TIMEOUT_MS = 5000;
        public static final int READ_TIMEOUT_MS = 5000;

        public static final class Github {
            private static final String BASE_URL = "https://api.github.com/";
            private static final String ACCEPT_RAW = "application/vnd.github.v3.raw+json";
            private static final String HEADER_AGENT = "User-Agent: alsash-reciper";
            private static final String JSON_ENDPOINT = "repos/alsash/reciper/contents/json/";

            public static final class Db {
                public static final long CHECK_INTERVAL_MS = TimeUnit.HOURS.toMillis(12);
                public static final String HEADER_ACCEPT = "Accept: " + Github.ACCEPT_RAW;
                public static final String HEADER_AGENT = Github.HEADER_AGENT;
                public static final String BASE_URL = Github.BASE_URL + JSON_ENDPOINT + "db/";
            }

        }

        public static final class Usda {
            public static final String API_KEY = BuildConfig.USDA_API_KEY;
            public static final String BASE_URL = "https://api.nal.usda.gov/";
            public static final String HEADER_CONTENT = "Content-Type: application/json";
        }
    }

    public static final class Db {
        public static final String DATABASE_NAME = "reciper_db";
    }

    // Glide framework
    public static final class Glide {
        public static final double MEMORY_CACHE_FACTOR = 1.5D;
        public static final double BITMAP_POOL_FACTOR = 1.5D;
        public static final int DISK_CACHE_SIZE_BYTES = 512 * 1024 * 1024; // 512 MiB
        public static final DiskCacheStrategy DEFAULT_CACHE_STRATEGY = DiskCacheStrategy.SOURCE;
    }

    // Animation payloads
    public static final class Payload {
        public static final String FLIP_BACK_TO_FRONT = TAG + ".flip_back_to_front";
        public static final String FLIP_FRONT_TO_BACK = TAG + ".flip_front_to_back";
        private static final String PAYLOAD_TAG = TAG + ".Payload";
    }

    // Public entity keys
    public static final class Key {
        private static final String KEY_TAG = TAG + ".Key";
        public static final String NO_ID = KEY_TAG + ".no_id";
        public static final String RECIPE_ID = KEY_TAG + ".recipe_id";
        public static final String CATEGORY_ID = KEY_TAG + ".category_id";
        public static final String LABEL_ID = KEY_TAG + ".label_id";
    }

}
