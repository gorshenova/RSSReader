package com.egorshenova.rss.database.dao;

import android.database.Cursor;

import com.egorshenova.rss.database.DbConstants;
import com.egorshenova.rss.models.RSSItem;

import java.util.List;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */

public class FeedItemDataSource extends BaseDataSource<RSSItem> {

    @Override
    protected RSSItem createFromCursor(Cursor c) {
        RSSItem item = new RSSItem();
        item.setId(c.getInt(c.getColumnIndex(DbConstants.TABLE_FEED_ITEMS_COLUMN_ID)));
        item.setFeedId(c.getInt(c.getColumnIndex(DbConstants.TABLE_FEED_ITEMS_COLUMN_FEED_ID)));
        item.setLink(c.getString(c.getColumnIndex(DbConstants.TABLE_FEED_ITEMS_COLUMN_LINK)));
        item.setDescription(c.getString(c.getColumnIndex(DbConstants.TABLE_FEED_ITEMS_COLUMN_DESCRIPTION)));
        item.setImageUrl(c.getString(c.getColumnIndex(DbConstants.TABLE_FEED_ITEMS_COLUMN_IMAGE_URL)));
        item.setTitle(c.getString(c.getColumnIndex(DbConstants.TABLE_FEED_ITEMS_COLUMN_TITLE)));
        item.setPubDate(c.getLong(c.getColumnIndex(DbConstants.TABLE_FEED_ITEMS_COLUMN_PUB_DATE)));
        return item;
    }

    public List<RSSItem> getAllItemsByFeedId(int feedId) {
        return get(DbConstants.SQL_SELECT_ALL_ITEMS_BY_FEED_ID, feedId);
    }
}
