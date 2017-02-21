package com.egorshenova.rss.mvp.addfeed;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.R;
import com.egorshenova.rss.RSSOperationManager;
import com.egorshenova.rss.RSSReaderApplication;
import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.models.FeedChangeObject;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.utils.NetworkHelper;
import com.egorshenova.rss.utils.StringUtils;

public class AddFeedPresenter extends BasePresenter<AddFeedContract.View> implements AddFeedContract.Presenter {

    private RSSOperationManager rssOperationManager;

    @Override
    public void detachView() {
        super.detachView();
        if (rssOperationManager != null) {
            rssOperationManager.unregister();
        }
    }

    @Override
    public void fetchFeed(String rssUrl) {
        checkViewAttached();

        if (!StringUtils.isURLValid(rssUrl)) {
            //check if url is valid
            getView().showError(getContext().getResources().getString(R.string.error_url_no_valid));

        } else if (GlobalContainer.getInstance().checkRSSLinkExists(rssUrl)) {
            //check the duplication of rss links
            getView().showError(getContext().getResources().getString(R.string.error_rss_link_exists));

        } else if (!NetworkHelper.isInternetAvailable(getContext())) {
            //check if network available
            getView().showError(getContext().getResources().getString(R.string.error_internet_required));

        } else {
            getView().showLoading();
            rssOperationManager = new RSSOperationManager(rssUrl, -1, false, downloadXmlCallback);
            rssOperationManager.startDownloadRSSData();
        }

    }

    private DownloadXmlCallback downloadXmlCallback = new DownloadXmlCallback() {
        @Override
        public void onError(String message) {
            getView().showError(message);
            getView().hideLoading();
        }

        @Override
        public void onSuccess(RSSFeed feed) {
            //display new feed
            RSSReaderApplication.get().getFeedObservable().setObj(new FeedChangeObject(feed, false));
            getView().hideLoading();
        }
    };

}
