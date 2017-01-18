package com.egorshenova.rss.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.R;
import com.egorshenova.rss.RssParser;
import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.exceptions.RSSVersionException;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.utils.Logger;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class DownloadXmlTask extends AsyncTask<String, Void, AsyncTaskResult<RSSFeed>> {

    public static final int READ_TIMEOUT = 10000; // in milliseconds
    public static final int CONNECT_TIMEOUT = 15000; // in milliseconds

    private Logger logger = Logger.getLogger(DownloadXmlTask.class);
    private DownloadXmlCallback callback;

    public DownloadXmlTask(DownloadXmlCallback callback) {
        this.callback = callback;
    }

    @Override
    protected AsyncTaskResult<RSSFeed> doInBackground(String... urls) {
        try {
            return new AsyncTaskResult<RSSFeed>(loadXmlFromNetwork(urls[0]));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new AsyncTaskResult<RSSFeed>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<RSSFeed> result) {
        logger.debug("Download AsyncTaskResult: " + result);
        if(callback == null) return;

        if (result.getError() != null) {
            Context context = GlobalContainer.getInstance().getContext();
            String errorMessage = context.getResources().getString(R.string.error_download_xml_file);
            callback.onError(errorMessage);
        } else {
            RSSFeed feed = result.getResult();

            //add new feed to container
            GlobalContainer.getInstance().addFeed(feed);

            callback.onSuccess(feed);
        }
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

    /**
     * Uploads XML from network, parses it.  Returns feed object.
     *
     * @param rssLink
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     * @throws RSSVersionException
     */
    private RSSFeed loadXmlFromNetwork(String rssLink) throws XmlPullParserException, IOException, RSSVersionException, ParseException {
        InputStream stream = null;
        RSSFeed feed = null;
        RssParser rssParser = new RssParser();
        try {
            stream = downloadUrl(rssLink);
            feed = rssParser.parserFeed(stream);
            feed.setRssLink(rssLink);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return feed;
    }

    public void setCallback(DownloadXmlCallback callback) {
        this.callback = callback;
    }
}

