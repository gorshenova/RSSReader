package com.egorshenova.rss.models;

/**
 * Created by eyablonskaya on 17-Feb-17.
 */

public class FeedChangeObject {
    private RSSFeed feed;
    private boolean updated;

    public FeedChangeObject(RSSFeed feed, boolean updated) {
        this.feed = feed;
        this.updated = updated;
    }

    public RSSFeed getFeed() {
        return feed;
    }

    public void setFeed(RSSFeed feed) {
        this.feed = feed;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}