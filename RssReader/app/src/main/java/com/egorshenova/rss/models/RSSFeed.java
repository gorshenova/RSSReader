package com.egorshenova.rss.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RSSFeed implements Serializable {

    private String title;
    private String imageUrl;
    private String rssLink;
    private List<RSSItem> items = new ArrayList<>();

    public RSSFeed() {
    }

    public RSSFeed(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public List<RSSItem> getItems() {
        return items;
    }

    public void setItems(List<RSSItem> items) {
        this.items = items;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    @Override
    public String toString() {
        return "RSSFeed{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", rssLink='" + rssLink + '\'' +
                ", items=" + (items != null ? items.size() : null) +
                '}';
    }
}
