package com.egorshenova.rss.mvp.addfeed;

import android.widget.TextView;

import com.egorshenova.rss.mvp.abs.IBasePresenter;
import com.egorshenova.rss.mvp.abs.IBaseView;

public interface AddFeedContract {

    interface View extends IBaseView{
        void showError(String message);
        void showError(int stringId);
        void showLoading();
        void hideLoading();
        void showSampleRSSLinks(String rssLink);
    }

    interface Presenter extends IBasePresenter<AddFeedContract.View>{
        void fetchFeed(String urlStr);
        void loadSampleRSSLinks();
    }
}
