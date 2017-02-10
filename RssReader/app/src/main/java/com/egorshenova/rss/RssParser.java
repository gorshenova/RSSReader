package com.egorshenova.rss;

import android.util.Xml;

import com.egorshenova.rss.exceptions.RSSVersionException;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.models.RSSItem;
import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.StringUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RssParser {

    private Logger logger = Logger.getLogger(RssParser.class);

    public static final int RSS_VERSION = 2;

    //don't use namespaces
    private static final String ns = null;

    private static final String TAG_CHANNEL = "channel";
    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_DESRIPTION = "description";
    private static final String TAG_ITEM = "item";
    private static final String TAG_PUB_DATE = "pubDate";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_URL = "url";
    private static final String TAG_LANGUAGE = "language";

    public RSSFeed parserFeed(InputStream rssStream, String rssLink) throws XmlPullParserException, IOException, RSSVersionException, ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(rssStream, null);
            parser.nextTag();
            RSSFeed feed = readFeed(parser);
            feed.setRssLink(rssLink);
            return feed;
        } finally {
            rssStream.close();
        }
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public RSSFeed readFeed(XmlPullParser parser) throws IOException, XmlPullParserException, RSSVersionException, ParseException {
        RSSFeed feed = new RSSFeed();
        String title = null;
        String imageUrl = null;
        String language =  null;
        List<RSSItem> items = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        double rssCurrentVersion = Double.valueOf(parser.getAttributeValue(null, "version"));

        if (rssCurrentVersion != RSS_VERSION) {
            throw new RSSVersionException();
        }

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equalsIgnoreCase(TAG_CHANNEL)) {
                parser.require(XmlPullParser.START_TAG, ns, TAG_CHANNEL);

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String nn = parser.getName();
                    if (nn.equalsIgnoreCase(TAG_TITLE)) {
                        title = readTitle(parser);
                    } else if (nn.equalsIgnoreCase(TAG_LANGUAGE)){
                        language = readLanguage(parser);
                    }else if (nn.equalsIgnoreCase(TAG_IMAGE)) {
                        imageUrl = readImage(parser);
                    } else if (nn.equalsIgnoreCase(TAG_ITEM)) {
                        items.add(readItem(parser, language));
                    } else {
                        skip(parser);
                    }
                }
            }

        }
        feed.setImageUrl(imageUrl);
        feed.setTitle(title);
        feed.setItems(items);
        logger.debug("Rss Feed: " + feed);
        return feed;
    }

    private String readLanguage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_LANGUAGE);
        String language = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_LANGUAGE);
        return language;
    }

    public RSSItem readItem(XmlPullParser parser, String channelLanguage) throws IOException, XmlPullParserException, ParseException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_ITEM);
        String title = null;
        String description = null;
        String link = null;
        String pubDateStr = null;
        String imageUrl = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equalsIgnoreCase(TAG_TITLE)) {
                title = readTitle(parser);
            } else if (name.equalsIgnoreCase(TAG_DESRIPTION)) {
                description = readDescription(parser);
            } else if (name.equalsIgnoreCase(TAG_LINK)) {
                link = readLink(parser);
            } else if (name.equalsIgnoreCase(TAG_PUB_DATE)) {
                pubDateStr = readPubDate(parser);
            } else if (name.equalsIgnoreCase(TAG_IMAGE)) {
                imageUrl = readImage(parser);
            } else {
                skip(parser);
            }
        }
        long pubDate = -1;
        if (pubDateStr != null) {
            pubDate = StringUtils.converDateToLong(pubDateStr,channelLanguage);
        }
        RSSItem item = new RSSItem(title, link, description, pubDate, imageUrl);
        logger.debug("RSSItem: " + item);
        return item;
    }

    private String readImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_IMAGE);
        String url = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nn = parser.getName();
            if (nn.equalsIgnoreCase(TAG_URL)) {
                url = readUrl(parser);
            } else {
                skip(parser);
            }
        }
        return url;
    }

    private String readUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_URL);
        String url = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_URL);
        return url;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_PUB_DATE);
        String date = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_PUB_DATE);
        return date;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_LINK);
        return link;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_DESRIPTION);
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_DESRIPTION);
        return description;
    }


    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_TITLE);
        return title;
    }

    /**
     * Reads text value inside the tag
     */
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
