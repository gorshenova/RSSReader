package com.egorshenova.rss;

import android.content.Context;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.utils.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class RSSFileManager {

    private Logger logger = Logger.getLogger(RSSFileManager.class);
    private String filename;
    private Context context;

    public RSSFileManager(Context context, String filename) {
        this.filename = filename;
        this.context = context;
    }

    public boolean writeFeeds(final List<RSSFeed> feeds) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(feeds);
            outputStream.close();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public List<RSSFeed> readFeeds() {
        FileInputStream fis;
        try {
            fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<RSSFeed> objs = (List<RSSFeed>) ois.readObject();
            ois.close();
            return objs;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
