package com.egorshenova.rss.models;

/**
 * Created by eyablonskaya on 17-Feb-17.
 */

public class FeedChangeObject {
    private RSSFeed feed;

    public FeedChangeObject(RSSFeed feed) {
        this.feed = feed;
    }
    public RSSFeed getFeed() {
        return feed;
    }

    public void setFeed(RSSFeed feed) {
        this.feed = feed;
    }

    @Override
    public String toString() {
        return "FeedChangeObject{" +
                "feed=" + feed +
                '}';
    }
}