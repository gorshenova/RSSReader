package com.egorshenova.rss.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.egorshenova.rss.database.RSSReaderContract.FeedEntry;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */

public class FeedDataSource extends BaseDataSource<RSSFeed> {
    @Override
    protected RSSFeed createFromCursor(Cursor c) {
        RSSFeed feed = new RSSFeed();
        feed.setId(c.getInt(c.getColumnIndex(FeedEntry.COLUMN_NAME_ID)));
        feed.setTitle(c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_TITLE)));
        feed.setImageUrl(c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_IMAGE_URL)));
        feed.setRssLink(c.getString(c.getColumnIndex(FeedEntry.COLUMN_NAME_RSS_LINK)));
        return feed;
    }

    public List<RSSFeed> getAllFeeds() {
        List<RSSFeed> feeds = new ArrayList<>();
        List<RSSFeed> res = get(FeedEntry.SQL_SELECT_ALL_FEEDS);
        if (res != null && res.size() > 0) {
            feeds.addAll(res);
        }
        return feeds;
    }

    public int createFeed(RSSFeed feed) {
        //Create a new map of values, where column names are the keys
        ContentValues values =  new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, StringUtils.replaceSingleQuoteByDoubleOne(feed.getTitle()));
        values.put(FeedEntry.COLUMN_NAME_RSS_LINK, feed.getRssLink());
        values.put(FeedEntry.COLUMN_NAME_IMAGE_URL, feed.getImageUrl());

        // Insert the new row, returning the primary key value of the new row
        return (int) getWritableDatabase().insert(FeedEntry.TABLE_NAME, null ,values);
    }
}
