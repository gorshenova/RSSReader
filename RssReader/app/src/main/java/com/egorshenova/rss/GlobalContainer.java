package com.egorshenova.rss;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.egorshenova.rss.database.RSSSQLiteHelper;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.utils.ComparatorByPubDate;
import com.egorshenova.rss.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalContainer {

    private static final Logger logger = Logger.getLogger(GlobalContainer.class);

    private static GlobalContainer instance;
    private List<RSSFeed> feeds = new ArrayList<>();
    private Context context;
    private Handler handler;
    private RSSSQLiteHelper dbHelper;

    private GlobalContainer() {

    }

    private GlobalContainer(RSSReaderApplication context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
    }

    public static GlobalContainer getInstance() {
        if (instance == null) {
            throw new RuntimeException("Container isn't initialized. Check Application.onCreate()");
        }
        return instance;
    }

    public List<RSSFeed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<RSSFeed> feeds) {
        this.feeds.clear();
        this.feeds.addAll(feeds);
    }


    public boolean addFeed(RSSFeed feed) {
        logger.debug("Add new feed: " + feed);
        return feeds.add(feed);
    }

    public boolean updateFeed(RSSFeed feed) {
        logger.debug("Update feed: " + feed);
        feeds.remove(feed);
        return feeds.add(feed);
    }

    public static GlobalContainer initialize(RSSReaderApplication cnt) {
        instance = new GlobalContainer(cnt);
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }

    public Context getContext() {
        return context;
    }

    public boolean checkRSSLinkExists(String urlStr) {
        for (RSSFeed f : feeds) {
            if (f.getRssLink().equalsIgnoreCase(urlStr)) {
                return true;
            }
        }
        return false;
    }

    public RSSSQLiteHelper getSQLiteDBHelper() {
        if (dbHelper == null) {
            dbHelper = new RSSSQLiteHelper(getContext());
        }
        return dbHelper;
    }
}
