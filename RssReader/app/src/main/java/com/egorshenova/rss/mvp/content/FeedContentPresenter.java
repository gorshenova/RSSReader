package com.egorshenova.rss.mvp.content;

import com.egorshenova.rss.R;
import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.tasks.DownloadXmlTask;
import com.egorshenova.rss.utils.ComparatorByPubDate;
import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.NetworkHelper;

import java.util.Collections;

public class FeedContentPresenter extends BasePresenter<FeedContentContract.View> implements FeedContentContract.Presenter {

    private static Logger logger = Logger.getLogger(FeedContentPresenter.class);

    private RSSFeed feed;

    @Override
    public void openFeedContent(RSSFeed feed) {
        checkViewAttached();

        if (feed.getItems() != null && feed.getItems().size() > 0) {
            this.feed = feed;
            getView().showFeedContent(feed);
        } else {
            downloadFeed(feed.getRssLink());
        }
    }

    @Override
    public void sortByNewest() {
        checkViewAttached();

        Collections.sort(feed.getItems(), Collections.reverseOrder(new ComparatorByPubDate()));
        getView().showFeedContent(feed);
    }

    @Override
    public void sortByOldest() {
        checkViewAttached();

        Collections.sort(feed.getItems(), new ComparatorByPubDate());
        getView().showFeedContent(feed);
    }

    public void downloadFeed(String rssLink) {
        if (!NetworkHelper.isInternetAvailable(getContext())) {
            getView().showError(getContext().getResources().getString(R.string.error_internet_required));

        } else {

            getView().showLoading();
            new DownloadXmlTask(new DownloadXmlCallback() {
                @Override
                public void onError(String message) {
                    getView().showError(message);
                }

                @Override
                public void onSuccess(RSSFeed feed) {
                    logger.debug("downloadFeed: " + feed);
                    prepareRSSContent(feed);
                }
            }).execute(rssLink);
        }
    }

    private void prepareRSSContent(RSSFeed feed) {
        this.feed = feed;
        getView().showFeedContent(feed);
        getView().hideLoading();
    }
}
