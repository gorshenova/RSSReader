package com.egorshenova.rss.observers;

import com.egorshenova.rss.models.FeedChangeObject;

import java.util.Observable;

/**
 * Observable is used to update feed views after swipe to refresh, add a new feed
 */
public class FeedChangeObservable extends Observable {

    private FeedChangeObject obj;

    public void setObj(FeedChangeObject obj) {
        this.obj = obj;
        setChanged();
        notifyObservers(obj);
    }

    public FeedChangeObject getObj() {
        return obj;
    }
}