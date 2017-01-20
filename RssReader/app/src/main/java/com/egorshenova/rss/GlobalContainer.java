package com.egorshenova.rss;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlobalContainer {

    private static final Logger logger = Logger.getLogger(GlobalContainer.class);

    private static GlobalContainer instance;
    private List<RSSFeed> feeds = new ArrayList<>();
    private Context context;
    private Handler handler;
    private RSSFileManager fileManager;

    private GlobalContainer() {

    }

    private GlobalContainer(RSSReaderApplication context) {
        this.context = context;
        this.handler = new Handler(Looper.getMainLooper());
        fileManager = new RSSFileManager(context, Constants.RSS_FILE_NAME);
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
        feed.setId(feeds.size());
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

    public void saveFeeds() throws IOException {
        logger.info("===> Save feeds");
        fileManager.writeFeeds(feeds);
    }

    public void loadFeeds() throws IOException, ClassNotFoundException {
        logger.debug("====> Load feeds");
        List<RSSFeed> result = fileManager.readFeeds();
        if (result != null) {
            feeds.addAll(result);
        }

    }

    public boolean checkRSSLinkExists(String urlStr) {
        for (RSSFeed f : feeds) {
            if (f.getRssLink().equalsIgnoreCase(urlStr)) {
                return true;
            }
        }
        return false;
    }
}
