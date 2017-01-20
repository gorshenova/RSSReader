package com.egorshenova.rss;

import android.content.Context;

import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.utils.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public boolean writeFeeds(final List<RSSFeed> feeds) throws IOException {
        FileOutputStream outputStream = null;
        ObjectOutputStream objectStream = null;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            objectStream = new ObjectOutputStream(outputStream);
            objectStream.writeObject(feeds);
            outputStream.close();
            return true;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (objectStream != null) {
                objectStream.close();
            }
        }
    }

    public List<RSSFeed> readFeeds() throws IOException, ClassNotFoundException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(filename);
            ois = new ObjectInputStream(fis);
            List<RSSFeed> objs = (List<RSSFeed>) ois.readObject();
            ois.close();
            return objs;
        } finally {
            if (fis != null) {
                fis.close();
            }

            if (ois != null) {
                ois.close();
            }
        }
    }
}
