package com.egorshenova.rss.database.dao;

import android.database.Cursor;
import android.support.v7.util.SortedList;

import com.egorshenova.rss.database.DbConstants;
import com.egorshenova.rss.models.RSSFeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */

public class FeedDataSource extends BaseDataSource<RSSFeed> {
    @Override
    protected RSSFeed createFromCursor(Cursor c) {
        RSSFeed feed = new RSSFeed();
        feed.setId(c.getInt(c.getColumnIndex(DbConstants.TABLE_FEED_COLUMN_ID)));
        feed.setTitle(c.getString(c.getColumnIndex(DbConstants.TABLE_FEED_COLUMN_TITLE)));
        feed.setImageUrl(c.getString(c.getColumnIndex(DbConstants.TABLE_FEED_COLUMN_IMAGE_URL)));
        feed.setRssLink(c.getString(c.getColumnIndex(DbConstants.TABLE_FEED_COLUMN_RSS_LINK)));
        return feed;
    }

    public List<RSSFeed> getAllFeeds(){
        List<RSSFeed> feeds =  new ArrayList<>();
        List<RSSFeed> res = get(DbConstants.SQL_SELECT_ALL_FEEDS);
        if(res != null && res.size()>0){
            feeds.addAll(res);
        }
        return  feeds;
    }
}
