package com.egorshenova.rss.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.database.RSSReaderDatabase;
import com.egorshenova.rss.models.BaseModel;
import com.egorshenova.rss.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eyablonskaya on 10-Feb-17.
 */

public abstract class BaseDataSource<T extends BaseModel> {
    public final Logger logger = Logger.getLogger(BaseDataSource.class);
    protected RSSReaderDatabase dbHelper;

    public BaseDataSource() {
        dbHelper = GlobalContainer.getInstance().getSQLiteDBHelper();
    }

    /**
     * Method is used to create object from cursor
     *
     * @param c cursor
     * @return object
     */
    protected abstract T createFromCursor(Cursor c);

    protected T find(String sql, Object... sqlArgs) {
        T value = null;
        Cursor cursor = rawQuery(sql, sqlArgs);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                value = createFromCursor(cursor);
            }
            cursor.close();
        }
        return value;
    }


    protected List<T> get(String sql, Object... selectionArgs) {
        List<T> values = new ArrayList<T>();
        Cursor cursor = rawQuery(sql, selectionArgs);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    values.add(createFromCursor(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return values;
    }

    protected synchronized Cursor rawQuery(String sql, Object... selectionArgs) {
        String[] queryArgs = new String[selectionArgs.length];
        for (int i = 0; i < selectionArgs.length; i++) {
            queryArgs[i] = selectionArgs[i].toString();

        }
        logger.debug("SQLHelper/rawQuery: sql=" + sql + ", queryArgs=" + queryArgs.toString());
        Cursor cursor = null;
        if (RSSReaderDatabase.getDatabase() != null) {
            cursor = RSSReaderDatabase.getDatabase().rawQuery(sql, queryArgs);
        }
        return cursor;
    }

    protected synchronized void execSql(String sql, Object... bindArgs) {
        RSSReaderDatabase.getDatabase().execSQL(sql, bindArgs);
    }

    protected SQLiteDatabase getWritableDatabase() {
        return RSSReaderDatabase.getDatabase();
    }
}
