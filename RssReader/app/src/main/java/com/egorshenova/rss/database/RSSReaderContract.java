package com.egorshenova.rss.database;

import android.provider.BaseColumns;

/**
 * Contract class is a container that defines the tables and columns.
 * It allows to use the same constant value for all other classes.
 */
public final class RSSReaderContract {


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public RSSReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "feed";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_RSS_LINK = "rss_link";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";


        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_NAME_TITLE + " TEXT NOT NULL,"
                + COLUMN_NAME_RSS_LINK + " TEXT NOT NULL,"
                + COLUMN_NAME_IMAGE_URL + " TEXT);";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_SELECT_ALL_FEEDS = "SELECT * FROM " + TABLE_NAME;

        
        public static final String SQL_INSERT_FEED = "INSERT INTO " + TABLE_NAME + "("
                + COLUMN_NAME_TITLE + ","
                + COLUMN_NAME_RSS_LINK + ","
                + COLUMN_NAME_IMAGE_URL + ") VALUES('?', '?', '?')";
                
        public static final String SQL_UPDATE_FEED_BY_ID = "UPDATE " + TABLE_NAME + " SET "
                + COLUMN_NAME_TITLE  + " = ?, "
                + COLUMN_NAME_IMAGE_URL + " = ? "
                + "WHERE " + COLUMN_NAME_ID + " = ?";

    }

    public static abstract class FeedItemsEntry implements BaseColumns {
        public static final String TABLE_NAME = "feed_items";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FEED_ID = "feed_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_PUB_DATE = "pub_date";
        public static final String COLUMN_NAME_LINK = "link";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IMAGE_URL = "image_url";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_NAME_FEED_ID + " LONG NOT NULL,"
                + COLUMN_NAME_TITLE + " TEXT NOT NULL,"
                + COLUMN_NAME_PUB_DATE + " LONG NOT NULL,"
                + COLUMN_NAME_LINK + " TEXT,"
                + COLUMN_NAME_DESCRIPTION + " TEXT,"
                + COLUMN_NAME_IMAGE_URL + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_NAME_FEED_ID + ") REFERENCES " + FeedEntry.TABLE_NAME + "(" + FeedEntry.COLUMN_NAME_ID + ")"
                + ");";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String SQL_SELECT_ALL_ITEMS_BY_FEED_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_FEED_ID + " = ? ";
    }
}
