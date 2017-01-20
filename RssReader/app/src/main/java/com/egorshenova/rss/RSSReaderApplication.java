package com.egorshenova.rss;

import android.app.Application;
import android.content.Context;

import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.PrefsHelper;

public class RSSReaderApplication extends Application {

    private static RSSReaderApplication instance;
    private GlobalContainer globalContainer;

    public static RSSReaderApplication getInstance() {
        if (instance == null) {
            throw new RuntimeException("Application initialization error.");
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.getLogger(RSSReaderApplication.class).debug("Application onCreate");

        instance = this;
        globalContainer = GlobalContainer.initialize(this);
        if (PrefsHelper.get().loadFirstRunApp()) {
            PrefsHelper.get().saveFirstRunApp(false);
        } else {
            try {
                globalContainer.loadFeeds();
            } catch (Exception e) {
                Logger.getLogger(RSSReaderApplication.class).error(e.getMessage(), e);
            }
        }

    }

    public GlobalContainer getGlobalContainer() {
        return globalContainer;
    }

    public Application getApplicationContext() {
        return this;
    }

    public Context getContext() {
        return this.getContext();
    }
}
