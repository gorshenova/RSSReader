package com.egorshenova.rss.models;

import java.io.Serializable;
import java.util.Date;

public class RSSItem implements Serializable {

    private String title;
    private String link;
    private String description;
    private Date pubDate;
    private String imageUrl;

    public RSSItem(){

    }

    public RSSItem(String title, String link, String description, Date pubdate, String imageUrl) {
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

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "RSSItem{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
