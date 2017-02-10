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

    public RSSSQLiteHelper(Context context) {
        super(context, DbConstants.DATABASE_NAME, null, DbConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.SQL_CREATE_TABLE_FEED);
        db.execSQL(DbConstants.SQL_CREATE_TABLE_FEED_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        logger.info("Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL(DbConstants.SQL_DROP_TABLE_FEED);
        db.execSQL(DbConstants.SQL_DROP_TABLE_FEED_ITEMS);

        onCreate(db);
    }
}
