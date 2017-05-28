package com.alsash.reciper.logic.exception;

/**
 * Created by alsash on 5/28/17.
 */

public class NoInternetException extends RuntimeException {
    public NoInternetException() {
        super("internet connection unavailable");
    }
}
