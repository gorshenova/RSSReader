package com.egorshenova.rss.utils;


import android.util.Log;

import com.egorshenova.rss.BuildConfig;

public class Logger {

    private Class<?> clazz;

    private Logger(Class<?> cls) {
        this.clazz = cls;
    }

    public static Logger getLogger(Class<?> cls) {
        return new Logger(cls);
    }

    public void debug(String message) {
        debug(clazz, message);
    }

    public void debug(String message, Throwable t) {
        debug(clazz, message, t);
    }

    public void error(String message) {
        error(clazz, message);
    }


    public void error(String message, Throwable t) {
        error(clazz, message, t);
    }

    public void info(String message) {
        info(clazz, message);
    }

    public static void debug(Class<?> cls, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(cls.getSimpleName(), "------- " + message);
        }
    }

    public static void debug(Class<?> cls, String message, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.d(cls.getSimpleName(), "------- " + message, t);
        }
    }

    public static void error(Class<?> cls, String message) {
        Log.e(cls.getSimpleName(), "------" + message);
    }


    public static void error(Class<?> cls, String message, Throwable t) {
        Log.e(cls.getSimpleName(), "------" + message, t);
    }

    public static void info(Class<?> cls, String message) {
        Log.i(cls.getSimpleName(), "Info message:\n" + message);
    }
}
