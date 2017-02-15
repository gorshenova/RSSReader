package com.egorshenova.rss.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.egorshenova.rss.database.RSSReaderContract.*;
import com.egorshenova.rss.models.RSSItem;
import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */

public class FeedItemDataSource extends BaseDataSource<RSSItem> {

    @Override
    protected RSSItem createFromCursor(Cursor c) {
        RSSItem item = new RSSItem();
        item.setId(c.getInt(c.getColumnIndex(FeedItemsEntry.COLUMN_NAME_ID)));
        item.setFeedId(c.getInt(c.getColumnIndex(FeedItemsEntry.COLUMN_NAME_FEED_ID)));
        item.setLink(c.getString(c.getColumnIndex(FeedItemsEntry.COLUMN_NAME_LINK)));
        item.setDescription(c.getString(c.getColumnIndex(FeedItemsEntry.COLUMN_NAME_DESCRIPTION)));
        item.setImageUrl(c.getString(c.getColumnIndex(FeedItemsEntry.COLUMN_NAME_IMAGE_URL)));
        item.setTitle(c.getString(c.getColumnIndex(FeedItemsEntry.COLUMN_NAME_TITLE)));
        item.setPubDate(c.getLong(c.getColumnIndex(FeedItemsEntry.COLUMN_NAME_PUB_DATE)));
        return item;
    }

    public List<RSSItem> getAllItemsByFeedId(int feedId) {
        return get(FeedItemsEntry.SQL_SELECT_ALL_ITEMS_BY_FEED_ID, feedId);
    }

    /**
     * Method adds a list of items for the current feed in database using database transactions.
     *
     * @param feedId is id of the feed which owns items
     * @param items  the list of items which correspond to the specific feed
     * @return If the item is added successfully, then it will have filled id and be added in a resulted list.
     * In case of failure, it will be absent in the resulted lists and the error log will be displayed.
     */
    public List<RSSItem> addItemsByFeedId(int feedId, List<RSSItem> items) {
        List<RSSItem> addedItems = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (RSSItem i : items) {
                //Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedItemsEntry.COLUMN_NAME_FEED_ID, feedId);
                values.put(FeedItemsEntry.COLUMN_NAME_TITLE, StringUtils.replaceSingleQuoteByDoubleOne(i.getTitle()));
                values.put(FeedItemsEntry.COLUMN_NAME_DESCRIPTION, StringUtils.replaceSingleQuoteByDoubleOne(i.getDescription()));
                values.put(FeedItemsEntry.COLUMN_NAME_LINK, i.getLink());
                values.put(FeedItemsEntry.COLUMN_NAME_IMAGE_URL, i.getImageUrl());
                values.put(FeedItemsEntry.COLUMN_NAME_PUB_DATE, i.getPubDate());

                int itemId = (int) db.insert(FeedItemsEntry.TABLE_NAME, null, values);
                if (itemId != -1) {
                    i.setId(itemId);
                    addedItems.add(i);
                } else {
                    Logger.error(FeedItemDataSource.class, "Item was not added in database: " + i);
                }

                // In case you do larger updates, use a methods yieldIfContendedSafely.
                // Because SQLite locks the database during an transaction.
                // With this call, Android checks if someone else queries the data and
                // if finish automatically the transaction and opens a new one.
                // This way the other process can access the data in between.
                db.yieldIfContendedSafely();
            }
            db.setTransactionSuccessful();
            return addedItems;
        } finally {
            db.endTransaction();
        }
    }

}
