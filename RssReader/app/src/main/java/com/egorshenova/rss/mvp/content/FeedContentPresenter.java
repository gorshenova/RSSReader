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
    private DownloadXmlTask downloadXmlTask;

    public FeedContentPresenter(RSSFeed feed) {
        this.feed = feed;
    }

    @Override
    public void detachView() {
        super.detachView();
        if(downloadXmlTask != null){
            downloadXmlTask.setCallback(null);
        }
    }

    @Override
    public void openFeedContent() {
        checkViewAttached();

        if (feed.getItems() != null && feed.getItems().size() > 0) {
            getView().showFeedContent(feed);
        } else {
            downloadFeed(false, -1, feed.getRssLink());
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

    @Override
    public void updateFeed() {
        checkViewAttached();

        //show loading
        getView().showLoading();

        //download feed again and show its content
        downloadFeed(true, feed.getId(), feed.getRssLink());
    }

    public void downloadFeed(boolean feedUpdate, int feedId, String rssLink) {
        if (!NetworkHelper.isInternetAvailable(getContext())) {
            getView().showError(getContext().getResources().getString(R.string.error_internet_required));

        } else {

            getView().showLoading();
            downloadXmlTask = new DownloadXmlTask(new DownloadXmlCallback() {
                @Override
                public void onError(String message) {
                    getView().showError(message);
                    getView().hideLoading();
                }

                @Override
                public void onSuccess(RSSFeed feed) {
                    logger.debug("downloadFeed: " + feed);
                    prepareRSSContent(feed);
                    getView().hideLoading();
                }
            }, feedUpdate, feedId);
            downloadXmlTask.execute(rssLink);
        }
    }

    private void prepareRSSContent(RSSFeed feed) {
        this.feed = feed;
        getView().showFeedContent(feed);
    }
}
