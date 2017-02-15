package com.egorshenova.rss.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.egorshenova.rss.utils.Logger;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */

public class RSSSQLiteHelper extends SQLiteOpenHelper {
    public final Logger logger = Logger.getLogger(RSSSQLiteHelper.class);

    public static final String DATABASE_NAME = "rssreader.db";
    public static final int DATABASE_VERSION = 1;

    public RSSSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RSSReaderContract.FeedEntry.SQL_CREATE_TABLE);
        db.execSQL(RSSReaderContract.FeedItemsEntry.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        logger.info("Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL(RSSReaderContract.FeedEntry.SQL_DROP_TABLE);
        db.execSQL(RSSReaderContract.FeedItemsEntry.SQL_DROP_TABLE);

        onCreate(db);
    }
}
