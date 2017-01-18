package com.egorshenova.rss.mvp.addfeed;

import com.egorshenova.rss.GlobalContainer;
import com.egorshenova.rss.R;
import com.egorshenova.rss.callbacks.DownloadXmlCallback;
import com.egorshenova.rss.models.RSSFeed;
import com.egorshenova.rss.mvp.abs.BasePresenter;
import com.egorshenova.rss.tasks.DownloadXmlTask;
import com.egorshenova.rss.utils.NetworkHelper;
import com.egorshenova.rss.utils.StringUtils;

public class AddFeedPresenter extends BasePresenter<AddFeedContract.View> implements AddFeedContract.Presenter {

    private DownloadXmlTask downloadXmlTask;

    @Override
    public void fetchFeed(String urlStr) {
        checkViewAttached();

        if (!StringUtils.isURLValid(urlStr)) {
            //check if url is valid
            getView().showError(getContext().getResources().getString(R.string.error_url_no_valid));

        } else if (GlobalContainer.getInstance().checkRSSLinkExists(urlStr)) {
            //check for duplication rss links
            getView().showError(getContext().getResources().getString(R.string.error_rss_link_exists));

        } else if (!NetworkHelper.isInternetAvailable(getContext())) {
            //check if network available
            getView().showError(getContext().getResources().getString(R.string.error_internet_required));

        } else {
            getView().showLoading();
            downloadXmlTask = new DownloadXmlTask(downloadXmlCallback);
            downloadXmlTask.execute(urlStr);
        }

    }

    public void unregister() {
        if (downloadXmlTask != null) {
            downloadXmlTask.setCallback(null);
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
            getView().openRSSContent(feed);
            getView().hideLoading();
        }
    };
}
