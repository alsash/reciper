package com.alsash.reciper.logic.exception;

import android.os.Looper;

/**
 * An logical exception caused by invoking method on main thread
 */
public class MainThreadException extends RuntimeException {
    private MainThreadException(String owner, String action) {
        super(owner + " : " + action + " must be called on background thread");
    }

    public static void throwOnMainThread(String owner, String action) throws MainThreadException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new MainThreadException(owner, action);
        }
    }
}
