package com.egorshenova.rss;

import android.app.Application;
import android.content.Context;

import com.egorshenova.rss.observers.FeedChangeObservable;
import com.egorshenova.rss.utils.Logger;
import com.squareup.leakcanary.LeakCanary;

public class RSSReaderApplication extends Application {

    private static RSSReaderApplication instance;
    private GlobalContainer globalContainer;
    private FeedChangeObservable feedChangeObservable;

    public static RSSReaderApplication get() {
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
        feedChangeObservable = new FeedChangeObservable();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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

    public FeedChangeObservable getFeedObservable() {
        return feedChangeObservable;
    }
}
