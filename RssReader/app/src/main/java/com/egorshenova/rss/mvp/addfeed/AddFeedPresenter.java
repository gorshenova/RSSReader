package com.egorshenova.rss.mvp.addfeed;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.R;
import com.egorshenova.rss.RSSOperationManager;
import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.utils.NetworkHelper;
import com.egorshenova.rss.utils.StringUtils;

public class AddFeedPresenter extends BasePresenter<AddFeedContract.View> implements AddFeedContract.Presenter {

    private RSSOperationManager downloadManager;

    @Override
    public void detachView() {
        super.detachView();
        if (downloadManager != null) {
            downloadManager.setCallback(null);
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
            downloadManager = new RSSOperationManager(rssUrl, false, -1, downloadXmlCallback);
            downloadManager.start();
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
            getView().openRSSContent(feed);
            getView().hideLoading();
        }
    };

}
