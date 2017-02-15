package com.egorshenova.rss.models;

import java.io.Serializable;
import java.util.Date;

public class RSSItem extends BaseModel implements Serializable {
    private int feedId;
    private String title;
    private String link;
    private String description;
    private long pubDate;
    private String imageUrl;

    public RSSItem() {

    }

    public RSSItem(Integer id, Integer feedId, String title, String link, String description, long pubDate, String imageUrl) {
        this.id = id;
        this.feedId = feedId;
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.imageUrl = imageUrl;
    }

    public RSSItem(String title, String link, String description, long pubdate, String imageUrl) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubdate;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    @Override
    public String toString() {
        return "RSSItem{" +
                "id=" + id +
                ", feedId=" + feedId +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate=" + pubDate +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
