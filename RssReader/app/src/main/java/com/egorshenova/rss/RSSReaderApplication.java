package com.egorshenova.rss;

import android.app.Application;
import android.content.Context;

import com.egorshenova.rss.models.FeedChangeObject;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.utils.Logger;

import java.util.Observable;

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

    public class FeedChangeObservable extends Observable {

        private FeedChangeObject obj;

        public void setObj(FeedChangeObject obj) {
            this.obj =  obj;
            setChanged();
            notifyObservers(obj);
        }

        public FeedChangeObject getObj() {
            return obj;
        }
    }
}
