package com.egorshenova.rss.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.egorshenova.rss.RSSReaderApplication;

public class PrefsHelper {

    private static PrefsHelper instance;

    public static PrefsHelper get() {
        if (instance == null) {
            instance = new PrefsHelper();
        }
        return instance;
    }

    public static final String PREFS_FIRST_RUN = RSSReaderApplication.getInstance().getPackageName() + ".PREFS_FIRST_RUN";

    private SharedPreferences getSharedPreferences() {
        return RSSReaderApplication.getInstance().getSharedPreferences(RSSReaderApplication.getInstance().getPackageName(), Context.MODE_PRIVATE);
    }

    public boolean loadFirstRunApp() {
        return getSharedPreferences().getBoolean(PREFS_FIRST_RUN, true);
    }

    public void saveFirstRunApp(boolean firstRun) {
        getSharedPreferences().edit().putBoolean(PREFS_FIRST_RUN, firstRun).commit();
    }
}
