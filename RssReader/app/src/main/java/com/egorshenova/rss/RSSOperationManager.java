package com.egorshenova.rss;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.database.dao.FeedDataSource;
import com.egorshenova.rss.database.dao.FeedItemDataSource;
import com.egorshenova.rss.exceptions.DatabaseException;
import com.egorshenova.rss.exceptions.RSSVersionException;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.models.RSSItem;
import com.egorshenova.rss.utils.ComparatorByPubDate;
import com.egorshenova.rss.utils.Logger;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/**
 * RSSOperationManager downloads rss file, parses it, inserts or updates data in database.
 */
public class RSSOperationManager {

    private Logger logger = Logger.getLogger(RSSOperationManager.class);

    public static final int READ_TIMEOUT = 10000; // in milliseconds
    public static final int CONNECT_TIMEOUT = 15000; // in milliseconds

    private static final int STATE_DOWNLOAD_TASK_COMPLETE = 101;
    private static final int STATE_ERROR = 102;
    private static final int STATE_DATABASE_OPERATIONS_COMPLETE = 103;

    private String rssLink;
    private DownloadXmlCallback callback;
    private int feedId;

    public RSSOperationManager(String rssLink, int feedId, DownloadXmlCallback callback) {
        this.rssLink = rssLink;
        this.callback = callback;
        this.feedId = feedId;
    }

    public void startDownloadRSSData() {
        Thread t = new Thread(new DownloadXMLRunnable());
        t.start();
    }

    public void unregister() {
        downloadHandler.removeCallbacksAndMessages(null);
        callback = null;
    }

   private Handler downloadHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STATE_DOWNLOAD_TASK_COMPLETE:
                    Thread databaseThread = new Thread(new DatabaseOperationRunnable((RSSFeed) msg.obj));
                    databaseThread.start();
                    break;
                case STATE_ERROR:
                    onError((Exception) msg.obj);
                    break;
                case STATE_DATABASE_OPERATIONS_COMPLETE:
                    RSSFeed feed = (RSSFeed) msg.obj;
                    if (feedId != RSSFeed.DEFAULT_ID) {
                        //update feed in container
                        GlobalContainer.getInstance().updateFeed(feed);
                    } else {
                        //add new feed to container
                        GlobalContainer.getInstance().addFeed(feed);
                    }
                    if (callback != null) {
                        callback.onSuccess(feed);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };


    public class DownloadXMLRunnable implements Runnable {

        @Override
        public void run() {
            try {
                RSSFeed feed = loadXmlFromNetwork(rssLink);
                Message completeMessage = downloadHandler.obtainMessage(STATE_DOWNLOAD_TASK_COMPLETE, feed);
                downloadHandler.sendMessage(completeMessage);
            } catch (Exception e) {
                Message errorMessage = downloadHandler.obtainMessage(STATE_ERROR, e);
                downloadHandler.sendMessage(errorMessage);
            }
        }
    }

    public class DatabaseOperationRunnable implements Runnable {

        private RSSFeed feed;

        public DatabaseOperationRunnable(RSSFeed feed) {
            this.feed = feed;
        }

        @Override
        public void run() {
            try {
                RSSFeed storedFeed;
                if (feedId != RSSFeed.DEFAULT_ID) {
                    feed.setId(feedId);
                    storedFeed = updateFeedInDatabase(feed);
                } else {
                    storedFeed = insertFeedInDatabase(feed);
                }
                Message completeMessage = downloadHandler.obtainMessage(STATE_DATABASE_OPERATIONS_COMPLETE, storedFeed);
                downloadHandler.sendMessage(completeMessage);
            } catch (Exception e) {
                Message errorMessage = downloadHandler.obtainMessage(STATE_ERROR, e);
                downloadHandler.sendMessage(errorMessage);
            }
        }
    }

    /**
     * Uploads XML from network, parses it.  Returns feed object.
     *
     * @param rssLink
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     * @throws RSSVersionException    returns when
     */
    private RSSFeed loadXmlFromNetwork(String rssLink) throws XmlPullParserException, IOException, RSSVersionException, ParseException {
        InputStream stream = null;
        RSSFeed feed = null;
        RssParser rssParser = new RssParser();
        try {
            stream = downloadUrl(rssLink);
            feed = rssParser.parserFeed(stream, rssLink);
            feed.setRssLink(rssLink);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return feed;
    }

    /**
     * Given a string representation of a URL, sets up connection and gets an input stream.
     *
     * @param urlString
     * @return
     * @throws IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }


    private RSSFeed updateFeedInDatabase(RSSFeed feed) throws DatabaseException {
        FeedDataSource feedDataSource = new FeedDataSource();
        int count = feedDataSource.updateFeed(feed);

        //check if feed is updated successfully
        if (count == -1) {
            throw new DatabaseException(GlobalContainer.getInstance().getContext().getString(R.string.error_updating_feed_in_db));
        } else {
            //Update items:
            // 1. Delete items by feedId
            // 2. Add new items by feedId
            FeedItemDataSource itemDataSource = new FeedItemDataSource();
            itemDataSource.deleteItemsByFeedId(feed.getId());
            addItemsByFeedId(feed);
            return feed;
        }
    }

    private RSSFeed insertFeedInDatabase(RSSFeed feed) throws DatabaseException {
        FeedDataSource feedDataSource = new FeedDataSource();
        int feedId = feedDataSource.createFeed(feed);
        feed.setId(feedId);

        //insert feed items
        //check if feed is added successfully
        if (feedId == -1) {
            throw new DatabaseException(GlobalContainer.getInstance().getContext().getString(R.string.error_adding_feed_in_db));
        } else {
            addItemsByFeedId(feed);
            return feed;
        }
    }

    private void addItemsByFeedId(RSSFeed feed) {
        if (feed.getItems() != null && feed.getItems().size() > 0) {
            FeedItemDataSource feedItemDataSource = new FeedItemDataSource();

            //sort items by pubDate
            Collections.sort(feed.getItems(), Collections.reverseOrder(new ComparatorByPubDate()));

            //insert in db
            List<RSSItem> addedItems = feedItemDataSource.addItemsByFeedId(feed.getId(), feed.getItems());

            //set items in the feed which are added in database successfully
            feed.setItems(addedItems);
        }
    }

    private String getErrorMessage(Exception error) {
        Context context = GlobalContainer.getInstance().getContext();
        String errorMessage = context.getResources().getString(R.string.error_download_xml_file);
        if (error.getMessage() != null && !error.getMessage().isEmpty()) {
            errorMessage = error.getMessage();
        }
        return errorMessage;
    }

    private void onError(Exception error) {
        logger.error(error.getMessage(), error);
        if (callback != null) {
            callback.onError(getErrorMessage(error));
        }
    }

}
