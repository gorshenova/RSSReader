package com.egorshenova.rss.database;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */

public class DbConstants {

    public static final String DATABASE_NAME = "rssreader.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_FEED = "feed";
    public static final String TABLE_FEED_COLUMN_ID = "id";
    public static final String TABLE_FEED_COLUMN_TITLE = "title";
    public static final String TABLE_FEED_COLUMN_RSS_LINK = "rss_link";
    public static final String TABLE_FEED_COLUMN_IMAGE_URL = "image_url";

    public static final String TABLE_NAME_FEED_ITEMS = "feed_items";
    public static final String TABLE_FEED_ITEMS_COLUMN_ID = "id";
    public static final String TABLE_FEED_ITEMS_COLUMN_FEED_ID = "feed_id";
    public static final String TABLE_FEED_ITEMS_COLUMN_TITLE = "title";
    public static final String TABLE_FEED_ITEMS_COLUMN_PUB_DATE = "pub_date";
    public static final String TABLE_FEED_ITEMS_COLUMN_LINK = "link";
    public static final String TABLE_FEED_ITEMS_COLUMN_DESCRIPTION = "description";
    public static final String TABLE_FEED_ITEMS_COLUMN_IMAGE_URL = "image_url";


    public static final String SQL_CREATE_TABLE_FEED = "CREATE TABLE " + TABLE_NAME_FEED + "("
            + TABLE_FEED_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TABLE_FEED_COLUMN_TITLE + " TEXT NOT NULL,"
            + TABLE_FEED_COLUMN_RSS_LINK + " TEXT NOT NULL,"
            + TABLE_FEED_COLUMN_IMAGE_URL + " TEXT);";

    public static final String SQL_CREATE_TABLE_FEED_ITEMS = "CREATE TABLE " + TABLE_NAME_FEED_ITEMS + "("
            + TABLE_FEED_ITEMS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TABLE_FEED_ITEMS_COLUMN_FEED_ID + " INTEGER NOT NULL,"
            + TABLE_FEED_ITEMS_COLUMN_TITLE + " TEXT NOT NULL,"
            + TABLE_FEED_ITEMS_COLUMN_PUB_DATE + " LONG NOT NULL,"
            + TABLE_FEED_ITEMS_COLUMN_LINK + " TEXT,"
            + TABLE_FEED_ITEMS_COLUMN_DESCRIPTION + " TEXT,"
            + TABLE_FEED_ITEMS_COLUMN_IMAGE_URL + " TEXT,"
            + "FOREIGN KEY(" + TABLE_FEED_ITEMS_COLUMN_FEED_ID + ") REFERENCES " + TABLE_NAME_FEED + "(" + TABLE_FEED_COLUMN_ID + ")"
            + ");";

    public static final String SQL_DROP_TABLE_FEED = "DROP TABLE IF EXISTS " + TABLE_NAME_FEED;
    public static final String SQL_DROP_TABLE_FEED_ITEMS = "DROP TABLE IF EXISTS " + TABLE_NAME_FEED_ITEMS;

    public static final String SQL_SELECT_ALL_FEEDS = "SELECT * FROM "  + TABLE_NAME_FEED;
    public static final String SQL_SELECT_ALL_ITEMS_BY_FEED_ID = "SELECT * FROM "  + TABLE_NAME_FEED_ITEMS + " WHERE " + TABLE_FEED_ITEMS_COLUMN_FEED_ID + " = ? ";
}
