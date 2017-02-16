package com.egorshenova.rss.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.RSSReaderApplication;
import com.egorshenova.rss.utils.Logger;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */
public class RSSReaderDatabase {

    private static final String DATABASE_NAME = "rssreader.db";
    private static final int DATABASE_VERSION = 1;

    private static OpenHelper openHelper =  new OpenHelper();

    private static class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper() {
            super(GlobalContainer.getInstance().getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RSSReaderContract.FeedEntry.SQL_CREATE_TABLE);
            db.execSQL(RSSReaderContract.FeedItemsEntry.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Logger.info(OpenHelper.class, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");

            db.execSQL(RSSReaderContract.FeedEntry.SQL_DROP_TABLE);
            db.execSQL(RSSReaderContract.FeedItemsEntry.SQL_DROP_TABLE);

            onCreate(db);
        }
    }


    public static SQLiteDatabase getDatabase(){
        return openHelper.getWritableDatabase();
    }
}
