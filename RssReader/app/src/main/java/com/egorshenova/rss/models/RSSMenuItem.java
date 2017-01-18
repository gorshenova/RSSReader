package com.egorshenova.rss.models;

public class RSSMenuItem {

    public enum MenuAction {
        ACTION_OPEN_FEED, ADD_FEED_ACTION
    }

    public MenuAction menuAction;
    public RSSFeed feed;
    public String name;

    public RSSMenuItem(MenuAction menuAction, RSSFeed feed) {
        this.menuAction = menuAction;
        this.feed = feed;
        this.name = feed.getTitle();
    }

    public RSSMenuItem(String name, MenuAction menuAction) {
        this.menuAction = menuAction;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuAction getMenuAction() {
        return menuAction;
    }

    public void setMenuAction(MenuAction menuAction) {
        this.menuAction = menuAction;
    }

    public RSSFeed getFeed() {
        return feed;
    }

    public void setFeed(RSSFeed feed) {
        this.feed = feed;
    }

    @Override
    public String toString() {
        return "RSSMenuItem{" +
                "menuAction=" + menuAction +
                ", feed=" + feed +
                ", name='" + name + '\'' +
                '}';
    }
}
