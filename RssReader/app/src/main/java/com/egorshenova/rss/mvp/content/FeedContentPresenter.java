package com.egorshenova.rss.mvp.content;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.R;
import com.egorshenova.rss.RSSOperationManager;
import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.database.dao.FeedItemDataSource;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.models.RSSItem;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.utils.Logger;
import com.egorshenova.rss.utils.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

public class FeedContentPresenter extends BasePresenter<FeedContentContract.View> implements FeedContentContract.Presenter {

    private static Logger logger = Logger.getLogger(FeedContentPresenter.class);

    private List<RSSFeed> feeds = new ArrayList<>();
    private RSSOperationManager rssOperationManager;

    public FeedContentPresenter() {
        feeds.addAll(GlobalContainer.getInstance().getFeeds());
    }

    @Override
    public void detachView() {
        super.detachView();
        if (rssOperationManager != null) {
            rssOperationManager.unregister();
        }
    }

    @Override
    public void initializeContent(RSSFeed selectedFeed) {
        checkViewAttached();

        for (RSSFeed feed : feeds) {
            if (feed.getItems() == null || feed.getItems().isEmpty()) {
                //get items by feed from database
                //update feed in global container
                FeedItemDataSource ds = new FeedItemDataSource();
                List<RSSItem> items = ds.getAllItemsByFeedId(feed.getId());
                feed.setItems(items);
                GlobalContainer.getInstance().updateFeed(feed);
            }
        }


        int selectedTabPos = selectedFeed == null ? 0 : selectedFeed.getId() - 1;
        getView().addTabsAndShowContent(feeds, selectedTabPos);

    }


    @Override
    public void sortByNewest() {
      /*  checkViewAttached();

        Collections.sort(feed.getItems(), Collections.reverseOrder(new ComparatorByPubDate()));
        getView().showFeedContent(feed);*/
    }

    @Override
    public void sortByOldest() {
/*        checkViewAttached();

        Collections.sort(feed.getItems(), new ComparatorByPubDate());
        getView().showFeedContent(feed);*/
    }

    @Override
    public void updateFeed(RSSFeed selectedFeed) {
        checkViewAttached();

        //show loading
        getView().showLoading();

        //download feed again and show its content
        downloadFeed(selectedFeed.getId(), selectedFeed.getRssLink());
    }

    public void downloadFeed(int feedId, String rssLink) {
        if (!NetworkHelper.isInternetAvailable(getContext())) {
            getView().showError(getContext().getResources().getString(R.string.error_internet_required));

        } else {

            getView().showLoading();
            rssOperationManager = new RSSOperationManager(rssLink, feedId, /*feedUpdated,*/ new DownloadXmlCallback() {
                @Override
                public void onError(String message) {
                    getView().showError(message);
                    getView().hideLoading();
                }

                @Override
                public void onSuccess(RSSFeed feed) {
                    logger.debug("downloadFeed: " + feed);
                    getView().updateFeedContent(feed);
                    getView().hideLoading();
                }
            });
            rssOperationManager.startDownloadRSSData();
        }
    }
}
