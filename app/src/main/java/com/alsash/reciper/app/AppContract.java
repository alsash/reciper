package com.alsash.reciper.app;

/**
 * Public keys for all elements in the Application
 */
public class AppContract {

    public static final int CACHE_SIZE_BYTE = 100 * 1024 * 1024; // 100 Mebibytes (honest Megabytes)

    private static final String TAG = AppContract.class.getCanonicalName();

    public static final String PAYLOAD_FLIP_BACK_TO_FRONT = TAG + ".payload_flip_back_to_front";
    public static final String PAYLOAD_FLIP_FRONT_TO_BACK = TAG + ".payload_flip_front_to_back";

    public static final String KEY_RECIPE_ID = TAG + ".key_recipe_id";
}
