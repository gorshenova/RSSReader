package com.egorshenova.rss.mvp.content;

import com.egorshenova.rss.R;
import com.egorshenova.rss.RSSOperationManager;
import com.egorshenova.rss.RSSReaderApplication;
import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.database.dao.FeedItemDataSource;
import com.egorshenova.rss.models.FeedChangeObject;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.models.RSSItem;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.utils.ComparatorByPubDate;
import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.NetworkHelper;

import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class FeedContentPresenter extends BasePresenter<FeedContentContract.View> implements FeedContentContract.Presenter{

    private static Logger logger = Logger.getLogger(FeedContentPresenter.class);

    private RSSFeed feed;
    private RSSOperationManager RSSOperationManager;

    public FeedContentPresenter(RSSFeed feed) {
        this.feed = feed;
    }

    @Override
    public void detachView() {
        super.detachView();
        if(RSSOperationManager != null){
            RSSOperationManager.setCallback(null);
        }
    }

    @Override
    public void openFeedContent() {
        checkViewAttached();

        if (feed.getItems() != null && feed.getItems().size() > 0) {
            getView().showFeedContent(feed);
        } else {
            //get items from db
            FeedItemDataSource ds =  new FeedItemDataSource();
            List<RSSItem> items = ds.getAllItemsByFeedId(feed.getId());
            feed.setItems(items);
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

    public void downloadFeed(final boolean feedUpdated, int feedId, String rssLink) {
        if (!NetworkHelper.isInternetAvailable(getContext())) {
            getView().showError(getContext().getResources().getString(R.string.error_internet_required));

        } else {

            getView().showLoading();
            RSSOperationManager = new RSSOperationManager(rssLink,feedId, feedUpdated, new DownloadXmlCallback() {
                @Override
                public void onError(String message) {
                    getView().showError(message);
                    getView().hideLoading();
                }

                @Override
                public void onSuccess(RSSFeed feed) {
                    logger.debug("downloadFeed: " + feed);
                    RSSReaderApplication.get().getFeedObservable().setObj(new FeedChangeObject(feed, feedUpdated));
                    getView().showFeedContent(feed);
                    getView().hideLoading();
                }
            });
            RSSOperationManager.start();
        }
    }
}
