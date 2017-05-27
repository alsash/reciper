package com.alsash.reciper.logic.error;

/**
 * Created by alsash on 5/28/17.
 */

public class NoInternetConnectionException extends RuntimeException {
    public NoInternetConnectionException() {
        super("internet connection unavailable");
    }
}
